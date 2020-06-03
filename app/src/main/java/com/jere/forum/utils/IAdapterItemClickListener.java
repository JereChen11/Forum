package com.jere.forum.utils;

import android.view.View;

/**
 * @author jere
 */
public interface IAdapterItemClickListener {
    /**
     * 单击
     * @param v
     * @param position
     */
    void onPositionClicked(View v, int position);

    /**
     * 长按
     * @param v
     * @param position
     */
    void onLongClicked(View v, int position);
}
