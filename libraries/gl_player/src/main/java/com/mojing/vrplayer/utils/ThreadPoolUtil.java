package com.mojing.vrplayer.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池工具类
 */
public class ThreadPoolUtil {
	private static ExecutorService cachedThreadPool = null;

	public static void runThread(Runnable runnable) {
		if (cachedThreadPool == null) {
			cachedThreadPool = Executors.newCachedThreadPool();
		}
		cachedThreadPool.execute(runnable);
	}

	/**
	 * 清理线程池
	 */
	public static void clear() {
		if (cachedThreadPool != null) {
			cachedThreadPool.shutdownNow();
			cachedThreadPool = null;
		}
	}
}
