package com.jere.forum.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * @author jere
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRotate = false;
    /**
     * 当前Activity渲染的视图View
     **/
    private View mContextView = null;

    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        initParams(bundle);
        View bindView = bindView();
        if (null == bindView) {
            mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        } else {
            mContextView = bindView;
        }

        //hide time, battery status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide app title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(mContextView);

        //bind ButterKnife
        ButterKnife.bind(this);

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        initView(mContextView);
        doBusiness(this);
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        // 透明状态栏 getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * [初始化参数] *
     *
     * @param params
     */
    public abstract void initParams(Bundle params);

    /**
     * [绑定视图] *
     *
     * @return
     */
    public abstract View bindView();

    /**
     * [绑定布局] * * @return
     */
    public abstract int bindLayout();

    /**
     * [初始化控件] *
     *
     * @param view
     */
    public abstract void initView(final View view);

    /**
     * [业务操作]
     *
     * @param mContext
     */
    public abstract void doBusiness(Context mContext);

    /**
     * [页面跳转]
     * * @param clz
     */
    public void startActivity(Class<?> targetClass) {
        startActivity(new Intent(BaseActivity.this, targetClass));
    }

    /**
     * [携带数据的页面跳转]
     */
    public void startActivity(Class<?> targetClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, targetClass);
        if (bundle != null) {
            intent.putExtras(bundle);

        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     * * @param tartgetClass
     *
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> tartgetClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, tartgetClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    protected void showToast(String msgContent) {
        Toast.makeText(this, msgContent, Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public void showInputMethod() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

}
