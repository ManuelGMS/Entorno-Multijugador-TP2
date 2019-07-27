package es.ucm.fdi.tp.visualComponents.JBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/*
 * Clase que realiza las funciones gráficas de dibujado,
 * como pintar las celdas y el tablero, marcar una casilla
 * o una ficha, etc.
 */

public abstract class JBoard extends JComponent {

	private static final long serialVersionUID = -4518722262994516431L;

	private int _CELL_HEIGHT = 50;
	private int _CELL_WIDTH = 50;
	private int _SEPARATOR = -2;

	public enum Shape {
		CIRCLE, RECTANGLE, CIRCLE_BORDER, CROSS, CHESS_PIECE, NONE
	}

	public JBoard() {
		initGUI();
	}

	private void initGUI() {
		
		setBorder(BorderFactory.createRaisedBevelBorder());

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				JBoard.this.keyTyped(e.getKeyChar());
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				JBoard.this.requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int col = (e.getX() / _CELL_WIDTH);
				int row = (e.getY() / _CELL_HEIGHT);

				int mouseButton = 0;

				if (SwingUtilities.isLeftMouseButton(e))
					mouseButton = 1;
				else if (SwingUtilities.isMiddleMouseButton(e))
					mouseButton = 2;
				else if (SwingUtilities.isRightMouseButton(e))
					mouseButton = 3;

				if (mouseButton == 0)
					return; // Unknown button, don't know if it is possible!

				JBoard.this.mouseClicked(row, col, e.getClickCount(), mouseButton);
			}
		});
		
		_SEPARATOR = getSepPixels();
		if ( _SEPARATOR < 0 ) _SEPARATOR = 0;

		this.setPreferredSize(new Dimension(400, 400));
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		fillBoard(g);
	}

	private void fillBoard(Graphics g) {
		int numCols = getNumCols();
		int numRows = getNumRows();

		if (numCols <= 0 || numRows <= 0) {
			g.setColor(Color.red);
			g.drawString("Waiting for game to start!", 20, this.getHeight() / 2);
			return;
		}

		_CELL_WIDTH = this.getWidth() / numCols;
		_CELL_HEIGHT = this.getHeight() / numRows;

		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				drawCell(i, j, g);
		
	}

	private void drawCell(int row, int col, Graphics g) {
		int x = col * _CELL_WIDTH;
		int y = row * _CELL_HEIGHT;

		g.setColor( getBackground(row, col));
		g.fillRect(x + _SEPARATOR, y + _SEPARATOR, _CELL_WIDTH - 2*_SEPARATOR, _CELL_HEIGHT - 2*_SEPARATOR);

		Integer p = getPosition(row, col);

		if (p != null) {
			Color c = getColor(p);
			Shape s = getShape(p);
			Image im = null;

			Graphics2D g2 = (Graphics2D) g;
			ImageIcon i = getIcon(p);
		    if(i!=null)
				im = i.getImage();
			g.setColor(c);
			switch (s) {
			case CIRCLE:
				g.fillOval(x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4);
				g.setColor(Color.BLACK);
				g.drawOval(x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4);
				g2.drawImage(im, x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4, this);
				break;
			case RECTANGLE:
				g.fillRect(x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4);
				g.setColor(Color.BLACK);
				g.drawRect(x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4);
				g2.drawImage(im,  x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4, this);
				break;
			case CIRCLE_BORDER:
				g2.setStroke(new BasicStroke(8));
				g2.drawOval(x + _SEPARATOR+10, y + _SEPARATOR+10, (_CELL_WIDTH-15) - 2*_SEPARATOR-4, (_CELL_HEIGHT-15) - 2*_SEPARATOR-4);
				break;
			case CROSS:
				g2.setStroke(new BasicStroke(8));
				g2.drawLine(x + _SEPARATOR+10, y + _SEPARATOR+10,x + _CELL_WIDTH - 10,y + _CELL_HEIGHT - 10);
	            g2.drawLine(x + _CELL_WIDTH - 10, y + _SEPARATOR+10, x + _SEPARATOR+10 ,y + _CELL_HEIGHT - 10);
	            break;
			default:
				break;
			
			}
		}

	}
	
	// Método que es llamado al seleccionarse una ficha.
	public void selectPiece(int col, int row, Color color) {
	
		int x = col * _CELL_WIDTH;
		int y = row * _CELL_HEIGHT;

		Graphics g = this.getGraphics();
	
		fillBoard(g);
		
		Integer p = getPosition(row, col);

		if (p != null) {
			
			Shape s = getShape(p);

			g.setColor(color);
			
			switch (s) {
				case CIRCLE:
					g.drawOval(x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4);
				break;
				case RECTANGLE:
					g.drawRect(x + _SEPARATOR+2, y + _SEPARATOR+2, _CELL_WIDTH - 2*_SEPARATOR-4, _CELL_HEIGHT - 2*_SEPARATOR-4);
				break;
				default:
				break;
			}
			
		}
		
	}
	
	/*
	 * Método que marca el borde de una casilla cuando se solicita ayuda.
	 * Lo que pasamos por parámetro es la fila, la columna y el color a pintar.
	 */
	
	public void markCell(int col, int row, Color color) {
		int x = col * _CELL_WIDTH;
		int y = row * _CELL_HEIGHT;

		Graphics g = this.getGraphics();
		g.setColor(color);
		g.drawRect(x + _SEPARATOR, y + _SEPARATOR, _CELL_WIDTH - 2*_SEPARATOR, _CELL_HEIGHT - 2*_SEPARATOR);
	}
	
	/*
	 * Los siguientes métodos son implementados por el tablero específico del juego,
	 * se definen en el JBoard de RectBoardView y pueden ser sobreescritos por la 
	 * vista particular del juego. 
	 */
	
	protected abstract ImageIcon getIcon(Integer p);
	
	protected abstract void keyTyped(int keyCode);

	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);

	protected abstract Shape getShape(int player);

	protected abstract Color getColor(int player);

	protected abstract Integer getPosition(int row, int col);

	protected abstract Color getBackground(int row, int col);

	protected abstract int getNumRows();

	protected abstract int getNumCols();
	
	protected int getSepPixels() {
		return 2;
	}

}
