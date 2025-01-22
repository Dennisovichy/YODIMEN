//CLASS THAT MANAGES THE GENERAL STATE OF THE GAME, RESPAWNS AND VICTORY CONDITIONS
class Match{
    int respawn_time = 100; //self explanatory
    int[] bluespawn = new int[2];
    int[] redspawn = new int[2];
    Player[] players;
    int[] respawn_queues = {0,0,0,0};

    int[] bluecorepos; //position of the primary objectives
    int[] redcorepos;
    boolean bluevictory = false; //see if teams have won or not
    boolean redvictory = false;

    Map ref;

    public Match(int[] blue, int[] red, Player[] play, int[] bluecore, int[] redcore, Map map){
        bluespawn = blue;
        redspawn = red;
        players = play;
        bluecorepos = bluecore;
        redcorepos = redcore;
        ref = map;
    }

    public String checkVictory(Map map){ //check if team has won by checking if the other team's shop still exists in the map
        boolean blue_dead = true;
        boolean red_dead = true;
        for(Tile tile: map.build_map){
            if(tile.getHitbox().contains(bluecorepos[0], bluecorepos[1])){
                blue_dead = false;
            }
            if(tile.getHitbox().contains(redcorepos[0], redcorepos[1])){
                red_dead = false;
            }
        }
        if(red_dead){
            return "blue";
        }
        else if(blue_dead){
            return "red";
        }
        return "null";
    }

    public void update(Map map){ //update respawn queues, also check if victory was achieved
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
                players[i].victor = checkVictory(map); //victory boolean is stored in players which is sent over in display packet
                if(players[i].dead){
                    if(respawn_queues[i] < respawn_time){
                        respawn_queues[i] += 1;
                    }
                    else{
                        players[i].health = 100;
                        players[i].dead = false;
                        respawn_queues[i] = 0;
                        if(players[i].red_team){
                            players[i].x = redspawn[0];
                            players[i].y = redspawn[1];
                        }
                        else{
                            players[i].x = bluespawn[0];
                            players[i].y = bluespawn[1];
                        }
                    }
                }
            }
        }
    }


}