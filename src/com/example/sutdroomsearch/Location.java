package com.example.sutdroomsearch;

import com.example.sutdroomsearch.util.DatabaseHelper;
import android.content.Context;

/**
 * Location Model
 * 
 * This model wraps around Location related database entries
 * @author Swayam
 *
 */

public class Location {
	int id;
	float x;
	float y;
	float number;
	Person person;

	/**
	 * Calculates closest Location to the point on the map
	 * 
	 * @param x : x coordinate of the point on the map
	 * @param y : y coordinate of the point on the map
	 * @param m : Activity context.
	 * @return : The Location object with the smallest distance between the points.
	 */
	
	public static Location findClosestLocationTo(float x, float y, int level, Context m) {
		int[][] rooms = DatabaseHelper.getInstance(m).getAllRooms();
		double smallest_distance = Double.MAX_VALUE; 		//distance of the closest point
		int location_id = -1;
		for(int i =  0 ; i < rooms.length; i++) {
			int[] room = rooms[0];
			if(room[3] != level) continue;
			double distance = getDistance(x,y,room[1],room[2]);
			if (distance < smallest_distance) {
				location_id = room[0];
				smallest_distance = distance;
			}
		}
		
		return getLocationById(location_id, m);
	}
	
	public static Location getLocationById(int id, Context m) {
		Location location = new Location();
		int[] room = DatabaseHelper.getInstance(m).getRoomById(id);
		if(room[5] != -1) {	// room[4] specifies the user id associated with the location
			location.person = Person.getPersonById(room[4], m);
		}
		return location;
	}

	/**
	 * Get the distance between two points.
	 */
	private static double getDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1-y2,2));
	}
}
