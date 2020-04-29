package com.jere.forum.chats;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jere.forum.App;
import com.jere.forum.R;
import com.jere.forum.base.BaseActivity;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.entity.ChatsListItemEntity;
import com.jere.forum.entity.ChatsListItemEntityDao;
import com.jere.forum.entity.DaoSession;
import com.jere.forum.utils.OnClickEvent;
import com.jere.forum.utils.Settings;

import org.greenrobot.greendao.query.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;

/**
 * @author jere
 */
public class NewTopicActivity extends BaseActivity {
    @BindView(R.id.done_tv)
    TextView doneTv;
    @BindView(R.id.input_title_et)
    EditText inputTitleEt;
    @BindView(R.id.input_content_et)
    EditText inputContentEt;

    private ChatsListItemEntityDao chatsListItemEntityDao;
    private ArrayList<ChatsListItemBean.DataBean> mChatsListData = new ArrayList<>();

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_new_topic;
    }

    @Override
    public void initView(View view) {
        doneTv.setOnClickListener(new OnClickEvent() {
            @Override
            public void onSingleClick(View v) {
                String title = inputTitleEt.getText().toString();
                String content = inputContentEt.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    showToast("Please input title and content info.");
                } else {
                    //todo insert to db
                    ChatsListItemBean.DataBean chatsListItemBean = new ChatsListItemBean.DataBean();
                    chatsListItemBean.setTitle(title);
                    chatsListItemBean.setContent(content);
                    chatsListItemBean.setIsMyTopic(true);
                    chatsListItemBean.setIsLike(false);
                    chatsListItemBean.setLikeNumber(0);
                    chatsListItemBean.setViewNumber(1);
                    int randomNum = ThreadLocalRandom.current().nextInt(5, 15);
                    chatsListItemBean.setPortrait("http://jerechen.gitee.io/imageweb/portraitImage/head" + randomNum + ".jpg");
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(c);
                    chatsListItemBean.setDate(formattedDate);
                    chatsListItemBean.setAuthor(Settings.getInstance(NewTopicActivity.this).getNickname());
                    chatsListItemBean.setId((long) (mChatsListData.size() + 1));
                    chatsListItemEntityDao.insert(chatsListItemBean.convertToEntity());
                    finish();
                }
            }
        });

    }

    @Override
    public void doBusiness(Context mContext) {
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        chatsListItemEntityDao = daoSession.getChatsListItemEntityDao();
        // query all notes, sorted a-z by their text
        Query<ChatsListItemEntity> chatsListItemEntityQuery = chatsListItemEntityDao.queryBuilder().build();
        List<ChatsListItemEntity> chatsListItemEntityList = chatsListItemEntityQuery.list();
        mChatsListData = new ArrayList<>();
        for (ChatsListItemEntity entity : chatsListItemEntityList) {
            mChatsListData.add(entity.convertToBean());
        }
    }

}
