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
    private JButton btnTranslate;
    private JPanel FormatDetails;
    private JSlider sldHardSoft;
    private JSlider sldSibilance;
    private JSlider sldLilt;
    private JSlider sldExoticness;
    private JButton btnNew;
    private JButton btnSample;

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

        maker = new LanguageMaker(this,100,100,100,100);

        addButtonListeners();


    }

    private void addButtonListeners () {
        btnTranslate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] windowOptions = new Object[] {"OK","Cancel"};
                if (JOptionPane.showOptionDialog(null,"This will create a new language and may take several minutes. \n Are you sure?",
                        "New Language",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        windowOptions,  //the titles of buttons
                        windowOptions[0]) == JOptionPane.YES_OPTION) {
                    String s = (String)JOptionPane.showInputDialog(
                            null,
                            "Language Name:\n",
                            "Customized Dialog",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "");
                    maker.writeLanguage(s);
                }
            }
        });
        btnSample.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maker.updateOptions(sldHardSoft.getValue(),sldSibilance.getValue(),sldLilt.getValue(),sldExoticness.getValue()); //int sh, int sib, int lilt, int ex
                String s = maker.makeWord(6,2);
                txtOutput.append(s + maker.estimateSyllables(s) + "\n");

            }
        });
    }

    private void loadComponents () {

    }

}
