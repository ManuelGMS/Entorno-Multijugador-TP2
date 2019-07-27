package es.ucm.fdi.tp.was;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import es.ucm.fdi.tp.games.was.WasAction;
import es.ucm.fdi.tp.games.was.WasElements;
import es.ucm.fdi.tp.games.was.WasState;

/**
* Class that test the game Wolf and Sheep
* @author Daniel Calle Sánchez
* @author Manuel Guerrero Moñús
* @version 1.0 08/03/2017
*/
public class WolfAndSheepStateTest {
	
	/**
	 * Test that a wolf in the opposite row wins
	 */
	@Test
	public void testWolfWin() {
		//Create a board with configuration for test
		int board [][] = new int[WasState.DIM][WasState.DIM];
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++)
				board[i][j]=WasElements.EMPTY.getValue();
		//Put the wolf
		board[0][1] = WasElements.WOLF.getValue();
		WasState state = new WasState(board,0);
		//Check if output is expected
		assertEquals("The wolf win",state.getWinner(),WasElements.WOLF.getValue());
	}
	
	/**
	 * Test that if the sheep surround the wolf they win
	 */
	@Test
	public void testSheepWin() {
		//Create a board with configuration for test
		int board [][] = new int[WasState.DIM][WasState.DIM];
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++)
				board[i][j]=WasElements.EMPTY.getValue();
		
		WasState state = new WasState();
		
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board.length;j++){
				//Put the wolf
				board[i][j] = WasElements.WOLF.getValue();
				//Put the sheep
				for(int i1=i-1;i1<=i+1;i1+=2)
					for(int j1=j-1;j1<=j+1;j1+=2)
						if(state.at(i1, j1)!=WasElements.OUTSIDE.getValue())
							board[i1][j1]=WasElements.SHEEP.getValue();
				
				state = new WasState(board,WasElements.SHEEP.getValue());
				
				//Check if output is expected
				assertEquals("The sheep win",state.getWinner(), WasElements.SHEEP.getValue());
				
				
				board[i][j] = WasElements.EMPTY.getValue();
				
				for(int k=i-1;k<=i+1;k+=2)
					for(int h=j-1;h<=j+1;h+=2)
						if(0<=k&&k<board.length&&0<=h&&h<board.length)
							board[k][h]=WasElements.EMPTY.getValue();

			}
				
		
		

		

	}
	
	/**
	 * Test that the wolf in the first turn has only one movement
	 */
	@Test
	public void testInitialActionsOfTheWolf() {
		//Creates an initial state
		WasState state = new  WasState();
		//Bring list of wolf actions
		List<WasAction> listOfActions = state.validActions(WasElements.WOLF.getValue());
		//Check if list of the wolf actions is the expected
		assertEquals("Number of actions is correct",listOfActions.size(),1);
		//Move the wolf
		state = listOfActions.get(WasElements.WOLF.getValue()).applyTo(state);
		//Bring list of wolf actions
		listOfActions = state.validActions(WasElements.WOLF.getValue());
		//Check if list of the wolf actions is the expected
		assertEquals("Number of actions is correct",listOfActions.size(),4);
		
	}
	
	/**
	 * Test that the sheep in the first turn have only seven movement
	 */
	@Test
	public void testInitialActionsOfTheSheep() {
		//Creates an initial state
		WasState gameToTest = new  WasState();
		int dim = gameToTest.getBoard().length;
		//Bring list of sheep actions
		List<WasAction> listOfActions = gameToTest.validActions(WasElements.SHEEP.getValue());
		//Check if list of the sheep actions is the expected
		if(dim%2==0)
			assertEquals("Number of actions is correct",listOfActions.size(),((dim)/2)*2-1);
		else
			assertEquals("Number of actions is correct",listOfActions.size(),((dim)/2+1)*2-2);
	}
	
}
