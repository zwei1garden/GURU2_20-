package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonToBoard: Button = findViewById(R.id.buttonToBoard)
        val buttonToTimer: Button = findViewById(R.id.buttonToTimer)
        val buttonToMypage: Button = findViewById(R.id.buttonToMypage)
        val lecturesContainer: LinearLayout = findViewById(R.id.lectures_selected_container)

        // Load selected lectures from SharedPreferences
        val sharedPreferences = getSharedPreferences("SelectedLectures", MODE_PRIVATE)
        val selectedLectures = sharedPreferences.getStringSet("lectures", setOf()) ?: setOf()

        // Clear previous content
        lecturesContainer.removeAllViews()

        // Display selected lectures
        selectedLectures.forEach { lecture ->
            val textView = TextView(this).apply {
                text = lecture
                textSize = 16f
                setPadding(8, 8, 8, 8)
            }
            lecturesContainer.addView(textView)
        }

        buttonToTimer.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        buttonToBoard.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)
        }

        buttonToMypage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        val dbHelper = DBHelper(this)
        addInitialLectures(dbHelper)

    }

    fun addInitialLectures(dbHelper: DBHelper) {
        val lectures = listOf(
            Triple("MT01001", "정주립", "웹디자인및기획"),
            Triple("MT01047", "정소영", "C++프로그래밍응용"),
            Triple("MT04010", "백종호", "확률과통계"),
            Triple("MT01025", "황준", "JAVA프로그래밍응용"),
            Triple("IP01075", "최보윤", "모바일프로그래밍"),
            Triple("IP01031", "최은정", "윈도우즈보안과운영실습"),
            Triple("MT01042", "방정호", "클라우드오픈소스소프트웨어"),
            Triple("MT01057", "김현진", "인공지능"),
            Triple("MT01025", "엄성용", "프로젝트종합설계2"),
            Triple("MT01040", "윤홍수", "웹프로그래밍")
        )

        for ((code, professor, name) in lectures) {
            val success = dbHelper.addLecture(code, professor, name)
            if (success) {
                Log.d("DatabaseHelper", "Lecture added: Code=$code, Professor=$professor, Name=$name")
            } else {
                Log.e("DatabaseHelper", "Failed to add lecture: Code=$code, Professor=$professor, Name=$name")
            }
        }


        val db = dbHelper.writableDatabase

        db.close()
    }

}
