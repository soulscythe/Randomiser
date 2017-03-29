/**
 * Created by Soulscythe on 06/05/2016.
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Looter {

    private JPanel panel1;
    private JTextArea UNASSIGNED_TABLE_NAME;
    private JTextField txtLevel;
    private JTextField txtSize;
    private JButton btnFluff;
    private JComboBox cmboType;
    private JPanel OutputPanel;
    private JPanel MainPanel;
    private JTextArea txtDebug;
    private JTextField txtDamage;
    private JSlider sldAlchem;
    private JSlider sldAmmo;
    private JSlider sldArmour;
    private JSlider sldArt;
    private JSlider sldClothes;
    private JSlider sldGames;
    private JSlider sldJewellery;
    private JSlider sldScrap;
    private JSlider sldShields;
    private JSlider sldTools;
    private JSlider sldWeapons;
    private JSlider sldKitchen;
    private JLabel lblAlchem;
    private JLabel lblAmmo;
    private JLabel lblArmour;
    private JLabel lblArt;
    private JLabel lblClothes;
    private JLabel lblGames;
    private JLabel lblJewellery;
    private JLabel lblScrap;
    private JLabel lblShields;
    private JLabel lblTools;
    private JLabel lblWeapons;
    private JLabel lblKitchen;
    private JSpinner spnLevel;
    private JSpinner spnSize;
    private JSpinner spnDamage;
    private JSpinner spnMinItems;
    private JSpinner spnMaxItems;
    private JLabel spn;
    private JSpinner spnMaterialChance;
    private JSlider sldMaterialChance;
    private JSlider sldDamageChance;
    private JLabel lblMaterialChance;
    private JLabel lblDamageChance;
    private JSlider sldMagicItems;
    private JLabel lblMagicItems;
    private JSlider sldGear;
    private JLabel lblGear;
    private JButton btnEnchant;
    private JSlider sldEnchantment;
    private JLabel lblEnchantment;
    private JTable lootTable;
    private JButton btnAlchemical;
    private JButton btnAmmo;
    private JButton btnArmour;
    private JButton btnArt;
    private JButton btnClothing;
    private JButton btnGames;
    private JButton btnJewellery;
    private JButton btnScrap;
    private JButton btnShields;
    private JButton btnTools;
    private JButton btnWeapons;
    private JButton btnKitchenware;
    private JButton btnMagicItems;
    private JButton btnGear;
    private JCheckBox chkOverwrite;
    private JPanel RarityPanel;
    private JPanel EnchantmentPanel;
    private JButton btnEncounter;
    private JPanel LeftPanels;
    private Randomiser randomiser;
    private JCheckBoxMenuItem[] cbMaterials;

    private String loot = "";

    private String[] tableColumns = {"Item", "Weight", "Source", "Category"};
    private String[][] tableData;
    DefaultTableModel tableModel;

    ArrayList<Item> lootItems;

    public Looter() {
        this.randomiser = new Randomiser(this);
        create();
    }

    public void create()  {
        JFrame frame = new JFrame("Looter");
        frame.setTitle("Looter");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setContentPane(panel1);

        frame.setSize(1200,900);
        frame.setVisible(true);

        for (int i = 0; i < LootType.types.length; i++) {
            cmboType.addItem(LootType.types[i]);
        }

        lootItems = new ArrayList<Item>();

        spnMaxItems.setValue(1);
        spnMinItems.setValue(1);
        spnLevel.setValue(1);

        JMenuBar menuBar;
        JMenu materials;
        JMenuItem menuReloadButton;

        menuBar = new JMenuBar();
        materials = new JMenu("Materials");
        materials.setMnemonic(KeyEvent.VK_M);
        materials.getAccessibleContext().setAccessibleDescription(
                "Tick the reference materials that you want the program to draw items from");
        menuBar.add(materials);

        cbMaterials = new JCheckBoxMenuItem[Randomiser.materials.length];

        for (int i = 0; i < cbMaterials.length; i++) {
            cbMaterials[i] = new JCheckBoxMenuItem(Randomiser.materials[i]);
            cbMaterials[i].setSelected(true);
            cbMaterials[i].setUI(new StayOpenCheckBoxMenuItemUI());
            materials.add(cbMaterials[i]);
        }
        menuReloadButton = new JMenuItem("Reload Materials",
                KeyEvent.VK_R);
        menuReloadButton.getAccessibleContext().setAccessibleDescription(
                "Reload the materials. Ensure you click this if you have changed the materials list");
        menuReloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMaterialsList();
            }
        });
        materials.add(menuReloadButton);

        frame.setJMenuBar(menuBar);

        addButtonListeners();
        addSliderListeners();

        tableModel = new DefaultTableModel(tableColumns,1);
        lootTable.setModel(tableModel);
        lootTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lootTable.setCellSelectionEnabled(true);
    }

    public void updateMaterialsList () {
        for (int i = 0; i < cbMaterials.length; i++){
            Randomiser.materialsChosen[i] = cbMaterials[i].isSelected();
            randomiser.setupMaterials();
        }
    }

    public void print(String in) {
        txtDebug.setText(txtDebug.getText() + "\n" + in);
    }

    public void printErr(String in) {
        JOptionPane.showMessageDialog(null, "ERROR: \n\n" + in);
    }

    public void updateTable () {
        lootItems = Randomiser.cleanItems(lootItems);

        tableModel = new DefaultTableModel(alToDualArray(lootItems),tableColumns);
        lootTable.setModel(tableModel);
    }

    public boolean parseToRandomiser() {
        boolean errFlag = false;
        try {
            Randomiser.level = (Integer)spnLevel.getValue();
        } catch (Exception ex) {
            printErr("Level is non-numeric");
            errFlag = true;
        }
        try {
            Randomiser.minItems = (Integer)spnMinItems.getValue();
        } catch (Exception ex) {
            printErr("Min Items is non-numeric");
            errFlag = true;
        }
        try {
            Randomiser.maxItems = (Integer)spnMaxItems.getValue();
        } catch (Exception ex) {
            printErr("Max Items is non-numeric");
            errFlag = true;
        }
        if (Randomiser.maxItems < Randomiser.minItems) {
            printErr("MaxItems must be an equal or higher value than MinItems");
            errFlag = true;
        }
        if (Randomiser.minItems < 1 || Randomiser.maxItems < 1 || Randomiser.level < 1) {
            printErr("MinItems, MaxItems, and Encounter Level must all be a value of 1 or higher");
        }
        Randomiser.damageChance = sldDamageChance.getValue();
        Randomiser.enchantedChance = sldEnchantment.getValue();
        Randomiser.universalRarityOfUnusualMaterials = sldMaterialChance.getValue();

        if (errFlag) return false;
        Randomiser.lootType = new LootType(sldAlchem.getValue(),
                sldAmmo.getValue(),
                sldArmour.getValue(),
                sldArt.getValue(),
                sldClothes.getValue(),
                sldGames.getValue(),
                sldJewellery.getValue(),
                sldScrap.getValue(),
                sldShields.getValue(),
                sldTools.getValue(),
                sldWeapons.getValue(),
                sldKitchen.getValue(),
                sldMagicItems.getValue(),
                sldGear.getValue());

        return true;
    }

    public void addButtonListeners () {

        btnEnchant.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String magic = randomiser.randomiseEnchantment().toString();
                StringSelection magicText = new StringSelection(magic);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(magicText,null);
                JOptionPane.showMessageDialog(null, "(The following text has been copied to your clipboard. You may paste it somewhere to save it)\n\n" + magic);
            }
        });

        btnFluff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (parseToRandomiser()) { //if parse returns false, it was not successful, so we abort randomisation

                    //loot = randomiser.randomiseCoins(Randomiser.level) + "\n"; //TODO include coin randoms in new table concept somehow

                    if (chkOverwrite.isSelected()) lootItems = new ArrayList<Item>();

                    lootItems.addAll(randomiser.randomiseFluff());

                    updateTable();
                } else return;
            }
        }); //end Randomise Fluff Button

        btnEncounter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (parseToRandomiser()) {

                    if (chkOverwrite.isSelected()) lootItems = new ArrayList<Item>();

                    //TODO: coins
                    lootItems.addAll(randomiser.randomiseGoods());
                    lootItems.addAll(randomiser.randomiseEquipment());

                    updateTable();
                } else return;
            }
        }); //end Randomise Encounter Button

        //Cherry Picker Buttons
        btnAlchemical.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseAlchemical().clean()));
            }
        });
        btnAmmo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseAmmunition().clean()));
            }
        });
        btnArmour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseArmour().clean()));
            }
        });
        btnArt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseArt(Randomiser.level).clean()));
            }
        });
        btnClothing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseClothing().clean()));
            }
        });
        btnGames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseGames().clean()));
            }
        });
        btnJewellery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseJewellery().clean()));
            }
        });
        btnScrap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseScrap().clean()));
            }
        });
        btnShields.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseShield().clean()));
            }
        });
        btnTools.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseTools().clean()));
            }
        });
        btnWeapons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseWeapon().clean()));
            }
        });
        btnKitchenware.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseKitchenware().clean()));
            }
        });
        btnMagicItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseEquip(false,true,false).get(0).clean()));
            }
        });
        btnGear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseToRandomiser();
                tableModel.addRow(itemArrayToTableRow(randomiser.randomiseGear().clean()));
            }
        });
    }

    private String[][] alToDualArray(ArrayList<Item> listIn) {
        String[][] out = new String[listIn.size()][4];
        for (int row = 0; row < listIn.size(); row++) { //rows
            for (int column = 0; column < 4; column++) { //columns
                out[row][column] = listIn.get(row).toStringArray()[column];
            }
        }

        return out;
    }

    private String[] itemArrayToTableRow (Item item) {
        String[] out = new String[4];
        for (int i = 0; i < 4; i ++) out[i] = item.toStringArray()[i];
        return out;
    }

    public void addSliderListeners () {
        sldAlchem.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblAlchem.setText(sldAlchem.getValue() + "");
            }
        });
        sldAmmo.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblAmmo.setText(sldAmmo.getValue() + "");
            }
        });
        sldArmour.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblArmour.setText(sldArmour.getValue() + "");
            }
        });
        sldArt.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblArt.setText(sldArt.getValue() + "");
            }
        });
        sldClothes.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblClothes.setText(sldClothes.getValue() + "");
            }
        });
        sldGames.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblGames.setText(sldGames.getValue() + "");
            }
        });
        sldJewellery.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblJewellery.setText(sldJewellery.getValue() + "");
            }
        });
        sldKitchen.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblKitchen.setText(sldKitchen.getValue() + "");
            }
        });
        sldScrap.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblScrap.setText(sldScrap.getValue() + "");
            }
        });
        sldShields.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblShields.setText(sldShields.getValue() + "");
            }
        });
        sldTools.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblTools.setText(sldTools.getValue() + "");
            }
        });
        sldWeapons.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblWeapons.setText(sldWeapons.getValue() + "");
            }
        });
        sldMagicItems.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblMagicItems.setText(sldMagicItems.getValue() + "");
            }
        });
        sldGear.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblGear.setText(sldGear.getValue() + "");
            }
        });


        sldEnchantment.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblEnchantment.setText(sldEnchantment.getValue() + "%");
            }
        });
        sldMaterialChance.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblMaterialChance.setText(sldMaterialChance.getValue() + "%");
            }
        });
        sldDamageChance.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblDamageChance.setText(sldDamageChance.getValue() + "%");
            }
        });
        cmboType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LootType lt = new LootType(cmboType.getSelectedIndex());
                sldTools.setValue(lt.tools);
                sldWeapons.setValue(lt.weapons);
                sldShields.setValue(lt.shields);
                sldScrap.setValue(lt.scrap);
                sldAlchem.setValue(lt.alchemicals);
                sldAmmo.setValue(lt.ammunition);
                sldArmour.setValue(lt.armour);
                sldArt.setValue(lt.art);
                sldClothes.setValue(lt.clothing);
                sldGames.setValue(lt.games);
                sldJewellery.setValue(lt.jewellery);
                sldKitchen.setValue(lt.kitchenWare);
                sldMagicItems.setValue(lt.magicItems);
                sldGear.setValue(lt.gear);
            }
        });
    }

}
