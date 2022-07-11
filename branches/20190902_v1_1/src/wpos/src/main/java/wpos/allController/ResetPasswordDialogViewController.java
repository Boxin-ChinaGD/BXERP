package wpos.allController;

import com.jfoenix.controls.JFXAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import wpos.listener.resetPasswordDialogListener;
import wpos.utils.FieldFormat;
import wpos.utils.ToastUtil;


public class ResetPasswordDialogViewController {

    private JFXAlert alert;
    private resetPasswordDialogListener listener;

    @FXML
    private TextField confirmPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField oldPassword;

    @FXML
    private void confirm(){
        String oldPasswordString = oldPassword.getText();
        String newPasswordString = newPassword.getText();
        String confirmPasswordString = confirmPassword.getText();
        listener.resetPasswordDialogConfirmClick(oldPasswordString, newPasswordString, confirmPasswordString);
    }

    public void cleanTextFiled(){
        newPassword.setText("");
        confirmPassword.setText("");
        oldPassword.setText("");
    }

    public void setAlert(JFXAlert alert) {
        this.alert = alert;
    }

    public void setListener(resetPasswordDialogListener listener) {
        this.listener = listener;
    }

    public void addPasswordFieldListener() {
        newPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > FieldFormat.MAX_LENGTH_Password) {
                    newPassword.setText(oldValue);
                    ToastUtil.toast("密码太长！", ToastUtil.LENGTH_SHORT);
                }
            }
        });

        confirmPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > FieldFormat.MAX_LENGTH_Password) {
                    confirmPassword.setText(oldValue);
                    ToastUtil.toast("密码太长！", ToastUtil.LENGTH_SHORT);
                }
            }
        });
    }
}
