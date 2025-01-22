//CLASS THAT IS SENT OVER THE NETWORK SERIALIZED TO EASILY CONVEY DISPLAY INFO TO CLIENTS
import java.io.*;

public class DisplayPacket implements Serializable{
  int player_x; //camera position
  int player_y;
  Tile[] game_map; //self explanatory
  Player[] players;
  Inventory inventory;
  Projectile[] projectiles;

  public DisplayPacket(int playerx, int playery, Tile[] gamemap, Player[] plurers, Inventory inv, Projectile[] projs){
    this.player_x = playerx;
    this.player_y = playery;
    this.game_map = gamemap;
    this.players = plurers;
    this.inventory = inv;
    this.projectiles = projs;
  }


  @Override
  public String toString(){
    return "working on the fighting side";
  }
}