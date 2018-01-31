package com.gmail.jskapplications.seoulpicture.main.home;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by culturetech on 2016-04-08.
 * 박물관 메인 화면의 소식을 위한 ViewPager
 */
public class  NoticeViewPager extends ViewPager {

    public NoticeViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    public NoticeViewPager(Context context, AttributeSet attr) {
        super(context, attr);
        setMyScroller();
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 500 /*0.5 secs*/);
        }
    }

}
