package AutoFabric.GUI.Dealer;

import AutoFabric.Dealers.Dealer;
import AutoFabric.GUI.Graphics;
import AutoFabric.GUI.Window;

import javax.swing.*;

public class WindowDealer extends JDialog implements Window {
    private final PanelDealer panelDealer;

    public WindowDealer(Graphics graphics, Dealer dealer) {
        super(graphics, "Dealer: " + dealer.getName(), false);
        graphics.add((Window) this);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        panelDealer = new PanelDealer(dealer);
        add(panelDealer);
        setLocationRelativeTo(null);
        revalidate();
        pack();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
                graphics.remove((Window) WindowDealer.this);
            }
        });
        setVisible(true);
    }

    @Override
    public void exit() {
        dispose();
    }
}
