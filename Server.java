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
    //private Graphics g;

    //initialize sockets
    private ServerSocket[] servers = new ServerSocket[4];
    private Socket[] clients = new Socket[4];
    private ConnectionWaiter[] waiters = new ConnectionWaiter[4];
    private boolean[] connection_status = {false,false,false,false};

    private ObjectInputStream[] inputs = new ObjectInputStream[4];
    private ObjectOutputStream[] outputs = new ObjectOutputStream[4];
    public InputPacket[] player_inputs = {null,null,null,null};
    private Player[] player_objects = {null,null,null,null};
    private boolean newline = false;

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
        //setFocusable(true);
        // starts server and waits for a connection
        
        try
        {
            for(int i = 0; i<4; i++){
                servers[i] = new ServerSocket(0);
                System.out.println(servers[i].getLocalPort());
            }
            //server = new ServerSocket(port);
            System.out.println("Server started");
            for (int i = 0; i < 4; i++) {
                waiters[i] = new ConnectionWaiter(servers[i]);
                waiters[i].start();
            }
            //waiter = new ConnectionWaiter(server);
            //waiter.start();

 
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
                //server operations
               for(int i = 0; i<4; i++){
                if(connection_status[i] == false){
                    clients[i] = waiters[i].getSocket();
                    if(clients[i] != null){
                        connection_status[i] = true;
                        player_objects[i] = new Player(300, 400);
                        try {
                            inputs[i] = new ObjectInputStream(clients[i].getInputStream());
                            outputs[i] = new ObjectOutputStream(clients[i].getOutputStream());
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    } 
                }
                if(connection_status[i] == true){
                    checkInput(i);
                }
               }
               //update the server state
                for(int i = 0; i < 4; i++){
                    if(player_objects[i] != null){
                        player_objects[i].checkInput(player_inputs[i].keys);
                        player_objects[i].updatePos();
                    }
                }
               //draw
               repaint();

               newline = false;
               for(int i = 0; i<4; i++){
                if(player_inputs[i] != null){
                    if(player_inputs[i].keys[KeyEvent.VK_LEFT]){
                        System.out.println(i);
                    }
                    //System.out.println(Arrays.toString(player_inputs[i].keys));
                    
                    newline = true;
                }
               }
               
            }
        };

        timer = new javax.swing.Timer(20, taskPerformer);
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public void paint(Graphics g){
        g.setColor(new Color(255,255,255));
        g.fillRect(0, 0, 9000, 9000);
        for(int i = 0; i < 4; i++){
            if(player_objects[i] != null){
                player_objects[i].draw(g);
            }
        }
    }

    public void checkInput(int id){
        try {
            try{
                boolean[] temp = {false};
                InputPacket line = null;
                line = (InputPacket)inputs[id].readObject();
                player_inputs[id] = line;
                //System.out.println(line);
 
            }
            catch(IOException i)
            {
                    connection_status[id] = false;
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
        catch(NullPointerException e){
            connection_status[id] = false;
            player_inputs[id] = null;
            System.out.println("Client was disconnected");
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