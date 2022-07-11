package Gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class BaseFrame extends JFrame {
    //字体
    final Font normalFont_15 = new Font("宋体", 1, 15);
    final Font normalFont_30 = new Font("宋体", 1, 30);
    final Font blackFont_15 = new Font("微软雅黑", 1, 15);
    final Font blackFont_20 = new Font("微软雅黑", 1, 20);
    final Font blackFont_30 = new Font("微软雅黑", 1, 30);

    //颜色
    final Color color_1A4 = new Color(Integer.decode("#1A4A9F")); //深蓝色
    final Color color_219 = new Color(Integer.decode("#2196F3"));//蓝色
    final Color color_263 = new Color(Integer.decode("#263238"));//蓝色 用来当侧边栏背景
    final Color color_455 = new Color(Integer.decode("#455A64"));//蓝色 item获取焦点时背景颜色
    final Color color_E3F = new Color(Integer.decode("#E3F2FD"));//浅蓝色 列表解释文本背景
    final Color color_DBD = new Color(Integer.decode("#DBDBDB"));//灰色，用作分割线
    final Color color_ECE = new Color(Integer.decode("#ECEFF1"));//fragment的底部背景
    final Color color_F0F = new Color(Integer.decode("#F0F0F0"));//灰色，背景色
    final Color color_9E9 = new Color(Integer.decode("#9E9E9E"));//灰色，搜索按钮背景色
    final Color color_00B = new Color(Integer.decode("#00BFA5"));//绿色，同步按钮背景色
    final Color color_F44 = new Color(Integer.decode("#F44336"));//红色，字体颜色

    //承接关闭图标
    JButton closeButton = new JButton();

    //图标地址
    URL closeUrl = getClass().getResource("/image/delete_all.png");

    //设置关闭窗口
    protected JPanel closeJpanel() {
        //创建容器JPanel
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.white);
        //设置布局居右对齐
        jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        MatteBorder matteBorder = new MatteBorder(0, 0, 10, 0, Color.white);
        jPanel.setBorder(matteBorder);

        //关闭窗口按钮
        Border border = BorderFactory.createEmptyBorder(0, 1, 0, 1);
        //添加图标
        closeButton.setIcon(getImageIcon(closeUrl, 32, 32));
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(50, 30));
        closeButton.setBackground(Color.WHITE);
        closeButton.setBorder(border);
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(color_1A4);
            }

            public void mouseExited(MouseEvent e) {
                // TODO 自动生成的方法存根
                closeButton.setBackground(Color.WHITE);
            }
        });
        closeButton.addActionListener((e) -> {
            this.dispose();
        });
        jPanel.add(closeButton);

        return jPanel;
    }

    //生成适应控件大小的图片
    public ImageIcon getImageIcon(URL path, int width, int height) {
        if (width == 0 || height == 0) {
            return new ImageIcon(path);
        }
        ImageIcon icon = new ImageIcon(path);
        icon.setImage(icon.getImage().getScaledInstance(width, height,
                Image.SCALE_DEFAULT));
        return icon;
    }

    //生成列表解释文本框
    protected class categoryTextFiled extends JTextField {
        public categoryTextFiled(String s) {
            setFont(blackFont_15);
            setText(s);
            setBackground(color_E3F);
            setEditable(false);
            setBackground(color_E3F);
            setHorizontalAlignment(CENTER);
            setForeground(color_263);
            setMargin(new Insets(14, 0, 14, 0));
            setBorder(new EmptyBorder(new Insets(14, 0, 14, 0)));
        }
    }
}
