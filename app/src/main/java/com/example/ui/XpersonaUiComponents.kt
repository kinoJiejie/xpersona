package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

// Phone simulated top status bar
@Composable
fun CustomStatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(26.dp)
            .background(ThemeBackgroundDeep)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Digital Clock
        Text(
            text = "1:01 PM",
            color = TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        // Signals container
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SignalCellular4Bar,
                contentDescription = "Signal Network",
                tint = TextPrimary,
                modifier = Modifier.size(11.dp)
            )
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = "Wifi connection",
                tint = TextPrimary,
                modifier = Modifier.size(11.dp)
            )
            // Simulated battery percentage bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 9.dp)
                        .border(1.dp, TextPrimary.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                        .padding(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.85f)
                            .background(Color(0xFF22C55E), RoundedCornerShape(1.dp))
                    )
                }
            }
        }
    }
}

// Global visual header brand row
@Composable
fun BrandRow(showUserAvatar: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(ThemeBackgroundDeep)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Xpersona Designer Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Neon gradient brand square symbol
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "X",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Text(
                text = "Xpersona",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Small user avatar with glowing online status Indicator
        if (showUserAvatar) {
            Box(
                modifier = Modifier.size(34.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                // Circular stylish avatar backdrop placeholder
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(PrimaryThemeColor.copy(alpha = 0.5f), Color(0xFF10151D))
                            )
                        )
                        .border(1.dp, PrimaryThemeColor.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BM",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Green online indicator dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFF05070B), CircleShape)
                        .padding(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF22C55E), CircleShape)
                    )
                }
            }
        }
    }
}

// Elegant rounded floating pill bottom navigation
@Composable
fun CustomBottomNavigation(
    currentTab: TabType,
    onTabSelected: (TabType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // High-end dark rounded floating nav container
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF10151D).copy(alpha = 0.95f), RoundedCornerShape(26.dp))
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(26.dp))
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First: Studio tab (Grid icon)
            BottomNavItem(
                icon = Icons.Default.GridView,
                label = "Studio",
                isActive = currentTab == TabType.STUDIO,
                onClick = { onTabSelected(TabType.STUDIO) }
            )

            // Second: Projects tab (Folder icon)
            BottomNavItem(
                icon = Icons.Default.Folder,
                label = "Projects",
                isActive = currentTab == TabType.PROJECTS,
                onClick = { onTabSelected(TabType.PROJECTS) }
            )

            // Central: Create tab (Plus icon inside stylized circle)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                val buttonScale by animateFloatAsState(if (currentTab == TabType.CREATE) 1.08f else 1.0f)
                val buttonBg by animateColorAsState(
                    if (currentTab == TabType.CREATE) PrimaryThemeColor else Color(0xFF1A1F26)
                )
                val iconColor by animateColorAsState(
                    if (currentTab == TabType.CREATE) Color.White else TextSecondary
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .scale(buttonScale)
                        .background(buttonBg, CircleShape)
                        .clickable { onTabSelected(TabType.CREATE) }
                        .border(
                            1.dp,
                            if (currentTab == TabType.CREATE) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.08f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Plus Icon",
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Fourth: Templates tab (Stacked Layers icon)
            BottomNavItem(
                icon = Icons.Default.Layers,
                label = "Templates",
                isActive = currentTab == TabType.TEMPLATES,
                onClick = { onTabSelected(TabType.TEMPLATES) }
            )

            // Fifth: Settings tab (Gear icon)
            BottomNavItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                isActive = currentTab == TabType.SETTINGS,
                onClick = { onTabSelected(TabType.SETTINGS) }
            )
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(if (isActive) PrimaryThemeColor else TextMuted)
    val textColor by animateColorAsState(if (isActive) PrimaryThemeColor else TextMuted)

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

// Styled Status badge chip
@Composable
fun StatusChip(status: String) {
    val isCompleted = status.equals("Completed", ignoreCase = true)
    val bgColor = if (isCompleted) AccentGreenSuccess.copy(alpha = 0.12f) else AccentAmberProgress.copy(alpha = 0.12f)
    val textColor = if (isCompleted) AccentGreenSuccess else AccentAmberProgress

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(0.5.dp, textColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
