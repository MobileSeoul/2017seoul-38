package com.gmail.jskapplications.seoulpicture.main.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by culturetech on 2016-04-08.
 * 박물관 Main 화면의 소식을 위한 ViewPagerAdapter
 *
 */
public class NoticePagerViewAdapter extends PagerAdapter {

    private Context context;
    private String[] notice;

    public NoticePagerViewAdapter(Context c, String[] notice){
        context = c;
        this.notice = notice;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return notice.length;
    }

    @Override
    public Object instantiateItem(ViewGroup pager, final int position) {
        ImageView image = new ImageView(context);
        Picasso.with(context).load(notice[position]).into(image);

        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //페이저 item 클릭 이벤트
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*
                Intent intent = new Intent(context, MuseumInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("wantMainPage", 2);
                intent.putExtra("wantExpandPosition", position);
                //Log.d(getClass().getName(), position + "");
                context.startActivity(intent);
                */
            }
        });
        pager.addView(image, 0);
        return image;
    }

    public void destroyItem(ViewGroup pager, int position, Object view) {
        pager.removeView((View) view);
    }
}
