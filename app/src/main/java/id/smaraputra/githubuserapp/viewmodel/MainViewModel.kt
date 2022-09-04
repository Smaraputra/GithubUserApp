package id.smaraputra.githubuserapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.smaraputra.githubuserapp.api.ConfigAPI
import id.smaraputra.githubuserapp.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    var searchWord: String = ""
    var firstList: Boolean = false
    var firstDetail: Boolean = false
    var firstFollower: Boolean = false
    var firstFollowing: Boolean = false

    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList

    private val _followingUser = MutableLiveData<List<FollowingUserResponse>>()
    val followingUser: LiveData<List<FollowingUserResponse>> = _followingUser

    private val _followerUser = MutableLiveData<List<FollowerUserResponse>>()
    val followerUser: LiveData<List<FollowerUserResponse>> = _followerUser

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSearchWord(word: String) {
        searchWord = word
    }

    fun saveStateList(bool: Boolean) {
        firstList = bool
    }

    fun saveStateDetail(bool: Boolean) {
        firstDetail = bool
    }

    fun saveStateFollower(bool: Boolean) {
        firstFollower = bool
    }

    fun saveStateFollowing(bool: Boolean) {
        firstFollowing = bool
    }

    fun searchUserData(ctx: Context, word: String) {
        _isLoading.value = true
        val client = ConfigAPI.getApiService().searchUser(word)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(call: Call<SearchUserResponse>, response: Response<SearchUserResponse>) {
                _isLoading.value = false
                when {
                    response.isSuccessful -> {
                        _userList.value = response.body()?.items as List<ItemsItem>
                    }
                    response.code()==403 -> {
                        Toast.makeText(ctx, "Too many API calls. Please wait.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(ctx, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun searchDetailUser(ctx: Context, word: String) {
        _isLoading.value = true
        val client = ConfigAPI.getApiService().detailUser(word)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body() as DetailUserResponse
                }else{
                    Toast.makeText(ctx, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showFollowing(ctx: Context, username: String) {
        _isLoading.value = true
        val client = ConfigAPI.getApiService().followingUser(username)
        client.enqueue(object : Callback<List<FollowingUserResponse>> {
            override fun onResponse(call: Call<List<FollowingUserResponse>>, response: Response<List<FollowingUserResponse>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if(!response.body().isNullOrEmpty()){
                        _followingUser.value = response.body() as List<FollowingUserResponse>
                    }
                }else {
                    Toast.makeText(ctx, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<FollowingUserResponse>>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showFollower(ctx: Context, username: String) {
        _isLoading.value = true
        val client = ConfigAPI.getApiService().followerUser(username)
        client.enqueue(object : Callback<List<FollowerUserResponse>> {
            override fun onResponse(call: Call<List<FollowerUserResponse>>, response: Response<List<FollowerUserResponse>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if(!response.body().isNullOrEmpty()){
                        _followerUser.value = response.body() as List<FollowerUserResponse>
                    }
                }else {
                    Toast.makeText(ctx, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<FollowerUserResponse>>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}