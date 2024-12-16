//this file is to overall run the game, basically act as the server. I'm thinking that the host would basically act as a client
//but connected to their own device, this way I can use the exact same code for both client and host.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Server extends JFrame{
 ServerConnection game= new ServerConnection(1100);
  
  public Server() {
    super("Yodimen Server");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    add(game);
    pack();
    setVisible(true);
  }
    
  public static void main(String[] arguments) {
    new Server();
  }
}

class ServerConnection extends JPanel
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private ObjectInputStream in       =  null;
    ConnectionWaiter waiter;
    boolean connected = false;
    javax.swing.Timer timer;
 
    // constructor with port
    public ServerConnection(int port)
    {
        setPreferredSize(new Dimension(800, 780));
        setFocusable(true);
        // starts server and waits for a connection
        
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
            waiter = new ConnectionWaiter(server);
            waiter.start();

 
            System.out.println("Waiting for a client ...");
        }
        catch(IOException i)
        {
            System.out.println("Closing connection");
            System.out.println(i);
        }

        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //...Perform a task...
                if(connected == false){
                    socket = waiter.getSocket();
                    if(socket != null){

                        connected = true;
                        try{
                            in = new ObjectInputStream(socket.getInputStream());
                        }
                        catch(IOException e){
                            System.out.println(e);
                        }
                    }
                }
                else{
                    checkInput();
                }
            }
        };

        timer = new javax.swing.Timer(20, taskPerformer);
        timer.setRepeats(true);
        timer.start();
    }

    public void checkInput(){
        try {
            try{
                boolean[] temp = {false};
                InputPacket line = new InputPacket(temp);
                line = (InputPacket)in.readObject();
                System.out.println(line);
 
            }
            catch(IOException i)
            {
                    System.out.println("SEIU");
                    socket.close();
                    in.close(); 
            }
            catch(ClassNotFoundException e){
                System.out.println("Fucker");
            }
        } catch (IOException e) {
            System.out.println("Clucker");
        }
    }
}

class ConnectionWaiter extends Thread{

    private ServerSocket server;
    private boolean connected = false;
    private Socket client;

    public ConnectionWaiter(ServerSocket server){
        this.server = server;
    }

    @Override
    public void run(){
        try{
            client = this.server.accept();
            System.out.println("Client accepted");
            connected = true;
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public Socket getSocket(){
        if(connected){
            return client;
        }
        return null;
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