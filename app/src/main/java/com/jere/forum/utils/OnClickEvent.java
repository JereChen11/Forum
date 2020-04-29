package com.jere.forum.utils;

import android.view.View;

public abstract class OnClickEvent implements View.OnClickListener {

    private static long lastClickTime = 0;
    private boolean checkLogin = false;


    public OnClickEvent() {

    }

    /**
     * 如果对登录进行验证
     *
     * @param checkLogin
     */
    public OnClickEvent(boolean checkLogin) {
        this.checkLogin = checkLogin;
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastClickTime) > 200) {
            v.setEnabled(false);
            onSingleClick(v);
        }
        lastClickTime = currentTime;
        v.setEnabled(true);

    }

    public abstract void onSingleClick(View v);
}
