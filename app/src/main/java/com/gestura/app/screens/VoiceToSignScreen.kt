package com.gestura.app.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gestura.app.ui.theme.*

@Composable
fun VoiceToSignScreen(
    viewModel: VoiceToSignViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    // Audio permission handling
    val context = LocalContext.current
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (granted) viewModel.startListening()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ── Title ──
        Text(
            text = "Voice to Sign",
            style = MaterialTheme.typography.headlineLarge,
            color = TextMain,
            fontWeight = FontWeight.Bold,
        )

        // ── Segmented Toggle ──
        SegmentedToggle(
            selectedIndex = selectedTab,
            onSelected = { selectedTab = it },
        )

        // ── Skeleton Canvas / Avatar Area ──
        SignAnimationArea(
            uiState = uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        // ── Error message ──
        AnimatedVisibility(visible = uiState.error != null) {
            uiState.error?.let { errorMsg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Error.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            Icons.Filled.ErrorOutline,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            text = errorMsg,
                            style = MaterialTheme.typography.bodySmall,
                            color = Error,
                        )
                    }
                }
            }
        }

        // ── Gloss chips (show which signs are being played) ──
        AnimatedVisibility(visible = uiState.glosses.isNotEmpty()) {
            GlossChipsRow(
                glosses = uiState.glosses,
                currentIndex = if (uiState.isPlaying) uiState.currentSignIndex else -1,
            )
        }

        // ── Transcription Card ──
        TranscriptionCard(
            transcription = uiState.transcription,
            isListening = uiState.isListening,
            isTranslating = uiState.isTranslating,
            onMicClick = {
                if (uiState.isListening) {
                    viewModel.stopListening()
                } else if (hasAudioPermission) {
                    viewModel.startListening()
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            },
        )
    }
}

// ──────────────────────────────────────────────
// Segmented Toggle: Microphone vs Camera
// ──────────────────────────────────────────────
@Composable
private fun SegmentedToggle(
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
) {
    val items = listOf(
        Pair(Icons.Filled.Mic, "Microphone"),
        Pair(Icons.Filled.Videocam, "Camera"),
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceLight,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
        ) {
            items.forEachIndexed { index, (icon, label) ->
                val isSelected = index == selectedIndex
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = { onSelected(index) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Primary else Color.Transparent,
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) Color.White else TextMuted,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else TextMuted,
                        )
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────
// Sign Animation Area (Canvas stick figure)
// ──────────────────────────────────────────────
@Composable
private fun SignAnimationArea(
    uiState: VoiceToSignUiState,
    modifier: Modifier = Modifier,
) {
    val currentGloss = if (uiState.isPlaying && uiState.signs.isNotEmpty()) {
        uiState.signs.getOrNull(uiState.currentSignIndex)?.gloss ?: ""
    } else ""

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(AvatarBackground)
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(24.dp),
            ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            // Fingerspell mode: show the letter large and centered
            uiState.fingerspellLetter != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (currentGloss.isNotEmpty()) {
                        Text(
                            text = currentGloss,
                            style = MaterialTheme.typography.titleMedium,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    Text(
                        text = uiState.fingerspellLetter,
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 96.sp),
                        color = TextMain,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "FINGERSPELL",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted,
                    )
                }
            }

            // Skeleton mode: draw stick figure on canvas
            uiState.currentFrame.isNotEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Current gloss label above canvas
                    if (currentGloss.isNotEmpty()) {
                        Text(
                            text = currentGloss,
                            style = MaterialTheme.typography.titleMedium,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                        )
                    }
                    // Stick figure canvas
                    SkeletonCanvas(
                        frame = uiState.currentFrame,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                    )
                }
            }

            // Placeholder: nothing is playing
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Accessibility,
                        contentDescription = "Sign Avatar",
                        tint = Primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(80.dp),
                    )
                    Text(
                        text = "Sign Language Avatar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted,
                    )
                    Text(
                        text = if (uiState.isTranslating) "Translating…"
                        else "Tap the mic and speak",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted.copy(alpha = 0.6f),
                    )
                    if (uiState.isTranslating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Primary,
                            strokeWidth = 2.dp,
                        )
                    }
                }
            }
        }
    }
}

// ──────────────────────────────────────────────
// Canvas: draw the MediaPipe stick figure
// ──────────────────────────────────────────────

/**
 * MediaPipe Holistic landmark layout (180 total):
 *   0–32   : Pose (33 landmarks)
 *   33–53  : Left hand (21 landmarks)
 *   54–74  : Right hand (21 landmarks)
 *   75–542 : Face (468 landmarks — we only have up to 180 total, so 75–179 = partial face)
 *
 * We draw:
 *   • Body connections (shoulders, elbows, wrists, hips, knees, ankles)
 *   • A head circle around pose landmark 0 (nose)
 *   • Left-hand dots (indices 33–53)
 *   • Right-hand dots (indices 54–74)
 *   • Hand internal connections
 */
@Composable
private fun SkeletonCanvas(
    frame: List<List<Float>>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Helper to convert a landmark index to pixel Offset
        fun lm(index: Int): Offset? {
            if (index >= frame.size) return null
            val point = frame[index]
            if (point.size < 2) return null
            return Offset(point[0] * w, point[1] * h)
        }

        // ── Body pose connections ──
        // Pose landmark indices (within the 0-32 range):
        //  11=left shoulder, 12=right shoulder
        //  13=left elbow,    14=right elbow
        //  15=left wrist,    16=right wrist
        //  23=left hip,      24=right hip
        //  25=left knee,     26=right knee
        //  27=left ankle,    28=right ankle

        val bodyColor = Primary
        val bodyStroke = 4f

        val bodyConnections = listOf(
            11 to 12, // shoulders
            11 to 13, 13 to 15, // left arm
            12 to 14, 14 to 16, // right arm
            11 to 23, 12 to 24, // torso sides
            23 to 24, // hips
            23 to 25, 25 to 27, // left leg
            24 to 26, 26 to 28, // right leg
        )

        for ((a, b) in bodyConnections) {
            val pA = lm(a) ?: continue
            val pB = lm(b) ?: continue
            drawLine(bodyColor, pA, pB, strokeWidth = bodyStroke, cap = StrokeCap.Round)
        }

        // Body joint dots
        val bodyJoints = listOf(11, 12, 13, 14, 15, 16, 23, 24, 25, 26, 27, 28)
        for (idx in bodyJoints) {
            val p = lm(idx) ?: continue
            drawCircle(bodyColor, radius = 6f, center = p)
        }

        // ── Head circle (around nose = landmark 0) ──
        val nose = lm(0)
        val leftShoulder = lm(11)
        val rightShoulder = lm(12)
        if (nose != null && leftShoulder != null && rightShoulder != null) {
            val shoulderDist = (rightShoulder - leftShoulder).getDistance()
            val headRadius = shoulderDist * 0.35f
            drawCircle(
                color = bodyColor,
                radius = headRadius,
                center = nose,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = bodyStroke),
            )
        }

        // ── Hands ──
        val handDotRadius = 3.5f
        val handLineStroke = 2.5f

        // MediaPipe hand connections (relative indices within a 21-point hand)
        val handConnections = listOf(
            0 to 1, 1 to 2, 2 to 3, 3 to 4,       // thumb
            0 to 5, 5 to 6, 6 to 7, 7 to 8,       // index
            0 to 9, 9 to 10, 10 to 11, 11 to 12,   // middle  (note: these are hand-relative, not pose)
            0 to 13, 13 to 14, 14 to 15, 15 to 16, // ring
            0 to 17, 17 to 18, 18 to 19, 19 to 20, // pinky
            5 to 9, 9 to 13, 13 to 17,             // palm
        )

        // Left hand: landmarks 33–53 (offset = 33)
        drawHand(frame, 33, handConnections, PrimaryLight, handDotRadius, handLineStroke, w, h)

        // Right hand: landmarks 54–74 (offset = 54)
        drawHand(frame, 54, handConnections, Accent, handDotRadius, handLineStroke, w, h)

        // ── Face: draw as a small cluster of dots (landmarks 75+) ──
        val faceColor = TextMuted.copy(alpha = 0.3f)
        val faceEnd = minOf(frame.size, 180) // up to landmark 179
        for (i in 75 until faceEnd) {
            val p = lm(i) ?: continue
            drawCircle(faceColor, radius = 1.5f, center = p)
        }
    }
}

private fun DrawScope.drawHand(
    frame: List<List<Float>>,
    offset: Int,
    connections: List<Pair<Int, Int>>,
    color: Color,
    dotRadius: Float,
    lineStroke: Float,
    canvasW: Float,
    canvasH: Float,
) {
    fun handLm(relIdx: Int): Offset? {
        val absIdx = offset + relIdx
        if (absIdx >= frame.size) return null
        val pt = frame[absIdx]
        if (pt.size < 2) return null
        return Offset(pt[0] * canvasW, pt[1] * canvasH)
    }

    // Draw connections
    for ((a, b) in connections) {
        val pA = handLm(a) ?: continue
        val pB = handLm(b) ?: continue
        drawLine(color, pA, pB, strokeWidth = lineStroke, cap = StrokeCap.Round)
    }

    // Draw dots
    for (i in 0 until 21) {
        val p = handLm(i) ?: continue
        drawCircle(color, radius = dotRadius, center = p)
    }
}

// ──────────────────────────────────────────────
// Gloss Chips Row (highlights current sign)
// ──────────────────────────────────────────────
@Composable
private fun GlossChipsRow(
    glosses: List<String>,
    currentIndex: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        glosses.forEachIndexed { index, gloss ->
            val isCurrent = index == currentIndex
            val isPast = index < currentIndex
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when {
                    isCurrent -> Primary
                    isPast -> Success.copy(alpha = 0.2f)
                    else -> SurfaceLight
                },
                tonalElevation = if (isCurrent) 4.dp else 0.dp,
            ) {
                Text(
                    text = gloss,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isCurrent -> Color.White
                        isPast -> Success
                        else -> TextMuted
                    },
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Transcription Card with pulsing mic FAB
// ──────────────────────────────────────────────
@Composable
private fun TranscriptionCard(
    transcription: String,
    isListening: Boolean,
    isTranslating: Boolean,
    onMicClick: () -> Unit,
) {
    Box {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = SurfaceLight),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .padding(end = 48.dp), // space for FAB
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Filled.SubtitlesOff,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = when {
                            isListening -> "Listening…"
                            isTranslating -> "Translating…"
                            else -> "Transcription"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMain,
                    )
                }

                Text(
                    text = if (transcription.isNotBlank()) "\"$transcription\""
                    else "\"Tap the microphone and start speaking…\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (transcription.isNotBlank()) TextMain else TextMuted,
                    fontStyle = FontStyle.Italic,
                )
            }
        }

        // Pulsing Microphone FAB
        PulsingMicFab(
            isListening = isListening,
            onClick = onMicClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-12).dp),
        )
    }
}

@Composable
private fun PulsingMicFab(
    isListening: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isListening) 500 else 800,
                easing = EaseInOut,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "mic_scale",
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = if (isListening) 0.5f else 0.3f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (isListening) 500 else 800,
                easing = EaseInOut,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "mic_glow",
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // Outer glow ring
        Box(
            modifier = Modifier
                .size(64.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    if (isListening) Error.copy(alpha = alpha)
                    else Primary.copy(alpha = alpha)
                ),
        )
        // FAB
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            containerColor = if (isListening) Error else Primary,
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = if (isListening) Icons.Filled.Stop else Icons.Filled.Mic,
                contentDescription = if (isListening) "Listening..." else "Start recording",
                modifier = Modifier.size(26.dp),
            )
        }
    }
}
