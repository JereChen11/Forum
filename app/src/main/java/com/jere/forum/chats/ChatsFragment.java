package com.jere.forum.chats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jere.forum.App;
import com.jere.forum.R;
import com.jere.forum.base.BaseFragment;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.entity.ChatsListItemEntity;
import com.jere.forum.entity.ChatsListItemEntityDao;
import com.jere.forum.entity.DaoSession;
import com.jere.forum.utils.IAdapterItemClickListener;
import com.jere.forum.utils.OnClickEvent;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * @author jere
 */
public class ChatsFragment extends BaseFragment {

    public static final String TOPIC_DETAIL_KEY = "topicDetail";

    @BindView(R.id.new_topic_tv)
    TextView newTopicTv;
    @BindView(R.id.chats_rcy)
    RecyclerView chatsRcy;

    //    private MyHandler mHandler;
    private ArrayList<ChatsListItemBean.DataBean> mChatsListData = new ArrayList<>();
    private ChatsListItemEntityDao chatsListItemEntityDao;
    private ChatsListAdapter mAdapter;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_chats;
    }

    @Override
    protected void setUpView() {
        newTopicTv.setOnClickListener(new OnClickEvent() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getActivity(), NewTopicActivity.class));
            }
        });

    }

    @Override
    protected void setUpData() {
        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        chatsListItemEntityDao = daoSession.getChatsListItemEntityDao();
        // query all notes, sorted a-z by their text
        Query<ChatsListItemEntity> chatsListItemEntityQuery = chatsListItemEntityDao.queryBuilder().build();
        List<ChatsListItemEntity> chatsListItemEntityList = chatsListItemEntityQuery.list();
        mChatsListData = new ArrayList<>();
        for (ChatsListItemEntity entity: chatsListItemEntityList) {
            mChatsListData.add(entity.convertToBean());
        }

        mAdapter = new ChatsListAdapter(getContext(), mChatsListData, new IAdapterItemClickListener() {
            @Override
            public void onPositionClicked(View v, int position) {
                ChatsListItemBean.DataBean dataBean = mChatsListData.get(position);
                int newViewNumber = dataBean.getViewNumber() + 1;
                dataBean.setViewNumber(newViewNumber);
                Intent intent = new Intent(getActivity(), TopicDetailActivity.class);
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
        chatsRcy.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Query<ChatsListItemEntity> chatsListItemEntityQuery = chatsListItemEntityDao.queryBuilder().build();
        List<ChatsListItemEntity> chatsListItemEntityList = chatsListItemEntityQuery.list();
        mChatsListData = new ArrayList<>();
        for (ChatsListItemEntity entity: chatsListItemEntityList) {
            mChatsListData.add(entity.convertToBean());
        }
        Log.e(TAG, "onResume: mChatsListData.size() = " + mChatsListData.size());
        mAdapter.setData(mChatsListData);
    }

}
