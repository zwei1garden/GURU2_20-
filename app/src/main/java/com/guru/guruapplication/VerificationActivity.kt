package com.guru.guruapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerificationActivity : AppCompatActivity() {

    private lateinit var questions: Map<String, String>
    private lateinit var answers: Map<String, List<String>>
    private lateinit var correctAnswers: Map<String, String>
    private lateinit var selectedDepartments: List<String>
    private var currentDepartmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val textViewQuestion = findViewById<TextView>(R.id.textViewQuestion)
        val radioGroupAnswers = findViewById<RadioGroup>(R.id.radioGroupAnswers)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        questions = mapOf(
            "소프트웨어융합학과" to "주디 아빠는 누구?",
            "디지털미디어학과" to "신현덕 교수님의 별명은?",
            "정보보호학과" to "푸린 교수님의 성함은?",
            "데이터사이언스학과" to "김예리 교수님의 mbti는?"
        )

        answers = mapOf(
            "소프트웨어융합학과" to listOf("백종호 교수님", "황준 교수님", "방정호 교수님", "정민교 교수님"),
            "디지털미디어학과" to listOf("현덕갓 교수님", "현더거더거덩", "현덕신 교수님", "갓현덕 교수님"),
            "정보보호학과" to listOf("이후린 교수님", "박후린 교수님", "김후린 교수님", "정후린 교수님"),
            "데이터사이언스학과" to listOf("isfp", "entj", "intp", "isfj")
        )

        // 학과별 정답
        correctAnswers = mapOf(
            "소프트웨어융합학과" to "백종호 교수님",
            "디지털미디어학과" to "갓현덕 교수님",
            "정보보호학과" to "박후린 교수님",
            "데이터사이언스학과" to "isfp"
        )

        selectedDepartments = intent.getStringArrayListExtra("departments") ?: emptyList()

        if (selectedDepartments.isNotEmpty()) {
            showQuestionForCurrentDepartment(textViewQuestion, radioGroupAnswers)
        } else {
            Toast.makeText(this, "인증할 학과가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        buttonSubmit.setOnClickListener {
            val selectedRadioButtonId = radioGroupAnswers.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedAnswer = findViewById<RadioButton>(selectedRadioButtonId).text.toString()
                val currentDepartment = selectedDepartments[currentDepartmentIndex]
                if (selectedAnswer == correctAnswers[currentDepartment]) {
                    Toast.makeText(this, "${currentDepartment} 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    currentDepartmentIndex++
                    if (currentDepartmentIndex < selectedDepartments.size) {
                        showQuestionForCurrentDepartment(textViewQuestion, radioGroupAnswers)
                    } else {
                        Toast.makeText(this, "모든 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "${currentDepartment} 인증에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "답변을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showQuestionForCurrentDepartment(textViewQuestion: TextView, radioGroupAnswers: RadioGroup) {
        val currentDepartment = selectedDepartments[currentDepartmentIndex]
        textViewQuestion.text = questions[currentDepartment]

        radioGroupAnswers.removeAllViews()
        answers[currentDepartment]?.forEach { answer ->
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioButton.setTextColor(resources.getColor(android.R.color.white))
            radioGroupAnswers.addView(radioButton)
        }
    }
}
