package es.ucm.fdi.tp.visualComponents.MessageViewer;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;


/**
 * MessageViewerComp is the concrete class we will use for
 * the message viewer. It uses a JTextArea to view messages.
 * In principle its disable, enable and update methods do
 * nothing, and it does not need a controller as well …
 * 
 * @author Daniel Calle Sanchez
 * @author Manuel Guerrero Moñus
 *
 */
public class MessageViewerComp<S extends GameState<S,A>,A extends GameAction<S,A>> 
extends MessageViewer<S,A> {

	private static final long serialVersionUID = -7057286934291279350L;

    boolean active;
	private JTextArea msgArea;
    
    public MessageViewerComp() {
        initGUI();
    }
    
    private void initGUI(){
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder("Status Messages"));
        this.msgArea = new JTextArea(15, 20);
        this.msgArea.setEditable(false);
        JScrollPane statusAreaScroll = new JScrollPane(this.msgArea);
        this.add(statusAreaScroll, BorderLayout.CENTER);
   }
   
   final protected void clearStatusAreaContent() {
	   this.msgArea.setText("");
   }
   
   public void addContent(String msg) {
	   this.msgArea.append("* " + msg + "\n");
   }
   
   public void setContent(String msg) {
	   this.msgArea.setText(msg);
   }
   
   public String getContent(){
        return this.msgArea.getText();
   }
   
	@Override
	public void enableWindowMode() {
		super.enableWindowMode();
		super.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.window.setSize(300, 500);
		super.window.setVisible(true);
	}
	
	@Override
	public void enable() {
		this.msgArea.setEnabled(true);
		
	}
	
	@Override
	public void disable() {
		this.msgArea.setEnabled(false);
		
	}
	
	@Override
	public void update(S state, boolean active) {
    	
	}
	
	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer) {
		
	}
	
	@Override
	public void setGameController(GUIController<S,A> gameCtrl) {
		
	}

	@Override
	public void setChatViewer(ChatViewer<S, A> chatViewer) {
		// TODO Auto-generated method stub
		
	}
	   
}