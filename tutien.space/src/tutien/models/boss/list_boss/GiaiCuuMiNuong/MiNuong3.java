package tutien.models.boss.list_boss.GiaiCuuMiNuong;

import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.Boss;
import barcoll.consts.ConstPlayer;
import tutien.models.item.Item;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.map.DoanhTraiDocNhan.DoanhTrai;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.EffectSkillService;
import barcoll.services.Service;
import tutien.utils.Util;
import java.util.Random;

/**
 * @author BTH sieu cap vippr0
 */
public class MiNuong3 extends Boss {

    protected Player playerAtt;

    public MiNuong3(Zone zone, int dame, int hp ) throws Exception {
        super(-102, new BossData(
                "Mị nương", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{841, 842, 843, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((1 + dame)), //dame
                new long[]{((1 + hp ))}, //hp
                new int[]{}, //map join
                new int[][]{
                {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11},{Skill.KAMEJOKO, 1, 12},
              {Skill.ANTOMIC, 1, 13},  {Skill.ANTOMIC, 2, 14},  {Skill.ANTOMIC, 3, 15},{Skill.ANTOMIC, 4, 16},  {Skill.ANTOMIC, 5, 17},{Skill.ANTOMIC, 6, 19},  {Skill.ANTOMIC, 7, 20},
                {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));
        
        this.zone = zone;
    }
    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap caitrangmi3 = new ItemMap(this.zone, 860, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            caitrangmi3.options.add(new Item.ItemOption(50, 24));
            caitrangmi3.options.add(new Item.ItemOption(77, 32));
            caitrangmi3.options.add(new Item.ItemOption(117, 20));
            caitrangmi3.options.add(new Item.ItemOption(93,  new Random().nextInt(3) + 4));
            Service.getInstance().dropItemMap(this.zone, caitrangmi3);
        }
    }
    @Override
    public void active() {
    super.active();
    }
     @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
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