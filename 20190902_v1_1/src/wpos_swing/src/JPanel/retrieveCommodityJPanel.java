package JPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class retrieveCommodityJPanel extends BaseJPanel {
    //库存的输入框
    JTextField condition_input;

    public retrieveCommodityJPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setLayout(new BorderLayout());

        //顶部列表解释文本行
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 6));
        top.setBackground(color_E3F);
        //条形码
        JTextField code = new categoryTextFiled("条形码");
        top.add(code);
        //条形码
        JTextField name = new categoryTextFiled("类别");
        top.add(name);
        //商品类别
        JTextField category = new categoryTextFiled("名称");
        top.add(category);
        //商品价格
        JTextField sales_price = new categoryTextFiled("商品价格");
        top.add(sales_price);
        //库存
        JTextField unit = new categoryTextFiled("库存");
        top.add(unit);
        //单位
        JTextField count = new categoryTextFiled("单位");
        top.add(count);

        add(top, BorderLayout.NORTH);

        // TODO: 2020/5/21 列表传值
        //展示列表的容器
        JList<String> jList = new JList<>(new MyListModel());
        JScrollPane jScrollPane = new JScrollPane(jList);
        jScrollPane.setBackground(Color.white);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        add(jScrollPane, BorderLayout.CENTER);

        //底部搜索容器
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        bottom.setBackground(color_ECE);

        //输入框和搜索按钮的容器
        JPanel searchinput = new JPanel(new BorderLayout());
        searchinput.setBorder(new MatteBorder(10, 20, 10, 20, color_ECE));
        //输入框
        condition_input = new JTextField();
        condition_input.setFont(normalFont_30);
        condition_input.setBackground(Color.white);
        condition_input.setMargin(new Insets(10, 10, 10, 0));
        condition_input.setBorder(new MatteBorder(1, 1, 1, 1, color_9E9));
        searchinput.add(condition_input, BorderLayout.CENTER);

        //搜索按钮
        JPanel searchpanel = new JPanel();
        //设置背景色
        searchpanel.setBackground(color_9E9);
        //按钮图片
        JLabel search = new JLabel(getImageIcon(getClass().getResource("/image/search.png"), 40, 40));
        //设置为透明
        search.setOpaque(false);
        //边框
        search.setBorder(new MatteBorder(10, 10, 10, 10, color_9E9));
        //添加进去
        searchpanel.add(search);
        searchinput.add(searchpanel, BorderLayout.EAST);

        //同步所有数据按钮
        JButton sync = new JButton();
        sync.setFont(blackFont_20);
        sync.setText("同  步  所  有  数  据");
        //设置颜色
        sync.setBackground(color_00B);
        sync.setForeground(Color.white);
        //去除字外面的虚线框
        sync.setFocusPainted(false);
        sync.setBorder(new MatteBorder(10, 10, 10, 10, color_ECE));

        //将窗口分隔成俩边
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, searchinput, sync);
        //分割线的位置  也就是初始位置
        jSplitPane.setDividerLocation(1100);
        //是否可展开或收起，在这里没用
        jSplitPane.setOneTouchExpandable(false);
        //设置分割线的宽度 像素为单位
        jSplitPane.setDividerSize(0);
        //设置分割线不可拖动！！
        jSplitPane.setEnabled(false);

        bottom.add(jSplitPane);

        add(bottom, BorderLayout.SOUTH);
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
