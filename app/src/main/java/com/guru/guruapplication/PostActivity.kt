package com.guru.guruapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PostActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var postButton: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // UI 요소들 초기화
        titleEditText = findViewById(R.id.editText_title)
        authorEditText = findViewById(R.id.editText_author)
        contentEditText = findViewById(R.id.editText_content)
        postButton = findViewById(R.id.button_post)

        // DatabaseHelper 초기화
        dbHelper = DatabaseHelper(this)

        // Post 버튼 클릭 리스너 설정
        postButton.setOnClickListener {
            // 입력된 텍스트 가져오기
            val title = titleEditText.text.toString()
            val author = authorEditText.text.toString()
            val content = contentEditText.text.toString()

            // 간단한 유효성 검사
            if (title.isBlank() || author.isBlank() || content.isBlank()) {
                Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 게시글 데이터 생성
                val newPost = Post(title = title, author = author, content = content)

                // 게시글 데이터 저장
                savePost(newPost)

                // 사용자에게 알림
                Toast.makeText(this, "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show()

                // 액티비티 종료 및 이전 화면으로 돌아가기
                finish()
            }
        }
    }


    // 게시글을 저장하는 메서드
    private fun savePost(post: Post) {
        // SQLite 데이터베이스에 게시글 저장
        dbHelper.insertPost(post)
    }
}
