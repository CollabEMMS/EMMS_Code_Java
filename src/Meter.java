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
public abstract class Meter<T> {
	String name = "";
	String moduleDesc = "";
	String version = "";
	String CBversion = "";
	
	/**
	 * Method to get module info from module to set for datatype
	 */
	public abstract void getModuleInfo();
	
	/**
	 * Method to set module info for type
	 * @exception RF might change this
	 * @param ModuleName
	 * @param ModuleDescription
	 * @param ModuleVersion
	 * @param CommandBoardVersionToUseWith
	 */
	public void setModuleInfo(String ModuleName, String ModuleDescription, String ModuleVersion, String CommandBoardVersionToUseWith) {
		name = ModuleName;
		moduleDesc = ModuleDescription;
		version = ModuleVersion;
		CBversion = CommandBoardVersionToUseWith;
	};
	
	/**
	 * Method to get power information from power Module
	 */
	public abstract void getPowerInfo();
	
	/**
	 * Command to push all data to CB via SPI
	 * INCOMPLETE
	 * @exception Buggy SPI as of 10/3/2019
	 */
	public void updateCB() {
		// TBD add in
	};
	
	/**
	 * Sets Module info to Command Board, only for menus!
	 * Module Info Dictionary Is for Menu name and description, more info to come in the future.
	 * INCOMPLETE
	 */
	public void setModuleInfoForCB(String ModuleName, String ModuleVersion, Dictionary<String,String> moduleInfo) {
		// TBD add in
	};

	/**
	 * Runs a custom command through wifi board and returns response.
	 * @param command Custom command to run through board
	 * @param prefix run the AT+ command? Might switch to enum depending on how the future turns out with types. Special prefixes for each board?
	 * @return Returns response of custom command. 
	 */
	public abstract String CustomCommand(String command, boolean prefix);
}
