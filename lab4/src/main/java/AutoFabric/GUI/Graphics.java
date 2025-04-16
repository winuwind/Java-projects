package AutoFabric.GUI;

import AutoFabric.Dealers.Dealer;
import AutoFabric.Factory.Factory;
import AutoFabric.GUI.Dealer.PanelDealers;
import AutoFabric.GUI.Storage.PanelStorages;
import AutoFabric.GUI.Supplier.PanelSuppliers;
import AutoFabric.GUI.Task.PanelTask;
import AutoFabric.Storage.Storage;
import AutoFabric.Suppliers.AbstractSupplier;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Graphics extends JFrame {
    private final PanelSuppliers panelSuppliers;
    private final PanelDealers panelDealers;
    private final PanelStorages panelStorages;
    private final PanelTask panelTask;
    private boolean isRunning = true;
    private final ArrayList<Window> dialogs;


    public Graphics(AbstractSupplier[] suppliers, Dealer[] dealers, Storage carStorage, Storage engineStorage, Storage bodyStorage, Storage accessoryStorage, int countTypeCar, Factory factory) {
        super("Fabric");
        dialogs = new ArrayList<>();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        panelTask = new PanelTask(this, factory);
        add(panelTask);
        panelSuppliers = new PanelSuppliers(this, suppliers);
        add(panelSuppliers);
        panelStorages = new PanelStorages(this, carStorage, engineStorage, bodyStorage, accessoryStorage, countTypeCar);
        add(panelStorages);
        panelDealers = new PanelDealers(this, dealers);
        add(panelDealers);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                for (Window dialog : dialogs) {
                    dialog.exit();
                }
                dialogs.clear();
                dispose();
                isRunning = false;
                panelDealers.exit();
                panelStorages.exit();
                panelSuppliers.exit();
                panelTask.exit();
            }
        });
        revalidate();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void add(Window dialog) {
        dialogs.add(dialog);
    }

    public void remove(Window dialog) {
        dialogs.remove(dialog);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
