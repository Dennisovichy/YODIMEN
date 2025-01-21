
class Match{
    int respawn_time = 100;
    int[] bluespawn = new int[2];
    int[] redspawn = new int[2];
    Player[] players;
    int[] respawn_queues = {0,0,0,0};

    int[] bluecorepos;
    int[] redcorepos;
    boolean bluevictory = false;
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

    public String checkVictory(Map map){
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

    public void update(Map map){
        for(int i = 0; i < players.length; i++){
            if(players[i] != null){
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
        String check = checkVictory(map);
        if(check.equals("blue")){
            System.out.println("Blue victory");
        }
        if(check.equals("red")){
            System.out.println("Red victory");
        }
    }


}