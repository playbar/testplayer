package com.mojing.vrplayer.publicc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wanghongfang on 2016/10/20.
 */
public class ThreadProxy {
    private static ThreadProxy mInstance;
    private ExecutorService cachedThreadPool;
    private ThreadProxy(){
        cachedThreadPool = Executors.newFixedThreadPool(1);
    }

    public static ThreadProxy getInstance(){
        if(mInstance==null){
            mInstance = new ThreadProxy();
        }
        return mInstance;
    }

    public void addRun(final IHandleThreadWork handleThreadWork){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                handleThreadWork.doWork();
            }
        });
    }

    public void addRunDelay(final IHandleThreadWork handleThreadWork, final int delayTime){
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delayTime);
                }catch (Exception e){
                    e.printStackTrace();
                }

                handleThreadWork.doWork();
            }
        });
    }

    public interface IHandleThreadWork{
        void doWork();
    }
}
