package com.jere.forum.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jere.forum.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author jere
 */
public class TitleBarCustomView extends ConstraintLayout {

    private TextView titleTv;

    public TitleBarCustomView(Context context) {
        this(context, null);
    }

    public TitleBarCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarCustomView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.custom_view_title_bar, this);

        ImageView backIv = findViewById(R.id.back_iv);
        titleTv = findViewById(R.id.title_bar_title_tv);
        TextView doneTv = findViewById(R.id.title_bar_done_tv);
        backIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) v.getContext()).onBackPressed();
            }
        });

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBarCustomView);
        String titleString = typedArray.getString(R.styleable.TitleBarCustomView_title);
        titleTv.setText(titleString);
        boolean isShowDoneTv = typedArray.getBoolean(R.styleable.TitleBarCustomView_isShowDoneBtn, false);
        if (isShowDoneTv) {
            doneTv.setVisibility(VISIBLE);
        } else {
            doneTv.setVisibility(GONE);
        }

        typedArray.recycle();
    }

    public void setTitle(String newTitle) {
        titleTv.setText(newTitle);
        invalidate();
    }
}
