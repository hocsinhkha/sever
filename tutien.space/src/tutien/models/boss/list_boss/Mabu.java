package barcoll.models.boss.list_boss;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Amodels.boss.list_boss;
//
//import Amodels.boss.Boss;
//import Amodels.boss.BossData;
//import Amodels.boss.BossID;
//import Amodels.boss.BossStatus;
//import Amodels.boss.BossesData;
//import Amodels.map.ItemMap;
//import Amodels.player.Player;
//import Amodels.skill.Skill;
//import Aservices.EffectSkillService;
//import Aservices.PetService;
//import Aservices.Service;
//import Autils.Util;
//import java.util.Random;
//
///**
// * @@Stole By barcoll
// */
//public class Mabu extends Boss {
//
//    public Mabu() throws Exception {
//        super(BossID.MABU, BossesData.MABU);
//    }
//
//    @Override
//    public void reward(Player plKill) {
//            if(Util.isTrue(33, 100)){
//        BossData Super = new BossData(
//            "Super Broly "+Util.nextInt(100),
//            this.gender,
//            new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
//            Util.nextInt(100000,200000), //dame
//            new int[]{Util.nextInt(1000000,16077777)}, //hp
//            new int[]{6}, //map join
//            new int[][]{
//                    {Skill.ANTOMIC,7,100},{Skill.MASENKO,7,100},
//                {Skill.KAMEJOKO,7,100},
//                    {Skill.TAI_TAO_NANG_LUONG,5,15000}},
//            new String[]{"|-2|SuperBroly"}, //text chat 1
//            new String[]{"|-1|Ọc Ọc"}, //text chat 2
//            new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
//            60
//            );
//
//            try {
//                new Super(Util.createIdBossClone((int) this.id), Super, this.zone);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }}}
//    @Override
//    public void active() {
//        Player player = this.zone.getPlayerInMap(id);
//        if (player!=null){
//            if (Util.canDoWithTime(st, 500)){
//        if (this.nPoint.hpMax < 16070777){
//                    this.nPoint.hpMax+=this.nPoint.hpMax/100;}
//            if(this.nPoint.hpMax > 16070777){
//                    this.nPoint.hpMax=16070777;}}}
//        super.active();
//        this.nPoint.dame = this.nPoint.hpMax/100;//To change body of generated methods, choose Tools | Templates.
//        if(!player.isBoss){if (Util.canDoWithTime(st, 900000)) {if(player==null){
//            this.changeStatus(BossStatus.LEAVE_MAP);
//            if(Util.isTrue(1, 100)){
//        BossData Super = new BossData(
//            "Super Broly "+Util.nextInt(100),
//            this.gender,
//            new short[]{294, 295, 296, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
//            Util.nextInt(100000,200000), //dame
//            new int[]{Util.nextInt(10000000,160707770)}, //hp
//            new int[]{6}, //map join
//            new int[][]{
//                    {Skill.ANTOMIC,7,100},{Skill.MASENKO,7,100},
//                {Skill.KAMEJOKO,7,100},
//                    {Skill.TAI_TAO_NANG_LUONG,5,15000}},
//            new String[]{"|-2|SuperBroly"}, //text chat 1
//            new String[]{"|-1|Ọc Ọc"}, //text chat 2
//            new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
//            60
//            );
//
//            try {
//                new Super(Util.createIdBossClone((int) this.id), Super, this.zone);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }}
//        }}
//    
//    }}
//    public int injured(Player plAtt, int damage, boolean piercing, boolean isMobAttack) {
//        if (plAtt != null) {
//            switch (plAtt.playerSkill.skillSelect.template.id) {
//                case Skill.ANTOMIC:
//                case Skill.DEMON:
//                case Skill.DRAGON:
//                case Skill.GALICK:
//                case Skill.KAIOKEN:    
//                case Skill.KAMEJOKO:    
//                case Skill.DICH_CHUYEN_TUC_THOI:    
//                case Skill.LIEN_HOAN:       
//                    damage=(int) (this.nPoint.hpMax/100);
//                    if (this.nPoint.hp<1) {
//                this.setDie(plAtt);
//                die(this);
//            }
//                    return (int) super.injured(plAtt, damage, !piercing, isMobAttack);
//            }
//        }
//        return (int) super.injured(plAtt, damage, piercing, isMobAttack);
//    }
//
//    
//    
//    @Override
//    public void joinMap() {
//        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//        st= System.currentTimeMillis();
//    }
//    private long st;
//}