package com.geek94.sync;


import com.geek94.langs.Throws;

import java.util.concurrent.*;

/**
 * Thread pools
 * if you use thread pool
 * then new Class extends FreeMonitor(Good Idea) or impl IMonitorRunnable(Next Choose)
 * and impl method:statusPrint with your log method or console print method
 */
public class ThreadPool {
    ThreadPoolExecutor executorPool = null;
   IMonitorRunnable monitor = null;
    public ThreadPool(int coreNumbs,int maxNumbs,int keepAlive,int cacheQueueNumbs,IMonitorRunnable monitor) {
        //RejectedExecutionHandler implementation
//        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        //Get the ThreadFactory implementation to use
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        //creating the ThreadPoolExecutor
        executorPool = new ThreadPoolExecutor(coreNumbs,maxNumbs,keepAlive ,TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(cacheQueueNumbs),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        //start the monitoring thread
        this.monitor = monitor;
//        monitor = new ThreadMonitor(executorPool, monitorInterval);
        Thread monitorThread = new Thread(monitor);
        monitorThread.setName("ThreadPoolMonitor");
        monitorThread.start();
    }


    public boolean addTask(Object sessionId,Runnable runnable){
        try {
            if(executorPool.isShutdown() || executorPool.isTerminated() || executorPool.isTerminating()){
                return false;
            }
            executorPool.execute(runnable);
            return true;
        }catch (RejectedExecutionException ree) {
            Throws.again(ree,"add task to threadpool is fail,maybe task is flow.");
            return  false;
        }
    }

    public void shutdown(){
        try {
            executorPool.shutdown();
            //shut down the monitor thread
            Thread.sleep(5000);
            monitor.shutdown();
        }catch (Throwable t){

        }
    }
}
