package com.magnocorrea.crawler_java_selenium_chrome.sandbox;

import java.util.ArrayList;
import java.util.List;

public class Unit {
	public String url;
	public String title;
	public String address;
	public List<String> amenities = new ArrayList<String>();
	public List<String> costs = new ArrayList<String>();
	
	@Override
	public String toString() {
		return "url:" + url + ";title:" + title  + ";address:" + address + ";amenity:" + amenities + ";costs:" + costs;
	}

}
