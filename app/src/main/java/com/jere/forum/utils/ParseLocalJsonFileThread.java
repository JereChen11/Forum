package com.jere.forum.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author jere
 */
public class ParseLocalJsonFileThread extends Thread {
    public static final String CHATS_LIST_LOCAL_JSON_FILE_NAME = "chatsList.json";
    private Context mContext;
    private ParseLocalJsonFileListener listener;
    private String mJsonFileName;

    public ParseLocalJsonFileThread(Context context, String jsonFileName, ParseLocalJsonFileListener listener) {
        this.mContext = context.getApplicationContext();
        this.listener = listener;
        this.mJsonFileName = jsonFileName;
    }

    @Override
    public void run() {
        super.run();
        String jsonFileString = getJson(mContext, mJsonFileName);
        listener.finish(jsonFileString);
    }

    private String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public interface ParseLocalJsonFileListener {
        /**
         * finish parse work
         * @param responseBody
         */
        void finish(String responseBody);
    }


}
