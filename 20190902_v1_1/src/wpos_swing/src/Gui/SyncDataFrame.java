package Gui;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Activity.FragmentActivity;
import JPanel.ImageRotateJpanel;

public class SyncDataFrame extends BaseFrame {
    Timer timer;

    public SyncDataFrame() {
        //去除标题栏
        setUndecorated(true);
        //设置可见性
        setVisible(true);
        //设置布局
        setLayout(new BorderLayout());
        //添加关闭按钮
        this.add(closeJpanel(), BorderLayout.NORTH);

        //将窗口分隔成上下俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, topJpanel(), bottomJpanel());
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(200);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(0);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);
        jSplitPane.setBorder(new MatteBorder(0, 0, 0, 0, Color.white));
        //加入到面板中就好了
        this.add(jSplitPane, BorderLayout.CENTER);

        timer = new Timer(2000, e -> {
            FragmentActivity.startFragmentActivity();
            timer.stop();
            this.dispose();
        });
        timer.start();
    }

    private JPanel topJpanel() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(color_F0F);
        //设置布局
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(new MatteBorder(0, 0, 2, 0, color_DBD));

        //顶部文本框
        JTextField textField = new JTextField("正在同步数据");
        //字体
        textField.setFont(normalFont_30);
        //居右对齐
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        //边距
        textField.setMargin(new Insets(20, 20, 20, 20));
        //背景
        textField.setBackground(color_F0F);
        //不可编辑
        textField.setEditable(false);
        //白色边框
        textField.setBorder(new MatteBorder(0, 20, 0, 0, color_F0F));

        //添加文本框
        jPanel.add(textField, BorderLayout.WEST);
        return jPanel;
    }

    private JPanel bottomJpanel() {
        // TODO: 2020/5/20 这里面有个timer，需要在此页面消失时释放掉
        JPanel jPanel = new ImageRotateJpanel(getImageIcon(getClass().getResource("/image/sync_data.png"), 200, 200).getImage());
        jPanel.setBackground(Color.white);

        return jPanel;
    }

}

