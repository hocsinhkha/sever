package Amodels.boss;

import tutien.models.boss.list_boss.HuyDiet.Champa;
import tutien.models.boss.list_boss.HuyDiet.ThanHuyDiet;
import tutien.models.boss.list_boss.HuyDiet.ThienSuWhis;
import tutien.models.boss.list_boss.HuyDiet.Vados;
import tutien.models.boss.list_boss.HangBang.Frost;
import tutien.models.boss.list_boss.BLACK.Black;
import tutien.models.boss.list_boss.BLACK.BlackGokuBase;
import tutien.models.boss.list_boss.BLACK.BlackGokuTl;
import tutien.models.boss.list_boss.BLACK.SuperBlack2;
import tutien.models.boss.list_boss.BLACK.ZamasKaio;
import tutien.models.boss.list_boss.nappa.Kuku;
import tutien.models.boss.list_boss.nappa.Rambo;
import tutien.models.boss.list_boss.nappa.MapDauDinh;
import tutien.models.boss.list_boss.android.Android15;
import tutien.models.boss.list_boss.android.Android14;
import tutien.models.boss.list_boss.android.Android13;
import tutien.models.boss.list_boss.android.Pic;
import tutien.models.boss.list_boss.android.Android19;
import tutien.models.boss.list_boss.android.Poc;
import tutien.models.boss.list_boss.android.DrKore;
import tutien.models.boss.list_boss.android.KingKong;
import tutien.models.boss.list_boss.NgucTu.CoolerGold;
import tutien.models.boss.list_boss.NgucTu.SongokuTaAc;
import tutien.models.boss.list_boss.NgucTu.Cumber;
import tutien.models.boss.list_boss.FideBack.Kingcold;
import tutien.models.boss.list_boss.Doraemon.Nobita;
import tutien.models.boss.list_boss.Doraemon.Xeko;
import tutien.models.boss.list_boss.Doraemon.Xuka;
import tutien.models.boss.list_boss.Doraemon.Chaien;
import tutien.models.boss.list_boss.Doraemon.Doraemon;
import tutien.models.boss.list_boss.Broly.Broly;
import tutien.models.boss.list_boss.FideBack.FideRobot;
import tutien.models.boss.list_boss.fide.Fide;
import tutien.models.boss.list_boss.Mabu12h.MabuBoss;
import tutien.models.boss.list_boss.Mabu12h.BuiBui;
import tutien.models.boss.list_boss.Mabu12h.BuiBui2;
import tutien.models.boss.list_boss.Mabu12h.Drabura;
import tutien.models.boss.list_boss.Mabu12h.Drabura2;
import tutien.models.boss.list_boss.Mabu12h.Yacon;
import tutien.models.boss.list_boss.cell.SieuBoHung;
import tutien.models.boss.list_boss.cell.XenBoHung;
import tutien.models.boss.list_boss.cell.Xencon;
import tutien.models.boss.list_boss.Virus.Anime;
import tutien.models.boss.list_boss.Virus.Anime2;
import tutien.models.boss.list_boss.Virus.Anime3;
import tutien.models.boss.list_boss.Virus.Anime4;
import tutien.models.boss.list_boss.Corona.Corona1;
import tutien.models.boss.list_boss.Corona.Corona2;
import tutien.models.boss.list_boss.Corona.Corona3;
import tutien.models.boss.list_boss.Corona.Corona4;
//import barcoll.models.boss.list_boss.Luwdev.thantai;
import tutien.models.player.Player;
import com.girlkun.network.io.Message;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen;
import tutien.models.boss.list_boss.Mabu2h.bossMabu2h;
import barcoll.models.boss.list_boss.TauPayPay;
import tutien.models.boss.list_boss.ginyu.TDST;
//import barcoll.models.boss.list_boss.Luwdev.thantai;
import barcoll.server.ServerManager;
import barcoll.services.ItemMapService;
import barcoll.services.MapService;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.TimeZone;

public class BossManager implements Runnable {

    private static BossManager I;
    public static final byte ratioReward = 2;

    public static BossManager gI() {
        if (BossManager.I == null) {
            BossManager.I = new BossManager();
        }
        return BossManager.I;
    }
    
    

    private BossManager() {
        this.bosses = new ArrayList<>();
    }

    private boolean loadedBoss;
    private final List<Boss> bosses;

    public List<Boss> getBosses() {
        return this.bosses;
    }

    public void addBoss(Boss boss) {
        this.bosses.add(boss);
    }

    public void removeBoss(Boss boss) {
        this.bosses.remove(boss);
    }

    public void loadBoss() {
        if (this.loadedBoss) {
            return;
        }
        try {
              this.createBoss(BossID.Anime_11);
            this.createBoss(BossID.Anime_22);
            this.createBoss(BossID.Anime_33);
            this.createBoss(BossID.Anime_44);
            this.createBoss(BossID.Anime_11);
            this.createBoss(BossID.Anime_22);
            this.createBoss(BossID.Anime_33);
            this.createBoss(BossID.Anime_44);
            
            this.createBoss(BossID.corona1);
            this.createBoss(BossID.corona2);
            this.createBoss(BossID.corona3);
            this.createBoss(BossID.corona4);
            this.createBoss(BossID.corona1);
            this.createBoss(BossID.corona2);
            this.createBoss(BossID.corona3);
            this.createBoss(BossID.corona4);
            
            this.createBoss(BossID.TDST);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
//            this.createBoss(BossID.BROLY);
            this.createBoss(BossID.PIC);
            this.createBoss(BossID.POC);
            this.createBoss(BossID.KING_KONG);
            this.createBoss(BossID.CUMBER);
            this.createBoss(BossID.SONGOKU_TA_AC);
            this.createBoss(BossID.COOLER_GOLD);
            this.createBoss(BossID.XEN_BO_HUNG);
            this.createBoss(BossID.SIEU_BO_HUNG);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
            this.createBoss(BossID.XEN_CON_1);
        //    this.createBoss(BossID.THIEN_SU_VADOS);
        //    this.createBoss(BossID.THIEN_SU_WHIS);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.ZAMASZIN);
            this.createBoss(BossID.BLACK2);
            this.createBoss(BossID.BLACK);
            this.createBoss(BossID.BLACK3);
            this.createBoss(BossID.KUKU);
            this.createBoss(BossID.MAP_DAU_DINH);
            this.createBoss(BossID.RAMBO);
            this.createBoss(BossID.FIDE);
            this.createBoss(BossID.DR_KORE);
            this.createBoss(BossID.ANDROID_14);
            this.createBoss(BossID.DORAEMON);
            this.createBoss(BossID.NOBITA);
            this.createBoss(BossID.XUKA);
            this.createBoss(BossID.CHAIEN);
            this.createBoss(BossID.XEKO);
            this.createBoss(BossID.FROST);
//            this.createBoss(BossID.thantai);
        //    this.createBoss(BossID.quy_la_la1);
        //    this.createBoss(BossID.ong_gia_la);
            this.createBoss(BossID.Mabu2h);
            this.createBoss(BossID.Mabu2h);
            this.createBoss(BossID.Mabu2h);
            this.createBoss(BossID.Mabu2h);
        //    this.createBoss(BossID.TAU_PAY_PAY_M);
        //    this.createBoss(BossID.TAU_PAY_PAY_M);
        //    this.createBoss(BossID.TAU_PAY_PAY_M);
        //    this.createBoss(BossID.TAU_PAY_PAY_M);
        //    this.createBoss(BossID.TAU_PAY_PAY_M);
        //    this.createBoss(BossID.TAU_PAY_PAY_M);
          
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadedBoss = true;
        new Thread(BossManager.I, "Update boss").start();
    }

    public Boss createBoss(int bossID) {
        try {
            switch (bossID) {
                case BossID.KUKU:
                    return new Kuku();
                case BossID.MAP_DAU_DINH:
                    return new MapDauDinh();
                case BossID.RAMBO:
                    return new Rambo();
                case BossID.DRABURA:
                    return new Drabura();
                case BossID.DRABURA_2:
                    return new Drabura2();
                case BossID.BUI_BUI:
                    return new BuiBui();
                case BossID.BUI_BUI_2:
                    return new BuiBui2();
                case BossID.YA_CON:
                    return new Yacon();
                case BossID.MABU_12H:
                    return new MabuBoss();
                case BossID.TAU_PAY_PAY_M:
                    return new TauPayPay();
                case BossID.FIDE:
                    return new Fide();
                case BossID.DR_KORE:
                    return new DrKore();
                case BossID.ANDROID_19:
                    return new Android19();
                case BossID.ANDROID_13:
                    return new Android13();
                case BossID.ANDROID_14:
                    return new Android14();
                case BossID.ANDROID_15:
                    return new Android15();
                case BossID.PIC:
                    return new Pic();
                case BossID.POC:
                    return new Poc();
                case BossID.KING_KONG:
                    return new KingKong();
                case BossID.TDST:
                    return new TDST();
                case BossID.XEN_BO_HUNG:
                    return new XenBoHung();
                case BossID.SIEU_BO_HUNG:
                    return new SieuBoHung();
                case BossID.VUA_COLD:
                    return new Kingcold();
                case BossID.FIDE_ROBOT:
                    return new FideRobot();
                    case BossID.Anime_11:
                    return new Anime();
                    case BossID.Anime_22:
                    return new Anime2();
                    case BossID.Anime_33:
                    return new Anime3();
                    case BossID.Anime_44:
                    return new Anime4();
                case BossID.corona1:
                    return new Corona1(); 
                case BossID.corona2:
                    return new Corona2(); 
                case BossID.corona3:
                    return new Corona3(); 
                case BossID.corona4:
                    return new Corona4();     
                case BossID.ZAMASZIN:
                    return new ZamasKaio();
                case BossID.BLACK2:
                    return new SuperBlack2();
                case BossID.BLACK1:
                    return new BlackGokuTl();
                case BossID.BLACK:
                    return new Black();
                case BossID.BLACK3:
                    return new BlackGokuBase();
                case BossID.XEN_CON_1:
                    return new Xencon();
                case BossID.XUKA:
                    return new Xuka();
                case BossID.NOBITA:
                    return new Nobita();
                case BossID.XEKO:
                    return new Xeko();
                case BossID.CHAIEN:
                    return new Chaien();
                case BossID.DORAEMON:
                    return new Doraemon();
                case BossID.THAN_HUY_DIET_CHAMPA:
                    return new Champa();
                case BossID.THIEN_SU_VADOS:
                    return new Vados();
                case BossID.THAN_HUY_DIET:
                    return new ThanHuyDiet();
                case BossID.THIEN_SU_WHIS:
                    return new ThienSuWhis();
                case BossID.COOLER_GOLD:
                    return new CoolerGold();
                case BossID.CUMBER:
                    return new Cumber();
                case BossID.SONGOKU_TA_AC:
                    return new SongokuTaAc();
                case BossID.FROST:
                    return new Frost();
                case BossID.BROLY:
                    return new Broly();
//                case BossID.thantai:
//                    return new thantai();
                case BossID.Mabu2h:
                    return new bossMabu2h();
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existBossOnPlayer(Player player) {
        return player.zone.getBosses().size() > 0;
    }

//    public void showListBoss(Player player) {
////        if (!player.isAdmin()) {
////            return;
////        }
//        Message msg;
//        try {
//            msg = new Message(-96);
//            msg.writer().writeByte(0);
//            msg.writer().writeUTF("Danh Sách Boss");
//            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])).count());
//            for (int i = 0; i < bosses.size(); i++) {
//                Boss boss = this.bosses.get(i);
//                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])) {
//                    continue;
//                }
//                msg.writer().writeInt(i);
//                msg.writer().writeInt(0);
//                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
//                if (player.getSession().version > 214) {
//                    msg.writer().writeShort(-1);
//                }
//                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
//                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
//                msg.writer().writeUTF(boss.data[0].getName());
//                if (boss.zone != null) {
//                    msg.writer().writeUTF("On");
//                    msg.writer().writeUTF(boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
//                } else {
//                    msg.writer().writeUTF("Off");
//                    msg.writer().writeUTF("Chưa có thông tin !");
//                }
//            }
//            player.sendMessage(msg);
//            msg.cleanup();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void showListBoss(Player player) {
        if (!player.isAdmin()) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer()
                    .writeByte(
                            (int) bosses.stream()
                                    .filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
//                                            && !MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
//                                            && !MapService.gI().isMapDiaNguc(boss.data[0].getMapJoin()[0])
//                                            && !MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
//                                            && !MapService.gI().isMapMabu13h(boss.data[0].getMapJoin()[0])
//                                            && !MapService.gI().isMapMiNuong(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                                            && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0]))

                                    .count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                   //     || MapService.gI().isMapSatan(boss.data[0].getMapJoin()[0])
                   ///     || MapService.gI().isMapDiaNguc(boss.data[0].getMapJoin()[0])
                   //     || MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])
                   //     || MapService.gI().isMapVodai(boss.data[0].getMapJoin()[0])
                    //    || MapService.gI().isMapMabu13h(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                    //    || MapService.gI().isMapMiNuong(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])

                ) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214 || player.getSession().version < 231) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                    msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF(
                            boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
         //   Logger.logException(Manager.class, e, "Lỗi show list boss");
        }
    }

    
    public void dobossmember(Player player) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Boss");
            msg.writer().writeByte((int) bosses.stream().filter(boss -> !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapGiaiCuuMiNuong1(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapHuyDiet(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMap22h(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapGiaiCuuMiNuong(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapChienTruong(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])
                    && !MapService.gI().isMapBlackBallWar(boss.data[0].getMapJoin()[0])).count());
            for (int i = 0; i < bosses.size(); i++) {
                Boss boss = this.bosses.get(i);
                if (MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapMaBu(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapDoanhTrai(boss.data[0].getMapJoin()[0])
                        || MapService.gI().isMapGiaiCuuMiNuong1(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapGiaiCuuMiNuong(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapKhiGaHuyDiet(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapHuyDiet(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMap22h(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapBanDoKhoBau(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapChienTruong(boss.data[0].getMapJoin()[0]) 
                        || MapService.gI().isMapConDuongRanDoc(boss.data[0].getMapJoin()[0])) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data[0].getOutfit()[0]);
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data[0].getOutfit()[1]);
                msg.writer().writeShort(boss.data[0].getOutfit()[2]);
                msg.writer().writeUTF(boss.data[0].getName());
                if (boss.zone != null) {
                   msg.writer().writeUTF("Sống");
                    msg.writer().writeUTF("Dịch chuyển");
                } else {
                    msg.writer().writeUTF("Chết");
                    msg.writer().writeUTF("Chết rồi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            System.out.println("iii");
        }
    }

    public synchronized void callBoss(Player player, int mapId) {
        try {
            if (BossManager.gI().existBossOnPlayer(player)
                    || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                    || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                return;
            }
            Boss k = null;
            if (k != null) {
                k.currentLevel = 0;
                k.joinMapByZone(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boss getBossById(int bossId) {
        return BossManager.gI().bosses.stream().filter(boss -> boss.id == bossId && !boss.isDie()).findFirst().orElse(null);
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (Boss boss : this.bosses) {
                    boss.update();
                }
                Thread.sleep(150 - (System.currentTimeMillis() - st));
            } catch (Exception ignored) {
            }

        }
    }
}
