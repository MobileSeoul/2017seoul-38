package com.gmail.jskapplications.seoulpicture.network;

import java.util.ArrayList;

/**
 * Created by for on 2017-10-15.
 */

public class LocationNetwork {

    private static LocationNetwork instance = new LocationNetwork();
    private LocationNetwork() { }

    public static LocationNetwork getInstance() {
        return instance;
    }

    private String[] locations = {
            "전체",
            "종로구",
            "중구",
            "용산구",
            "성동구",
            "광진구",
            "동대문구",
            "중랑구",
            "성북구",
            "강북구",
            "도봉구",
            "노원구",
            "은평구",
            "서대문구",
            "마포구",
            "양천구",
            "강서구",
            "구로구",
            "금천구",
            "영등포구",
            "동작구",
            "관악구",
            "서초구",
            "강남구",
            "송파구",
            "강동구"
    };

    private String[] locationsUpload = {
            "선택",
            "종로구",
            "중구",
            "용산구",
            "성동구",
            "광진구",
            "동대문구",
            "중랑구",
            "성북구",
            "강북구",
            "도봉구",
            "노원구",
            "은평구",
            "서대문구",
            "마포구",
            "양천구",
            "강서구",
            "구로구",
            "금천구",
            "영등포구",
            "동작구",
            "관악구",
            "서초구",
            "강남구",
            "송파구",
            "강동구"
    };

    public String[] getLocations() {
        return locations;
    }
    public String getLocation(int id) {
        return locations[id];
    }
    public String[] getLocationsUpload() {
        return locationsUpload;
    }
}
