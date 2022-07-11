package Gui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import Interface.IFloginButton;
import Utils.MyToast;
import Utils.RoundBorder;
import Layout.CustomLayout;
import Layout.VFlowLayout;
import JPanel.BanerJpanel;

public class LoginFrame extends BaseFrame {
    int x, y;

    //承接用户名图标
    JLabel usersImage = new JLabel();
    //承接密码图标
    JLabel pswImage = new JLabel();
    //承接背景图片
    JLabel bg = new JLabel();
    //用户名输入框
    JTextField phonefield = new Create_textfield();
    //密码输入框
    JPasswordField pswfield = new Create_codefield();
    //登录按钮
    JButton login = new Create_Login_Button("登录");
    //单选框
    JCheckBox remenberpsw = new JCheckBox();
    //忘记密码
    JTextField forgetpsw = new Create_textfield();
    //资源文件路径
    URL url0 = getClass().getResource("/image/user.png");
    URL url1 = getClass().getResource("/image/lock.png");
    URL url4 = getClass().getResource("/image/checkbox_normal.png");
    URL url5 = getClass().getResource("/image/checkbox_press.png");

    int left, top;

     IFloginButton loginButtonListener;

    //构造方法
    public LoginFrame(IFloginButton loginButtonListener) {
        //设置窗体大小
        setSize(1080, 1920);
        //去除标题栏
        setUndecorated(true);
        //设置可见性
        setVisible(true);
        // 设置窗体透明度
        com.sun.awt.AWTUtilities.setWindowOpacity(this, 1.0f);

        this.loginButtonListener = loginButtonListener;

        //由于没有标题栏所以界面不能拖动改变位置
        //采取以下方法可以解决
        //静态鼠标触发器
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //当鼠标点击时获取距离屏幕的坐标
                x = e.getX();
                y = e.getY();
            }
        });
        //动态鼠标触发器
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                //获取当前位置的横坐标和纵坐标 left and top

                //横向移动距离=横向动态坐标-鼠标点击时的横向静态坐标
                //纵向移动距离=纵向动态坐标-鼠标点击时的纵向静态坐标
                //设置可变化的位置 加上原来的位置即可
                left = getLocation().x;
                top = getLocation().y;
                setLocation(left + e.getX() - x, top + e.getY() - y);
            }
        });

        //将窗口分隔成俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, leftJpanel(), rightJpanel());
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(800);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(0);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);
        //加入到面板中就好了
        this.add(jSplitPane);
    }

    //右侧登录界面
    protected JPanel rightJpanel() {
        //创建容器JPanel
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.white);

        //设置背景
        jPanel.setBackground(Color.white);
        jPanel.setOpaque(true);
//		//设置布局
        jPanel.setLayout(new VFlowLayout());
//		//添加文本框
        MatteBorder matteBorder = new MatteBorder(0, 0, 1, 0, Color.gray);

        //添加关闭窗口按钮
        JPanel closeJpanel= closeJpanel();
        //设置关闭窗口与手机号控件的距离
        closeJpanel.setBorder(new MatteBorder(0,0,30,0,Color.white));
        jPanel.add(closeJpanel);
        //添加手机号控件
        jPanel.add(phoneJpanel());
        //添加密码控件
        jPanel.add(pswJpanel());
        //添加记住密码控件
        jPanel.add(remenberJpanel());

        //添加登录按钮
        jPanel.add(login);

        login.addActionListener((e) -> {
            String phone = phonefield.getText();
            @SuppressWarnings("deprecation")
            String psw = pswfield.getText();

            loginButtonListener.loginClick(phone,psw);

        });
        return jPanel;
    }

    //左侧轮播图
    protected JPanel leftJpanel() {
         LinkedList<ImageIcon> list = new LinkedList();
         list.add(getImageIcon(getClass().getResource("/image/viewpager1.jpg"), 800, 800));
         list.add(getImageIcon(getClass().getResource("/image/viewpager2.jpg"), 800, 800));
         list.add(getImageIcon(getClass().getResource("/image/viewpager3.jpg"), 800, 800));
         list.add(getImageIcon(getClass().getResource("/image/viewpager4.jpg"), 800, 800));
        JPanel jPanel = new BanerJpanel(list);

        return jPanel;
    }

    //输入手机号的JPanel
    protected JPanel phoneJpanel() {
        JPanel jPanel = new JPanel();
        usersImage.setIcon(getImageIcon(url0, 26, 26));
        //设置背景
        jPanel.setBackground(Color.white);
        jPanel.setOpaque(true);
        //使jPanel带边框
        jPanel.setBorder(new RoundBorder(color_219));


        //使文本框没有边框
        MatteBorder matteBorder = new MatteBorder(0, 0, 0, 0, Color.gray);
        phonefield.setBorder(matteBorder);
        //设置hint
        phonefield.addFocusListener(new JTextFieldHintListener(phonefield, "手机号"));
        //设置只能输入数字
        // TODO: 2020/5/19 设置了之后，hint无法显示
//        phonefield.setDocument(new NumberDocument());

        //添加用户图标
        jPanel.add(usersImage);
        //添加输入框
        jPanel.add(phonefield);

        return jPanel;
    }

    //输入密码的JPanel
    protected JPanel pswJpanel() {
        JPanel jPanel = new JPanel();
        pswImage.setIcon(getImageIcon(url1, 26, 26));
        //设置背景
        jPanel.setBackground(Color.white);
        jPanel.setOpaque(true);
        //使jPanel带边框
        jPanel.setBorder(new RoundBorder(color_219));

        //使文本框没有边框
        MatteBorder matteBorder = new MatteBorder(0, 0, 0, 0, Color.gray);
        pswfield.setBorder(matteBorder);

        /**
         * 设置了也看不见，密码框是加密的
         */
        //设置hint
//        pswfield.addFocusListener(new JTextFieldHintListener(pswfield, "手机号"));
        //添加用户图标
        jPanel.add(pswImage);
        //添加输入框
        jPanel.add(pswfield);

        return jPanel;
    }

    //记住密码和忘记密码
    protected JPanel remenberJpanel() {
        JPanel jPanel = new JPanel();
        //设置背景
        jPanel.setBackground(Color.white);
        jPanel.setOpaque(true);
        jPanel.setLayout(new BorderLayout());

        //记住密码
        remenberpsw.setText("记住密码");
        remenberpsw.setFont(normalFont_15);
        //默认选中
        remenberpsw.setSelected(true);
        //设置距离上下10，,边框为白色
        MatteBorder matteBorder = new MatteBorder(10, 0, 20, 0, Color.white);
        remenberpsw.setBorder(matteBorder);
        //设置背景色
        remenberpsw.setBackground(Color.white);
        //设置文本居中对齐
        remenberpsw.setHorizontalAlignment(SwingConstants.CENTER);
        //设置选中图标
        Icon checked = new ImageIcon(url5);
        remenberpsw.setSelectedIcon(checked);
        //设置未选中图标
        Icon unchecked = new ImageIcon(url4);
        remenberpsw.setIcon(unchecked);


        //忘记密码控件
        forgetpsw.setText("忘记密码");
        //不可编辑
        forgetpsw.setEditable(false);
        //大小
        forgetpsw.setPreferredSize(new Dimension(80, 20));
        //文本颜色
        forgetpsw.setForeground(color_219);
        //背景颜色白色
        forgetpsw.setBackground(Color.white);
        //文本框为白色
        forgetpsw.setBorder(matteBorder);
        //文本居右
        forgetpsw.setHorizontalAlignment(SwingConstants.RIGHT);

        forgetpsw.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MyToast("请联系管理员");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //添加记住密码
        jPanel.add(remenberpsw, BorderLayout.WEST);
        //添加忘记密码
        jPanel.add(forgetpsw, BorderLayout.EAST);

        return jPanel;
    }

    //自定义文本框
    private class Create_textfield extends JTextField {
        public Create_textfield() {
            super();
            setFont(normalFont_15);
            setPreferredSize(new Dimension(400, 50));
            setMargin(new Insets(5, 0, 5, 0));
        }
    }

    //自定义密码框
    private class Create_codefield extends JPasswordField {
        public Create_codefield() {
            super();
            setFont(normalFont_15);
            setPreferredSize(new Dimension(400, 50));
            setMargin(new Insets(5, 0, 5, 0));
        }
    }

    //
    private class Create_label extends JLabel {
        public Create_label(String name, Color color) {
            super(name);
            setFont(new Font("微软雅黑", 1, 40));
            setForeground(color);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    //自定义按钮
    private class Create_Login_Button extends JButton {
        public Create_Login_Button(String text) {
            super(text);
            setBackground(color_1A4);
            setPreferredSize(new Dimension(215, 50));
            setForeground(Color.white);
            setFocusPainted(false);
            setFont(blackFont_15);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    //见包中CustomLayout  抽象类
    private class SimpleLayout extends CustomLayout {

        @Override
        public void layoutContainer(Container parent) {
            // TODO 自动生成的方法存根
            Rectangle rect = parent.getBounds();

            if (phonefield.isVisible()) {
                Dimension size = phonefield.getPreferredSize();
                int x = rect.width * 1 / 5 + 30;
                int y = rect.height * 1 / 2 + 1;
                phonefield.setBounds(x, y, 687, 88);
            }
            if (pswImage.isVisible()) {
                Dimension size = pswImage.getPreferredSize();
                int x = rect.width * 1 / 5 + 15;
                int y = rect.height * 1 / 2 + 42;
                pswImage.setBounds(x, y, size.width, size.height);
            }
            if (pswfield.isVisible()) {
                Dimension size = pswfield.getPreferredSize();
                int x = rect.width * 1 / 5 + 30;
                int y = rect.height * 1 / 2 + 39;
                pswfield.setBounds(x, y, 687, 88);
            }
            if (login.isVisible()) {
                Dimension size = login.getPreferredSize();
                int x = (rect.width - size.width) / 2 + 12;
                int y = rect.height * 1 / 2 + 89;
                login.setBounds(x, y, size.width, size.height);
            }
            if (closeButton.isVisible()) {
                Dimension size = closeButton.getPreferredSize();
                int x = rect.width - size.width;
                int y = 0;
                closeButton.setBounds(x, y, size.width, size.height);
            }
        }
    }

    class JTextFieldHintListener implements FocusListener {
        private String hintText;
        private JTextField textField;

        public JTextFieldHintListener(JTextField jTextField, String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if (temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if (temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }

        }

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

    //限制只能输入数字
    public class NumberDocument extends PlainDocument {
        public NumberDocument() {
        }

        public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
            if (this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }

        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }

}
