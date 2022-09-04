package id.smaraputra.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.smaraputra.githubuserapp.database.FavoriteModel
import id.smaraputra.githubuserapp.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorites(): LiveData<List<FavoriteModel>> = mFavoriteRepository.getAllFavorites()
    fun checkFavorite(mIdGithub: Int): LiveData<List<FavoriteModel>> = mFavoriteRepository.checkFavorite(mIdGithub)
}