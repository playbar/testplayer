package com.baofeng.mj.vrplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baofeng.mj.vrplayer.R;
import com.baofeng.mj.vrplayer.bean.Resource;
import com.baofeng.mj.vrplayer.util.GlideUtil;
import com.baofeng.mojing.MojingSDKReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 飞屏视频列表
 */
public class FlyScreenVideoListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Resource.PageItem> dataList;
    private LayoutInflater inflater;

    public FlyScreenVideoListAdapter(Context context, List<Resource.PageItem> dataList) {
        mContext = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.flyscreen_video_item, null);
            holder = new ViewHolder();
            holder.fileImage = new WeakReference<ImageView>((ImageView) convertView.findViewById(R.id.iv_file_image));
            holder.fileName = (TextView) convertView.findViewById(R.id.tv_file_name);
            holder.videoPlay = (TextView) convertView.findViewById(R.id.tv_video_play);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Resource.PageItem pageItem = dataList.get(position);
        String videoName = stringFilter(pageItem.getName());
        showFlyScreenTitle(holder.fileName, videoName, 12);//显示文件名
        if (pageItem.getBDir()) {
            GlideUtil.displayImage(mContext, holder.fileImage, R.mipmap.flyscreen_folder_icon, R.mipmap.flyscreen_video_icon);
            holder.videoPlay.setVisibility(View.GONE);
            int width = holder.fileImage.get().getLayoutParams().width;
            holder.fileImage.get().getLayoutParams().height = width;
        } else {
            GlideUtil.displayImage(mContext, holder.fileImage, R.mipmap.flyscreen_video_icon, R.mipmap.flyscreen_video_icon);
//            v.videoPlay.setVisibility(View.VISIBLE);
//            final String path = FlyScreenUtil.getResUri(
//                    FlyScreenUtil.urlEncode(pageItem.getUri()), FlyScreenBusiness.getInstance().getCurrentDevice());
//
//           final boolean hasSub = pageItem.getSubtitleType()>0 ?true:false; //是否有字幕文件
//
//            v.videoPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    reportClick("preview");
//                    Intent intent = new Intent(mContext, MediaGlActivity.class);
//                    intent.putExtra("videoPath", path);
//                    intent.putExtra("videoName", videoName);
//                    intent.putExtra("hasSub",hasSub);
//                    mContext.startActivity(intent);
//                }
//            });
        }

        return convertView;
    }

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 飞屏显示title
     */
    private void showFlyScreenTitle(TextView tv_title , String title, int lengthLimit){
        int lastIndex = title.lastIndexOf(".");
        if(lastIndex > 0){
            String titlePrefix = title.substring(0, lastIndex);
            String titleSuffix = title.substring(lastIndex);
            int titlePrefixLength = titlePrefix.length();
            if(titlePrefixLength <= lengthLimit){
                tv_title.setText(titlePrefix + titleSuffix);
            }else{
                tv_title.setText(titlePrefix.substring(0, lengthLimit - 5) + "..." + titlePrefix.substring(titlePrefixLength - 4) + titleSuffix);
            }
        }else{
            tv_title.setText(title);
        }
    }

    //click 报数
    private void reportClick(String airvideohelp){
        try {
            JSONObject reportClick = new JSONObject();
            reportClick.put("etype", "click");
            reportClick.put("tpos", "1");
            reportClick.put("pagetype", "airvideo");
            reportClick.put("clicktype", "chooseitem");
            reportClick.put("local_menu_id", "3");
            reportClick.put("airevideohelp", airvideohelp);
            MojingSDKReport.onEvent(reportClick.toString(), "UNKNOWN", "UNKNOWN", 0, "UNKNOWN", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ViewHolder {
        WeakReference<ImageView> fileImage;
        TextView fileName;
        TextView videoPlay;
    }
}
