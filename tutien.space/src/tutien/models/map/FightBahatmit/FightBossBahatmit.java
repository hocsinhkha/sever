/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tutien.models.map.FightBahatmit;

import barcoll.consts.ConstPlayer;
import barcoll.models.npc.NpcFactory;
import tutien.models.player.Player;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import tutien.services.func.ChangeMapService;

/**
 *
 * @author HP
 */
public class FightBossBahatmit {
    public final byte POINT_MAX = 5;

    public int pointbahatmit = 0;
    private Player player;

    public FightBossBahatmit(Player player){
        this.player = player;
    }

    public void changePoint(byte pointAdd) {
        if (player.pkbahatmit.zone.map.mapId==112) {
            pointbahatmit += pointAdd;
            if (pointbahatmit == POINT_MAX) {
                Service.getInstance().sendThongBao(player.pkbahatmit, "Chúc mừng bạn đã chiến thắng các đệ tử của bà hạt mít");
                ChangeMapService.gI().changeMapBaHatMit(player.pkbahatmit, 112, -1, 217,408);
                PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                NpcFactory.timebahatmit=0;
            }
        }
    }

    public void clear() {
        this.pointbahatmit=0;
    }
}

