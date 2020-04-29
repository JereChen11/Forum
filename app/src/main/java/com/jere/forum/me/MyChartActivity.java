package com.jere.forum.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.jere.forum.base.BaseActivity;
import com.jere.forum.chats.RecyclerItemClickListener;
import com.jere.forum.chats.TopicDetailActivity;
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
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    private MyChatsListAdapter mAdapter;

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
        mAdapter = new MyChatsListAdapter(this, mChatsListData);
        myChatsRcy.setAdapter(mAdapter);

        myChatsRcy.addOnItemTouchListener(new RecyclerItemClickListener(this, myChatsRcy, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
            public void onLongItemClick(View view, int position) {

            }
        }));
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


    public static class MyChatsListAdapter extends RecyclerView.Adapter<MyChatsListAdapter.MyViewHolder> {
        private ArrayList<ChatsListItemBean.DataBean> chatsListData;
        private WeakReference<Context> weakReference;

        public MyChatsListAdapter(Context context, ArrayList<ChatsListItemBean.DataBean> chatsListData) {
            this.weakReference = new WeakReference<>(context);
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

            Context context =  weakReference.get();
            if (context != null) {
                RequestOptions requestOptions = RequestOptions.circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);
                Glide.with(context).load(data.getPortrait()).apply(requestOptions).into(holder.portraitIv);
                holder.nameTv.setText(data.getAuthor());
                holder.publishTimeTv.setText(data.getDate());
                holder.topicTitleTv.setText(data.getTitle());
                holder.likeNumberTv.setText(String.valueOf(data.getLikeNumber()));
                holder.viewNumberTv.setText(String.valueOf(data.getViewNumber()));
                if (data.isIsLike()) {
                    Glide.with(context).load(R.drawable.give_a_like_icon).into(holder.likeIv);
                } else {
                    Glide.with(context).load(R.drawable.give_a_unlike_icon).into(holder.likeIv);
                }
                holder.likeIv.setOnClickListener(new OnClickEvent() {
                    @Override
                    public void onSingleClick(View v) {
                        if (data.isIsLike()) {
                            Glide.with(context).load(R.drawable.give_a_unlike_icon).into(holder.likeIv);
                            data.setIsLike(false);
                            int newLikeNumber = data.getLikeNumber() - 1;
                            holder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                            data.setLikeNumber(newLikeNumber);
                        } else {
                            Glide.with(context).load(R.drawable.give_a_like_icon).into(holder.likeIv);
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
