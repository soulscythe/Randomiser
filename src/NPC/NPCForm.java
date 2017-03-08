package NPC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Soulscythe on 16/08/2016.
 */
public class NPCForm {
	private JPanel panel1;
	private JButton btnLoad;
	private JButton btnNew;
	private JSpinner spnNPCCount;
	private JTable tblNPC;
	private JButton btnSettings;
	private JCheckBox chkOverwrite;

	DefaultTableModel tableModel;
	private String[] tableColumns = {"Name", "Occupation", "Gender", "Race", "Alignment"};
	ArrayList<NPC> NPCs;
	private NPCGenerator npcGen;
	private int rowClick;

	public NPCForm () {
		create();
	}

	public void create()  {
		JFrame frame = new JFrame("NPCGen");
		frame.setTitle("NPCGen");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(panel1);

		frame.setSize(1200,900);
		frame.setVisible(true);

		npcGen = new NPCGenerator();
		NPCs = new ArrayList<NPC>();
		addButtonListeners();

		tableModel = new DefaultTableModel(tableColumns,1);
		tblNPC.setModel(tableModel);
		tblNPC.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tblNPC.setCellSelectionEnabled(true);

		tblNPC.addMouseListener(new PopClickListener());
		JTableHeader tblHead = tblNPC.getTableHeader();

		tblHead.addMouseListener(new HeaderListener(tblHead));

		updateTable();
	}

	public void updateTable () {
		tableModel = new DefaultTableModel(alToDualArray(NPCs),tableColumns);
		tblNPC.setModel(tableModel);
	}

	private String[][] alToDualArray(ArrayList<NPC> listIn) {
		String[][] out = new String[listIn.size()][5];
		for (int row = 0; row < listIn.size(); row++) { //rows
			for (int column = 0; column < 5; column++) { //columns
				out[row][column] = listIn.get(row).toStringArray()[column];
			}
		}

		return out;
	}

	private void addButtonListeners() {

		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chkOverwrite.isSelected()) NPCs = new ArrayList<NPC>();
				for (int i = 0; i < (Integer)spnNPCCount.getValue(); i++) NPCs.add(NPC.newRandom(npcGen));
				updateTable();
			}
		});

		btnSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NPCGeneratorForm npcGeneratorForm = new NPCGeneratorForm(npcGen);
			}
		});

		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable();
			}
		});
	}
	class NPCContextMenu extends JPopupMenu {
		JMenuItem socialView;
		JMenuItem combatView;
		JMenuItem fullView;
		public NPCContextMenu(){
			socialView = new JMenuItem("Social view");
			socialView.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new NPCDetails(NPCs.get(rowClick),0);
				}
			});
			combatView = new JMenuItem("Combat view");
			combatView.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new NPCDetails(NPCs.get(rowClick),1);
				}
			});
			fullView = new JMenuItem("Full view");
			fullView.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new NPCDetails(NPCs.get(rowClick),2);
				}
			});
			add(socialView);
			add(combatView);
			add(fullView);
		}
	}
	class PopClickListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {

		}
		public void mousePressed(MouseEvent e){
			rowClick = tblNPC.rowAtPoint(e.getPoint());
			if (e.isPopupTrigger())
				doPop(e);
		}

		public void mouseReleased(MouseEvent e){
			rowClick = tblNPC.rowAtPoint(e.getPoint());
			if (e.isPopupTrigger())
				doPop(e);
		}

		private void doPop(MouseEvent e){
			NPCContextMenu menu = new NPCContextMenu();
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	class HeaderListener extends MouseAdapter {
		JTableHeader header;

		HeaderListener(JTableHeader header) {
			this.header = header;
		}

		public void mousePressed(MouseEvent e) {
			final int col = header.columnAtPoint(e.getPoint());
			final String sortMode = tableColumns[col];
			Collections.sort(NPCs, new Comparator<NPC>() {
				@Override
				public int compare(NPC n1, NPC n2) {
					if (sortMode.equals("Name")) return n1.name.compareToIgnoreCase(n2.name);
					else if (sortMode.equals("Occupation")) return n1.occupation.compareToIgnoreCase(n2.occupation);
					else if (sortMode.equals("Gender")) return n1.gender.compareToIgnoreCase(n2.gender);
					else if (sortMode.equals("Race")) return n1.race.compareToIgnoreCase(n2.race);
					else if (sortMode.equals("Alignment")) return n1.alignment.compareToIgnoreCase(n2.alignment);
					else return n1.name.compareToIgnoreCase(n2.name);
				}
			});
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000); //Ritualistic and unexplained. However seems to work, whereas yield does not. If this isn't present, an ioob exception occurs when repainting the jtable.
					} catch (Exception e){}
					updateTable();
				}
			}).run();
		}

	}
}
