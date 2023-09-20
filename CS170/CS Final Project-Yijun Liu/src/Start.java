import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Start extends JFrame {
    private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

    private JFrame frame = new JFrame();
    private int frameWidth = 980;
    private int frameHeight = 820;

    private Gameboard game = new Gameboard();

    //Components in the frame
    private String[] listBoard = new String[]{"3 X 3", "4 X 4", "5 X 5", "6 X 6", "7 X 7", "8 X 8", "9 X 9", "10 X 10", "11 X 11", "12 X 12", "13 X 13", "14 X 14", "15 X 15", "16 X 16"};
    private JComboBox<String> comboBoard = new JComboBox<String>(listBoard);
    private JLabel labelBoard = new JLabel("BOARD");
    private JButton exist = new JButton(" EXIT ");
    private JButton restart = new JButton("RESTART");
    private JLabel design = new JLabel("CS 170_OX Final Project");
    private JLabel design2 = new JLabel("Yijun Liu");

    private JPanel panel = new JPanel();

    private int row = 3;


    public Start(){

        //Frame
        setTitle("Tic Tac Toe");
        setBounds((d.width - frameWidth) / 2, (d.height - frameHeight) / 2, frameWidth, frameHeight);
        setSize(980,760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //Label
        labelBoard.setFont(new Font(null,Font.PLAIN, 20));
        labelBoard.setForeground(new Color(1,0,56));

        //Start button
        restart.setFont(new Font(null,Font.BOLD, 20));
        restart.setForeground(new Color(1,0,56));
        restart.setBorderPainted(false);
        restart.setContentAreaFilled(false);
        restart.setFocusPainted(false);

        //Exist button
        exist.setFont(new Font(null,Font.BOLD, 20));
        exist.setForeground(new Color(1,0,56));
        exist.setBorderPainted(false);
        exist.setContentAreaFilled(false);
        exist.setFocusPainted(false);

        //Empty panels for formatting
        JPanel empty = new JPanel();
        empty.setPreferredSize(new Dimension(frameWidth, 300));
        JPanel empty2 = new JPanel();
        empty2.setPreferredSize(new Dimension(frameWidth, 270));
        empty.setOpaque(false);

        //Panel
        panel.setPreferredSize(new Dimension(240, frameHeight));
        panel.add(empty);
        panel.add(labelBoard);
        panel.add(comboBoard);
        panel.add(exist);
        panel.add(restart);
        panel.add(empty2);
        panel.add(design);
        panel.add(design2);

        add(panel,BorderLayout.EAST);

        add(game);

        comboBoard.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                row = comboBoard.getSelectedIndex() + 3;
            }
        });

        //Add actLister
        actLister lister = new actLister();
        restart.addActionListener(lister);
        exist.addActionListener(lister);

        String msg = String.format("When you change the board size, please click restart to refresh");
        JOptionPane.showMessageDialog(this, msg);

        setVisible(true);

    }

    private class actLister implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Object a = e.getSource();
            if (a == restart){
                game.restart(row);
            }
            if (a == exist){
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        new Start();
    }
}
