package Dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class InputNumberDialog extends JDialog implements MouseListener {

    JButton sure,cancel;
    JTextField reserve_et;
    Font font = new Font("微软雅黑", 1, 20);
    JTextField jt7;

    public InputNumberDialog(JFrame parent, boolean modal, int windowWidth, int windowHeight) {
        super(parent, modal);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setUndecorated(true);
        setResizable(false);
        setSize(windowWidth, windowHeight);
        setLocation((screenWidth - windowWidth) / 2,(screenHeight - windowHeight) / 2);
        setLayout(new BorderLayout());


        JTextField textField = new JTextField("输入数量");
        textField.setBorder(new EmptyBorder(10, 10, 10, 10));
        textField.setEditable(false);
        textField.setFont(font);

        add(textField, BorderLayout.NORTH);
        add(Center(), BorderLayout.CENTER);
        add(Bottom(), BorderLayout.SOUTH);

        setListener();
    }

    private void setListener() {
        sure.addActionListener(e -> {
            dispose();
        });
        cancel.addActionListener(e -> dispose());
    }


    private JPanel Center() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout());

        reserve_et = new JTextField();
        reserve_et.setBackground(Color.white);
        reserve_et.setFont(font);
        reserve_et.setMargin(new Insets(10, 10, 10, 10));
        panel.add(reserve_et, BorderLayout.NORTH);

        JPanel keyboard = new JPanel();
        keyboard.setBorder(new EmptyBorder(20, 10, 10, 10));
        keyboard.setLayout(new GridLayout(4, 3, 8, 8));
        jt7 = new KeyBoard("7");
        jt7.addMouseListener(this);
        keyboard.add(jt7);
        JTextField jt8 = new KeyBoard("8");
        keyboard.add(jt8);
        JTextField jt9 = new KeyBoard("9");
        keyboard.add(jt9);
        JTextField jt4 = new KeyBoard("4");
        keyboard.add(jt4);
        JTextField jt5 = new KeyBoard("5");
        keyboard.add(jt5);
        JTextField jt6 = new KeyBoard("6");
        keyboard.add(jt6);
        JTextField jt1 = new KeyBoard("1");
        keyboard.add(jt1);
        JTextField jt2 = new KeyBoard("2");
        keyboard.add(jt2);
        JTextField jt3 = new KeyBoard("3");
        keyboard.add(jt3);
        JTextField jt0 = new KeyBoard("0");
        keyboard.add(jt0);
        JTextField jtpoint = new KeyBoard(".");
        keyboard.add(jtpoint);
        JPanel delte = new JPanel();
        delte.setLayout(new BorderLayout());
        delte.setBackground(Color.white);
        JLabel jLabel = new JLabel(getImageIcon(getClass().getResource("/image/delete.png"), 40, 40));
        jLabel.setBackground(Color.WHITE);
        delte.add(jLabel, BorderLayout.CENTER);
        keyboard.add(delte);
        panel.add(keyboard, BorderLayout.CENTER);

        return panel;
    }

    private JPanel Bottom() {
        JPanel panel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(0, 20, 0);
        flowLayout.setAlignment(FlowLayout.RIGHT);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.setLayout(flowLayout);

        cancel = new JButton("取消");
        cancel.setFocusPainted(false);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.white);
        cancel.setMargin(new Insets(10, 20, 10, 20));
        cancel.setFont(font);
        panel.add(cancel);

        sure = new JButton("确定");
        sure.setFocusPainted(false);
        sure.setBackground(new Color(Integer.decode("#2196F3")));
        sure.setForeground(Color.white);
        sure.setMargin(new Insets(10, 20, 10, 20));
        sure.setFont(font);
        panel.add(sure);


        return panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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


    class KeyBoard extends JTextField {
        public KeyBoard(String s) {
            setText(s);
            setFont(font);
            Insets insets = new Insets(0, 10, 0, 10);
            setMargin(insets);
            setBorder(new EmptyBorder(insets));
            setEditable(false);
            setHorizontalAlignment(CENTER);
            setBackground(Color.white);
            setForeground(Color.BLACK);
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

}
