package com.gmail.jskapplications.seoulpicture.model;

import com.gmail.jskapplications.seoulpicture.network.LocationNetwork;

/**
 * Created by for on 2017-10-28.
 */

public class LocationSingleton {
    private static LocationSingleton instance = new LocationSingleton();
    private LocationSingleton() { }

    public static LocationSingleton getInstance() {
        return instance;
    }

    private Location[] locations = {
            null,
            new Location("종로구", 37.594635, 126.977003, 1),
            new Location("중구", 37.559915, 126.995768, 2),
            new Location("용산구", 37.531353, 126.980069, 3),
            new Location("성동구", 37.550887, 127.040978, 4),
            new Location("광진구", 37.545722, 127.086282, 5),
            new Location("동대문구", 37.581960, 127.055224, 6),
            new Location("중랑구", 37.598017, 127.093276, 7),
            new Location("성북구", 37.605968, 127.017906, 8),
            new Location("강북구", 37.643560, 127.011338, 9),
            new Location("도봉구", 37.669208, 127.032374, 10),
            new Location("노원구", 37.652592, 127.075298, 11),
            new Location("은평구", 37.619228, 126.927277, 12),
            new Location("서대문구", 37.577865, 126.939134, 13),
            new Location("마포구", 37.559456, 126.908403, 14),
            new Location("양천구", 37.524907, 126.855393, 15),
            new Location("강서구", 37.560752, 126.823016, 16),
            new Location("구로구", 37.494510, 126.856754, 17),
            new Location("금천구", 37.460516, 126.901018, 18),
            new Location("영등포구", 37.522446, 126.910451, 19),
            new Location("동작구", 37.498708, 126.951391, 20),
            new Location("관악구", 37.467277, 126.945127, 21),
            new Location("서초구", 37.472978, 127.030905, 22),
            new Location("강남구", 37.496105, 127.063491, 23),
            new Location("송파구", 37.504731, 127.116020, 24),
            new Location("강동구", 37.550170, 127.147436, 25)
    };

    public Location[] getLocations() {
        return locations;
    }
    public Location getLocation(int id) {
        return locations[id];
    }
}
