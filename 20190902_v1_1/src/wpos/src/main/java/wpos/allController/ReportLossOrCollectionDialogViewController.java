package wpos.allController;

import javafx.fxml.FXML;

public class ReportLossOrCollectionDialogViewController {

    private ButtonClickListener listener;

    @FXML
    private void removeCommodity_click(){
        listener.onRemoveCommodity();
    }

    public void setListener(ButtonClickListener listener) {
        this.listener = listener;
    }

    interface ButtonClickListener{
        void onRemoveCommodity();
    }
}
