package JPanel;

import Utils.ImgRotate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.*;

public class ImageRotateJpanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final int DELAY = 50;// 转动快慢设置
    //	private final static Long time = (long) 5000;	//窗体关闭事件
    private Timer timer;    //动画计时器
    private int x = 0;
    private Image image = null;
    private int drawX = -1, drawY = -1, imageWidh = -1, imageHeight = -1;

    /**
     * 面板构造函数，初始化面板。包括Timer 的场景。
     */
    public ImageRotateJpanel(Image image) {
        this.image = image;
        if (timer == null) {
            timer = new Timer(DELAY, new ReboundListener());
            timer.start();
        }
    }

    //此方法用来自定义绘画的位置和大小
    public ImageRotateJpanel(Image image, int drawX, int drawY, int imageWidh, int imageHeight) {
        this.image = image;
        if (timer == null) {
            timer = new Timer(DELAY, new ReboundListener());
            timer.start();
        }
        this.drawX = drawX;
        this.drawY = drawY;
        this.imageWidh = imageWidh;
        this.imageHeight = imageHeight;
    }

    /**
     * 动画效果：不断的更新图像的位置，以达到动画的效果。
     */
    private class ReboundListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (x < 360) {
                //控制每个DELAY周期旋转的角度，+ 为逆时针  - 为顺时针
                x = x + 10;
            } else {
                x = 0;
            }
            repaint();
        }
    }

    /**
     * 绘出图像在面板中的位置
     */
    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        drawArc(page);
    }


    /**
     * 画图形
     */
    private void drawArc(Graphics g) {
        if (imageWidh == -1) {
            drawX = getWidth() / 2;
            drawY = getHeight() / 2;
            imageWidh = imageHeight = 180;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        //抗锯齿
        //JDK文档：http://tool.oschina.net/uploads/apidocs/jdk-zh/java/awt/RenderingHints.html
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        int width = getWidth();
//        int height = getHeight();
//        //设置画笔颜色
////        g2d.setColor(Color.BLACK);
////        g2d.drawArc(width / 2 - 110, height / 2 - 110, 10 + 200, 10 + 200, 0, 360);
//        g2d.setColor(Color.RED);
//        g2d.fillArc(width / 2 - 110, height / 2 - 110, 10 + 200, 10 + 200, x, 240);
//        g2d.setColor(Color.BLACK);
//        g2d.fillArc(width / 2 - 90, height / 2 - 90, 10 + 160, 10 + 160, 0, 360);
//        g2d.dispose();

        ImageObserver imageObserver = new ImageObserver() {

            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                // TODO Auto-generated method stub
                return false;
            }
        };
        if (image != null) {
            BufferedImage bufferedImage = ImgRotate.rotateImage(ImgRotate.ImageToBufferedImage(image), x);
            g.drawImage(bufferedImage, drawX, drawY, imageWidh, imageHeight, imageObserver);
        }

    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (!aFlag){
            //不可见时
            destory();
        }else {
            //可见时
            if (timer == null){
                timer = new Timer(DELAY, new ReboundListener());
                timer.start();
            }
        }
    }

    public void destory() {
        timer.stop();
        x = 0;
        timer = null;
    }
}