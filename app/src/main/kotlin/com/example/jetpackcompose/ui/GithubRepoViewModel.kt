package com.example.jetpackcompose.ui

import androidx.compose.Model
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.GithubRepoResult
import com.example.jetpackcompose.GithubRepoState
import com.example.jetpackcompose.LiveEvent
import com.example.jetpackcompose.ViewStatus
import com.example.jetpackcompose.api.GithubService
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult


class GithubRepoViewModel(
    private val githubService: GithubService): ViewModel() {
    val keyboardHidden = LiveEvent<Boolean>()

    fun fetchRepositories(query: String) {
        keyboardHidden.call(true)
        viewModelScope.launch {
            GithubRepoState.viewStatus =
                ViewStatus.LOADING
            when(val result = githubService.getGithubRepositories(query).awaitResult()) {
                is Result.Ok -> {
                    GithubRepoState.githubRepoResult = result.value
                    GithubRepoState.viewStatus =
                        if (result.value.totalCount == 0) {
                            ViewStatus.EMPTY
                        } else {
                            ViewStatus.COMPLETED
                        }
                }
                else -> GithubRepoState.viewStatus =
                    ViewStatus.ERROR
            }
        }
    }
}