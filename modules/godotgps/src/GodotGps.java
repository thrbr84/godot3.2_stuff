package org.godotengine.godot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;


public class GodotGps extends Godot.SingletonBase {

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            latitude        = loc.getLatitude();
            longitude       = loc.getLongitude();
            altitude        = loc.getAltitude();
            gps_time        = loc.getTime();
            gps_speed       = loc.getSpeed();
            gps_bearing     = loc.getBearing();
            gps_accuracy    = loc.getAccuracy();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    // The minimum distance to change Updates in meters // 10 meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; 

    // The minimum time between updates in milliseconds // 1 sec
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;     

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    // Location
    Location location; 
    
    // Latitude
    private double latitude = 0; 
    
    // Longitude
    private double longitude = 0; 
    
    // Altitude
    private double altitude = 0;     
    
    // Time
    private long gps_time = 0;    
    
    // Speed
    private float gps_speed = 0;    
   
    // Bearing
    private float gps_bearing = 0;       

    // Accuracy
    private float gps_accuracy = 0;     
    
    // The main activity of the game
	private Activity activity = null; 
    
    //LocationListener locationListener;
    LocationListener locationListener = new MyLocationListener();
    
    // Declaring a Location Manager
    protected LocationManager locationManager;
    

	/* Init
	 * ********************************************************************** */

    /**
     * Function to get latitude
     */
    
    public String getStringLatitude() {
        String value =  Double.toString(this.latitude);
        
        return(value);
    } 

    
    /**
     * Function to get longitude
     * */ 
   
    public String getStringLongitude() {
        String value = Double.toString(this.longitude);
        return(value);
    } 


    /**
     * Function to get altitude
     * */ 
   
    public String getStringAltitude() {
        String value = Double.toString(this.altitude);
        return(value);
    } 

    
    /**
     * Function to get GPS Time
     * */ 
   
    public String getStringGPSTime() {
        String value = Long.toString(this.gps_time);
        return(value);
    } 

    
    /**
     * Function to get GPS Speed
     * */ 
   
    public String getStringGPSSpeed() {
        String value = Float.toString(this.gps_speed);
        return(value);
    } 
    

    /**
     * Function to get GPS Bearing
     * */ 
   
    public String getStringGPSBearing() {
        String value = Float.toString(this.gps_bearing);
        return(value);
    } 


    /**
     * Function to get GPS Accuracy
     * */ 
   
    public String getStringGPSAccuracy() {
        String value = Float.toString(this.gps_accuracy);
        return(value);
    } 

    
    public String getGPSState() {
        int value = 0;
        if (this.isGPSEnabled){
            value = 1;
        }
        return (Integer.toString(value));
    } 

    
    public String getNetworkState() {
        int value = 0;
        if (this.isNetworkEnabled){
            value = 1;
        }
        return (Integer.toString(value));
    } 

    public void getInit() {
		activity.runOnUiThread(new Runnable()
		{
			@Override public void run()
			{
                try {
                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        
                        // Getting GPS status
                        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                        // Getting network status
                        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        if (locationManager != null) 
                        {
                            canGetLocation = true;
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) 
                            {
                                latitude =  location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        } 
                        
                        locationManager.requestLocationUpdates(
                                                                LocationManager.GPS_PROVIDER,
                                                                MIN_TIME_BW_UPDATES,
                                                                MIN_DISTANCE_CHANGE_FOR_UPDATES, 
                                                                locationListener
                                                             );        
                }
                catch (Exception e) 
                {
                    //e.printStackTrace();
                    //Log.w("w",e);
                }                
			}
		});            
            
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    
    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
	public void stopUsingGPS()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override public void run()
			{
                if(locationManager != null){
                    isGPSEnabled = false;
                    isNetworkEnabled = false;
                    locationManager.removeUpdates(locationListener);
                }
			}
		});
	}    
    
    
	/* Definitions
	 * ********************************************************************** */

	/**
	 * Initilization Singleton
	 * @param Activity The main activity
	 */
 	static public Godot.SingletonBase initialize(Activity activity)
 	{
 		return new GodotGps(activity);
 	}

	/**
	 * Constructor
	 * @param Activity Main activity
	 */
	public GodotGps(Activity p_activity) {
		registerClass("GodotGps", new String[] {
			"init",
            "getInit",
            "getGPSState",
            "stopUsingGPS",
            "getNetworkState",
            "getStringLatitude",
            "getStringLongitude",
            "getStringAltitude",
            "getStringGPSTime",
            "getStringGPSSpeed",
            "getStringGPSBearing",
            "getStringGPSAccuracy"
		});
		activity = p_activity;
	}
}
