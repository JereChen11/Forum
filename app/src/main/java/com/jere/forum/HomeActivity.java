package com.jere.forum;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jere.forum.base.BaseActivity;
import com.jere.forum.chats.ChatsFragment;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.entity.ChatsListItemEntity;
import com.jere.forum.entity.ChatsListItemEntityDao;
import com.jere.forum.entity.DaoSession;
import com.jere.forum.me.MeFragment;
import com.jere.forum.utils.ParseLocalJsonFileThread;
import com.jere.forum.utils.Settings;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author jere
 */
public class HomeActivity extends BaseActivity {


    @BindView(R.id.chats_btn)
    Button chatsBtn;
    @BindView(R.id.me_btn)
    Button meBtn;
    @BindView(R.id.home_bottom_bar_container_ll)
    LinearLayout homeBottomBarContainerLl;

    private ChatsListItemEntityDao chatsListItemEntityDao;
    private Query<ChatsListItemEntity> chatsListItemEntityQuery;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initView(View view) {
        replaceFragment(new ChatsFragment());
    }

    @Override
    public void doBusiness(Context mContext) {

        if (!Settings.getInstance().getIsInitChatsDb()) {
            DaoSession daoSession = ((App) getApplication()).getDaoSession();
            chatsListItemEntityDao = daoSession.getChatsListItemEntityDao();

            // query all notes, sorted a-z by their text
            chatsListItemEntityQuery = chatsListItemEntityDao.queryBuilder().build();
            List<ChatsListItemEntity> chatsListItemEntityList = chatsListItemEntityQuery.list();
            Log.e(TAG, "doBusiness: chatsListItemEntityList.size() = " + chatsListItemEntityList.size());

            new ParseLocalJsonFileThread(this, ParseLocalJsonFileThread.CHATS_LIST_LOCAL_JSON_FILE_NAME, jsonFileString -> {
                Gson gson = new Gson();
                ChatsListItemBean chatsListItemBean = gson.fromJson(jsonFileString, ChatsListItemBean.class);
                ArrayList<ChatsListItemBean.DataBean> chatsListItemDataList = chatsListItemBean.getData();

                for (ChatsListItemBean.DataBean dataBean : chatsListItemDataList) {
                    chatsListItemEntityDao.insertOrReplace(dataBean.convertToEntity());
                }
                Settings.getInstance().setIsInitChatsDb(true);
            }).start();
        }
    }


    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, newFragment);
        ft.commit();
    }

    @OnClick({R.id.chats_btn, R.id.me_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chats_btn:
                replaceFragment(new ChatsFragment());
                break;
            case R.id.me_btn:
                replaceFragment(new MeFragment());
                break;
            default:
                break;
        }
    }
    
}
