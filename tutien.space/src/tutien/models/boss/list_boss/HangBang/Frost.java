package tutien.models.boss.list_boss.HangBang;

import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.services.EffectSkillService;
import barcoll.services.Service;
import Amodels.boss.*;
import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossManager;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.EffectSkillService;
import barcoll.services.PetService;
import barcoll.services.Service;
import tutien.utils.Util;
import java.util.Random;
import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstRatio;
import tutien.models.intrinsic.Intrinsic;
import tutien.models.item.Item;
import tutien.models.skill.Skill;
import barcoll.server.Manager;
import barcoll.services.EffectSkillService;
import barcoll.services.InventoryServiceNew;
import barcoll.services.ItemService;
import barcoll.services.MapService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Logger;
import tutien.utils.SkillUtil;
import tutien.utils.Util;
import tutien.utils.Util;
import java.util.Random;


public class Frost extends Boss {

    public Frost() throws Exception {
        super(BossID.FROST , BossesData.FROST_1,BossesData.FROST_2, BossesData.FROST_3);
 }
   @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
             plKill.Tamkjlltutien[0] += 100000;
                plKill.ExpTamkjll += 1000000;
                Service.gI().sendMoney(plKill);
            ItemMap caitrang = new ItemMap(this.zone, 1324, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            caitrang.options.add(new Item.ItemOption(50, 200));
            caitrang.options.add(new Item.ItemOption(77, 150));
            caitrang.options.add(new Item.ItemOption(117, 150));
               caitrang.options.add(new Item.ItemOption(95, 150));
            caitrang.options.add(new Item.ItemOption(96, 150));
            caitrang.options.add(new Item.ItemOption(93,  new Random().nextInt(3) + 4));
            Service.getInstance().dropItemMap(this.zone, caitrang);
            Service.gI().sendThongBao(plKill, "Bạn nhận đc 100k EXP Tu Tiên và 1m tu vi do kết liễu boss.");

        }
    }
   @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 3000000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

     @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 10)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = (double) this.nPoint.subDameInjureWithDeff(damage/7);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                  damage = damage/4;
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
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
    }





















