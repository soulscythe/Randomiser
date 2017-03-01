import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Soulscythe on 26/05/2016.
 */
public class Translator {
    private JPanel panel1;
    private JTextArea txtInput;
    private JTextArea txtOutput;
    private JButton btnCreate;
    private JSpinner spnSizeMax;
    private JSpinner spnSizeMin;
    private JPanel FormatDetails;
    private JSlider sldHardSoft;
    private JSlider sldSibilance;
    private JSlider sldLilt;
    private JSlider sldExoticness;

    private LanguageMaker maker;

    public Translator() {
        create();
    }

    private void create() {
        JFrame frame = new JFrame("Randomiser");
        frame.setTitle("Randomiser");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setContentPane(panel1);

        frame.setSize(600,450);
        frame.setVisible(true);

        maker = new LanguageMaker(this);

        addButtonListeners();


    }

    private void addButtonListeners () {
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //maker.writeLanguage("English",maker.wordList);
                txtOutput.append(maker.makeWord(6,2) + "\n");

            }
        });
    }

    private void loadComponents () {

    }

}
