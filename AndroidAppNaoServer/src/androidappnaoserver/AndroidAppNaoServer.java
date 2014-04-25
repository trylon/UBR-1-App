/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package androidappnaoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 * Android
 * 
 * @author bShipman
 */
public class AndroidAppNaoServer {

	static String NAO_IPADDRESS;
	static int NAO_PORT;

	/**
	 * @param args
	 *            The port to listen for connections on.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		boolean isNaoIPValid = false;
		boolean isNaoPortValid = false;
		boolean isServerPortValid = false;
		String ipAddress = "";
		String port = "";
		String listenPort = "";

		do {
			ipAddress = JOptionPane.showInputDialog(null,
					"Enter Nao's IP Address:", "Nao Connection.",
					JOptionPane.INFORMATION_MESSAGE);
			String[] addr = ipAddress.split("\\.");
			if (addr.length == 4) {
				isNaoIPValid = true;
				for (int i = 0; i < addr.length; i++) {
					try {
						int lowerBound = 0;
						int upperBound = 254;
						if (i == 0 || i == 3) {
							lowerBound++;
						}
						int octet = Integer.parseInt(addr[i]);
						if (octet < lowerBound || octet > upperBound) {
							isNaoIPValid &= false;
						}
					} catch (Exception ex) {
						isNaoIPValid = false;
					}
				}
			}
		} while (!isNaoIPValid);

		do {
			port = JOptionPane.showInputDialog(null,
					"Enter Nao's Port Number:", "Nao Connection.",
					JOptionPane.INFORMATION_MESSAGE);
			try {
				int portTest = Integer.parseInt(port);
				if (portTest > 0 && portTest <= 65535) {
					isNaoPortValid = true;
				}
			} catch (Exception ex) {
				isNaoPortValid = false;
			}
		} while (!isNaoPortValid);

		do {
			listenPort = JOptionPane.showInputDialog(null,
					"Enter Server Listening Port:", "Server Connection",
					JOptionPane.INFORMATION_MESSAGE);
			try {
				int portTest = Integer.parseInt(listenPort);
				if (portTest > 0 && portTest <= 65535) {
					isServerPortValid = true;
				}
			} catch (Exception ex) {
				isServerPortValid = false;
			}
		} while (!isServerPortValid);

		NAO_IPADDRESS = ipAddress;
		NAO_PORT = Integer.parseInt(port);

		RobotConnection robot = new RobotConnection(NAO_IPADDRESS, NAO_PORT);

		ServerSocket listener = new ServerSocket(Integer.parseInt(listenPort));
		PrintWriter streamWriter;
		try {

			boolean socketOpen = true;

			while (socketOpen) {

				// Listen for connections.
				Socket socket = listener.accept();

				try {
					// Buffered reader.
					BufferedReader input = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					streamWriter = new PrintWriter(socket.getOutputStream(),
							true);
					String appString = input.readLine();

					// Temp Output String For reciept verification.
					System.out.println(appString);

					if (appString != null) {

						switch (appString) {
						case "Shutdown Nao Server":
							robot.Stop();
							System.out
									.println("Server Shutdown Remotely, Goodbuy.");
							socketOpen = false; // Shutdown the server remotely.
							break;

						case "sj":
							robot.Stiffen();
							break;

						case "u":
							robot.Unstiffen();
							break;

						case "su":
							robot.Stand();
							break;

						case "sd":
							robot.Sit();
							break;

						case "w":
							robot.Walk();
							break;

						case "l":
							robot.Turn(90.0f);
							break;

						case "r":
							robot.Turn(-90.0f);
							break;

						case "sl":
							robot.Strafe(true);
							break;

						case "sr":
							robot.Strafe(false);
							break;

						case "e":
							robot.emergencyStop();
							break;

						case "v":
							streamWriter.println(robot.getVideoFrame());
							break;

						case "s":
						default:
							robot.Stop();
							break;
						}

					}
				} finally {
					socket.close();
				}

			}
		} finally {
			listener.close();
		}
	}
}