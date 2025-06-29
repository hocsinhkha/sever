/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutien.models.boss.list_boss.NgucTu;

import Amodels.boss.Boss;
import Amodels.boss.BossID;
import Amodels.boss.BossesData;
import barcoll.models.map.ItemMap;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.EffectSkillService;
import barcoll.services.PetService;
import barcoll.services.Service;
import tutien.utils.Util;
import java.util.Random;

/**
 *
 * @Stole By barcoll
 */
    public class Cumber extends Boss {

    public Cumber() throws Exception {
       super(BossID.CUMBER, BossesData.CUMBER);
   }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100,100)) {
            if (Util.isTrue(100, 100)) {
                if (Util.isTrue(100, 100)) {
                    if (Util.isTrue(100, 100)) {
                        if (Util.isTrue(100, 100)) {
                }
            }
            ItemMap it = new ItemMap(this.zone, 2153, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ItemMap it1 = new ItemMap(this.zone, 2154, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ItemMap it2 = new ItemMap(this.zone, 2155, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ItemMap it3 = new ItemMap(this.zone, 2156, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            ItemMap it4 = new ItemMap(this.zone, 1384, 3, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
            Service.getInstance().dropItemMap(this.zone, it1);
            Service.getInstance().dropItemMap(this.zone, it2);
            Service.getInstance().dropItemMap(this.zone, it3);
            Service.getInstance().dropItemMap(this.zone, it4);
        
}
    }
}
}
}
