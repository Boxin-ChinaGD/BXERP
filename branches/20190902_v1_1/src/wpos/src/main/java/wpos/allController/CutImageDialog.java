package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class CutImageDialog {
    @FXML
    public ImageView image;
    @FXML
    public Rectangle cutSize;

    JFXAlert alert;
    SureClickListener listener;

    public void onCreate(){
        //缩放
        cutSize.setOnScroll(event -> {
            double zoomNumber = 1.05;
            double deltaY = event.getDeltaY();

            if (deltaY < 0) {
                zoomNumber = 0.95;
            }
            if (cutSize.getScaleX() * cutSize.getWidth()<=580) {
                cutSize.setScaleX(cutSize.getScaleX() * zoomNumber);
                cutSize.setScaleY(cutSize.getScaleY() * zoomNumber);
            }else {
                cutSize.setScaleX(1);
                cutSize.setScaleY(1);
            }
            event.consume();
        });

        DragHandler dragHandler = new DragHandler(cutSize);

        cutSize.setOnMousePressed(dragHandler);
        cutSize.setOnMouseDragged(dragHandler);
    }

    @FXML
    private void cancel_click(){
        alert.close();
    }

    @FXML
    private void sure_click(){
        Bounds boundsInParent = cutSize.getBoundsInParent();
        System.out.println("宽度："+cutSize.getScaleX() * cutSize.getWidth());
        System.out.println("高度："+cutSize.getScaleY() * cutSize.getHeight());
        listener.onClick(boundsInParent.getMinX(),boundsInParent.getMinY(),cutSize.getScaleX() * cutSize.getWidth(),cutSize.getScaleY() * cutSize.getHeight());
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void setListener(SureClickListener listener) {
        this.listener = listener;
    }

    interface SureClickListener{
        void onClick(double x,double y,double width,double height);
    }

    class DragHandler implements EventHandler<MouseEvent> {

        private final Rectangle rect;//primaryStage为start方法头中的Stage
        private double oldX;
        private double oldY;

        public DragHandler(Rectangle rect) {//构造器
            this.rect = rect;
        }

        @Override
        public void handle(MouseEvent e) {
            System.out.println(e.getEventType());
            if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {    //鼠标按下的事件
                this.oldX = e.getScreenX();
                this.oldY = e.getScreenY();
            } else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {  //鼠标拖动的事件
                this.rect.setTranslateX(e.getScreenX() - oldX);
                this.rect.setTranslateY(e.getScreenY() - oldY);
            }
        }
    }

}
