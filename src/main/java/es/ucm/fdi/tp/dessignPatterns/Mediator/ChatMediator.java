package es.ucm.fdi.tp.dessignPatterns.Mediator;

import java.util.ArrayList;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;

public class ChatMediator<S extends GameState<S,A>,A extends GameAction<S,A>> {

	private ArrayList<ChatViewer<S,A>> chats;
	
	public ChatMediator() {
		
		this.chats = new ArrayList<>();
		
	}

	public void addChat(ChatViewer<S,A> chat) {
		
		this.chats.add(chat);
		
	}
	
	public void talkToPlayers(String message) {

		for(ChatViewer<S,A> chat : this.chats)
			
			chat.getMessage(message);
			
	}
	
}
