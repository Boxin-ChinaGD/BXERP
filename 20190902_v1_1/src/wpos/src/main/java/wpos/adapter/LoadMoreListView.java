package wpos.adapter;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import wpos.model.Commodity;

public class LoadMoreListView  extends ListCell<Commodity> {
    private double lastYposition = 0;

    public LoadMoreListView() {

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("setOnMousePressed");
                lastYposition = event.getSceneY();
            }
        });

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("setOnMouseDragged");
                double newYposition = event.getSceneY();
                double diff = newYposition - lastYposition;
                lastYposition = newYposition;
//                CustomScrollEvent cse = new CustomScrollEvent();
//                cse.fireVerticalScroll((int) diff, LoadMoreListView.this, (EventTarget) event.getSource());
            }
        });
    }
}


//class CustomScrollEvent {
//
//    public void fireVerticalScroll(int deltaY, Object source, EventTarget target){
//
//        ScrollEvent newScrollEvent = null;
//        newScrollEvent = new ScrollEvent(source,
//                target,
//                ScrollEvent.SCROLL,
//                0,
//                0,
//                0,
//                0,
//                false,
//                false,
//                false,
//                false,
//                false,
//                false,
//                0,
//                deltaY,
//                0,
//                0,
//                ScrollEvent.HorizontalTextScrollUnits.CHARACTERS,
//                0,
//                ScrollEvent.VerticalTextScrollUnits.NONE,
//                deltaY,
//                0,
//                null);
//
//        Event.fireEvent(target, newScrollEvent);
//    }
//}