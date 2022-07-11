package JPanel;

import Layout.VFlowLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SmallSheetJPanel extends BaseJPanel {
    //分割线集合
    List<JTextField> delimiter = new ArrayList<>();
    //logo图
    JLabel template_logo;
    //页眉
    JTextField template_header;
    //单据号
    JTextField template_document_number;
    //日期
    JTextField template_date;
    //商品名称
    JTextField template_goods_name;
    //数量
    JTextField template_goods_number;
    //小计
    JTextField template_subtotal;

    String fengexian;
    //商品A
    private JTextField template_first_goods_name;
    //数量A
    private JTextField template_first_goods_number;
    //小计A
    private JTextField template_first_subtotal;
    //商品B
    private JTextField template_second_goods_name;
    //数量B
    private JTextField template_second_goods_number;
    //小计B
    private JTextField template_second_subtotal;
    //商品C
    private JTextField template_third_goods_name;
    //数量C
    private JTextField template_third_goods_number;
    //小计C
    private JTextField template_third_subtotal;
    //合计
    private JTextField total_money;
    //支付方式
    private JTextField payment_method;
    //优惠
    private JTextField template_discont;
    //应付
    private JTextField template_payable;
    //微信支付
    private JTextField template_payment_method1;
    //现金支付
    private JTextField template_payment_method3;
    //页脚1
    private JTextField template_footer1;
    //页脚2
    private JTextField template_footer2;
    //页脚3
    private JTextField template_footer3;
    //页脚4
    private JTextField template_footer4;
    //页脚5
    private JTextField template_footer5;
    //页脚6
    private JTextField template_footer6;
    //页脚7
    private JTextField template_footer7;
    //页脚8
    private JTextField template_footer8;
    //页脚9
    private JTextField template_footer9;
    //页脚10
    private JTextField template_footer10;
    //底部
    private JTextField template_ticket_bottom;
    //可编辑页眉
    private JTextField design_header;
    //可编辑分割线
    private JTextField design_delimiterToRepeat;
    //可编辑页脚1
    private JTextField design_footer1;
    //可编辑页脚2
    private JTextField design_footer2;
    //可编辑页脚3
    private JTextField design_footer3;
    //可编辑页脚4
    private JTextField design_footer4;
    //可编辑页脚5
    private JTextField design_footer5;
    //可编辑页脚6
    private JTextField design_footer6;
    //可编辑页脚7
    private JTextField design_footer7;
    //可编辑页脚8
    private JTextField design_footer8;
    //可编辑页脚9
    private JTextField design_footer9;
    //可编辑页脚10
    private JTextField design_footer10;
    //控制显示页脚2-10
    private Box design_footer2_layout;
    private Box design_footer3_layout;
    private Box design_footer4_layout;
    private Box design_footer5_layout;
    private Box design_footer6_layout;
    private Box design_footer7_layout;
    private Box design_footer8_layout;
    private Box design_footer9_layout;
    private Box design_footer10_layout;
    //添加页脚按钮
    private JPanel add_footer;
    //底部编辑框
    private JTextField design_ticket_bottom;
    //测试打印
    private JButton print_test;
    //同步按钮
    private JButton loading_config_data;
    //新建格式按钮
    private JButton smallsheet_create;
    //修改格式按钮
    private JButton smallsheet_to_update;


    public SmallSheetJPanel() {
        setLayout(new BorderLayout());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 60; i++) {
            builder.append("*");
        }
        fengexian = builder.toString();

        add(TopBox(), BorderLayout.NORTH);
        add(center(), BorderLayout.CENTER);
        add(BottomBox(),BorderLayout.SOUTH);
    }

    private JPanel TopBox() {
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.setBorder(new EmptyBorder(20, 0, 20, 0));
        top.setBackground(Color.white);
        top.add(Box.createHorizontalStrut(10));

        JPanel panel =new JPanel();
        FlowLayout flowLayout = new FlowLayout(0,20,0);
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel.setBackground(Color.white);
        panel.setLayout(flowLayout);

        //字号选择
        JComboBox<String> text_size = new JComboBox<>();
        text_size.setFont(blackFont_15);
        text_size.addItem("字号： 10");
        text_size.addItem("字号： 20");
        text_size.addItem("字号： 25");
        text_size.addItem("字号： 30");
        panel.add(text_size);
        //粗体
        JTextField control_bold = new JTextField("  B  ");
        control_bold.setHorizontalAlignment(SwingConstants.CENTER);
        control_bold.setForeground(Color.BLACK);
        control_bold.setFont(blackFont_30);
        control_bold.setBackground(Color.white);
        control_bold.setBorder(new MatteBorder(1, 1, 1, 1, color_BEB));
        control_bold.setMargin(new Insets(0,10,0,10));
        control_bold.setEditable(false);
        panel.add(control_bold);
        //左对齐
        JLabel control_keep_left = new TopbuttonJlabel(getClass().getResource("/image/left_align_unclick.png"));
        panel.add(control_keep_left);
        //居中对齐
        JLabel control_keep_center = new TopbuttonJlabel(getClass().getResource("/image/center_align_unclick.png"));
        panel.add(control_keep_center);
        //右对齐
        JLabel control_keep_right = new TopbuttonJlabel(getClass().getResource("/image/right_align_unclick.png"));
        panel.add(control_keep_right);
        //左缩进
        JLabel left_shift = new TopbuttonJlabel(getClass().getResource("/image/left_shift_unclick.png"));
        panel.add(left_shift);
        //右缩进
        JLabel right_shift = new TopbuttonJlabel(getClass().getResource("/image/right_shift_unclick.png"));
        panel.add(right_shift);
        //格式选择
        JComboBox<String> printFormatVersion = new JComboBox<>();
        printFormatVersion.setFont(blackFont_15);
        printFormatVersion.addItem("小票格式1");
        printFormatVersion.addItem("小票格式2");
        printFormatVersion.addItem("小票格式3");
        printFormatVersion.addItem("小票格式4");
        printFormatVersion.addItem("小票格式5");
        printFormatVersion.addItem("小票格式6");
        printFormatVersion.addItem("小票格式7");
        printFormatVersion.addItem("小票格式8");
        printFormatVersion.addItem("小票格式9");
        printFormatVersion.addItem("小票格式10");
        panel.add(printFormatVersion);
        //使用该格式按钮
        JButton useCurrentFormat = new JButton("使用该格式");
        useCurrentFormat.setFont(blackFont_20);
        useCurrentFormat.setBackground(color_219);
        useCurrentFormat.setForeground(Color.white);
        useCurrentFormat.setMargin(new Insets(0, 10, 0, 10));
        useCurrentFormat.setFocusPainted(false);
        panel.add(useCurrentFormat);
        //删除按钮
        JButton smallsheet_delete = new JButton("删除");
        smallsheet_delete.setFont(blackFont_20);
        smallsheet_delete.setBackground(color_219);
        smallsheet_delete.setForeground(Color.white);
        Insets insets = new Insets(0, 10, 0, 10);
        smallsheet_delete.setMargin(insets);
        smallsheet_delete.setBorder(new EmptyBorder(0,20,0,20));
        smallsheet_delete.setFocusPainted(false);
        top.add(smallsheet_delete,BorderLayout.EAST);

        top.add(panel,BorderLayout.CENTER);
        return top;
    }

    private JPanel center() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new BorderLayout());

        //左边的小票展示界面
        JScrollPane left = new JScrollPane();
        left.setBackground(Color.BLACK);
        left.setBorder(new EmptyBorder(10, 10, 10, 10));
        //
        JPanel smallsheetshow = new JPanel();
        smallsheetshow.setLayout(new BorderLayout());
        //
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.white);
        panel1.setBorder(new EmptyBorder(10,0,0,0));
        panel1.setLayout(new BorderLayout());
        //logo图
        template_logo = new JLabel(getImageIcon(getClass().getResource("/image/viewpager1.jpg"), 150, 150));
        panel1.add(template_logo, BorderLayout.CENTER);
        //页眉
        template_header = new SmallSheetShowTextFiled("博昕");
        panel1.add(template_header, BorderLayout.SOUTH);

        smallsheetshow.add(panel1, BorderLayout.NORTH);

        //展示小票数据的容器
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(27, 1));
        //分割线
        JTextField delimiter_diy1 = new SmallSheetShowTextFiled(fengexian);
        delimiter.add(delimiter_diy1);
        jPanel.add(delimiter_diy1);
        //单据号
        template_document_number = new SmallSheetShowTextFiled("单据号：");
        jPanel.add(template_document_number);
        //日期
        template_date = new SmallSheetShowTextFiled("日期");
        jPanel.add(template_date);
        //分割线
        JTextField delimiter_diy2 = new SmallSheetShowTextFiled(fengexian);
        delimiter.add(delimiter_diy2);
        jPanel.add(delimiter_diy2);

        Box box = Box.createHorizontalBox();
        //商品名称
        template_goods_name = new SmallSheetShowTextFiled("商品名称");
        box.add(template_goods_name);
        //数量
        template_goods_number = new SmallSheetShowTextFiled("数量");
        box.add(template_goods_number);
        //小计
        template_subtotal = new SmallSheetShowTextFiled("小计");
        box.add(template_subtotal);
        jPanel.add(box);

        Box box2 = Box.createHorizontalBox();
        //商品名称
        template_first_goods_name = new SmallSheetShowTextFiled("展示商品");
        box2.add(template_first_goods_name);
        //数量
        template_first_goods_number = new SmallSheetShowTextFiled("X1");
        box2.add(template_first_goods_number);
        //小计
        template_first_subtotal = new SmallSheetShowTextFiled("20");
        box2.add(template_first_subtotal);
        jPanel.add(box2);

        Box box3 = Box.createHorizontalBox();
        //商品名称
        template_second_goods_name = new SmallSheetShowTextFiled("展示商品");
        box3.add(template_second_goods_name);
        //数量
        template_second_goods_number = new SmallSheetShowTextFiled("X2");
        box3.add(template_second_goods_number);
        //小计
        template_second_subtotal = new SmallSheetShowTextFiled("05");
        box3.add(template_second_subtotal);
        jPanel.add(box3);

        Box box4 = Box.createHorizontalBox();
        //商品名称
        template_third_goods_name = new SmallSheetShowTextFiled("展示商品");
        box4.add(template_third_goods_name);
        //数量
        template_third_goods_number = new SmallSheetShowTextFiled("X3");
        box4.add(template_third_goods_number);
        //小计
        template_third_subtotal = new SmallSheetShowTextFiled("27");
        box4.add(template_third_subtotal);
        jPanel.add(box4);

        //分割线
        JTextField delimiter_diy3 = new SmallSheetShowTextFiled(fengexian);
        delimiter.add(delimiter_diy3);
        jPanel.add(delimiter_diy3);
        //合计
        total_money = new SmallSheetShowTextFiled("合计：￥");
        jPanel.add(total_money);
        //支付方式
        payment_method = new SmallSheetShowTextFiled("支付方式：");
        jPanel.add(payment_method);
        //优惠
        template_discont = new SmallSheetShowTextFiled("优惠：￥");
        jPanel.add(template_discont);
        //应付
        template_payable = new SmallSheetShowTextFiled("应付：￥");
        jPanel.add(template_payable);
        //微信支付
        template_payment_method1 = new SmallSheetShowTextFiled("微信支付：￥");
        jPanel.add(template_payment_method1);
        //现金支付
        template_payment_method3 = new SmallSheetShowTextFiled("现金支付：￥");
        jPanel.add(template_payment_method3);
        //分割线
        JTextField delimiter_diy4 = new SmallSheetShowTextFiled(fengexian);
        delimiter.add(delimiter_diy4);
        jPanel.add(delimiter_diy4);
        //页脚1
        template_footer1 = new SmallSheetShowTextFiled("页脚1");
        jPanel.add(template_footer1);
        //页脚2
        template_footer2 = new SmallSheetShowTextFiled("页脚1");
        template_footer2.setVisible(false);
        jPanel.add(template_footer2);
        //页脚3
        template_footer3 = new SmallSheetShowTextFiled("页脚1");
        template_footer3.setVisible(false);
        jPanel.add(template_footer3);
        //页脚4
        template_footer4 = new SmallSheetShowTextFiled("页脚1");
        template_footer4.setVisible(false);
        jPanel.add(template_footer4);
        //页脚5
        template_footer5 = new SmallSheetShowTextFiled("页脚1");
        template_footer5.setVisible(false);
        jPanel.add(template_footer5);
        //页脚6
        template_footer6 = new SmallSheetShowTextFiled("页脚1");
        template_footer6.setVisible(false);
        jPanel.add(template_footer6);
        //页脚7
        template_footer7 = new SmallSheetShowTextFiled("页脚1");
        template_footer7.setVisible(false);
        jPanel.add(template_footer7);
        //页脚8
        template_footer8 = new SmallSheetShowTextFiled("页脚1");
        template_footer8.setVisible(false);
        jPanel.add(template_footer8);
        //页脚9
        template_footer9 = new SmallSheetShowTextFiled("页脚1");
        template_footer9.setVisible(false);
        jPanel.add(template_footer9);
        //页脚10
        template_footer10 = new SmallSheetShowTextFiled("页脚1");
        template_footer10.setVisible(false);
        jPanel.add(template_footer10);
        //底部
        template_ticket_bottom = new SmallSheetShowTextFiled("博昕POS收银系统");
        jPanel.add(template_ticket_bottom);

        //
        smallsheetshow.add(jPanel, BorderLayout.CENTER);
        left.setViewportView(smallsheetshow);
        panel.add(left, BorderLayout.WEST);

        //右边的小票操作页面
        JScrollPane right = new JScrollPane();
        right.setBorder(new EmptyBorder(10, 10, 10, 10));
        right.setBackground(Color.BLACK);
        JPanel smallsheetcontrol = new JPanel();
        smallsheetcontrol.setBackground(Color.BLACK);
        smallsheetcontrol.setLayout(new VFlowLayout());
        smallsheetcontrol.setBorder(new EmptyBorder(10, 5, 10, 10));
        //尺寸
        Box box1 = Box.createHorizontalBox();
        JTextField textField = new SmallSheetShowTextFiled("尺寸");
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        box1.add(textField);
        JTextField textField1 = new SmallSheetShowTextFiled("80MM");
        textField1.setHorizontalAlignment(SwingConstants.RIGHT);
        textField1.setForeground(color_219);
        box1.add(textField1);
        smallsheetcontrol.add(box1);
        //打印张数
        Box box5 = Box.createHorizontalBox();
        box5.setBorder(new EmptyBorder(0, 0, 20, 0));
        JTextField textField2 = new SmallSheetShowTextFiled("打印张数");
        textField2.setHorizontalAlignment(SwingConstants.LEFT);
        box5.add(textField2);
        JTextField textField3 = new SmallSheetShowTextFiled("1");
        textField3.setHorizontalAlignment(SwingConstants.RIGHT);
        textField3.setForeground(color_219);
        box5.add(textField3);
        smallsheetcontrol.add(box5);
        //顶部logo
        Box box6 = Box.createHorizontalBox();
        JTextField textField4 = new SmallSheetShowTextFiled("顶部logo");
        textField4.setHorizontalAlignment(SwingConstants.LEFT);
        box6.add(textField4);
        JTextField textField5 = new SmallSheetShowTextFiled("无");
        textField5.setHorizontalAlignment(SwingConstants.RIGHT);
        textField5.setForeground(color_219);
        box6.add(textField5);
        smallsheetcontrol.add(box6);
        //页眉
        Box box7 = Box.createHorizontalBox();
        JTextField textField6 = new SmallSheetShowTextFiled("页眉");
        textField6.setHorizontalAlignment(SwingConstants.LEFT);
        box7.add(textField6);
        design_header = new SmallSheetShowTextFiled("");
        design_header.setEditable(true);
        design_header.setHorizontalAlignment(SwingConstants.RIGHT);
        box7.add(design_header);
        smallsheetcontrol.add(box7);
        //分割线
        Box box8 = Box.createHorizontalBox();
        box8.setBorder(new EmptyBorder(0, 0, 20, 0));
        JTextField textField8 = new SmallSheetShowTextFiled("分割线");
        textField8.setHorizontalAlignment(SwingConstants.LEFT);
        box8.add(textField8);
        design_delimiterToRepeat = new SmallSheetShowTextFiled("");
        design_delimiterToRepeat.setEditable(true);
        design_delimiterToRepeat.setHorizontalAlignment(SwingConstants.RIGHT);
        box8.add(design_delimiterToRepeat);
        smallsheetcontrol.add(box8);
        //页脚1
        Box box9 = Box.createHorizontalBox();
        JTextField textField7 = new SmallSheetShowTextFiled("页脚1");
        textField7.setHorizontalAlignment(SwingConstants.LEFT);
        box9.add(textField7);
        design_footer1 = new SmallSheetShowTextFiled("");
        design_footer1.setEditable(true);
        design_footer1.setHorizontalAlignment(SwingConstants.RIGHT);
        box9.add(design_footer1);
        smallsheetcontrol.add(box9);
        //页脚2
        design_footer2_layout = Box.createHorizontalBox();
        JTextField textField9 = new SmallSheetShowTextFiled("页脚2");
        textField9.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer2_layout.add(textField9);
        design_footer2 = new SmallSheetShowTextFiled("");
        design_footer2.setEditable(true);
        design_footer2.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer2_layout.add(design_footer2);
        smallsheetcontrol.add(design_footer2_layout);
        //页脚3
        design_footer3_layout = Box.createHorizontalBox();
        JTextField textField10 = new SmallSheetShowTextFiled("页脚3");
        textField10.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer3_layout.add(textField10);
        design_footer3 = new SmallSheetShowTextFiled("");
        design_footer3.setEditable(true);
        design_footer3.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer3_layout.add(design_footer3);
        smallsheetcontrol.add(design_footer3_layout);
        //页脚4
        design_footer4_layout = Box.createHorizontalBox();
        JTextField textField11 = new SmallSheetShowTextFiled("页脚4");
        textField11.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer4_layout.add(textField11);
        design_footer4 = new SmallSheetShowTextFiled("");
        design_footer4.setEditable(true);
        design_footer4.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer4_layout.add(design_footer4);
        smallsheetcontrol.add(design_footer4_layout);
        //页脚5
        design_footer5_layout = Box.createHorizontalBox();
        JTextField textField12 = new SmallSheetShowTextFiled("页脚5");
        textField12.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer5_layout.add(textField12);
        design_footer5 = new SmallSheetShowTextFiled("");
        design_footer5.setEditable(true);
        design_footer5.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer5_layout.add(design_footer5);
        smallsheetcontrol.add(design_footer5_layout);
        //页脚6
        design_footer6_layout = Box.createHorizontalBox();
        JTextField textField13 = new SmallSheetShowTextFiled("页脚6");
        textField13.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer6_layout.add(textField13);
        design_footer6 = new SmallSheetShowTextFiled("");
        design_footer6.setEditable(true);
        design_footer6.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer6_layout.add(design_footer6);
        smallsheetcontrol.add(design_footer6_layout);
        //页脚7
        design_footer7_layout = Box.createHorizontalBox();
        JTextField textField14 = new SmallSheetShowTextFiled("页脚7");
        textField14.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer7_layout.add(textField14);
        design_footer7 = new SmallSheetShowTextFiled("");
        design_footer7.setEditable(true);
        design_footer7.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer7_layout.add(design_footer7);
        smallsheetcontrol.add(design_footer7_layout);
        //页脚8
        design_footer8_layout = Box.createHorizontalBox();
        JTextField textField15 = new SmallSheetShowTextFiled("页脚8");
        textField15.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer8_layout.add(textField15);
        design_footer8 = new SmallSheetShowTextFiled("");
        design_footer8.setEditable(true);
        design_footer8.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer8_layout.add(design_footer8);
        smallsheetcontrol.add(design_footer8_layout);
        //页脚9
        design_footer9_layout = Box.createHorizontalBox();
        JTextField textField16 = new SmallSheetShowTextFiled("页脚9");
        textField16.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer9_layout.add(textField16);
        design_footer9 = new SmallSheetShowTextFiled("");
        design_footer9.setEditable(true);
        design_footer9.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer9_layout.add(design_footer9);
        smallsheetcontrol.add(design_footer9_layout);
        //页脚10
        design_footer10_layout = Box.createHorizontalBox();
        JTextField textField17 = new SmallSheetShowTextFiled("页脚10");
        textField17.setHorizontalAlignment(SwingConstants.LEFT);
        design_footer10_layout.add(textField17);
        design_footer10 = new SmallSheetShowTextFiled("");
        design_footer10.setEditable(true);
        design_footer10.setHorizontalAlignment(SwingConstants.RIGHT);
        design_footer10_layout.add(design_footer10);
        smallsheetcontrol.add(design_footer10_layout);
        //添加页脚按钮
        add_footer = new JPanel();
        add_footer.setLayout(new BorderLayout());
        add_footer.setBackground(Color.white);
        JLabel label = new JLabel(getImageIcon(getClass().getResource("/image/add_footer.png"),40,40));
        label.setBorder(new EmptyBorder(20,0,20,0));
        label.setOpaque(false);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add_footer.add(label,BorderLayout.CENTER);
        smallsheetcontrol.add(add_footer);
        //底部空行
        Box box10 = Box.createHorizontalBox();
        box10.setBackground(Color.white);
        JTextField textField18 = new SmallSheetShowTextFiled("底部空行");
        textField18.setHorizontalAlignment(SwingConstants.LEFT);
        box10.add(textField18);
        box10.add(Box.createHorizontalStrut(400));
        JTextField textField19 = new SmallSheetShowTextFiled("+");
        textField19.setBackground(new Color(Integer.decode("#828282")));
        textField19.setForeground(Color.white);
        box10.add(textField19);
        JTextField textField20 = new SmallSheetShowTextFiled("0");
        box10.add(textField20);
        JTextField textField21 = new SmallSheetShowTextFiled("-");
        textField21.setBackground(new Color(Integer.decode("#828282")));
        textField21.setForeground(Color.white);
        box10.add(textField21);
        smallsheetcontrol.add(box10);
        //底部
        Box box11 = Box.createHorizontalBox();
        JTextField textField22 = new SmallSheetShowTextFiled("底部");
        textField22.setHorizontalAlignment(SwingConstants.LEFT);
        box11.add(textField22);
        design_ticket_bottom = new SmallSheetShowTextFiled("");
        design_ticket_bottom.setHorizontalAlignment(SwingConstants.RIGHT);
        design_ticket_bottom.setEditable(true);
        box11.add(design_ticket_bottom);
        smallsheetcontrol.add(box11);

        right.setViewportView(smallsheetcontrol);
        panel.add(right, BorderLayout.CENTER);
//        panel.add(right);
        return panel;
    }

    private JPanel BottomBox(){
//        Box box = Box.createHorizontalBox();
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout());
        box.setBackground(Color.white);
        box.setBorder(new EmptyBorder(30,10,30,10));
        //测试打印按钮
        print_test = new JButton("测试打印");
        print_test.setFont(blackFont_20);
        print_test.setFocusPainted(false);
        print_test.setForeground(color_219);
        print_test.setBackground(color_E3F);
        print_test.setMargin(new Insets(20,30,20,30));
        box.add(print_test,BorderLayout.WEST);
//        box.add(Box.createHorizontalGlue());

        JPanel right = new JPanel();
        right.setBackground(Color.white);
        right.setLayout(new FlowLayout(10,20,0));
        //同步按钮
        loading_config_data = new JButton("   同   步   ");
        loading_config_data.setFont(blackFont_20);
        loading_config_data.setFocusPainted(false);
        loading_config_data.setForeground(Color.white);
        loading_config_data.setBackground(color_00B);
        loading_config_data.setMargin(new Insets(20,40,20,40));
        right.add(loading_config_data);
//        box.add(Box.createHorizontalStrut(20));

        //新建格式
        smallsheet_create = new JButton("新建格式");
        smallsheet_create.setFont(blackFont_20);
        smallsheet_create.setFocusPainted(false);
        smallsheet_create.setForeground(Color.white);
        smallsheet_create.setBackground(color_219);
        smallsheet_create.setMargin(new Insets(20,40,20,40));
        right.add(smallsheet_create);
//        box.add(Box.createHorizontalStrut(20));

        //修改格式
        smallsheet_to_update = new JButton("修改格式");
        smallsheet_to_update.setFont(blackFont_20);
        smallsheet_to_update.setFocusPainted(false);
        smallsheet_to_update.setForeground(Color.white);
        smallsheet_to_update.setBackground(color_219);
        smallsheet_to_update.setMargin(new Insets(20,40,20,40));
        right.add(smallsheet_to_update);
//        box.add(Box.createHorizontalStrut(20));
        box.add(right,BorderLayout.EAST);
        return box;
    }

    private
    class TopbuttonJlabel extends JLabel {
        public TopbuttonJlabel(URL path) {
            setIcon(getImageIcon(path, 40, 40));
            setBorder(new MatteBorder(1, 1, 1, 1, color_BEB));
        }
    }

    class SmallSheetShowTextFiled extends JTextField {
        public SmallSheetShowTextFiled(String s) {
            setText(s);
            setFont(blackFont_20);
            setEditable(false);
            setHorizontalAlignment(CENTER);
            setForeground(Color.black);
            setBackground(Color.white);
            Insets insets = new Insets(20, 0, 20, 0);
            setMargin(insets);
            setBorder(new EmptyBorder(insets));
        }
    }


}
