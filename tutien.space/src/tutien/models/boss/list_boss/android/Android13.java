package tutien.models.boss.list_boss.android;

import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;


public class Android13 extends Boss {

    public Android13() throws Exception {
        super(BossID.ANDROID_13, BossesData.ANDROID_13);
    }

//    @Override
//    public void reward(Player plKill) {
//      plKill.Tamkjlltutien[0] += 100000;
//                plKill.ExpTamkjll += 2000000;
//                Service.gI().sendMoney(plKill);
//                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x + 70, this.location.y, plKill.id));
//                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 10, this.location.x + 0, this.location.y, plKill.id));
//                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 1, this.location.x - 50, this.location.y, plKill.id));
//                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 12, this.location.x - 100, this.location.y, plKill.id));
//                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x - 59, this.location.y, plKill.id));
//                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 10, this.location.x - 55, this.location.y, plKill.id));
//           Service.gI().sendThongBao(plKill, "Bạn nhận đc 100k EXP Tu Tiên và 2 Triệu Tu Vi do kết liễu boss.");
//
//    }
 @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{1142};
        int itemId = itemRan[0];
        if (Util.isTrue(20, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

    @Override
    public void doneChatS() {
        if (this.parentBoss == null) {
            return;
        }
        if (this.parentBoss.bossAppearTogether == null
                || this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.ANDROID_15 && !boss.isDie()) {
                boss.changeToTypePK();
                break;
            }
        }
        this.parentBoss.changeToTypePK();
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (damage >= this.nPoint.hp) {
            boolean flag = true;
            if (this.parentBoss != null) {
                if (this.parentBoss.bossAppearTogether != null && this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel] != null) {
                    for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
                        if (boss.id == BossID.ANDROID_15 && !boss.isDie()) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag && !this.parentBoss.isDie()) {
                    flag = false;
                }
            }
            if (!flag) {
                return 0;
            }
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
