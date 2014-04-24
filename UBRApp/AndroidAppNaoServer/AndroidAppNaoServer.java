/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package androidappnaoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * @param args The port to listen for connections on.
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

        try {

            boolean socketOpen = true;

            while (socketOpen) {

                // Listen for connections.
                Socket socket = listener.accept();

                try {
                    // Buffered reader.
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

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

                            case "Stiffen Robot Joints":
                                robot.Stiffen();
                                break;

                            case "Unstiffen Robot Joints":
                                robot.Unstiffen();
                                break;

                            case "Stand Robot Up":
                                robot.Stand();
                                break;

                            case "Sit Robot Down":
                                robot.Sit();
                                break;

                            case "Walk Forward":
                                robot.Walk();
                                break;

                            case "Turn Robot Left":
                                robot.Turn(90.0f);
                                break;

                            case "Turn Robot Right":
                                robot.Turn(-90.0f);
                                break;

                            case "Sidestep Robot Left":
                                robot.Strafe(true);
                                break;

                            case "Sidestep Robot Right":
                                robot.Strafe(false);
                                break;

                            case "Emergency Stop":
                                robot.emergencyStop();
                                break;

                            case "Stop Walking":
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