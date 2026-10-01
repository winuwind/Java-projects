package AutoFabric.GUI.Storage;

import AutoFabric.GUI.Window;
import AutoFabric.Storage.Storage;

import javax.swing.*;
import java.awt.*;

public class WindowStorage extends JDialog implements AutoFabric.GUI.Window {
    private final PanelStorage panelStorage;

    public WindowStorage(AutoFabric.GUI.Graphics graphics, Storage storage, int countTypeCar) {
        super(graphics, "Storage", false);
        graphics.add((AutoFabric.GUI.Window) this);
        setTitle("Storage " + storage.getTypeProduction());
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        panelStorage = new PanelStorage(storage, countTypeCar);
        add(panelStorage);
        JPanel buttonPanel = new JPanel();
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(_ -> {
            storage.clear();
        });
        buttonPanel.add(clearButton);
        add(buttonPanel);
        setLocationRelativeTo(null);
        setSize(new Dimension(200, 150));
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
                graphics.remove((Window) WindowStorage.this);
            }
        });
        setVisible(true);
    }

    @Override
    public void exit() {
        panelStorage.exit();
        dispose();
    }
}
