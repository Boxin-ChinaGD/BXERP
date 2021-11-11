package JPanel;

import Activity.FragmentActivity;
import Gui.FragmentFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RetailTradeAggregationJPanel extends BaseJPanel {
    //营业额
    JTextField showAmount;
    //交易单数
    JTextField showTradeNo;
    //准备金
    JTextField showReserveAmount;
    //现金收入
    JTextField showCashAmount;
    //微信收入
    JTextField showWechatAmount;
    //交接时间
    JTextField showWorkTimeEnd;
    //操作员
    JTextField operator;
    //交班按钮
    public JButton change_shifts;
    //返回按钮
    public JPanel back;

    public RetailTradeAggregationJPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        add(center(), BorderLayout.CENTER);
        add(Bottom(), BorderLayout.SOUTH);

        setListener();
    }

    private void setListener() {

    }

    public JPanel center() {
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(3, 3, 20, 20));
        center.setBorder(new EmptyBorder(20, 20, 20, 20));
        center.setBackground(Color.white);

        //营业额
        JPanel panel = new RetailTradeAggregationPanel(color_219);
        JTextField textField = new RetailTradeAggregationTextFiled("营业额", color_219, Color.WHITE);
        panel.add(textField);
        showAmount = new RetailTradeAggregationTextFiled("13967.42", color_219, Color.WHITE);
        panel.add(showAmount);

        //交易单数
        JPanel panel1 = new RetailTradeAggregationPanel(color_00B);
        JTextField textField1 = new RetailTradeAggregationTextFiled("交易单数", color_00B, Color.WHITE);
        panel1.add(textField1);
        showTradeNo = new RetailTradeAggregationTextFiled("857", color_00B, Color.WHITE);
        panel1.add(showTradeNo);

        //准备金
        JPanel panel2 = new RetailTradeAggregationPanel(new Color(Integer.decode("#009688")));
        JTextField textField2 = new RetailTradeAggregationTextFiled("准备金", new Color(Integer.decode("#009688")), Color.WHITE);
        panel2.add(textField2);
        showReserveAmount = new RetailTradeAggregationTextFiled("857.00", new Color(Integer.decode("#009688")), Color.WHITE);
        panel2.add(showReserveAmount);

        //现金收入
        JPanel panel3 = new RetailTradeAggregationPanel(color_219);
        JTextField textField3 = new RetailTradeAggregationTextFiled("现金收入", color_219, Color.WHITE);
        panel3.add(textField3);
        showCashAmount = new RetailTradeAggregationTextFiled("3717.00", color_219, Color.WHITE);
        panel3.add(showCashAmount);

        //微信收入
        JPanel panel4 = new RetailTradeAggregationPanel(color_219);
        JTextField textField4 = new RetailTradeAggregationTextFiled("微信收入", color_219, Color.WHITE);
        panel4.add(textField4);
        showWechatAmount = new RetailTradeAggregationTextFiled("9818.00", color_219, Color.WHITE);
        panel4.add(showWechatAmount);

        center.add(panel);
        center.add(panel1);
        center.add(panel2);
        center.add(panel3);
        JPanel panel5 = new JPanel();
        panel5.setBackground(Color.white);
        JPanel panel6 = new JPanel();
        panel6.setBackground(Color.white);
        center.add(panel5);
        center.add(panel6);
        center.add(panel4);

        return center;
    }

    private Box Bottom() {
        Box box = Box.createHorizontalBox();

        //退出按钮
        back = new JPanel();
        back.setLayout(new GridLayout());
        back.setBackground(color_ECE);
        JLabel label = new JLabel(getImageIcon(getClass().getResource("/image/out.png"), 60, 53));
        label.setOpaque(false);
        back.add(label);

        //交接时间，操作员
        JPanel panel1 = new JPanel();
        panel1.setBorder(new EmptyBorder(20,0,20,0));
        panel1.setLayout(new GridLayout(2, 2, 0, 10));
        //
        JTextField textField = new RetailTradeAggregationTextFiled("交接时间：", color_ECE, Color.black);
        panel1.add(textField);
        showWorkTimeEnd = new RetailTradeAggregationTextFiled("2020/05/28 11:30:25", color_ECE, Color.black);
        panel1.add(showWorkTimeEnd);
        //
        JTextField textField1 = new RetailTradeAggregationTextFiled("操作员：", color_ECE, Color.black);
        panel1.add(textField1);
        operator = new RetailTradeAggregationTextFiled("海龟先生", color_ECE, Color.black);
        panel1.add(operator);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2,1));
        panel2.add(new JPanel());
        //打印张数
        JPanel jPanel = new JPanel();
        jPanel.setBackground(color_ECE);
        jPanel.setBorder(new EmptyBorder(0,0,10,0));
        jPanel.setLayout(new FlowLayout());
        JTextField textField2 = new RetailTradeAggregationTextFiled("交班表打印数量", color_ECE, Color.black);
        jPanel.add(textField2);
        JComboBox<String> box1 = new JComboBox<>();
        box1.setFont(blackFont_30);
        box1.addItem("  1  ");
        box1.addItem("  2  ");
        box1.addItem("  3  ");
        box1.addItem("  4  ");
        box1.addItem("  5  ");
        jPanel.add(box1);
        JTextField textField3 = new RetailTradeAggregationTextFiled("张", color_ECE, Color.black);
        jPanel.add(textField3);
        panel2.add(jPanel);

        //按钮
        JPanel panel3 = new JPanel();
        panel3.setBorder(new EmptyBorder(20,0,20,20));
        panel3.setLayout(new GridLayout(1,2,20,0));
        //
        JButton change_shifts_record = new JButton("  交班记录  ");
        change_shifts_record.setFont(blackFont_30);
        change_shifts_record.setFocusPainted(false);
        change_shifts_record.setMargin(new Insets(10,20,10,20));
        change_shifts_record.setBackground(Color.black);
        change_shifts_record.setForeground(Color.white);
        //
        change_shifts = new JButton("  交    班  ");
        change_shifts.setFont(blackFont_30);
        change_shifts.setFocusPainted(false);
        change_shifts.setMargin(new Insets(10,20,10,20));
        change_shifts.setBackground(color_219);
        change_shifts.setForeground(Color.white);
        //
        panel3.add(change_shifts_record);
        panel3.add(change_shifts);

        box.add(back);
        box.add(panel1);
        box.add(panel2);
        box.add(panel3);

        return box;
    }

    class RetailTradeAggregationTextFiled extends JTextField {
        public RetailTradeAggregationTextFiled(String s, Color bgcolor, Color forgroundcolor) {
            setText(s);
            setFont(blackFont_30);
            setBackground(bgcolor);
            setForeground(forgroundcolor);
            setEditable(false);
            setBorder(new EmptyBorder(0, 10, 0, 0));
        }
    }

    class RetailTradeAggregationPanel extends JPanel {
        public RetailTradeAggregationPanel(Color color) {
            setLayout(new GridLayout(2, 1, 0, 10));
            setBackground(color);
            setForeground(Color.white);

        }
    }

}
