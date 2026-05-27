package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.ProjectRepository
import com.example.ui.BrandRow
import com.example.ui.CustomBottomNavigation
import com.example.ui.CustomStatusBar
import com.example.ui.TabType
import com.example.ui.XpersonaViewModel
import com.example.ui.XpersonaViewModelFactory
import com.example.ui.screens.CreateScreen
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThemeBackgroundDeep

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Initialize Room SQLite persistence components
    val database = AppDatabase.getDatabase(applicationContext)
    val projectDao = database.projectDao()
    val repository = ProjectRepository(projectDao)
    val factory = XpersonaViewModelFactory(repository)
    val viewModel = ViewModelProvider(this, factory)[XpersonaViewModel::class.java]

    setContent {
      MyApplicationTheme {
        val currentTab by viewModel.currentTab.collectAsState()

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          bottomBar = {
            CustomBottomNavigation(
              currentTab = currentTab,
              onTabSelected = { viewModel.setTab(it) }
            )
          }
        ) { innerPadding ->
          Box(
              modifier = Modifier
                  .fillMaxSize()
                  .background(ThemeBackgroundDeep)
                  .padding(bottom = innerPadding.calculateBottomPadding())
          ) {
              Column(modifier = Modifier.fillMaxSize()) {
                  CustomStatusBar()
                  BrandRow(showUserAvatar = currentTab != TabType.SETTINGS)
                  
                  // Smooth Crossfade transition for premium visual polish
                  Crossfade(targetState = currentTab, label = "TabTransition") { tab ->
                      when (tab) {
                          TabType.STUDIO -> StudioScreen(viewModel)
                          TabType.PROJECTS -> ProjectsScreen(viewModel)
                          TabType.CREATE -> CreateScreen(viewModel)
                          TabType.TEMPLATES -> TemplatesScreen(viewModel)
                          TabType.SETTINGS -> SettingsScreen(viewModel)
                      }
                  }
              }
          }
        }
      }
    }
  }
}

