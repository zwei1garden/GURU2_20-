package com.guru.guruapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class TimerActivity : AppCompatActivity() {
    private lateinit var timerDisplay: TextView
    private lateinit var startPauseButton: ImageButton
    private lateinit var homeButton: ImageButton
    private lateinit var boardButton: ImageButton
    private lateinit var handler: Handler
    private var isRunning = false
    private var elapsedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerDisplay = findViewById(R.id.chronometer_display)
        startPauseButton = findViewById(R.id.imageButton2)
        homeButton = findViewById(R.id.imageButton3)
        boardButton = findViewById(R.id.imageButton5)

        handler = Handler(Looper.getMainLooper())

        startPauseButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
        homeButton.setOnClickListener {
            // MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        boardButton.setOnClickListener {
            // BoardActivity로 이동
            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)
        }

        resetTimerAtMidnight()
    }

    private fun startTimer() {
        isRunning = true
        startPauseButton.setImageResource(R.mipmap.ic_pause_round) // 일시정지 아이콘

        handler.postDelayed(object : Runnable {
            override fun run() {
                elapsedTime++
                updateTimerDisplay(elapsedTime)
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }

    private fun pauseTimer() {
        isRunning = false
        startPauseButton.setImageResource(R.mipmap.ic_play_round) // 재생 아이콘
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateTimerDisplay(seconds: Long) {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        timerDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun resetTimerAtMidnight() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val currentTime = System.currentTimeMillis()
        val resetTime = calendar.timeInMillis

        if (currentTime >= resetTime) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = calendar.timeInMillis - currentTime
        handler.postDelayed({
            elapsedTime = 0L
            updateTimerDisplay(elapsedTime)
            resetTimerAtMidnight()
        }, delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}