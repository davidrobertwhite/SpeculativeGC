package uk.ac.glasgow.etparser;

import uk.ac.glasgow.etparser.events.Event.TypeOfEvent;

public class ObjectStatus {

	private int size;
	private int timeOfLastEvent;
	private String id;
	private boolean born;
	private boolean dead;

	private TypeOfEvent lastEvent;

	public ObjectStatus( int t, String id,TypeOfEvent event) {
		timeOfLastEvent = t;
		this.id = id;
		lastEvent = event;
		born = false;
		dead = false;
	}

	public int getSize() {
		return size;
	}
	public void setSize(int size){
		this.size=size;
	}

	public int getTimeOfLastEvent() {
		return timeOfLastEvent;
	}

	public String getID() {
		return id;
	}

	public TypeOfEvent getLastEvent() {
		return lastEvent;
	}

	public void updateEvent(int time, TypeOfEvent event) {
		if (lastEvent == TypeOfEvent.DEATH) {
			return;

		}
		this.timeOfLastEvent = time;
		lastEvent = event;

	}

	public void giveBirth() {
		born = true;
	}

	public void kill() {
		dead = true;
	}

	public boolean isBorn() {
		return born;
	}

	public boolean isDead() {
		return dead;
	}

}
