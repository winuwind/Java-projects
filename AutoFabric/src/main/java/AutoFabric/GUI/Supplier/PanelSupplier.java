package AutoFabric.GUI.Supplier;

import AutoFabric.Suppliers.AbstractSupplier;

import javax.swing.*;

public class PanelSupplier extends JPanel {
    JSlider slider;
    JLabel label;
    AbstractSupplier supplier;

    public PanelSupplier(AbstractSupplier supplier) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.supplier = supplier;
        slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, supplier.getDelay());
        label = new JLabel();
        label.setText("Product: " + supplier.getName() + "; Delay: " + supplier.getDelay() + " seconds");
        label.revalidate();
        slider.addChangeListener(_ -> {
            String text = "Product: " + supplier.getName() + "; Delay: " + supplier.getDelay() + " seconds";
            label.setText(text);
            label.revalidate();
            revalidate();
            if(supplier.getDelay() != slider.getValue()){
                supplier.changeDelay(slider.getValue());
            }
        });
        slider.revalidate();
        add(label);
        add(slider);
        revalidate();
    }
}
