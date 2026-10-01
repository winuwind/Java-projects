package AutoFabric.GUI.Supplier;

import AutoFabric.GUI.Graphics;
import AutoFabric.GUI.Window;
import AutoFabric.Suppliers.AbstractSupplier;

import javax.swing.*;

public class WindowSupplier extends JDialog implements Window {
    private final PanelSupplier panelSupplier;

    public WindowSupplier(Graphics graphics, AbstractSupplier supplier) {
        super(graphics, "Supplier: " + supplier.getName(), false);
        graphics.add((Window) this);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        panelSupplier = new PanelSupplier(supplier);
        add(panelSupplier);
        setLocationRelativeTo(null);
        revalidate();
        pack();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
                graphics.remove((Window) WindowSupplier.this);
            }
        });
        setVisible(true);
    }

    @Override
    public void exit(){
        dispose();
    }
}
