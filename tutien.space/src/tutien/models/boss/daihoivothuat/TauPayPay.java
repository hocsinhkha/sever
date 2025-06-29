package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.player.Player;

/**
 * @author BTH sieu cap vippr0 
 */
public class TauPayPay extends BossDHVT {

    public TauPayPay(Player player) throws Exception {
        super(BossID.TAU_PAY_PAY, BossesData.TAU_PAY_PAY);
        this.playerAtt = player;
    }
}