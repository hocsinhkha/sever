package tutien.services.func;

import barcoll.models.map.Zone;
import tutien.models.player.Player;
import com.girlkun.network.io.Message;
import barcoll.services.Service;


public class EffectMapService {

    private static EffectMapService i;

    private EffectMapService() {

    }

    public static EffectMapService gI() {
        if (i == null) {
            i = new EffectMapService();
        }
        return i;
    }

    public void sendEffectMapToPlayer(Player player, int id, int layer, int loop, int x, int y, int delay) {
        Message msg;
        try {
            msg = new Message(113);
            msg.writer().writeByte(id);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeShort(delay);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendEffectMapToAllInMap(Zone zone, int id, int layer, int loop, int x, int y, int delay) {
        Message msg;
        try {
            msg = new Message(113);
            msg.writer().writeByte(loop);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeShort(delay);
            Service.getInstance().sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

}
