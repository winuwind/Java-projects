package AutoFabric.GUI.Dealer;

import AutoFabric.Dealers.Dealer;

import javax.swing.*;

public class PanelDealer extends JPanel {
    JSlider slider;
    JLabel label;
    Dealer dealer;

    public PanelDealer(Dealer dealer) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.dealer = dealer;
        slider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, dealer.getDelay());
        label = new JLabel();
        label.setText("Product: " + dealer.getName() + "; Delay: " + dealer.getDelay() + " seconds");
        label.revalidate();
        slider.addChangeListener(_ ->{
            String text = "Product: " + dealer.getName() + "; Delay: " + dealer.getDelay() + " seconds";
            label.setText(text);
            label.revalidate();
            revalidate();
            if(dealer.getDelay() != slider.getValue()) {
                dealer.changeDelay(slider.getValue());
            }
        });
        slider.revalidate();
        add(label);
        add(slider);
        revalidate();
    }
}
