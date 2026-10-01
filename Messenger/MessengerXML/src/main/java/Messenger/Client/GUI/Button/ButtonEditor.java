package Messenger.Client.GUI.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private final String name;

    public ButtonEditor(JCheckBox checkBox, String name) {
        super(checkBox);
        this.name = name;
        button = new JButton();
        button.setOpaque(true);
    }

    public void setListener(ActionListener l) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
        button.addActionListener(l);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = (value == null) ? name : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}