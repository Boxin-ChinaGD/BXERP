package wpos.utils;

import javafx.application.Platform;
import wpos.listener.PlatFormHandlerMessage;
import wpos.model.Message;

public class PlatForm {

    private static PlatForm platForm;
    private static PlatFormHandlerMessage platFormHandlerMessage;

    private PlatForm(){}

    public static PlatForm get(){
        if (platForm == null){
            synchronized (PlatForm.class){
                if (platForm == null){
                    platForm = new PlatForm();
                }
            }
        }
        return platForm;
    }

    public void setHandlerMessage(PlatFormHandlerMessage platFormHandlerMessage) {
        this.platFormHandlerMessage = platFormHandlerMessage;
    }

    public void unRegister(){
        platFormHandlerMessage = null;
    }

    public void sendMessage(Message message){
        if (platFormHandlerMessage != null) {
            runLater(message);
        }else {
            System.out.println("当前暂无注册监听！");
        }
    }

    private void runLater(Message message){
        Platform.runLater(new runnable(message));
    }

    static class runnable implements Runnable{
        Message message;
        public runnable(Message message){
            this.message = message;
        }

        @Override
        public void run() {
            platFormHandlerMessage.handleMessage(message);
        }
    }

}
