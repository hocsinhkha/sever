package tutien.models.item;

import tutien.models.player.NPoint;
import tutien.models.player.Player;
import barcoll.services.Service;
import tutien.utils.Util;
import barcoll.services.ItemTimeService;


public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;
    public static final byte KHI_GA_HUY_DIET = 2;
    public static final byte CON_DUONG_RAN_DOC = 3;
    public static final byte GIAI_CUU_MI_NUONG = 4;
    public static final int TIME_ITEM = 600000;
    public static final int TIME_ITEM45P = 2700000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_MAY_DO2 = 1800000;
    public static final int TIME_EAT_MEAL = 600000;
    public static final int TIME_EAT_MEAL1 = 600000;
    public static final int TIME_BI_NGO = 1800000;
    public static final int TIME_DUOI_KHI = 600000;
    public static final int TIME_BINH_CAN = 1800000;
    private Player player;
    public static final byte TX = 33;
    public static final byte CL = 34;
    public boolean isUseBoHuyet;
    public boolean isUseBoKhi;
    public boolean isUseGiapXen;
    public boolean isUseCuongNo;
    public boolean isUseAnDanh;
    
    public boolean isOpenPower;
    public long lastTimeOpenPower;
    
    public long lastTimeBoHuyet;
    public long lastTimeBoKhi;
    public long lastTimeGiapXen;
    public long lastTimeCuongNo;
    public long lastTimeAnDanh;
    
    public boolean is1822;
    public boolean is1823;
    public boolean is1824;
    public boolean is1825;
    public boolean is1826;
    public boolean is1827;
    public boolean is1828;
    public boolean is1829;
    public boolean is1830;
    public boolean is1831;
    public boolean is1832;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;//lastime de chung 1 cai neu time = nhau
    public boolean isUseMayDo2;
    public long lastTimeUseMayDo2;
    
    public boolean isBiNgo;
    public long lastTimeBiNgo;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;
    
    public long lastTimeBinhCanx2;
    public long lastTimeBinhCanx5;
    public long lastTimeBinhCanx7;
    public long lastTimeBinhCanx10;
    
    
    public long lastTime1822;
    public long lastTime1823;
    public long lastTime1824;
    public long lastTime1825;
    public long lastTime1826;
    public long lastTime1827;
    public long lastTime1828;
    public long lastTime1829;
    public long lastTime1830;
    public long lastTime1831;
    public long lastTime1832;
    
    public boolean isUseBinhCanx2;
    public boolean isUseBinhCanx5;
    public boolean isUseBinhCanx7;
    public boolean isUseBinhCanx10;

    public boolean isDuoikhi;
    public long lastTimeDuoikhi;
    public int iconDuoi;
    
    

    public ItemTime(Player player) {
        this.player = player;
    }

    public void update() {
        if (isDuoikhi) {
            if (Util.canDoWithTime(lastTimeDuoikhi, TIME_DUOI_KHI)) {
                isDuoikhi = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseBoHuyet) {
            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
                isUseBoHuyet = false;
                Service.getInstance().point(player);
//                Service.getInstance().Send_Info_NV(this.player);
            }
        }
        
        if (isUseBoKhi) {
            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
                isUseBoKhi = false;
                Service.getInstance().point(player);
            }
        }
       
        if (isUseGiapXen) {
            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
                isUseGiapXen = false;
            }
        }
        if (isUseCuongNo) {
            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
                isUseCuongNo = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseAnDanh) {
            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
                isUseAnDanh = false;
            }
        }
        if (isBiNgo) {
            if (Util.canDoWithTime(lastTimeBiNgo, TIME_BI_NGO)) {
                isBiNgo = false;
            }
        }
        if (isUseMayDo) {
            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
                isUseMayDo = false;
            }
        }
        if (isUseMayDo2) {
            if (Util.canDoWithTime(lastTimeUseMayDo2, TIME_MAY_DO2)) {
                isUseMayDo2 = false;
            }
        }
        if (isUseBinhCanx2) {
            if (Util.canDoWithTime(lastTimeBinhCanx2, TIME_BINH_CAN)) {
                isUseBinhCanx2 = false;
            }
        }
        if (isUseBinhCanx5) {
            if (Util.canDoWithTime(lastTimeBinhCanx5, TIME_BINH_CAN)) {
                isUseBinhCanx5 = false;
            }
        }
        if (isUseBinhCanx7) {
            if (Util.canDoWithTime(lastTimeBinhCanx7, TIME_BINH_CAN)) {
                isUseBinhCanx7 = false;
            }
        }
        if (isUseBinhCanx10) {
            if (Util.canDoWithTime(lastTimeBinhCanx10, TIME_BINH_CAN)) {
                isUseBinhCanx10 = false;
            }
        }
        if (isUseTDLT) {
            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
                this.isUseTDLT = false;
                ItemTimeService.gI().sendCanAutoPlay(this.player);
            }
        }
    }
    
    public void dispose(){
        this.player = null;
    }
}
