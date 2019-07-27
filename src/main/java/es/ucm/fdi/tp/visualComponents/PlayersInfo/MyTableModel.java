package es.ucm.fdi.tp.visualComponents.PlayersInfo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

// Esta clase representa la tabla de informacion que le es visible al usuario.

class MyTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 725564528822048909L;
	
	private String[] colNames;
	private List<String> players;

	MyTableModel() {
		this.colNames = new String[] { "#Player", "Color" };
		players = new ArrayList<>();
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return players != null ? players.size() : 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return rowIndex;
		} else {
			return players.get(rowIndex);
		}
	}

	public void addPlayer(String name) {
		players.add(name);
		refresh();
	}
	
	public void clearPlayers() {
		players.clear();
		refresh();
	}

	public void refresh() {
		fireTableDataChanged();
	}

}