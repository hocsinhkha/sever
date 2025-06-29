package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.player.Player;;

/**
 * @author BTH sieu cap vippr0 
 */
public class SoiHecQuyn extends BossDHVT {
    public SoiHecQuyn(Player player) throws Exception {
        super(BossID.SOI_HEC_QUYN, BossesData.SOI_HEC_QUYN);
        this.playerAtt = player;
    }
}
