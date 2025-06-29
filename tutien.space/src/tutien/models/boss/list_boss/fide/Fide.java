package tutien.models.boss.list_boss.fide;

import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Util;


public class Fide extends Boss {

   
     
    public Fide() throws Exception {
        super(BossID.FIDE, BossesData.FIDE_DAI_CA_1, BossesData.FIDE_DAI_CA_2, BossesData.FIDE_DAI_CA_3);
    }
    @Override
  
    public void reward(Player plKill) {
    if(Util.isTrue(100,100)){
        ItemMap it = new ItemMap(this.zone, Util.nextInt(17, 19), 1, this.location.x, this.location.y, plKill.id);
        Service.gI().dropItemMap(this.zone, it);
    }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
      @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;

}





















