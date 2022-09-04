package id.smaraputra.githubuserapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.smaraputra.githubuserapp.R
import id.smaraputra.githubuserapp.database.FavoriteModel
import id.smaraputra.githubuserapp.helper.FavoriteDiffCallback

class FavoriteAdapter(private val ctx: Context) : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val listFavorite = ArrayList<FavoriteModel>()

    fun setListFavorite(listFavorite: List<FavoriteModel>) {
        val diffCallback = FavoriteDiffCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(ctx)
            .asBitmap()
            .load(listFavorite[position].avatarLink)
            .placeholder(R.drawable.portrait_placeholder)
            .error(R.drawable.ic_baseline_sync_problem_24)
            .into(holder.avatarUser)
        holder.tvUsername.text = listFavorite[position].username
        holder.itemView.setOnClickListener {
            listFavorite[holder.adapterPosition].username?.let { it1 ->
                onItemClickCallback.onItemClicked(
                    it1
                )
            }
        }
        holder.tvType.text = listFavorite[position].type
        holder.tvUrl.text = listFavorite[position].repoLink
        holder.tvId.text = listFavorite[position].idGithub.toString()
    }

    override fun getItemCount(): Int = listFavorite.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatarUser: ImageView = itemView.findViewById(R.id.avatar_user_list)
        var tvUsername: TextView = itemView.findViewById(R.id.username_user)
        var tvType: TextView = itemView.findViewById(R.id.user_type)
        var tvUrl: TextView = itemView.findViewById(R.id.url_github_user)
        var tvId: TextView = itemView.findViewById(R.id.id_user)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }
}