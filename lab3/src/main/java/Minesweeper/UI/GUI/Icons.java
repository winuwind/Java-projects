package Minesweeper.UI.GUI;

import Minesweeper.Field.Cell;
import Minesweeper.Field.StateOpen;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Icons {
    private final ImageIcon[] icons;

    public Icons() {
        icons = new ImageIcon[33];
        icons[0] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/zero.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[1] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/one.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[2] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/two.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[3] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/three.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[4] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/four.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[5] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/five.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[6] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/six.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[7] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/seven.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[8] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/eight.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[9] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/closedCell.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[10] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/notedCell.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[11] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/mineCell.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[12] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/openedMineCell.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[13] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/wrongNotedCell.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[14] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/question.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[15] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/questionPressed.png"))).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        icons[16] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[17] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel-.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[18] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel0.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[19] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel1.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[20] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel2.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[21] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel3.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[22] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel4.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[23] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel5.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[24] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel6.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[25] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel7.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[26] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel8.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[27] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/panel9.png"))).getImage().getScaledInstance(13, 23, Image.SCALE_SMOOTH));
        icons[28] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/mainSmile.png"))).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        icons[29] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/pressedSmile.png"))).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        icons[30] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/unknownSmile.png"))).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        icons[31] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/winSmile.png"))).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
        icons[32] = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/lossSmile.png"))).getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    }

    public ImageIcon getIcon(Cell cell){
        ImageIcon icon;
        if (cell.getStateOpen() == StateOpen.Opened) {
            icon = switch (cell.getStateMine()) {
                case Mine -> icons[11];
                case OpenedMine -> icons[12];
                case WrongNoted -> icons[13];
                case ZeroMineNearby -> icons[0];
                case OneMineNearby -> icons[1];
                case TwoMineNearby -> icons[2];
                case ThreeMineNearby -> icons[3];
                case FourMineNearby -> icons[4];
                case FiveMineNearby -> icons[5];
                case SixMineNearby -> icons[6];
                case SevenMineNearby -> icons[7];
                case EightMineNearby -> icons[8];
            };
        } else if (cell.getStateOpen() == StateOpen.Closed) {
            icon = icons[9];
        } else if (cell.getStateOpen() == StateOpen.Noted) {
            icon = icons[10];
        } else {
            icon = icons[0];
        }
        return icon;
    }

    public ImageIcon getIcon(String panel){
        ImageIcon icon;
        icon = switch (panel){
            case "" -> icons[16];
            case "-" -> icons[17];
            case "0" -> icons[18];
            case "1" -> icons[19];
            case "2" -> icons[20];
            case "3" -> icons[21];
            case "4" -> icons[22];
            case "5" -> icons[23];
            case "6" -> icons[24];
            case "7" -> icons[25];
            case "8" -> icons[26];
            case "9" -> icons[27];
            default -> throw new IllegalStateException("Unexpected value: " + panel);
        };
        return icon;
    }

    public ImageIcon getIcon(StateButton state) {
        ImageIcon icon;
        icon = switch (state){
            case Simple -> icons[28];
            case Pressed -> icons[29];
            case Win -> icons[31];
            case Loss -> icons[32];
            default -> icons[30];
        };
        return icon;
    }
}
