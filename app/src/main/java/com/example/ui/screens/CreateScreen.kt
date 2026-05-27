package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.XpersonaViewModel
import com.example.ui.helpers.getPortraitDrawableId
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(viewModel: XpersonaViewModel) {
    val selectedMode by viewModel.selectedMode.collectAsState()
    val createPrompt by viewModel.createPrompt.collectAsState()
    val uploadedImageRes by viewModel.uploadedImageRes.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val generationProgress by viewModel.generationProgress.collectAsState()
    val selectedTemplateId by viewModel.selectedTemplateId.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeBackgroundDeep),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Page Title Row
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "AI Generator",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Start a modern portrait workflow stream",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Section: Mode Selection Title
        item {
            Text(
                text = "Generation Mode",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Modern 2-row multi-column card grid
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 1: 3 columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModeGridCard(
                        title = "Persona",
                        subtitle = "Create unique AI personas",
                        icon = Icons.Default.Face,
                        isSelected = selectedMode == "Persona",
                        onClick = { viewModel.setSelectedMode("Persona") },
                        modifier = Modifier.weight(1f)
                    )
                    ModeGridCard(
                        title = "Avatar",
                        subtitle = "Generate profile/avatars",
                        icon = Icons.Default.AccountCircle,
                        isSelected = selectedMode == "Avatar",
                        onClick = { viewModel.setSelectedMode("Avatar") },
                        modifier = Modifier.weight(1f)
                    )
                    ModeGridCard(
                        title = "Enhance",
                        subtitle = "Improve quality & detail",
                        icon = Icons.Default.AutoAwesome,
                        isSelected = selectedMode == "Enhance",
                        onClick = { viewModel.setSelectedMode("Enhance") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: 2 columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ModeGridCard(
                        title = "Copywriter",
                        subtitle = "Craft engaging AI content",
                        icon = Icons.Default.Edit,
                        isSelected = selectedMode == "Copywriter",
                        onClick = { viewModel.setSelectedMode("Copywriter") },
                        modifier = Modifier.weight(1f)
                    )
                    ModeGridCard(
                        title = "Style Shift",
                        subtitle = "Transform style & mood",
                        icon = Icons.Default.SwapHoriz,
                        isSelected = selectedMode == "Style Shift",
                        onClick = { viewModel.setSelectedMode("Style Shift") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Alert about template being active if selected
        if (selectedTemplateId != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ThemeCardBgDark)
                        .border(1.dp, PrimaryThemeColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info Active Template",
                        tint = PrimaryThemeColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Active Preset Template selected. Generating will match template features.",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Reset",
                        color = PrimaryThemeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.setSelectedTemplate(null) }
                    )
                }
            }
        }

        // Prompt input area
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Describe your persona",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${createPrompt.length}/500",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                OutlinedTextField(
                    value = createPrompt,
                    onValueChange = { if (it.length <= 500) viewModel.setCreatePrompt(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    placeholder = {
                        Text(
                            text = "Describe clothing type, face look, facial hair, hairstyle, camera lens detail, lighting environment (e.g., editorial portrait, studio light)...",
                            color = TextMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ThemeCardBgDark,
                        unfocusedContainerColor = ThemeCardBgDark,
                        focusedBorderColor = PrimaryThemeColor,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp),
                    maxLines = 4
                )
            }
        }

        // Upload image area (dashed container representation)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Upload Pose or Face Blueprint",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                if (uploadedImageRes != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Left: Uploaded Preview Box
                        Box(
                            modifier = Modifier
                                .size(height = 80.dp, width = 100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, PrimaryThemeColor, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            val imgRes = getPortraitDrawableId(uploadedImageRes!!)
                            Image(
                                painter = painterResource(id = imgRes),
                                contentDescription = "Uploaded",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Close button overlay
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(20.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .clickable { viewModel.setUploadedImage(null) },
                                contentAlignment = Alignment.Center
                              ) {
                                  Icon(
                                      imageVector = Icons.Default.Close,
                                      contentDescription = "Remove",
                                      tint = Color.White,
                                      modifier = Modifier.size(12.dp)
                                  )
                              }
                          }
                          
                          // Right: Empty dashed placeholder box to simulate adding other reference poses!
                          Box(
                              modifier = Modifier
                                  .size(height = 80.dp, width = 100.dp)
                                  .clip(RoundedCornerShape(12.dp))
                                  .background(ThemeCardBgDark)
                                  .border(
                                      1.dp,
                                      Color.White.copy(alpha = 0.08f),
                                      RoundedCornerShape(12.dp)
                                  )
                                  .clickable { viewModel.setUploadedImage(null) },
                              contentAlignment = Alignment.Center
                          ) {
                              Icon(
                                  imageVector = Icons.Default.Add,
                                  contentDescription = "Add Pose",
                                  tint = TextMuted,
                                  modifier = Modifier.size(20.dp)
                              )
                          }
                      }
                  } else {
                      // Standard empty dash container
                      Box(
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(80.dp)
                              .clip(RoundedCornerShape(12.dp))
                              .background(ThemeCardBgDark)
                              .border(
                                  1.dp,
                                  Color.White.copy(alpha = 0.08f),
                                  RoundedCornerShape(12.dp)
                              )
                              .clickable { viewModel.setUploadedImage("img_minimal_pro") },
                          contentAlignment = Alignment.Center
                      ) {
                          Column(
                              horizontalAlignment = Alignment.CenterHorizontally,
                              verticalArrangement = Arrangement.Center
                          ) {
                              Icon(
                                  imageVector = Icons.Default.Add,
                                  contentDescription = "Add Pose",
                                  tint = TextSecondary,
                                  modifier = Modifier.size(22.dp)
                              )
                              Spacer(modifier = Modifier.height(4.dp))
                              Text(
                                  text = "Upload pose blueprint (Simulate)",
                                  color = TextSecondary,
                                  fontSize = 10.sp,
                                  fontWeight = FontWeight.Medium
                              )
                          }
                      }
                  }
            }
        }

        // Active simulator generator state details
        item {
            AnimatedVisibility(
                visible = isGenerating,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ThemeCardBgLight),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryThemeColor.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = PrimaryThemeColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Executing $selectedMode Preset Engine",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${(generationProgress * 100).toInt()}% Done",
                                color = PrimaryThemeColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = generationProgress,
                            color = PrimaryThemeColor,
                            trackColor = Color.White.copy(alpha = 0.05f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(CircleShape)
                        )
                        
                        Text(
                            text = "Analyzing facial descriptors and mapping lights. Do not close the app.",
                            color = TextMuted,
                            fontSize = 10.sp,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }

        // Primary Generate button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        )
                    )
                    .clickable(enabled = !isGenerating) {
                        viewModel.startGenerationFlow()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isGenerating) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Synthesizing Studio Pixels...",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Sparkle Icon",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Generate $selectedMode Preset",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Pro Boosts guide card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeCardBgLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFFFAEC), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Bolt Premium Pro",
                            tint = AccentGoldVIP,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "VIP Priority Queue",
                            color = AccentGoldVIP,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Render up to 10x faster (under 3 seconds per matching face preset) with multi-model speed pipelines.",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow right settings",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Spacer back tab
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun ModeGridCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.5.dp,
                if (isSelected) PrimaryThemeColor else Color.White.copy(alpha = 0.06f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1E1A33) else ThemeCardBgDark
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) PrimaryThemeColor else TextMuted,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 8.sp,
                lineHeight = 10.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
