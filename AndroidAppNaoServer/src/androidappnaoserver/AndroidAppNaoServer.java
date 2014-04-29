/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package androidappnaoserver;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
		DataOutputStream streamWriter;
		try {

			boolean socketOpen = true;

			while (socketOpen) {

				// Listen for connections.
				Socket socket = listener.accept();

				try {
					// Buffered reader.
					BufferedReader input = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					streamWriter = new DataOutputStream(
							socket.getOutputStream());
					String appString = input.readLine();

					// Temp Output String For reciept verification.
					//System.out.println(appString);

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
							String motionVal = input.readLine();
							float val = Float.parseFloat(motionVal);
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
							byte[] videoData = robot.getVideoFrame();
							if (videoData != null) {
								BufferedImage img = createRGBImage(videoData, 160, 120);
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								ImageIO.write(img, "jpeg", baos);
								streamWriter.write(baos.toByteArray());
							}
							break;

						case "s":
							robot.Stop();
							break;
						default:
							robot.sayMessage(appString);
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
	
	public static class ImgPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		BufferedImage img;
		public ImgPanel(BufferedImage img)
		{
			this.img = img;
		}
		
		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawImage(img, 0, 0, null);
		}
	}
	
	private static BufferedImage createRGBImage(byte[] bytes, int width, int height) {
	    DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
	    ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
	}
}