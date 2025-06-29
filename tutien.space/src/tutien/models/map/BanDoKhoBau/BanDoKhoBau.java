package tutien.models.map.BanDoKhoBau;

import tutien.models.clan.Clan;
import barcoll.models.map.TrapMap;
import barcoll.models.map.Zone;
import tutien.models.mob.Mob;
import tutien.models.player.Player;
import barcoll.services.ItemTimeService;
import barcoll.services.MapService;
import barcoll.services.Service;
import tutien.services.func.ChangeMapService;
import tutien.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BTH
 */
public class BanDoKhoBau implements Runnable {

    public static final long POWER_CAN_GO_TO_DBKB = 2000000000;

    public static final List<BanDoKhoBau> BAN_DO_KHO_BAUS;
    public static final int MAX_AVAILABLE = 50;
    public static final int TIME_BAN_DO_KHO_BAU = 1800000;

    private Player player;

    static {
        BAN_DO_KHO_BAUS = new ArrayList<>();
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            BAN_DO_KHO_BAUS.add(new BanDoKhoBau(i));
        }
    }

    public int id;
    public byte level;
    public final List<Zone> zones;

    public Clan clan;
    public boolean isOpened;
    private long lastTimeOpen;
    private boolean running;
    private long lastTimeUpdate;

    public BanDoKhoBau(int id) {
        this.id = id;
        this.zones = new ArrayList<>();
        running = true;
//        new Thread(this).start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10000);
                if (Util.canDoWithTime(lastTimeUpdate, 10000)) {
                    update();
                    lastTimeUpdate = System.currentTimeMillis();
                }
            } catch (Exception ignored) {
            }

        }
    }

    public void update() {
        for (BanDoKhoBau bando : BAN_DO_KHO_BAUS) {
            if (bando.isOpened) {
//                if (Util.canDoWithTime(lastTimeOpen, TIME_BAN_DO_KHO_BAU)) {
//                    this.finish();
                }
            }
        }  

    public void openBanDoKhoBau(Player plOpen, Clan clan, byte level) {
        this.level = level;
        this.lastTimeOpen = System.currentTimeMillis();
        this.isOpened = true;
        this.clan = clan;
        this.clan.timeOpenBanDoKhoBau = this.lastTimeOpen;
        this.clan.playerOpenBanDoKhoBau = plOpen;
        this.clan.BanDoKhoBau = this;

        resetBanDo();
        ChangeMapService.gI().goToDBKB(plOpen);
        sendTextBanDoKhoBau();
    }

    private void resetBanDo() {
//        for (Zone zone : zones) {
//            for (TrapMap trap : zone.trapMaps) {
//                trap.dame = this.level * 10000;
//            }
//        }
        for (Zone zone : zones) {
            for (Mob m : zone.mobs) {
                m.initMobBanDoKhoBau(m, this.level);
                m.hoiSinhMob(m);
                m.hoiSinh();
                m.sendMobHoiSinh();
            }
        }
    }

    //kết thúc bản đồ kho báu
    public void finish() {
        List<Player> plOutBD = new ArrayList();
        for (Zone zone : zones) {
            List<Player> players = zone.getPlayers();
            for (Player pl : players) {
                plOutBD.add(pl);
                kickOutOfBDKB(pl);
            }

        }
        for (Player pl : plOutBD) {
            ChangeMapService.gI().changeMapBySpaceShip(pl, 5, -1, 64);
        }


        this.clan.BanDoKhoBau = null;
        this.clan = null;
        this.isOpened = false;
    }

    private void kickOutOfBDKB(Player player) {
        if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
            Service.getInstance().sendThongBao(player, "Hang Kho Báu Đã Sập Bạn Đang Được Đưa Ra Ngoài");
            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1038);
            running = false;
            this.clan.BanDoKhoBau = null;
        }
    }

    public Zone getMapById(int mapId) {
        for (Zone zone : zones) {
            if (zone.map.mapId == mapId) {
                return zone;
            }
        }
        return null;
    }

    public static void addZone(int idBanDo, Zone zone) {
        BAN_DO_KHO_BAUS.get(idBanDo).zones.add(zone);
    }

    private void sendTextBanDoKhoBau() {
        for (Player pl : this.clan.membersInGame) {
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }
    }
}
