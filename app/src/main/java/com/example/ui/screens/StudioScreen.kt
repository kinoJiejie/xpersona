package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.TabType
import com.example.ui.XpersonaViewModel
import com.example.ui.StatusChip
import com.example.ui.helpers.getPortraitDrawableId
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(viewModel: XpersonaViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()

    // Filter tools/recent works by search query if any
    val filteredProjects = remember(allProjects, searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            allProjects.take(4) // show top 4 on Studio
        } else {
            allProjects.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.prompt.contains(searchQuery, ignoreCase = true)
            }.take(4)
        }
    }

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
                    text = "AI Studio",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Design your professional identity presets",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Search Bar & Create Button Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    placeholder = {
                        Text(
                            text = "Search tools or profiles...",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ThemeCardBgDark,
                        unfocusedContainerColor = ThemeCardBgDark,
                        focusedBorderColor = Color.White.copy(alpha = 0.15f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                
                Button(
                    onClick = { viewModel.setTab(TabType.CREATE) },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryThemeColor),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Create",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // VIP Pro Week Row
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setTab(TabType.SETTINGS) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeCardBgDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentGoldVIP.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // VIP Badge
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
                            fontWeight = FontWeight.Black
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pro Week",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Unlock Pro Tools & faster generations this week.",
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                    
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Go",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Hero card: Create AI Persona
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF141923))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.setTab(TabType.CREATE) }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Create\nAI Persona",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Design unique personas with AI in seconds.",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )
                        }
                        
                        Button(
                            onClick = { viewModel.setTab(TabType.CREATE) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryThemeColor),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = "Start",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    // Portrait Image covering the right half
                    Box(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxHeight()
                    ) {
                        Image(
                            painter = painterResource(id = getPortraitDrawableId("img_minimal_pro")),
                            contentDescription = "Create persona girl",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        )
                    }
                }
            }
        }

        // Section Title: Tool Grid
        item {
            Text(
                text = "Studio Tools",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Expanded Tools Grid (3 columns, 2 rows)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VerticalToolItem(
                        title = "Persona",
                        icon = Icons.Default.Face,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.setSelectedMode("Persona")
                        viewModel.setTab(TabType.CREATE)
                    }
                    VerticalToolItem(
                        title = "Avatar",
                        icon = Icons.Default.AccountCircle,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.setSelectedMode("Avatar")
                        viewModel.setTab(TabType.CREATE)
                    }
                    VerticalToolItem(
                        title = "Enhance",
                        icon = Icons.Default.AutoAwesome,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.setSelectedMode("Enhance")
                        viewModel.setTab(TabType.CREATE)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VerticalToolItem(
                        title = "Copywriter",
                        icon = Icons.Default.Edit,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.setSelectedMode("Copywriter")
                        viewModel.setTab(TabType.CREATE)
                    }
                    VerticalToolItem(
                        title = "Style Shift",
                        icon = Icons.Default.SwapHoriz,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.setSelectedMode("Style Shift")
                        viewModel.setTab(TabType.CREATE)
                    }
                    VerticalToolItem(
                        title = "Templates",
                        icon = Icons.Default.Layers,
                        modifier = Modifier.weight(1f)
                    ) {
                        viewModel.setTab(TabType.TEMPLATES)
                    }
                }
            }
        }

        // Section: Recent Works
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Works",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "View All",
                    color = PrimaryThemeColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.setTab(TabType.PROJECTS) }
                )
            }
        }

        if (filteredProjects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(ThemeCardBgDark, RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No generations yet. Tap Start or tools above!",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            // Horizontal Recent Works scroll container matching first screen mockup
            item {
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredProjects) { project ->
                        RecentWorkItem(project = project) {
                            viewModel.setTab(TabType.PROJECTS)
                        }
                    }
                }
            }
        }
        
        // Navigation visual margin
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun VerticalToolItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ThemeCardBgDark)
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = PrimaryThemeColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RecentWorkItem(
    project: com.example.data.ProjectEntity,
    onClick: () -> Unit
) {
    val isCompleted = project.status.equals("Completed", ignoreCase = true)
    
    Box(
        modifier = Modifier
            .width(110.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        val imgRes = getPortraitDrawableId(project.imageResName)
        // Profile Photo
        Image(
            painter = painterResource(id = imgRes),
            contentDescription = project.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Outer black/shadow overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                    )
                )
        )
        
        // Status capsule at bottom left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .border(0.5.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(
                            if (isCompleted) Color(0xFF22C55E) else Color(0xFFFBBF24),
                            CircleShape
                        )
                )
                Text(
                    text = if (isCompleted) "Completed" else "In Progress",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BoxBorderGold(): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(0.5.dp, AccentGoldVIP.copy(alpha = 0.3f))
}
