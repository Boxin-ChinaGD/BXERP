package Dialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class RemoveCommodityDialog extends JDialog{
    //移除商品按钮
    JButton remove_commodity;
    Font font = new Font("微软雅黑", 1, 20);
    JButton closeButton = new JButton();

    public RemoveCommodityDialog(JFrame parent, boolean modal, int windowWidth, int windowHeight){
        super(parent, modal);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setUndecorated(true);
        setResizable(false);
        setSize(windowWidth, windowHeight);
        setLocation((screenWidth - windowWidth) / 2,(screenHeight - windowHeight) / 2);
        setLayout(new BorderLayout());

        JPanel allButton = new JPanel();
        allButton.setLayout(new GridLayout(1,3,10,0));
        allButton.setBorder(new EmptyBorder(10,10,10,10));
        remove_commodity = new JButton("移除商品");
        remove_commodity.setFocusPainted(false);
        remove_commodity.setFont(font);
        remove_commodity.setBackground(Color.BLACK);
        remove_commodity.setForeground(Color.white);
        allButton.add(remove_commodity);
        //
        JButton button = new JButton("添加到收藏夹");
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBackground(new Color(Integer.decode("#2196F3")));
        button.setForeground(Color.white);
        allButton.add(button);
        //
        JButton button1 = new JButton("以当前数量报损");
        button1.setFocusPainted(false);
        button1.setFont(font);
        button1.setBackground(new Color(Integer.decode("#00BFA5")));
        button1.setForeground(Color.white);
        allButton.add(button1);

        add(closeJpanel(),BorderLayout.NORTH);
        add(allButton,BorderLayout.CENTER);
        setListener();
    }

    private void setListener(){
        remove_commodity.addActionListener(e -> dispose());
    }

    protected JPanel closeJpanel() {
        //创建容器JPanel
        JPanel jPanel = new JPanel();
        //设置布局居右对齐
        jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jPanel.setBorder(new EmptyBorder(0,0,10,0));

        //关闭窗口按钮
        Border border = BorderFactory.createEmptyBorder(0, 1, 0, 1);
        //添加图标
        closeButton.setIcon(getImageIcon(getClass().getResource("/image/delete_all.png"), 32, 32));
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(50, 30));
        closeButton.setBackground(new Color(Integer.decode("#EEEEEE")));
        closeButton.setBorder(border);
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(Integer.decode("#1A4A9F")));
            }

            public void mouseExited(MouseEvent e) {
                // TODO 自动生成的方法存根
                closeButton.setBackground(new Color(Integer.decode("#EEEEEE")));
            }
        });
        closeButton.addActionListener((e) -> {
            this.dispose();
        });
        jPanel.add(closeButton);

        return jPanel;
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
