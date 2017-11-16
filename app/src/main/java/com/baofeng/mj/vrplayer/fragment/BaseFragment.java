package com.baofeng.mj.vrplayer.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * fragment基类
 */
public abstract class BaseFragment extends Fragment {
    protected View rootView;

    public void removeRootView(){
        if(rootView != null){
            ViewGroup parent = (ViewGroup)rootView.getParent();
            if(parent != null) {
                parent.removeView(rootView);
            }
        }
    }

    /**
     * 当Fragment嵌套时，startActivityForResult无法收到onActivityResult的回调
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 当Fragment嵌套时，子Fragment无法收到onActivityResult的回调，
     * 如果Google某天修复了这个问题，可以去掉这个方法
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // notifying nested fragments (support library bug fix)
        final FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager != null) {
            final List<Fragment> nestedFragments = childFragmentManager.getFragments();
            if (nestedFragments == null || nestedFragments.size() == 0){
                return;
            }
            for (Fragment childFragment : nestedFragments) {
                if (childFragment != null && !childFragment.isDetached() && !childFragment.isRemoving()) {
                    childFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
