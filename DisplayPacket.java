import java.io.*;

public class DisplayPacket implements Serializable{
  int player_x;
  int player_y;
  Map game_map;
  Player[] players;
  Inventory inventory;
  Projectile[] projectiles;

  public DisplayPacket(int playerx, int playery, Map gamemap, Player[] plurers, Inventory inv, Projectile[] projs){
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