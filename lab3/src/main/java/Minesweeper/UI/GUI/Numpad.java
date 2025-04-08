package Minesweeper.UI.GUI;

import javax.swing.*;
import java.awt.*;

public class Numpad extends JPanel {
    private final JLabel[] digits;
    private String number;
    private final Icons icons;

    public Numpad(Icons icons) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0 ));
        digits = new JLabel[3];
        digits[0] = new JLabel();
        digits[0].setPreferredSize(new Dimension(13, 23));
        digits[0].setMaximumSize(new Dimension(13, 23));
        digits[1] = new JLabel();
        digits[1].setPreferredSize(new Dimension(13, 23));
        digits[1].setMaximumSize(new Dimension(13, 23));
        digits[2] = new JLabel();
        digits[2].setPreferredSize(new Dimension(13, 23));
        digits[2].setMaximumSize(new Dimension(13, 23));
        add(digits[2]);
        add(digits[1]);
        add(digits[0]);
        this.number = String.valueOf(number);
        this.icons = icons;
        setPreferredSize(new Dimension(39, 23));
        setMaximumSize(new Dimension(39, 23));
    }

    public void update(int number_){
        number = String.valueOf(number_);
        for(int i = 0; i < digits.length; i++){
            ImageIcon icon;
            if(i < number.length()){
                icon = icons.getIcon(String.valueOf(number.charAt(number.length() - 1 - i)));
            }
            else{
                icon = icons.getIcon("");
            }
            digits[i].setIcon(icon);
        }
    }
}
