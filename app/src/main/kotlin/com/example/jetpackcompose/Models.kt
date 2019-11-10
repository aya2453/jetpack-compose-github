package com.example.jetpackcompose

import androidx.compose.Model
import com.squareup.moshi.Json

data class GithubRepo(
    @Json(name = "full_name")
    val name: String,
    @Json(name = "html_url")
    val htmlUrl: String,
    val description: String?
)

data class GithubRepoResult(
    @Json(name = "total_count")
    val totalCount: Int,
    val items: List<GithubRepo>
)

@Model
object GithubRepoState {
    var viewStatus = ViewStatus.DEFAULT
    var githubRepoResult : GithubRepoResult? = null
}


enum class ViewStatus(val message: String) {
    DEFAULT("Enter your search terms"),
    LOADING("Now Loading"),
    EMPTY("Items not found"),
    COMPLETED(""),
    ERROR("Oops, Something is wrong");

    val shouldShowSingleMessage: Boolean
        get() = this == DEFAULT || this == EMPTY || this == ERROR
}

