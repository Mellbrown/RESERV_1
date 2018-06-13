package com.comma.cw01272.reservation.bean;

import java.util.ArrayList;

public class NoticeGroup {

	private String noticeName;
	private String date;
	private ArrayList<NoticeChild> Items;

	public String getnoticeName() {
		return noticeName;
	}

	public void setnoticeName(String noticeName) {
		this.noticeName = noticeName;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


	public ArrayList<NoticeChild> getItems() {
		return Items;
	}

	public void setItems(ArrayList<NoticeChild> Items) {
		this.Items = Items;
	}

}