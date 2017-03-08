package NPC;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Created by Soulscythe on 16/08/2016.
 */
public class NPCGeneratorForm {
	private JButton btnApply;
	private JButton btnCancel;
	private JSlider sldChaosLaw;
	private JSlider sldEvilGood;
	private JSlider sldAgrarian;
	private JSlider sldHuman;
	private JSlider sldHillDwarf;
	private JSlider sldMountainDwarf;
	private JSlider sldHighElf;
	private JSlider sldWoodElf;
	private JSlider sldDrow;
	private JSlider sldForestGnome;
	private JSlider sldRockGnome;
	private JSlider sldHalfElf;
	private JSlider sldHalfOrc;
	private JSlider sldLightfootHalfling;
	private JSlider sldStoutHalfling;
	private JSlider sldDragonborn;
	private JSlider sldTiefling;
	private JSlider sldArtists;
	private JSlider sldCraftsmen;
	private JSlider sldCriminal;
	private JSlider sldGovernment;
	private JSlider sldMercantile;
	private JSlider sldMilitary;
	private JSlider sldNonStandard;
	private JSlider sldSailors;
	private JSlider sldScholars;
	private JSlider sldServices;
	private JLabel lblHuman;
	private JLabel lblHillDwarf;
	private JLabel lblMountainDwarf;
	private JLabel lblHighElf;
	private JLabel lblWoodElf;
	private JLabel lblDrow;
	private JLabel lblForestGnome;
	private JLabel lblRockGnome;
	private JLabel lblHalfElf;
	private JLabel lblHalfOrc;
	private JLabel lblLightfootHalfling;
	private JLabel lblStoutHalfling;
	private JLabel lblDragonborn;
	private JLabel lblTiefling;
	private JSlider sldAge;
	private JLabel lblAgrarian;
	private JLabel lblArtists;
	private JLabel lblCraftsmen;
	private JLabel lblCriminal;
	private JLabel lblGovernment;
	private JLabel lblMercantile;
	private JLabel lblMilitary;
	private JLabel lblNonStandard;
	private JLabel lblSailors;
	private JLabel lblScholars;
	private JLabel lblServices;
	private JPanel panel1;
	private JPanel pnlLower;
	JFrame frame;

	public NPCGenerator gen;

	public NPCGeneratorForm (NPCGenerator generator) {
		/////WindowSetup/////////////////////////////
		frame = new JFrame("NPC Settings");
		frame.setTitle("NPC Settings");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(panel1);

		frame.setSize(800,800);
		frame.setVisible(true);
		frame.pack();
		/////////////////////////////////////////////

		gen = generator;

		setSliders();
		addButtonListeners();
		addChangeListeners();

	}

	public void setSliders() {
		float fge = gen.goodEvil;
		fge*=100;
		int ge = (int)fge;

		float fcl = gen.lawfulChaotic;
		fcl*=100;
		int cl = (int)fcl;

		System.out.println("ge = " + ge);
		System.out.println("cl = " + cl);

		sldEvilGood.setValue(ge);
		sldChaosLaw.setValue(cl);

		sldAge.setValue(gen.averageAge);
		//{100,30,100,10,5,60,75,10,20,15,100};
		//agrarian,art,craftsmen,criminal,government,mercantile,military,nonstandard,sailors,scholarly,services
		sldAgrarian.setValue(gen.occupationTypeFrequencies[0]);
		sldArtists.setValue(gen.occupationTypeFrequencies[1]);
		sldCraftsmen.setValue(gen.occupationTypeFrequencies[2]);
		sldCriminal.setValue(gen.occupationTypeFrequencies[3]);
		sldGovernment.setValue(gen.occupationTypeFrequencies[4]);
		sldMercantile.setValue(gen.occupationTypeFrequencies[5]);
		sldMilitary.setValue(gen.occupationTypeFrequencies[6]);
		sldNonStandard.setValue(gen.occupationTypeFrequencies[7]);
		sldSailors.setValue(gen.occupationTypeFrequencies[8]);
		sldScholars.setValue(gen.occupationTypeFrequencies[9]);
		sldServices.setValue(gen.occupationTypeFrequencies[10]);
		//human,hillDwarf,mountainDwarf,highElf,woodElf,drow,forestGnome,rockGnome,halfElf,halfOrc,lightfootHalfling,stoutHalfling,dragonborn,tiefling
		sldHuman.setValue(gen.raceAmounts[0]);
		sldHillDwarf.setValue(gen.raceAmounts[1]);
		sldMountainDwarf.setValue(gen.raceAmounts[2]);
		sldHighElf.setValue(gen.raceAmounts[3]);
		sldWoodElf.setValue(gen.raceAmounts[4]);
		sldDrow.setValue(gen.raceAmounts[5]);
		sldForestGnome.setValue(gen.raceAmounts[6]);
		sldRockGnome.setValue(gen.raceAmounts[7]);
		sldHalfElf.setValue(gen.raceAmounts[8]);
		sldHalfOrc.setValue(gen.raceAmounts[9]);
		sldLightfootHalfling.setValue(gen.raceAmounts[10]);
		sldStoutHalfling.setValue(gen.raceAmounts[11]);
		sldDragonborn.setValue(gen.raceAmounts[12]);
		sldTiefling.setValue(gen.raceAmounts[13]);

		lblAgrarian.setText(sldAgrarian.getValue()+"");
		lblArtists.setText(sldArtists.getValue()+"");
		lblCraftsmen.setText(sldCraftsmen.getValue()+"");
		lblCriminal.setText(sldCriminal.getValue()+"");
		lblGovernment.setText(sldGovernment.getValue()+"");
		lblMercantile.setText(sldMercantile.getValue()+"");
		lblMilitary.setText(sldMilitary.getValue()+"");
		lblNonStandard.setText(sldNonStandard.getValue()+"");
		lblSailors.setText(sldSailors.getValue()+"");
		lblScholars.setText(sldScholars.getValue()+"");
		lblServices.setText(sldServices.getValue()+"");
		//human,hillDwarf,mountainDwarf,highElf,woodElf,drow,forestGnome,rockGnome,halfElf,halfOrc,lightfootHalfling,stoutHalfling,dragonborn,tiefling
		lblHuman.setText(sldHuman.getValue()+"");
		lblHillDwarf.setText(sldHillDwarf.getValue()+"");
		lblMountainDwarf.setText(sldMountainDwarf.getValue()+"");
		lblHighElf.setText(sldHighElf.getValue()+"");
		lblWoodElf.setText(sldWoodElf.getValue()+"");
		lblDrow.setText(sldDrow.getValue()+"");
		lblForestGnome.setText(sldForestGnome.getValue()+"");
		lblRockGnome.setText(sldRockGnome.getValue()+"");
		lblHalfElf.setText(sldHalfElf.getValue()+"");
		lblHalfOrc.setText(sldHalfOrc.getValue()+"");
		lblLightfootHalfling.setText(sldLightfootHalfling.getValue()+"");
		lblStoutHalfling.setText(sldStoutHalfling.getValue()+"");
		lblDragonborn.setText(sldDragonborn.getValue()+"");
		lblTiefling.setText(sldTiefling.getValue()+"");
	}
	public void addButtonListeners () {
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				float ge = (float)sldEvilGood.getValue();
				ge/=100;
				float cl = (float)sldChaosLaw.getValue();
				cl/=100;

				gen.goodEvil = ge;
				gen.lawfulChaotic = cl;

				gen.averageAge = sldAge.getValue();
				//{100,30,100,10,5,60,75,10,20,15,100};
				//agrarian,art,craftsmen,criminal,government,mercantile,military,nonstandard,sailors,scholarly,services
				gen.occupationTypeFrequencies[0] = sldAgrarian.getValue();
				gen.occupationTypeFrequencies[1] = sldArtists.getValue();
				gen.occupationTypeFrequencies[2] = sldCraftsmen.getValue();
				gen.occupationTypeFrequencies[3] = sldCriminal.getValue();
				gen.occupationTypeFrequencies[4] = sldGovernment.getValue();
				gen.occupationTypeFrequencies[5] = sldMercantile.getValue();
				gen.occupationTypeFrequencies[6] = sldMilitary.getValue();
				gen.occupationTypeFrequencies[7] = sldNonStandard.getValue();
				gen.occupationTypeFrequencies[8] = sldSailors.getValue();
				gen.occupationTypeFrequencies[9] = sldScholars.getValue();
				gen.occupationTypeFrequencies[10] = sldServices.getValue();
				//human,hillDwarf,mountainDwarf,highElf,woodElf,drow,forestGnome,rockGnome,halfElf,halfOrc,lightfootHalfling,stoutHalfling,dragonborn,tiefling
				gen.raceAmounts[0] = sldHuman.getValue();
				gen.raceAmounts[1] = sldHillDwarf.getValue();
				gen.raceAmounts[2] = sldMountainDwarf.getValue();
				gen.raceAmounts[3] = sldHighElf.getValue();
				gen.raceAmounts[4] = sldWoodElf.getValue();
				gen.raceAmounts[5] = sldDrow.getValue();
				gen.raceAmounts[6] = sldForestGnome.getValue();
				gen.raceAmounts[7] = sldRockGnome.getValue();
				gen.raceAmounts[8] = sldHalfElf.getValue();
				gen.raceAmounts[9] = sldHalfOrc.getValue();
				gen.raceAmounts[10] = sldLightfootHalfling.getValue();
				gen.raceAmounts[11] = sldStoutHalfling.getValue();
				gen.raceAmounts[12] = sldDragonborn.getValue();
				gen.raceAmounts[13] = sldTiefling.getValue();
				close();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
	}
	public void addChangeListeners () {
		sldAgrarian.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblAgrarian.setText(sldAgrarian.getValue()+"");
			}
		});
		sldArtists.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblArtists.setText(sldArtists.getValue()+"");
			}
		});
		sldCraftsmen.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblCraftsmen.setText(sldCraftsmen.getValue()+"");
			}
		});
		sldCriminal.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblCriminal.setText(sldCriminal.getValue()+"");
			}
		});
		sldGovernment.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblGovernment.setText(sldGovernment.getValue()+"");
			}
		});
		sldMercantile.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblMercantile.setText(sldMercantile.getValue()+"");
			}
		});
		sldMilitary.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblMilitary.setText(sldMilitary.getValue()+"");
			}
		});
		sldNonStandard.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblNonStandard.setText(sldNonStandard.getValue()+"");
			}
		});
		sldSailors.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblSailors.setText(sldSailors.getValue()+"");
			}
		});
		sldScholars.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblScholars.setText(sldScholars.getValue()+"");
			}
		});
		sldServices.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblServices.setText(sldServices.getValue()+"");
			}
		});
		sldHuman.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblHuman.setText(sldHuman.getValue()+"");
			}
		});
		sldHillDwarf.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblHillDwarf.setText(sldHillDwarf.getValue()+"");
			}
		});
		sldMountainDwarf.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblMountainDwarf.setText(sldMountainDwarf.getValue()+"");
			}
		});
		sldHighElf.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblHighElf.setText(sldHighElf.getValue()+"");
			}
		});
		sldWoodElf.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblWoodElf.setText(sldWoodElf.getValue()+"");
			}
		});
		sldDrow.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblDrow.setText(sldDrow.getValue()+"");
			}
		});
		sldForestGnome.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblForestGnome.setText(sldForestGnome.getValue()+"");
			}
		});
		sldRockGnome.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblRockGnome.setText(sldRockGnome.getValue()+"");
			}
		});
		sldHalfElf.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblHalfElf.setText(sldHalfElf.getValue()+"");
			}
		});
		sldHalfOrc.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblHalfOrc.setText(sldHalfOrc.getValue()+"");
			}
		});
		sldLightfootHalfling.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblLightfootHalfling.setText(sldLightfootHalfling.getValue()+"");
			}
		});
		sldStoutHalfling.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblStoutHalfling.setText(sldStoutHalfling.getValue()+"");
			}
		});
		sldDragonborn.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblDragonborn.setText(sldDragonborn.getValue()+"");
			}
		});
		sldTiefling.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				lblTiefling.setText(sldTiefling.getValue()+"");
			}
		});
	}
	public void close() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
}
