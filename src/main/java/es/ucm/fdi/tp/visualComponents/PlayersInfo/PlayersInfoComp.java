package es.ucm.fdi.tp.visualComponents.PlayersInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.ColorChooser.ColorChooser;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;

/**
 * PlayersInfoComp is the concrete class we will use
 * for changing colours of players, etc. It should be
 * based on the use of a JTable (with a TableModel!)
 * and JColorChooser — see the example in the
 * package extra.jcolor that we have given you
 * 
 * @author Daniel Calle Sanchez
 * @author Manuel Guerrero Moñus
 *
 */
public class PlayersInfoComp<S extends GameState<S,A>,A extends GameAction<S,A>> 
extends PlayersInfoViewer<S,A> {

	private static final long serialVersionUID = 7173131957449911117L;

	private MyTableModel tModel;
	private JTable table;

	private Map<Integer, Color> colors; // Line -> Color
	private ColorChooser colorChooser;
	private int numberOfPlayer;
	private Iterator<Color> colorGenerator;
	
	/*
	 * This is a skeleton of the class, you have more
	 * code in extra.jboard! You should do
	 * something similar. Remember to call
	 * notifyObserver (that is defined in the super
	 * class) when a color of a player changes.
	 */
	 public PlayersInfoComp() {
		 this.observers = new ArrayList<PlayersInfoObserver>();
		 this.numberOfPlayer = 0;
		 colorGenerator = Utils.colorsGenerator();
		 initGUI();
	 }
	 
	private void initGUI() {

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder("Player Information"));
		
		this.colors = new HashMap<>();
		this.colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);

		// names table
		this.tModel = new MyTableModel();
		this.tModel.getRowCount();
		this.table = new JTable(this.tModel) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1) {
					comp.setBackground(colors.get(row));
				}
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		this.table.setToolTipText("Click on a row to change the color of a player");
		this.table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});

		this.add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel ctrlPabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.add(ctrlPabel, BorderLayout.PAGE_START);
		
		this.setOpaque(true);
		
	}
	
	@Override
	public void enableWindowMode() {
		super.enableWindowMode();
		super.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.window.setSize(300, 500);
		super.window.setVisible(true);
	}
	
	private void changeColor(int playerId) {
		
		this.colorChooser.setSelectedColorDialog(this.colors.get(playerId));
		this.colorChooser.openDialog();
		
		if (this.colorChooser.getColor() != null) {
			this.colors.put(playerId,this.colorChooser.getColor());
			repaint();
			this.notifyObservers(playerId,this.colorChooser.getColor());
		}

	}
	
	public void setNumberOfPlayer(int n) {
		this.numberOfPlayer = n;
		this.tModel.clearPlayers();
		for(int i=0;i<this.numberOfPlayer;i++) {
			this.tModel.addPlayer("");
			this.getPlayerColor(i);
		}
	}

	@Override
	public void enable() {
		
	}

	@Override
	public void disable() {
		
	}

	@Override
	public void update(S state, boolean active) {
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		
	}

	@Override
	public void setGameController(GUIController<S,A> gameCtrl) {
		
	}

	@Override
	public Color getPlayerColor(int p) {
		if(p>=0&&this.colors.get(p)==null){
			this.colors.put(p, this.colorGenerator.next());
		}
		return this.colors.get(p);
	}

	@Override
	public void setChatViewer(ChatViewer<S, A> chatViewer) {
		// TODO Auto-generated method stub
		
	}

}
