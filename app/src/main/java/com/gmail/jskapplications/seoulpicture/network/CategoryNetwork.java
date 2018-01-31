package com.gmail.jskapplications.seoulpicture.network;

import java.util.ArrayList;

/**
 * Created by for on 2017-10-15.
 */

public class CategoryNetwork {

    private static CategoryNetwork instance = new CategoryNetwork();
    private CategoryNetwork() { }

    public static CategoryNetwork getInstance() {
        return instance;
    }

    private String[] category = {
            "전체",
             "풍경",
            "음식",
            "유적지",
            "기타"
    };
    private String[] categoryUpload = {
            "선택",
            "풍경",
            "음식",
            "유적지",
            "기타"
    };

    public String[] getCategory() {
        return category;
    }
    public String getCat(int id) {
        return category[id];
    }
    public String[] getCategoryUpload() {
        return categoryUpload;
    }
}
