package com.gft.retailManagerApp.support;

/**
 * Class that represents a GeoCoordinate
 * 
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */
public class GeoCoordinate {

	private double latitude;
	private double longitude;

	public GeoCoordinate(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}
}
