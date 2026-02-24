package com.gestura.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestura.app.ui.theme.*

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // ── Header ──
        HomeHeader()

        // ── Card 1: Talk to a Signer ──
        TalkToSignerCard()

        // ── Card 2: Understand a Signer ──
        UnderstandSignerCard()

        // ── Card 3: Learning Portal (Dark) ──
        LearningPortalCard()
    }
}

// ──────────────────────────────────────────────
// Header
// ──────────────────────────────────────────────
@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
            )
            Text(
                text = "Sarah",
                style = MaterialTheme.typography.headlineLarge,
                color = Primary,
                fontWeight = FontWeight.Bold,
            )
        }
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = Primary,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

// ──────────────────────────────────────────────
// Card 1: Talk to a Signer
// ──────────────────────────────────────────────
@Composable
private fun TalkToSignerCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SurfaceLight),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.RecordVoiceOver,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Column {
                    Text(
                        text = "Talk to a Signer",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextMain,
                    )
                    Text(
                        text = "Voice → Sign Language Translation",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                    )
                }
            }

            Text(
                text = "Speak naturally and watch your words transform into expressive sign language through our 3D avatar in real time.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
            )

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White,
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Start Speaking",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Card 2: Understand a Signer
// ──────────────────────────────────────────────
@Composable
private fun UnderstandSignerCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SurfaceLight),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.Videocam,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Column {
                    Text(
                        text = "Understand a Signer",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextMain,
                    )
                    Text(
                        text = "Sign Language → Text Translation",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                    )
                }
            }

            Text(
                text = "Point your camera at a signer and get instant text translation on your screen, powered by AI vision.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
            )

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Primary),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp,
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
            ) {
                Icon(
                    Icons.Outlined.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Open Camera",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

// ──────────────────────────────────────────────
// Card 3: Learning Portal (Dark Theme)
// ──────────────────────────────────────────────
@Composable
private fun LearningPortalCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Accent, AccentDark)
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Column {
                    Text(
                        text = "Learning Portal",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                    )
                    Text(
                        text = "Interactive sign language courses",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextOnDarkMuted,
                    )
                }
            }

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DarkStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.LocalFireDepartment,
                    iconTint = Accent,
                    value = "12",
                    label = "Week Streak",
                )
                DarkStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Translate,
                    iconTint = PrimaryLight,
                    value = "248",
                    label = "Words Learned",
                )
            }

            // Progress
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Current: Basic Greetings",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextOnDarkMuted,
                    )
                    Text(
                        "65%",
                        style = MaterialTheme.typography.labelMedium,
                        color = Accent,
                    )
                }
                LinearProgressIndicator(
                    progress = { 0.65f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Accent,
                    trackColor = Color.White.copy(alpha = 0.1f),
                )
            }
        }
    }
}

@Composable
private fun DarkStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.07f),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp),
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextOnDarkMuted,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    GesturaTheme(darkTheme = false) {
        HomeScreen()
    }
}

