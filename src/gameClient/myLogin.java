package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

/**
 * This class represents a very simple GUI class to present a
 * login display on a graph.
 * We used a specific display following this site:
 * https://www.tutorialsfield.com/login-form-in-java-swing-with-source-code/
 */
public class myLogin extends JFrame {

    Container container = getContentPane();

    private JTextField id;
    private JLabel id_label;
    private JTextField level;
    private JLabel level_label;
    private int levelPicked;
    private JButton play;
    private boolean statusLogin;

    /**
     * this method runs the Login Display based on given information from the user,
     * such as: ID, Level num between 0-23.
     * @param a - String
     */
    public myLogin(String a) {
        super(a);

        id = new JTextField();
        level = new JTextField();
        id_label = new JLabel();
        id_label.setText("ID number: ");
        level_label = new JLabel();
        level_label.setText("Pick level:  ");
        play = new JButton("GO!");


        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();

            play.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String givenID = id.getText();
                    String givenLevel = level.getText();

                    if (givenID.length() != 9 || !givenID.matches("[0-9]+")) {
                        JOptionPane.showMessageDialog(null, "Write down 9 digits (numbers only)");
                    } else {
                        setLevel(Integer.parseInt(givenLevel));
                        setID(id);
                        setStatus(true);
                        setVisible(false);
                    }
                }
            });

            setSize(300, 400);
            setVisible(true);

//        show();

    }
    /**
     * this method set the ID of the user.
     * @param newID - JTextField
     * @return
     */
    private void setID(JTextField newID) {
       id=newID;
    }
    /**
     * this method set the Login status.
     * @param b - boolean
     * @return
     */
    private void setStatus(boolean b) {
        statusLogin=b;
    }
    /**
     * this method set the Level.
     * @param i - int
     * @return
     */
    private void setLevel(int i) {
        levelPicked=i;
    }
    /**
     * this method set the Layout Manager.
     */
    public void setLayoutManager() {
        container.setLayout(null);
    }
    /**
     * this method set the Location And Size.
     */
    public void setLocationAndSize() {
        id_label.setBounds(50, 150, 100, 30);
        level_label.setBounds(50, 220, 100, 30);
        id.setBounds(150, 150, 100, 30);
        level.setBounds(150, 220, 100, 30);
        play.setBounds(75, 300, 100, 30);
        container.setBackground(Color.PINK);
        play.setBackground(Color.GRAY);
    }
    /**
     * this method add Components To Container.
     */
    public void addComponentsToContainer() {
        container.add(id_label);
        container.add(level_label);
        container.add(id);
        container.add(level);
        container.add(play);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    /**
     * this method get the Login Status.
     * @return statusLogin - boolean
     */
    public boolean getStatus() {
        return statusLogin;
    }
    /**
     * this method get the ID.
     * @return id number  - long
     */
    public long getID() {
        String ID = id.getText();
        System.out.println("User ID: " + ID);
        System.out.println("Level Number: " +level.getText());
        return Long.parseLong(ID);
    }
    /**
     * this method get the level number.
     * @return level number - int
     */
    public int getLEVEL() {
        String LEVEL = level.getText();
        return Integer.parseInt(LEVEL);
    }
}