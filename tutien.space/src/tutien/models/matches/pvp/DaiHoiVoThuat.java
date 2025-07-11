package tutien.models.matches.pvp;

import barcoll.models.matches.PVP;
import barcoll.models.matches.TYPE_LOSE_PVP;
import barcoll.models.matches.TYPE_PVP;
import tutien.models.player.Player;
import barcoll.server.Manager;
import tutien.utils.Util;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class DaiHoiVoThuat implements Runnable{
    public ArrayList<Player> listReg = new ArrayList<>();
    public ArrayList<Player> listPlayerWait = new ArrayList<>();
    public String NameCup;
    public String[] Time;
    public int gem;
    public int gold;
    public int min_start;
    public int min_start_temp;
    public int min_limit;
    public int round = 1;
    
    public int Hour;
    public int Minutes;
    public int Second;
    
    private static DaiHoiVoThuat instance;    

    public static DaiHoiVoThuat gI() {
        if (instance == null) {
            instance = new DaiHoiVoThuat();
        }
        return instance;
    }
    
    public DaiHoiVoThuat getDaiHoiNow(){
        for(DaiHoiVoThuat dh : Manager.LIST_DHVT){
            if(dh != null && Util.contains(dh.Time, String.valueOf(Hour))){
                return dh;
            }
        }
        return null;
    }
    
    public String Info() {
        for(DaiHoiVoThuat daihoi : Manager.LIST_DHVT){
            if (daihoi.gold > 0) {
                return "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,14,18h\bGiải Siêu cấp 1: 9,13,19h\bGiải Siêu Cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\n"
                        + "Giải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng\n"
                        + "Lệ phí đăng ký các giải đấu\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng";
            } else if (daihoi.gem > 0) {
                return "Lịch thi đấu trong ngày\bGiải Nhi đồng: 8,14,18h\bGiải Siêu cấp 1: 9,13,19h\bGiải Siêu Cấp 2: 10,15,20h\bGiải Siêu cấp 3: 11,16,21h\bGiải Ngoại hạng: 12,17,22,23h\n"
                        + "Giải thưởng khi thắng mỗi vòng\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng\n"
                        + "Lệ phí đăng ký các giải đấu\bGiải Nhi đồng: 2 ngọc\bGiải Siêu cấp 1: 4 ngọc\bGiải Siêu cấp 2: 6 ngọc\bGiải Siêu cấp 3: 8 ngọc\bGiải Ngoại hạng: 20.000 vàng";
            }
        }
        return "Đã hết thời gian đăng ký vui lòng đợi đến giải đấu sau\b";
    }

    
    @Override
    public void run() {
        while (true) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            try { 
                Second = calendar.get(Calendar.SECOND);
                Minutes = calendar.get(Calendar.MINUTE);
                Hour = calendar.get(Calendar.HOUR_OF_DAY);
               
                DaiHoiVoThuatService.gI(getDaiHoiNow()).Update();
                Thread.sleep(1000);
            }catch(Exception e){
            }
        }
    }
}





















