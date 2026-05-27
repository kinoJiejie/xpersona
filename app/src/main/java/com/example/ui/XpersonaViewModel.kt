package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ProjectEntity
import com.example.data.ProjectRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TabType {
    STUDIO, PROJECTS, CREATE, TEMPLATES, SETTINGS
}

enum class ProjectFilter {
    ALL, PERSONAS, AVATARS, ENHANCED
}

class XpersonaViewModel(private val repository: ProjectRepository) : ViewModel() {

    // Bottom Navigation current tab state
    private val _currentTab = MutableStateFlow(TabType.STUDIO)
    val currentTab: StateFlow<TabType> = _currentTab.asStateFlow()

    // Project Filter chips
    private val _activeFilter = MutableStateFlow(ProjectFilter.ALL)
    val activeFilter: StateFlow<ProjectFilter> = _activeFilter.asStateFlow()

    // Studio Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Create Screen Workspace States
    private val _selectedMode = MutableStateFlow("Persona") // "Persona", "Avatar", "Enhance", "Copywriter", "Style Shift"
    val selectedMode: StateFlow<String> = _selectedMode.asStateFlow()

    private val _createPrompt = MutableStateFlow("")
    val createPrompt: StateFlow<String> = _createPrompt.asStateFlow()

    private val _uploadedImageRes = MutableStateFlow<String?>(null) // "img_minimal_pro" style image identifier
    val uploadedImageRes: StateFlow<String?> = _uploadedImageRes.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _generationProgress = MutableStateFlow(0f)
    val generationProgress: StateFlow<Float> = _generationProgress.asStateFlow()

    // Template selection state
    private val _selectedTemplateId = MutableStateFlow<String?>("minimal_pro")
    val selectedTemplateId: StateFlow<String?> = _selectedTemplateId.asStateFlow()

    // Templates category filter
    private val _templatesFilter = MutableStateFlow("Trending")
    val templatesFilter: StateFlow<String> = _templatesFilter.asStateFlow()

    // Flow of all database projects
    val allProjects: StateFlow<List<ProjectEntity>> = repository.allItemsFlowIfAny()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Pre-propagate default records if database database is entirely empty
        viewModelScope.launch {
            repository.allProjects.first().let { currentList ->
                if (currentList.isEmpty()) {
                    val sdf = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
                    val now = System.currentTimeMillis()
                    repository.insertProject(
                        ProjectEntity(
                            title = "Minimal Pro Corporate",
                            type = "Persona",
                            createdTime = sdf.format(Date(now - 1000 * 60 * 120)), // 2 hrs ago
                            status = "Completed",
                            prompt = "High-end editorial business executive profile, clean gray background, beautiful studio lighting, 8k",
                            imageResName = "img_minimal_pro"
                        )
                    )
                    repository.insertProject(
                        ProjectEntity(
                            title = "Street Style Vibe",
                            type = "Avatar",
                            createdTime = sdf.format(Date(now - 1000 * 60 * 360)), // 6 hrs ago
                            status = "Completed",
                            prompt = "Candid daily view street casual clothing style, beautiful Tokyo evening neon backlights",
                            imageResName = "img_street_style"
                        )
                    )
                    repository.insertProject(
                        ProjectEntity(
                            title = "Cinematic Studio Actor",
                            type = "Persona",
                            createdTime = sdf.format(Date(now - 1000 * 60 * 720)), // 12 hrs ago
                            status = "Completed",
                            prompt = "A dramatic shadow dark portrait headshot, warm orange accent ambient lighting",
                            imageResName = "img_creator_port"
                        )
                    )
                    repository.insertProject(
                        ProjectEntity(
                            title = "Fashion Journal Cover",
                            type = "Enhanced",
                            createdTime = sdf.format(Date(now - 1000 * 60 * 1440)), // 1 day ago
                            status = "Completed",
                            prompt = "Creative abstract conceptual photoshoot, minimal studio colors, high contrast editorial photography",
                            imageResName = "img_fashion_port"
                        )
                    )
                }
            }
        }
    }

    fun setTab(tab: TabType) {
        _currentTab.value = tab
    }

    fun setFilter(filter: ProjectFilter) {
        _activeFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedMode(mode: String) {
        _selectedMode.value = mode
    }

    fun setCreatePrompt(p: String) {
        _createPrompt.value = p
    }

    fun setUploadedImage(imgId: String?) {
        _uploadedImageRes.value = imgId
    }

    fun setSelectedTemplate(templateId: String?) {
        _selectedTemplateId.value = templateId
    }

    fun setTemplatesFilter(category: String) {
        _templatesFilter.value = category
    }

    // Trigger AI Generation sequence
    fun startGenerationFlow() {
        if (_isGenerating.value) return
        _isGenerating.value = true
        _generationProgress.value = 0f

        viewModelScope.launch {
            val sdf = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
            val promptText = if (_createPrompt.value.trim().isEmpty()) {
                "Professional AI portrait portrait generation"
            } else {
                _createPrompt.value.trim()
            }

            // Decide output image based on selected template or mode
            val associatedImage = when (_selectedTemplateId.value) {
                "minimal_pro" -> "img_minimal_pro"
                "street_style" -> "img_street_style"
                "biz_headshot" -> "img_biz_headshot"
                "creator_port" -> "img_creator_port"
                "fashion_port" -> "img_fashion_port"
                else -> {
                    when (_selectedMode.value) {
                        "Persona" -> "img_minimal_pro"
                        "Avatar" -> "img_street_style"
                        "Enhance" -> "img_creator_port"
                        else -> "img_fashion_port"
                    }
                }
            }

            val defaultTitle = when (_selectedMode.value) {
                "Persona" -> "AI Persona Studio"
                "Avatar" -> "Creative Avatar Profile"
                "Enhance" -> "Enhanced Image Portrait"
                "Copywriter" -> "Avatar Writer Script"
                else -> "Style Shift Avatar"
            }

            // Create temporary ID container
            val newProject = ProjectEntity(
                title = defaultTitle,
                type = _selectedMode.value,
                createdTime = "Just now",
                status = "In Progress",
                prompt = promptText,
                imageResName = associatedImage
            )

            // Insert into SQLite database (Room repository automatically notifies subscription flow!)
            repository.insertProject(newProject)

            // Simulate progress ticks
            for (i in 1..20) {
                delay(150L + (i * 10L)) // dynamic ease deceleration
                _generationProgress.value = (i / 20f)
            }

            // After simulated GPU computation finishes, fetches the list, finds the "In Progress" item, and makes it Completed!
            val updatedList = repository.allProjects.first()
            val createdItem = updatedList.firstOrNull { it.status == "In Progress" && it.prompt == promptText }
            if (createdItem != null) {
                repository.insertProject(
                    createdItem.copy(
                        status = "Completed",
                        createdTime = sdf.format(Date())
                    )
                )
            }

            // Clean up workspace text
            _createPrompt.value = ""
            _isGenerating.value = false
            _generationProgress.value = 0f
            // Navigate directly to Projects tab to review!
            _currentTab.value = TabType.PROJECTS
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProjectById(project.id)
        }
    }
}

// Extension to retrieve flow directly or bridge helper
fun ProjectRepository.allItemsFlowIfAny(): Flow<List<ProjectEntity>> = this.allProjects
