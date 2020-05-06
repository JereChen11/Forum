package com.jere.forum.chats;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jere.forum.App;
import com.jere.forum.R;
import com.jere.forum.base.BaseActivity;
import com.jere.forum.chats.model.ChatsCommentListItemBean;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.entity.ChatsCommentListItemEntity;
import com.jere.forum.entity.ChatsCommentListItemEntityDao;
import com.jere.forum.entity.ChatsListItemEntity;
import com.jere.forum.entity.ChatsListItemEntityDao;
import com.jere.forum.entity.DaoSession;
import com.jere.forum.utils.OnClickEvent;
import com.jere.forum.utils.Settings;

import org.greenrobot.greendao.query.Query;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author jere
 */
public class TopicDetailActivity extends BaseActivity {

    @BindView(R.id.reply_tv)
    TextView replyTv;
    @BindView(R.id.portrait_iv)
    ImageView portraitIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.publish_time_tv)
    TextView publishTimeTv;
    @BindView(R.id.topic_title_tv)
    TextView topicTitleTv;
    @BindView(R.id.topic_content_tv)
    TextView topicContentTv;
    @BindView(R.id.like_iv)
    ImageView likeIv;
    @BindView(R.id.like_number_tv)
    TextView likeNumberTv;
    @BindView(R.id.topic_content_container_cl)
    ConstraintLayout topicContentContainerCl;
    @BindView(R.id.replies_tv)
    TextView repliesTv;
    @BindView(R.id.topic_comments_rcy)
    RecyclerView topicCommentsRcy;
    @BindView(R.id.topic_comments_et)
    EditText topicCommentsEt;
    @BindView(R.id.topic_no_comments_tv)
    TextView topicNoCommentsTv;

    private ChatsListItemBean.DataBean mChatsDetailData;
    private ChatsListItemEntityDao chatsListItemEntityDao;
    private ChatsCommentListItemEntityDao chatsCommentListItemEntityDao;
    private ArrayList<ChatsCommentListItemBean.DataBean> chatsCommentListBeanData;
    private TopicDetailCommentsListAdapter mAdapter;

    @Override
    public void initParams(Bundle params) {
        mChatsDetailData = (ChatsListItemBean.DataBean) params.get(ChatsFragment.TOPIC_DETAIL_KEY);
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void initView(View view) {
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        chatsListItemEntityDao = daoSession.getChatsListItemEntityDao();
        chatsCommentListItemEntityDao = daoSession.getChatsCommentListItemEntityDao();

        queryChatsListItemTable();
        mAdapter = new TopicDetailCommentsListAdapter(this, chatsCommentListBeanData);
        topicCommentsRcy.setAdapter(mAdapter);

        if (chatsCommentListBeanData.size() > 0) {
            topicNoCommentsTv.setVisibility(View.GONE);
        }

        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        Glide.with(this).load(mChatsDetailData.getPortrait()).apply(requestOptions).into(portraitIv);
        nameTv.setText(mChatsDetailData.getAuthor());
        publishTimeTv.setText(mChatsDetailData.getDate());
        topicTitleTv.setText(mChatsDetailData.getTitle());
        topicContentTv.setText(mChatsDetailData.getContent());
        likeNumberTv.setText(String.valueOf(mChatsDetailData.getLikeNumber()));
        if (mChatsDetailData.isIsLike()) {
            Glide.with(this).load(R.drawable.give_a_like_icon).into(likeIv);
        } else {
            Glide.with(this).load(R.drawable.give_a_unlike_icon).into(likeIv);
        }

        topicCommentsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (topicCommentsEt.getText().toString().length() > 10) {
                    showToast("please limit content on 50 char.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        queryChatsListItemTable();
        mAdapter.setData(chatsCommentListBeanData);
    }

    private void queryChatsListItemTable() {
        Query<ChatsListItemEntity> chatsListItemEntityQuery = chatsListItemEntityDao
                .queryBuilder()
                .where(ChatsListItemEntityDao.Properties.Id.eq(mChatsDetailData.getId()))
                .build();
        List<ChatsListItemEntity> chatsListItemEntityList = chatsListItemEntityQuery.list();
        ChatsListItemEntity chatsListItemEntity = chatsListItemEntityList.get(0);
        List<ChatsCommentListItemEntity> chatsCommentListItemEntities = chatsListItemEntity.getChatComments();
        chatsCommentListBeanData = new ArrayList<>();
        for (ChatsCommentListItemEntity entity: chatsCommentListItemEntities) {
            chatsCommentListBeanData.add(entity.convertToBean());
        }
    }

    @OnClick({R.id.reply_tv, R.id.like_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reply_tv:
                showInputMethod();

                ChatsCommentListItemBean.DataBean commentListItemBean = new ChatsCommentListItemBean.DataBean();
                commentListItemBean.setChatsTopicId(mChatsDetailData.getId());
                commentListItemBean.setLikeNumber(0);
                commentListItemBean.setIsLike(false);
                int randomNum = ThreadLocalRandom.current().nextInt(5, 15);
                commentListItemBean.setPortrait("http://jerechen.gitee.io/imageweb/portraitImage/head" + randomNum + ".jpg");
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);
                commentListItemBean.setDate(formattedDate);
                commentListItemBean.setAuthor(Settings.getInstance().getNickname());
//                commentListItemBean.setContent(topicCommentsEt.getText().toString());
                commentListItemBean.setContent("new comments~");
                chatsCommentListItemEntityDao.insertOrReplace(commentListItemBean.convertToEntity());

                break;
            case R.id.like_iv:
                if (mChatsDetailData.isIsLike()) {
                    Glide.with(TopicDetailActivity.this).load(R.drawable.give_a_unlike_icon).into(likeIv);
                    mChatsDetailData.setIsLike(false);
                    int newLikeNumber = mChatsDetailData.getLikeNumber() - 1;
                    likeNumberTv.setText(String.valueOf(newLikeNumber));
                    mChatsDetailData.setLikeNumber(newLikeNumber);
                } else {
                    Glide.with(TopicDetailActivity.this).load(R.drawable.give_a_like_icon).into(likeIv);
                    mChatsDetailData.setIsLike(true);
                    int newLikeNumber = mChatsDetailData.getLikeNumber() + 1;
                    likeNumberTv.setText(String.valueOf(newLikeNumber));
                    mChatsDetailData.setLikeNumber(newLikeNumber);
                }

                chatsListItemEntityDao.update(mChatsDetailData.convertToEntity());

                break;
            default:
                break;
        }
    }

    public static class TopicDetailCommentsListAdapter extends RecyclerView.Adapter<TopicDetailCommentsListAdapter.MyViewHolder> {
        private ArrayList<ChatsCommentListItemBean.DataBean> chatsCommentListData;
        private WeakReference<Context> weakReference;

        public TopicDetailCommentsListAdapter(Context context, ArrayList<ChatsCommentListItemBean.DataBean> chatsCommentListData) {
            this.weakReference = new WeakReference<>(context);
            this.chatsCommentListData = chatsCommentListData;
        }

        public void setData(ArrayList<ChatsCommentListItemBean.DataBean> chatsCommentListData) {
            this.chatsCommentListData = chatsCommentListData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_view_topic_comment_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ChatsCommentListItemBean.DataBean data = chatsCommentListData.get(position);

            Context context = weakReference.get();
            if (context != null) {
                RequestOptions requestOptions = RequestOptions.circleCropTransform();
                Glide.with(context).load(data.getPortrait()).apply(requestOptions).into(holder.portraitIv);
                holder.nameTv.setText(data.getAuthor());
                holder.publishTimeTv.setText(data.getDate());
                holder.topicContentTv.setText(data.getContent());
                holder.likeNumberTv.setText(String.valueOf(data.getLikeNumber()));
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
            return chatsCommentListData.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.portrait_iv)
            ImageView portraitIv;
            @BindView(R.id.name_tv)
            TextView nameTv;
            @BindView(R.id.publish_time_tv)
            TextView publishTimeTv;
            @BindView(R.id.topic_content_tv)
            TextView topicContentTv;
            @BindView(R.id.like_iv)
            ImageView likeIv;
            @BindView(R.id.like_number_tv)
            TextView likeNumberTv;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
