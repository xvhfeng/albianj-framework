package com.geek94.sync;


import java.util.concurrent.ThreadPoolExecutor;

public abstract class FreeMonitorThread implements IMonitorRunnable {

    private ThreadPoolExecutor executor;

    private int seconds;

    private boolean run=true;

    public FreeMonitorThread(ThreadPoolExecutor executor, int interval) {
        this.executor = executor;
        this.seconds= interval;
    }

    public void shutdown(){
        this.run = false;
    }


    @Override
    public void run()
    {
        while(run){
            try {
                String msg = String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                        this.executor.getPoolSize(),
                        this.executor.getCorePoolSize(),
                        this.executor.getActiveCount(),
                        this.executor.getCompletedTaskCount(),
                        this.executor.getTaskCount(),
                        this.executor.isShutdown(),
                        this.executor.isTerminated());
                statusPrint( msg );
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {

            }
        }

    }
}