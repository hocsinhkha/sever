package tutien.models.boss.list_boss.Luwdev;

//package barcoll.models.boss.list_boss.Luwdev;
//
//import Amodels.boss.models.boss.Boss;
//import Amodels.boss.models.boss.BossID;
//import com.barcoll.models.boss.BossesData;
//import com.barcoll.models.map.ItemMap;
//import com.barcoll.models.player.Player;
//import com.barcoll.services.EffectSkillService;
//import com.barcoll.services.Service;
//import com.barcoll.utils.Util;
//import java.util.Random;
//
//public class thantai extends Boss {
//
//    public thantai() throws Exception {
//        super(Util.randomBossId(), BossesData.THAN_HUY_DIET);
//    }
//    //khó ncc 
//    //hoang van luu
//   @Override
//    public void reward(Player plKill) {
//        if (UServicetil.isTrue(100, 100)) {
//            Service.gI().dropItemMap(this.zone, Util.raitijaky(zone, 710, 1, this.location.x + 1, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190, 100000, this.location.x + 0, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 100000, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 2, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 3, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190, 100000, this.location.x + 1000005, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 20, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 25, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 30, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 35, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 5, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 1000000, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 1000005, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 20, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190, 100000, this.location.x + 40, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 45, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 50, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 55, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 60, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 25, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 30, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 35, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 40, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190, 100000, this.location.x + 65, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 70, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 75, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 80, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 85, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 30, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 35, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 40, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 45, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190, 100000, this.location.x + 90, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 95, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x + 10000000, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 50, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 55, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 60, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 65, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 70, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 75, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 85, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 90, this.location.y, plKill.id));
//            Service.gI().dropItemMap(this.zone, Util.ratiDTL(zone, 190,  100000, this.location.x - 95, this.location.y, plKill.id));
//              return;    
//        }
//
//  
//   @Override
//    public void active() {
//        super.active(); //To change body of generated methods, choose Tools | Templates.
//        if (Util.canDoWithTime(st, 3000000)) {
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//    @Override
//    public void joinMap() {
//        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//        st= System.currentTimeMillis();
//    }
//    private long st;
//
//     @Override
//    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
//        if (!this.isDie()) {
//            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 10)) {
//                this.chat("Xí hụt");
//                return 0;
//            }
//            damage = (double) this.nPoint.subDameInjureWithDeff(damage/7);
//            if (!piercing && effectSkill.isShielding) {
//                if (damage > nPoint.hpMax) {
//                    EffectSkillService.gI().breakShield(this);
//                }
//                  damage = damage/4;
//            }
//            this.nPoint.subHP(damage);
//            if (isDie()) {
//                this.setDie(plAtt);
//                die(plAtt);
//            }
//            return damage;
//        } else {
//            return 0;
//        }
//    
//    }
///**
// * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
// * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
// * 
// * 
// * 
// * lồn
// * 
// * 
// * 
// * 
// * 
// * 
// * 
// * 
// * 
// *luw 
//    }
