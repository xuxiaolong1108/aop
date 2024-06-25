package com.example.aspectj;

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
}
