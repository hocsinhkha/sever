/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MaQuaTang;

import tutien.models.item.Item.ItemOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class MaQuaTang {
    String code;
    int countLeft;
    int id;
    public HashMap<Integer, Integer> detail = new HashMap<>();
    public ArrayList<Integer> listIdPlayer = new ArrayList<>();
    public ArrayList<ItemOption> option = new ArrayList<>();
//    Timestamp datecreate;
//    Timestamp dateexpired;

    public boolean isUsedGiftCode(int idPlayer) {
        return listIdPlayer.contains(idPlayer);
    }

    public void addPlayerUsed(int idPlayer, int idCode) {
        listIdPlayer.add(idPlayer);
        MaQuaTangManager.gI().updateListUserCode(idCode, listIdPlayer);
    }

//    public boolean timeCode() {
//        return this.datecreate.getTime() > this.dateexpired.getTime() ? true : false;
//    }
}
