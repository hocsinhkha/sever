package tutien.models.boss.ConDuongRanDoc;


import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import tutien.models.boss.list_boss.cell.SieuBoHung;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import barcoll.services.EffectSkillService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.SkillService;
import tutien.utils.Util;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Saibamen extends Boss {
    
    private long lastTimeHP;
    private int timeHP;
    private boolean calledNinja;

    public Saibamen() throws Exception {
        super(BossID.SAIBAMEN, BossesData.Saibamen);
    }
    @Override
public void active() {
    super.active();
    if(Util.canDoWithTime(st,300000)){
        this.changeStatus(BossStatus.LEAVE_MAP);
    }
    try {
            this.TuSat();
        } catch (Exception ex) {
            Logger.getLogger(SieuBoHung.class.getName()).log(Level.SEVERE, null, ex);
        }
}

private void TuSat() throws Exception {
        if (this.nPoint.hp == 0) {
            
         SkillService.gI().selectSkill(playerTarger, 14);
            
        }
    }
    
    @Override
    public void joinMap() {
        super.joinMap();
        st= System.currentTimeMillis();
    }
    private long st;
    
    @Override
    public void moveToPlayer(Player player) {
        this.moveTo(player.location.x, player.location.y);
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
            if (this.nPoint.hp >= 49999999 && !this.calledNinja) {
                try {
//                    new BrolySuper(this.zone, 2, Util.nextInt(1000, 10000), BossID.BROLY_SUPER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.calledNinja = true;
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
}
