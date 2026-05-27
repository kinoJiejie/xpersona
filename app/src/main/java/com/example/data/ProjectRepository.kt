package com.example.data

import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {
    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjectsFlow()

    suspend fun insertProject(project: ProjectEntity) {
        projectDao.insertProject(project)
    }

    suspend fun deleteProjectById(id: Int) {
        projectDao.deleteProjectById(id)
    }
}
