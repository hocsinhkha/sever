package tutien.models.player;

import tutien.models.shop.ShopServiceNew;
import barcoll.services.MapService;
import barcoll.consts.ConstMap;
import barcoll.models.map.Map;
import barcoll.models.map.Zone;
import barcoll.server.Manager;
import barcoll.services.MapService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import tutien.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class Referee1 extends Player {

    private long lastTimeChat;
    private Player playerTarget;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 10000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initReferee1() {
        init();
    }

    @Override
    public short getHead() {
        return 83;
    }

    @Override
    public short getBody() {
        return 84;
    }

    @Override
    public short getLeg() {
        return 85;
    }

    public void joinMap(Zone z, Player player) {
        MapService.gI().goToMap(player, z);
        z.load_Me_To_Another(player);
    }

   // @Override
   public void update() {
       if (Util.canDoWithTime(lastTimeChat, 5000)) {
          //  /Service.getInstance().chat(this, "Đại Hội Võ Thuật lần thứ 23 đã chính thức khai mạc");
           // Service.getInstance().chat(this, "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn");
           lastTimeChat = System.currentTimeMillis();
        }
   }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
            if (m.mapId == 189 || m.mapId == 190 || m.mapId == 191) {
                for (Zone z : m.zones) {
                    Referee1 pl = new Referee1();
                    pl.name = "Pôpô";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 1000000000;
                    pl.nPoint.hpg = 1000000000;
                    pl.nPoint.hp = 1000000000;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 223;
                    pl.location.y = 336;
                    pl.typePk = 5;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
                }
            }
        }
    }