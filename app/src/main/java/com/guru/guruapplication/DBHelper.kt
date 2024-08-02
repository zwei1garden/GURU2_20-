package com.guru.guruapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GuruApp.db"
        private const val DATABASE_VERSION = 2

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

        // Post Table
        private const val TABLE_POSTS = "Posts"
        private const val COLUMN_POST_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_CONTENT = "content"

        private const val SQL_CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_NICKNAME TEXT,
                $COLUMN_MAJOR TEXT
            )
        """

        private const val SQL_CREATE_LECTURES_TABLE = """
            CREATE TABLE $TABLE_LECTURES (
                $COLUMN_LECTURE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LECTURE_CODE TEXT,
                $COLUMN_LECTURE_PROFESSOR TEXT,
                $COLUMN_LECTURE_NAME TEXT
            )
        """

        private const val SQL_CREATE_USER_LECTURES_TABLE = """
            CREATE TABLE $TABLE_USER_LECTURES (
                $COLUMN_USER_ID INTEGER,
                $COLUMN_USER_LECTURE_ID INTEGER,
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID),
                FOREIGN KEY ($COLUMN_USER_LECTURE_ID) REFERENCES $TABLE_LECTURES($COLUMN_LECTURE_ID),
                PRIMARY KEY ($COLUMN_USER_ID, $COLUMN_USER_LECTURE_ID)
            )
        """

        private const val SQL_CREATE_POSTS_TABLE = """
            CREATE TABLE $TABLE_POSTS (
                $COLUMN_POST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_CONTENT TEXT
            )
        """

        private const val SQL_DELETE_USERS_TABLE = "DROP TABLE IF EXISTS $TABLE_USERS"
        private const val SQL_DELETE_LECTURES_TABLE = "DROP TABLE IF EXISTS $TABLE_LECTURES"
        private const val SQL_DELETE_USER_LECTURES_TABLE = "DROP TABLE IF EXISTS $TABLE_USER_LECTURES"
        private const val SQL_DELETE_POSTS_TABLE = "DROP TABLE IF EXISTS $TABLE_POSTS"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DatabaseHelper", "Creating tables")

        db.execSQL(SQL_CREATE_USERS_TABLE)
        Log.d("DatabaseHelper", "Users table created")

        db.execSQL(SQL_CREATE_LECTURES_TABLE)
        Log.d("DatabaseHelper", "Lectures table created")

        db.execSQL(SQL_CREATE_USER_LECTURES_TABLE)
        Log.d("DatabaseHelper", "UserLectures table created")

        db.execSQL(SQL_CREATE_POSTS_TABLE)
        Log.d("DatabaseHelper", "Posts table created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DatabaseHelper", "Upgrading database from version $oldVersion to $newVersion")

        db.execSQL(SQL_DELETE_USER_LECTURES_TABLE)
        db.execSQL(SQL_DELETE_LECTURES_TABLE)
        db.execSQL(SQL_DELETE_USERS_TABLE)
        db.execSQL(SQL_DELETE_POSTS_TABLE)
        onCreate(db)
    }

    // Users table methods
    fun addUser(username: String, password: String, nickname: String, major: String): Boolean {
        val db = writableDatabase
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

    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun checkUsername(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun checkNickname(nickname: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_NICKNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(nickname))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    fun getUserId(username: String): Int {
        val db = readableDatabase
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

    fun clearUserLectures(userId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete(
            TABLE_USER_LECTURES,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId.toString())
        )
        db.close()
        return result > 0
    }

    // Lectures table methods
    fun addLecture(code: String, professor: String, name: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LECTURE_CODE, code)
            put(COLUMN_LECTURE_PROFESSOR, professor)
            put(COLUMN_LECTURE_NAME, name)
        }
        val result = db.insert(TABLE_LECTURES, null, values)
        db.close()
        return result != -1L
    }

    fun searchLectures(keyword: String): List<String> {
        val db = readableDatabase
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

    fun getUserSelectedLectures(userId: Int): List<String> {
        val db = readableDatabase
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

    fun getAllLectures(): List<String> {
        val db = readableDatabase
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
        val db = readableDatabase
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

    fun addLectureToUser(userId: Int, lectureId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_USER_LECTURE_ID, lectureId)
        }
        val result = db.insert(TABLE_USER_LECTURES, null, values)
        db.close()
        return result != -1L
    }

    // Posts table methods
    fun insertPost(post: Post): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, post.title)
            put(COLUMN_AUTHOR, post.author)
            put(COLUMN_CONTENT, post.content)
        }

        return db.insert(TABLE_POSTS, null, values)
    }

    fun getAllPosts(): List<Post> {
        val db = readableDatabase

        val projection = arrayOf(COLUMN_POST_ID, COLUMN_TITLE, COLUMN_AUTHOR, COLUMN_CONTENT)

        val cursor = db.query(
            TABLE_POSTS,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        val posts = mutableListOf<Post>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR))
                val content = getString(getColumnIndexOrThrow(COLUMN_CONTENT))
                posts.add(Post(id, title, author, content))
            }
        }
        cursor.close()

        return posts
    }

    fun deletePost(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_POSTS, "$COLUMN_POST_ID = ?", arrayOf(id.toString()))
    }


}
