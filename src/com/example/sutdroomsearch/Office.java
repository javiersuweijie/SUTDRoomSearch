package com.example.sutdroomsearch;

public class Office {
	private String occupant;
	private String contact;
	private Point position;
	private String email;
	private String location;
	
	public void setPosition(int x, int y) {
		this.position = new Point(x,y);
	}

	public String getOccupant() {
		return occupant;
	}

	public void setOccupant(String occupant) {
		this.occupant = occupant;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}

class Point {
	public double x;
	public double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
