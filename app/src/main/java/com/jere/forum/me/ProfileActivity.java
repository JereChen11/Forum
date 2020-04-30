package com.jere.forum.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.OnClick;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jere.forum.R;
import com.jere.forum.base.BaseActivity;
import com.jere.forum.utils.PermissionsUtils;
import com.jere.forum.utils.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends BaseActivity implements PermissionsUtils.PermissionResultCallback {
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final int FROM_ALBUM_REQUEST_CODE = 2;

    @BindView(R.id.nickname_arrow)
    ImageView nicknameArrow;
    @BindView(R.id.nickname_detail_content_tv)
    TextView nicknameDetailContentTv;
    @BindView(R.id.nickname_container_cl)
    ConstraintLayout nicknameContainerCl;
    @BindView(R.id.avatar_arrow)
    ImageView avatarArrow;
    @BindView(R.id.avatar_picture_iv)
    ImageView avatarPictureIv;
    @BindView(R.id.avatar_container_cl)
    ConstraintLayout avatarContainerCl;

    private Uri imageUri;

    private boolean isPermissionGranted;
    /**
     * 权限申请工具
     */
    private PermissionsUtils mPermissionsUtils;


    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_profile;
    }

    @Override
    public void initView(View view) {
        nicknameDetailContentTv.setText(Settings.getInstance(this).getNickname());
        String avatarUriString = Settings.getInstance(this).getAvatarUrl();
        if (!TextUtils.isEmpty(avatarUriString)) {
            setAvatar(avatarUriString);
        } else {
            RequestOptions requestOptions = RequestOptions.circleCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                    .skipMemoryCache(true);//不做内存缓存
            Glide.with(this).load(R.drawable.portrait_icon).apply(requestOptions).into(avatarPictureIv);
        }
    }

    @Override
    public void doBusiness(Context mContext) {

    }


    @OnClick({R.id.nickname_arrow, R.id.nickname_detail_content_tv, R.id.avatar_arrow, R.id.avatar_picture_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nickname_arrow:
            case R.id.nickname_detail_content_tv:
                showChangeNicknameDialog();
                break;
            case R.id.avatar_arrow:
            case R.id.avatar_picture_iv:
                if (!isPermissionGranted) {
                    checkPermission();
                } else {
                    addAvatarAlertDialog();
                }
                break;
            default:
                break;
        }
    }

    private void checkPermission() {
        mPermissionsUtils = new PermissionsUtils(this);
        /**
         * 读取图片需要的权限
         */
        ArrayList<String> permissionsList = new ArrayList<>();
        permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsList.add(Manifest.permission.CAMERA);
        mPermissionsUtils.checkPermission(permissionsList, "need these permissions to upload Avatar！", 1);
    }

    private void showChangeNicknameDialog() {
        final EditText setNicknameEt = new EditText(this);
        setNicknameEt.setText(Settings.getInstance(this).getNickname());
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("please input new nickname!")
                .setView(setNicknameEt)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(setNicknameEt.getText().toString())) {
                            String newNickname = setNicknameEt.getText().toString();
                            Settings.getInstance(ProfileActivity.this).setNickname(newNickname);
                            nicknameDetailContentTv.setText(newNickname);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(ProfileActivity.this, "please input right nickname", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void addAvatarAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("please select the way to add avatar");
        // add a list
        final String[] itemArray = {"Camera", "Album"};
        builder.setItems(itemArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String element = itemArray[position];
                if (position == 0) {
                    cameraIntent();
                } else {
                    galleryIntent();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cameraIntent() {
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.jere.foreign_currency_new.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    setAvatar(imageUri.toString());
                    Settings.getInstance(this).setAvatarUrl(imageUri.toString());
                }
                break;
            case FROM_ALBUM_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    setAvatar(uri.toString());
                    Settings.getInstance(this).setAvatarUrl(uri.toString());
                }
            default:
                break;
        }
    }

    private void setAvatar(String avatarUriString) {
        RequestOptions requestOptions = RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true);//不做内存缓存
        Glide.with(this).load(Uri.parse(avatarUriString)).apply(requestOptions).into(avatarPictureIv);
    }

    @Override
    public void permissionGranted(int requestCode) {
        isPermissionGranted = true;
    }

    @Override
    public void partialPermissionGranted(int requestCode, ArrayList<String> grantedPermissions) {

    }

    @Override
    public void permissionDenied(int requestCode) {

    }

    @Override
    public void neverAskAgain(int requestCode) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
