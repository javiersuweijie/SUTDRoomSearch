package com.example.sutdroomsearch;

import com.example.sutdroomsearch.util.DatabaseHelper;

import android.content.Context;

public class Person {
	int id;
	String name;
	String position;
	String email;
	String number;
	Location location;
	
	// Class invariant: person.location.person = person.
	
	public static Person getPersonById(int id, Context m) {
		Person person = new Person();
		String[] person_data = DatabaseHelper.getInstance(m).getPerson(id);
		person.id = Integer.parseInt(person_data[0]); 
		if(id == -1) return null;
		person.name = person_data[1]; 
		person.position = (person_data[2]); 
		person.email = (person_data[3]); 
		person.number = person_data[4];

		return person;
	}
}