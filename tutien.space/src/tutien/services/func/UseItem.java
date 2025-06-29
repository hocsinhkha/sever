package tutien.services.func;

import tutien.card.Card;
import tutien.card.RadarCard;
import tutien.card.RadarService;
import barcoll.consts.ConstMap;
import tutien.models.item.Item;
import barcoll.consts.ConstNpc;
import barcoll.consts.ConstPlayer;
import tutien.models.item.Item.ItemOption;
import barcoll.models.map.Zone;
import Amodels.boss.BossManager;
import tutien.models.player.Inventory;
import barcoll.services.NpcService;
import tutien.models.player.Player;
import tutien.models.skill.Skill;
import com.girlkun.network.io.Message;
import tutien.utils.SkillUtil;
import barcoll.services.Service;
import tutien.utils.Util;
import tutien.server.io.MySession;
import barcoll.services.EffectSkillService;
import barcoll.services.ItemService;
import barcoll.services.ItemTimeService;
import barcoll.services.PetService;
import barcoll.services.PlayerService;
import barcoll.services.TaskService;
import barcoll.services.InventoryServiceNew;
import barcoll.services.MapService;
import barcoll.services.NgocRongNamecService;
import barcoll.services.RewardService;
import barcoll.services.SkillService;
import tutien.utils.Logger;
import tutien.utils.TimeUtil;
import java.util.Date;
import java.util.Random;
//
public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(MySession session, Message msg) {
        Player player = session.player;

        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryServiceNew.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryServiceNew.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryServiceNew.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryServiceNew.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryServiceNew.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryServiceNew.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryServiceNew.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.getInstance().point(player);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);

        }
    }

    public void testItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            System.out.println("type: " + type);
            System.out.println("where: " + where);
            System.out.println("index: " + index);
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        byte type;
        try {
            type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
//            System.out.println(type + " " + where + " " + index);
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            Item item = player.inventory.itemsBag.get(index);
                            if (item.isNotNullItem()) {
                                if (item.template.type == 7) {
                                    msg = new Message(-43);
                                    msg.writer().writeByte(type);
                                    msg.writer().writeByte(where);
                                    msg.writer().writeByte(index);
                                    msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.itemsBag.get(index).template.name + "?");
                                    player.sendMessage(msg);
                                } else {
                                    UseItem.gI().useItem(player, item, index);
                                }
                            }
                        } else {
                            this.eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            item = player.inventory.itemsBody.get(index);
                        } else {
                            item = player.inventory.itemsBag.get(index);
                        }
                        msg = new Message(-43);
                        msg.writer().writeByte(type);
                        msg.writer().writeByte(where);
                        msg.writer().writeByte(index);
                        msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                        player.sendMessage(msg);
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryServiceNew.gI().throwItem(player, where, index);
                    Service.getInstance().point(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                case ACCEPT_USE_ITEM:
                    UseItem.gI().useItem(player, player.inventory.itemsBag.get(index), index);
                    break;
            }
        } catch (Exception e) {
//            Logger.logException(UseItem.class, e);
        }
    }

    private void useItem(Player pl, Item item, int indexBag) {
        if (item.template.strRequire <= pl.nPoint.power) {
            switch (item.template.type) {
                case 33:
                    UseCard(pl,item);
                    break;
//                case 27:
//                    switch (item.template.id) {
//                        case 942:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl,966, 967, 968);
//                            Service.getInstance().point(pl);
//                            break;
//                        case 943:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl,969, 970, 971);
//                            Service.getInstance().point(pl);
//                            break;
//                        case 944:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl,972, 973, 974);
//                            Service.getInstance().point(pl);
//                            break;
//                        case 967:
//                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
//                            PetService.Pet2(pl,1050, 1051, 1052);
//                            Service.getInstance().point(pl);
//                            break;
//                    }
//                    break;
                case 7: //sách học, nâng skill
                    learnSkill(pl, item);
                    break;
                case 6: //đậu thần
                    this.eatPea(pl);
                    break;
                case 12: //ngọc rồng các loại
                    controllerCallRongThan(pl, item);
                    break;
                    
                case 23: //thú cưỡi mới
                case 24: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;
                case 11: //item bag          data body
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendFlagBag(pl);
                    break;
                case 73: //thú cưỡi cũ
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    break;         
                case 75:
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.gI().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                case 72: {
                    InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                    Service.getInstance().sendPetFollow(pl, (short) (item.template.iconID - 1));
                    break;
                    
                }
                default:
                    switch (item.template.id) {
                        case 457:
                            Input.gI().createFormUseGold(pl);
                            break;
                        case 1450:
                            Input.gI().createFormUseGold1(pl);
                            break;    
                        case 992:
                            pl.type = 1;
                            pl.maxTime = 7;
                            Service.getInstance().Transport(pl);
                            break;
                        case 1423:
                            pl.type = 1;
                            pl.maxTime = 6;
                            Service.getInstance().Transport(pl);
                            Service.gI().clearMap(pl);
                            break;    
                        case 361:
                            if (pl.idNRNM != -1) {
                                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                                return;
                            }
                            pl.idGo = (short) Util.nextInt(0, 6);
                            NpcService.gI().createMenuConMeo(pl, ConstNpc.CONFIRM_TELE_NAMEC, -1, "1 Sao (" + NgocRongNamecService.gI().getDis(pl, 0, (short) 353) + " m)\n2 Sao (" + NgocRongNamecService.gI().getDis(pl, 1, (short) 354) + " m)\n3 Sao (" + NgocRongNamecService.gI().getDis(pl, 2, (short) 355) + " m)\n4 Sao (" + NgocRongNamecService.gI().getDis(pl, 3, (short) 356) + " m)\n5 Sao (" + NgocRongNamecService.gI().getDis(pl, 4, (short) 357) + " m)\n6 Sao (" + NgocRongNamecService.gI().getDis(pl, 5, (short) 358) + " m)\n7 Sao (" + NgocRongNamecService.gI().getDis(pl, 6, (short) 359) + " m)", "Đến ngay\nViên " + (pl.idGo + 1) + " Sao\n50 ngọc", "Kết thức");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            break;
                        case 1408: // Cua đỏ
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1074, 1075, 1076);
                            Service.gI().point(pl);
                            break;
                        case 1411: // Mèo đuôi vàng đen
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1183, 1184, 1185);
                            Service.gI().point(pl);
                            break;
                        case 1412: // Mèo đuôi vàng trắng
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1201, 1202, 1203);
                            Service.gI().point(pl);
                            break; 
                        case 1489: //chú lùn noel
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1616, 1617, 1618);
                            Service.gI().point(pl);
                            break; 
                        case 1490: //heo bướm
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1619, 1620, 1621);
                            Service.gI().point(pl);
                            break; 
                        case 1491: //kaido
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1631, 1632, 1633);
                            Service.gI().point(pl);
                            break; 
                        case 1492: //chó
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1645, 1646, 1647);
                            Service.gI().point(pl);
                            break;  
                        case 1493: //chó
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1535, 1536, 1537);
                            Service.gI().point(pl);
                            break; 
                        case 1494: //chó
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1660, 1661, 1662);
                            Service.gI().point(pl);
                            break; 
                        case 1495: //chó
                            InventoryServiceNew.gI().itemBagToBody(pl, indexBag);
                            PetService.Pet2(pl, 1663, 1664, 1665);
                            Service.gI().point(pl);
                            break;     
                            case 1536:
                            if (pl.lastTimeTitle1 == 0) {
                                pl.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365);
                            } else {
                                pl.lastTimeTitle1 += (1000 * 60 * 60 * 24 * 365);
                            }
                            pl.isTitleUse = true;
                            Service.gI().point(pl);
                            Service.gI().sendTitle(pl, 888);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendThongBao(pl, "Bạn nhận được 365 ngày danh hiệu !");
                            break;
                        case 1537:
                            if (pl.lastTimeTitle2 == 0) {
                                pl.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365);
                            } else {
                                pl.lastTimeTitle2 += (1000 * 60 * 60 * 24 * 365);
                            }
                            pl.isTitleUse2 = true;
                            Service.gI().point(pl);
                            Service.gI().sendTitle(pl, 889);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendThongBao(pl, "Bạn nhận được 365 ngày danh hiệu !");
                            break;
                        case 1538:
                            if (pl.lastTimeTitle3 == 0) {
                                pl.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365);
                            } else {
                                pl.lastTimeTitle3 += (1000 * 60 * 60 * 24 * 365);
                            }
                            pl.isTitleUse3 = true;
                            Service.gI().point(pl);
                            Service.gI().sendTitle(pl, 890);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendThongBao(pl, "Bạn nhận được 365 ngày danh hiệu !");
                            break;
                        case 1539:
                            if (pl.lastTimeTitle4 == 0) {
                                pl.lastTimeTitle4 += System.currentTimeMillis() + (86400000L * 60L);
                            } else {
                                pl.lastTimeTitle4 += (1000 * 60 * 60 * 24 * 650);
                            }
                            pl.isTitleUse4 = true;
                            Service.gI().point(pl);
                            Service.gI().sendTitle(pl, 891);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            Service.gI().sendThongBao(pl, "Bạn nhận được 365 ngày danh hiệu !");
                            break;
                        case 1625: {  // danh hiệu dạng item
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item linhThu = ItemService.gI().createNewItem((short)(1642));
                                linhThu.itemOptions.add(new Item.ItemOption(50, Util.nextInt(0,10)));
                                linhThu.itemOptions.add(new Item.ItemOption(77, Util.nextInt(0,10)));
                                linhThu.itemOptions.add(new Item.ItemOption(103, Util.nextInt(0,10)));
                                linhThu.itemOptions.add(new Item.ItemOption(95, Util.nextInt(0,10)));
                                linhThu.itemOptions.add(new Item.ItemOption(96, Util.nextInt(0,10)));
                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Chúc mừng bạn nhận được  " + linhThu.template.name);
                            }
                            break;    
                        }
                        case 211: //nho tím
                        case 212: //nho xanh
                            eatGrapes(pl, item);
                            break;
                        case 1997://hop qua ct vip
                            Openhopct(pl, item);
                            break;
                        case 1998://hop qua leo 
                             Openhopflagbag(pl, item);
                            break;
                        case 1999://hop qua linh thú
                            Openhoppet(pl, item);
                            break;
                        case 1396://hop qua pet
                            Openhoppet1(pl, item);
                            break;
                        case 2152://hop qua pet
                            tuicaitrang(pl, item);
                            break;    
                        case 1397://túi ngọc đỏ
                            tuingoc(pl, item);
                            break;
                        case 1398://hòm kho báu
                            hombau(pl, item);
                            break;    
                        case 1197://hop qua ca
                            Openhopca(pl, item);
                            break;
                        case 1105://hop qua skh, item 2002 xd
                            UseItem.gI().Hopts(pl, item);
                            break;
                         case 1879:
                            UseItem.gI().Hopchiton(pl, item);
                            break;
                        case 1255:
                            ItemService.gI().openHopQuaHeTHUONG(pl, item);
                            break;
                        case 1256 : 
                            randomcthit(pl,item);
                            break;
                        case 1257 : 
                            randomcthp(pl,item);
                            break;
                        case 1258 : 
                            randomcode(pl,item);
                            break;                            
                        case 1267 : 
                            AHDtd(pl,item);
                            break;                            
                        case 1268 :
                            QHDtd(pl,item);
                            break;
                        case 1269 : 
                            AHDnm(pl,item);
                            break;
                        case 1270 : 
                            QHDnm(pl,item);
                            break;                            
                        case 1271 :
                            AHDxd(pl,item);
                            break;                            
                        case 1272 :
                            QHDxd(pl,item);
                            break;                            
                        case 1273 :
                            NHD(pl,item);
                            break;                         
                        case 1274 : 
                            GHDtd(pl,item);
                            break;                            
                        case 1275 :
                            JHDtd(pl,item);
                            break;
                        case 1276 : 
                            GHDnm(pl,item);
                            break;
                        case 1277 : 
                            JHDnm(pl,item);
                            break;                            
                        case 1278 :
                            GHDxd(pl,item);
                            break;                            
                        case 1279  :
                            JHDxd(pl,item);
                        case 1550 : 
                            hopao(pl,item);
                            break;                            
                        case 1551 :
                            hopquan(pl,item);
                            break;                            
                        case 1552 :
                            hopgang(pl,item);
                            break;                            
                        case 1553 :
                            hopgiay(pl,item);
                            break;                         
                        case 1554 : 
                            hopnhan(pl,item);
//                       case 1800 : 
//                            hop20K(pl,item);
//                            break;                            
//                        case 1801 :
//                            hop50k(pl,item);
//                            break;                            
//                        case 1802 :
//                            hop100k(pl,item);
//                            break;                            
//                        case 1803 :
//                            hop200k(pl,item);
//                            break;                         
//                        case 1804 : 
//                            hop500k(pl,item);
//                            break;         
//                       case 1805 : 
//                            hop1m(pl,item);
                            break;   
                        case 342:
                        case 343:
                        case 344:
                        case 345:
                            if (pl.zone.items.stream().filter(it -> it != null && it.itemTemplate.type == 22).count() < 5) {
                                Service.getInstance().DropVeTinh(pl, item, pl.zone, pl.location.x, pl.location.y);
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            } else {
                                Service.getInstance().sendThongBao(pl, "Đặt ít thôi con");
                            }
                            break;
                        case 380: //cskb
                            openCSKB(pl, item);
                            break;
                            // đan dược
                             case 1679:
                            pl.TamkjllDauLaDaiLuc[0]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1680:
                            pl.TamkjllDauLaDaiLuc[1]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1681:
                            pl.TamkjllDauLaDaiLuc[2]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1682:
                            pl.TamkjllDauLaDaiLuc[3]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1683:
                            pl.TamkjllDauLaDaiLuc[4]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1684:
                            pl.TamkjllDauLaDaiLuc[5]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                        case 1685:
                            pl.TamkjllDauLaDaiLuc[6]++;
                            InventoryServiceNew.gI().subQuantityItem(pl.inventory.itemsBag, item, 1);
                            InventoryServiceNew.gI().sendItemBags(pl);
                            Service.gI().sendThongBao(pl, "Ngươi đã hấp thụ " + item.template.name);
                            break;
                         case 1422: //máy dò bosss
                            maydoboss(pl);
                            break;   
                        case 381: //cuồng nộ
                        case 382: //bổ huyết
                        case 383: //bổ khí
                        case 384: //giáp xên
                        case 385: //ẩn danh
                        case 379: //máy dò capsule
                        case 1352: //máy dò capsule   
                            case 1353: //máy dò capsule   
                        case 2037: //máy dò cosmos
                        case 663: //bánh pudding
                        case 664: //xúc xíc
                        case 665: //kem dâu
                        case 666: //mì ly
                        case 667: //sushi
                        case 2024:
                        case 2023: //bánh pudding
//                        case 2026: //xúc xíc
                        case 2027: //kem dâu
                        case 2028: //mì ly
                        case 2029: //sushi    
                        case 752:
                        case 753:
                        case 1099:
                        case 1100:
                        case 1101:
                        case 1102:
                        case 1103:
                            useItemTime(pl, item);
                            break;
                        case 2033:
                        case 2034:
                        case 2035:
                        case 2036:    
                        case 1201:
                        case 899:
                        case 1354:
                        case 465:
                        case 466:
                        case 472:
                        case 473:
                            useItemTime(pl, item);
                            break;    
                        case 1128:
                            openDaBaoVe(pl, item);
                            break;
                        case 1129:
                            openSPL(pl, item);
                            break;
                        case 1130:
                            openDaNangCap(pl, item);
                            break;
                        case 1131 : ChangeSkill2_3(pl,item);
                            break;
                        case 1979 : changeskill4(pl,item);
                            break;
                        case 1236:
                            openManhTS(pl, item);
                            break;
                        case 570:
                            openWoodChest(pl,item);
                            break;
                        case 1822:
                            case 1823:
                                case 1824:
                                    case 1825:
                                        case 1826:
                                            case 1827:
                                                case 1828:
                            Service.getInstance().sendThongBao(pl, "Hoang Van Luu ");
                            useItemTimenrd(pl, item);
                            break;
                                 case 1829:
                                    case 1830:
                                            case 1831:
                                                case 1832:
                            Service.getInstance().sendThongBao(pl, "Hoang Van Luu");
                            useItemTimenrd2(pl, item);
                            break;
                        case 521: //tdlt
                            useTDLT(pl, item);
                            break;
                        case 454: //bông tai
                            UseItem.gI().usePorata(pl);
                            break;
                        case 1389: //bông tai c2
                            pl.fusion.isBTC2 = item.template.id == 1389;
                            UseItem.gI().usePorata2(pl);                           
                            break;
                        case 1386: //bông tai c3
                            UseItem.gI().usePorata3(pl);
                            break;
                        case 1387: //bông tai c4
                            UseItem.gI().usePorata4(pl);
                            break;
                        case 1388: //bông tai c5
                            UseItem.gI().usePorata5(pl);
                            break;    
                        case 193: //gói 10 viên capsule
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                        case 194: //capsule đặc biệt
                            openCapsuleUI(pl);
                            break;
                        case 722: //Capsule hồng ngọc
                            int radomsoluonghongngoc = Util.nextInt(50, 135);
                            pl.inventory.ruby += radomsoluonghongngoc;
                            Service.gI().sendMoney(pl);
                            Service.gI().sendThongBao(pl, "Bạn nhận được " + radomsoluonghongngoc + " hồng ngọc");
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            break;    
                        case 401: //đổi đệ tử
                            changePet(pl, item);
                            break;
                        case 1108: //đổi đệ tử
                            doide(pl, item);
                            break;
                        case 1107: //đổi đệ tử
                            changePicPet(pl, item);
                            break;    
                            case 574:
                            UseItem.gI().useruonggioto(pl);
                            break;
                        case 1185: // quà chúc tết
                            UseItem.gI().usequachuctet(pl);
                            break;
                        case 1186: // quà chúc tết
                            UseItem.gI().usehopquatet(pl);
                            break;
                        case 1187: // quà chúc tết
                            UseItem.gI().usecapsunnammoi(pl);
                            break;    
                              case 1884: // quà chúc tết
                           usenap500k(pl, item);
                            break;    
                        case 1880: //nap moc
                            usenap50k(pl, item);
                            break;    
                            case 1881:
                            usenap100k(pl, item);
                            break;
                        case 1882: // quà chúc tết
                            usenap200k(pl, item);
                            break;
                        case 1883: // quà chúc tết
                         usenap300k(pl, item);
                            break;
                        case 1735: // quà chúc tết
                           thanff(pl, item);
                            break;    
                            case 1690: 
                           thanlua225(pl, item);
                           break;
                            case 1691: 
                           pet225(pl, item);
                            break;  
                        case 1885:
                            usenap800k(pl, item);
                            break;
                        case 1886: // quà chúc tết
                            usenap1m(pl, item);
                            break;
                        case 1887: // quà chúc tết
                            usenap1m5(pl, item);
                            break;
                        case 1621: // quà chúc tết
                            random300ngoc(pl, item);
                            break;
                        case 1622: // quà chúc tết
                            random500ngoc(pl, item);
                          break;
                              case 1623: //nap moc
                            top1sever(pl, item);
                            break;    
                            case 1624:
                            top10sever(pl, item);
                            case 1627: //nap moc
                            top30sever(pl, item);
                            break;    
                            case 1626:
                            top20sever(pl, item);
                            case 1629:
                            quafree(pl, item);
                             case 1580:
                            freefire(pl, item);
                            
//                        case 1888: // quà chúc tết
//                            usenap2m(pl, item);
//                            break;    
                            
                        case 402: //sách nâng chiêu 1 đệ tử
                        case 403: //sách nâng chiêu 2 đệ tử
                        case 404: //sách nâng chiêu 3 đệ tử
                        case 759: //sách nâng chiêu 4 đệ tử
                        case 1980: //sách nâng chiêu 4 đệ tử    
                            upSkillPet(pl, item);
                            break;
                        case 1426:
                                
                             int time = 5000;
                            if (pl != null) {
                                
                               EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), time);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.CANCAUTHUONG_EFFECT);
                                ItemTimeService.gI().sendItemTime(pl, 9595,time / 1000);
                                try {
                                    Thread.sleep(time);
                                } catch (Exception e) {
                                }
                               
                              ItemService.gI().OpenItem1322(pl, item);
                                    
                            }
                  
                            break;    
                        case 1427:
                               
                            time = 5500;
                            if (pl != null) {
                                EffectSkillService.gI().setBlindDCTT(pl, System.currentTimeMillis(), time);
                                EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT, EffectSkillService.CANCAUCAOCAP_EFFECT);
                                ItemTimeService.gI().sendItemTime(pl, 9596, time / 1000);
                                try {
                                    Thread.sleep(time);
                                } catch (Exception e) {
                                }
                                
                               ItemService.gI().OpenItem1323(pl, item);
                                    
                                
                            }
                             
                            break;    
                        case 2000://hop qua skh, item 2000 td
                        case 2001://hop qua skh, item 2001 nm
                        case 2002://hop qua skh, item 2002 xd
                            UseItem.gI().ItemSKH(pl, item);
                            break;

                        case 2003://hop qua skh, item 2003 td
                        case 2004://hop qua skh, item 2004 nm
                        case 2005://hop qua skh, item 2005 xd
                            UseItem.gI().ItemDHD(pl, item);
                            break;
                        case 736:
                            ItemService.gI().OpenItem736(pl, item);
                            break;
                        case 987:
                            Service.getInstance().sendThongBao(pl, "Bảo vệ trang bị không bị rớt cấp"); //đá bảo vệ
                            break;
                        case 2006:
                            Input.gI().createFormChangeNameByItem(pl);
                            break;
                        case 1259: {
                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
                                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống");
                            } else {
                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                                Item PET = ItemService.gI().createNewItem((short) Util.nextInt(1311, 1313));
                                PET.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5,20)));
                                PET.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5,20)));
                                PET.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5,20)));
                                InventoryServiceNew.gI().addItemBag(pl, PET);
                                InventoryServiceNew.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Chúc mừng bạn nhận được  " + PET.template.name);
                            }
                        }
                        break;
                                
//                        
                    //   case 2028: {
//                            if (InventoryServiceNew.gI().getCountEmptyBag(pl) == 0) {
//                                Service.getInstance().sendThongBao(pl, "Hành trang không đủ chỗ trống");
//                            } else {
//                                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//                                Item linhThu = ItemService.gI().createNewItem((short) Util.nextInt(2019, 2026));
//                                linhThu.itemOptions.add(new Item.ItemOption(50, 10));
//                                linhThu.itemOptions.add(new Item.ItemOption(77, 10));
//                                linhThu.itemOptions.add(new Item.ItemOption(103, 10));
//                                linhThu.itemOptions.add(new Item.ItemOption(95, 3));
//                                linhThu.itemOptions.add(new Item.ItemOption(96, 3));
//                                InventoryServiceNew.gI().addItemBag(pl, linhThu);
//                                InventoryServiceNew.gI().sendItemBags(pl);
//                                Service.getInstance().sendThongBao(pl, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
//                            }
//                            break; }
                    }
                    break;
            }
            InventoryServiceNew.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.getInstance().point(player);
        Service.getInstance().sendFlagBag(player);
    }

    private void changePet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender + 1;
            if (gender > 2) {
                gender = 0;
            }
            PetService.gI().changeNormalPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
                private void usenap300k(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1458);
            pet.itemOptions.add(new Item.ItemOption(50,100));
            pet.itemOptions.add(new Item.ItemOption(77,100));
            pet.itemOptions.add(new Item.ItemOption(103,100));
            pet.itemOptions.add(new Item.ItemOption(101,100));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 500k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
                   private void usenap500k(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1247);
            pet.itemOptions.add(new Item.ItemOption(50,300));
            pet.itemOptions.add(new Item.ItemOption(77,300));
            pet.itemOptions.add(new Item.ItemOption(103,300));
            pet.itemOptions.add(new Item.ItemOption(101,00));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 800k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
                    private void usenap800k(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1832);
            pet.itemOptions.add(new Item.ItemOption(50,200));
            pet.itemOptions.add(new Item.ItemOption(77,200));
            pet.itemOptions.add(new Item.ItemOption(103,200));
            pet.itemOptions.add(new Item.ItemOption(101,00));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 1000k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
                         private void usenap1m(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1360);
            pet.itemOptions.add(new Item.ItemOption(50,200));
            pet.itemOptions.add(new Item.ItemOption(77,200));
            pet.itemOptions.add(new Item.ItemOption(103,200));
            pet.itemOptions.add(new Item.ItemOption(5,200));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 1000k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
                            private void usenap1m5(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1510);
            pet.itemOptions.add(new Item.ItemOption(50,300));
            pet.itemOptions.add(new Item.ItemOption(77,300));
            pet.itemOptions.add(new Item.ItemOption(103,300));
            pet.itemOptions.add(new Item.ItemOption(101,100));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 1000k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
          private void thanff(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1509);
            pet.itemOptions.add(new Item.ItemOption(50,50));
            pet.itemOptions.add(new Item.ItemOption(77,50));
            pet.itemOptions.add(new Item.ItemOption(103,50));
            pet.itemOptions.add(new Item.ItemOption(101,50));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được than lua " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
        private void usenap50k(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
   
//    int[] piet = new int[]{1288};
//    int[] rdpt = new int[]{1408};
//    int[] l = new int[]{1408};
//    int[] lu = new int[]{1408};
//    int[] luu = new int[]{1408};
//    int[] ct2 = new int[]{50, 77, 103};
     
      

    Item pet = ItemService.gI().createNewItem((short) 1288);
            pet.itemOptions.add(new Item.ItemOption(50,175));
            pet.itemOptions.add(new Item.ItemOption(77,175));
            pet.itemOptions.add(new Item.ItemOption(103,175));
            pet.itemOptions.add(new Item.ItemOption(101,175));
            pet.itemOptions.add(new Item.ItemOption(30,0));
         
       
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
   
       
   
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 50k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
         private void thanlua225(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1509);
            pet.itemOptions.add(new Item.ItemOption(50,225));
            pet.itemOptions.add(new Item.ItemOption(77,225));
            pet.itemOptions.add(new Item.ItemOption(103,225));
            pet.itemOptions.add(new Item.ItemOption(5,225));
            pet.itemOptions.add(new Item.ItemOption(14,20));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được than lua " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
          private void pet225(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1494);
            pet.itemOptions.add(new Item.ItemOption(50,225));
            pet.itemOptions.add(new Item.ItemOption(77,225));
            pet.itemOptions.add(new Item.ItemOption(103,225));
            pet.itemOptions.add(new Item.ItemOption(5,225));
            pet.itemOptions.add(new Item.ItemOption(14,20));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được than lua " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
                    private void usenap200k(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1611);
            pet.itemOptions.add(new Item.ItemOption(0,50000));
//            pet.itemOptions.add(new Item.ItemOption(77,75));
//            pet.itemOptions.add(new Item.ItemOption(103,75));
//            pet.itemOptions.add(new Item.ItemOption(101,75));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 300k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
            private void usenap100k(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    Item pet = ItemService.gI().createNewItem((short) 1492);
            pet.itemOptions.add(new Item.ItemOption(50,75));
            pet.itemOptions.add(new Item.ItemOption(77,75));
            pet.itemOptions.add(new Item.ItemOption(103,75));
            pet.itemOptions.add(new Item.ItemOption(101,75));
            pet.itemOptions.add(new Item.ItemOption(30,0));
     InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được quà mốc 200k " );
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
              private void random300ngoc(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1309,1311,1312,1313,1314};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 70){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(150, 350)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(150, 350)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(150, 350)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 35)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(50, 150)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(3, 7)));            
             } else {                      
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(150, 350)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(150, 350)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(150, 350)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 35)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(50, 150)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
                   private void random500ngoc(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1307,1308,1309,1311,1312,1313,1314};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 75){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(350, 500)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(350, 500)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(350, 500)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 35)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(100, 250)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(3, 7)));            
             } else {                      
           pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(350, 500)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(350, 500)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(350, 500)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 35)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(100, 250)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
                    private void top1sever(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1325};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 100){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(555, 555)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(555, 555)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(555, 555)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 35)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(235, 235)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(30,Util.nextInt(1, 1)));
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
        private void top10sever(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1342};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 100){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(470, 470)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(470, 470)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(470, 470)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 20)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(175, 175)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(30,Util.nextInt(1, 1)));
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
          private void top20sever(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1342};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 100){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(445, 445)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(445, 445)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(445, 445)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 20)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(145, 145)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(30,Util.nextInt(1, 1)));
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
          private void quafree(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1342};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 100){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(1, 3)));
            pet.itemOptions.add(new Item.ItemOption(30,Util.nextInt(1, 1)));
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
            private void freefire(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1509};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 100){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(20, 20)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(200, 200)));
            pet.itemOptions.add(new Item.ItemOption(30,Util.nextInt(1, 1)));
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
            private void top30sever(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1342};
    int randompet = new Random().nextInt(rdpet.length);
   
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
  if  (id <= 100){           
            pet.itemOptions.add(new Item.ItemOption(50,Util.nextInt(400, 400)));
            pet.itemOptions.add(new Item.ItemOption(77,Util.nextInt(400, 400)));
            pet.itemOptions.add(new Item.ItemOption(103,Util.nextInt(400, 400)));
            pet.itemOptions.add(new Item.ItemOption(14,Util.nextInt(14, 20)));
            pet.itemOptions.add(new Item.ItemOption(5,Util.nextInt(125, 125)));
            pet.itemOptions.add(new Item.ItemOption(205,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(207,Util.nextInt(1, 1)));
            pet.itemOptions.add(new Item.ItemOption(30,Util.nextInt(1, 1)));
       }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
       
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
    private void Openhopct(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdct = new int[]{1098,729};
    int[] rdop = new int[]{5, 14, 94, 108, 97};
    int randomct = new Random().nextInt(rdct.length);
    int randomop = new Random().nextInt(rdop.length);
    Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
    Item vt = ItemService.gI().createNewItem((short) Util.nextInt(342, 345));
        if (id <= 90){           
            ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(15, 30)));
            ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(15, 30)));
            ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(15, 30)));
            ct.itemOptions.add(new Item.ItemOption(93,Util.nextInt(1, 3)));            
        } else {                      
            ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(20, 30)));
            ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(20, 30)));
            ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(20, 30)));           
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, ct);
        InventoryServiceNew.gI().addItemBag(pl, vt);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name + " và " + vt.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
    private void Openhopflagbag(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdfl = new int[]{1330,1331,1332,1333,1334,1335,1336,1337,1338,1339,1340,1341,1342,1343,1344,1345,1346};
    int[] rdop = new int[]{50, 77, 103};
    int randomfl = new Random().nextInt(rdfl.length);
    int randomop = new Random().nextInt(rdop.length);
    Item fl = ItemService.gI().createNewItem((short) rdfl[randomfl]);
    Item vt = ItemService.gI().createNewItem((short) Util.nextInt(342, 345));
        if (id <= 90){           
            fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 15)));           
            fl.itemOptions.add(new Item.ItemOption(93,Util.nextInt(3, 30)));            
        } else {                      
            fl.itemOptions.add(new Item.ItemOption(rdop[randomop], Util.nextInt(5, 15)));       
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, fl);
        InventoryServiceNew.gI().addItemBag(pl, vt);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + fl.template.name+ " và " + vt.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
    private void Openhoppet(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1235,1236,1237,1238,1239,1240,1241,1242,1243,1244,1245,1246,1247,1248};
    int[] rdop = new int[]{50, 77, 103};
    int randompet = new Random().nextInt(rdpet.length);
    int randomop = new Random().nextInt(rdop.length);
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
    Item vt = ItemService.gI().createNewItem((short) Util.nextInt(342, 345));
        if (id <= 90){           
            pet.itemOptions.add(new Item.ItemOption(50,13));
            pet.itemOptions.add(new Item.ItemOption(77,12));
            pet.itemOptions.add(new Item.ItemOption(103,14));      
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(3, 15)));
        } else {                      
           pet.itemOptions.add(new Item.ItemOption(50,13));
            pet.itemOptions.add(new Item.ItemOption(77,12));
            pet.itemOptions.add(new Item.ItemOption(103,14));
            
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().addItemBag(pl, vt);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name+ " và " + vt.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
    private void Openhoppet1(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1408,1411,1412};
    int[] rdop = new int[]{50, 77, 103};
    int randompet = new Random().nextInt(rdpet.length);
    int randomop = new Random().nextInt(rdop.length);
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
    Item vt = ItemService.gI().createNewItem((short) Util.nextInt(342,345));

    Item nro = ItemService.gI().createNewItem((short) Util.nextInt(14,20));
        if (id <= 90){           
            pet.itemOptions.add(new Item.ItemOption(50,10));
            pet.itemOptions.add(new Item.ItemOption(77,5));
            pet.itemOptions.add(new Item.ItemOption(103,5)); 
            pet.itemOptions.add(new Item.ItemOption(47,5));
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(1, 5)));
        } else {                      
           pet.itemOptions.add(new Item.ItemOption(50,13));
            pet.itemOptions.add(new Item.ItemOption(77,12));
            pet.itemOptions.add(new Item.ItemOption(103,14));
            pet.itemOptions.add(new Item.ItemOption(47,5));
            
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().addItemBag(pl, vt);
    //    InventoryServiceNew.gI().addItemBag(pl, spl);
        InventoryServiceNew.gI().addItemBag(pl, nro);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name+ " và " + vt.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
    private void tuicaitrang(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1320, 1321, 1322};
    int[] rdop = new int[]{50, 77, 103};
    int randompet = new Random().nextInt(rdpet.length);
    int randomop = new Random().nextInt(rdop.length);
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
    Item vt = ItemService.gI().createNewItem((short) Util.nextInt(220,224));
        if (id <= 90){           
            pet.itemOptions.add(new Item.ItemOption(50,25));
            pet.itemOptions.add(new Item.ItemOption(77,22));
            pet.itemOptions.add(new Item.ItemOption(103,23));      
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(1, 5)));
        } else {                      
           pet.itemOptions.add(new Item.ItemOption(50,25));
            pet.itemOptions.add(new Item.ItemOption(77,22));
            pet.itemOptions.add(new Item.ItemOption(103,23));
            
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().addItemBag(pl, vt);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name+ " và " + vt.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }
    private void tuingoc(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{77, 861};
    int[] rdop = new int[]{30};
    int randompet = new Random().nextInt(rdpet.length);
    int randomop = new Random().nextInt(rdop.length);
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
        if (id <= 90){           
            pet.itemOptions.add(new Item.ItemOption(30,0));
        } else {                      
           pet.itemOptions.add(new Item.ItemOption(30,0));
            
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
    private void hombau(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{76, 188, 189, 190};
    int[] rdop = new int[]{30};
    int randompet = new Random().nextInt(rdpet.length);
    int randomop = new Random().nextInt(rdop.length);
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
        if (id <= 90){           
            pet.itemOptions.add(new Item.ItemOption(30,0));
        } else {                      
           pet.itemOptions.add(new Item.ItemOption(30,0));
            
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
    }
    private void Openhopca(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdpet = new int[]{1155,1156,1157,1158,1159,1160,1161,1162,1145,1146,1143};
    int[] rdop = new int[]{50, 77, 103};
    int randompet = new Random().nextInt(rdpet.length);
    int randomop = new Random().nextInt(rdop.length);
    Item pet = ItemService.gI().createNewItem((short) rdpet[randompet]);
    Item vt = ItemService.gI().createNewItem((short) Util.nextInt(16,17));
        if (id <= 90){           
            pet.itemOptions.add(new Item.ItemOption(50,13));
            pet.itemOptions.add(new Item.ItemOption(77,12));
            pet.itemOptions.add(new Item.ItemOption(103,14));      
            pet.itemOptions.add(new Item.ItemOption(93,Util.nextInt(3, 15)));
        } else {                      
           pet.itemOptions.add(new Item.ItemOption(50,13));
            pet.itemOptions.add(new Item.ItemOption(77,12));
            pet.itemOptions.add(new Item.ItemOption(103,14));
            
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, pet);
        InventoryServiceNew.gI().addItemBag(pl, vt);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + pet.template.name+ " và " + vt.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
    }
  }

    private void doide(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changeBerusPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }
    
    private void changePicPet(Player player, Item item) {
        if (player.pet != null) {
            int gender = player.pet.gender;
            PetService.gI().changePicPet(player, gender);
            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
        } else {
            Service.getInstance().sendThongBao(player, "Không thể thực hiện");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryServiceNew.gI().addItemBag(pl, ct);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.getInstance().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }
        private void openDaBaoVe(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {987, 987};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openSPL(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {441, 442, 443, 444, 445, 446, 447};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openDaNangCap(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {220, 221, 222, 223, 224};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 10);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }

    private void openManhTS(Player player, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
            short[] possibleItems = {1066, 1067, 1068, 1069, 1070};
            byte selectedIndex = (byte) Util.nextInt(0, possibleItems.length - 2);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item newItem = ItemService.gI().createNewItem(possibleItems[selectedIndex]);
            newItem.itemOptions.add(new ItemOption(73, 0));
            newItem.quantity = (short) Util.nextInt(1, 99);
            InventoryServiceNew.gI().addItemBag(player, newItem);
            icon[1] = newItem.template.iconID;

            InventoryServiceNew.gI().subQuantityItemsBag(player, item, 1);
            InventoryServiceNew.gI().sendItemBags(player);

            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.gI().sendThongBao(player, "Hàng trang đã đầy");
        }
    }
    private void openCSKB(Player pl, Item item) {
        if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.gold += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.gold > Inventory.LIMIT_GOLD) {
                    pl.inventory.gold = Inventory.LIMIT_GOLD;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }
    private void useItemTime(Player pl, Item item) {
        switch (item.template.id) {
            case 382: //bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                break;
            case 383: //bổ khí
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                break;
            case 384: //giáp xên
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                break;
            case 381: //cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                Service.getInstance().point(pl);
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case 379: //máy dò capsule
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1354:// Máy dò mảnh ấn bên ngục tù
                pl.itemTimesieucap.lastTimeUseXiMuoi = System.currentTimeMillis();
                pl.itemTimesieucap.isUseXiMuoi = true;
                Service.getInstance().point(pl);
                break;
            case 1099:// cn
                pl.itemTimesieucap.lastTimeCuongNo3 = System.currentTimeMillis();
                pl.itemTimesieucap.isUseCuongNo3 = true;
                Service.getInstance().point(pl);
                break;
            case 1100:// bo huyet
                pl.itemTimesieucap.lastTimeBoHuyet3 = System.currentTimeMillis();
                pl.itemTimesieucap.isUseBoHuyet3 = true;
                break;
            case 1102://bo khi
                pl.itemTimesieucap.lastTimeBoKhi3 = System.currentTimeMillis();
                pl.itemTimesieucap.isUseBoKhi3 = true;
                break;
            case 1101://xbh
                pl.itemTimesieucap.lastTimeGiapXen3 = System.currentTimeMillis();
                pl.itemTimesieucap.isUseGiapXen3 = true;
                break;
            case 1103://an danh
                pl.itemTimesieucap.lastTimeAnDanh3 = System.currentTimeMillis();
                pl.itemTimesieucap.isUseAnDanh3 = true;
                break;
            case 899:// bo huyet
                pl.itemTimesieucap.lastTimeKeo = System.currentTimeMillis();
                pl.itemTimesieucap.isKeo = true;
                break;
            // bánh trung thu
            case 465:
            case 466:
            case 472:
            case 473:
                pl.itemTimesieucap.lastTimeUseBanh = System.currentTimeMillis();
                pl.itemTimesieucap.isUseTrungThu = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTimesieucap.iconBanh);
                pl.itemTimesieucap.iconBanh = item.template.iconID;
                break;
            case 2033: //máy dò đồ
                pl.itemTime.lastTimeBinhCanx2 = System.currentTimeMillis();
                pl.itemTime.isUseBinhCanx2 = true;
                break;
            case 2034: //máy dò đồ
                pl.itemTime.lastTimeBinhCanx5 = System.currentTimeMillis();
                pl.itemTime.isUseBinhCanx5 = true;
                break;
            case 2035: //máy dò đồ
                pl.itemTime.lastTimeBinhCanx7 = System.currentTimeMillis();
                pl.itemTime.isUseBinhCanx7 = true;
                break;
            case 2036: //máy dò đồ
                pl.itemTime.lastTimeBinhCanx10 = System.currentTimeMillis();
                pl.itemTime.isUseBinhCanx10 = true;
                break;    
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667://sushi
                pl.itemTimesieucap.lastTimeMeal = System.currentTimeMillis();
                pl.itemTimesieucap.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTimesieucap.iconMeal);
                pl.itemTimesieucap.iconMeal = item.template.iconID;
                break;
            case 2051: // đuôi khỉ
                pl.itemTime.lastTimeDuoikhi = System.currentTimeMillis();
                pl.itemTime.isDuoikhi = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconDuoi);
                pl.itemTime.iconDuoi = item.template.iconID;
                break;
            case 1201: //máy dò đồ
                pl.itemTime.lastTimeUseMayDo2 = System.currentTimeMillis();
                pl.itemTime.isUseMayDo2 = true;
                break;
        }
        Service.getInstance().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId >= SummonDragon.NGOC_RONG_1_SAO && tempId <= SummonDragon.NGOC_RONG_7_SAO) {
            switch (tempId) {
                case SummonDragon.NGOC_RONG_1_SAO:
 //               case SummonDragon.NGOC_RONG_2_SAO:
//                case SummonDragon.NGOC_RONG_3_SAO:
                    SummonDragon.gI().openMenuSummonShenron(pl, (byte) (tempId - 13));
                    break;
                default:
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.TUTORIAL_SUMMON_DRAGON,
                            -1, "Bạn chỉ có thể gọi rồng từ ngọc 3 sao, 2 sao, 1 sao", "Hướng\ndẫn thêm\n(mới)", "OK");
                    break;
            }
        }
    }
   

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                if (curSkill.point == 7) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            }
        } catch (Exception e) {
            Logger.logException(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 10) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.fusion.typeFusion == 120) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }
    
    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 12 || pl.fusion.typeFusion == 14) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 14) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata5(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4 || pl.fusion.typeFusion == 6 || pl.fusion.typeFusion == 8 || pl.fusion.typeFusion == 10 || pl.fusion.typeFusion == 12) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion5(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }
    public boolean maydoboss(Player pl) {
        try {
            BossManager.gI().showListBoss(pl);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
    public void usequachuctet(Player player) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 2) {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            Item ruonggioto = null;
            for (Item item : player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id ==  1185) {
                    ruonggioto= item;
                    break;
                }
            }
            if (ruonggioto != null){
            int rd2 = Util.nextInt(0, 100);
            int rac2 = 60;
            int ruby2 = 15;
            int tv2 = 20;
            int ct2 = 5;          
            Item item = randomRac(true);
            if (rd2 <= rac2){
                item = randomRac(true);
            } else if (rd2 <= rac2 + ruby2){
                item = hongngocrdv2();
            } else if (rd2 <= rac2 + ruby2 + tv2) {
                item = thoivangrdv2(true);
            } else if (rd2 <= rac2 + ruby2 + tv2 + ct2) {
                item = caitrangtet(true);
            }
            icon[0] = ruonggioto.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, ruonggioto, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            player.diemsukientet +=1;
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Item caitrangtet(boolean rating) {
       short[] ct2 = {1339,1340,1341,1342,1343,1344,1345,1346,1348,1349};
        Item item = ItemService.gI().createNewItem(ct2[Util.nextInt(ct2.length - 1)], 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25,30)));//hp 50%
        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25,30)));//ki 50%
        item.itemOptions.add(new Item.ItemOption(147, Util.nextInt(25,30)));//sd 50%
        item.itemOptions.add(new Item.ItemOption(106, 0)); //k ảnh hưởng bới cái lạnh
        if (Util.isTrue(1, 2) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(50) + 1));//hsd
        }
        return item;
    }
    public void usehopquatet(Player player) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 2) {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            Item ruonggioto = null;
            for (Item item : player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id ==  1185) {
                    ruonggioto= item;
                    break;
                }
            }
            if (ruonggioto != null){
            int rd2 = Util.nextInt(0, 100);
            int rac2 = 60;
            int ruby2 = 15;
            int tv2 = 20;
            int ct2 = 5;          
            Item item = randomRac(true);
            if (rd2 <= rac2){
                item = randomRac(true);
            } else if (rd2 <= rac2 + ruby2){
                item = hongngocrdv2();
            } else if (rd2 <= rac2 + ruby2 + tv2) {
                item = thoivangrdv2(true);
            } else if (rd2 <= rac2 + ruby2 + tv2 + ct2) {
                item = pettet(true);
            }
            icon[0] = ruonggioto.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, ruonggioto, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            player.diemsukientet +=1;
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Item pettet(boolean rating) {
       short[] ct2 = {1492,1494,1495};
        Item item = ItemService.gI().createNewItem(ct2[Util.nextInt(ct2.length - 1)], 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(80, Util.nextInt(5000,10000)));//ki 50%
        item.itemOptions.add(new Item.ItemOption(81, Util.nextInt(5000,10000)));//sd 50%
        if (Util.isTrue(1, 2) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(50) + 1));//hsd
        }
        return item;
    }
    public void usecapsunnammoi(Player player) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 2) {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            Item ruonggioto = null;
            for (Item item : player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id ==  1185) {
                    ruonggioto= item;
                    break;
                }
            }
            if (ruonggioto != null){
            int rd2 = Util.nextInt(0, 100);
            int rac2 = 60;
            int ruby2 = 15;
            int tv2 = 20;
            int ct2 = 5;          
            Item item = randomRac(true);
            if (rd2 <= rac2){
                item = randomRac(true);
            } else if (rd2 <= rac2 + ruby2){
                item = hongngocrdv2();
            } else if (rd2 <= rac2 + ruby2 + tv2) {
                item = thoivangrdv2(true);
            } else if (rd2 <= rac2 + ruby2 + tv2 + ct2) {
                item = thucuoitet(true);
            }
            icon[0] = ruonggioto.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, ruonggioto, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            player.diemsukientet +=1;
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Item thucuoitet(boolean rating) {
       short[] ct2 = {1523,1524,1525};
        Item item = ItemService.gI().createNewItem(ct2[Util.nextInt(ct2.length - 1)], 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(104, Util.nextInt(10,15)));//ki 50%
        if (Util.isTrue(1, 2) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(50) + 1));//hsd
        }
        return item;
    }
    public Item randomRac(boolean rating) {
        short[] rac2 = {220,221,222,223,224};
        Item item = ItemService.gI().createNewItem(rac2[Util.nextInt(rac2.length - 1)], 1);
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(20) + 1));//hsd
       }
        return item;
    }

    public void useruonggioto(Player player) {
        try {
            if (InventoryServiceNew.gI().getCountEmptyBag(player) <= 2) {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 3 ô trống hành trang");
                return;
            }
            short[] icon = new short[2];
            Item ruonggioto = null;
            for (Item item : player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id ==  574) {
                    ruonggioto= item;
                    break;
                }
            }
            if (ruonggioto != null){
            int rd2 = Util.nextInt(0, 100);
            int rac2 = 60;
            int ruby2 = 15;
            int tv2 = 20;
            int ct2 = 5;          
            Item item = randomRac2(true);
            if (rd2 <= rac2){
                item = randomRac2(true);
            } else if (rd2 <= rac2 + ruby2){
                item = hongngocrdv2();
            } else if (rd2 <= rac2 + ruby2 + tv2) {
                item = thoivangrdv2(true);
            } else if (rd2 <= rac2 + ruby2 + tv2 + ct2) {
                item = caitrangrdv2(true);
            }
            icon[0] = ruonggioto.template.iconID;
            icon[1] = item.template.iconID;
            InventoryServiceNew.gI().subQuantityItemsBag(player, ruonggioto, 1);
            InventoryServiceNew.gI().addItemBag(player, item);
            InventoryServiceNew.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + item.template.name);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Item randomRac2(boolean rating) {
        short[] rac2 = {1227,1228,1229,1230,1236,1237,1238};
        Item item = ItemService.gI().createNewItem(rac2[Util.nextInt(rac2.length - 1)], 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70,100)));//hp 50%
        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70,100)));//ki 50%
        item.itemOptions.add(new Item.ItemOption(147, Util.nextInt(50,110)));//sd 50%
        item.itemOptions.add(new Item.ItemOption(101, Util.nextInt(100,160)));//smtn + 500%
        item.itemOptions.add(new Item.ItemOption(30, 0)); //k thể gd
        item.itemOptions.add(new Item.ItemOption(106, 0)); //k ảnh hưởng bới cái lạnh
        if (Util.isTrue(995, 1000) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(20) + 1));//hsd
       }
        return item;
    }
    public Item caitrangrdv2(boolean rating) {
       short[] ct2 = {1222,1223};
        Item item = ItemService.gI().createNewItem(ct2[Util.nextInt(ct2.length - 1)], 1);
        item.itemOptions.add(new Item.ItemOption(76, 1));//VIP
        item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(70,150)));//hp 50%
        item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(70,120)));//ki 50%
        item.itemOptions.add(new Item.ItemOption(147, Util.nextInt(80,100)));//sd 50%
        item.itemOptions.add(new Item.ItemOption(101, Util.nextInt(160,170)));//smtn + 500%
        item.itemOptions.add(new Item.ItemOption(106, 0)); //k ảnh hưởng bới cái lạnh
        if (Util.isTrue(1, 2) && rating) {// tỉ lệ ra hsd
            item.itemOptions.add(new Item.ItemOption(93, Util.nextInt(50) + 1));//hsd
        }
        return item;
    }
    public Item hongngocrdv2() {
        Item item = ItemService.gI().createNewItem((short)861);
        item.quantity = Util.nextInt(1000,1500);
        return item;
    }
    public Item thoivangrdv2(boolean rating) {
         short[] thoivangrdv2 = {14,15,16,17,18,19,20};
        Item item = ItemService.gI().createNewItem(thoivangrdv2[Util.nextInt(thoivangrdv2.length - 1)],30 );
       return item;
        }


    private void openCapsuleUI(Player pl) {
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        Zone zoneChose = pl.mapCapsule.get(index);
        //Kiểm tra số lượng người trong khu

        if (zoneChose.getNumOfPlayers() > 25
                || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapMaBu(zoneChose.map.mapId)
                || MapService.gI().isMapConDuongRanDoc(zoneChose.map.mapId)
                || MapService.gI().isMapHuyDiet(zoneChose.map.mapId)) {
            Service.getInstance().sendThongBao(pl, "Hiện tại không thể vào được khu!");
            return;
        }
        if (zoneChose.map.mapId == 178 || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)) {
            Service.gI().sendThongBaoOK(pl, "map này không thể quy trở lại!!");
            return;
        }
        
        if (zoneChose.map.mapId == 196 || MapService.gI().isMapDoanhTrai(zoneChose.map.mapId)
                || MapService.gI().isMapGiaiCuuMiNuong(zoneChose.map.mapId)
                || MapService.gI().isMapBanDoKhoBau(zoneChose.map.mapId)) {
            Service.gI().sendThongBaoOK(pl, "Map này không thể khứ hồi!!");
            return;
            } else {
        if (index != 0 || zoneChose.map.mapId == 21
                || zoneChose.map.mapId == 22
                || zoneChose.map.mapId == 23) {
            pl.mapBeforeCapsule = pl.zone;
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }
    }
    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (Item.ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.getInstance().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.getInstance().sendInfoPlayerEatPea(player.pet);
                Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }

            InventoryServiceNew.gI().subQuantityItemsBag(player, pea, 1);
            InventoryServiceNew.gI().sendItemBags(player);
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 1980: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 4)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;    

            }

        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }

    private void ItemSKH(Player pl, Item item) {//hop qua skh
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void ItemDHD(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Hãy chọn một món quà", "Áo", "Quần", "Găng", "Giày", "Rada", "Từ Chối");
    }

    private void Hopts(Player pl, Item item) {//luuudeptrai
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của bạn đi", "Trái Đất", "Namec", "Xayda", "Từ chổi");
    }
    private void Hopchiton(Player pl, Item item) {//luuudeptrai
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Lấy?", "Mở");
    }
     private void openWoodChest(Player pl, Item item) {
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0) {
            Item itemReward = null;
            int param = pl.inventory.getParam(item, 72);
            short[] temp = {1079, 722};
            int[][] gold = {{1000, 2000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (param < 9) {
                pl.inventory.ruby += Util.nextInt(gold[0][0], gold[0][1]);
                if (pl.inventory.ruby > 2000000000) {
                    pl.inventory.ruby = 2000000000;
                }
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 7743;
            } else if (param == 9 || param == 10) {
                itemReward = ItemService.gI().createNewItem((short) 861);
                itemReward.quantity = Util.nextInt(2000, 5000);
                InventoryServiceNew.gI().addItemBag(pl, itemReward);
                icon[1] = itemReward.template.iconID;
            }
            if (param == 11) {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.quantity = Util.nextInt(50, 100);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryServiceNew.gI().addItemBag(pl, it);
                icon[1] = it.template.iconID;
            }
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
//        pl.inventory.addGold(gold);
//        InventoryServiceNew.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);

        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng đợi 24h");
        }
    }

    private int randClothes(int level) {
        return LIST_ITEM_CLOTHES[Util.nextInt(0, 2)][Util.nextInt(0, 4)][level - 1];
    }
    public static final int[][][] LIST_ITEM_CLOTHES = {
            // áo , quần , găng ,giày,rada
            //td -> nm -> xd
            {{0, 33, 3, 34, 136, 137, 138, 139, 230, 231, 232, 233, 555}, {6, 35, 9, 36, 140, 141, 142, 143, 242, 243, 244, 245, 556}, {21, 24, 37, 38, 144, 145, 146, 147, 254, 255, 256, 257, 562}, {27, 30, 39, 40, 148, 149, 150, 151, 266, 267, 268, 269, 563}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
            {{1, 41, 4, 42, 152, 153, 154, 155, 234, 235, 236, 237, 557}, {7, 43, 10, 44, 156, 157, 158, 159, 246, 247, 248, 249, 558}, {22, 46, 25, 45, 160, 161, 162, 163, 258, 259, 260, 261, 564}, {28, 47, 31, 48, 164, 165, 166, 167, 270, 271, 272, 273, 565}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}},
            {{2, 49, 5, 50, 168, 169, 170, 171, 238, 239, 240, 241, 559}, {8, 51, 11, 52, 172, 173, 174, 175, 250, 251, 252, 253, 560}, {23, 53, 26, 54, 176, 177, 178, 179, 262, 263, 264, 265, 566}, {29, 55, 32, 56, 180, 181, 182, 183, 274, 275, 276, 277, 567}, {12, 57, 58, 59, 184, 185, 186, 187, 278, 279, 280, 281, 561}}
    };

   public void UseCard(Player pl, Item item){
        RadarCard radarTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(c -> c.Id == item.template.id).findFirst().orElse(null);
        if(radarTemplate == null) return;
        if (radarTemplate.Require != -1){
            RadarCard radarRequireTemplate = RadarService.gI().RADAR_TEMPLATE.stream().filter(r -> r.Id == radarTemplate.Require).findFirst().orElse(null); 
            if(radarRequireTemplate == null) return;
            Card cardRequire = pl.Cards.stream().filter(r -> r.Id == radarRequireTemplate.Id).findFirst().orElse(null);  
            if (cardRequire == null || cardRequire.Level < radarTemplate.RequireLevel){
                Service.gI().sendThongBao(pl,"Bạn cần sưu tầm "+radarRequireTemplate.Name+" ở cấp độ "+radarTemplate.RequireLevel+" mới có thể sử dụng thẻ này");
                return;
            }
        }
        Card card = pl.Cards.stream().filter(r->r.Id == item.template.id).findFirst().orElse(null);
        if (card == null){
            Card newCard = new Card(item.template.id,(byte)1,radarTemplate.Max,(byte)-1,radarTemplate.Options);
            if (pl.Cards.add(newCard)){
                RadarService.gI().RadarSetAmount(pl,newCard.Id, newCard.Amount, newCard.MaxAmount);
                RadarService.gI().RadarSetLevel(pl,newCard.Id, newCard.Level);
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        }else{
            if (card.Level >= 2){
                Service.gI().sendThongBao(pl,"Thẻ này đã đạt cấp tối đa");
                return;
            }
            card.Amount++;
            if (card.Amount >= card.MaxAmount){
                card.Amount = 0;
                if (card.Level == -1){
                    card.Level = 1;
                }else {
                    card.Level++;
                }
                Service.gI().point(pl);
            }
            RadarService.gI().RadarSetAmount(pl,card.Id, card.Amount, card.MaxAmount);
            RadarService.gI().RadarSetLevel(pl,card.Id, card.Level);
            InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            InventoryServiceNew.gI().sendItemBags(pl);
        }
    }
   public void ChangeSkill2_3(Player player , Item item ){
       if(player.pet == null ){
           Service.gI().sendThongBao(player, "Mày làm gì có đệ ?");
       }
       if(player.pet.playerSkill.skills.get(1).skillId != -1 && player.pet.playerSkill.skills.get(2).skillId != -1){
           player.pet.openSkill2();
           player.pet.openSkill3();
           InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, item, 1);
           InventoryServiceNew.gI().sendItemBags(player);
           Service.gI().sendThongBao(player,"Đổi chiêu đệ thành công");
   }
       else{
           Service.gI().sendThongBao(player,"Đéo có chiêu thằng ngu");
   
       }}       
   public void changeskill4(Player player , Item item ){
       if(player.pet == null ){
           Service.gI().sendThongBao(player, "Mày làm gì có đệ ?");
       }
       if(player.pet.playerSkill.skills.get(3).skillId != -1 && player.pet.playerSkill.skills.get(2).skillId != -1){
           player.pet.openSkill4();
           InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, item, 1);
           InventoryServiceNew.gI().sendItemBags(player);
           Service.gI().sendThongBao(player,"Đổi chiêu đệ thành công");
   }
       else{
           Service.gI().sendThongBao(player,"Đéo có chiêu thằng ngu");
           
      }}
   
   private void useItemTimenrd(Player pl, Item item) {
        switch (item.template.id) {
            case 1828: //bánh pudding
                pl.itemTime.lastTime1828 = System.currentTimeMillis();
                pl.itemTime.is1828 = true;
                break;    
            case 1827: //bánh pudding
                pl.itemTime.lastTime1827 = System.currentTimeMillis();
                pl.itemTime.is1827 = true;
                break;  
            case 1822: //bánh pudding
               pl.itemTime.lastTime1822 = System.currentTimeMillis();
                pl.itemTime.is1822 = true;
                break;  
            case 1823: //bánh pudding
               pl.itemTime.lastTime1823 = System.currentTimeMillis();
                pl.itemTime.is1823 = true;
                break;  
            case 1824: //bánh pudding
                pl.itemTime.lastTime1824 = System.currentTimeMillis();
                pl.itemTime.is1824 = true;
                break;  
            case 1825: //bánh pudding
                pl.itemTime.lastTime1822 = System.currentTimeMillis();
                pl.itemTime.is1825 = true;
                break;  
            case 1826: //bánh pudding
                pl.itemTime.lastTime1826 = System.currentTimeMillis();
                pl.itemTime.is1826 = true;
                break;  
            case 1829: //bánh pudding
                pl.itemTime.lastTime1829 = System.currentTimeMillis();
                pl.itemTime.is1829 = true;
                pl.itemTime.is1830 = false;
                pl.itemTime.is1831 = false;
                pl.itemTime.is1832 = false;
                break;  
            case 1830: //bánh pudding
                pl.itemTime.lastTime1830 = System.currentTimeMillis();
                pl.itemTime.is1830 = true;
                pl.itemTime.is1829 = false;
                pl.itemTime.is1831 = false;
                pl.itemTime.is1832 = false;
                break;  
            case 1831: //bánh pudding
                pl.itemTime.lastTime1830 = System.currentTimeMillis();
                pl.itemTime.is1831 = true;
                pl.itemTime.is1829 = false;
                pl.itemTime.is1830 = false;
                pl.itemTime.is1832 = false;
                break;  
            case 1832: //bánh pudding
                pl.itemTime.lastTime1832 = System.currentTimeMillis();
                pl.itemTime.is1832 = true;
                pl.itemTime.is1830 = false;
                pl.itemTime.is1831 = false;
                pl.itemTime.is1829 = false;
                break;      
        }
        Service.getInstance().sendThongBao(pl, "Đã kích hoạt ");
        Service.getInstance().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);    
        InventoryServiceNew.gI().sendItemBags(pl);
    }
   
   private void useItemTimenrd2(Player pl, Item item) {
        switch (item.template.id) {
             
            case 1829: //bánh pudding
                pl.itemTime.lastTime1829 = System.currentTimeMillis();
                pl.itemTime.is1829 = true;
                pl.itemTime.is1830 = false;
                pl.itemTime.is1831 = false;
                pl.itemTime.is1832 = false;
                break;  
            case 1830: //bánh pudding
                pl.itemTime.lastTime1830 = System.currentTimeMillis();
                pl.itemTime.is1830 = true;
                pl.itemTime.is1829 = false;
                pl.itemTime.is1831 = false;
                pl.itemTime.is1832 = false;
                break;  
            case 1831: //bánh pudding
                pl.itemTime.lastTime1830 = System.currentTimeMillis();
                pl.itemTime.is1831 = true;
                pl.itemTime.is1829 = false;
                pl.itemTime.is1830 = false;
                pl.itemTime.is1832 = false;
                break;  
            case 1832: //bánh pudding
                pl.itemTime.lastTime1832 = System.currentTimeMillis();
                pl.itemTime.is1832 = true;
                pl.itemTime.is1830 = false;
                pl.itemTime.is1831 = false;
                pl.itemTime.is1829 = false;
                break;      
        }
        Service.getInstance().sendThongBao(pl, "Đã kích hoạt ");
        Service.getInstance().point(pl);
        ItemTimeService.gI().sendAllItemTime(pl);    
        InventoryServiceNew.gI().sendItemBags(pl);
    }//ao
   //qua nap moc
  
   private void hopao(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdluw = new int[]{555,557,559};
    int randomluw = new Random().nextInt(rdluw.length);
    Item luw = ItemService.gI().createNewItem((short) rdluw[randomluw]);
        if (id <= 90){           
         luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(47,Util.nextInt(2000,5500)));
                  luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,20)));
//                   luw.itemOptions.add(new Item.ItemOption(93,  Util.nextInt(3,5)));
        }
       InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            
        InventoryServiceNew.gI().addItemBag(pl, luw);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + luw.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
   private void hopquan(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdluw = new int[]{556,558,560};
    int randomluw = new Random().nextInt(rdluw.length);
    Item luw = ItemService.gI().createNewItem((short) rdluw[randomluw]);
        if (id <= 90){           
         luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(6,Util.nextInt(80000,200000)));
                     luw.itemOptions.add(new Item.ItemOption(77,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
        }
       InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            
        InventoryServiceNew.gI().addItemBag(pl, luw);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + luw.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
   private void hopgang(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdluw = new int[]{562,564,566};
    int randomluw = new Random().nextInt(rdluw.length);
    Item luw = ItemService.gI().createNewItem((short) rdluw[randomluw]);
        if (id <= 90){           
             luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(0,Util.nextInt(3000,12000)));
                     luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,50)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
        }
       InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            
        InventoryServiceNew.gI().addItemBag(pl, luw);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + luw.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
   private void hopgiay(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdluw = new int[]{563,565,567};
    int randomluw = new Random().nextInt(rdluw.length);
    Item luw = ItemService.gI().createNewItem((short) rdluw[randomluw]);
        if (id <= 90){           
           luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
               luw.itemOptions.add(new Item.ItemOption(7,Util.nextInt(120000,220000)));
                     luw.itemOptions.add(new Item.ItemOption(103,Util.nextInt(15,40)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(5,25)));
        }
       InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
            
        InventoryServiceNew.gI().addItemBag(pl, luw);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + luw.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void hopnhan(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdluw = new int[]{561};
    int randomluw = new Random().nextInt(rdluw.length);
    Item luw = ItemService.gI().createNewItem((short) rdluw[randomluw]);
        if (id <= 90){           
           luw.itemOptions.add(new Item.ItemOption(72,Util.nextInt(1,12)));
                  luw.itemOptions.add(new Item.ItemOption(14,Util.nextInt(10,20)));
                  luw.itemOptions.add(new Item.ItemOption(50,Util.nextInt(15,35)));
                  luw.itemOptions.add(new Item.ItemOption(21,80));
                  luw.itemOptions.add(new Item.ItemOption(107,Util.nextInt(10,25)));
        }
       InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
       InventoryServiceNew.gI().addItemBag(pl, luw);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + luw.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void randomcthit(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdct = new int[]{884};
    int randomct = new Random().nextInt(rdct.length);
    Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
        if (id <= 90){           
            ct.itemOptions.add(new Item.ItemOption(50, Util.nextInt(1, 10)));                 
            ct.itemOptions.add(new Item.ItemOption(5, Util.nextInt(40, 90)));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, ct);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
        private void randomcthp(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdct = new int[]{635};
    int randomct = new Random().nextInt(rdct.length);
    Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
        if (id <= 90){           
            ct.itemOptions.add(new Item.ItemOption(77, Util.nextInt(30, 80)));                 
            ct.itemOptions.add(new Item.ItemOption(103, Util.nextInt(30, 80)));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, ct);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
        }        
        private void randomcode(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = Util.nextInt(0, 100);
    int[] rdct = new int[]{463};
    int randomct = new Random().nextInt(rdct.length);
    Item ct = ItemService.gI().createNewItem((short) rdct[randomct]);
        if (id <= 90){           
            ct.itemOptions.add(new Item.ItemOption(50, 50));
            ct.itemOptions.add(new Item.ItemOption(77, 50));                 
            ct.itemOptions.add(new Item.ItemOption(103, 50));
            ct.itemOptions.add(new Item.ItemOption(93, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, ct);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + ct.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
        }//hop mo do vip by 
    private void AHDtd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{650};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(2400, 2880)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void QHDtd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{651};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(110, 132)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void GHDtd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{657};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(8400, 10600)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void JHDtd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{658};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(100, 120)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void NHD(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{656};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(14, Util.nextInt(20, 22)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void AHDnm(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{652};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(2400, 2880)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void QHDnm(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{653};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(100, 120)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void GHDnm(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{659};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(8200, 10000)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void JHDnm(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{660};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(120, 150)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void AHDxd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{654};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(47, Util.nextInt(2400, 2880)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void QHDxd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{655};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(22, Util.nextInt(120, 150)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void GHDxd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{661};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(0, Util.nextInt(9200, 11100)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
    private void JHDxd(Player pl, Item item) {
            if (InventoryServiceNew.gI().getCountEmptyBag(pl) > 0) {
    int id = (100);
    int[] rdhd = new int[]{662};
    int randomhd = new Random().nextInt(rdhd.length);
    Item hd = ItemService.gI().createNewItem((short) rdhd[randomhd]);
        if (id == 100){           
        hd.itemOptions.add(new Item.ItemOption(23, Util.nextInt(100, 120)));                 
        hd.itemOptions.add(new Item.ItemOption(21, 60));
        hd.itemOptions.add(new Item.ItemOption(30, 1));
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, item, 1);
        InventoryServiceNew.gI().addItemBag(pl, hd);
        InventoryServiceNew.gI().sendItemBags(pl);
        Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + hd.template.name);
    } else {
        Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống trong hành trang.");
            }
    }
}



