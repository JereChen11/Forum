package com.jere.forum.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jere.forum.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author jere
 */
public class MeListItemCustomView extends ConstraintLayout {

    public MeListItemCustomView(Context context) {
        this(context, null);
    }

    public MeListItemCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeListItemCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        inflate(context, R.layout.custom_view_me_list_item, this);
        TextView titleTv = findViewById(R.id.me_list_item_title_text);
        ImageView iconIv = findViewById(R.id.icon_iv);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeListItemCustomView);
        String titleTextString = typedArray.getString(R.styleable.MeListItemCustomView_meListItemTitleText);
        titleTv.setText(titleTextString);

        int iconResourceId = typedArray.getResourceId(R.styleable.MeListItemCustomView_meListIconReference, R.drawable.ic_launcher_background);
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true);//不做内存缓存
        Glide.with(this).load(iconResourceId).apply(requestOptions).into(iconIv);

        typedArray.recycle();
    }
}
