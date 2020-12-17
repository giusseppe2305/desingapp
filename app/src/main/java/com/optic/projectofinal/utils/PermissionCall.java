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
import com.optic.projectofinal.R;

import static com.optic.projectofinal.utils.Utils.TAG_LOG;

public class PermissionCall {

    private static final int REQUEST_PERMISSION_CALL = 1;
    private static final int REQUEST_PERMISSION_CALL_AGAIN = 2;
    private final Activity context;

    private String number;

    public PermissionCall(Activity activity) {
        this.context = activity;

    }

    public void call(String number) {
        this.number = number;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i(TAG_LOG, "PERMISSION GRANTED");
            callInside();
        } else {//api level >=23 or disabled the permissions
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.FLAG_PERMISSION_WHITELIST_INSTALLER) {
                Log.i(TAG_LOG, "hte user previously rejected the request");

            } else {
                Log.i(TAG_LOG, "request permissions");
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
        }
    }


        public void executeOnRequestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (requestCode == REQUEST_PERMISSION_CALL || requestCode == REQUEST_PERMISSION_CALL_AGAIN) {
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG_LOG, "permission granted->request");
                    callInside();
                } else {
                    Log.i(TAG_LOG, "Permission denied ->request");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CALL_PHONE)) {
                        new MaterialAlertDialogBuilder(context)
                                .setTitle(context.getString(R.string.permission_call_title))
                                .setMessage(context.getString(R.string.permission_call_reject_message))
                                .setPositiveButton(context.getString(R.string.permission_call_try_again), (dialogInterface, i) ->
                                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL_AGAIN)
                                )
                                .setNegativeButton(context.getString(R.string.permission_call_no_thanks), (dialogInterface, i) -> Log.i(TAG_LOG, "leave")).show();
                    } else {
                        if (requestCode == REQUEST_PERMISSION_CALL_AGAIN) {
                            Toast.makeText(context, context.getString(R.string.permission_call_enable_manually), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG_LOG, "reject permission only way is enable on settings");
                            new MaterialAlertDialogBuilder(context).setTitle(context.getString(R.string.permission_call_title))
                                    .setMessage(context.getString(R.string.permission_call_reject_many_times_message))
                                    .setPositiveButton(context.getString(R.string.permission_call_ok),(dialogInterface, i) -> {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                        intent.setData(uri);
                                        context.startActivity(intent);
                                    })
                                    .setNegativeButton(context.getString(R.string.permission_call_no_thanks),null)
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
