package AutoFabric.GUI.WindowTable;

import AutoFabric.GUI.Graphics;

import javax.swing.*;

public class Window extends JDialog {
    public Window(Graphics graphics, JScrollPane scrollPane, String title) {
        super(graphics);
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        add(scrollPane);
        revalidate();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
