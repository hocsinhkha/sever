package tutien.services.func;

import barcoll.services.Service;
import barcoll.services.ItemService;
import barcoll.services.InventoryServiceNew;
import barcoll.services.RewardService;
import barcoll.consts.ConstNpc;
import tutien.models.item.Item;
import tutien.models.item.Item.ItemOption;
import barcoll.models.map.ItemMap;
import barcoll.models.npc.Npc;
import static barcoll.models.npc.NpcFactory.tosukaio;
import static barcoll.models.npc.NpcFactory.trieuhoithu;
import barcoll.models.npc.NpcManager;
import tutien.models.player.Player;
import barcoll.server.Manager;
import barcoll.server.ServerNotify;
import com.girlkun.network.io.Message;
import tutien.utils.Logger;
import tutien.utils.Util;

import java.util.*;
import java.util.stream.Collectors;


public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;

    private static final int COST = 500000000;

    private static final int TIME_COMBINE = 1500;
    private static final int RUBY = 20000;
    private static final byte MAX_STAR_ITEM = 20;
    private static final byte MAX_LEVEL_ITEM = 8;
    private static final byte MAX_STAR_KHAM_DA = 10;
    private static final byte MAX_LEVEL_PET = 8;
    private static final byte MAX_LEVEL_PET2 = 6;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int CHUYEN_HOA_TRANG_BI = 502;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;  //c2
    public static final int NANG_CAP_BONG_TAI_CAP3 = 512;  //c3
    public static final int NANG_CAP_BONG_TAI_CAP4 = 518;  //c4
    public static final int NANG_CAP_BONG_TAI_CAP5 = 527;  //c5
    public static final int MO_CHI_SO_BONG_TAI = 519;
    public static final int PS_HOA_TRANG_BI = 2150;
    public static final int TAY_PS_HOA_TRANG_BI = 2151;
    public static final int THANG_HOA_NGOC_BOI = 700;
    public static final int THANG_CAP_NGOC_BOI = 701;
    public static final int CHE_TAO_TRANG_BI_TS = 520;
    public static final int CHE_TAO_TRANG_BI_TL = 521; // barcoll
    public static final int CHE_TAO_TRANG_BI_HD = 522;
    public static final int GIA_HAN_VAT_PHAM = 2152;
    public static final int AN_TRANG_BI = 517;
    public static final int NANG_CAP_CHAN_MENH = 5380;
    public static final int NANG_NGOC_BOI = 5381;
    
    
 //   public static final int NANG_CAP_BONG_TAI_CAP3 = 517;
 //   public static final int MO_CHI_SO_BONG_TAI_CAP3 = 518;
 //   public static final int NANG_CAP_BONG_TAI_CAP4 = 523;
 //   public static final int MO_CHI_SO_BONG_TAI_CAP4 = 524;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int REN_KIEM_Z = 525;
    
    private static final int GOLD_BONG_TAI = 500_000_000;
    private static final int GOLD_KIEM_Z = 200_000_000;
    private static final int GEM_BONG_TAI = 5_000;
    private static final int GEM_KIEM_Z = 1_000;
    private static final int RATIO_BONG_TAI = 50;
    private static final int RATIO_NANG_CAP = 45;
    private static final int RATIO_KIEM_Z2 = 40;
    public static final int NANG_CAP_DO_KICH_HOAT = 550;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int NANG_CAP_SKH_VIPhd = 555;
    public static final int NANG_CAP_SKH_VIPts = 556;
    
    public static final int kh_T = 551;
    public static final int kh_Tl = 552; // barcoll
    public static final int kh_Hd = 553;
    public static final int kh_Ts = 554;
    
    public static final int PHAN_RA_TRANG_BI1 = 618;
    public static final int PHAN_RA_TRANG_BI2 = 619;
    public static final int PHAN_RA_TRANG_BI3 = 620;
    
    public static final int KHAM_DA_HP = 505;
    public static final int KHAM_DA_MP = 506;
    public static final int KHAM_DA_DAME = 507;
    
    public static final int NANG_CAP_PET = 509;
    public static final int NANG_CAP_PET2 = 515;
    public static final int NANG_CAP_PK = 611;
    
    public static final int CUONG_HOA = 612; // ấn
    
    
    public static final int NANG_KICH_HOAT_VIP = 614;
    public static final int NANG_KICH_HOAT_VIP2 = 613;
    
    public static final int DOI_HOP_QUA = 508;


    private final Npc baHatMit;
    private final Npc trieuhoithu;
    private final Npc phithang;
    private final Npc thiensuwhis2;
    private final Npc tosukaio;
    private final Npc whis;
    private final Npc npsthiensu64;
    private final Npc thoNgoc;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.trieuhoithu = NpcManager.getNpc(ConstNpc.TRIEU_HOI_THU);
        this.phithang = NpcManager.getNpc(ConstNpc.PHI_THANG);
        this.thiensuwhis2 = NpcManager.getNpc(ConstNpc.thien_su_whis_2);
        this.tosukaio = NpcManager.getNpc(ConstNpc.TO_SU_KAIO);
        this.whis = NpcManager.getNpc(ConstNpc.WHIS);
        this.npsthiensu64 = NpcManager.getNpc(ConstNpc.WHIS);
        this.thoNgoc = NpcManager.getNpc(ConstNpc.THO_NGOC);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }

    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type   kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            if (player.iDMark.getNpcChose() != null) {
                msg.writer().writeShort(player.iDMark.getNpcChose().tempId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int i = 0; i < index.length; i++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[i]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 454) {
                            bongTai = item;
                        } else if (item.template.id == 1390) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 500) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1 và X500 Mảnh vỡ bông tai cấp 2", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1 và X500 Mảnh vỡ bông tai cấp 2", "Đóng");
                }
                break;
                
                case NANG_KICH_HOAT_VIP:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    //Item item1 = player.combineNew.itemsCombine.get(1);
                    //if (isTrangBiGod1(item) && isTrangBiThuong(item1)) {
                    if (isTrangBiHakai(item) ) {
                        player.combineNew.goldCombine = 10000;
                        String npcSay ="Con có muốn nâng cấp trang bị :\n"+ item.template.name + " \n|3|Trở thành trang bị Kích Hoạt\n";
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 2025);
                            if (thoivang != null && thoivang.quantity >= 5000 )  {
                                npcSay += "Cần 5K Xu \n" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Không đủ 5K Ngọc  ";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho 1 trang bị Hủy Diệt ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;
                case DOI_HOP_QUA:
                if (player.combineNew.itemsCombine.size() == 4) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    Item item3 = player.combineNew.itemsCombine.get(3);
                    if (isChuvan(item) && isChusu(item1) && isChunhu(item2) && isChuy(item3)) {
                        player.combineNew.goldCombine = 2000000000;
                        String npcSay ="Con có muốn chúc phúc:\n"+ item.template.name + "để nhận hộp quà không?";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            trieuhoithu.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần 2tỷ vàng vàng");
                        } else {
                            npcSay += " Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            trieuhoithu.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.trieuhoithu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "hãy chọn 4 câu đố theo thứ tự: Vạn-sự-như-ý", "Đóng");
                    }
                } else {
                    this.trieuhoithu.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để chúc phúc", "Đóng");
                }
                break;
                case NANG_KICH_HOAT_VIP2:
                if (player.combineNew.itemsCombine.size() == 5) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    Item item3 = player.combineNew.itemsCombine.get(3);
                    Item item4 = player.combineNew.itemsCombine.get(4);
                    
                    if (isTrangBiHakai(item) && isTrangBiHakai(item1) && isTrangBiHakai(item2) && isTrangBiHakai(item3) && isTrangBiHakai(item4) ) {
                    //if (isTrangBiGod1(item) ) {
                        player.combineNew.goldCombine = 100000;
                        if (player.gender == 0){
                            String npcSay ="Con có muốn nâng cấp trang bị của mình\nTrang bị nâng cấp sẽ phụ thuộc Hành Tinh của Áo \n|3|Tỉ lệ thấp nhận được trang bị Thiên Sứ kích hoạt\nChắc chắn nhận được trang bị cấp Jean";
                            if (player.combineNew.goldCombine <= player.inventory.ruby) {
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n" + player.combineNew.goldCombine + " Hồng Ngọc\n");
                            } else {
                                npcSay += "|3|Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.ruby) + " Hồng Ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        }if (player.gender == 1){
                            String npcSay ="Con có muốn nâng cấp trang bị của mình\nTrang bị nâng cấp sẽ phụ thuộc trang bị Hủy diệt đầu tiên  \n|3|Tỉ lệ thấp nhận được trang bị Thiên Sứ kích hoạt\nChắc chắn nhận được trang bị cấp Vàng Zealot";
                            if (player.combineNew.goldCombine <= player.inventory.ruby) {
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n" + player.combineNew.goldCombine + " Hồng Ngọc\n");
                            } else {
                                npcSay += "|3|Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.ruby) + " Hồng Ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        }
                        if (player.gender == 2){
                            String npcSay ="Con có muốn nâng cấp trang bị của mình\nTrang bị nâng cấp sẽ phụ thuộc Hành Tinh của Áo \n|3|Tỉ lệ thấp nhận được trang bị Thiên Sứ kích hoạt\nChắc chắn nhận được trang bị cấp Lưỡng Long";
                            if (player.combineNew.goldCombine <= player.inventory.ruby) {
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\n" + player.combineNew.goldCombine + " Hồng Ngọc\n");
                            } else {
                                npcSay += "|3|Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.ruby) + " Hồng Ngọc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        }
                    } else {
                         this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho 1 trang bị Thần linh ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");                
                }
                break; 
                case NANG_CAP_PET2:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    if (isTrangBiGod(item1) && isLinhThu(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_LEVEL_PET) {
                        player.combineNew.goldCombine = 5000;
                            player.combineNew.ngusacCombine = getngusacKhamDa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa2(star);
                            
                        String npcSay = "Con có muốn nâng cấp linh thú :\n" + item.template.name + " không \n";   
                        if (star == 0) {
                            npcSay += "|7|Tỉ lệ thành công: 80%" + "\n";
                        }
                        if (star == 1) {
                            npcSay += "|7|Tỉ lệ thành công: 70%" + "\n";
                        }
                        if (star == 2) {
                            npcSay += "|7|Tỉ lệ thành công: 60%" + "\n";
                        }
                        if (star == 3) {
                            npcSay += "|7|Tỉ lệ thành công: 50%" + "\n";
                        }
                        if (star == 4) {
                            npcSay += "|7|Tỉ lệ thành công: 40%" + "\n";
                        }
                        if (star == 5) {
                            npcSay += "|7|Tỉ lệ thành công: 30%" + "\n";
                        }
                        if (star == 6) {
                            npcSay += "|7|Tỉ lệ thành công: 20%" + "\n";
                        }
                        if (star == 7) {
                            npcSay += "|7|Tỉ lệ thành công: 10%" + "\n";
                        }
                        Item dangusac = InventoryServiceNew.gI().findItemBag(player, 2025);
                        
                        if (dangusac != null && dangusac.quantity >=  player.combineNew.ngusacCombine )  {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.ngusacCombine) + " Xu \n" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
//                        if (player.combineNew.goldCombine <= player.inventory.ruby ) {
//                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
//                                    "Nâng cấp\ncần " + player.combineNew.goldCombine + " hồng ngọc " );
                            
                        } else {
                            npcSay += "Còn thiếu hồng ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    }else{
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị đã quá cấp", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho linh thú và 1 món đồ thần vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;
                case CUONG_HOA:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (istrangbiKH(item)) {
                        player.combineNew.goldCombine = 2000000000;
                        boolean star = false;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 36) {
                                star = true;
                                break;
                            }
                        }
                        if (!star) {
                            player.combineNew.dakham = 50;
//                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);
                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 34 && io.optionTemplate.id != 35 && io.optionTemplate.id != 36) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + 10 + "%" + "\n";
                           Item dakham = InventoryServiceNew.gI().findItemBag(player, 1616); //Util.nextInt(2055, 2057));
                            if (dakham != null && dakham.quantity >=  player.combineNew.dakham && player.inventory.gold >= player.combineNew.goldCombine)  {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.dakham) + " Đá ngọc bảo  \n và Cần " +  player.combineNew.ngusacCombine + " Xu " ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Không đủ XU rồi bé ơi";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ có thể nâng cấp vật phẩm chưa có ấn khảm", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể khảm ấn tộc", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để khảm ấn tộc", "Đóng");
                }
                break;
            case NANG_CAP_PK:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
//                    Item item2 = player.combineNew.itemsCombine.get(2);
                    if (isTrangBiGod(item1) && isPhuKien(item) ) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_LEVEL_PET2) {
                        player.combineNew.goldCombine = 5000;
                            player.combineNew.ngusacCombine = 20;
                            player.combineNew.ratioCombine = getRatioPhuKien(star);
                            
                        String npcSay = "Con có muốn nâng cấp linh thú :\n" + item.template.name + " không \n";   
                        if (star == 0) {
                            npcSay += "|7|Tỉ lệ thành công: 50%" + "\n";
                        }
                        if (star == 1) {
                            npcSay += "|7|Tỉ lệ thành công: 40%" + "\n";
                        }
                        if (star == 2) {
                            npcSay += "|7|Tỉ lệ thành công: 30%" + "\n";
                        }
                        if (star == 3) {
                            npcSay += "|7|Tỉ lệ thành công: 20%" + "\n";
                        }
                        if (star == 4) {
                            npcSay += "|7|Tỉ lệ thành công: 10%" + "\n";
                        }
                        if (star == 5) {
                            npcSay += "|7|Tỉ lệ thành công: 10%" + "\n";
                        }
                                               
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                            if (thoivang != null && thoivang.quantity >=  player.combineNew.ngusacCombine )  {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.ngusacCombine) + " Thỏi vàng \n" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Thiếu nguyên liệu ";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                    }else{
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị đã quá cấp", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho vật phẩm đeo lưng và 1 món đồ thần vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;  
                
                
                case AN_TRANG_BI:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        Item dangusac = player.combineNew.itemsCombine.get(1);
                        if (isTrangBiAn(item)) {
                            if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1401 || dangusac.template.id == 1403 || dangusac.template.id == 1402) && dangusac.quantity >= 99) {
                                String npcSay = item.template.name + "\n|2|";
                                for (Item.ItemOption io : item.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn biến trang bị " + item.template.name + " thành\n"
                                        + "trang bị Ấn không?\b|4|Đục là lên\n"
                                        + "|7|Cần 99 " + dangusac.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể hóa ấn", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
                
                case NANG_CAP_BONG_TAI_CAP3: //c3
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1389) {
                            bongTai = item;
                        } else if (item.template.id == 1391) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 1000) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 2 và X1000 Mảnh vỡ bông tai cấp 3", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 2 và X1000 Mảnh vỡ bông tai cấp 3", "Đóng");
                }
                break;
                
                case NANG_NGOC_BOI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item  ngocboi1 = null, TV = null, NRO = null;
                    
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id == 457) {
                                TV = item;
                            } else if (item.template.id == 16) {
                                NRO = item; 
                            } else if (item.template.id == 1700
                                    || item.template.id == 1701
                                    || item.template.id == 1702
                                    || item.template.id == 1703
                                    || item.template.id == 1704
                                    || item.template.id == 1705
                                    || item.template.id == 1706
                                    || item.template.id == 1707) {
                                ngocboi1 = item;                                
                            }  
                        }
                    }                    
                    
                
                    if ( ngocboi1 == null || TV == null || NRO == null  ){
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần Ngọc Bội và X49 Thỏi Vàng và x10 3 sao", "Đóng");
                    }
                    if (player.inventory.ruby < 1000) {
                    Service.getInstance().sendThongBao(player, "Chuẩn bị đủ 1K Hồng Ngọc Và x99 TV hãy đến tìm ta");
                        return;
                    }
                    else if (ngocboi1 != null && TV != null && NRO != null ) {
                        String npcSay = "|6|" + ngocboi1.template.name + "\n";
                        for (Item.ItemOption io : ngocboi1.itemOptions) {
                            npcSay += "|2|" + io.getOptionString() + "\n";
                        }
                        npcSay += "Ngươi có muốn Tiến Hóa Ngọc Bội không?\n";
                        if (player.inventory.ruby >= 1000 ) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Tiến Hóa\n Ngọc Bội");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Chuẩn bị đủ tiền rồi hãy gặp ta!!!");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta cần Ngọc Bội + 1K Hồng Ngọc + 50 Tr Vàng và 99 Thỏi Vàng Và 10 Ngọc Rồng 3 Sao", "Đóng");
                    }
                }
                break;
                
                case NANG_CAP_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Hãy đưa ta chân mệnh và 5000 viên Xu ta sẽ phù phép cho !",
                            "Đóng");
                    return;
                }

                if (player.combineNew.itemsCombine.size() == 2) {
                    if (player.combineNew.itemsCombine.stream().filter(
                            item -> item.isNotNullItem() && (item.template.id >= 1239 && item.template.id <= 1247))
                            .count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu chân mệnh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream()
                            .filter(item -> item.isNotNullItem() && item.template.id == 2025)
                            .count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Xu", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "|7|Chân mệnh "
                            + "10 viên ngọc rồng 1 sao + 50k HN";

                    if (player.inventory.ruby < 50000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con",
                                "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + "50k" + " hồng ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp",
                                "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break; 
                
                case PHAN_RA_TRANG_BI1:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiGod(item)) {                       
                        String npcSay ="Con có muốn luyện hóa trang bị :\n"+ item.template.name + " không";
                            phithang.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Luyện hóa");
                    } else {
                        this.phithang.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ có thể luyện hóa trang bị thần linh", "Đóng");
                    }
                } else {
                    this.phithang.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho vật phẩm vào..", "Đóng");
                }
                break;
            case PHAN_RA_TRANG_BI2:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiHakai(item)) {                       
                        String npcSay ="Con có muốn luyện hóa trang bị :\n"+ item.template.name + " không";
                            phithang.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Luyện hóa");
                    } else {
                        this.phithang.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ có thể luyện hóa trang bị hủy diệt", "Đóng");
                    }
                } else {
                    this.phithang.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho vật phẩm vào..", "Đóng");
                }
                break;
            case PHAN_RA_TRANG_BI3:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiAngel(item)) {                       
                        String npcSay ="Con có muốn luyện hóa trang bị :\n"+ item.template.name + " không";
                            phithang.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                            "Luyện hóa");
                    } else {
                        this.phithang.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chỉ có thể luyện hóa trang bị thiên sứ", "Đóng");
                    }
                } else {
                    this.phithang.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho vật phẩm vào..", "Đóng");
                }
                break; 
                
                case THANG_HOA_NGOC_BOI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isNgocBoi()).count() < 1) {
                        Service.getInstance().sendThongBaoOK(player, "Yêu cầu trang bị là\" Ngọc Bội\"");
                        return;
                    }
                    Item NgocBoi = player.combineNew.itemsCombine.stream().filter(Item::isNgocBoi).findFirst().get();
                    if (NgocBoi != null) {
                        for (ItemOption itopt : NgocBoi.itemOptions) {
                            if (itopt.optionTemplate.id == 225 && itopt.optionTemplate.id != 226) {
                                if (itopt.param >= 1) {
                                    Service.getInstance().sendThongBao(player, "Trang bị đã thăng hoa cho bản thân");
                                    return;
                                }
                            }
                            if (itopt.optionTemplate.id != 225 && itopt.optionTemplate.id == 226) {
                                if (itopt.param >= 1) {
                                    Service.getInstance().sendThongBao(player, "Trang bị đã thăng hoa cho đệ tử");
                                    return;
                                }
                            }
                        }
                    }
                    Item item = player.combineNew.itemsCombine.get(0);
//                    if(item.template.type == 0){
                    String npcSay = "Trang bị được nâng cấp \"" + item.Name() + "\"";

                    npcSay += "\n|0|Thăng hoa giúp sử dụng ngọc bội\n"
                            + "Tỷ lệ thành công: 100%\n"
                            + "|2|Cần 10.000 ngọc xanh";

                    int SoVangThieu = (int) (10000 - player.inventory.gem);
                    if (player.inventory.gem < 10000) {
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu + " vàng");
                        return;
                    }
                    this.thoNgoc.createOtherMenu(player, ConstNpc.MENU_THANG_HOA_NGOC_BOI,
                            npcSay, "Thăng hoa\nBản thân", "Thăng hoa\nĐệ tử");
                } else {
                    if (player.combineNew.itemsCombine.size() > 1) {
                        Service.getInstance().sendThongBaoOK(player, "Yêu cầu trang bị là\" Ngọc Bội\"");
                        return;
                    }
                    this.thoNgoc.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu trang bị là\" Ngọc Bội\"", "Đóng");
                }
                break;
                
                case GIA_HAN_VAT_PHAM:

                if (player.combineNew.itemsCombine.size() == 2) {
                    Item daHacHoa = null;
                    Item itemHacHoa = null;
                    for (Item item_ : player.combineNew.itemsCombine) {
                        if (item_.template.id == 1191) {
                            daHacHoa = item_;
                        } else if (item_.isTrangBiHSD()) {
                            itemHacHoa = item_;
                        }
                    }
                    if (daHacHoa == null) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 Sách Gia hạn");
                        return;
                    }
                    if (itemHacHoa == null) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 Sách Gia hạn");
                        return;
                    }
                    for (ItemOption itopt : itemHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == 93) {
                            if (itopt.param < 0 && itopt == null) {
                                Service.getInstance().sendThongBaoOK(player, "Trang bị này không phải trang bị có Hạn Sử Dụng");
                                return;
                            }
                        }
                    }

                    String npcSay = "Trang bị được gia hạn \"" + itemHacHoa.template.name + "\"";

                    npcSay += "\n|0|Sau khi gia hạn +1 ngày\n";

                    npcSay += "|0|Tỉ lệ thành công: 100%" + "\n";
                    if (player.inventory.gold > 200000000) {
                        npcSay += "|2|Cần 200Tr vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp", "Từ chối");

                    } else if (player.inventory.gold < 200000000) {
                        int SoVangThieu2 = (int) (200000000 - player.inventory.gold);
                        Service.getInstance().sendThongBaoOK(player, "Bạn còn thiếu " + SoVangThieu2 + " vàng");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 Sách Gia hạn");
                    }
                } else {

                    Service.getInstance().sendThongBaoOK(player, "Hành trang cần ít nhất 1 chỗ trống");
                }
                break;
                case NANG_CAP_BONG_TAI_CAP4:  //c4
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1386) {
                            bongTai = item;
                        } else if (item.template.id == 1392) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 3000) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 3 và X3000 Mảnh vỡ bông tai cấp 3", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 3 và X3000 Mảnh vỡ bông tai cấp 3", "Đóng");
                }
                break;
                case NANG_CAP_BONG_TAI_CAP5: //C5
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1387) {
                            bongTai = item;
                        } else if (item.template.id == 1393) {
                            manhVo = item;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_BONG_TAI;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 4 và X9999 Mảnh vỡ bông tai cấp 4", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 4 và X9999 Mảnh vỡ bông tai cấp 4", "Đóng");
                }
                break;
                
                case TAY_PS_HOA_TRANG_BI:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daHacHoa = null;
                        Item itemHacHoa = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 1383) {
                                daHacHoa = item_;
                            } else if (item_.isTrangBiHacHoa()) {
                                itemHacHoa = item_;
                            }
                        }
                        if (daHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá ánh sáng", "Đóng");
                            return;
                        }
                        if (itemHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu trang bị bóng tối", "Đóng");
                            return;
                        }

                        String npcSay = "|2|Hiện tại " + itemHacHoa.template.name + "\n|0|";
                        for (Item.ItemOption io : itemHacHoa.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi nâng cấp sẽ xoá hết các chỉ số bóng tối ngẫu nhiên"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                + "Cần " + Util.numberToMoney(COST) + " vàng";

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị có thể bóng tối và đá ánh sáng", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;
            case PS_HOA_TRANG_BI:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item daHacHoa = null;
                        Item itemHacHoa = null;
                        for (Item item_ : player.combineNew.itemsCombine) {
                            if (item_.template.id == 1382) {
                                daHacHoa = item_;
                            } else if (item_.isTrangBiHacHoa()) {
                                itemHacHoa = item_;
                            }
                        }
                        if (daHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu đá bóng tối", "Đóng");
                            return;
                        }
                        if (itemHacHoa == null) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu trang bị", "Đóng");
                            return;
                        }
                        if (itemHacHoa != null) {
                            for (ItemOption itopt : itemHacHoa.itemOptions) {
                                if (itopt.optionTemplate.id == 223) {
                                    if (itopt.param >= 8) {
                                        Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn bóng tối");
                                        return;
                                    }
                                }
                            }
                        }
                        
                        player.combineNew.goldCombine = COST;
                        player.combineNew.gemCombine = RUBY;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;
                        
                        
                        String npcSay = "|2|Hiện tại " + itemHacHoa.template.name + "\n|0|";
                        for (Item.ItemOption io : itemHacHoa.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng"
                                     + "Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " vàng";
                                      
                        }

                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                npcSay, "Nâng cấp", "Từ chối");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần có trang bị có thể bóng tối và đá bóng tối", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }

                break;  
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1388) {
                            bongTai = item;
                        } else if (item.template.id == 934) {
                            manhHon = item;
                        } else if (item.template.id == 935) {
                            daXanhLam = item;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = GOLD_BONG_TAI;
                        player.combineNew.gemCombine = GEM_BONG_TAI;
                        player.combineNew.ratioCombine = RATIO_NANG_CAP;

                        String npcSay = "Bông tai Porata cấp 5" + "\n|2|";
                        for (Item.ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 5, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 5, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
                
                case CHE_TAO_TRANG_BI_TL:
                 if (player.combineNew.itemsCombine.size() == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "", "Yes");
                    return;
                }
                  if (player.combineNew.itemsCombine.size() >= 2 &&  player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() &&  item.isCongThucVip()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTL() && item.quantity >= 99).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thần linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
                        return;
                    }
                    Item mTL = null, daNC = null, daMM = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (item.isManhTL()) {
                                mTL = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;    
                    int tilenew = tilemacdinh;
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;                     
                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTL).findFirst().get().typeNameManh() + " Hủy Diệt " 
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|1|Mảnh ghép " +  mTL.quantity + "/99\n"
                            + "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                            + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n"
                            + "|3|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                            + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n"
                            + "|5|Tỉ lệ thành công: " + tilenew + "%\n"
                            + "|4|Phí nâng cấp: 500 triệu vàng";
                    
                    if (daNC != null) {
                        
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap() 
                                  + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                  + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;  
                
                case CHE_TAO_TRANG_BI_HD:
                 if (player.combineNew.itemsCombine.size() == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ciin", "Yes");
                    return;
                }
                  if (player.combineNew.itemsCombine.size() >= 2 &&  player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() &&  item.isCongThucVip()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhHD() && item.quantity >= 99).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ huy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
                        return;
                    }
                    Item mTS = null, daNC = null, daMM = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (item.isManhHD()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;    
                    int tilenew = tilemacdinh;
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;                     
                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhHD).findFirst().get().typeNameManh() + " Hủy Diệt " 
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|1|Mảnh ghép " +  mTS.quantity + "/99\n"
                            + "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                            + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n"
                            + "|3|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                            + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n"
                            + "|5|Tỉ lệ thành công: " + tilenew + "%\n"
                            + "|4|Phí nâng cấp: 500 triệu vàng";
                    
                    if (daNC != null) {
                        
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap() 
                                  + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                  + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;  
                
                 case NANG_CAP_DO_KICH_HOAT:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 Món đồ Thần Linh bất kì", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item dtl = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 555 && item.template.id <= 567) {
                                dtl = item;
                                break;
                            }
                        }
                    }
                    if (dtl == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi Món đồ Thần Linh này để nhận được một món đồ kích hoạt bất kì?\n|7|"
                            + "Và nhận được 1 món đồ kích hoạt bất kì\n"
                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";

                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_DAP_DO_KICH_HOAT,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món Thần Linh và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần Linh", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
                case NANG_CAP_SKH_VIPhd:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món hủy diệt và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 1) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
                case NANG_CAP_SKH_VIPts:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món thiên sứ và 2 món SKH ngẫu nhiên", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() < 1) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() < 2) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ kích hoạt", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.thiensuwhis2.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NHAP_NGOC_RONG:
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                            String npcSay = "|2|Con có muốn biến 7 " + item.template.name + " thành\n"
                                    + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                    + "|7|Cần 7 " + item.template.name;
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng 2 sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (Item.ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (Item.ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;
                
//            case kh_T:
//                if (player.combineNew.itemsCombine.size() == 0) {
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hiến tế cho ta 1 món đồ hủy diệt bất kì", "Đóng");
//                    return;
//                }
//                if (player.combineNew.itemsCombine.size() == 1) {
//                    Item dtl = null;
//                    for (Item item : player.combineNew.itemsCombine) {
//                        if (item.isNotNullItem()) {
//                            if (item.template.id >= 650 && item.template.id <= 662) {
//                                dtl = item;
//                                break;
//                            }
//                        }
//                    }
//                    if (dtl == null) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Hủy diệt", "Đóng");
//                        return;
//                    }
//
//                    String npcSay = "|2|Con có muốn hiến tế Món đồ HỦY DIỆT này\n"
//                            + "|2|để nhận được một món đồ\n"
//                            + "|7|KÍCH HOẠT?\n"
//                            + "|2|---------------------\n"
//                            + "|2|Chỉ cần chọn Hiến tế\n"
//                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
//
//                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                        return;
//                    }
//                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_DAP_DO_KICH_HOAT,
//                            npcSay, "Hiến tế\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng", "Từ chối");
//                } else {
//                    if (player.combineNew.itemsCombine.size() > 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
//                        return;
//                    }
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
//                }
//                break;
                case kh_Tl:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 Món đồ Thần Linh bất kì", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item dtl = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 555 && item.template.id <= 567) {
                                dtl = item;
                                break;
                            }
                        }
                    }
                    if (dtl == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi Món đồ Thần Linh này để nhận được một món đồ kích hoạt bất kì?\n|7|"
                            + "Và nhận được 1 món đồ kích hoạt bất kì\n"
                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";

                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_DAP_DO_KICH_HOAT,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
//                case kh_Hd:
//                if (player.combineNew.itemsCombine.size() == 0) {
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hiến tế cho ta 1 món đồ thần linh bất kì", "Đóng");
//                    return;
//                }
//                if (player.combineNew.itemsCombine.size() == 1) {
//                    Item dtl = null;
//                    for (Item item : player.combineNew.itemsCombine) {
//                        if (item.isNotNullItem()) {
//                            if (item.template.id >= 555 && item.template.id <= 567) {
//                                dtl = item;
//                                break;
//                            }
//                        }
//                    }
//                    if (dtl == null) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ thần linh", "Đóng");
//                        return;
//                    }
//
//                    String npcSay = "|2|Con có muốn hiến tế Món đồ THẦN LINH này\n để nhận được một món đồ\n"
//                            + "|7|HỦY DIỆT?\n"
//                            + "|2|-----------\n"
//                            + "|2|Chỉ cần chọn Hiến tế\n"
//                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";
//
//                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                        return;
//                    }
//                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_DAP_DO_KICH_HOAT,
//                            npcSay, "Hiến tế\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng", "Từ chối");
//                } else {
//                    if (player.combineNew.itemsCombine.size() > 1) {
//                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
//                        return;
//                    }
//                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
//                }
//                break;
                case kh_Hd:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    
                    //if (isTrangBiGod1(item) && isTrangBiThuong(item1)) {
                    if (isTrangBiGod1(item)) {
                        player.combineNew.goldCombine = 10000;
                        String npcSay ="Con có muốn nâng cấp trang bị :\n"+ item.template.name + " thành trang bị Hủy Diệt\n";
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                            if (thoivang != null && thoivang.quantity >= 10 )  {
                                npcSay += "|1|Cần 5 Thỏi vàng \n"
                                        + "Tỉ lệ thành công: 50%\n" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Không đủ 5 thỏi vàng ";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho 1 trang bị Thần linh ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;    
            case kh_T:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    
                    //if (isTrangBiGod1(item) && isTrangBiThuong(item1)) {
                    if (isTrangBiHakai(item)) {
                        player.combineNew.goldCombine = 10000;
                        String npcSay ="Con có muốn nâng cấp trang bị :\n"+ item.template.name + " thành trang bị Thiên Sứ\n";
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                        Item thoivang2 = InventoryServiceNew.gI().findItemBag(player, 987);
                            if (thoivang != null && thoivang.quantity >= 20 && thoivang2 != null && thoivang2.quantity >= 10 )  {
                                npcSay += "|1|Cần 5 Thỏi vàng và 5 Bảo thạch tím \nTỉ lệ thành công: 10%\n" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Không đủ 20 thỏi vàng hoặc 10 Đá bảo vệ ";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy cho 1 trang bị Hủy diệt ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có vật phẩm để nâng cấp", "Đóng");
                }
                break;
                case kh_Ts:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 Món đồ Thiên sứ bất kì", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item dtl = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.isNotNullItem()) {
                            if (item.template.id >= 1048 && item.template.id <= 1062) {
                                dtl = item;
                                break;
                            }
                        }
                    }
                    if (dtl == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi Món đồ Thiên sứ này để nhận được một món đồ kích hoạt bất kì?\n|7|"
                            + "Và nhận được 1 món đồ kích hoạt bất kì\n"
                            + "|1|Cần " + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng";

                    if (player.inventory.gold < COST_DAP_DO_KICH_HOAT) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_DAP_DO_KICH_HOAT,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST_DAP_DO_KICH_HOAT) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (Item.ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (Item.ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isTrangBiPhaLeHoa(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 107) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_ITEM) {
                            player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                            player.combineNew.gemCombine = getGemPhaLeHoa(star);
                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            if (player.combineNew.goldCombine <= player.inventory.gold) {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp\n" + player.combineNew.gemCombine + " ngọc\n" + "1 lần", "Nâng cấp\n" + (player.combineNew.gemCombine * 10) + " ngọc\n" + "10 Lần", "Nâng cấp\n" + (player.combineNew.gemCombine * 100) + " ngọc\n" + "100 lần");
                            } else {
                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
                 case REN_KIEM_Z:
    if (player.combineNew.itemsCombine.size() == 2) {
        Item manhKiemZ = null;
        Item quangKiemZ = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id == 865 || item.template.id == 1200) {
                manhKiemZ = item;
            } else if (item.template.id == 1201) {
                quangKiemZ = item;
            }
        }
        if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 99) {
            player.combineNew.goldCombine = GOLD_KIEM_Z;
            player.combineNew.gemCombine = GEM_KIEM_Z;
            player.combineNew.ratioCombine = RATIO_KIEM_Z2;
            String npcSay = "Kiếm Z cấp 1" + "\n|2|";
            for (Item.ItemOption io : manhKiemZ.itemOptions) {
                npcSay += io.getOptionString() + "\n";
            }
            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
            if (player.combineNew.goldCombine <= player.inventory.gold) {
                npcSay += "|1|Rèn Kiếm Z " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                tosukaio.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                        "Rèn Kiếm Z\ncần " + player.combineNew.gemCombine + " Ngọc xanh");
            } else {
                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
            }
        } else if (manhKiemZ == null || quangKiemZ == null) {
            this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
        } else {
            this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                    "Số lượng quặng Kiếm Z không đủ", "Đóng");
        }
    } else {
        this.tosukaio.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                "Cần 1 Kiếm Z và X99 Quặng Kiếm Z", "Đóng");
    }
    break;
 
case CHE_TAO_TRANG_BI_TS:
                 if (player.combineNew.itemsCombine.size() == 0) {
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ciin", "Yes");
                    return;
                }
                  if (player.combineNew.itemsCombine.size() >= 2 &&  player.combineNew.itemsCombine.size() < 5) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() &&  item.isCongThucVip()).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Công thức Vip", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 99).count() < 1) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá nâng cấp", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đá may mắn", "Đóng");
                        return;
                    }
                    Item mTS = null, daNC = null, daMM = null;
                        for (Item item : player.combineNew.itemsCombine) {
                            if (item.isNotNullItem()) {
                                if (item.isManhTS()) {
                                mTS = item;
                            } else if (item.isDaNangCap()) {
                                daNC = item;
                            } else if (item.isDaMayMan()) {
                                daMM = item;
                            }
                        }
                    }
                    int tilemacdinh = 35;    
                    int tilenew = tilemacdinh;
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;                     
                    }

                    String npcSay = "|2|Chế tạo " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " Thiên sứ " 
                            + player.combineNew.itemsCombine.stream().filter(Item::isCongThucVip).findFirst().get().typeHanhTinh() + "\n"
                            + "|7|Mảnh ghép " +  mTS.quantity + "/99\n"
                            + "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap()
                            + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n"
                            + "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                            + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n"
                            + "|2|Tỉ lệ thành công: " + tilenew + "%\n"
                            + "|7|Phí nâng cấp: 500 triệu vàng";
                    
                    if (daNC != null) {
                        
                        npcSay += "|2|Đá nâng cấp " + player.combineNew.itemsCombine.stream().filter(Item::isDaNangCap).findFirst().get().typeDanangcap() 
                                  + " (+" + (daNC.template.id - 1073) + "0% tỉ lệ thành công)\n";
                    }
                    if (daMM != null) {
                        npcSay += "|2|Đá may mắn " + player.combineNew.itemsCombine.stream().filter(Item::isDaMayMan).findFirst().get().typeDaMayman()
                                  + " (+" + (daMM.template.id - 1078) + "0% tỉ lệ tối đa các chỉ số)\n";
                    }
                    if (daNC != null) {
                        tilenew += (daNC.template.id - 1073) * 10;
                        npcSay += "|2|Tỉ lệ thành công: " + tilenew + "%\n";
                    } else {
                        npcSay += "|2|Tỉ lệ thành công: " + tilemacdinh + "%\n";
                    }
                    npcSay += "|7|Phí nâng cấp: 500 triệu vàng";
                    if (player.inventory.gold < 500000000) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn không đủ vàng", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.MENU_DAP_DO,
                            npcSay, "Nâng cấp\n500 Tr vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npsthiensu64.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ nguyên liệu, mời quay lại sau", "Đóng");
                }
                break;
        case KHAM_DA_HP:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isradavip(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 195) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_KHAM_DA) {
                            player.combineNew.ngusacCombine = getngusacKhamDa(star);
                            player.combineNew.dakham = getDaKham(star);
//                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 195) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + 100 + "%" + "\n";
                            Item dakham = InventoryServiceNew.gI().findItemBag(player, 1613);//Util.nextInt(1613, 1615));
                            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 674);
                            if (dakham != null && dakham.quantity >=  player.combineNew.dakham && dangusac != null && dangusac.quantity >=  player.combineNew.ngusacCombine)  {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.dakham) + " đá sinh lực \n và Cần " +  Util.numberToMoney(player.combineNew.ngusacCombine) + " đá ngũ sắc" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Cần đá sinh lực và đá ngũ sắc";
                               baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa số lượng đá khảm", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể khảm đá", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 Huy hiệu để khảm đá", "Đóng");
                }
                break;
            case KHAM_DA_MP:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isradavip(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 195) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_KHAM_DA) {
                            player.combineNew.ngusacCombine = getngusacKhamDa(star);
                            player.combineNew.dakham = getDaKham(star);
//                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 195) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + 100 + "%" + "\n";
                            Item dakham = InventoryServiceNew.gI().findItemBag(player, 1614); //Util.nextInt(1613, 1615));
                            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 674);
                            if (dakham != null && dakham.quantity >=  player.combineNew.dakham && dangusac != null && dangusac.quantity >=  player.combineNew.ngusacCombine)  {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.dakham) + " đá năng lượng \n và Cần " +  Util.numberToMoney(player.combineNew.ngusacCombine) + " đá ngũ sắc" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Cần đá năng lượng và đá ngũ sắc";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa số lượng đá khảm", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể khảm đá", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 huy hiệu để khảm đá", "Đóng");
                }
                break;
            case KHAM_DA_DAME:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (isradavip(item)) {
                        int star = 0;
                        for (Item.ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 195) {
                                star = io.param;
                                break;
                            }
                        }
                        if (star < MAX_STAR_KHAM_DA) {
                            player.combineNew.ngusacCombine = getngusacKhamDa(star);
                            player.combineNew.dakham = getDaKham(star);
//                            player.combineNew.ratioCombine = getRatioPhaLeHoa(star);

                            String npcSay = item.template.name + "\n|2|";
                            for (Item.ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id != 195) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + 100 + "%" + "\n";
                            Item dakham = InventoryServiceNew.gI().findItemBag(player, 1615); //Util.nextInt(1613, 1615));
                            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 674);
                            if (dakham != null && dakham.quantity >=  player.combineNew.dakham && dangusac != null && dangusac.quantity >=  player.combineNew.ngusacCombine)  {
                                npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.dakham) + " đá sức mạnh \n và Cần " +  Util.numberToMoney(player.combineNew.ngusacCombine) + " đá ngũ sắc" ;
                                baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                        "Nâng cấp");
                            } else {
                                npcSay += "Cần đá sức mạnh và đá ngũ sắc";
//                                npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.dakham - dakham.quantity) + " đá khảm \n và " + Util.numberToMoney(player.combineNew.ngusacCombine - dangusac.quantity) +" đá ngũ sắc";
                                baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm đã đạt tối đa số lượng đá khảm", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể khảm đá", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 huy hiệu để khảm đá", "Đóng");
                }
                break;        
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case NANG_NGOC_BOI:
                NgocBoi(player);    
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case DOI_HOP_QUA:
                setDoiHopQua(player);
                break;    
            case CHUYEN_HOA_TRANG_BI:
                break;
                case PS_HOA_TRANG_BI:
                psHoaTrangBi(player);
                break;
             case AN_TRANG_BI:
                antrangbi(player);
                break;   
            case GIA_HAN_VAT_PHAM:
                GiaHanTrangBi(player);
                break;  
                case THANG_HOA_NGOC_BOI:
                ThangHoaBanThan(player);
                break;
            case TAY_PS_HOA_TRANG_BI:
                tayHacHoaTrangBi(player);
                break;
            case NANG_CAP_CHAN_MENH:
                nangCapChanMenh(player);
                break;    
                
            case PHAN_RA_TRANG_BI1:
                prtl(player);
                break;
            case PHAN_RA_TRANG_BI2:
                prhd(player);
                break;
            case PHAN_RA_TRANG_BI3:
                prts(player);
                break;       
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
                case NANG_CAP_BONG_TAI_CAP3:
                nangCapBongTaicap3(player);
                break;
                case NANG_CAP_BONG_TAI_CAP4:
                nangCapBongTaicap4(player);
                break;
                case NANG_CAP_BONG_TAI_CAP5:
                nangCapBongTaicap5(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai(player);
                break; 
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case REN_KIEM_Z:
                renKiemZ(player);
                break;
            case NANG_CAP_DO_KICH_HOAT:
                dapDoKichHoat(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
                case NANG_CAP_SKH_VIPhd:
                openSKHVIPhd(player);
                break;
                case NANG_CAP_SKH_VIPts:
                openSKHVIPts(player);
                break;
            case kh_T:
                khT(player);    
                break;
            case kh_Tl:
                khTl(player);
            case kh_Hd:
                khHd(player);
            case kh_Ts:
                khTs(player);   
            case CHE_TAO_TRANG_BI_TS:
                openCreateItemAngel(player);
                break; 
            case KHAM_DA_HP:
                khamdaHP(player);
                break;
            case KHAM_DA_MP:
                khamdaMP(player);
                break;
            case KHAM_DA_DAME:
                khamdaDAME(player);
                break;
            
            case NANG_CAP_PET2:
                setNangCapPet2(player);
                break;
            case NANG_CAP_PK:
                setNangCapPK(player);
                break;    
            case CUONG_HOA:
                setCuongHoa(player);
                break;    
                case CHE_TAO_TRANG_BI_TL:
                openCreateItemAngel1(player);
                break;
                case NANG_KICH_HOAT_VIP:
                nangKichHoatVip(player);
                break;
            case NANG_KICH_HOAT_VIP2:
                nangKichHoatVip2(player);
                break; 
            case CHE_TAO_TRANG_BI_HD:
                openCreateItemAngel2(player);
                break;   
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }
    
    public void startCombine1(Player player, int select) {
        switch (player.combineNew.typeCombine) {
            case THANG_HOA_NGOC_BOI:
                ThangHoaBanThan(player);
                break;   
        }
        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }
        public void GetTrangBiKichHoathuydiet(Player player, int id){
        Item item = ItemService.gI().createNewItem((short)id);
        int[][] optionNormal = {{127,128},{130,132},{133,135}};
        int[][] paramNormal = {{139,140},{142,144},{136,138}};
        int[][] optionVIP = {{129},{131},{134}};
        int[][] paramVIP = {{141},{143},{137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type== 0){
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500,2000)));
        }
        if (item.template.type== 1){
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100,150)));
        }
        if (item.template.type== 2){
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000,11000)));
        }
        if (item.template.type== 3){
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90,150)));
        }
        if (item.template.type== 4){
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15,20)));
        }
        if (randomSkh <= 20){//tile ra do kich hoat
            if (randomSkh <= 5){ // tile ra option vip
        item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
        item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
        item.itemOptions.add(new ItemOption(30, 0));
            }else{// 
        item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
        item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
        item.itemOptions.add(new ItemOption(30, 0));
            }
        }
        
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }
    public void GetTrangBiKichHoatthiensu(Player player, int id){
        Item item = ItemService.gI().createNewItem((short)id);
        int[][] optionNormal = {{127,128},{130,132},{133,135}};
        int[][] paramNormal = {{139,140},{142,144},{136,138}};
        int[][] optionVIP = {{129},{131},{134}};
        int[][] paramVIP = {{141},{143},{137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type== 0){
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000,2500)));
        }
        if (item.template.type== 1){
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150,200)));
        }
        if (item.template.type== 2){
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000,20000)));
        }
        if (item.template.type== 3){
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150,200)));
        }
        if (item.template.type== 4){
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20,25)));
        }
        if (randomSkh <= 10){//tile ra do kich hoat
            if (randomSkh <= 2){ // tile ra option vip
        item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
        item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
        item.itemOptions.add(new ItemOption(30, 0));
            }else{// 
        item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
        item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
        item.itemOptions.add(new ItemOption(30, 0));
            }
        }
        
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
    }
        public void openSKHVIP(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thần linh");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVip[6];
                }
            } else {
                itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVip[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 15));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
        public void openSKHVIPhd(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ Hủy diệt");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHViphd[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHViphd[6];
                }
            } else {
                itemId = Manager.doSKHViphd[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHViphd[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDHD()) {
                item = Util.ratiItemHD(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 50));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
        public void openSKHVIPts(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu đồ thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).count() != 2) {
            Service.gI().sendThongBao(player, "Thiếu đồ kích hoạt");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.gI().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemSKH = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isSKH()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (itemTS.template.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVipts[Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.radaSKHVipts[6];
                }
            } else {
                itemId = Manager.doSKHVipts[itemTS.template.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (player.getSession().bdPlayer > 0 && Util.isTrue(1, (int) (100 / player.getSession().bdPlayer))) {
                    itemId = Manager.doSKHVipts[itemTS.template.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(itemTS.template.gender);
            Item item;
            if (new Item(itemId).isDTS()) {
                item = Util.ratiItemTS(itemId);
                item.itemOptions.add(new Item.ItemOption(skhId, 1));
                item.itemOptions.add(new Item.ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new Item.ItemOption(21, 80));
                item.itemOptions.add(new Item.ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().subQuantityItemsBag(player, itemTS, 1);
            itemSKH.forEach(i -> InventoryServiceNew.gI().subQuantityItemsBag(player, i, 1));
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
        
        private void antrangbi(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                Item dangusac = player.combineNew.itemsCombine.get(1);
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 35) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1401 || dangusac.template.id == 1403 || dangusac.template.id == 1402) && dangusac.quantity >= 99) {
                    if (optionStar == null) {
                        if (dangusac.template.id == 1401) {
                            item.itemOptions.add(new Item.ItemOption(34, 1));
                            sendEffectSuccessCombine(player);
                        } else if (dangusac.template.id == 1403) {
                            item.itemOptions.add(new Item.ItemOption(35, 1));
                            sendEffectSuccessCombine(player);
                        } else if (dangusac.template.id == 1402) {
                            item.itemOptions.add(new Item.ItemOption(36, 1));
                            sendEffectSuccessCombine(player);
                        }
//                    InventoryServiceNew.gI().addItemBag(player, item);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, dangusac, 99);
                        InventoryServiceNew.gI().sendItemBags(player);
                        reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị của bạn có ấn rồi mà !!!");
                    }
                }
            }
        }
    }
        
        private void dapDoKichHoat(Player player) {

        //barcoll Cot ddow
        if (player.combineNew.itemsCombine.size() == 2) {
            Item dtl1 = null;
            Item dtl2 = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 555 && item.template.id <= 567) {
                        if (dtl1 == null) {
                            dtl1 = item;
                        } else {
                            dtl2 = item;
                        }
                    }
                }
            }
            if (dtl1 != null && dtl2 != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= 500000000) {
                    player.inventory.gold -= 500000000;
                    int tiLe = 100;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dtl1.template.gender, dtl1.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl1, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl2, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void doiKiemThan(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item keo = null, luoiKiem = null, chuoiKiem = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 2015) {
                    keo = it;
                } else if (it.template.id == 2016) {
                    chuoiKiem = it;
                } else if (it.template.id == 2017) {
                    luoiKiem = it;
                }
            }
            if (keo != null && keo.quantity >= 99 && luoiKiem != null && chuoiKiem != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2018);
                    item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(9, 15)));
                    item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(8, 15)));
                    item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(8, 15)));
                    if (Util.isTrue(80, 100)) {
                        item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
                    }
                    InventoryServiceNew.gI().addItemBag(player, item);

                    InventoryServiceNew.gI().subQuantityItemsBag(player, keo, 99);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, luoiKiem, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, chuoiKiem, 1);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void renKiemZ(Player player) {
    if (player.combineNew.itemsCombine.size() == 2) {
        int gold = player.combineNew.goldCombine;
        if (player.inventory.gold < gold) {
            Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
            return;
        }
        
        int gem = player.combineNew.gemCombine;
        if (player.inventory.gem < gem) {
            Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
            return;
        }
        
        Item manhKiemZ = null;
        Item quangKiemZ = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id == 865 || item.template.id == 1200) {
                manhKiemZ = item;
            } else if (item.template.id == 1201) {
                quangKiemZ = item;
            }
        }
        
        if (manhKiemZ != null && quangKiemZ != null && quangKiemZ.quantity >= 99) {
             //Item findItemBag = InventoryServiceNew.gI().findItemBag(player, 1200); //Nguyên liệu
            //if (findItemBag != null) {
                //Service.gI().sendThongBao(player, "Con đã có Kiếm Z trong hành trang rồi, không thể rèn nữa.");
                //return;
            //}
            player.inventory.gold -= gold;
            player.inventory.gem -= gem;
            InventoryServiceNew.gI().subQuantityItemsBag(player, quangKiemZ, 99);
            if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
    manhKiemZ.template = ItemService.gI().getTemplate(1200);
    manhKiemZ.itemOptions.clear();
    Random rand = new Random();
int ratioCombine = rand.nextInt(60) + 1;
int level = 0;
if (ratioCombine <= 40) {
    level = 1 + rand.nextInt(4);
} else if (ratioCombine <= 70) {
    level = 5 + rand.nextInt(4);
} else if (ratioCombine <= 90) {
    level = 9 + rand.nextInt(4);
} else if (ratioCombine <= 95) {
    level = 13 + rand.nextInt(3);
} else {
    level = 16;
}
manhKiemZ.itemOptions.add(new Item.ItemOption(0, level * 200 + 10000));
manhKiemZ.itemOptions.add(new Item.ItemOption(49, level * 1 + 20));
manhKiemZ.itemOptions.add(new Item.ItemOption(14, level));
manhKiemZ.itemOptions.add(new Item.ItemOption(97, level));
manhKiemZ.itemOptions.add(new Item.ItemOption(30, 0));
manhKiemZ.itemOptions.add(new Item.ItemOption(72, level));
sendEffectSuccessCombine(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        }
    }
}

    private void doiLuoiKiem(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item manhSat = player.combineNew.itemsCombine.get(0);
            if (manhSat.template.id == 2013 && manhSat.quantity >= 99) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                    sendEffectSuccessCombine(player);
                    Item item = ItemService.gI().createNewItem((short) 2017);
                    InventoryServiceNew.gI().addItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhSat, 99);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
    
    private void ThangHoaBanThan(Player player) {
        if (player.combineNew.itemsCombine == null || player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBaoOK(player, "Yêu cầu là trang bị ngọc bội");
            return;
        }

        Item NgocBoi = player.combineNew.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && item.isNgocBoi())
                .findFirst()
                .orElse(null);

        if (NgocBoi == null) {
            Service.getInstance().sendThongBaoOK(player, "Yêu cầu là trang bị ngọc bội");
            return;
        }

        for (ItemOption itopt : NgocBoi.itemOptions) {
            if (itopt.optionTemplate.id == 225 && itopt.optionTemplate.id != 226) {
                if (itopt.param > 0) {
                    Service.getInstance().sendThongBao(player, "Ngọc Bội Đã Thăng Hoa");
                    return;
                }
            }
        }

        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }

        sendEffectSuccessCombine(player);

        for (ItemOption itopt : NgocBoi.itemOptions) {
            if (itopt.optionTemplate.id != 225) {
                NgocBoi.itemOptions.add(new ItemOption(225, 1));
                break;
            }
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
    }


    public void openCreateItemAngel(Player player) {
        // Công thức vip + x99 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
        //    Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 99).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thiên sứ");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
            return;
        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isManhTS()) {
                        mTS = item;
                    } else if (item.isDaNangCap()) {
                        daNC = item;
                    } else if (item.isDaMayMan()) {
                        daMM = item;
                    } else if (item.isCongThucVip()) {
                        CtVip = item;
                    }
                }
            }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 ) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
                    player.inventory.gold -= 500000000;
                    
                    int tilemacdinh = 35;
                    int tileLucky = 20;
                    if (daNC != null) {
                        tilemacdinh += (daNC.template.id - 1073)*10;
                    } else {
                        tilemacdinh = tilemacdinh;
                    }
                    if (daMM != null) {
                        tileLucky += tileLucky*(daMM.template.id - 1078)*10/100;
                    } else {
                        tileLucky = tileLucky;
                    }
                    
                    if (Util.nextInt(0, 100) < tilemacdinh) {
                        Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                        if (daNC != null) {
                        Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                        }
                        if (daMM != null) {
                        Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                        }
                        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 99).findFirst().get();
                        
                        tilemacdinh = Util.nextInt(0, 50);
                        if (tilemacdinh == 49) { tilemacdinh = 20;}
                        else if (tilemacdinh == 48 || tilemacdinh == 47) { tilemacdinh = 19;}
                        else if (tilemacdinh == 46 || tilemacdinh == 45) { tilemacdinh = 18;}
                        else if (tilemacdinh == 44 || tilemacdinh == 43) { tilemacdinh = 17;}
                        else if (tilemacdinh == 42 || tilemacdinh == 41) { tilemacdinh = 16;}
                        else if (tilemacdinh == 40 || tilemacdinh == 39) { tilemacdinh = 15;}
                        else if (tilemacdinh == 38 || tilemacdinh == 37) { tilemacdinh = 14;}
                        else if (tilemacdinh == 36 || tilemacdinh == 35) { tilemacdinh = 13;}
                        else if (tilemacdinh == 34 || tilemacdinh == 33) { tilemacdinh = 12;}
                        else if (tilemacdinh == 32 || tilemacdinh == 31) { tilemacdinh = 11;}
                        else if (tilemacdinh == 30 || tilemacdinh == 29) { tilemacdinh = 10;}
                        else if (tilemacdinh <= 28 || tilemacdinh >= 26) { tilemacdinh = 9;}
                        else if (tilemacdinh <= 25 || tilemacdinh >= 23) { tilemacdinh = 8;}
                        else if (tilemacdinh <= 22 || tilemacdinh >= 20) { tilemacdinh = 7;}
                        else if (tilemacdinh <= 19 || tilemacdinh >= 17) { tilemacdinh = 6;}
                        else if (tilemacdinh <= 16 || tilemacdinh >= 14) { tilemacdinh = 5;}
                        else if (tilemacdinh <= 13 || tilemacdinh >= 11) { tilemacdinh = 4;}
                        else if (tilemacdinh <= 10 || tilemacdinh >= 8) { tilemacdinh = 3;}
                        else if (tilemacdinh <= 7 || tilemacdinh >= 5) { tilemacdinh = 2;}
                        else if (tilemacdinh <= 4 || tilemacdinh >= 2) { tilemacdinh = 1;}
                        else if (tilemacdinh <= 1) { tilemacdinh = 0;}
                        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

                        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh()], itemCtVip.template.gender);
                        
                        tilemacdinh += 10;
                        
                        if (tilemacdinh > 0) {
                            for(byte i = 0; i < itemTS.itemOptions.size(); i++) {
                            if(itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                                itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param*tilemacdinh/100);
                            }
                        }
                    }
                        tilemacdinh = Util.nextInt(0, 100);
                        
                        if (tilemacdinh <= tileLucky) {
                        if (tilemacdinh >= (tileLucky - 3)) {
                            tileLucky = 3;
                        } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                            tileLucky = 2;
                        } else { tileLucky = 1; }
                        itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                        ArrayList<Integer> listOptionBonus = new ArrayList<>();
                        listOptionBonus.add(50); 
                        listOptionBonus.add(77); 
                        listOptionBonus.add(103); 
                        listOptionBonus.add(98);
                        listOptionBonus.add(99);
                        for (int i = 0; i < tileLucky; i++) {
                            tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                            itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                            listOptionBonus.remove(tilemacdinh);
                        }
                    }
                        
                        InventoryServiceNew.gI().addItemBag(player, itemTS);
                        sendEffectSuccessCombine(player);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    if (mTS != null && daMM != null && daNC != null && CtVip != null ) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    } else if (CtVip != null && mTS != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                    } else if (CtVip != null && mTS != null && daNC != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                    } else if (CtVip != null && mTS != null && daMM != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    }
                    
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void GiaHanTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHSD()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị HSD");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1191).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Sách Gia Hạn");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            Item daHacHoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1191).findFirst().get();
            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHSD).findFirst().get();
            if (daHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu Sách Gia Hạn");
                return;
            }
            if (trangBiHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị HSD");
                return;
            }

            if (trangBiHacHoa != null) {
                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 93) {
                        if (itopt.param < 0 && itopt == null) {
                            Service.getInstance().sendThongBao(player, "Không Phải Trang Bị Có HSD");
                            return;
                        }
                    }
                }
            }
            if (Util.isTrue(100, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionHacHoa = Arrays.asList(219, 220, 221, 222);
                int randomOption = idOptionHacHoa.get(Util.nextInt(0, 3));

                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 93) {
                        itopt.param += 1;
                        break;
                    }
                }
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, daHacHoa, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 1390) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 500) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 500);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1389);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 2));
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 500); // xoa manh vo
                    Service.gI().sendThongBao(player,"Xin lỗi ta đã cố hết sức");
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
private void nangCapBongTaicap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1389) {
                    bongTai = item;
                } else if (item.template.id == 1391) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 1000) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 1000);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1386);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 3));
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 1000); // xoa manh vo
                    Service.gI().sendThongBao(player,"Xin lỗi ta đã cố hết sức");
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
private void nangCapBongTaicap4(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1386) {
                    bongTai = item;
                } else if (item.template.id == 1392) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 3000) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 3000);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1387);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 4));
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 3000); // xoa manh vo
                    Service.gI().sendThongBao(player,"Xin lỗi ta đã cố hết sức");
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
private void nangCapBongTaicap5(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1387) {
                    bongTai = item;
                } else if (item.template.id == 1393) {
                    manhVo = item;
                }
            }
            if (bongTai != null && manhVo != null && manhVo.quantity >= 9999) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongTai.template = ItemService.gI().getTemplate(1388);
                    bongTai.itemOptions.add(new Item.ItemOption(72, 5));
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryServiceNew.gI().subQuantityItemsBag(player, manhVo, 9999); // xoa manh vo
                    Service.gI().sendThongBao(player,"Xin lỗi ta đã cố hết sức");
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void moChiSoBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item honbongtai = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1388) {
                    bongtai = item;
                } else if (item.template.id == 934) {
                    honbongtai = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && honbongtai != null && honbongtai.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, honbongtai, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 25)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(1, 25)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(1, 25)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(1, 25)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(1, 25)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(1, 25)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(1, 25)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(1, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }


    private void moChiSoBongTaicap3(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item thachPhu = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1151) {
                    bongtai = item;
                } else if (item.template.id == 2036) {
                    thachPhu = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && thachPhu != null && thachPhu.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thachPhu, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
         

    private void moChiSoBongTaicap4(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item bongtai = null;
            Item thachPhu = null;
            Item daxanhlam = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1152) {
                    bongtai = item;
                } else if (item.template.id == 2036) {
                    thachPhu = item;
                } else if (item.template.id == 935) {
                    daxanhlam = item;
                }
            }
            if (bongtai != null && thachPhu != null && thachPhu.quantity >= 99 && daxanhlam != null && daxanhlam.quantity >= 15) {
                player.inventory.gold -= gold;
                player.inventory.gem -= gem;
                InventoryServiceNew.gI().subQuantityItemsBag(player, thachPhu, 99);
                InventoryServiceNew.gI().subQuantityItemsBag(player, daxanhlam, 15);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    bongtai.itemOptions.clear();
                    bongtai.itemOptions.add(new Item.ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    if (rdUp == 0) {
                        bongtai.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 25)));
                    } else if (rdUp == 1) {
                        bongtai.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 25)));
                    } else if (rdUp == 2) {
                        bongtai.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 25)));
                    } else if (rdUp == 3) {
                        bongtai.itemOptions.add(new Item.ItemOption(108, Util.nextInt(5, 25)));
                    } else if (rdUp == 4) {
                        bongtai.itemOptions.add(new Item.ItemOption(94, Util.nextInt(5, 15)));
                    } else if (rdUp == 5) {
                        bongtai.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 15)));
                    } else if (rdUp == 6) {
                        bongtai.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5, 25)));
                    } else if (rdUp == 7) {
                        bongtai.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5, 25)));
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void doiVeHuyDiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item item = player.combineNew.itemsCombine.get(0);
            if (item.isNotNullItem() && item.template.id >= 555 && item.template.id <= 567) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_VE_DOI_DO_HUY_DIET) {
                    player.inventory.gold -= COST_DOI_VE_DOI_DO_HUY_DIET;
                    Item ticket = ItemService.gI().createNewItem((short) (2001 + item.template.type));
                    ticket.itemOptions.add(new Item.ItemOption(30, 0));
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
                    InventoryServiceNew.gI().addItemBag(player, ticket);
                    sendEffectOpenItem(player, item.template.iconID, ticket.template.iconID);

                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc hồng để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(102, 1));
                    }

                    InventoryServiceNew.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phaLeHoaTrangBi(Player player) {
        boolean flag = false;
        int solandap = player.combineNew.quantities;
        while (player.combineNew.quantities > 0 && !player.combineNew.itemsCombine.isEmpty() && !flag) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gold < gold) {
                Service.gI().sendThongBao(player, "Không đủ vàng để thực hiện");
                break;
            } else if (player.inventory.gem < gem) {
                Service.gI().sendThongBao(player, "Không đủ ngọc để thực hiện");
                break;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiPhaLeHoa(item)) {
                int star = 0;
                Item.ItemOption optionStar = null;
                for (Item.ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 107) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    //float ratio = getRatioPhaLeHoa(star);
                    int epint = (int) getRatioPhaLeHoa(star);
                    flag = Util.isTrue(epint,100);
                    if (flag) {
                        if (optionStar == null) {
                            item.itemOptions.add(new Item.ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        Service.getInstance().sendThongBao(player, "Lên cấp sau " + (solandap - player.combineNew.quantities + 1) + " lần đập");
                        if (optionStar != null && optionStar.param >= 12) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
            }
            player.combineNew.quantities -= 1;          
        }
        if (!flag) {
            sendEffectFailCombine(player);
        }
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendMoney(player);
        reOpenItemCombine(player);
    }


    private void nhapNgocRong(Player player) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null && item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= 7) {
                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                    InventoryServiceNew.gI().addItemBag(player, nr);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item, 7);
                    InventoryServiceNew.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                    sendEffectCombineDB(player, item.template.iconID);
                }
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) return;
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) return;
                    if (itemDBV.quantity < countDaBaoVe) return;
                }

                int level = 0;
                Item.ItemOption optionLevel = null;
                for (Item.ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    Item.ItemOption option = null;
                    Item.ItemOption option2 = null;
                    for (Item.ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new Item.ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
//                        if (optionLevel != null && optionLevel.param >= 5) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
//                                    + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
//                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 10 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 10 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3)
                        InventoryServiceNew.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
//    private void khT(Player player) {
//
//        //Trum Cot ddow
//        if (player.combineNew.itemsCombine.size() == 1) {
//            Item dtl1 = null;
//        //    Item dtl2 = null;
//            for (Item item : player.combineNew.itemsCombine) {
//                if (item.isNotNullItem()) {
//                    if (item.template.id >= 650 && item.template.id <= 662) {
//                        if (dtl1 == null) {
//                            dtl1 = item;
//                    //    } else {
//                    //        dtl2 = item;
//                        }
//                    }
//                }
//            }
//            if (dtl1 != null) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
//                        && player.inventory.gold >= 500000000) {
//                    player.inventory.gold -= 500000000;
//                    int tiLe = 100;
//                    if (Util.isTrue(tiLe, 100)) {
//                        sendEffectSuccessCombine(player);
//                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0(dtl1.template.gender, dtl1.template.type));
//                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
//                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
//                        InventoryServiceNew.gI().addItemBag(player, item);
//                    } else {
//                        sendEffectFailCombine(player);
//                    }
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl1, 1);
//            //        InventoryServiceNew.gI().subQuantityItemsBag(player, dtl2, 1);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
    private void khT(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            
            Item item = player.combineNew.itemsCombine.get(0); 
            
            Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
            Item thoivang2 = InventoryServiceNew.gI().findItemBag(player, 987);
            if (thoivang.quantity < 20) {
                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng để thực hiện");
                return;
            }
            if (thoivang.quantity < 10) {
                Service.getInstance().sendThongBao(player, "Không đủ Đá bảo vệ để thực hiện");
                return;
            }
                if (isTrangBiHakai(item) ) {   
                    int idItemKichHoatTDop[][] = {{1048,1048},
                        {1051,1051},
                        {1054,1054},
                        {1057,1057},
                        {1060,1060}};
                    int idItemKichHoatNMop[][] = {{1049,1049},
                        {1052,1052},
                        {1055,1055},
                        {1058,1058},
                        {1061,1061}};
                    int idItemKichHoatXDop[][] = {{1050,1050},
                        {1053,1053},
                        {1056,1056},
                        {1059,1059},
                        {1062,1062}};                  
                int type = item.template.type;
                int gender = item.template.gender;
                int random = Util.nextInt(0, 2);
                if (gender == 3) {             
                    gender = player.gender;               
                }           
                if (Util.isTrue(5,100))    {    
                Item itemKichHoat = null;                      
                if (gender == 0) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatTDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);                   
                    
                        if ( itemKichHoat.template.id == 1048){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1600 + (Util.nextInt(10,25 ))*16));
                        }                              
                        if ( itemKichHoat.template.id == 1051){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,104 + (Util.nextInt(10,25 ))*1)); 
                        }                         
                        if ( itemKichHoat.template.id == 1054){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,8800 + (Util.nextInt(10,25 ))*88)); 
                        }                   
                        if ( itemKichHoat.template.id == 1057){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,96 + (Util.nextInt(10,25 ))*1)); 
                        }                                   
                        if ( itemKichHoat.template.id == 1060){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(20,22 ))); 
                        }
                }               
                if (gender == 1) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatNMop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    
                        if ( itemKichHoat.template.id == 1049){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1700 + (Util.nextInt(10,25 ))*17));
                        }                                  
                        if ( itemKichHoat.template.id == 1052){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,94 + (Util.nextInt(10,25 ))*1)); 
                        }                  
                        if ( itemKichHoat.template.id == 1055){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,8600 + (Util.nextInt(10,25 ))*86)); 
                        }
                        if ( itemKichHoat.template.id == 1058){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,104 + (Util.nextInt(10,25 ))*1)); 
                        }             
                        if ( itemKichHoat.template.id == 1061){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(20,22))); 
                        }                   
                }
                if (gender == 2) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatXDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);   
                    
                        if ( itemKichHoat.template.id == 1050){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1800 + (Util.nextInt(10,25 ))*18));
                        }                  
                        if ( itemKichHoat.template.id == 1053){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,95 + (Util.nextInt(10,25 ))*1)); 
                        }                               
                        if ( itemKichHoat.template.id == 1056){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,9000 + (Util.nextInt(10,25 ))*90)); 
                        }                    
                        if ( itemKichHoat.template.id == 1059){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,100 + (Util.nextInt(10,25 ))*1)); 
                        }                                    
                        if ( itemKichHoat.template.id == 1062){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(20,22 ))); 
                        }                                   
                }
                sendEffectSuccessCombine(player);                
                itemKichHoat.itemOptions.add(new ItemOption(21, 90));
                itemKichHoat.itemOptions.add(new ItemOption(30, 0));
                sendEffectSuccessCombine(player); 
                sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().addItemBag(player, itemKichHoat);
                ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được Trang bị thiên sứ "+itemKichHoat.template.id+"" );
                } else {
                    sendEffectFailCombine(player);
                }                           
                //sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);               
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang2, 5);
                //InventoryServiceNew.gI().removeItemBag(player, item1);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void khTl(Player player) {

        //Barcoll
        if (player.combineNew.itemsCombine.size() == 2) {
            Item dtl1 = null;
            Item dtl2 = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 555 && item.template.id <= 567) {
                        if (dtl1 == null) {
                            dtl1 = item;
                        } else {
                            dtl2 = item;
                        }
                    }
                }
            }
            if (dtl1 != null && dtl2 != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= 500000000) {
                    player.inventory.gold -= 500000000;
                    int tiLe = 100;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0tl(dtl1.template.gender, dtl1.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl1, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl2, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
//    private void khHd(Player player) {
//
//        //Barcoll
//        if (player.combineNew.itemsCombine.size() == 1) {
//            Item dtl1 = null;
//        //    Item dtl2 = null;
//            for (Item item : player.combineNew.itemsCombine) {
//                if (item.isNotNullItem()) {
//                    if (item.template.id >= 555 && item.template.id <= 567) {
//                        if (dtl1 == null) {
//                            dtl1 = item;
//                    //    } else {
//                    //        dtl2 = item;
//                        }
//                    }
//                }
//            }
//            if (dtl1 != null) {
//                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
//                        && player.inventory.gold >= 500000000) {
//                    player.inventory.gold -= 500000000;
//                    int tiLe = 100;
//                    if (Util.isTrue(tiLe, 100)) {
//                        sendEffectSuccessCombine(player);
//                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0hd(dtl1.template.gender, dtl1.template.type));
//                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
//                    //    RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
//                        InventoryServiceNew.gI().addItemBag(player, item);
//                    } else {
//                        sendEffectFailCombine(player);
//                    }
//                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl1, 1);
//                //    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl2, 1);
//                    InventoryServiceNew.gI().sendItemBags(player);
//                    Service.gI().sendMoney(player);
//                    reOpenItemCombine(player);
//                }
//            }
//        }
//    }
    private void khHd(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            
            Item item = player.combineNew.itemsCombine.get(0); 
            
            Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
            if (thoivang.quantity < 20) {
                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng để thực hiện");
                return;
            }
                if (isTrangBiGod1(item) ) {   
                    int idItemKichHoatTDop[][] = {{650,650},
                        {651,651},
                        {657,657},
                        {658,658},
                        {656,656}};
                    int idItemKichHoatNMop[][] = {{652,652},
                        {653,653},
                        {659,659},
                        {660,660},
                        {656,656}};
                    int idItemKichHoatXDop[][] = {{654,654},
                        {655,655},
                        {661,661},
                        {662,662},
                        {656,656}};                  
                int type = item.template.type;
                int gender = item.template.gender;
                int random = Util.nextInt(0, 2);
                if (gender == 3) {             
                    gender = player.gender;               
                }  
                if (Util.isTrue(50,100))    {    
                Item itemKichHoat = null;
                
                if (gender == 0) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatTDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);                   
                    if (Util.isTrue(5,100)){
                        if ( itemKichHoat.template.id == 650){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1600 + (Util.nextInt(10,15 ))*16));
                        }                              
                        if ( itemKichHoat.template.id == 651){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,104 + (Util.nextInt(10,15 ))*1)); 
                        }                         
                        if ( itemKichHoat.template.id == 657){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,8800 + (Util.nextInt(10,15 ))*88)); 
                        }                   
                        if ( itemKichHoat.template.id == 658){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,96 + (Util.nextInt(10,15 ))*1)); 
                        }                                   
                        if ( itemKichHoat.template.id == 656){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(17,18 ))); 
                        }
                    }else{
                        if ( itemKichHoat.template.id == 650){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1600 + (Util.nextInt(1,9 ))*16));
                        }                              
                        if ( itemKichHoat.template.id == 651){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,104 + (Util.nextInt(1,9 ))*1)); 
                        }                         
                        if ( itemKichHoat.template.id == 657){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,8800 + (Util.nextInt(1,9 ))*88)); 
                        }                   
                        if ( itemKichHoat.template.id == 658){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,96 + (Util.nextInt(1,9 ))*1)); 
                        }                                   
                        if ( itemKichHoat.template.id == 656){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,16)); 
                        }
                    }
                }
                if (gender == 1) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatNMop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    if (Util.isTrue(5,100)){
                        if ( itemKichHoat.template.id == 652){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1700 + (Util.nextInt(10,15 ))*17));
                        }                                  
                        if ( itemKichHoat.template.id == 653){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,94 + (Util.nextInt(10,15 ))*1)); 
                        }                  
                        if ( itemKichHoat.template.id == 659){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,8600 + (Util.nextInt(10,15 ))*86)); 
                        }
                        if ( itemKichHoat.template.id == 660){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,104 + (Util.nextInt(10,15 ))*1)); 
                        }             
                        if ( itemKichHoat.template.id == 656){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(16,18 ))); 
                        }
                    }else{
                        if ( itemKichHoat.template.id == 652){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1700 + (Util.nextInt(1,9 ))*17));
                        }                                  
                        if ( itemKichHoat.template.id == 653){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,94 + (Util.nextInt(1,9 ))*1)); 
                        }                  
                        if ( itemKichHoat.template.id == 659){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,8600 + (Util.nextInt(1,9 ))*86)); 
                        }
                        if ( itemKichHoat.template.id == 660){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,104 + (Util.nextInt(1,9 ))*1)); 
                        }             
                        if ( itemKichHoat.template.id == 656){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14, 16)); 
                        }
                    }
                }
                if (gender == 2) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatXDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);   
                    if (Util.isTrue(5,100)){
                        if ( itemKichHoat.template.id == 654){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1800 + (Util.nextInt(10,15 ))*18));
                        }                  
                        if ( itemKichHoat.template.id == 655){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,95 + (Util.nextInt(10,15 ))*1)); 
                        }                               
                        if ( itemKichHoat.template.id == 661){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,9000 + (Util.nextInt(10,15 ))*90)); 
                        }                    
                        if ( itemKichHoat.template.id == 662){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,100 + (Util.nextInt(10,15 ))*1)); 
                        }                                    
                        if ( itemKichHoat.template.id == 656){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(16,18 ))); 
                        }
                    }else{
                        if ( itemKichHoat.template.id == 654){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(47,1800 + (Util.nextInt(1,9 ))*18));
                        }                  
                        if ( itemKichHoat.template.id == 655){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(22,95 + (Util.nextInt(1,9 ))*1)); 
                        }                               
                        if ( itemKichHoat.template.id == 661){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(0,9000 + (Util.nextInt(1,9 ))*90)); 
                        }                    
                        if ( itemKichHoat.template.id == 662){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(23,100 + (Util.nextInt(1,9 ))*1)); 
                        }                                    
                        if ( itemKichHoat.template.id == 656){
                            itemKichHoat.itemOptions.add(new Item.ItemOption(14,16)); 
                        }
                    }
                }

                itemKichHoat.itemOptions.add(new ItemOption(21, 70));
                itemKichHoat.itemOptions.add(new ItemOption(30, 0));
                InventoryServiceNew.gI().addItemBag(player, itemKichHoat);                
                sendEffectCombineDB(player, item.template.iconID);
                } else {
                    sendEffectFailCombine(player);
                }  
                InventoryServiceNew.gI().removeItemBag(player, item);               
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                //InventoryServiceNew.gI().removeItemBag(player, item1);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    public void openCreateItemAngel1(Player player) {
        // Công thức vip + x99 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
        //    Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTL() && item.quantity >= 99).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh thần linh");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
            return;
        }
        Item mTL = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isManhTL()) {
                        mTL = item;
                    } else if (item.isDaNangCap()) {
                        daNC = item;
                    } else if (item.isDaMayMan()) {
                        daMM = item;
                    } else if (item.isCongThucVip()) {
                        CtVip = item;
                    }
                }
            }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 ) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
                    player.inventory.gold -= 500000000;
                    
                    int tilemacdinh = 35;
                    int tileLucky = 20;
                    if (daNC != null) {
                        tilemacdinh += (daNC.template.id - 1073)*10;
                    } else {
                        tilemacdinh = tilemacdinh;
                    }
                    if (daMM != null) {
                        tileLucky += tileLucky*(daMM.template.id - 1078)*10/100;
                    } else {
                        tileLucky = tileLucky;
                    }
                    
                    if (Util.nextInt(0, 100) < tilemacdinh) {
                        Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                        if (daNC != null) {
                        Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                        }
                        if (daMM != null) {
                        Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                        }
                        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTL() && item.quantity >= 99).findFirst().get();
                        
                        tilemacdinh = Util.nextInt(0, 50);
                        if (tilemacdinh == 49) { tilemacdinh = 20;}
                        else if (tilemacdinh == 48 || tilemacdinh == 47) { tilemacdinh = 19;}
                        else if (tilemacdinh == 46 || tilemacdinh == 45) { tilemacdinh = 18;}
                        else if (tilemacdinh == 44 || tilemacdinh == 43) { tilemacdinh = 17;}
                        else if (tilemacdinh == 42 || tilemacdinh == 41) { tilemacdinh = 16;}
                        else if (tilemacdinh == 40 || tilemacdinh == 39) { tilemacdinh = 15;}
                        else if (tilemacdinh == 38 || tilemacdinh == 37) { tilemacdinh = 14;}
                        else if (tilemacdinh == 36 || tilemacdinh == 35) { tilemacdinh = 13;}
                        else if (tilemacdinh == 34 || tilemacdinh == 33) { tilemacdinh = 12;}
                        else if (tilemacdinh == 32 || tilemacdinh == 31) { tilemacdinh = 11;}
                        else if (tilemacdinh == 30 || tilemacdinh == 29) { tilemacdinh = 10;}
                        else if (tilemacdinh <= 28 || tilemacdinh >= 26) { tilemacdinh = 9;}
                        else if (tilemacdinh <= 25 || tilemacdinh >= 23) { tilemacdinh = 8;}
                        else if (tilemacdinh <= 22 || tilemacdinh >= 20) { tilemacdinh = 7;}
                        else if (tilemacdinh <= 19 || tilemacdinh >= 17) { tilemacdinh = 6;}
                        else if (tilemacdinh <= 16 || tilemacdinh >= 14) { tilemacdinh = 5;}
                        else if (tilemacdinh <= 13 || tilemacdinh >= 11) { tilemacdinh = 4;}
                        else if (tilemacdinh <= 10 || tilemacdinh >= 8) { tilemacdinh = 3;}
                        else if (tilemacdinh <= 7 || tilemacdinh >= 5) { tilemacdinh = 2;}
                        else if (tilemacdinh <= 4 || tilemacdinh >= 2) { tilemacdinh = 1;}
                        else if (tilemacdinh <= 1) { tilemacdinh = 0;}
                        short[][] itemIdl = {{555, 556, 662, 663, 662}, {557, 558, 564, 565, 561}, {559, 560, 566, 567, 561}}; // thứ tự td - 0,nm - 1, xd - 2

                        Item itemTL = ItemService.gI().DoThanLinh(itemIdl[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh1()], itemCtVip.template.gender);
                        
                        tilemacdinh += 10;
                        
                        if (tilemacdinh > 0) {
                            for(byte i = 0; i < itemTL.itemOptions.size(); i++) {
                            if(itemTL.itemOptions.get(i).optionTemplate.id != 21 && itemTL.itemOptions.get(i).optionTemplate.id != 30) {
                                itemTL.itemOptions.get(i).param += (itemTL.itemOptions.get(i).param*tilemacdinh/100);
                            }
                        }
                    }
                        tilemacdinh = Util.nextInt(0, 100);
                        
                        if (tilemacdinh <= tileLucky) {
                        if (tilemacdinh >= (tileLucky - 3)) {
                            tileLucky = 3;
                        } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                            tileLucky = 2;
                        } else { tileLucky = 1; }
                        itemTL.itemOptions.add(new Item.ItemOption(15, tileLucky));
                        ArrayList<Integer> listOptionBonus = new ArrayList<>();
                        listOptionBonus.add(50); 
                        listOptionBonus.add(77); 
                        listOptionBonus.add(103); 
                        listOptionBonus.add(98);
                        listOptionBonus.add(99);
                        for (int i = 0; i < tileLucky; i++) {
                            tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                            itemTL.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                            listOptionBonus.remove(tilemacdinh);
                        }
                    }
                        
                        InventoryServiceNew.gI().addItemBag(player, itemTL);
                        sendEffectSuccessCombine(player);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    if (mTL != null && daMM != null && daNC != null && CtVip != null ) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTL, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    } else if (CtVip != null && mTL != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTL, 99);
                    } else if (CtVip != null && mTL != null && daNC != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTL, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                    } else if (CtVip != null && mTL != null && daMM != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTL, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    }
                    
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
    public void nangCapChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.gI().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && (item.template.id >= 1239 && item.template.id <= 1247))
                .count() != 1) {
            Service.gI().sendThongBao(player, "Chân mệnh");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 2025)
                .count() != 1) {
            Service.gI().sendThongBao(player, "Thiếu Xu");
            return;
        }
        ///

        Item chanMenh = null;
        Item ngoc1sao = null;

        for (Item item : player.combineNew.itemsCombine) {
            if (item.template.id >= 1239 && item.template.id <= 1247) {
                chanMenh = item;
            } else if (item.template.id == 2025) {
                ngoc1sao = item;
            }
        }

        if (chanMenh != null && ngoc1sao != null && ngoc1sao.quantity >= 5000 && player.inventory.ruby > 5000) {
            player.inventory.ruby -= 50000;
            player.inventory.gem -= 0;
            Item chanMenhMoi = ItemService.gI().createNewItem((short) (chanMenh.template.id + 1));
            int capChanMenh = chanMenhMoi.template.id - 1239;
            chanMenhMoi.itemOptions.add(new ItemOption(50, 25 + capChanMenh));
            chanMenhMoi.itemOptions.add(new ItemOption(77, 25 + capChanMenh));
            chanMenhMoi.itemOptions.add(new ItemOption(103, 25 + capChanMenh));
            chanMenhMoi.itemOptions.add(new ItemOption(5, 5 + capChanMenh));
            chanMenhMoi.itemOptions.add(new ItemOption(14, 25 + capChanMenh));
            chanMenhMoi.itemOptions.add(new ItemOption(101, 25 + capChanMenh));
            chanMenhMoi.itemOptions.add(new ItemOption(98, 25 + capChanMenh));
            InventoryServiceNew.gI().addItemBag(player, chanMenhMoi);
            Service.gI().sendThongBao(player, "|7|Bạn nhận được " + chanMenhMoi.template.name );
            InventoryServiceNew.gI().subQuantityItemsBag(player, ngoc1sao, 5000);
            InventoryServiceNew.gI().subQuantityItemsBag(player, chanMenh, 1);
            sendEffectSuccessCombine(player);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.gI().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.gI().sendThongBao(player, "Không đủ nguyên liệu nâng cấp!");
            reOpenItemCombine(player);
        }

        ///
    }
    
    public void openCreateItemAngel2(Player player) {
        // Công thức vip + x99 Mảnh thiên sứ + đá nâng cấp + đá may mắn
        if (player.combineNew.itemsCombine.size() < 2 || player.combineNew.itemsCombine.size() > 4) {
        //    Service.getInstance().sendThongBao(player, "Thiếu vật phẩm, vui lòng thêm vào");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Công thức Vip");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhHD() && item.quantity >= 99).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Mảnh hủy diệt");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá nâng cấp");
            return;
        }
        if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1 || player.combineNew.itemsCombine.size() == 4 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Đá may mắn");
            return;
        }
        Item mTS = null, daNC = null, daMM = null, CtVip = null;
        for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isManhHD()) {
                        mTS = item;
                    } else if (item.isDaNangCap()) {
                        daNC = item;
                    } else if (item.isDaMayMan()) {
                        daMM = item;
                    } else if (item.isCongThucVip()) {
                        CtVip = item;
                    }
                }
            }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 ) {//check chỗ trống hành trang
            if (player.inventory.gold < 500000000) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
                    player.inventory.gold -= 500000000;
                    
                    int tilemacdinh = 35;
                    int tileLucky = 20;
                    if (daNC != null) {
                        tilemacdinh += (daNC.template.id - 1073)*10;
                    } else {
                        tilemacdinh = tilemacdinh;
                    }
                    if (daMM != null) {
                        tileLucky += tileLucky*(daMM.template.id - 1078)*10/100;
                    } else {
                        tileLucky = tileLucky;
                    }
                    
                    if (Util.nextInt(0, 100) < tilemacdinh) {
                        Item itemCtVip = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThucVip()).findFirst().get();
                        if (daNC != null) {
                        Item itemDaNangC = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaNangCap()).findFirst().get();
                        }
                        if (daMM != null) {
                        Item itemDaMayM = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDaMayMan()).findFirst().get();
                        }
                        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhHD() && item.quantity >= 99).findFirst().get();
                        
                        tilemacdinh = Util.nextInt(0, 50);
                        if (tilemacdinh == 49) { tilemacdinh = 20;}
                        else if (tilemacdinh == 48 || tilemacdinh == 47) { tilemacdinh = 19;}
                        else if (tilemacdinh == 46 || tilemacdinh == 45) { tilemacdinh = 18;}
                        else if (tilemacdinh == 44 || tilemacdinh == 43) { tilemacdinh = 17;}
                        else if (tilemacdinh == 42 || tilemacdinh == 41) { tilemacdinh = 16;}
                        else if (tilemacdinh == 40 || tilemacdinh == 39) { tilemacdinh = 15;}
                        else if (tilemacdinh == 38 || tilemacdinh == 37) { tilemacdinh = 14;}
                        else if (tilemacdinh == 36 || tilemacdinh == 35) { tilemacdinh = 13;}
                        else if (tilemacdinh == 34 || tilemacdinh == 33) { tilemacdinh = 12;}
                        else if (tilemacdinh == 32 || tilemacdinh == 31) { tilemacdinh = 11;}
                        else if (tilemacdinh == 30 || tilemacdinh == 29) { tilemacdinh = 10;}
                        else if (tilemacdinh <= 28 || tilemacdinh >= 26) { tilemacdinh = 9;}
                        else if (tilemacdinh <= 25 || tilemacdinh >= 23) { tilemacdinh = 8;}
                        else if (tilemacdinh <= 22 || tilemacdinh >= 20) { tilemacdinh = 7;}
                        else if (tilemacdinh <= 19 || tilemacdinh >= 17) { tilemacdinh = 6;}
                        else if (tilemacdinh <= 16 || tilemacdinh >= 14) { tilemacdinh = 5;}
                        else if (tilemacdinh <= 13 || tilemacdinh >= 11) { tilemacdinh = 4;}
                        else if (tilemacdinh <= 10 || tilemacdinh >= 8) { tilemacdinh = 3;}
                        else if (tilemacdinh <= 7 || tilemacdinh >= 5) { tilemacdinh = 2;}
                        else if (tilemacdinh <= 4 || tilemacdinh >= 2) { tilemacdinh = 1;}
                        else if (tilemacdinh <= 1) { tilemacdinh = 0;}
                        short[][] itemIdd = {{650, 651, 657, 656, 656}, {652, 653, 659, 660, 656}, {654, 655, 661, 662, 656}}; // thứ tự td - 0,nm - 1, xd - 2

                        Item itemTS = ItemService.gI().DoHuyDiet(itemIdd[itemCtVip.template.gender > 2 ? player.gender : itemCtVip.template.gender][itemManh.typeIdManh2()], itemCtVip.template.gender);
                        
                        tilemacdinh += 10;
                        
                        if (tilemacdinh > 0) {
                            for(byte i = 0; i < itemTS.itemOptions.size(); i++) {
                            if(itemTS.itemOptions.get(i).optionTemplate.id != 21 && itemTS.itemOptions.get(i).optionTemplate.id != 30) {
                                itemTS.itemOptions.get(i).param += (itemTS.itemOptions.get(i).param*tilemacdinh/100);
                            }
                        }
                    }
                        tilemacdinh = Util.nextInt(0, 100);
                        
                        if (tilemacdinh <= tileLucky) {
                        if (tilemacdinh >= (tileLucky - 3)) {
                            tileLucky = 3;
                        } else if (tilemacdinh <= (tileLucky - 4) && tilemacdinh >= (tileLucky - 10)) {
                            tileLucky = 2;
                        } else { tileLucky = 1; }
                        itemTS.itemOptions.add(new Item.ItemOption(15, tileLucky));
                        ArrayList<Integer> listOptionBonus = new ArrayList<>();
                        listOptionBonus.add(50); 
                        listOptionBonus.add(77); 
                        listOptionBonus.add(103); 
                        listOptionBonus.add(98);
                        listOptionBonus.add(99);
                        for (int i = 0; i < tileLucky; i++) {
                            tilemacdinh = Util.nextInt(0, listOptionBonus.size());
                            itemTS.itemOptions.add(new ItemOption(listOptionBonus.get(tilemacdinh), Util.nextInt(1, 5)));
                            listOptionBonus.remove(tilemacdinh);
                        }
                    }
                        
                        InventoryServiceNew.gI().addItemBag(player, itemTS);
                        sendEffectSuccessCombine(player);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    if (mTS != null && daMM != null && daNC != null && CtVip != null ) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    } else if (CtVip != null && mTS != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                    } else if (CtVip != null && mTS != null && daNC != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daNC, 1);
                    } else if (CtVip != null && mTS != null && daMM != null) {
                        InventoryServiceNew.gI().subQuantityItemsBag(player, CtVip, 1);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, mTS, 99);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, daMM, 1);
                    }
                    
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }
    
    private void NgocBoi(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            Item ngocboi1 = null; 
            Item TV = null;
            Item NRO = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.isNotNullItem()) {
                            if (item.template.id == 457) {
                                TV = item;
                            } else if (item.template.id == 16) {
                                NRO = item;
                            } else if (item.template.id == 1700
                                    || item.template.id == 1701
                                    || item.template.id == 1702
                                    || item.template.id == 1703
                                    || item.template.id == 1704
                                    || item.template.id == 1705
                                    || item.template.id == 1706
                                    || item.template.id == 1707) {
                                ngocboi1 = item;                             
                            }  
                        }
                    }
                }

                    if (ngocboi1 == null || TV == null || NRO == null) {
                        Service.getInstance().sendThongBao(player, "Không đủ vật phẩm ");
                        return;                  
                    }
                    if (Util.isTrue(40, 100)) {
                        if (player.inventory.ruby > 1000 ){
                     
                    if (ngocboi1.template.id == 1700 && TV.quantity >=49 && NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1701);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(2, 5)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(2, 5)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(2, 5)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(2, 5)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(2, 5)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                    }
                    
                    if (ngocboi1.template.id == 1701  && TV.quantity >=49  && NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                         InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1702);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5, 7)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5, 7)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5, 7)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(5, 7)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(5, 7)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                    }
                    
                    if (ngocboi1.template.id == 1702 &&  TV.quantity >=49 &&  NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1703);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(7, 10)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(7, 10)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(7, 10)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(7, 10)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(7, 10)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                    }
                    
                    if (ngocboi1.template.id == 1703  && TV.quantity >=49  && NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1704);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 17)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 17)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 17)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(15, 17)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(15, 17)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                      
                    }
                    if (ngocboi1.template.id == 1704  && TV.quantity >=49  && NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1705);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(17, 20)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(17, 20)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(17, 20)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(17, 20)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(17, 20)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                      
                    }
                    if (ngocboi1.template.id == 1705  && TV.quantity >=49  && NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1706);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 25)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 25)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 25)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(20, 25)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(20, 25)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                      
                    }
                    if (ngocboi1.template.id == 1706  && TV.quantity >=49  && NRO.quantity >=10) {
                                   
                        InventoryServiceNew.gI().subQuantityItemsBag(player, TV, 49);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, NRO, 10);
                        InventoryServiceNew.gI().subQuantityItemsBag(player, ngocboi1, 1);
                        
                        Item item = ItemService.gI().createNewItem((short) 1707);
                        item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25, 30)));
                        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25, 30)));
                        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25, 30)));
                        item.itemOptions.add(new Item.ItemOption(5, Util.nextInt(25, 30)));
                        item.itemOptions.add(new Item.ItemOption(14, Util.nextInt(25, 30)));
                        InventoryServiceNew.gI().addItemBag(player, item);
                        Service.getInstance().sendThongBao(player, "Bạn đã Tiến Hóa Ngọc Bội thành công");
                        player.inventory.ruby -= 1000;
                        sendEffectSuccessCombine(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.getInstance().sendMoney(player);
                        player.combineNew.itemsCombine.clear();
                        reOpenItemCombine(player);
                        return;
                        
                      
                    }
                    
                    
                        }else {
                    Service.getInstance().sendThongBao(player, "Không đủ điều kiện để Tiến Hóa Ngọc Bội");
                    return;
                     }
                    }
        }
    }
    private void khTs(Player player) {

        //Barcoll
        if (player.combineNew.itemsCombine.size() == 2) {
            Item dtl1 = null;
            Item dtl2 = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.isNotNullItem()) {
                    if (item.template.id >= 1048 && item.template.id <= 1062) {
                        if (dtl1 == null) {
                            dtl1 = item;
                        } else {
                            dtl2 = item;
                        }
                    }
                }
            }
            if (dtl1 != null && dtl2 != null) {
                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0 //check chỗ trống hành trang
                        && player.inventory.gold >= 500000000) {
                    player.inventory.gold -= 500000000;
                    int tiLe = 100;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) getTempIdItemC0ts(dtl1.template.gender, dtl1.template.type));
                        RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                        RewardService.gI().initActivationOption(item.template.gender < 3 ? item.template.gender : player.gender, item.template.type, item.itemOptions);
                        InventoryServiceNew.gI().addItemBag(player, item);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl1, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, dtl2, 1);
                    InventoryServiceNew.gI().sendItemBags(player);
                    Service.gI().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }
    private void khamdaHP(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            int ngusac = player.combineNew.ngusacCombine;
            int dakham = player.combineNew.dakham;
            Item dadakham = InventoryServiceNew.gI().findItemBag(player, 1613);
            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 674);
            if (dadakham.quantity < dakham) {
                Service.getInstance().sendThongBao(player, "Không đủ đá khảm để thực hiện");
                return;
            }
            if (dangusac.quantity < ngusac) {
                Service.getInstance().sendThongBao(player, "Không đủ đá ngũ sắc để thực hiện");
                return;
            }
            int star = 0;
            Item trangBi = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isradavip(item)) {
                    trangBi = item;
                }
            }
            if (trangBi != null ) {
                Item.ItemOption optionDaKham = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 195) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
            if (star < MAX_STAR_KHAM_DA) {
                dadakham.quantity -= dakham;
                dangusac.quantity -= ngusac;
                if(dadakham.template.id == 1613) {
                    int optionId = getOptionDaKham(dadakham);
                    int param = getParamDaKhamHPMP(star);
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                    }
                }
                    if (optionDaKham != null) {
                        optionDaKham.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(195, 1));
                    }
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
        }
      }
    }
    private void khamdaMP(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            int ngusac = player.combineNew.ngusacCombine;
            int dakham = player.combineNew.dakham;
            Item dadakham = InventoryServiceNew.gI().findItemBag(player, 1614);
            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 674);
            if (dadakham.quantity < dakham) {
                Service.getInstance().sendThongBao(player, "Không đủ đá khảm để thực hiện");
                return;
            }
            if (dangusac.quantity < ngusac) {
                Service.getInstance().sendThongBao(player, "Không đủ đá ngũ sắc để thực hiện");
                return;
            }
            int star = 0;
            Item trangBi = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isradavip(item)) {
                    trangBi = item;
                }
            }
            if (trangBi != null ) {
                Item.ItemOption optionDaKham = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 195) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
                if (star < MAX_STAR_KHAM_DA) {
                    dadakham.quantity -= dakham;
                    dangusac.quantity -= ngusac;
                    if(dadakham.template.id == 1614) {
                        int optionId = getOptionDaKham(dadakham);
                        int param = getParamDaKhamHPMP(star);
                        Item.ItemOption option = null;
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == optionId) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            option.param += param;
                        } else {
                            trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                        }
                    }
                    if (optionDaKham != null) {
                        optionDaKham.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(195, 1));
                    }
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void khamdaDAME(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            int ngusac = player.combineNew.ngusacCombine;
            int dakham = player.combineNew.dakham;
            Item dadakham = InventoryServiceNew.gI().findItemBag(player, 1615);
            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 674);
            if (dadakham.quantity < dakham) {
                Service.getInstance().sendThongBao(player, "Không đủ đá khảm để thực hiện");
                return;
            }
            if (dangusac.quantity < ngusac) {
                Service.getInstance().sendThongBao(player, "Không đủ đá ngũ sắc để thực hiện");
                return;
            }
            int star = 0;
            Item trangBi = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isradavip(item)) {
                    trangBi = item;
                }
            }
            if (trangBi != null ) {
                Item.ItemOption optionDaKham = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 195) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
                if (star < MAX_STAR_KHAM_DA) {
                    dadakham.quantity -= dakham;
                    dangusac.quantity -= ngusac;
                    if(dadakham.template.id == 1615) {
                        int optionId = getOptionDaKham(dadakham);
                        int param = getParamDaKhamDame(star);
                        Item.ItemOption option = null;
                        for (Item.ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == optionId) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            option.param += param;
                        } else {
                            trangBi.itemOptions.add(new Item.ItemOption(optionId, param));
                        }
                    }
                    if (optionDaKham != null) {
                        optionDaKham.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(195, 1));
                    }
                    sendEffectSuccessCombine(player);
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    
    
    private void setNangCapPet2(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int star = 0;
            int gold = 5000;
            int ngusac = player.combineNew.ngusacCombine;
            Item dangusac = InventoryServiceNew.gI().findItemBag(player, 457);
            if (dangusac.quantity < ngusac) {
                Service.getInstance().sendThongBao(player, "Không đủ Thỏi vàng để thực hiện");
                return;
            }
            if (player.inventory.ruby < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ hồng ngọc thực hiện");
                return;
            }
            Item trangBi = player.combineNew.itemsCombine.get(0);
            Item trangBiGod = player.combineNew.itemsCombine.get(1);
            for (Item item : player.combineNew.itemsCombine) {
                if (isLinhThu(item)) {
                    trangBi = item;
                }
            }
            if (trangBi != null && isTrangBiGod(trangBiGod)) {
                Item.ItemOption optionDaKham = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
                if (star < MAX_LEVEL_PET) {
                    player.inventory.ruby -= gold;
                    dangusac.quantity -= ngusac;
                    int[] optionIds = {50, 77, 103, 14, 5,101};
                    int param;
                    if(trangBiGod.template.type ==2 ||trangBiGod.template.type ==4){
                         param = 2;
                    }else {
                         param = 1;
                    }
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        for (int id : optionIds) {
                            if (io.optionTemplate.id == id) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            break;
                        }
                    }
                    //byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (option != null) {
                            for (int id : optionIds) {
                                for (Item.ItemOption io : trangBi.itemOptions) {
                                    if (io.optionTemplate.id == id) {
                                        io.param += param;
                                        break;
                                    }
                                }
                            }
                        } else {
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[0], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[1], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[2], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[3], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[4], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[5], param));
                        }
                    if (optionDaKham != null) {
                        optionDaKham.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(72, 1));
                    }
                        sendEffectSuccessCombine(player);
                        if (optionDaKham != null && optionDaKham.param >= 3) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name
                                    + "thành công nâng cấp " + trangBi.template.name + " lên cấp " + optionDaKham.param );
                        }
                } else {
                    sendEffectFailCombine(player);
                }
                }
                InventoryServiceNew.gI().removeItemBag(player, trangBiGod);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void setNangCapPK(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int star = 0;
            int gold = 5000;
//            int thoivang = 10;
            Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
            if (thoivang.quantity < 20) {
                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng để thực hiện");
                return;
            }
            
            Item trangBi = player.combineNew.itemsCombine.get(0);
            Item trangBiGod = player.combineNew.itemsCombine.get(1);
            
            for (Item item : player.combineNew.itemsCombine) {
                if (isLinhThu(item)) {
                    trangBi = item;
                }
            }
            if (trangBi != null && isTrangBiGod(trangBiGod)) {
                Item.ItemOption optionDaKham = null;
                for (Item.ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        star = io.param;
                        optionDaKham = io;
                    }
                }
                if (star < MAX_LEVEL_PET) {
                    //player.inventory.ruby -= gold;
                    //dangusac.quantity -= ngusac;
                    int[] optionIds = {50, 77, 103, 14, 5,101};
                    int param;
                    if(trangBiGod.template.type ==2 ||trangBiGod.template.type ==4){
                         param = 2;
                    }else {
                         param = 1;
                    }
                    Item.ItemOption option = null;
                    for (Item.ItemOption io : trangBi.itemOptions) {
                        for (int id : optionIds) {
                            if (io.optionTemplate.id == id) {
                                option = io;
                                break;
                            }
                        }
                        if (option != null) {
                            break;
                        }
                    }
                    //byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    byte ratio = (optionDaKham != null && optionDaKham.param > 4) ? (byte) 1 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (option != null) {
                            for (int id : optionIds) {
                                for (Item.ItemOption io : trangBi.itemOptions) {
                                    if (io.optionTemplate.id == id) {
                                        io.param += param;
                                        break;
                                    }
                                }
                            }
                        } else {
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[0], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[1], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[2], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[3], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[4], param));
                            trangBi.itemOptions.add(new Item.ItemOption(optionIds[5], param));
                        }
                    if (optionDaKham != null) {
                        optionDaKham.param++;
                    } else {
                        trangBi.itemOptions.add(new Item.ItemOption(72, 1));
                    }
                        sendEffectSuccessCombine(player);
                        if (optionDaKham != null && optionDaKham.param >= 3) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name
                                    + "thành công nâng cấp " + trangBi.template.name + " lên cấp " + optionDaKham.param );
                        }
                } else {
                    sendEffectFailCombine(player);
                }
                }
                InventoryServiceNew.gI().removeItemBag(player, trangBiGod);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 20);
                InventoryServiceNew.gI().sendItemBags(player);
                
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    //--------------------------------------------------------------------------

    /**r
     * Hiệu ứng mở item
     *
     * @param player
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 5000000;
            case 1:
                return 25000000;
            case 2:
                return 40000000;
            case 3:
                return 60000000;
            case 4:
                return 75000000;
            case 5:
                return 90000000;
            case 6:
                return 120000000;
            case 7:
                return 160000000;
            case 8:
                return 500000000;
            case 9:
                return 1300000000;
            case 10:
                return 500000000;
            case 11:
                return 500000000;
            case 12:
                return 500000000;
                 case 13:
                return 1500000000;
            case 14:
                return 1500000000;
            case 15:
                return 1500000000;
            
            case 16:
                return 1500000000;
            case 17:
                return 1500000000;
                 case 18:
                return 1500000000;
            case 19:
                return 2000000000;
            case 20:
                return 2000000000;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 90f;
            case 1:
                return 60f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 8f;
            case 5:
                return 2f;
            case 6:
                return 1.5f;
            case 7:
                return 1.2f;
            case 8:
                return 1.1f; 
            case 9:
                return 1f;    
            case 10:
                return 1f;
            case 11:
                return 1f;
             case 12:
                return 1f;   
                 case 13:
                return 1f;
            case 14:
                return 1f;
             case 15:
                return 1f; 
              case 16:
                return 1f;
             case 17:
                return 1f;   
              case 18:
                return 1f;
            case 19:
                return 1f;
             case 20:
                return 1f; 
        }
        
        
        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 70;
            case 7:
                return 80;
            case 8:
                return 90;    
            case 9:
                return 150;    
            case 10:
                return 50;
//            case 11:
//                return 50;
//             case 12:
//                return 50;      
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
                
            case 5:
                return 50;
            case 6:
                return 100;
        }
        return 0;
    }

    private double getTileNangCapDo(int level) {
        switch (level) {
            case 0:
                return 80;
            case 1:
                return 50;
            case 2:
                return 20;
            case 3:
                return 15;
            case 4:
                return 10;
            case 5:
                return 8;
            case 6:
                return 5;
            case 7: // 7 sao
                return 2;
            case 8:
                return 1;
//            case 9:
//                return 1;
//            case 10: // 7 sao
//                return 1.3;
//            case 11: // 7 sao
//                return 1.3; 
//            case 12: // 7 sao
//                return 1.3;     
//           case 13:
//                return 50;
//            case 14:
//                return 20;
//            case 15:
//                return 15;
//            case 16:
//                return 10;
//            case 5:
//                return 8;
//            case 6:
//                return 5;
//            case 7: // 7 sao
//                return 2;
//            case 8:
//                return 1;
//            case 9:
//                return 1;
//            case 10: // 7 sao
//                return 0.3;
//            case 11: // 7 sao
//                return 0.3; 
//            case 12: // 7 sao
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) {
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 15;
            case 3:
                return 20;
            case 4:
                return 27;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 70;
            case 8:
                return 90;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        switch (level) {
            case 0:
                return 1;
            case 1:
                return 1;
            case 2:
                return 1;
            case 3:
                return 1;
            case 4:
                return 1;
            case 5:
                return 1;
            case 6:
                return 1;
            case 7:
                return 1;
            case 8:
                return 1;
        }
        return 0;
    }
    
    private int getngusacKhamDa(int star) {
        switch (star) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 40;
            case 4:
                return 60;
            case 5:
                return 80;
            case 6:
                return 100;
            case 7:
                return 150;
            case 8:
                return 200;
            case 9:
                return 300;
        }
        return 0;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 1000000;
            case 1:
                return 7000000;
            case 2:
                return 20000000;
            case 3:
                return 50000000;
            case 4:
                return 70000000;
            case 5:
                return 90000000;
            case 6:
                return 120000000;
            case 7:
                return 150000000;
            case 8:
                return 180000000;
        }
        return 0;
    }
    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    
        private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }
private int getTempIdItemC0tl(int gender, int type) {
        if (type == 4) {
            return 561;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 555;
                    case 1:
                        return 556;
                    case 2:
                        return 562;
                    case 3:
                        return 563;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 557;
                    case 1:
                        return 558;
                    case 2:
                        return 564;
                    case 3:
                        return 565;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 559;
                    case 1:
                        return 560;
                    case 2:
                        return 566;
                    case 3:
                        return 567;
                }
                break;
        }
        return -1;
    }

private int getDaKham(int star) {
        switch (star) {
            case 0:
                return 10;
            case 1:
                return 20;
            case 2:
                return 40;
            case 3:
                return 80;
            case 4:
                return 160;
            case 5:
                return 320;
            case 6:
                return 640;
            case 7:
                return 1280;
            case 8:
                return 3000;
            case 9:
                return 5000;
        }
        return 0;
    }
private int getParamDaKhamHPMP(int star) {
        switch (star) {
            case 0:
                return 3000; // hp
            case 1:
                return 8000; // ki
            case 2:
                return 11000; // hp 30s
            case 3:
                return 14000; // hp
            case 4:
                return 20000; // ki
            case 5:
                return 25000; // hp 30s
            case 6:
                return 35000; // hp
            case 7:
                return 50000; // ki
            case 8:
                return 90000; // hp 30s
            case 9:
                return 200000; // hp
            default:
                return -1;
        }
    }
private int getTempIdItemC0hd(int gender, int type) {
        if (type == 4) {
            return 656;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 650;
                    case 1:
                        return 651;
                    case 2:
                        return 657;
                    case 3:
                        return 658;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 652;
                    case 1:
                        return 653;
                    case 2:
                        return 659;
                    case 3:
                        return 660;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 654;
                    case 1:
                        return 655;
                    case 2:
                        return 661;
                    case 3:
                        return 662;
                }
                break;
        }
        return -1;
    }
private int getTempIdItemC0ts(int gender, int type) {
        if (type == 4) {
            return 1981;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 1048;
                    case 1:
                        return 1051;
                    case 2:
                        return 1054;
                    case 3:
                        return 1057;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1049;
                    case 1:
                        return 1052;
                    case 2:
                        return 1055;
                    case 3:
                        return 1058;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 1050;
                    case 1:
                        return 1053;
                    case 2:
                        return 1056;
                    case 3:
                        return 1059;
                }
                break;
        }
        return -1;
    }
    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }
    private float getRatioPhaLeHoa2(int star) { //tile nang cap pet2
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 50f;
            case 2:
                return 20f;
            case 3:
                return 7f;
            case 4:
                return 3f;
            case 5:
                return 0.6f;
            case 6:
                return 0.45f;//1f;
            case 7:
                return 0.25f;//0.5f;
            case 8:
                return 0.5f;
            case 9:
                return 0.7f;    
            case 10:
                return 0.5f;
            case 11:
                return 0.03f;
             case 12:
                return 0.1f;    
        }
        
        
        return 0;
    }
    private float getRatioPhuKien(int star) { //tile nang cap pet2
        switch (star) {
            case 0:
                return 50f;
            case 1:
                return 20f;
            case 2:
                return 5f;
            case 3:
                return 2f;
            case 4:
                return 1f;
            case 5:
                return 1f;
            case 6:
                return 2f;//1f;
            case 7:
                return 10f;//0.5f;
            case 8:
                return 0.5f;
            case 9:
                return 0.7f;    
            case 10:
                return 0.5f;
            case 11:
                return 0.03f;
             case 12:
                return 0.1f;    
        }
        
        
        return 0;
    }
    private String getNameItemC0hd(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo Hủy diệt";
                    case 1:
                        return "Quần Hủy diệt";
                    case 2:
                        return "Găng Hủy diệt";
                    case 3:
                        return "Giầy Hủy diệt";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo Hủy diệt";
                    case 1:
                        return "Quần Hủy diệt";
                    case 2:
                        return "Găng Hủy diệt";
                    case 3:
                        return "Giầy Hủy diệt";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo Hủy diệt";
                    case 1:
                        return "Quần Hủy diệt";
                    case 2:
                        return "Găng Hủy diệt";
                    case 3:
                        return "Giầy Hủy diệt";
                }
                break;
        }
        return "";
    }
    private int getOptionDaKham(Item daKham) {
        switch (daKham.template.id) {
            case 1613:
                return 6; // hp
            case 1614:
                return 7; // ki
            case 1615:
                return 0; // hp 30s
            default:
                return -1;
        }
    }
    private int getParamDaKhamDame(int start) {
        switch (start) {
            case 0:
                return 400; // hp
            case 1:
                return 500; // ki
            case 2:
                return 600; // hp 30s
            case 3:
                return 850; // hp
            case 4:
                return 1300; // ki
            case 5:
                return 1700; // hp 30s
            case 6:
                return 2300; // hp
            case 7:
                return 3000; // ki
            case 8:
                return 4000; // hp 30s
            case 9:
                return 6000; // hp
            default:
                return -1;
        }
    }
    private String getNameItemC0ts(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo Thiên Sứ";
                    case 1:
                        return "Quần Thiên Sứ";
                    case 2:
                        return "Găng Thiên Sứ";
                    case 3:
                        return "Giầy Thiên Sứ";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo Thiên Sứ";
                    case 1:
                        return "Quần Thiên Sứ";
                    case 2:
                        return "Găng Thiên Sứ";
                    case 3:
                        return "Giầy Thiên Sứ";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo Thiên Sứ";
                    case 1:
                        return "Quần Thiên Sứ";
                    case 2:
                        return "Găng Thiên Sứ";
                    case 3:
                        return "Giầy Thiên Sứ";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------check
    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean istrangbiKH(Item item){
        if (item != null && item.isNotNullItem()) {
            for (Item.ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id >= 127 && io.optionTemplate.id <= 135){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isTrangBiHakai(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 650 && item.template.id <= 662) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean isTrangBiGod1(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 555 && item.template.id <= 567) {
                //if(item.template.type ==2 || item.template.type ==4) {
                    return true;
                //}
            } else {
                return false;
            }
        } else {
            return false;               
        }
    }
    
    public boolean isChuvan(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1178) {
                //if(item.template.type ==2 || item.template.type ==4) {
                    return true;
                //}
            } else {
                return false;
            }
        } else {
            return false;               
        }
    }
    public boolean isChusu(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1179 ) {
                
                    return true;
                
            
            }
        } else {
            return false;
        }
        return false;
    }
    public boolean isChunhu(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1180) {
                //if(item.template.type ==2 || item.template.type ==4) {
                    return true;
                //}
            } else {
                return false;
            }
        } else {
            return false;               
        }
    }
    public boolean isChuy(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1181) {
                //if(item.template.type ==2 || item.template.type ==4) {
                    return true;
                //}
            } else {
                return false;
            }
        } else {
            return false;               
        }
    }
    
    public boolean isTrangBiGod(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 555 && item.template.id <= 567) {
                    return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean isTrangBiAngel(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 1048 && item.template.id <= 1062) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    public boolean isPhuKien(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type ==11) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean isLinhThu(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type ==72) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private boolean isradavip(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >=1609 && item.template.id <=1612) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private void setCuongHoa(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int dakham = player.combineNew.dakham;
            Item dadakham = InventoryServiceNew.gI().findItemBag(player, 1616);
            if (dadakham.quantity < dakham) {
                Service.getInstance().sendThongBao(player, "Không đủ Đá ngọc bảo để thực hiện");
                return;
            }
           boolean star = false;
            Item trangBi = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (istrangbiKH(item)) {
                    trangBi = item;
                }
            }
            if (trangBi != null ) {
                if (!star) {
                    dadakham.quantity -= dakham;
                    player.inventory.gold -= gold;
                    if (Util.isTrue(1,10)) {
                        if (trangBi.template.gender == 0) {
                            trangBi.itemOptions.add(new Item.ItemOption(35, 0));
                        } else if (trangBi.template.gender == 1) {
                            trangBi.itemOptions.add(new Item.ItemOption(36, 0));
                        } else if (trangBi.template.gender == 2) {
                            trangBi.itemOptions.add(new Item.ItemOption(34, 0));
                         } else if (trangBi.template.gender == 3) {
                            trangBi.itemOptions.add(new Item.ItemOption(Util.nextInt(34, 35),0));
                        }
                        sendEffectSuccessCombine(player);
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa khảm ấn "
                                    + "thành công " + trangBi.template.name);
                    }else{
                        sendEffectFailCombine(player);
                    }
                }
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    
    private void prtl(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiGod(item)) {
                player.diemphangiai += Util.nextInt(1,10);               
        //        sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void prhd(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            if (isTrangBiHakai(item)) {               
                player.diemphangiai += Util.nextInt(10,50);               
        //        sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void prts(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);            
            if (isTrangBiAngel(item)) {               
                player.diemphangiai += Util.nextInt(50,200);               
            //    sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    
    private void setDoiHopQua(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            Item item1 = player.combineNew.itemsCombine.get(1);
            Item item2 = player.combineNew.itemsCombine.get(2);
            Item item3 = player.combineNew.itemsCombine.get(3);
            if (  isChuvan(item) && isChusu(item1 ) && isChunhu(item2 ) && isChuy(item3 )){
                if(item.template.type == 0 || item.template.type == 1 || item.template.type == 3 || item.template.type == 27) {
                if (player.gender == 0 ) {
                    Item hopqua = ItemService.gI().createNewItem((short) Util.nextInt(1185, 1189), 1);
                    InventoryServiceNew.gI().addItemBag(player, hopqua);
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.inventory.gold -= 2000000000;
                    sendEffectCombineDB(player, item.template.iconID);
                    InventoryServiceNew.gI().removeItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                    player.diemsukientet +=1;
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
                else if (player.gender == 1 ) {
                    Item hopqua = ItemService.gI().createNewItem((short) Util.nextInt(1185, 1189), 1);
                    InventoryServiceNew.gI().addItemBag(player, hopqua);
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.inventory.gold -= 2000000000;
                    sendEffectCombineDB(player, item.template.iconID);
                    InventoryServiceNew.gI().removeItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                    player.diemsukientet +=1;
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
                else if (player.gender == 2 ) {
                    Item hopqua = ItemService.gI().createNewItem((short) Util.nextInt(1185, 1189), 1);
                    InventoryServiceNew.gI().addItemBag(player, hopqua);
                    InventoryServiceNew.gI().sendItemBags(player);
                    player.inventory.gold -= 2000000000;
                    sendEffectCombineDB(player, item.template.iconID);
                    InventoryServiceNew.gI().removeItemBag(player, item);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item2, 1);
                    InventoryServiceNew.gI().subQuantityItemsBag(player, item3, 1);
                    player.diemsukientet +=1;
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
                }
                }
        }
        }
    
private void psHoaTrangBi(Player player) {
    
           Item dabongtoi = InventoryServiceNew.gI().findItemBag(player, 1382);
            if (dabongtoi.quantity < 50) {
                Service.getInstance().sendThongBao(player, "Không đủ Đá Bóng Tối để thực hiện");
                return;
            }
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHacHoa()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị bóng tối");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1382).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá bóng tối");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < COST) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để bóng tối hóa trang bị!!!");
                return;
            }
            if (player.inventory.ruby < RUBY) {
                Service.getInstance().sendThongBao(player, "Con cần thêm hồng ngọc để bóng tối hóa trang bị!!!");
                return;
            }
            player.inventory.gold -= COST;
            player.inventory.ruby -= RUBY;
            Item daHacHoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1382).findFirst().get();
            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst().get();
            if (daHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu mắt thần");
                return;
            }
            if (trangBiHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị bóng tối");
                return;
            }

            if (trangBiHacHoa != null) {
                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 223) {
                        if (itopt.param >= 8) {
                            Service.getInstance().sendThongBao(player, "Trang bị đã đạt tới giới hạn bóng tối");
                            return;
                        }
                    }
                }
            }

            if (Util.isTrue(50, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionHacHoa = Arrays.asList(224, 220, 221, 222);
                int randomOption = idOptionHacHoa.get(Util.nextInt(0, 3));
                if (!trangBiHacHoa.haveOption(223)) {
                    trangBiHacHoa.itemOptions.add(new ItemOption(223, 1));
                } else {
                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == 223) {
                            itopt.param += 1;
                            break;
                        }
                    }
                }
                if (!trangBiHacHoa.haveOption(randomOption)) {
                    trangBiHacHoa.itemOptions.add(new ItemOption(randomOption, Util.nextInt(5,10)));
                } else {
                    for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                        if (itopt.optionTemplate.id == randomOption) {
                            itopt.param += Util.nextInt(5,10);
                            break;
                        }
                    }
                }

                Service.getInstance().sendThongBao(player, "Bạn đã bóng tối hóa thành công");
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, daHacHoa, 50);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

private void nangKichHoatVip(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = 100;
            Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
            if (thoivang.quantity < 20) {
                Service.getInstance().sendThongBao(player, "Không đủ thỏi vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            //Item item1 = player.combineNew.itemsCombine.get(1);
            //if (isTrangBiThuong(item1) && isTrangBiGod1(item)) {
            if ( isTrangBiHakai(item)) {
                
                
                int idItemKichHoatTDop[][] = {{ 136, 137, 138, 139, 230, 231, 232, 233  },
                        { 140, 141, 142, 143, 242, 243, 244, 245 },
                        { 144, 145, 146, 147, 254, 255, 256, 257 },
                        { 148, 149, 150, 151, 266, 267, 268, 269 },
                        { 184, 185, 186, 187, 278, 279, 280, 281 }};
                int idItemKichHoatXDop[][] = {{ 168, 169, 170, 171, 238, 239, 240, 241 },
                        { 172, 173, 174, 175, 250, 251, 252, 253 },
                        { 176, 177, 178, 179, 262, 263, 264, 265 },
                        { 180, 181, 182, 183, 274, 275, 276, 277 },
                        { 184, 185, 186, 187, 278, 279, 280, 281 }};
                int idItemKichHoatNMop[][] = {{ 152, 153, 154, 155, 234, 235, 236, 237 },
                        { 156, 157, 158, 159, 246, 247, 248, 249 },
                        { 160, 161, 162, 163, 258, 259, 260, 261 },
                        { 164, 165, 166, 167, 270, 271, 272, 273 },
                        { 184, 185, 186, 187, 278, 279, 280, 281 }};

                int optionItemKichHoat[][] = {
                    {127, 128, 129, 213, 215, 139, 140, 141, 214, 216},
                    {130, 131, 132, 213, 217, 142, 143, 144, 214, 218}, 
                    {133, 134, 135, 213, 219, 136, 137, 138, 214, 220}};

                //int  type = item1.template.type;
                //int   gender = item1.template.gender;
                int type = item.template.type;
                int gender = item.template.gender;
                int random = Util.nextInt(0, 2);
                if (gender == 3) {
                //if (player.pet != null) {
                    //if (Util.nextInt(0, 2) < 1) {
                       // gender = player.pet.gender;
                    //} else {
                        //gender = player.gender;
                    //}
                //} else {
                    gender = player.gender;
                //}
                }
                int option1 = optionItemKichHoat[gender][random];
                int option2 = optionItemKichHoat[gender][random + 5];
                Item itemKichHoat = null;
                if (gender == 0) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatTDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    //itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatTDop[type][Util.nextInt(0, 11)]));
                    //ao{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233},
                    if ( itemKichHoat.template.id == 0 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 2));
                    }
                    if ( itemKichHoat.template.id == 33){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 4));
                    }
                    if ( itemKichHoat.template.id == 3 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47,8));
                    }
                    if ( itemKichHoat.template.id == 34){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 16));
                    }
                    if ( itemKichHoat.template.id == 136){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 24));
                    }
                    if ( itemKichHoat.template.id == 137){
                        itemKichHoat.itemOptions.add(new ItemOption(47,40));
                    }
                    if ( itemKichHoat.template.id ==  138){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 50));
                    }
                    if ( itemKichHoat.template.id == 139){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 90));
                    }
                    if ( itemKichHoat.template.id ==230){
                        itemKichHoat.itemOptions.add(new ItemOption(47,200));
                    }
                    if ( itemKichHoat.template.id ==231 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 250));
                    }
                    if ( itemKichHoat.template.id ==232 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 300));
                    }
                    if ( itemKichHoat.template.id == 233){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 400));
                    }
                    //quan{6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245},
                    if ( itemKichHoat.template.id ==  6){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 20));
                    }
                    if ( itemKichHoat.template.id == 35){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 150));
                    }
                    if ( itemKichHoat.template.id == 9){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 300));
                    }
                    if ( itemKichHoat.template.id == 36){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 600));
                    }
                    if ( itemKichHoat.template.id == 140){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 1400));
                    }
                    if ( itemKichHoat.template.id == 141){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 3000));
                    }
                    if ( itemKichHoat.template.id ==  142){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 6000));
                    }
                    if ( itemKichHoat.template.id == 143){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 10000));
                    }
                    if ( itemKichHoat.template.id ==242){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 14000));
                    }
                    if ( itemKichHoat.template.id == 243){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 18000));
                    }
                    if ( itemKichHoat.template.id == 244){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 22000));
                    }
                    if ( itemKichHoat.template.id == 245){
                        itemKichHoat.itemOptions.add(new ItemOption(6,26000 ));
                    }
                    //gang{21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257},
                    if ( itemKichHoat.template.id ==  21){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 4));
                    }
                    if ( itemKichHoat.template.id == 24){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 7));
                    }
                    if ( itemKichHoat.template.id ==37){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 14));
                    }
                    if ( itemKichHoat.template.id == 38){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 28));
                    }
                    if ( itemKichHoat.template.id == 144){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 55));
                    }
                    if ( itemKichHoat.template.id == 145){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 110));
                    }
                    if ( itemKichHoat.template.id == 146 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 220));
                    }
                    if ( itemKichHoat.template.id ==147 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 530));
                    }
                    if ( itemKichHoat.template.id ==254){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 680));
                    }
                    if ( itemKichHoat.template.id == 255){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 1000));
                    }
                    if ( itemKichHoat.template.id == 256){
                        itemKichHoat.itemOptions.add(new ItemOption(0,1500) );
                    }
                    if ( itemKichHoat.template.id == 257){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2200 ));
                    }
                    //giay{27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269},
                    if ( itemKichHoat.template.id ==  27){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 10));
                    }
                    if ( itemKichHoat.template.id == 30){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25));
                    }
                    if ( itemKichHoat.template.id ==39){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 120));
                    }
                    if ( itemKichHoat.template.id == 40){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 250));
                    }
                    if ( itemKichHoat.template.id == 148){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 500));
                    }
                    if ( itemKichHoat.template.id == 149){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 1200));
                    }
                    if ( itemKichHoat.template.id ==  150){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 2400));
                    }
                    if ( itemKichHoat.template.id == 151){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 5000));
                    }
                    if ( itemKichHoat.template.id ==266){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 9000));
                    }
                    if ( itemKichHoat.template.id == 267){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 14000));
                    }
                    if ( itemKichHoat.template.id == 268){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 19000));
                    }
                    if ( itemKichHoat.template.id == 269){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 24000));
                    }
                    //nhan{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                    if ( itemKichHoat.template.id == 12 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 1));
                    }
                    if ( itemKichHoat.template.id == 57){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 2));
                    }
                    if ( itemKichHoat.template.id ==58){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 3));
                    }
                    if ( itemKichHoat.template.id == 59){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 4));
                    }
                    if ( itemKichHoat.template.id == 184){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 5));
                    }
                    if ( itemKichHoat.template.id == 185){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 6));
                    }
                    if ( itemKichHoat.template.id == 186 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 7));
                    }
                    if ( itemKichHoat.template.id == 187){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 8));
                    }
                    if ( itemKichHoat.template.id ==278){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 9));
                    }
                    if ( itemKichHoat.template.id == 279){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 10));
                    }
                    if ( itemKichHoat.template.id == 280){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 11));
                    }
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                }
                if (gender == 1) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatNMop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    //itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatNMop[type][Util.nextInt(0, 11)]));
                    //ao{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237},
                    if ( itemKichHoat.template.id == 1 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 2));
                    }
                    if ( itemKichHoat.template.id == 41){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 4));
                    }
                    if ( itemKichHoat.template.id == 4 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47,8));
                    }
                    if ( itemKichHoat.template.id == 42){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 16));
                    }
                    if ( itemKichHoat.template.id == 152){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 24));
                    }
                    if ( itemKichHoat.template.id == 153){
                        itemKichHoat.itemOptions.add(new ItemOption(47,40));
                    }
                    if ( itemKichHoat.template.id ==  154){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 50));
                    }
                    if ( itemKichHoat.template.id == 155){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 90));
                    }
                    if ( itemKichHoat.template.id ==234){
                        itemKichHoat.itemOptions.add(new ItemOption(47,200));
                    }
                    if ( itemKichHoat.template.id ==235 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 250));
                    }
                    if ( itemKichHoat.template.id ==236 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 300));
                    }
                    if ( itemKichHoat.template.id == 237){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 400));
                    }
                    //quan{7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249},
                    if ( itemKichHoat.template.id ==  7){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 25));
                    }
                    if ( itemKichHoat.template.id == 43){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 50));
                    }
                    if ( itemKichHoat.template.id == 10){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 100));
                    }
                    if ( itemKichHoat.template.id == 44){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 250));
                    }
                    if ( itemKichHoat.template.id == 156){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 600));
                    }
                    if ( itemKichHoat.template.id == 157){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 1200));
                    }
                    if ( itemKichHoat.template.id ==  158){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 2400));
                    }
                    if ( itemKichHoat.template.id == 159){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 4800));
                    }
                    if ( itemKichHoat.template.id ==246){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 13000));
                    }
                    if ( itemKichHoat.template.id == 247){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 17000));
                    }
                    if ( itemKichHoat.template.id == 248){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 21000));
                    }
                    if ( itemKichHoat.template.id == 249){
                        itemKichHoat.itemOptions.add(new ItemOption(6,25000 ));
                    }
                    //gang{22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261},
                    if ( itemKichHoat.template.id ==  22){
                        itemKichHoat.itemOptions.add(new ItemOption(0,3));
                    }
                    if ( itemKichHoat.template.id == 46){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 6));
                    }
                    if ( itemKichHoat.template.id ==25){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 12));
                    }
                    if ( itemKichHoat.template.id == 45){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 24));
                    }
                    if ( itemKichHoat.template.id == 160){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 50));
                    }
                    if ( itemKichHoat.template.id == 161){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 100));
                    }
                    if ( itemKichHoat.template.id == 162 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 200));
                    }
                    if ( itemKichHoat.template.id ==163 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 500));
                    }
                    if ( itemKichHoat.template.id ==258){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 630));
                    }
                    if ( itemKichHoat.template.id == 259){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 950));
                    }
                    if ( itemKichHoat.template.id == 260){
                        itemKichHoat.itemOptions.add(new ItemOption(0,1450) );
                    }
                    if ( itemKichHoat.template.id == 261){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2150 ));
                    }
                    //giay{28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273},
                    if ( itemKichHoat.template.id ==  28){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 15));
                    }
                    if ( itemKichHoat.template.id == 47){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 30));
                    }
                    if ( itemKichHoat.template.id ==31){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 150));
                    }
                    if ( itemKichHoat.template.id == 48){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 300));
                    }
                    if ( itemKichHoat.template.id == 164){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 600));
                    }
                    if ( itemKichHoat.template.id == 165){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 1500));
                    }
                    if ( itemKichHoat.template.id ==  166){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 3000));
                    }
                    if ( itemKichHoat.template.id == 167){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 6000));
                    }
                    if ( itemKichHoat.template.id ==270){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 10000));
                    }
                    if ( itemKichHoat.template.id == 271){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 15000));
                    }
                    if ( itemKichHoat.template.id == 272){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 20000));
                    }
                    if ( itemKichHoat.template.id == 273){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25000));
                    }
                    //nhan{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                    if ( itemKichHoat.template.id == 12 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 1));
                    }
                    if ( itemKichHoat.template.id == 57){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 2));
                    }
                    if ( itemKichHoat.template.id ==58){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 3));
                    }
                    if ( itemKichHoat.template.id == 59){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 4));
                    }
                    if ( itemKichHoat.template.id == 184){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 5));
                    }
                    if ( itemKichHoat.template.id == 185){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 6));
                    }
                    if ( itemKichHoat.template.id == 186 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 7));
                    }
                    if ( itemKichHoat.template.id == 187){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 8));
                    }
                    if ( itemKichHoat.template.id ==278){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 9));
                    }
                    if ( itemKichHoat.template.id == 279){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 10));
                    }
                    if ( itemKichHoat.template.id == 280){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 11));
                    }
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                }
                if (gender == 2) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatXDop[type][Util.nextInt(0, 1)]);
                    itemKichHoat = new Item(_item);
                    //itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatXDop[type][Util.nextInt(0, 11)]));
                    //ao{{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241},
                    if ( itemKichHoat.template.id == 2 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 5));
                    }
                    if ( itemKichHoat.template.id == 49){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 5));
                    }
                    if ( itemKichHoat.template.id == 5 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47,10));
                    }
                    if ( itemKichHoat.template.id == 50){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 20));
                    }
                    if ( itemKichHoat.template.id == 168){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 30));
                    }
                    if ( itemKichHoat.template.id == 169){
                        itemKichHoat.itemOptions.add(new ItemOption(47,50));
                    }
                    if ( itemKichHoat.template.id ==  170){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 70));
                    }
                    if ( itemKichHoat.template.id == 171){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 100));
                    }
                    if ( itemKichHoat.template.id ==238){
                        itemKichHoat.itemOptions.add(new ItemOption(47,230));
                    }
                    if ( itemKichHoat.template.id ==239){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 280));
                    }
                    if ( itemKichHoat.template.id ==240 ){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 330));
                    }
                    if ( itemKichHoat.template.id == 241){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 450));
                    }
                    //quan{8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253},
                    if ( itemKichHoat.template.id ==  8){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 25));
                    }
                    if ( itemKichHoat.template.id == 51){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 50));
                    }
                    if ( itemKichHoat.template.id == 11){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 100));
                    }
                    if ( itemKichHoat.template.id == 52){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 200));
                    }
                    if ( itemKichHoat.template.id == 172){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 500));
                    }
                    if ( itemKichHoat.template.id == 173){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 1000));
                    }
                    if ( itemKichHoat.template.id ==  174){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 2000));
                    }
                    if ( itemKichHoat.template.id == 175){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 4000));
                    }
                    if ( itemKichHoat.template.id ==250){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 12000));
                    }
                    if ( itemKichHoat.template.id == 251){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 16000));
                    }
                    if ( itemKichHoat.template.id == 252){
                        itemKichHoat.itemOptions.add(new ItemOption(6, 20000));
                    }
                    if ( itemKichHoat.template.id == 253){
                        itemKichHoat.itemOptions.add(new ItemOption(6,24000 ));
                    }
                    //gnag23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265},
                    if ( itemKichHoat.template.id ==  23){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 5));
                    }
                    if ( itemKichHoat.template.id == 53){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 8));
                    }
                    if ( itemKichHoat.template.id ==26){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 16));
                    }
                    if ( itemKichHoat.template.id == 54){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 32));
                    }
                    if ( itemKichHoat.template.id == 176){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 60));
                    }
                    if ( itemKichHoat.template.id == 177){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 120));
                    }
                    if ( itemKichHoat.template.id == 178 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 240));
                    }
                    if ( itemKichHoat.template.id ==179 ){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 560));
                    }
                    if ( itemKichHoat.template.id ==262){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 700));
                    }
                    if ( itemKichHoat.template.id == 263){
                        itemKichHoat.itemOptions.add(new ItemOption(0, 1050));
                    }
                    if ( itemKichHoat.template.id == 264){
                        itemKichHoat.itemOptions.add(new ItemOption(0,1550) );
                    }
                    if ( itemKichHoat.template.id == 265){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2250 ));
                    }
                    //giay29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277,
                    if ( itemKichHoat.template.id ==  29){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 10));
                    }
                    if ( itemKichHoat.template.id == 55){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25));
                    }
                    if ( itemKichHoat.template.id ==32){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 120));
                    }
                    if ( itemKichHoat.template.id == 56){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 250));
                    }
                    if ( itemKichHoat.template.id == 180){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 500));
                    }
                    if ( itemKichHoat.template.id == 181){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 1200));
                    }
                    if ( itemKichHoat.template.id ==  182){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 2400));
                    }
                    if ( itemKichHoat.template.id == 183){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 5000));
                    }
                    if ( itemKichHoat.template.id ==274){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 9000));
                    }
                    if ( itemKichHoat.template.id == 275){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 14000));
                    }
                    if ( itemKichHoat.template.id == 276){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 19000));
                    }
                    if ( itemKichHoat.template.id == 277){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 24000));
                    }
                    //nhan{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                    if ( itemKichHoat.template.id == 12 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 1));
                    }
                    if ( itemKichHoat.template.id == 57){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 2));
                    }
                    if ( itemKichHoat.template.id ==58){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 3));
                    }
                    if ( itemKichHoat.template.id == 59){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 4));
                    }
                    if ( itemKichHoat.template.id == 184){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 5));
                    }
                    if ( itemKichHoat.template.id == 185){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 6));
                    }
                    if ( itemKichHoat.template.id == 186 ){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 7));
                    }
                    if ( itemKichHoat.template.id == 187){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 8));
                    }
                    if ( itemKichHoat.template.id ==278){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 9));
                    }
                    if ( itemKichHoat.template.id == 279){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 10));
                    }
                    if ( itemKichHoat.template.id == 280){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 11));
                    }
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                }
                

                itemKichHoat.itemOptions.add(new ItemOption(option1, 0));
                itemKichHoat.itemOptions.add(new ItemOption(option2, 0));
                itemKichHoat.itemOptions.add(new ItemOption(30, 0));

                InventoryServiceNew.gI().addItemBag(player, itemKichHoat);
               
                sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 20);

                //InventoryServiceNew.gI().removeItemBag(player, item1);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
    private void nangKichHoatVip2(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.ruby < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
                    Item item1 = player.combineNew.itemsCombine.get(1);
                    Item item2 = player.combineNew.itemsCombine.get(2);
                    Item item3 = player.combineNew.itemsCombine.get(3);
                    Item item4 = player.combineNew.itemsCombine.get(4);
                    
                if (isTrangBiHakai(item) && isTrangBiHakai(item1) && isTrangBiHakai(item2) && isTrangBiHakai(item3) && isTrangBiHakai(item4) ) {
            //if (isTrangBiThuong(item1) && isTrangBiGod1(item)) {
            //if ( isTrangBiGod1(item))   
                    int idItemKichHoatTDop[][] = {{233,233,555},
                        {245,245,556},
                        {257,257,562},
                        {269,269,563},
                        {281,281,561}};
                    int idItemKichHoatNMop[][] = {{237,237,559},
                        {249,249,560},
                        {261,261,566},
                        {273,273,567},
                        {281,281,561}};
                    int idItemKichHoatXDop[][] = {{241,241,557},
                        {253,253,558},
                        {265,265,564},
                        {277,277,565},
                        {281,281,561}};
                    int optionItemKichHoat[][] = {{127, 128, 129, 139, 140, 141}, {130, 131, 132, 142, 143, 144}, {133, 134, 135, 136, 137, 138}};

                //int  type = item1.template.type;
                //int   gender = item1.template.gender;
                    

                //int  type = item1.template.type;
                //int   gender = item1.template.gender;
                int type = item.template.type;
                int gender = item.template.gender;
                int random = Util.nextInt(0, 2);
                if (gender == 3) {
                //if (player.pet != null) {
                    //if (Util.nextInt(0, 2) < 1) {
                       // gender = player.pet.gender;
                    //} else {
                        //gender = player.gender;
                    //}
                //} else {
                    gender = player.gender;
                //}
                }
                int option1 = optionItemKichHoat[gender][random];
                int option2 = optionItemKichHoat[gender][random + 3];
                Item itemKichHoat = null;
                
                if (gender == 0) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatTDop[type][Util.nextInt(0, 2)]);
                    itemKichHoat = new Item(_item);
                    //itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatTDop[type][Util.nextInt(0, 11)]));
                    //ao{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233},
                    
                    if ( itemKichHoat.template.id == 233){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 400));
                    }
                    if ( itemKichHoat.template.id == 555){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(47,(Util.nextInt(50,100 ))*10));
                    }
                    //quan{6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245},
                    
                    if ( itemKichHoat.template.id == 245){
                        itemKichHoat.itemOptions.add(new ItemOption(6,26000 ));
                    }
                    if ( itemKichHoat.template.id == 556){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(6,(Util.nextInt(350,601 ))*100)); 
                    }
                    //gang{21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257},
                    
                    if ( itemKichHoat.template.id == 257){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2200 ));
                    }
                    if ( itemKichHoat.template.id == 562){
                       itemKichHoat.itemOptions.add(new Item.ItemOption(0,(Util.nextInt(30,60 ))*100)); 
                    }
                    //giay{27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269},
                    
                    if ( itemKichHoat.template.id == 269){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 24000));
                    }
                    if ( itemKichHoat.template.id == 563){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(7,(Util.nextInt(350,601 ))*100)); 
                    }
                    //nhan{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                    if ( itemKichHoat.template.id == 561){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14,17 ))); 
                    }
                }
                if (gender == 1) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatNMop[type][Util.nextInt(0, 2)]);
                    itemKichHoat = new Item(_item);
                    //itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatNMop[type][Util.nextInt(0, 11)]));
                    //ao{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237},
                    
                    if ( itemKichHoat.template.id == 237){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 400));
                    }
                    if ( itemKichHoat.template.id == 557){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(47,(Util.nextInt(50,100 ))*10));
                    }
                    //quan{7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249},
                    
                    if ( itemKichHoat.template.id == 249){
                        itemKichHoat.itemOptions.add(new ItemOption(6,25000 ));
                    }
                    if ( itemKichHoat.template.id == 558){
                       itemKichHoat.itemOptions.add(new Item.ItemOption(6,(Util.nextInt(350,601 ))*100)); 
                    }
                    //gang{22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261},
                    
                    if ( itemKichHoat.template.id == 261){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2150 ));
                    }
                    if ( itemKichHoat.template.id == 564){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(0,(Util.nextInt(30,60 ))*100)); 
                    }
                    //giay{28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273},
                    
                    if ( itemKichHoat.template.id == 273){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 25000));
                    }
                    if ( itemKichHoat.template.id == 565){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(7,(Util.nextInt(350,601 ))*100)); 
                    }
                    //nhan{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                    
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                    if ( itemKichHoat.template.id == 561){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14,17 ))); 
                    }
                }
                if (gender == 2) {
                    Item _item = ItemService.gI().createNewItem((short) idItemKichHoatXDop[type][Util.nextInt(0, 2)]);
                    itemKichHoat = new Item(_item);
                    //itemKichHoat.itemOptions.addAll(ItemService.gI().getListOptionItemShop((short) idItemKichHoatXDop[type][Util.nextInt(0, 11)]));
                    //ao{{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241},
                    
                    if ( itemKichHoat.template.id == 241){
                        itemKichHoat.itemOptions.add(new ItemOption(47, 450));
                    }
                    if ( itemKichHoat.template.id == 559){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(47,(Util.nextInt(50,100 ))*10));
                    }
                    //quan{8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253},
                    
                    if ( itemKichHoat.template.id == 253){
                        itemKichHoat.itemOptions.add(new ItemOption(6,24000 ));
                    }
                    if ( itemKichHoat.template.id == 560){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(6,(Util.nextInt(350,601 ))*100)); 
                    }
                    //gnag23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265},
                    
                    if ( itemKichHoat.template.id == 265){
                        itemKichHoat.itemOptions.add(new ItemOption(0,2250 ));
                    }
                    if ( itemKichHoat.template.id == 566){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(0,(Util.nextInt(30,60 ))*100)); 
                    }
                    //giay{27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269},
                    
                    if ( itemKichHoat.template.id == 269){
                        itemKichHoat.itemOptions.add(new ItemOption(7, 24000));
                    }
                    if ( itemKichHoat.template.id == 567){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(7,(Util.nextInt(350,601 ))*100)); 
                    }
                    //nhan{12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281}};
                    
                    if ( itemKichHoat.template.id == 281){
                        itemKichHoat.itemOptions.add(new ItemOption(14, 12));
                    }
                    if ( itemKichHoat.template.id == 561){
                        itemKichHoat.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14,17 ))); 
                    }
                }

                itemKichHoat.itemOptions.add(new ItemOption(option1, 0));
                itemKichHoat.itemOptions.add(new ItemOption(option2, 0));
                itemKichHoat.itemOptions.add(new ItemOption(30, 2));

                InventoryServiceNew.gI().addItemBag(player, itemKichHoat);
                player.inventory.ruby -= 100000;
                sendEffectCombineDB(player, item.template.iconID);
                InventoryServiceNew.gI().removeItemBag(player, item);
                InventoryServiceNew.gI().removeItemBag(player, item1);
                InventoryServiceNew.gI().removeItemBag(player, item2);
                InventoryServiceNew.gI().removeItemBag(player, item3);
                InventoryServiceNew.gI().removeItemBag(player, item4);
                //InventoryServiceNew.gI().subQuantityItemsBag(player, item1, 1);

                //InventoryServiceNew.gI().removeItemBag(player, item1);
                InventoryServiceNew.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void tayHacHoaTrangBi(Player player) {

        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHacHoa()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị bóng tối");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1383).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đá ánh sáng");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 0) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= 0;
            Item buagiaihachoa = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1383).findFirst().get();
            Item trangBiHacHoa = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHacHoa).findFirst().get();
            if (buagiaihachoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu đá ánh sáng giải trang bị");
                return;
            }
            if (trangBiHacHoa == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị bóng tối");
                return;
            }

            if (Util.isTrue(100, 100)) {
                sendEffectSuccessCombine(player);
                List<Integer> idOptionHacHoa = Arrays.asList(224, 220, 221, 222);

                ItemOption option_223 = new ItemOption();
                ItemOption option_224 = new ItemOption();
                ItemOption option_220 = new ItemOption();
                ItemOption option_221 = new ItemOption();
                ItemOption option_222 = new ItemOption();

                for (ItemOption itopt : trangBiHacHoa.itemOptions) {
                    if (itopt.optionTemplate.id == 223) {
                        System.out.println("223");
                        option_223 = itopt;
                    }
                    if (itopt.optionTemplate.id == 224) {
                        System.out.println("224");
                        option_224 = itopt;
                    }
                    if (itopt.optionTemplate.id == 220) {
                        System.out.println("220");
                        option_220 = itopt;
                    }
                    if (itopt.optionTemplate.id == 221) {
                        System.out.println("221");
                        option_221 = itopt;
                    }
                    if (itopt.optionTemplate.id == 222) {
                        System.out.println("222");
                        option_222 = itopt;
                    }
                }
                if (option_223 != null) {
                    trangBiHacHoa.itemOptions.remove(option_223);
                }
                if (option_224 != null) {
                    trangBiHacHoa.itemOptions.remove(option_224);
                }
                if (option_220 != null) {
                    trangBiHacHoa.itemOptions.remove(option_220);
                }
                if (option_221 != null) {
                    trangBiHacHoa.itemOptions.remove(option_221);
                }
                if (option_222 != null) {
                    trangBiHacHoa.itemOptions.remove(option_222);
                }
                Service.getInstance().sendThongBao(player, "Bạn đã tẩy thành công");
                InventoryServiceNew.gI().sendItemBags(player);
            } else {
                sendEffectFailCombine(player);
            }
            InventoryServiceNew.gI().subQuantityItemsBag(player, buagiaihachoa, 1);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null && (item.template.type == 30 || (item.template.id >= 14 && item.template.id <= 20));
    }
    private boolean isTrangBiAn(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 1048 && item.template.id <= 1062) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type < 5 || item.template.type == 32) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5;
            case 19:
                return 5;
            case 18:
                return 5;
            case 17:
                return 5;
            case 16:
                return 3;
            case 15:
                return 2;
            case 14:
                return 5;
            case 441:
                return 5;
            case 442:
                return 5;
            case 443:
                return 5;
            case 444:
                return 5;
            case 445:
                return 5;
            case 446:
                return 5;
            case 447:
                return 5;
            case 964:
                return 10;
            case 965:
                return 10;
            default:
                return -1;
        }
    }
    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77; // hp
            case 19:
                return 103; // ki
            case 18:
                return 80; // hp 30s
            case 17:
                return 81; // mp 30s
            case 16:
                return 50; // sức đánh
            case 15:
                return 94; // giáp %
            case 14:
                return 108; // né đòn
            case 441:
                return 95; // hút hp
            case 442:
                return 96; // hút ki
            case 443:
                return 97; // phả sát thương
            case 444:
                return 98; // xuyên giáp chưởng
            case 445:
                return 99; // xuyên giáp đấm
            case 446:
                return 100; // vàng rơi từ quái
            case 447:
                return 101; // tấn công % khi đánh quái19
            case 964:
                return 14; // chí mạng
            case 965: 
                return 50; // sức đánh
            default:
                return -1;
        }
    }


   

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case NANG_NGOC_BOI:
                return "Cậu có muốn nâng ngọc bội? \n hãy để ta mở sức mạnh ngọc bội của cậu\n trở thành cấp cao hơn";     
            case NANG_CAP_CHAN_MENH:
                return "Cậu có muốn khai mở chân mệnh? \n hãy để ta khai mở sức mạnh chân mệnh của cậu\n trở thành chân mệnh cấp cao hơn";     
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
                case AN_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Ấn";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho 7 viên Ngọc Rồng\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 2";
            case NANG_CAP_BONG_TAI_CAP3:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 3";
            case NANG_CAP_BONG_TAI_CAP4:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 4";
            case NANG_CAP_BONG_TAI_CAP5:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành cấp 5";    
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 5 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case DOI_HOP_QUA:
                return "Ta sẽ chúc phúc\ncho ngươi\nđổi ra hộp quà may mắn";    
            case REN_KIEM_Z:
                return "Ta sẽ rèn\ncho con thanh\nKiếm Z này";
            case PHAN_RA_TRANG_BI1:
                return "Ta sẽ phân giải\ncho trang bị thần linh của ngươi\ntrở thành điểm luyện hóa";
                case PHAN_RA_TRANG_BI2:
                return "Ta sẽ phân giải\ncho trang bị hủy diệt của ngươi\ntrở thành điểm luyện hóa";
                case PHAN_RA_TRANG_BI3:    
            case NANG_CAP_DO_KICH_HOAT:
                return "Ta sẽ giúp ngươi\n làm điều đó";
            case NANG_CAP_SKH_VIP:
                return "Thần linh nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
                case NANG_CAP_SKH_VIPhd:
                return "Hủy diệt nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
                case NANG_CAP_SKH_VIPts:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case CHE_TAO_TRANG_BI_TS:
                return "Chế tạo\ntrang bị thiên sứ";
                case CHE_TAO_TRANG_BI_TL:
                return "Chế tạo\ntrang bị thần linh";
                case CHE_TAO_TRANG_BI_HD:
                return "Chế tạo\ntrang bị hủy diệt";
            case kh_T:
                return "Ta sẽ giúp ngươi\n làm điều đó";
            case kh_Tl:
                return "Ta sẽ giúp ngươi\n làm điều đó"; 
                case kh_Hd:
                return "Ta sẽ giúp ngươi\n làm điều đó"; 
                case kh_Ts:
                return "Ta sẽ giúp ngươi\n làm điều đó"; 
                case PS_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\nthành trang bị bóng tối";
            case TAY_PS_HOA_TRANG_BI:
                return "Ta sẽ giúp ngươi\ntẩy trang bị\nbóng tối";
                case GIA_HAN_VAT_PHAM:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\nthêm hạn sử dụng";
                case THANG_HOA_NGOC_BOI:
                return "Ta sẽ phù phép\ncho ngọc bội của ngươi\nthăng hoa";
            case KHAM_DA_HP:
                return "Ta sẽ khảm đá\ncho trang bị của ngươi\nchắc ngươi thích lắm";
            case KHAM_DA_MP:
                return "Ta sẽ khảm đá\ncho trang bị của ngươi\nchắc ngươi thích lắm";
            case KHAM_DA_DAME:
                return "Ta sẽ khảm đá\ncho trang bị của ngươi\nchắc ngươi thích lắm";  
            case NANG_CAP_PET:
                return "Ta sẽ phù phép\ncho linh thú đặc quyền của ngươi\nCó những chỉ số đẹp";
            case NANG_CAP_PET2:
                return "Ta sẽ phù phép\ncho linh thú của ngươi\nCó những chỉ số đẹp";
                case NANG_CAP_PK:
                return "Ta sẽ phù phép\ncho Phụ kiện của ngươi\nCó những chỉ số đẹp";    
                case CUONG_HOA:
                return "Những Trang bị\nđẹp mắt sẽ có sau khi ngươi ấn nâng cấp";
                case NANG_KICH_HOAT_VIP:
                return "Ta sẽ phù phéo\ncho trang bị của ngươi\ntrở thành trang bị cực phẩm";
                case NANG_KICH_HOAT_VIP2:
                return "Ta sẽ phù phéo\ncho trang bị của ngươi\ntrở thành trang bị cực phẩm";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\nChọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_NGOC_BOI:
                return "vào hành trang\nChọn 1 ngọc bội bất kì\nvà 10 viên ngọc rồng 3 sao + x49 Thỏi Vàng + 1k HN"
                        + "Chỉ cần chọn 'Nâng Cấp'";     
            case NANG_CAP_CHAN_MENH:
                return "vào hành trang\nChọn 1 chân mệnh bất kì\nvà 5K XU + 50k HN\n Mỗi Lần Nâng Cấp Chân Mệnh Cộng 25% Max 225%(âng 9 Lần)"
                        + "Chỉ cần chọn 'Nâng Cấp'";     
                case AN_TRANG_BI:
                return "Vào hành trang\nChọn 1 Trang bị THIÊN SỨ và 99 mảnh Ấn\nSau đó chọn 'Làm phép'\n--------\nTinh ấn (5 món +30%HP)\n Nhật ấn (5 món +30%KI\n Nguyệt ấn (5 món +20%SD)";
           
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nSau đó chọn 'Nâng cấp'";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\nChọn 7 viên ngọc cùng sao\nSau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "vào hành trang\nChọn trang bị\n(Áo, quần, găng, giày hoặc rađa)\nChọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI: // thay tab
                return "Vào hành trang\nChọn bông tai Porata cấp 1\nChọn mảnh bông tai cấp 2 để nâng cấp, số lượng\n500 cái\nSau đó chọn 'Nâng cấp'";
                case NANG_CAP_BONG_TAI_CAP3: // thay tab
                return "Vào hành trang\nChọn bông tai Porata cấp 2\nChọn mảnh bông tai cấp 3 để nâng cấp, số lượng\n1000 cái\nSau đó chọn 'Nâng cấp'";
                case NANG_CAP_BONG_TAI_CAP4: // thay tab
                return "Vào hành trang\nChọn bông tai Porata cấp 3\nChọn mảnh bông tai cấp 4 để nâng cấp, số lượng\n3000 cái\nSau đó chọn 'Nâng cấp'";
                case NANG_CAP_BONG_TAI_CAP5: // thay tab
                return "Vào hành trang\nChọn bông tai Porata cấp 4\nChọn mảnh bông tai cấp 5 để nâng cấp, số lượng\n9999 cái\nSau đó chọn 'Nâng cấp'";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\nChọn bông tai Porata cấp 5\nChọn mảnh hồn bông tai số lượng 99 cái\nvà đá xanh lam để nâng cấp\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_DO_KICH_HOAT:
                return "vào hành trang\nChọn 2 trang bị Thần Linh bất kì\n "
                        + " và 500tr vàng!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "vào hành trang\nChọn 1 trang bị Thần Linh bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + "----Theo Thứ Tự Như Sau----\n"
                        + "----kích hoạt ->Thần linh ->kích hoạt--\n"
                        +"--Món Đầu Làm gốc--\n"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case PHAN_RA_TRANG_BI1:
                return "Ta sẽ phân giải\ncho trang bị thần linh của ngươi\ntrở thành 1-10 điểm luyện hóa";
                case PHAN_RA_TRANG_BI2:
                return "Ta sẽ phân giải\ncho trang bị hủy diệt của ngươi\ntrở thành 10-50 điểm luyện hóa";
                case PHAN_RA_TRANG_BI3:
                return "Ta sẽ phân giải\ncho trang bị thiên sứ của ngươi\ntrở thành 50-200 điểm luyện hóa";    
                case NANG_CAP_SKH_VIPhd:
                return "vào hành trang\nChọn 1 trang bị Hủy diệt bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + "----Theo Thứ Tự Như Sau----\n"
                        + "----kích hoạt ->Hủy diệt ->kích hoạt--\n"
                        +"--Món Đầu Làm gốc--\n"
                        + "Chỉ cần chọn 'Nâng Cấp'";
                case NANG_CAP_SKH_VIPts:
                return "vào hành trang\nChọn 1 trang bị Thiên sứ bất kì\nChọn tiếp ngẫu nhiên 2 món SKH thường \n "
                        + "----Theo Thứ Tự Như Sau----\n"
                        + "----kích hoạt ->Thiên sứ ->kích hoạt--\n"
                        +"--Món Đầu Làm gốc--\n"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case CHE_TAO_TRANG_BI_TS:
                return "Cần 1 công thức vip\nMảnh trang bị Thiên sứ\n"
                        + "Số Lượng\n99"
                        + "Có thể thêm\nĐá nâng cấp (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Đá may mắn (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";
                case CHE_TAO_TRANG_BI_TL:
                return "Cần 1 công thức vip\nMảnh trang bị thần linh\n"
                        + "Số Lượng\n99"
                        + "Có thể thêm\nĐá nâng cấp (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Đá may mắn (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case CHE_TAO_TRANG_BI_HD:
                return "Cần 1 công thức vip\nMảnh trang bị hủy diệt\n"
                        + "Số Lượng\n99"
                        + "Có thể thêm\nĐá nâng cấp (tùy chọn) để tăng tỉ lệ chế tạo\n"
                        + "Đá may mắn (tùy chọn) để tăng tỉ lệ các chỉ số cơ bản và chỉ số ẩn\n"
                        + "Sau đó chọn 'Nâng cấp'";    
            case REN_KIEM_Z:
                return "VChọn Kiếm Z\nChọn Quặng Z, số lượng\n99 cái\nSau đó chọn 'Rèn Kiếm'\n Ngẫu nhiên Kiếm Z cấp 1 đến cấp 16"; 
                
            case kh_T:
                return "Vào hành trang\nChọn trang bị HỦY DIỆT để hiến tế\n"
                        + "-------------\n "
                        + "Sau khi hiến tế bạn sẽ nhận được trang bị THIÊN SỨ\n"
                        + "Chỉ cần chọn 'Hiến tế'"; 
            case kh_Tl:
                return "vào hành trang\nChọn 2 trang bị Thần Linh bất kì\n "
                        + " và 500tr vàng!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
            case kh_Hd:
                return "Vào hành trang\nChọn trang bị THẦN LINH để hiến tế\n"
                        + "-------------\n "
                        + "Sau khi hiến tế bạn sẽ nhận được trang bị HỦY DIỆT\n"
                        + "Chỉ cần chọn 'Hiến tế'"; 
            case kh_Ts:
                return "vào hành trang\nChọn 2 trang bị Thiên Sứ bất kì\n "
                        + " và 500tr vàng!"
                        + "Chỉ cần chọn 'Nâng Cấp'";
                case PS_HOA_TRANG_BI:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị\n"
                        + "Cần 50 Đá Bóng Tối\n"
                        + "(Cải trang, Pet hoặc Phụ kiện bang)\n"
                        + "Chọn loại đá bóng tối phù hợp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case TAY_PS_HOA_TRANG_BI:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị\n"
                        + "(Cải trang, Pet hoặc Phụ kiện bang)\n"
                        + "Chọn loại đá ánh sáng\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GIA_HAN_VAT_PHAM:
                return "vào hành trang\n"
                        + "Chọn 1 trang bị có hạn sử dụng\n"
                        + "Chọn Sách gia hạn\n"
                        + "Sau đó chọn 'Gia hạn'"; 
                case THANG_HOA_NGOC_BOI:
                return "vào hành trang\n"
                        + "Chọn 1 vật phẩm ngọc bội\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case DOI_HOP_QUA:
                return "Chọn\n 4 câu đối\n"
                        + "Theo thứ tự như sau:\n"
                        + "Vạn-Sự-Như-Ý\n"
                        + "Sau đó chọn 'Nâng cấp'";    
            case KHAM_DA_HP:
                return "Chọn Huy hiệu cho vào ô 1\nSau đó chọn 'Nâng cấp'";
            case KHAM_DA_MP:
                return "Chọn Huy hiệu cho vào ô 1\nSau đó chọn 'Nâng cấp'";
            case KHAM_DA_DAME:
                return "Chọn Huy hiệu cho vào ô 1\nSau đó chọn 'Nâng cấp'";
            case NANG_CAP_PET2:
                return "Vào hành trang\n"
                        + "Chọn LINH THÚ và trang bị THẦN LINH\n"
                              + "Sau đó chọn 'Nâng cấp'\n"
                         + "Ngươi sẽ nhận được điều bất ngờ!!\n"
                             + "Chúc nhà ngươi may mắn.";
                case NANG_CAP_PK:
                return "Vào hành trang\n"
                        + "Chọn PHỤ KIỆN và trang bị THẦN LINH\n"
                              + "Sau đó chọn 'Nâng cấp'\n"
                         + "Ngươi sẽ nhận được điều bất ngờ!!\n"
                             + "Chúc nhà ngươi may mắn.";
                case CUONG_HOA:
                return "Vào hành trang chọn trang bị kích hoạt\n"
                        + "-------Lưu ý-------\n"
                        + "cần 50 viên Đá ngọc bảo trong hành trang\n"
                        + "Sau đó chọn 'Nâng cấp'";
                case NANG_KICH_HOAT_VIP:
                return "Chọn trang bị Hủy Diệt cho vào ô 1\nSau đó chọn 'Nâng cấp'\n";
            case NANG_KICH_HOAT_VIP2:
                return "Chọn 5 trang bị Hủy Diệt cho vào 5 ô\nHãy xếp theo thứ tự\nSau đó chọn 'Nâng cấp'\nMay mắn nhận được cực phẩm";
            default:
                return "";
        }
    }

}
