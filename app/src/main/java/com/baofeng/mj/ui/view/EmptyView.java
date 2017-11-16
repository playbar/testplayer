package com.baofeng.mj.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baofeng.mj.utils.NetworkUtils;
import com.baofeng.mj.vrplayer.R;

/** 空页面
 * Created by muyu on 2016/6/13.
 */
public class EmptyView extends FrameLayout {
    protected TextView textView;
    protected ImageView imageView;
    protected TextView refreshView;

    private String networkError;
    private String normalText;
    private String loadingText;
    private String failureText;
    private boolean init = true;

    public EmptyView(Context context) {
        super(context);
        init(null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.view_empty, this);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        textView = (TextView) view.findViewById(R.id.textView);
        refreshView = (TextView) view.findViewById(R.id.refreshView);
        if (attrs == null) {
            normalText = getContext().getString(R.string.no_content);
            networkError = getContext().getString(R.string.no_content);
            imageView.setImageResource(R.mipmap.empty_img_content);
            return;
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EmptyView);
        if (a.hasValue(R.styleable.EmptyView_imageWarn)) {
            Drawable drawable = a.getDrawable(R.styleable.EmptyView_imageWarn);
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setImageResource(R.mipmap.empty_img_content);
        }

        if (a.hasValue(R.styleable.EmptyView_textLoading)) {
            loadingText = a.getString(R.styleable.EmptyView_textLoading);
        } else {
            loadingText = getContext().getString(R.string.loading_text);
        }

        if (a.hasValue(R.styleable.EmptyView_textNormal)) {
            normalText = a.getString(R.styleable.EmptyView_textNormal);
        } else {
            normalText = getContext().getString(R.string.no_content);
        }

        if (a.hasValue(R.styleable.EmptyView_textWithoutNetwork)) {
            networkError = a.getString(R.styleable.EmptyView_textWithoutNetwork);
        } else {
            networkError = getContext().getString(R.string.no_content);
        }
        if (a.hasValue(R.styleable.EmptyView_textFailure)) {
            failureText = a.getString(R.styleable.EmptyView_textFailure);
        } else {
            failureText = getContext().getString(R.string.no_content);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            //无网络时
            if (NetworkUtils.getNetworkState(getContext()) == NetworkUtils.TYPE_NO) {
                textView.setText(networkError);
            } else {
                textView.setText(normalText);
            }
        }
    }

    public String getNetworkError() {
        return networkError;
    }

    public void setNetworkError(String networkError) {
        this.networkError = networkError;
    }

    public String getNormalText() {
        return normalText;
    }

    public void setNormalText(String normalText) {
        this.normalText = normalText;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public String getFailureText() {
        return failureText;
    }

    public void setFailureText(String failureText) {
        this.failureText = failureText;
    }

    public void startLoading() {
        setText(loadingText);
    }

    public void loadFailure() {
        setText(failureText);
    }

    public CharSequence getText() {
        return textView.getText();
    }

    public void setText(int stringId) {
        textView.setText(stringId);
    }

    public void setText(CharSequence text) {
        textView.setText(text);
    }

    public TextView getRefreshView() {
        return refreshView;
    }

    public void setRefreshView(TextView refreshView) {
        this.refreshView = refreshView;
    }
}