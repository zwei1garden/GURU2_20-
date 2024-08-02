package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var lecturesContainer: LinearLayout
    private lateinit var selectedLectures: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        dbHelper = DBHelper(this)
        lecturesContainer = findViewById(R.id.lectures_checkboxes_container)
        selectedLectures = mutableListOf()

        // Load all lectures from the database and create checkboxes
        val allLectures = dbHelper.getAllLectures()
        for (lecture in allLectures) {
            val checkBox = CheckBox(this)
            checkBox.text = lecture
            lecturesContainer.addView(checkBox)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedLectures.add(lecture)
                } else {
                    selectedLectures.remove(lecture)
                }
            }
        }

        // Save button
        val buttonSaveLectures: Button = findViewById(R.id.buttonSaveLectures)
        buttonSaveLectures.setOnClickListener {
            saveSelectedLectures()
            // Return to main page after saving
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Main page button
        val buttonGoToMainPage: Button = findViewById(R.id.buttonGoToMainPage)
        buttonGoToMainPage.setOnClickListener {
            // Return to main page without saving
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveSelectedLectures() {
        val sharedPreferences = getSharedPreferences("SelectedLectures", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("lectures", selectedLectures.toSet())
        editor.apply()
    }
}
