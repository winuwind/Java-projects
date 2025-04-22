package AutoFabric.GUI.Task;

import AutoFabric.Factory.Factory;
import AutoFabric.GUI.Button.ButtonEditor;
import AutoFabric.GUI.Button.ButtonRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class PanelTasks extends JPanel {
    private ArrayList<Factory.Task> tasks;
    private final Timer timer;
    private final JTable table;
    private final DefaultTableModel model;
    private final ButtonEditor buttonEditor;

    public PanelTasks(Factory factory) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        tasks = new ArrayList<>(factory.getTasks());

        String[] columnNames = {"Name Car", "Remove Button"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumn("Remove Button").setCellRenderer(new ButtonRenderer("remove"));
        buttonEditor = new ButtonEditor(new JCheckBox(), "remove");
        table.getColumn("Remove Button").setCellEditor(buttonEditor);
        add(scrollPane);

        for(Factory.Task task : tasks) {
            Object[] formatLine = {task.getNameCar()};
            model.addRow(formatLine);
        }

        timer = new Timer(100, e -> {
            buttonEditor.setListener(_ ->{
                int row = table.getEditingRow();
                if (row >= 0) {
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }
                    factory.deleteTask(tasks.get(row));
                }
            });
            ArrayList<Factory.Task> newTasks = new ArrayList<>(factory.getTasks());
            ArrayList<Factory.Task> filteredTasks = new ArrayList<>();
            for (Factory.Task task : newTasks) {
                if (task != null) {
                    filteredTasks.add(task);
                }
            }
            if (filteredTasks.isEmpty()) {
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }
                model.setRowCount(0);
                tasks.clear();
                return;
            }
            ArrayList<Integer> rows = new ArrayList<>();
            ArrayList<Factory.Task> copyTasks = new ArrayList<>(tasks);
            for (Factory.Task task : copyTasks.reversed()) {
                if (!filteredTasks.contains(task)) {
                    int index = tasks.indexOf(task);
                    if (index >= 0) {
                        rows.add(index);
                    }
                }
            }
            for(int row : rows) {
                int modelRow = table.convertRowIndexToModel(row);
                model.removeRow(modelRow);
                tasks.remove(row);
            }
            for (Factory.Task task : filteredTasks) {
                if (!tasks.contains(task)) {
                    Object[] formatLine = {task.getNameCar()};
                    model.addRow(formatLine);
                    tasks.add(task);
                }
            }
            revalidate();
        });
        timer.start();
    }

    public void exit(){
        timer.stop();
    }
}
