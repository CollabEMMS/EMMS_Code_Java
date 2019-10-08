import java.util.Dictionary;

/**
 * Wifi Meter Class
 * @author Zachery Holsinger
 *
 */
public class Wifi_Meter extends Meter {
	
	/**
	 * Initialization of Meter. Name will be reset and used for tracking.
	 * Initializing data type also calls it to get module information.
	 * @implNote Automatically gets module information, but you must manually call the getPowerInfo()
	 * @param Name // Name of Meter
	 */
	public Wifi_Meter(String Name) {
		name = Name;
		getModuleInfo();
	}

	@Override
	public void getModuleInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPowerInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String CustomCommand(String command, boolean prefix) {
		// TODO Auto-generated method stub
		return null;
	}
}
