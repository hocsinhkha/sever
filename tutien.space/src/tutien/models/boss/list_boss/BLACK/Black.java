package tutien.models.boss.list_boss.BLACK;

import Amodels.boss.*;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.server.Manager;
import barcoll.services.EffectSkillService;
import barcoll.services.Service;
import tutien.utils.Util;

import java.util.Random;


public class Black extends Boss {

    public Black() throws Exception {
        super(BossID.BLACK, BossesData.BLACK_GOKU);
    }

     @Override
   public void reward(Player plKill) {
        {
            if (plKill != null) {
                plKill.Tamkjlltutien[0] += 200000;
                plKill.ExpTamkjll += 2000000;
                Service.gI().sendMoney(plKill);
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 14, this.location.x + 70, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 10, this.location.x + 0, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 12, this.location.x - 50, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 12, this.location.x - 100, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 14, this.location.x - 59, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 20, this.location.x - 55, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x - 45, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x + 70, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 11, this.location.x + 32, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 12, this.location.x + 55, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 3, this.location.x + 100, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x + 59, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x - 10, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 21, this.location.x - 25, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 1, this.location.x - 21, this.location.y, plKill.id));
                 Service.gI().sendThongBao(plKill, "Bạn nhận đc 200k EXP Tu Tiên và 2 Triệu Tu Vi do kết liễu boss.");

                 return;
           }
        }
    
    }

    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 400000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage/7);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
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
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }

    private long st;

//    @Override
//    public void moveTo(int x, int y) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.moveTo(x, y);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.reward(plKill);
//    }
//    
//    @Override
//    protected void notifyJoinMap() {
//        if(this.currentLevel == 1){
//            return;
//        }
//        super.notifyJoinMap();
//    }
}






















