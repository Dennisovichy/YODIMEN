import java.io.*;

public class DisplayPacket implements Serializable{
  int player_x;
  int player_y;
  Map game_map;
  Player[] players;


  public DisplayPacket(int playerx, int playery, Map gamemap, Player[] plurers){
    this.player_x = playerx;
    this.player_y = playery;
    this.game_map = gamemap;
    this.players = plurers;
  }


  @Override
  public String toString(){
    return "working on the fighting side";
  }
}