package tutien.models.matches.pvp;

import barcoll.models.matches.PVP;
import barcoll.models.matches.TYPE_LOSE_PVP;
import barcoll.models.matches.TYPE_PVP;
import tutien.models.player.Player;
import barcoll.services.Service;
import tutien.services.func.ChangeMapService;
import tutien.utils.Util;

/**
 *
 * @Stole By barcoll
 */
public class TraThu extends PVP {

    public TraThu(Player p1, Player p2) {
        super(TYPE_PVP.TRA_THU, p1, p2);
    }

    @Override
    public void start() {
        ChangeMapService.gI().changeMap(p1,
                p2.zone,
                p2.location.x + Util.nextInt(-5, 5), p2.location.y);
        Service.getInstance().sendThongBao(p2, "Có người tìm tới bạn để trả thù");
        Service.getInstance().chat(p1, "Mày Tới Số Rồi Con Ạ!");
        super.start();
    }

    @Override
    public void finish() {
        
    }

    @Override
    public void update() {
        
    }

    @Override
    public void reward(Player plWin) {

    }

    @Override
    public void sendResult(Player plLose, TYPE_LOSE_PVP typeLose) {

    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
