package Minesweeper.UI.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class About {
    String infoTextEnglish;
    String infoTextRussian;
    JLabel infoLabel;
    boolean flag;

    public About(){
        infoTextEnglish = "<html><h2>Minesweeper Game - Help</h2>" +
                "<h3>Objective:</h3>" +
                "<p>The goal of Minesweeper is to uncover all the empty spaces on the grid without triggering any mines.</p>" +
                "<p>If you reveal a cell that contains a mine, the game is over.</p>" +
                "<h3>How to Play:</h3>" +
                "<ul>" +
                "<li><b>Click on a cell:</b><ul><li>Left-click to open a cell.</li><li>If the cell is empty, it will reveal the number of neighboring mines.</li>" +
                "<li>If number of neighboring mines equals zero, cell won't reveal zero.</li>" +
                "<li>If the cell contains a mine, the game ends.</li></ul></li>" +
                "<li><b>Click on an opened cell:</b><ul><li>Left-click to open an adjacent cells which aren't noted with flag.</li><li>If the number of neighboring noted cells equals with the number of neighboring mines, all cells without flag will be opened.</li></ul></li>" +
                "<li><b>Numbers:</b> Each revealed cell may display a number. This number indicates how many mines are in the eight adjacent cells around that particular cell.</li>" +
                "<li><b>Flagging Mines:</b> If you think a cell contains a mine, right-click to place a flag on it.</li>" +
                "<li><b>Uncovering Cells:</b> If you uncover a cell without the number, all adjacent cells will automatically be uncovered.</li>" +
                "</ul>" +
                "<h3>Winning the Game:</h3>" +
                "<p>You win when all safe cells are uncovered.</p>" +
                "<h3>Game Features:</h3>" +
                "<ul>" +
                "<li><b>Timer:</b> The game keeps track of how much time you take to solve the puzzle.</li>" +
                "<li><b>Difficulty Levels:</b> Choose from different difficulty levels - Beginner, Intermediate, Expert - or your own level.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>Shortcuts:</b>" +
                "<ul>" +
                "<li><b>ALT+F4</b> - Close focused window.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+R</b> - Start new game with same settings.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+F</b> - Finish this game.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+S</b> - Save the score of the current game.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+H</b> - Display a record table.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+SPACE</b> - Pause game.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>SHIFT+SPACE</b> - Resume game.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>ALT+F7</b> - Display information about the game.</li>" +
                "</ul>" +
                "</ul>" +
                "<h3>Tips:</h3>" +
                "<ul>" +
                "<li>Start by clicking a cell in the center of the grid.</li>" +
                "<li>Always flag the suspected mines as you progress.</li>" +
                "<li>If you are stuck, consider using the numbers to logically deduce which cells are safe to click.</li>" +
                "</ul>" +
                "<h3>Troubleshooting:</h3>" +
                "<ul>" +
                "<li><b>Game doesn’t start:</b> Make sure your computer meets the game’s system requirements.</li>" +
                "<li><b>Game freezes:</b> Try restarting the game.</li>" +
                "</ul>" +
                "</html>";
        infoTextRussian = "<html><h2>Игра Сапёр - Помощь</h2>" +
                "<h3>Цель:</h3>" +
                "<p>Цель игры в Сапёр - открыть все пустые клетки на поле, не подорвав при этом ни одной мины.</p>" +
                "<p>Если вы откроете клетку, содержащую мину, игра закончится.</p>" +
                "<h3>Как играть:</h3>" +
                "<ul>" +
                "<li><b>Щелчок по клетке:</b><ul><li>Левый клик — откройте клетку.</li><li>Если клетка пустая, она покажет количество мин в соседних клетках.</li>" +
                "<li>Если количество соседних мин равно нулю, клетка не покажет ноль.</li>" +
                "<li>Если клетка содержит мину, игра закончится.</li></ul></li>" +
                "<li><b>Щелчок по открытой клетке:</b><ul><li>Левый клик — откроет соседние клетки, которые не помечены флагом.</li><li>Если количество соседних помеченных клеток совпадает с количеством соседних мин, все клетки без флага будут открыты.</li></ul></li>" +
                "<li><b>Числа:</b> Каждая открытая клетка может показать число. Это число указывает, сколько мин в восьми соседних клетках вокруг этой клетки.</li>" +
                "<li><b>Пометка мин:</b> Если вы думаете, что клетка содержит мину, щелкните правой кнопкой мыши, чтобы поставить на неё флаг.</li>" +
                "<li><b>Открытие клеток:</b> Если вы открываете клетку без числа, все соседние клетки будут автоматически открыты.</li>" +
                "</ul>" +
                "<h3>Победа в игре:</h3>" +
                "<p>Вы выигрываете, когда все безопасные клетки будут открыты.</p>" +
                "<h3>Особенности игры:</h3>" +
                "<ul>" +
                "<li><b>Таймер:</b> Игра отслеживает время, которое вы тратите на решение головоломки.</li>" +
                "<li><b>Уровни сложности:</b> Выберите один из уровней сложности - Начинающий, Средний, Эксперт - или ваш собственный уровень.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>Горячие клавиши:</b>" +
                "<ul>" +
                "<li><b>ALT+F4</b> - Закрыть фокусированное окно.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+R</b> - Начать новую игру с теми же настройками.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+F</b> - Завершить эту игру.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+S</b> - Сохранить результат текущей игры.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+H</b> - Показать таблицу рекордов.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>CTRL+SPACE</b> - Пауза в игре.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>SHIFT+SPACE</b> - Возобновить игру.</li>" +
                "</ul>" +
                "<ul>" +
                "<li><b>ALT+F7</b> - Показать информацию об игре.</li>" +
                "</ul>" +
                "</ul>" +
                "<h3>Советы:</h3>" +
                "<ul>" +
                "<li>Начните с клика по клетке в центре поля.</li>" +
                "<li>Всегда помечайте подозрительные мины по мере продвижения.</li>" +
                "<li>Если застряли, попробуйте использовать числа, чтобы логически понять, какие клетки безопасны для клика.</li>" +
                "</ul>" +
                "<h3>Устранение неисправностей:</h3>" +
                "<ul>" +
                "<li><b>Игра не запускается:</b> Убедитесь, что ваш компьютер соответствует системным требованиям игры.</li>" +
                "<li><b>Игра зависает:</b> Попробуйте перезапустить игру.</li>" +
                "</ul>" +
                "</html>";
        flag = false;
        infoLabel = new JLabel(infoTextEnglish);
    }

    public void printAbout(JFrame frame) {
        JDialog dialog = new JDialog(frame, "About Minesweeper", true);
        dialog.setSize(600, 600);
        dialog.setLayout(new BorderLayout());

        JButton buttonEn = new JButton("English");
        buttonEn.setBackground(Color.LIGHT_GRAY);
        buttonEn.addActionListener(_ -> infoLabel.setText(infoTextEnglish));

        JButton buttonRu = new JButton("Russian");
        buttonRu.setBackground(Color.LIGHT_GRAY);
        buttonRu.addActionListener(_ -> infoLabel.setText(infoTextRussian));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.add(buttonEn);
        panel.add(buttonRu);
        dialog.add(panel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(infoLabel);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_DOWN_MASK), "Exit");
        dialog.getRootPane().getActionMap().put("Exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                dialog.dispose();
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
