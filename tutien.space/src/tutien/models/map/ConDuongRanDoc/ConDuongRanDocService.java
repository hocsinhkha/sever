package tutien.models.map.ConDuongRanDoc;


import tutien.models.boss.list_boss.ConDuongRanDoc.Nappa;
import tutien.models.boss.list_boss.doanh_trai.NinjaTim;
import tutien.models.item.Item;
import tutien.models.boss.list_boss.KhiGasHuyDiet.Hatchiyack;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen2;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen3;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen4;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen5;
import tutien.models.boss.list_boss.ConDuongRanDoc.Saibamen6;
import tutien.models.boss.list_boss.ConDuongRanDoc.Vegeta;
import tutien.models.map.KhiGasHuyDiet.KhiGasHuyDiet;
import static tutien.models.map.KhiGasHuyDiet.KhiGasHuyDiet.TIME_KHI_GA_HUY_DIET;
import barcoll.models.map.Zone;
import tutien.models.mob.Mob;
import tutien.models.player.Player;
import barcoll.services.InventoryServiceNew;
import barcoll.services.MapService;
import barcoll.services.Service;
import tutien.services.func.ChangeMapService;
import tutien.utils.Logger;
import tutien.utils.Util;
import java.util.List;

/**
 *
 * @author BTH
 *
 */
public class ConDuongRanDocService {

    private static ConDuongRanDocService i;

    private ConDuongRanDocService() {

    }

    public static ConDuongRanDocService gI() {
        if (i == null) {
            i = new ConDuongRanDocService();
        }
        return i;
    }
    
    public void openConDuongRanDoc(Player player, byte level) {
        if (level >= 1 && level <= 110) {
            if (player.clan != null && player.clan.ConDuongRanDoc == null) {
                Item item = InventoryServiceNew.gI().findItemBag(player, 2046);
                if (item != null && item.quantity > 0) {
                    ConDuongRanDoc conDuongRanDoc = null;
                    for (ConDuongRanDoc cdrd : ConDuongRanDoc.CON_DUONG_RAN_DOCS) {
                        if (!cdrd.isOpened) {
                            conDuongRanDoc = cdrd;
                            break;
                        }
                    }
                    if (conDuongRanDoc != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                        InventoryServiceNew.gI().sendItemBags(player);
                        conDuongRanDoc.openConDuongRanDoc(player, player.clan, level);
                        try {
                            long bossDamage = (20 * level);
                            long bossMaxHealth = (2 * level);
                            bossDamage = Math.min(bossDamage, 200000000L);
                            bossMaxHealth = Math.min(bossMaxHealth, 2000000000L);
                            
                            Nappa boss = new Nappa(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            ); 
                            Saibamen boss1  = new Saibamen(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );   
                            Saibamen2 boss2 = new Saibamen2(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                            Saibamen3 boss3 = new Saibamen3(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                            Saibamen4 boss4 = new Saibamen4(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                            Saibamen5 boss5 = new Saibamen5(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                            Saibamen6 boss6 = new Saibamen6(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                            Vegeta boss7 = new Vegeta(
                                    player.clan.ConDuongRanDoc.getMapById(144),
                                    player.clan.ConDuongRanDoc.level,
                                    (int) bossDamage,
                                    (int) bossMaxHealth
                            );
                        } catch (Exception exception) {
                            Logger.logException(ConDuongRanDocService.class, exception, "Error initializing boss");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Con Đường Rắn Độc Đã Đầy, vui lòng quay lại sau");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Yêu cầu có vé truy nã Cđrđ ");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
}
