package AutoFabric.GUI.Task;

import AutoFabric.Factory.Factory;
import AutoFabric.GUI.Graphics;
import AutoFabric.GUI.Window;

import javax.swing.*;

public class WindowTasks extends JDialog implements Window {
    private final PanelTasks panel;
    private final Timer timer;

    WindowTasks(Graphics graphics, Factory factory) {
        super(graphics, false);
        graphics.add((AutoFabric.GUI.Window) this);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Tasks: " + factory.getCountTasks());
        panel = new PanelTasks(factory);
        add(label);
        add(panel);
        timer = new Timer(100, _ ->{
            label.setText("Tasks: " + factory.getCountTasks());
        });
        timer.start();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
                graphics.remove((Window) WindowTasks.this);
            }
        });
        revalidate();
        pack();
        setVisible(true);
    }

    @Override
    public void exit() {
        panel.exit();
        timer.stop();
        dispose();
    }
}
