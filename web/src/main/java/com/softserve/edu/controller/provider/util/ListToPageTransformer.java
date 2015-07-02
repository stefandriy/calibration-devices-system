package com.softserve.edu.controller.provider.util;

import java.util.List;

public class ListToPageTransformer<T> {
	private int totalItems;
	private List<T> content;
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	

}
