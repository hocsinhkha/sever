/*
 * Beo Sờ tu đi ô
*/
package MaQuaTang;

import com.girlkun.database.GirlkunDB;
import tutien.models.item.Item.ItemOption;
import tutien.models.player.Player;
import barcoll.services.NpcService;
import barcoll.services.Service;
import tutien.utils.Logger;
 import tutien.utils.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Administrator
 */
public class MaQuaTangManager {
    public String name;
    public final ArrayList<MaQuaTang> listGiftCode = new ArrayList<>();

    private static MaQuaTangManager instance;

    public static MaQuaTangManager gI() {
        if (instance == null) {
            instance = new MaQuaTangManager();
        }
        return instance;
    }

    public void init() {
        try (Connection con = GirlkunDB.getConnection();) {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM giftcode");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MaQuaTang giftcode = new MaQuaTang();
                giftcode.code = rs.getString("code");
                giftcode.id = rs.getInt("id");
                giftcode.countLeft = rs.getInt("count_left");
//                giftcode.datecreate = rs.getTimestamp("datecreate");
//                giftcode.dateexpired = rs.getTimestamp("expired");
                JSONArray jar = (JSONArray) JSONValue.parse(rs.getString("detail"));
                if (jar != null) {
                    for (int i = 0; i < jar.size(); ++i) {
                        JSONObject jsonObj = (JSONObject) jar.get(i);
                        giftcode.detail.put(Integer.parseInt(jsonObj.get("id").toString()),
                                Integer.parseInt(jsonObj.get("quantity").toString()));
                        jsonObj.clear();
                    }
                }

                JSONArray option = (JSONArray) JSONValue.parse(rs.getString("itemoption"));
                System.out.println("GIFCODE => " + option.toString());
                if (option != null) {
                    for (int u = 0; u < option.size(); u++) {
                        JSONObject jsonobject = (JSONObject) option.get(u);
                        giftcode.option.add(new ItemOption(Integer.parseInt(jsonobject.get("id").toString()),
                                Integer.parseInt(jsonobject.get("param").toString())));
                        jsonobject.clear();

                    }

                }
                JSONArray listPlayer = (JSONArray) JSONValue.parse(rs.getString("listIdUser"));

                if (listPlayer != null) {
                    for (int i = 0; i < listPlayer.size(); ++i) {
                        giftcode.listIdPlayer.add(Integer.parseInt(listPlayer.get(i).toString()));
                    }
                }

                listGiftCode.add(giftcode);
            }
            con.close();
        } catch (Exception erorlog) {
            erorlog.printStackTrace();
        }
    }

    public void sizeList(Player pl) {
        Service.gI().sendThongBao(pl, "" + MaQuaTang.class);
    }

    public MaQuaTang checkUseGiftCode(int idPlayer, String code) {
        for (MaQuaTang giftCode : listGiftCode) {
            if (giftCode.code.equals(code) && giftCode.countLeft > 0 && !giftCode.isUsedGiftCode(idPlayer)) {
                giftCode.countLeft -= 1;
                giftCode.addPlayerUsed(idPlayer, giftCode.id);
                return giftCode;
            }
        }
        return null;
    }

    public void updateListUserCode(int idCode, ArrayList<Integer> UserCode) {
        Logger.warning("" + UserCode);
        try {
            GirlkunDB.executeUpdate("update giftcode set listIdUser = ? where id = ?", "" + UserCode,
                    idCode);
        } catch (Exception e) {
            Logger.warning("" + e);
        }
    }
}
//    public void checkInfomationGiftCode(Player p) {
//        StringBuilder sb = new StringBuilder();
//        for (MaQuaTang giftCode : listGiftCode) {
//            sb.append("Code: ").append(giftCode.code).append(", Số lượng: ").append(giftCode.countLeft).append("\b")
//                    .append(", Ngày tạo: ")
//                    .append(giftCode.datecreate).append("Ngày hết hạn").append(giftCode.dateexpired);
//        }
//
//        NpcService.gI().createTutorial(p, 5073, sb.toString());
//    }
//}