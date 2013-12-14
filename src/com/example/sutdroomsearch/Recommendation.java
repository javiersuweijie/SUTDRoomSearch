package com.example.sutdroomsearch;

public class Recommendation {

	String name;
	String room_name;
	
	public Recommendation(String name, String rn) {
		this.name = name;
		this.room_name = rn;
	}
	
	public Recommendation(String rn) {
		this.room_name = rn;
	}
	
	@Override
	public String toString() {
		if (name == null) return room_name;
		else return room_name+"|"+name;
	}
}
