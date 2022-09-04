package id.smaraputra.githubuserapp.helper

import androidx.recyclerview.widget.DiffUtil
import id.smaraputra.githubuserapp.database.FavoriteModel

class FavoriteDiffCallback(private val mOldFavorite: List<FavoriteModel>, private val mNewFavorite: List<FavoriteModel>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavorite.size
    }
    override fun getNewListSize(): Int {
        return mNewFavorite.size
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavorite[oldItemPosition].idGithub == mNewFavorite[newItemPosition].idGithub
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = mOldFavorite[oldItemPosition]
        val newFavorite = mNewFavorite[newItemPosition]
        return oldFavorite.username == newFavorite.username
                && oldFavorite.repoLink == newFavorite.repoLink
                && oldFavorite.type == newFavorite.type
                && oldFavorite.avatarLink == newFavorite.avatarLink
                && oldFavorite.idGithub == newFavorite.idGithub
    }
}