package com.optic.projectofinal.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PermissionCall {
    private static final String TAG = "own";
    private static final int REQUEST_PERMISSION_CALL = 1;
    private static final int REQUEST_PERMISSION_CALL_AGAIN = 2;
    private Activity context;

    private String number;

    public PermissionCall(Activity activity) {
        this.context = activity;

    }

    public void call(String number) {
        this.number = number;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i(TAG, "PERMISSION GRANTED");
            callInside();
        } else {//api level >=23 or disabled the permissions
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.FLAG_PERMISSION_WHITELIST_INSTALLER) {
                Log.i(TAG, "hte user previously rejected the request");

            } else {
                Log.i(TAG, "request permissions");
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
        }
    }


        public void executeOnRequestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == REQUEST_PERMISSION_CALL || requestCode == REQUEST_PERMISSION_CALL_AGAIN) {
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "permission granted->request");
                    callInside();
                } else {
                    Log.i(TAG, "Permission denied ->request");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CALL_PHONE)) {
                        new MaterialAlertDialogBuilder(context).setMessage("you need to enable the permisions")
                                .setPositiveButton("Try again", (dialogInterface, i) ->
                                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL_AGAIN)
                                )
                                .setNegativeButton("No thanks", (dialogInterface, i) -> {
                                    Log.i(TAG, "leave");
                                }).show();
                    } else {
                        if (requestCode == REQUEST_PERMISSION_CALL_AGAIN) {
                            Toast.makeText(context, "You need to able permissions manually", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "reject permission only way is enable on settins");
                            new MaterialAlertDialogBuilder(context).setTitle("Information")
                                    .setMessage("You have rejected many times permissions, so if you want call, you have to enable permissions  manually.")
                                    .setPositiveButton("OK",(dialogInterface, i) -> {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                        intent.setData(uri);
                                        context.startActivity(intent);
                                    })
                                    .setNegativeButton("No thanks",null)
                                    .show();
                        }
                    }
                }
            }

        }
    private void callInside() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }
}
