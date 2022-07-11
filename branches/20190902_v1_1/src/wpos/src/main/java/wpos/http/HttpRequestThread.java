package wpos.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wpos.bo.NtpHttpBO;
import wpos.common.GlobalController;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HttpRequestThread extends Thread {
    private Log log = LogFactory.getLog(this.getClass());
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
