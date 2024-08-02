package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BoardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        recyclerView = findViewById(R.id.recyclerView)
        dbHelper = DBHelper(this)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadPosts()

        // FloatingActionButton 클릭 리스너 설정
        val fabAddPost: FloatingActionButton = findViewById(R.id.fab_add_post)
        fabAddPost.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

        // ImageButton 클릭 리스너 설정
        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
        val imageButton4: ImageButton = findViewById(R.id.imageButton4)
        val imageButton5: ImageButton = findViewById(R.id.imageButton5)

        imageButton3.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        imageButton4.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadPosts() {
        val posts = dbHelper.getAllPosts().toMutableList()
        postAdapter = PostAdapter(posts) { id ->
            deletePost(id)
        }
        recyclerView.adapter = postAdapter
    }

    private fun deletePost(id: Long) {
        val rowsDeleted = dbHelper.deletePost(id)
        if (rowsDeleted > 0) {
            postAdapter.removePost(id)
        }
    }
}
