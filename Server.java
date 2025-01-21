//this file is to overall run the game, basically act as the server. I'm thinking that the host would basically act as a client
//but connected to their own device, this way I can use the exact same code for both client and host.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;

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
    private ConnectionChecker[] checkers = new ConnectionChecker[4];
    private boolean[] connection_status = {false,false,false,false};
    private boolean[] made_teamDecision = {false, false, false,false};

    private boolean[] send_turn = {true, true, true, true};
    private ObjectInputStream[] inputs = new ObjectInputStream[4];
    private ObjectOutputStream[] outputs = new ObjectOutputStream[4];
    public InputPacket[] player_inputs = {null,null,null,null};
    private Player[] player_objects = {null,null,null,null};
    private Inventory[] player_inventorys = {null, null, null, null};
    
    private ArrayList<Projectile> projectile_list = new ArrayList<Projectile>();
    private ArrayList<Integer> delete_projectiles = new ArrayList<Integer>();
    boolean deleting_projectiles = false;

    javax.swing.Timer timer;
    ServerConnection ref = this;

    Map map = new Map();
    int[] blu_spawn = {300, 400};
    int[] red_spawn = {300, 400};
    Match match = new Match(blu_spawn, red_spawn, player_objects);
    public static final int screenwidth = 800;
    public static final int screenheight = 780;
    // constructor with port
    public ServerConnection(int port)
    {
        setPreferredSize(new Dimension(screenwidth, screenheight));
        //setFocusable(true);
        // starts server and waits for a connection
        try
        {
            for(int i = 0; i<4; i++){
                servers[i] = new ServerSocket(0);
                servers[i].setPerformancePreferences(0, 1, 0);
                System.out.println(servers[i].getLocalPort());
            }
            //server = new ServerSocket(port);
            System.out.println(InetAddress.getLocalHost());
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
                        //player_objects[i] = new Player(300, 400, true);
                        try {
                            inputs[i] = new ObjectInputStream(clients[i].getInputStream());
                            outputs[i] = new ObjectOutputStream(clients[i].getOutputStream());
                            outputs[i].flush();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        //checkInput(i);
                        checkers[i] = new ConnectionChecker(i, ref);
                        checkers[i].start();
                    } 
                }
                if(connection_status[i] == true){
                    //System.out.println(player_objects[i].x);
                    //player_objects[i].x += 1;
                } 
               }
               //update the server state
               match.update();
                for(int i = 0; i < 4; i++){
                    if(player_objects[i] != null){
                        if(player_objects[i].health <= 0){
                            player_objects[i].dead = true;
                        }
                        if(player_inventorys[i] != null){
                            player_inventorys[i].updateSlots();
                            player_objects[i].held_item = player_inventorys[i].hotbar[player_objects[i].holding_slot];
                        }
                        if(player_inputs[i] != null){
                            if(player_inputs[i].keys != null){
                                if(!player_objects[i].dead){
                                    player_objects[i].checkInput(player_inputs[i].keys);
                                    player_objects[i].updateLookPos(player_inputs[i].mousex_offset, player_inputs[i].mousey_offset);
                                }
                            }
                            if(player_inputs[i].inventory_swap_request != null){
                                if(player_inputs[i].inventory_swap_request[0] != -1 || player_inputs[i].inventory_swap_request[1] != -1){
                                    player_inventorys[i].acknowledge_swap = true;
                                }
                                if(player_inputs[i].inventory_swap_request[0] == -1 && player_inputs[i].inventory_swap_request[1] == -1){
                                    player_inventorys[i].acknowledge_swap = false;
                                }
                                player_inventorys[i].swapSlots(player_inputs[i].inventory_swap_request);
                            }
                            if(player_inputs[i].mouse_pressed){
                                if(player_inventorys[i].hotbar[player_objects[i].holding_slot] != null){
                                    if(player_inventorys[i].hotbar[player_objects[i].holding_slot].cooldown_counter == player_inventorys[i].hotbar[player_objects[i].holding_slot].cooldown){
                                        if(player_inventorys[i].hotbar[player_objects[i].holding_slot].id.equals("pistol") || player_inventorys[i].hotbar[player_objects[i].holding_slot].id.equals("autogun")){

                                            projectile_list.add(new Projectile(player_objects[i].x, player_objects[i].y, (float)(Math.atan2((float)player_inputs[i].mousey_offset,(float)player_inputs[i].mousex_offset) + Math.PI), "bullet_small", player_objects[i]));
                                            //System.out.println("poundington");
                                        }
                                        if(player_inventorys[i].hotbar[player_objects[i].holding_slot].id.equals("drill")){
                                            projectile_list.add(new Projectile(player_objects[i].x, player_objects[i].y, (float)(Math.atan2((float)player_inputs[i].mousey_offset,(float)player_inputs[i].mousex_offset) + Math.PI), "laser", player_objects[i]));
                                        }
                                        if(player_inventorys[i].hotbar[player_objects[i].holding_slot].id.equals("fabricator")){
                                            projectile_list.add(new Projectile(player_objects[i].x, player_objects[i].y, (float)(Math.atan2((float)player_inputs[i].mousey_offset,(float)player_inputs[i].mousex_offset) + Math.PI), "grenade", player_objects[i]));
                                        }
                                        player_inventorys[i].hotbar[player_objects[i].holding_slot].cooldown_counter = 0;
                                        player_inventorys[i].hotbar[player_objects[i].holding_slot].uses_counter++;
                                    }
                                }
                            }
                            
                            player_objects[i].checkMapCollision(map);
                            player_objects[i].updatePos();
                            //System.out.println(player_objects[i].x);
                        }
                    }
                } 
                int index = 0;
                for(int i = 0; i < projectile_list.size(); i++){
                    projectile_list.get(i).update();
                    if(projectile_list.get(i).checkPlayerCollision(player_objects) != null){
                        //System.out.println("Collide player");
                        delete_projectiles.add(i);
                    }
                    else if(projectile_list.get(i).checkMapCollision(map) != null){
                        //System.out.println("Collide terrain");
                        delete_projectiles.add(i);
                    }
                    else if(projectile_list.get(i).lifetime == 0){
                        delete_projectiles.add(i);
                    }
                    index += 1;
                }

                deleting_projectiles = true;
                map.update();
                deleting_projectiles = false;

                Collections.reverse(delete_projectiles);
                deleting_projectiles = true;
                for(int del : delete_projectiles){
                    projectile_list.remove(del);
                }
                deleting_projectiles = false;
                delete_projectiles.clear();
               //draw
               //repaint();
               
            }
        };
        load();
        timer = new javax.swing.Timer(10, taskPerformer);
        timer.setRepeats(true);
        timer.start();
    }

    public void load(){
        try (FileInputStream file = new FileInputStream("maps/Map1");
        ObjectInputStream inputStream = new ObjectInputStream(file);){// stream the plot data to a file
            this.map = (Map)inputStream.readObject();// write it 
        } catch (Exception e) {
            System.out.println(e);
        }
      }

    @Override
    public void paint(Graphics g){
        g.setColor(new Color(255,255,255));
        g.fillRect(0, 0, 1000, 1000);
        for(Projectile bullet: projectile_list){
            bullet.draw(g);
        }
        for(int i = 0; i < 4; i++){
            if(player_inputs[i] != null){
                player_objects[i].draw(g);
                if(player_inputs[i].mouse_pressed){
                    g.setColor(Color.RED);
                }
                else{
                    g.setColor(Color.BLUE);
                }
                g.drawLine(player_objects[i].x, player_objects[i].y, player_objects[i].lookat_x, player_objects[i].lookat_y);
                //System.out.println(Math.round(Math.toDegrees(Math.atan2((float)player_inputs[i].mousey_offset,(float)player_inputs[i].mousex_offset))));
            }
        }
        map.draw(g);
    }

    public void checkInput(int id){
        
            try{
                boolean[] temp = {false};
                InputPacket line;
                line = (InputPacket)inputs[id].readObject();
                
                    if(player_objects[id] == null){
                        System.out.println(line.decision_made);
                        if(line.decision_made == true){
                            if(line.red_team){
                                player_objects[id] = new Player(red_spawn[0], red_spawn[1], line.red_team);
                            }
                            else{
                                player_objects[id] = new Player(blu_spawn[0], blu_spawn[1], line.red_team);
                            }
                            player_inventorys[id] = new Inventory();
                            player_inventorys[id].addItem("pistol");
                            player_inventorys[id].addItem("autogun");
                            player_inventorys[id].addItem("drill");
                            player_inventorys[id].addItem("fabricator");
                        }
                    }
                        player_inputs[id] = line;
                        send_turn[id] = true;
                    
                
                
                //System.out.println(line);
                //player_inputs[id] = line;
                //System.out.println(line);
 
            }
            catch(IOException i)
            {
                    checkers[id].running = false;
                    System.out.println("SEIU");
                    //socket.close();
                    //in.close(); 
            }
            catch(ClassNotFoundException e){
                System.out.println("Fucker");
            }
        catch(NullPointerException e){
            connection_status[id] = false;
            player_inputs[id] = null;
            System.out.println("Client was disconnected");
        }
    }

    public void sendClientDisplay(int id){
        if(send_turn[id]){
            
            Player[] sendy = {null, null, null, null};
            for(int i = 0; i < 4; i++){
                if(player_objects[i] != null){
                try{
                sendy[i] = (Player)player_objects[i].clone();
                }
                catch(Exception e){
                    System.out.println("299:" + e);
                }
                }
            }

            
            Tile[] cendy = new Tile[map.build_map.size()];
            for(int i = 0; i < map.build_map.size(); i++){
                try {
                    cendy[i] = (Tile)map.build_map.get(i).clone();
                } catch (Exception e) {
                    System.out.println("310:" + e);
                }
            }
            
            /*
            map.build_map = (ArrayList<Tile>)map.build_map.clone();
            Map wth = map;
            try{
            wth = (Map)map.clone();
            }
            catch(CloneNotSupportedException t){

            }
            */
            
            Projectile[] bendy = new Projectile[projectile_list.size()];
            
            for(int i = 0; i < projectile_list.size(); i++){
                try{
                bendy[i] = (Projectile)projectile_list.get(i).clone();
                }
                catch(Exception e){
                    System.out.println("332:" + e);
                }
            }
            

            DisplayPacket send = null;
            try{
                player_inventorys[id].hotbar = player_inventorys[id].hotbar.clone();
                if(player_objects[id] != null){
                    send = new DisplayPacket(player_objects[id].x, player_objects[id].y, cendy, sendy, (Inventory)player_inventorys[id].clone(), bendy);
                }
                else{
                    send = new DisplayPacket(map.default_camx, map.default_camy, cendy, sendy, (Inventory)player_inventorys[id].clone(), bendy);
                }
            }
            catch(Exception e){
                System.out.println("348:" + e);
            }

            
            try{
            outputs[id].writeObject(send);
            outputs[id].flush();
            }
            catch(Exception e){
                System.out.println("357:" + e);
            }
            
                //send_turn[id] = false;
            
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

class ConnectionChecker extends Thread{
    int inte;
    ServerConnection laziness;
    boolean running = true;

    public ConnectionChecker(int bint, ServerConnection lazy){
        this.inte = bint;
        this.laziness = lazy;
    }

    @Override
    public void run(){
        while (running) { 
            this.laziness.sendClientDisplay(this.inte);
            this.laziness.checkInput(this.inte);
        }
    }
}
