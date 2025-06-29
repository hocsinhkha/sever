package tutien.models.boss.list_boss.Mabu2h;

import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.consts.ConstPlayer;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.services.EffectSkillService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;
import java.util.Random;

 //mo gioi han danme roi 
public class bossMabu2h extends Boss {
  private long lasttimehakai;
  private int timehakai;
    
    public bossMabu2h() throws Exception {
        super(BossID.Mabu2h , BossesData.Mabu2h_1,BossesData.Mabu2h_2, BossesData.Mabu2h_3,BossesData.Mabu2h_4,BossesData.Mabu2h_5);
    }
   @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662,};
        int randomDo = new Random().nextInt(itemDos.length);
        if (Util.isTrue(20, 100)) {
           Service.getInstance().dropItemMap(this.zone, Util.ratiItemhd(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
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
        
        }
    private void huydiet() {
        if (!Util.canDoWithTime(this.lasttimehakai, this.timehakai) || !Util.isTrue(1, 100)) {
            return;
        }

        Player pl = this.zone.getRandomPlayerInMap();
        if (pl == null || pl.isDie()) {
            return;
        }
    }
  }






















