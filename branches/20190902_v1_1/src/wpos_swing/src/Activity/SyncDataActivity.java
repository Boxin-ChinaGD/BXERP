package Activity;

import Gui.SyncDataFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SyncDataActivity {

    private JFrame frame;

    public static void startSyncDataActivity() {
        SyncDataActivity syncDataActivity = new SyncDataActivity();
    }

    /**
     * Create the application.
     */
    private SyncDataActivity() {

        initialize();

        frame.setVisible(true);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new SyncDataFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //获取屏幕宽高,将窗口大小设置为满屏
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float proportionW = screenSize.width;
        float proportionH = screenSize.height;
        frame.setSize((int) proportionW, (int) proportionH);

        //设置窗口居中显示
        frame.setLocationRelativeTo(null);

    }

}
