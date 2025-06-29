package barcoll.services;
import com.girlkun.database.GirlkunDB;
import tutien.jdbc.daos.PlayerDAO;
import tutien.models.item.Item;

import tutien.models.player.NPoint;
import tutien.models.player.Pet;
import tutien.models.player.Player;
import barcoll.server.Client;
import tutien.utils.Util;
import java.awt.Point;

public class OpenPowerService {

    public static final int COST_SPEED_OPEN_LIMIT_POWER = 500000000;

    private static OpenPowerService i;

    private OpenPowerService() {

    }

    public static OpenPowerService gI() {
        if (i == null) {
            i = new OpenPowerService();
        }
        return i;
    }

    public boolean openPowerBasic(Player player) {
        byte curLimit = player.nPoint.limitPower;
        if (curLimit < NPoint.MAX_LIMIT) {
            if (!player.itemTime.isOpenPower && player.nPoint.canOpenPower()) {
                player.itemTime.isOpenPower = true;
                player.itemTime.lastTimeOpenPower = System.currentTimeMillis();
                ItemTimeService.gI().sendAllItemTime(player);
                return true;
            } else {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
                return false;
            }
        } else {
            Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            return false;
        }
    }
    public boolean chuyenSinh(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 0)  {
            Service.gI().sendThongBao(player, "Hành trang không đủ chỗ trống"); 
            } else {
        if (player.nPoint.power >= 200000000000L) {
            Item linhhon = null;
            Item fire = null;
            Item water = null;
            Item wood = null;
            Item metal = null;
            Item nr6s = null;
            Item nr7s = null;
             Item ngocchau = null;
            try {
            linhhon = InventoryServiceNew.gI().findItemBag(player, 1822);
            fire = InventoryServiceNew.gI().findItemBag(player, 1823);
            water = InventoryServiceNew.gI().findItemBag(player, 1824);
            wood = InventoryServiceNew.gI().findItemBag(player, 1825);
            metal = InventoryServiceNew.gI().findItemBag(player, 1826);
            nr6s = InventoryServiceNew.gI().findItemBag(player, 1827);
            nr7s = InventoryServiceNew.gI().findItemBag(player, 1828);
             ngocchau = InventoryServiceNew.gI().findItemBag(player, 2025);
            } catch (Exception e) {
            }
            if (linhhon == null || linhhon.quantity < 10) {
            Service.gI().sendThongBao(player, "Bạn còn thiếu ngọc rồng 1 sao huyết long");
            }else if (fire == null || fire.quantity < 10) {    
            Service.gI().sendThongBao(player, "Bạn còn thiếu ngọc rồng 2 sao huyết long");
            }else if (water == null || water.quantity < 10) {    
             Service.gI().sendThongBao(player, "Bạn còn thiếu ngọc rồng 3 sao huyết long"); 
             }else if (wood == null || wood.quantity < 10) {    
             Service.gI().sendThongBao(player, "Bạn còn thiếu ngọc rồng 4 sao huyết long"); 
             }else if (metal == null || metal.quantity < 10) {    
             Service.gI().sendThongBao(player, "Bạn còn thiếu gọc rồng 5 sao huyết long");
             }else if (nr6s == null || nr6s.quantity < 10) {    
             Service.gI().sendThongBao(player, "Bạn còn thiếu ngọc rồng 6 sao huyết long");
             }else if (nr7s == null || nr7s.quantity < 10) {    
             Service.gI().sendThongBao(player, "Bạn còn thiếu ngọc rồng 7 sao huyết long");
                }else if (ngocchau == null || ngocchau.quantity < 5000) {    
             Service.gI().sendThongBao(player, "Bạn Không Đủ 5k Xu");
            } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.gI().sendThongBao(player, "Hành trang của bạn không đủ chỗ trống");
            } else {
            InventoryServiceNew.gI().subQuantityItemsBag(player, linhhon, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, fire, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, water, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, wood, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, metal, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, nr6s, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, nr7s, 10);
            InventoryServiceNew.gI().subQuantityItemsBag(player, ngocchau, 7000);
//          player.diemchuyensinh +=1;
//            Item hopquatet = ItemService.gI().createNewItem((short) 1454, 1);
//            InventoryServiceNew.gI().addItemBag(player, hopquatet);
//            InventoryServiceNew.gI().sendItemBags(player);
//            Service.gI().sendThongBao(player, "Bạn nhận được Hộp Quà");
            player.nPoint.power -= (player.nPoint.power - 2000);
               player.chuyenSinh++;
            player.nPoint.hpg += 50000;
            player.nPoint.dameg += 10000;
            player.nPoint.mpg += 50000;
            Service.getInstance().point(player);
            Client.gI().kickSession(player.getSession());
        if (player.nPoint.power < 199999999999L) {
            }          
        if (!player.isPet) {
            Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được reset");
        } else {
            Service.gI().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được reset");
        }}
            return true; 
        } else {
            if (!player.isPet) {
                Service.gI().sendThongBao(player, "Bạn không đủ điều kiện để chuyển sinh");
            } else {
                Service.gI().sendThongBao(((Pet) player).master, "Bạn không đủ điều kiện để chuyển sinh");
            }
            return false;
        }
        }
        return true;
    }
     public boolean openPowerSpeed(Player player) {
        if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
//            if (player.nPoint.power >= 17900000000L) {
            player.nPoint.limitPower++;
            if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
                player.nPoint.limitPower = NPoint.MAX_LIMIT;
            }
            if (!player.isPet) {
                Service.gI().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
            } else {
                Service.gI().sendThongBao(((Pet) player).master, "Giới hạn sức mạnh của đệ tử đã được tăng lên 1 bậc");
            }
            return true;
//            } else {
//                if (!player.isPet) {
//                    Service.gI().sendThongBao(player, "Sức mạnh của bạn không đủ để thực hiện");
//                } else {
//                    Service.gI().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử không đủ để thực hiện");
//                }
//                return false;
//            }
        } else {
            if (!player.isPet) {
                Service.gI().sendThongBao(player, "Sức mạnh của bạn đã đạt tới mức tối đa");
            } else {
                Service.gI().sendThongBao(((Pet) player).master, "Sức mạnh của đệ tử đã đạt tới mức tối đa");
            }
            return false;
        }
    }

}
