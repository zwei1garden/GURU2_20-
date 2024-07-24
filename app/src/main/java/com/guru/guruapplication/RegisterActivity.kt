package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DBHelper(this)

        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val nicknameEditText: EditText = findViewById(R.id.editTextNickname)

        val checkBoxSoftware: CheckBox = findViewById(R.id.checkboxSoftware)
        val checkBoxDigitalMedia: CheckBox = findViewById(R.id.checkboxDigitalMedia)
        val checkBoxInformationSecurity: CheckBox = findViewById(R.id.checkboxInformationSecurity)
        val checkBoxDataScience: CheckBox = findViewById(R.id.checkboxDataScience)

        val registerButton: Button = findViewById(R.id.buttonRegister)
        val textViewBackToLogin = findViewById<TextView>(R.id.textViewBackToLogin)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val nickname = nicknameEditText.text.toString().trim()

            val selectedMajors = mutableListOf<String>()
            if (checkBoxSoftware.isChecked) selectedMajors.add("소프트웨어융합학과")
            if (checkBoxDigitalMedia.isChecked) selectedMajors.add("디지털미디어학과")
            if (checkBoxInformationSecurity.isChecked) selectedMajors.add("정보보호학과")
            if (checkBoxDataScience.isChecked) selectedMajors.add("데이터사이언스학과")

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nickname)) {
                Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show()
            } else if (!username.matches(Regex("^[a-zA-Z0-9]{6,15}$"))) {
                Toast.makeText(this, "아이디는 6~15자 영문/숫자 조합으로 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (!password.matches(Regex("^[a-zA-Z0-9]{8,15}$"))) {
                Toast.makeText(this, "비밀번호는 8~15자 영문/숫자 조합으로 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.checkUsername(username)) {
                Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
            } else if (dbHelper.checkNickname(nickname)) {
                Toast.makeText(this, "이미 존재하는 닉네임입니다.", Toast.LENGTH_SHORT).show()
            } else if (selectedMajors.isEmpty()) {
                Toast.makeText(this, "하나 이상의 학과를 선택하세요.", Toast.LENGTH_SHORT).show()
            } else {
                val major = selectedMajors.joinToString(", ")
                val success = dbHelper.addUser(username, password, nickname, major)
                if (success) {
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }

        textViewBackToLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
