import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Zachery Holsinger
 *
 */
public class Communication {

	public static void main(String[] args) {
		//		Object meters[] = meterScan.getMeters();
		//		Client client = new Client();
		//		String response;
		//		String ip = (String) meters[0]; // THIS WILL ALWAYS PING THE FIRST LOWEST METER, SO USE FOR WHEN ONLY 1 IS PLUGGED in
		//		fetchNewMeterConfig(ip);
		//		updateCSVInfoNetwork(ip);
		startUp();
		//		client.Communicate(ip, 80, "!MOD;PULSE*");

	}

	public static void startUp() {
		Object[] metersobj = meterScan.getMeters();
		String[] meters;
		meters = new String[metersobj.length];
		for (int i = 0; i < meters.length; i++) {
			meters[i] = (String) metersobj[i];
			String ip = (String) meters[i]; 
			fetchNewMeterConfig(ip);
			updateCSVInfoNetwork(ip);
		}
		refreshActive((String[]) meters);
	}


	public static String[][] ReturnFileValues() {
		File info = new File("src/meterData.csv");
		try {
			info.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//			System.out.print("file operation error");
		}

		File file= info;

		// this gives you a 2-dimensional array of strings
		List<List<String>> lines = new ArrayList<>();
		Scanner inputStream;

		try{
			inputStream = new Scanner(file);

			while(inputStream.hasNextLine()){
				String line= inputStream.nextLine();
				String[] values = line.split(",");
				// this adds the currently parsed line to the 2-dimensional string array
				lines.add(Arrays.asList(values));
				//                System.out.println(Arrays.asList(values));
			}

			inputStream.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// the following code lets you iterate through the 2-dimensional array
		int lineNo = 1;
		int rowNum = 0;
		int columnNum = 0;

		// Just count for initial array size from double arrayList
		for(List<String> line: lines) {
			int columnNo = 1;
			for (String value: line) {
				// CREATE Nice ARRAY BOI
				//                System.out.println("Line " + lineNo + " Column " + columnNo + ": " + value);
				columnNo++;
				if (columnNo - 1 > columnNum) {
					columnNum = columnNo;
				}

				if( lineNo > rowNum) {
					rowNum = lineNo;
				}

			}
			lineNo++;
		}


		// Actually put values in for array 

		//        System.out.println(rowNum + " " + columnNum);
		String[][] meterInfo = new String[rowNum][columnNum];
		lineNo = 1;
		for(List<String> line: lines) {
			int columnNo = 1;
			for (String value: line) {
				// CREATE Nice ARRAY BOI
				//                System.out.println("Line " + lineNo + " Column " + columnNo + ": " + value);

				meterInfo[lineNo - 1][columnNo - 1] = value;
				columnNo++;
			}
			lineNo++;
		}

		//        System.out.println(Arrays.deepToString(meterInfo));
		return meterInfo;
	}

	private static String[] fetchNewNetworkInfo(String ip) {
		String[] meterInfo = new String[2];
		Client client = new Client();
		String response = client.Communicate(ip, 80, "!MOD;NETWORK*");
		String[] blocks = response.split(",");
		blocks[1] = blocks[1].replaceAll("[^\\d.]", "");
		blocks[2] = blocks[2].replaceAll("OK", "");		
		meterInfo[0] = blocks[1];
		meterInfo[1] = blocks[2];
		System.out.println("Ip: " + meterInfo[0] + ", MAC: " + meterInfo[1]);

		return meterInfo;
	}

	private static String[] fetchNewMeterConfig(String ip) {
		String[] meterInfo = new String[5];
		Client client = new Client();
		String response = client.Communicate(ip, 80, "!MOD;CONFIG*");
		String[] blocks = response.split(";");
		//		System.out.println(Arrays.toString(blocks));
		meterInfo[0] = blocks[1];
		meterInfo[1] = blocks[2];
		meterInfo[2] = blocks[3];
		meterInfo[3] = blocks[4];
		meterInfo[4] = blocks[5];
//		System.out.println("Date: " + meterInfo[0] + ", Version No: " + meterInfo[1] + ", Name: " + meterInfo[2] + ", Number: " + meterInfo[3] + ", Location: " + meterInfo[4]);

		return meterInfo;
	}

	public static void updateCSVInfoNetwork(String ip) {
		String[][] CSVold = ReturnFileValues();
		String[][] CSVnew;
		String[] netWorkStuff = fetchNewNetworkInfo(ip);
		String MacAddr = netWorkStuff[1];
		//		System.out.println(MacAddr);
		boolean exists = false;

		for (int i = 0; i < CSVold.length; i++ ) {
			String MACinCSV = CSVold[i][11];
			//			System.out.print(MACinCSV); // This is MAC Addr List
			if (MACinCSV.contentEquals(MacAddr)) {
				exists = true;
				String old_ip = CSVold[i][3];
				String current_ip = netWorkStuff[0];
				//				System.out.println(old_ip + " " + current_ip);
				if (!old_ip.equals(current_ip)) {
					System.out.println("Replacing Ip for " + CSVold[i][4]);
					CSVold[i][3] = current_ip;
				}
			}
		}

		if (!exists) {
			System.out.println("Meter isn't found in System, adding...");
			String meterConfig[] = fetchNewMeterConfig(ip);

			CSVnew = new String[CSVold.length + 1][CSVold[0].length];
			for(int i = 0; i < CSVold.length; i++ ) {
				for(int j = 0; j < CSVold[0].length; j++) {
					CSVnew[i][j] = CSVold[i][j]; 
				}
			}

			CSVnew[CSVold.length][0] = "EMMS Collaboratory Team";
			CSVnew[CSVold.length][1] = meterConfig[0];
			CSVnew[CSVold.length][2] = meterConfig[1];
			CSVnew[CSVold.length][3] = netWorkStuff[0];
			CSVnew[CSVold.length][4] = meterConfig[2];
			CSVnew[CSVold.length][5] = meterConfig[3];
			CSVnew[CSVold.length][6] = meterConfig[4];
			CSVnew[CSVold.length][11] = netWorkStuff[1];
			CSVnew[CSVold.length][12] = "TRUE";


			//			System.out.println(Arrays.deepToString(CSVnew));
		} else {

			CSVnew = new String[CSVold.length][CSVold[0].length];
			for(int i = 0; i < CSVold.length; i++ ) {
				for(int j = 0; j < CSVold[0].length; j++) {
					CSVnew[i][j] = CSVold[i][j]; 
				}
			}
		}

		File file_old = new File("src/meterData.csv");
		file_old.delete();
		File file = new File("src/meterData.csv");
		FileWriter fr;
		for (int i = 0; i < CSVnew.length; i++) {
			try {
				fr = new FileWriter(file, true);
				// Converting and sending to CSV
				String line = Arrays.toString(CSVnew[i]);
				line = line.replace("[", "");
				line = line.replace("]", "");
				line = line.replace(", ", ",");
				fr.write(line);
				fr.write("\n");
				//				System.out.println("Done Adding..");
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 

	}

	public static void refreshActive(String[] activeIps) {
		String[][] CSVold = ReturnFileValues(); 

		for (int i = 0; i < CSVold.length; i++ ) {
			CSVold[i][12] = "FALSE";
			for (int j = 0; j < activeIps.length; j++) {
				String IPfile = CSVold[i][3];
				if(IPfile.contains(activeIps[j])) {
					CSVold[i][12] = "TRUE";
				}
			}
		}

		String[][] CSVnew = new String[CSVold.length][CSVold[0].length];
		for(int i = 0; i < CSVold.length; i++ ) {
			for(int j = 0; j < CSVold[0].length; j++) {
				CSVnew[i][j] = CSVold[i][j]; 
			}
		}

		File file_old = new File("src/meterData.csv");
		file_old.delete();
		File file = new File("src/meterData.csv");
		FileWriter fr;
		for (int i = 0; i < CSVnew.length; i++) {
			try {
				fr = new FileWriter(file, true);
				// Converting and sending to CSV
				String line = Arrays.toString(CSVnew[i]);
				line = line.replace("[", "");
				line = line.replace("]", "");
				line = line.replace(", ", ",");
				fr.write(line);
				fr.write("\n");
				//				System.out.println("Done Adding..");
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		//		System.out.println(Arrays.deepToString(CSVold));
	}

	public static void showOff(String[][] ips, int trials) {
		Client client = new Client();
		for( int r = 0; r < trials * 2; r ++) {
			for(int i = 0; i < ips.length; i++ ) {
				String active = ips[i][12];
				if(active.contains("TRUE")) {
					String ip = ips[i][3];
//					System.out.println(ip);
					client.Communicate(ip, 80, "!MOD;PULSE*");
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
				}
			}
		}

	}
}
