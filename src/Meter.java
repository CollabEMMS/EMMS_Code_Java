import java.util.Dictionary;

/**
 * MeterADT.java
 * copyright EMMS 2019
 * @author Zachery Holsinger
 * @param Basic Config for all meter types
 * @version 1.0
 */

/*
 * Interface for all meter types
 * @param <T>
 */
public interface Meter<T> {
	
	/**
	 * Method to get network info
	 * @exception RF might change this
	 */
	public void getNetworkInfo();
	
	/**
	 * Method to get power information from power Module
	 */
	public void getPowerInfo();
	
	/**
	 * Command to push all data to CB via SPI
	 * @exception Buggy SPI as of 10/3/2019
	 */
	public void updateCB();
	
	/**
	 * Sets Module info to Command Board, only for menus!
	 * Module Info Dictionary Is for Menu name and description, more info to come in the future.
	 */
	public void setCBModuleInfo(String ModuleName, String ModuleVersion, Dictionary<String,String> moduleInfo);

	/**
	 * Runs a custom command through wifi board and returns response.
	 * @param command Custom command to run through board
	 * @param prefix run the AT+ command? Might switch to enum depending on how the future turns out with types. Special prefixes for each board?
	 * @return Returns response of custom command. 
	 */
	public String CustomCommand(String command, boolean prefix);
}
