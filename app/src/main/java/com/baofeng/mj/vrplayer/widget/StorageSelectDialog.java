package com.baofeng.mj.vrplayer.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.adapter.StorageSelectAdapter;
import com.baofeng.mj.vrplayer.bean.NewFile;
import com.baofeng.mj.vrplayer.business.LocalVideoPathBusiness;
import com.baofeng.mj.vrplayer.util.PixelsUtil;
import com.baofeng.mj.vrplayer.util.ReportUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchuanchi on 2016/6/14.
 * 存储选择对话框
 */
public class StorageSelectDialog implements View.OnClickListener{
    private Context context;
    private Dialog dialog;//对话框
    private TextView tv_current_dir;
    private LinearLayout ll_back;
    private ListView listView;
    private TextView tv_cancel;
    private TextView tv_confirm;
    private MyDialogInterface myDialogInterface;//回调接口
    private StorageSelectAdapter storageSelectAdapter;
    private List<NewFile> fileList;
    private String externalDir;//可以浏览的根路径
    private List<String> defaultDirList;//所有默认根路径
    private List<String> customDirList;//所有自定义根路径

    public StorageSelectDialog(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_storage_select,null);//生成布局文件
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.width = PixelsUtil.getWidthPixels(context) - PixelsUtil.dip2px(context, 40);
        params.height = PixelsUtil.getHeightPixels(context) - PixelsUtil.dip2px(context, 70);
        dialog = new Dialog(context, R.style.translucence_dialog);//创建半透明对话框
        dialog.setContentView(view, params);//设置布局文件
        dialog.setCancelable(true);//true可以点击返回键取消对话框
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {//监听返回键
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    back();//返回
                    return true;
                }
                return false;
            }
        });
        initView(view);//初始化控件
    }

    /**
     * 初始化控件
     */
    private void initView(View view){
        tv_current_dir = (TextView) view.findViewById(R.id.tv_current_dir);
        ll_back = (LinearLayout) view.findViewById(R.id.ll_back);
        listView = (ListView) view.findViewById(R.id.listView);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        ll_back.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewFile newFile = fileList.get(position);
                refreshListView(new File(newFile.getPath()));//刷新ListView
            }
        });
        fileList = new ArrayList<NewFile>();
        storageSelectAdapter = new StorageSelectAdapter(context, fileList);
        listView.setAdapter(storageSelectAdapter);
        externalDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back://返回
                back();//返回
                break;
            case R.id.tv_cancel://取消
                dialog.dismiss();
                ReportUtil.reportFolderAdd(null);
                break;
            case R.id.tv_confirm://确定
                String selectPath = tv_current_dir.getText().toString();
                if(externalDir.equals(selectPath)){
                    Toast.makeText(context, "请选择文件夹！", Toast.LENGTH_SHORT).show();
                }else if(isExistCustomDir(selectPath)){
                    Toast.makeText(context, "当前文件夹已经添加！", Toast.LENGTH_SHORT).show();
                }else{
                    if (myDialogInterface != null) {
                        myDialogInterface.dialogCallBack(selectPath);
                    }
                    dialog.dismiss();
                    ReportUtil.reportFolderAdd(selectPath);
                }
                break;
        }
    }

    /**
     * 显示对话框
     */
    public void showDialog(MyDialogInterface myDialogInterface){
        if(dialog != null && !dialog.isShowing()){
            this.myDialogInterface = myDialogInterface;
            defaultDirList = LocalVideoPathBusiness.getAllDefaultDir(context, true);
            customDirList = LocalVideoPathBusiness.getAllCustomDir(context, true);
            refreshListView(new File(externalDir));//刷新ListView
            dialog.show();
        }
    }

    /**
     * 刷新ListView
     */
    private void refreshListView(File file){
        if(file != null && file.exists() && file.isDirectory()) {
            File[] fileArr = file.listFiles();
            if(fileArr != null && fileArr.length > 0){
                fileList.clear();
                for (File child : fileArr){//遍历文件
                    if(child.isDirectory()){//是文件夹
                        if(!isInDefaultDir(child.getAbsolutePath())){//不在默认路径里
                            NewFile newFile = new NewFile();
                            newFile.setName(child.getName());
                            newFile.setPath(child.getAbsolutePath());
                            fileList.add(newFile);
                        }
                    }
                }
                storageSelectAdapter.notifyDataSetChanged();
                tv_current_dir.setText(file.getAbsolutePath());//显示当前目录
                if(externalDir.equals(file.getAbsolutePath())){//根目录
                    ll_back.setVisibility(View.GONE);//隐藏返回按钮
                }else{//不是根目录
                    ll_back.setVisibility(View.VISIBLE);//显示返回按钮
                }
            }
        }
    }

    /**
     * true在默认路径里，false相反
     */
    private boolean isInDefaultDir(String pathDir){
        for(String defaultDir : defaultDirList){
            if(pathDir.startsWith(defaultDir)){
                return true;
            }
        }
        return false;
    }

    /**
     * true自定义路径已经存在，false相反
     */
    private boolean isExistCustomDir(String pathDir){
        for(String customDir : customDirList){
            if(pathDir.equals(customDir)){
                return true;
            }
        }
        return false;
    }

    /**
     * 返回
     */
    private void back(){
        String curPath = tv_current_dir.getText().toString();
        if(externalDir.equals(curPath)){//根目录
            dialog.dismiss();
            ReportUtil.reportFolderAdd(null);
        }else{//不是根目录
            refreshListView(new File(curPath).getParentFile());//刷新ListView
        }
    }

    public interface MyDialogInterface {
        void dialogCallBack(String selectPath);
    }
}
