package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

        userId = intent.getIntExtra("USER_ID", -1)
        if (userId != -1) {
            val selectedLectures = loadSelectedLectures()

            val listView: ListView = findViewById(R.id.user_lectures_list_view)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedLectures)
            listView.adapter = adapter

            val calendarView: CalendarView = findViewById(R.id.calendar_view)
        } else {
            Toast.makeText(this, "사용자 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

        // Find and set up the button to navigate to MypageActivity
        val buttonToMypage: Button = findViewById(R.id.buttonToMypage)
        buttonToMypage.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            startActivity(intent)
        }
    }



    private fun loadSelectedLectures(): List<String> {
        return dbHelper.getUserSelectedLectures(userId)
    }
}
