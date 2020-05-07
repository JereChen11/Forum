package com.jere.forum.chats;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    @BindView(R.id.topic_detail_divide_line_view)
    View topicDetailDivideLineView;
    @BindView(R.id.topic_comments_et)
    EditText topicCommentsEt;
    @BindView(R.id.send_btn)
    Button sendBtn;
    @BindView(R.id.comment_container_cl)
    ConstraintLayout commentContainerCl;
    @BindView(R.id.topic_detail_rcy)
    RecyclerView topicDetailRcy;

    private ChatsListItemBean.DataBean mChatsDetailData;
    private ChatsListItemEntityDao chatsListItemEntityDao;
    private ChatsCommentListItemEntityDao chatsCommentListItemEntityDao;
    private ArrayList<ChatsCommentListItemBean.DataBean> chatsCommentListBeanData;
    private TopicDetailAdapter mAdapter;
    private List<ArrayList> detailData = new ArrayList<>();

    @Override
    public void initParams(Bundle params) {
        mChatsDetailData = (ChatsListItemBean.DataBean) params.get(ChatsFragment.TOPIC_DETAIL_KEY);
        ArrayList<ChatsListItemBean.DataBean> list = new ArrayList<>();
        list.add(mChatsDetailData);
        detailData.add(list);
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
        detailData.add(chatsCommentListBeanData);
        mAdapter = new TopicDetailAdapter(this, detailData);
        topicDetailRcy.setAdapter(mAdapter);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        for (ChatsCommentListItemEntity entity : chatsCommentListItemEntities) {
            chatsCommentListBeanData.add(entity.convertToBean());
        }
    }

    @OnClick({R.id.send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_btn:
                if (TextUtils.isEmpty(topicCommentsEt.getText().toString())) {
                    showToast("Please input content~");
                    break;
                }
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
                commentListItemBean.setContent(topicCommentsEt.getText().toString());
                commentListItemBean.setId((long) (chatsCommentListBeanData.size() + 1));
                chatsCommentListBeanData.add(commentListItemBean);
                detailData.set(1, chatsCommentListBeanData);
                mAdapter.setData(detailData);
                //scroll to recyclerView bottom and clear EditText
                topicDetailRcy.smoothScrollToPosition(chatsCommentListBeanData.size() + 1);
                topicCommentsEt.clearFocus();
                topicCommentsEt.setText("");
                chatsCommentListItemEntityDao.insertOrReplace(commentListItemBean.convertToEntity());

                hideSoftInput();
                break;
            default:
                break;
        }
    }

    public class TopicDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private WeakReference<Context> weakReference;
        private static final int DETAIL = 0;
        private static final int COMMENTS = 1;
        private List<ArrayList> listData;

        TopicDetailAdapter(Context context, List<ArrayList> listDat) {
            this.weakReference = new WeakReference<>(context);
            this.listData = listDat;
        }

        void setData(List<ArrayList> listData) {
            this.listData = listData;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return DETAIL;
            } else {
                return COMMENTS;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view;
            switch (viewType) {
                case 0:
                    view = layoutInflater.inflate(R.layout.recycler_list_item_view_topic_detail_item, parent, false);
                    return new TopicDetailViewHolder(view);
                case 1:
                    view = layoutInflater.inflate(R.layout.recycler_list_item_view_topic_comment_item, parent, false);
                    return new CommentsViewHolder(view);
                default:
                    break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Context context = weakReference.get();
            if (holder instanceof TopicDetailViewHolder) {
                if (context != null) {
                    TopicDetailViewHolder topicDetailViewHolder = (TopicDetailViewHolder) holder;
                    ArrayList<ChatsListItemBean.DataBean> chatsCommentListBeanData;
                    chatsCommentListBeanData = listData.get(0);
                    ChatsListItemBean.DataBean dataBean = chatsCommentListBeanData.get(0);

                    RequestOptions requestOptions = RequestOptions.circleCropTransform();
                    Glide.with(context).load(dataBean.getPortrait()).apply(requestOptions).into(topicDetailViewHolder.portraitIv);
                    topicDetailViewHolder.nameTv.setText(dataBean.getAuthor());
                    topicDetailViewHolder.publishTimeTv.setText(dataBean.getDate());
                    topicDetailViewHolder.topicTitleTv.setText(dataBean.getTitle());
                    topicDetailViewHolder.topicContentTv.setText(dataBean.getContent());
                    topicDetailViewHolder.likeNumberTv.setText(String.valueOf(dataBean.getLikeNumber()));
                    if (dataBean.isIsLike()) {
                        Glide.with(context).load(R.drawable.give_a_like_icon).into(topicDetailViewHolder.likeIv);
                    } else {
                        Glide.with(context).load(R.drawable.give_a_unlike_icon).into(topicDetailViewHolder.likeIv);
                    }

                    topicDetailViewHolder.likeIv.setOnClickListener(v -> {
                        if (dataBean.isIsLike()) {
                            Glide.with(context).load(R.drawable.give_a_unlike_icon).into(topicDetailViewHolder.likeIv);
                            dataBean.setIsLike(false);
                            int newLikeNumber = dataBean.getLikeNumber() - 1;
                            topicDetailViewHolder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                            dataBean.setLikeNumber(newLikeNumber);
                        } else {
                            Glide.with(context).load(R.drawable.give_a_like_icon).into(topicDetailViewHolder.likeIv);
                            dataBean.setIsLike(true);
                            int newLikeNumber = dataBean.getLikeNumber() + 1;
                            topicDetailViewHolder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                            dataBean.setLikeNumber(newLikeNumber);
                        }

                        chatsListItemEntityDao.update(dataBean.convertToEntity());
                    });
                }

            }

            if (holder instanceof CommentsViewHolder) {
                if (context != null) {
                    CommentsViewHolder commentsViewHolder = (CommentsViewHolder) holder;
                    ArrayList<ChatsCommentListItemBean.DataBean> chatsCommentListData;
                    chatsCommentListData = listData.get(1);
                    ChatsCommentListItemBean.DataBean dataBean = chatsCommentListData.get(position - 1);
                    RequestOptions requestOptions = RequestOptions.circleCropTransform();

                    Glide.with(context).load(dataBean.getPortrait()).apply(requestOptions).into(commentsViewHolder.portraitIv);
                    commentsViewHolder.nameTv.setText(dataBean.getAuthor());
                    commentsViewHolder.publishTimeTv.setText(dataBean.getDate());
                    commentsViewHolder.topicContentTv.setText(dataBean.getContent());
                    commentsViewHolder.likeNumberTv.setText(String.valueOf(dataBean.getLikeNumber()));
                    if (dataBean.isIsLike()) {
                        Glide.with(context).load(R.drawable.give_a_like_icon).into(commentsViewHolder.likeIv);
                    } else {
                        Glide.with(context).load(R.drawable.give_a_unlike_icon).into(commentsViewHolder.likeIv);
                    }
                    commentsViewHolder.likeIv.setOnClickListener(new OnClickEvent() {
                        @Override
                        public void onSingleClick(View v) {
                            if (dataBean.isIsLike()) {
                                Glide.with(context).load(R.drawable.give_a_unlike_icon).into(commentsViewHolder.likeIv);
                                dataBean.setIsLike(false);
                                int newLikeNumber = dataBean.getLikeNumber() - 1;
                                commentsViewHolder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                                dataBean.setLikeNumber(newLikeNumber);
                            } else {
                                Glide.with(context).load(R.drawable.give_a_like_icon).into(commentsViewHolder.likeIv);
                                dataBean.setIsLike(true);
                                int newLikeNumber = dataBean.getLikeNumber() + 1;
                                commentsViewHolder.likeNumberTv.setText(String.valueOf(newLikeNumber));
                                dataBean.setLikeNumber(newLikeNumber);
                            }

                            chatsCommentListItemEntityDao.update(dataBean.convertToEntity());
                        }
                    });

                }
            }
        }

        @Override
        public int getItemCount() {
            int size = 0;
            for (ArrayList list : listData) {
                size += list.size();
            }
            return size;
        }

        class CommentsViewHolder extends RecyclerView.ViewHolder {
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

            CommentsViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        class TopicDetailViewHolder extends RecyclerView.ViewHolder {
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
            @BindView(R.id.comments_tv)
            TextView commentsTv;

            TopicDetailViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
