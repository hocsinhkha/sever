package tutien.models.boss.list_boss.Corona;

import barcoll.consts.ConstPlayer;
import tutien.models.item.Item;
import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.server.Manager;
import barcoll.services.EffectSkillService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.SkillService;
import barcoll.services.TaskService;
import tutien.services.func.ChangeMapService;
import tutien.utils.SkillUtil;
import tutien.utils.Util;
import java.util.Random;


public class Corona3 extends Boss {
    private long lasttimehakai;
    private int timehakai;

    public Corona3() throws Exception {
        super(BossID.corona3, BossesData.Corona_3);
}
@Override
   public void reward(Player plKill) {
        {
            if (plKill != null) {
                plKill.Tamkjlltutien[0] += 2000000;
                plKill.ExpTamkjll += 20000000;
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
                          Service.gI().sendThongBao(plKill, "Bạn nhận đc 2 Triệu EXP Tu Tiên và 20 Triệu Tu Vi do kết liễu boss.");

                 return;
           }
        }
    
    }
   public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage/3);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
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

        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        this.huydiet();
        this.attack();
        
//        super.active(); //To change body of generated methods, choose Tools | Templates.
//        if (Util.canDoWithTime(st, 1000000)) {
//            this.changeStatus(BossStatus.LEAVE_MAP);
        }

//    }   @Override
//    public void joinMap() {
//        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//        st = System.currentTimeMillis();
//    }
//    private long st;

    private void huydiet() {
        if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
//        this.nPoint.dameg += (pl.nPoint.dame * 5 / 100);
//        this.nPoint.hpg += (pl.nPoint.hp * 2 / 100);
//        this.nPoint.critg++;
//        this.nPoint.calPoint();
//        PlayerService.gI().hoiPhuc(this, pl.nPoint.hp, 0);
//        pl.injured(null, pl.nPoint.hpMax, true, false);
//        Service.gI().sendThongBao(pl, "Bạn vừa bị " + this.name + " cho bay màu");
//        this.chat(2, "Hắn ta mạnh quá,coi chừng " + pl.name + ",tên " + this.name + " hắn không giống như những kẻ thù trước đây");
//        this.chat("Thật là yếu ớt " + pl.name);
//        this.lasttimehakai = System.currentTimeMillis();
//        this.timehakai = Util.nextInt(20000, 30000);
    }
}
