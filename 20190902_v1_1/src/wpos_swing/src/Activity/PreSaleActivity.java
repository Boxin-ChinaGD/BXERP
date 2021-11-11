package Activity;

import Gui.LoginFrame;
import Gui.PresaleFrame;

import javax.swing.*;
import java.awt.*;

public class PreSaleActivity {
    //本窗口的frame
    private JFrame frame;
    //上一个登录窗口的frame
    private JFrame lastframe;

    public static void startPreSaleActivity(JFrame frame){
        PreSaleActivity preSaleActivity = new PreSaleActivity(frame);
    }


    private PreSaleActivity(JFrame frame) {
        lastframe = frame;
        initialize();
    }

    private void initialize() {
        frame = new PresaleFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //获取屏幕宽高,将窗口大小设置为屏幕的2/3
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float proportionW = screenSize.width*2/3;
        float proportionH = screenSize.height*2/3;
        frame.setSize((int)proportionW,(int)proportionH);

        //设置窗口居中显示
        frame.setLocationRelativeTo(null);
    }

}
