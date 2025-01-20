
class Match{
    int respawn_time = 100;
    int[] bluespawn = new int[2];
    int[] redspawn = new int[2];
    Player[] players;
    int[] respawn_queues = {0,0,0,0};

    public Match(int[] blue, int[] red, Player[] play){
        bluespawn = blue;
        redspawn = red;
        players = play;
    }

    public void update(){
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
    }


}