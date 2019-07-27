package es.ucm.fdi.tp.visualComponents.JCrossTable;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import es.ucm.fdi.tp.dessignPatterns.Memento.JCrossJTableMemento;

public abstract class JCrossTableViewer {

	protected JScrollPane container; 
	protected JTable rowHeaderTable;
	protected JTable columnHeaderTable;
	protected DefaultTableModel rowTableModel;
	protected JCrossJTableMemento initialState;
	protected DefaultTableModel columnTableModel;
	protected DefaultTableCellRenderer cellRenderer;

	public JScrollPane getComponent() {
		
		return this.container;
		
	}
	
	public int getRowCount() {
		
		return rowHeaderTable.getRowCount();
		
	}
	
	public int getColumnCount() {
		
		return columnHeaderTable.getColumnCount();
		
	}
	
	public Object getValueAt(int row, int column) {
		
		return this.columnHeaderTable.getValueAt(row, column);
		
	}
	
	public void setValueAt(Object value, int row, int column) {
		
		this.columnHeaderTable.setValueAt(value, row, column);
		
	}
	
	public void setSize(int width, int height) {
		
		container.setPreferredSize(new Dimension(width,height));
		
	}
	
	public void resetToInitialState() {

		for( int i = 0 ; i < this.rowHeaderTable.getRowCount() ; ++i ) {
			
			for( int j = 0 ; j < this.columnHeaderTable.getColumnCount() ; ++j ) {
				
				this.setValueAt(this.initialState.getInitialState()[i][j], i, j);
				
			}
			
		}
		
	}
	
}
