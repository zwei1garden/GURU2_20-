package com.guru.guruapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MypageActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var lecturesCheckBoxContainer: LinearLayout
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        dbHelper = DBHelper(this)
        lecturesCheckBoxContainer = findViewById(R.id.lectures_checkboxes_container)
        saveButton = findViewById(R.id.buttonSaveLectures)

        loadLectures()

        saveButton.setOnClickListener {
            saveSelectedLectures()
        }
    }

    private fun loadLectures() {
        // Clear existing checkboxes
        lecturesCheckBoxContainer.removeAllViews()

        // Fetch all lectures from the database
        val lectures = dbHelper.getAllLectures()
        for (lecture in lectures) {
            val checkBox = CheckBox(this)
            checkBox.text = lecture
            // checkBox.textColor = getColor(R.color.white) // Assuming you have a white color resource
            lecturesCheckBoxContainer.addView(checkBox)
        }
    }

    private fun saveSelectedLectures() {
        val selectedLectures = mutableListOf<String>()

        // Iterate through all child views (CheckBoxes) in the container
        for (i in 0 until lecturesCheckBoxContainer.childCount) {
            val checkBox = lecturesCheckBoxContainer.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                selectedLectures.add(checkBox.text.toString())
            }
        }

        // Assuming user ID is passed via Intent or fetched from somewhere
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId != -1) {
            // Clear previous selections for this user
            dbHelper.clearUserLectures(userId)

            // Add selected lectures to the database
            for (lectureName in selectedLectures) {
                val lectureId = dbHelper.getLectureId(lectureName)
                if (lectureId != -1) {
                    dbHelper.addLectureToUser(userId, lectureId)
                }
            }
            Toast.makeText(this, "강의가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
