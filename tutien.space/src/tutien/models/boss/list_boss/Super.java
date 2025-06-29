package barcoll.models.boss.list_boss;

import barcoll.consts.ConstPlayer;
import Amodels.boss.*;
import tutien.models.boss.list_boss.Broly.detu;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.EffectSkillService;
import barcoll.services.PetService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import tutien.utils.Util;
import java.util.Random;

/**
 * @Stole By barcoll
 */
public class Super extends Boss {
private boolean calledNinja;
    public Super(int bossID, BossData bossData, Zone zone) throws Exception {
        super(BossID.SUPER, bossData);
        this.zone = zone;
    }

    @Override
        //vật phẩm rơi khi diệt boss nhân bản
        public void reward(Player plKill) {
        int[] itemCt = new int[]{568};
        int randomDo = new Random().nextInt(itemCt.length);
        if (Util.isTrue(99, 100)) {
            if (Util.isTrue(1, 100)) {
                Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, 568, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.gI().dropItemMap(this.zone, Util.useItem(zone, itemCt[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } 
    }
    @Override
    public void active() {
            if (!calledNinja ) {               
               try {
                   BossData bossDataClone = new BossData(
                                                "Đệ tử",
                                                (byte) 0,
                                                new short[]{285, 286, 287, -1, -1, -1},
                                                0,
                                                new long[]{1111},
                                                new int[]{140},
                                                new int[][]{},
                                                new String[]{}, //text chat 1
                                                new String[]{}, //text chat 2
                                                new String[]{}, //text chat 3
                                                60
                                        );
                   detu dt = new detu(Util.createIdBossClone((int) this.id), bossDataClone, this.zone, this.location.x - 20, this.location.y);
                  
               } catch (Exception ex) {
                   ex.printStackTrace();
               }
                this.calledNinja = true;
             }
     
        Player player = this.zone.getPlayerInMap(id);
        if (player!=null){
            if (Util.canDoWithTime(st, 500)){
        if (this.nPoint.hpMax < 16070777){
                    this.nPoint.hpMax+=this.nPoint.hpMax/100;}
            if(this.nPoint.hpMax > 16070777){
                    this.nPoint.hpMax=16070777;}}}
        super.active();
        this.nPoint.dame = this.nPoint.hpMax/100;//To change body of generated methods, choose Tools | Templates.
        if(!player.isBoss){if (Util.canDoWithTime(st, 900000)) {if(player==null){
            this.changeStatus(BossStatus.LEAVE_MAP);
            if(Util.isTrue(1, 100)){
        BossData Super = new BossData(
            "Super Broly "+Util.nextInt(100),
            this.gender,
            new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
            Util.nextInt(100000,200000), //dame
            new long[]{Util.nextInt(10000000,16070777)}, //hp
          new int[]{},
          new int[][]{
                    {Skill.ANTOMIC,7,100},{Skill.MASENKO,7,100},
                {Skill.KAMEJOKO,7,100},
                    {Skill.TAI_TAO_NANG_LUONG,5,15000}},
            new String[]{"|-2|SuperBroly"}, //text chat 1
            new String[]{}, //text chat 2
            new String[]{}, //text chat 3
            0
            );

            try {
                new Super(Util.createIdBossClone((int) this.id), Super, this.zone);
            } catch (Exception e) {
                e.printStackTrace();
            }}
        }}
    
    }}
    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch (plAtt.playerSkill.skillSelect.template.id) {
                case Skill.ANTOMIC:
                case Skill.DEMON:
                case Skill.DRAGON:
                case Skill.GALICK:
                case Skill.KAIOKEN:    
                case Skill.KAMEJOKO:    
                case Skill.DICH_CHUYEN_TUC_THOI:    
                case Skill.LIEN_HOAN:       
                    damage=(int) (this.nPoint.hpMax/100);
                    if (this.nPoint.hp<1) {
                this.setDie(plAtt);
                die(this);
            }
                    return (int) super.injured(plAtt, damage, !piercing, isMobAttack);
            }
        }
        return (int) super.injured(plAtt, damage, piercing, isMobAttack);
    }

    
    
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;
}