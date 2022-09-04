package id.smaraputra.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteModel: FavoriteModel)

    @Delete
    fun delete(favoriteModel: FavoriteModel)

    @Query("SELECT * from favorite_user ORDER BY username ASC")
    fun getAllFavorite(): LiveData<List<FavoriteModel>>

    @Query("SELECT * from favorite_user WHERE idGithub = :mIdGithub")
    fun checkFavorite(mIdGithub : Int): LiveData<List<FavoriteModel>>
}