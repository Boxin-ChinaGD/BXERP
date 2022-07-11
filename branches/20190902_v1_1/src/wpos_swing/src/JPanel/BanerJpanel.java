package JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class BanerJpanel extends JPanel {
    private int index = 0;
    private LinkedList<ImageIcon> list;

    public BanerJpanel(LinkedList<ImageIcon> list) {
        this.list = list;
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (list!= null && list.size() != 0) {
            g.drawImage(list.get(index).getImage(), 0, 0, this);
            if (index == list.size()-1){
                index = 0;
                return;
            }
        }
        index++;
    }
}
