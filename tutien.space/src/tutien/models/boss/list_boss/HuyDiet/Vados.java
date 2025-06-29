package tutien.models.boss.list_boss.HuyDiet;

import Amodels.boss.Boss;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.consts.ConstPlayer;
import tutien.models.player.Player;
import barcoll.services.EffectSkillService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;
import java.util.Random;

public class Vados extends Boss {
 private long lasttimehakai;
    private int timehakai;
    public Vados() throws Exception {
        super(Util.randomBossId(), BossesData.THIEN_SU_VADOS);
    }

   @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062,};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(10, 100)) {
           Service.getInstance().dropItemMap(this.zone, Util.ratiItemts(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        }    else {
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }

 @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (Util.isTrue(30, 100) && plAtt != null) {//tỉ lệ hụt của thiên sứ
            Util.isTrue(this.nPoint.tlNeDon, 10000);
            if (Util.isTrue(1, 100)) {
                this.chat("Hãy để bản năng tự vận động");
                this.chat("Tránh các động tác thừa");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Chậm lại,các ngươi quá nhanh rồi");
                this.chat("Chỉ cần hoàn thiện nó!");
                this.chat("Các ngươi sẽ tránh được mọi nguy hiểm");
            } else if (Util.isTrue(1, 100)) {
                this.chat("Đây chính là bản năng vô cực");
            }
            damage = 0;

        }
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
            }
            if (damage > 1) damage /= 15/10;
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
}

