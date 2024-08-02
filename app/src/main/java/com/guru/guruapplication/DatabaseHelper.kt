package com.guru.guruapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Posts.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_TITLE TEXT," +
                    "$COLUMN_AUTHOR TEXT," +
                    "$COLUMN_CONTENT TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertPost(post: Post): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, post.title)
            put(COLUMN_AUTHOR, post.author)
            put(COLUMN_CONTENT, post.content)
        }

        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllPosts(): List<Post> {
        val db = readableDatabase

        val projection = arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_AUTHOR, COLUMN_CONTENT)

        val cursor = db.query(
            TABLE_NAME,
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
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR))
                val content = getString(getColumnIndexOrThrow(COLUMN_CONTENT))
                posts.add(Post(title, author, content))
            }
        }
        cursor.close()

        return posts
    }


    fun deletePost(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}
