package tutien.models.map.GiaiCuuMiNuong;

import tutien.models.boss.list_boss.GiaiCuuMiNuong.MiNuong;
import tutien.models.boss.list_boss.GiaiCuuMiNuong.MiNuong2;
import tutien.models.boss.list_boss.GiaiCuuMiNuong.MiNuong3;
import tutien.models.player.Player;
import barcoll.services.Service;


public class GiaiCuuMiNuongService {

    private static GiaiCuuMiNuongService i;

    private GiaiCuuMiNuongService() {
    }

    public static GiaiCuuMiNuongService gI() {
        if (i == null) {
            i = new GiaiCuuMiNuongService();
        }
        return i;
    }

    public void openGiaiCuuMiNuong(Player player) {
        if (player.clan != null && player.clan.giaiCuuMiNuong == null) {
            GiaiCuuMiNuong giaicuuminuong = null;
            for (GiaiCuuMiNuong gcmn : GiaiCuuMiNuong.GIAI_CUU_MI_NUONG) {
                if (!gcmn.isOpened) {
                    giaicuuminuong = gcmn;
                    break;
                }
            }
            if (giaicuuminuong != null) {
                giaicuuminuong.opengiaicuuminuong(player, player.clan);
                try {
                    long totalDame = 0;
                    long totalHp = 0;
                    for (Player play : player.clan.membersInGame) {
                        totalDame += play.nPoint.dame;
                        totalHp += play.nPoint.hpMax;
                    }
                    long dame = (totalHp / 50);
                    long hp = (totalDame * 200);
                    if (dame >= 2000000000L) {
                        dame = 2000000000L;
                    }
                    if (hp >= 2000000000L) {
                        hp = 2000000000L;
                    }
                    long bossDamage = dame;
                    long bossMaxHealth = hp;
                    bossDamage = Math.min(bossDamage, 200000000L);
                    bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);

                    new MiNuong(player.clan.giaiCuuMiNuong.getMapById(188),
                            (int) dame / 10,
                            (int) 100000000
                    );
                    new MiNuong2(player.clan.giaiCuuMiNuong.getMapById(188),
                            (int) dame / 10,
                            (int) 100000000
                    );
                    new MiNuong3(player.clan.giaiCuuMiNuong.getMapById(188),
                            (int) dame / 10,
                            (int) 100000000
                    );

                } catch (Exception e) {
                }
            } else {
                Service.getInstance().sendThongBao(player, "Giải Cứu Mị Nương đã đầy, vui lòng quay lại sau");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Yêu cầu phải có bang hội mới có thể tham gia");
        }
    }
}
