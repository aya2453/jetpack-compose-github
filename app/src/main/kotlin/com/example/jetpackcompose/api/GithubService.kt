package com.example.jetpackcompose.api

import com.example.jetpackcompose.GithubRepoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("/search/repositories")
    fun getGithubRepositories(
        @Query("q") query: String) : Call<GithubRepoResult>
}