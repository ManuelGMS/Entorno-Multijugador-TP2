package es.ucm.fdi.tp.launcher;

import static org.junit.Assert.*;

import es.ucm.fdi.tp.launcher.MainPr4;
import org.junit.Test;

/**
* Class that test the class Main
* @author Daniel Calle Sánchez
* @author Manuel Guerrero Moñús
* @version 1.0 08/03/2017
*/
public class MainTest {
	
	/**
	 * Test with correct arguments
	 */
	@Test
	public void testValidArguments() {
		//Add arguments
		String arguments[] = {"ttt","smart","rand"};
		//Check if output is the expect
		assertEquals("The arguments are correct",MainPr4.isValidArguments(arguments),true);
	}

	/**
	 * Test that if there are too many arguments return an error message
	 */
	@Test
	public void testTooManyArguments() {
		//Add arguments
		String arguments[] = {"ttt","random","smart","smart"};
		//Check if output is the expect
		assertEquals("The number of the arguments are not correct",MainPr4.isValidArguments(arguments),false);
	}
	
	/**
	 * Test that if there are insufficient arguments return an error message
	 */
	@Test
	public void testFewArguments() {
		//Add arguments
		String arguments[] = {"ttt","random"};
		//Check if output is the expect
		assertEquals("The number of the arguments are not correct",MainPr4.isValidArguments(arguments),false);
	}
	
	/**
	 * Test that if the name of game passed by argument not exist, then show a error message
	 */
	@Test
	public void testInvalidGameName() {
		//Add arguments
		String arguments[] = {"chess","random","smart"};
		//Check if output is the expect
		assertEquals("The game name not exist",MainPr4.isValidArguments(arguments),false);
	}
	
	/**
	 * Test if the first name of player passed by argument not exist, then show a error message
	 */
	@Test
	public void testInvalidPlayerName1() {
		//Add arguments
		String arguments[] = {"ttt","luis","smart"};
		//Check if output is the expect
		assertEquals("The type of player not exist",MainPr4.isValidArguments(arguments),false);
	}
	
	/**
	 * Test if the second name of player passed by argument not exist, then show a error message
	 */
	@Test
	public void testInvalidPlayerName2() {
		//Add arguments
		String arguments[] = {"ttt","random","marcos"};
		//Check if output is the expect
		assertEquals("The type of player not exist",MainPr4.isValidArguments(arguments),false);
	}
	

}
