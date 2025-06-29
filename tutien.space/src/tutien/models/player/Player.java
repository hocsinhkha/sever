package tutien.models.player;

import BoMong.BoMong;
import tutien.card.Card;
import tutien.models.map.MapMaBu.MapMaBu;
import tutien.models.skill.PlayerSkill;

import java.util.List;
import tutien.models.item.ItemTimeSieuCap;
import tutien.models.clan.Clan;
import tutien.models.intrinsic.IntrinsicPlayer;
import tutien.models.item.Item;
import tutien.models.item.ItemTime;
import tutien.models.npc.special.MagicTree;
import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstTask;
import tutien.models.npc.special.MabuEgg;
import tutien.models.mob.MobMe;
import tutien.data.DataGame;
import tutien.models.clan.ClanMember;
import tutien.models.map.BanDoKhoBau.BanDoKhoBauService;
import tutien.models.map.FightBahatmit.FightBossBahatmit;
import tutien.models.map.Mapchichi.Map22h;
import barcoll.models.map.TrapMap;
import barcoll.models.map.Zone;
import tutien.models.map.blackball.BlackBallWar;
import barcoll.models.matches.IPVP;
import barcoll.models.matches.TYPE_LOSE_PVP;
import tutien.models.npc.special.DuaHau;
import tutien.models.skill.Skill;
import barcoll.server.Manager;
import barcoll.services.Service;
import tutien.server.io.MySession;
import tutien.models.task.TaskPlayer;
import com.girlkun.network.io.Message;
import barcoll.server.Client;
import barcoll.services.EffectSkillService;
import barcoll.services.FriendAndEnemyService;
import barcoll.services.PetService;
import barcoll.services.TaskService;
import tutien.services.func.ChangeMapService;
import tutien.services.func.ChonAiDay;
import tutien.services.func.CombineNew;
import tutien.services.func.TopService;
import tutien.utils.Logger;
import tutien.utils.Util;

import java.util.ArrayList;

public class Player {

    public MySession session;

    public boolean beforeDispose;
public boolean haveDuongTang;
    public boolean isPet;
    public boolean isNewPet;
    public boolean isNewPet1;
    public boolean isBoss;
    public int NguHanhSonPoint = 0;
    public int pointvongquay = 0;
    
    public int diemhotong = 0; 
    public int diemtrungthu = 0;
    public int diemnaubanhtrung = 0;
    
    public int diemgapthu = 0; // gấp thú
    public int diemrutthuong = 0;
    
    public int quaythuong = 0; // điểm rut
    public int quayvip = 0;
    public int diemquay = 0;
    public int diemquaydoidiem = 0;
    public int diemtrieuhoithu = 0;
    public int toptrieuhoi = 0;
    
    public int ExpTamkjll = 0;
    public int goldTai;
    public int goldXiu;
    public String Hppl = "\n";
    public int diemsanboss = 0;
    public int diemphangiai = 0;
    public int diemsukientet = 0;
    public IPVP pvp;
    public int pointPvp;
    public int chuyenSinh;
    public byte maxTime = 30;
    public byte type = 0;
    public int kichhoat;
    public short idAura = -1;
    public int ThamGiaDaiChienBangHoi = 0;
    public int HoiSinhDaiChienBangHoi = 5;
    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public List<Zone> mapMaBu;
    public boolean haveBarcoll;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public NewPet newpet;
    //  public NewPet newpet1;
    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public DuaHau duahau;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public ItemTimeSieuCap itemTimesieucap;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;
    public FightMabu fightMabu;
    public long diemdanh;
    public int vnd;
    public long[] TamkjllDauLaDaiLuc = new long[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    public long[] Tamkjlltutien = new long[] { 0, 0, 0 };
    public int tongnap;
//    public byte vip
//    public long timevip        
    
    public Clan clan;
    public ClanMember clanMember;
    public SkillSpecial skillSpecial;
    public FightBossBahatmit fightbahatmit;
    public Player pkbahatmit;
    public List<Friend> friends;
    public List<Enemy> enemies;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember;
    public short head;

    public byte typePk;
    public int mocnap1 = 0;
//    public int mocnap2 = 0;
//     public int mocnap3 = 0;
    public boolean nhanqua;
    public int nhanqua2;
    public byte cFlag;

    public boolean haveTennisSpaceShip;

    public boolean justRevived;
    public long lastTimeRevived;

    public int violate;
    public byte totalPlayerViolate;
    public long timeChangeZone;
    public long lastTimeUseOption;
    
    
    
    public String TrieuHoiNamePlayer;
    public int TrieuHoiCapBac = -1;
    public String TenThuTrieuHoi;
    public int TrieuHoiThucAn;
    public long TrieuHoiDame;
    public long TrieuHoiHP;
    public long TrieuHoilastTimeThucan;
    public int TrieuHoiLevel;
    public long TrieuHoiExpThanThu;
    public Player TrieuHoiPlayerAttack;
    public double TrieuHoidamethanmeo;
    public long Autothucan;
    public boolean trangthai = false;

    public short idNRNM = -1;
    public short idGo = -1;
    public long lastTimePickNRNM;
    public int goldNormar;
    public int goldVIP;
    public long lastTimeWin;
    public boolean isWin;
    public long limitgold;
    public int levelWoodChest;
    public boolean receivedWoodChest;
    public int goldChallenge;
    public int gemChallenge;
    public List<Card> Cards = new ArrayList<>();
    public long lasttimeuseoption;
    public BoMong achievement;
    public byte capCS;
    public byte capTT;
    public boolean titleitem;
    public boolean titlett;
    public boolean isTitleUse;
    public long lastTimeTitle1;
    public boolean isTitleUse2;
    public long lastTimeTitle2;
    public boolean isTitleUse3;
    public long lastTimeTitle3;
    public boolean isTitleUse4;
    public long lastTimeTitle4;  
public long lasttimenhanqua;
    public Player() {
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory();
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer();
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag();
        fightMabu = new FightMabu(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer();
        friends = new ArrayList<>();
        enemies = new ArrayList<>();
        itemTime = new ItemTime(this);
        itemTimesieucap = new ItemTimeSieuCap(this);
        charms = new Charms();
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        achievement = new BoMong(this);
    }

    //--------------------------------------------------------------------------
    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp <= 0;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    public void setSession(MySession session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public MySession getSession() {
        return this.session;
    }

    public boolean isPl() {
        return !isPet && !isBoss && !isNewPet;
    }

    public void update() {
        if (!this.beforeDispose) {
            try {
                if (!iDMark.isBan()) {
                    if (this.name != null) {
                        if (this.isPl() && Client.gI().getPlayers().size() >= 10
                                && Util.canDoWithTime(this.lasttimenhanqua, 60000)) {
                            this.lasttimenhanqua = System.currentTimeMillis();
                            this.ExpTamkjll += Client.gI().getPlayers().size();
                            this.inventory.ruby += Client.gI().getPlayers().size() / 10;
                            Service.gI().sendMoney(this);
                        }
                        if (this.isPl() && !this.isDie() && this.Tamkjlltutien[2] >= 1
                                && this.Tamkjlltutien[0] >= TamkjllDieukiencanhgioi(
                                        Util.TamkjllGH(this.Tamkjlltutien[1]))) {
                            if (Util.isTrue(Tamkjlltilecanhgioi(Util.TamkjllGH(this.Tamkjlltutien[1])), 100)) {
                                this.Tamkjlltutien[0] -= TamkjllDieukiencanhgioi(
                                        Util.TamkjllGH(this.Tamkjlltutien[1]));
                                this.Tamkjlltutien[1]++;
                                Service.gI().sendThongBao(this, "Bạn đã thăng cảnh giới thành công lên: "
                                        + this.TamkjllTuviTutien(Util.TamkjllGH(this.Tamkjlltutien[1])));
                            } else {
                                this.Tamkjlltutien[0] -= TamkjllDieukiencanhgioi(
                                        Util.TamkjllGH(this.Tamkjlltutien[1]));
                                if (Util.isTrue(20f, 100)) {
                                    this.inventory.gold += Util.nextInt(1, Util.nextInt(1, Util.nextInt(1, 5)));
                                    Service.gI().sendThongBaoOK(this, "Trong lúc tu tiên thất bại bạn nhận đc 1 vàng");
                                }
                                Service.gI().sendThongBao(this,
                                        "Bạn đã thăng cảnh giới thất bại và bị mất Chân Khí, cảnh giới bạn vẫn ở: "
                                                + this.TamkjllTuviTutien(Util.TamkjllGH(this.Tamkjlltutien[1])));
                                this.setDie(this);
                            }
                        }
                    }
                        
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (newpet != null) {
                        newpet.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (itemTimesieucap != null) {
                        itemTimesieucap.update();
                    }
                    if (this.lastTimeTitle1 != 0 && Util.canDoWithTime(this.lastTimeTitle1, 6000)) {
                        lastTimeTitle1 = 0;
                        isTitleUse = false;
                    }
                    if (this.lastTimeTitle2 != 0 && Util.canDoWithTime(this.lastTimeTitle2, 6000)) {
                        lastTimeTitle2 = 0;
                        isTitleUse2 = false;
                    }

                    if (this.lastTimeTitle3 != 0 && Util.canDoWithTime(this.lastTimeTitle3, 6000)) {
                        lastTimeTitle3 = 0;
                        isTitleUse3 = false;
                    }
                    if (this.lastTimeTitle4 != 0 && Util.canDoWithTime(this.lastTimeTitle4, 6000)) {
                        lastTimeTitle4 = 0;
                        isTitleUse4 = false;
                    }
                    long now = System.currentTimeMillis();     
                    BlackBallWar.gI().update(this);
                    MapMaBu.gI().update(this);
                    Map22h.gI().update(this);
                    if (!isBoss && this.iDMark.isGotoFuture() && Util.canDoWithTime(this.iDMark.getLastTimeGoToFuture(), 1000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.iDMark.setGotoFuture(false);
                    }
                    if (!isBoss && this.iDMark.isGoToBDKB() && Util.canDoWithTime(this.iDMark.getLastTimeGoToBDKB(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 135, -1, 100);
                        this.iDMark.setGoToBDKB(false);
                    }
                    if (!isBoss && this.iDMark.isGoToKGHD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToKGHD(), 6000)) {
                        ChangeMapService.gI().changeMapInYard(this, 149, -1, 100);
                        this.iDMark.setGoToKGHD(false);
                    }
                    if (!isBoss && this.iDMark.isGoToCDRD() && Util.canDoWithTime(this.iDMark.getLastTimeGoToKGHD(), 10000)) {
                        ChangeMapService.gI().changeMapInYard(this, 143, -1, 1108);
                        this.iDMark.setGoToCDRD(false);
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (this.isPl() && this.inventory.itemsBody.get(7) != null) {
                        Item it = this.inventory.itemsBody.get(7);
                        if (it != null && it.isNotNullItem() && this.newpet == null) {
                            switch (it.template.id) {
                                case 1407: // Con cún vàng
                                    PetService.Pet2(this, 663, 664, 665);
                                    Service.gI().point(this);
                                    break;
                                case 1408: // Cua đỏ
                                    PetService.Pet2(this, 1074, 1075, 1076);
                                    Service.gI().point(this);
                                    break;
                                case 1409: // Bí ma
                                    PetService.Pet2(this, 1158, 1159, 1160);
                                    Service.gI().point(this);
                                    break;
                                case 1410: // Bí ma vương
                                    PetService.Pet2(this, 1155, 1156, 1157);
                                    Service.gI().point(this);
                                    break;
                                case 1411: //Mèo đuôi vàng đen
                                    PetService.Pet2(this, 1183, 1184, 1185);
                                    Service.gI().point(this);
                                    break;
                                case 1412: //Mèo đuôi vàng trắng
                                    PetService.Pet2(this, 1201, 1202, 1203);
                                    Service.gI().point(this);
                                    break;
                                case 1311: //Gà 9 cự
                                    PetService.Pet2(this,  1419,1420,1421);
                                    Service.gI().point(this);
                                    break;
                                case 1312: //Ngựa 9 hồng mao
                                    PetService.Pet2(this,  1422,1423,1424);
                                    Service.gI().point(this);
                                    break;
                                case 1313: //Voi 9 ngà
                                    PetService.Pet2(this, 1425,1426,1427);
                                    Service.gI().point(this);
                                    break;
                                case 1416: //Minions
                                    PetService.Pet2(this, 1254, 1255, 1256);
                                    Service.gI().point(this);
                                    break;
                                case 1179: //Minions
                                    PetService.Pet2(this, 1347, 1348, 1349);
                                    Service.gI().point(this);
                                    break;

                            }
                        }
                    } else if (this.isPl() && newpet != null && !this.inventory.itemsBody.get(7).isNotNullItem()) {
                        newpet.dispose();
                        newpet = null;
                        // newpet1.dispose();
                        //  newpet1 = null;
                    }
                    if (location.lastTimeplayerMove < System.currentTimeMillis() - 30 * 60 * 1000) {
                        Client.gI().kickSession(getSession());
                    }
                } else {
                    if (Util.canDoWithTime(iDMark.getLastTimeBan(), 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
                Logger.logException(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     * {2033,2034,2035}: ht c3 td
     * {2030,2031,2032}: ht c3 nm
     * {2027,2028,2029}: ht c3 xd*/
    private static final short[][] idOutfitFusion = {
        {380, 381, 382}, {383, 384, 385}, {391, 392, 393},//luong long nhat the, va cap 1
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869},//bt cap 2
        {630, 631, 632}, {633, 634, 635}, {627, 628, 629}, //bt cap 3
        {1458, 1459, 1460}, {1490, 1491, 1492}, {1464, 1465, 1466},//bt cap 4
        {1271, 1272, 1273}, {1277, 1278, 1279}, {1274, 1275, 1276},//bt cap 5
    };

    public byte getAura() {
        Card card = this.Cards.stream().filter(r -> r != null && r.Used == 1).findFirst().orElse(null);
        if (card != null) {
            if (card.Id == 1142 && card.Used == 1 && card.Level >= 1) {
                return 1;
            }
            if (card.Id == 956 && card.Used == 1 && card.Level >= 1) {
                return 0;
            }

        }
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(5);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.id == 1121) {
            return 10;//
        } else if (item.template.id == 1339) {
            return 31;
            } else if (item.template.id == 1340) {
            return 31;
//        } else if (item.template.id == 283) {
//            return 2;
//        } else if (item.template.id == 284) {
//            return 3;
//        } else if (item.template.id == 285) {
//            return 4;
//        } else if (item.template.id == 286) {
//            return 5;
//        } else if (item.template.id == 287) {
//            return 6;
//        } else if (item.template.id == 288) {
//            return 7;
//        } else if (item.template.id == 289) {
//            return 8;
//        } else if (item.template.id == 290) {
//            return 9;
//        } else if (item.template.id == 291) {
//            return 10;
//        } else if (item.template.id == 292) {
//            return 11;
//        } else if (item.template.id == 386) {
//            return 12;
//        } else if (item.template.id == 387) {
//            return 6;
//        } else if (item.template.id == 388) {
//            return 7;
//        } else if (item.template.id == 389) {
//            return 8;
//        } else if (item.template.id == 390) {
//            return 9;
//        } else if (item.template.id == 391) {
//            return 10;
//        } else if (item.template.id == 392) {
//            return 11;
//        } else if (item.template.id == 393) {
//            return 12;
//        } else if (item.template.id == 968) {
//            return 20;
//        } else if (item.template.id == 969) {
//            return 22;
//        } else if (item.template.id == 970) {
//            return 23;
//        } else if (item.template.id == 971) {
//            return 24;
//        } else if (item.template.id == 972) {
//            return 25;
//            } else if (item.template.id == 1284) {
//            return 24;
//            } else if (item.template.id == 1443) {
//            return 30;
//            } else if (item.template.id == 1445) {
//            return 32;
//            } else if (item.template.id == 1322) {
//            return 17;
//        } else if (item.template.id == 1321) {
//            return 20;
//            } else if (item.template.id == 1320) {
//            return 23;
//        } else if (item.template.id == 1285) {
//            return 2;
//        } else if (item.template.id == 1286) {
//            return 3;
//        } else if (item.template.id == 1287) {
//            return 4;
//        } else if (item.template.id == 1288) {
//            return 5;
//        } else if (item.template.id == 1289) {
//            return 6;
//        } else if (item.template.id == 1290) {
//            return 7;
//        } else if (item.template.id == 1291) {
//            return 8;
//        } else if (item.template.id == 1292) {
//            return 9;
//        } else if (item.template.id == 1293) {
//            return 10;
//        } else if (item.template.id == 1294) {
//            return 11;
//        } else if (item.template.id == 1295) {
//            return 17;
//        } else if (item.template.id == 1296) {
//            return 6;
//        } else if (item.template.id == 1297) {
//            return 7;
//        } else if (item.template.id == 1298) {
//            return 8;
//        } else if (item.template.id == 1299) {
//            return 9;
//        } else if (item.template.id == 1300) {
//            return 10;
//        } else if (item.template.id == 1301) {
//            return 11;
//        } else if (item.template.id == 1302) {
//            return 12;
//        } else if (item.template.id == 1303) {
//            return 20;
//        } else if (item.template.id == 1304) {
//            return 22;
//        } else if (item.template.id == 1305) {
//            return 23;
//        } else if (item.template.id == 1306) {
//            return 24;
//        } else if (item.template.id == 1307) {
//            return 25;
//            } else if (item.template.id == 1308) {
//            return 24;
//            } else if (item.template.id == 1309) {
//            return 16;
//        } else if (item.template.id == 1310) {
//            return 20;
//            } else if (item.template.id == 1311) {
//            return 23;
//            } else if (item.template.id == 1312) {
//            return 23;
//            } else if (item.template.id == 1313) {
//            return 23;
//            } else if (item.template.id == 1314) {
//            return 23;
//            } else if (item.template.id == 1315) {
//            return 23;
//            } else if (item.template.id == 1316) {
//            return 23;
//            } else if (item.template.id == 1317) {
//            return 23;
//            } else if (item.template.id == 1318) {
//            return 23;
//            } else if (item.template.id == 1319) {
//            return 23;
//            } else if (item.template.id == 719) {
//            return 22;
//            } else if (item.template.id == 1329) {
//            return 1;
//            } else if (item.template.id == 1330) {
//            return 16;
//            } else if (item.template.id == 1331) {
//            return 0;
//            } else if (item.template.id == 1332) {
//            return 7;
//            } else if (item.template.id == 2032) {
//            return 28;
//            } else if (item.template.id == 1355) {
//            return 29;
//            } else if (item.template.id == 1365) {
//            return 24;
//            } else if (item.template.id == 1363) {
//            return 0;
//            } else if (item.template.id == 1364) {
//            return 24;
//            } else if (item.template.id == 1366) {
//            return 1;
            
        } else {
            return -1;
        }
    }

    public byte getEffFront() {  // by barcoll
        
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return 5;
        }
        Item item = this.inventory.itemsBody.get(5);
        if (!item.isNotNullItem()) {
            return 4;
        } else if (item.template.id == 1329) {
            return 9;  
        } else if (item.template.id == 1330) {
            return 13; 
        } else if (item.template.id == 1332) {
            return 15; 
        } else if (item.template.id == 1333) {
            return 16;     
        } else {
            return 6;
        }
    } 


    public short getHead() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }

                return idOutfitFusion[6 + this.gender][0];

            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }

                return idOutfitFusion[9 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][0];
//                }

                return idOutfitFusion[12 + this.gender][0];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int head = inventory.itemsBody.get(5).template.head;
            if (head != -1) {
                return (short) head;
            }
        }
        return this.head;
    }

    public short getBody() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
                return idOutfitFusion[3 + this.gender][1];
            
         } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];
//                }
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];

                return idOutfitFusion[9 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][1];

                return idOutfitFusion[12 + this.gender][1];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int body = inventory.itemsBody.get(5).template.body;
            if (body != -1) {
                return (short) body;
            }
        }
        if (inventory != null && inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION) {
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                return idOutfitFusion[3 + this.gender][2];
           } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                return idOutfitFusion[6 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                return idOutfitFusion[9 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
//                if (this.pet.typePet == 1) {
//                    return idOutfitFusion[3 + this.gender][2];
//                }
                return idOutfitFusion[12 + this.gender][2];
            }
        } else if (inventory != null && inventory.itemsBody.get(5).isNotNullItem()) {
            int leg = inventory.itemsBody.get(5).template.leg;
            if (leg != -1) {
                return (short) leg;
            }
        }
        if (inventory != null && inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.iDMark.isHoldBlackBall()) {
            return 31;
        } else if (this.idNRNM >= 353 && this.idNRNM <= 359) {
            return 30;
        }
        if (this.inventory.itemsBody.size() == 12) {   /// data body
            
            if (this.inventory.itemsBody.get(8).isNotNullItem()) {
                return this.inventory.itemsBody.get(8).template.part;
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (this.inventory.itemsBody.isEmpty() || this.inventory.itemsBody.size() < 10) {
            return -1;
        }
        Item item = this.inventory.itemsBody.get(9);
        if (!item.isNotNullItem()) {
            return -1;
        }
        if (item.template.type == 24) {
            if (item.template.gender == 3 || item.template.gender == this.gender) {
                return item.template.id;
            } else {
                return -1;
            }
        } else {
            if (item.template.id < 500) {
                return item.template.id;
            } else {
                return (short) DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
            }
        }
    }

    //--------------------------------------------------------------------------
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (this.pet != null && this.pet.status == Pet.PROTECT) {
                this.pet.playerAttack = plAtt;
            }
            boolean isSkillChuong = false;
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                    case Skill.MASENKO:
                    case Skill.ANTOMIC:
                        if (this.nPoint.voHieuChuong > 0) {
                            barcoll.services.PlayerService.gI().hoiPhuc(this, 0, Util.TamkjllGH(damage * this.nPoint.voHieuChuong / 100));
                            return 0;
                        }
                        isSkillChuong = true;
                }
            }
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = (int) (this.nPoint.hp - 1);
            }
            if (plAtt != null) {
                if (isSkillChuong && plAtt.nPoint != null && plAtt.nPoint.multicationChuong > 0 && Util.canDoWithTime(plAtt.nPoint.lastTimeMultiChuong, PlayerSkill.TIME_MUTIL_CHUONG)) {
                    damage *= plAtt.nPoint.multicationChuong;
                    plAtt.nPoint.lastTimeMultiChuong = System.currentTimeMillis();
                }
            }

            this.nPoint.subHP(damage);
            if (isDie()) {
                if (this.zone.map.mapId == 129 && plAtt != null) {
                    plAtt.pointPvp++;
                }

                if (plAtt != null && plAtt.isPl() && this.isPl()) {
                    TaskService.gI().checkDoneTaskPvP(plAtt);
                }

                setDie(plAtt);
            }

            return damage;
        } else {
            return 0;
        }
    }

    public void setDie(Player plAtt) {
        // Xóa phù
        if (this.effectSkin != null && this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.getInstance().point(this);
        }

        // Xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;

        // Xóa hiệu ứng skill
        if (this.effectSkill != null) {
            this.effectSkill.removeSkillEffectWhenDie();
        }

        // Xóa HP và MP
        this.nPoint.setHp(0);
        this.nPoint.setMp(0);

        // Xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }

        // Gọi Service xử lý khi nhân vật chết
        Service.getInstance().charDie(this);

        // Thêm kẻ thù
        if (!this.isPet && !this.isNewPet && !this.isNewPet1 && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isNewPet && !plAtt.isNewPet1 && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }

            // Xử lý nếu cả hai đều là người chơi
            if (this.isPl() && plAtt.isPl()) {
                plAtt.achievement.plusCount(3);
            }
        }

        // Kết thúc PK
        if (this.pvp != null) {
            this.pvp.lose(this, TYPE_LOSE_PVP.DEAD);
        }

        // Drop Black Ball
        BlackBallWar.gI().dropBlackBall(this);
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
    }

    public void preparedToDispose() {

    }
    public String TamkjllTuviTutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return "Ngưng khí kỳ";
            case 1:
                return "Trúc Cơ Kỳ";
            case 2:
                return "Kim đan kỳ";
            case 3:
                return "Nguyên Anh Kỳ";
            case 4:
                return "Hóa Thần đỉnh phong";
            case 5:
                return "Kết đan đỉnh phong";
            case 6:
                return "Luyện Hư Kỳ";
            case 7:
                return "Hợp Thể Kỳ";
            case 8:
                return "Thiên Nhân tầng 1";
            case 9:
                return "Thiên Nhân tầng 2";
            case 10:
                return "Thiên Nhân đỉnh phong";
            case 11:
                return "bán thần tầng 1";
            case 12:
                return "bán thần tầng 2";
            case 13:
               return "bán thần đỉnh phong";
            case 14:
                return "Thiên tôn Đỉnh Phong";
            case 15:
                return "Thái tổ Vô Địch";
            case 16:
                return "Chúa tể tầng 1";
            case 17:
                return "Chúa tể tầng 2";
            case 18:
                return "Chúa tể tầng 3";
            case 19:
                return "Chúa tể tầng 4";
            case 20:
                return "Chúa tể Đỉnh cao";
            case 21:
                return "Vĩnh hằng sơ kì";
            case 22:
                return "Vĩnh hằng trung kì";
            case 23:
                return "Vĩnh hằng Hậu kì";
            case 24:
                return "Vĩnh hằng đỉnh phong";
            case 25:
                return "Vĩnh hằng đỉnh cao";
            case 26:
                return "Vĩnh hằng hoàn mỹ";
            default:
                return "Phế vật";
        }
    }

    public int TamkjllDieukiencanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
                 return 5000000;
            case 1:
                 return 15000000;
            case 2:
                 return 25000000;
            case 3:
                 return 35000000;
            case 4:
                 return 35000000;
            case 5:
                 return 35000000;
            case 6:
                 return 35000000;
            case 7:
                 return 35000000;
            case 8:
                return 35000000;
            case 9:
                return 3700000;
            case 10:
                return 3700000;
            case 11:
                 return 35000000;
            case 12:
                 return 35000000;
            case 13:
                 return 40000000;
            case 14:
                  return 45000000;
            case 15:
                return 45000000;
            case 16:
                return 50000000;
            case 17:
                return 55000000;
            case 18:
                return 58000000;
            case 19:
                return 60000000;
            case 20:
                return 70000000;
            case 21:
                return 80000000;
            case 22:
                return 90000000;
            case 23:
                return 100000000;
            case 24:
                return 100000000;
            case 25:
                return 100000000;
            case 26:
                return 100000000;
            default:
                return Integer.MAX_VALUE;
        }
    }

    public float Tamkjlltilecanhgioi(int lvtt) {
        switch (lvtt) {
            case 0:
                  return 70f;
            case 1:
                  return 70f;
            case 2:
                  return 70f;
            case 3:
                  return 70f;
            case 4:
                  return 70f;
            case 5:
                  return 70f;
            case 6:
                  return 70f;
            case 7:
                  return 70f;
            case 8:
                return 70f;
            case 9:
                return 65f;
            case 10:
                return 60f;
            case 11:
                return 60f;
            case 12:
                return 60f;
            case 13:
                return 60f;
            case 14:
                return 60f;
            case 15:
                return 60f;
            case 16:
                return 60f;
            case 17:
                return 60f;
            case 18:
                return 60f;
            case 19:
                return 50f;
            case 20:
                return 47f;
            case 21:
                return 46f;
            case 22:
                return 43f;
            case 23:
                return 40f;
            case 24:
                return 39f;
            case 25:
                return 37f;
            case 26:
                return 35f;
            default:
                return 0.5f;
        }
    }

    public int TamkjllDametutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return 5;
            case 1:
                return 35;
            case 2:
                return 39;
            case 3:
                return 43;
            case 4:
                return 47;
            case 5:
                return 50;
            case 6:
                return 54;
            case 7:
                return 58;
            case 8:
                return 63;
            case 9:
                return 66;
            case 10:
                return 70;
            case 11:
                return 75;
            case 12:
                return 79;
            case 13:
                return 83;
            case 14:
                return 87;
            case 15:
                return 90;
            case 16:
                return 100;
            case 17:
                return 110;
            case 18:
                return 125;
            case 19:
                return 136;
            case 20:
                return 155;
            case 21:
                return 170;
            case 22:
                return 189;
            case 23:
                return 200;
            case 24:
                return 230;
            case 25:
                return 245;
            case 26:
                return 260;       
            default:
                return 0;
        }
    }

    public int TamkjllHpKiGiaptutien(int lvtt) {
        switch (lvtt) {
            case 0:
                return 10;
            case 1:
                return 30;
            case 2:
                return 50;
            case 3:
                return 70;
            case 4:
                return 90;
            case 5:
                return 110;
            case 6:
                return 130;
            case 7:
                return 150;
            case 8:
                return 170;
            case 9:
                return 200;
            case 10:
                return 230;
            case 11:
                return 250;
            case 12:
                return 280;
            case 13:
                return 310;
            case 14:
                return 330;
            case 15:
                return 360;
            case 16:
                return 390;
            case 17:
                return 440;
            case 18:
                return 490;
            case 19:
                return 500;
            case 20:
                return 520;
            case 21:
                return 550;
            case 22:
                return 570;
            case 23:
                return 600;
            case 24:
                return 630;
            case 25:
                return 670;
            case 26:
                return 700;            
            default:
                return 0;
        }
    }

    public void dispose() {
        if (pet != null) {
            pet.dispose();
            pet = null;
        }
        if (newpet != null) {
            newpet.dispose();
            newpet = null;
        }
        //  if (newpet1 != null) {
        //    newpet1.dispose();
        //     newpet1 = null;
        //}
        if (mapBlackBall != null) {
            mapBlackBall.clear();
            mapBlackBall = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapMaBu != null) {
            mapMaBu.clear();
            mapMaBu = null;
        }
        zone = null;
        mapBeforeCapsule = null;
        if (mapCapsule != null) {
            mapCapsule.clear();
            mapCapsule = null;
        }
        if (mobMe != null) {
            mobMe.dispose();
            mobMe = null;
        }
        location = null;
        if (setClothes != null) {
            setClothes.dispose();
            setClothes = null;
        }
        if (effectSkill != null) {
            effectSkill.dispose();
            effectSkill = null;
        }
        if (mabuEgg != null) {
            mabuEgg.dispose();
            mabuEgg = null;
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (playerTask != null) {
            playerTask.dispose();
            playerTask = null;
        }
        if (itemTime != null) {
            itemTime.dispose();
            itemTime = null;
        }
        if (itemTimesieucap != null) {
            itemTimesieucap.dispose();
            itemTimesieucap = null;
        }
        if (fusion != null) {
            fusion.dispose();
            fusion = null;
        }
        if (magicTree != null) {
            magicTree.dispose();
            magicTree = null;
        }
        if (playerIntrinsic != null) {
            playerIntrinsic.dispose();
            playerIntrinsic = null;
        }
        if (inventory != null) {
            inventory.dispose();
            inventory = null;
        }
        if (playerSkill != null) {
            playerSkill.dispose();
            playerSkill = null;
        }
        if (combineNew != null) {
            combineNew.dispose();
            combineNew = null;
        }
        if (iDMark != null) {
            iDMark.dispose();
            iDMark = null;
        }
        if (charms != null) {
            charms.dispose();
            charms = null;
        }
        if (effectSkin != null) {
            effectSkin.dispose();
            effectSkin = null;
        }
        if (gift != null) {
            gift.dispose();
            gift = null;
        }
        if (nPoint != null) {
            nPoint.dispose();
            nPoint = null;
        }
        if (rewardBlackBall != null) {
            rewardBlackBall.dispose();
            rewardBlackBall = null;
        }
        if (effectFlagBag != null) {
            effectFlagBag.dispose();
            effectFlagBag = null;
        }
        if (pvp != null) {
            pvp.dispose();
            pvp = null;
        }
        effectFlagBag = null;
        clan = null;
        clanMember = null;
        friends = null;
        enemies = null;
        session = null;
        name = null;
    }
 public String TamkjllNameHoncot(int Honcot) {
        switch (Honcot - 1) {
            case 0:
                return "-B\u00E1t Chu M\u00E2u ";
            case 1:
                return "-Tinh Th\u1EA7n Ng\u01B0ng T\u1EE5 Chi Tr\u00ED Tu\u1EC7 \u0110\u1EA7u C\u1ED1t";
            case 2:
                return "-Nhu C\u1ED1t Th\u1ECF H\u1EEFu T\u00ED C\u1ED1t";
            case 3:
                return "-Th\u00E1i Th\u1EA3n C\u1EF1 Vi\u00EAn";
            case 4:
                return "-Lam Ng\u00E2n Ho\u00E0ng";
            case 5:
                return "-T\u00E0 Ma H\u1ED5 K\u00ECnh";
            default:
                return "-H\u1ED3n x\u00E1c l\u00ECa \u0111\u1EDDi";
        }
    }

    public String percentGold(int type) {
        try {
            if (type == 0) {
                double percent = ((double) this.goldNormar / ChonAiDay.gI().goldNormar) * 100;
                return String.valueOf(Math.ceil(percent));
            } else if (type == 1) {
                double percent = ((double) this.goldVIP / ChonAiDay.gI().goldVip) * 100;
                return String.valueOf(Math.ceil(percent));
            }
        } catch (ArithmeticException e) {
            return "0";
        }
        return "0";
    }

    public List<String> textRuongGo = new ArrayList<>();

}
//nplayer
