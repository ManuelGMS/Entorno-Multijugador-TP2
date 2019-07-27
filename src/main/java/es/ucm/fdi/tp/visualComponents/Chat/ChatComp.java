package es.ucm.fdi.tp.visualComponents.Chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.mvc.gui.GameEvent;
import es.ucm.fdi.tp.mvc.gui.GameEvent.EventType;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;

public class ChatComp<S extends GameState<S,A>,A extends GameAction<S,A>> extends ChatViewer<S,A> {

	private static final long serialVersionUID = 1436878446417093253L;

	private volatile JPanel mainPanel;
	private volatile JButton sendButton;
	private volatile JTextArea textArea;
	private volatile JTextField textField;
	private volatile JScrollPane scrollPane;
	private volatile GUIController<S,A> ctrl;
	
	public ChatComp() {
		initGUI();
	}
	
	private void initGUI() {
		
		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createTitledBorder("Chat"));
		
		this.mainPanel = new JPanel();
		
		this.sendButton = new JButton("Send");
		
		this.textField = new JTextField(18);
		
		this.textArea = new JTextArea(4,24);
		
		this.textArea.setEditable(false);
		
		this.scrollPane = new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		this.sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ChatComp.this.sendMessage(ChatComp.this.textField.getText());
				
			}
			
		});
		
		this.textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.mainPanel.add(this.scrollPane);
		
		this.mainPanel.add(this.sendButton);
		
		this.mainPanel.add(this.textField);
		
		this.add(this.mainPanel);
		
	}
	
	//************************* MÉTODOS ESPECÍFICOS DEL CHAT ********************************************************//

	@Override
	public void getMessage(String message) {
	
		this.textArea.append(message + System.getProperty("line.separator"));

	}
	
	@Override
	public void sendMessage(String message) {
		
		this.textField.setText("");
		
		ctrl.handleEvent(new GameEvent<S,A>(EventType.Chat,null,null,null,"Player " + ctrl.getPlayerId() + ": " + message));
			
	}

	//***************************************************************************************************************//
	
	//************************* MÉTODOS ESPECÍFICOS DE LA VISTA *****************************************************//
	
	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(S state, boolean active) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameController(GUIController<S, A> gameCtrl) {
		this.ctrl = gameCtrl;
	}

	@Override
	public void setChatViewer(ChatViewer<S, A> chatViewer) {
		// TODO Auto-generated method stub
		
	}

	//***************************************************************************************************************//

}
