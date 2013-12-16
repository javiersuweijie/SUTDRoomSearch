package com.example.sutdroomsearch;

public class Recommendation {

	String user_name;
	int user_id;
	String room_name;
	int room_id;
	
	public Recommendation(String user_id, String user_name, String room_id, String room_name) {
		if (user_id==null) this.user_id = -1;
		else this.user_id = Integer.parseInt(user_id);
		this.user_name = user_name;
		if (room_id==null) this.room_id = -1;
		else this.room_id = Integer.parseInt(room_id);
		this.room_name = room_name;
	}
	
	
	@Override
	public String toString() {
		if (user_name == null) return room_name;
		else return room_name+"|"+user_name;
	}
}
