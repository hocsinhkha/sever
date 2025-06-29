package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.player.Player;
/**
 * @author BTH sieu cap vippr0 
 */
public class LiuLiu extends BossDHVT {

    public LiuLiu(Player player) throws Exception {
        super(BossID.LIU_LIU, BossesData.LIU_LIU);
        this.playerAtt = player;
    }
}