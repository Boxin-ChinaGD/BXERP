package Activity;

import Gui.RetailTradeAggregationFrame;

import javax.swing.*;
import java.awt.*;

public class RetailTradeAggregationActivity {
    private static RetailTradeAggregationActivity retailTradeAggregationActivity;
    private JFrame frame;

    public static void startRetailTradeAggregationActivity(){
        retailTradeAggregationActivity = new RetailTradeAggregationActivity();
        retailTradeAggregationActivity.frame.setVisible(true);
    }

    private RetailTradeAggregationActivity() {
        initialize();
    }

    private void initialize() {
        frame = new RetailTradeAggregationFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //获取屏幕宽高,将窗口大小设置为屏幕的2/3
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float proportionW = screenSize.width;
        float proportionH = screenSize.height;
        frame.setSize((int)proportionW,(int)proportionH);

        //设置窗口居中显示
        frame.setLocationRelativeTo(null);
    }

}
