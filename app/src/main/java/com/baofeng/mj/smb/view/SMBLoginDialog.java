package com.baofeng.mj.smb.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baofeng.mj.smb.activity.HelpFeedBackActivity;
import com.baofeng.mj.smb.interfaces.ISMBLoginListener;
import com.baofeng.mj.smb.util.SmbIpUtil;
import com.baofeng.mj.util.publicutil.PixelsUtil;
import com.baofeng.mj.utils.StringUtils;
import com.baofeng.mj.vrplayer.R;
import com.mj.smb.smblib.bean.FileItem;

import java.net.MalformedURLException;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by panxin on 2017/8/9.
 */

public class SMBLoginDialog extends Dialog {


    private Context mContext;
    private String ip;
    private String deviceName;
    private EditText smbLogin_edit_name, smbLogin_edit_password;
    private TextView smbLogin_textview_title;
    private TextView smbLogin_textview_title_linkto;

    public SMBLoginDialog(Context mContext) {
        super(mContext, R.style.SMB_Loging_Dialog);
        this.mContext = mContext;
        init(mContext);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_smb_login_device, null);
        addContentView(layout, new ViewGroup.LayoutParams(
                PixelsUtil.getWidthPixels() - PixelsUtil.dip2px(40), RelativeLayout.LayoutParams.WRAP_CONTENT));
        smbLogin_textview_title_linkto = (TextView) layout.findViewById(R.id.smbLogin_textview_title_linkto);
        smbLogin_textview_title = (TextView) layout.findViewById(R.id.smbLogin_textview_title);
        smbLogin_edit_name = (EditText) layout.findViewById(R.id.smbLogin_edit_name);
        smbLogin_edit_password = (EditText) layout.findViewById(R.id.smbLogin_edit_password);
        final ImageView imageview_username_delete = (ImageView) layout.findViewById(R.id.imageview_username_delete);
        final ImageView imageview_password_delete = (ImageView) layout.findViewById(R.id.imageview_password_delete);
        final TextView smbLogin_textview_login_help = (TextView)layout.findViewById(R.id.smbLogin_textview_login_help);
        final TextView smbLogin_textview_login_guest = (TextView)layout.findViewById(R.id.smbLogin_textview_login_guest);
        final TextView smbLogin_button_ok = (TextView) layout.findViewById(R.id.smbLogin_button_ok);
        final TextView smbLogin_button_close = (TextView) layout.findViewById(R.id.smbLogin_button_close);

        smbLogin_button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = smbLogin_edit_name.getText().toString();
                String password = smbLogin_edit_password.getText().toString();
                if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
                    setNoPasswordTitle();
                    return;
                }
                new SearchTask().execute(ip, username, password);
            }
        });

        smbLogin_button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        smbLogin_edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    imageview_username_delete.setVisibility(View.VISIBLE);
                } else {
                    imageview_username_delete.setVisibility(View.GONE);
                }
            }
        });


        smbLogin_edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    imageview_password_delete.setVisibility(View.VISIBLE);
                } else {
                    imageview_password_delete.setVisibility(View.GONE);
                }
            }
        });

        imageview_username_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smbLogin_edit_name.setText("");
            }
        });

        imageview_password_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smbLogin_edit_password.setText("");
            }
        });


        smbLogin_textview_login_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HelpFeedBackActivity.class);
                mContext.startActivity(intent);
            }
        });


        smbLogin_textview_login_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGuestTask();
            }
        });

    }

    public void setIpName(String ip, String deviceName) {
        this.ip = ip;
        this.deviceName = deviceName;
        smbLogin_textview_title.setText(deviceName);
    }


    private void requestGuestTask() {
        new SearchTask().execute(ip, "", "");
    }

    public void requestTask(String username, String password) {
        new SearchTask().execute(ip, username, password);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        smbLogin_edit_name.setText("");
        smbLogin_edit_password.setText("");
        resetTitle();
    }

    private void resetTitle(){
        smbLogin_textview_title_linkto.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_black));
        smbLogin_textview_title.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_black));
        smbLogin_textview_title_linkto.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_linkto));
        smbLogin_textview_title.setText(deviceName);
    }

    public void setNoPasswordTitle(){
        smbLogin_textview_title_linkto.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_red));
        smbLogin_textview_title.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_red));
        smbLogin_textview_title_linkto.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_error));
        smbLogin_textview_title.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_no_password));
    }

    public void setNoAuthorityTitle(){
        smbLogin_textview_title_linkto.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_red));
        smbLogin_textview_title.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_red));
        smbLogin_textview_title_linkto.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_error));
        smbLogin_textview_title.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_no_authority));
    }

    public void setErrorPasswordTitle(){
        smbLogin_textview_title_linkto.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_red));
        smbLogin_textview_title.setTextColor(mContext.getResources().getColor(R.color.smb_login_title_red));
        smbLogin_textview_title_linkto.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_error));
        smbLogin_textview_title.setText(mContext.getResources().getString(R.string.mj_share_smb_login_title_error_password));
    }


    class SearchTask extends AsyncTask<String, Void, List<FileItem>> {

        private String ip;
        private String username;
        private String password;
        int error = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mISMBLoginListener != null) {
                mISMBLoginListener.loginStart();
            }
        }

        @Override
        protected List<FileItem> doInBackground(String... params) {
            try {
                ip = params[0];
                username = params[1];
                password = params[2];
                error = 0;
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", username, password);
                SmbFile smbFile;
                if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                    smbFile = new SmbFile(SmbIpUtil.getSmbIp(ip));
                } else {
                    smbFile = new SmbFile(SmbIpUtil.getSmbIp(ip), auth);
                }
                SmbFile[] fs = smbFile.listFiles();
            } catch (SmbAuthException e) {
                e.printStackTrace();
                error = -2;//登录失败
            } catch (MalformedURLException e) {
                e.printStackTrace();
                error = -1;
            } catch (SmbException e) {
                e.printStackTrace();
                error = -1;
            } catch (Exception e) {
                e.printStackTrace();
                error = -1;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<FileItem> fileItems) {
            super.onPostExecute(fileItems);

            if (error == 0) {

                if (mISMBLoginListener != null) {
                    mISMBLoginListener.loginSuccess(ip, deviceName,username,password);
                }

            } else {
//                SharedPreferencesUtil.getInstance().remove(ip);
                if (mISMBLoginListener != null) {
                    mISMBLoginListener.loginError(ip,deviceName);
                }
                if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
                    setNoAuthorityTitle();
                }else{
                    setErrorPasswordTitle();
                }

            }
        }
    }


    ISMBLoginListener mISMBLoginListener;

    public void setISMBLoginListener(ISMBLoginListener listener) {
        this.mISMBLoginListener = listener;
    }
}
