package tutien.models.boss.list_boss.Mabu12h;

import Amodels.boss.Boss;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.server.Manager;
import barcoll.services.Service;
import tutien.utils.Util;

import java.util.Random;

public class Yacon extends Boss {

    public Yacon() throws Exception {
        super(Util.randomBossId(), BossesData.YACON);
    }

    @Override
    public void reward(Player plKill) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length);
        byte randomc12 = (byte) new Random().nextInt(Manager.itemDC12.length -1);

        if (Util.isTrue(1, 130)) {
            if (Util.isTrue(1, 5)) {
                Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else
        if (Util.isTrue(20, 100)) {
            Service.getInstance().dropItemMap(this.zone,new ItemMap (Util.RaitiDoc12(zone, Manager.itemDC12[randomc12], 1, this.location.x, this.location.y, plKill.id)));
            return;
        }
        else {
            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, this.location.y, plKill.id));
        }
        plKill.fightMabu.changePoint((byte) 20);
    }

//    @Override
//    public void active() {
//        super.active(); //To change body of generated methods, choose Tools | Templates.
//        if (Util.canDoWithTime(st, 300000)) {
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//        st = System.currentTimeMillis();
//    }
//    private long st;

}





















