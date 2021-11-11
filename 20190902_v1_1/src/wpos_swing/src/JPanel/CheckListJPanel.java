package JPanel;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class CheckListJPanel extends BaseJPanel implements ListSelectionListener {
    Box box = Box.createHorizontalBox();

    //退货页面
    JPanel returngoods;

    //查单的输入框
    JTextField check_list_sn;
    //查单的输入日期控件
    JButton check_list_startDate;
    JButton check_list_endtDate;
    //查单的搜索按钮
    JButton check_list_search;
    //退货数量
    JTextField tv_returngoods_number;
    //退货金额
    JTextField return_amount;
    //退货页面的原价金额
    JTextField original_price;
    //退货页面的应收
    JTextField receivable;
    //退货页面的实收
    JTextField net_receipts;
    //退货页面的折扣
    JTextField discount;
    //退货页面的支付方式
    JTextField payment_method;
    //退货页面的重打小票按钮
    JButton reprint_SmallSheet;
    //退货页面的退货按钮
    JButton confirm_return_goods;

    public CheckListJPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setLayout(new BorderLayout());

        JPanel check = new JPanel();
        check.setLayout(new BorderLayout());

        //顶部列表解释文本行
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 6));
        top.setBackground(color_E3F);
        //销售单号
        JTextField salecode = new categoryTextFiled("销售单号");
        top.add(salecode);
        //结算时间
        JTextField saletime = new categoryTextFiled("结算时间");
        top.add(saletime);
        //销售金额
        JTextField saleprice = new categoryTextFiled("销售金额");
        top.add(saleprice);
        //添加到查单页面
        check.add(top, BorderLayout.NORTH);

        // TODO: 2020/5/21 列表传值
        //展示列表的容器
        JList<String> jList = new JList<>(new MyListModel());
        JScrollPane jScrollPane = new JScrollPane(jList);
        jScrollPane.setBackground(Color.white);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //列表项监听
        jList.addListSelectionListener(this);
        //位于父布局中央
        check.add(jScrollPane, BorderLayout.CENTER);

        //底部搜索容器
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.setBackground(color_ECE);

        //页码容器
        JPanel selectpage = new JPanel();
        selectpage.setBackground(color_ECE);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        selectpage.setLayout(flowLayout);

        //上一页
        JLabel lastpage = new JLabel(getImageIcon(getClass().getResource("/image/left_arrow.png"), 20, 20));
        lastpage.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        //当前页数
        JTextField currentpage = new JTextField("1");
        currentpage.setFont(normalFont_30);
        currentpage.setBackground(Color.white);
        currentpage.setEditable(false);
        currentpage.setMargin(new Insets(10, 10, 10, 10));
        //斜杠
        JTextField textField = new JTextField(" / ");
        textField.setFont(normalFont_30);
        textField.setOpaque(false);
        textField.setEditable(false);
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        //总页数
        JTextField totalpage = new JTextField("1");
        totalpage.setFont(normalFont_30);
        textField.setOpaque(false);
        totalpage.setEditable(false);
        totalpage.setBorder(new EmptyBorder(0, 0, 0, 0));
        //下一页
        JLabel nextpage = new JLabel(getImageIcon(getClass().getResource("/image/right_arrow.png"), 20, 20));
        lastpage.setBorder(new EmptyBorder(0, 10, 0, 0));

        selectpage.add(lastpage);
        selectpage.add(currentpage);
        selectpage.add(textField);
        selectpage.add(totalpage);
        selectpage.add(nextpage);

        JPanel rightJpanel = new JPanel();
        rightJpanel.setLayout(new BorderLayout(20, 20));
        rightJpanel.setBorder(new MatteBorder(20, 0, 20, 10, color_ECE));
        rightJpanel.setBackground(color_ECE);

        //单号搜索容器
        JPanel checklistSnJpanel = new JPanel();
        checklistSnJpanel.setLayout(new BorderLayout());
        checklistSnJpanel.setBorder(new MatteBorder(1, 1, 1, 1, color_9E9));
        checklistSnJpanel.setBackground(Color.white);

        //搜索图标
        JLabel searchicon = new JLabel(getImageIcon(getClass().getResource("/image/search_black.png"), 40, 40));
        searchicon.setBorder(new EmptyBorder(10, 10, 10, 10));
        //输入框
        check_list_sn = new JTextField();
        check_list_sn.setFont(normalFont_30);
        check_list_sn.setMargin(new Insets(10, 10, 10, 10));
        check_list_sn.setBorder(new EmptyBorder(10, 10, 10, 10));
        check_list_sn.setBackground(Color.white);
        //
        checklistSnJpanel.add(searchicon, BorderLayout.WEST);
        checklistSnJpanel.add(check_list_sn, BorderLayout.CENTER);
        rightJpanel.add(checklistSnJpanel, BorderLayout.NORTH);
        //
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 10));
        //起始日期
        check_list_startDate = new JButton();
        check_list_startDate.setText("选择起始日期");
        check_list_startDate.setFont(blackFont_15);
        check_list_startDate.setBackground(Color.white);
        check_list_startDate.setFocusPainted(false);
        check_list_startDate.setHorizontalAlignment(SwingConstants.CENTER);
        check_list_startDate.setMargin(new Insets(20, 10, 20, 10));
        panel.add(check_list_startDate);
        //结束日期
        check_list_endtDate = new JButton();
        check_list_endtDate.setText("选择结束日期");
        check_list_endtDate.setFont(blackFont_15);
        check_list_endtDate.setFocusPainted(false);
        check_list_endtDate.setBackground(Color.white);
        check_list_endtDate.setHorizontalAlignment(SwingConstants.CENTER);
        check_list_startDate.setMargin(new Insets(20, 10, 20, 10));
        panel.add(check_list_endtDate);
        //筛选按钮
        check_list_search = new JButton("筛 选");
        check_list_search.setFont(blackFont_20);
        check_list_search.setForeground(color_219);
        check_list_search.setBackground(color_E3F);
        check_list_search.setFocusPainted(false);
        check_list_search.setMargin(new Insets(20, 10, 20, 10));
        check_list_search.setBorder(BorderFactory.createLineBorder(color_219, 1, true));
        panel.add(check_list_search);
        rightJpanel.add(panel, BorderLayout.CENTER);

        //将窗口分隔成俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, selectpage, rightJpanel);
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(1000);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(0);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);
        //
        bottom.add(jSplitPane);
        //位于父布局的底部
        check.add(bottom, BorderLayout.SOUTH);

        returngoods = returnGoodsJpanel();

        box.add(check);
        add(box);
    }

    //退货页面
    protected JPanel returnGoodsJpanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());

        //顶部列表解释文本行
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 6));
        top.setBackground(color_219);
        //销售单号
        JTextField code = new categoryTextFiled("序号");
        code.setBackground(color_219);
        top.add(code);
        //结算时间
        JTextField name = new categoryTextFiled("名称");
        name.setBackground(color_219);
        top.add(name);
        //销售金额
        JTextField oneprice = new categoryTextFiled("单价");
        oneprice.setBackground(color_219);
        top.add(oneprice);
        //销售金额
        JTextField num = new categoryTextFiled("数量");
        num.setBackground(color_219);
        top.add(num);
        //销售金额
        JTextField price = new categoryTextFiled("金额");
        price.setBackground(color_219);
        top.add(price);
        //添加到查单页面
        jPanel.add(top, BorderLayout.NORTH);

        //展示列表的容器
        JList<String> jList = new JList<>(new MyListModel());
        JScrollPane jScrollPane = new JScrollPane(jList);
        jScrollPane.setBackground(Color.white);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        //位于父布局中央
        jPanel.add(jScrollPane, BorderLayout.CENTER);

        //底部容器
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());

        //退货数量以及金额
        Box returnNun = Box.createHorizontalBox();
        returnNun.setBackground(Color.white);
        returnNun.setOpaque(false);
        //
        JTextField textField = new JTextField("退货数量:");
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(blackFont_20);
        textField.setBackground(Color.white);
        textField.setEditable(false);
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        returnNun.add(textField);
        //退货数量
        tv_returngoods_number = new JTextField("0件");
        tv_returngoods_number.setFont(blackFont_20);
        tv_returngoods_number.setEditable(false);
        tv_returngoods_number.setBackground(Color.white);
        tv_returngoods_number.setBorder(new EmptyBorder(0, 0, 0, 0));
        returnNun.add(tv_returngoods_number);
        //
        JTextField textField1 = new JTextField("退货金额:");
        textField1.setHorizontalAlignment(SwingConstants.CENTER);
        textField1.setFont(blackFont_20);
        textField1.setForeground(color_F44);
        textField1.setEditable(false);
        textField1.setBackground(Color.white);
        textField1.setBorder(new EmptyBorder(0, 0, 0, 0));
        returnNun.add(textField1);
        //退货金额
        return_amount = new JTextField("00.00");
        return_amount.setFont(blackFont_30);
        return_amount.setForeground(color_F44);
        return_amount.setEditable(false);
        return_amount.setBackground(Color.white);
        return_amount.setBorder(new EmptyBorder(0, 0, 0, 0));
        returnNun.add(return_amount);

        bottom.add(returnNun, BorderLayout.NORTH);

        //零售单详情
        JPanel tradedetail = new JPanel();
        tradedetail.setLayout(new BorderLayout());
        tradedetail.setBorder(new EmptyBorder(0,0,0,0));
        tradedetail.setBackground(color_ECE);

        JPanel left = new JPanel();
        left.setLayout(new GridLayout(4, 2,0,10));
        left.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField textField2 = new ReturnGoodsTextFiled("原价总金额：");
        left.add(textField2);
        original_price = new ReturnGoodsTextFiled("000.00");
        original_price.setHorizontalAlignment(SwingConstants.LEFT);
        left.add(original_price);

        JTextField textField3 = new ReturnGoodsTextFiled("应收：");
        left.add(textField3);
        receivable = new ReturnGoodsTextFiled("000.00");
        receivable.setHorizontalAlignment(SwingConstants.LEFT);
        left.add(receivable);

        JTextField textField4 = new ReturnGoodsTextFiled("实收：");
        left.add(textField4);
        net_receipts = new ReturnGoodsTextFiled("000.00");
        net_receipts.setHorizontalAlignment(SwingConstants.LEFT);
        left.add(net_receipts);

        JTextField textField5 = new ReturnGoodsTextFiled("折扣：");
        left.add(textField5);
        discount = new ReturnGoodsTextFiled("000.00");
        discount.setHorizontalAlignment(SwingConstants.LEFT);
        left.add(discount);

        //支付方式以及重打小票和退货按钮
        JPanel right = new JPanel();
        right.setLayout(new GridLayout(2, 1));
        right.setBorder(new EmptyBorder(0,0,20,30));
        //支付方式
        JPanel paytype = new JPanel();
        paytype.setLayout(new GridLayout(1, 2));

        JTextField textField6 = new ReturnGoodsTextFiled("支付方式：");
        textField6.setHorizontalAlignment(SwingConstants.LEFT);
        paytype.add(textField6);
        payment_method = new ReturnGoodsTextFiled("支付宝支付");
        payment_method.setHorizontalAlignment(SwingConstants.LEFT);
        paytype.add(payment_method);

        right.add(paytype);

        //重打小票按钮
        reprint_SmallSheet = new JButton("重打小票");
        reprint_SmallSheet.setFont(blackFont_20);
        reprint_SmallSheet.setForeground(Color.white);
        reprint_SmallSheet.setBackground(color_00B);
        reprint_SmallSheet.setFocusPainted(false);
        right.add(reprint_SmallSheet);
        //退货按钮
        //todo 退货按钮还没有添加进去，只是实例化
        confirm_return_goods = new JButton("退货");
        confirm_return_goods.setFont(blackFont_20);
        confirm_return_goods.setForeground(Color.white);
        confirm_return_goods.setBackground(color_00B);

        //将窗口分隔成俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, left, right);
        jSplitPane.setBorder(new EmptyBorder(0,0,0,0));
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(400);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(0);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);
        tradedetail.add(jSplitPane, BorderLayout.CENTER);

        bottom.add(tradedetail, BorderLayout.CENTER);

        //位于父布局底部
        jPanel.add(bottom, BorderLayout.SOUTH);
        return jPanel;
    }

    //查单列表的点击事件
    @Override
    public void valueChanged(ListSelectionEvent e) {
        //查单页面的列表点击事件
        box.add(returngoods);
        box.validate();
        box.repaint();
    }

    class ReturnGoodsTextFiled extends JTextField {
        public ReturnGoodsTextFiled(String s) {
            setText(s);
            setFont(blackFont_20);
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setEditable(false);
            setHorizontalAlignment(RIGHT);
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


