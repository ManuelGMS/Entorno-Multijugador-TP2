package es.ucm.fdi.tp.visualComponents.JCrossTable;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.dessignPatterns.Memento.JCrossJTableMemento;

public class JCrossTableComp extends JCrossTableViewer {
	
	
	public JCrossTableComp(String[] rowNames, String[] columnNames, Object[][] dataMatrix) {
		
		initialState = new JCrossJTableMemento(dataMatrix);
		
		initGUI(rowNames,columnNames,initialState.getInitialState());
		
	}
	
	private void initGUI(String[] rowNames, String[] columnNames, Object[][] dataMatrix) {
		
		initalizeRowTable(rowNames.length,1,rowNames);
		
		initalizeColumnTable(rowNames.length,columnNames.length,columnNames);
		
		initalizeData(dataMatrix);
		
		initalizeAppearance();

	}
	
	private void initalizeData(Object[][] dataMatrix) {
		
		for(int i = 0 ; i < dataMatrix.length ; ++i) {
			
			for(int j = 0 ; j < dataMatrix[0].length ; ++j ) 
				
				columnHeaderTable.setValueAt(dataMatrix[i][j],i,j);
			
		}
		
	}
	
	private void initalizeAppearance() {
		
		rowHeaderTable.setPreferredScrollableViewportSize(new Dimension(70,0));
		
		cellRenderer = new DefaultTableCellRenderer();
		
		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		for(int i = 0 ; i < columnHeaderTable.getColumnCount() ; ++i ) 
		
			columnHeaderTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
		
		container = new JScrollPane(columnHeaderTable);
		
		container.setRowHeaderView(rowHeaderTable);
		
		container.setPreferredSize(new Dimension(250,70));
		
	}
	
	private void initalizeRowTable(int rows, int cols, String[] rowNames) {
		
		rowTableModel = new DefaultTableModel() {

			private static final long serialVersionUID = 4538921888524908545L;

			@Override
            public int getRowCount() {
                return rows;
            }
			
			@Override
            public int getColumnCount() {
                return cols;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
            
        };
         
		rowHeaderTable = new JTable(rowTableModel);
		
		rowHeaderTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {

			@Override
	        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
	        		boolean hasFocus, int row, int column) {
				
				return rowHeaderTable.getTableHeader().getDefaultRenderer().
						getTableCellRendererComponent(rowHeaderTable, value, false, false, 0, 0);
				
			}
	        	
	    } );
		
		for(int i = 0 ; i < rowHeaderTable.getRowCount() ; ++i ) 
			
			rowHeaderTable.setValueAt(rowNames[i],i,0);
		
	}
	
	private void initalizeColumnTable(int rows, int cols, String[] columnNames) {
		
		columnTableModel = new DefaultTableModel() {
			
			private static final long serialVersionUID = 7482078010006634331L;
	
			@Override
	        public int getRowCount() {
				return rows;
	        }
				
			@Override
	        public int getColumnCount() {
	            return cols;
	        }
				
			@Override
	        public boolean isCellEditable(int row, int col) {
	            return false;
	        }
			
			@Override
			public String getColumnName(int column) {
				return columnNames[column];
			}
			
		};
		
		columnHeaderTable = new JTable(columnTableModel);
		
		columnHeaderTable.setFocusable(false);
		
		columnHeaderTable.setRowSelectionAllowed(false);
		
		columnHeaderTable.getTableHeader().setResizingAllowed(false);
		
		columnHeaderTable.getTableHeader().setReorderingAllowed(false);
		
	}
	
}
