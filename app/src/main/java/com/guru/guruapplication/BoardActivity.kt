package com.guru.guruapplication

import PostAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        recyclerView = findViewById(R.id.recyclerView)
        dbHelper = DatabaseHelper(this)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadPosts()

        // FloatingActionButton 클릭 리스너 설정
        val fabAddPost: FloatingActionButton = findViewById(R.id.fab_add_post)
        fabAddPost.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPosts() {
        val posts = dbHelper.getAllPosts()
        postAdapter = PostAdapter(posts)
        recyclerView.adapter = postAdapter
    }
}
