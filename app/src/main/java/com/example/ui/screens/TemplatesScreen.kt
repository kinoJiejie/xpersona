package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.helpers.getPortraitDrawableId
import com.example.ui.theme.*

data class TemplateItemData(
    val id: String,
    val name: String,
    val type: String,
    val imageResName: String,
    val isNew: Boolean = false,
    val isPro: Boolean = false,
    val samplePrompt: String
)

@Composable
fun TemplatesScreen(viewModel: XpersonaViewModel) {
    val selectedTemplateId by viewModel.selectedTemplateId.collectAsState()
    val templatesFilter by viewModel.templatesFilter.collectAsState()

    // 6 suggested templates from user prompt
    val templates = remember {
        listOf(
            TemplateItemData(
                id = "minimal_pro",
                name = "Minimal Pro",
                type = "Persona",
                imageResName = "img_minimal_pro",
                isPro = true,
                samplePrompt = "Premium executive headshot, clean gray background, beautiful facial details, warm lighting"
            ),
            TemplateItemData(
                id = "street_style",
                name = "Street Style",
                type = "Avatar",
                imageResName = "img_street_style",
                isNew = true,
                samplePrompt = "Candid lifestyle street portrait, casual hoodie, urban background of Tokyo, sunset orange vibe"
            ),
            TemplateItemData(
                id = "business_headshot",
                name = "Business Headshot",
                type = "Persona",
                imageResName = "img_biz_headshot",
                isPro = true,
                samplePrompt = "Smiling modern young businessman, executive attire, elegant corporate backdrop, soft bokeh"
            ),
            TemplateItemData(
                id = "creator_portrait",
                name = "Creator Portrait",
                type = "Avatar",
                imageResName = "img_creator_port",
                isPro = true,
                samplePrompt = "Male holding camera, atmospheric warm ambient lights, soft studio background"
            ),
            TemplateItemData(
                id = "fashion_portrait",
                name = "Fashion Portrait",
                type = "Enhanced",
                imageResName = "img_fashion_port",
                isPro = true,
                samplePrompt = "Creative abstract fashion editorial, dramatic light shadow modeling, high-fashion journal cover"
            ),
            TemplateItemData(
                id = "cinematic_profile",
                name = "Cinematic Profile",
                type = "Persona",
                imageResName = "img_creator_port",
                isNew = true,
                samplePrompt = "Cinematic mood portrait, rich lighting shadows, professional movie character style preset"
            )
        )
    }

    // Quick filter simulation matching category chips
    val filteredTemplates = remember(templatesFilter) {
        when (templatesFilter) {
            "Trending" -> templates
            "Persona" -> templates.filter { it.type == "Persona" }
            "Avatar" -> templates.filter { it.type == "Avatar" }
            "Social" -> templates.filter { !it.isPro }
            "Pro" -> templates.filter { it.isPro }
            else -> templates
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeBackgroundDeep)
            .padding(top = 12.dp)
    ) {
        // Scrollable column with embedded Grid using LazyVerticalGrid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Content
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "Aesthetic Templates",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Choose a preset style to begin your portrait workflow",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Category list row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Trending", "Persona", "Avatar", "Social", "Pro").forEach { category ->
                    val isSelected = templatesFilter == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) PrimaryThemeColor else ThemeCardBgDark)
                            .border(
                                1.dp,
                                if (isSelected) PrimaryThemeColor else Color.White.copy(alpha = 0.08f),
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.setTemplatesFilter(category) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Grid layout of templates
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp) // Bottom padding for floating navigation spacing
            ) {
                items(filteredTemplates) { item ->
                    val isSelected = selectedTemplateId == item.id
                    TemplateCard(
                        item = item,
                        selected = isSelected,
                        onSelect = {
                            viewModel.setSelectedTemplate(item.id)
                            viewModel.setSelectedMode(item.type)
                            viewModel.setCreatePrompt(item.samplePrompt)
                            // Navigate directly to generator
                            viewModel.setTab(TabType.CREATE)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TemplateCard(
    item: TemplateItemData,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.5.dp,
                if (selected) PrimaryThemeColor else Color.White.copy(alpha = 0.08f),
                RoundedCornerShape(16.dp)
            )
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ThemeCardBgDark)
    ) {
        Column {
            // Photo Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(ThemeCardBgLight)
            ) {
                val imageRes = getPortraitDrawableId(item.imageResName)
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top bar in card layout to hold badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge details
                    if (item.isPro || item.isNew) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (item.isPro) AccentGoldVIP else PrimaryThemeColor
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (item.isPro) "PRO" else "NEW",
                                color = if (item.isPro) Color.Black else Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Save icon button representation
                    var bookmarked by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                            .clickable { bookmarked = !bookmarked },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save template",
                            tint = if (bookmarked) AccentGoldVIP else Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            // Description area
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.name,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.type,
                    color = if (selected) PrimaryThemeColor else TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
