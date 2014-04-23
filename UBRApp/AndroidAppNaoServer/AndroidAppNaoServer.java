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
 * @author bShipman
 */
public class AndroidAppNaoServer {

    static String NAO_IPADDRESS;
    static int NAO_PORT;
    
    /**
     * @param args The port to listen for connections on.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException 
    {
        String ipAddress = JOptionPane.showInputDialog(null, "Enter Nao's IP Address:", "Nao Connection.", JOptionPane.INFORMATION_MESSAGE);
        String port = JOptionPane.showInputDialog(null, "Enter Nao's Port Number:", "Nao Connection.", JOptionPane.INFORMATION_MESSAGE);
        String listenPort = JOptionPane.showInputDialog(null, "Enter Server Listening Port:", "Server Connection", JOptionPane.INFORMATION_MESSAGE);
                
        NAO_IPADDRESS = ipAddress;
        NAO_PORT = Integer.parseInt(port);
        
        ServerSocket listener = new ServerSocket(Integer.parseInt(listenPort));

        try {

            boolean socketOpen = true;

            while (socketOpen) {

                // Listen for connections.
                Socket socket = listener.accept();

                try {
                    // Buffered reader.
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String appString = input.readLine();

                    // Temp Output String For reciept verification.
                    System.out.println(appString);

                    if (appString != null){

                        switch(appString){
                            case "Shutdown Nao Server":
                                System.out.println("Server Shutdown Remotely, Goodbuy.");
                                socketOpen = false;             // Shutdown the server remotely.
                                break;

                            case "Stiffen Robot Joints":            
                                Stiffen();
                                break;                                    

                            case "Unstiffen Robot Joints":      
                                Unstiffen();
                                break;                                    

                            case "Stand Robot Up":
                                Stand();
                                break;                                    

                            case "Sit Robot Down":
                                Sit();
                                break;

                            case "Walk Forward":
                                Walk();
                                break;

                            case "Turn Robot Left":
                                Turn(-90.0f);
                                break;

                            case "Turn Robot Right":
                                Turn(90.0f);
                                break;

                            case "Sidestep Robot Left":
                                Strafe(true);
                                break;

                            case "Sidestep Robot Right":
                                Strafe(false);
                                break;

                            case "Stop Walking":
                            default:
                                Stop();
                                break;
                        }

                    }
                }
                finally {
                    socket.close();
                }

            }
        }
        finally {
            listener.close();
        }
    }
    
    /**
     * Stiffen Nao's joints for movement.
     */
    public static void Stiffen(){
        System.out.println("Stiffening Joints");
    }
    
    /**
     * Unsitffen Nao's joints.
     */
    public static void Unstiffen(){
        System.out.println("Relaxing Joints");
    }
    
    /**
     * Have Nao walk forward.
     */
    public static void Walk(){
        System.out.println("Walking Forward");
    }
    
    /**
     * Have Nao turn.
     * @param turnAmount Degrees to turn from current heading.
     */
    public static void Turn(float turnAmount){
        System.out.println("Turning " + turnAmount + " degrees.");
    }
    
    /**
     * Have Nao Step left or right.
     * @param left True to move left false to move right.
     */
    public static void Strafe(boolean left){
        System.out.println("Stepping to the " + (left ? "left" : "right"));
    }
    
    /**
     * Stand Nao up.
     */
    public static void Stand(){
        System.out.println("Standing Up.");
    }
    
    /**
     * Sit Nao down.
     */
    public static void Sit(){
        System.out.println("Sitting Down.");
    }
    
    public static void Stop(){
        System.out.println("Stopping Movement.");
    }
}