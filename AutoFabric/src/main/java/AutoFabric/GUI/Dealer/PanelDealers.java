package AutoFabric.GUI.Dealer;

import AutoFabric.Dealers.Dealer;
import AutoFabric.GUI.Button.ButtonEditor;
import AutoFabric.GUI.Button.ButtonRenderer;
import AutoFabric.GUI.Graphics;
import AutoFabric.GUI.WindowTable.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelDealers extends JPanel {
    private final Timer timer;
    private final JTable table;
    private final DefaultTableModel model;

    public PanelDealers(Graphics graphics, Dealer[] dealers) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton toggleButton = new JButton("Show dealers");
        add(toggleButton);

        String[] columnNames = {"Name Dealer", "Delay", "View Button"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumn("View Button").setCellRenderer(new ButtonRenderer("view"));
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), "view");
        table.getColumn("View Button").setCellEditor(buttonEditor);
        scrollPane.setVisible(true);

        for (Dealer dealer : dealers) {
            Object[] formatLine = {dealer.getName(), dealer.getDelay()};
            model.addRow(formatLine);
        }

        buttonEditor.setListener(_ ->{
            int row = table.getEditingRow();
            if (row >= 0) {
                WindowDealer window = new WindowDealer(graphics, dealers[row]);
            }
        });

        toggleButton.addActionListener(_ ->{
            Window window = new Window(graphics, scrollPane, "Dealers");
        });
        timer = new Timer(100, _->{
            for (int i = 0; i < dealers.length; i++) {
                model.setValueAt(dealers[i].getDelay(), i, 1);
            }
        });
        timer.start();
        revalidate();
    }

    public void exit(){
        timer.stop();
    }
}
