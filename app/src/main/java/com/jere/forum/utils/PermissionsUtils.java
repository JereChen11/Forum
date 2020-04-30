package com.jere.forum.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author jere
 */
public class PermissionsUtils {

    private Context context;
    private Activity currentActivity;
    private PermissionResultCallback permissionResultCallback;
    private ArrayList<String> permissionList = new ArrayList<>();
    private ArrayList<String> listPermissionsNeeded = new ArrayList<>();
    private String dialogContent = "";
    private int requestCode;
    public PermissionsUtils(Context context) {
        this.context = context;
        this.currentActivity = (Activity) context;
        permissionResultCallback = (PermissionResultCallback) context;
    }

    public PermissionsUtils(Context context, PermissionResultCallback callback) {
        this.context = context;
        this.currentActivity = (Activity) context;
        permissionResultCallback = callback;
    }


    /**
     * Check the API Level & Permission
     *
     * @param permissions
     * @param dialogContent
     * @param requestCode
     */

    public void checkPermission(ArrayList<String> permissions, String dialogContent, int requestCode) {
        this.permissionList = permissions;
        this.dialogContent = dialogContent;
        this.requestCode = requestCode;

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(permissions, requestCode)) {
                permissionResultCallback.permissionGranted(requestCode);
            }
        } else {
            permissionResultCallback.permissionGranted(requestCode);
        }
    }


    /**
     * Check and request the Permissions
     *
     * @param permissions
     * @param requestCode
     * @return
     */

    private boolean checkAndRequestPermissions(ArrayList<String> permissions, int requestCode) {

        if (permissions.size() > 0) {
            listPermissionsNeeded = new ArrayList<>();

            for (int i = 0; i < permissions.size(); i++) {
                int hasPermission = ContextCompat.checkSelfPermission(context, permissions.get(i));

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions.get(i));
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(currentActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
                return false;
            }
        }
        return true;
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    Map<String, Integer> perms = new HashMap<>();

                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(permissions[i], grantResults[i]);
                    }

                    final ArrayList<String> pendingPermissions = new ArrayList<>();

                    for (int i = 0; i < listPermissionsNeeded.size(); i++) {
                        if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, listPermissionsNeeded.get(i))) {
                                pendingPermissions.add(listPermissionsNeeded.get(i));
                            } else {
                                permissionResultCallback.neverAskAgain(this.requestCode);
                                Toast.makeText(currentActivity, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }

                    if (pendingPermissions.size() > 0) {
                        showMessageOKCancel(dialogContent,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkPermission(permissionList, dialogContent, PermissionsUtils.this.requestCode);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                if (permissionList.size() == pendingPermissions.size()) {
                                                    permissionResultCallback.permissionDenied(PermissionsUtils.this.requestCode);
                                                } else {
                                                    permissionResultCallback.partialPermissionGranted(PermissionsUtils.this.requestCode, pendingPermissions);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                });
                    } else {
                        permissionResultCallback.permissionGranted(this.requestCode);
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * Explain why the app needs permissions
     *
     * @param message
     * @param okListener
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("ok", okListener)
                .setNegativeButton("cancel", okListener)
                .create()
                .show();
    }

    public interface PermissionResultCallback {

        void permissionGranted(int requestCode);

        void partialPermissionGranted(int requestCode, ArrayList<String> grantedPermissions);

        void permissionDenied(int requestCode);

        void neverAskAgain(int requestCode);
    }
}