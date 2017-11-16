package com.mojing.vrplayer.view;

import android.content.Context;
import android.text.TextUtils;

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
import com.mojing.vrplayer.adapter.VarietyAdapter;
import com.mojing.vrplayer.bean.SelectInfo;
import com.mojing.vrplayer.interfaces.IPlayerSettingCallBack;
import com.mojing.vrplayer.publicc.bean.VideoDetailBean;
import com.mojing.vrplayer.utils.HeadControlUtil;

import java.util.ArrayList;

/**
 * 综艺和电影
 * Created by yushaochen on 2017/4/17.
 */

public class SelectVarietyView extends GLRelativeView{

    private Context mContext;

    private SelectBottomView selectBottomView;

    private GLGridViewScroll gridView;

    private VarietyAdapter varietyAdapter;

    private ArrayList<SelectInfo> datas = new ArrayList<>();

    private ArrayList<VideoDetailBean.AlbumsBean.VideosBean> gridDatas = new ArrayList<>();

    private int currentNum = 1;

    public SelectVarietyView(Context context) {
        super(context);

        mContext = context;

        //创建gridview
        createGridView();

        //创建底部快速导航
        createBottomView();
    }

    private void createBottomView() {
        selectBottomView = new SelectBottomView(mContext);
        selectBottomView.setMargin(0f,365f,0f,0f);
        selectBottomView.setVisible(false);

        addView(selectBottomView);
        
        setBottomListener();
    }

    private void setBottomListener() {
        selectBottomView.setSelectCallBack(new SelectBottomView.SelectCallBack() {
            @Override
            public void onCallBack(String nums) {
                handleGridData(nums);
            }
        });
    }

    private void createGridView() {
        gridView = new GLGridViewScroll(mContext, 4, 2);
        gridView.setOrientation(GLConstant.GLOrientation.HORIZONTAL );
        gridView.setScrollDirection(GLGridView.ScrollDirection.UP_DOWN);
        gridView.setCloseAnimation(true);
        gridView.setMargin( 95, 35, 0, 0 );
        gridView.setLayoutParams(1000-95-95, 365-35-30);
//        gridView.setBackground( new GLColor(1.0f, 0.50f, 0.50f ));
        gridView.setHorizontalSpacing( 10f);
        gridView.setVerticalSpacing( 20f);

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
        gridView.setProcessViewHeight(160);//滚动条的高

        varietyAdapter = new VarietyAdapter(mContext);

        gridView.setAdapter(varietyAdapter);

        setOnPageListener();

        addView(gridView);

        setGridListener();

    }

    private void setGridListener() {
        gridView.setOnItemClickListener(new GLAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                currentNum = gridDatas.get(position).getSeq();
                varietyAdapter.setNum(currentNum);
                varietyAdapter.notifyDataSetChanged();
//                System.out.println("!!!!!!!!!!!!!----------当前选中:------"+(currentNum-1));
                if(null != mCallBack) {
                    mCallBack.onSelected(currentNum-1);
                }
            }
        });
        gridView.setOnItemSelectedListener(new GLAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                if(gridDatas.get(position).getSeq() != currentNum) {
                    GLTextView textView = (GLTextView) ((VarietyItemView)glview).getView("textView");
                    textView.setBackground("play_bg_control_normal_400_60");
                    textView.setTextColor(new GLColor(0x888888));
                }
            }

            @Override
            public void onNothingSelected(GLAdapterView<?> glparent, GLView glview, int position, long id) {
                if(gridDatas.get(position).getSeq() != currentNum) {
                    GLTextView textView = (GLTextView) ((VarietyItemView)glview).getView("textView");
                    textView.setBackground("play_bg_control_hover_400_60");
                    textView.setTextColor(new GLColor(0x888888));
                }
            }

            @Override
            public void onNoItemData() {

            }
        });
    }

    public void setData(ArrayList<SelectInfo<VideoDetailBean.AlbumsBean.VideosBean>> data) {
        this.datas.clear();
        if(null != data && data.size() > 0) {
            this.datas.addAll(data);

        } else {
            return;
        }

        setBottomData();

        setGridData();
        
    }

    private void setGridData() {

        if(datas.get(0).getTotal() <= 10) {
            gridView.setPrvBtnImgViewVisible(false);
            gridView.setNextBtnImgViewVisible(false);
		    gridView.setSeekBarVisible( false );
        } else {
            gridView.setPrvBtnImgViewVisible(true);
            gridView.setNextBtnImgViewVisible(true);
		    gridView.setSeekBarVisible( true );
        }

        varietyAdapter.setNum(currentNum);
        handleGridData(datas.get(0).getNums());
    }

    private void handleGridData(String nums) {

        if(!TextUtils.isEmpty(nums)) {
            gridDatas.clear();

            fillData(nums);

            varietyAdapter.setData(gridDatas);

            if(gridDatas.size() > 10) {//单页数据超过10，翻页焦点可以选中
                gridView.setPrvBtnImgViewVisible(true);
                gridView.setNextBtnImgViewVisible(true);
                gridView.setSeekBarVisible( true );
                updateIcon();

            } else {
                gridView.setPrvBtnImgViewVisible(false);
                gridView.setNextBtnImgViewVisible(false);
                gridView.setSeekBarVisible( false );
            }
        }
    }

    private void fillData(String nums) {
        for(int x = 0; x < datas.size(); x++) {
            if(nums.equals(datas.get(x).getNums())) {
                gridDatas.addAll(datas.get(x).getInfos());
            }
        }
    }

    private void setBottomData() {
        if(this.datas.size() > 1) {
            selectBottomView.initView(this.datas);
            selectBottomView.setSelectedItem(this.datas.get(0).getNums());
            selectBottomView.setVisible(true);
        } else {
            selectBottomView.setVisible(false);
        }
    }

    //设置当前播放第几集
    public void setCurrentNum(int index) {
        this.currentNum = index + 1;
        if(datas.size() > 1) {
            for(SelectInfo selectInfo : this.datas) {
                String nums = selectInfo.getNums();
                String[] split = nums.split("-");
                int start = Integer.parseInt(split[0]);
                int end = Integer.parseInt(split[1]);
                if((currentNum) >= start && (currentNum) <= end) {
                    selectBottomView.setSelectedItem(nums);
                    handleGridData(nums);
                    break;
                }
            }
        }

        varietyAdapter.setNum(currentNum);
        varietyAdapter.notifyDataSetChanged();

    }

    public void prePage() {
        if(null != gridView && !gridView.isFirstPage()) {
            gridView.previousPage();
            updateIcon();
        }
    }

    public void nextPage() {
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
//        HeadControlUtil.bindView(nextBtnImgView);
    }

    public void updateIcon() {
        if(gridView.isFirstPage()) {
            HeadControlUtil.unbindView(prvBtnImgView);
            prvBtnImgView.setImage("play_button_up_disable");
        } else {
            HeadControlUtil.bindView(prvBtnImgView);
            prvBtnImgView.setImage("play_button_up_normal");
        }
        if(gridView.isLastPage()) {
            HeadControlUtil.unbindView(nextBtnImgView);
            nextBtnImgView.setImage("play_button_down_disable");
        } else {
            HeadControlUtil.bindView(nextBtnImgView);
            nextBtnImgView.setImage("play_button_down_normal");
        }
    }

    private IPlayerSettingCallBack mCallBack;

    public void setIPlayerSettingCallBack(IPlayerSettingCallBack callBack) {

        this.mCallBack = callBack;
    }
}
