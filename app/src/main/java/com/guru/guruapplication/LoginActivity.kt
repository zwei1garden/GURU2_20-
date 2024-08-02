package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var loginAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val loginButton: Button = findViewById(R.id.buttonLogin)
        val registerTextView: TextView = findViewById(R.id.textViewRegister)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (!username.matches(Regex("^[a-zA-Z0-9]{6,15}$"))) {
                Toast.makeText(this, "아이디는 6~15자 영문/숫자 조합으로 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (!password.matches(Regex("^[a-zA-Z0-9]{8,15}$"))) {
                Toast.makeText(this, "비밀번호는 8~15자 영문/숫자 조합으로 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.checkUser(username, password)
                if (success) {
                    val userId = dbHelper.getUserId(username)
                    if (userId != -1) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                        finish()  // End the LoginActivity
                    } else {
                        Toast.makeText(this, "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    loginAttempts++
                    if (loginAttempts >= 5) {
                        Toast.makeText(this, "로그인 실패: 5회 시도 초과", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
