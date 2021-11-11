package JPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResetBaseDataJPanel extends BaseJPanel {

    JButton sync_base_data;

    public ResetBaseDataJPanel() {
//        FlowLayout flowLayout = new FlowLayout();
//        flowLayout.setAlignment(FlowLayout.CENTER);
        setLayout(null);
        setBackground(Color.white);

        sync_base_data = new JButton("点 击 进 行 重 置 基 础 资 料");
        sync_base_data.setBounds(700,450,290,80);
        sync_base_data.setFont(blackFont_20);
        sync_base_data.setBackground(color_219);
        sync_base_data.setForeground(Color.white);
        sync_base_data.setFocusPainted(false);
        sync_base_data.setVerticalAlignment(SwingConstants.CENTER);
        sync_base_data.setHorizontalAlignment(SwingConstants.CENTER);
        sync_base_data.setMargin(new Insets(20,20,20,20));
        sync_base_data.setBorder(new EmptyBorder(0,0,0,0));
        add(sync_base_data);
    }
}
