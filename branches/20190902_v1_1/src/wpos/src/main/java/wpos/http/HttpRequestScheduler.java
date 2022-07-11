package wpos.http;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpRequestScheduler {
    private AtomicInteger aiThreadSignal;

    private HttpRequestThread hrt;

    public HttpRequestScheduler() {
        aiThreadSignal = new AtomicInteger();
        hrt = new HttpRequestThread();
    }

    public void start() {
        HttpRequestManager.register(HttpRequestManager.EnumDomainType.EDT_Communication, hrt);

        hrt.setAtomicInteger(aiThreadSignal);
        hrt.setName("底层通信线程");
        hrt.start();
    }

    public void stop() {
        aiThreadSignal.set(HttpRequestThread.SIGNAL_ThreadExit);
        synchronized(hrt) {
            hrt.notify();
        }
        try {
            hrt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
