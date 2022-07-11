package Gui;

import Activity.LoginActivity;
import Interface.IFloginButton;
import JPanel.PreSaleJPanel;

public class PresaleFrame extends BaseFrame{

    //构造方法
    public PresaleFrame() {
        //设置窗体大小
        setSize(1080, 1920);
        //去除标题栏
        setUndecorated(true);
        //设置可见性
        setVisible(true);
        // 设置窗体透明度
        com.sun.awt.AWTUtilities.setWindowOpacity(this, 1.0f);

        PreSaleJPanel preSaleJPanel = new PreSaleJPanel();

        add(preSaleJPanel);

        preSaleJPanel.logout.addActionListener(e -> {
            this.dispose();
            LoginActivity.startLoginActivity();
        });
    }





}
