package tutien.models.boss.list_boss.ginyu;

import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;
import java.util.Random;


public class TDST extends Boss {

    public TDST() throws Exception {
        super(BossID.TDST, BossesData.SO_4, BossesData.SO_3, BossesData.SO_2, BossesData.SO_1, BossesData.TIEU_DOI_TRUONG);
    }

    @Override
    public void moveTo(int x, int y) {
        if (this.currentLevel == 1) {
            return;
        }
        super.moveTo(x, y);
    }

    @Override
    public void reward(Player plKill) {
     plKill.Tamkjlltutien[0] += 10000;
                plKill.ExpTamkjll += 200000;
                Service.gI().sendMoney(plKill);
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x + 70, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 1, this.location.x + 0, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 1, this.location.x - 50, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 2, this.location.x - 100, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 4, this.location.x - 59, this.location.y, plKill.id));
                 Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 2025, 1, this.location.x - 55, this.location.y, plKill.id));
           Service.gI().sendThongBao(plKill, "Bạn nhận đc 10k EXP Tu Tiên và 200k Vi do kết liễu boss.");

    
        super.reward(plKill);
        if (this.currentLevel == 1) {
            return;
        }
    }

    @Override
    protected void notifyJoinMap() {
        if (this.currentLevel == 1) {
            return;
        }
        super.notifyJoinMap();
    }
    @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if (Util.canDoWithTime(st, 300000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st = System.currentTimeMillis();
    }
    private long st;
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
