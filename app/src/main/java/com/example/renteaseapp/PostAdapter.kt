package com.example.renteaseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.renteaseapp.databinding.ItemPostBinding
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


class PostAdapter(
    private val postList: List<Post>,
    private val onClickListener: (Post, ActionType) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // Enum for button actions
    enum class ActionType {
        LIKE, COMMENT, SHARE
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postUsername: TextView = itemView.findViewById(R.id.postUsername)
        val postProfilePicture: ImageView = itemView.findViewById(R.id.postProfilePicture)
        val postDate: TextView = itemView.findViewById(R.id.postDate)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val userLocation: TextView = itemView.findViewById(R.id.userLocation)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val moreDetails: TextView = itemView.findViewById(R.id.moreDetails)

        val likeButton: Button = itemView.findViewById(R.id.likebtn)
        val commentButton: Button = itemView.findViewById(R.id.commentbtn)
        val shareButton: Button = itemView.findViewById(R.id.sharebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        // Set data to the views
        holder.postUsername.text = post.username
        holder.postDate.text = formatDate(post.date)
        holder.productName.text = "Product: ${post.itemName}"
        holder.productPrice.text = "Price: ${post.price}"
        holder.userLocation.text = "Location: ${post.location}"
        holder.moreDetails.text = "Click for more details..."

        // Load profile picture and product image using Glide
        Glide.with(holder.itemView.context)
            .load(post.profilePictureUrl)
            .placeholder(R.drawable.profile_circle_svgrepo_com)
            .into(holder.postProfilePicture)

        Glide.with(holder.itemView.context)
            .load(post.itemImageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.productImage)

        // Set click listeners for buttons
        holder.likeButton.setOnClickListener { onClickListener(post, ActionType.LIKE) }
        holder.commentButton.setOnClickListener { onClickListener(post, ActionType.COMMENT) }
        holder.shareButton.setOnClickListener { onClickListener(post, ActionType.SHARE) }
    }

    override fun getItemCount(): Int = postList.size

    // Helper method to format Timestamp to a readable String
    private fun formatDate(timestamp: Timestamp?): String {
        return if (timestamp != null) {
            val sdf = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
            sdf.format(timestamp.toDate())
        } else {
            "Unknown Date"
        }
    }
}



