package AutoFabric.GUI.Storage;

import AutoFabric.GUI.Button.ButtonEditor;
import AutoFabric.GUI.Button.ButtonRenderer;
import AutoFabric.GUI.Graphics;
import AutoFabric.GUI.WindowTable.Window;
import AutoFabric.Storage.Storage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelStorages extends JPanel {
    private final Storage carStorage;
    private final Storage engineStorage;
    private final Storage bodyStorage;
    private final Storage accessoryStorage;
    private final Timer timer;

    private final JTable table;
    private final DefaultTableModel model;

    public PanelStorages(Graphics graphics, Storage carStorage, Storage engineStorage, Storage bodyStorage, Storage accessoryStorage, int countTypeCar) {
        super();
        this.carStorage = carStorage;
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton toggleButton = new JButton("Show storages");
        add(toggleButton);

        String[] columnNames = {"Name Storage", "Count Product", "View Button"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumn("View Button").setCellRenderer(new ButtonRenderer("view"));
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox(), "view");
        table.getColumn("View Button").setCellEditor(buttonEditor);
        scrollPane.setVisible(true);

        Object[] formatLine = {"Cars", carStorage.getSize()};
        model.addRow(formatLine);
        formatLine = new Object[]{"Engines", engineStorage.getSize()};
        model.addRow(formatLine);
        formatLine = new Object[]{"Bodies", bodyStorage.getSize()};
        model.addRow(formatLine);
        formatLine = new Object[]{"Accessories", accessoryStorage.getSize()};
        model.addRow(formatLine);

        buttonEditor.setListener(_ ->{
            int row = table.getEditingRow();
            WindowStorage window = switch (row){
                case 0 -> new WindowStorage(graphics, this.carStorage, countTypeCar);
                case 1 -> new WindowStorage(graphics, this.engineStorage, countTypeCar);
                case 2 -> new WindowStorage(graphics, this.bodyStorage, countTypeCar);
                case 3 -> new WindowStorage(graphics, this.accessoryStorage, countTypeCar);
                default -> null;
            };
        });

        toggleButton.addActionListener(_ ->{
            Window window = new Window(graphics, scrollPane, "Storages");
        });
        timer = new Timer(100, _ ->{
            model.setValueAt(carStorage.getSize(), 0, 1);
            model.setValueAt(engineStorage.getSize(), 1, 1);
            model.setValueAt(bodyStorage.getSize(), 2, 1);
            model.setValueAt(accessoryStorage.getSize(), 3, 1);
        });
        timer.start();
        revalidate();
    }

    public void exit(){
        timer.stop();
    }
}
