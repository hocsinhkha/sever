package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class Xinbato extends BossDHVT {

    public Xinbato(Player player) throws Exception {
        super(BossID.XINBATO, BossesData.XINBATO);
        this.playerAtt = player;
    }
}