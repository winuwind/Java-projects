package Minesweeper.UI.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private JTable table;
    private DefaultTableModel model;

    public ButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel model) {
        super(checkBox);
        this.table = table;
        this.model = model;
        button = new JButton();
        button.setOpaque(true);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getEditingRow();
                if (row >= 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    model.removeRow(modelRow);
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}