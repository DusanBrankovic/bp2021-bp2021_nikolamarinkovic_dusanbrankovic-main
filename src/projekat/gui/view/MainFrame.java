package projekat.gui.view;

import lombok.Getter;
import lombok.Setter;
import projekat.AppCore;
import projekat.error.MyError;
import projekat.gui.controller.ActionManager;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public class MainFrame extends JFrame {

    private static MainFrame instance;

    private AppCore appCore;
    private JTable jTable;
    private JTextArea jTextArea;
    private JTextArea errorArea;
    private ActionManager actionManager;

    public static MainFrame getInstance() {

        if (instance == null) {
            instance = new MainFrame();
        }

        return instance;
    }

    public MainFrame(){
        actionManager = new ActionManager();
    }

    public void initialiseGUI(){

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(new Dimension(2*screenWidth/3, 2*screenHeight/3));
        setTitle("Projekat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Glavni panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(2*screenWidth/3, 2*screenHeight/3));

        //Panel sa JTextArea za input i errorArea
        JPanel panelGore = new JPanel();
        GridLayout gridLayout = new GridLayout();
        gridLayout.setHgap(10);
        panelGore.setLayout(gridLayout);

        panelGore.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        jTextArea = new JTextArea(10,10);
        jTextArea.setWrapStyleWord(true);
        panelGore.add(new JScrollPane(jTextArea));

        errorArea = new JTextArea(10, 10);
        errorArea.setForeground(Color.red);
        errorArea.setEditable(false);
        panelGore.add(new JScrollPane(errorArea));

        //Panel sa dugmetom
        JPanel panelSredina = new JPanel();
        panelSredina.setLayout(new BoxLayout(panelSredina, BoxLayout.X_AXIS));

        JPanel panelBtn = new JPanel();
        JButton btnSend = new JButton(actionManager.getSendQueryAction());
        btnSend.setPreferredSize(new Dimension(150,25));
        btnSend.setText("Send Query");

        panelBtn.add(btnSend);
        panelSredina.add(panelBtn);
        panelSredina.add(Box.createHorizontalGlue());

        //Panel sa JTable za output
        JPanel panelDole = new JPanel();
        panelDole.setLayout(new BorderLayout());
        panelDole.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        jTable = new JTable();
        jTable.setFillsViewportHeight(true);
        panelDole.add(new JScrollPane(jTable));

        panel.add(panelGore);
        panel.add(panelSredina);
        panel.add(panelDole);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    public void showError(MyError e){
        JOptionPane.showMessageDialog(null, e.getMessage(), e.getTitle(), JOptionPane.ERROR_MESSAGE);
    }



}
