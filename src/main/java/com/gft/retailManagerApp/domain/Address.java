package com.gft.retailManagerApp.domain;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * It contains information related to the address of the {@link Shop} that
 * belongs to.
 *
 * For latitudes or longitudes equals to zero , they are considered as not valid
 * values.
 * 
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

public class Address {

	@NotNull
	@Digits(fraction = 0, integer = 5)
	private int number;
	@NotNull
	@Size(min = 1, max = 10)
	private String postcode;

	@JsonIgnore
	private double latitude;
	@JsonIgnore
	private double longitude;

	public Address() {
	}

	public Address(int number, String postcode) {
		this.number = number;
		this.postcode = postcode;
	}

	public Address(Address address) {
		this.number = address.number;
		this.postcode = address.postcode;
		this.latitude = address.latitude;
		this.longitude = address.longitude;
	}

	public int getNumber() {
		return number;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * The address to be considered valid must have at least one of its latitude
	 * and longitude values different from 0 If both are 0 , it is considered
	 * that the geolocalization process went wrong
	 * 
	 * @return boolean true if the latitude and longitude are valid values
	 */
	public boolean validValues() {
		boolean result = false;

		if (latitude != 0 || longitude != 0) {
			result = true;
		}

		return result;
	}

	@Override
	public String toString() {

		return "number: " + getNumber() + ", postcode: " + getPostcode() + " , latitude: " + getLatitude()
				+ ", longitude " + getLongitude();

	}
}
