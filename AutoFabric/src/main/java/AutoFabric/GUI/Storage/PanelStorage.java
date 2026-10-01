package AutoFabric.GUI.Storage;

import AutoFabric.Meta.IntClass;
import AutoFabric.Storage.Storage;

import javax.swing.*;
import java.util.HashMap;

public class PanelStorage extends JPanel {
    private JLabel[] labels;
    private final Storage storage;
    private final Timer timer;
    private final int countTypeCar;


    public PanelStorage(Storage storage, int countTypeCar) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.storage = storage;
        this.countTypeCar = countTypeCar;
        initLabels();

        timer = new Timer(100, _ ->{
            HashMap<String, IntClass> info = storage.getInfoAboutStorage().getDictionary();
            int index = 0;
            for (String key : info.keySet()) {
                labels[index].setText(key + ": " + info.get(key).getValue());
                labels[index++].revalidate();
            }
            for(; index < labels.length; index++){
                labels[index].setText("");
                labels[index].revalidate();
            }
            revalidate();
        });
        timer.start();

        for (JLabel label : labels) {
            add(label);
        }
        revalidate();
    }

    private void initLabels(){
        int countLabels = switch (storage.getTypeProduction()){
            case "Engine" -> 3;
            case "Bodies" -> 3;
            case "Accessories" -> 3;
            case "Car" -> countTypeCar;
            default -> 0;
        };
        labels = new JLabel[countLabels];
        for (int i = 0; i < countLabels; i++){
            labels[i] = new JLabel();
        }
    }

    public void exit(){
        timer.stop();
    }
}
