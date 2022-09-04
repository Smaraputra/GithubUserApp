package id.smaraputra.githubuserapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.smaraputra.githubuserapp.R
import id.smaraputra.githubuserapp.response.*

class FollowerDetailAdapter(private val ctx: Context, private val listFollower: List<FollowerUserResponse>) : RecyclerView.Adapter<FollowerDetailAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(ctx)
            .asBitmap()
            .load(listFollower[position].avatarUrl)
            .placeholder(R.drawable.portrait_placeholder)
            .error(R.drawable.ic_baseline_sync_problem_24)
            .into(holder.avatarUser)
        holder.tvUsername.text = listFollower[position].login
        holder.tvType.text = listFollower[position].type
        holder.tvUrl.text = listFollower[position].htmlUrl
        holder.tvId.text = listFollower[position].id.toString()
    }

    override fun getItemCount(): Int = listFollower.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatarUser: ImageView = itemView.findViewById(R.id.avatar_user_list)
        var tvUsername: TextView = itemView.findViewById(R.id.username_user)
        var tvType: TextView = itemView.findViewById(R.id.user_type)
        var tvUrl: TextView = itemView.findViewById(R.id.url_github_user)
        var tvId: TextView = itemView.findViewById(R.id.id_user)
    }
}