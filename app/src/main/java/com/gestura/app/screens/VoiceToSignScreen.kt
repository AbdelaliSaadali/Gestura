package com.gestura.app.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gestura.app.ui.theme.*

@Composable
fun VoiceToSignScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

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

        // ── 3D Avatar Placeholder ──
        AvatarPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        // ── Transcription Card ──
        TranscriptionCard()
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
// 3D Avatar Placeholder
// ──────────────────────────────────────────────
@Composable
private fun AvatarPlaceholder(modifier: Modifier = Modifier) {
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Accessibility,
                contentDescription = "3D Avatar",
                tint = Primary.copy(alpha = 0.4f),
                modifier = Modifier.size(80.dp),
            )
            Text(
                text = "3D Sign Language Avatar",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
            )
            Text(
                text = "Avatar will animate here",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted.copy(alpha = 0.6f),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Transcription Card with pulsing mic FAB
// ──────────────────────────────────────────────
@Composable
private fun TranscriptionCard() {
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
                        text = "Transcription",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextMain,
                    )
                }

                Text(
                    text = "\"Hello, how are you doing today?\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMain,
                    fontStyle = FontStyle.Italic,
                )
            }
        }

        // Pulsing Microphone FAB
        PulsingMicFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-12).dp),
        )
    }
}

@Composable
private fun PulsingMicFab(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "mic_scale",
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
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
                .background(Primary.copy(alpha = alpha)),
        )
        // FAB
        FloatingActionButton(
            onClick = { },
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            containerColor = Primary,
            contentColor = Color.White,
        ) {
            Icon(
                Icons.Filled.Mic,
                contentDescription = "Start recording",
                modifier = Modifier.size(26.dp),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VoiceToSignScreenPreview() {
    GesturaTheme(darkTheme = false) {
        VoiceToSignScreen()
    }
}

