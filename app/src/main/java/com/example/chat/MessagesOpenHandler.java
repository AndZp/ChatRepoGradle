package com.example.chat;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Des63rus on 23.12.2014.
 */
public class MessagesOpenHandler extends SQLiteOpenHelper{

	public MessagesOpenHandler(Context context){
		super(context,"messagesDb", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL("CREATE TABLE messages (from TEXT NOT NULL, to TEXT NOT NULL, datetime INT NOT NULL, isReaden INTEGER DEFAULT 0)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}
}
