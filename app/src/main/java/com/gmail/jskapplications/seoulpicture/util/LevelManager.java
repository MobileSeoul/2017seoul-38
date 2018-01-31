package com.gmail.jskapplications.seoulpicture.util;

import com.gmail.jskapplications.seoulpicture.model.User;

/**
 * Created by for on 2017-10-28.
 */

public class LevelManager {

    /*
    private static LevelManager instance = new LevelManager();
    private LevelManager() { }

    public static LevelManager getInstance() {
        return instance;
    }
    */
    public static int getLevel(int total_likes) {

        if(total_likes == 0) {
            return 1;
        } else {
            return (int)Math.ceil(total_likes/5.0);
        }
    }
}
