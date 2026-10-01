package AutoFabric.GUI.Task;

import AutoFabric.Factory.Factory;
import AutoFabric.GUI.Graphics;

import javax.swing.*;
import java.util.ArrayList;

public class PanelTask extends JPanel {
    Timer timer;

    public PanelTask(Graphics graphics, Factory factory) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Tasks: " + factory.getTasks().size());
        JButton buttonView = new JButton("View Tasks");
        buttonView.addActionListener(_ ->{
            WindowTasks window = new WindowTasks(graphics, factory);
        });
        JButton buttonClear = new JButton("Clear All");
        buttonClear.addActionListener(_ ->{
            factory.deleteAllTasks();
        });
        add(label);
        add(buttonView);
        add(buttonClear);
        timer = new Timer(100, _ -> {
            label.setText("Tasks: " + factory.getTasks().size());
        });
        timer.start();
        revalidate();
    }

    public void exit(){
        timer.stop();
    }
}
