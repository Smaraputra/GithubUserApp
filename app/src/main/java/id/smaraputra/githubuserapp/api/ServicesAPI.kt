package id.smaraputra.githubuserapp.api

import id.smaraputra.githubuserapp.response.*
import retrofit2.Call
import retrofit2.http.*

interface ServicesAPI {

    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun detailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/following")
    fun followingUser(
        @Path("username") username: String
    ): Call<List<FollowingUserResponse>>

    @GET("users/{username}/followers")
    fun followerUser(
        @Path("username") username: String
    ): Call<List<FollowerUserResponse>>
}