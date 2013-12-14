package com.example.sutdroomsearch;

import java.util.ArrayList;

import com.example.sutdroomsearch.util.DatabaseHelper;
import android.content.Context;

/**
 * Location Model
 * 
 * This model wraps around Location related database entries
 * 
 * Class invariant:
 * 
 * location.person.location == location 
 * @author Swayam
 *
 */

public class Location {
	int id;
	float x;
	float y;
	String number;
	float level;
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
		ArrayList<String[]> rooms = DatabaseHelper.getInstance(m).getAllRoomsOnLevel(level);
		double smallest_distance = Double.MAX_VALUE; 		//distance of the closest point
		String[] closest_room = null;
		for(String[] room:rooms) 
		{
			if(!(room[1] == null || room[2] == null)) {
				double distance = getDistance(x,y,Integer.parseInt(room[1]),Integer.parseInt(room[2]));
				if (distance < smallest_distance) {
					closest_room = room;
					smallest_distance = distance;
				}
			}
		}
		
		return getLocationById(Integer.parseInt(closest_room[0]), m);
	}
	
	/**
	 * Given an array of Strings from the Database, constructs a database model
	 * @param data
	 * @return
	 */
	public static Location getLocationById(int id, Context m) {
		String[] data = DatabaseHelper.getInstance(m).getRoomById(id);
		return parseLocation(data, m);
	}
	
	public static Location parseLocation(String[] data, Context m) {
		Location location = new Location();
		location.id = Integer.parseInt(data[0]);
		if(location.id == -1) return null;

		location.x = Integer.parseInt(data[1]);
		location.y = Integer.parseInt(data[2]);
		location.level = Integer.parseInt(data[3]);
		location.number = data[4];
		location.person = Person.getPersonById(Integer.parseInt(data[5]), m);
		location.person.location = location; // satisfying the invariant.
		return location;
	}

<<<<<<< HEAD
=======
	public static Location getLocationById(int id, Context m) {
		String[] room = DatabaseHelper.getInstance(m).getRoomById(id);
		return parseLocation(room,m);
	}

	public static Location getLocationByName(String name, Context m) {
		String[] room = DatabaseHelper.getInstance(m).getRoomByName(name);
		return parseLocation(room,m);
	}

>>>>>>> Added Highlight functionality
	/**
	 * Get the distance between two points.
	 */
	private static double getDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1-y2,2));
	}
}
