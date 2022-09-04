package id.smaraputra.githubuserapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "idGithub")
    var idGithub: Int? = 0,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "repoLink")
    var repoLink: String? = null,

    @ColumnInfo(name = "avatarLink")
    var avatarLink: String? = null,

) : Parcelable