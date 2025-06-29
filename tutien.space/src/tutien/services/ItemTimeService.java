package barcoll.services;

import barcoll.consts.ConstPlayer;
import tutien.models.item.Item;
import static tutien.models.item.ItemTime.*;
import static tutien.models.item.ItemTimeSieuCap.TIME_EAT_MEAL;
import static tutien.models.item.ItemTimeSieuCap.TIME_ITEM3;
import static tutien.models.item.ItemTimeSieuCap.TIME_XI_MUOI;
import static tutien.models.item.ItemTimeSieuCap.TIME_TRUNGTHU;
import static tutien.models.item.ItemTimeSieuCap.TIME_KEO;
import tutien.models.player.Fusion;
import tutien.models.map.BanDoKhoBau.BanDoKhoBau;
import tutien.models.map.ConDuongRanDoc.ConDuongRanDoc;
import tutien.models.map.GiaiCuuMiNuong.GiaiCuuMiNuong;
import tutien.models.map.DoanhTraiDocNhan.DoanhTrai;
import tutien.models.map.KhiGasHuyDiet.KhiGasHuyDiet;
import tutien.models.player.Player;
import tutien.services.func.TaiXiu;
import com.girlkun.network.io.Message;
import tutien.utils.Logger;


public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

    //gửi cho client
    public void sendAllItemTime(Player player) {
        sendTextDoanhTrai(player);
        sendTextBanDoKhoBau(player);
        sendTextGiaiCuuMiNuong(player);
        sendTextConDuongRanDoc(player);
        sendTextKhiGaHuyDiet(player);
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isBiNgo) {
            sendItemTime(player, 5138, (int) ((TIME_BI_NGO - (System.currentTimeMillis() - player.itemTime.lastTimeBiNgo)) / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
        if (player.itemTime.isUseMayDo2) {
            sendItemTime(player, 22071, (int) ((TIME_MAY_DO2 - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo2)) / 1000));
        }
        if (player.itemTime.isDuoikhi) {
            sendItemTime(player, player.itemTime.iconDuoi, (int) ((TIME_DUOI_KHI - (System.currentTimeMillis() - player.itemTime.lastTimeDuoikhi)) / 1000));
        }
        if (player.itemTime.isUseTDLT) {
            sendItemTime(player, 4387,player.itemTime.timeTDLT / 1000);
        }
        if (player.itemTimesieucap.isUseBoHuyet3) {
            sendItemTime(player, 14424, (int) ((TIME_ITEM3 - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeBoHuyet3)) / 1000));
        }
        if (player.itemTimesieucap.isUseBoKhi3) {
            sendItemTime(player, 14425, (int) ((TIME_ITEM3 - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeBoKhi3)) / 1000));
        }
        if (player.itemTimesieucap.isUseGiapXen3) {
            sendItemTime(player, 10712, (int) ((TIME_ITEM3 - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeGiapXen3)) / 1000));
        }
        if (player.itemTimesieucap.isUseCuongNo3) {
            sendItemTime(player, 14426, (int) ((TIME_ITEM3 - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeCuongNo3)) / 1000));
        }
        
        if (player.itemTimesieucap.isUseAnDanh3) {
            sendItemTime(player, 10717, (int) ((TIME_ITEM3 - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeAnDanh3)) / 1000));
        }
        if (player.itemTimesieucap.isKeo) {
            sendItemTime(player, 8243, (int) ((TIME_KEO - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeKeo)) / 1000));
        }
        if (player.itemTimesieucap.isUseXiMuoi) {
            sendItemTime(player, 22727, (int) ((TIME_XI_MUOI - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeUseXiMuoi)) / 1000));
        }
        if (player.itemTimesieucap.isUseTrungThu) {
            sendItemTime(player, player.itemTimesieucap.iconBanh, (int) ((TIME_TRUNGTHU - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeUseBanh)) / 1000));
        }
        if (player.itemTime.isUseBinhCanx2) {//bình cần x2
            sendItemTime(player, 22066, (int) ((TIME_BINH_CAN - (System.currentTimeMillis() - player.itemTime.lastTimeBinhCanx2)) / 1000));
        }
        if (player.itemTime.isUseBinhCanx5) {//bình cần x5
            sendItemTime(player, 22067, (int) ((TIME_BINH_CAN - (System.currentTimeMillis() - player.itemTime.lastTimeBinhCanx5)) / 1000));
        }
        if (player.itemTime.isUseBinhCanx7) {//bình cần x7
            sendItemTime(player, 22068, (int) ((TIME_BINH_CAN - (System.currentTimeMillis() - player.itemTime.lastTimeBinhCanx7)) / 1000));
        }
        if (player.itemTime.isUseBinhCanx10) {//binh cần x10
            sendItemTime(player, 22069, (int) ((TIME_BINH_CAN - (System.currentTimeMillis() - player.itemTime.lastTimeBinhCanx10)) / 1000));
        }
        if (player.itemTimesieucap.isEatMeal) {
            sendItemTime(player, player.itemTimesieucap.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeMeal)) / 1000));
        }
    }

    //bật tđlt
    public void turnOnTDLT(Player player, Item item) {
        int min = 0;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseTDLT = true;
        player.itemTime.timeTDLT = min * 60 * 1000;
        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
        sendCanAutoPlay(player);
        sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    //tắt tđlt
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (Item.ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param += (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }
        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (Exception e) {
            Logger.logException(ItemTimeService.class, e);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && !player.clan.haveGoneDoanhTrai
                && player.clan.timeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenDoanhTrai) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn: ", secondsLeft);
        }
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null
                && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Động kho báu: ", secondsLeft);
        }
    }
    public void sendTextTX(Player player) {
        if (TaiXiu.gI().goldTai > 0 || TaiXiu.gI().goldXiu > 0) {
            int secondsLeft = (int) ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis())/1000);
            sendTextTime(player, TX, "Tài Xỉu", secondsLeft);
            if (secondsLeft <=0){
                removeTextTX(player);
            }
        }
    }
    public void removeTextTX(Player player) {
        removeTextTime(player, TX);
    }
    
    public void sendTextKhiGaHuyDiet(Player player) {
        if (player.clan != null
                && player.clan.timeOpenKhiGaHuyDiet != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenKhiGaHuyDiet) / 1000);
            int secondsLeft = (KhiGasHuyDiet.TIME_KHI_GA_HUY_DIET / 1000) - secondPassed;
            sendTextTime(player, KHI_GA_HUY_DIET, "Khí gas hủy diệt: ", secondsLeft);
        }
    }
    public void sendTextConDuongRanDoc(Player player) {
          if (player.clan != null && !player.clan.haveGoneConDuongRanDoc
                && player.clan.timeOpenConDuongRanDoc != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenConDuongRanDoc) / 1000);
            int secondsLeft = (ConDuongRanDoc.TIME_CON_DUONG_RAN_DOC / 1000) - secondPassed;
            sendTextTime(player, CON_DUONG_RAN_DOC, "Con đường rắn độc: ", secondsLeft);
        }
    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }
    
    public void sendTextGiaiCuuMiNuong(Player player) {
        if (player.clan != null && !player.clan.haveGoneGiaiCuuMiNuong
                && player.clan.timeOpenGiaiCuuMiNuong != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenGiaiCuuMiNuong) / 1000);
            int secondsLeft = (GiaiCuuMiNuong.TIME_GIAI_CUU_MI_NUONG / 1000) - secondPassed;
            sendTextTime(player, GIAI_CUU_MI_NUONG, "Giải cứu mị nương: ", secondsLeft);
        }
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }
    public void removeTextGiaiCuuMiNuong(Player player) {
        removeTextTime(player, GIAI_CUU_MI_NUONG);
    }

    private void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
