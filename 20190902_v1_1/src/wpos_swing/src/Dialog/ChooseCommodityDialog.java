package Dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChooseCommodityDialog extends JDialog {

    Font font = new Font("微软雅黑", Font.BOLD, 20);

    //商品库存
    JTextField stock;
    //返回
    JButton cancel_tv;
    //加入
    JButton add_tv;

    public ChooseCommodityDialog(JFrame parent, boolean modal, int windowWidth, int windowHeight) {
        super(parent, modal);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setUndecorated(true);
        setResizable(false);
        setSize(windowWidth, windowHeight);
        setLocation((screenWidth - windowWidth) / 2,(screenHeight - windowHeight) / 2);
        setLayout(new BorderLayout());

        JTextField textField = new JTextField("选择商品");
        textField.setBorder(new EmptyBorder(10, 10, 10, 10));
        textField.setEditable(false);
        textField.setFont(font);

        add(textField, BorderLayout.NORTH);
        add(Center(), BorderLayout.CENTER);
        add(Bottom(), BorderLayout.SOUTH);

//        add(panel);
    }

    private JPanel Center() {
        JPanel center = new JPanel();
        center.setBorder(new EmptyBorder(10,10,10,10));
        center.setLayout(new BorderLayout());

        JPanel jPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        jPanel.setLayout(flowLayout);

        //条形码输入框
        JTextField field = new JTextField("输入条形码");
        field.setPreferredSize(new Dimension(400, 50));
        field.setBackground(Color.white);
        field.setMargin(new Insets(10, 10, 10, 10));
        field.setFont(font);
        jPanel.add(field);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        //顶部
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 10));
        //
        JTextField textField = new categoryTextFiled("序号");
        top.add(textField);
        JTextField textField2 = new categoryTextFiled("条形码");
        top.add(textField2);
        JTextField textField3 = new categoryTextFiled("名称");
        top.add(textField3);
        JTextField textField4 = new categoryTextFiled("单位");
        top.add(textField4);
        JTextField textField9 = new categoryTextFiled("零售价");
        top.add(textField9);
        JTextField textField10 = new categoryTextFiled("库存");
        top.add(textField10);
        //
        panel.add(top, BorderLayout.NORTH);

        // TODO: 2020/5/28 列表传值
        //展示列表的容器
        JList<String> jList = new JList<>(new MyListModel());
        JScrollPane jScrollPane = new JScrollPane(jList);
        jScrollPane.setBackground(Color.white);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //位于父布局中央
        panel.add(jScrollPane, BorderLayout.CENTER);

        center.add(jPanel, BorderLayout.NORTH);
        center.add(panel, BorderLayout.CENTER);

        return center;
    }

    private JPanel Bottom() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(20, 10, 20, 10));
        panel.setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel1.setLayout(flowLayout);
        JTextField textField = new JTextField("当前商品库存：");
        textField.setBorder(new EmptyBorder(0,0,0,0));
        textField.setEditable(false);
        textField.setFont(font);
        panel1.add(textField);
        stock = new JTextField("0");
        stock.setBorder(new EmptyBorder(0,0,0,0));
        stock.setEditable(false);
        stock.setFont(font);
        panel1.add(stock);

        panel.add(panel1, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1, 2, 10, 20));
        //
        cancel_tv = new JButton("返 回");
        cancel_tv.setBackground(Color.BLACK);
        cancel_tv.setMargin(new Insets(20,50,20,50));
        cancel_tv.setFont(font);
        cancel_tv.setForeground(Color.white);
        cancel_tv.setFocusPainted(false);
        cancel_tv.addActionListener(e -> dispose());
        panel2.add(cancel_tv);
        //
        add_tv = new JButton("加 入");
        add_tv.setBackground(new Color(Integer.decode("#2196F3")));
        add_tv.setMargin(new Insets(20,50,20,50));
        add_tv.setFont(font);
        add_tv.setForeground(Color.white);
        add_tv.setFocusPainted(false);
        panel2.add(add_tv, BorderLayout.EAST);

        panel.add(panel2,BorderLayout.EAST);

        return panel;
    }

    //生成列表解释文本框
    protected class categoryTextFiled extends JTextField {
        public categoryTextFiled(String s) {
            setFont(font);
            setText(s);
            setBackground(Color.decode("#E3F2FD"));
            setEditable(false);
            setHorizontalAlignment(CENTER);
            setForeground(Color.BLACK);
            setMargin(new Insets(14, 0, 14, 0));
            setBorder(new EmptyBorder(new Insets(14, 0, 14, 0)));
        }
    }

    //列表适配器
    class MyListModel extends AbstractListModel<String> {// 继承抽象类 AbstractListModel
        private static final long serialVersionUID = 1L;
        // 设置列表框内容
        private String[] contents = {"项目1", "项目2", "项目3", "项目4", "项目5", "项目6", "项目1", "项目2", "项目3", "项目4", "项目5", "项目6", "项目1", "项目2", "项目3", "项目4", "项目5", "项目6"};

        @Override
        public String getElementAt(int index) {// 重写 getElementAt() 方法
            if (index < contents.length) {
                return contents[index++];
            } else {
                return null;
            }
        }

        @Override
        public int getSize() {// 重写 getSize() 方法
            return contents.length;
        }
    }
}
