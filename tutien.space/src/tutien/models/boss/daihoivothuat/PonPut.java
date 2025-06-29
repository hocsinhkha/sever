package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class PonPut extends BossDHVT {

    public PonPut(Player player) throws Exception {
        super(BossID.PON_PUT, BossesData.PON_PUT);
        this.playerAtt = player;
    }
}