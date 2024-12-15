//this file is to overall run the game, basically act as the server. I'm thinking that the host would basically act as a client
//but connected to their own device, this way I can use the exact same code for both client and host.

import java.io.*;
import java.net.*;

public class Server
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private ObjectInputStream in       =  null;
 
    // constructor with port
    public Server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
 
            // takes input from the client socket
            in = new ObjectInputStream(socket.getInputStream());
 
            boolean[] temp = {false};
            InputPacket line = new InputPacket(temp);
 
            // reads message from client until "Over" is sent
            
            while (line != null)
            {
                try
                {
                    line = (InputPacket)in.readObject();
                    System.out.println(line);
 
                }
                catch(IOException i)
                {
                    System.out.println(i);
                    socket.close();
                    in.close(); 
                    break;
                }
                catch(ClassNotFoundException e){

                }
            }
            
            
            System.out.println("Closing connection");
 
            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println("Closing connection2");
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Server server = new Server(2600);
    }
}

class InputPacket implements Serializable{
  boolean[] keys;

  public InputPacket(boolean[] inputs){
    this.keys = inputs;
  }

  @Override
  public String toString(){
    return "working on the fighting side";
  }
}