package Dialog;

import Layout.VFlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ResetPasswordDialog extends JDialog {
    /**
     * 使用方法：
     * ResetPasswordDialog dialog = new ResetPasswordDialog(frame,true,500,300);
     * dialog.setVisible(true);
     */


    Font font = new Font("微软雅黑", 1, 20);
    //提交密码按钮
    JButton reset_password_submit;
    //新密码
    JTextField newpassword;
    //确认密码
    JTextField confirmpassword;

    public ResetPasswordDialog(JFrame parent, boolean modal, int windowWidth, int windowHeight) {
        super(parent, modal);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setUndecorated(true);
        setResizable(false);
        setSize(windowWidth, windowHeight);
        setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
        setLayout(new BorderLayout());

        JTextField textField = new JTextField("修改密码");
        textField.setBorder(new EmptyBorder(10, 10, 10, 10));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setEditable(false);
        textField.setFont(font);

        JPanel panel = new JPanel();
        panel.setLayout(new VFlowLayout());

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        //
        JTextField textField1 = new JTextField("请输入密码");
        textField1.setBorder(new EmptyBorder(10, 10, 10, 10));
        textField1.setEditable(false);
        textField1.setFont(font);
        //
        newpassword = new JPasswordField();
        newpassword.setBorder(new MatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        newpassword.setFont(font);
        newpassword.setBackground(new Color(Integer.decode("#EEEEEE")));
        panel1.add(textField1, BorderLayout.WEST);
        panel1.add(newpassword, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        //
        JTextField textField2 = new JTextField("确 认 密 码");
        textField2.setBorder(new EmptyBorder(10, 10, 10, 10));
        textField2.setEditable(false);
        textField2.setFont(font);
        //
        confirmpassword = new JPasswordField();
        confirmpassword.setBorder(new MatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        confirmpassword.setBackground(new Color(Integer.decode("#EEEEEE")));
        confirmpassword.setFont(font);
        panel2.add(textField2, BorderLayout.WEST);
        panel2.add(confirmpassword, BorderLayout.CENTER);

        panel.add(panel1);
        panel.add(panel2);

        reset_password_submit = new JButton("提交");
        reset_password_submit.setFocusPainted(false);
        reset_password_submit.setBackground(new Color(Integer.decode("#2196F3")));
        reset_password_submit.setForeground(Color.white);
        reset_password_submit.setMargin(new Insets(10, 20, 10, 20));
        reset_password_submit.setFont(font);

        add(reset_password_submit, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        add(textField, BorderLayout.NORTH);

        setListener();
    }

    private void setListener() {
        reset_password_submit.addActionListener(e -> {
            dispose();
        });
    }

}
