package Activity;

import Gui.FragmentFrame;

import javax.swing.*;
import java.awt.*;

public class FragmentActivity {
    private JFrame frame;
    private static FragmentActivity fragmentActivity;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                  FragmentActivity.startFragmentActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void startFragmentActivity(){
        if (fragmentActivity == null) {
            synchronized (FragmentActivity.class) {
                if (fragmentActivity == null) {
                    fragmentActivity = new FragmentActivity();
                }
            }
        }
        fragmentActivity.frame.setVisible(true);
    }

    private FragmentActivity(){
        initialize();
    }

    private void initialize() {
        frame = new FragmentFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //获取屏幕宽高,将窗口大小设置为满屏
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float proportionW = screenSize.width;
        float proportionH = screenSize.height;
        frame.setSize((int)proportionW,(int)proportionH);

        //设置窗口居中显示
        frame.setLocationRelativeTo(null);
    }

}
