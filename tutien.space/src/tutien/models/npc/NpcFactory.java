package barcoll.models.npc;

import barcoll.services.Service;
import barcoll.services.TaskService;
import barcoll.services.ClanService;
import barcoll.services.InventoryServiceNew;
import barcoll.services.OpenPowerService;
import barcoll.services.NgocRongNamecService;
import barcoll.services.ItemService;
import barcoll.services.PlayerService;
import barcoll.services.NpcService;
import barcoll.services.MapService;
import barcoll.services.FriendAndEnemyService;
import barcoll.services.SkillService;
import barcoll.services.IntrinsicService;
import barcoll.services.ItemMapService;
import barcoll.services.PetService;
import barcoll.consts.ConstMap;

import java.util.Calendar;
import java.util.TimeZone;
import barcoll.consts.ConstNpc;
import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstTask;
import com.girlkun.database.GirlkunDB;
import tutien.jdbc.daos.PlayerDAO;
import tutien.kygui.ShopKyGuiService;
import Amodels.boss.Boss;
import Amodels.boss.BossData;
import Amodels.boss.BossID;
import Amodels.boss.BossManager;
import barcoll.models.boss.list_boss.NhanBan;
import tutien.models.clan.Clan;
import tutien.models.clan.ClanMember;
import barcoll.models.boss.list_boss.DuongTank;
import java.util.HashMap;
import java.util.List;
import tutien.services.func.ChangeMapService;
import tutien.services.func.SummonDragon;
import static tutien.services.func.SummonDragon.SHENRON_1_STAR_WISHES_1;
import static tutien.services.func.SummonDragon.SHENRON_1_STAR_WISHES_2;
import static tutien.services.func.SummonDragon.SHENRON_SAY;
import tutien.models.player.Player;
import tutien.models.item.Item;
import barcoll.models.map.Map;
import barcoll.models.map.Zone;
import tutien.models.map.blackball.BlackBallWar;
import tutien.models.map.MapMaBu.MapMaBu;
import tutien.models.map.DaiHoiVoThuat23.MartialCongressService;
import tutien.models.map.DoanhTraiDocNhan.DoanhTrai;
import tutien.models.map.DoanhTraiDocNhan.DoanhTraiService;
import tutien.models.map.BanDoKhoBau.BanDoKhoBau;
import tutien.models.map.BanDoKhoBau.BanDoKhoBauService;
import tutien.models.map.ConDuongRanDoc.ConDuongRanDoc;
import tutien.models.map.ConDuongRanDoc.ConDuongRanDocService;
import tutien.models.map.KhiGasHuyDiet.KhiGasHuyDiet;
import tutien.models.map.KhiGasHuyDiet.KhiGasHuyDietService;
import tutien.models.player.NPoint;
import barcoll.models.matches.PVPService;
import tutien.models.matches.pvp.DaiHoiVoThuat;
import tutien.models.matches.pvp.DaiHoiVoThuatService;
import tutien.models.map.GiaiCuuMiNuong.GiaiCuuMiNuongService;
import tutien.models.shop.ShopServiceNew;
import tutien.models.skill.Skill;
import barcoll.server.Client;
import barcoll.server.Maintenance;
import barcoll.server.Manager;
import tutien.services.func.CombineServiceNew;
import tutien.services.func.Input;
import tutien.services.func.LuckyRound;
import tutien.utils.Logger;
import tutien.utils.TimeUtil;
import tutien.utils.Util;
import java.util.ArrayList;
import tutien.services.func.ChonAiDay;
import MaQuaTang.MaQuaTangManager;
import tutien.models.item.Item.ItemOption;
import tutien.models.map.Mapchichi.Map22h;
import tutien.models.map.Mapmabu2h.mabu2h;
import tutien.models.npc.special.DuaHau;
import barcoll.server.ServerNotify;
import barcoll.services.ChatGlobalService;
import tutien.services.func.TaiXiu;
import tutien.services.func.TopService;
import tutien.utils.SkillUtil;
import com.girlkun.network.io.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class NpcFactory {

    private static final int COST_HD = 50000000;
    public static long timebahatmit;
    private static boolean nhanVang = false;
    private static boolean nhanDeTu = false;

    //playerid - object
    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    private static Npc onggianoel(int mapId, int status, int cx, int cy, int tempId, int avatar) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    /////////////////////////////////=Trọng Tài=////////////////////////////////////////////////////////
    private static Npc TrongTai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.zone.map.mapId == 13) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại chiến bang hội toàn vũ trụ đang được diễn ra\n"
                                + "Ngươi muốn ta giúp gì?", "Tham gia", "Cửa hàng\nHồng ngọc", "Hướng\ndẫn");
                        return;
                    }
                    this.createOtherMenu(player, 99, "Ngươi vừa vào chiến trường lại muốn chạy trốn rồi cơ à?", "Về\nĐảo guru");
                    return;
                }
            }
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            int Gio = calendar.get(Calendar.HOUR_OF_DAY);

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && player.zone.map.mapId == 13) {
                        switch (select) {

                            case 0:

                                if (Gio >= 7 && Gio <= 8) {
                                    if (player.ThamGiaDaiChienBangHoi == 0) {
                                        this.createOtherMenu(player, 251, "Mỗi ngày ngươi có một lượt tham gia miễn phí\n"
                                                + "Ngươi sẽ có 15 phút để tham gia, ngươi đã sẵn sàng?", "Tham gia\n(Miễn phí)", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, 252, "Ngươi có muốn trả phí để tiếp tục tham gia không?", "Tham gia\n(" + player.ThamGiaDaiChienBangHoi * 20 + "Tr vàng)", "Từ chối");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Đại chiến bang hội mở vào lúc 19h - 20h hằng ngày");
                                }
                                return;
                            case 1:
                                ShopServiceNew.gI().opendShop(player, "VAT_PHAM", true);
                                break;
                            
                            case 2:
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.DAI_CHIEN_BANG_HOI);
                            case 3:
                                if (Gio >= 19 && Gio < 20) {
                                    System.out.println("Thời gian hiện tại nằm trong khoảng từ 19h giờ sáng đến 20h giờ tối.");
                                } else {
                                    System.out.println("Thời gian hiện tại không nằm trong khoảng từ 19h giờ sáng đến 20 giờ tối.");
                                }
                        }
                    }
                    if (player.iDMark.getIndexMenu() == 99) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 13, -1, 312);
                        }
                    }
                    if (player.iDMark.getIndexMenu() == 251) {
                        int vangconthieu;
                        vangconthieu = (int) (player.inventory.gold - (player.ThamGiaDaiChienBangHoi * 20_000_000));
                        switch (select) {
                            case 0:
                                if (player.inventory.gold >= (player.ThamGiaDaiChienBangHoi * 20_000_000)) {
                                    player.inventory.gold -= player.ThamGiaDaiChienBangHoi * 20_000_000;
                                    Service.gI().changeFlag(player, 8);
                                    player.HoiSinhDaiChienBangHoi = 5;
                                    Service.gI().sendMoney(player);
                                    player.ThamGiaDaiChienBangHoi++;
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 178, -1, 312);
                                    return;
                                } else {
                                    Service.gI().sendThongBaoOK(player, "Bạn còn thiếu " + vangconthieu + " vàng");
                                }
                        }
                    }
                    if (player.iDMark.getIndexMenu() == 252) {
                        switch (select) {
                            case 0:
                                Service.gI().changeFlag(player, 8);
                                player.HoiSinhDaiChienBangHoi = 5;
                                player.inventory.gold -= player.ThamGiaDaiChienBangHoi * 20_000_000;
                                player.ThamGiaDaiChienBangHoi++;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 178, -1, 312);
                                return;
                        }
                    }
                }
            }
        };
        
    }
    
    public static Npc duahau(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.duahau != null) {
                        player.duahau.sendduahau();
                        if (player.duahau.getSecondDone() != 0) {
                            this.createOtherMenu(player, ConstNpc.NOT_NHAN_DUA, "Dưa ơi dưa à dưa mau lớn để ta thu.",
                                    "Dưa chưa chín chờ đi", "Đóng");
                        } else {
                            this.createOtherMenu(player, ConstNpc.CAN_THU_HOACH_DUA, "Dưa ơi dưa à dưa mau lớn để ta thu", "Thu Hoạch", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.NOT_NHAN_DUA:
                            Service.getInstance().sendThongBao(player, "Chờ đi tham gì chời");
                            break;
                        case ConstNpc.CAN_THU_HOACH_DUA:
                            switch (select) {
                                case 0:
                                    Item duahau = ItemService.gI().createNewItem((short) 569, 1);
                                    InventoryServiceNew.gI().addItemBag(player, duahau);
                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận được dưa hấu");
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    DuaHau.createduahau(player);
                                    break;
                            }
                            break;
                    }
                }
            }
        };
    }
    public static Npc hungvuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Giỗ tổ hùng vương là 1 ngày lễ của việt nam ta con muốn gì nào:" +
                            "\n|7| Để Gộp Tre Trăm Đốt Cần 1K khung tre + 300tr vàng" +
                            "\n|3| Cần 50 điểm cho 1 lần đổi", "Gộp Tre Trăm Đốt", "Đổi điểm đánh sơn tinh", "Đổi điểm đánh thủy tinh", "Đổi dưa hấu", "Xem Số Điểm sự kiện", "Đóng");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                            //    if (Manager.SUKIEN == 2) {
                                    if (player.inventory.gold >= 300000000L) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item khungtre = InventoryServiceNew.gI().findnlskgiotohungvuong(player);
                                            if (khungtre != null && khungtre.quantity >= 1000) {
                                                Item caytretramdot = ItemService.gI().createNewItem((short) 1190, 1);

                                                // - Số item sự kiện có trong rương
                                                InventoryServiceNew.gI().subQuantityItemsBag(player, khungtre, 1000);

                                                khungtre.itemOptions.add(new Item.ItemOption(73, 0));
                                                player.inventory.gold -= 300000000L;
                                                InventoryServiceNew.gI().addItemBag(player, caytretramdot);
                                                InventoryServiceNew.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Khắc nhập khắc nhập");
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không đủ khung tre");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ 300tr vàng");
                                    }
                               // } else {
                            //        Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                            //    }
                                break;
                            case 1:
                            //    if (Manager.SUKIEN == 2) {
                                 
                             //   } else {
                             //       Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                             //   }
                                break;
//                            case 2:
//                              //  if (Manager.SUKIEN == 2) {
//                                    if (player.diemthuytinh >= 50) {
//                                        Item thuytinh = ItemService.gI().createItemSetKichHoat(422, 1);
//                                        thuytinh.itemOptions.add(new Item.ItemOption(50, Util.nextInt(10, 50)));
//                                        thuytinh.itemOptions.add(new Item.ItemOption(77, Util.nextInt(10, 50)));
//                                        thuytinh.itemOptions.add(new Item.ItemOption(103, Util.nextInt(10, 150)));
//                                        if (Util.isTrue(199, 200)) {
//                                            thuytinh.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1, 15)));
//                                        }
//                                        InventoryServiceNew.gI().addItemBag(player, thuytinh);
//                                        InventoryServiceNew.gI().sendItemBags(player);
//                                        player.diemthuytinh -= 50;
//                                        Service.getInstance().sendThongBao(player, "\n|5|Chúc mừng bạn nhận được cải trang thành thủy tinh");
//                                        break;
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Không đủ 50 điểm");
//                                    }
                            //    } else {
                            //        Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                            //    }
//                                break;
                            case 3:
                           //     if (Manager.SUKIEN == 2) {
                                    this.createOtherMenu(player, ConstNpc.MENUSUKIENGIOTO, "Con muốn đổi dưa hấu hả", "1 quả 10 thỏi vàng", "5 quả 70 thỏi vàng", "10 quả 200 thỏi vàng", "15 quả 350 thỏi vàng", "20 quả 500 thỏi vàng", "25 quả 700 thỏi vàng", "Đóng");
                          //      } else {
                           //         Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
                           //     }
                                break;
//                            case 4:
//                             //   if (Manager.SUKIEN == 2) {
//                                    Service.getInstance().sendThongBaoOK(player, "Bạn đã tiêu diệt số sơn tinh là :" + player.diemsontinh + "\nBạn đã tiêu diệt số thủy tinh là :" + player.diemthuytinh);
//                            //    } else {
//                            //        Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc");
//                            //    }
//                                break;
                       }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENUSUKIENGIOTO) {
                     //   if (Manager.SUKIEN == 2) {
                            switch (select) {
                                case 0:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 1) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 10);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 1);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 5) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 70);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 5);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 10) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 200);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 10);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 3:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 15) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 350);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 15);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 4:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 20) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 500);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 20);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                                case 5:
                                    if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                        Item duahau = InventoryServiceNew.gI().findduahaugioto(player);
                                        if (duahau != null && duahau.quantity >= 25) {
                                            Item thoivang = ItemService.gI().createNewItem((short) 457, 700);

                                            // - Số item sự kiện có trong rương
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, duahau, 25);

                                            thoivang.itemOptions.add(new Item.ItemOption(73, 0));
                                            InventoryServiceNew.gI().addItemBag(player, thoivang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Đổi thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ dưa");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                    }
                                    break;
                            }
                    //    } else {
                   //        Service.getInstance().sendThongBaoOK(player, "Sự kiện đã kết thúc");
                    //    }
                    }
                }
            }
        };
    }

    public static Npc GhiDanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            String[] menuselect = new String[]{};

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 52) {
                        createOtherMenu(pl, 0, DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Giai(pl), "Thông tin\nChi tiết", DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(pl) ? "Đăng ký" : "OK", "Đại Hội\nVõ Thuật\nLần thứ\n23");
                    } else if (this.mapId == 129) {
                        int goldchallenge = pl.goldChallenge;
                        int goldchallenge1 = pl.gemChallenge;
                        if (pl.levelWoodChest == 0) {
                            menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Về\nĐại Hội\nVõ Thuật"};
                        } else {
                            menuselect = new String[]{"Hướng\ndẫn\nthêm", "Thi đấu\n" + Util.numberToMoney(goldchallenge) + " vàng", "Nhận thưởng\nRương cấp\n" + pl.levelWoodChest, "Về\nĐại Hội\nVõ Thuật"};
                        }
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU, "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm, ngày nghỉ, ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào", menuselect, "Từ chối");

                    } else {
                        super.openBaseMenu(pl);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 52) {
                        switch (select) {
                            case 0:
                                Service.gI().sendPopUpMultiLine(player, tempId, avartar, DaiHoiVoThuat.gI().Info());
                                break;
                            case 1:
                                if (DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).CanReg(player)) {
                                    DaiHoiVoThuatService.gI(DaiHoiVoThuat.gI().getDaiHoiNow()).Reg(player);
                                }
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapNonSpaceship(player, 129, player.location.x, 360);
                                break;
                        }
                    } else if (this.mapId == 129) {
                        int goldchallenge = player.goldChallenge;
                        int goldchallenge1 = player.gemChallenge;
                        if (player.levelWoodChest == 0) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 50000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
//                                case 2:
//                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
//                                        if (player.inventory.gem >= goldchallenge1) {
//                                            MartialCongressService.gI().startChallenge(player);
//                                            player.inventory.gem -= (goldchallenge1);
//                                            PlayerService.gI().sendInfoHpMpMoney(player);
//                                            player.gemChallenge += 10000;
//                                        } else {
//                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
//                                        }
//                                    } else {
//                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
//                                    }
//                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.NPC_DHVT23);
                                    break;
                                case 1:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gold >= goldchallenge) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gold -= (goldchallenge);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.goldChallenge += 50000000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 2:
                                    if (InventoryServiceNew.gI().finditemWoodChest(player)) {
                                        if (player.inventory.gem >= goldchallenge1) {
                                            MartialCongressService.gI().startChallenge(player);
                                            player.inventory.gem -= (goldchallenge1);
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            player.gemChallenge += 10000;
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu " + Util.numberToMoney(goldchallenge - player.inventory.gold) + " vàng");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Hãy mở rương báu vật trước");
                                    }
                                    break;
                                case 3:
                                    if (!player.receivedWoodChest) {
                                        if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            Item it = ItemService.gI().createNewItem((short) 570);
                                            it.itemOptions.add(new Item.ItemOption(72, player.levelWoodChest));
                                            it.itemOptions.add(new Item.ItemOption(30, 0));
                                            it.createTime = System.currentTimeMillis();
                                            InventoryServiceNew.gI().addItemBag(player, it);
                                            InventoryServiceNew.gI().sendItemBags(player);

                                            player.receivedWoodChest = true;
                                            player.levelWoodChest = 0;
                                            Service.getInstance().sendThongBao(player, "Bạn nhận được rương gỗ");
                                        } else {
                                            this.npcChat(player, "Hành trang đã đầy");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                    }
                                    break;
                                case 4:
                                    ChangeMapService.gI().changeMapNonSpaceship(player, 52, player.location.x, 336);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }


    private NpcFactory() {

    }
    
    private static Npc trungLinhThu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng Linh thú cần:\b|7|X99 Hồn Linh Thú + 2 Tỷ vàng", "Đổi Trứng\nLinh thú", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 2029);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 Hồn Linh thú");
                                    } else if (player.inventory.gold < 2_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 2_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2028);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Trứng Linh thú");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }
        public static Npc unkonw(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 0,
                                "Éc éc Bạn muốn gì ở tôi :3?", "Đến Võ đài Unknow", "Võ Đài Siêu Cấp");
                                
                    }
                    if (this.mapId == 112) {
                        this.createOtherMenu(player, 0,
                                "Bạn đang còn : " + player.pointPvp + " điểm PvP Point", "Về đảo Kame", "Đổi Cải trang sự kiên", "Top PVP");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 112, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                                case 1:             
                                    if (player.getSession().player.nPoint.power >= 10000000000L) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, 145, -1, 495);
                                        Service.gI().changeFlag(player, Util.nextInt(8));
                                    } else {
                                        this.npcChat(player, "Bạn cần 10 tỷ sức mạnh mới có thể vào");
                                    }
                                    break; // qua vo dai
                                    
                            }
                        }
                    }

                    if (this.mapId == 112) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 319);
                                    break; // ve dao kame
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất cả chỉ số là 80%\n ", "Ok", "Không");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
                                    Service.gI().showListTop(player, Manager.topPVP);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1104));
                                        item.itemOptions.add(new Item.ItemOption(50, 10));
                                        item.itemOptions.add(new Item.ItemOption(77, 10));
                                        item.itemOptions.add(new Item.ItemOption(103, 10));
                                        item.itemOptions.add(new Item.ItemOption(207, 1));
                                        item.itemOptions.add(new Item.ItemOption(33, 1));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
        
        public static Npc usop(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
                public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                         this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                       "|1|Cảnh giới: "
                                                    + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "\n|7|EXP Tu Tiên: " + Util.getFormatNumber(player.Tamkjlltutien[0])
                                                    + "\n|7|Tu Vi: " + player.ExpTamkjll + ""
                                                    + "\n|4|VND: " + player.getSession().vnd ,
                                      "Thông Tin", 
                                      "Đột Phá",
                                      "Tăng thiên phú",//luudeptrai ctrl+c + ctrl+v
                                    "Xem Cảnh Giới\n"
                                            + "Chỉ Số");   }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
             if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                           case 0:
                                   this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|Các Cấp Độ Trong Tu Tiên:"
                                                + "\n|1|Ngưng Khí,Trúc Cơ,Kim Đan,Nguyên Anh (Kỳ)"
                                                + "\n|1|Hóa Thần,Kết Đan (Đỉnh Phong),Luyện Hư,Hợp Thể Kỳ"
                                                + "\n|1|Thiên Nhân(1>3),Bán Thần...Lười Kể"
                                                + "\n|1|Thiên Tôn,Thái tổ Vô Địch,Chúa Tể Đỉnh Cao,Vĩnh hằng hoàn mỹ"
                                                + "\n|1|Hiện Tại Có 26 Cấp Xem Chi Tiết Trong Nhóm Zalo",
                                          "Thông Tin", "Đột Phá",
                                            "Mở thiên phú", "Xem Cảnh Giới");
                                case 1:
                                    if (player.Tamkjlltutien[2] < 1) {
                                        Service.gI().sendThongBao(player, "Con chưa mở thiên phú.");
                                        return;
                                    }
                                    String dktt;
                                    if (player.Tamkjlltutien[1] < 26) {
                                        dktt = "Điều kiện tấn thăng lên "
                                                + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1]) + 1);
                                        dktt += "\nExp tu tiên cần: "
                                                + player.TamkjllDieukiencanhgioi(
                                                        Util.TamkjllGH(player.Tamkjlltutien[1]));
                                        dktt += "\nTỉ lệ thành công: "
                                                + player.Tamkjlltilecanhgioi(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                + "%";
                                    } else {
                                        dktt = "XIN CHÀO CHỦ NHÂN THIÊN GIỚI";
                                    }
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|1|Cảnh giới: "
                                                    + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "\n|6|EXP Tu Tiên: " + Util.getFormatNumber(player.Tamkjlltutien[0])
                                                    + "\n|6|Thiên phú: " + player.Tamkjlltutien[2] + " Sao"
                                                    + "\n|8|" + dktt
                                                    + "\n|7|Lưu Ý :Khi Đủ EXP Tu Tiên Hệ Thống Sẽ Tự Động Đột Phá Giúp Bạn Tăng Cảnh Giới,Thiên Phú Càng Nhiều Tỉ Lệ Càng Cao(Thất Bại Sẽ Die)",
                                            "về nhà", "Xem thông tin\ntu tiên",
                                            "Tẩy thiên phú", "Xem Cảnh Giới");
                                    break;
                                case 2:           
                                    if (player.Tamkjlltutien[2] < 1) {
                                        if (player.ExpTamkjll < 5000000) {
                                            Service.gI().sendThongBao(player, "cần ít nhất 5M Tu Vi");
                                            return;
                                        }
                                        player.ExpTamkjll -= 5000000;
                                        int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 8)));
                                        player.Tamkjlltutien[2] = tp;
                                        Service.gI().sendThongBao(player,
                                                "Chúc mừng con con mở đc thiên phú:\n" + tp + " sao");
                                 } else {
                                       
                                        if (player.ExpTamkjll < 5000000) {
                                            Service.gI().sendThongBao(player, "cần ít nhất 5M Tu Vi");
                                            return;
                                        }
                                        if (player.Tamkjlltutien[2] >= 50) {
                                            Service.gI().sendThongBao(player, "Con đã là tuyệt thế thiên tài");
                                            return;
                                        }
                                        player.ExpTamkjll -= 5000000;
                                        if (Util.isTrue(70f, 100)) {
                                            player.Tamkjlltutien[2]++;
                                            Service.gI().sendThongBao(player,
                                                    "Chúc mừng con đã tăng thiên phú thành công\ntừ: "
                                                            + (player.Tamkjlltutien[2] - 1) + " sao lên: "
                                                            + player.Tamkjlltutien[2] + " sao.");
                                        } else {
                                            Service.gI().sendThongBao(player, "Xin lỗi nhưng ta đã cố hết xức.");
                                        }
                                    }
                                    break;
                                case 3:
                                    if (player.Tamkjlltutien[2] < 1) {
                                        Service.gI().sendThongBao(player, "Con chưa mở thiên phú.");
                                        return;
                                    }
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|8|Cảnh giới: "
                                                    + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "\nHp: "
                                                    + player.TamkjllHpKiGiaptutien(
                                                            Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "%\nKi: "
                                                    + player.TamkjllHpKiGiaptutien(
                                                            Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "%\nGiáp: "
                                                    + player.TamkjllHpKiGiaptutien(
                                                            Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "%\nDame: "
                                                    + player.TamkjllDametutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "%\nHút HP,KI: "
                                                    + player.TamkjllDametutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    + "%",
                                             "Xem Thông Tin", "tu tiên",
                                            "thiên phú", "Xem Cảnh Giới");
                                    break;
                                  case 4:
                                                                       if (player.Tamkjlltutien[2] < 1) {
                                        if (player.ExpTamkjll < 35000000) {
                                            Service.gI().sendThongBao(player, "cần ít nhất 35M Tu Vi");
                                            return;
                                        }
                                        player.ExpTamkjll -= 35000000;
                                        int tp = Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 8)));
                                        player.Tamkjlltutien[2] = tp;
                                        Service.gI().sendThongBao(player,
                                                "Chúc mừng con con mở đc thiên phú:\n" + tp + " sao");
                                 } else {
                                       
                                        if (player.ExpTamkjll < 15000000) {
                                            Service.gI().sendThongBao(player, "cần ít nhất 15M Tu Vi");
                                            return;
                                        }
                                        if (player.Tamkjlltutien[2] >= 50) {
                                            Service.gI().sendThongBao(player, "Con đã là tuyệt thế thiên tài");
                                            return;
                                        }
                                        player.ExpTamkjll -= 15000000;
                                        if (Util.isTrue(30f, 100)) {
                                            player.Tamkjlltutien[2]++;
                                            Service.gI().sendThongBao(player,
                                                    "Chúc mừng con đã tăng thiên phú thành công\ntừ: "
                                                            + (player.Tamkjlltutien[2] - 1) + " sao lên: "
                                                            + player.Tamkjlltutien[2] + " sao.");
                                        } else {
                                            Service.gI().sendThongBao(player, 
                                             "Bạn đã thăng cảnh giới thất bại và bị mất Chân Khí, cảnh giới bạn vẫn ở: "
                                                + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1])));
                           
                                        }
                                    }
                                    break;
                                  
                            }
                        }
                    }
                }
            }
        };
    }
        
                public static Npc npcThienSu64(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 14) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 7) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 0) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ dẫn cậu tới hành tinh Berrus với điều kiện\n 2. đạt 80 tỷ sức mạnh "
                            + "\n 3. chi phí vào cổng  50 triệu vàng", "Tới ngay", "Từ chối");
                }
                if (this.mapId == 146) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 147) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 148) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu không chịu nổi khi ở đây sao?\nCậu sẽ khó mà mạnh lên được", "Trốn về", "Ở lại");
                }
                if (this.mapId == 48) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!", "Hướng Dẫn",
                            "Đổi SKH VIP", "Từ Chối");
                }
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!",
                            "Chế Tạo trang bị thần linh","Chế Tạo trang bị hủy diệt","Chế Tạo trang bị thiên sứ", "Shop\nchế tạo","Đóng");
                }
            }

            //if (player.inventory.gold < 500000000) {
//                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
//                return;
//            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 7) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 146, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 14) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 148, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 0) {
                        if (select == 0) {
                            if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                                player.inventory.gold -= COST_HD;
                                Service.gI().sendMoney(player);
                                ChangeMapService.gI().changeMapBySpaceShip(player, 147, -1, 168);
                            } else {
                                this.npcChat(player, "Bạn chưa đủ điều kiện để vào");
                            }
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 147) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 148) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 14, -1, 450);
                        }
                        if (select == 1) {
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 146) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 450);
                        }
                        if (select == 1) {
                        }

                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 48) {
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOI_SKH_VIP);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP);
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                    if (player.iDMark.isBaseMenu() && this.mapId == 5) {
                        if (select == 0) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TL);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_HD);
                        }
                        if (select == 2) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                        }
                        if (select == 3) {
                                    ShopServiceNew.gI().opendShop(player, "PHU_KIEN", false);                                                                      
                        } 

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                        if (select == 1) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                        if (select == 2) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                }
            }

        };
    }
       public static Npc Tapion(int mapId, int status, int cx, int cy, int tempId, int avartar) {
                   return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    Map22h.gI().setTimeJoinMap22h();} 
                            if (this.mapId == 19) {                                               
                                long now = System.currentTimeMillis();
                                if (now > Map22h.TIME_OPEN_22h && now < Map22h.TIME_CLOSE_22h) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB,   "phong ấn đã bị phá vỡ, "
                                            + "Xin hãy cứu lấy người dân",
                                           "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                    "Ác quỷ truyền thuyết Hirudegarn đã thoát khỏi phong ấn ngàn năm nHãy giúp tôi chế ngự nó?",
                                            "Hướng dẫn", "Từ chối");
                                }                           
                            }
                        }    
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                   switch (this.mapId) {
                        case 19:
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                            case ConstNpc.MENU_OPEN_MMB:
                                    if (select == 0)
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_chi22h);
                                    if (!player.getSession().actived) {                                   
                                    }
                                   if (select == 1){
                                        ChangeMapService.gI().changeMap(player, 212, 0, 66, 312);
                                    }
                                    break;
                                   case ConstNpc.MENU_NOT_OPEN_BDW:
                                    if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_2h);
                                }
                                break;
                               }
                          }
                      }
                  }    
              };
           }
    ///////////////////////////////////////////NPC Chopper///////////////////////////////////////////
    public static Npc chopper(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Khu vực này có ba chị em Hải tặc   khét tiếng hãy cẩn thận nhé cậu nhóc ....",
                                "Đi đến\nĐảo Kho Báu","Chi tiết", "Từ chối");
                    }
                    if (this.mapId == 203) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn quay về NHA VOI ME SAO",
                                "Đi thôi","Từ chối");
                    }
                }
            }
            
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player,203, -1, 1560);
                        break;
                            }
                        }
                    }
                    if (this.mapId == 203) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 312);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
     private static Npc fide(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Trứng con cua cần:\b|7|X1 Hổ Mập Vàng + 1 Tỷ vàng", "Đổi con\ncua", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 942);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ 1 con pet ho map");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 697);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 con cua");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc monaito(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        this.createOtherMenu(player, 0,
                                "Chào bạn tôi sẽ đưa bạn đến hành tinh Cereal?", "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 170) {
                        this.createOtherMenu(player, 0,
                                "Ta ở đây để đưa con về", "Về Làng Mori", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 7) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 264);
                                    break; // den hanh tinh cereal
                            }
                        }
                    }
                    if (this.mapId == 170) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 7, -1, 432);
                                    break; // quay ve

                            }
                        }
                    }
                }
            }
        };
    }
    
    private static Npc barcoll(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đổi Lấy Đá ma thuat:\b|7|X99 đá ngũ sắc + 1 Tỷ vàng", "Đổi da mag\nthuat", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item honLinhThu = null;
                                    try {
                                        honLinhThu = InventoryServiceNew.gI().findItemBag(player, 674);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (honLinhThu == null || honLinhThu.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 đá ngũ sắc");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, honLinhThu, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 2030);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Đá ma thuat");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }
       
    ///////////////////////////////////////////NPC Potage///////////////////////////////////////////
    private static Npc poTaGe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đa vũ trụ song song \b|7|Con muốn gọi con trong đa vũ trụ \b|1|Với giá 500tr vàng không?", "Gọi Boss\nNhân bản", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 140) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Boss oldBossClone = BossManager.gI().getBossById(Util.createIdBossClone((int) player.id));
                                    if (oldBossClone != null) {
                                        this.npcChat(player, "Nhà ngươi hãy tiêu diệt Boss lúc trước gọi ra đã, con boss đó đang ở khu " + oldBossClone.zone.zoneId);
                                    } else if (player.inventory.gold < 500_000_000) {
                                        this.npcChat(player, "Nhà ngươi không đủ 500 Triệu vàng ");
                                    } else {
                                        List<Skill> skillList = new ArrayList<>();
                                        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                            Skill skill = player.playerSkill.skills.get(i);
                                            if (skill.point > 0) {
                                                skillList.add(skill);
                                            }
                                        }
                                        int[][] skillTemp = new int[skillList.size()][3];
                                        for (byte i = 0; i < skillList.size(); i++) {
                                            Skill skill = skillList.get(i);
                                            if (skill.point > 0) {
                                                skillTemp[i][0] = skill.template.id;
                                                skillTemp[i][1] = skill.point;
                                                skillTemp[i][2] = skill.coolDown;
                                            }
                                        }
                                        BossData bossDataClone = new BossData(
                                                "Nhân Bản " + player.name,
                                                player.gender,
                                                new short[]{player.getHead(), player.getBody(), player.getLeg(), player.getFlagBag(), player.idAura, player.getEffFront()},
                                                (int) player.nPoint.hpMax/200,
                                                new long[]{(int)player.nPoint.hpMax},
                                                new int[]{140},
                                                skillTemp,
                                                new String[]{"|-2|Boss nhân bản đã xuất hiện rồi"}, //text chat 1
                                                new String[]{"|-1|Ta sẽ chiếm lấy thân xác của ngươi hahaha!"}, //text chat 2
                                                new String[]{"|-1|Lần khác ta sẽ xử đẹp ngươi"}, //text chat 3
                                                60
                                        );
                                        try {
                                            new NhanBan(Util.createIdBossClone((int) player.id), bossDataClone, player.zone);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //trừ vàng khi gọi boss
                                        player.inventory.gold -= 500_000_000;
                                        Service.gI().sendMoney(player);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }
        ///////////////////////////////////////////Tổ Sư Kaio///////////////////////////////////////////////
    public static Npc tosukaio(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn gì ở ta?",
                                "Luyện tập");
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                 case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.REN_KIEM_Z);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.REN_KIEM_Z:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
   ///////////////////////////////////////////NPC Quy Lão Kame///////////////////////////////////////////
    private static Npc quyLaoKame(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                    "Là lá la",
                    "La lá là",
                    "Lá là la",
                    "Tới đây nào baby",
                    "Em tuyệt lắm",
                    "Aaaaaaaaaaaa",
                    "Bắn rồi",
                    "Phẹt Phẹt!!!"
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;
                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 10000, 10000);
            }
            @Override
            public void openBaseMenu(Player player) {
                chatWithNpc(player);
                Item ruacon = InventoryServiceNew.gI().findItemBag(player, 874);
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (ruacon != null && ruacon.quantity >= 1) {
                            this.createOtherMenu(player, 12, "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                    "Giao\nRùa con", "Nói chuyện", "Bảng\nXếp");
                        } else {
                            this.createOtherMenu(player, 13, "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                     "Nói chuyện");
                        }
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == 12) {
                        switch (select) {
                            case 0:
                            this.createOtherMenu(player, 5,
                                    "Cảm ơn cậu đã cứu con rùa của ta\n Để cảm ơn ta sẽ tặng cậu món quà.",
                                    "Nhận quà","Đóng");
                            break;
                            case 1:
                                this.createOtherMenu(player, 6,
                                        "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                        "Chức Năng\nBang Hội","Kho Báu\ndưới biển", "Khiêu Chiến");
                                break;
                            case 2:
                                    Service.gI().showListTop(player, Manager.topSM);
                                break;
                                
                                
                                
                        }
                    } else if (player.iDMark.getIndexMenu() == 5) {
                        switch (select) {
                            case 0:
                                try {
                                    Item RuaCon = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 874);
                                    if (RuaCon != null) {
                                        if (RuaCon.quantity >= 1 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                            int randomItem = Util.nextInt(6); // Random giữa 0 và 1
                                            if (randomItem == 0) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 7));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z", "Ok");
                                            } else if (randomItem == 1) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z", "Ok");
                                            } else if (randomItem == 2) {
                                                Item VatPham = ItemService.gI().createNewItem((short) (865));
                                                VatPham.itemOptions.add(new Item.ItemOption(50, 20));
                                                VatPham.itemOptions.add(new Item.ItemOption(77, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(103, 10));
                                                VatPham.itemOptions.add(new Item.ItemOption(14, 5));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 30));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Kiếm Z", "Ok");
                                            } else if (randomItem == 3) {
                                                Item VatPham = ItemService.gI().createNewItem((short) 733);
                                                VatPham.itemOptions.add(new Item.ItemOption(84, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(30, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 7));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Cân đẩu vân ngũ sắc", "Ok");
                                            } else if (randomItem == 4) {
                                                Item VatPham = ItemService.gI().createNewItem((short) 733);
                                                VatPham.itemOptions.add(new Item.ItemOption(84, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(30, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Cân đẩu vân ngũ sắc", "Ok");
                                            } else if (randomItem == 5) {
                                                Item VatPham = ItemService.gI().createNewItem((short) 733);
                                                VatPham.itemOptions.add(new Item.ItemOption(84, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(30, 0));
                                                VatPham.itemOptions.add(new Item.ItemOption(93, 14));
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Cân đẩu vân ngũ sắc", "Ok");
                                            } else {
                                                Item VatPham = ItemService.gI().createNewItem((short) 16);
                                                InventoryServiceNew.gI().addItemBag(player, VatPham);
                                                createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta tặng cậu Ngọc rồng 3 sao", "Ok");
                                            }
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, RuaCon, 1);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 219, -1, 432);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;
                            case 1:
                                Clan clan = player.clan;
                                if (clan != null) {
                                    ClanMember cm = clan.getClanMember((int) player.id);
                                    if (cm != null) {
                                        if (clan.members.size() > 1) {
                                            Service.gI().sendThongBao(player, "Bang phải còn một người");
                                            break;
                                        }
                                        if (!clan.isLeader(player)) {
                                            Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                            break;
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                                "Đồng ý", "Từ chối!");
                                    }
                                    break;
                                }
                                Service.gI().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                                break;
                            case 2:
                                 if (player.clan != null) {
                                    if (player.clan.BanDoKhoBau != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                        + player.clan.BanDoKhoBau.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                 }
                                 break;
                            case 3:
                                if(player.getSession().player.nPoint.power < 10000000000L){
                                Service.gI().sendThongBao(player, "Cần Có Sức Mạnh Là 10 Tỉ");}
                                else if(player.getSession().player.inventory.gold < 300000000){
                                Service.gI().sendThongBao(player, "Cần 200tr Vàng");
                            }   else {player.nPoint.power -=10000000 ;
                            player.getSession().player.inventory.gold -= 300000000;
                            player.nPoint.teleport=true;
//                            player.idAura=95;
                            player.name="[ Hủy Diệt]\n"+player.name;
                            Service.gI().player(player);
                            Service.gI().Send_Caitrang(player);
                            Service.gI().sendFlagBag(player);
                            Zone zone = player.zone;
                            ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
//                            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 495);
                                        Service.gI().changeFlag(player, 8);
                            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                            new Thread(() -> {
                            try {
                            Thread.sleep(240000);
                                } catch (Exception e) {
                                }
                            Client.gI().kickSession (player.getSession());
                            }).start();
                            
                            Service.gI().sendThongBaoAllPlayer("Kẻ "+player.name+" Đang Ở "+ player.zone.map.mapName + " Khu " + player.zone.zoneId);
                                    break;
                                }     
                                 
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    ChangeMapService.gI().goToDBKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                    Input.gI().createFormChooseLevelBDKB(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                        switch (select) {
                            case 0:
                                BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                    }
                } if (player.iDMark.getIndexMenu() == 13) {
                    switch (select) {
                        case 0:
                            this.createOtherMenu(player, 7,
                                    "Chào con, ta rất vui khi gặp con\n Con muốn làm gì nào ?",
                                   "Chức Năng\nBang hội","Kho Báu\ndưới biển","TOP Server");//,"Rút coin","Top Server");
                            break;
                        case 1:
                                    Service.gI().showListTop(player, Manager.topSM);
                                break;
                                
                     
                                    
                                 
                    }
                }
                    else if (player.iDMark.getIndexMenu() == 20){
                            switch (select) {
                                case 0:
                                    Service.gI().showListTop(player, Manager.topSM);
                                   // Service.getInstance().sendThongBaoOK(player,TopService.QUERY_TOP_POWER());
                                    break;
                                case 1:
                                    Service.gI().showListTop(player, Manager.topNV);
                                    break;
                                case 2:
                                    Service.gI().showListTop(player, Manager.topVND);
                                    //Service.getInstance().sendThongBaoOK(player,TopService.getTopNap());   
                                
                                    
                            }}
                    else if (player.iDMark.getIndexMenu() == 19){
                            switch (select) {
                                case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                case 1:
                                    Input.gI().createFormQDHN(player);
                                    break;
                         }
                } else if (player.iDMark.getIndexMenu() == 7) {
                    switch (select) {
                      //  case 0:
                     //       if (player.getSession().player.nPoint.power >= 80000000000L) {
                   //             ChangeMapService.gI().changeMapBySpaceShip(player, 219, -1, 432);
                   //         } else {
                   //             this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                  //          }
                //            break;
                        case 0:
                                createOtherMenu(player, ConstNpc.BANGHOI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Giải tán\nbang hội", "Khu Vực\nBang Hội", "Nâng cấp\nBang hội","Quyên góp");
                                break;
                        case 1:
                            if (player.clan != null) {
                                if (player.clan.BanDoKhoBau != null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                            "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                    + player.clan.BanDoKhoBau.level + "\nCon có muốn đi theo không?",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                            "Đây là bản đồ kho báu x4 tnsm\nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                            "Chọn\ncấp độ", "Từ chối");
                                }
                            } else {
                                this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                            }
                            break;
                            
                            
                               
                                    case 2:
                                    this.createOtherMenu(player, 20, "TOP Máy Chủ Ngoc Rồng"
                                            + "","Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Nạp");
                                    break;
                    }
                    
                }
                else if (player.iDMark.getIndexMenu() == ConstNpc.BANGHOI) {
                            switch (select) {
                                case 0:
                            Clan clan = player.clan;
                            if (clan != null) {
                                ClanMember cm = clan.getClanMember((int) player.id);
                                if (cm != null) {
                                    if (clan.members.size() > 1) {
                                        Service.gI().sendThongBao(player, "Bang phải còn một người");
                                        break;
                                    }
                                    if (!clan.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Phải là bảng chủ");
                                        break;
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DISSOLUTION_CLAN, -1, "Con có chắc chắn muốn giải tán bang hội không? Ta cho con 2 lựa chọn...",
                                            "Đồng ý", "Từ chối!");
                                }
                                break;
                            }
                            Service.gI().sendThongBao(player, "bạn đã có bang hội đâu!!!");
                            break;
                                case 1:
//                                    if (player.kichhoat == 1){
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 177, -1, 432);
//                                    } else {
//                                       Service.getInstance().sendThongBao(player, "Hãy mở thành viên Để sử dụng");
//                                   }
                                    break;
                                case 2:
                                Clan clan2 = player.clan;
                                if (player.clan != null) {
                                    if (!clan2.isLeader(player)) {
                                        Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ!");
                                        break;
                                    }
                                    if (player.clan.level >= 0 && player.clan.level <= 9) {
                                        this.createOtherMenu(player, ConstNpc.CHUC_NANG_BANG_HOI2,
                                                "Bạn có muốn Nâng cấp lên " + (player.clan.level + 11) + " thành viên không?\n"
                                                + "Cần 1000 Capsule Bang\n"
                                                + "(Thu thập Capsule Bang bằng cách tiêu diệt quái tại Map Rừng Bamboo\n"
                                                + "cùng các thành viên khác)", "Nâng cấp\n(1 tỷ vàng)", "Từ chối");
                                    } else {
                                        Service.gI().sendThongBao(player, "Bang của bạn đã đạt cấp tối đa!");
                                        return;
                                    }
                                    break;
                                }  else if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                            case 3:
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu câu tham gia bang hội");
                                    break;
                                }
                                Input.gI().DonateCsbang(player);
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_BANG_HOI2) {
                        Clan clan = player.clan;
                        switch (select) {
                            case 0:
                                if (player.clan.capsuleClan >= 1000 && clan.isLeader(player) && player.inventory.gold >= 1000000000L) {
                                    player.clan.level += 1;
                                    player.clan.maxMember += 1;
                                    player.clan.capsuleClan -= 1000;
                                    player.inventory.gold -= 1000000000L;
                                    player.clan.update();
                                    Service.gI().sendThongBao(player, "Yêu cầu nâng cấp bang hội thành công");
                                    break;
                                } //                                else if (!clan.isDeputy(player)) {
                                //                                    Service.gI().sendThongBao(player, "Yêu cầu phải là bang chủ");
                                //                                    return;
                                //                                }
                                else if (player.inventory.gold < 1000000000L) {
                                    Service.gI().sendThongBaoOK(player, "Bạn còn thiều " + (1000000000L - player.inventory.gold) + " vàng");
                                    return;
                                } else if (player.clan.capsuleClan < 1000) {
                                    Service.gI().sendThongBaoOK(player, "Bang của bạn còn thiều " + (1000 - player.clan.capsuleClan) + " Capsule bang");
                                     return;
                                }
                        }} 
                else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                    switch (select) {
                        case 0:
                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                ChangeMapService.gI().goToDBKB(player);
                            } else {
                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                            }
                            break;
                    }
                    
                }
                else if (player.iDMark.getIndexMenu() == 19){
                            switch (select) {
                                case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                case 1:
                                    Input.gI().createFormQDHN(player);
                                    break;
                                case 2:
                                    if (player.getSession().vnd >= 10000){
                                        if (player.kichhoat == 0){
                                            player.kichhoat = 1;
                                            PlayerDAO.subvnd(player, 10000);
                                            player.getSession().vnd -= 10000;
                                            Service.gI().sendThongBao(player, "Kích hoạt thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Đã kích hoạt thành viên rồi");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn không đủ 10k");
                                    }
                                    break;
                            }
                }
                else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                    switch (select) {
                        case 0:
                            if (player.isAdmin() || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                Input.gI().createFormChooseLevelBDKB(player);
                            } else {
                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                            }
                            break;
                    }
                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                    switch (select) {
                        case 0:
                            BanDoKhoBauService.gI().openBanDoKhoBau(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                            break;
                            
                    }
                }
            }
        };
    }
    
    public static Npc mavuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào\n|8|Các MAP up Tương ứng với các mảnh Up MVBT Cấp 2,3,4,5", "Tới Ngay", "Nâng Cấp\nBông Tai","Mở Chỉ Số","Cửa Hàng");
                    } else if (this.mapId == 156) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                                break; 
                                case 1:
                                createOtherMenu(player, ConstNpc.NANG_CAP_BONG_TAI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Nâng cấp\nPorata cấp 1","Nâng cấp\nPorata cấp 2", "Nâng cấp\nPorata cấp 3","Nâng cấp\nPorata cấp 4");
                                break;
                                case 2:
                                createOtherMenu(player, ConstNpc.MO_CHI_SO_BONG_TAI,
                                        "Chỉ Porata cấp 5 mới có thể mở chỉ số!!!",
                                        "Mở chỉ số Porata cấp 5","Từ chối");
                                break;
                                case 3:
                                    ShopServiceNew.gI().opendShop(player, "BONGTAI", false);
                                    break;
                            }
                        }
                           else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_BONG_TAI) {
                            switch (select) {
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP4 );
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5 );
                                    break;    
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP3 );
                                    break;
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MO_CHI_SO_BONG_TAI) {
                            switch (select) {
                                
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI );
                                    break;
                                
                                
                            }
                        }
                        
                    } else if (this.mapId == 156) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về lanh dia bang hoi
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 288);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
     public static Npc mamnguqua(int mapId, int status, int cx, int cy, int tempId, int avartar) {
         return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                       this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|5|Thí Luyện Của Ta Là Nơi Chứa Các Sinh Vật Tà Ác,Sau Khi Ngươi Tiêu Diệt Sẽ Nhận Được Các Phần Quà Giá Trị", "Tham Gia", "Từ Chối");
             
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:                            
                            if (player.getSession().actived){
                                this.createOtherMenu(player, 12,
                                    "|8|Cảnh giới Hiện Tại: \n"
                                                    + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                                    +"\n Các Thí Luyện Yêu Cầu Giới,Thí Luyện Lâu Năm Yêu Cầu Cảnh Giới Cực Cao\n|7| Thí Luyện 100 Năm Có Boss Mabu Tiêu Diệt Nhận Đồ Hủy Diệt (25-50% CS,20-30 Sao)"
                                                    + "\n|7|Thí Luyện 1000 Đánh Boss Hủy Diệt Sẽ Ra Các Trang Bị Thiên Sứ (25-70% CS,25-35 Sao)\n|8| UPDATE..."
                                            ,
                                    "Thí Luyện\n100 Năm", "Thí Luyện\n1000 Năm","Thí Luyện\n10 Nghìn Năm","Thí Luyện\n1 Vạn Năm");
                            
                            }else{
                                Service.getInstance().sendThongBao(player, "Chỉ dành cho thành viên");
                            }
                            break;
                    }               
                }else if(player.iDMark.getIndexMenu() == 12){
                    switch (select) {
                        case 0:                                                                        
                        if  (player.Tamkjlltutien[1] <= 2) {
                         Service.gI().sendThongBao(player, "Yêu Cầu Cảnh Giới Trúc Cơ");
                         return;
                        }
                       ChangeMapService.gI().changeMapBySpaceShip(player, 127, -1, 360);
                            break;
                        case 1:
                           if  (player.Tamkjlltutien[1] <= 10) {
                         Service.gI().sendThongBao(player, "Yêu Cầu Cảnh Giới  Thiên Nhân đỉnh phong");
                         return;
                        }
                       ChangeMapService.gI().changeMapBySpaceShip(player, 209, -1, 360);
                            break; 
                        case 2:
                           if  (player.Tamkjlltutien[1] <= 20) {
                         Service.gI().sendThongBao(player, "Yêu Cầu Cảnh Giới Chúa tể Đỉnh Cao");
                         return;
                        }
                       ChangeMapService.gI().changeMapBySpaceShip(player, 217, -1, 360);
                            break; 
                     case 3:
                           if  (player.Tamkjlltutien[1] <= 26) {
                         Service.gI().sendThongBao(player, "Yêu Cầu Cảnh Giới Vĩnh Hằng Hoàn Mỹ");
                         return;
                        }
                       ChangeMapService.gI().changeMapBySpaceShip(player, 211, -1, 360);
                            break; 
                        case 4:
                            Input.gI().createFormChangeTNSMServer(player);
                            break;    
                    }
                }
            }
        };
    }
    
    
    public static Npc Skienhe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|- Sự kiện truy lùng Virus:\n"
                                    + "|1| Tiêu diệt Virus để tìm kiếm vật phẩm, điểm sự kiện\n"
                                    + "|1| Đem vật phẩm sự kiện quay lại gặp ta để đổi lấy các vật phẩm hấp dẫn\n"
                                            + "|2|Chúc các bạn chơi game vui vẻ!!!",
                                    "Vắc Xin", "Giấy chứng\n Nhận","TOP","Hướng dẫn");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item chao = null;
                                Item vitamin = null;
                                Item tv = null;
                                    try {
                                        chao = InventoryServiceNew.gI().findItemBag(player, 2019);
                                        vitamin = InventoryServiceNew.gI().findItemBag(player, 2020);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
                                    }
                                    if (chao == null || chao.quantity < 10) {
                                        this.npcChat(player, "Bạn còn thiếu x10 Cháo dinh dưỡng");
                                    }else if (vitamin == null || vitamin.quantity < 10) {    
                                        this.npcChat(player, "Bạn còn thiếu x10 Vitamin");
                                    }else if (tv == null || tv.quantity < 1) {    
                                        this.npcChat(player, "Bạn còn thiếu x1 Thỏi vàng");    
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, chao, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vitamin, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                        Item vacxin = ItemService.gI().createNewItem((short) Util.nextInt(2020,2020));
                                        InventoryServiceNew.gI().addItemBag(player, vacxin);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được giấy Vắc xin");
                                    }
                            }
                            
                            if (select == 1) {
                                Item vacxin = null;
                                Item tv = null;
                                    try {
                                        vacxin = InventoryServiceNew.gI().findItemBag(player, 2021);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (vacxin == null || vacxin.quantity < 10) {
                                        this.npcChat(player, "Bạn còn thiếu x10 Vắc xin");
                                    } else if (tv == null || tv.quantity < 1) {
                                        this.npcChat(player, "Bạn còn thiếu x1 Thỏi vàng");    
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vacxin, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                        Item giaycn = ItemService.gI().createNewItem((short) Util.nextInt(2022,2022));
                                        InventoryServiceNew.gI().addItemBag(player, giaycn);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được giấy Vắc xin");
                                    }
                            }
                            if (select == 2) {
                                        //Service.gI().showListTop(player, Manager.sanboss);
                                        Service.getInstance().sendThongBaoOK(player,TopService.getsanbos());
                            
                                    }
                            
                        }
                    }
                    if (select == 3) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONGDANSKIEN);
                        }
                    else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc sukienphuongdao(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "|7|- Sự kiện truy lùng Virus:\n"
                                    + "|1| Tiêu diệt Virus để tìm kiếm vật phẩm, điểm sự kiện\n"
                                    + "|1| Đem vật phẩm sự kiện quay lại gặp ta để đổi lấy các vật phẩm hấp dẫn\n"
                                            + "|2|Chúc các bạn chơi game vui vẻ!!!",
                                    "Vắc Xin", "Giấy chứng\n Nhận","TOP","Hướng dẫn");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item chao = null;
                                Item vitamin = null;
                                Item tv = null;
                                    try {
                                        chao = InventoryServiceNew.gI().findItemBag(player, 2046);
                                        vitamin = InventoryServiceNew.gI().findItemBag(player, 2019);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (chao == null || chao.quantity < 10) {
                                        this.npcChat(player, "Bạn còn thiếu x10 Cháo dinh dưỡng");
                                    }else if (vitamin == null || vitamin.quantity < 10) {    
                                        this.npcChat(player, "Bạn còn thiếu x10 Vitamin");
                                    }else if (tv == null || tv.quantity < 1) {    
                                        this.npcChat(player, "Bạn còn thiếu x1 Thỏi vàng");    
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, chao, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vitamin, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                        Item vacxin = ItemService.gI().createNewItem((short) Util.nextInt(2020,2020));
                                        InventoryServiceNew.gI().addItemBag(player, vacxin);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được giấy Vắc xin");
                                    }
                            }
                            
                            if (select == 1) {
                                Item vacxin = null;
                                Item tv = null;
                                    try {
                                        vacxin = InventoryServiceNew.gI().findItemBag(player, 2020);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (vacxin == null || vacxin.quantity < 10) {
                                        this.npcChat(player, "Bạn còn thiếu x10 Vắc xin");
                                    } else if (tv == null || tv.quantity < 1) {
                                        this.npcChat(player, "Bạn còn thiếu x1 Thỏi vàng");    
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vacxin, 10);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                        Item giaycn = ItemService.gI().createNewItem((short) Util.nextInt(2021,2021));
                                        InventoryServiceNew.gI().addItemBag(player, giaycn);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được giấy Vắc xin");
                                    }
                            }
                            if (select == 2) {
                                        Service.gI().showListTop(player, Manager.sanboss);
                            
                                    }
                            
                        }
                    }
                    if (select == 3) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONGDANSKIEN);
                        }
                    else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc Skiencorona(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "-Sự Kiện Hè-\n Sao Biển: Ngọc Rồng 1-7 Sao Ngẫu Nhiên\nCon Cua: Cải Trang Chỉ Số 25-30% Có Thể Vĩnh Viễn\nVò Sò: Linh thú Ngãu Nhiên\n Vỏ Ốc: Vàng (Rất Nhiều)", "Sao Biển", "Con Cua","Vỏ Sò","Vỏ Ốc");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                Item SaoBien = null;
                                    try {
                                        SaoBien = InventoryServiceNew.gI().findItemBag(player, 698);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (SaoBien == null || SaoBien.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 sao biển");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, SaoBien, 99);
                                        Service.gI().sendMoney(player);
                                        Item ngocrong = ItemService.gI().createNewItem((short) Util.nextInt(14,20));
                                        InventoryServiceNew.gI().addItemBag(player, ngocrong);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được ngọc rồng");
                                    }
                            }if (select == 1) {
                            Item ConCua = null;
                                    try {
                                        ConCua = InventoryServiceNew.gI().findItemBag(player, 697);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (ConCua == null || ConCua.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 vật phẩm sự kiện");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, ConCua, 99);
                                        Service.gI().sendMoney(player);
                                        Item caitrang = ItemService.gI().createNewItem((short) (876+player.gender));
                                        caitrang.itemOptions.add(new Item.ItemOption(50, Util.nextInt(35,40)));
                                        caitrang.itemOptions.add(new Item.ItemOption(77, Util.nextInt(35,40)));
                                        caitrang.itemOptions.add(new Item.ItemOption(103, Util.nextInt(35,40)));
                                        if (Util.isTrue(99, 100)){
                                        caitrang.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1,10)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, caitrang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được cải trang");
                                    }
                            }if (select == 2) {
                            Item VoSo = null;
                                    try {
                                        VoSo = InventoryServiceNew.gI().findItemBag(player, 696);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (VoSo == null || VoSo.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 vật phẩm sự kiện");
                                    } else if (player.inventory.gold < 1_000_000_000) {
                                        this.npcChat(player, "Bạn không đủ 1 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 1_000_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, VoSo, 99);
                                        Service.gI().sendMoney(player);
                                        Item pet = ItemService.gI().createNewItem((short) Util.nextInt(1373,1378));
                                        pet.itemOptions.add(new Item.ItemOption(50, Util.nextInt(25,30)));
                                        pet.itemOptions.add(new Item.ItemOption(77, Util.nextInt(25,30)));
                                        pet.itemOptions.add(new Item.ItemOption(103, Util.nextInt(25,30)));
                                        if (Util.isTrue(99, 100)){
                                        pet.itemOptions.add(new Item.ItemOption(93, Util.nextInt(1,10)));
                                        }
                                        InventoryServiceNew.gI().addItemBag(player, pet);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được pet");
                                    }
                            }if (select == 3) {
                            Item VoOc = null;
                                    try {
                                        VoOc = InventoryServiceNew.gI().findItemBag(player, 695);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (VoOc == null || VoOc.quantity < 99) {
                                        this.npcChat(player, "Bạn không đủ 99 vỏ ốc");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold += Util.nextInt(500000000,1000000000);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, VoOc, 99);
                                        Service.gI().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được vàng");
                                    }
                            }
                        }
                    } else if (this.mapId == 104) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }
    
    public static Npc trieuhoithu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Xin chào " + player.name + "\nCó tiền không mà đòi làm phiền ta",
                                "Nói Chuyện","Đổi Điểm","BXH","Đóng");//,"Nhận Quà\nmốc nạp","Hộp quà tích điểm");
								
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (!canOpenNpc(player)) {
                    return;
                }
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
			case 0:
			    this.createOtherMenu(player, 2, 
                                    "Các Player Thu thập vật phẩm để trang trí cây nêu\n"
                                    + "|3|Trang trí Thú sẽ tiêu hao 1 thỏi vàng\n"
                                    + "|1|Nhận ngẫu nhiên các vật phẩm như:\n"
                                    + "|7|Cơm Gà, Mì, Các chữ chúc ngày tết, 2024."
                                    , "Trang trí", "Chúc phúc","Đóng");   
                            break;
                        case 1:
                       //     Service.getInstance().sendThongBao(player, "Sẽ được cập nhật tại phiên bản chính thức");
			    this.createOtherMenu(player, 3,"Người chơi "+player.name+" đã đạt "+player.diemsukientet+" Điểm Đổi quà tết"
                                    , "Đạt\n100 điểm", "Đạt\n200 điểm","Đạt\n500 điểm","Đóng" );   
                            break;

                        case 2:
                            Service.getInstance().sendThongBaoOK(player,TopService.gettopskientet());
                                break;
                                                   

                        case 3 :
                            break;
                    }
                  
                } else if (player.iDMark.getIndexMenu() == 3) {
                    switch (select) {
                        case 0:
                            if (player.diemsukientet >= 100 ){
                                if (Util.isTrue(50,100)){
                                    int op1[] = {5,77,103,103};  
                                    int op2[] = {50,14,14};  
                                    Item _item = ItemService.gI().createNewItem((short)1523, 1); 
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op2.length-1)],Util.nextInt(1,15)));
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(1,10)));                                                         
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemsukientet -=100;
                                    player.diemsukientet +=10;
                                    Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                }else{
                                    int op1[] = {5,77,103,103};  
                                    int op2[] = {50,14,14};  
                                    Item _item = ItemService.gI().createNewItem((short)1523, 1); 
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(1,15)));
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op2.length-1)],Util.nextInt(1,10)));                                                         
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                     player.diemsukientet -=100;
                                    Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                }                                
                            }else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                            }
                            break;
                        case 1:
                            if (player.diemsukientet >= 200 ){
                                if (Util.isTrue(50,100)){
                                    int op1[] = {5,77,103,103};  
                                    int op2[] = {50,14,14};  
                                    Item _item = ItemService.gI().createNewItem((short)1347, 1); 
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op2.length-1)],Util.nextInt(30,35)));
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(30,35)));                                                         
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemsukientet +=20;
                                    player.diemsukientet -=200;
                                    Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                }else{
                                    int op1[] = {5,77,103,103};  
                                    int op2[] = {50,14,14};  
                                    Item _item = ItemService.gI().createNewItem((short)1348, 1); 
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(30,35)));
                                    _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op2.length-1)],Util.nextInt(30,35)));                                                         
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                     player.diemsukientet -=100;
                                    Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                }                                
                            }
                            else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                            }
                            break;
                        case 2:
                            if (player.diemsukientet >= 500 ){
                                
                                    int op1[] = {5,77,103,103};  
                                    int op2[] = {50,14,14};  
                                    Item _item = ItemService.gI().createNewItem((short)1492, 1); 
                                    _item.itemOptions.add(new Item.ItemOption(50,20));
                                    _item.itemOptions.add(new Item.ItemOption(103,20));   
                                    _item.itemOptions.add(new Item.ItemOption(77,20));  
                                    _item.itemOptions.add(new Item.ItemOption(14,15));  
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemsukientet +=50;
                                    Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                
                                     player.diemsukientet -=500;
                                                                
                            }else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                            }
                            break;
                        }      
                } else if (player.iDMark.getIndexMenu() == 2) {
                    switch (select) {
                        case 0:     
                                Item phao = null;
                                Item chucmungnammoi = null;
                                Item daybuoc = null;
                                Item denlong = null;
                                Item tv = null;
                                    try {
                                        phao = InventoryServiceNew.gI().findItemBag(player, 1173);
                                        chucmungnammoi = InventoryServiceNew.gI().findItemBag(player, 1174);
                                        daybuoc = InventoryServiceNew.gI().findItemBag(player, 1175);
                                        denlong = InventoryServiceNew.gI().findItemBag(player, 1176);
                                        tv = InventoryServiceNew.gI().findItemBag(player, 457);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (phao == null || phao.quantity < 2) {
                                        this.npcChat(player, "Bạn còn thiếu x2 Pháo Nổ");
                                    }else if (chucmungnammoi == null || chucmungnammoi.quantity < 1) {    
                                        this.npcChat(player, "Bạn còn thiếu x1 Chúc mừng năm mới");
                                    }else if (daybuoc == null || daybuoc.quantity < 20) {    
                                        this.npcChat(player, "Bạn còn thiếu x20 Dây pháo");
                                    }else if (denlong == null || denlong.quantity < 2) {    
                                        this.npcChat(player, "Bạn còn thiếu x2 Lồng đèn treo cây");    
                                    }else if (tv == null || tv.quantity < 1) {    
                                        this.npcChat(player, "Bạn còn thiếu x1 Thỏi vàng");    
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, phao, 2);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, chucmungnammoi, 1);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, daybuoc, 20);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, denlong, 2);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, tv, 1);
                                        Item chucphuc = ItemService.gI().createNewItem((short) Util.nextInt(1177,1184));
                                        player.diemsukientet +=1;
                                        InventoryServiceNew.gI().addItemBag(player, chucphuc);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Chúc mừng bạn trang trí thành công, nhận sẽ nhận được phần quà hấp dẫn");
                                    }
                    
                           break;
                        case 1:
                     CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DOI_HOP_QUA);
                                    break;
                    }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.DOI_HOP_QUA:
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        
                } else if (player.iDMark.getIndexMenu() == 3) {
                    switch (select) {
                        case 0:
                            if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    Item cut1 = InventoryServiceNew.gI().cut1985(player);
                                    Item cut2 = InventoryServiceNew.gI().cut1986(player);
                                    Item cut3 = InventoryServiceNew.gI().cut1987(player);
                                    Item cut4 = InventoryServiceNew.gI().cut(player);
                                    
			            
				if(player.inventory.gold >= 500000000)	{				
                                    if (cut1 !=null && cut2 !=null && cut3 !=null && cut4 !=null ) {                                       
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cut1, 1);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cut2, 1);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cut3, 1);      
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, cut4, 1);      
					if (Util.isTrue(1, 50)) {
                                            short[] item962 = new short[]{1571,1572,1573};
                                            int op1[] = {50,77,103,103};  
                                            int op2[] = {27,28,5,14,14}; 
                                            
                                            Item _item = ItemService.gI().createNewItem(item962[Util.nextInt(item962.length)], 1); 
                                            
                                            _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(7,10)));    
                                            _item.itemOptions.add(new Item.ItemOption(op2[Util.nextInt(0, op2.length-1)],Util.nextInt(3,5)));    
                                            _item.itemOptions.add(new Item.ItemOption(30,0)); 
                                                                                                
                                            InventoryServiceNew.gI().addItemBag(player, _item);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            player.diemsukientet +=10;
                                            Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                            ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                        }if (Util.isTrue(1, 10)) {
                                            short[] item962 = new short[]{1571,1572,1573};
                                            int op1[] = {50,77,103,103};  
                                            int op2[] = {27,28,5,14,14}; 
                                            
                                            Item _item = ItemService.gI().createNewItem(item962[Util.nextInt(item962.length)], 1); 
                                            
                                            _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(7,10)));    
                                            _item.itemOptions.add(new Item.ItemOption(op2[Util.nextInt(0, op2.length-1)],Util.nextInt(1,2)));    
                                            _item.itemOptions.add(new Item.ItemOption(30,0)); 
                                                                                                
                                            InventoryServiceNew.gI().addItemBag(player, _item);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                            ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );    
                                        }if (Util.isTrue(1, 10)) {
                                            short[] item962 = new short[]{1571,1572,1573};
                                            int op1[] = {50,77,103,103};  
                                            int op2[] = {27,28,5,14,14}; 
                                            
                                            Item _item = ItemService.gI().createNewItem(item962[Util.nextInt(item962.length)], 1); 
                                            
                                            _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(1,7)));    
                                            _item.itemOptions.add(new Item.ItemOption(op2[Util.nextInt(0, op2.length-1)],Util.nextInt(1,5)));    
                                            _item.itemOptions.add(new Item.ItemOption(30,0)); 
                                                                                                
                                            InventoryServiceNew.gI().addItemBag(player, _item);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            player.diemsukientet +=20;
                                            Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                            ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                        }if (Util.isTrue(1, 10)) {
                                            short[] item962 = new short[]{1571,1572,1573};
                                            int op1[] = {50,77,103,103};  
                                            int op2[] = {27,28,5,14,14}; 
                                            
                                            Item _item = ItemService.gI().createNewItem(item962[Util.nextInt(item962.length)], 1); 
                                            
                                            _item.itemOptions.add(new Item.ItemOption(op1[Util.nextInt(0, op1.length-1)],Util.nextInt(1,3)));    
                                            _item.itemOptions.add(new Item.ItemOption(op2[Util.nextInt(0, op2.length-1)],5));    
                                            _item.itemOptions.add(new Item.ItemOption(30,0)); 
                                                                                                
                                            InventoryServiceNew.gI().addItemBag(player, _item);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _item.template.name);
                                            ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được " + _item.template.name + " " );
                                        } else {
                                            //short [] rdbanh = new short[] {966,891};
                                                                          
                                            short [] caitrang = new short[] {16,17,18,19,20,441,442,443,444,445,446,447,447};                                  
                                            Item _caitrang = ItemService.gI().createNewItem(caitrang[Util.nextInt(caitrang.length-1)], 1);                                   
     
                                            Service.getInstance().sendThongBao(player, "Chúc mừng con nhận được " + _caitrang.template.name);
                                            InventoryServiceNew.gI().addItemBag(player, _caitrang);
                                            InventoryServiceNew.gI().sendItemBags(player);
                                            }
                                        player.inventory.gold -= 500000000;
                                        Service.getInstance().sendMoney(player);
                                        }else{
                                             Service.getInstance().sendThongBao(player, "Không đủ cứt");   
                                            }
                                        
                                    }else{
                                        Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                    }
                            }else{
                                Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                            }
                            break;

                        case 1:                           
                            break;
                    }    
                    
                        
                    
                }                                  
            }
        };
    }

    public static Npc whis(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 154) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thử đánh với ta xem nào.\nNgươi còn 1 lượt cơ mà.",
                            "Nói chuyện", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu() && this.mapId == 154) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 5, "Ta sẽ giúp ngươi chế tạo trang bị thiên sứ", "Chế tạo", "Cửa hàng whis","Từ chối");
                                break;
                            case 7:
                                Item BiKiepTuyetKy = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1978);
                                if (BiKiepTuyetKy != null) {
                                    if (player.gender == 0) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Super kamejoko\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 1) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ Ma phông ba\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                    if (player.gender == 2) {
                                        this.createOtherMenu(player, 6, "|1|Ta sẽ dạy ngươi tuyệt kỹ "
                                                + "đíc chưởng liên hoàn\n" + "|7|Bí kiếp tuyệt kỹ: " + BiKiepTuyetKy.quantity + "/999\n" + "|2|Giá vàng: 10.000.000\n" + "|2|Giá ngọc: 99",
                                                "Đồng ý", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Hãy tìm bí kíp rồi quay lại gặp ta!");
                                }
                                break;
                        }
                    }  if (player.iDMark.getIndexMenu() == 5) {
                        if (select == 0) {
                            CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CHE_TAO_TRANG_BI_TS);
                        }
                        if (select == 1) {
                                    ShopServiceNew.gI().opendShop(player, "PHU_KIEN", false);                                                                      
                        } 
                    }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO) {
                        if (select == 0) {
                            CombineServiceNew.gI().startCombine(player);
                        }
                    }
                    
                    else if (player.iDMark.getIndexMenu() == 6) {
                        switch (select) {
                            case 0:
                                Item sach = InventoryServiceNew.gI().findItemBag(player, 1978);
                                if (sach != null && sach.quantity >= 999 && player.inventory.gold >= 10000000 && player.inventory.gem > 99 && player.nPoint.power >= 60000000000L) {

                                    if (player.gender == 2) {
                                        SkillService.gI().learSkillSpecial(player, Skill.LIEN_HOAN_CHUONG);
                                    }
                                    if (player.gender == 0) {
                                        SkillService.gI().learSkillSpecial(player, Skill.SUPER_KAME);
                                    }
                                    if (player.gender == 1) {
                                        SkillService.gI().learSkillSpecial(player, Skill.MA_PHONG_BA);
                                    }
                                    InventoryServiceNew.gI().subQuantityItem(player.inventory.itemsBag, sach, 999);
                                    player.inventory.gold -= 10000000;
                                    player.inventory.gem -= 99;
                                    InventoryServiceNew.gI().sendItemBags(player);
                                } else if (player.nPoint.power < 60000000000L) {
                                    Service.getInstance().sendThongBao(player, "Ngươi không đủ sức mạnh để học tuyệt kỹ");
                                    return;
                                } else if (sach.quantity <= 999) {
                                    int sosach = 9999 - sach.quantity;
                                    Service.getInstance().sendThongBao(player, "Ngươi còn thiếu " + sosach + " bí kíp nữa.\nHãy tìm đủ rồi đến gặp ta.");
                                    return;
                                } else if (player.inventory.gold <= 500000000) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ vàng thì quay lại gặp ta.");
                                    return;
                                } else if (player.inventory.gem <= 999) {
                                    Service.getInstance().sendThongBao(player, "Hãy có đủ ngọc xanh thì quay lại gặp ta.");
                                    return;
                                }

                                break;
                        }
                    }
                }
            }

        };
    }
    public static Npc truongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                    if (TaskService.gI().getIdTask(player) == ConstTask.TASK_16_4) {
                    TaskService.gI().sendNextTaskMain(player);  
                  }
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }
    
    public static Npc huvang(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "Vàng ngày một nhiều, đừng bỏ lỡ cơ hội trở thành tỉ phú như ta", 
                        "Tìm Hiểu", 
                        "Bắt Đầu");
            }

            @Override
            public void confirmMenu(Player pl, int select) throws InterruptedException {
                if (canOpenNpc(pl)) {
                    if (pl.getSession().actived){
                        String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 60000) + " phút";
                        int gold = ChonAiDay.gI().goldNormar;
                        int goldmn1 = gold*1/1000;
                        int goldmn2 = gold*2/1000;
                        int goldmn5 = gold*5/1000;
                        int goldw1 = gold*1/100;
                        int goldw2 = gold*(Util.nextInt(2,3))/100;
                        int goldw5 = gold*(Util.nextInt(5,10))/100;
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian lưu trữ vàng là 1 ngày\n"
                                    + "Khi đặt cược sẽ có tỉ lệ chiến thắng \n"
                                    + "với các mức phần thưởng khác nhau\n"
                                    + "Nếu thất bại giá trị tổng vàng Sever sẽ tăng đột biến",
                                    
                                     "Đã hiểu");
                        } else if (select == 1) {
                            createOtherMenu(pl, 1, "Hũ vàng: " + ChonAiDay.gI().goldNormar + " thỏi vàng\n"
                                    
                                    + " Giá trị vàng thắng tương ứng với số vàng cược\n " 
                                    + "Nếu thất bại giá trị tổng vàng Sever sẽ tăng đột biến",
                                    //+ " |3|Thời gian làm mới còn: " + time , 
                                    "Theo dõi", "Đặt cược\n"+goldmn1+" Thỏi",                                           
                                    "Đặt cược\n"+goldmn2+" Thỏi",
                                    "Đặt cược\n"+goldmn5+" Thỏi");
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Hũ vàng: " + ChonAiDay.gI().goldNormar + " thỏi vàng\n"
                                   
                                    + "Giá trị vàng thắng tương ứng với số vàng cược\n " 
                                    + "Nếu thất bại giá trị tổng vàng Sever sẽ tăng đột biến",
                                    //+ " |3|Thời gian làm mới còn: " + time , 
                                    "Theo dõi", "Đặt cược\n"+goldmn1+" Thỏi",                                           
                                    "Đặt cược\n"+goldmn2+" Thỏi",
                                    "Đặt cược\n"+goldmn5+" Thỏi");
                                    
                                    break;
                                case 1:  {                                   
                                    Item thoivang = InventoryServiceNew.gI().findItemBag(pl, 457);
                                    if (thoivang != null && thoivang.quantity >= goldmn1) {				                                        
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, thoivang, goldmn1);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        int TimeSeconds = 10;
                                        
                                        while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Service.gI().sendThongBao(pl, "Đợi "+TimeSeconds +" giây để biết kết quả");
                                        this.npcChat(pl, "Đợi "+TimeSeconds +" giây để biết kết quả");
                                        Thread.sleep(1000);
                                        }
					if (Util.isTrue(1, 100)) {                                                                                         
                                            Item _item = ItemService.gI().createNewItem((short)457, goldw1); 
                                            ChonAiDay.gI().goldNormar -= goldw1;                        
                                            InventoryServiceNew.gI().addItemBag(pl, _item);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng nhận được"+goldw1+" Thỏi vàng");
                                            Service.getInstance().sendThongBao(pl, "Chúc mừng con nhận được " + _item.template.name);   
                                        }else{
                                            ChonAiDay.gI().goldNormar += goldw1*(Util.nextInt(1,3));    
                                            Service.getInstance().sendThongBao(pl, "Ngươi không phải kẻ may mắn rồi");   
                                        }
                                    }else{
                                        Service.getInstance().sendThongBao(pl, "Không đủ thỏi vàng");   
                                    }
                                }
                                    
                                break;

                                case 2: {
                                    Item thoivang = InventoryServiceNew.gI().findItemBag(pl, 457);
                                    if (thoivang != null && thoivang.quantity >= goldmn2) {				                                        
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, thoivang, goldmn2);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        int TimeSeconds = 10;
                                        
                                        while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Service.gI().sendThongBao(pl, "Đợi "+TimeSeconds +" giây để biết kết quả");
                                        Thread.sleep(1000);
                                        }
					if (Util.isTrue(1, 100)) {                                                                                         
                                            Item _item = ItemService.gI().createNewItem((short)457, goldw2); 
                                            ChonAiDay.gI().goldNormar -= goldw2;                        
                                            InventoryServiceNew.gI().addItemBag(pl, _item);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng nhận được"+goldw2+" Thỏi vàng");
                                            Service.getInstance().sendThongBao(pl, "Chúc mừng con nhận được " + _item.template.name);   
                                        }else{
                                            ChonAiDay.gI().goldNormar += goldw2*(Util.nextInt(1,2));    
                                            Service.getInstance().sendThongBao(pl, "Ngươi không phải kẻ may mắn rồi");   
                                        }
                                    }else{
                                        Service.getInstance().sendThongBao(pl, "Không đủ thỏi vàng");   
                                    }
                                }
                                break;
                                case 3: {
                                    Item thoivang = InventoryServiceNew.gI().findItemBag(pl, 457);
                                    if (thoivang != null && thoivang.quantity >= goldmn5) {				                                        
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, thoivang, goldmn5);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        int TimeSeconds = 10;
                                        
                                        while (TimeSeconds > 0) {
                                        TimeSeconds--;
                                        Service.gI().sendThongBao(pl, "Đợi "+TimeSeconds +" giây để biết kết quả");
                                        Thread.sleep(1000);
                                        }
					if (Util.isTrue(1, 100)) {                                                                                         
                                            Item _item = ItemService.gI().createNewItem((short)457, goldw5); 
                                            ChonAiDay.gI().goldNormar -= goldw5;                        
                                            InventoryServiceNew.gI().addItemBag(pl, _item);
                                            InventoryServiceNew.gI().sendItemBags(pl);
                                            ChatGlobalService.gI().chat(pl, pl.name + " đã chiến thắng nhận được"+goldw5+" Thỏi vàng");
                                            Service.getInstance().sendThongBao(pl, "Chúc mừng con nhận được " + _item.template.name);   
                                        }else{
                                            ChonAiDay.gI().goldNormar += goldw5*(Util.nextInt(1,2));    
                                            Service.getInstance().sendThongBao(pl, "Ngươi không phải kẻ may mắn rồi");   
                                        }
                                    }else{
                                        Service.getInstance().sendThongBao(pl, "Không đủ thỏi vàng");   
                                    }
                                }
                                break;

                            
                        }
                    }
                }else{
                        Service.getInstance().sendThongBao(pl, "Muốn làm giàu thì phải mở thành viên ");
                    }
                }
            }
        };
    }
    
//    public static Npc taixiu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
//        return new Npc(mapId, status, cx, cy, tempId, avartar) {
//            
//
//            @Override
//            public void openBaseMenu(Player player) {
//                if (canOpenNpc(player)) {  
//                    Item tv1 = InventoryServiceNew.gI().findItemBag(player, 457);
//                    if (tv1 !=null){
//                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                        "Xin chào "+player.name+", Ngươi đang có:\n|3|" + tv1.quantity + " Thỏi vàng",
//                         "Nói Chuyện");             
//                    }else{
//                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
//                        "Xin chào "+player.name+", Ngươi đang có:\n|3| 0 Thỏi vàng",
//                         "Nói Chuyện");
//                    }
//                }
//            }
//            @Override
//            public void confirmMenu(Player player, int select) {
//                if (!canOpenNpc(player)) {
//                    return;
//                }
//                if (player.iDMark.isBaseMenu()) {
//                    switch (select) {
//                        case 0:                            
//                            if (player.getSession().actived){
//                                this.createOtherMenu(player, 12,
//                                    "Chọn đi, ta sẽ giúp ngươi thành người gi"
//                                            ,
//                                    "Lẻ", "Chẵn","Đóng");
//                            
//                            }else{
//                                Service.getInstance().sendThongBao(player, "Chỉ dành cho thành viên");
//                            }
//                            break;
//                    }               
//                }else if(player.iDMark.getIndexMenu() == 12){
//                    switch (select) {
//                        case 0:
//                            Input.gI().XIU(player);
//                            break;
//                        case 1:
//                            Input.gI().TAI(player);
//                            break;    
//                    }
//                }
//            }
//        };
//    }
    public static Npc taixiu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
           @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu! Đặt cược và dự đoán đúng"
                        + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                        + "\n\n|7|(Điều kiện tham gia : mở thành viên)\n\n|2|Đặt tối thiểu: 1.000 Hồng ngọc\n Tối đa: 10.000.000 Hồng ngọc"
                        + "\n\n|7| Lưu ý : Thoát game khi chốt Kết quả sẽ MẤT Tiền cược và Tiền thưởng", "Thể lệ", "Cược Hn","Cược TV");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                    + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI\nNếu 3 Xí ngầu cùng 1 số : TAM HOA (Nhà cái lụm hết)"
                                    + "\n\n|7|Lưu ý: Số Hồng ngọc nhận được sẽ bị nhà cái lụm đi 20%. Trong quá trình diễn ra khi đặt cược nếu thoát game trong lúc phát thưởng phần quà sẽ bị HỦY", "Ok");
                        } else if (select == 1) {
                            if (TaiXiu.gI().baotri == false){
                            if(pl.goldTai==0 && pl.goldXiu==0){
                               createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                            } 
                            else if(pl.goldTai > 0){
                                createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI TÀI XỈU---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"        
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            } 
                            else {
                                createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            } 
                            } else {
                                if(pl.goldTai==0 && pl.goldXiu==0){
                                  createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                            }  else if(pl.goldTai > 0){
                                   createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            }  else {
                                    createOtherMenu(pl, 123, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z +                                            "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time +"\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                }
                            }
                        }
                        } else if (select == 2) {
                            if (TaiXiu.gI().baotri == false){
                            if(pl.goldTai==0 && pl.goldXiu==0){
                               createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                            } 
                            else if(pl.goldTai > 0){
                                createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI TÀI XỈU---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"        
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            } 
                            else {
                                createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            } 
                            } else {
                                if(pl.goldTai==0 && pl.goldXiu==0){
                                  createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                            }  else if(pl.goldTai > 0){
                                   createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            }  else {
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z +                                            "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time +"\n\n|7|Bạn đã cược Xỉu : " + Util.format(pl.goldXiu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                }
                            }
                        }
                     else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai==0 && pl.goldXiu==0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                               case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                            break;
                               case 1:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                       Input.gI().TAI_taixiu(pl);
                                    }
                                    break;
                              case 2:
                                    if (!pl.getSession().actived) {
                                        Service.gI().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                    } else {
                                        Input.gI().XIU_taixiu(pl);
                                    }
                                    break;
                            }
                        } else if(((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == false){
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            
                                    break;
                            }
                        }else if(((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == false){
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                                    break;       }
                       }else if(((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldTai > 0 && TaiXiu.gI().baotri == true){
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            
                                    break;
                            }
                        }else if(((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu > 0 && TaiXiu.gI().baotri == true){
                            switch (select) {
                                case 0:
                                     createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Đóng");
                            
                                    break;
                             }
                         }else if(((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && pl.goldXiu == 0 && pl.goldTai == 0 && TaiXiu.gI().baotri == true){
                             switch (select) {
                                 case 0:
                                     createOtherMenu(pl, 1, "\n|7|---NHÀ CÁI---\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " +  TaiXiu.gI().y + " : " +  TaiXiu.gI().z 
                                         +"\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Hồng ngọc"
                                         +"\n|6|Tổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Hồng ngọc"
                                         +"\n\n|1|Tổng người chơi: " + (TaiXiu.gI().PlayersTai.size()+ TaiXiu.gI().PlayersXiu.size()) + " người"
                                         +"\n|4|Số Tiền Bạn Đã Đặt: " + (pl.goldTai+ pl.goldXiu) + " Hồng ngọc"
                                         +"\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                            
                                    break;
                             }
                         }
                     }
                 }
             }
         };
    }
    
    public static Npc phithang(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    
                     if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "|8|Xin chào " + player.name + "\nCon Đường Tu Tiên Rất Nhiều Gian Nan\n Ngươi Hãy Lên Núi Kiếm Trang Bị Về Đây Cho Ta",
                                "Luyện Hóa","Tiến Cấp","SHOP","UP \nVật Phẩm");//,"Nhận Quà\nmốc nạp","Hộp quà tích điểm");   
                    
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        Item nhan1 = InventoryServiceNew.gI().findItemBag(player, 1829);
                                Item nhan2 = InventoryServiceNew.gI().findItemBag(player, 1830);
                                Item nhan3 = InventoryServiceNew.gI().findItemBag(player, 1831);
                                Item nhan4 = InventoryServiceNew.gI().findItemBag(player, 1832);
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                this.createOtherMenu(player, 21, "Huyết Tế Trang Bị Qua Điểm Luyện Hóa\n"                                                                                                          
                                    , "Huyết Tế\nThần Linh", "Huyết Tế\nHủy Diệt","Huyết Tế\nThiên Sứ","Đóng");   
                            
                                break;
                            case 1:
                                    this.createOtherMenu(player, 22, "Luyện hóa trang bị sẽ giúp tôi luyện bản thân"
                                            + "\n|7|Siêu Thần Tăng 50% Chỉ Số"
                                            + "\n|7|Vương Giả Tăng 60% Chỉ Số"
                                            + "\n|7|Thiên Tử Tăng 75% Chỉ Số"
                                            + "\n|7|Kỳ Thiên Tăng 100% Chỉ Số"                                                                                                          
                                    , "Siêu Thần", "Vương Giả","Thiên Tử","Kỳ Thiên","Đóng");   
                                    break; 			      
                               

                            case 2:   
                                ShopServiceNew.gI().opendShop(player, "PHITHANG", false);
                                break;
                            case 3:
                                    this.createOtherMenu(player, 221,   "Ngươi tìm ta có việc gì?\n|7| Up Đá Ánh Sáng, Bóng Tối 30%\n|7|(Dùng Để Bóng Tối Trang Bị Từ 5-15%)\n|7| Đá Khảm 2% (SD,KI,HP)\n|7|(Dùng Để Khảm Từ 3-5K SD,KI,HP)\n|7| Up Ngọc Bảo 50%\n|5|Nâng Cấp Tất Cả Tại Bà Hạt Mít",
                                "Fram Ngay" , "Từ Chối");
                                                 break; 			      
                               
                            }
                        } else if (player.iDMark.getIndexMenu() == 22) {
                            switch (select) { 
                                case 0:
                                    this.createOtherMenu(player, 24, "Luyện hóa trang bị sẽ giúp tôi luyện bản thân\n"
                                            + "Cấp độ Siêu Thần cần đạt 300 điểm\n"
                                            + "|3|Điểm hiện tại: "+player.diemphangiai+"/300"                                                                                                          
                                    , "Đồng ý","Đóng");   
                                    break; 
                                case 1:
                                    this.createOtherMenu(player, 25, "Luyện hóa trang bị sẽ giúp tôi luyện bản thân\n"
                                            + "Cấp độ Vương Giả cần đạt 1000 điểm\n"
                                            + "|3|Điểm hiện tại: "+player.diemphangiai+"/1000"                                                                                                          
                                    , "Đồng ý","Đóng");   
                                    break;
                                case 2:
                                    this.createOtherMenu(player, 26, "Luyện hóa trang bị sẽ giúp tôi luyện bản thân\n"
                                            + "Cấp độ Thiên Tử cần đạt 2000 điểm\n"
                                            + "|3|Điểm hiện tại: "+player.diemphangiai+"/2000"                                                                                                          
                                    , "Đồng ý","Đóng");   
                                    break;
                                case 3:
                                    this.createOtherMenu(player, 27, "Luyện hóa trang bị sẽ giúp tôi luyện bản thân\n"
                                            + "Cấp độ Kỳ Thiên cần đạt 5000 điểm\n"
                                            + "|3|Điểm hiện tại: "+player.diemphangiai+"/5000"                                                                                                          
                                    , "Đồng ý","Đóng");   
                                    break;
                                 
                            }
                        } else if (player.iDMark.getIndexMenu() == 24) {
                        switch (select) { 
                            case 0:
                              if (player.diemphangiai < 300) {
                                  player.diemphangiai -= 300;
                                   Item _item = ItemService.gI().createNewItem((short)1829, 1); 
                                    _item.itemOptions.add(new Item.ItemOption(50,50));   
                                    _item.itemOptions.add(new Item.ItemOption(77,50));   
                                    _item.itemOptions.add(new Item.ItemOption(103,50));   
                                    _item.itemOptions.add(new Item.ItemOption(30,0));   
                                    InventoryServiceNew.gI().removeItemBag(player, nhan2);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan3);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan4);
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    this.npcChat(player, "Chúc mừng con nhé");
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " đạt cấp độ Siêu Thần" );
                                    
                                }else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                                }
                                break;
                        }  
                        } else if (player.iDMark.getIndexMenu() == 25) {
                        switch (select) { 
                            case 0:
                                if (player.diemphangiai >= 1000){
                                      player.diemphangiai -= 1000;
                                    Item _item = ItemService.gI().createNewItem((short)1830, 1); 
                                       _item.itemOptions.add(new Item.ItemOption(50,60));   
                                    _item.itemOptions.add(new Item.ItemOption(77,60));   
                                    _item.itemOptions.add(new Item.ItemOption(103,60));   
                                    _item.itemOptions.add(new Item.ItemOption(30,0));   
                                    _item.itemOptions.add(new Item.ItemOption(0,5000)); 
                                    InventoryServiceNew.gI().removeItemBag(player, nhan1);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan3);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan4);
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    this.npcChat(player, "Chúc mừng con nhé");
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " đạt cấp độ Vương Giả" );
                                }else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                                }
                                break;
                        }  
                        } else if (player.iDMark.getIndexMenu() == 26) {
                        switch (select) { 
                            case 0:
                                if (player.diemphangiai >= 2000){
                                      player.diemphangiai -= 2000;
                                    Item _item = ItemService.gI().createNewItem((short)1831, 1); 
                                       _item.itemOptions.add(new Item.ItemOption(50,75));   
                                    _item.itemOptions.add(new Item.ItemOption(77,75));   
                                    _item.itemOptions.add(new Item.ItemOption(103,75));   
                                    _item.itemOptions.add(new Item.ItemOption(0,10000));   
                                    _item.itemOptions.add(new Item.ItemOption(30,0));    
                                    InventoryServiceNew.gI().removeItemBag(player, nhan2);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan1);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan4);
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    this.npcChat(player, "Chúc mừng con nhé");
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " đạt cấp độ Thiên Tử" );
                                }else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                                }
                                break;
                            }
                        } else if (player.iDMark.getIndexMenu() == 27) {
                        switch (select) { 
                            case 0:
                                if (player.diemphangiai >= 5000){
                                      player.diemphangiai -= 5000;
                                    Item _item = ItemService.gI().createNewItem((short)1832, 1); 
                                       _item.itemOptions.add(new Item.ItemOption(50,100));   
                                    _item.itemOptions.add(new Item.ItemOption(77,100));   
                                    _item.itemOptions.add(new Item.ItemOption(103,100));   
                                    _item.itemOptions.add(new Item.ItemOption(0,12000));   
                                    _item.itemOptions.add(new Item.ItemOption(30,0));     
                                    InventoryServiceNew.gI().removeItemBag(player, nhan1);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan3);
                                    InventoryServiceNew.gI().removeItemBag(player, nhan2);
                                    InventoryServiceNew.gI().addItemBag(player, _item);
                                    InventoryServiceNew.gI().sendItemBags(player);    
                                    this.npcChat(player, "Chúc mừng con nhé");
                                    ServerNotify.gI().notify("Chúc mừng " + player.name + " đạt cấp độ Kỳ Thiên" );
                                }else{
                                Service.getInstance().sendThongBao(player, "Tiếp tục nỗ lực để kiếm thêm điểm nhé " );
                                }
                                break;
                            }   
                          } else if (player.iDMark.getIndexMenu() == 221) {                   
                            switch (select) {                       
                                case 0:
                                //đến Map UP bóng tối
                                ChangeMapService.gI().changeMapBySpaceShip(player, 196, -1, 360);
                                break; 
                            }
                        } else if (player.iDMark.getIndexMenu() == 21) {                   
                            switch (select) {                       
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_TRANG_BI1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_TRANG_BI2);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_TRANG_BI3);
                                    break;
                                case 3:
                                    this.createOtherMenu(player, 22, "Luyện hóa trang bị sẽ giúp tôi luyện bản thân\n"                                                                                                          
                                    , "Siêu Thần", "Vương Giả","Thiên Tử","Kỳ Thiên","Đóng");   
                                    break; 
                    }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.PHAN_RA_TRANG_BI1:
                                    case CombineServiceNew.PHAN_RA_TRANG_BI2:
                                        case CombineServiceNew.PHAN_RA_TRANG_BI3:
                                            

                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc vuaVegeta(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                  if (TaskService.gI().getIdTask(player) == ConstTask.TASK_16_4) {
                    TaskService.gI().sendNextTaskMain(player);  
                  }
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }
 public static Npc ongGohan_ongMoori_ongParagus1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chơi vui vẻ nhé con!"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                                "Điểm Danh", "Nhận\n Ngọc Xanh", "Kích hoạt\n Tài khoản", "Nhập\nGiftcode", "Nhận đệ tử");
                    }
                }
               
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
//                            case 0:
//                                if (player.diemdanh < 1) {
//                                    int tv = 0;
//                                    int hn = 0;
//                                    switch (player.vip) {
//                                        case 0:
//                                            hn = 5000;
//                                            break;
//                                        case 1:
//                                            tv = 10;
//                                            hn = 10000;
//                                            break;
//                                        case 2:
//                                            tv = 15;
//                                            hn = 15000;
//                                            break;
//                                        case 3:
//                                            tv = 20;
//                                            hn = 20000;
//                                            break;
//                                        case 4:
//                                            tv = 25;
//                                            hn = 25000;
//                                            break;
//                                    }
//                                    player.inventory.ruby += hn;
//                                    Item thoivang = ItemService.gI().createNewItem((short) 457, tv);
//                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    Service.gI().sendMoney(player);
//                                    player.diemdanh++;
//                                    Service.getInstance().sendThongBao(player, "|7|Điểm danh thành công!\nNhận được " + tv + " Thỏi vàng và " + Util.format(hn) + " Hồng ngọc");
//                                } else {
//                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
//                                }
//                                break;
                            case 1:
                                if (player.inventory.gem >= 2000000000) {
                                    this.npcChat(player, "Dùng hết rồi hãy nhận tiếp, đừng tham lam nhé con!");
                                    break;
                                }
                                player.inventory.gem = 2000000000;
                                Service.gI().sendMoney(player);
                                Service.gI().sendThongBao(player, "Bạn vừa nhận được 2 Tỉ Ngọc Xanh");
                                break;
//                            case 2:
//                                this.createOtherMenu(player, 0, "|7|Kích Hoạt Tài Khoản\n"
//                                        + "|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
//                                        + "\n|5|Trạng thái tài khoản : " + (player.getSession().actived  ? "Chưa kích hoạt" : "Đã kích hoạt")
//                                        + "\n|2|Trạng thái VIP : " + (player.vip == 1 ? "VIP" : player.vip == 2 ? "VIP2" : player.vip == 3 ? "VIP3" : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt")
//                                        + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip) : ""),
//                                        "Kích Hoạt\nTài Khoản", "Kích Hoạt\nVIP", "Đóng");
//                                break;
                            case 3:
                                Input.gI().createFormGiftCode(player);
                                break;
                            case 4:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.getInstance().sendThongBao(player,
                                            "Con vừa nhận được đệ tử! Hãy chăm sóc nó nhé");
                                } else {
                                    this.npcChat(player, "Đã có đệ tử rồi mà!");

                                }
                                break;
                        }
//                    } else if (player.iDMark.getIndexMenu() == 0) {
//                        switch (select) {
//                            case 1:
//                                this.createOtherMenu(player, 1, "|7|MUA THẺ VIP THÁNG\n"
//                                        + "|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
//                                        + "\n|2|Trạng thái VIP : " + (player.vip == 1 ? "VIP" : player.vip == 2 ? "VIP2" : player.vip == 3 ? "VIP3" : player.vip == 4 ? "SVIP" : "Chưa Kích Hoạt")
//                                        + (player.timevip > 0 ? "\n|5|Hạn còn : " + Util.msToThang(player.timevip) : ""),
//                                        "Kích Hoạt\nVIP1\n50.000Đ", "Kích Hoạt\nVIP2\n70.000Đ", "Kích Hoạt\nVIP3\n100.000Đ", "Kích Hoạt\nSVIP\n150.000Đ", "Đóng");
//                                break;
//                            case 0:
//                                this.createOtherMenu(player, ConstNpc.CONFIRM_ACTIVE, "|5|Mở thành viên Free\n" + "|5|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ" + "\n|7|Trạng thái : " + (player.getSession().actived ? "Chưa kích hoạt" : "Đã kích hoạt"), "Kích hoạt", "Đóng");
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 1) {
//                        switch (select) {
//                            case 0:
//                                this.createOtherMenu(player, 2, "|7|VIP1\n"
//                                        + "|2|Quyền lợi đi kèm\n"
//                                        + "|5|Nhận 10 Thỏi Vàng/ngày"
//                                        + "\nNhận 10.000 Hồng Ngọc/ngày"
//                                        + "\nTăng 10% TNSM"
//                                        + "\nDanh hiệu VIP1"
//                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
//                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
//                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
//                                break;
//                            case 1:
//                                this.createOtherMenu(player, 3, "|7|VIP2\n"
//                                        + "|2|Quyền lợi đi kèm\n"
//                                        + "|5|Nhận 15 Thỏi Vàng/ngày"
//                                        + "\nNhận 15.000 Hồng Ngọc/ngày"
//                                        + "\nTăng 10% TNSM"
//                                        + "\nTăng 5% Rơi Ngọc Rồng"
//                                        + "\nTăng 10% Dò Pha Lê"
//                                        + "\nDanh hiệu VIP2"
//                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
//                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
//                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
//                                break;
//                            case 2:
//                                this.createOtherMenu(player, 4, "|7|VIP3\n"
//                                        + "|2|Quyền lợi đi kèm\n"
//                                        + "|5|Nhận 20 Thỏi Vàng/ngày"
//                                        + "\nNhận 20.000 Hồng Ngọc/ngày"
//                                        + "\nTăng 15% TNSM"
//                                        + "\nTăng 10% Rơi Ngọc Rồng"
//                                        + "\nTăng 15% Dò Pha Lê"
//                                        + "\nDanh hiệu VIP3"
//                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
//                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
//                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
//                                break;
//                            case 3:
//                                this.createOtherMenu(player, 5, "|7|SVIP\n"
//                                        + "|2|Quyền lợi đi kèm\n"
//                                        + "|5|Nhận 25 Thỏi Vàng/ngày"
//                                        + "\nNhận 25.000 Hồng Ngọc/ngày"
//                                        + "\nTăng 20% TNSM"
//                                        + "\nTăng 15% Rơi Ngọc Rồng"
//                                        + "\nTăng 20% Dò Pha Lê"
//                                        + "\nDanh hiệu VIP4"
//                                        + "\n|2|Số tiền hiện tại : " + Util.format(player.getSession().vnd) + " VNĐ"
//                                        + (player.vip == 1 ? "\n|7|Trạng thái VIP : VIP1" : player.vip == 2 ? "\n|7|Trạng thái VIP : VIP2" : player.vip == 3 ? "\n|7|Trạng thái VIP : VIP3" : player.vip == 4 ? "\n|7|Trạng thái VIP : SVIP" : "")
//                                        + (player.timevip > 0 ? "\nHạn còn : " + Util.msToThang(player.timevip) : ""), "Kích Hoạt", "Đóng");
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 2) {
//                        switch (select) {
//                            case 0:
//                                if (player.vip >= 1) {
//                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
//                                    return;
//                                }
//                                if (player.getSession().vnd >= 50000) {
//                                    player.vip = 1;
//                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
//                                    PlayerDAO.subvndmua(player, 50000);
//                                    Service.gI().sendMoney(player);
//                                    this.npcChat(player, "|6|Đã mở thành công\n|7|VIP1");
//                                } else {
//                                    this.npcChat(player, "Bạn không đủ tiền");
//                                }
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 3) {
//                        switch (select) {
//                            case 0:
//                                if (player.vip >= 2) {
//                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
//                                    return;
//                                }
//                                if (player.getSession().vnd >= 70000) {
//                                    player.vip = 2;
//                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
//                                    PlayerDAO.subvndmua(player, 70000);
//                                    Service.gI().sendMoney(player);
//                                    this.npcChat(player, "|6|Đã mở thành công\n|7|VIP2");
//                                } else {
//                                    this.npcChat(player, "Bạn không đủ tiền");
//                                }
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 4) {
//                        switch (select) {
//                            case 0:
//                                if (player.vip >= 3) {
//                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
//                                    return;
//                                }
//                                if (player.getSession().vnd >= 100000) {
//                                    player.vip = 3;
//                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
//                                    PlayerDAO.subvndmua(player, 100000);
//                                    Service.gI().sendMoney(player);
//                                    this.npcChat(player, "|6|Đã mở thành công\n|7|VIP3");
//                                } else {
//                                    this.npcChat(player, "Bạn không đủ tiền");
//                                }
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 5) {
//                        switch (select) {
//                            case 0:
//                                if (player.vip >= 4) {
//                                    this.npcChat(player, "|7|Bạn đang là thành viên " + (player.vip == 4 ? "SVIP" : "VIP" + player.vip) + " rồi");
//                                    return;
//                                }
//                                if (player.getSession().vnd >= 150000) {
//                                    player.vip = 4;
//                                    player.timevip = (System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) + (1000 * 60 * 60 * 24 * 16);
//                                    PlayerDAO.subvndmua(player, 150000);
//                                    Service.gI().sendMoney(player);
//                                    this.npcChat(player, "|6|Đã mở thành công\n|7|SVIP");
//                                } else {
//                                    this.npcChat(player, "Bạn không đủ tiền");
//                                }
//                                break;
//                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.CONFIRM_ACTIVE) {
                        switch (select) {
                            case 0:
                                if (select == 0) {
                                    if (!player.getSession().actived) {
                                    if (player.getSession().vnd >= 10000) {
                                        if (PlayerDAO.subvnd(player, 10000)) {
                                            player.getSession().actived = true;
                                            PlayerDAO.activePlayer(player);
                                            this.npcChat(player, "Kích hoạt thành viên thành công!");
                                            player.inventory.ruby += 20000;
                                            player.getSession().vnd -= 10000;
                                            Service.gI().sendMoney(player);

                                        }
                                    } else {
                                        this.npcChat(player, "Kích hoạt thành viên không thành công");
                                    }
                                } else {
                                    this.npcChat(player, "Bạn đã kích hoạt thành viên rồi!");
                                    }
                                }
                                break;  
                            case 1:
                                this.npcChat(player, "Lần sau tiếp lúa cho ta nữa nha con!!!");
                                break;
                        }
                    }
                }

            }

        };
    }
    public static Npc ongGohan_ongMoori_ongParagus(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
                public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    String checkvnd;
                    int thoivang;
                    if (player.vnd >= 0 && player.vnd < 500000) {
                        checkvnd = "Tân Thủ";
                        thoivang = 5;
                    } else if (player.vnd > 500000 && player.vnd < 1000000) {
                        checkvnd = "VIP";
                        thoivang = 10;
                    } else {
                        checkvnd = "SVIP";
                        thoivang = 50;
                    }
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                       "|5|Chơi Game Vui Vẻ Nhé Con!"
                                        + "\n|1|Cảnh giới: " + player.TamkjllTuviTutien(Util.TamkjllGH(player.Tamkjlltutien[1]))
                                        + "\n|1|Sức Mạnh Của Con : " + Util.getFormatNumber(player.nPoint.power)
                                        + "\n|7|Số tiền của bạn còn : " + player.getSession().vnd 
                                        + "\n|6|Tổng Số Tiền Con Nạp : " + player.getSession().tongnap + "\n"
                                        .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta"),
                       
                           "Nhập\nGiftCode", "Nhận\nNgọc", "Nhận đệ tử","Hỗ trợ\nhiệm vụ","Mở Thành Viên","Fram Vàng\nHồng Ngọc");//, "Giftcode","Hỗ Trợ\nNV");

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                        //    case 0:
                        //        Input.gI().createFormChangePassword(player);
                        //        break;
                            case 7:
                                createOtherMenu(player, ConstNpc.MIENPHI,
                                        "Con muốn nhận miễn phí phần thưởng gì?",
                                        "Nhận 1 Tr\nNgọc Xanh","Quà\nThành viên","Từ chối");
                                break;
                            case 2:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                    Service.gI().sendThongBaoOK(player, "Chức mừng bạn đã nhận được đệ tử!! hãy mau mau tu luyện cho đệ tử trở nên càng mạnh nào.");
                                } else {
                                    this.npcChat(player, "Bạn đã có rồi");
                                }
                                break;    
                            case 1:
                                if (player.inventory.gem == 5000000) {
                                    this.npcChat(player, "Tham Lam");
                                    break;
                                }
                                player.inventory.gem = 5000000;
                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 5 Triệu ngọc xanh");
                                break;                                
                            case 0:
                                Input.gI().createFormGiftCode(player);
                                break;
//                                  case 4:
//                                    ShopServiceNew.gI().opendShop(player, "CAI_TRANG", false);                                                                      
//                                    break;
                                    case 5:
                                //đến Map UP bóng tối
                                ChangeMapService.gI().changeMapBySpaceShip(player, 161, -1, 144);
                                break; 
                            case 4:
                                if (!player.getSession().actived) {
                                    if (player.getSession().vnd >= 10000) {
                                        if (PlayerDAO.subvnd(player, 10000)) {
                                            player.getSession().actived = true;
                                            PlayerDAO.activePlayer(player);
                                            this.npcChat(player, "Kích hoạt thành viên thành công!");
                                            player.inventory.ruby += 20000;
                                            player.getSession().vnd -= 10000;
                                            Service.gI().sendMoney(player);
                                        }
                                    } else {
                                        this.npcChat(player, "Kích hoạt thành viên không thành công");
                                    }
                                } else {
                                    this.npcChat(player, "Bạn đã kích hoạt thành viên rồi!");

                                }
                                break;  
                            case 6:
                                if (!player.getSession().actived) {
                                        Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");
                                   } else
                                if (player.diemdanh == 0) {
                                    int thoivang1;
                                    if (player.vnd >= 0 && player.vnd < 500000) {
                                        thoivang1 = 5;
                                    } else if (player.vnd > 500000 && player.vnd < 1000000) {
                                        thoivang1 = 10;
                                    } else {
                                        thoivang1 = 50;
                                    }
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, thoivang1);
                                    InventoryServiceNew.gI().addItemBag(player, thoivang);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemdanh = System.currentTimeMillis();
                                    Service.getInstance().sendThongBao(player, "|7|Bạn vừa nhận được " + thoivang1 + " Thỏi vàng");
                                } else {
                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                }
                                break;
                        
                        
//                            case 4:
//                            createOtherMenu(player, ConstNpc.quanap,
//                                    "Hiện tại bạn đang có " + player.getSession().tongnap + " điểm\n"
//                                            ,
//                                    "Mốc\n20k", "Mốc\n50k","Mốc\n100k", "Mốc\n200k","Mốc\n500k");
//                            
                            //Input.gI().createFormQDHNTV(player);
//                             break;    
                            case 3: {
                                if (player.playerTask.taskMain.id == 11) {
                                    if (player.playerTask.taskMain.index == 0) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_0);
                                    } else if (player.playerTask.taskMain.index == 1) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_1);
                                    } else if (player.playerTask.taskMain.index == 2) {
                                        TaskService.gI().DoneTask(player, ConstTask.TASK_11_2);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Ta đã giúp con hoàn thành nhiệm vụ rồi mau đi trả nhiệm vụ");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Chỉ Hỗ Trợ NHiệm Vụ: Kết giao");
                                }
                       
                                break;
                            }
                        }
                        
                    }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.MIENPHI) {
                            switch (select) {
                                case 0:
                                    if (player.inventory.gem == 2000000000) {
                                    this.npcChat(player, "Tham Lam");
                                    break;
                                }
                                player.inventory.gem = 1000000;
                                Service.getInstance().sendMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1Tr Ngọc Xanh");
                                break;
                                case 1:
                            if (player.getSession().actived){
                                if (player.nhanqua2 == 0){
                                    short[] rdpet1 = new short[]{1428,1429,1430,1431};
                                        Item _item = ItemService.gI().createNewItem((short)rdpet1[Util.nextInt(rdpet1.length-1)], 1);                                            
                                        _item.itemOptions.add(new Item.ItemOption(50, Util.nextInt(5,10)));
                                        _item.itemOptions.add(new Item.ItemOption(77, Util.nextInt(5,10)));
                                        _item.itemOptions.add(new Item.ItemOption(103, Util.nextInt(5,10)));
                                        _item.itemOptions.add(new Item.ItemOption(101, Util.nextInt(40,50))); 
                                        _item.itemOptions.add(new Item.ItemOption(106, 0));
                                        InventoryServiceNew.gI().addItemBag(player, _item);                                        
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + _item.template.name);                                        
                                        ServerNotify.gI().notify("Chúc mừng " + player.name + " nhận được "+_item.template.name+" " );
                                    player.nhanqua2++;
                                }else{
                                    Service.getInstance().sendThongBao(player, "Đã nhận thưởng rồi");
                                }
                            }else{
                                if(player.gender == 0) {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trở về nhà gặp ông Gohan mở thành viên. Sau đó hãy quay lại đây", "Tạm biệt");
                                }if(player.gender == 1) {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trở về nhà gặp ông Moori mở thành viên. Sau đó hãy quay lại đây", "Tạm biệt");
                                }if(player.gender == 2) {
                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trở về nhà gặp ông Paragus mở thành viên. Sau đó hãy quay lại đây", "Tạm biệt");
                                }
                            }
                            break;
                                
                            }
                        }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.QUA_TAN_THU) {
                        switch (select) {
                            case 2:
                                if (nhanDeTu) {
                                    if (player.pet == null) {
                                        PetService.gI().createNormalPet(player);
                                        Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                    } else {
                                        this.npcChat("Con đã nhận đệ tử rồi");
                                    }
                                }
                                break;
                        }
                    }
//                    else if (player.iDMark.getIndexMenu() == ConstNpc.quanap) {
//                        switch (select) {
//                            case 0:
//                        if(player.getSession().tongnap >= 20000)   {
//                            if (player.mocnap1 == 0){
//                            //PlayerDAO.updatediemthuongnap(player,10);
//                                Item _item = ItemService.gI().createNewItem((short)457, 10); 
//                                
//                                Item _item4 = ItemService.gI().createNewItem((short) 1130,10);
//                                player.mocnap1 += 1;
//                                Service.getInstance().sendMoney(player);
//                                InventoryServiceNew.gI().addItemBag(player, _item);
//                                  Item i0 = ItemService.gI().createNewItem((short) 2091, 10);
//                                    Item i1 = ItemService.gI().createNewItem((short) 2092, 10);
//                                    Item i2 = ItemService.gI().createNewItem((short) 2093, 10);
//                                    Item i00 = ItemService.gI().createNewItem((short) 381, 99);
//                                    Item i10 = ItemService.gI().createNewItem((short) 382, 99);
//                                    Item i20 = ItemService.gI().createNewItem((short) 383, 99);
//                                    Item i200= ItemService.gI().createNewItem((short) 384, 99);
//                                    Item i3 = ItemService.gI().createNewItem((short) 14, 10);
//                                    Item i4 = ItemService.gI().createNewItem((short) 15, 10);
//                                    Item i5 = ItemService.gI().createNewItem((short) 16, 10);
//                                    Item i6 = ItemService.gI().createNewItem((short) 17, 10);
//                                    Item i7 = ItemService.gI().createNewItem((short) 18, 10);
//                                    Item i9 = ItemService.gI().createNewItem((short) 19, 10);
//                                    Item i8 = ItemService.gI().createNewItem((short) 20, 10);
//                                    InventoryServiceNew.gI().addItemBag(player, i0);
//                                    InventoryServiceNew.gI().addItemBag(player, i1);
//                                    InventoryServiceNew.gI().addItemBag(player, i2);
//                                    InventoryServiceNew.gI().addItemBag(player, i00);
//                                    InventoryServiceNew.gI().addItemBag(player, i10);
//                                    InventoryServiceNew.gI().addItemBag(player, i20);
//                                    InventoryServiceNew.gI().addItemBag(player, i200);
//                                    InventoryServiceNew.gI().addItemBag(player, i4);
//                                    InventoryServiceNew.gI().addItemBag(player, i3);
//                                    InventoryServiceNew.gI().addItemBag(player, i5);
//                                    InventoryServiceNew.gI().addItemBag(player, i6);
//                                    InventoryServiceNew.gI().addItemBag(player, i7);
//                                    InventoryServiceNew.gI().addItemBag(player, i8);
//                                    InventoryServiceNew.gI().addItemBag(player, i9);
//                                InventoryServiceNew.gI().addItemBag(player, _item4);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được 10 " + _item.template.name);
//                                
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được 10 " + _item4.template.name);
//                            }else{
//                                Service.getInstance().sendThongBao(player, "Đã nhận thưởng này rồi");
//                            }
//                            break;
//                        }else{
//                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
//                        }
//                        break;
//                        case 1:
//                            if(player.getSession().tongnap >= 50000)   {
//                               if (player.mocnap1 == 1){
//                            //PlayerDAO.updatediemthuongnap(player,10);
//                                Item item = ItemService.gI().createNewItem((short) 1558,1);
//                                Item item2 = ItemService.gI().createNewItem((short) 457,15);
//                                  Item i0 = ItemService.gI().createNewItem((short) 2091, 10);
//                                    Item i1 = ItemService.gI().createNewItem((short) 2092, 10);
//                                    Item i2 = ItemService.gI().createNewItem((short) 2093, 10);
//                                    Item i00 = ItemService.gI().createNewItem((short) 381, 99);
//                                    Item i10 = ItemService.gI().createNewItem((short) 382, 99);
//                                    Item i20 = ItemService.gI().createNewItem((short) 383, 99);
//                                    Item i200= ItemService.gI().createNewItem((short) 384, 99);
//                                    Item i3 = ItemService.gI().createNewItem((short) 14, 10);
//                                    Item i4 = ItemService.gI().createNewItem((short) 15, 10);
//                                    Item i5 = ItemService.gI().createNewItem((short) 16, 10);
//                                    Item i6 = ItemService.gI().createNewItem((short) 17, 10);
//                                    Item i7 = ItemService.gI().createNewItem((short) 18, 10);
//                                    Item i9 = ItemService.gI().createNewItem((short) 19, 10);
//                                    Item i8 = ItemService.gI().createNewItem((short) 20, 10);
//                                    InventoryServiceNew.gI().addItemBag(player, i0);
//                                    InventoryServiceNew.gI().addItemBag(player, i1);
//                                    InventoryServiceNew.gI().addItemBag(player, i2);
//                                    InventoryServiceNew.gI().addItemBag(player, i00);
//                                    InventoryServiceNew.gI().addItemBag(player, i10);
//                                    InventoryServiceNew.gI().addItemBag(player, i20);
//                                    InventoryServiceNew.gI().addItemBag(player, i200);
//                                    InventoryServiceNew.gI().addItemBag(player, i4);
//                                    InventoryServiceNew.gI().addItemBag(player, i3);
//                                    InventoryServiceNew.gI().addItemBag(player, i5);
//                                    InventoryServiceNew.gI().addItemBag(player, i6);
//                                    InventoryServiceNew.gI().addItemBag(player, i7);
//                                    InventoryServiceNew.gI().addItemBag(player, i8);
//                                    InventoryServiceNew.gI().addItemBag(player, i9);
//                                Item item4 = ItemService.gI().createNewItem((short) 1130,15);
//                                
//                                player.mocnap1 += 1;
//                                item.itemOptions.add(new Item.ItemOption(50,7));
//                                item.itemOptions.add(new Item.ItemOption(77,7));
//                                item.itemOptions.add(new Item.ItemOption(103,7));
//                                item.itemOptions.add(new Item.ItemOption(30,0));
//                                Service.getInstance().sendMoney(player);
//                                InventoryServiceNew.gI().addItemBag(player, item);
//                                InventoryServiceNew.gI().addItemBag(player, item2);
//                                
//                                InventoryServiceNew.gI().addItemBag(player, item4);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item.template.name);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item2.template.name);
//                                
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item4.template.name);
//                                }else{
//                                Service.getInstance().sendThongBao(player, "Đã nhận thưởng hoặc chưa nhận phần thưởng trước đó");
//                                }
//                                break;
//                            }else{
//                                Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
//                            }
//                            break;
//                        case 2:
//                            if(player.getSession().tongnap >= 100000)   {
//                                if (player.mocnap1 == 2){
//                            //PlayerDAO.updatediemthuongnap(player,10);
//                                Item item = ItemService.gI().createNewItem((short) 1557,1);
//                                Item item2 = ItemService.gI().createNewItem((short) 457,30);
//                               
//                                Item item4 = ItemService.gI().createNewItem((short) 1130,50);
//                                player.mocnap1 += 1;
//                                item.itemOptions.add(new Item.ItemOption(50,11));
//                                item.itemOptions.add(new Item.ItemOption(77,11));
//                                item.itemOptions.add(new Item.ItemOption(103,11));
//                                item.itemOptions.add(new Item.ItemOption(95,5));
//                                item.itemOptions.add(new Item.ItemOption(96,5));
//                                item.itemOptions.add(new Item.ItemOption(30,0));
//                                Service.getInstance().sendMoney(player);
//                                InventoryServiceNew.gI().addItemBag(player, item);
//                                InventoryServiceNew.gI().addItemBag(player, item2);
//                                
//                                InventoryServiceNew.gI().addItemBag(player, item4);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item.template.name);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item2.template.name);
//                                
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item4.template.name);
//                            }else{
//                                 Service.getInstance().sendThongBao(player, "Đã nhận thưởng hoặc chưa nhận phần thưởng trước đó");
//                            }
//                                break;
//                        }else{
//                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
//                        }
//                            break;
//                        case 3:
//                            if(player.getSession().tongnap >= 200000)   {
//                            if (player.mocnap1 == 3){
//                            //PlayerDAO.updatediemthuongnap(player,10);
//                            Item item = ItemService.gI().createNewItem((short) 1556,1);
//                                Item item2 = ItemService.gI().createNewItem((short) 457,50);
//                                
//                                Item item4 = ItemService.gI().createNewItem((short) 1130,100);
//                            
//                                player.mocnap1 += 1;
//                                item.itemOptions.add(new Item.ItemOption(50,15));
//                                item.itemOptions.add(new Item.ItemOption(77,15));
//                                item.itemOptions.add(new Item.ItemOption(103,15));
//                                item.itemOptions.add(new Item.ItemOption(95,10));
//                                item.itemOptions.add(new Item.ItemOption(96,10));
//                                item.itemOptions.add(new Item.ItemOption(14,5));
//                                item.itemOptions.add(new Item.ItemOption(30,0));
//                            //item.itemOptions.add(new Item.ItemOption(5,23));
//                            //Service.getInstance().sendMoney(player);
//                                InventoryServiceNew.gI().addItemBag(player, item);
//                                InventoryServiceNew.gI().addItemBag(player, item2);
//                                
//                                InventoryServiceNew.gI().addItemBag(player, item4);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item.template.name);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item2.template.name);
//                                
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item4.template.name);
//                        }else{
//                            Service.getInstance().sendThongBao(player, "Đã nhận thưởng hoặc chưa nhận phần thưởng trước đó");
//                        }
//                            break;
//                        }else{
//                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
//                        }
//                            break;
//                        case 4:
//                            if(player.getSession().tongnap >= 500000)   {
//                            if (player.mocnap1 == 4){
//                            //PlayerDAO.updatediemthuongnap(player,10);
//                            Item item = ItemService.gI().createNewItem((short) 1555,1);
//                                Item item2 = ItemService.gI().createNewItem((short) 457,100);
//                                
//                                Item item4 = ItemService.gI().createNewItem((short) 1130,200);
//                            
//                                player.mocnap1 += 1;
//                                item.itemOptions.add(new Item.ItemOption(50,19));
//                                item.itemOptions.add(new Item.ItemOption(77,19));
//                                item.itemOptions.add(new Item.ItemOption(103,19));
//                                item.itemOptions.add(new Item.ItemOption(95,12));
//                                item.itemOptions.add(new Item.ItemOption(96,12));
//                                item.itemOptions.add(new Item.ItemOption(14,10));
//                                item.itemOptions.add(new Item.ItemOption(5,10));
//                                item.itemOptions.add(new Item.ItemOption(30,0));
//                            //item.itemOptions.add(new Item.ItemOption(5,23));
//                            //Service.getInstance().sendMoney(player);
//                                InventoryServiceNew.gI().addItemBag(player, item);
//                                InventoryServiceNew.gI().addItemBag(player, item2);
//                                
//                                InventoryServiceNew.gI().addItemBag(player, item4);
//                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item.template.name);
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item2.template.name);
//                               
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được " + item4.template.name);
//                        }else{
//                            Service.getInstance().sendThongBao(player, "Đã nhận thưởng hoặc chưa nhận phần thưởng trước đó");
//                        }
//                            break;
//                        }else{
//                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
//                        }
//                            break;
//                        }
//                    }
//                    else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_THUONG) {
//                        switch (select) {

//                        }
//                    }
                }

            }

        };
    }

      public static Npc npc70(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {//test di
             public void chatWithNpc(Player player) {
                String[] chat = {
                  "Mấy Thằng Tu Tiên Què Chưa Có Đệ Thì Đến Đây"

                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 1000, 1001);
            }

            @Override
            public void openBaseMenu(Player pl) {
                 chatWithNpc(pl);
                if (canOpenNpc(pl)) {
                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                            "\b|1|Đây là nơi ngươi có thể đổi bất cứ thứ gì\nMiễn là ngươi có tiền \n\b|7|Bạn đang có :" + pl.getSession().vnd + " VNĐ",
                            "Đổi đệ", "Shop Skill");

                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {                        
                            case 0:
                                this.createOtherMenu(player, 781,
                                        "\b|2|Muốn đổi đệ mới bằng VND à?\n Tùy chọn 1: Đệ tử Ngộ Không (hợp thể + 200%)\n Tùy chọn 2: Đệ tử Whis (hợp thể + 300%)\n Tùy chọn 3: Đệ tử Uron (hợp thể + 500%)\n \b|7|Bạn đang có :" + player.getSession().vnd + " VNĐ",
                                        "Đệ Ngộ Không\n25K", "Đệ Whis\n70K", "Đệ Uron\n130K");
                                break;
                            case 1:
//                                if (player.getSession().vnd < 1) {
//                                    Service.gI().sendThongBao(player, "Yêu Cầu Có Ít Nhất 1 Đồng Trong tài Khoản");
//                                    return;
//                                }   //luwdev
                                   ShopServiceNew.gI().opendShop(player, "DETUVIP", false);
                                
                       
                              break;                          
                        }

            
                    } else if (player.iDMark.getIndexMenu() == 781) {
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd < 25000) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ 25K VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, 25000)) {
                                    PetService.gI().changeBerusPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Ngộ Không ");
                                }
                                break;
                            case 1:
                                if (player.getSession().vnd < 70000) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ 70K VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, 70000)) {
                                    PetService.gI().changeWhisPet(player, player.gender);
                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Whis");
                                }
                                break;
                                  case 2:
                                if (player.getSession().vnd < 130000) {
                                    Service.gI().sendThongBao(player, "Bạn không đủ 130K VND");
                                    return;
                                }

                                if (PlayerDAO.subvnd(player, 130000)) {

                                    PetService.gI().changeGokuPet(player, player.gender);

                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Uron");
                                }
                                break;  
                                
//                            case 4:
//                                if (player.getSession().vnd < 150000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 150000 VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 150000)) {
//                                    PetService.gI().changeHeartPet(player, player.gender);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Heart");
//                                }
//                                break;
//
//                            case 5:
//                                if (player.getSession().vnd < 150000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 150000 VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 150000)) {
//                                    PetService.gI().changeMaiPet(player, player.gender);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Mai");
//                                }
//                                break;
//
//                            case 6:
//                                if (player.getSession().vnd < 150000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 150k VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 150000)) {
//
//                                    PetService.gI().changeGohanPet(player, player.gender);
//
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Gohan");
//                                }
//                                break;
//                            case 7:
//                                if (player.getSession().vnd < 200000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 200000 VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 200000)) {
//
//                                    PetService.gI().changeJirenPet(player, player.gender);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Jiren");
//                                }
//                                break;
//
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 783) {
//                        switch (select) {
//                            case 0:
//                                if (player.getSession().vnd < 350000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 350000 VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 350000)) {
//                                    PetService.gI().changeBlack3Pet(player, player.gender);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku Black SSJ3");
//                                }
//                                break;
//                            case 1:
//                                if (player.getSession().vnd < 350000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 350000 VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 350000)) {
//                                    PetService.gI().changeGoku4Pet(player, player.gender);
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku SSJ4");
//                                }
//                                break;
//
//                            case 2:
//                                if (player.getSession().vnd < 350000) {
//                                    Service.gI().sendThongBao(player, "Bạn không đủ 350000 VND");
//                                    return;
//                                }
//
//                                if (PlayerDAO.subvnd(player, 350000)) {
//
//                                    PetService.gI().changeGokuUltraPet(player, player.gender);
//
//                                    Service.gI().sendThongBao(player, "Bạn đã nhận được đệ Goku Vô Cực");
//                                }
//                                break;
//                        }
//                    } else if (player.iDMark.getIndexMenu() == 782) {
//                        switch (select) {
//                            case 0:
//                                if (player.getSession().actived) {
//                                    Service.gI().sendThongBao(player, "Bạn đã mở thành viên rồi");
//                                    return;
//                                }
//                                if (player.getSession().vnd < 10000) {
//                                    Service.gI().sendThongBao(player, "Cần nạp 10K để mở khóa giao dịch");
//                                    return;
//                                }
//                                if (PlayerDAO.subvnd(player, 10000)) {
//                                    player.getSession().actived = true;
//
//                                    if (PlayerDAO.activedUser(player)) {
//                                        Service.gI().sendThongBao(player, "Bạn đã mở thành viên thành công");
//                                    } else {
//                                        Service.gI().sendThongBao(player, "Đã có lỗi xẩy ra khi kích hoạt tài khoản, vui long liên hệ admin nếu bị trừ tiền mà không kích hoạt được, chụp lại thông báo này");
//                                    }
//                                }
//                                break;
//                            case 1:
//
//                                break;

                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaQK(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            
           public void Npcchat(Player player) {
                String[] chat = {
                    "Giúp Ta đẫn Hổ mập Về Nhà",
                    "Em buông tay anh vì lí do gì ",
                    "Người hãy nói đi , đừng Bắt Anh phải nghĩ suy"
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 6000, 6000);
            }
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Cậu cần trang bị gì cứ đến chỗ tôi nhé\n Bạn đang có\b|7| " + player.diemhotong  +" Điểm đi buôn", "Cửa\nhàng","Hộ tống","Cửa hàng\nHộ tống","TOP Hộ Tống","hướng dẫn\nhộ tống");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.TRAI_DAT) {
                                    ShopServiceNew.gI().opendShop(player, "BUNMA", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                }
                                break;
                        
                        case 1:
                                Boss oldDuongTank = BossManager.gI().getBossById(Util.createIdDuongTank((int) player.id));
                                if (oldDuongTank != null) {
                                    this.npcChat(player, " Bé rồng đang được hộ tống" + oldDuongTank.zone.zoneId);
                                } else if (player.inventory.gold< 500000000) {
                                  //  player.inventory.ruby -= 2000;
                                    this.npcChat(player, "Nhà ngươi không đủ 500 triệu vàng");
                                } else {
                                
                                        BossData bossDataClone = new BossData(
                                            "Bé rồng do" +" "+ player.name + " hộ tống",
                                            (byte) 2,
                                            new short[]{1660, 1661, 1662, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                                            100000,
                                            new long[]{(int)player.nPoint.hpMax * 2},
                                            new int[]{103},
                                            new int[][]{
                                            {Skill.TAI_TAO_NANG_LUONG, 7, 15000}},
                                            new String[]{}, //text chat 1
                                            new String[]{}, //text chat 2
                                            new String[]{}, //text chat 3
                                            60
                                    );

                                    try {
                                        DuongTank dt = new DuongTank(Util.createIdDuongTank((int) player.id), bossDataClone, player.zone, player.location.x - 20, player.location.y);
                                        dt.playerTarger = player;
                                        int[] map = {6,29,30,4,5,27,28};
                                        dt.mapCongDuc = map[Util.nextInt(map.length)];
                                        player.haveDuongTang = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //trừ vàng khi gọi boss
                                    player.inventory.gold -= 500000000;
                                    Service.getInstance().sendMoney(player);
                                break;
                                }
                                case 2://Shop
                                    ShopServiceNew.gI().opendShop(player, "QUA_BOSS", true);
                                //     NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CHUA_UPDATE);
                                
                                break;
                                case 3:
                                    Service.getInstance().sendThongBaoOK(player,TopService.gethotong());   
                        break;
                                 case 4:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_HO_TONG);
                                
                                break;
                        }    
                    }
            }
        }
     };
     }         
    public static Npc dende(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        if (player.idNRNM != -1) {
                            if (player.zone.map.mapId == 7) {
                                this.createOtherMenu(player, 1, "Ồ, ngọc rồng namếc, bạn thật là may mắn\nnếu tìm đủ 7 viên sẽ được Rồng Thiêng Namếc ban cho điều ước", "Hướng\ndẫn\nGọi Rồng", "Gọi rồng", "Từ chối");
                            }
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Anh cần trang bị gì cứ đến chỗ em nhé", "Cửa\nhàng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.NAMEC) {
                                    ShopServiceNew.gI().opendShop(player, "DENDE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1) {
                        if (player.zone.map.mapId == 7 && player.idNRNM != -1) {
                            if (player.idNRNM == 353) {
                                NgocRongNamecService.gI().tOpenNrNamec = System.currentTimeMillis() + 86400000;
                                NgocRongNamecService.gI().firstNrNamec = true;
                                NgocRongNamecService.gI().timeNrNamec = 0;
                                NgocRongNamecService.gI().doneDragonNamec();
                                NgocRongNamecService.gI().initNgocRongNamec((byte) 1);
                                NgocRongNamecService.gI().reInitNrNamec((long) 86399000);
                                SummonDragon.gI().summonNamec(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Anh phải có viên ngọc rồng Namếc 1 sao");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc appule(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi cần trang bị gì cứ đến chỗ ta nhé", "Cửa\nhàng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0://Shop
                                if (player.gender == ConstPlayer.XAYDA) {
                                    ShopServiceNew.gI().opendShop(player, "APPULE", true);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Về hành tinh hạ đẳng của ngươi mà mua đồ cùi nhé. Tại đây ta chỉ bán đồ cho người Xayda thôi", "Đóng");
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    public static Npc drDrief(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (this.mapId == 84) {
                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất" : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda" );
                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                        }
                    }
                }
            }
            
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 84) {
                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                    } else if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                            case 3:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                     ChangeMapService.gI().changeMapBySpaceShip(player, 217, -1, 124);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;   
                        }
                    }
                }
            }
        };
    }
    public static Npc npc92(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|5|Có Ngọc Hư Vô Không?",
                             "Trang Bị\n Thần","Vật Phẩm","Từ Chối");
             }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                ShopServiceNew.gI().opendShop(player, "DO", false);
                                 break;  
                                case 1:
                                ShopServiceNew.gI().opendShop(player, "DO1", false);
                                 break;
                        }
                    }
                }
            }
        }
      };
    }
    public static Npc cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                    "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                    "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");//,"VamChar");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                break;
                            case 1:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                break;
                            case 3:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                     ChangeMapService.gI().changeMapBySpaceShip(player, 217, -1, 124);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;  
                        }
                    }
                }
            }
        };
    }

    public static Npc cui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_FIND_BOSS = 50000000;

            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                        if (pl.playerTask.taskMain.id == 7) {
                            NpcService.gI().createTutorial(pl, this.avartar, "Hãy lên đường cứu đứa bé nhà tôi\n"
                                    + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                        } else {
                            if (this.mapId == 19) {

                                int taskId = TaskService.gI().getIdTask(pl);
                                switch (taskId) {
                                    case ConstTask.TASK_21_0:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_21_1:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nMập đầu đinh\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    case ConstTask.TASK_21_2:
                                        this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS) + " vàng)", "Đến Cold", "Đến\nNappa", "Từ chối");
                                        break;
                                    default:
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                "Đến Cold", "Đến\nNappa", "Từ chối");

                                        break;
                                }
                            } else if (this.mapId == 68) {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                            } else {
                                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                        "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                        + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                        "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");//,"VamChar");
                            }
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                    break;
                                case 3:
                                if (player.getSession().player.nPoint.power >= 80000000000L) {
                                     ChangeMapService.gI().changeMapBySpaceShip(player, 217, -1, 124);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh để vào");
                                }
                                break;  
                            }
                        }
                    }
                    if (this.mapId == 19) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.KUKU);
                                    if (boss != null || !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z != null && z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.getInstance().sendMoney(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                    }
                                    Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.MAP_DAU_DINH);
                                    if (boss != null && !boss.isDie()) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.getInstance().sendMoney(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    }
                                    Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                            switch (select) {
                                case 0:
                                    Boss boss = BossManager.gI().getBossById(BossID.RAMBO);
                                    if (boss != null && !boss.isDie() || player != null) {
                                        if (player.inventory.gold >= COST_FIND_BOSS) {
                                            Zone z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId, boss.zone.zoneId);
                                            if (z.getNumOfPlayers() < z.maxPlayer) {
                                                player.inventory.gold -= COST_FIND_BOSS;
                                                ChangeMapService.gI().changeMap(player, boss.zone, boss.location.x, boss.location.y);
                                                Service.getInstance().sendMoney(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Không đủ vàng, còn thiếu "
                                                    + Util.numberToMoney(COST_FIND_BOSS - player.inventory.gold) + " vàng");
                                        }
                                        break;
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                    }
                                    Service.getInstance().sendThongBao(player, "Chết rồi ba...");
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                    break;
                                case 2:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 68) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc Kichi(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
                public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Ngươi cần gì ở ta!!\b|1|ta sẽ tặng ngươi những\ngì ngươi muốn với điều kiện....", "Đổi PET", "Đổi linh thú","Đổi Đeo lưng","Đóng");
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {  // pet
                                    Item voso = null;
                                    Item vooc = null;
                                    try {
                                        voso = InventoryServiceNew.gI().findItemBag(player, 2137);
                                        vooc = InventoryServiceNew.gI().findItemBag(player, 2138);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (voso == null || voso.quantity < 99 || vooc == null || vooc.quantity < 99) {
                                        this.npcChat(player, "|1|Bạn không đủ x99 Vỏ sò và x99 vỏ ốc");
                                //    if (vooc == null || vooc.quantity < 99) {
                                //        this.npcChat(player, "Bạn không đủ x99 vỏ ốc");    
                                    } else if (player.inventory.gold < 500_000_000) {
                                        this.npcChat(player, "|1|Bạn không đủ 500 triệu vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "|1|Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 500_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voso, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vooc, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item PETrandom = ItemService.gI().createNewItem((short) 1396);
                                        InventoryServiceNew.gI().addItemBag(player, PETrandom);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Bạn nhận được 1 Túi pet");
                                    }
                                    break;
                                    }
                                
                                case 1: {   // linh thú
                                    Item voso = null;
                                    Item vooc = null;
                                    try {
                                        voso = InventoryServiceNew.gI().findItemBag(player, 2141);
                                        vooc = InventoryServiceNew.gI().findItemBag(player, 2142);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (voso == null || voso.quantity < 99 || vooc == null || vooc.quantity < 99) {
                                        this.npcChat(player, "|1|Bạn không đủ x99 Hạt giống và x99 Lông linh thú");
                                //    if (vooc == null || vooc.quantity < 99) {
                                //        this.npcChat(player, "Bạn không đủ x99 Lông linh thú");    
                                    } else if (player.inventory.gold < 500_000_000) {
                                        this.npcChat(player, "|1|Bạn không đủ 500 triệu vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "|1|Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 500_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voso, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vooc, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item PETrandom = ItemService.gI().createNewItem((short) 1999);
                                        InventoryServiceNew.gI().addItemBag(player, PETrandom);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Bạn nhận được 1 Túi quà linh thú");
                                    }
                                    break;
                                }
                                
                                case 2: {// đổi đeo lưng
                                    Item voso = null;
                                    Item vooc = null;
                                    try {
                                        voso = InventoryServiceNew.gI().findItemBag(player, 2139);
                                        vooc = InventoryServiceNew.gI().findItemBag(player, 2140);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (voso == null || voso.quantity < 99 || vooc == null || vooc.quantity < 99) {
                                        this.npcChat(player, "|1|Bạn không đủ x99 da thú và x99 Lông thú");
                                //    if (vooc == null || vooc.quantity < 99) {
                                //        this.npcChat(player, "Bạn không đủ x99 Lông thú");    
                                    } else if (player.inventory.gold < 500_000_000) {
                                        this.npcChat(player, "|1|Bạn không đủ 500 triệu vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "|1|Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 500_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voso, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vooc, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item PETrandom = ItemService.gI().createNewItem((short) 1998);
                                        InventoryServiceNew.gI().addItemBag(player, PETrandom);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Bạn nhận được 1 Túi quà đeo lưng");
                                    }
                                    break;
                                }
                                
                                
                                
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc Anime(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
                public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|Ngươi cần gì ở ta!!\b|1|ta sẽ tặng ngươi những\ngì ngươi muốn với điều kiện....", "Đổi Thời Trang","Đóng");
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {  // pet
                                    Item voso = null;
                                    Item vooc = null;
                                    Item voso1 = null;
                                    Item vooc2 = null;
                                    try {
                                        voso = InventoryServiceNew.gI().findItemBag(player, 1399);
                                        vooc = InventoryServiceNew.gI().findItemBag(player, 1400);
                                        voso1 = InventoryServiceNew.gI().findItemBag(player, 2150);
                                        vooc2 = InventoryServiceNew.gI().findItemBag(player, 2151);
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (voso == null || voso.quantity < 1 || vooc == null || vooc.quantity < 99 || voso1 == null || voso1.quantity < 99 || vooc2 == null || vooc2.quantity < 99) {
                                        this.npcChat(player, "|1|Bạn không đủ x1 ngọc thạch, x99 Cánh thiên thần + x99 Áo choàng kháng phép + x99 Trượng phép thuật");
                                //    if (vooc == null || vooc.quantity < 99) {
                                //        this.npcChat(player, "Bạn không đủ x99 vỏ ốc");    
                                    } else if (player.inventory.gold < 2_000_000_000) {
                                        this.npcChat(player, "|1|Bạn không đủ 2 Tỷ vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "|1|Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        player.inventory.gold -= 500_000_000;
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voso, 1);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vooc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, voso1, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, vooc2, 99);
                                        Service.getInstance().sendMoney(player);
                                        Item PETrandom = ItemService.gI().createNewItem((short) 2152);
                                        InventoryServiceNew.gI().addItemBag(player, PETrandom);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Bạn nhận được 1 Túi Thời trang Anime");
                                    }
                                    break;
                                    }
                                
                                
                                
                                
                                
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc santa(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Cửa hàng ta có bán các vật phẩm, cải trang đặc biệt cũng như có hỗ trợ\n"
                            + "mua nhanh vật phẩm bằng Thỏi vàng\n"
                            + "Ngươi muốn ta giúp gì?", "Cửa hàng\nSanta","Cửa Hàng", "Vật Phẩm","Trang Bị");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                    int itemId = 722;
                    Item capsulehong = ItemService.gI().createNewItem(((short) itemId));
                    if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "SANTA", false);
                                    break;
                                    case 1:
                                    ShopServiceNew.gI().opendShop(player, "CAI_TRANG", false);                                                                      
                                    break;
                              case 2:
                                    ShopServiceNew.gI().opendShop(player, "FREE", false);
                                    break;
                                    case 3:
                                    ShopServiceNew.gI().opendShop(player, "TRANGBIVIP", false);
                                    break;
                            }
                     //luudeptraiso1tggggggggggg          
                        }
                    }
                }
            }
        };
    }


    public static Npc uron(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player pl) {
                if (canOpenNpc(pl)) {
                    ShopServiceNew.gI().opendShop(pl, "URON", false);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc baHatMit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Ép sao\ntrang bị", "Pha lê\nhóa\ntrang bị", "Bóng tối\ntrang bị"
                                ,"Nâng cấp\ntrang bị", "Gia hạn\nvật phẩm","Ấn\nKích Hoạt"
                                ,"Nâng Cấp\nKích hoạt","Nâng cấp\nchân mệnh"
                                ,"Khảm Đá","Luyện Hóa");//,"ngọc");//,"Nâng cấp\nHủy Diệt","Nâng cấp\nThiên Sứ");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm", "Nhập\nNgọc Rồng","Nâng cấp\nBông tai","Mở chỉ số bông tai");//,
                               // "Nâng cấp\nBông tai\nPorata cấp2", "Nâng cấp\nBông tai\nPorata cấp3","Nâng cấp\nBông tai\nPorata cấp4","Nâng cấp\nBông tai\nPorata cấp5","Mở chỉ số\nBông tai\nPorata Cấp5","Tiệm đá");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                    break;
                                case 2:
                                createOtherMenu(player, ConstNpc.PHAP_SU,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Nâng\ncấp","Xóa\ndòng", "Hướng đẫn");
                                break;
                                case 3:
                                createOtherMenu(player, ConstNpc.NANG_CAP_TRANG_BI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Hiến tế\n THẦN LINH","Hiến tế\n HỦY DIỆT","Hướng dẫn");
                                break;
                                case 4:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.GIA_HAN_VAT_PHAM);
                                    break;
                                case 5:
                                createOtherMenu(player, ConstNpc.TINH_AN_TRANG_BI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Ấn trang bị","Hướng dẫn");
                                break; 
                                case 6:
                                createOtherMenu(player, ConstNpc.SKH,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "NÂNG CẤP\n(1)","NÂNG CẤP\n(2)","Từ Chối");
                                break;
                                case 7:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                    break;    
//                                case 8:
//                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_NGOC_BOI);
//                                    break; 
                                case 8:
                                createOtherMenu(player, ConstNpc.KHAM_DA,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Khảm HP","Khảm Ki", "Khảm Dame","Từ Chối");
                                break;    
                                case 9:
                                createOtherMenu(player, ConstNpc.LUYEN_HOA,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Luyện\nLinh Thú", "Luyện\nPhụ Kiện","Từ Chối");
                                break;     
                                
                            //    case 4:
                            //        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIPts);
                            //        break;    
                            //    case 2:
                            //        CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Tl);
                            //        break;
                            //        case 3:
                            //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd);
                            //        break;
                            //        case 4:
                            //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Ts);
                            //        break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                    case CombineServiceNew.AN_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                    case CombineServiceNew.NANG_CAP_SKH_VIPhd:
                                        case CombineServiceNew.NANG_CAP_SKH_VIPts:
                            //    case CombineServiceNew.PLH_CAITRANG:
                                case CombineServiceNew.PS_HOA_TRANG_BI:
                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI: 
                                    case CombineServiceNew.GIA_HAN_VAT_PHAM:
                                case CombineServiceNew.kh_Tl:
                                case CombineServiceNew.kh_Hd:
                                case CombineServiceNew.kh_Ts:
                                    case CombineServiceNew.KHAM_DA_HP:
                                case CombineServiceNew.KHAM_DA_MP:
                                case CombineServiceNew.KHAM_DA_DAME:
                                    case CombineServiceNew.THANG_HOA_NGOC_BOI:
                                        case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                            case CombineServiceNew.NANG_NGOC_BOI:
                                                case CombineServiceNew.NANG_CAP_PET2:
                                                    case CombineServiceNew.NANG_CAP_PK:
                                                        case CombineServiceNew.CUONG_HOA:
                                                            case CombineServiceNew.NANG_KICH_HOAT_VIP:
                                                                case CombineServiceNew.NANG_KICH_HOAT_VIP2:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;      
                                    }
                                        CombineServiceNew.gI().startCombine(player);
                            }
                        }
                       else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_THANG_HOA_NGOC_BOI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().startCombine1(player, 1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().startCombine1(player, 1);
                                    break;    
                            }}
                       else if (player.iDMark.getIndexMenu() == ConstNpc.LUYEN_HOA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PET2 );
                                    break;  
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PK );
                                    break;    
                            }}
                       else if (player.iDMark.getIndexMenu() == ConstNpc.KHAM_DA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_HP );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_MP );
                                    break; 
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_DAME );
                                    break;    
                            }}
                        else if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU) {
                            switch (select) {
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_PHAP_SU_HOA);
                                    break;
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI );
                                    break;
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_T );
                                    break;
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONGDANHIENTE);
                                    break;  
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.TINH_AN_TRANG_BI) {
                            switch (select) {
                                case 0:
                                //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI );
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CUONG_HOA );
                                    break;
                                case 1:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.AN_TRANG_BI_HUONG_DAN);
                                    break;  
                                
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.SKH) {
                            switch (select) {
                                case 0:
                                //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP );
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_KICH_HOAT_VIP );
                                    break;
                                case 1:
                                //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd);
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_KICH_HOAT_VIP2 );
                                    break;
                                case 2:
                                //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Ts);
                                  //  CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP );
                                    break;    
                                
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                                case 1://nâng cấp vật phẩm
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2://nhập ngọc rồng
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;    
                                case 3:
                                createOtherMenu(player, ConstNpc.NANG_CAP_BONG_TAI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Nâng cấp\nPorata cấp 1","Nâng cấp\nPorata cấp 2", "Nâng cấp\nPorata cấp 3","Nâng cấp\nPorata cấp 4");
                                break;
                                case 4:
                                createOtherMenu(player, ConstNpc.MO_CHI_SO_BONG_TAI,
                                        "Chỉ Porata cấp 5 mới có thể mở chỉ số!!!",
                                        "Mở chỉ số Porata cấp 5","Từ chối");
                                break;
                                    case 5: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP4);
                                    break;
                                    case 6: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5);
                                    break;
                            case 7: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;  
                            case 8:
                            ShopServiceNew.gI().opendShop(player, "VAT_PHAM", false);                                                                      
                                    break;        
                                    
                                    
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_BONG_TAI) {
                            switch (select) {
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP4 );
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5 );
                                    break;    
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP3 );
                                    break;
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MO_CHI_SO_BONG_TAI) {
                            switch (select) {
                                
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI );
                                    break;
                                
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                    case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                        case CombineServiceNew.NANG_CAP_BONG_TAI_CAP4:
                                            case CombineServiceNew.NANG_CAP_BONG_TAI_CAP5:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:            
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }                          
                        }
                    }
                }
            }
        };
    }
       public static Npc nangcap(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?\n|7| Up Đá Ánh Sáng, Bóng Tối Tỉ Lệ 30%\n|7|(Dùng Để Bóng Tối Trang Bị Từ 5-15%)\n|7| Đá Khảm 2% (SD,KI,HP)\n|7|(Dùng Để Khảm Từ 3-5K SD,KI,HP)\n|7| Up Ngọc Bảo Tỉ lệ 50%\n|5|Nâng Cấp Tất Cả Tại Bà Hạt Mít",
                                "Up Đá\nBóng tối"
                               , "Up Đá\nKI,HP,SD"
                               ,"Up Đá\nNgọc Bảo "
                        );
                            }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                             case 0:
                                //đến Map UP bóng tối
                                ChangeMapService.gI().changeMapBySpaceShip(player, 196, -1, 360);
                                break; 
                               case 1:
                                //đến map up đá 
                                ChangeMapService.gI().changeMapBySpaceShip(player, 197, -1, 360);
                                break; 
                               case 2:
                                //đến map up đá ngọc bảo
                                ChangeMapService.gI().changeMapBySpaceShip(player, 198, -1, 360);
                                break; 
                               case 3:
                                //đến tay thanh dia
                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 360);
                                break; 
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                              case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                    case CombineServiceNew.AN_TRANG_BI:
                               case CombineServiceNew.PS_HOA_TRANG_BI:
                                case CombineServiceNew.KHAM_DA_HP:
                                case CombineServiceNew.KHAM_DA_MP:
                                case CombineServiceNew.KHAM_DA_DAME:
                                 case CombineServiceNew.NANG_CAP_PET2:
                                                    case CombineServiceNew.NANG_CAP_PK:
                                                        case CombineServiceNew.CUONG_HOA:
                                                        
                                    switch (select) {
                                        case 0:
                                          break;      
                                    }
                            }
                        }
                     else if (player.iDMark.getIndexMenu() == ConstNpc.LUYEN_HOA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PET2 );
                                    break;  
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PK );
                                    break;    
                            }}
                       else if (player.iDMark.getIndexMenu() == ConstNpc.KHAM_DA) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_HP );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_MP );
                                    break; 
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.KHAM_DA_DAME );
                                    break;    
                            }}
                        else if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU) {
                            switch (select) {
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_PHAP_SU_HOA);
                                    break;
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI );
                                    break;
                                
                            }
                        }
                         else if (player.iDMark.getIndexMenu() == ConstNpc.TINH_AN_TRANG_BI) {
                            switch (select) {
                                case 0:
                                //    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI );
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.CUONG_HOA );
                                    break;
                                case 1:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.AN_TRANG_BI_HUONG_DAN);
                                    break;  
                                
                            }
                        }
                  
                                
                         
                   
                    
                  
                      
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    break;
                            
                                              
                            }                          
                        }
                    }
                }
            }
        };
    }
    public static Npc thiensuwhis2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                               "Nói chuyện","Từ chối");//,"ngọc");//,"Nâng cấp\nHủy Diệt","Nâng cấp\nThiên Sứ");
                    } else if (this.mapId == 121) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Về đảo\nrùa");

                    } else {

                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Ngươi tìm ta có việc gì?",
                                "Cửa hàng\nBùa", "Nâng cấp\nVật phẩm", "Nhập\nNgọc Rồng","Nâng cấp\nBông tai","Mở chỉ số bông tai");//,
                               // "Nâng cấp\nBông tai\nPorata cấp2", "Nâng cấp\nBông tai\nPorata cấp3","Nâng cấp\nBông tai\nPorata cấp4","Nâng cấp\nBông tai\nPorata cấp5","Mở chỉ số\nBông tai\nPorata Cấp5","Tiệm đá");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                   
                                case 0:
                                createOtherMenu(player, ConstNpc.SKH,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Thần Linh","Hủy diệt", "Thiên sứ","Từ Chối");
                                
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                case CombineServiceNew.CHUYEN_HOA_TRANG_BI:
                                    case CombineServiceNew.AN_TRANG_BI:
                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                    case CombineServiceNew.NANG_CAP_SKH_VIPhd:
                                        case CombineServiceNew.NANG_CAP_SKH_VIPts:
                            //    case CombineServiceNew.PLH_CAITRANG:
                                case CombineServiceNew.PS_HOA_TRANG_BI:
                                case CombineServiceNew.TAY_PS_HOA_TRANG_BI: 
                                    case CombineServiceNew.GIA_HAN_VAT_PHAM:
                                case CombineServiceNew.kh_Tl:
                                case CombineServiceNew.kh_Hd:
                                case CombineServiceNew.kh_Ts:
                                    case CombineServiceNew.THANG_HOA_NGOC_BOI:
                                    switch (select) {
                                        case 0:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 1;
                                            }
                                            break;
                                        case 1:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 10;
                                            }
                                            break;
                                        case 2:
                                            if (player.combineNew.typeCombine == CombineServiceNew.PHA_LE_HOA_TRANG_BI) {
                                                player.combineNew.quantities = 100;
                                            }
                                            break;      
                                    }
                                        CombineServiceNew.gI().startCombine(player);
                            }
                        }
                       else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_THANG_HOA_NGOC_BOI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().startCombine1(player, 1);
                                    break;
                                case 1:
                                    CombineServiceNew.gI().startCombine1(player, 1);
                                    break;    
                            }}
                        else if (player.iDMark.getIndexMenu() == ConstNpc.PHAP_SU) {
                            switch (select) {
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_PHAP_SU_HOA);
                                    break;
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PS_HOA_TRANG_BI );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PS_HOA_TRANG_BI );
                                    break;
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_Hd );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.kh_T );
                                    break;
                                case 2:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONGDANHIENTE);
                                    break;  
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.TINH_AN_TRANG_BI) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI );
                                    break;
                                case 1:
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.AN_TRANG_BI_HUONG_DAN);
                                    break;  
                                
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.SKH) {
                            switch (select) {
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIP );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIPhd);
                                    break;
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_VIPts);
                                    break;    
                                
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAP_DO_KICH_HOAT) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DOI_SKH_VIP) {
                            if (select == 0) {
                                CombineServiceNew.gI().startCombine(player);
                            }
                        }
                    } else if (this.mapId == 112) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                    break;
                            }
                        }
                    } else if (this.mapId == 42 || this.mapId == 43 || this.mapId == 44 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: //shop bùa
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                                case 1://nâng cấp vật phẩm
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                    break;
                                case 2://nhập ngọc rồng
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                    break;    
                                case 3:
                                createOtherMenu(player, ConstNpc.NANG_CAP_BONG_TAI,
                                        "Ngươi tìm ta có việc gì?\n",
                                        "Nâng cấp\nPorata cấp 1","Nâng cấp\nPorata cấp 2", "Nâng cấp\nPorata cấp 3","Nâng cấp\nPorata cấp 4");
                                break;
                                case 4:
                                createOtherMenu(player, ConstNpc.MO_CHI_SO_BONG_TAI,
                                        "Chỉ Porata cấp 5 mới có thể mở chỉ số!!!",
                                        "Mở chỉ số Porata cấp 5","Từ chối");
                                break;
                                    case 5: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP4);
                                    break;
                                    case 6: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5);
                                    break;
                            case 7: //
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                    break;  
                            case 8:
                            ShopServiceNew.gI().opendShop(player, "VAT_PHAM", false);                                                                      
                                    break;        
                                    
                                    
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_BONG_TAI) {
                            switch (select) {
                                case 2:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP4 );
                                    break;
                                case 3:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP5 );
                                    break;    
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI );
                                    break;
                                case 1:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_CAP3 );
                                    break;
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MO_CHI_SO_BONG_TAI) {
                            switch (select) {
                                
                                case 0:
                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI );
                                    break;
                                
                                
                            }
                        }
                        
                        else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                            switch (select) {
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1H", true);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "BUA_8H", true);
                                    break;
                                case 2:
                                    ShopServiceNew.gI().opendShop(player, "BUA_1M", true);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                            switch (player.combineNew.typeCombine) {
                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                    case CombineServiceNew.NANG_CAP_BONG_TAI_CAP3:
                                        case CombineServiceNew.NANG_CAP_BONG_TAI_CAP4:
                                            case CombineServiceNew.NANG_CAP_BONG_TAI_CAP5:
                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                case CombineServiceNew.NHAP_NGOC_RONG:            
                                    if (select == 0) {
                                        CombineServiceNew.gI().startCombine(player);
                                    }
                                    break;
                            }                          
                        }
                    }
                }
            }
        };
    }


    public static Npc ruongDo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    InventoryServiceNew.gI().sendItemBox(player);
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {

                }
            }
        };
    }

    public static Npc duongtank(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0) {
                        this.createOtherMenu(player, 0, "A mi phò phò,thí chủ hãy giúp giải cứu đồ đệ của bần tăng đang bị phong ấn tại ngũ hành sơn, chỉ hỗ trợ từ 1,5tr sức mạnh đến 16 tỷ sức mạnh mới có thể vào", "Vào Ngay","Từ chối");
                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Aru?", "OK", "Từ chối");

                    }
                    if (mapId == 124) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.NguHanhSonPoint + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?", "Có", "Không");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                         if (select == 0) {
                            if (mapId == 0) {
                                if (player.nPoint.power < 1500000 || player.nPoint.power >= 16000000000L) {
                                    Service.gI().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 122, -1, 174);
                            }
                
                            if (mapId == 122) {
                                ChangeMapService.gI().changeMapInYard(player, 0, -1, 469);
                            }
                            if (mapId == 124) {
                               switch (select) {
                                   case 0:
                                    if (player.NguHanhSonPoint >= 500) {
                                        player.NguHanhSonPoint -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (711));
                                        item.itemOptions.add(new Item.ItemOption(50, 1));
                                        item.itemOptions.add(new Item.ItemOption(77, 1));
                                        item.itemOptions.add(new Item.ItemOption(103, 1));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.gI().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");          
                                    }
                                   if (select == 1) {
                                        Service.gI().showListTop(player, Manager.topNHS);
                                        break;
                            }
                        }
                    }
                }
            }
        }
     };
  }
    

        public static Npc thodaika(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đưa cho ta thỏi vàng và ngươi sẽ mua đc oto\nĐây không phải chẵn lẻ tài xỉu đâu=)))",
                            "Xỉu", "Tài");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    Input.gI().TAI(player);
                                    break;
                                case 1:
                                    Input.gI().XIU(player);
                                    break;

                            }
                        }
                    }
                }
            }
        };
        
        }
    
    public static Npc blackgoku(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 170 || mapId == 5) {
                        this.createOtherMenu(player, 0, "Map Fam \nHỗ trợ cho Ae Từ\b|1|80 Tỷ SM?", "OK", "Oéo");
                    }
                    if (mapId == 123) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Ảru?", "OK", "Từ chối");

                    }
                    if (mapId == 122) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.NguHanhSonPoint + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?", "Âu kê", "Top Ngu Hanh Son", "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 170|| mapId == 5) {
                                if (player.nPoint.power <= 80000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 171, -1, -1);
                            }
                            if (mapId == 123) {
                                ChangeMapService.gI().changeMapInYard(player, 0, -1, -1);
                            }
                            if (mapId == 122) {
                                if (select == 0) {
                                    if (player.NguHanhSonPoint >= 500) {
                                        player.NguHanhSonPoint -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (711));
                                        item.itemOptions.add(new Item.ItemOption(49, 8));
                                        item.itemOptions.add(new Item.ItemOption(77, 8));
                                        item.itemOptions.add(new Item.ItemOption(103, 5));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");
                                    }
                                } else if (select == 1) {
                                    Util.showListTop(player, (byte) 4
                                    );
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc dauThan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.magicTree.openMenuTree();
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                if (player.magicTree.level == 10) {
                                    player.magicTree.fastRespawnPea();
                                } else {
                                    player.magicTree.showConfirmUpgradeMagicTree();
                                }
                            } else if (select == 2) {
                                player.magicTree.fastRespawnPea();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                            if (select == 0) {
                                player.magicTree.harvestPea();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUpgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                            if (select == 0) {
                                player.magicTree.upgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_UPGRADE:
                            if (select == 0) {
                                player.magicTree.fastUpgradeMagicTree();
                            } else if (select == 1) {
                                player.magicTree.showConfirmUnuppgradeMagicTree();
                            }
                            break;
                        case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                            if (select == 0) {
                                player.magicTree.unupgradeMagicTree();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc calick(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            private final byte COUNT_CHANGE = 50;
            private int count;

            private void changeMap() {
                if (this.mapId != 102) {
                    count++;
                    if (this.count >= COUNT_CHANGE) {
                        count = 0;
                        this.map.npcs.remove(this);
                        Map map = MapService.gI().getMapForCalich();
                        if (map != null) {
                            this.mapId = map.mapId;
                            this.cx = Util.nextInt(100, map.mapWidth - 100);
                            this.cy = map.yPhysicInTop(this.cx, 0);
                            this.map = map;
                            this.map.npcs.add(this);
                        }
                    }
                }
            }

            @Override
            public void openBaseMenu(Player player) {
                player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                    Service.gI().hideWaitDialog(player);
                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                    return;
                }
                if (this.mapId != player.zone.map.mapId) {
                    Service.gI().sendThongBao(player, "Calích đã rời khỏi map!");
                    Service.gI().hideWaitDialog(player);
                    return;
                }
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    if (this.mapId == 102) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào chú, cháu có thể giúp gì? Muốn qua tương lai cần làm xong nhiệm vụ đưa thuốc trợ tim cho quy lão",
                                "Kể\nChuyện", "Quay về\nQuá khứ");
                    } else {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào chú, cháu có thể giúp gì? Muốn qua tương lai cần làm xong nhiệm vụ đưa thuốc trợ tim cho quy lão", "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (this.mapId == 102) {
                    if (player.iDMark.isBaseMenu()) {
                        if (select == 0) {
                            //kể chuyện
                            NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                        } else if (select == 1) {
                            //về quá khứ
                            ChangeMapService.gI().goToQuaKhu(player);
                        }
                    }
                } else if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        //kể chuyện
                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                    } else if (select == 1) {
                        //đến tương lai
                        changeMap();
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_3) {
                            Service.gI().sendThongBao(player, "Vui lòng hoàn thành nhiệm vụ trước khi tới đây!");
                     
                    
                    
                            ChangeMapService.gI().goToTuongLai(player);
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                    }
                }
            }
        };
    }

    public static Npc jaco(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến potaufeu
                                ChangeMapService.gI().goToPotaufeu(player);
                            }
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc unkn(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (mapId == 0 || mapId == 5) {
                        this.createOtherMenu(player, 0, "Làng Quái Vật x100 Tnsm\nHỗ trợ cho Ae Từ\b|1| 80 Tỷ SM?", "OK", "Oéo");
                    }
                    if (mapId == 169) {
                        this.createOtherMenu(player, 0, "Bạn Muốn Quay Trở Lại Làng Ảru?", "OK", "Từ chối");

                    }
                    if (mapId == 169) {
                        this.createOtherMenu(player, 0, "Xia xia thua phùa\b|7|Thí chủ đang có: " + player.NguHanhSonPoint + " điểm ngũ hành sơn\b|1|Thí chủ muốn đổi cải trang x4 chưởng ko?", "Âu kê", "Top Ngu Hanh Son", "No");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (select) {
                        case 0:
                            if (mapId == 169 || mapId == 5) {
                                if (player.nPoint.power <= 80000000000L) {
                                    Service.getInstance().sendThongBao(player, "Sức mạnh bạn không phù hợp để qua map!");
                                    return;
                                }
                                ChangeMapService.gI().changeMapInYard(player, 169, -1, -1);
                            }
                            if (mapId == 169) {
                                ChangeMapService.gI().changeMapInYard(player, 0, -1, -1);
                            }
                            if (mapId == 169) {
                                if (select == 0) {
                                    if (player.NguHanhSonPoint >= 500) {
                                        player.NguHanhSonPoint -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (711));
                                        item.itemOptions.add(new Item.ItemOption(50, 1));
                                        item.itemOptions.add(new Item.ItemOption(77, 1));
                                        item.itemOptions.add(new Item.ItemOption(103, 1));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Vật Phẩm Thành Công !");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ điểm, bạn còn " + (500 - player.pointPvp) + " điểm nữa");
                                    }
                                } else if (select == 1) {
                                    Util.showListTop(player, (byte) 4
                                    );
                                }
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc npclytieunuong54(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                createOtherMenu(player, 0, "Trò chơi Chọn ai đây đang được diễn ra, nếu bạn tin tưởng mình đang tràn đầy may mắn thì có thể tham gia thử", "Thể lệ", "Chọn\nThỏi vàng");
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    String time = ((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                    if (pl.iDMark.getIndexMenu() == 0) {
                        if (select == 0) {
                            createOtherMenu(pl, ConstNpc.IGNORE_MENU, "Thời gian giữa các giải là 5 phút\nKhi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn.\nLưu ý: Số thỏi vàng nhận được sẽ bị nhà cái lụm đi 5%!Trong quá trình diễn ra khi đặt cược nếu thoát game mọi phần đặt đều sẽ bị hủy", "Ok");
                        } else if (select == 1) {
                            createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                        }
                    } else if (pl.iDMark.getIndexMenu() == 1) {
                        if (((ChonAiDay.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0) {
                            switch (select) {
                                case 0:
                                    createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    break;
                                case 1: {
                                    if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 20) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 20);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        pl.goldNormar += 20;
                                        ChonAiDay.gI().goldNormar += 20;
                                        ChonAiDay.gI().addPlayerNormar(pl);
                                        createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    } else {
                                        Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                    }
                                }
                                break;
                                case 2: {
                                    if (InventoryServiceNew.gI().findItemBag(pl, 457).isNotNullItem() && InventoryServiceNew.gI().findItemBag(pl, 457).quantity >= 200) {
                                        InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 457), 200);
                                        InventoryServiceNew.gI().sendItemBags(pl);
                                        pl.goldVIP += 200;
                                        ChonAiDay.gI().goldVip += 200;
                                        ChonAiDay.gI().addPlayerVIP(pl);
                                        createOtherMenu(pl, 1, "Tổng giải thường: " + ChonAiDay.gI().goldNormar + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(0) + "%\nTổng giải VIP: " + ChonAiDay.gI().goldVip + " thỏi vàng, cơ hội trúng của bạn là: " + pl.percentGold(1) + "%\nSố thỏi vàng đặt thường: " + pl.goldNormar + "\nSố thỏi vàng đặt VIP: " + pl.goldVIP + "\n Thời gian còn lại: " + time, "Cập nhập", "Thường\n20 thỏi\nvàng", "VIP\n200 thỏi\nvàng", "Đóng");
                                    } else {
                                        Service.getInstance().sendThongBao(pl, "Bạn không đủ thỏi vàng");
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc chiton(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 5) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|8|Bạn muốn đổi lấy set chí tôn?\nĐồ Chí Tôn Tăng X10 Lần Đồ Thiên Sứ (Chỉ Số Trên 100%)\n Cần Đồ Thần Linh ,Mảnh Chí Tôn", "Đổi Set\n Chí Tôn");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                      Service.gI().sendThongBao(player, "Admim Đang Hoàn Thiện");
                                    break;
                               case 1:
                                    this.createOtherMenu(player, 1,
                                            "Bạn muốn đổi 5 món đồ thần linh \n cùng loại và x30 đá ngũ sắc \n|6|Để đổi lấy 1 món đồ húy diệt có tý lệ ra SKH ko", "Áo\nChí Tôn", "Quần\nChí Tôn", "Găng\nChí Tôn", "Giày\nChí Tôn", "Nhẫn\nChí Tôn", "Thôi Khỏi");
                                    break;
                            
                            }
                        } else if (player.iDMark.getIndexMenu() == 1) { // action đổi dồ húy diệt
                            switch (select) {
                                case 0: // trade
                                try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item tl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 555);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item thl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 555 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 555 + i) && soLuong >= 30) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, thl, 5);
                                            CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 650 + i);
                                            this.npcChat(player, "Chuyển Hóa Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần Áo Thần linh trái đất + x30 Đá Ngũ Sắc!");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 1: // trade
                                    try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item tl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 556);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item thl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 556 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 556 + i) && soLuong >= 30) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, thl, 1);
                                            CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 651 + i);
                                            this.npcChat(player, "Chuyển Hóa Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần Quần Thần linh trái đất + x30 Đá Ngũ Sắc!");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 2: // trade
                                    try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item tl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 562);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item thl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 562 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 562 + i) && soLuong >= 30) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, thl, 1);
                                            CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 657 + i);
                                            this.npcChat(player, "Chuyển Hóa Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần Găng Thần linh trái đất + x30 Đá Ngũ Sắc!");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 3: // trade
                                    try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item tl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 563);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item thl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 563 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 563 + i) && soLuong >= 30) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, thl, 1);
                                            CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 658 + i);
                                            this.npcChat(player, "Chuyển Hóa Thành Công!");

                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần Giày Thần linh trái đất + x30 Đá Ngũ Sắc!");
                                        }

                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 4: // trade
                                    try {
                                    Item dns = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 674);
                                    Item tl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 561);
                                    int soLuong = 0;
                                    if (dns != null) {
                                        soLuong = dns.quantity;
                                    }
                                    for (int i = 0; i < 12; i++) {
                                        Item thl = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 561 + i);

                                        if (InventoryServiceNew.gI().isExistItemBag(player, 561 + i) && soLuong >= 30) {
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, dns, 30);
                                            InventoryServiceNew.gI().subQuantityItemsBag(player, thl, 1);
                                            CombineServiceNew.gI().GetTrangBiKichHoathuydiet(player, 656 + i);
                                            this.npcChat(player, "Chuyển Hóa Thành Công!");
                                            break;
                                        } else {
                                            this.npcChat(player, "Yêu cầu cần Nhận Thần linh trái đất + x30 Đá Ngũ Sắc!");
                                        }
                                    }
                                } catch (Exception e) {

                                }
                                break;
                                case 5: // canel
                                    break;
                         }
                        }
                    }
                }
            }
        };
    }
    
    
    public static Npc gapthu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                if (this.mapId == 5 || this.mapId == 7) {
                    this.createOtherMenu(player, 1234, "|6|MÁY GẮP LINH THÚ\n"+"|4|GẮP THƯỜNG : 5-10% CHỈ SỐ\nGẮP CAO CẤP : 10-20% CHỈ SỐ\nGẮP VIP : 15-25% CHỈ SỐ"+"\nGẮP X1 : GẮP THỦ CÔNG\nGẮP X10 : AUTO X10 LẦN GẮP\nGẮP X100 : AUTO X100 LẦN GẮP\n"+"|7|LƯU Ý : MỌI CHỈ SỐ ĐỀU RANDOM KHÔNG CÓ OPTION NHẤT ĐỊNH\nNẾU MUỐN NGƯNG AUTO GẤP CHỈ CẦN THOÁT GAME VÀ VÀO LẠI!",
                    "Gắp Thường","Gắp Cao Cấp","Gắp VIP","Rương Đồ","TOP Gấp Thú");
                }
            }
        }
        @Override
        public void confirmMenu(Player player, int select) {
            if (canOpenNpc(player)) {
                if (this.mapId == 5 || this.mapId == 7) {
                    if (player.iDMark.getIndexMenu()==1234) {
                        switch (select) {
                            case 0:
                                this.createOtherMenu(player, 12345, "|6|Gắp Pet Thường"+"\n|7|Tiến Hành Gắp!",
                                 "Gắp x1","Gắp x10","Gắp x100","Rương Đồ");
                                break;
                            case 1:
                                this.createOtherMenu(player, 12346, "|6|Gắp Pet Cao Cấp"+"\n|7|Tiến Hành Gắp!",
                                 "Gắp x1","Gắp x10","Gắp x100","Rương Đồ");
                                break;
                            case 2:
                                this.createOtherMenu(player, 12347, "|6|Gắp Pet VIP"+"\n|7|Tiến Hành Gắp!",
                                 "Gắp x1","Gắp x10","Gắp x100","Rương Đồ");
                                break;
                            case 3:
                                this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                        "|1|Mày là ai??\n"
                                                + "|6|Tao là Florentino.",
                                        "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa Hết\nRương Phụ", "Đóng");
                                break;
                            case 4:
                                Service.getInstance().sendThongBaoOK(player,TopService.getdiemgapthu());
                                        break;   
                            }
                        } else if (player.iDMark.getIndexMenu() == 12345) {
                        switch (select) { 
                            case 0:
                                if (player.inventory.ruby < 300) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
                                    player.inventory.ruby -= 300;
                                    Service.gI().sendMoney(player);
                                Item gapt = Util.petrandom(Util.nextInt(1428,1429));
                                if(Util.isTrue(10, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapt);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12345, "|2|Bạn vừa gắp được : "+gapt.template.name+"\nSố Hồng Ngọc Trừ : 100"+"\n|7|Chiến tiếp ngay!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                } else {
                                    this.createOtherMenu(player, 12345, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\nSố Hồng Ngọc Trừ : 100"+"\n|7|Chiến tiếp ngay!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }
                                break;
                            case 1:
                                if (player.inventory.ruby < 3000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                int timex10 = 10;
                                int hn = 0;
                                while (timex10 > 0) {
                                    timex10--;
                                    hn+=300;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    break;
                                    }
                                    player.inventory.ruby -= 300;
                                    Service.gI().sendMoney(player);
                                    Item gapx10 = Util.petrandom(Util.nextInt(1428,1429));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(10, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n" + "\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(10, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n : ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 2:
                                if (player.inventory.ruby < 30000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                int timex100 = 100;
                                int hn = 0;
                                while (timex100 > 0) {
                                    timex100--;
                                    hn+=300;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12345, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    break;
                                    }
                                    player.inventory.ruby -= 300;
                                    Service.gI().sendMoney(player);
                                    Item gapx100 = Util.petrandom(Util.nextInt(1428,1429));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(10, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(10, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 3:
                                this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                       "|1|Mày là ai??\n"
                                                + "|6|Tao là Florentino.",
                                        "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa Hết\nRương Phụ", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 12346) {
                        switch (select) { 
                            case 0:
                                if (player.inventory.ruby < 400) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
                                    player.inventory.ruby -= 600;
                                    Service.gI().sendMoney(player);
                                Item gapt = Util.petccrandom(Util.nextInt(1430,1434));
                                if(Util.isTrue(12, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapt);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12346, "|2|Bạn vừa gắp được : "+gapt.template.name+"\nSố Hồng Ngọc Trừ : 100"+"\n|7|Chiến tiếp ngay!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                } else {
                                    this.createOtherMenu(player, 12346, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\n|7|Chiến tiếp ngay!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }
                                break;
                            case 1:
                                if (player.inventory.ruby < 4000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                int timex10 = 10;
                                int hn = 0;
                                while (timex10 > 0) {
                                    timex10--;
                                    hn+=600;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12346, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    break;
                                    }
                                    player.inventory.ruby -= 600;
                                    Service.gI().sendMoney(player);
                                    Item gapx10 = Util.petccrandom(Util.nextInt(1430,1434));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(12, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(10, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 2:
                                if (player.inventory.ruby < 60000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                int timex100 = 100;
                                int hn = 0;
                                while (timex100 > 0) {
                                    timex100--;
                                    hn+=600;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12346, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    break;
                                    }
                                    player.inventory.ruby -= 600;
                                    Service.gI().sendMoney(player);
                                    Item gapx100 = Util.petccrandom(Util.nextInt(1430,1434));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(12, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12346, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n "+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(10, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12346, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 3:
                                this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                        "|1|Mày là ai??\n"
                                                + "|6|Tao là Florentino.",
                                        "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa Hết\nRương Phụ", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 12347) {
                        switch (select) { 
                            case 0:
                                if (player.inventory.ruby < 1000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
                                    player.inventory.ruby -= 1000;
                                    Service.gI().sendMoney(player);
                                Item gapt = ItemService.gI().createNewItem((short)Util.nextInt(1435,1442));
                                if(Util.isTrue(15, 100)) {
                                    gapt.itemOptions.add(new ItemOption(50,Util.nextInt(20,30)));
                                    gapt.itemOptions.add(new ItemOption(77,Util.nextInt(20,30)));
                                    gapt.itemOptions.add(new ItemOption(103,Util.nextInt(20,30)));
                                    if(Util.isTrue(80, 100)) {
                                    gapt.itemOptions.add(new ItemOption(93,Util.nextInt(7,15)));    
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, gapt);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12347, "|2|Bạn vừa gắp được : "+gapt.template.name+"\nSố Hồng Ngọc Trừ : 100"+"\n|7|Chiến tiếp ngay!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                } else {
                                    this.createOtherMenu(player, 12347, "|6|Gắp hụt rồi, bạn bỏ cuộc sao?"+"\n|7|Thất bại ở đâu gắp đôi ở đó!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }
                                break;
                            case 1:
                                if (player.inventory.ruby < 10000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto gắp x10 lần");
                                int timex10 = 10;
                                int hn = 0;
                                while (timex10 > 0) {
                                    timex10--;
                                    hn+=1000;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12347, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    break;
                                    }
                                    player.inventory.ruby -= 1000;
                                    Service.gI().sendMoney(player);
                                    Item gapx10 = Util.petviprandom(Util.nextInt(1435,1442));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(15, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(15, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã gắp được : "+gapx10.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 2:
                                if (player.inventory.ruby < 100000) {
                                    Service.gI().sendThongBao(player, "Hết Tiền Roài");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto gắp x100 lần");
                                int timex100 = 100;
                                int hn = 0;
                                while (timex100 > 0) {
                                    timex100--;
                                    hn+=1000;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12347, "|7|DỪNG AUTO GẮP, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT GẮP : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    break;
                                    }
                                    player.inventory.ruby -= 1000;
                                    Service.gI().sendMoney(player);
                                    Item gapx100 = Util.petviprandom(Util.nextInt(1435,1442));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(15, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH GẮP AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(15, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                    player.diemgapthu += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã gắp được : "+gapx100.template.name+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                    } else {
                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH GẮP AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Gắp hụt rồi!"+"\nSố hồng ngọc đã trừ : "+hn+"\n",
                                    "Gắp X1","Gắp X10","Gắp X100","Rương Đồ");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 3:
                                this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                        "|1|Mày là ai??\n"
                                                + "|6|Tao là Florentino.",
                                        "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa Hết\nRương Phụ", "Đóng");
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.RUONG_PHU) { 
                        switch (select) {
                            case 0:
                                ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player,
                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                        "|3|Bạn chắc muốn xóa hết vật phẩm trong rương phụ?\n"
                                                +"|7|Sau khi xóa sẽ không thể khôi phục!",
                                        "Đồng ý", "Hủy bỏ");
                                break;
                        }
                    }
                }
            }
        }
    };
}
    
    public static Npc vongquaycoin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
        @Override
        public void openBaseMenu(Player player) {
            if (canOpenNpc(player)) {
                if (this.mapId == 5 || this.mapId == 7 || this.mapId == 14 || this.mapId == 196) {
                    this.createOtherMenu(player, 1234, "|6|Bạn Hãy Đưa Tiền Vào Trong Tôi\n|7|Tôi Sẽ Tặng Bạn Món Quà Có Giá Trị\nRandom Tỉ Lệ Cải Trang 150-350%[40%]\n Tỉ Lệ Ngọc Châu [60%] \n|1|Lưu Ý:Chỉ Rút X1 Mới Được Tính Điểm\n[Điểm Quy Đổi Và Điểm TOP]",
                    "Rút VIP","Đổi Điểm VIP","TOP Rút Thưởng","Từ chối");
                }
            }
        }
        @Override
        public void confirmMenu(Player player, int select) {
            if (canOpenNpc(player)) {
                if (this.mapId == 5 || this.mapId == 7 || this.mapId == 14 || this.mapId == 196) {
                    if (player.iDMark.getIndexMenu()==1234) {
                        switch (select) {
//                            case 0:
//                                this.createOtherMenu(player, 12345, "|6|Rút Thưởng"+"\n|7|Tiến Hành Rút!\n"
//                                        + "|2|Point Vòng quay: "+player.quaythuong+" Point Thường\n",
//                               "Rút 1 Lần","Rút 10 Lần","Rút 100 Lần");
//                                break;
                                case 0:
                                this.createOtherMenu(player, 12347, "|6|Rút VIP"+"\n|7|Chỉ Rút Ra Ngọc Châu Mới Nhận Được Điểm\n"
                                        + "|2|Máu của bạn còn: "+player.getSession().vnd+" Vnđ\n",
                                "Rút [5K]");
                                break;
                            case 1:
                                this.createOtherMenu(player, 1234567, "|6|Xin chào ngài\n"
                                        + "|5|Đây là nơi để quy đổi điểm quay vip\n"
                                        + "|5|200D[500vnro3s] 500D[Thú Cưỡi 100-200%]\n"
                                         + "|5|1000D[Linh Thú 100-200%],5K Điểm 1 Hộp TS[SKH]\n"
                                        + "|1|Điểm sẽ trừ vào điểm đã tích của ngài\n"
//                                        + "|1|chúc ngài chơi game vui vẻ!\n"
                                        + "|7|Điểm tích vòng quay VIP: "+player.diemquaydoidiem+" Điểm VIP\n",
                               "Thử vận may 200 điểm","Thử vận may 500 điểm","Thử vận may 1000 điểm","Thử vận may 5000 điểm");
                                break;  
                            case 2:
                                Service.getInstance().sendThongBaoOK(player,TopService.getdiemquay());
                                        break;   
                            }
                        }
                    else if (player.iDMark.getIndexMenu() == 12347) { //   quay víp 
                        switch (select) {               //luwdev fix
                            case 0:
                                if (player.getSession().vnd < 5000) {
                                    Service.gI().sendThongBao(player, "Bơm máu đi ngươi bị thiếu màu rồi!!!");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
                                PlayerDAO.subvnd(player, 5000);    
                                player.getSession().vnd -= 5000;
                                    Service.gI().sendMoney(player);
                                  Item gapt = ItemService.gI().createNewItem((short)5  , 500);
                                  //day la nhan = so luong
//                                  Item gapt = ItemService.gI().createNewItem((short) 2025,1620);
                                  //day la nhan theo danh sach liet ke
                                if(Util.isTrue(70, 100)) {
                                  Item gapt1 = ItemService.gI().createNewItem((short)Util.nextInt(1369,1374));
//@luwdev
                                    gapt1.itemOptions.add(new ItemOption(50,Util.nextInt(150,250)));
                                    gapt1.itemOptions.add(new ItemOption(77,Util.nextInt(200,300)));
                                    gapt1.itemOptions.add(new ItemOption(103,Util.nextInt(200,300)));
                                    gapt1.itemOptions.add(new ItemOption(230,Util.nextInt(100,150)));
                                    gapt1.itemOptions.add(new ItemOption(231,Util.nextInt(100,150)));
                                    gapt1.itemOptions.add(new ItemOption(232,Util.nextInt(100,150)));
                                    gapt1.itemOptions.add(new ItemOption(237,Util.nextInt(0,0)));
                                    if(Util.isTrue(12, 100)) {
                                        
                                   Item gapt2 = ItemService.gI().createNewItem((short)Util.nextInt(1363,1370));
//@luwdev
                                    gapt2.itemOptions.add(new ItemOption(50,Util.nextInt(150,200)));
                                    gapt2.itemOptions.add(new ItemOption(77,Util.nextInt(200,200)));
                                    gapt2.itemOptions.add(new ItemOption(103,Util.nextInt(200,200)));
                                    gapt2.itemOptions.add(new ItemOption(95,Util.nextInt(75,120)));
                                    gapt2.itemOptions.add(new ItemOption(96,Util.nextInt(76,120)));
                                    gapt2.itemOptions.add(new ItemOption(237,Util.nextInt(0,0)));
                                    if(Util.isTrue(30, 100)) 
                                    InventoryServiceNew.gI().addItemBag(player, gapt);
                                    InventoryServiceNew.gI().addItemBag(player, gapt1);
                                    InventoryServiceNew.gI().addItemBag(player, gapt2);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemquay += 1;
                                    player.diemquaydoidiem += 20;
                                    Service.getInstance().sendMoney(player); }
                                    this.createOtherMenu(player, 12347, "|2|Bạn vừa Quay được : "+gapt.template.name+"\nSố Vnđ Trừ : 1000"+"\n|7|Chiến tiếp ngay!",
                                    "Quay X1");
                                    this.createOtherMenu(player, 12347, "|2|Bạn vừa Quay được : "+gapt1.template.name+"\nSố Vnđ Trừ : 1000"+"\n|7|Chiến tiếp ngay!",
                                    "Quay X1");
                                } else {
                                    this.createOtherMenu(player, 12347, "|6|Quay hụt rồi, bạn bỏ cuộc sao?"+"\n|7|Thất bại ở đâu Quay đôi ở đó!",
                                    "Quay X1");
                                }
                                break;
//                            case 1:
//                                if (player.getSession().vnd < 10000) {
//                                    Service.gI().sendThongBao(player, "Bơm máu đi ngươi bị thiếu màu rồi!!!");
//                                    return;
//                                }
//                                try {
//                                Service.gI().sendThongBao(player, "Tiến hành auto Quay x10 lần");
//                                int timex10 = 10;
//                                int hn = 0;
//                                while (timex10 > 0) {
//                                    timex10--;
//                                    hn+=1000;
//                                    Thread.sleep(100);
//                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
//                                    this.createOtherMenu(player, 12347, "|7|DỪNG AUTO Quay, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT Quay : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    break;
//                                    }
//                                    PlayerDAO.subvnd(player, 1000);
//                                    player.getSession().vnd -= 1000;
//                                    Service.gI().sendMoney(player);
//                                    Item gapx10 = Util.optiontvVIP(Util.nextInt(1620,1620));  
//                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    if(Util.isTrue(45, 100)) {
//                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH Quay AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã Quay được : "+gapx10.template.name+"\nSố Vnđ đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    } else {
//                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH Quay AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Vnđ đã trừ : "+hn+"\n",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    }}
//                                   if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
//                                    if(Util.isTrue(15, 100)) {
////                                    player.inventory.itemsBoxCrackBall.add(gapx10);
////                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
////                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    player.diemquay += 1;
//                                    player.diemquaydoidiem += 10;
//                                    Service.getInstance().sendMoney(player);
//                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã Quay được : "+gapx10.template.name+"\nSố Vnđ đã trừ : "+hn+"\n",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    } else {
//                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Vnđ đã trừ : "+hn+"\n",
//                                    "Quay X1","Quay X10","Quay X100");
//                                }}}} catch (Exception e) {
//                                }
//                                break;
//                            case 2:
//                                if (player.getSession().vnd < 100000) {
//                                    Service.gI().sendThongBao(player, "Bơm máu đi ngươi bị thiếu màu rồi!!!");
//                                    return;
//                                }
//                                try {
//                                Service.gI().sendThongBao(player, "Tiến hành auto Quay x100 lần");
//                                int timex100 = 100;
//                                int hn = 0;
//                                while (timex100 > 0) {
//                                    timex100--;
//                                    hn+=1000;
//                                    Thread.sleep(100);
//                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
//                                    this.createOtherMenu(player, 12347, "|7|DỪNG AUTO Quay, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT Quay : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    break;
//                                    }
//                                    PlayerDAO.subvnd(player, 1000);
//                                    player.getSession().vnd -= 1000;
//                                    Service.gI().sendMoney(player);
//                                    Item gapx100 = Util.optiontvVIP(Util.nextInt(457,457));  
//                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
//                                    if(Util.isTrue(15, 100)) {
//                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH Quay AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã Quay được : "+gapx100.template.name+"\nSố Vnđ đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    } else {
//                                    this.createOtherMenu(player, 12347, "|7|ĐANG TIẾN HÀNH Quay AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Vnđ đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    }}
//                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
//                                    if(Util.isTrue(15, 100)) {
//                                    player.inventory.itemsBoxCrackBall.add(gapx100);
//                                    player.diemquay += 1;
//                                    player.diemquaydoidiem += 5;
//                                    Service.getInstance().sendMoney(player);
//                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã Quay được : "+gapx100.template.name+"\nSố Vnđ đã trừ : "+hn+"\n",
//                                    "Quay X1","Quay X10","Quay X100");
//                                    } else {
//                                    this.createOtherMenu(player, 12347, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Vnđ đã trừ : "+hn+"\n",
//                                    "Quay X1","Quay X10","Quay X100");
//                                }}}} catch (Exception e) {
//                                }
//                                break;
//                            case 3:
//                                this.createOtherMenu(player, ConstNpc.RUONG_PHU,
//                                       "|1|Mày là ai??\n"
//                                                + "|6|Tao là Florentino.",
//                                        "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
//                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
//                                        + " món)",
//                                        "Xóa Hết\nRương Phụ", "Đóng");
//                                break;    
                               
                       
                    }
                    } else if (player.iDMark.getIndexMenu() == 12345) {  // quay thường bằng điểm quay player pem boss 196 197 by luudeptrai
                        switch (select) { 
                            case 0:
                                if (player.quaythuong < 1000) {
                                    Service.gI().sendThongBao(player, "Bạn Không Đủ Điều kiện!!!");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
                                    PlayerDAO.subquaythuong(player, 1000);
                                    player.quaythuong -= 1000;
                                    Service.gI().sendMoney(player);
                                Item gapt = ItemService.gI().createNewItem((short)Util.nextInt(457,457));
                                if(Util.isTrue(15, 100)) {
                                //    gapt.itemOptions.add(new ItemOption(50,Util.nextInt(20,30)));
                                //    gapt.itemOptions.add(new ItemOption(77,Util.nextInt(20,30)));
                                    gapt.itemOptions.add(new ItemOption(225,Util.nextInt(0,0)));
                                    if(Util.isTrue(80, 100)) {
                                    gapt.itemOptions.add(new ItemOption(93,Util.nextInt(7,15)));    
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, gapt);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                //    player.diemquay += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12345, "|2|Bạn vừa Quay được : "+gapt.template.name+"\nSố Điểm Trừ : 100"+"\n|7|Chiến tiếp ngay!",
                                    "Quay X1","Quay X10","Quay X100");
                                } else {
                                    this.createOtherMenu(player, 12345, "|6|Quay hụt rồi, bạn bỏ cuộc sao?"+"\n|7|Thất bại ở đâu Quay đôi ở đó!",
                                    "Quay X1","Quay X10","Quay X100");
                                }
                                break;
                            case 1:
                                if (player.quaythuong < 10000) {
                                    Service.gI().sendThongBao(player, "Bơm máu đi ngươi bị thiếu màu rồi!!!");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto Quay x10 lần");
                                int timex10 = 10;
                                int hn = 0;
                                while (timex10 > 0) {
                                    timex10--;
                                    hn+=1000;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12345, "|7|DỪNG AUTO Quay, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT Quay : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Quay X1","Quay X10","Quay X100");
                                    break;
                                    }
                                    PlayerDAO.subquaythuong(player, 1000);
                                    player.quaythuong -= 1000;
                                    Service.gI().sendMoney(player);
                                    Item gapx10 = Util.optiontv(Util.nextInt(457,457));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(15, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx10);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH Quay AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã Quay được : "+gapx10.template.name+"\nSố Điểm đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Quay X1","Quay X10","Quay X100");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH Quay AUTO X10\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Điểm đã trừ : "+hn+"\n",
                                    "Quay X1","Quay X10","Quay X100");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(15, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx10);
                                //    player.diemquay += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Đã Quay được : "+gapx10.template.name+"\nSố Điểm đã trừ : "+hn+"\n",
                                    "Quay X1","Quay X10","Quay X100");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X10 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex10+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Điểm đã trừ : "+hn+"\n",
                                    "Quay X1","Quay X10","Quay X100");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 2:
                                if (player.quaythuong < 100000) {
                                    Service.gI().sendThongBao(player, "Bơm máu đi ngươi bị thiếu màu rồi!!!");
                                    return;
                                }
                                try {
                                Service.gI().sendThongBao(player, "Tiến hành auto Quay x100 lần");
                                int timex100 = 100;
                                int hn = 0;
                                while (timex100 > 0) {
                                    timex100--;
                                    hn+=1000;
                                    Thread.sleep(100);
                                    if(1+player.inventory.itemsBoxCrackBall.size() > 500) {
                                    this.createOtherMenu(player, 12345, "|7|DỪNG AUTO Quay, RƯƠNG PHỤ ĐÃ ĐẦY!\n"+"|2|TỔNG LƯỢT Quay : "+hn/800+" LƯỢT"+"\n|7|VUI LÒNG LÀM TRỐNG RƯƠNG PHỤ!",
                                    "Quay X1","Quay X10","Quay X100");
                                    break;
                                    }
                                    PlayerDAO.subquaythuong(player, 1000);
                                    player.quaythuong -= 1000;
                                    Service.gI().sendMoney(player);
                                    Item gapx100 = Util.optiontv(Util.nextInt(457,457));  
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if(Util.isTrue(15, 100)) {
                                    InventoryServiceNew.gI().addItemBag(player, gapx100);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH Quay AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã Quay được : "+gapx100.template.name+"\nSố Điểm đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Quay X1","Quay X10","Quay X100");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|ĐANG TIẾN HÀNH Quay AUTO X100\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Điểm đã trừ : "+hn+"\n"+"\nNẾU HÀNH TRANG ĐẦY, ITEM SẼ ĐƯỢC THÊM VÀO RƯƠNG PHỤ",
                                    "Quay X1","Quay X10","Quay X100");
                                    }}
                                    if(InventoryServiceNew.gI().getCountEmptyBag(player) == 0){
                                    if(Util.isTrue(15, 100)) {
                                    player.inventory.itemsBoxCrackBall.add(gapx100);
                                //    player.diemquay += 1;
                                    Service.getInstance().sendMoney(player);
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Đã Quay được : "+gapx100.template.name+"\nSố Điểm đã trừ : "+hn+"\n",
                                    "Quay X1","Quay X10","Quay X100");
                                    } else {
                                    this.createOtherMenu(player, 12345, "|7|HÀNH TRANG ĐÃ ĐẦY\nĐANG TIẾN HÀNH Quay AUTO X100 VÀO RƯƠNG PHỤ\nSỐ LƯỢT CÒN : "+timex100+" LƯỢT\n"+"|2|Quay hụt rồi!"+"\nSố Điểm đã trừ : "+hn+"\n",
                                    "Quay X1","Quay X10","Quay X100");
                                }}}} catch (Exception e) {
                                }
                                break;
                            case 3:
                                this.createOtherMenu(player, ConstNpc.RUONG_PHU,
                                       "|1|Mày là ai??\n"
                                                + "|6|Tao là Florentino.",
                                        "Rương Phụ\n("+ (player.inventory.itemsBoxCrackBall.size()
                                        - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                        + " món)",
                                        "Xóa Hết\nRương Phụ", "Đóng");
                                break; 
                            
                            
                               
                       
                    }
                    }          //doidiem 
                    else if (player.iDMark.getIndexMenu() == 1234567) {
                        switch (select) { 
      //lon me m @luu
                            case 0:
                                if (player.diemquaydoidiem < 200) {
                                    Service.gI().sendThongBao(player, "không đủ điểm");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
                                  player.diemquaydoidiem -= 200;
                                    Service.getInstance().sendThongBao(player, "Đổi thành công ");
                                    Service.gI().sendMoney(player);
                                      Item vanmay200 = ItemService.gI().createNewItem((short) 16, 500);//djtme mai ms mo dc 
                                 if(Util.isTrue(100, 100)) {
//                                 
                                    InventoryServiceNew.gI().addItemBag(player, vanmay200);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemquay += 200;
                                    Service.getInstance().sendMoney(player);
                                }
                                break;
                            case 1:
                                if (player.diemquaydoidiem < 500) {
                                    Service.gI().sendThongBao(player, "không đủ điểm");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
//                                    PlayerDAO.subdiemquaydoidiem(player, 500);
                                    player.diemquaydoidiem -= 500;
                                    Service.getInstance().sendThongBao(player, "Đổi thành công ");
                                    Service.gI().sendMoney(player);
                                Item vanmay500 = ItemService.gI().createNewItem((short) 2040,1);
                                if(Util.isTrue(100, 100)) {
                                    vanmay500.itemOptions.add(new ItemOption(50,Util.nextInt(100,200)));
                                    vanmay500.itemOptions.add(new ItemOption(77,Util.nextInt(100,200)));
                                    vanmay500.itemOptions.add(new ItemOption(103,Util.nextInt(100,200)));
// HOANGVANLUU
{
                                    vanmay500.itemOptions.add(new ItemOption(5,Util.nextInt(100,200)));    
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, vanmay500);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemquay += 500;
                                    Service.getInstance().sendMoney(player);
                                }
                                break;
                            case 2:
                                if (player.diemquaydoidiem < 1000) {
                                    Service.gI().sendThongBao(player, "không đủ điểm ");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
//                                    PlayerDAO.subdiemquaydoidiem(player, 1000);
                                    player.diemquaydoidiem -= 1000;
                                    Service.getInstance().sendThongBao(player, "Đổi thành công");
                                    Service.gI().sendMoney(player);
                                Item vanmay1000 = ItemService.gI().createNewItem((short) 1355,1);
//                              
                                if(Util.isTrue(100, 100)) {
                                    vanmay1000.itemOptions.add(new ItemOption(50,Util.nextInt(100,200)));
                                    vanmay1000.itemOptions.add(new ItemOption(77,Util.nextInt(100,200)));
                                    vanmay1000.itemOptions.add(new ItemOption(103,Util.nextInt(100,200)));
// HOANGVANLUU
{
                                    vanmay1000.itemOptions.add(new ItemOption(5,Util.nextInt(100,200)));    
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, vanmay1000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemquay += 1000;
                                    Service.getInstance().sendMoney(player);
                                }
                                break;  
                            case 3:
                                if (player.diemquaydoidiem < 5000) {
                                    Service.gI().sendThongBao(player, "không đủ điểm");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    Service.gI().sendThongBao(player, "Hết chỗ trống rồi");
                                    return;
                                }
//                                    PlayerDAO.subdiemquaydoidiem(player, 5000);
                                    player.diemquaydoidiem -= 5000;
                                    Service.getInstance().sendThongBao(player, "Đổi thành công!");
                                    Service.gI().sendMoney(player);
                                Item vanmay5000 = ItemService.gI().createNewItem((short)Util.nextInt(1105,1105));
                                if(Util.isTrue(100, 100)) {
                                //    vanmay5000.itemOptions.add(new ItemOption(50,Util.nextInt(20,30)));
                                //    vanmay5000.itemOptions.add(new ItemOption(77,Util.nextInt(20,30)));
                                    vanmay5000.itemOptions.add(new ItemOption(225,Util.nextInt(0,0)));
                                    if(Util.isTrue(100, 100)) {
                                    vanmay5000.itemOptions.add(new ItemOption(30,Util.nextInt(0,0)));    
                                    }
                                    InventoryServiceNew.gI().addItemBag(player, vanmay5000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    player.diemquay += 5000;
                                    Service.getInstance().sendMoney(player);
                                }
                                break;    
                                
                        }}
                }
            }
        }
    };
    }
    private static Npc Skien_trungthu(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tu Tiên\n" + //
                                       
                                         "|8|Đan Trúc Cơ Hiện Có: " + player.TamkjllDauLaDaiLuc[0]
                                        + "\n|8|Đan Trung Kì Hiện Có: " +player.TamkjllDauLaDaiLuc[1]
                                        + "\n|8|Đan Nguyên Anh Hiện Có: " + player.TamkjllDauLaDaiLuc[2]
                                        + "\n|8|Đan Thượng Phẩm Hiện Có: " + player.TamkjllDauLaDaiLuc[3]
                                        + "\n|8|Đan Hỏa Thần Hiện Có: " + player.TamkjllDauLaDaiLuc[4]
                                        + "\n|8|Đan Hóa Thần Hiện Có: " + player.TamkjllDauLaDaiLuc[5]
                                        + "\n|8|Đan Thánh Đế Hiện Có "
                                        + player.TamkjllDauLaDaiLuc[6]
                           +"\n|1|Tu Vi Của Bạn Là : " + Util.format(player.ExpTamkjll),
                            "Thông Tin Đan", "Shop Đan");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0: {
                                this.createOtherMenu(player, 1234,
                                          "Ngọc Rồng Tu Tiên\n"
                                                  +//luwdev
                                                 "|5|Đan Trúc Cơ: 100k hp, 10k dame."
                                                + "\n|5|Đan Trung Kì: 1tr hp, 100k dame."
                                                + "\n|5|Đan Nguyên Anh: T\u0103ng 10tr hp, 1tr dame."
                                                + "\n|5|Đan Thượng Phẩm 100tr hp, 10tr dame."
                                                + "\n|5|Đan Hỏa Thần 1tỉ hp, 100tr dame."
                                                + "\n|5|Đan Hóa Thần: 10t\u1EC9 hp, 1t\u1EC9 dame."
                                                + "\n|5|Kì Đan Nghìn Vạn Năm: 100t\u1EC9 hp, 10t\u1EC9 dame.",
                                        "Đã hiểu");
                                break;
                            }
                            case 1: {
                                //    case 9:
                                   ShopServiceNew.gI().opendShop(player, "DANDUOC", false);                                                                      
                            //        break;
                                break;
                            }
                         
                            case 3:
                            Service.getInstance().sendThongBaoOK(player,TopService.gettopnaubanhtrung());
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == 1111) {
                        switch (select) {
                            case 0: {
                                Item hatsen = null;
                                Item botnep = null;
                                Item moilua = null;
                                try {
                                    hatsen = InventoryServiceNew.gI().findItemBag(player, 949);
                                    botnep = InventoryServiceNew.gI().findItemBag(player, 1249);
                                    moilua = InventoryServiceNew.gI().findItemBag(player, 1250);
                                } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                }
                                if (hatsen == null || hatsen.quantity < 99) {
                                    this.npcChat(player, "|7|Bạn không đủ 99 Thịt Mỡ");
                                } else if (botnep == null || botnep.quantity < 50) {
                                    this.npcChat(player, "|7|Bạn không đủ 50 Hạt nếp");
                                } else if (moilua == null || moilua.quantity < 2) {
                                    this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
                                } else if (player.inventory.gold < 2_000_000_000) {
                                    this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 2_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
                                    Service.getInstance().sendMoney(player);
                                    Item banhtrungthu = ItemService.gI().createNewItem((short) 465);
                                    InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được Bánh Nhưng Mỡ");
                                }
                                break;
                            }
                            case 1: {
                                Item dauxanh = null;
                                Item botnep = null;
                                Item moilua = null;
                                try {
                                    dauxanh = InventoryServiceNew.gI().findItemBag(player, 950);
                                    botnep = InventoryServiceNew.gI().findItemBag(player, 1249);
                                    moilua = InventoryServiceNew.gI().findItemBag(player, 1250);
                                } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                }
                                if (dauxanh == null || dauxanh.quantity < 99) {
                                    this.npcChat(player, "|7|Bạn không đủ 99 Thịt Nạt");
                                } else if (botnep == null || botnep.quantity < 50) {
                                    this.npcChat(player, "|7|Bạn không đủ 50 Hạt Nếp");
                                } else if (moilua == null || moilua.quantity < 2) {
                                    this.npcChat(player, "|7|Bạn không đủ 2 Mồi lửa");
                                } else if (player.inventory.gold < 2_000_000_000) {
                                    this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 2_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 50);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 2);
                                    Service.getInstance().sendMoney(player);
                                    Item banhtrungthu = ItemService.gI().createNewItem((short) 466);
                                    InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được Bánh Nhưng Thịt");
                                }
                                break;
                            }
                            case 2: {
                                Item hatsen = null;
                                Item dauxanh = null;
                                Item botnep = null;
                                Item moilua = null;
                                try {
                                    hatsen = InventoryServiceNew.gI().findItemBag(player, 949);
                                    dauxanh = InventoryServiceNew.gI().findItemBag(player, 1339);
                                    botnep = InventoryServiceNew.gI().findItemBag(player, 1249);
                                    moilua = InventoryServiceNew.gI().findItemBag(player, 1250);
                                } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                }
                                if (hatsen == null || hatsen.quantity < 99) {
                                    this.npcChat(player, "|7|Bạn không đủ 99 Thịt Nạt");
                                } else if (botnep == null || botnep.quantity < 99) {
                                    this.npcChat(player, "|7|Bạn không đủ 99 Thịt Mỡ");
                                } else if (dauxanh == null || dauxanh.quantity < 99) {
                                    this.npcChat(player, "|7|Bạn không đủ 99 Hạt Nếp");
                                } else if (moilua == null || moilua.quantity < 5) {
                                    this.npcChat(player, "|7|Bạn không đủ 5 Mồi lửa");
                                } else if (player.inventory.gold < 2_000_000_000) {
                                    this.npcChat(player, "|7|Bạn không đủ 2Ty Vàng");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                    this.npcChat(player, "|7|Hành trang của bạn không đủ chỗ trống");
                                } else {
                                    player.inventory.gold -= 2_000_000_000;
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, hatsen, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, dauxanh, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, botnep, 99);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, moilua, 5);
                                    Service.getInstance().sendMoney(player);
                                    Item banhtrungthu = ItemService.gI().createNewItem((short) 472);
                                    InventoryServiceNew.gI().addItemBag(player, banhtrungthu);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được Bánh Trưng Thập cẩm");
                                }
                                break;
                            }
                        }
                    } else if (player.iDMark.getIndexMenu() == 2222) {
                        switch (select) {
                            case 0: {
                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
                                int manh = Manager.itemManh[randommanh];
                                if (player.diemtrungthu < 500) {
                                    this.npcChat(player, "|7|Bạn không đủ 500 Điểm sự kiện");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 5) {
                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 5 ô trống");
                                } else {
                                    player.diemtrungthu -= 500;
                                    player.diemnaubanhtrung += 500;
                                    player.inventory.ruby += 250_000;
                                    Service.getInstance().sendMoney(player);
                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
                                    Item hoptt = ItemService.gI().createNewItem((short) 1186);
                                    Item phieugg = ItemService.gI().createNewItem((short) 459);
                                    Item thegh = ItemService.gI().createNewItem((short) 1191);
                                    phieugg.itemOptions.add(new Item.ItemOption(30, 1));
                                    manhthiensu.quantity = 200;
                                    ruongthan.quantity = 50;
                                    hoptt.quantity = 30;
                                    thegh.quantity = 30;
                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
                                    InventoryServiceNew.gI().addItemBag(player, hoptt);
                                    InventoryServiceNew.gI().addItemBag(player, phieugg);
                                    InventoryServiceNew.gI().addItemBag(player, thegh);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được 200 " + manhthiensu.template.name
                                            + ", 50 Rương thần linh, 30 Hộp Bánh Trưng, 30 Thẻ gia hạn, 1 Phiếu giảm giá và 250k Hồng ngọc");
                                }
                                break;
                            }
                            case 1: {
                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
                                int manh = Manager.itemManh[randommanh];
                                if (player.diemtrungthu < 300) {
                                    this.npcChat(player, "|7|Bạn không đủ 300 Điểm sự kiện");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
                                } else {
                                    player.diemtrungthu -= 300;
                                    player.diemnaubanhtrung += 300;
                                    player.inventory.ruby += 150_000;
                                    Service.getInstance().sendMoney(player);
                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
                                    Item hoptt = ItemService.gI().createNewItem((short) 1186);
                                    Item thegh = ItemService.gI().createNewItem((short) 1191);
                                    manhthiensu.quantity = 100;
                                    ruongthan.quantity = 40;
                                    hoptt.quantity = 15;
                                    thegh.quantity = 10;
                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
                                    InventoryServiceNew.gI().addItemBag(player, hoptt);
                                    InventoryServiceNew.gI().addItemBag(player, thegh);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được 100 " + manhthiensu.template.name
                                            + ", 40 Rương thần linh, 15 Hộp Bánh trưng, 10 Thẻ gia hạn và 150k Hồng ngọc");
                                }
                                break;
                            }
                            case 2: {
                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
                                int manh = Manager.itemManh[randommanh];
                                if (player.diemtrungthu < 200) {
                                    this.npcChat(player, "|7|Bạn không đủ 200 Điểm sự kiện");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 4) {
                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 4 ô trống");
                                } else {
                                    player.diemtrungthu -= 200;
                                    player.diemnaubanhtrung += 200;
                                    player.inventory.ruby += 100_000;
                                    Service.getInstance().sendMoney(player);
                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
                                    Item hoptt = ItemService.gI().createNewItem((short) 1186);
                                    Item thegh = ItemService.gI().createNewItem((short) 1191);
                                    manhthiensu.quantity = 50;
                                    ruongthan.quantity = 30;
                                    hoptt.quantity = 10;
                                    thegh.quantity = 5;
                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
                                    InventoryServiceNew.gI().addItemBag(player, hoptt);
                                    InventoryServiceNew.gI().addItemBag(player, thegh);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được 50 " + manhthiensu.template.name
                                            + ", 30 Rương thần linh, 10 Hộp Bánh trưng, 5 Thẻ gia hạn và 100k Hồng ngọc");
                                }
                                break;
                            }
                            case 3: {
                                byte randommanh = (byte) new Random().nextInt(Manager.itemManh.length);
                                int manh = Manager.itemManh[randommanh];
                                if (player.diemtrungthu < 50) {
                                    this.npcChat(player, "|7|Bạn không đủ 50 Điểm sự kiện");
                                } else if (InventoryServiceNew.gI().getCountEmptyBag(player) < 2) {
                                    this.npcChat(player, "|7|Hành trang của bạn cần ít nhất 2 ô trống");
                                } else {
                                    player.diemtrungthu -= 50;
                                    player.diemnaubanhtrung += 50;
                                    player.inventory.ruby += 25_000;
                                    Service.getInstance().sendMoney(player);
                                    Item manhthiensu = ItemService.gI().createNewItem((short) manh);
                                    Item ruongthan = ItemService.gI().createNewItem((short) 2005);
                                    manhthiensu.quantity = 10;
                                    ruongthan.quantity = 5;
                                    InventoryServiceNew.gI().addItemBag(player, manhthiensu);
                                    InventoryServiceNew.gI().addItemBag(player, ruongthan);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    this.npcChat(player, "|4|Bạn nhận được 10 " + manhthiensu.template.name
                                            + ", 5 Rương thần linh và 25k Hồng ngọc");
                                }
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc thuongDe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    }
                    if (this.mapId == 141) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "ở đây rất nguy hiểm con có muốn đi tiếp khon??",
                                  "Đồng ý", "Từ chối");
                    }
                    if (this.mapId == 129) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay ve");
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 141) { 
                       switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 144, -1, 354);
                                break;
                            }
                        }
                    }
                    if (this.mapId == 129) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                                break;
                        }
                    }
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }           
        };
    }
    
     public static Npc robotsiucap(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 45) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào", "Đến Kaio", "Quay số\nmay mắn");
                    }
                    if (this.mapId == 169) {
                        this.createOtherMenu(player, 0,
                                "\b|3|Con muốn gì nào?\nCon đang còn : " + player.pointPvp + " \b|7|điểm Đánh Boss", "Đến Khu Boss", "Đổi Cải trang sự kiên", "Top Đánh Boss");
                    }
                    if (this.mapId == 129) {
                        this.createOtherMenu(player, 0,
                                "Con muốn gì nào?", "Quay ve");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 169) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 170, -1, 354);
                                    Service.getInstance().changeFlag(player, Util.nextInt(8));
                                    break; // qua dhvt
                                case 1:  // 
                                    this.createOtherMenu(player, 1,
                                            "Bạn có muốn đổi 500 điểm PVP lấy \n|6|Cải trang Mèo Kid Lân với tất cả chỉ số là 80%\n ", "Ok", "Tu choi");
                                    // bat menu doi item
                                    break;

                                case 2:  // 
                                    Util.showListTop(player, (byte) 3);
                                    // mo top pvp
                                    break;

                            }
                        }
                        if (player.iDMark.getIndexMenu() == 1) { // action doi item
                            switch (select) {
                                case 0: // trade
                                    if (player.pointPvp >= 500) {
                                        player.pointPvp -= 500;
                                        Item item = ItemService.gI().createNewItem((short) (1104));
                                        item.itemOptions.add(new Item.ItemOption(49, 80));
                                        item.itemOptions.add(new Item.ItemOption(77, 80));
                                        item.itemOptions.add(new Item.ItemOption(103, 50));
                                        item.itemOptions.add(new Item.ItemOption(207, 0));
                                        item.itemOptions.add(new Item.ItemOption(33, 0));
//                                      
                                        InventoryServiceNew.gI().addItemBag(player, item);
                                        Service.getInstance().sendThongBao(player, "Chúc Mừng Bạn Đổi Cải Trang Thành Công !");
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Không đủ điểm bạn còn " + (500 - player.pointPvp) + " Điểm nữa");
                                    }
                                    break;
                            }
                        }
                    }
                    if (this.mapId == 129) {
                        switch (select) {
                            case 0: // quay ve
                                ChangeMapService.gI().changeMapBySpaceShip(player, 0, -1, 354);
                                break;
                        }
                    }
                    if (this.mapId == 45) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                            "Con muốn làm gì nào?", "Quay bằng\nvàng",
                                            "Rương phụ\n("
                                            + (player.inventory.itemsBoxCrackBall.size()
                                            - InventoryServiceNew.gI().getCountEmptyListItem(player.inventory.itemsBoxCrackBall))
                                            + " món)",
                                            "Xóa hết\ntrong rương", "Đóng");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                            switch (select) {
                                case 0:
                                    LuckyRound.gI().openCrackBallUI(player, LuckyRound.USING_GOLD);
                                    break;
                                case 1:
                                    ShopServiceNew.gI().opendShop(player, "ITEMS_LUCKY_ROUND", true);
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player,
                                            ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                            "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                            + "sẽ không thể khôi phục!",
                                            "Đồng ý", "Hủy bỏ");
                                    break;
                            }
                        }
                    }

                }
            }
        };
    }

    public static Npc thanVuTru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Con muốn làm gì nào?", "Di chuyển");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                            "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio", "Con\nđường\nrắn độc", "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                                case 2:
                                    if (player.clan != null) {
                                        if (player.clan.ConDuongRanDoc != null) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPENED_CDRD,
                                                    "Bang hội của con đang đi con đường rắn độc cấp độ "
                                                    + player.clan.ConDuongRanDoc.level + "\nCon có muốn đi theo không?",
                                                    "Đồng ý", "Từ chối");
                                        } else {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_CDRD,
                                                    "Đây là Con đường rắn độc \nCác con cứ yên tâm lên đường\n"
                                                    + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                    "Chọn\ncấp độ", "Từ chối");
                                        }
                                     } else {
                                        this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                    }
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin() || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        ChangeMapService.gI().goToCDRD(player);
                                    } else {
                                        Service.gI().sendThongBao(player, "Không đủ sức mạnh yêu cầu");
                                    }
                                    if (player.clan.haveGoneConDuongRanDoc) {
                                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Bang hội của ngươi đã đi con đường rắn độc lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenConDuongRanDoc, "HH:mm:ss") + " hôm nay. Người mở\n"
                                                + "(" + player.clan.playerOpenDoanhTrai + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                                        return;
                                    } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                        Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 2 ngày!");
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;

                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_CDRD) {
                            switch (select) {
                                case 0:
                                    if (player.isAdmin() || player.nPoint.power >= ConDuongRanDoc.POWER_CAN_GO_TO_CDRD) {
                                        Input.gI().createFormChooseLevelCDRD(player);
                                    } else {
                                        this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                + Util.numberToMoney(ConDuongRanDoc.POWER_CAN_GO_TO_CDRD));
                                    }
                                    break;
                            }

                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                            switch (select) {
                                case 0:
                                    ConDuongRanDocService.gI().openConDuongRanDoc(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                    break;
                            }
                        }
                    }
                }
            }

        };
    }

    public static Npc giuma(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 6 || this.mapId == 25 || this.mapId == 26) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                    } else if (this.mapId == 139) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Người muốn trở về?", "Quay về", "Từ chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                //đến potaufeu
                                ChangeMapService.gI().goToPotaufeu(player);
                            }
                        }
                    } else if (this.mapId == 139) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                //về trạm vũ trụ
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                   } else if (this.mapId == 154) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn đi về Trái Đất hay đi đến Thung Lũng Hủy Diệt?",
                                "Về thánh địa","Đến\nHành tinh\nNgục tù","Đóng");//,"Đến\n Thung Lũng Hủy Diệt", "Đến\n Hành tinh\nNgục tù", "Đến\n Cánh Đồng\nThiên Sứ" ,"Hướng dẫn");
                   } else if (this.mapId == 208) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp ngươi trở về Vùng Đất của các Vị Thần ?",
                                "Quay về\n Vùng Đất của Thần", "Tạm biệt");
                   } else if (this.mapId == 218) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp ngươi trở về Vùng Đất của các Vị Thần ?",
                                "Quay về\n Vùng Đất của Thần", "Tạm biệt");
                   } else if (this.mapId == 155) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp ngươi trở về Vùng Đất của các Vị Thần ?",
                                "Quay về\n Vùng Đất của Thần", "Tạm biệt");
                   } else if (this.mapId == 52) {
                        try {
                            MapMaBu.gI().setTimeJoinMapMaBu();
                            if (this.mapId == 52) {
                                long now = System.currentTimeMillis();
                                if (now > MapMaBu.TIME_OPEN_MABU && now < MapMaBu.TIME_CLOSE_MABU) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB, "Đại chiến Ma Bư đã mở, "
                                            + "ngươi có muốn tham gia không?",
                                            "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }

                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu osin");
                        }

                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.fightMabu.pointMabu >= player.fightMabu.POINT_MAX) {
                            this.createOtherMenu(player, ConstNpc.GO_UPSTAIRS_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Lên Tầng!", "Quay về", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                    "Quay về", "Từ chối");
                        }
                    } else if (this.mapId == 120) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 50) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 154, -1, 200, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 154) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                    break;
                               case 5:
                                    ChangeMapService.gI().changeMap(player, 208, -1, 181, 144);
                                    break;
                                case 1:
                                    ChangeMapService.gI().changeMap(player, 155, -1, 166, 792);
                                    break;
                                case 7:
                                    ChangeMapService.gI().changeMap(player, 218, -1, 70, 792);
                                    break;
                            }
                        }
                    } else if (this.mapId == 208) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 780, 312);
                            }
                        }
                    } else if (this.mapId == 218) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 780, 312);
                            }
                        }
                    } else if (this.mapId == 155) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMap(player, 154, -1, 780, 312);
                            }
                        }
                    } else if (this.mapId == 52) {
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                                break;
                            case ConstNpc.MENU_OPEN_MMB:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                } else if (select == 1) {
                                    ChangeMapService.gI().changeMap(player, 114, -1, 318, 336);
                                }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_MA_BU);
                                }
                                break;
                        }
                    } else if (this.mapId >= 114 && this.mapId < 120 && this.mapId != 116) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.GO_UPSTAIRS_MENU) {
                            if (select == 0) {
                                player.fightMabu.clear();
                                ChangeMapService.gI().changeMap(player, this.map.mapIdNextMabu((short) this.mapId), -1, this.cx, this.cy);
                            } else if (select == 1) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        } else {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    } else if (this.mapId == 120) {
                        if (player.iDMark.getIndexMenu() == ConstNpc.BASE_MENU) {
                            if (select == 0) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                            }
                        }
                    }
                }
            }
        };
    }
           public static Npc kibit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
                   return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    mabu2h.gI().setTimeJoinmabu2h();} 
                            if (this.mapId == 52) {                                               
                                long now = System.currentTimeMillis();
                                if (now > mabu2h.TIME_OPEN_2h && now < mabu2h.TIME_CLOSE_2h) {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_MMB,   "Ma Bư đã hồi sinh hãy giải cứu Piccolo",
                                           "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_MMB,
                                        "Xin hãy cứu lấy người dân",
                                             "Hướng dẫn", "Từ chối");
                                }                           
                            }
                        }    
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                   switch (this.mapId) {
                        case 52:
                        switch (player.iDMark.getIndexMenu()) {
                            case ConstNpc.MENU_REWARD_MMB:
                            case ConstNpc.MENU_OPEN_MMB:
                                    if (select == 0)
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_2h);
                                    if (!player.getSession().actived) {                                   
                                    }
                                   if (select == 1){
                                        ChangeMapService.gI().changeMap(player, 127, 0, 66, 312);
                                        break;
                                                            }
                                break;
                            case ConstNpc.MENU_NOT_OPEN_BDW:
                                if (select == 0) {
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_MAP_2h);
                                }
                                break;
                               }
                          }
                      }
                  }    
              };
           }
    public static Npc linhCanh(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.clan == null) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                        return;
                    }
                    if (player.clan.getMembers().size() < DoanhTrai.N_PLAYER_CLAN) {
                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                        return;
                    }
                    if (player.clan.doanhTrai != null) {
                        createOtherMenu(player, ConstNpc.MENU_OPENED_DOANH_TRAI,
                                "Bang hội của ngươi đang đánh trại độc nhãn\n"
                                + "Thời gian còn lại là "
                                + TimeUtil.getMinLeft(player.clan.timeOpenDoanhTrai, DoanhTrai.TIME_DOANH_TRAI / 1000)
                                + " phút. Ngươi có muốn tham gia không?",
                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    int nPlSameClan = 0;
                    for (Player pl : player.zone.getPlayers()) {
                        if (!pl.equals(player) && pl.clan != null
                                && pl.clan.equals(player.clan) && pl.location.x >= 1285
                                && pl.location.x <= 1645) {
                            nPlSameClan++;
                        }
                    }
                    if (nPlSameClan < DoanhTrai.N_PLAYER_MAP) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Ngươi phải có ít nhất " + DoanhTrai.N_PLAYER_MAP + " đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                + "Hahaha.", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 0) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Doanh trại chỉ cho phép những người ở trong bang trên 0 ngày. Hẹn ngươi quay lại vào lúc khác",
                                "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    if (player.clan.haveGoneDoanhTrai) {
                        createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Bang hội của ngươi đã đi trại lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenDoanhTrai, "HH:mm:ss") + " hôm nay. Người mở\n"
                                + "(" + player.clan.playerOpenDoanhTrai + "). Hẹn ngươi quay lại vào ngày mai", "OK", "Hướng\ndẫn\nthêm");
                        return;
                    }
                    createOtherMenu(player, ConstNpc.MENU_JOIN_DOANH_TRAI,
                            "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                            + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                            "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_DOANH_TRAI:
                            if (select == 0) {
                                DoanhTraiService.gI().opendoanhtrai(player);
                            } else if (select == 2) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.IGNORE_MENU:
                            if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DOANH_TRAI);
                            }
                            break;
                        case ConstNpc.MENU_OPENED_DOANH_TRAI:
                            if (select == 0) {
                                ChangeMapService.gI().changeMapInYard(player, 53, player.clan.doanhTrai.id, 60);
                            }
                            break;
                    }
                }
            }
        };
    }
    private static Npc mrpopo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                        if (player.getSession().is_gift_box) {
//                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?", "Giải tán bang hội", "Nhận quà\nđền bù");
                        } else {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Thượng Đế vừa phát hiện 1 loại khí đang âm thầm\nhủy diệt mọi mầm sống trên Trái Đất,\nnó được gọi là Destron Gas.\nTa sẽ đưa các cậu đến nơi ấy, các cậu sẵn sàng chưa?","Thông tin chi tiết","OK","Từ chối");
                        }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                return;
                        //    case 1:
                        //        return;
                            case 1:
                              if (player.clan != null) {
                                    if (player.clan.KhiGaHuyDiet != null) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPENED_KGHD,
                                                "Bang hội của con đang đi khí ga hủy diệt cấp độ "
                                                        + player.clan.KhiGaHuyDiet.level + "\nCon có muốn đi theo không?",
                                                "Đồng ý", "Từ chối");
                                    } else {

                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_KGHD,
                                                "Đây là khí ga hủy diệt \nCác con cứ yên tâm lên đường\n"
                                                        + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                "Chọn\ncấp độ", "Từ chối");
                                    }
                                } else {
                                    this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                }
                                break;
                            case 3:
                                return;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_KGHD) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    ChangeMapService.gI().goToKGHD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;

                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_KGHD) {
                        switch (select) {
                            case 0:
                                if (player.isAdmin() || player.nPoint.power >= KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD) {
                                    Input.gI().createFormChooseLevelKGHD(player);
                                } else {
                                    this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                            + Util.numberToMoney(KhiGasHuyDiet.POWER_CAN_GO_TO_KGHD));
                                }
                                break;
                        }

                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_KGHD) {
                        switch (select) {
                            case 0:
                                KhiGasHuyDietService.gI().openKhiGaHuyDiet(player, Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                break;
                        }
                }
            }
        };
      };
    }
    public static Npc quaTrung(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            private final int COST_AP_TRUNG_NHANH = 1000000000;

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    player.mabuEgg.sendMabuEgg();
                    if (player.mabuEgg.getSecondDone() != 0) {
                        this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                "Hủy bỏ\ntrứng", "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                    } else {
                        this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở", "Hủy bỏ\ntrứng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.CAN_NOT_OPEN_EGG:
                            if (select == 0) {
                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                            } else if (select == 1) {
                                if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                    player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                    player.mabuEgg.timeDone = 0;
                                    Service.getInstance().sendMoney(player);
                                    player.mabuEgg.sendMabuEgg();
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để thực hiện, còn thiếu "
                                            + Util.numberToMoney((COST_AP_TRUNG_NHANH - player.inventory.gold)) + " vàng");
                                }
                            }
                            break;
                        case ConstNpc.CAN_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                            "Bạn có chắc chắn cho trứng nở?\n"
                                            + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                            "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda", "Từ chối");
                                    break;
                                case 1:
                                    this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                            "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_OPEN_EGG:
                            switch (select) {
                                case 0:
                                    player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                    break;
                                case 1:
                                    player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                    break;
                                case 2:
                                    player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case ConstNpc.CONFIRM_DESTROY_EGG:
                            if (select == 0) {
                                player.mabuEgg.destroyEgg();
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc quocVuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử  
                  
                                break;
                            case 2:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_HOP_NHAT,
                                               "|3|CHÀO CON!\n|5|--CON CÓ MUỐN HỢP NHẤT VỚI ĐỆ TỬ--"
                                                     + "\n|1|ĐỂ GIA TĂNG SỨC MẠNH BẢN THÂN HAY KHÔNG?"
                                                   + "\n|1|CON SẼ MẤT 30 TỈ SỨC MẠNH VÀ ĐỆ TỬ CỦA CON!!!"
                                                     + "\n|1|CHỈ SỐ GỐC SẼ SẼ ĐƯỢC CỘNG NGẪU NHIÊN CHO CON"
                                                       + "\n|1|HP Từ 20K-100K|SD Từ 5K-20K|KI Từ 20K-100K"
                                                        + "\n|7|Yêu Cầu:30 Tỷ TNSM 5"
                                                         + "\n|7|Yêu Cầu:" + Util.getFormatNumber(player.pet.nPoint.power) +
                                                         "/" + "200.000.000.000 sức mạnh",
                                                 "Hợp Nhất",  "từ chối");
                                    }}
                                //giới hạn đệ tử
                                break;    
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.gI().sendMoney(player);
                                    }
                                } else {
                                    Service.gI().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_HOP_NHAT) {
                        switch (select) {
                            case 0:
                                Item dt = null;

                                    try {
                                        dt = InventoryServiceNew.gI().findItemBag(player, 1763);
 
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (dt == null || dt.quantity < 1) {
                                       Service.gI().sendThongBao(player, "|2|CHỨC NĂNG YÊU CẦU CÓ 3 THẺ CHUYỂN SINH ĐỆ TỬ <>!");
                                    } else if (player.pet.nPoint.power < 200000000000L) { 
                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU ĐỆ TỬ PHẢI 200 TỈ SM NHÉ !");
                                   // } else if (player.nPoint.power <200000000000L) { 
                                    } else if (player.nPoint.power <200000000000L) {
                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU SƯ PHỤ TRIÊN 200 TỈ  TỈ SM NHÉ !");                                                                                                                      
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, dt, 1);
                                       // PetService.gI().createNormalPet(player);   tạo pett mới thì bật lên ko thì tắt đi 
                                        Service.gI().sendMoney(player);
                                        player.nPoint.power -= 30000000000L;
                                        player.pet.nPoint.power = 1200L;
                                        player.nPoint.hpg += Util.nextInt(20000, 100000); // chỉ số cộng cho sư phụ 
                                        player.nPoint.dameg += Util.nextInt(5000, 20000);
                                        player.nPoint.mpg += Util.nextInt(20000, 100000); 
                                        // chỉ số pet ki bị trừ 
                                        player.pet.nPoint.hpg = Util.nextInt(200, 2000);
                                        player.pet.nPoint.mpg = Util.nextInt(200, 2000);
                                        player.pet.nPoint.dameg = Util.nextInt(10, 200);
                                        player.pet.nPoint.critg = Util.nextInt(1, 5);
                                        player.pet.nPoint.defg  =  Util.nextInt(10, 309);
                                        player.pet.nPoint.critg = 1;
                                        player.pet.nPoint.defg = 100;
                                        player.pet.playerSkill.skills.get(1).skillId = -1;
                                        player.pet.playerSkill.skills.get(2).skillId = -1;
                                        player.pet.playerSkill.skills.get(4).skillId = -1;
                                        player.pet.playerSkill.skills.get(3).skillId = -1;    
                                        InventoryServiceNew.gI().sendItemBags(player);                                       
                                        Service.gI().sendThongBao(player, "|2| ĐỆ NHÀ NGƯƠI ĐÃ BỊ Hoang Van Luu RÚT MÁU HAHA 🤣 CHỈ \n chỉ số nhà người đã dc cộng ngẫu nghiên ");
                                      //  Client.gI().kickSession(player.getSession());
                                    break;
                                    }
                        }
                    }
                    else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.gI().sendMoney(player);
                                }
                            } else {
                                Service.gI().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }
     public static Npc blackrose(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?",
                        "Bản thân", "Đệ tử", "Từ chối");
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isBaseMenu()) {
                        switch (select) {
                            case 0:
                                if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                            + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                            "Nâng\ngiới hạn\nsức mạnh",
                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                            "Sức mạnh của con đã đạt tới giới hạn",
                                            "Đóng");
                                }
                                break;
                            case 1:
                                if (player.pet != null) {
                                    if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                        this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                "Đóng");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                }
                                //giới hạn đệ tử
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT) {
                        switch (select) {
                            case 0:
                                OpenPowerService.gI().openPowerBasic(player);
                                break;
                            case 1:
                                if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                    if (OpenPowerService.gI().openPowerSpeed(player)) {
                                        player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                        Service.getInstance().sendMoney(player);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Bạn không đủ vàng để mở, còn thiếu "
                                            + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                }
                                break;
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET) {
                        if (select == 0) {
                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                    Service.getInstance().sendMoney(player);
                                }
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn không đủ vàng để mở, còn thiếu "
                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                            }
                        }
                    }
                }
            }
        };
    }

    public static Npc bulmaTL(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                    if (canOpenNpc(player)) {

                        if (this.mapId == 102) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                                }
                        //        if (select == 1) {
                        //        ShopServiceNew.gI().opendShop(player, "QUA_BOSS", true);
                        //    }
                        }
                    }
                }
            }
        };
    }
     public static Npc caythong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "tao la?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 104) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 104 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "binn", true);
                            }
                        }
                    } else if (this.mapId == 104 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
//                                ShopServiceNew.gI().opendShop(player, "BUNMA_LINHTHU", true);
                            }
                        }
                    }
                }
            }
        };
    }

     public static Npc Brook(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {                   
                  if (this.mapId == 5) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                      "|7|Hư Vô Là Nơi Khí Tức Rồi Rào! \n|8|Giúp Tích Lũy EXP Tu Tiên Đột Phá Cảnh Giới,\n|5| Ngươi Có Muốn Tới Thí Luyện?",
                            "Tham Ra", "Đóng");
                  }
                        if (this.mapId == 181) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "|1|Cậu muốn quay về NHA VOI ME SAO",
                                "Đi thôi","Từ chối");
                    }
                }
            }
            
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player,181, -1, 792);
                                break;
                            }
                        }
                    }
                    if (this.mapId == 181) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 550);
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
                 
    public static Npc rongOmega(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    BlackBallWar.gI().setTime();
                    if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                        try {
                            long now = System.currentTimeMillis();
                            if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW, "Đường đến với ngọc rồng sao đen đã mở, "
                                        + "ngươi có muốn tham gia không?",
                                        "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                            } else {
                                String[] optionRewards = new String[7];
                                int index = 0;
                                for (int i = 0; i < 7; i++) {
                                    if (player.rewardBlackBall.timeOutOfDateReward[i] > System.currentTimeMillis()) {
                                        String quantily = player.rewardBlackBall.quantilyBlackBall[i] > 1 ? "x" + player.rewardBlackBall.quantilyBlackBall[i] + " " : "";
                                        optionRewards[index] = quantily + (i + 1) + " sao";
                                        index++;
                                    }
                                }
                                if (index != 0) {
                                    String[] options = new String[index + 1];
                                    for (int i = 0; i < index; i++) {
                                        options[i] = optionRewards[i];
                                    }
                                    options[options.length - 1] = "Từ chối";
                                    this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW, "Ngươi có một vài phần thưởng ngọc "
                                            + "rồng sao đen đây!",
                                            options);
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                            "Ta có thể giúp gì cho ngươi?", "Hướng dẫn", "Từ chối");
                                }
                            }
                        } catch (Exception ex) {
                            Logger.error("Lỗi mở menu rồng Omega");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_REWARD_BDW:
                            player.rewardBlackBall.getRewardSelect((byte) select);
                            break;
                        case ConstNpc.MENU_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            } else if (select == 1) {
                                player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                ChangeMapService.gI().openChangeMapTab(player);
                            }
                            break;
                        case ConstNpc.MENU_NOT_OPEN_BDW:
                            if (select == 0) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                            }
                            break;
                    }
                }
            }

        };
    }

    public static Npc rong1_to_7s(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.isHoldBlackBall()) {
                        this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?", "Phù hộ", "Từ chối");
                    } else {
                        if (BossManager.gI().existBossOnPlayer(player)
                                || player.zone.items.stream().anyMatch(itemMap -> ItemMapService.gI().isBlackBall(itemMap.itemTemplate.id))
                                || player.zone.getPlayers().stream().anyMatch(p -> p.iDMark.isHoldBlackBall())) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                        } else {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME, "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối", "Gọi BOSS");
                        }
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                        if (select == 0) {
                            this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                    "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                    "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                    "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                    "x9000 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                    "Từ chối"
                            );
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                        } else if (select == 2) {
                            BossManager.gI().callBoss(player, mapId);
                        } else if (select == 1) {
                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                        }
                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                        if (player.effectSkin.xHPKI > 1) {
                            Service.getInstance().sendThongBao(player, "Bạn đã được phù hộ rồi!");
                            return;
                        }
                        switch (select) {
                            case 0:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                break;
                            case 1:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                break;
                            case 2:
                                BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                break;
                            case 3:
                                this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                break;
                        }
                    }
                }
            }
        };
    }

  public static Npc bill(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 48) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Đả Thông Kinh Mạch" ,
                                "Shop");
     
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 48:
                          
                            Client.gI().kickSession(player.getSession());
                            break;
                    }
                }
            }
        };
    }

    public static Npc boMong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                "Chào bạn \btôi có thể giúp bạn làm nhiệm vụ", "Nhiệm vụ\nhàng ngày", "Nhận ngọc\nmiễn phí", "Danh hiệu");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 47 || this.mapId == 84) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.playerTask.sideTask.template != null) {
                                        String npcSay = "Nhiệm vụ hiện tại: " + player.playerTask.sideTask.getName() + " ("
                                                + player.playerTask.sideTask.getLevel() + ")"
                                                + "\nHiện tại đã hoàn thành: " + player.playerTask.sideTask.count + "/"
                                                + player.playerTask.sideTask.maxCount + " ("
                                                + player.playerTask.sideTask.getPercentProcess() + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                + player.playerTask.sideTask.leftTask + "/" + ConstTask.MAX_SIDE_TASK;
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                "Tôi có vài nhiệm vụ theo cấp bậc, "
                                                + "sức cậu có thể làm được cái nào?",
                                                "Dễ", "Bình thường", "Khó", "Siêu khó", "Địa ngục", "Từ chối");
                                    }
                                    break;
                                    case 1:
                                        player.achievement.Show();
                                    break;
                                    case 2:
                                    this.createOtherMenu(player, 888,
                                            "Đây là danh hiệu mà ngươi có"
                                                    + (player.lastTimeTitle1 > 0 ? "\n Danh hiệu VIP 1: " + Util.msToTime(player.lastTimeTitle1) : "")+ (player.lastTimeTitle2 > 0 ? "\n Danh hiệu Fan cứng: " + Util.msToTime(player.lastTimeTitle2): "")+ (player.lastTimeTitle3 > 0 ? "\n Danh hiệu Bất bại: " + Util.msToTime(player.lastTimeTitle3): ""),
                                            ("Danh hiệu \n VIP 1: " + (player.isTitleUse == true ? "On" : "Off")),
                                            ("Danh hiệu \n Fan cứng: " + (player.isTitleUse2 == true ? "On" : "Off") + "\n"),
                                            ("Danh hiệu \n Bất bại: " + (player.isTitleUse3 == true ? "On" : "Off") + "\n"),
                                            "Từ chối");
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    TaskService.gI().changeSideTask(player, (byte) select);
                                    break;
                            }
                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                            switch (select) {
                                case 0:
                                    TaskService.gI().paySideTask(player);
                                    break;
                                case 1:
                                    TaskService.gI().removeSideTask(player);
                                    break;
                            }
                            
                        }
                    }
                }
            }
        };
    }

     public static Npc karin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                        this.createOtherMenu(player, 0,
                                "Làm Lại Cuộc Đời\n"
                                        + "|1|Sau Khi Chuyển Sinh Tăng: \n50K HP,50K KI,10K SD\n"
                                          + "|1|Ngọc 1-3 Sao Săn Boss Bên Map Up Đá các Loại\n"
                                          + "|1|Ngọc 4-7 Sao Rơi Tại MAP Up Đồ Thần VIP\n"
                                        + "|1|Cần 10 Bộ Ngọc Rồng Huyết Long VÀ 5K Xu\n"
                                        + "|7|Bạn đạt 200 tỷ SỨC MẠNH\n"
                                        + "|2|Sức Mạnh hiện tại: " + Util.getFormatNumber(player.nPoint.power) + "/" + "200.000.000.000 sức mạnh",
                                "Chuyển sinh",
                                "Từ Chối");
                    }
                }
            }
        }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 46) {
                        if (player.iDMark.getIndexMenu() == 0) { // 
                             if (select == 0) {
                              OpenPowerService.gI().chuyenSinh(player);
                             
                             }
                       }
                    }
                }
            }
        };
    }
    public static Npc Mai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {                   
                  if (this.mapId == 5) {
                        createOtherMenu(player, ConstNpc.BASE_MENU,
                      "|7|Xin chào ,cảm ơn bạn đã ủng hộ cho Sever\n|1|Ngọc Rồng Tu Tiên [tutien.space]",
                            "Nạp Xu","Nạp Ngọc Hư Vô","Quà Nạp Mốc", "Đóng");
                    }
                }
            }

             @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                       case 0:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI, "|7|Số tiền của bạn còn : " + player.getSession().vnd + "\n"
                                            + "Muốn quy đổi không", "Quy Đổi\n10.000\n 10K Xu Ác Quỷ", "Quy Đổi\n20.000\n 20k Xu ", "Quy Đổi\n50.000\n 60K Xu", "Quy Đổi\n100.000\n 120K Xu", "Quy Đổi\n200.000 \n250K ", "Đóng");
                                    break;
                                     case 1:
                                    this.createOtherMenu(player, ConstNpc.QUY_DOI1, "|7|Số tiền của bạn còn : " + player.getSession().vnd + "\n"
                                            + "Muốn quy đổi không", "10K\n100 Ngọc", "50K\n500 Ngọc", "100K\n1K3 Ngoc", "200K\n2K5 Ngọc ", "Đóng");
                                    break;
                                    case 333:
                                this.createOtherMenu(player, 2,
                                        "|1|Ngươi muốn đổi Quà Nạp à?\n|7|Mốc 20K: 99 Viên 3S,7S 99 Lê Dược X5 30 Item Siêu Cấp"
                                                + "\n|1|Mốc 50K:1 Món TS Kh,30 Lê Dược,20 Máy Dò Boss,CT 200% SD"
                                                + "\n|1|Mốc 100K: 2 Món Kích Hoạt Thiên Sứ,100 Viên Đá Bóng Tối,10 Bộ NRO 1S,50 Máy Dò,50 Lê Dược X7"
                                                + "\n|1|Mốc 200k: x99(CN,BH,GX,BK), 1 Set Kích Hoạt Thiên Sứ ,1 Cải Trang 300% SD,HP,KI,100 Máy Dò Boss"
                                                + "\n|1|Mốc 500K: PET 300% Chỉ Số,Ván Bay 100% Chỉ Số,100 máy dò boss,1 Set Thiên Sứ,x200 item siêu cấp, x200 viên đá các loại"
                                                + "\n|1|Mốc 1M:1 CT 500%,1 Chân Mệnh 300%,1 Huy Hiệu(đỏ)50K SD\n2 Set Kích Hoạt Thiên Sứ, BTC5,299 Item (full),ĐÁ 300 Viên Đá Các Loại"
                                                + "\n|1|Mốc 2M:UPDATE...."
                                        + "\b|7|Bạn đang có :" +  player.getSession().tongnap + " Tổng Nạp",
                                         "Nhận Mốc 20K", "Nhận Mốc 50K", "Nhận Mốc 100K","Nhận Mốc 200K","Nhận Mốc 500K","Nhận Mốc 1M","Nhận Mốc 2M");
                                    break;
                                        case 2:
                                createOtherMenu(player, 0,
                                        "Xin chào\n|2|Hiện tại con đã nạp : " + Util.format(player.getSession().tongnap) + " VNĐ"
                               
                                        + "\n|-1|Con muốn nhận phần thường nào?",
                                        "Nhận Quà\nMốc Nạp");
                                break;
                            }
                        }
                      else if (player.iDMark.getIndexMenu() == 1){
                            switch (select) {
                                case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                //hoang van luu
                            }
                            } else if (player.iDMark.getIndexMenu() == 2) {
                        switch (select) {
                                    case 0:
                        if(player.getSession().tongnap >= 20000)   {
                            if (player.mocnap1 == 0){
//                            PlayerDAO.updatediemthuongnap(player,10);
//                                Item _item = ItemService.gI().createNewItem((short)457, 1); 
                                
//                                Item _item4 = ItemService.gI().createNewItem((short) 711,10);
                                player.mocnap1 += 1;
                                Service.getInstance().sendMoney(player);
//                             
                                InventoryServiceNew.gI().sendItemBags(player);
//                                Service.getInstance().sendThongBao(player, "Đang Lỗi Ib Key Vàng Nhé");// + _item.template.name);
//                                
////                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được 10 " + _item4.template.name);
//                            }else{
//                                Service.getInstance().sendThongBao(player, "Hiện Tại Đang Bảo Trì Mốc Nạp Trong Game Vui Lòng Ib Key Bạc Để Nhận Thưởng Mốc");
//                            }
//                            break;
//                        }else{
                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                        }
                        break;
                        }
                        case 1:
                            if(player.getSession().tongnap >= 50000)   {
                               if (player.mocnap1 == 1){
//                        
                            }else{
                                Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                            }
                            break;
                            }
                        case 2:
                            if(player.getSession().tongnap >= 100000)   {
                                if (player.mocnap1 == 2){
                         
                                InventoryServiceNew.gI().sendItemBags(player);
                                   Service.getInstance().sendThongBao(player, "Hiện Tại Đang Bảo Trì Mốc Nạp Trong Game Vui Lòng Ib Key Bạc Để Nhận Thưởng Mốc");// + _item.template.name);
                                
//                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được 10 " + _item4.template.name);
                            }else{
                                Service.getInstance().sendThongBao(player, "Hiện Tại Đang Bảo Trì Mốc Nạp Trong Game Vui Lòng Ib Key Bạc Để Nhận Thưởng Mốc");
                            }
                            break;
                        }else{
                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                        }
                            break;
                        case 3:
                            if(player.getSession().tongnap >= 200000)   {
                            if (player.mocnap1 == 3){
                      
                                player.mocnap1 += 1;
//                               
                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                        }
                            break;
                            }
                        case 4:
                            if(player.getSession().tongnap >= 500000)   {
                            if (player.mocnap1 == 4){
//                        
//                            
                            Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                        }
                            break;
                        }
                        }
                        } else if (player.iDMark.getIndexMenu() == 0) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, 100,
                                        "Xin chào"
                                        + "\n|2|Hiện tại con đã nạp được: " + Util.format(player.getSession().tongnap) + " VNĐ"
                                        + "\n|-1|Con hãy chọn mốc nạp muốn nhận?",
                                    "Xem Mốc\n50K VNĐ", "Xem Mốc\n100K VNĐ", "Xem Mốc\n200K VNĐ", "Xem Mốc\n300K VNĐ",
                                        "Xem Mốc\n500K VNĐ", "Xem Mốc\n800K VNĐ", "Xem Mốc\n1 TRIỆU VNĐ", "Xem Mốc\n1 TRIỆU 5 VNĐ","Xem Mốc\n 2TR VND");
                                break;
                       }
                        /*Mốc Nạp*/                    } else if (player.iDMark.getIndexMenu() == 100) {
                        switch (select) {
                            case 0:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 50.000 VNĐ Gồm:"
                                        + "\n|2|- 1 Món Thiên Sứ Kích Hoạt"
                                        + "\n- 1 Cải Trang FUll 175% Chỉ Số"
                                        + "\n- 7M Tu Vi"
                                        + "\n- 10 Máy Dò Boss"
                                        + "\n- 99 Ngọc Rồng 3,6,7 Sao"
                                        + "\n- 30 Lê Dược X7 TNSM"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                                 case 1:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 100.000 VNĐ Gồm:"
                                        + "\n|2|- 2 Món Thiên Sứ Kích Hoạt"
                                        + "\n- 10M Tu VI"
                                        + "\n- 10 Máy Dò Boss"
                                        + "\n- 10 Bộ Ngọc Rồng"
                                        + "\n- 50 Lê Dược X7"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                            case 2:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 200.000 VNĐ Gồm:"
                                        + "\n|2|- 1 Set Thiên Sứ Kích Hoạt"
                                        + "\n- 1 Món Chí Tôn"
                                        + "\n- PET Rồng 75% Chỉ Số"
                                        + "\n- 15M Tu Vi"
                                        + "\n- X99 CN,BH,BK(Siêu Cấp)"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                            case 3:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 300.000 VNĐ Gồm:"
                                        + "\n|2|- Huy Hiệu 50K Sức Đánh"
                                        + "\n- 1 Món Chí Tôn"
                                        + "\n- 10 Máy Dò Boss"
                                        + "\n- 30M Tu Vi"
                                        + "\n- 10 Bộ Ngọc Rồng"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                            case 4:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 500.000 VNĐ Gồm:"
                                        + "\n|2|- Thú Cưỡi(VIP) 100% Chỉ Số"
                                        + "\n- X200 ITEM Siêu Cấp"
                                        + "\n- 1 Món Chí Tôn"
                                        + "\n- 20 Máy Dò Boss"
                                        + "\n- 50M Tu Vi"
                                        + "\n- X50 Lê Dược X7"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                            case 5:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 800.000 VNĐ Gồm:"
                                        + "\n|2|- Chân Mệnh 300% Chỉ Số"
                                        + "\n- 80M Tu Vi"
                                        + "\n- 1 Món Chí Tôn"
                                        + "\n- 20 Máy Dò Boss"
                                        + "\n- X99 Lê Dược X7"
                                        + "\n- X99 ITEM Siêu Cấp"
                                        + "\n|7|Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                            case 6:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 1.000.000 VNĐ Gồm:"
                                        + "\n|2|- Ngọc [Level 99] 200% Chỉ Số"
                                        + "\n- 100M Tu Vi"
                                        + "\n- 2 Món Chí Tôn" 
                                        + "\n- X10 Bộ Huyết Long"        
                                        + "\n- 50 Máy Dò Boss"
                                        + "\n- 99 iTEM Siêu Cấp "
                                        + "\n- 50 Bộ Ngọc Rồng"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                            case 7:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 1.500.000 VNĐ Gồm:"
                                        + "\n|2|- Huyền Thú (200%) Chỉ Số"
                                        + "\n- 2 Món Chí Tôn"
                                        + "\n- 150M Tu Vi"
                                        + "\n- 20 Bộ Huyết Long"        
                                        + "\n- 70 Máy Dò Boss"
                                        + "\n- 200 ITEM Siêu Cấp"
                                        + "\n- 20 Bộ Ngọc Rồng"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                                break;
                                   case 8:
                                createOtherMenu(player, 2,
                                        "|7|Phần Thưởng Mốc Nạp 2.000.000 VNĐ Gồm:"
                                       + "\n|2|- Cánh Quỷ (300%) Chỉ Số"
                                        + "\n- 1 Set Chí Tôn"
                                        + "\n- 250M Tu Vi"
                                        + "\n- 30 Bộ Huyết Long"        
                                        + "\n- 100 Máy Dò Boss"
                                        + "\n- 300 ITEM Siêu Cấp"
                                        + "\n- 99 Bộ Ngọc Rồng"
                                        + "\n|7| Bạn có mún nhận khum?",
                                        "Nhận Ngay", "Đéo");
                                   Service.getInstance().sendThongBao(player, "Chưa đủ diều kiện nhận thưởng");
                      
                              break;
                        }
                           } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI1) {
                            PreparedStatement ps = null;
                            try (Connection con = GirlkunDB.getConnection();) {
                                switch (select) {
                                    case 0:
                                        Item luudz = ItemService.gI().createNewItem((short) (2026));
                                        luudz.quantity += 100;
                                        if (player.getSession().vnd < 10000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 10k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 10000;
                                        InventoryServiceNew.gI().addItemBag(player, luudz);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 10K " + luudz.template.name + " Nhớ out game vô lại");
                                        break;
                                   
                                  case 1:
                                        Item luudzz = ItemService.gI().createNewItem((short) (2026));
                                        luudzz.quantity += 500;
                                        if (player.getSession().vnd < 50000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 50k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 50000;
                                        InventoryServiceNew.gI().addItemBag(player, luudzz);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 500 " + luudzz.template.name + " Nhớ out game vô lại");
                                        break;
                                    case 2:
                                        Item luudzzz = ItemService.gI().createNewItem((short) (2026));
                                        luudzzz.quantity += 1300;
                                        if (player.getSession().vnd < 100000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 100k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 100000;
                                        InventoryServiceNew.gI().addItemBag(player, luudzzz);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 120K " + luudzzz.template.name + " Nhớ out game vô lại");
                                        break;
                                          case 3:
                                        Item luudzs = ItemService.gI().createNewItem((short) (2026));
                                        luudzs.quantity += 2500;
                                        if (player.getSession().vnd < 200000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 100k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 200000;
                                        InventoryServiceNew.gI().addItemBag(player, luudzs);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 2K5 " + luudzs.template.name + " Nhớ out game vô lại");
                                        break;
                                }

                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
                                ps.setInt(1, player.getSession().vnd);
                                ps.setInt(2, player.getSession().userId);
                                ps.executeUpdate();
                                ps.close();
//luudeptrai
                            } catch (Exception e) {
                                Logger.logException(NpcFactory.class, e, "Lỗi update VND " + player.name);
                            } finally {
                                try {
                                    if (ps != null) {
                                        ps.close();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Lỗi khi update VND");
                                }
                            }
                            
                                   } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI) {
                            PreparedStatement ps = null;
                            try (Connection con = GirlkunDB.getConnection();) {
                                switch (select) {
                                    case 0:
                                        Item thoivang = ItemService.gI().createNewItem((short) (2025));
                                        thoivang.quantity += 10000;
                                        if (player.getSession().vnd < 10000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 10k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 10000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 10K " + thoivang.template.name + " Nhớ out game vô lại");
                                        break;
                                    case 1:
                                        Item thoivangg = ItemService.gI().createNewItem((short) (2025));
                                        thoivangg.quantity += 20000;
                                        if (player.getSession().vnd < 20000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 20k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 20000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivangg);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 20K " + thoivangg.template.name + " Nhớ out game vô lại");
                                        break;
                                  case 2:
                                        Item thoivangggg = ItemService.gI().createNewItem((short) (2025));
                                        thoivangggg.quantity += 60000;
                                        if (player.getSession().vnd < 50000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 50k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 50000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivangggg);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 60K " + thoivangggg.template.name + " Nhớ out game vô lại");
                                        break;
                                    case 3:
                                        Item thoivanggggg = ItemService.gI().createNewItem((short) (2025));
                                        thoivanggggg.quantity += 120000;
                                        if (player.getSession().vnd < 100000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 100k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 100000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivanggggg);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 120K " + thoivanggggg.template.name + " Nhớ out game vô lại");
                                        break;
                                          case 4:
                                        Item thoivangggggg = ItemService.gI().createNewItem((short) (2025));
                                        thoivangggggg.quantity += 250000;
                                        if (player.getSession().vnd < 200000) {
                                            Service.gI().sendThongBao(player, "Bạn không có đủ 100k vnd");
                                            return;
                                        }
                                        player.getSession().vnd -= 200000;
                                        InventoryServiceNew.gI().addItemBag(player, thoivangggggg);
                                        Service.gI().sendThongBao(player, "Bạn Nhận Được 250K " + thoivangggggg.template.name + " Nhớ out game vô lại");
                                        break;
                                }

                                ps = con.prepareStatement("update account set vnd = ? where id = ?");
                                ps.setInt(1, player.getSession().vnd);
                                ps.setInt(2, player.getSession().userId);
                                ps.executeUpdate();
                                ps.close();
//luudeptrai
                            } catch (Exception e) {
                                Logger.logException(NpcFactory.class, e, "Lỗi update VND " + player.name);
                            } finally {
                                try {
                                    if (ps != null) {
                                        ps.close();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Lỗi khi update VND");

                                }
                            }
                        }
                    }
                }
            }
        };
    }

//                       Luwdev fix 
//                           
//                                }
//                        }
//                    }
//                }
//            }
//        };
//    }
    
    public static Npc quytoc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?", "Cửa hàng", "Đóng");
                        }
                    } else if (this.mapId == 46 || this.mapId == 5) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Kính chào Ngài Linh thú sư!", "Cửa hàng", "Đóng");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 102) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "BUNMA_FUTURE", true);
                            }
                        }
                    } else if (this.mapId == 46 || this.mapId == 5) {
                        if (player.iDMark.isBaseMenu()) {
                            if (select == 0) {
                                ShopServiceNew.gI().opendShop(player, "quytoc", true);
                            }
                        }
                    }
                }
            }
        };
    }
public static Npc vados(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            public void chatWithNpc(Player player) {
                String[] chat = {
                    "TOP Máy Chủ",
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;
                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 1000, 1000);
            }
            @Override
            public void openBaseMenu(Player player) {
                 chatWithNpc(player);
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|2|Ta Vừa Hắc Mắp Xêm Được T0p Của Toàn Server\b|7|Mi Muống Xem Tóp Gì?",
                            "Tóp Sức Mạnh", "Top Nhiệm Vụ", "Top Sức Đánh", "Top Hồng Ngọc","Top Nạp","Đóng");
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (this.mapId) {
                        case 5:
                            switch (player.iDMark.getIndexMenu()) {
                                case ConstNpc.BASE_MENU:
                                    if (select == 0) {
                                        Service.gI().showListTop(player, Manager.topSM);
                                        break;
                                    }// này à yes
                                    if (select == 1) {
                                        Service.gI().showListTop(player, Manager.topNV);
                                        break;
                                    }
                                    if (select == 2) {
                                        Service.gI().showListTop(player, Manager.topSD);
                                        break;
                                    }
                                    
                                  //  if (select == 5) {
                               //         Service.gI().showListTop(player, Manager.topCS);
                              //      }
                             //       break;
                            }
                            break;
                    }
                }
            }
        };
    }

   public static Npc gokuSSJ_1(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 80) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart", "Từ chối");
                    } else if (this.mapId == 131) {
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                    } else {
                        super.openBaseMenu(player);
                    }
                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.BASE_MENU:
                            if (this.mapId == 131) {
                                if (select == 0) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                break;
                            }
                        }
                    }
                    if (this.mapId == 80) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0:
                                    if (player.getSession().player.nPoint.power >= 18000000000L) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 312);
                                } else {
                                    this.npcChat(player, "Bạn chưa đủ 18 tỷ sức mạnh để vào");
                                }
                                break;   
                        }
                    }
                }
            }
        }
    };
  }
    public static Npc gokuSSJ_2(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Vào các khung giờ chẵn trong ngày\n"
                                + "Khi luyện tập với Mộc nhân với chế độ bật Cờ sẽ đánh rơi Bí kíp\n"
                                + "Hãy cố găng tập luyện thu thập 9999 bí kíp rồi quay lại gặp ta nhé", "Nhận\nthưởng", "OK");

                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    try {
                        Item biKiep = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 590);
                        if (select == 0) {
                            if (biKiep != null) {
                                if (biKiep.quantity >= 10000 && InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                    yardart.itemOptions.add(new Item.ItemOption(47, 400));
                                    yardart.itemOptions.add(new Item.ItemOption(108, 10));
                                    InventoryServiceNew.gI().addItemBag(player, yardart);
                                    InventoryServiceNew.gI().subQuantityItemsBag(player, biKiep, 10000);
                                    InventoryServiceNew.gI().sendItemBags(player);
                                    Service.gI().sendThongBao(player, "Bạn vừa nhận được trang phục tộc Yardart");
                                } else if (biKiep.quantity < 10000) {
                                    Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                }
                            }else {
                                Service.gI().sendThongBao(player, "Vui lòng sưu tầm đủ\n9999 bí kíp");
                                return;
                            }
                        }else {
                            return;
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        };
    }
    
    
    public static Npc Nit(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Xin chào\n|1|Tao Sẽ Tặng Quà Cho Lũ Newbie Các Ngươi\n"
                                    + "|5|cửa hàng hỗ trợ giá siêu rẻ\n"
                                    + "|1|Tân Thủ \n|3|I Need You",
                            "Hỗ Trợ","Đóng");//"Cải trang","Khiêu chiến","Map\nUp Mảnh Vỡ");//,"Phụ kiện", "Vật phẩm");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 21 || this.mapId == 22 || this.mapId == 23) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                            //    case 0: //shop
                            //        ShopServiceNew.gI().opendShop(player, "SANTA", false);
                            //        break;
                                case 0:
                                    ShopServiceNew.gI().opendShop(player, "CAI_TRANG", false);                                                                      
                                    break;
                             //   case 2:
                              //      ShopServiceNew.gI().opendShop(player, "PHU_KIEN", false);                                                                      
                              //      break;
                        //            case 2:
                        //        if(player.getSession().player.nPoint.power < 10000000000L){
                        //        Service.gI().sendThongBao(player, "Cần Có Sức Mạnh Là 10 Tỉ");}
                        //        else if(player.getSession().player.inventory.gold < 300000000){
                        //        Service.gI().sendThongBao(player, "Cần 300tr Vàng");
                        //    }   else {player.nPoint.power -=10000000000L ;
                        //    player.getSession().player.inventory.gold -= 300000000;
                       ///     player.nPoint.teleport=true;
                        //    player.idAura=95;
                         //   player.name="[ Hủy Diệt]\n"+player.name;
                        //    Service.gI().player(player);
                        //    Service.gI().Send_Caitrang(player);
                        //    Service.gI().sendFlagBag(player);
                        //    Zone zone = player.zone;
                        //    ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
//                            ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 495);
                        //                Service.gI().changeFlag(player, 8);
                        //    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                        //    new Thread(() -> {
                         //   try {
                         //   Thread.sleep(240000);
                         //       } catch (Exception e) {
                        //        }
                        //    Client.gI().kickSession (player.getSession());
                        //    }).start();
                        //    
                        //    Service.gI().sendThongBaoAllPlayer("Kẻ "+player.name+" Đang Ở "+ player.zone.map.mapName + " Khu " + player.zone.zoneId);
                        //            break;
                       //         }
                        //     case 3:
                        //         if (player.getSession().player.nPoint.power >= 80000000000L && player.inventory.gold > COST_HD) {
                        //        player.inventory.gold -= COST_HD;
                        //        Service.gI().sendMoney(player);
                         //           ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, 90);
                         //           } else {
                         //       this.npcChat(player, "Bạn chưa đủ 80 tỷ sức mạnh, 50 triệu vàng");
                         //           break;   
                         //        }
                            
                            //    case 3:
                            //        ShopServiceNew.gI().opendShop(player, "VAT_PHAM", false);                                                                      
                            //        break;
                            //    case 9:
                            //        ShopServiceNew.gI().opendShop(player, "SANTA_EVENT", false);                                                                      
                            //        break;
                            //    case 4:
                            //        this.createOtherMenu(player, 1, "|7|Số tiền của bạn còn : " + player.getSession().vnd + " VND\n"
                            //                + "Tỉ lệ quy đổi là 1000VND = 4 thỏi vàng\n" + "1000VND = 2000 hồng ngọc\n Ví Dụ Có 10.000VND Thì Nhập Vào Là 10\nCứ Quy Đổi Kể Cả 1 Thỏi Vàng Là Được Kích Hoạt Tài Khoản\nQuy Đổi Lỗi Thì Quy Đổi Lại Lần 2", "Quy đổi\n Thỏi vàng", "Quy Đổi\nHồng Ngọc", "Mở thành viên");
                            //        break;
                            }
                        }
                        else if (player.iDMark.getIndexMenu() == 1){
                            switch (select) {
                                case 0:
                                    Input.gI().createFormQDTV(player);
                                    break;
                                case 1:
                                    Input.gI().createFormQDHN(player);
                                    break;
                                case 2:
                                    if (player.getSession().vnd >= 20000){
                                        if (player.kichhoat == 0){
                                            player.kichhoat = 1;
                                            PlayerDAO.subvnd(player, 20000);
                                            player.getSession().vnd -= 20000;
                                            Service.gI().sendThongBao(player, "Kích hoạt thành công");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Đã kích hoạt thành viên rồi");
                                        }
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn không đủ 20k");
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }
    public static Npc hopnhat(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
// muôn tắt thì tắt chuyển sinh đệ ở đây 
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 5) {
                        this.createOtherMenu(player, 550,
                                "|3|CHÀO CON!\n|5|--CON CÓ MUỐN HỢP NHẤT VỚI ĐỆ TỬ--"
                                                   + "\n|1|ĐỂ GIA TĂNG SỨC MẠNH BẢN THÂN HAY KHÔNG?"
                                                   + "\n|1|CON SẼ MẤT 30 TỈ SỨC MẠNH VÀ ĐỆ TỬ CỦA CON!!!"
                                                   + "\n|1|CHỈ SỐ GỐC SẼ SẼ ĐƯỢC CỘNG NGẪU NHIÊN CHO CON"
                                                   + "\n HP Từ 20K-100K|SD Từ 5K-20K|KI Từ 20K-100K"
                                                   + "\n|7|Yêu Cầu:30 Tỷ TNSM"
//                                                   + "\n|7|Yêu Cầu:" + Util.getFormatNumber(player.pet.nPoint.power) +
                                               + "200.000.000.000 sức mạnh",
                                                 "Hợp Nhất",  "Từ Chối");
                    }
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
//                        if (player.pet.nPoint.power >= 200000000000L) {
 {
                                                 
                                Item dt = null;

                                    try {
                                        dt = InventoryServiceNew.gI().findItemBag(player, 1425);
 
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (dt == null || dt.quantity < 3) {
                                       Service.gI().sendThongBao(player, "|2|CHỨC NĂNG YÊU CẦU CÓ 3 Đá Hợp Nhất <>!");
                                    } else if (player.pet.nPoint.power < 200000000000L) { 
                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU ĐỆ TỬ PHẢI 200 TỈ SM NHÉ !");
                                   // } else if (player.nPoint.power <200000000000L) { 
                                    } else if (player.nPoint.power <200000000000L) {
                                        Service.gI().sendThongBao(player, "|2|YÊU CẦU SƯ PHỤ TRIÊN 200 TỈ  TỈ SM NHÉ !");                                                                                                                      
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, dt, 3);
                                       // PetService.gI().createNormalPet(player);   tạo pett mới thì bật lên ko thì tắt đi 
                                        Service.gI().sendMoney(player);
                                        player.nPoint.power -= 30000000000L;
                                        player.pet.nPoint.power = 1200L;
                                        player.nPoint.hpg += Util.nextInt(20000, 100000); // chỉ số cộng cho sư phụ 
                                        player.nPoint.dameg += Util.nextInt(5000, 20000);
                                        player.nPoint.mpg += Util.nextInt(20000, 100000); 
                                        // chỉ số pet ki bị trừ 
                                        player.pet.nPoint.hpg = Util.nextInt(200, 2000);
                                        player.pet.nPoint.mpg = Util.nextInt(200, 2000);
                                        player.pet.nPoint.dameg = Util.nextInt(10, 200);
                                        player.pet.nPoint.critg = Util.nextInt(1, 5);
                                        player.pet.nPoint.defg  =  Util.nextInt(10, 309);
                                        player.pet.nPoint.critg = 1;
                                        player.pet.nPoint.defg = 100;
                                        player.pet.playerSkill.skills.get(1).skillId = -1;
                                        player.pet.playerSkill.skills.get(2).skillId = -1;
                                        player.pet.playerSkill.skills.get(4).skillId = -1;
                                        player.pet.playerSkill.skills.get(3).skillId = -1;    
                                        InventoryServiceNew.gI().sendItemBags(player);                                       
                                        Service.gI().sendThongBao(player, "|2| ĐỆ NHÀ NGƯƠI ĐÃ BỊ Hoang Van Luu RÚT MÁU HAHA 🤣 CHỈ \n chỉ số nhà người đã dc cộng ngẫu nghiên ");
                                      //  Client.gI().kickSession(player.getSession());
                                      //  fix roi nhe by luwddev
                              
                                

                        }
                    }
                }
            
        };
    } 

    ///////////////////////////////////////////NPC Ký Gửi///////////////////////////////////////////
    private static Npc kyGui(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {

            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    if (player.getSession().actived) {
                    ShopKyGuiService.gI().openShopKyGui(player);
                    return;

                }
                }else{
                    Service.gI().sendThongBaoOK(player, "Yêu cầu phải kích hoạt tài khoản mới có thể sử dụng chức năng này!");
                    return;
                }
            }

            @Override
            public void confirmMenu(Player pl, int select) {
                if (canOpenNpc(pl)) {
                    switch (select) {
                        case 0:
                            Service.getInstance().sendPopUpMultiLine(pl, tempId, avartar, "Cửa hàng chuyên nhận ký gửi mua bán vật phẩm\bChỉ với 1 Thỏi vàng\bGiá trị ký gửi 1-500k Thỏi vàng");
                            break;
                        case 1:

//                            Service.gI().sendThongBaoOK(pl, "Chức năng đang được bảo trì để tối ưu!");
                    }
                }
            }
        };
    }
        public static Npc Pic2Mai(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (this.mapId == 219 ) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "|7|Hiện Tại Bạn Đang Đang Có Số Điểm Sự Kiện Là " + player.NguHanhSonPoint + "\nĐể Đổi Hộp Qùa Sự Kiện Hè Bạn Cần 99 San Hô + 99 Bình Nước + 99 Khúc Gỗ + 1 Que Diêm + 5 Thỏi Vàng. Bạn Sẽ Nhận Được Hộp Qùa VIP Và Điểm Sự Kiện. Ngoài Ra Bạn Có Thể Sử Dụng X99 Nguyên Liệu Mỗi Loại Kết Hợp Với 1 Que Diêm Để Nhận Được Hộp Qùa Thường Và Điểm Sự Kiện.", "Đổi \n Hộp Qùa VIP", "Đổi \n Hộp Qùa VIP");

                }
            }
            @Override
            public void confirmMenu(Player player, int select) {
                if (canOpenNpc(player)) {
                    if (this.mapId == 219 ) {
                        if (player.iDMark.isBaseMenu()) {
                            switch (select) {
                                case 0: {
                                    Item sanho = null;
                                    Item binhnuoc = null;
                                    Item khucgo = null;
                                    Item quediem = null;
                                    Item thoivang = null;
                                    

                                    try {
                                        sanho = InventoryServiceNew.gI().findItemBag(player, 1251);
                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1252);
                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 1253);
                                        quediem = InventoryServiceNew.gI().findItemBag(player, 1254);
                                        thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                                        
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (sanho == null || sanho.quantity < 99 || binhnuoc == null || binhnuoc.quantity < 99 || khucgo == null || khucgo.quantity < 99 || quediem == null || quediem.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (thoivang == null || thoivang.quantity < 5) {
                                        this.npcChat(player, "Bạn không đủ thỏi vàng");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, thoivang, 5);
                                        
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1255);
                                        player.NguHanhSonPoint += 1;
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Hộp Qùa VIP");
                                    }
                                    break;
                                }

                                case 1: {
                                    Item sanho = null;
                                    Item binhnuoc = null;
                                    Item khucgo = null;
                                    Item quediem = null;
                                    

                                    try {
                                        sanho = InventoryServiceNew.gI().findItemBag(player, 1251);
                                        binhnuoc = InventoryServiceNew.gI().findItemBag(player, 1252);
                                        khucgo = InventoryServiceNew.gI().findItemBag(player, 1253);
                                        quediem = InventoryServiceNew.gI().findItemBag(player, 1254);
                                        
                                        
                                    } catch (Exception e) {
//                                        throw new RuntimeException(e);
                                    }
                                    if (sanho == null || sanho.quantity < 99 || binhnuoc == null || binhnuoc.quantity < 99 || khucgo == null || khucgo.quantity < 99 || quediem == null || quediem.quantity < 1) {
                                        this.npcChat(player, "Bạn không đủ nguyên liệu để nấu bánh");
                                    } else if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {
                                        this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                    } else {
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, sanho, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 1);
                                        Service.getInstance().sendMoney(player);
                                        Item trungLinhThu = ItemService.gI().createNewItem((short) 1255);
                                        player.NguHanhSonPoint += 1;
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, binhnuoc, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, khucgo, 99);
                                        InventoryServiceNew.gI().subQuantityItemsBag(player, quediem, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().addItemBag(player, trungLinhThu);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        this.npcChat(player, "Bạn nhận được 1 Hộp Qùa Thường");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }
        
        
        public static Npc minuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
        public void Npcchat(Player player) {
                String[] chat = {
                    "Giúp Ta đẫn Mị Nương Về Nha",
                    "Em buông tay anh vì lí do gì ",
                    "Người hãy nói đi , đừng Bắt Anh phải nghĩ suy"
                };
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    int index = 0;

                    @Override
                    public void run() {
                        npcChat(player, chat[index]);
                        index = (index + 1) % chat.length;
                    }
                }, 6000, 6000);
            }
            @Override
              public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                               "Mị nương đang đi lạc ngươi hãy giúp ta đưa nàng đến đảo kame \n Ta trao thưởng quà Hậu hĩnh,"
                                        
                                        , "Hướng dẫn\n Hộ Tống mị", "Hộ Tống" ,"Đóng");
                        
                
                }

            }                                   
                                    
                                    
            @Override
         
              public void confirmMenu(Player player, int select) {
                   Npcchat(player);
                if (canOpenNpc(player)) {
                    if (this.mapId == 0) {
                        if (player.iDMark.isBaseMenu()) { 
                           switch (select) {
                                case 1:
                                Boss oldDuongTank = BossManager.gI().getBossById(Util.createIdDuongTank((int) player.id));
                                if (oldDuongTank != null) {
                                    this.npcChat(player, " Mị Nương đang được hộ tống" + oldDuongTank.zone.zoneId);
                                } else if (player.inventory.ruby < 20000) {
                                    this.npcChat(player, "Nhà ngươi không đủ 20K Hồng Ngọc ");
                                } else {
                                //    List<Skill> skillList = new ArrayList<>();
                                //        for (byte i = 0; i < player.playerSkill.skills.size(); i++) {
                                //            Skill skill = player.playerSkill.skills.get(i);
                                //            if (skill.point > 0) {
                                //                skillList.add(skill);
                                //            }
                                //        }
                                //        int[][] skillTemp = new int[skillList.size()][3];
                                //        for (byte i = 0; i < skillList.size(); i++) {
                                //            Skill skill = skillList.get(i);
                                //            if (skill.point > 0) {
                                //                skillTemp[i][0] = skill.template.id;
                                //                skillTemp[i][1] = skill.point;
                                //                skillTemp[i][2] = skill.coolDown;
                                //            }
                                //        }
                                        BossData bossDataClone = new BossData(
                                            "Mị nương do" +" "+ player.name + " hộ tống",
                                            (byte) 2,
                                            new short[]{841, 842, 843, -1, -1, -1},
                                            100000,
                                  //          (int) player.nPoint.hpMax * 2,
                                            new long[]{(int)player.nPoint.hpMax * 2},
                                            new int[]{103},
                                            new int[][]{
                                            {Skill.TAI_TAO_NANG_LUONG, 7, 15000}},
                                            new String[]{}, //text chat 1
                                            new String[]{}, //text chat 2
                                            new String[]{}, //text chat 3
                                            60
                                    );

                                    try {
                                        DuongTank dt = new DuongTank(Util.createIdDuongTank((int) player.id), bossDataClone, player.zone, player.location.x - 20, player.location.y);
                                        dt.playerTarger = player;
                                        int[] map = {6,29,30,4,5,27,28};
                                        dt.mapCongDuc = map[Util.nextInt(map.length)];
                                        player.haveDuongTang = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //trừ vàng khi gọi boss
                                    player.inventory.ruby -= 20000;
                                    Service.getInstance().sendMoney(player);
                                break;
                                }
                               case 0: 
                             Service.getInstance().sendThongBaoFromAdmin(player, " Gặp Npc VUA HÙNG , Chọn Hộ Tống Rồi Dắt Mị đếnVị Trí được chỉ định \n "
                                     + "Phần quà 1k Ngọc Hồng , Ran Dom 5 -10 Đồng Bạc , Phí dắt 20k Ngọc Hồng ..");
                                break;
                        //    case 2:
                        //            ShopServiceNew.gI().opendShop(player, "MI", true);
                        //            break;
                    
                 
                            case 7:
                                    this.createOtherMenu(player, 997,
                                            "|5|Bạn đang có :" + player.getSession().vnd + " vnd\n Bạn có Muốn Mua  Đồng Bạc?\n Luu ý phải có đồng bạc trong hành trang mới có thể mua ", "500k Vnd\n9990 đồng Bạc",  "1000k\n18888 Đồng Bạc", "Từ chối");
                                    break;
                                        }   
                              } else if (player.iDMark.getIndexMenu() == 997) {
                            switch (select) {
                                  case 0:
                                    if (player.getSession().vnd < 500000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        Item honthu = null;
                               try {
                                    honthu = InventoryServiceNew.gI().findItemBag(player, 1187);
                                     honthu.itemOptions.add(new Item.ItemOption(30, 1));
                                } catch (Exception e) {
                                }
                                       PlayerDAO.subvnd(player, 500000);
                                      Service.gI().sendMoney(player);
                                        honthu.quantity += 9990;
                                  this.npcChat(player, "Bạn Nhận được 9990 đồng bạc");     
                                  break;
                                    }
                                     case 1:
                                    if (player.getSession().vnd < 1000000) {
                                        Service.gI().sendThongBao(player, "Bạn không có đủ tiền !");
                                        break;
                                    } else {
                                        Item thoivang = null;
                                try {
                                    thoivang = InventoryServiceNew.gI().findItemBag(player, 1187);
                                     thoivang.itemOptions.add(new Item.ItemOption(30, 1));
                                } catch (Exception e) {
                                }
                                        PlayerDAO.subvnd(player, 1000000);
                                        Service.gI().sendMoney(player);
                                         thoivang.quantity += 18888;
                                     this.npcChat(player, "Bạn Nhận được 18888 đồng bạc"); 
                                      break;
                                    }
                    
                            } 
                    }
                }
            }
        }
     };
     }
        
        
        public static Npc miNuong(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        return new Npc(mapId, status, cx, cy, tempId, avartar) {
            @Override
            public void openBaseMenu(Player player) {
                if (canOpenNpc(player)) {
                    createOtherMenu(player, ConstNpc.MENU_JOIN_GIAI_CUU_MI_NUONG,
                            "Ta đang bị kẻ xấu lợi dụng kiểm soát bản thân\n"
                            + "Các chàng trai hãy cùng nhau nhanh chóng tập hợp lên đường giải cứu ta",
                            "Tham gia", "Hướng dẫn", "Từ chối");
                }
            }

            @Override
            public void confirmMenu(Player player, int select) {
                int nPlSameClan = 0;
                for (Player pl : player.zone.getPlayers()) {
                    if (!pl.equals(player) && pl.clan != null
                            && pl.clan.equals(player.clan) && pl.location.x >= 1285
                            && pl.location.x <= 1645) {
                        nPlSameClan++;
                    }
                }
                if (canOpenNpc(player)) {
                    switch (player.iDMark.getIndexMenu()) {
                        case ConstNpc.MENU_JOIN_GIAI_CUU_MI_NUONG:
                            if (select == 0) {
                                if (player.clan == null) {
                                    Service.gI().sendThongBao(player, "Yêu cầu gia nhập bang hội");
                                    break;
                                } if (player.clan.giaiCuuMiNuong != null) {
                                    ChangeMapService.gI().changeMapInYard(player, 185, player.clan.giaiCuuMiNuong.id, 60);
                                    break;
                                } else if (nPlSameClan < 0) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia cùng 2 đồng đội");
                                    break;
                                } else if (player.clanMember.getNumDateFromJoinTimeToToday() < 0) {
                                    Service.gI().sendThongBao(player, "Yêu cầu tham gia bang hội trên 1 ngày");
                                    break;
                                } else if (player.clan.haveGoneGiaiCuuMiNuong) {
                                    Service.gI().sendThongBaoOK(player, "Bang hội của ngươi đã tham gia vào lúc " + TimeUtil.formatTime(player.clan.lastTimeOpenGiaiCuuMiNuong, "HH:mm:ss") + "\nVui lòng tham gia vào ngày mai");
                                    break;
                                } else {
                                    GiaiCuuMiNuongService.gI().openGiaiCuuMiNuong(player);
                                }
                            } else if (select == 1) {
                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_GIAI_CUU_MI_NUONG);
                            }
                            break;
                    }
                }
            }
        };
    }

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId) {
        int avatar = Manager.NPC_TEMPLATES.get(tempId).avatar;
        try {
            switch (tempId) {
                case ConstNpc.TRUNG_LINH_THU:
                    return trungLinhThu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRONG_TAI:
                    return TrongTai(mapId, status, cx, cy, tempId, avatar);  
                case ConstNpc.HUNG_VUONG:
                    return hungvuong(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.POTAGE:
                    return poTaGe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MAM_NGU_QUA:
                    return mamnguqua(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.QUY_LAO_KAME:
                    return quyLaoKame(mapId, status, cx, cy, tempId, avatar);
                 case ConstNpc.CUA_HANG_KY_GUI:
                    return kyGui(mapId, status, cx, cy, tempId, avatar);
            //        case ConstNpc.THO_NGOC:
            //        return thoNgoc(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TRUONG_LAO_GURU:
                    return truongLaoGuru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.HU_VANG:
                    return huvang(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.VUA_VEGETA:
                    return vuaVegeta(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.PHI_THANG:
                    return phithang(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.TAI_XIU:
                    return taixiu(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.THO_DAI_CA:
                    return thodaika(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    return ongGohan_ongMoori_ongParagus(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA:
                    return bulmaQK(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.nit:
                    return Nit(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.hop_nhat:
                    return hopnhat(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DENDE:
                    return dende(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.APPULE:
                    return appule(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DR_DRIEF:
                    return drDrief(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CARGO:
                    return cargo(mapId, status, cx, cy, tempId, avatar);
                 case ConstNpc.kichi:
                    return Kichi(mapId, status, cx, cy, tempId, avatar);   
                case ConstNpc.CUI:
                    return cui(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.SANTA:
                    return santa(mapId, status, cx, cy, tempId, avatar);
                        case ConstNpc.detu:
                    return npc70(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.URON:
                    return uron(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BA_HAT_MIT:
                    return baHatMit(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.thien_su_whis_2:
                    return thiensuwhis2(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.TRIEU_HOI_THU:
                    return trieuhoithu(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RUONG_DO:
                    return ruongDo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DAU_THAN:
                    return dauThan(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GAP_THU:
                    return gapthu(mapId, status, cx, cy, tempId, avatar); 
                    case ConstNpc.VONG_QUAY_COIN:
                    return vongquaycoin(mapId, status, cx, cy, tempId, avatar); 
                case ConstNpc.SkienTrungThu:
                    return Skien_trungthu(mapId, status, cx, cy, tempId, avatar);        
                case ConstNpc.CALICK:
                    return calick(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.JACO:
                    return jaco(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THUONG_DE:
                    return thuongDe(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.VADOS:
                    return vados(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_VU_TRU:
                    return thanVuTru(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.KIBIT:
                    return kibit(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.OSIN:
                    return osin(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LY_TIEU_NUONG:
                    return npclytieunuong54(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.LINH_CANH:
                    return linhCanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GIUMA_DAU_BO:
                    return giuma(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUA_TRUNG:
                    return quaTrung(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.QUOC_VUONG:
                    return quocVuong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BUNMA_TL:
                    return bulmaTL(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GHI_DANH:
                    return GhiDanh(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.DUA_HAU:
                    return duahau(mapId, status, cx, cy, tempId, avatar);    
                case ConstNpc.Monaito:
                    return monaito(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_OMEGA:
                    return rongOmega(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    return rong1_to_7s(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.TO_SU_KAIO:
                    return tosukaio(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.BILL:
                    return bill(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.NPC_64:
                    return npcThienSu64(mapId, status, cx, cy, tempId, avatar); 
                case ConstNpc.BO_MONG:
                    return boMong(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.THAN_MEO_KARIN:
                    return karin(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.BROOK:
                    return Brook(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.Usop:
                    return usop(mapId, status, cx, cy, tempId, avatar); 
                case ConstNpc.GOKU_SSJ:
                    return gokuSSJ_1(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.CHOPPER:
                    return chopper(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.MR_POPO:
                    return mrpopo(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.GOKU_SSJ_:
                    return gokuSSJ_2(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.mai:
                    return Mai(mapId, status, cx, cy, tempId, avatar);  
                case ConstNpc.DUONG_TANG:
                    return duongtank(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Mi:
                    return minuong(mapId, status, cx, cy, tempId, avatar);  
                case ConstNpc.mi:
                    return miNuong(mapId, status, cx, cy, tempId, avatar);     
                case ConstNpc.WHIS:
                    return whis(mapId, status, cx, cy, tempId, avatar);
                     case ConstNpc.nangcap:
                    return nangcap(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.skienhe:
                    return Skienhe(mapId, status, cx, cy, tempId, avatar); 
                case ConstNpc.Mavuong:
                    return mavuong(mapId, status, cx, cy, tempId, avatar);      
                case ConstNpc.TAPION:
                    return Tapion(mapId, status, cx, cy, tempId, avatar); 
                case ConstNpc.npc92:
                    return npc92(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.UNKOWN:
                    return unkonw(mapId, status, cx, cy, tempId, avatar);
                    case ConstNpc.anime:
                    return Anime(mapId, status, cx, cy, tempId, avatar);
                case ConstNpc.Pic:
                    return Pic2Mai(mapId, status, cx, cy, tempId, avatar);
                     case ConstNpc.Granola:
                    return chiton(mapId, status, cx, cy, tempId, avatar);
                default:
                    return new Npc(mapId, status, cx, cy, tempId, avatar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
//                                ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0, player.gender);
                            }
                        }
                    };
            }
        } catch (Exception e) {
            Logger.logException(NpcFactory.class, e, "Lỗi load npc");
            return null;
        }
    }

    //girlbeo-mark
    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1 && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2 && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }
    
    
    public static void createNpcSanTa() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.SANTA, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.BAN_NHIEU_THOI_VANG:
                        Item thoivang = InventoryServiceNew.gI().findItemBag(player, 457);
                        if (select == 0 && thoivang.quantity >= 1 && thoivang != null && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 1;
                            player.inventory.gold += 500_000_000;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 1 && thoivang == null && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 1 Thỏi\nvàng để thực hiện");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 5 && thoivang != null && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 5;
                            player.inventory.gold += 2_500_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 5 && thoivang == null && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 5 Thỏi\nvàng để thực hiện");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 10 && thoivang != null && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 10;
                            player.inventory.gold += 5_000_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 10 && thoivang == null && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 10 Thỏi\nvàng để thực hiện");
                            return;
                        }
                        if (select == 1 && thoivang.quantity >= 19 && thoivang != null && player.inventory.gold < 9_500_000_000L) {
                            thoivang.quantity -= 19;
                            player.inventory.gold += 9_500_000_000L;
                            Service.gI().sendMoney(player);
                            InventoryServiceNew.gI().sendItemBags(player);
                            break;
                        } else if (select == 0 && thoivang.quantity < 19 && thoivang == null && player.inventory.gold < 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Cần có đủ 19 Thỏi\nvàng để thực hiện");
                            return;
                        } else if (player.inventory.gold > 9_500_000_000L) {
                            Service.gI().sendThongBao(player, "Số vàng sau khi bán vượt quá 10 tỷ");
                            return;
                        }
                        break;
                }
            }
        };
    }
    

    public static void createNpcConMeo() {
        Npc npc;
        npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP: //                        if (player.kichhoat == 1) 
                    {
                        if (Maintenance.isRuning) {
                            break;
                        }
                        PVPService.gI().sendInvitePVP(player, (byte) select);
                        break;
                    }
//                        else {
//                            Service.getInstance().sendThongBao(player, "|5|VUI LÒNG KÍCH HOẠT TÀI KHOẢN TẠI\n|7|NROGOD.COM\n|5|ĐỂ MỞ KHÓA TÍNH NĂNG");
//                            break;
//                        }
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                        case ConstNpc.RUONG_GO:
                        int size = player.textRuongGo.size();
                        if (size > 0) {
                            String menuselect = "OK [" + (size - 1) + "]";
                            if (size == 1) {
                                menuselect = "OK";
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1, player.textRuongGo.get(size - 1), menuselect);
                            player.textRuongGo.remove(size - 1);
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPService.gI().acceptRevenge(player);
                        }
                        break;
                        
                     
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                         case ConstNpc.MENU_OPTION_USE_ITEM1879:
                        if (select == 0) {
                            IntrinsicService.gI().satchiton(player);
                       }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2000:
                    case ConstNpc.MENU_OPTION_USE_ITEM2001:
                    case ConstNpc.MENU_OPTION_USE_ITEM2002:
                        try {
                            ItemService.gI().OpenSKH(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                         
                
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM2003:
                    case ConstNpc.MENU_OPTION_USE_ITEM2004:
                   case ConstNpc.MENU_OPTION_USE_ITEM2005:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM736:
                        try {
                            ItemService.gI().OpenDHD(player, player.iDMark.getIndexMenu(), select);
                        } catch (Exception e) {
                            Logger.error("Lỗi mở hộp quà");
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player, "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;

                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    InventoryServiceNew.gI().addItemBag(player, item);
                                }
                                InventoryServiceNew.gI().sendItemBags(player);
                                break;
                            case 1:
                               if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                new Thread(() -> {
                                    Client.gI().close();
                                }).start();
                                break;
                            case 2:
                                if (player.isAdmin()) {
                                    System.out.println(player.name);
//                                PlayerService.gI().baoTri();
                             Maintenance.gI().start(15);
                             System.out.println(player.name);
                                }
                                break;
                            case 3:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 4:
                                BossManager.gI().showListBoss(player);
                                break;
                             case 5:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Input.gI().BUFFETK(player);
                                break;
                            case 6:
                                Input.gI().createFormChangeTNSMServer(player);
                                break;
                            case 7:
                                Input.gI().createFormGiveItem(player);
                                break;
                            case 8: {
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                    return;
                                }
                                Message msg;
                                try {
                                    msg = new Message(-96);
                                    msg.writer().writeByte(0);
                                    msg.writer().writeUTF("Người Online");
                                    msg.writer().writeByte(Client.gI().getPlayers().size());
                                    for (int i = 0; i < Client.gI().getPlayers().size(); i++) {
                                        Player pl = Client.gI().getPlayers().get(i);
                                        msg.writer().writeInt(i + 1);
                                        msg.writer().writeInt((int) pl.id);
                                        msg.writer().writeShort(pl.getHead());
                                        if (player.getSession().version > 214) {
                                            msg.writer().writeShort(-1);
                                        }
                                        msg.writer().writeShort(pl.getBody());
                                        msg.writer().writeShort(pl.getLeg());
                                        msg.writer().writeUTF(pl.name);
                                        msg.writer().writeUTF(!pl.isAdmin() ? "Member" : "Admin");
                                        if (pl.zone != null) {
                                            
                                            msg.writer().writeUTF(pl.zone.map.mapName + "(" + pl.zone.map.mapId
                                                    + ") khu " + pl.zone.zoneId + ""  + "\n|8|Tu Vi: " + Util.getFormatNumber(player.ExpTamkjll)
                                                + "\n|7|Hp: " + Util.getFormatNumber(player.nPoint.hp) + "/"
                                                + Util.getFormatNumber(player.nPoint.hpMax)
                                                + "\n|2|Ki: " + Util.getFormatNumber(player.nPoint.mp) + "/"
                                                + Util.getFormatNumber(player.nPoint.mpMax));
                                        } else {
                                            
                                            msg.writer().writeUTF("Die");
                                        }
                                    }
                                    player.sendMessage(msg);
                                    msg.cleanup();
                                } catch (Exception e) {
                                    Logger.logException(NpcFactory.class, e);
                                }
                                break;
                            }
                         
                            case 9:
                                this.createOtherMenu(player, ConstNpc.CALL_BOSS,
                                        "Chọn Boss?", "Full Cụm\nANDROID", "BLACK", "BROLY", "Cụm\nCell",
                                        "Cụm\nDoanh trại", "DOREMON", "FIDE", "FIDE\nBlack", "Cụm\nGINYU", "Cụm\nNAPPA", "NGỤC\nTÙ");
                                break;
                                case 10:
                                if (!player.isAdmin()) {
                                    PlayerService.gI().banPlayer(player);
                                   return;
                                }
                                Input.gI().BUFFETT(player);
                                break;
                        }
                        break;
                             case 11:
                               Input.gI().createFormSendItem(player);
                                break;
                                  case 12:
                               Input.gI().createFormSendItem(player);
                                break;
                    case ConstNpc.CALL_BOSS:
                        switch (select) {
                            case 0:
                                BossManager.gI().createBoss(BossID.ANDROID_13);
                                BossManager.gI().createBoss(BossID.ANDROID_14);
                                BossManager.gI().createBoss(BossID.ANDROID_15);
                                BossManager.gI().createBoss(BossID.ANDROID_19);
                                BossManager.gI().createBoss(BossID.DR_KORE);
                                BossManager.gI().createBoss(BossID.KING_KONG);
                                BossManager.gI().createBoss(BossID.PIC);
                                BossManager.gI().createBoss(BossID.POC);
                                break;
                            case 1:
                                BossManager.gI().createBoss(BossID.BLACK);
                                break;
                            case 2:
                                BossManager.gI().createBoss(BossID.BROLY);
                                break;
                            case 3:
                                BossManager.gI().createBoss(BossID.SIEU_BO_HUNG);
                                BossManager.gI().createBoss(BossID.XEN_BO_HUNG);
                                break;
                            case 4:
                                Service.getInstance().sendThongBao(player, "Không có boss");
                                break;
                            case 5:
                                BossManager.gI().createBoss(BossID.CHAIEN);
                                BossManager.gI().createBoss(BossID.XEKO);
                                BossManager.gI().createBoss(BossID.XUKA);
                                BossManager.gI().createBoss(BossID.NOBITA);
                                BossManager.gI().createBoss(BossID.DORAEMON);
                                break;
                            case 6:
                                BossManager.gI().createBoss(BossID.FIDE);
                            case 7:
                                BossManager.gI().createBoss(BossID.FIDE_ROBOT);
                                BossManager.gI().createBoss(BossID.VUA_COLD);
                                break;
                            case 8:
                                BossManager.gI().createBoss(BossID.SO_1);
                                BossManager.gI().createBoss(BossID.SO_2);
                                BossManager.gI().createBoss(BossID.SO_3);
                                BossManager.gI().createBoss(BossID.SO_4);
                                BossManager.gI().createBoss(BossID.TIEU_DOI_TRUONG);
                                break;
                            case 9:
                                BossManager.gI().createBoss(BossID.KUKU);
                                BossManager.gI().createBoss(BossID.MAP_DAU_DINH);
                                BossManager.gI().createBoss(BossID.RAMBO);
                                break;
                            case 10:
                                BossManager.gI().createBoss(BossID.COOLER_GOLD);
                                BossManager.gI().createBoss(BossID.CUMBER);
                                BossManager.gI().createBoss(BossID.SONGOKU_TA_AC);
                                break;
                            case 11:
                                BossManager.gI().createBoss(BossID.BLACK);
                                BossManager.gI().createBoss(BossID.BLACK3);
                                BossManager.gI().createBoss(BossID.BLACK1);
                                BossManager.gI().createBoss(BossID.ZAMASZIN);
                                BossManager.gI().createBoss(BossID.ZAMASMAX);
                                break;
                            case 12:
                                BossManager.gI().createBoss(BossID.TAU_PAY_PAY_M);
                        }
                        break;
                        case ConstNpc.Tamkjllmenu:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                        "|1|Thông Tin Cường Giả\n"
                                                + "\n|8|Tu Vi: " + Util.getFormatNumber(player.ExpTamkjll)
                                                + "\n|7|Hp: " + Util.getFormatNumber(player.nPoint.hp) + "/"
                                                + Util.getFormatNumber(player.nPoint.hpMax)
                                                + "\n|2|Ki: " + Util.getFormatNumber(player.nPoint.mp) + "/"
                                                + Util.getFormatNumber(player.nPoint.mpMax)
                                                + "\n|4|Dame: " + Util.getFormatNumber(player.nPoint.dame)
                                                + "\n|8|Giáp: " + Util.getFormatNumber(player.nPoint.def)
                                                + "\n|7|Hp Gốc: " + Util.getFormatNumber(player.nPoint.hpg)
                                                + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.nPoint.mpg)
                                                + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.nPoint.dameg)
                                                + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.nPoint.defg),
                                        "Thông tin nhân vật", "Đổi Hành Tinh");
                              break;
                      
                            case 1:
//                                if (player.ExpTamkjll < 200) {
//                                    Service.gI().sendThongBao(player, "Bạn cần ít nhất 200K ");
//                                    return;
//                                }
                                if (player.inventory.ruby < 500000) {
                                    Service.gI().sendThongBao(player, "Bạn cần ít nhất 500k hồng ngọc.");
                                    return;
                                }
                                if (player.ExpTamkjll < 100000000) {
                                    Service.gI().sendThongBao(player, "Bạn cần ít nhất 100tr Tu Vi");
                                    return;
                                }
                                if (InventoryServiceNew.gI().getCountEmptyBag(player) > 0) {
                                    if (player.inventory.itemsBody.get(0).quantity < 1
                                            && player.inventory.itemsBody.get(1).quantity < 1
                                            && player.inventory.itemsBody.get(2).quantity < 1
                                            && player.inventory.itemsBody.get(3).quantity < 1
                                            && player.inventory.itemsBody.get(4).quantity < 1) {
                                        player.ExpTamkjll -= 100000000;
                                        player.inventory.ruby -= 500000;
                                        Service.gI().sendMoney(player);
                                        player.gender += 1;
                                        if (player.gender > 2) {
                                            player.gender = 0;
                                        }
                                        short[] headtd = { 30, 31, 64 };
                                        short[] headnm = { 9, 29, 32 };
                                        short[] headxd = { 27, 28, 6 };
                                        player.playerSkill.skills.clear();
                                        for (Skill skill : player.playerSkill.skills) {
                                            skill.point = 1;
                                        }
                                        int[] skillsArr = player.gender == 0 ? new int[] { 0, 1, 6, 9, 10, 20, 22, 19 }
                                                : player.gender == 1 ? new int[] { 2, 3, 7, 11, 12, 17, 18, 19 }
                                                        : new int[] { 4, 5, 8, 13, 14, 21, 23, 19 };
                                        for (int i = 0; i < skillsArr.length; i++) {
                                            player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                        }
                                        player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                        player.playerIntrinsic.intrinsic.param1 = 0;
                                        player.playerIntrinsic.intrinsic.param2 = 0;
                                        player.playerIntrinsic.countOpen = 0;
                                        if (player.gender == 0) {
                                            player.head = headtd[Util.nextInt(headtd.length)];
                                        } else if (player.gender == 1) {
                                            player.head = headnm[Util.nextInt(headnm.length)];
                                        } else if (player.gender == 2) {
                                            player.head = headxd[Util.nextInt(headxd.length)];
                                        }
                                        Service.gI().sendThongBao(player,
                                                "Đổi hành tinh thành công, vui lòng thoát ra vô lại để load");
                                        PlayerService.gI().sendInfoHpMpMoney(player);
                                        Service.gI().Send_Info_NV(player);
                                    } else {
                                        Service.gI().sendThongBao(player, "Tháo hết 5 món đầu đang mặc ra nha");
                                    }
                                } else {
                                    Service.gI().sendThongBao(player, "Balo đầy");
                                }
                                break;
                       
                            case 2:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllParam, -1,
                                        "|1|Liên hệ Diệt Thần qua telegram: @Diệt Thần\n"
                                                + "|1|Bạn muốn xem chỉ số đồ bị giới hạn hiện thị:",
                                        "chỉ số đồ mặc\nô 1", "chỉ số đồ mặc\nô 2", "chỉ số đồ mặc\nô 3",
                                        "chỉ số đồ mặc\nô 4", "chỉ số đồ mặc\nô 5", "chỉ số đồ mặc\nô 6",
                                        "chỉ số đồ mặc\nô 7", "chỉ số đồ mặc\nô 8", "chỉ số đồ mặc\nô 9",
                                        "chỉ số đồ mặc\nô 10", "chỉ số đồ mặc\nô 11");
                                break;
                            case 8:
                                if (player.pet != null) {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.Tamkjllmenu, -1,
                                            "|1|Thông Tin Đệ Tử\n"
                                                    + "|7|Hp: " + Util.getFormatNumber(player.pet.nPoint.hp) + "/"
                                                    + Util.getFormatNumber(player.pet.nPoint.hpMax)
                                                    + "\n|2|Ki: " + Util.getFormatNumber(player.pet.nPoint.mp) + "/"
                                                    + Util.getFormatNumber(player.pet.nPoint.mpMax)
                                                    + "\n|4|Dame: " + Util.getFormatNumber(player.pet.nPoint.dame)
                                                    + "\n|8|Giáp: " + Util.getFormatNumber(player.pet.nPoint.def)
                                                    + "\n-Tiềm năng:\n"
                                                    + "|7|Hp Gốc: " + Util.getFormatNumber(player.pet.nPoint.hpg)
                                                    + "\n|2|Ki Gốc: " + Util.getFormatNumber(player.pet.nPoint.mpg)
                                                    + "\n|4|Dame Gốc: " + Util.getFormatNumber(player.pet.nPoint.dameg)
                                                    + "\n|8|Giáp Gốc: " + Util.getFormatNumber(player.pet.nPoint.defg),
                                            "Thông tin nhân vật", "thông tin Mục tiêu");
                                }
                                break;
//                            case 9:
//                                Input.gI().CheckInfo(player);
//                                break;
                      }
                        break;
                         case ConstNpc.menuchiton:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setchiton(player);
                                } catch (Exception e) {
                                }
                                break;
                                 }
                        break;
                        
                    case ConstNpc.menutd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().settaiyoken(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgenki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setkamejoko(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menunm:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodki(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setgoddam(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setsummon(player);
                                } catch (Exception e) {
                                }
                                break;
                        }
                        break;

                    case ConstNpc.menuxd:
                        switch (select) {
                            case 0:
                                try {
                                    ItemService.gI().setgodgalick(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 1:
                                try {
                                    ItemService.gI().setmonkey(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 2:
                                try {
                                    ItemService.gI().setgodhp(player);
                                } catch (Exception e) {
                                }
                                break;
                            case 3:
                                try {
                                    ItemService.gI().setkidbu(player);
                                } catch (Exception e) {
                                }
                                break;    
                        }
                        break;

                    case ConstNpc.CONFIRM_DISSOLUTION_CLAN:
                        switch (select) {
                            case 0:
                                Clan clan = player.clan;
                                clan.deleteDB(clan.id);
                                Manager.CLANS.remove(clan);
                                player.clan = null;
                                player.clanMember = null;
                                ClanService.gI().sendMyClan(player);
                                ClanService.gI().sendClanId(player);
                                Service.getInstance().sendThongBao(player, "Đã giải tán bang hội.");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_ACTIVE:
                        switch (select) {
                            case 0:
                                if (player.getSession().goldBar >= 20) {
                                    player.kichhoat = 1;
                                    
                                        Service.getInstance().sendThongBao(player, "Đã mở thành viên thành công!");
                                        break;
                                    
                                }
//                                Service.getInstance().sendThongBao(player, "Bạn không có vàng\n Vui lòng NROGOD.COM để nạp thỏi vàng");
                                break;
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            player.inventory.itemsBoxCrackBall.clear();
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x, p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x, player.location.y);
                                    }
                                    break;
                                case 2:
                                    Input.gI().createFormChangeName(player, p);
                                    break;
                                case 3:
                                    String[] selects = new String[]{"Đồng ý", "Hủy"};
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                            "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    break;
                                case 4:
                                    Service.getInstance().sendThongBao(player, "Kik người chơi " + p.name + " thành công");
                                    Client.gI().getPlayers().remove(p);
                                    Client.gI().kickSession(p.getSession());
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.DOI_TIEN:  // 
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_DOITIEN1, 564,
                                        "Con Đang Có " + player.getSession().vnd + "vnd", "20.000vnđ", "50.000vnđ", "100.000vnđ", "200.000vnđ");
                                                break;
                                                
                            case 1:  //

                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_DOITIEN2, 564,
                                        "Con Đang Có" + player.getSession().vnd + "vnd", "20.000vnđ", "50.000vnđ", "100.000vnđ", "200.000vnđ");
                                break;
                                
                            case 2:  //

                                break;

                        }
                        break;
                    case ConstNpc.DOI_DTVIP:  // 
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 30000) {
                                    if (player.pet == null) {
                                        Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                    } else {
                                   try {
                                        
                                        
                                         PetService.gI().changeBillPet(player, player.gender);
                                        GirlkunDB.executeUpdate("update player set vnd = (vnd - 300000) where id = " + player.id);
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }                                    
                                }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                }
                                break;          
                            case 1:  //

                                if (player.getSession().vnd >= 50000) {
                                    if (player.pet == null) {
                                        Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                    } else {
                                  try {
                                        
                                       
                                         PetService.gI().changeWhisPet(player, player.gender);
                                        GirlkunDB.executeUpdate("update player set vnd = (vnd - 500000) where id = " + player.id);
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }                                    
                                }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                }
                                break;
                                
                            case 2:  //

                               if (player.getSession().vnd >= 100000) {
                                    if (player.pet == null) {
                                        Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                    } else {
                                   try {
                                        
                                       
                                         PetService.gI().changeGokuPet(player, player.gender);
                                        GirlkunDB.executeUpdate("update player set vnd = (vnd - 100000) where id = " + player.id);
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }                                    
                                }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                }
                                break;
                            
                             case 3:  //

                               if (player.getSession().vnd >= 150000) {
                                    if (player.pet == null) {
                                        Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                    } else {
                                  try {
                                        
                                        
                                         PetService.gI().changeCumberPet(player, player.gender);
                                        GirlkunDB.executeUpdate("update player set vnd = (vnd - 150000) where id = " + player.id);
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }                                    
                                }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");
                                }
                                break;

                        }
                        break;
                    case ConstNpc.MENU_DOITIEN1:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 20000) {
                                    try {
                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 40);
                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 20000);
                                        
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 50000) {
                                    try {
                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 100);
                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 50000);
                                       
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;
                            case 2:  // 
                                if (player.getSession().vnd >= 100000) {
                                    try {
                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 200);
                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 100000);
                                        
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;
                            case 3:  // 
                                if (player.getSession().vnd >= 200000) {
                                    try {
                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 400);
                                        InventoryServiceNew.gI().addItemBag(player, thoivang);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 200000);
                                        
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;
                        }
                        break;
                    case ConstNpc.MENU_DOITIEN2:
                        switch (select) {
                            case 0:
                                if (player.getSession().vnd >= 20000) {
                                    try {
                                        player.inventory.ruby += 20000;
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 20000);
                                        
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;

                            case 1:
                                if (player.getSession().vnd >= 50000) {

                                    try {
                                        player.inventory.gem += 50000;
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 50000);
                                        
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;
                            case 2:  // 
                                if (player.getSession().vnd >= 100000) {

                                    try {
                                        player.inventory.gem += 100000;
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        PlayerDAO.subvnd(player, 100000);
                                        
                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;
                            case 3:  // 
                                if (player.getSession().vnd >= 200000) {
                                    try {
                                        player.inventory.gem += 200000;
                                        Service.getInstance().sendMoney(player);
                                        InventoryServiceNew.gI().sendItemBags(player);
                                        
                                        GirlkunDB.executeUpdate("update player set vnd = (vnd + 200000) where id = " + player.id);

                                        Service.getInstance().sendThongBao(player, "doi thanh cong");
                                    } catch (Exception ex) {
                                        java.util.logging.Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ tiền");

                                }
                                break;

                        }
                        break;
                    case ConstNpc.MENU_EVENT:
                        switch (select) {
                            case 0:
                                Service.getInstance().sendThongBaoOK(player, "Điểm sự kiện: " + player.inventory.event + " ngon ngon...");
                                break;
                            case 1:
                                Util.showListTop(player, (byte) 2);
                                break;
                            case 2:
                                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_GIAO_BONG, -1, "Người muốn giao bao nhiêu bông...",
//                                        "100 bông", "1000 bông", "10000 bông");
                                break;
                            case 3:
                                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc...");
//                                NpcService.gI().createMenuConMeo(player, ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN, -1, "Con có thực sự muốn đổi thưởng?\nPhải giao cho ta 3000 điểm sự kiện đấy... ",
//                                        "Đồng ý", "Từ chối");
                                break;

                        }
                        break;
                        
                    case ConstNpc.TamkjllTruytim:
                        if (player.TamkjllDauLaDaiLuc[7] == 0) {
                            switch (select) {
                                case 0:
                                    if (player.ExpTamkjll < 1000000) {
                                        Service.gI().sendThongBaoOK(player, "Cần 1tr Exp Diệt Thần");
                                        return;
                                    }
                                    player.ExpTamkjll -= 1000000;
                                    if (Util.isTrue(0.2f, 100)) {
                                         player.nPoint.dameg += Util.nextInt(5, 20);
                                         player.nPoint.hpg += Util.nextInt(5, 20);
                                        String hcnhan = player
                                                .TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7]))
                                                + "\n";
                                        if (player.TamkjllDauLaDaiLuc[7] == 1) {
                                            hcnhan += "Tăng: " +  player.nPoint.dameg + " % chỉ số\n";
                                            hcnhan += "giảm: " + player.nPoint.hpg / 3
                                                    + " % HP Gốc\n";
                                        }
                                         if (player.TamkjllDauLaDaiLuc[7] == 2) {
                                            hcnhan += "Tăng: " +  player.nPoint.hpg * 5
                                                    + "% Hp Gốc\n";
//                                        }
//                                        if (player.TamkjllDauLaDaiLuc[7] == 3) {
//                                            hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
//                                                    + "% sát thương nhận.\n";
//                                            hcnhan += "Có tỉ lệ x2 dame.\n";
//                                        }
//                                        if (player.TamkjllDauLaDaiLuc[7] == 4) {
//                                            hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 250000000L
//                                                    + "dame.\n";
//                                            hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 2
//                                                    + "% dame người ở gần.\n";
//                                        }
//                                        if (player.TamkjllDauLaDaiLuc[7] == 5) {
//                                            hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 1000000000L
//                                                    + "Sinh lực.\n";
//                                            hcnhan += "hồi phục: " + player.TamkjllDauLaDaiLuc[8] / 3
//                                                    + "% Sinh lực sau 3s.\n";
//                                        }
//                                        if (player.TamkjllDauLaDaiLuc[7] == 6) {
//                                            hcnhan += "Đánh Sát thương chuẩn: "
//                                                    + player.TamkjllDauLaDaiLuc[8] * 100000000L
//                                                    + "dame.\n";
                                        }
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                                "Telegram: @Diệt Thần\n"
                                                        + "Thông tin hồn cốt\n"
                                                        + hcnhan
                                                        + "\nHãy chọn theo lí trí của mình.",
                                                "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                    } else {
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                                "Telegram: @Diệt Thần\n"
                                                        + "Truy tìm thất bại.",
                                                "Truy tìm");
                                    }
                                    break;
                                case 1:
                                    if (player.ExpTamkjll < 1) {
                                        Service.gI().sendThongBaoOK(player, "Cần 500tr Exp Diệt Thần");
                                        return;
                                    }
                                    player.ExpTamkjll -= 1;
                                    player.TamkjllDauLaDaiLuc[7] += Util.nextInt(1, 6);
                                    player.TamkjllDauLaDaiLuc[8] += Util.nextInt(5, 20);
                                    String hcnhan = player
                                            .TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7]))
                                            + "\n";
                                    if (player.TamkjllDauLaDaiLuc[7] == 1) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] + " % chỉ số\n";
                                        hcnhan += "giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                + " % thời gian Skill đấm, max 20%.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 2) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] / 5
                                                + "% Up Sao Pha Lê\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 3) {
                                        hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                + "% sát thương nhận.\n";
                                        hcnhan += "Có tỉ lệ x2 dame.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 4) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 250000000d
                                                + "dame.\n";
                                        hcnhan += "Giảm: " + player.TamkjllDauLaDaiLuc[8] / 2
                                                + "% dame người ở gần.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 5) {
                                        hcnhan += "Tăng: " + player.TamkjllDauLaDaiLuc[8] * 1000000000d
                                                + "Sinh lực.\n";
                                        hcnhan += "hồi phục: " + player.TamkjllDauLaDaiLuc[8] / 3
                                                + "% Sinh lực sau 3s.\n";
                                    }
                                    if (player.TamkjllDauLaDaiLuc[7] == 6) {
                                        hcnhan += "Đánh Sát thương chuẩn: "
                                                + player.TamkjllDauLaDaiLuc[8] * 100000000d
                                                + "dame.\n";
                                    }
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                            "Telegram: @Diệt Thần\n"
                                                    + "Thông tin hồn cốt\n"
                                                    + hcnhan
                                                    + "\nHãy chọn theo lí trí của mình.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    break;
                                case 1:
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    Service.gI().sendThongBaoOK(player, "Đã hủy hồn cốt");
                                    break;
                                case 2:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllDYHapthu, -1,
                                            "Telegram: @Diệt Thần\n"
                                                    + "Chọn phương pháp hấp thụ.",
                                            "Dựa vào bản thân\n(50% thành công)", "Nhờ trợ giúp\n(100% thành công)");
                                    break;
                            }
                        }
                        break;
//                              case ConstNpc.TamkjllNCHC:
//                        Item mhc = InventoryServiceNew.gI().findItem(player.inventory.itemsBag,
//                                1156);
//                        switch (select) {
//                            case 0:
//                                if (player.CapTamkjll < 200) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
//                                    return;
//                                }
//                                if (player.ExpTamkjll < 125000000) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 125tr Exp Diệt Thần");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[9] != 1) {
//                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
//                                    return;
//                                }
//                                if (mhc != null && mhc.quantity > 12500) {
//                                    player.TamkjllDauLaDaiLuc[10] += Util.nextInt(5, 20);
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
//                                            "Telegram: @Diệt Thần\n"
//                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.TamkjllNameHoncot(1)
//                                                    + "\nChỉ Số sau khi nâng cấp :\n"
//                                                    + "Tăng: " + player.TamkjllDauLaDaiLuc[10] + " % chỉ số\n"
//                                                    + "giảm: "
//                                                    + (player.TamkjllDauLaDaiLuc[10] / 3 >= 20 ? 20
//                                                            : player.TamkjllDauLaDaiLuc[10] / 3)
//                                                    + "% thời gian Skill đấm",
//                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
//                                            player.TamkjllNameHoncot(3),
//                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
//                                            player.TamkjllNameHoncot(6));
//                                    player.ExpTamkjll -= 125000000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 12500);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                } else {
//                                    Service.gI().sendThongBaoOK(player, "Cần 12,5k mảnh hồn cốt");
//                                }
//                                break;
//                            case 1:
//                                if (player.CapTamkjll < 200) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
//                                    return;
//                                }
//                                if (player.ExpTamkjll < 130000000) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 130tr Exp Diệt Thần");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[11] != 1) {
//                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[12] / 5 >= 20) {
//                                    Service.gI().sendThongBaoOK(player, "Đã tối đa không thể tăng thêm nữa");
//                                    return;
//                                }
//                                if (mhc != null && mhc.quantity > 13000) {
//                                    player.TamkjllDauLaDaiLuc[12] += Util.nextInt(5, 20);
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
//                                            "Telegram: @Diệt Thần\n"
//                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.TamkjllNameHoncot(2)
//                                                    + "\nChỉ Số sau khi nâng cấp :\n"
//                                                    + "Tăng: "
//                                                    + (player.TamkjllDauLaDaiLuc[12] / 5 >= 20 ? 20
//                                                            : player.TamkjllDauLaDaiLuc[12] / 5)
//                                                    + "% Khả năng up các loại exp cao cấp của thế giới này.",
//                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
//                                            player.TamkjllNameHoncot(3),
//                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
//                                            player.TamkjllNameHoncot(6));
//                                    player.ExpTamkjll -= 130000000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 13000);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                } else {
//                                    Service.gI().sendThongBaoOK(player, "Cần 13k mảnh hồn cốt");
//                                }
//                                break;
//                            case 2:
//                                if (player.CapTamkjll < 200) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
//                                    return;
//                                }
//                                if (player.ExpTamkjll < 110000000) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 110tr Exp Diệt Thần");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[13] != 1) {
//                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[14] / 3 >= 80) {
//                                    Service.gI().sendThongBaoOK(player, "Đã tối đa không thể tăng thêm nữa");
//                                    return;
//                                }
//                                if (mhc != null && mhc.quantity > 11000) {
//                                    player.TamkjllDauLaDaiLuc[14] += Util.nextInt(5, 20);
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
//                                            "Telegram: @Diệt Thần\n"
//                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.TamkjllNameHoncot(3)
//                                                    + "\nChỉ Số sau khi nâng cấp :\n"
//                                                    + "Giảm: " + (player.TamkjllDauLaDaiLuc[14] / 3 >= 80 ? 80
//                                                            : player.TamkjllDauLaDaiLuc[14] / 3)
//                                                    + "% sát thương nhận.\n",
//                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
//                                            player.TamkjllNameHoncot(3),
//                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
//                                            player.TamkjllNameHoncot(6));
//                                    player.ExpTamkjll -= 110000000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 11000);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                } else {
//                                    Service.gI().sendThongBaoOK(player, "Cần 11k mảnh hồn cốt");
//                                }
//                                break;
//                            case 3:
//                                if (player.CapTamkjll < 200) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
//                                    return;
//                                }
//                                if (player.ExpTamkjll < 115000000) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 115tr Exp Diệt Thần");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[15] != 1) {
//                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
//                                    return;
//                                }
//                                if (mhc != null && mhc.quantity > 11500) {
//                                    player.TamkjllDauLaDaiLuc[16] += Util.nextInt(5, 20);
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
//                                            "Telegram: @Diệt Thần\n"
//                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.TamkjllNameHoncot(4)
//                                                    + "\nChỉ Số sau khi nâng cấp :\n"
//                                                    + "Tăng: "
//                                                    + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[16] * 250000000d)
//                                                    + "dame.\n"
//                                                    + "Giảm: " + (player.TamkjllDauLaDaiLuc[16] / 2 >= 90 ? 90
//                                                            : player.TamkjllDauLaDaiLuc[16] / 2)
//                                                    + "% dame người ở gần.",
//                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
//                                            player.TamkjllNameHoncot(3),
//                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
//                                            player.TamkjllNameHoncot(6));
//                                    player.ExpTamkjll -= 115000000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 11500);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                } else {
//                                    Service.gI().sendThongBaoOK(player, "Cần 11,5k mảnh hồn cốt");
//                                }
//                                break;
//                            case 4:
//                                if (player.CapTamkjll < 200) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
//                                    return;
//                                }
//                                if (player.ExpTamkjll < 100000000) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 100tr Exp Diệt Thần");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[17] != 1) {
//                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
//                                    return;
//                                }
//                                if (mhc != null && mhc.quantity > 9000) {
//                                    player.TamkjllDauLaDaiLuc[18] += Util.nextInt(5, 20);
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
//                                            "Telegram: @Diệt Thần\n"
//                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.TamkjllNameHoncot(5)
//                                                    + "\nChỉ Số sau khi nâng cấp :\n"
//                                                    + "Tăng: "
//                                                    + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[18] * 1000000000d)
//                                                    + "Sinh lực.\n"
//                                                    + "+hồi phục: " + (player.TamkjllDauLaDaiLuc[18] / 3 >= 90 ? 90
//                                                            : player.TamkjllDauLaDaiLuc[18] / 3)
//                                                    + "% Sinh lực sau 3s.",
//                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
//                                            player.TamkjllNameHoncot(3),
//                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
//                                            player.TamkjllNameHoncot(6));
//                                    player.ExpTamkjll -= 100000000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 9000);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                } else {
//                                    Service.gI().sendThongBaoOK(player, "Cần 9k mảnh hồn cốt");
//                                }
//                                break;
//                            case 5:
//                                if (player.CapTamkjll < 200) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 200 cấp Diệt Thần");
//                                    return;
//                                }
//                                if (player.ExpTamkjll < 100000000) {
//                                    Service.gI().sendThongBaoOK(player, "Cần ít nhất 100tr Exp Diệt Thần");
//                                    return;
//                                }
//                                if (player.TamkjllDauLaDaiLuc[19] != 1) {
//                                    Service.gI().sendThongBaoOK(player, "Bạn không sỡ hữu hồn cồn này");
//                                    return;
//                                }
//                                if (mhc != null && mhc.quantity > 10000) {
//                                    player.TamkjllDauLaDaiLuc[20] += Util.nextInt(5, 20);
//                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllNCHC, -1,
//                                            "Telegram: @Diệt Thần\n"
//                                                    + "Bạn nâng cấp thành công hồn cốt:\n" + player.TamkjllNameHoncot(6)
//                                                    + "\nChỉ Số sau khi nâng cấp :\n"
//                                                    + "Đánh Sát thương chuẩn: "
//                                                    + Util.getFormatNumber(player.TamkjllDauLaDaiLuc[20] * 100000000d)
//                                                    + "dame.",
//                                            player.TamkjllNameHoncot(1), player.TamkjllNameHoncot(2),
//                                            player.TamkjllNameHoncot(3),
//                                            player.TamkjllNameHoncot(4), player.TamkjllNameHoncot(5),
//                                            player.TamkjllNameHoncot(6));
//                                    player.ExpTamkjll -= 100000000;
//                                    InventoryServiceNew.gI().subQuantityItemsBag(player, mhc, 10000);
//                                    InventoryServiceNew.gI().sendItemBags(player);
//                                } else {
//                                    Service.gI().sendThongBaoOK(player, "Cần 10k mảnh hồn cốt");
//                                }
//                                break;
//                        }
//                        break;
                    case ConstNpc.TamkjllDYHapthu:
                        switch (select) {
                            case 0:
                                if (player.TamkjllDauLaDaiLuc[7] == 1 && player.TamkjllDauLaDaiLuc[9] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.TamkjllNameHoncot(
                                                        Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[9] = 1;
                                        player.TamkjllDauLaDaiLuc[10] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 2 && player.TamkjllDauLaDaiLuc[11] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.TamkjllNameHoncot(
                                                        Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[11] = 1;
                                        player.TamkjllDauLaDaiLuc[12] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 3 && player.TamkjllDauLaDaiLuc[13] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.TamkjllNameHoncot(
                                                        Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[13] = 1;
                                        player.TamkjllDauLaDaiLuc[14] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 4 && player.TamkjllDauLaDaiLuc[15] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.TamkjllNameHoncot(
                                                        Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[15] = 1;
                                        player.TamkjllDauLaDaiLuc[16] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 5 && player.TamkjllDauLaDaiLuc[17] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.TamkjllNameHoncot(
                                                        Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[17] = 1;
                                        player.TamkjllDauLaDaiLuc[18] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 6 && player.TamkjllDauLaDaiLuc[19] != 1) {
                                    if (Util.isTrue(30f, 100)) {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                                + player.TamkjllNameHoncot(
                                                        Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                        player.TamkjllDauLaDaiLuc[19] = 1;
                                        player.TamkjllDauLaDaiLuc[20] = player.TamkjllDauLaDaiLuc[8];
                                    } else {
                                        Service.gI().sendThongBaoOK(player, "Hấp thụ thất bại hồn cốt đã tan biến.");
                                    }
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                } else {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                            "Telegram: @Diệt Thần\n"
                                                    + "Ngươi đã sở hữu hồn cốt này rồi."
                                                    + "\nChỉ còn hủy hồn cốt hoặc để đó trưng.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                }
                                break;
                            case 1:
                                if (player.ExpTamkjll < 150000000) {
                                    Service.gI().sendThongBaoOK(player, "Cần 150tr exp Diệt Thần.");
                                    return;
                                }
//                                if (player.CapTamkjll < 50) {
//                                    Service.gI().sendThongBaoOK(player, "Cần 50 Cấp Diệt Thần.");
//                                    return;
                                }
                                if (player.TamkjllDauLaDaiLuc[7] == 1 && player.TamkjllDauLaDaiLuc[9] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[9] = 1;
                                    player.TamkjllDauLaDaiLuc[10] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 2 && player.TamkjllDauLaDaiLuc[11] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[11] = 1;
                                    player.TamkjllDauLaDaiLuc[12] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 3 && player.TamkjllDauLaDaiLuc[13] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[13] = 1;
                                    player.TamkjllDauLaDaiLuc[14] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 4 && player.TamkjllDauLaDaiLuc[15] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[15] = 1;
                                    player.TamkjllDauLaDaiLuc[16] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 5 && player.TamkjllDauLaDaiLuc[17] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[17] = 1;
                                    player.TamkjllDauLaDaiLuc[18] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else if (player.TamkjllDauLaDaiLuc[7] == 6 && player.TamkjllDauLaDaiLuc[19] != 1) {
                                    Service.gI().sendThongBaoOK(player, "Hấp thụ thành công hồn cốt: "
                                            + player.TamkjllNameHoncot(Util.TamkjllGH(player.TamkjllDauLaDaiLuc[7])));
                                    player.TamkjllDauLaDaiLuc[19] = 1;
                                    player.TamkjllDauLaDaiLuc[20] = player.TamkjllDauLaDaiLuc[8];
                                    player.TamkjllDauLaDaiLuc[7] = 0;
                                    player.TamkjllDauLaDaiLuc[8] = 0;
                                    player.ExpTamkjll -= 150000000;
                                } else {
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TamkjllTruytim, -1,
                                            "Telegram: @Diệt Thần\n"
                                                    + "Ngươi đã sở hữu hồn cốt này rồi."
                                                    + "\nChỉ còn hủy hồn cốt hoặc để đó trưng.",
                                            "đóng", "Hủy hồn cốt", "Hấp thụ hồn cốt");
                                }
                                break;
//                        }
//                        break;
                        
                  case ConstNpc.MENU_GIAO_BONG:
                        ItemService.gI().giaobong(player, (int) Util.tinhLuyThua(10, select + 2));
                        break;
                    case ConstNpc.CONFIRM_DOI_THUONG_SU_KIEN:
                        if (select == 0) {
                            ItemService.gI().openBoxVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_TELE_NAMEC:
                        if (select == 0) {
                            NgocRongNamecService.gI().teleportToNrNamec(player);
                            player.inventory.subGemAndRuby(50);
                            Service.getInstance().sendMoney(player);
                        }
                        break;
                }
            }
        };
    }

}
