package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLAdapterView;
import com.bfmj.viewcore.view.GLGridView;
import com.bfmj.viewcore.view.GLGridViewScroll;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.bfmj.viewcore.view.GLView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.adapter.MovieAdapter;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.publicc.bean.ContentInfo;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.HeadControlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/5/19.
 */

public class SelectMovieView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 1000;
    private int mHeight = 388;

    private GLGridViewScroll gridView;

    private MovieAdapter movieAdapter;

    private List<ContentInfo> datas = new ArrayList<>();

    private int currentNum = -1;

    public SelectMovieView(Context context) {
        super(context);
        mContext = context;
        setLayoutParams(mWidth,mHeight);
        Bitmap bitmap = BitmapUtil.getBitmap(mWidth, mHeight, 20f, "#272729");
        setBackground(bitmap);
        //创建资源列表
        createGridView();

        setFocusListener(focusListener);

        //测试数据
//        test();
    }

    private void test() {
        List<ContentInfo> data = new ArrayList<>();
        for(int x = 0; x < 35; x++) {
            ContentInfo contentInfo = new ContentInfo();
            contentInfo.setTitle("测试"+x);
            if(x%2 == 0) {
                contentInfo.setPic_url("http://pic.static.mojing.cn//upload/1c526/70/8ce355ee.jpg");
            } else {
                contentInfo.setPic_url("http://pic.static.mojing.cn//upload/839cd/78/f1ddb947.jpg");
            }
            data.add(contentInfo);
        }
        setData(data);
    }

    private void createGridView() {
        gridView = new GLGridViewScroll(mContext, 2, 3);
        gridView.setOrientation(GLConstant.GLOrientation.HORIZONTAL );
        gridView.setScrollDirection(GLGridView.ScrollDirection.UP_DOWN);
        gridView.setCloseAnimation(true);
        gridView.setMargin( 90, 35, 0, 0 );
        gridView.setLayoutParams(mWidth-90-90, mHeight-35-35);
//        gridView.setBackground( new GLColor(1.0f, 0.50f, 0.50f ));
        gridView.setHorizontalSpacing( 14f);
        gridView.setVerticalSpacing( 18f);

        gridView.setBtnHorSpace( 20);//滚动条的按钮和滚动条之间的距离
        gridView.setBottomSpaceing( 40);//滚动条距离网格的距离

//        gridView.setNumOnFouseColor( new GLColor(1.0f, 0.0f, 1.0f ));
        gridView.setFlipLeftIcon("play_button_up_disable");
        gridView.setFlipRightIcon( "play_button_down_normal" );
        gridView.setProcessBackground("play_slider_bg");
        gridView.setBarImage("play_slider_progress");
//        gridView.setOffsetY( 100);
        gridView.setBtnImageWidth(50);
        gridView.setBtnImageHeight(50);

        gridView.setProcessViewWidth(8);//滚动条的宽
        gridView.setProcessViewHeight(178);//滚动条的高

        movieAdapter = new MovieAdapter(mContext);

        gridView.setAdapter(movieAdapter);

        setOnPageListener();

        addView(gridView);

        setGridListener();
    }

    private void setGridListener() {
        gridView.setOnItemClickListener(new GLAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                currentNum = position;
                setCurrentNum(currentNum);
                //System.out.println("!!!!!!!!!!!!!----------当前选中:------"+currentNum);
                if(null != mCallBack) {
                    mCallBack.onSelectedMovie(datas.get(currentNum),currentNum);
                }
            }
        });
        gridView.setOnItemSelectedListener(new GLAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                if(position != currentNum) {
                    GLImageView imageView = (GLImageView) ((MovieItemView)glview).getView("imageView");
                    imageView.setBackground("play_video_online_bg_hover");
                    GLTextView titleView = (GLTextView) ((MovieItemView)glview).getView("titleView");
                    titleView.setTextColor(new GLColor(0xeeeeee));
                }
            }

            @Override
            public void onNothingSelected(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                if(position != currentNum) {
                    GLImageView imageView = (GLImageView) ((MovieItemView)glview).getView("imageView");
                    imageView.setBackground("play_video_online_bg_empty");
                    GLTextView titleView = (GLTextView) ((MovieItemView)glview).getView("titleView");
                    titleView.setTextColor(new GLColor(0xcccccc));
                }
            }

            @Override
            public void onNoItemData() {

            }
        });
    }

    public void prePage() {
        HeadControlUtil.bindView(nextBtnImgView);
        if(null != gridView && !gridView.isFirstPage()) {
            gridView.previousPage();
            updateIcon();
        }
    }

    public void nextPage() {
        HeadControlUtil.bindView(prvBtnImgView);
        if(null != gridView && !gridView.isLastPage()) {
            gridView.nextPage();
            updateIcon();
        }
    }
    
    GLImageView prvBtnImgView;
    GLImageView nextBtnImgView;
    private void setOnPageListener() {
        prvBtnImgView = gridView.getPrvBtnImgView();
        prvBtnImgView.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(!gridView.isFirstPage()) {
                    if(focused) {
                        prvBtnImgView.setImage("play_button_up_hover");
                    } else {
                        prvBtnImgView.setImage("play_button_up_normal");
                    }
                } else {
                    prvBtnImgView.setImage("play_button_up_disable");
                }
            }
        });
        prvBtnImgView.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        prePage();
                        break;
                }

                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
//        HeadControlUtil.bindView(prvBtnImgView);

        nextBtnImgView = gridView.getNextBtnImgView();
        nextBtnImgView.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(!gridView.isLastPage()) {
                    if(focused) {
                        nextBtnImgView.setImage( "play_button_down_hover" );
                    } else {
                        nextBtnImgView.setImage( "play_button_down_normal" );
                    }
                } else {
                    nextBtnImgView.setImage("play_button_down_disable");
                }
            }
        });
        nextBtnImgView.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                switch (keycode) {
                    case MojingKeyCode.KEYCODE_ENTER:
                        nextPage();
                        break;
                }

                return false;
            }

            @Override
            public boolean onKeyUp(GLRectView view, int keycode) {
                return false;
            }

            @Override
            public boolean onKeyLongPress(GLRectView view, int keycode) {
                return false;
            }
        });
        HeadControlUtil.bindView(nextBtnImgView);
    }

    private void updateIcon() {
        if(gridView.isFirstPage()) {
            HeadControlUtil.unbindView(prvBtnImgView);
            prvBtnImgView.setImage("play_button_up_disable");
        } else {
            prvBtnImgView.setImage("play_button_up_normal");
        }
        if(gridView.isLastPage()) {
            HeadControlUtil.unbindView(nextBtnImgView);
            nextBtnImgView.setImage("play_button_down_disable");
        } else {
            nextBtnImgView.setImage("play_button_down_normal");
        }
    }

    public void setData(List<ContentInfo> data) {
        this.datas.clear();
        if(null != data && data.size() > 0) {
            datas.addAll(data);

        } else {
            return;
        }

        setGridData();

    }

    private void setGridData() {
        if(datas.size() <= 6) {
            gridView.setPrvBtnImgViewVisible(false);
            gridView.setNextBtnImgViewVisible(false);
            gridView.setSeekBarVisible( false );
        } else {
            gridView.setPrvBtnImgViewVisible(true);
            gridView.setNextBtnImgViewVisible(true);
            gridView.setSeekBarVisible( true );
        }
        movieAdapter.setData(datas);
    }

    public void setCurrentNum(int index) {
        currentNum = index;
        movieAdapter.setNum(currentNum);
        movieAdapter.notifyDataSetChanged();
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }

    private GLViewFocusListener focusListener = new GLViewFocusListener() {
        @Override
        public void onFocusChange(GLRectView view, boolean focused) {
            if(focused) {
//                ((GLBaseActivity)getContext()).showCursorView();
                if(null != mCallBack) {
                    mCallBack.onHideControlAndSettingView(false);
                }
            } else {
//                ((GLBaseActivity)getContext()).hideCursorView2();
                if(null != mCallBack) {
                    mCallBack.onHideControlAndSettingView(true);
                }
            }
        }
    };
}