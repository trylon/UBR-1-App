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

/**
 * Android 
 * @author bShipman
 */
public class AndroidAppNaoServer {

    /**
     * @param args The port to listen for connections on.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException 
    {
        if (args.length > 0){            
            ServerSocket listener = new ServerSocket(Integer.parseInt(args[0]));
            
            try {
                while (true) {
                    
                    // Listen for connections.
                    Socket socket = listener.accept();
                    
                    try {
                        // Buffered reader.
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                        String appString = input.readLine();
                        
                        // Temp Output String.
                        System.out.println(appString);
                        
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
        else {
            System.out.println("No Port Provided.");
        }
    }
}
