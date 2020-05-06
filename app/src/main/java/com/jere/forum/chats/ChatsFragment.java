package com.jere.forum.chats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jere.forum.App;
import com.jere.forum.R;
import com.jere.forum.base.BaseFragment;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.entity.ChatsListItemEntity;
import com.jere.forum.entity.ChatsListItemEntityDao;
import com.jere.forum.entity.DaoSession;
import com.jere.forum.utils.OnClickEvent;

import org.greenrobot.greendao.query.Query;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

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

        chatsRcy.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                chatsRcy,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
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
                    public void onLongItemClick(View view, int position) {

                    }
                }));

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
        mAdapter = new ChatsListAdapter(this, mChatsListData);
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


    public static class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.MyViewHolder> {
        private ArrayList<ChatsListItemBean.DataBean> chatsListData;
        private WeakReference<Fragment> weakReference;

        public ChatsListAdapter(Fragment fragment, ArrayList<ChatsListItemBean.DataBean> chatsListData) {
            this.weakReference = new WeakReference<>(fragment);
            this.chatsListData = chatsListData;
        }

        public void setData(ArrayList<ChatsListItemBean.DataBean> chatsListData) {
            this.chatsListData = chatsListData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_view_chats_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ChatsListItemBean.DataBean data = chatsListData.get(position);

            ChatsFragment fragment = (ChatsFragment) weakReference.get();
            if (fragment != null) {
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(fragment).load(data.getPortrait()).apply(requestOptions).into(holder.portraitIv);
                holder.nameTv.setText(data.getAuthor());
                holder.publishTimeTv.setText(data.getDate());
                holder.topicTitleTv.setText(data.getTitle());
                holder.likeNumberTv.setText(String.valueOf(data.getLikeNumber()));
                holder.viewNumberTv.setText(String.valueOf(data.getViewNumber()));
                if (data.isIsLike()) {
                    Glide.with(fragment).load(R.drawable.give_a_like_icon).into(holder.likeIv);
                } else {
                    Glide.with(fragment).load(R.drawable.give_a_unlike_icon).into(holder.likeIv);
                }
                holder.likeIv.setOnClickListener(new OnClickEvent() {
                    @Override
                    public void onSingleClick(View v) {
                        if (data.isIsLike()) {
                            Glide.with(fragment).load(R.drawable.give_a_unlike_icon).into(holder.likeIv);
                            data.setIsLike(false);
                            int newLikeNumber = data.getLikeNumber() - 1;
                            holder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                            data.setLikeNumber(newLikeNumber);
                        } else {
                            Glide.with(fragment).load(R.drawable.give_a_like_icon).into(holder.likeIv);
                            data.setIsLike(true);
                            int newLikeNumber = data.getLikeNumber() + 1;
                            holder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                            data.setLikeNumber(newLikeNumber);
                        }
                        //todo update db
                    }
                });

            }

        }

        @Override
        public int getItemCount() {
            return chatsListData.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.portrait_iv)
            ImageView portraitIv;
            @BindView(R.id.name_tv)
            TextView nameTv;
            @BindView(R.id.publish_time_tv)
            TextView publishTimeTv;
            @BindView(R.id.topic_title_tv)
            TextView topicTitleTv;
            @BindView(R.id.like_iv)
            ImageView likeIv;
            @BindView(R.id.like_number_tv)
            TextView likeNumberTv;
            @BindView(R.id.view_number_tv)
            TextView viewNumberTv;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
