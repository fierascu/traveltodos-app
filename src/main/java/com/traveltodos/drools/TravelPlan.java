package com.traveltodos.drools;

public class TravelPlan {

	private String type;
	private String wanted;

	public TravelPlan() {
	}
	
	public TravelPlan(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWanted() {
		return wanted;
	}

	public void setWanted(String wanted) {
		this.wanted = wanted;
	}

}
