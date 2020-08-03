package ddns.net.src.entities;

import androidx.annotation.NonNull;

public class LocationData {

    private double latitude;

    private double longitude;

    @NonNull
    @Override
    public String toString() {
        return "LAT: " + latitude + " LON: " + longitude;
    }

    public LocationData(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
