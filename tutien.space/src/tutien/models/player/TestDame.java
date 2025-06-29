package tutien.models.player;

import tutien.models.shop.ShopServiceNew;
import barcoll.services.MapService;
import barcoll.consts.ConstMap;
import barcoll.consts.ConstPlayer;
import barcoll.models.map.Map;
import barcoll.models.map.Zone;
import barcoll.server.Manager;
import barcoll.services.MapService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.SkillService;
import tutien.utils.SkillUtil;
import tutien.utils.Util;
// đây
import java.util.ArrayList;
import java.util.List;

/**
 * @author BTH sieu cap vippr0
 */
public class TestDame extends Player {

    private long lastTimeChat;
//    protected Player playerTarger;

    private long lastTimeTargetPlayer;
    private long timeTargetPlayer = 5000;
    private long lastZoneSwitchTime;
    private long zoneSwitchInterval;
    private List<Zone> availableZones;

    public void initTestDame() {
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
    public void changeToTypePK() {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_ALL);
    }
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
    }

    protected long lastTimeAttack;
    

    @Override
    public void update() {
        active();
        if(this.isDie()){
            Service.getInstance().sendMoney(this);
            PlayerService.gI().hoiSinh(this);
            Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMp(this);
        }
    }

    private void init() {
        int id = -1000000;
        for (Map m : Manager.MAPS) {
           if (m.mapId == 189 || m.mapId == 190 || m.mapId == 191 || m.mapId == 14) {
                for (Zone z : m.zones) {
                    TestDame pl = new TestDame();
                    pl.name = "POPO";
                    pl.gender = 0;
                    pl.id = id++;
                    pl.nPoint.hpMax = 2000000000L;
                    pl.nPoint.hpg =   2000000000L;
                    pl.nPoint.hp =    2000000000L;
                    pl.nPoint.setFullHpMp();
                    pl.location.x = 223;
                    pl.location.y = 336;
                    joinMap(z, pl);
                    z.setReferee(pl);
                }
            }

        }
    }
}
