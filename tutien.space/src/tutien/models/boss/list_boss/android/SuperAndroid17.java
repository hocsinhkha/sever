package tutien.models.boss.list_boss.android;

import barcoll.consts.ConstMob;
import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossManager;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.mob.Mob;
import tutien.models.player.Player;
import barcoll.services.EffectSkillService;
import barcoll.services.PetService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;

public class SuperAndroid17 extends Boss {

    public SuperAndroid17() throws Exception {
        super(BossID.SUPER_ANDROID_17, BossesData.SUPER_ANDROID_17);
          this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
    }
    }
    @Override
    public void reward(Player plKill) {
      plKill.Tamkjlltutien[0] += 100000;
                plKill.ExpTamkjll += 2000000;
                Service.gI().sendMoney(plKill);
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x + 70, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 10, this.location.x + 0, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 1, this.location.x - 50, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 12, this.location.x - 100, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x - 59, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 10, this.location.x - 55, this.location.y, plKill.id));
           Service.gI().sendThongBao(plKill, "Bạn nhận đc 100k EXP Tu Tiên và 2 Triệu Tu Vi do kết liễu boss.");

    }
     @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 100000;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }
@Override
    public void active() {
        super.active();
    }


    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
 @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
