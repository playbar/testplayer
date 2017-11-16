package com.baofeng.mj.vrplayer.http.server;



import android.util.Log;

import com.baofeng.mj.vrplayer.http.bean.HtmlBean;
import com.baofeng.mj.vrplayer.http.constvalue.ConstValue;
import com.baofeng.mj.vrplayer.http.constvalue.HtmlConst;
import com.baofeng.mj.vrplayer.http.util.FileAccessUtil;
import com.baofeng.mj.vrplayer.util.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class SimpleFileServer extends NanoHTTPD {

    public SimpleFileServer(int port) {
        super(port);
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header, Map<String, String> parms,
                          Map<String, String> files) {
        String path = null;
        if (Method.GET.equals(method)) {
            String action = parms.get("action");
            if (action != null) {
                if (action.equals("del")) {
                    String key_id = parms.get("id");
                    try {
                        String filePath = HtmlConst.map.get(key_id).url;
                        File file = new File(filePath);
                        if (file.exists()) {
                            file.delete();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    HtmlConst.map.remove(key_id);
                }
            }
            return new Response(HtmlConst.getNewHtml());
        } else {
            for (String s : files.keySet()) {
                try {
                  //  Log.v("mm21111111","path file get:"+files.get(s));
                    FileInputStream fis = new FileInputStream(files.get(s));
                    path = ConstValue.BASE_DIR + "/" + parms.get("file");
                   // Log.v("mm21111111","path:"+path);
                   // Log.v("mm2","path:"+FileAccessUtil.getFile(path));
                    FileOutputStream fos = new FileOutputStream(FileAccessUtil.getFile(path));
                    byte[] buffer = new byte[1024*500];
                    while (true) {
                        int byteRead = fis.read(buffer);
                        if (byteRead == -1) {
                            break;
                        }
                        fos.write(buffer, 0, byteRead);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            HtmlBean bean = new HtmlBean();
            bean.id = MD5.getMD5(path);
            bean.name = path.substring(path.lastIndexOf("/"));
            bean.url = path;
            if(HtmlConst.map!=null){
                HtmlConst.map.put(bean.id, bean);
            }
            return new Response(HtmlConst.getNewHtml());
        }
    }
}
