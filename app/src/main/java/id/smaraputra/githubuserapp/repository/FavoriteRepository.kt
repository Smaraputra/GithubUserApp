package id.smaraputra.githubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import id.smaraputra.githubuserapp.database.FavoriteDao
import id.smaraputra.githubuserapp.database.FavoriteModel
import id.smaraputra.githubuserapp.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }
    fun getAllFavorites(): LiveData<List<FavoriteModel>> = mFavoriteDao.getAllFavorite()
    fun checkFavorite(mIdGithub: Int): LiveData<List<FavoriteModel>> = mFavoriteDao.checkFavorite(mIdGithub)
    fun insert(favoriteModel: FavoriteModel) {
        executorService.execute { mFavoriteDao.insert(favoriteModel) }
    }
    fun delete(favoriteModel: FavoriteModel) {
        executorService.execute { mFavoriteDao.delete(favoriteModel) }
    }
}