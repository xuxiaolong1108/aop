package com.example.asm;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Method;

public class Utils {

    private static long mLastClickTime = 0; // 上次的点击时间

    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeValue = time - mLastClickTime;
        if (timeValue > 0 && timeValue < 1000) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }

    public static void onMethodIntercepted(String annDesc, Context context, Object caller, Method method, Object... objects) {

        new AlertDialog.Builder(context)
                .setTitle(annDesc)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            method.setAccessible(true);
                            method.invoke(caller, objects);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

}
