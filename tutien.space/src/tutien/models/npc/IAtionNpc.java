package barcoll.models.npc;

import tutien.models.player.Player;

/**
 *
 *@Stole By barcoll
 *
 */
public interface IAtionNpc {
    
    void openBaseMenu(Player player);

    void confirmMenu(Player player, int select) throws Exception;


}