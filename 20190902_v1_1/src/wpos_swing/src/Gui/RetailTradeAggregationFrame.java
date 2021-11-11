package Gui;

import Activity.FragmentActivity;
import Activity.LoginActivity;
import JPanel.RetailTradeAggregationJPanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RetailTradeAggregationFrame extends BaseFrame{

    RetailTradeAggregationJPanel retailTradeAggregationJPanel;

    public RetailTradeAggregationFrame(){
        //设置窗体大小
        setSize(1080, 1920);
        //去除标题栏
        setUndecorated(true);
        //设置可见性
        setVisible(true);

       retailTradeAggregationJPanel = new RetailTradeAggregationJPanel();
        add(retailTradeAggregationJPanel);

        setListener();

    }

    private void setListener() {
        //返回按钮
        retailTradeAggregationJPanel.back.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FragmentActivity.startFragmentActivity();
                RetailTradeAggregationFrame.this.dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                retailTradeAggregationJPanel.back.setBackground(color_455);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                retailTradeAggregationJPanel.back.setBackground(color_ECE);
            }
        });
        //交班按钮
        retailTradeAggregationJPanel.change_shifts.addActionListener(e -> {
            LoginActivity.startLoginActivity();
            RetailTradeAggregationFrame.this.dispose();
        });

    }


}
