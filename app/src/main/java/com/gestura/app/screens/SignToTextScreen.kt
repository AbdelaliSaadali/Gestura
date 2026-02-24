package com.gestura.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestura.app.ui.theme.*

@Composable
fun SignToTextScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CameraPlaceholder),
    ) {
        // ── Camera Placeholder Content ──
        CameraViewPlaceholder()

        // ── Top Status Bar ──
        TopStatusOverlay(
            modifier = Modifier.align(Alignment.TopCenter),
        )

        // ── Bottom Frosted Glass Overlay ──
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            TranslationOverlayCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(12.dp))

            // ── Control Buttons Row ──
            ControlButtonsRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 20.dp),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Camera View Placeholder
// ──────────────────────────────────────────────
@Composable
private fun CameraViewPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Videocam,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.2f),
                modifier = Modifier.size(80.dp),
            )
            Text(
                text = "CameraX Live Feed",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.3f),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Top Status Overlay
// ──────────────────────────────────────────────
@Composable
private fun TopStatusOverlay(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.5f),
                        Color.Transparent,
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Recording indicator
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Error),
            )
            Text(
                text = "LIVE",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }

        // AI Indicator
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.15f),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = "AI Active",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Frosted Glass Translation Card
// ──────────────────────────────────────────────
@Composable
private fun TranslationOverlayCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = FrostedGlass, // semi-transparent dark background
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Status label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Success),
                )
                Text(
                    text = "TRANSLATING...",
                    style = MaterialTheme.typography.labelMedium,
                    color = Success,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                )
            }

            // Translated text
            Text(
                text = "Hello, how can I help you today?",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
            )

            // Confidence
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    Icons.Filled.Verified,
                    contentDescription = null,
                    tint = PrimaryLight,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = "Confidence: 94%",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextOnDarkMuted,
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Control Buttons Row
// ──────────────────────────────────────────────
@Composable
private fun ControlButtonsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Clear button
        CircularControlButton(
            icon = Icons.Filled.Close,
            label = "Clear",
            containerColor = Color.White.copy(alpha = 0.15f),
            contentColor = Color.White,
            size = 52.dp,
        )

        // Prominent Speak button (larger)
        CircularControlButton(
            icon = Icons.Filled.VolumeUp,
            label = "Speak",
            containerColor = Primary,
            contentColor = Color.White,
            size = 68.dp,
            iconSize = 30.dp,
        )

        // Flip Camera button
        CircularControlButton(
            icon = Icons.Filled.FlipCameraAndroid,
            label = "Flip",
            containerColor = Color.White.copy(alpha = 0.15f),
            contentColor = Color.White,
            size = 52.dp,
        )
    }
}

@Composable
private fun CircularControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    containerColor: Color,
    contentColor: Color,
    size: androidx.compose.ui.unit.Dp,
    iconSize: androidx.compose.ui.unit.Dp = 24.dp,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        FloatingActionButton(
            onClick = { },
            modifier = Modifier.size(size),
            shape = CircleShape,
            containerColor = containerColor,
            contentColor = contentColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
            ),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(iconSize),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignToTextScreenPreview() {
    GesturaTheme(darkTheme = false) {
        SignToTextScreen()
    }
}

