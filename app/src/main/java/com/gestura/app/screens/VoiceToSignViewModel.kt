package com.gestura.app.screens

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gestura.app.data.api.GesturaApi
import com.gestura.app.data.model.SignItem
import com.gestura.app.data.model.TranslateRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VoiceToSignUiState(
    val isListening: Boolean = false,
    val transcription: String = "",
    val glosses: List<String> = emptyList(),
    val signs: List<SignItem> = emptyList(),
    val isTranslating: Boolean = false,
    val isPlaying: Boolean = false,
    val currentSignIndex: Int = 0,
    val currentFrameIndex: Int = 0,
    /** The current frame's 180 landmarks – each landmark is [x, y, z]. */
    val currentFrame: List<List<Float>> = emptyList(),
    /** Non-null when a fingerspell letter is being displayed. */
    val fingerspellLetter: String? = null,
    val error: String? = null,
)

class VoiceToSignViewModel(application: Application) : AndroidViewModel(application) {

    private val api = GesturaApi.create()

    private val _uiState = MutableStateFlow(VoiceToSignUiState())
    val uiState: StateFlow<VoiceToSignUiState> = _uiState.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private var animationJob: Job? = null

    // ── Speech Recognition ──────────────────────────────────────────

    fun startListening() {
        val context = getApplication<Application>()
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _uiState.value = _uiState.value.copy(error = "Speech recognition not available")
            return
        }

        // Cancel any running animation
        animationJob?.cancel()

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(GesturaSpeechListener())
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        _uiState.value = _uiState.value.copy(
            isListening = true,
            error = null,
            transcription = "",
            glosses = emptyList(),
            signs = emptyList(),
            isPlaying = false,
            currentFrame = emptyList(),
            fingerspellLetter = null,
        )
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _uiState.value = _uiState.value.copy(isListening = false)
    }

    // ── Backend API call ────────────────────────────────────────────

    private fun translateText(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTranslating = true, error = null)
            try {
                val response = api.translate(TranslateRequest(text = text))
                if (response.success && response.signs.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isTranslating = false,
                        glosses = response.glosses,
                        signs = response.signs,
                    )
                    // Start the frame-by-frame animation loop
                    startAnimationLoop(response.signs)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isTranslating = false,
                        glosses = response.glosses,
                        signs = emptyList(),
                        error = if (response.signs.isEmpty()) "No sign data found" else null,
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Translation API error", e)
                _uiState.value = _uiState.value.copy(
                    isTranslating = false,
                    error = "Connection failed: ${e.localizedMessage}",
                )
            }
        }
    }

    // ── Skeleton / Fingerspell Animation Loop ───────────────────────

    private fun startAnimationLoop(signs: List<SignItem>) {
        animationJob?.cancel()
        animationJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isPlaying = true, currentSignIndex = 0)

            for ((signIdx, sign) in signs.withIndex()) {
                _uiState.value = _uiState.value.copy(
                    currentSignIndex = signIdx,
                    currentFrameIndex = 0,
                    fingerspellLetter = null,
                    currentFrame = emptyList(),
                )

                when (sign.type) {
                    "skeleton" -> {
                        if (sign.found && sign.frames.isNotEmpty()) {
                            for ((frameIdx, frame) in sign.frames.withIndex()) {
                                _uiState.value = _uiState.value.copy(
                                    currentFrameIndex = frameIdx,
                                    currentFrame = frame,
                                    fingerspellLetter = null,
                                )
                                delay(SKELETON_FRAME_DELAY_MS)
                            }
                        } else {
                            // Sign not found – show gloss text briefly
                            _uiState.value = _uiState.value.copy(
                                fingerspellLetter = sign.gloss,
                            )
                            delay(FINGERSPELL_LETTER_DELAY_MS * sign.gloss.length)
                        }
                    }

                    "fingerspell" -> {
                        // Show each letter for 500ms
                        for (ch in sign.gloss) {
                            _uiState.value = _uiState.value.copy(
                                fingerspellLetter = ch.toString(),
                                currentFrame = emptyList(),
                            )
                            delay(FINGERSPELL_LETTER_DELAY_MS)
                        }
                    }

                    else -> {
                        // Unknown type – just show gloss briefly
                        _uiState.value = _uiState.value.copy(
                            fingerspellLetter = sign.gloss,
                        )
                        delay(FINGERSPELL_LETTER_DELAY_MS * sign.gloss.length)
                    }
                }
            }

            // Animation finished
            _uiState.value = _uiState.value.copy(
                isPlaying = false,
                fingerspellLetter = null,
            )
        }
    }

    // ── Cleanup ─────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        animationJob?.cancel()
        speechRecognizer?.destroy()
    }

    // ── SpeechRecognizer Listener ───────────────────────────────────

    private inner class GesturaSpeechListener : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            _uiState.value = _uiState.value.copy(isListening = false)
        }

        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                else -> "Recognition error ($error)"
            }
            _uiState.value = _uiState.value.copy(
                isListening = false,
                error = message,
            )
        }

        override fun onResults(results: Bundle?) {
            val text = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()

            _uiState.value = _uiState.value.copy(
                isListening = false,
                transcription = text,
            )

            // Automatically call the backend API with the transcribed text
            if (text.isNotBlank()) {
                translateText(text)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val partial = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                .orEmpty()

            if (partial.isNotBlank()) {
                _uiState.value = _uiState.value.copy(transcription = partial)
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    companion object {
        private const val TAG = "VoiceToSignVM"
        private const val SKELETON_FRAME_DELAY_MS = 80L
        private const val FINGERSPELL_LETTER_DELAY_MS = 500L
    }
}
