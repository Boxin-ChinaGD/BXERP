package Dialog;

import Layout.VFlowLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;


public abstract class LogoutDialog extends JDialog {

    JButton sure;
    JButton cacel;

    /***
     * 自定义 Dialog
     * @param parent
     *             父Frame
     * @param modal
     *             是否模式窗体
     * @param windowWidth
     *             宽度 需根据数据计算高度
     * @param windowHeight
     *             高度  默认320即可
     */
    public LogoutDialog(JFrame parent, boolean modal, int windowWidth, int windowHeight) {
        super(parent, modal);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setUndecorated(true);
        setResizable(false);
        setSize(screenWidth, screenHeight);
        setOpacity(0.9f);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap((screenHeight - windowHeight) / 2);
        setLayout(flowLayout);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(true);
        panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
        panel.setBackground(Color.white);
        panel.add(Center(), BorderLayout.CENTER);
        panel.add(Bottom(), BorderLayout.SOUTH);
        add(panel);

        setListener();

    }

    private void setListener() {
        sure.addActionListener(e -> {
            sureclick();
        });
        cacel.addActionListener(e -> {
            setVisible(false);
        });
    }

    protected abstract void sureclick();

    private JPanel Center() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setLayout(new VFlowLayout());

        JTextField textField = new JTextField("提示");
        textField.setEditable(false);
        textField.setForeground(Color.red);
        textField.setBackground(Color.white);
        textField.setFont(new Font("微软雅黑", 1, 30));
        textField.setBorder(new EmptyBorder(20, 10, 0, 0));
        panel.add(textField);

        JTextField textField1 = new JTextField("确认交班？");
        textField1.setEditable(false);
        textField1.setForeground(Color.black);
        textField1.setBackground(Color.white);
        textField1.setFont(new Font("微软雅黑", 1, 20));
        textField1.setBorder(new EmptyBorder(20, 10, 0, 0));
        panel.add(textField1);

        return panel;
    }

    private JPanel Bottom() {
        JPanel panel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(0, 20, 0);
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.setLayout(flowLayout);
        panel.setBackground(Color.white);

        sure = new JButton("确定");
        sure.setFocusPainted(false);
        sure.setBackground(new Color(Integer.decode("#2196F3")));
        sure.setForeground(Color.white);
        sure.setMargin(new Insets(10, 20, 10, 20));
        sure.setFont(new Font("微软雅黑", 1, 20));
        panel.add(sure);

        cacel = new JButton("取消");
        cacel.setFocusPainted(false);
        cacel.setBackground(Color.black);
        cacel.setForeground(Color.white);
        cacel.setMargin(new Insets(10, 20, 10, 20));
        cacel.setFont(new Font("微软雅黑", 1, 20));
        panel.add(cacel);

        return panel;
    }

}
