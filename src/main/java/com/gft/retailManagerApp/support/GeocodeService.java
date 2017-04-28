package com.gft.retailManagerApp.support;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.gft.retailManagerApp.domain.Address;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PendingResult;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;

/**
 * Service to wrap the Google Maps API functionalities
 * 
 * @author Vanessa Cantalapiedra
 * @version 1.0
 * @since 20-04-2017
 */

@Service
public class GeocodeService {

	private final static String country = "ES"; // this should be provided in
												// order to get a more accurate
												// address

	private static final Logger logger = LoggerFactory.getLogger(GeocodeService.class);

	public static final double radius = 6372.8; // Earth radius In kilometers

	/**
	 * It retrieves the distance between 2 different locations using the
	 * Haversine Formula The haversine formula determines the great-circle
	 * distance between two points on a sphere given their longitudes and
	 * latitudes.
	 * 
	 * @param lat1
	 *            latitude of the first location
	 * @param lon1
	 *            longitude of the first location
	 * @param lat2
	 *            latitude of the second location
	 * @param lon2
	 *            longitude of the second location
	 * 
	 * @return double the distance between the 2 locations
	 */
	public static double getDistanceHaversine(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return radius * c;
	}

	/**
	 * Given a post code it returns the latitude and longitude values
	 * corresponding to that address. It uses Google Api Maps to get these
	 * values in an asynchronous way, by setting a callback
	 * 
	 * @param address
	 *            contains the post code info to get the latitude and longitude
	 *            values
	 * 
	 * @return CompletableFuture<GeoCoordenate> the address translation
	 */
	public CompletableFuture<GeoCoordinate> generateCoordenates(Address address) {

		CompletableFuture<GeoCoordinate> futureResults = new CompletableFuture<>();
		GeoApiContext geoContext = new GeoApiContext().setApiKey("AIzaSyCn1eD3xZcpYupgmXNBUcj6TOIWmQHaCz0");

		PendingResult.Callback<GeocodingResult[]> callback = new PendingResult.Callback<GeocodingResult[]>() {
			@Override
			public void onResult(GeocodingResult[] result) {
				Geometry geometry = result[0].geometry;
				futureResults.complete(new GeoCoordinate(geometry.location.lat, geometry.location.lng));
			}

			@Override
			public void onFailure(Throwable e) {
				logger.error("Error translating address : " + e.getMessage());
			}
		};

		GeocodingApi.newRequest(geoContext)
				.components(ComponentFilter.country(country), ComponentFilter.postalCode(address.getPostcode()))
				.setCallback(callback);

		return futureResults;

	}

}
