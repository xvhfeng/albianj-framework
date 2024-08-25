package com.geek94.sync;

public interface IMonitorRunnable extends  Runnable{

     void shutdown();

     void statusPrint(String status);
}
