package AutoFabric.GUI.Supplier;

import AutoFabric.GUI.Button.ButtonEditor;
import AutoFabric.GUI.Button.ButtonRenderer;
import AutoFabric.GUI.Graphics;
import AutoFabric.GUI.WindowTable.Window;
import AutoFabric.Suppliers.AbstractSupplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelSuppliers extends JPanel {
    private final Timer timer;
    private final JTable table;
    private final DefaultTableModel model;

    public PanelSuppliers(Graphics graphics, AbstractSupplier[] suppliers) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton toggleButton = new JButton("Show suppliers");
        add(toggleButton);

        String[] columnNames = {"Name Supplier", "Delay", "View Button"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumn("View Button").setCellRenderer(new ButtonRenderer("view"));
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), "view");
        table.getColumn("View Button").setCellEditor(buttonEditor);
        scrollPane.setVisible(true);

        for (AbstractSupplier supplier : suppliers) {
            Object[] formatLine = {supplier.getName(), supplier.getDelay()};
            model.addRow(formatLine);
        }

        buttonEditor.setListener(_ ->{
            int row = table.getEditingRow();
            if (row >= 0) {
                WindowSupplier window = new WindowSupplier(graphics, suppliers[row]);
            }
        });

        toggleButton.addActionListener(_ ->{
            Window window = new Window(graphics, scrollPane, "Suppliers");
        });
        timer = new Timer(100, _->{
            for (int i = 0; i < suppliers.length; i++) {
                model.setValueAt(suppliers[i].getDelay(), i, 1);
            }
        });
        timer.start();
        revalidate();
    }

    public void exit(){
        timer.stop();
    }
}
