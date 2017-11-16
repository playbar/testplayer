package com.baofeng.mj.vrplayer.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.baofeng.mj.unity.UnityActivity;
import com.baofeng.mj.vrplayer.util.ReportUtil;

/**
 * Created by liuchuanchi on 2017/7/19.
 * 打开activity业务
 */
public class OpenActivity {
    /**
     * 打开眼镜选择页面
     */
    public static void openGlassesSelect(Activity activity){
        Intent intent = new Intent("com.main.intent.action.vr.DEVICE_SELECT");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage(activity.getPackageName());
        //intent.putExtra("from", ConstantKey.FROM_MAIN_APP_SETTING);
        activity.startActivityForResult(intent, 100);
    }

    /**
     * 打开眼镜选择页面
     */
    public static void openGlassesSelect(Fragment fragment){
        Intent intent = new Intent("com.main.intent.action.vr.DEVICE_SELECT");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage(fragment.getActivity().getPackageName());
        //intent.putExtra("from", from);
        fragment.startActivityForResult(intent, 100);
    }

    /**
     * 打开unity页面
     */
    public static void openUnity(Context context){
        ReportUtil.reportTimer("1");
        Intent intent = new Intent(context, UnityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
