package com.jere.forum.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jere.forum.App;
import com.jere.forum.R;
import com.jere.forum.chats.model.ChatsListItemBean;
import com.jere.forum.utils.IAdapterItemClickListener;
import com.jere.forum.utils.OnClickEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jere
 */
public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.MyViewHolder> {
    private ArrayList<ChatsListItemBean.DataBean> chatsListData;
    private WeakReference<Context> weakReference;
    private IAdapterItemClickListener clickListener;

    public ChatsListAdapter(Context context, ArrayList<ChatsListItemBean.DataBean> chatsListData, IAdapterItemClickListener iAdapterItemClickListener) {
        this.weakReference = new WeakReference<>(context);
        this.chatsListData = chatsListData;
        this.clickListener = iAdapterItemClickListener;
    }

    public void setData(ArrayList<ChatsListItemBean.DataBean> chatsListData) {
        this.chatsListData = chatsListData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_view_chats_item, parent, false);
        return new MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatsListItemBean.DataBean data = chatsListData.get(position);

        Context context = weakReference.get();
        if (context != null) {
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
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
                    App.getInstance().getDaoSession().getChatsListItemEntityDao().update(data.convertToEntity());
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return chatsListData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private IAdapterItemClickListener clickListener;
        @BindView(R.id.chats_item_container_cl)
        ConstraintLayout chatsItemContainerCl;
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

        public MyViewHolder(@NonNull View itemView, IAdapterItemClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            ButterKnife.bind(this, itemView);

            chatsItemContainerCl.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onPositionClicked(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onPositionClicked(v, getAdapterPosition());
            return true;
        }
    }
}
