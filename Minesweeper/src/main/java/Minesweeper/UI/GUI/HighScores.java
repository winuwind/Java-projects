package Minesweeper.UI.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Comparator;

public class HighScores {
    private final JTable table;
    private final DefaultTableModel model;
    private final JScrollPane scrollPane;
    private final TableRowSorter<DefaultTableModel> sorter;

    public HighScores() {
        String[] columnNames = {"Score", "Time", "Start Time", "Mines", "Width", "Height", "Game state", "Game result", "Interface", "Delete button"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        sorter = new TableRowSorter<>(model);
        setSorter();
        table.setRowSorter(sorter);
        table.getTableHeader().setBackground(Color.CYAN);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        setRenderer();
        table.getColumn("Delete button").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete button").setCellEditor(new ButtonEditor(new JCheckBox(), table, model));
    }

    private void setSorter(){
        sorter.setComparator(0, (Comparator<Integer>) Integer::compareTo);
        sorter.setComparator(1, (Comparator<Integer>) Integer::compareTo);
        sorter.setComparator(3, (Comparator<Integer>) Integer::compareTo);
        sorter.setComparator(4, (Comparator<Integer>) Integer::compareTo);
        sorter.setComparator(5, (Comparator<Integer>) Integer::compareTo);
        sorter.setSortKeys(java.util.Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
        sorter.sort();
    }

    private void setRenderer(){
        table.setDefaultRenderer(Object.class, (table1, value, _, _, row, column) -> {
            if(column != 9) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                if ((Integer) table1.getValueAt(row, 4) <= 13 && (Integer) table1.getValueAt(row, 5) <= 13) {
                    if (column < 7) {
                        label.setBackground(Color.GREEN);
                    }
                } else if ((Integer) table1.getValueAt(row, 4) >= 26 && (Integer) table1.getValueAt(row, 5) >= 26) {
                    if (column < 7) {
                        label.setBackground(Color.RED);
                    }
                } else {
                    if (column < 7) {
                        label.setBackground(Color.YELLOW);
                    }
                }
                if (column == 7) {
                    label.setFont(new Font("Arial", Font.BOLD, 16));
                    if (table1.getValueAt(row, column).equals("Win")) {
                        label.setForeground(Color.GREEN);
                    } else if (table1.getValueAt(row, column).equals("Loss")) {
                        label.setForeground(Color.RED);
                    } else {
                        label.setForeground(Color.BLACK);
                    }
                }
                if(column == 8) {
                    label.setFont(new Font("Arial", Font.BOLD, 16));
                    if (table1.getValueAt(row, column).equals("GUI")) {
                        label.setBackground(Color.MAGENTA);
                    }
                    else if (table1.getValueAt(row, column).equals("TUI")) {
                        label.setBackground(Color.PINK);
                    }
                }
                label.setText(value != null ? value.toString() : "");
                return label;
            }
            return null;
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public void printHighScores(JFrame frame, File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(" ");
            Object[] formatLine = {Integer.valueOf(words[0]), Integer.valueOf(words[1]), words[2] + " " + words[3], Integer.valueOf(words[4]), Integer.valueOf(words[5]), Integer.valueOf(words[6]), words[7], words[8], words[9], "Remove"};
            model.addRow(formatLine);
        }
        reader.close();
        resizeColumnWidth(table);

        JDialog dialog = new JDialog(frame, "Records", true);
        dialog.setSize(1000, Math.min(700, scrollPane.getPreferredSize().height));
        dialog.add(scrollPane);

        dialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_DOWN_MASK), "Exit");
        dialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for(int i = 0; i < model.getRowCount(); i++) {
            for(int j = 0; j < model.getColumnCount() - 1; j++) {
                writer.write(model.getValueAt(i, j).toString() + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }

    private void resizeColumnWidth(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, table.getColumnName(column), false, false, -1, column);
            int maxWidth = headerComponent.getPreferredSize().width;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                maxWidth = Math.max(maxWidth, c.getPreferredSize().width);
            }
            columnModel.getColumn(column).setPreferredWidth(maxWidth + 10);
        }
    }
}
