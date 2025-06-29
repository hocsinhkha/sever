package tutien.models.boss.daihoivothuat;

import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import tutien.models.boss.daihoivothuat.BossDHVT;
import tutien.models.player.Player;


/**
 *
 * @author BTH fix
 */
public class Yamcha extends BossDHVT {

    public Yamcha(Player player) throws Exception {
        super(BossID.YAMCHA, BossesData.YAMCHA);
        this.playerAtt = player;
    }
}
