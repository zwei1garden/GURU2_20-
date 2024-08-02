package com.guru.guruapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private val postList: MutableList<Post>,
    private val onDeleteClick: (Long) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.postTitle.text = post.title
        holder.postAuthor.text = post.author
        holder.postContent.text = post.content

        holder.deleteButton.setOnClickListener {
            onDeleteClick(post.id) // Pass the post ID to the onDeleteClick callback
        }
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val postAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val postContent: TextView = itemView.findViewById(R.id.textViewContent)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDelete)
    }

    // Method to remove a post from the list and notify the change
    fun removePost(postId: Long) {
        val position = postList.indexOfFirst { it.id == postId }
        if (position >= 0) {
            postList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
