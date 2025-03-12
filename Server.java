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
    private ServerSocket[] servers = new ServerSocket[4]; //SERVERSOCKETS
    private Socket[] clients = new Socket[4]; //SOCKET CONNECTIONS
    private ConnectionWaiter[] waiters = new ConnectionWaiter[4]; //THREADS THAT WAIT FOR CLIENTS TO CONNECT TO SERVERSOCKETS
    private ConnectionChecker[] checkers = new ConnectionChecker[4]; //THREADS THAT SPAM DISPLAYPACKETS TO THE CLIENT
    private boolean[] connection_status = {false,false,false,false}; //CONNECTED?
    private boolean[] made_teamDecision = {false, false, false,false};

    private boolean[] send_turn = {true, true, true, true}; //USELESS
    private ObjectInputStream[] inputs = new ObjectInputStream[4]; //INPUTS FROM CLIENT
    private ObjectOutputStream[] outputs = new ObjectOutputStream[4]; //OUTPUTS TO CLIENT
    public InputPacket[] player_inputs = {null,null,null,null}; //STORE THE PLAYER INPUTS FOR USE
    public ArrayList<boolean[]> just_pressed = new ArrayList<>(); //BUTTONS THAT WERE JUST PRESSED, FOR THE SHOP
    public ArrayList<boolean[]> can_pressAgain = new ArrayList<>();;
    private Player[] player_objects = {null,null,null,null}; //PLAYER OBJECT STORAGE
    private Inventory[] player_inventorys = {null, null, null, null}; //PLAYER INVENTORY STORAGE
    private int[][] last_swap_request = {{-1,-1},{-1,-1},{-1,-1},{-1,-1}}; 
    
    private ArrayList<Projectile> projectile_list = new ArrayList<Projectile>(); 
    private ArrayList<Integer> delete_projectiles = new ArrayList<Integer>(); //STORE THE PROJECTILES THAT SHOULD BE DELETED
    
    int money_counter = 0; //GENERATE MONEY
    int money_tick = 25;


    javax.swing.Timer timer;
    ServerConnection ref = this;

    Map map = new Map(); //SPAWN THE MAP AND ASSIGN IMPORTANT POINTS 
    int[] blu_spawn = {-1300, 658};
    int[] red_spawn = {2140, 739};
    int[] blu_core = {-1257, 693};
    int[] red_core = {2102, 777};
    Match match = new Match(blu_spawn, red_spawn, player_objects, blu_core, red_core, map); //INIT THE MATCH
    public static final int screenwidth = 0;
    public static final int screenheight = 0;
    // constructor with port
    public ServerConnection(int port) 
    {
        for(int i = 0; i < 4; i++){ //INITIALIZE JUST PRESSED ARRAYS
            boolean[] temp = new boolean[KeyEvent.KEY_LAST + 1];
            just_pressed.add(temp);
            boolean[] hemp = new boolean[KeyEvent.KEY_LAST + 1];
            for(int x = 0; x<hemp.length; x++){
                hemp[x] = true;
            }
            can_pressAgain.add(hemp);
        }
        //setPreferredSize(new Dimension(screenwidth, screenheight)); //FILL THE ENTIRE SCREEN
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setPreferredSize(new Dimension(screen.width, screen.height));
        //setFocusable(true);
        // starts server and waits for a connection
        try
        {
            for(int i = 0; i<4; i++){
                servers[i] = new ServerSocket(0); //START THE SERVER SOCKETS, ADJUST SETTINGS
                servers[i].setPerformancePreferences(0, 1, 0);
                servers[i].setReceiveBufferSize(100000);
                System.out.println(servers[i].getLocalPort()); //PRINT OUT PORTS FOR CLIENT CONNECTIONS
            }
            //server = new ServerSocket(port);
            System.out.println(InetAddress.getLocalHost()); //PRINT IP ADDRESS FOR CONNECTIONS
            System.out.println("Server started");
            for (int i = 0; i < 4; i++) { //START WAITING FOR THE CONNECTIONS
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
               for(int i = 0; i<4; i++){ //CHECK THE CONNECTIONS OF EACH OF THE SPOTS
                if(connection_status[i] == false){
                    clients[i] = waiters[i].getSocket(); //CHECK IF SOCKET WAS FOUND
                    if(clients[i] != null){
                        connection_status[i] = true; //CONNECTED
                        //player_objects[i] = new Player(300, 400, true);
                        try {
                            inputs[i] = new ObjectInputStream(clients[i].getInputStream()); //MAKE THE INPUT AND OUTPUT STREAMS
                            outputs[i] = new ObjectOutputStream(clients[i].getOutputStream());
                            outputs[i].flush();
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                        //checkInput(i);
                        checkers[i] = new ConnectionChecker(i, ref); ///START THE THREAD THAT WILL SPAM READ THE INPUTS AND OUTPUTS
                        checkers[i].start();
                    } 
                }
                if(connection_status[i] == true){
                    //System.out.println(player_objects[i].x);
                    //player_objects[i].x += 1;
                } 
               }
               //update the server state
               money_counter++; 
               try{
               match.update((Map)map.clone()); //UPDATE THE MATCH MAP BECAUSE OF COURSE
               }
               catch(Exception br){

               }
                for(int i = 0; i < 4; i++){
                    if(player_objects[i] != null){
                        if(money_counter == money_tick){ //MAKE MONEY
                            player_objects[i].money++;
                        }
                        if(player_objects[i].y > 5000){ //DIE IN THE VOID
                            player_objects[i].health = 0;
                        }
                        if(player_objects[i].health <= 0){ //DEAD IF YOURE DEAD
                            player_objects[i].dead = true;
                        }
                        if(player_inventorys[i] != null){ //UPDATE YOUR SLOTS AND MAKE PLAYER HOLD WHAT HE'S HOLDING
                            player_inventorys[i].updateSlots();
                            player_objects[i].held_item = player_inventorys[i].hotbar[player_objects[i].holding_slot];
                        }
                        if(player_objects[i].on_shop){ //INTERACTIONS WITH THE SHOP
                            if(just_pressed.get(i)[KeyEvent.VK_T]){
                                if(player_objects[i].money >= 200){
                                    player_objects[i].money -= 200;
                                    player_inventorys[i].addItem("autogun");
                                }
                            }
                            if(just_pressed.get(i)[KeyEvent.VK_Y]){
                                if(player_objects[i].money >= 200){
                                    player_objects[i].money -= 200;
                                    player_inventorys[i].addItem("drill");
                                }
                            }
                            if(just_pressed.get(i)[KeyEvent.VK_U]){
                                if(player_objects[i].money >= 200){
                                    player_objects[i].money -= 200;
                                    player_inventorys[i].addItem("fabricator");
                                }
                            }
                        }
                        if(player_inputs[i] != null){ //FIGURE OUT THE KEYS THAT WERE JUST PRESSED 
                            for(int x = 0; x < KeyEvent.KEY_LAST+1; x++){
                                if(player_inputs[i].keys[x]){
                                    if(can_pressAgain.get(i)[x]){
                                        just_pressed.get(i)[x] = true;
                                        can_pressAgain.get(i)[x] = false;
                                    }
                                    else{
                                        just_pressed.get(i)[x] = false;
                                    }
                                }
                                else{
                                    can_pressAgain.get(i)[x] = true;
                                    just_pressed.get(i)[x] = false;
                                }
                            }
                            if(player_inputs[i].keys != null){ //CHECK PLAYER'S INPUTS AND UPDATE LOOKING ANGLE
                                if(!player_objects[i].dead){
                                    player_objects[i].checkInput(player_inputs[i].keys);
                                    player_objects[i].updateLookPos(player_inputs[i].mousex_offset, player_inputs[i].mousey_offset);
                                }
                            }
                            if(player_inputs[i].inventory_swap_request != null){ //SWAP INVENTORY SLOTS IF REQUESTED
                                if(player_inputs[i].inventory_swap_request != (last_swap_request[i])){
                                    player_inventorys[i].swapSlots(player_inputs[i].inventory_swap_request);
                                    player_inventorys[i].acknowledge_swap = true;
                                }
                                if(player_inputs[i].inventory_swap_request[0] == -1 && player_inputs[i].inventory_swap_request[1] == -1){
                                    player_inventorys[i].acknowledge_swap = false;
                                }
                                last_swap_request[i] = player_inputs[i].inventory_swap_request;
                            }
                            if(player_inputs[i].mouse_pressed){ //FIRE GUN
                                if(!player_objects[i].dead){ //CANT SHOOT IF YOURE DEAD
                                    if(player_inventorys[i].hotbar[player_objects[i].holding_slot] != null){ 
                                        if(player_inventorys[i].hotbar[player_objects[i].holding_slot].cooldown_counter == player_inventorys[i].hotbar[player_objects[i].holding_slot].cooldown){ //OFF COOLDOWN
                                            //DETERMINE WHAT ITEM IS BEING HELD, AND WWHAT PROJECTILE TO SPPAWN FROM THAT ITEM
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
                                            //WAIT FOR COOLDOWN AGAIN
                                            player_inventorys[i].hotbar[player_objects[i].holding_slot].cooldown_counter = 0;
                                            player_inventorys[i].hotbar[player_objects[i].holding_slot].uses_counter++;
                                        }
                                    }
                                }
                            }
                            //UPDATE PLAYER POSITION
                            player_objects[i].checkMapCollision(map);
                            player_objects[i].updatePos();
                            //System.out.println(player_objects[i].x);
                        }
                    }
                } 
                if(money_counter == money_tick){ //YOU MAADE THE MONEY, NOW WAIT AGAIN
                    money_counter = 0;
                }
                
                for(int i = 0; i < projectile_list.size(); i++){ //UPDATE PROJECTILES
                    projectile_list.get(i).update();
                    if(projectile_list.get(i).checkPlayerCollision(player_objects) != null){
                        //System.out.println("Collide player");
                        delete_projectiles.add(i); //ADD IT TO DESTROY LIST
                    }
                    else if(projectile_list.get(i).checkMapCollision(map) != null){
                        //System.out.println("Collide terrain");
                        delete_projectiles.add(i); //DESTROY
                    }
                    else if(projectile_list.get(i).lifetime == 0){
                        delete_projectiles.add(i); //EXPIRE, DESTROYED
                    }
                    
                }

            
                map.update(); //UPDATE THE MAP (REMOVE DESTROYED TILES)

                Collections.reverse(delete_projectiles); //DELETE FROM RIGHT TO LEFT TO AVOID INDEX PROBLEMS
               
                for(int del : delete_projectiles){ //DELETE
                    if(projectile_list.get(del).id.equals("grenade")){ //IF IT'S A GRENADE, SPAWN MORE PROJECTILES THAT REPRESENT THE EXPLOSION
                        for(int i = 0; i < 40; i++){
                            projectile_list.add(new Projectile(projectile_list.get(del).display_x, projectile_list.get(del).display_y, (float)(Math.toRadians(i*(360/40))), "explosion", null));
                        }
                    }
                }
                for(int del : delete_projectiles){ //NVM, ACTUALLY DELETE IT HERE
                    projectile_list.remove(del);
                }
                
                delete_projectiles.clear(); 
               //draw
               repaint();
               
            }
        };
        load(); //LOADD MAP
        timer = new javax.swing.Timer(10, taskPerformer); //TIMER FOR WHEN TO UPDATE THE GAME
        timer.setRepeats(true);
        timer.start();
    }

    public void load(){
        try (FileInputStream file = new FileInputStream("maps/Map1"); //LOAD MAP FROM A FILE
        ObjectInputStream inputStream = new ObjectInputStream(file);){// stream the plot data FROM a file
            this.map = (Map)inputStream.readObject();// wREADD it 
        } catch (Exception e) {
            System.out.println(e);
        }
      }

    @Override
    public void paint(Graphics g){ //ANTIQUATED
        g.setColor(new Color(255,255,255));
        g.fillRect(0, 0, 10000, 10000);
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

    public void checkInput(int id){ //CHECK THE INPUT OF A PARTICULAR PLAYER
        
            try{
                boolean[] temp = {false};
                InputPacket line;
                line = (InputPacket)inputs[id].readObject(); //GET THE INPUT PACKET
                
                    if(player_objects[id] == null){ //LET PLAYER MAKE TEAM DECISION
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
                            //player_inventorys[id].addItem("autogun");
                            //player_inventorys[id].addItem("drill");
                            //player_inventorys[id].addItem("fabricator");
                        }
                    }
                        player_inputs[id] = line; //STORE THE KEYBOARD INPUTS
                        send_turn[id] = true;
                    
                
                
                //System.out.println(line);
                //player_inputs[id] = line;
                //System.out.println(line);
 
            }
            catch(IOException ip)//THIS EXCEPTION IS THROWN ON PLAYER DISCONNECT. RESET EVERYTHING TO PREPARE FOR A NEW PLAYER CONNECTION
            {
                    checkers[id].running = false;
                    checkers[id] = null;
                    System.out.println("SEIU");
                    connection_status[id] = false;
                    player_objects[id] = null;
                    player_inputs[id] = null;
                    waiters[id] = new ConnectionWaiter(servers[id]);
                    waiters[id].start();
                    //socket.close();
                    //in.close(); 
            }
            catch(ClassNotFoundException e){
                System.out.println("ucker");
            }
        catch(NullPointerException e){
            connection_status[id] = false;
            player_inputs[id] = null;
            System.out.println("Client was disconnected");
        }
    }

    public void sendClientDisplay(int id){ //SENDING THE DISPLAY INFO TO THE CLIENT
        if(send_turn[id]){
            
            Player[] sendy = {null, null, null, null}; //CLONE EACH OF THE PLAYERS BECAUSE OTHERWISE IT JUST DOESN'T UPDATE
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

            
            Tile[] cendy = new Tile[map.build_map.size()]; //CLONE EVERY SINGLE TILE
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
            
            Projectile[] bendy = new Projectile[projectile_list.size()]; //CLONE EVERY SINGHLE PROJECTILE
            
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
                player_inventorys[id].hotbar = player_inventorys[id].hotbar.clone(); //CLONE THE HOTBAR
                if(player_objects[id] != null){ //PLAYER MADE TEAM CHOICE, PLAYER EXISTS. SEND THEIR POSITION
                    send = new DisplayPacket(player_objects[id].x, player_objects[id].y, cendy, sendy, (Inventory)player_inventorys[id].clone(), bendy);
                }
                else{ //PLAYER DID NOT MAKE A CHOICE YET, THEY DON'T EXIST. SEND DEFAULT CAMERA POSITION
                    send = new DisplayPacket(map.default_camx, map.default_camy, cendy, sendy, (Inventory)player_inventorys[id].clone(), bendy);
                }
            }
            catch(Exception e){
                System.out.println("348:" + e);
            }

            
            try{
            outputs[id].writeObject(send); //WRITING TO THE OUTPUT STREAAM
            //outputs[id].flush();
            }
            catch(Exception e){
                System.out.println("357:" + e);
            }
            
                //send_turn[id] = false;
            
        }
    }
}

class ConnectionWaiter extends Thread{

    private ServerSocket server; //SERVERSOCKET THAT WAITS
    private boolean connected = false;
    private Socket client; //CLIENT THAT GETS AACCEPTED

    public ConnectionWaiter(ServerSocket server){
        this.server = server;
    }

    @Override
    public void run(){
        try{
            client = this.server.accept(); //THIS METHOD BLOCKS WHILE WAITING, HENCE THE NEED FOR THE THREAD
            System.out.println("Client accepted");
            connected = true;
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public Socket getSocket(){ //GET SOCKET FROM HERE IF THERE EXISTS ONE
        if(connected){
            return client;
        }
        return null;
    }
}

class ConnectionChecker extends Thread{ //SPAMS RECEIVING AND SENDING PACKETS 
    int inte;
    ServerConnection laziness;
    boolean running = true; //BOOLEAN THAT ALLOWS FOR THIS THREAD TO END

    public ConnectionChecker(int bint, ServerConnection lazy){
        this.inte = bint;
        this.laziness = lazy;
    }

    @Override
    public void run(){
        while (running) { 
            this.laziness.sendClientDisplay(this.inte); //SPAM THE SOCKETS
            this.laziness.checkInput(this.inte); //SPAM THE SOCKETS
        }
    }
}
