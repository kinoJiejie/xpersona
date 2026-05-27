package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
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
import com.example.ui.ProjectFilter
import com.example.ui.XpersonaViewModel
import com.example.ui.StatusChip
import com.example.ui.helpers.getPortraitDrawableId
import com.example.ui.theme.*

@Composable
fun ProjectsScreen(viewModel: XpersonaViewModel) {
    val activeFilter by viewModel.activeFilter.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()

    // Filtered lists matching current category chips
    val filteredProjects = remember(allProjects, activeFilter) {
        when (activeFilter) {
            ProjectFilter.ALL -> allProjects
            ProjectFilter.PERSONAS -> allProjects.filter { it.type.equals("Persona", ignoreCase = true) }
            ProjectFilter.AVATARS -> allProjects.filter { it.type.equals("Avatar", ignoreCase = true) }
            ProjectFilter.ENHANCED -> allProjects.filter { it.type.equals("Enhanced", ignoreCase = true) || it.type.equals("Enhance", ignoreCase = true) }
        }
    }

    val projectPairs = remember(filteredProjects) { filteredProjects.chunked(2) }

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
                    text = "My Projects",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Manage your generated virtual identities",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Filter chips bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(
                    label = "All",
                    selected = activeFilter == ProjectFilter.ALL,
                    onClick = { viewModel.setFilter(ProjectFilter.ALL) }
                )
                FilterChipItem(
                    label = "Personas",
                    selected = activeFilter == ProjectFilter.PERSONAS,
                    onClick = { viewModel.setFilter(ProjectFilter.PERSONAS) }
                )
                FilterChipItem(
                    label = "Avatars",
                    selected = activeFilter == ProjectFilter.AVATARS,
                    onClick = { viewModel.setFilter(ProjectFilter.AVATARS) }
                )
                FilterChipItem(
                    label = "Enhanced",
                    selected = activeFilter == ProjectFilter.ENHANCED,
                    onClick = { viewModel.setFilter(ProjectFilter.ENHANCED) }
                )
            }
        }

        // Storage usage card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ThemeCardBgMedium),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cloud Model Space",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        val sizeString = "${(allProjects.size * 12.4).toInt() + 14} MB / 512 MB"
                        Text(
                            text = sizeString,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    // Linear progress bar styling
                    val progressValue = (allProjects.size * 12.4f + 14f) / 512f
                    LinearProgressIndicator(
                        progress = progressValue,
                        color = PrimaryThemeColor,
                        trackColor = Color.White.copy(alpha = 0.08f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "VIP members get up to 10 GB storage with automated preset sync.",
                        color = TextMuted,
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }

        // Projects grid content in pairs of 2
        if (filteredProjects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .background(ThemeCardBgDark, RoundedCornerShape(18.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = "Empty folder",
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "No projects found in this category",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Any generated works will appear here automatically.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        } else {
            items(projectPairs) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { project ->
                        ProjectGridCard(
                            project = project,
                            modifier = Modifier.weight(1f),
                            onDelete = {
                                viewModel.deleteProject(project)
                            }
                        )
                    }
                    if (pair.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        // Navigation bottom barrier
        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun FilterChipItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) PrimaryThemeColor else ThemeCardBgDark)
            .border(
                1.dp,
                if (selected) PrimaryThemeColor else Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ProjectGridCard(
    project: com.example.data.ProjectEntity,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit
) {
    val isCompleted = project.status.equals("Completed", ignoreCase = true)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ThemeCardBgDark)
    ) {
        Column {
            // 4:3 or 1:1 image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(ThemeCardBgLight)
            ) {
                val imgRes = getPortraitDrawableId(project.imageResName)
                Image(
                    painter = painterResource(id = imgRes),
                    contentDescription = project.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Delete / Options icon upper right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .clickable { onDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            
            // Text Content Below
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = project.title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${project.type} • ${project.createdTime}",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(6.dp))
                
                // Status capsule at bottom
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isCompleted) Color(0xFF22C55E).copy(alpha = 0.12f)
                            else Color(0xFFFBBF24).copy(alpha = 0.12f)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(
                                    if (isCompleted) Color(0xFF22C55E) else Color(0xFFFBBF24),
                                    CircleShape
                                )
                        )
                        Text(
                            text = if (isCompleted) "Completed" else "In Progress",
                            color = if (isCompleted) Color(0xFF22C55E) else Color(0xFFFBBF24),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
