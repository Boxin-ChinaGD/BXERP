package com.bx.erp.http;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.helper.Constants;
//import com.bx.erp.view.activity.LoginActivity;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import okhttp3.OkHttpClient;

public class HttpRequestThread extends Thread {
    private Logger log = Logger.getLogger(this.getClass());
    public final static int SIGNAL_ThreadExit = 1;

    private AtomicInteger atomicInteger;

    public void setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    protected ReentrantReadWriteLock lock;
    protected Queue<HttpRequestUnit> queue;

    public void pushHttpRequest(HttpRequestUnit hu) {
        if (hu != null) {
            lock.writeLock().lock();
            queue.offer(hu);
            lock.writeLock().unlock();
        }
        synchronized (this) {
            notify();
        }
    }

    public HttpRequestThread() {
        lock = new ReentrantReadWriteLock();
        queue = new LinkedList<HttpRequestUnit>();
    }

    @Override
    public void run() {
        while (atomicInteger.get() != SIGNAL_ThreadExit) {
            lock.writeLock().lock();
            doTask();
            lock.writeLock().unlock();
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("线程：" + this.getName() + "已经退出");
    }

    protected void doTask() {
        while (!queue.isEmpty()) {
            lock.writeLock().lock();
            HttpRequestUnit hu = queue.poll();
            if (hu == null) {
                lock.writeLock().unlock();
                break;
            }
            hu.setDateStart(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
            GlobalController.client.newCall(hu.getRequest()).enqueue(hu);
            lock.writeLock().unlock();
        }
    }
}
