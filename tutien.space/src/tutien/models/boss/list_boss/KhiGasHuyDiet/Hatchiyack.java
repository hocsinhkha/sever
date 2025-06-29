package tutien.models.boss.list_boss.KhiGasHuyDiet;
import Amodels.boss.BossData;
import Amodels.boss.BossManager;
import Amodels.boss.BossID;
import Amodels.boss.Boss;
import barcoll.consts.ConstPlayer;
import tutien.models.item.Item;
import tutien.models.map.BanDoKhoBau.BanDoKhoBau;
import barcoll.models.map.ItemMap;
import barcoll.models.map.Zone;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import barcoll.services.Service;
import tutien.utils.Util;


public class Hatchiyack extends Boss {
    private static final int[][] FULL_DEMON = new int[][]{{Skill.DEMON, 1}, {Skill.DEMON, 2}, {Skill.DEMON, 3}, {Skill.DEMON, 4}, {Skill.DEMON, 5}, {Skill.DEMON, 6}, {Skill.DEMON, 7}};

    public Hatchiyack(Zone zone , int level, int dame, int hp) throws Exception {
        super(BossID.TRUNG_UY_TRANG, new BossData(
                "Hatchiyack",
                ConstPlayer.TRAI_DAT,
                new short[]{639, 640, 641, -1, -1, -1},
                ((10000 + dame) * level),
                new long[]{((500000 + hp) * level)},
                new int[]{103},
                (int[][]) Util.addArray(FULL_DEMON),
                new String[]{},
                new String[]{"|-1|Nhóc con"},
                new String[]{},
                60
        ));
        this.zone = zone;
    }

    public void reward(Player plKill) {
        if (Util.isTrue(100,100)) {
            ItemMap it = new ItemMap(this.zone, 729, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 729), plKill.id);
            it.options.add(new Item.ItemOption(50, Util.nextInt(1, 45)));
            it.options.add(new Item.ItemOption(77, Util.nextInt(1, 45)));
            it.options.add(new Item.ItemOption(103, Util.nextInt(1, 45)));
            it.options.add(new Item.ItemOption(5, Util.nextInt(1, 20)));
            it.options.add(new Item.ItemOption(93, Util.nextInt(1, 3)));
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