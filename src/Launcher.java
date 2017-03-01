import NPC.NPCForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Soulscythe on 26/05/2016.
 */
public class Launcher {
    private JButton btnLoot;
    private JPanel panel1;
    private JButton btnNPC;
    private JButton btnLanguage;

    public Looter looter;
    public Translator translator;
    public NPCForm npcForm;

    public Launcher () {
        create();
    }

    private void create() {
        JFrame frame = new JFrame("Randomiser");
        frame.setTitle("Randomiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel1);

        frame.setSize(300,150);
        frame.setVisible(true);

        addButtonListeners();
    }
    private void addButtonListeners() {
        ToolTipManager.sharedInstance().setDismissDelay(60000);

        btnLoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                looter = new Looter();
            }
        });
        btnNPC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                npcForm = new NPCForm();
            }
        });
        btnLanguage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translator = new Translator();
            }
        });
    }
}
