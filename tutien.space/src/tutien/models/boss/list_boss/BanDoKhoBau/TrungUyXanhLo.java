package tutien.models.boss.list_boss.BanDoKhoBau;

import Amodels.boss.BossData;
import Amodels.boss.BossManager;
import Amodels.boss.BossID;
import Amodels.boss.Boss;
import barcoll.consts.ConstPlayer;
import tutien.models.map.BanDoKhoBau.BanDoKhoBau;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.Service;
import tutien.utils.Util;


public class TrungUyXanhLo extends Boss {
    private static final int[][] FULL_MASENKO = new int[][]{{Skill.MASENKO, 1}, {Skill.MASENKO, 2}, {Skill.MASENKO, 3}, {Skill.MASENKO, 4}, {Skill.MASENKO, 5}, {Skill.MASENKO, 6}, {Skill.MASENKO, 7}};

    public TrungUyXanhLo(Zone zone , int level, int dame, int hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "CỮU VĨ HÌNH",
                ConstPlayer.TRAI_DAT,
                new short[]{1636, 1637, 1638, -1, -1, -1},
                ((10000 + dame) * level),
                new long[]{((500000 + hp) * level)},
                new int[]{103},
                (int[][]) Util.addArray(FULL_MASENKO),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 861, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }
    @Override
    public void active() {
        super.active();
    }

    @Override
    public void joinMap() {
        super.joinMap();
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        this.dispose();
    }
}