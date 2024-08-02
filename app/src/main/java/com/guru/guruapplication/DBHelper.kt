package com.guru.guruapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "GodSWU.db"

        // User Table
        private const val TABLE_USERS = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NICKNAME = "nickname"
        private const val COLUMN_MAJOR = "major"

        // Lecture Table
        private const val TABLE_LECTURES = "Lectures"
        private const val COLUMN_LECTURE_ID = "id"
        private const val COLUMN_LECTURE_CODE = "code"
        private const val COLUMN_LECTURE_PROFESSOR = "professor"
        private const val COLUMN_LECTURE_NAME = "name"

        // User-Lectures Relation Table
        private const val TABLE_USER_LECTURES = "UserLectures"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USER_LECTURE_ID = "lecture_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DBHelper", "Creating tables")

        // Create Users table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_NICKNAME TEXT,
                $COLUMN_MAJOR TEXT
            )
        """
        db.execSQL(createUsersTable)
        Log.d("DBHelper", "Users table created")

        // Create Lectures table
        val createLecturesTable = """
            CREATE TABLE $TABLE_LECTURES (
                $COLUMN_LECTURE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LECTURE_CODE TEXT,
                $COLUMN_LECTURE_PROFESSOR TEXT,
                $COLUMN_LECTURE_NAME TEXT
            )
        """
        db.execSQL(createLecturesTable)
        Log.d("DBHelper", "Lectures table created")

        // Create UserLectures table
        val createUserLecturesTable = """
            CREATE TABLE $TABLE_USER_LECTURES (
                $COLUMN_USER_ID INTEGER,
                $COLUMN_USER_LECTURE_ID INTEGER,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID),
                FOREIGN KEY ($COLUMN_USER_LECTURE_ID) REFERENCES $TABLE_LECTURES($COLUMN_LECTURE_ID),
                PRIMARY KEY ($COLUMN_USER_ID, $COLUMN_USER_LECTURE_ID)
            )
        """
        db.execSQL(createUserLecturesTable)
        Log.d("DBHelper", "UserLectures table created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DBHelper", "Upgrading database from version $oldVersion to $newVersion")

        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER_LECTURES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LECTURES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Add a user to the database
    fun addUser(username: String, password: String, nickname: String, major: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_NICKNAME, nickname)
            put(COLUMN_MAJOR, major)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    // Check if a user exists
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    // Check if username exists
    fun checkUsername(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    // Check if nickname exists
    fun checkNickname(nickname: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_NICKNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(nickname))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    // Add a lecture to the Lectures table
    fun addLecture(code: String, professor: String, name: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LECTURE_CODE, code)
            put(COLUMN_LECTURE_PROFESSOR, professor)
            put(COLUMN_LECTURE_NAME, name)
        }
        val result = db.insert(TABLE_LECTURES, null, values)
        db.close()
        return result != -1L
    }

    // Search for lectures by keyword
    fun searchLectures(keyword: String): List<String> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_LECTURES WHERE $COLUMN_LECTURE_NAME LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("%$keyword%"))
        val lectures = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                lectures.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LECTURE_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lectures
    }

    // Get the list of lectures selected by a user
    fun getUserSelectedLectures(userId: Int): List<String> {
        val db = this.readableDatabase
        val query = """
            SELECT l.$COLUMN_LECTURE_NAME
            FROM $TABLE_LECTURES l
            JOIN $TABLE_USER_LECTURES ul
            ON l.$COLUMN_LECTURE_ID = ul.$COLUMN_USER_LECTURE_ID
            WHERE ul.$COLUMN_USER_ID = ?
        """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val lectures = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                lectures.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LECTURE_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lectures
    }

    // Get user ID by username
    fun getUserId(username: String): Int {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun addLectureToUser(userId: Int, lectureId: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_USER_LECTURE_ID, lectureId)
        }
        val result = db.insert(TABLE_USER_LECTURES, null, values)
        db.close()
        return result != -1L
    }

    fun getAllLectures(): List<String> {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_LECTURE_NAME FROM $TABLE_LECTURES"
        val cursor = db.rawQuery(query, null)
        val lectures = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                lectures.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LECTURE_NAME)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lectures
    }

    fun getLectureId(lectureName: String): Int {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_LECTURE_ID FROM $TABLE_LECTURES WHERE $COLUMN_LECTURE_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(lectureName))
        var lectureId = -1
        if (cursor.moveToFirst()) {
            lectureId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LECTURE_ID))
        }
        cursor.close()
        db.close()
        return lectureId
    }

    fun clearUserLectures(userId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_USER_LECTURES,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString())
        )
        db.close()
        return result > 0
    }
}
