package JPanel;

import Dialog.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainJPanel extends BaseJPanel{
    Box box = Box.createHorizontalBox();
    //会员搜索按钮
    JButton search_vip;
    //会员手机号
    JTextField show_client_phone;
    //上一单支付方式
    JTextField last_paymenttype;
    //上一单应收金额
    JTextField last_amount;
    //上一单找零
    JTextField last_changemoney;
    //结账按钮
    JButton balance_tv;
    //结账时总金额
    JTextField totalpay_money;
    //优惠券列表
    JComboBox spinner;
    //微信支付金额
    JTextField wechat_paying_amount;
    //现金支付金额
    JTextField cash_paying_amount;
    //未付余额
    JTextField unpaid_balance;
    //找零
    JTextField change_money;
    //取消支付
    JButton payment_close;
    //扫描条形码
    JTextField scan_barcode_text;

    JLayeredPane jLayeredPane = new JLayeredPane();
    //右侧支付面板
    JPanel rightPay;
    //main的页面的列表
    JScrollPane jScrollPane;
    //列表数据
    JList<String> jList;
    //父窗口
    JFrame frame;

    public MainJPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        //顶部
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 10));
        //
        JTextField textField = new categoryTextFiled("序号");
        top.add(textField);
        JTextField textField2 = new categoryTextFiled("条形码");
        top.add(textField2);
        JTextField textField3 = new categoryTextFiled("商品名称/规格/属性");
        top.add(textField3);
        JTextField textField4 = new categoryTextFiled("单位");
        top.add(textField4);
        JTextField textField5 = new categoryTextFiled("数量");
        top.add(textField5);
        JTextField textField6 = new categoryTextFiled("单价(元)");
        top.add(textField6);
        JTextField textField7 = new categoryTextFiled("折扣(金额)");
        top.add(textField7);
        JTextField textField8 = new categoryTextFiled("折后单价(元)");
        top.add(textField8);
        JTextField textField9 = new categoryTextFiled("金额");
        top.add(textField9);
        JTextField textField10 = new categoryTextFiled("备注");
        top.add(textField10);
        //
        jPanel.add(top, BorderLayout.NORTH);

        // TODO: 2020/5/21 列表传值
        //展示列表的容器
        jList = new JList<>(new MyListModel());
        jScrollPane = new JScrollPane(jList);
        jScrollPane.setBackground(Color.white);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        jList.addMouseListener(new MouseAdapter());

        //位于父布局中央
        jPanel.add(jScrollPane, BorderLayout.CENTER);

        //底部左侧
        JPanel bottomLeft = new JPanel();
        bottomLeft.setLayout(new GridLayout(2, 1, 0, 10));
        bottomLeft.setBorder(new EmptyBorder(10, 10, 10, 20));

        //底部左侧第一行
        JPanel firstline = new JPanel();
        firstline.setLayout(new BorderLayout());
        //输入框
        scan_barcode_text = new InputJTextFiled("扫描条形码");
        firstline.add(scan_barcode_text, BorderLayout.CENTER);
        //搜索图标
        JPanel searchpanel = new JPanel();
        searchpanel.setBackground(color_9E9);
        JLabel search = new JLabel(getImageIcon(getClass().getResource("/image/search.png"), 40, 40));
        search.setOpaque(false);
        search.setBorder(new MatteBorder(10, 10, 10, 10, color_9E9));
        searchpanel.add(search);
        firstline.add(searchpanel, BorderLayout.EAST);

        //底部左侧第二行
        JPanel secondline = new JPanel();
        secondline.setLayout(new GridLayout(1, 3, 10, 0));
        //
        JTextField show_client_name = new InputJTextFiled("会员名称");
        secondline.add(show_client_name);
        show_client_phone = new InputJTextFiled("会员号(手机号)");
        secondline.add(show_client_phone);
        search_vip = new JButton("搜 索");
        search_vip.setBackground(color_219);
        search_vip.setBorder(new MatteBorder(1, 1, 1, 1, color_E3F));
        search_vip.setForeground(Color.white);
        search_vip.setFont(blackFont_20);
        search_vip.setFocusPainted(false);
        secondline.add(search_vip);

        bottomLeft.add(firstline);
        bottomLeft.add(secondline);

        //底部右侧
        JPanel bottomRight = new JPanel();
        bottomRight.setLayout(new BorderLayout());
        //上一单信息
        JPanel lastRetailTrade = new JPanel();
        lastRetailTrade.setLayout(new GridLayout(1, 9));
        lastRetailTrade.setBorder(new EmptyBorder(10, 10, 0, 10));
        //
        JTextField jTextField = new LastRetailTradeJTextFiled("上一单信息");
        lastRetailTrade.add(jTextField);
        //
        JTextField jTextField1 = new LastRetailTradeJTextFiled("支付方式:");
        jTextField1.setHorizontalAlignment(SwingConstants.RIGHT);
        lastRetailTrade.add(jTextField1);
        last_paymenttype = new LastRetailTradeJTextFiled("现金支付");
        lastRetailTrade.add(last_paymenttype);
        //
        JTextField jTextField3 = new LastRetailTradeJTextFiled("应收金额:");
        jTextField3.setHorizontalAlignment(SwingConstants.RIGHT);
        lastRetailTrade.add(jTextField3);
        last_amount = new LastRetailTradeJTextFiled("0.00");
        lastRetailTrade.add(last_amount);
        //
        JTextField jTextField5 = new LastRetailTradeJTextFiled("实收金额:");
        jTextField5.setHorizontalAlignment(SwingConstants.RIGHT);
        lastRetailTrade.add(jTextField5);
        JTextField jTextField6 = new LastRetailTradeJTextFiled("0.00");
        lastRetailTrade.add(jTextField6);
        //
        JTextField jTextField7 = new LastRetailTradeJTextFiled("找零:");
        jTextField7.setHorizontalAlignment(SwingConstants.RIGHT);
        lastRetailTrade.add(jTextField7);
        last_changemoney = new LastRetailTradeJTextFiled("0.00");
        lastRetailTrade.add(last_changemoney);
        //
        bottomRight.add(lastRetailTrade, BorderLayout.NORTH);

        //本次结账的信息
        JPanel currenRetailTrade = new JPanel();
        currenRetailTrade.setBorder(new EmptyBorder(10, 10, 10, 10));
        currenRetailTrade.setLayout(new GridLayout(2, 2, 10, 4));
        //商品数量
        JTextField commodity_quantity = new InputJTextFiled("商品数量：0");
        commodity_quantity.setBackground(color_ECE);
        commodity_quantity.setForeground(Color.BLACK);
        currenRetailTrade.add(commodity_quantity);
        //总计金额
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        //
        JTextField textField1 = new InputJTextFiled("总计金额");
        textField1.setBackground(color_ECE);
        textField1.setForeground(color_F44);
        panel.add(textField1);
        //
        JTextField jTextField2 = new InputJTextFiled("￥");
        jTextField2.setBackground(color_ECE);
        jTextField2.setForeground(color_F44);
        jTextField2.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(jTextField2);
        //
        JTextField total_money = new InputJTextFiled("00.00");
        total_money.setBackground(color_ECE);
        total_money.setForeground(color_F44);
        total_money.setFont(blackFont_30);
        panel.add(total_money);
        currenRetailTrade.add(panel);

        JButton guadan = new JButton("挂 单");
        guadan.setFont(blackFont_20);
        guadan.setForeground(Color.white);
        guadan.setBackground(color_219);
        guadan.setFocusPainted(false);
        currenRetailTrade.add(guadan);

        balance_tv = new JButton("结 账");
        balance_tv.setFont(blackFont_20);
        balance_tv.setForeground(Color.white);
        balance_tv.setBackground(color_F44);
        balance_tv.setFocusPainted(false);
        currenRetailTrade.add(balance_tv);

        bottomRight.add(currenRetailTrade, BorderLayout.CENTER);

        //将窗口分隔成俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, bottomLeft, bottomRight);
        jSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(800);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(1);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);
        jSplitPane.setBorder(new EmptyBorder(20, 0, 20, 0));
        jPanel.add(jSplitPane, BorderLayout.SOUTH);

        jLayeredPane.setLayout(new BorderLayout());
        jLayeredPane.setBackground(Color.red);
        jLayeredPane.setPreferredSize(new Dimension(1080, 784));
        jLayeredPane.add(jPanel, BorderLayout.CENTER, 2);

        box.add(jPanel);
        add(box, BorderLayout.CENTER);

        rightPay = rightPay();

        setOnclickLinstener();
    }

    public JPanel rightPay() {
        //顶部被隐藏，是否能有cardlayout来完成？
        JPanel rightpay = new JPanel();
        rightpay.setBackground(Color.white);

        rightpay.setLayout(new GridLayout(2, 1, 0, 100));

        JPanel left = new JPanel();
        left.setBorder(new EmptyBorder(10, 10, 10, 10));
        left.setBackground(Color.white);
        left.setLayout(new BorderLayout());

        JPanel leftTop = new JPanel();
        leftTop.setBackground(Color.white);
        leftTop.setLayout(new GridLayout(10, 1, 0, 10));
        //第一行
        Box firstline = Box.createHorizontalBox();
        JTextField textField = new InputJTextFiled("总计金额");
        textField.setForeground(Color.BLACK);
        firstline.add(textField);
        JTextField textField1 = new InputJTextFiled("￥");
        textField1.setForeground(color_F44);
        textField1.setHorizontalAlignment(SwingConstants.RIGHT);
        firstline.add(textField1);
        totalpay_money = new InputJTextFiled("000.00");
        totalpay_money.setForeground(color_F44);
        firstline.add(totalpay_money);
        //第二行
        spinner = new JComboBox();
        spinner.addItem("有可用优惠券");
        spinner.addItem("第一张优惠券");
        spinner.addItem("第二张优惠券");
        spinner.addItem("第三张优惠券");
        spinner.addItem("第四张优惠券");
        spinner.setVisible(true);
        //第三行
        Box thredline = Box.createHorizontalBox();
        JTextField wechat_paying = new InputJTextFiled("微信支付");
        wechat_paying.setForeground(Color.BLACK);
        thredline.add(wechat_paying);
        JTextField textField3 = new InputJTextFiled("￥");
        textField3.setForeground(color_F44);
        textField3.setHorizontalAlignment(SwingConstants.RIGHT);
        thredline.add(textField3);
        wechat_paying_amount = new InputJTextFiled("000.00");
        wechat_paying_amount.setForeground(color_F44);
        thredline.add(wechat_paying_amount);
        //第四行
        Box forthline = Box.createHorizontalBox();
        JTextField cash_paying = new InputJTextFiled("现金支付");
        cash_paying.setForeground(Color.BLACK);
        forthline.add(cash_paying);
        JTextField textField4 = new InputJTextFiled("￥");
        textField4.setForeground(color_F44);
        textField4.setHorizontalAlignment(SwingConstants.RIGHT);
        forthline.add(textField4);
        cash_paying_amount = new InputJTextFiled("000.00");
        cash_paying_amount.setForeground(color_F44);
        forthline.add(cash_paying_amount);

        leftTop.add(firstline);
        leftTop.add(spinner);
        leftTop.add(thredline);
        leftTop.add(forthline);
        left.add(leftTop, BorderLayout.CENTER);

        JPanel leftBottom = new JPanel();
        leftBottom.setBackground(Color.white);
        leftBottom.setLayout(new GridLayout(2, 1, 0, 10));
        //第五行
        Box fiveline = Box.createHorizontalBox();
        JTextField textField2 = new InputJTextFiled("未付余额");
        textField2.setForeground(Color.BLACK);
        fiveline.add(textField2);
        JTextField textField5 = new InputJTextFiled("￥");
        textField5.setForeground(color_F44);
        textField5.setHorizontalAlignment(SwingConstants.RIGHT);
        fiveline.add(textField5);
        unpaid_balance = new InputJTextFiled("000.00");
        unpaid_balance.setForeground(color_F44);
        fiveline.add(unpaid_balance);
        //第六行
        Box sixline = Box.createHorizontalBox();
        JTextField textField6 = new InputJTextFiled("找零       ");
        textField6.setForeground(Color.BLACK);
        sixline.add(textField6);
        JTextField textField7 = new InputJTextFiled("￥");
        textField7.setForeground(color_F44);
        textField7.setHorizontalAlignment(SwingConstants.RIGHT);
        sixline.add(textField7);
        change_money = new InputJTextFiled("000.00");
        change_money.setForeground(color_F44);
        sixline.add(change_money);

        leftBottom.add(fiveline);
        leftBottom.add(sixline);
        left.add(leftBottom, BorderLayout.SOUTH);

        JPanel right = new JPanel();
        right.setBackground(Color.white);
        right.setLayout(new GridLayout(4, 1, 10, 60));
        right.setBorder(new EmptyBorder(50, 50, 50, 50));
        //微信支付按钮
        JPanel wechat_pay = new JPanel();
        wechat_pay.setBackground(color_F0F);
        wechat_pay.setBorder(new MatteBorder(1, 1, 1, 1, color_9E9));
        wechat_pay.setLayout(new BorderLayout());
        JLabel label = new JLabel(getImageIcon(getClass().getResource("/image/wechat_logo.png"), 40, 40));
        label.setBorder(new EmptyBorder(0, 10, 0, 0));
        wechat_pay.add(label, BorderLayout.WEST);
        JTextField textField8 = new InputJTextFiled("微信支付");
        textField8.setForeground(Color.BLACK);
        textField8.setBackground(color_F0F);
        wechat_pay.add(textField8, BorderLayout.CENTER);
        right.add(wechat_pay);
        //现金支付按钮
        JPanel cash_pay = new JPanel();
        cash_pay.setLayout(new BorderLayout());
        cash_pay.setBackground(color_F0F);
        cash_pay.setBorder(new MatteBorder(1, 1, 1, 1, color_9E9));
        JLabel label1 = new JLabel(getImageIcon(getClass().getResource("/image/cash_logo.png"), 40, 40));
        label1.setBorder(new EmptyBorder(0, 10, 0, 0));
        cash_pay.add(label1, BorderLayout.WEST);
        JTextField textField9 = new InputJTextFiled("现金支付");
        textField9.setForeground(Color.BLACK);
        textField9.setBackground(color_F0F);
        cash_pay.add(textField9, BorderLayout.CENTER);
        right.add(cash_pay);

        //分隔成左右
        JSplitPane Hsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, left, right);
        Hsplit.setBorder(new EmptyBorder(0, 0, 0, 0));
        Hsplit.setDividerLocation(400);
        Hsplit.setOneTouchExpandable(false);
        Hsplit.setDividerSize(1);
        Hsplit.setEnabled(false);

        rightpay.add(Hsplit);

        JPanel keyboardAndButton = new JPanel();
        keyboardAndButton.setLayout(new BorderLayout());
        keyboardAndButton.setBackground(color_ECE);

        JPanel keyboard = new JPanel();
        keyboard.setBorder(new EmptyBorder(10, 10, 10, 10));
        keyboard.setLayout(new GridLayout(4, 4, 8, 8));
        JTextField jt7 = new KeyBoard("7");
        keyboard.add(jt7);
        JTextField jt8 = new KeyBoard("8");
        keyboard.add(jt8);
        JTextField jt9 = new KeyBoard("9");
        keyboard.add(jt9);
        JTextField jt100 = new KeyBoard("￥100");
        keyboard.add(jt100);
        JTextField jt4 = new KeyBoard("4");
        keyboard.add(jt4);
        JTextField jt5 = new KeyBoard("5");
        keyboard.add(jt5);
        JTextField jt6 = new KeyBoard("6");
        keyboard.add(jt6);
        JTextField jt50 = new KeyBoard("￥50");
        keyboard.add(jt50);
        JTextField jt1 = new KeyBoard("1");
        keyboard.add(jt1);
        JTextField jt2 = new KeyBoard("2");
        keyboard.add(jt2);
        JTextField jt3 = new KeyBoard("3");
        keyboard.add(jt3);
        JTextField jt20 = new KeyBoard("￥20");
        keyboard.add(jt20);
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
        JTextField jt10 = new KeyBoard("￥10");
        keyboard.add(jt10);
        keyboardAndButton.add(keyboard, BorderLayout.CENTER);

        JPanel button = new JPanel();
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        button.setLayout(new GridLayout(1, 3, 10, 0));
        //挂单按钮
        JButton guadan = new JButton("挂 单");
        guadan.setMargin(new Insets(20, 100, 20, 100));
        guadan.setFont(blackFont_20);
        guadan.setForeground(Color.white);
        guadan.setFocusPainted(false);
        guadan.setBackground(color_219);
        button.add(guadan);
        //取消支付
        payment_close = new JButton("取 消 支 付");
        payment_close.setFont(blackFont_20);
        payment_close.setBackground(color_263);
        payment_close.setFocusPainted(false);
        payment_close.setForeground(Color.white);
        button.add(payment_close);
        //支付
        JButton pay = new JButton("支 付");
        pay.setFont(blackFont_20);
        pay.setBackground(color_F44);
        pay.setFocusPainted(false);
        pay.setForeground(Color.white);
        button.add(pay);

        keyboardAndButton.add(button, BorderLayout.SOUTH);
        rightpay.add(keyboardAndButton, BorderLayout.SOUTH);
        return rightpay;
    }

    public void setOnclickLinstener() {
        balance_tv.addActionListener(e -> {
//            jLayeredPane.add(rightPay(),1);
//            jLayeredPane.validate();
//            jLayeredPane.repaint();
            box.add(rightPay);
            box.validate();
            box.repaint();

        });

        payment_close.addActionListener(e -> {
            box.remove(rightPay);
            box.validate();
            box.repaint();
        });

        scan_barcode_text.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChooseCommodityDialog commodityDialog = new ChooseCommodityDialog(frame, true, 1000, 600);
                commodityDialog.setVisible(true);
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
    }

    class InputJTextFiled extends JTextField {
        public InputJTextFiled(String s) {
            setText(s);
            setFont(blackFont_20);
            Insets insets = new Insets(0, 10, 0, 10);
            setMargin(insets);
            setBorder(new EmptyBorder(insets));
            setEditable(false);
            setHorizontalAlignment(LEFT);
            setBackground(Color.white);
            setForeground(color_F0F);
        }
    }

    class KeyBoard extends JTextField {
        public KeyBoard(String s) {
            setText(s);
            setFont(blackFont_20);
            Insets insets = new Insets(0, 10, 0, 10);
            setMargin(insets);
            setBorder(new EmptyBorder(insets));
            setEditable(false);
            setHorizontalAlignment(CENTER);
            setBackground(Color.white);
            setForeground(Color.BLACK);
        }
    }

    class LastRetailTradeJTextFiled extends JTextField {
        public LastRetailTradeJTextFiled(String s) {
            setText(s);
            setFont(normalFont_15);
            setForeground(color_616);
            setEditable(false);
            setBorder(new EmptyBorder(0, 0, 0, 0));
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

    //列表item右键
    class MouseAdapter extends java.awt.event.MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            //鼠标右键响应
            if (e.getButton() == MouseEvent.BUTTON3) {
                RemoveCommodityDialog removeCommodityDialog = new RemoveCommodityDialog(frame, true, 600, 150);
                jList.setSelectedIndex(jList.locationToIndex(e.getPoint()));
                jScrollPane.validate();
                jScrollPane.repaint();
                removeCommodityDialog.setVisible(true);
            } else if (e.getButton() == MouseEvent.BUTTON1) {//鼠标左键响应
                //列表被点击
                InputNumberDialog inputNumberDialog = new InputNumberDialog(frame, true, 600, 500);
                //展示选择数量之前刷新一下列表，否则会有俩个item同时处于选中状态
                jScrollPane.validate();
                jScrollPane.repaint();
                inputNumberDialog.setVisible(true);
            }
        }
    }
}
