package com.baofeng.mj.vrplayer.business.flyscreen.util;

import java.io.File;
import java.util.Stack;

/**
 * ClassName: FileDirStack <br/>
 * @author qiguolong
 * @date: 2015-9-1 下午4:03:23 <br/>
 * @description: 目录操作
 */
public class FileDirStack {
	private Stack<String> dirStack = new Stack<String>();
	private String dir = File.separator;

	public void push(String dir) {
		dirStack.push(dir);
	}

	public String getCurDir() {
		return dirStack.peek();
	}

	public void clear() {
		dirStack.clear();
	}

	public boolean isEmpty() {
		return dirStack.isEmpty();
	}

	/**
	 * @author qiguolong @Date 2015-9-1 下午4:13:31
	 * @description:{根目录返回false
	 * @return
	 */
	public boolean back() {
		if (!dirStack.isEmpty()) {
			String dir=dirStack.pop();
		}
		return !isEmpty();

	}

	public int getSize(){
		return dirStack.size();
	}

	/**
	 * @author qiguolong @Date 2015-9-1 下午4:16:21
	 * @description:{返回上级目录
	 * @return
	 */
	public String getBackDir() {
		if (back()) {
			return getCurDir();
		} else {
			return "";
		}
	}
}
