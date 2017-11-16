package com.mojing.vrplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.baofeng.mojing.input.base.MojingKeyCode;
import com.bfmj.viewcore.interfaces.GLOnKeyListener;
import com.bfmj.viewcore.interfaces.GLViewFocusListener;
import com.bfmj.viewcore.render.GLColor;
import com.bfmj.viewcore.render.GLConstant;
import com.bfmj.viewcore.view.GLAdapterView;
import com.bfmj.viewcore.view.GLGridView;
import com.bfmj.viewcore.view.GLGridViewScroll;
import com.bfmj.viewcore.view.GLImageView;
import com.bfmj.viewcore.view.GLLinearView;
import com.bfmj.viewcore.view.GLRectView;
import com.bfmj.viewcore.view.GLRelativeView;
import com.bfmj.viewcore.view.GLTextView;
import com.bfmj.viewcore.view.GLView;
import com.mojing.vrplayer.R;
import com.mojing.vrplayer.adapter.LocalSourceAdapter;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.publicc.bean.LocalVideoBean;
import com.mojing.vrplayer.utils.BitmapUtil;
import com.mojing.vrplayer.utils.HeadControlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yushaochen on 2017/5/22.
 */

public class LocalSelectSourceView extends GLRelativeView{

    private Context mContext;

    private int mWidth = 1280;
    private int mHeight = 910;

    private GLGridViewScroll gridView;

    private LocalSourceAdapter localAdapter;

    private List<LocalVideoBean> datas = new ArrayList<>();

    private int currentNum = -1;

    public LocalSelectSourceView(Context context) {
        super(context);

        mContext = context;
        setLayoutParams(mWidth, mHeight+300);
        setPadding(0,300,0,0);
        Bitmap bitmap = BitmapUtil.getBitmap(mWidth, mHeight, 20f, "#272729");

        gridViewContainer=new GLLinearView(mContext);
        gridViewContainer.setLayoutParams(mWidth, mHeight-50);
        gridViewContainer.setOrientation(GLConstant.GLOrientation.VERTICAL );
        gridViewContainer.setBackground(bitmap);
        addView(gridViewContainer);
        GLTextView sceneTextView = new GLTextView(mContext);
        sceneTextView.setLayoutParams(240f,56f);
        sceneTextView.setTextSize(54);
        sceneTextView.setTextColor(new GLColor(0x888888));
        sceneTextView.setAlignment(GLTextView.ALIGN_CENTER);
        sceneTextView.setText("文件选择");
        sceneTextView.setMargin(480f,70f,0f,70f);
        gridViewContainer.addView(sceneTextView);


        //创建内容资源展示列表
        createGridView();

        setFocusListener(focusListener);
    }
    GLLinearView gridViewContainer=null;
    GLImageView mCloseImage;
    private void createGridView() {
        gridView = new GLGridViewScroll(mContext, 2, 3);
        gridView.setOrientation(GLConstant.GLOrientation.HORIZONTAL );
        gridView.setScrollDirection(GLGridView.ScrollDirection.UP_DOWN);
        gridView.setCloseAnimation(true);
        gridView.setMargin( 90-4, 0, 0, 0 );
        gridView.setLayoutParams(mWidth-200, 270*2+60);//mHeight-140
//        gridView.setBackground( new GLColor(1.0f, 0.50f, 0.50f ));
        gridView.setHorizontalSpacing( 80f);
        gridView.setVerticalSpacing( 60f);

        gridView.setBtnHorSpace( 20);//滚动条的按钮和滚动条之间的距离
        gridView.setBottomSpaceing( 40);//滚动条距离网格的距离

//        gridView.setNumOnFouseColor( new GLColor(1.0f, 0.0f, 1.0f ));
        gridView.setFlipLeftIcon("play_button_up_disable");
        gridView.setFlipRightIcon( "play_button_down_normal" );
        gridView.setProcessBackground("play_slider_bg");
        gridView.setBarImage("play_slider_progress");
        gridView.setOffsetY(- 80);
        gridView.setBtnImageWidth(80);
        gridView.setBtnImageHeight(80);

        gridView.setProcessViewWidth(8);//滚动条的宽
        gridView.setProcessViewHeight(mHeight-140-160-200);//滚动条的高

        localAdapter = new LocalSourceAdapter(mContext);

        gridView.setAdapter(localAdapter);

        setOnPageListener();

        gridViewContainer.addView(gridView);

        setGridListener();

        mCloseImage = new GLImageView(mContext);
        mCloseImage.setLayoutParams(80,80);
        mCloseImage.setImage("play_menu_button_close_normal");
        mCloseImage.setMargin(620,mHeight+40,0,0);
        HeadControlUtil.bindView(mCloseImage);
        mCloseImage.setOnKeyListener(new GLOnKeyListener() {
            @Override
            public boolean onKeyDown(GLRectView view, int keycode) {
                if(mContrlViewInterface!=null){
                    mContrlViewInterface.showContrlView();
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
        mCloseImage.setFocusListener(new GLViewFocusListener() {
            @Override
            public void onFocusChange(GLRectView view, boolean focused) {
                if(focused) {
                    mCloseImage.setImage("play_menu_button_close_hover");
                } else {
                    mCloseImage.setImage("play_menu_button_close_normal");
                }
            }
        });
        this.addView(mCloseImage);
    }



    public void setData(List<LocalVideoBean> data) {
        this.datas.clear();
        if(null != data && data.size() > 0) {
            datas.addAll(data);

        } else {
            return;
        }

        setGridData();

    }

    private void setGridData() {
        if(datas.size() <= 7) {
            gridView.setPrvBtnImgViewVisible(false);
            gridView.setNextBtnImgViewVisible(false);
            gridView.setSeekBarVisible( false );
        } else {
            gridView.setPrvBtnImgViewVisible(true);
            gridView.setNextBtnImgViewVisible(true);
            gridView.setSeekBarVisible( true );
        }
        localAdapter.setData(datas);
    }

    public void setCurrentNum(int index) {
        currentNum = index;
        localAdapter.setNum(currentNum);
        localAdapter.notifyDataSetChanged();
    }

    private void setGridListener() {
        gridView.setOnItemClickListener(new GLAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                currentNum = position;
                setCurrentNum(currentNum);
                //System.out.println("!!!!!!!!!!!!!----------当前选中:------"+currentNum);
                if(null != mCallBack) {
                    mCallBack.onSelectedLocalMovie(datas.get(currentNum),currentNum);
                }
            }
        });
        gridView.setOnItemSelectedListener(new GLAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                if(position != currentNum) {
                    GLImageView imageView = (GLImageView) ((LocalSourceItemView)glview).getView("imageView");
                    imageView.setBackground("play_video_local_bg_hover");
//                    GLTextView titleView = (GLTextView) ((LocalSourceItemView)glview).getView("titleView");
//                    titleView.setTextColor(GLColorUitl.HoverTextColor);
                }
            }

            @Override
            public void onNothingSelected(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                if(position != currentNum) {
                    GLImageView imageView = (GLImageView) ((LocalSourceItemView)glview).getView("imageView");
                    imageView.setBackground("play_video_local_bg_empty");
//                    GLTextView titleView = (GLTextView) ((LocalSourceItemView)glview).getView("titleView");
//                    titleView.setTextColor(GLColorUitl.DefaultTextColor);
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

    public void notifyDataSetChanged() {
//        if(null != gridView ) {
//            gridView.pageChange();
//        }
        localAdapter.notifyDataSetChanged();
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
                        nextBtnImgView.setImage("play_button_down_hover" );
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
    MoviePlayerSettingView.ContrlViewInterface mContrlViewInterface;
    public void setContrlViewInterface(MoviePlayerSettingView.ContrlViewInterface contrlViewInterface){
        mContrlViewInterface=contrlViewInterface;
    }
}
