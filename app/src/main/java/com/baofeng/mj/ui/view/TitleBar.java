package com.baofeng.mj.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;


/**
 * Created by zhaominglei on 2016/5/20.
 */
public class TitleBar extends RelativeLayout {

    private ImageButton mLeftBtn;
    private TextView mTitleText;
    private ImageButton mRightBtn;
    private TextView mRightText;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_title_bar, this);
        mLeftBtn = (ImageButton) view.findViewById(R.id.back);
        mTitleText = (TextView) view.findViewById(R.id.title);
        mRightBtn = (ImageButton) view.findViewById(R.id.ib_right);
        mRightText = (TextView) view.findViewById(R.id.tv_right);
        //setBackgroundColor(getContext().getResources().getColor(R.color.theme_main_color));
    }

    public void setTitleBarTitle(String title) {
        this.mTitleText.setText(title);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (mLeftBtn != null) {
            mLeftBtn.setOnClickListener(onClickListener);
        }
        if (mRightBtn != null) {
            mRightBtn.setOnClickListener(onClickListener);
        }
        if (mRightText != null) {
            mRightText.setOnClickListener(onClickListener);
        }
    }

    public ImageButton getRightBtn() {
        return mRightBtn;
    }

    public TextView getRightTv() {
        return mRightText;
    }

}
