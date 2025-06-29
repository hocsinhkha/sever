package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.player.Player;
/**
 * @author BTH sieu cap vippr0 
 */
public class ChaPa extends BossDHVT {

    public ChaPa(Player player) throws Exception {
        super(BossID.CHA_PA, BossesData.CHA_PA);
        this.playerAtt = player;
    }
}