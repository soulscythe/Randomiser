package NPC;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 * Created by Soulscythe on 16/01/2017.
 */
public class NPCDetails {
	private JTable tblNPC;
	private JPanel panel1;

	DefaultTableModel tableModel;
	private String[] tableColumns = {"", ""};
	private NPC npc;
	private int tableForm;
	private String[] tableRowHeaders;

	public NPCDetails (NPC npc, int tf) {
		this.npc = npc;
		this.tableForm = tf;

		if (tableForm == 0) tableRowHeaders = NPC.StringArrayFormat_Social();
		else if (tableForm == 1) tableRowHeaders = NPC.StringArrayFormat_Combat();
		else if (tableForm == 2) tableRowHeaders = NPC.stringArrayFormat_Full();

		create();
	}

	public void create()  {
		JFrame frame = new JFrame(npc.name);
		frame.setTitle(npc.name);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setContentPane(panel1);

		frame.setSize(800,(16*tableRowHeaders.length) + 50);
		frame.setVisible(true);

		tableModel = new DefaultTableModel(tableColumns,1);
		tblNPC.setModel(tableModel);
		tblNPC.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tblNPC.setCellSelectionEnabled(true);

		updateTable();
	}

	public void updateTable () {

		tableModel = new DefaultTableModel(formatNPCforTable(npc),tableColumns);
		tblNPC.setModel(tableModel);

		DefaultTableCellRenderer rightRendererBold = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table,
														   Object value, boolean isSelected, boolean hasFocus,
														   int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
						row, column);
				setFont(getFont().deriveFont(Font.BOLD));
				return this;
			}

		};
		rightRendererBold.setHorizontalAlignment(JLabel.RIGHT);
		tblNPC.getColumnModel().getColumn(0).setCellRenderer(rightRendererBold);
		tblNPC.getColumnModel().getColumn(0).setMaxWidth(150);
		tblNPC.getColumnModel().getColumn(0).setMinWidth(150);
	}

	private String[][] formatNPCforTable(NPC n) {
		String[] npcAsArray = n.toStringArray(tableForm);

		String[][] out = new String[tableRowHeaders.length][2];
		for (int row = 0; row < tableRowHeaders.length; row++) { //rows
			out[row][0] = tableRowHeaders[row];
			out[row][1] = npcAsArray[row];
		}

		return out;
	}
}
