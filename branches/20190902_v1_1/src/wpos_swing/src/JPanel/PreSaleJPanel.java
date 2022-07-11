package JPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PreSaleJPanel extends BaseJPanel{

    private final JScrollPane jScrollPane;
    private final JList<String> jList;
    //下载基础资料按钮
    JButton download_basedata;
    //按钮
    public JButton logout;

    public PreSaleJPanel(){
        setLayout(new BorderLayout());

        JPanel left = new JPanel();
        left.setBorder(new EmptyBorder(20,20,20,20));
        left.setLayout(new GridLayout(2,1,10,500));
        //
        download_basedata = new JButton("下载基础资料");
        download_basedata.setBackground(color_219);
        download_basedata.setForeground(Color.white);
        download_basedata.setFont(blackFont_20);
        download_basedata.setMargin(new Insets(0,20,0,20));
        download_basedata.setFocusPainted(false);
        //
        logout = new JButton("退出");
        logout.setBackground(Color.BLACK);
        logout.setForeground(Color.white);
        logout.setFont(blackFont_20);
        logout.setFocusPainted(false);

        left.add(download_basedata);
        left.add(logout);

        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());

        JTextField textField = new JTextField("基础资料下载信息：");
        textField.setEditable(false);
        textField.setFont(blackFont_20);
        textField.setBorder(new EmptyBorder(20,20,20,20));

        //展示列表的容器
        jList = new JList<>(new MyListModel());
        jScrollPane = new JScrollPane(jList);
        jScrollPane.setBackground(Color.white);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        center.add(textField,BorderLayout.NORTH);
        center.add(jScrollPane,BorderLayout.CENTER);

        add(left,BorderLayout.WEST);
        add(center,BorderLayout.CENTER);

    }


    //列表适配器
    class MyListModel extends AbstractListModel<String> {// 继承抽象类 AbstractListModel
        private static final long serialVersionUID = 1L;
        // 设置列表框内容
        private String[] contents = {"下载完成1", "下载完成2", "下载完成3", "下载完成4", "下载完成5", "下载完成6", "下载完成1", "下载完成2", "下载完成3", "下载完成4", "下载完成5", "下载完成6", "下载完成1", "下载完成2", "下载完成3", "下载完成4", "下载完成5", "下载完成6"};

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
