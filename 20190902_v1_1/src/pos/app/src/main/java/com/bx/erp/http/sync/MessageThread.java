package com.bx.erp.http.sync;

import android.os.AsyncTask;
import android.os.Handler;

public class MessageThread extends AsyncTask {
    public Handler handler;
    public MessageThread(Handler handler) {
        this.handler = handler;
    }

//    @Override
//    public void run() {
//        System.out.println(Thread.currentThread());
//        handler.getLooper().quit();
//    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }
}
