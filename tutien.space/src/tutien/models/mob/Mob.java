package tutien.models.mob;

import barcoll.services.Service;
import barcoll.services.TaskService;
import barcoll.services.MapService;
import barcoll.services.ItemMapService;
import barcoll.consts.ConstMap;
import barcoll.consts.ConstMob;
import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstTask;
import barcoll.models.Template;
import tutien.models.item.Item;
import barcoll.models.map.ItemMap;

import java.util.List;

import barcoll.models.map.Zone;
import tutien.models.player.Location;
import tutien.models.player.Pet;
import tutien.models.player.Player;
import tutien.models.reward.ItemMobReward;
import tutien.models.reward.MobReward;
import tutien.models.skill.PlayerSkill;
import tutien.models.skill.Skill;
import com.girlkun.network.io.Message;
import barcoll.server.Maintenance;
import barcoll.server.Manager;
import barcoll.server.ServerManager;
import barcoll.services.InventoryServiceNew;
import barcoll.services.ItemService;
import tutien.utils.Logger;
import tutien.utils.Util;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class Mob {

    public int id;
    public Zone zone;
    public int tempId;
    public String name;
    public byte level;

    public MobPoint point;
    public MobEffectSkill effectSkill;
    public Location location;

    public byte pDame;
    public int pTiemNang;
    private long maxTiemNang;

    public long lastTimeDie;
    public int lvMob = 0;
    public int status = 5;
    public short cx;
    public short cy;
    public int Gio;
    public int action = 0;
    public static int TIME_START_HIRU = 23;

    public Mob(Mob mob) {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
        this.id = mob.id;
        this.tempId = mob.tempId;
        this.level = mob.level;
        this.point.setHpFull(mob.point.getHpFull());
        this.point.sethp(this.point.getHpFull());
        this.location.x = mob.location.x;
        this.location.y = mob.location.y;
        this.pDame = mob.pDame;
        this.pTiemNang = mob.pTiemNang;
        this.setTiemNang();
    }

    public Mob() {
        this.point = new MobPoint(this);
        this.effectSkill = new MobEffectSkill(this);
        this.location = new Location();
    }

    public static void initMobBanDoKhoBau(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void initMobKhiGaHuyDiet(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public static void initMobConDuongRanDoc(Mob mob, byte level) {
        mob.point.dame = level * 3250 * mob.level * 4;
        mob.point.maxHp = level * 12472 * mob.level * 2 + level * 7263 * mob.tempId;
    }

    public boolean isSieuQuai() {
        return this.lvMob > 0;
    }

    public synchronized void injured(Player plAtt, double damage, boolean dieWhenHpFull) {
        if (!this.isDie()) {
            if (damage >= this.point.hp) {
                damage = (int) this.point.hp;
            }
            if (this.zone.map.mapId == 112) {
                plAtt.NguHanhSonPoint++;
            }
            if (!dieWhenHpFull) {
                if (this.point.hp == this.point.maxHp && damage >= this.point.hp) {
                    damage = (int) (this.point.hp - 1);
                }
                if (this.tempId == 0 && damage > 10) {
                    damage = 10;
                }
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                            damage *= plAtt.nPoint.multicationChuong;
                            plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                        }

                }
            }
            this.point.hp -= damage;

            if (this.isDie()) {
                this.lvMob = 0;
                this.status = 0;
                this.sendMobDieAffterAttacked(plAtt, damage);
                TaskService.gI().checkDoneTaskKillMob(plAtt, this);
                TaskService.gI().checkDoneSideTaskKillMob(plAtt, this);
                this.lastTimeDie = System.currentTimeMillis();
                if (this.id == 13) {
                    this.zone.isbulon13Alive = false;
                }
                if (this.id == 14) {
                    this.zone.isbulon14Alive = false;
                }
                if (this.isSieuQuai()) {
                    plAtt.achievement.plusCount(12);
                }
            } else {
                this.sendMobStillAliveAffterAttacked(damage, plAtt != null ? plAtt.nPoint.isCrit : false);
            }
            if (plAtt != null) {
                Service.gI().addSMTN(plAtt, (byte) 2, getTiemNangForPlayer(plAtt, damage), true);
            }
        }
    }

    public static void hoiSinhMob(Mob mob) {
        mob.point.hp = mob.point.maxHp;
        mob.setTiemNang();
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(mob.id);
            msg.writer().writeByte(mob.tempId);
            msg.writer().writeByte(0); //level mob
            msg.writer().writeInt(Util.TamkjllGH(mob.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(mob.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void setTiemNang() {
        this.maxTiemNang = this.point.getHpFull() * (this.pTiemNang + Util.nextInt(-2, 2)) / 100;
    }

    private long lastTimeAttackPlayer;

    public boolean isDie() {
        return this.point.gethp() <= 0;
    }


    public long getTiemNangForPlayer(Player pl, double dame) {
        int levelPlayer = Service.getInstance().getCurrLevel(pl);
        int n = levelPlayer - this.level;
        long pDameHit = 0;
        if (point.getHpFull() >= 100000000)
        {
        pDameHit = Util.TamkjllGH(dame) * 500 / point.getHpFull();
        }
        else pDameHit = Util.TamkjllGH(dame) * 100 / point.getHpFull();
        
        long tiemNang = pDameHit * maxTiemNang / 100;
        if (n >= 0) {
            for (int i = 0; i < n; i++) {
                long sub = tiemNang * 10 / 100;
                if (sub <= 0) {
                    sub = 1;
                }
                tiemNang -= sub;
            }
        } else {
            for (int i = 0; i < -n; i++) {
                long add = tiemNang * 10 / 100;
                if (add <= 0) {
                    add = 1;
                }
                tiemNang += add;
            }
        }
        if (tiemNang <= 0) {
            tiemNang = 1;
        }
        tiemNang = Util.TamkjllGH(pl.nPoint.calSucManhTiemNang(tiemNang));
        if (pl.zone.map.mapId == 122 || pl.zone.map.mapId == 123 || pl.zone.map.mapId == 124) {
            tiemNang *= 20;
        }
        return tiemNang;
    }

    public void update() {
        if (isDie() && this.tempId == 70 || this.tempId == 71 || this.tempId == 72 && (System.currentTimeMillis() - lastTimeDie) > 5000 && level <= 2) {
            if (level == 0) {
                level = 1;
                action = 6;
                this.point.hp = this.point.maxHp;
            } else if (level == 1) {
                level = 2;
                action = 5;
                this.point.hp = this.point.maxHp;
            } else if (level == 2) {
                level = 3;
                action = 9;
            }
            int trai = 0;
            int phai = 1;
            int next = 0;
            ItemMap GOLD = new ItemMap(this.zone, 190, Util.nextInt(10000, 20000), this.location.x, this.location.y, -1);
//            for(int i = 0; i < 30; i++){
//                int X = next == 0 ? -5 * trai : 5 * phai;
//                if(next == 0){
//                    trai++;
//                }
//                else{
//                    phai++;
//                }
//                next = next == 0 ? 1 : 0;
//                if(trai > 10){
//                    trai = 0;
//                }
//                if(phai > 10){
//                    phai = 1;
//                }
//                Service.gI().dropItemMap(zone, GOLD);
//            }
            Service.gI().sendBigBoss2(zone, action, this);
            if (level <= 2) {
                Message msg = null;
                try {
                    msg = new Message(-9);
                    msg.writer().writeByte(this.id);
                    msg.writer().writeInt((int) this.point.hp);
                    msg.writer().writeInt(1);
                    Service.gI().sendMessAllPlayerInMap(zone, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (msg != null) {
                        msg.cleanup();
                        msg = null;
                    }
                }
            } else {
                cx = -1000;
                cy = -1000;
            }
        }

        if (this.isDie() && !Maintenance.isRuning) {
            switch (zone.map.type) {
                case ConstMap.MAP_DOANH_TRAI:
                    break;
                case ConstMap.MAP_BAN_DO_KHO_BAU:
                    break;
                case ConstMap.MAP_KHI_GA_HUY_DIET:
                    break;
                case ConstMap.MAP_CON_DUONG_RAN_DOC:
                    break;
                case ConstMap.MAP_GIAI_CUU_MI_NUONG:
                    break;    
                case ConstMap.MAP_HIRU:
                    if (Util.canDoWithTime(this.lastTimeDie, 5000) && this.tempId == 70 && !Util.isTimeHiru()) {
                        this.level = 0;
                        this.hoiSinh();
                        this.sendMobHoiSinh();
                    } else if (this.tempId == 70 || this.tempId == 71 || this.tempId == 72 && this.isDie() && this.level == 3) {
                        this.zone.mobs.remove(0);
                    }
                    break;
                default:
                    if (Util.canDoWithTime(lastTimeDie, 5000)) {
                        if (this.tempId == 77) {
                            long currentTime = System.currentTimeMillis();
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(currentTime);
                            cal.set(Calendar.HOUR_OF_DAY, 20); // Đặt giờ hồi sinh là 20:00
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            long respawnTime = cal.getTimeInMillis();

                            // Kiểm tra nếu đã đến thời gian hồi sinh
                            if (currentTime >= respawnTime) {
                                this.sendMobHoiSinh();
                            }
                        } else {
                            this.hoiSinh();
                            this.sendMobHoiSinh();
                        }
                    }
            }
        }
        effectSkill.update();
        if (this.tempId == 70 || this.tempId == 71 || this.tempId == 72) {
            BigbossAttack();
        } else {
            attackPlayer();
        }
    }

    private void attackPlayer() {
        if (!isDie() && !effectSkill.isHaveEffectSkill() && !(tempId == 0) && !(tempId == 82) && Util.canDoWithTime(lastTimeAttackPlayer, 2000)) {
            Player pl = getPlayerCanAttack();
            if (pl != null) {
                this.mobAttackPlayer(pl);
            }
            this.lastTimeAttackPlayer = System.currentTimeMillis();
        }
    }

    private Player getPlayerCanAttack() {
        int distance = 100;
        Player plAttack = null;
        try {
            List<Player> players = this.zone.getNotBosses();
            for (Player pl : players) {
                if (!pl.isDie() && !pl.isNewPet && !pl.name.equals("Jajirô") && !pl.isBoss && !pl.effectSkin.isVoHinh) {
                    int dis = Util.getDistance(pl, this);
                    if (dis <= distance) {
                        plAttack = pl;
                        distance = dis;
                    }
                }
            }
        } catch (Exception e) {

        }
        return plAttack;
    }

    //**************************************************************************
    private void mobAttackPlayer(Player player) {
        int dameMob = (int) this.point.getDameAttack();
        if (player.charms.tdDaTrau > System.currentTimeMillis()) {
            dameMob /= 2;
        }
        double dame = player.injured(null, dameMob, false, true);
        this.sendMobAttackMe(player, dame);
        this.sendMobAttackPlayer(player);
    }

    private void sendMobAttackMe(Player player, double dame) {
        if (!player.isPet) {
            Message msg;
            try {
                msg = new Message(-11);
                msg.writer().writeByte(this.id);
                msg.writer().writeInt(Util.TamkjllGH(dame)); //dame
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    private void sendMobAttackPlayer(Player player) {
        Message msg;
        try {
            msg = new Message(-10);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeInt((int) player.nPoint.hp);
            Service.gI().sendMessAnotherNotMeInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hoiSinh() {
        this.status = 5;
        this.point.hp = this.point.maxHp;
        this.setTiemNang();
    }

    public void sendMobHoiSinh() {
        Message msg;
        try {
            msg = new Message(-13);
            msg.writer().writeByte(this.id);
            msg.writer().writeByte(this.tempId);
            msg.writer().writeByte(lvMob);
            msg.writer().writeInt(Util.TamkjllGH(this.point.hp));
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //**************************************************************************
    private void sendMobDieAffterAttacked(Player plKill, double dameHit) {
        Message msg;
        try {
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(Util.TamkjllGH(dameHit));
            msg.writer().writeBoolean(plKill.nPoint.isCrit); // crit
            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (IOException e) {
        }
    }

    public void sendMobDieAfterMobMeAttacked(Player plKill, int dameHit) {
        this.status = 0;
        Message msg;
        try {
            if (this.id == 13) {
                this.zone.isbulon13Alive = false;
            }
            if (this.id == 14) {
                this.zone.isbulon14Alive = false;
            }
            msg = new Message(-12);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(dameHit);
            msg.writer().writeBoolean(false); // crit

            List<ItemMap> items = mobReward(plKill, this.dropItemTask(plKill), msg);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
            hutItem(plKill, items);
        } catch (IOException e) {
            Logger.logException(Mob.class, e);
        }
//        if (plKill.isPl()) {
//            if (TaskService.gI().IsTaskDoWithMemClan(plKill.playerTask.taskMain.id)) {
//                TaskService.gI().checkDoneTaskKillMob(plKill, this, true);
//            } else {
//                TaskService.gI().checkDoneTaskKillMob(plKill, this, false);
//            }
//
//        }
        this.lastTimeDie = System.currentTimeMillis();
    }

    private void hutItem(Player player, List<ItemMap> items) {
        if (!player.isPet && !player.isNewPet) {
            if (player.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(player, item.itemMapId, true);
                    }
                }
            }
        } else {
            if (((Pet) player).master.charms.tdThuHut > System.currentTimeMillis()) {
                for (ItemMap item : items) {
                    if (item.itemTemplate.id != 590) {
                        ItemMapService.gI().pickItem(((Pet) player).master, item.itemMapId, true);
                    }
                }
            }
        }
    }

    private List<ItemMap> mobReward(Player player, ItemMap itemTask, Message msg) {
//        nplayer
        List<ItemMap> itemReward = new ArrayList<>();
        try {
            if ((!player.isPet && player.setClothes.setDTS == 5) || (player.isPet && ((Pet) player).setClothes.setDTS == 5)) {
                if (Util.isTrue(30, 100)) {
                    byte random = 1;
                    if (Util.isTrue(2, 100)) {
                        random = 2;
                    }
                    Item i = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
                    i.quantity = random;
                    InventoryServiceNew.gI().addItemBag(player, i);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + random + " hồng ngọc");
                }
            }


            itemReward = this.getItemMobReward(player, this.location.x + Util.nextInt(-10, 10),
                    this.location.y);
            if (itemTask != null) {
                itemReward.add(itemTask);
            }
            msg.writer().writeByte(itemReward.size()); //sl item roi
            for (ItemMap itemMap : itemReward) {
                msg.writer().writeShort(itemMap.itemMapId);// itemmapid
                msg.writer().writeShort(itemMap.itemTemplate.id); // id item
                msg.writer().writeShort(itemMap.x); // xend item
                msg.writer().writeShort(itemMap.y); // yend item
                msg.writer().writeInt((int) itemMap.playerId); // id nhan nat
            }
        } catch (IOException e) {
            System.out.println("llllll");
        }
        return itemReward;
    }

    public List<ItemMap> getItemMobReward(Player player, int x, int yEnd) { //quái rơi vật phẩm
        List<ItemMap> list = new ArrayList<>();

        final Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(11);

        int tileVang = 0;
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
        // bí kiếp rơi giờ chẵn 
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Gio = calendar.get(Calendar.HOUR_OF_DAY);
        if ( ( zone.map.mapId == 0 || zone.map.mapId == 7 || zone.map.mapId == 14 ) && Util.isTrue(100, 100) && this.tempId == 0 && (Gio == 2 || Gio == 4 || Gio == 6 || Gio == 8 || Gio == 10 || Gio == 12 || Gio == 14 || Gio == 16 || Gio == 18 || Gio == 20 || Gio == 22 || Gio == 24) && player.cFlag == 8) {
            list.add(new ItemMap(zone, 590, 1, x, player.location.y, player.id));
            list.add(new ItemMap(zone, 590, 1, x - 15, player.location.y, player.id));
            list.add(new ItemMap(zone, 590, 1, x + 15, player.location.y, player.id));
        }
        
        // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
       if (this.zone.map.mapId == 156) {
            if(Util.isTrue(70, 100)){
                Item manh2 = ItemService.gI().createNewItem((short) (1390));                
                InventoryServiceNew.gI().addItemBag(player, manh2);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
                            }}
       // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
       if (this.zone.map.mapId == 157) {
            if(Util.isTrue(70, 100)){
                Item manh3 = ItemService.gI().createNewItem((short) (1391));                
                InventoryServiceNew.gI().addItemBag(player, manh3);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
                            }}
       // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
       if (this.zone.map.mapId == 157) {
            if(Util.isTrue(50, 100)){
                Item manh4 = ItemService.gI().createNewItem((short) (1392));                
                InventoryServiceNew.gI().addItemBag(player, manh4);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
                            }}
       // Rơi vật phẩm Mảnh Vỡ Bông Tai ( ID 541 )        
       if (this.zone.map.mapId == 157) {
            if(Util.isTrue(40, 100)){
                Item manh4 = ItemService.gI().createNewItem((short) (1393));                
                InventoryServiceNew.gI().addItemBag(player, manh4);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Mảnh Vỡ Bông Tai ");
                            }}
          if (this.zone.map.mapId == 196) {
            if(Util.isTrue(15, 100)){
                Item dabongtoi = ItemService.gI().createNewItem((short) (1382));                
                InventoryServiceNew.gI().addItemBag(player, dabongtoi);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá Bóng Tối ");
                            }}
          if (this.zone.map.mapId == 196) {
            if(Util.isTrue(2, 200)){
                Item dabongtoi = ItemService.gI().createNewItem((short) (1383));                
                InventoryServiceNew.gI().addItemBag(player, dabongtoi);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá Ánh Sáng ");
                            }}
         if (this.zone.map.mapId == 196) {
            if(Util.isTrue(2, 200)){
                Item dakham = ItemService.gI().createNewItem((short) (1613));                
                InventoryServiceNew.gI().addItemBag(player, dakham);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá... ");
                            }}
          if (this.zone.map.mapId == 196) {
            if(Util.isTrue(2, 200)){
                Item dakham = ItemService.gI().createNewItem((short) (1614));                
                InventoryServiceNew.gI().addItemBag(player, dakham);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá... ");
                            }}
          if (this.zone.map.mapId == 196) {
            if(Util.isTrue(2, 100)){
                Item dakham = ItemService.gI().createNewItem((short) (1615));                
                InventoryServiceNew.gI().addItemBag(player, dakham);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá... ");
                            }}
            if (this.zone.map.mapId == 196) {
            if(Util.isTrue(20, 100)){
                Item dakham = ItemService.gI().createNewItem((short) (1616));                
                InventoryServiceNew.gI().addItemBag(player, dakham);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Đá Ngọc Bảo ");
                            }}
            //thuc an
             if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(5, 100)){
                 Item luw = ItemService.gI().createNewItem((short) (663));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được thuc an");
                    }}
               if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(5, 100)){
                 Item luw = ItemService.gI().createNewItem((short) (663));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được thuc an");
                    }}
                 if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(5, 100)){
                 Item luw = ItemService.gI().createNewItem((short) (663));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được thuc an");
                    }}
                   if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(5, 100)){
                 Item luw = ItemService.gI().createNewItem((short) (663));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được thuc an");
                    }}
                     if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(5, 100)){
                 Item luw = ItemService.gI().createNewItem((short) (663));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được thuc an");
                    }}
                       if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(5, 100)){
                 Item luw = ItemService.gI().createNewItem((short) (663));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được thuc an");
                    }}
                if (this.zone.map.mapId == 161) {
               if(Util.isTrue(100, 100)){
                 Item dakham = ItemService.gI().createNewItem((short) (457));                
                InventoryServiceNew.gI().addItemBag(player, dakham);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Thỏi Vàng");
                            }}
         if (this.zone.map.mapId == 161) {
               if(Util.isTrue(40, 100)){
                 Item dakham = ItemService.gI().createNewItem((short) (861));                
                InventoryServiceNew.gI().addItemBag(player, dakham);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn vừa nhận được Hồng Ngọc");
                            }}
                 if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           //quan 
            if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// quan hsd //luwdev
            if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//gang
              if (this.zone.map.mapId == 108) {//luwdev gaggggggggggggggg
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// gang hsd //luwdev
             if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
               }}
           //dz
         // ngoc rong huyet long
             if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 6500)){
                 Item luw = ItemService.gI().createNewItem((short) (1828));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
               if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 7500)){
                 Item luw = ItemService.gI().createNewItem((short) (1827));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                 if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (1826));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                   if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (1825));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
           //giayyyyyyyyyyyyy
              if (this.zone.map.mapId == 108) {//luwdev 
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// giay hsd //luwdev
              if (this.zone.map.mapId == 108) {//luwdev 
               if(Util.isTrue(1, 14000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 108) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhan tl
                  if (this.zone.map.mapId == 108) { //nhan tl
               if(Util.isTrue(1, 20000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhantl hsd 
                    if (this.zone.map.mapId == 108) { //nhan tl hsd 
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 108) { //ao
               if(Util.isTrue(1, 9500)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }} // 25s
             if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 108) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
        if (this.zone.map.mapId == 108) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 108) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 108) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// map108
              if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           //quan 
            if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// quan hsd //luwdev
            if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//gang
              if (this.zone.map.mapId == 105) {//luwdev gaggggggggggggggg
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// gang hsd //luwdev
             if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
               }}
           //dz
         // ngoc rong huyet long
             if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 6500)){
                 Item luw = ItemService.gI().createNewItem((short) (1828));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
               if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 7500)){
                 Item luw = ItemService.gI().createNewItem((short) (1827));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                 if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (1826));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                   if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (1825));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
           //giayyyyyyyyyyyyy
              if (this.zone.map.mapId == 105) {//luwdev 
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 15000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// giay hsd //luwdev
              if (this.zone.map.mapId == 105) {//luwdev 
               if(Util.isTrue(1, 14000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 105) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhan tl
                  if (this.zone.map.mapId == 105) { //nhan tl
               if(Util.isTrue(1, 20000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhantl hsd 
                    if (this.zone.map.mapId == 105) { //nhan tl hsd 
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 105) { //ao
               if(Util.isTrue(1, 9500)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }} // 25s
             if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 105) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
        if (this.zone.map.mapId == 105) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 105) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 105) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//map105
              if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           //quan 
            if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// quan hsd //luwdev
            if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//gang
              if (this.zone.map.mapId == 109) {//luwdev gaggggggggggggggg
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// gang hsd //luwdev
             if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
               }}
           //dz
         // ngoc rong huyet long
             if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 6500)){
                 Item luw = ItemService.gI().createNewItem((short) (1828));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
               if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 7500)){
                 Item luw = ItemService.gI().createNewItem((short) (1827));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                 if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (1826));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                   if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (1825));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
           //giayyyyyyyyyyyyy
              if (this.zone.map.mapId == 109) {//luwdev 
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// giay hsd //luwdev
              if (this.zone.map.mapId == 109) {//luwdev 
               if(Util.isTrue(1, 14000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 109) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhan tl
                  if (this.zone.map.mapId == 109) { //nhan tl
               if(Util.isTrue(1, 20000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhantl hsd 
                    if (this.zone.map.mapId == 109) { //nhan tl hsd 
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 109) { //ao
               if(Util.isTrue(1, 9500)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }} // 25s
             if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 109) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
        if (this.zone.map.mapId == 109) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 109) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 109) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//map109
             if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           //quan 
            if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// quan hsd //luwdev
            if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//gang
              if (this.zone.map.mapId == 107) {//luwdev gaggggggggggggggg
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// gang hsd //luwdev
             if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
               }}
           //dz
         // ngoc rong huyet long
             if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 6500)){
                 Item luw = ItemService.gI().createNewItem((short) (1828));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
               if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 7500)){
                 Item luw = ItemService.gI().createNewItem((short) (1827));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                 if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (1826));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                   if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (1825));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
           //giayyyyyyyyyyyyy
              if (this.zone.map.mapId == 107) {//luwdev 
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// giay hsd //luwdev
              if (this.zone.map.mapId == 107) {//luwdev 
               if(Util.isTrue(1, 14000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 107) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhan tl
                  if (this.zone.map.mapId == 107) { //nhan tl
               if(Util.isTrue(1, 20000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhantl hsd 
                    if (this.zone.map.mapId == 107) { //nhan tl hsd 
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 107) { //ao
               if(Util.isTrue(1, 9500)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }} // 25s
             if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 107) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
        if (this.zone.map.mapId == 107) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 107) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 107) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           //quan 
            if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// quan hsd //luwdev
            if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//gang
              if (this.zone.map.mapId == 106) {//luwdev gaggggggggggggggg
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// gang hsd //luwdev
             if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
               }}
           //dz
         // ngoc rong huyet long
             if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 6500)){
                 Item luw = ItemService.gI().createNewItem((short) (1828));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
               if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 7500)){
                 Item luw = ItemService.gI().createNewItem((short) (1827));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                 if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (1826));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                   if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (1825));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
           //giayyyyyyyyyyyyy
              if (this.zone.map.mapId == 106) {//luwdev 
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// giay hsd //luwdev
              if (this.zone.map.mapId == 106) {//luwdev 
               if(Util.isTrue(1, 14000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 106) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhan tl
                  if (this.zone.map.mapId == 106) { //nhan tl
               if(Util.isTrue(1, 20000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhantl hsd 
                    if (this.zone.map.mapId == 106) { //nhan tl hsd 
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 106) { //ao
               if(Util.isTrue(1, 9500)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }} // 25s
             if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 106) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
        if (this.zone.map.mapId == 106) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 106) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 106) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
 if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           //quan 
            if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// quan hsd //luwdev
            if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (560));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (558));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (556));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//gang
              if (this.zone.map.mapId == 110) {//luwdev gaggggggggggggggg
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 18000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// gang hsd //luwdev
             if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (564));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (566));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (562));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,7)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
               }}
           //dz
         // ngoc rong huyet long
             if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 6500)){
                 Item luw = ItemService.gI().createNewItem((short) (1828));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
               if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 7500)){
                 Item luw = ItemService.gI().createNewItem((short) (1827));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                 if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (1826));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
                   if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (1825));
                luw.itemOptions.add(new Item.ItemOption(21,80));
               InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được ngọc rồng huyết long");
                    }}
           //giayyyyyyyyyyyyy
              if (this.zone.map.mapId == 110) {//luwdev 
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 17000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}// giay hsd //luwdev
              if (this.zone.map.mapId == 110) {//luwdev 
               if(Util.isTrue(1, 14000)){
                 Item luw = ItemService.gI().createNewItem((short) (563));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (565));
                  luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                   luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
           if (this.zone.map.mapId == 110) {//luwdev
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (567));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhan tl
                  if (this.zone.map.mapId == 110) { //nhan tl
               if(Util.isTrue(1, 20000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}//nhantl hsd 
                    if (this.zone.map.mapId == 110) { //nhan tl hsd 
               if(Util.isTrue(1, 13000)){
                 Item luw = ItemService.gI().createNewItem((short) (561));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                     luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 110) { //ao
               if(Util.isTrue(1, 9500)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }} // 25s
             if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 15500)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 110) {
               if(Util.isTrue(1, 10000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                 InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
        if (this.zone.map.mapId == 110) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (555));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
          if (this.zone.map.mapId == 110) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (557));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            if (this.zone.map.mapId == 110) {
               if(Util.isTrue(2, 7000)){
                 Item luw = ItemService.gI().createNewItem((short) (559));
                   luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
                  luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
                InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               Service.gI().sendThongBao(player, "Bạn vừa nhận được 1 thứ gì đó... ");
                    }}
            // tu tien
                        if (this.zone.map.mapId == 181) {
               if(Util.isTrue(50, 200)){
                 Item luw = ItemService.gI().createNewItem((short) (1645));
              InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
                    }}  
                       if (this.zone.map.mapId == 181) {
               if(Util.isTrue(100, 200)){
                 Item luw = ItemService.gI().createNewItem((short) (1644));
              InventoryServiceNew.gI().addItemBag(player, luw);
                InventoryServiceNew.gI().sendItemBags(player);
               }}
        if (player.itemTime.isUseMayDo && Util.isTrue(8, 100) && this.tempId > 57 && this.tempId < 66) {
      
            list.add(new ItemMap(zone, 380, 1, x, player.location.y, player.id));
        }
        
        if (player.itemTime.isUseMayDo2 && Util.isTrue(1, 2000) && this.tempId > 77 && this.tempId < 80) {
      
            list.add(new ItemMap(zone, 1382, 1, x, player.location.y, player.id));
        }
        
        if (player.itemTime.isUseMayDo2 && Util.isTrue(1, 3000) && this.tempId > 77 && this.tempId < 80) {
      
            list.add(new ItemMap(zone, 1383, 1, x, player.location.y, player.id));
        }
        
        if (player.itemTimesieucap.isUseXiMuoi && Util.isTrue(1, 5000) && this.tempId > 77 && this.tempId < 80) {
      
            list.add(new ItemMap(zone, 1616, 1, x, player.location.y, player.id));
        }
        
//        if (player.itemTimesieucap.isUseXiMuoi && Util.isTrue(1, 10000) && this.tempId > 77 && this.tempId < 80) {
//      
//            list.add(new ItemMap(zone, 1402, 1, x, player.location.y, player.id));
//        }
//        
//        if (player.itemTimesieucap.isUseXiMuoi && Util.isTrue(1, 10000) && this.tempId > 77 && this.tempId < 80) {
//      
//            list.add(new ItemMap(zone, 1403, 1, x, player.location.y, player.id));
//        }
        int nPlSameClan = 0;
            for (Player pl : player.zone.getPlayers()) {
            if (!pl.equals(player) && pl.clan != null) {
                nPlSameClan++;
            }
        if (player.zone.map.mapId >= 27 && this.zone.map.mapId <= 27 && player.clan != null && nPlSameClan >= 1){
            if (Util.isTrue(1, 100)){ // cs bang
                list.add(new ItemMap(zone, 1192, 1, x, player.location.y, player.id));
        }
            }
                }
            

       
        tileVang = player.nPoint.tlGold / 100;
        if (Util.isTrue(5, 100)) {
            int vang = (Util.nextInt(30000, 50000) + Util.nextInt(30000, 50000) * tileVang);
            list.add(new ItemMap(zone, 190, vang, this.location.x, this.location.y, player.id));
        }
        
        Item item = player.inventory.itemsBody.get(1); // Sự kiện quần bơi
        if (this.zone.map.mapId > 0){
            if(item.isNotNullItem()){
            if (item.template.id == 1170){ // Id vật phẩm 1170
        if (Util.isTrue(5, 100)) {    
            list.add(new ItemMap(zone, Util.nextInt(1173,1177), 1, x, player.location.y, player.id));} // Rơi vật phẩm sự kiện 695 - 698
        }else if (item.template.id != 1170 && item.template.id != 1171 && item.template.id != 1172){
            if (Util.isTrue(0, 1))
            list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id)); // Rơi vàng
        }
        }
            
        }if (this.zone.map.mapId > 0){
            if(item.isNotNullItem()){
            if (item.template.id == 1171){
        if (Util.isTrue(5, 100)) {    
            list.add(new ItemMap(zone, Util.nextInt(1173,1177), 1, x, player.location.y, player.id));}
        }else if (item.template.id != 1170 && item.template.id != 1171 && item.template.id != 1172){
            if (Util.isTrue(0, 1))
            list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
        }
        }
            
        }if (this.zone.map.mapId > 0){
            if(item.isNotNullItem()){
            if (item.template.id == 1172){
        if (Util.isTrue(5, 100)) {    
            list.add(new ItemMap(zone, Util.nextInt(1173,1177), 1, x, player.location.y, player.id));}
        }else if (item.template.id != 1170 && item.template.id != 1171 && item.template.id != 1172){
            if (Util.isTrue(0, 1))
            list.add(new ItemMap(zone, 76, 1, x, player.location.y, player.id));
        }
        }
        }
        
        if ((zone.map.mapId >= 135 && zone.map.mapId <= 138) && Util.isTrue(100, 100)) {
            if (player.clan.BanDoKhoBau.level <= 10) {
                int min = 1000;
                int max = 1700;
                Random random = new Random();
                int randomvang = random.nextInt(max - min + 1) + min;
                 int randomvang2 = random.nextInt(max - min + 1) + min;
                        
                for (int i = 0; i < player.clan.BanDoKhoBau.level/2; i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang,this.location.x + i * 20, this.location.y, player.id);
                 //   ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                 //   Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.BanDoKhoBau.level/3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.BanDoKhoBau.level/4; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.BanDoKhoBau.level/4; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }
            }
            if (player.clan.BanDoKhoBau.level > 10 && player.clan.BanDoKhoBau.level <= 50) {
                int min = 1200;
                int max = 2000;
                Random random = new Random();
                int randomvang = random.nextInt(max - min + 1) + min;
                int randomvang2 = random.nextInt(max - min + 1) + min;
                        
                for (int i = 0; i < player.clan.BanDoKhoBau.level*(3/5); i++) {
                    ItemMap it = new ItemMap(this.zone, 76, randomvang,this.location.x + i * 20, this.location.y, player.id);
                 //   ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                 //   Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.BanDoKhoBau.level/2; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.BanDoKhoBau.level/3; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.BanDoKhoBau.level/3; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }
            } else if(player.clan.BanDoKhoBau.level > 50 && player.clan.BanDoKhoBau.level <= 80) {
                int min = 3000;
                int max = 3500;
                int minx = 42;
                int maxx = 1165;
                Random random = new Random();
                int randomvang2 = random.nextInt(max - min + 1) + min;
//                int randomtoado = ;
                for (int i = 0; i < player.clan.BanDoKhoBau.level/4; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    
                }
                for (int i = 0; i < player.clan.BanDoKhoBau.level/4; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.BanDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.BanDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }
            }else {
                int min = 3500;
                int max = 5500;
                int minx = 42;
                int maxx = 1165;
                Random random = new Random();
                int randomvang2 = random.nextInt(max - min + 1) + min;
//                int randomtoado = ;
                for (int i = 0; i < player.clan.BanDoKhoBau.level/3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2,this.location.x + i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                    
                }
                for (int i = 0; i < player.clan.BanDoKhoBau.level/3; i++) {
                    ItemMap it = new ItemMap(this.zone, 190, randomvang2, this.location.x - i * 20, this.location.y, player.id);
                    Service.gI().dropItemMap(this.zone, it);
                }
                for (int i = 0; i < player.clan.BanDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x + i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }for (int i = 0; i < player.clan.BanDoKhoBau.level/6; i++) {ItemMap it = new ItemMap(this.zone, 76, randomvang2, this.location.x + i * 20, this.location.y, player.id);
                //    ItemMap it2 = new ItemMap(this.zone, 861, 1,this.location.x - i * 17, this.location.y, player.id);
                //    Service.gI().dropItemMap(this.zone, it2);
                }
            }
        }

        if (Util.isTrue(40, 100) && this.zone.map.mapId > 155 && this.zone.map.mapId < 159) {
            list.add(new ItemMap(zone, 933, 1, this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(5, 100) && this.zone.map.mapId > 158 && this.zone.map.mapId < 160) {
            list.add(new ItemMap(zone, 934, 1, this.location.x, this.location.y, player.id));
        }       
        
        if (Util.isTrue(40, 100) && this.tempId < 1 && (this.zone.map.mapId == 131)) {
            list.add(new ItemMap(zone, 590, Util.nextInt(15, 30), this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(40, 100) && this.tempId < 1 && (this.zone.map.mapId == 132)) {
            list.add(new ItemMap(zone, 590, Util.nextInt(15, 30), this.location.x, this.location.y, player.id));
        }
        if (Util.isTrue(40, 100) && this.tempId < 1 && (this.zone.map.mapId == 133)) {
            list.add(new ItemMap(zone, 590, Util.nextInt(15, 30), this.location.x, this.location.y, player.id));
        }
        
        // mob rơi sự kiện
        
        if (player.zone.map.mapId >= 122 && this.zone.map.mapId <= 124) {
            player.NguHanhSonPoint += 1;
        }
        if (player.zone.map.mapId >= 196 && this.zone.map.mapId <= 197) {
            player.pointvongquay += 1;
        }
        
        
        
        if (Util.isTrue(30, 100) && this.tempId > 76 && this.tempId < 78) {
            byte random = 1;
            if (Util.isTrue(2, 100)) {
                random = 2;
            }
            Item i = Manager.RUBY_REWARDS.get(Util.nextInt(0, Manager.RUBY_REWARDS.size() - 1));
            i.quantity = random;
            InventoryServiceNew.gI().addItemBag(player, i);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được " + random + " hồng ngọc");
        }
    
        return list;
    }

    private ItemMap dropItemTask(Player player) {
        ItemMap itemMap = null;
        switch (this.tempId) {
            case ConstMob.KHUNG_LONG:
            case ConstMob.LON_LOI:
            case ConstMob.QUY_DAT:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_2_0) {
                    itemMap = new ItemMap(this.zone, 73, 1, this.location.x, this.location.y, player.id);
                }
                break;
        }
        switch (this.tempId) {
            case ConstMob.THAN_LAN_ME:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_9_1) {
                    itemMap = new ItemMap(this.zone, 20, 1, this.location.x, this.location.y, player.id);
                }
                break;
            case ConstMob.HEO_XAYDA_ME:
            case ConstMob.OC_MUON_HON:
            case ConstMob.OC_SEN:
                if (TaskService.gI().getIdTask(player) == ConstTask.TASK_15_1) {
                    itemMap = new ItemMap(this.zone, 85, 1, this.location.x, this.location.y, player.id);
                }
        }
        if (itemMap != null) {
            return itemMap;
        }
        return null;
    }

    private void sendMobStillAliveAffterAttacked(double dameHit, boolean crit) {
        Message msg;
        try {
            msg = new Message(-9);
            msg.writer().writeByte(this.id);
            msg.writer().writeInt(Util.TamkjllGH(this.point.gethp()));
            msg.writer().writeInt(Util.TamkjllGH(dameHit));
            msg.writer().writeBoolean(crit); // chí mạng
            msg.writer().writeInt(-1);
            Service.getInstance().sendMessAllPlayerInMap(this.zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private void BigbossAttack() {
        if (!isDie() && !this.effectSkill.isHaveEffectSkill() && Util.canDoWithTime(lastTimeAttackPlayer, 1000)) {
            Message msg = null;
            try {
                switch (this.tempId) {
                    case 70: // Hirudegarn 
                        if (!Util.canDoWithTime(lastTimeAttackPlayer, 3000)) {
                            return;
                        }
                        // 0: bắn - 1: Quật đuôi - 2: dậm chân - 3: Bay - 4: tấn công - 5: Biến hình - 6: Biến hình lên cấp
                        // 7: vận chiêu - 8: Di chuyển - 9: Die
                        int[] idAction = new int[]{1, 2, 7};
                        if (this.level >= 2) {
                            idAction = new int[]{1, 2};
                        }
                        action = action == 7 ? 0 : idAction[Util.nextInt(0, idAction.length - 1)];
                        int index = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player = zone.getPlayers().get(index);
                        if (player == null || player.isDie()) {
                            return;
                        }
                        if (action == 1) {
                            cx = (short) player.location.x;
                            Service.gI().sendBigBoss2(zone, 8, this);
                        }
                        msg = new Message(101);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 4) {
                            if (action == 1) {
                                msg.writer().writeByte(1);
                                int dame = (int) player.injured(player, (int) this.point.getDameAttack(), false, true);
                                if (dame <= 0) {
                                    dame = 1;
                                }
                                msg.writer().writeInt((int) player.id);
                                msg.writer().writeInt(dame);
                            } else if (action == 3) {
                                cx = (short) player.location.x;
                                msg.writer().writeShort(cx);
                                msg.writer().writeShort(this.location.y);
                            } else {
                                msg.writer().writeByte(zone.getNotBosses().size());
                                for (int i = 0; i < zone.getNotBosses().size(); i++) {
                                    Player pl = zone.getNotBosses().get(i);
                                    int dame = (int) player.injured(player, (int) this.point.getDameAttack(), false, true);
                                    if (dame <= 0) {
                                        dame = 1;
                                    }
                                    msg.writer().writeInt((int) pl.id);
                                    msg.writer().writeInt(dame);
                                }
                            }
                        } else {
                            if (action == 6 || action == 8) {
                                cx = (short) player.location.x;
                                msg.writer().writeShort(cx);
                                msg.writer().writeShort(this.location.y);
                            }
                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                case 71: // Vua Bạch Tuộc
                        int[] idAction2 = new int[]{3, 4, 5};
                        action = action == 7 ? 0 : idAction2[Util.nextInt(0, idAction2.length - 1)];
                        int index2 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player2 = zone.getPlayers().get(index2);
                        if (player2 == null || player2.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 5) {
                            if (action != 5) {
                                msg.writer().writeByte(1);
                                int dame = player2.clan.BanDoKhoBau.level * 3250 * 18 * 4;
                                msg.writer().writeInt((int) player2.id);
                                msg.writer().writeInt(dame);
                            }
                            if (action == 5) {
                                cx = (short) player2.location.x;
                                msg.writer().writeShort(cx);
                            }
                        } else {

                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;
                    case 72: // Rôbốt bảo vệ
                        int[] idAction3 = new int[]{0, 1, 2};
                        action = action == 7 ? 0 : idAction3[Util.nextInt(0, idAction3.length - 1)];
                        int index3 = Util.nextInt(0, zone.getPlayers().size() - 1);
                        Player player3 = zone.getPlayers().get(index3);
                        if (player3 == null || player3.isDie()) {
                            return;
                        }
                        msg = new Message(102);
                        msg.writer().writeByte(action);
                        if (action >= 0 && action <= 2) {
                            msg.writer().writeByte(1);
                            int dame = player3.clan.BanDoKhoBau.level * 3250 * 18 * 4;
                            msg.writer().writeInt((int) player3.id);
                            msg.writer().writeInt(dame);
                        }
                        Service.gI().sendMessAllPlayerInMap(zone, msg);
                        lastTimeAttackPlayer = System.currentTimeMillis();
                        break;        
                }
            } catch (Exception e) {
//                Util.debug("ERROR BIG BOSS");
            } finally {
                if (msg != null) {
                    msg.cleanup();
                    msg = null;
                }
            }
        }
    }
}
