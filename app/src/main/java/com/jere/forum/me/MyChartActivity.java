package com.jere.forum.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jere.forum.App;
import com.jere.forum.R;
import com.jere.forum.base.BaseActivity;
import com.jere.forum.chats.ChatsListAdapter;
import com.jere.forum.chats.TopicDetailActivity;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.entity.ChatsListItemEntity;
import com.jere.forum.entity.ChatsListItemEntityDao;
import com.jere.forum.entity.DaoSession;
import com.jere.forum.utils.IAdapterItemClickListener;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.jere.forum.chats.ChatsFragment.TOPIC_DETAIL_KEY;

/**
 * @author jere
 */
public class MyChartActivity extends BaseActivity {


    @BindView(R.id.no_favorite_item_tv)
    TextView noFavoriteItemTv;
    @BindView(R.id.my_chats_rcy)
    RecyclerView myChatsRcy;

    private ArrayList<ChatsListItemBean.DataBean> mChatsListData = new ArrayList<>();
    private ChatsListItemEntityDao chatsListItemEntityDao;
    private ChatsListAdapter mAdapter;

    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_my_chart;
    }

    @Override
    public void initView(View view) {
        queryChatsDb();
        if (mChatsListData.size() > 0) {
            noFavoriteItemTv.setVisibility(View.GONE);
        }
        mAdapter = new ChatsListAdapter(this, mChatsListData, new IAdapterItemClickListener() {
            @Override
            public void onPositionClicked(View v, int position) {
                ChatsListItemBean.DataBean dataBean = mChatsListData.get(position);
                int newViewNumber = dataBean.getViewNumber() + 1;
                dataBean.setViewNumber(newViewNumber);
                Intent intent = new Intent(MyChartActivity.this, TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TOPIC_DETAIL_KEY, dataBean);
                intent.putExtras(bundle);
                startActivity(intent);

                chatsListItemEntityDao.update(dataBean.convertToEntity());
            }

            @Override
            public void onLongClicked(View v, int position) {

            }
        });
        myChatsRcy.setAdapter(mAdapter);

    }

    @Override
    public void doBusiness(Context mContext) {
        queryChatsDb();
        mAdapter.setData(mChatsListData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryChatsDb();
        mAdapter.setData(mChatsListData);
    }

    private void queryChatsDb() {
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        chatsListItemEntityDao = daoSession.getChatsListItemEntityDao();
        // query all notes, sorted a-z by their text
        Query<ChatsListItemEntity> chatsListItemEntityQuery = chatsListItemEntityDao
                .queryBuilder()
                .where(ChatsListItemEntityDao.Properties.IsMyTopic.eq(true))
                .build();
        List<ChatsListItemEntity> chatsListItemEntityList = chatsListItemEntityQuery.list();
        mChatsListData = new ArrayList<>();
        for (ChatsListItemEntity entity: chatsListItemEntityList) {
            mChatsListData.add(entity.convertToBean());
        }
    }

}
