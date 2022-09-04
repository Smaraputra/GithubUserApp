package id.smaraputra.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import id.smaraputra.githubuserapp.database.FavoriteModel
import id.smaraputra.githubuserapp.repository.FavoriteRepository

class FavoriteAddDeleteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun insert(favoriteModel: FavoriteModel) {
        mFavoriteRepository.insert(favoriteModel)
    }
    fun delete(favoriteModel: FavoriteModel) {
        mFavoriteRepository.delete(favoriteModel)
    }
}