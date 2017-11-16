package com.baofeng.mj.vrplayer.http.constvalue;


import com.baofeng.mj.vrplayer.http.bean.HtmlBean;

import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class HtmlConst {
	public static String text;


	public static String html;
	public static LinkedHashMap<String,HtmlBean> map;

	public static void initMap(){
		map = new LinkedHashMap();
	}

	public static void cleanMap(){
		if(map!=null){
			map.clear();
			map = null;
		}

	}
	public static String addListToHtml(){
		StringBuffer sb = new StringBuffer();
		if(map==null){
			return sb.toString();
		}
		Iterator iterator = map.keySet().iterator();
		for(int i = 0;i<map.keySet().size();i++){
			if(iterator.hasNext()){
				HtmlBean bean = map.get(iterator.next());
				sb.append(
						"\t\t\t<tr>\n" +
								"\t\t\t\t<td>"+bean.name+"</td>\n" +
								"\t\t\t\t<td class=\"tc\"><a href=\"javascript:;\" onclick=\"del('"+bean.id+"', this)\">删除</a></td>\n" +
								"\t\t\t</tr>\n"
				);
			}
		}
		return sb.toString();
	}


	public static String getNewHtml(){
		return html = "\n" +
				"<!doctype html>\n" +
				"<html>\n" +
				"<head>\n" +
				"\t<meta charset=\"utf-8\">\n" +
				"\t<title>文件上传</title>\n" +
				"\t<meta name=\"keywords\" content=\"keywords\">\n" +
				"\t<meta name=\"description\" content=\"description\">\n" +
				"\t<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge, chrome=1\">\n" +
				"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0\">\n" +
				"\t<link rel=\"shortcut icon\" href=\"/static/favicon.ico\">\n" +
				"\t<style>\n" +
				"    html { background: #f5f5f5; font-size: 14px; }\n" +
				"    body { padding: 0%; color: #666666; margin: 0; }\n" +
				"    \n" +
				"    .upload-box { padding: 1%; max-width: 960px; margin: 0 auto; }\n" +
				"    .upload-box table { width: 100%; border-collapse: separate; border-spacing: 1px; background: #dddddd; }\n" +
				"    .upload-box table td, table th { background: #ffffff; padding: 10px 3%; }\n" +
				"    .upload-box table thead th { background-color: #f8f8f8; font-size: 14px; font-weight: normal; color: #888888; text-align: left; }\n" +
				"    .upload-box table a { font-size: 14px; text-decoration: none; color: #444444; transition: all 300ms ease-in-out; display: block; border: 1px solid #2d9a7f; background-color: #48c9a9; color: #ffffff; border-radius: 2px; }\n" +
				"    .upload-box table a:hover { background-color: #37bc9b; }\n" +
				"    .upload-box table tbody td { transition: all 300ms ease-in-out; }\n" +
				"    .upload-box table tbody tr:hover td { background: #f2f7f6; }\n" +
				"    .upload-box .tc { text-align: center; }\n" +
				"    \n" +
				"    .btn-submit { width: 120px; height: 38px; border: 1px #2d9a7f solid; background-color: #48c9a9; color: #ffffff; border-radius: 2px; cursor: pointer; outline: inherit; float: right; }\n" +
				"    .btn-submit:hover { background-color: #37bc9b; }\n" +
				"    .btn-submit:active { background-color: #2e9e83; box-shadow: 4px 4px 8px rgba(0,0,0,0.2) inset; }\n" +
				"    .btn-submit:disabled, .btn-submit:active:disabled { box-shadow: none; background-color: #77cdb8; border-color: #77cdb8; color: #d7eee8; }\n" +
				"    \n" +
				"    .btn-choose { width: 120px; height: 36px; border: 1px #cccccc solid; background-color: #f0f0f0; color: #666666; border-radius: 2px; cursor: pointer; outline: inherit; }\n" +
				"    .btn-choose:hover { background-color: #eaeaea; }\n" +
				"    .btn-choose:active { box-shadow: 4px 4px 8px rgba(0,0,0,0.2) inset; }\n" +
				"    .btn-choose:disabled, .btn-default:active:disabled { background-color: #efefef; border-color: #e3e3e3; box-shadow: none; color: #999999; }\n" +
				"    \n" +
				"    .upload-form { height: 38px; border: 1px #dddddd solid; background: #eeeeee; border-bottom: 0; padding: 20px 2%; position: relative; }\n" +
				"    .upload-input { width: 80%; position: relative; height: 36px; border: 1px #2d9a7f solid; background: #ffffff; float: left; border-radius: 3px; overflow: hidden; }\n" +
				"    .upload-input input[type=\"file\"] { width: 100%; height: 36px; opacity: 0; }\n" +
				"    .upload-input input[name=\"path\"] { width: 96%; height: 100%; border: 0; position: absolute; left: 0; top: 0; padding: 0 2%; }\n" +
				"    .btn-choose { float: none; border: 0; border-radius: 0; border-left: 1px #2d9a7f solid; position: absolute; right: 0; top: 0; pointer-events: none; }\n" +
				"    \n" +
				"    \n" +
				"</style>\n" +
				"</head>\n" +
				"\n" +
				"<body>\n" +
				"<div class=\"upload-box\">\n" +
				"\t<div class=\"upload-form\">\n" +
				"\t\t<form id=\"uploadform\" name=\"uploadform\" method=\"post\" enctype=\"multipart/form-data\" action=\"\">\n" +
				"\t\t\t<div class=\"upload-input\">\n" +
				"\t\t\t\t<input type=\"text\" name=\"path\" id=\"path\" placeholder=\"没有选择文件\">\n" +
				"\t\t\t\t<input type=\"file\" name=\"file\" id=\"file\" onchange=\"getFilePath(this)\">\n" +
				"\t\t\t\t<button type=\"button\" class=\"btn-choose\">请选择文件</button>\n" +
				"\t\t\t</div>\n" +
				"\t\t\t<button type=\"submit\" class=\"btn-submit\">上传文件</button>\n" +
				"\t\t</form>\n" +
				"\t</div>\n" +
				"\t<div class=\"files\">\n" +
				"\t\t<table>\n" +
				"\t\t\t<thead>\n" +
				"\t\t\t<tr>\n" +
				"\t\t\t\t<th width=\"90%\">文件名</th>\n" +
				"\t\t\t\t<th width=\"10%\" class=\"tc\">操作</th>\n" +
				"\t\t\t</tr>\n" +
				"\t\t\t</thead>\n" +
				"\t\t\t<tbody>\n" +
				addListToHtml() +
				"\t\t\t</tbody>\n" +
				"\t\t</table>\n" +
				"\t</div>\n" +
				"</div>\n" +
				"<script>\n" +
				"        function getFilePath(file){\n" +
				"            document.getElementById('path').value = file.value;\n" +
				"            document.getElementById('uploadform').action ='?uploadFilePath='+window.encodeURI(window.encodeURI(file.value)); \n" +
				"        };\n" +
				"        function del(id, link){\n" +
				"            ajax({ \n" +
				"                type: 'GET', \n" +
				"                url: '?action=del&id=' + id, \n" +
				"                dataType: 'json', \n" +
				"                beforeSend: function(){ }, \n" +
				"                success: function(msg){\n" +
				"                    var tr = link.parentNode.parentNode;\n" +
				"                    tr.parentNode.removeChild(tr);\n" +
				"                    alert('删除成功'); \n" +
				"                }, \n" +
				"                error: function(){ \n" +
				"                    alert('删除文件出错！');\n" +
				"                } \n" +
				"            });\n" +
				"            \n" +
				"        };\n" +
				"        function ajax(){ \n" +
				"            var ajaxData = { \n" +
				"                type: arguments[0].type || 'GET', \n" +
				"                url: arguments[0].url || '', \n" +
				"                async: arguments[0].async || 'true', \n" +
				"                data: arguments[0].data || null, \n" +
				"                dataType: arguments[0].dataType || 'text', \n" +
				"                contentType: arguments[0].contentType || 'application/x-www-form-urlencoded', \n" +
				"                beforeSend: arguments[0].beforeSend || function(){}, \n" +
				"                success: arguments[0].success || function(){}, \n" +
				"                error: arguments[0].error || function(){} \n" +
				"            };\n" +
				"            ajaxData.beforeSend();\n" +
				"            \n" +
				"            var xhr = createxmlHttpRequest();  \n" +
				"            xhr.responseType=ajaxData.dataType; \n" +
				"            xhr.open(ajaxData.type,ajaxData.url,ajaxData.async);  \n" +
				"            xhr.setRequestHeader('Content-Type',ajaxData.contentType);  \n" +
				"            xhr.send(convertData(ajaxData.data));  \n" +
				"            xhr.onreadystatechange = function() {  \n" +
				"                if (xhr.readyState == 4) {  \n" +
				"                    if(xhr.status == 200){ \n" +
				"                        ajaxData.success(xhr.response) \n" +
				"                    }else{ \n" +
				"                        ajaxData.error() \n" +
				"                    }  \n" +
				"                } \n" +
				"            }; \n" +
				"        };\n" +
				"\n" +
				"        function createxmlHttpRequest() {  \n" +
				"            if (window.ActiveXObject) {  \n" +
				"                return new ActiveXObject('Microsoft.XMLHTTP');  \n" +
				"            } else if (window.XMLHttpRequest) {  \n" +
				"                return new XMLHttpRequest();  \n" +
				"            }  \n" +
				"        }; \n" +
				"\n" +
				"        function convertData(data){ \n" +
				"            if(typeof data === 'object'){ \n" +
				"                var convertResult = '' ;  \n" +
				"                for(var c in data){  \n" +
				"                    convertResult+= c + '=' + data[c] + '&';  \n" +
				"                }  \n" +
				"                convertResult = convertResult.substring(0, convertResult.length-1);\n" +
				"                return convertResult; \n" +
				"            } else { \n" +
				"                return data; \n" +
				"            } \n" +
				"        };\n" +
				"    </script>\n" +
				"</body>\n" +
				"</html>";
	}
}
