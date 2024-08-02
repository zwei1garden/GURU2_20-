package com.guru.guruapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guru.guruapplication.Post
import com.guru.guruapplication.R

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        // Bind data to views
        holder.postTitle.text = post.title
        holder.postAuthor.text = post.author
        holder.postContent.text = post.content
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val postAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val postContent: TextView = itemView.findViewById(R.id.textViewContent)
    }
}

