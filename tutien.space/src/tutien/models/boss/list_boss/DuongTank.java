package barcoll.models.boss.list_boss;

import barcoll.consts.ConstPlayer;
import Amodels.boss.*;
import tutien.models.item.Item;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import barcoll.server.Client;
import barcoll.services.EffectSkillService;
import barcoll.services.InventoryServiceNew;
import barcoll.services.ItemService;
import barcoll.services.MapService;
import barcoll.services.Service;
import tutien.services.func.ChangeMapService;
import tutien.utils.Util;
import tutien.models.player.Inventory;

/**
 * @author Administrator
 */
public class DuongTank extends Boss {

    public DuongTank(int bossID, BossData bossData, Zone zone, int x, int y) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
    }

    long lasttimemove;
    
    @Override
    public void reward(Player plKill) {
        ItemMap it = new ItemMap(this.zone, 76, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                this.location.y - 24), plKill.id);
//        it.options.add(new Item.ItemOption(73, 1));
        Service.getInstance().dropItemMap(this.zone, it);
     //   plKill.diemhotong += 1;
    }

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) { // người khác không thể tấn công
            this.changeToTypePK();
        }
        if (this.playerTarger != null && Client.gI().getPlayer(this.playerTarger.id) == null) {
            playerTarger.haveDuongTang = false;
            this.leaveMap();
        }
        if (Util.getDistance(playerTarger, this) > 500 && this.zone == this.playerTarger.zone) {
            Service.gI().sendThongBao(this.playerTarger, "Đi quá xa ,  Bé rồng  đã rời đi ! ");
            playerTarger.haveDuongTang = false;
            this.leaveMap();
        }
        if (Util.getDistance(playerTarger, this) > 300 && this.zone == this.playerTarger.zone) {
            Service.gI().sendThongBao(this.playerTarger, "Khoảng cách qua xa, Bé rồng sắp rời xa bạn!! ");
        }
        if (this.playerTarger != null && Util.getDistance(playerTarger, this) <= 300) {
            int dir = this.location.x - this.playerTarger.location.x <= 0 ? -1 : 1;
            if (Util.canDoWithTime(lasttimemove, 1000)) {
                lasttimemove = System.currentTimeMillis();
                this.moveTo(this.playerTarger.location.x + Util.nextInt(dir == -1 ? 0 : -30, dir == -1 ? 10 : 0), this.playerTarger.location.y);
            }
        }
        if (this.playerTarger != null && playerTarger.haveDuongTang && this.zone.map.mapId == this.mapCongDuc) { // xử lý khi đến map muốn đến
            playerTarger.haveDuongTang = false;
            playerTarger.diemhotong += 100 ;
            playerTarger.inventory.ruby+=1000;
            Item xuacquy = ItemService.gI().createNewItem((short) (2052));
            int xu = Util.nextInt(1,5);
            xuacquy.quantity = xu;
            playerTarger.inventory.gem += 2000;
            Service.getInstance().sendMoney(playerTarger);
            InventoryServiceNew.gI().addItemBag(playerTarger, xuacquy);
           Service.getInstance().sendThongBaoOK(playerTarger, "Bạn nhận được 2k hồng ngọc, "+xu+" Xu ác quỷ !");
            this.leaveMap();
        }
        if (this.playerTarger != null && this.zone != null && this.zone.map.mapId != this.playerTarger.zone.map.mapId) {
            ChangeMapService.gI().changeMap(this, this.playerTarger.zone, this.playerTarger.location.x, this.playerTarger.location.y);
        }
        if (Util.canDoWithTime(this.lastTimeAttack, 10000)) {
            Service.gI().chat(this,  playerTarger.name + ", Hãy đưa ta đến " + MapService.gI().getMapById(this.mapCongDuc).mapName);
            this.lastTimeAttack = System.currentTimeMillis();
        }
    }

//    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage;
            }
            if (plAtt != this.playerTarger) {
                damage = this.nPoint.hpMax / 120;
//                damage = damage / 30;
            } else {
                damage = 0;
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
    public void joinMap() {
        if (zoneFinal != null) {
            joinMapByZone(zoneFinal);
            this.notifyJoinMap();
            return;
        }
        if (this.zone == null) {
            if (this.parentBoss != null) {
                this.zone = parentBoss.zone;
            } else if (this.lastZone == null) {
                this.zone = getMapJoin();
            } else {
                this.zone = this.lastZone;
            }
        }
        if (this.zone != null) {
            if (this.currentLevel == 0) {
                if (this.parentBoss == null) {
                    ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
                } else {
                    ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);;
                }
//                this.wakeupAnotherBossWhenAppear();
            } else {
                ChangeMapService.gI().changeMap(this, this.zone, this.location.x, this.location.y);
            }
            Service.getInstance().sendFlagBag(this);
            this.notifyJoinMap();
        }
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}
