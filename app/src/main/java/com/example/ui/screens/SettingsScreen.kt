package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.XpersonaViewModel
import com.example.ui.theme.*
import com.example.ui.helpers.getPortraitDrawableId

@Composable
fun SettingsScreen(viewModel: XpersonaViewModel) {
    var showGuideDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeBackgroundDeep),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Row
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Manage your model preferences and credentials",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Profile card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeCardBgMedium),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar view of Alexandra Lee
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, AccentGoldVIP, CircleShape)
                        ) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = getPortraitDrawableId("img_minimal_pro")),
                                contentDescription = "Alexandra Lee Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Info details
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Alexandra Lee",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "alexandra@xpersona.ai",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Badges and Upgrade buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pro Member status indicator
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star badge",
                                    tint = AccentGoldVIP,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Pro Member",
                                    color = AccentGoldVIP,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        // Upgrade Button
                        Button(
                            onClick = { showGuideDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryThemeColor),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Upgrade Plan",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Settings items list
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeCardBgDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
            ) {
                Column {
                    SettingsRowItem(
                        icon = Icons.Default.Person,
                        title = "Account Management",
                        desc = "E-mail, password and security factors"
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    SettingsRowItem(
                        icon = Icons.Default.CardMembership,
                        title = "Membership Privilege",
                        desc = "View billing periods and custom generation bounds"
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    SettingsRowItem(
                        icon = Icons.Default.Notifications,
                        title = "Smart Notifications",
                        desc = "Alert status for finished offline model queues"
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    SettingsRowItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & Encryption",
                        desc = "Control training permissions of source faces"
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    SettingsRowItem(
                        icon = Icons.Default.Palette,
                        title = "App Appearance",
                        desc = "Force dark studio layout and system borders"
                    )
                    HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                    SettingsRowItem(
                        icon = Icons.Default.HelpOutline,
                        title = "Help & Live Support",
                        desc = "Contact creators or browse training document papers"
                    )
                }
            }
        }

        // Pro Member Guide card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeCardBgDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.3f)) {
                        // VIP badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AccentGoldVIP)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "VIP",
                                color = Color.Black,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Pro Member Guide",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Get the most out of Xpersona Pro with premium tips, tutorials and master workflows.",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // View Guide Action Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryThemeColor)
                                .clickable { showGuideDialog = true }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "View Guide",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Isometric Neon Cube Canvas representation on the right
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .height(86.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IsometricCubeCanvas(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }

        // Bottom space navigation
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    // Guide popup alert dialog
    if (showGuideDialog) {
        AlertDialog(
            onDismissRequest = { showGuideDialog = false },
            confirmButton = {
                TextButton(onClick = { showGuideDialog = false }) {
                    Text("Got It", color = PrimaryThemeColor, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified Member privilege guide",
                        tint = AccentGoldVIP,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PRO Member Guide",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Welcome to the premium dark studio, Brand Creator!",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "As an active VIP member, you unlock these advanced offline features:",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    BulletPoint(text = "Ultra-HD 8K texture filters (Standard is 2K).")
                    BulletPoint(text = "Zero wait time priority graphics card queue.")
                    BulletPoint(text = "Postural and gesture extraction with customizable weight models (0.1 to 1.0).")
                }
            },
            containerColor = ThemeCardBgLight,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
fun SettingsRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryThemeColor.copy(alpha = 0.9f),
                modifier = Modifier.size(18.dp)
            )
            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = desc,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Chevron",
            tint = TextMuted,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun BulletPoint(text: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("•", color = PrimaryThemeColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text, color = TextSecondary, fontSize = 11.sp, lineHeight = 14.sp)
    }
}

@Composable
fun IsometricCubeCanvas(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = size.width / 4f // size factor
        
        // Define isometric projection vertices
        // Top face
        val p1 = androidx.compose.ui.geometry.Offset(cx, cy - r * 0.8f)                 // Center top
        val p2 = androidx.compose.ui.geometry.Offset(cx + r * 1.732f / 2f, cy - r * 0.3f) // Right top
        val p3 = androidx.compose.ui.geometry.Offset(cx, cy + r * 0.2f)                 // Bottom top
        val p4 = androidx.compose.ui.geometry.Offset(cx - r * 1.732f / 2f, cy - r * 0.3f) // Left top
        
        // Bottom face (shifted downwards)
        val dy = r * 0.75f
        val b1 = androidx.compose.ui.geometry.Offset(p1.x, p1.y + dy)
        val b2 = androidx.compose.ui.geometry.Offset(p2.x, p2.y + dy)
        val b3 = androidx.compose.ui.geometry.Offset(p3.x, p3.y + dy)
        val b4 = androidx.compose.ui.geometry.Offset(p4.x, p4.y + dy)
        
        val neonColor = Color(0xFF6366F1)
        val cyanColor = Color(0xFF06B6D4)
        val strokeWidth = 1.5.dp.toPx()
        
        // Top Face
        drawLine(color = neonColor, start = p1, end = p2, strokeWidth = strokeWidth)
        drawLine(color = neonColor, start = p2, end = p3, strokeWidth = strokeWidth)
        drawLine(color = neonColor, start = p3, end = p4, strokeWidth = strokeWidth)
        drawLine(color = neonColor, start = p4, end = p1, strokeWidth = strokeWidth)
        
        // Bottom Face
        drawLine(color = cyanColor, start = b1, end = b2, strokeWidth = strokeWidth)
        drawLine(color = cyanColor, start = b2, end = b3, strokeWidth = strokeWidth)
        drawLine(color = cyanColor, start = b3, end = b4, strokeWidth = strokeWidth)
        drawLine(color = cyanColor, start = b4, end = b1, strokeWidth = strokeWidth)
        
        // Verticals connecting Top to Bottom
        drawLine(color = neonColor, start = p1, end = b1, strokeWidth = strokeWidth)
        drawLine(color = neonColor, start = p2, end = b2, strokeWidth = strokeWidth)
        drawLine(color = neonColor, start = p3, end = b3, strokeWidth = strokeWidth)
        drawLine(color = neonColor, start = p4, end = b4, strokeWidth = strokeWidth)
    }
}
