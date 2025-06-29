package tutien.models.boss.list_boss.Broly;

import barcoll.consts.ConstPlayer;
import Amodels.boss.Boss;
import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossManager;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import Amodels.boss.TypeAppear;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.server.Manager;
import barcoll.services.EffectSkillService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;
import barcoll.services.PlayerService;
import tutien.services.func.ChangeMapService;
import java.util.Random;

/**
 * @Stole By
 */
public class detu extends Boss {
    public detu(int bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
    }
     @Override
     public void active() {
                if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        Boss boss = BossManager.gI().getBossById(BossID.SUPER);   
              this.moveTo(boss.location.x, boss.location.y);
       
        }
    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}