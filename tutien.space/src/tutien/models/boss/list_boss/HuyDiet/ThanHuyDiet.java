package tutien.models.boss.list_boss.HuyDiet;

import barcoll.consts.ConstPlayer;
import Amodels.boss.Boss;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import tutien.models.item.Item;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.server.Client;
import barcoll.server.Manager;
import barcoll.server.ServerNotify;
import barcoll.services.EffectSkillService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;
import java.util.Random;


public class ThanHuyDiet extends Boss {
    private long lasttimehakai;
    private int timehakai;

    public ThanHuyDiet() throws Exception {
        super(Util.randomBossId(), BossesData.THAN_HUY_DIET);
    }

     @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062,};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(20, 100)) {
           Service.getInstance().dropItemMap(this.zone, Util.ratiItemts(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }    else {
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
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


