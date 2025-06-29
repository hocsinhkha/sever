package tutien.models.boss.list_boss.ConDuongRanDoc;

import Amodels.boss.BossData;
import Amodels.boss.BossManager;
import Amodels.boss.BossID;
import Amodels.boss.Boss;
import barcoll.consts.ConstPlayer;
import tutien.models.boss.list_boss.doanh_trai.NinjaClone;
import tutien.models.map.BanDoKhoBau.BanDoKhoBau;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.EffectSkillService;
import barcoll.services.Service;
import tutien.utils.Util;


public class Saibamen2 extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public Saibamen2(Zone zone , int level, int dame, int hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "số 2",
                ConstPlayer.TRAI_DAT,
                new short[]{642, 643, 644, -1, -1, -1},
                ((10000 + dame) * level),
                 new long[]{((500000 + hp) * level)},
                 new int[]{103},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 19, 0, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    @Override
    public void active() {
        super.active();
    }
    
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (int) this.nPoint.subDameInjureWithDeff(damage/2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage/2;
            }
            this.nPoint.subHP(damage);
            if (this.nPoint.hp == 0) {
                try {
                    
                 new Saibamen3(this.zone, 2, Util.nextInt(1000, 10000), BossID.SAIBAMEN_2);   
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}