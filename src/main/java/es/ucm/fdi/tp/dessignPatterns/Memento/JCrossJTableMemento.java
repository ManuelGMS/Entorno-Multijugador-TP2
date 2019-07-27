package es.ucm.fdi.tp.dessignPatterns.Memento;

public class JCrossJTableMemento {

	private Object[][] initialState;
	
	public JCrossJTableMemento(Object[][] initialState) {
	
		this.initialState = initialState;
	
	}
	
	public Object[][] getInitialState() {
		
		Object[][] state = new Object[initialState.length][initialState[0].length];
		
		for( int i = 0 ; i < this.initialState.length ; ++i ) {
			
			for( int j = 0 ; j < this.initialState[0].length ; ++j ) {
				
				state[i][j] = initialState[i][j];
				
			}
			
		}
		
		return state;
		
	}

}
