package com.example.chat.dal;

import android.content.Context;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Des63rus on 23.12.2014.
 */
public class ChatDaoUtil {

	Realm realm;
	String userName;

	public ChatDaoUtil(Context context, String user){
		realm = Realm.getInstance(context);
		userName = user;
	}

	public void addMessage(String from, Date date, boolean isReaden){
		realm.beginTransaction();

		Message message = realm.createObject(Message.class);
		message.setFrom(from);
		message.setTo(userName);
		message.setDate(date);
		message.setReaden(isReaden);
		realm.commitTransaction();
	}

	public List<Message> getMessages(String from){
		LinkedList<Message> res = new LinkedList<Message>();
		if(from==null||"".equals(from)){
			return res;
		}
		RealmQuery<Message> query = realm.where(Message.class);
		query.equalTo("from", from);
		query.equalTo("to", userName);
		RealmResults<Message> results = query.findAll();
		for (Message message : results) {
			res.add(message);
		}
		return res;
	}
}
