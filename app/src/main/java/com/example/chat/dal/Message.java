package com.example.chat.dal;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Des63rus on 23.12.2014.
 */
public class Message extends RealmObject{

	private String from;

	private String to;

	private Date date;

	private String text;

	private boolean isReaden;

	public boolean isReaden() {
		return isReaden;
	}

	public void setReaden(boolean isReaden) {
		this.isReaden = isReaden;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
