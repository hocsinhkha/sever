package tutien.models.map.MapTuTien;

//import barcoll.models.player.Player;
//import barcoll.services.MapService;
//import barcoll.services.Service;
//import barcoll.services.func.ChangeMapService;
//import barcoll.utils.TimeUtil;
//import barcoll.utils.Util;
//
//import java.util.List;
//
//public class TuTien {
//
//
//    public static final byte HOUR_OPEN_MAP_TuTien = 18;
//    public static final byte MIN_OPEN_MAP_TuTien = 0;
//    public static final byte SECOND_OPEN_MAP_TuTien = 0;
//
//
//    public static final byte HOUR_CLOSE_MAP_TuTien = 23;
//    public static final byte MIN_CLOSE_MAP_TuTien = 0;
//    public static final byte SECOND_CLOSE_MAP_TuTien = 0;
//
//    public static final int AVAILABLE = 7;
//
//    private static TuTien i;
//
//    public static long TIME_OPEN_2h;
//    public static long TIME_CLOSE_2h;
//
//    private int day = -1;
//
//    public static TuTien gI() {
//        if (i == null) {
//            i = new TuTien();
//        }
//        i.setTimeJoinTuTien();
//        return i;
//    }
//
//    public void setTimeJoinTuTien() {
//        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
//            i.day = TimeUtil.getCurrDay();
//            try {
//                TIME_OPEN_2h = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN_MAP_TuTien + ":" + MIN_OPEN_MAP_TuTien + ":" + SECOND_OPEN_MAP_TuTien, "dd/MM/yyyy HH:mm:ss");
//                TIME_CLOSE_2h = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE_MAP_TuTien + ":" + MIN_CLOSE_MAP_TuTien + ":" + SECOND_CLOSE_MAP_TuTien, "dd/MM/yyyy HH:mm:ss");
//            } catch (Exception ignored) {
//            }
//        }
//    }
//
//
//    private void kickOutOfTuTien(Player player) {
//        if (MapService.gI().isMapMaBu(player.zone.map.mapId) && player.isPl()) {
//            Service.getInstance().sendThongBao(player, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
//            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
//        }
//    }
//
//    private void ketthuc2h(Player player) {
//        player.zone.finishmabu2h = true;
//        List<Player> playersMap = player.zone.getPlayers();
//        for (int i = playersMap.size() - 1; i >= 0; i--) {
//            Player pl = playersMap.get(i);
//            kickOutOfTuTien(pl);
//        }
//    }
//
//    public void joinMapTuTien(Player player) {
//        boolean changed = false;
//        if (player.clan != null) {
//            List<Player> players = player.zone.getPlayers();
//            for (Player pl : players) {
//                if (pl.clan != null && !player.equals(pl) && player.clan.equals(pl.clan) && !player.isBoss) {
//                    Service.getInstance().changeFlag(player, Util.nextInt(9, 10));
//                    changed = true;
//                    break;
//                }
//            }
//        }
//        if (!changed && !player.isBoss) {
//            Service.getInstance().changeFlag(player, Util.nextInt(9, 10));
//        }
//    }
//
//    public void update(Player player) {
//        if (player.zone == null || !MapService.gI().isMapBlackBallWar(player.zone.map.mapId)) {
//            try {
//                long now = System.currentTimeMillis();
//                if (now < TIME_OPEN_2h || now > TIME_CLOSE_2h) {
//                    ketthuc2h(player);
//                }
//            } catch (Exception ignored) {
//            }
//        }
//
//    }
//}
