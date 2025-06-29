package tutien.models.npc.special;

import tutien.models.item.Item;
import tutien.models.player.Player;
import barcoll.services.PetService;
import barcoll.services.Service;
import tutien.services.func.ChangeMapService;
import tutien.utils.Logger;
import tutien.utils.Util;
import com.girlkun.network.io.Message;
/**
 *
 * @author Heroes x BTH
 * 
 */
public class DuaHau {
    private static final long DEFAULT_TIME_DONE = 120000;

    private Player player;
    public long lastTimeCreate;
    public long timeDone;

    private final short id = 51;

    public DuaHau(Player player, long lastTimeCreate, long timeDone) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
    }
//    public NOIBANH(Player player, long lastTimeCreate, long timeDone) {
//        this.player = player;
//        this.lastTimeCreate = lastTimeCreate;
//        this.timeDone = timeDone;
//    }
    public static void createduahau(Player player) {
        player.duahau = new DuaHau(player, System.currentTimeMillis(), DEFAULT_TIME_DONE);
    }
    public void sendduahau() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(4672);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Logger.logException(DuaHau.class, e);
        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public void openduahau(int gender) {

    }


    public void dispose(){
        this.player = null;
    }
}
