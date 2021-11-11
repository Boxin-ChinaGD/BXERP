package Utils;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class MyToast extends javax.swing.JFrame {
    private JLabel text;
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension screensize = tk.getScreenSize();
    int height = screensize.height;
    int width = screensize.width;
    private String str = null;

    public MyToast(String str) {
        this.str = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initGUI();
            }
        }).start();
    }

    private void initGUI() {
        try {
            setUndecorated(true);
            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                text = new JLabel("<html>" + str + "</html>", JLabel.CENTER);
                getContentPane().add(text, BorderLayout.CENTER);
                getContentPane().setBackground(Color.white);
                com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.8f);
                text.setBackground(Color.white);
                text.setForeground(new Color(Integer.decode("#263238")));
                text.setFont(new Font("宋体", 1, 15));
            }
            pack();
            setBounds(width / 2 - 180, height * 2 / 3 - 30, 100, 60);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
