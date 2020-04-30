package com.jere.forum.me;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jere.forum.R;
import com.jere.forum.base.BaseFragment;
import com.jere.forum.utils.MeListItemCustomView;
import com.jere.forum.utils.Settings;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author jere
 */
public class MeFragment extends BaseFragment {

    @BindView(R.id.portrait_iv)
    ImageView portraitIv;
    @BindView(R.id.nick_name_tv)
    TextView nickNameTv;
    @BindView(R.id.settings_iv)
    ImageView settingsIv;
    @BindView(R.id.divide_line_view)
    View divideLineView;
    @BindView(R.id.my_chats_item)
    MeListItemCustomView myChatsItem;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void setUpView() {
        String avatarUriString = Settings.getInstance(getContext()).getAvatarUrl();
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        if (!TextUtils.isEmpty(avatarUriString)) {
            Glide.with(this).load(Uri.parse(avatarUriString)).apply(requestOptions).into(portraitIv);
        } else {
            Glide.with(this).load(R.drawable.portrait_icon).apply(requestOptions).into(portraitIv);
        }
        nickNameTv.setText(Settings.getInstance(getContext()).getNickname());
    }

    @Override
    protected void setUpData() {

    }

    @OnClick({R.id.portrait_iv, R.id.nick_name_tv, R.id.settings_iv, R.id.my_chats_item})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.portrait_iv:
            case R.id.nick_name_tv:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.settings_iv:
                break;
            case R.id.my_chats_item:
                startActivity(new Intent(getActivity(), MyChartActivity.class));
                break;
            default:
                break;
        }
    }
}
