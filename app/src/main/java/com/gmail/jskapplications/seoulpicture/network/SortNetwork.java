package com.gmail.jskapplications.seoulpicture.network;

/**
 * Created by for on 2017-10-20.
 */

public class SortNetwork {

    private static SortNetwork instance = new SortNetwork();
    private SortNetwork() { }

    public static SortNetwork getInstance() {
        return instance;
    }

    private String[] sorts = {
            "최신순",
            "좋아요순"
    };


    public String[] getSorts() {
        return sorts;
    }
}
