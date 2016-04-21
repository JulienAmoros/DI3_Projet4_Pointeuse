package slaves;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;

public class TimeClockModel {
	private int id;
	private ZonedDateTime time;

	private static boolean waitClient;
	private static String buffer = "";
	private static ServerSocket srvSocket = null;
	private static OutputStream outStream = null;
	private static ArrayList<TimeClockModel> listOfCheckInOut = 
			new ArrayList<TimeClockModel>();
	
	
	public static void add(int id) {
		listOfCheckInOut.add(new TimeClockModel(id));
	}
	
	private static void waitForClient() {
		try {
			if(srvSocket == null)
				srvSocket = new ServerSocket(1337, 1);
		
			if(outStream == null)
				waitClient = true;
			
			if(waitClient) {
				System.out.println("Waiting for a client");
				//Block until a connection
				Socket socket = srvSocket.accept();
				outStream = socket.getOutputStream();
				waitClient = false;
				System.out.println("Client here");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendAll() {
		waitForClient();
		
		//The purpose of the iterator is to allow deleting while iterating
		Iterator<TimeClockModel> it = listOfCheckInOut.iterator();
		while(it.hasNext()) {
			buffer += it.next().stringFormat() + '\n';
			it.remove();
		}
		
		if(!buffer.isEmpty()) {
			try {
				outStream.write(buffer.getBytes());
				buffer = ""; //Clear the buffer
			} catch (IOException e) {
				System.out.println("Client disconnected");
				waitClient = true;
			}
		}
	}
	
	
	private TimeClockModel(int id) {		
		this.id = id;
		this.time = Utils.roundTimeMinQuarter(ZonedDateTime.now(ZoneOffset.UTC));
	}
	
	private String stringFormat() {
		String format = String.valueOf(id) + ' ' + time.toString();
		return format;
	}
}