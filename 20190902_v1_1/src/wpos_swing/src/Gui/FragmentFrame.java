package Gui;

import Activity.RetailTradeAggregationActivity;
import JPanel.*;
import Layout.VFlowLayout;
import Dialog.LogoutDialog;
import Dialog.ReserveDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;

public class FragmentFrame extends BaseFrame {

    //fragment 的切换
    private CardLayout fragmentCardLayout;
    //交班页面的切换
    private CardLayout cardLayout;
    private JPanel fragemnt;
    //时间
    private final JTextPane time = new JTextPane();

    //退出按钮
    JTextField logout;
    //销售按钮
    JTextField sale;
    //库存按钮
    JTextField retrieveCommodity;
    //设置按钮
    private JMenuBar menuBar;
    JMenu setting;
    //数据处理按钮
    JMenuItem resetBaseDataItem;
    //前台小票按钮
    JMenuItem smallSheetItem;
    //查单按钮
    JTextField checkList;

    ReserveDialog reserveDialog;
    Timer timer;

    public FragmentFrame() {
        //设置窗体大小
        setSize(1080, 1920);
        //去除标题栏
        setUndecorated(true);
        //设置可见性
        setVisible(true);
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        //将窗口分隔成俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, leftJpanel(), rightJpanel());
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(200);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(0);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);

        //交班页面
//        JPanel retailTradeAggregationJPanel = new RetailTradeAggregationJPanel(FragmentFrame.this);

        this.add(jSplitPane, "FragmentActivity");
//        this.add(retailTradeAggregationJPanel, "retailTradeAggregation");

        reserveDialog = new ReserveDialog(FragmentFrame.this, true, 700, 500);

        onClickListener();

        //展示dialog
        timer = new Timer(200, e -> {
            reserveDialog.setVisible(true);
            timer.stop();
        });
        timer.start();
    }

    @Override
    protected void frameInit() {
        super.frameInit();

    }

    private JPanel leftJpanel() {
        //创建容器JPanel
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());


        // 顶部头像和手机号
        JPanel photoWithPhone = new JPanel();
        photoWithPhone.setLayout(new VFlowLayout());
        photoWithPhone.setBackground(color_263);
        //头像控件
        JLabel headPhoto = new JLabel();
        //设置图标
        headPhoto.setIcon(getImageIcon(getClass().getResource("/image/bxlogo.png"), 100, 100));
        //设置水平居中
        headPhoto.setHorizontalAlignment(SwingConstants.CENTER);
        //设置边距
        headPhoto.setBorder(new MatteBorder(8, 0, 0, 0, color_263));
        //添加进去
        photoWithPhone.add(headPhoto, CENTER_ALIGNMENT);

        //手机号控件
        JTextField showPhone = new JTextField("13250551510");
        //字体
        showPhone.setFont(normalFont_15);
        //居中对齐
        showPhone.setHorizontalAlignment(SwingConstants.CENTER);
        //边距
        showPhone.setMargin(new Insets(8, 0, 0, 0));
        //背景
        showPhone.setBackground(color_263);
        //字体颜色
        showPhone.setForeground(Color.white);
        //不可编辑
        showPhone.setEditable(false);
        //白色边框
        showPhone.setBorder(new MatteBorder(0, 0, 0, 0, color_263));
        //添加进去
        photoWithPhone.add(showPhone);

        jPanel.add(photoWithPhone, BorderLayout.NORTH);


        //中间的item
        JPanel item = new JPanel();
        item.setLayout(new VFlowLayout());
        item.setBackground(color_263);

        sale = new itemJtextField("销售");
        retrieveCommodity = new itemJtextField("库存");
        checkList = new itemJtextField("查单");

        menuBar = new JMenuBar();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.CENTER);
        menuBar.setLayout(flowLayout);
        menuBar.setAlignmentX(0.5f);
        menuBar.setBackground(color_263);
        menuBar.setBorderPainted(false);

        setting = new JMenu("设置");
        setting.setFont(normalFont_30);
        setting.setHorizontalAlignment(SwingConstants.CENTER);
        Insets insets = new Insets(20, 20, 20, 20);
        setting.setMargin(insets);
        setting.setBorder(new EmptyBorder(insets));
        setting.setForeground(Color.white);

        resetBaseDataItem = new JMenuItem("数据处理");
        resetBaseDataItem.setFont(normalFont_30);
        resetBaseDataItem.setHorizontalAlignment(SwingConstants.CENTER);
        resetBaseDataItem.setMargin(insets);
        resetBaseDataItem.setBorder(new EmptyBorder(insets));
        resetBaseDataItem.setBackground(color_263);
        resetBaseDataItem.setForeground(Color.white);

        smallSheetItem = new JMenuItem("前台小票");
        smallSheetItem.setFont(normalFont_30);
        smallSheetItem.setHorizontalAlignment(SwingConstants.CENTER);
        smallSheetItem.setMargin(insets);
        smallSheetItem.setBorder(new EmptyBorder(insets));
        smallSheetItem.setBackground(color_263);
        smallSheetItem.setForeground(Color.white);

        setting.add(resetBaseDataItem);
        setting.add(smallSheetItem);

        menuBar.add(setting);

        item.add(sale);
        item.add(retrieveCommodity);
        item.add(menuBar);
//        item.add(setting);
        item.add(checkList);

        //添加进去
        jPanel.add(item, BorderLayout.CENTER);


        // 底部退出和时间显示

        JPanel timeWithLogout = new JPanel();
        timeWithLogout.setLayout(new VFlowLayout());
        timeWithLogout.setBackground(color_263);

        //退出
        logout = new itemJtextField("退出");
        //添加进去
        timeWithLogout.add(logout);

        //时间显示
        time.setText("2020-02-20 \n 16:31:30 \n 联通5G");
        time.setEditable(false);
        time.setBackground(color_263);
        time.setFont(normalFont_15);
        time.setForeground(Color.white);
        //设置文本居中对齐
        SimpleAttributeSet bSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
        //
        StyledDocument styledDocument = time.getStyledDocument();
        styledDocument.setParagraphAttributes(0, styledDocument.getLength(), bSet, false);
        //添加进去
        timeWithLogout.add(time);

        //添加进去
        jPanel.add(timeWithLogout, BorderLayout.SOUTH);

        return jPanel;
    }

    private JPanel rightJpanel() {
        fragemnt = new JPanel();
        fragmentCardLayout = new CardLayout();
        fragemnt.setLayout(fragmentCardLayout);

        //库存页面
        JPanel mainJpanel = new MainJPanel(FragmentFrame.this);
        fragemnt.add(mainJpanel, "main");

        //库存页面
        JPanel retrieveCommodityJpanel = new retrieveCommodityJPanel();
        fragemnt.add(retrieveCommodityJpanel, "retrieveCommodity");

        //查单页面
        JPanel checkListJpanel = new CheckListJPanel();
        fragemnt.add(checkListJpanel, "checkList");

        //重置基础资料页面
        JPanel resetBaseDataJpanel = new ResetBaseDataJPanel();
        fragemnt.add(resetBaseDataJpanel, "resetBaseData");

        //小票格式页面
        JPanel smallSheetJpanel = new SmallSheetJPanel();
        fragemnt.add(smallSheetJpanel, "smallSheet");

        return fragemnt;
    }

    //设置点击事件监听
    private void onClickListener() {
        //点击销售
        sale.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fragmentCardLayout.show(fragemnt, "main");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sale.setBackground(color_455);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sale.setBackground(color_263);
            }
        });
        //点击库存
        retrieveCommodity.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fragmentCardLayout.show(fragemnt, "retrieveCommodity");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                retrieveCommodity.setBackground(color_455);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                retrieveCommodity.setBackground(color_263);
            }
        });
        //点击查单
        checkList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fragmentCardLayout.show(fragemnt, "checkList");
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                checkList.setBackground(color_455);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                checkList.setBackground(color_263);
            }
        });
        //点击退出
        logout.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LogoutDialog d = new LogoutDialog(FragmentFrame.this, true, 700, 320) {
                    @Override
                    protected void sureclick() {
                        //页面切换到交班页面
//                        cardLayout.show(FragmentFrame.this.getContentPane(), "retailTradeAggregation");
                        RetailTradeAggregationActivity.startRetailTradeAggregationActivity();
                        //清除MyDialog
                        dispose();
                        FragmentFrame.this.setVisible(false);
                    }
                };
                d.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logout.setBackground(color_455);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logout.setBackground(color_263);
            }
        });

        //点击设置
        menuBar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setting.doClick();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menuBar.setBackground(color_455);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuBar.setBackground(color_263);
            }
        });

        resetBaseDataItem.addActionListener(e -> fragmentCardLayout.show(fragemnt, "resetBaseData"));
        smallSheetItem.addActionListener(e -> fragmentCardLayout.show(fragemnt, "smallSheet"));

    }

    //自定义item的文本框
    private class itemJtextField extends JTextField {
        itemJtextField(String text) {
            //text
            setText(text);
            //字体
            setFont(normalFont_30);
            //居中对齐
            setHorizontalAlignment(SwingConstants.CENTER);
            //设置外边距和边框
            Insets insets = new Insets(20, 0, 20, 0);
            setMargin(insets);
            setBorder(new EmptyBorder(insets));
            //背景
            setBackground(color_263);
            //字体颜色
            setForeground(Color.white);
            //不可编辑
            setEditable(false);
            setEnabled(true);
        }
    }

}
