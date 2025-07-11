package barcoll.server;

import tutien.card.OptionCard;
import tutien.card.RadarCard;
import tutien.card.RadarService;
import com.girlkun.database.GirlkunDB;
import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstMap;
import tutien.data.DataGame;
import tutien.jdbc.daos.GodGK;
import tutien.jdbc.daos.ShopDAO;
import tutien.kygui.ItemKyGui;
import tutien.kygui.ShopKyGuiManager;
import barcoll.models.Template.*;
import tutien.models.clan.Clan;
import tutien.models.clan.ClanMember;
import tutien.models.intrinsic.Intrinsic;
import tutien.models.item.Item;
import barcoll.models.map.WayPoint;
import barcoll.models.map.Zone;
//import barcoll.models.map.Zone;
import barcoll.models.matches.TOP;
import tutien.models.matches.pvp.DaiHoiVoThuat;
import barcoll.models.npc.Npc;
import barcoll.models.npc.NpcFactory;
import tutien.models.reward.ItemMobReward;
import tutien.models.reward.ItemOptionMobReward;
import tutien.models.reward.MobReward;
import tutien.models.shop.Shop;
import tutien.models.skill.NClass;
import tutien.models.skill.Skill;
import tutien.models.task.SideTaskTemplate;
import tutien.models.task.SubTaskMain;
import tutien.models.task.TaskMain;
import tutien.models.player.Referee1;
import barcoll.services.ItemService;
//import map.WayPoint;
import barcoll.services.MapService;
import tutien.utils.Logger;
import tutien.models.player.Referee;
import tutien.models.player.TestDame;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tutien.utils.Util;
import java.time.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Manager {

    private static Manager i;
    public static byte SUKIEN;// sau khi chinh
    public static byte KHUYEN_MAI_NAP = 1;
    public static byte SERVER = 1;
    public static byte SECOND_WAIT_LOGIN = 2;
    public static int MAX_PER_IP = 10;
    public static int MAX_PLAYER = 2000;
    public static byte RATE_EXP_SERVER = 2;
    public static boolean LOCAL = false;
//    public static byte RATE_EXP_SERVER = 1;// sau khi chinh

    public static MapTemplate[] MAP_TEMPLATES;
    public static final List<barcoll.models.map.Map> MAPS = new ArrayList<>();
    public static final List<ItemOptionTemplate> ITEM_OPTION_TEMPLATES = new ArrayList<>();
    public static final Map<Integer, MobReward> MOB_REWARDS = new HashMap<>();
    public static final List<ItemLuckyRound> LUCKY_ROUND_REWARDS = new ArrayList();
    public static final Map<String, Byte> IMAGES_BY_NAME = new HashMap<String, Byte>();
    public static final List<ItemTemplate> ITEM_TEMPLATES = new ArrayList<>();
    public static final List<MobTemplate> MOB_TEMPLATES = new ArrayList<>();
    public static final List<NpcTemplate> NPC_TEMPLATES = new ArrayList<>();
    public static final List<String> CAPTIONS = new ArrayList<>();
    public static final List<TaskMain> TASKS = new ArrayList<>();
    public static final List<SideTaskTemplate> SIDE_TASKS_TEMPLATE = new ArrayList<>();
    public static final List<Intrinsic> INTRINSICS = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_TD = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_NM = new ArrayList<>();
    public static final List<Intrinsic> INTRINSIC_XD = new ArrayList<>();
    public static final List<HeadAvatar> HEAD_AVATARS = new ArrayList<>();
    public static final List<FlagBag> FLAGS_BAGS = new ArrayList<>();
    public static final List<NClass> NCLASS = new ArrayList<>();
    public static final List<Npc> NPCS = new ArrayList<>();
    public static List<Shop> SHOPS = new ArrayList<>();
    public static final List<Clan> CLANS = new ArrayList<>();
    public static final List<String> NOTIFY = new ArrayList<>();
    public static final ArrayList<DaiHoiVoThuat> LIST_DHVT = new ArrayList<>();
    public static final List<Item> RUBY_REWARDS = new ArrayList<>();
    public static final String queryTopSM = "SELECT id, \n"
            + "       CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(data_point, ',', 2), ',', -1) AS UNSIGNED) AS sm\n"
            + "FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1)\n"
            + "ORDER BY sm DESC\n"
            + "LIMIT 10;";
    public static final String queryTopvirus = "SELECT id, \n"
            + "       CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(data_inventory, ',', 5), ',', -1) AS UNSIGNED) AS sm\n"
            + "FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1)\n"
            + "ORDER BY sm DESC\n"
            + "LIMIT 10;";
    public static final String queryTopgapthu = "SELECT id, CAST( diemgapthu AS UNSIGNED) AS diemgapthu FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( diemgapthu AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryToprutthuong = "SELECT id, CAST( diemrutthuong AS UNSIGNED) AS diemrutthuong FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( diemrutthuong AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopsanboss = "SELECT id, CAST( diemsanboss AS UNSIGNED) AS diemsanboss FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( diemsanboss AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopSD = "SELECT id, \n"
            + "       CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(data_point, ',', 8), ',', -1) AS UNSIGNED) AS sd\n"
            + "FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1)\n"
            + "ORDER BY sd DESC\n"
            + "LIMIT 20;";
    public static final String queryTopHP = "SELECT id, \n"
            + "       CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(data_point, ',', 6), ',', -1) AS UNSIGNED) AS hp\n"
            + "FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1)\n"
            + "ORDER BY hp DESC\n"
            + "LIMIT 20;";
    public static final String queryTopKI = "SELECT id, CAST( split_str(data_point,',',7) AS UNSIGNED) AS ki FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( split_str(data_point,',',7)  AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopNV =  "SELECT id, \n"
            + "       CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(data_task, ',', 1), ',', -1) AS UNSIGNED) AS sm\n"
            + "FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1)\n"
            + "ORDER BY sm DESC\n"
            + "LIMIT 10;";
    public static final String querytopSB = "SELECT id, CAST( diemsanboss AS UNSIGNED) AS diemsanboss FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( diemsanboss AS UNSIGNED) DESC LIMIT 20;";
    public static final String querytopSK = "SELECT id, CAST( split_str( data_inventory,',',4)  AS UNSIGNED) AS event FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( split_str( data_inventory,',',4)  AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopPVP = "SELECT id, CAST( pointPvp AS UNSIGNED) AS pointPvp FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( pointPvp AS UNSIGNED) DESC LIMIT 50;";
    public static final String queryTopNHS = "SELECT id, CAST( NguHanhSonPoint AS UNSIGNED) AS nhs FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( NguHanhSonPoint AS UNSIGNED) DESC LIMIT 20;";
    public static final String queryTopKhiGas = "SELECT id, CAST( khi_gas AS UNSIGNED) AS khi_gas FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( khi_gas AS UNSIGNED) DESC LIMIT 50;";
    public static final String queryTopVND = "SELECT id, CAST( vnd AS UNSIGNED) AS vnd FROM player WHERE id NOT IN (SELECT id FROM account WHERE is_admin = 1) ORDER BY CAST( vnd AS UNSIGNED) DESC LIMIT 20;";
    
    public static List<TOP> topSM;
    public static List<TOP> topvirus;
    public static List<TOP> gapthu;
    public static List<TOP> rutthuong;
    public static List<TOP> sanboss;
    public static List<TOP> topSD;
    public static List<TOP> topHP;
    public static List<TOP> topKI;
    public static List<TOP> topNV;
    public static List<TOP> topSB;
    public static List<TOP> topSK;
    public static List<TOP> topPVP;
    public static List<TOP> topNHS;
    public static List<TOP> topKhiGas;
    public static List<TOP> topSieuHang;
    public static List<TOP> topVND;
    public static long timeRealTop = 0;
    public static final short[] itemIds_TL = {555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567, 561};
    public static final short[] itemIds_HD = {650, 652, 654, 651, 653, 655, 657, 659, 661, 658, 660, 662, 656};
    public static final byte[] itemIds_NR_SB = {14, 15, 16};
    public static final short[] itemDC12 = {233, 237, 241, 245, 249, 253, 257, 261, 265, 269, 273, 277};
    
    public static final short[] itemManh = {1066, 1067, 1068, 1069, 1070};
// skh tl
    public static final short[] aotd   = {555};
    public static final short[] quantd = {556};
    public static final short[] gangtd = {562};
    public static final short[] giaytd = {563};
    public static final short[] aoxd   = {559};
    public static final short[] quanxd = {560};
    public static final short[] gangxd = {566};
    public static final short[] giayxd = {567};
    public static final short[] aonm   = {557};
    public static final short[] quannm = {558};
    public static final short[] gangnm = {564};
    public static final short[] giaynm = {565};
    //public static final short[] radaSKHVip = {186, 187, 278, 279, 280, 281, 561};
    // skh hd
    public static final short[] aotdhd   = {232, 233, 650};
    public static final short[] quantdhd = {244, 245, 651};
    public static final short[] gangtdhd = {256, 257, 657};
    public static final short[] giaytdhd = {268, 269, 658};
    public static final short[] aoxdhd   = {240, 241, 652};
    public static final short[] quanxdhd = {252, 253, 653};
    public static final short[] gangxdhd = {264, 265, 659};
    public static final short[] giayxdhd = {276, 277, 660};
    public static final short[] aonmhd   = {236, 237, 654};
    public static final short[] quannmhd = {248, 249, 655};
    public static final short[] gangnmhd = {260, 261, 661};
    public static final short[] giaynmhd = {272, 273, 662};
  //  public static final short[] radaSKHViphd = {186, 187, 278, 279, 280, 281, 561};
    // skh ts
    public static final short[] aotdts =   {233, 555, 1048};
    public static final short[] quantdts = {245, 556, 1051};
    public static final short[] gangtdts = {257, 562, 1054};
    public static final short[] giaytdts = {269, 563, 1057};
    public static final short[] aoxdts =   {241, 559, 1049};
    public static final short[] quanxdts = {253, 560, 1052};
    public static final short[] gangxdts = {265, 566, 1055};
    public static final short[] giayxdts = {277, 567, 1058};
    public static final short[] aonmts =   {237, 557, 1050};
    public static final short[] quannmts = {249, 558, 1053};
    public static final short[] gangnmts = {261, 564, 1056};
    public static final short[] giaynmts = {273, 565, 1059};
   // public static final short[] radaSKHVip = {186, 187, 278, 279, 280, 281, 561};
    //rada
    public static final short[] radaSKHVip =   {561};
    public static final short[] radaSKHViphd = {279, 280, 281, 656};
    public static final short[] radaSKHVipts = {279, 280, 281, 1060};

    public static final short[][][] doSKHVip = {{aotd, quantd, gangtd, giaytd}, {aonm, quannm, gangnm, giaynm}, {aoxd, quanxd, gangxd, giayxd}};
    public static final short[][][] doSKHViphd = {{aotdhd, quantdhd, gangtdhd, giaytdhd}, {aonmhd, quannmhd, gangnmhd, giaynmhd}, {aoxdhd, quanxdhd, gangxdhd, giayxdhd}};
    public static final short[][][] doSKHVipts = {{aotdts, quantdts, gangtdts, giaytdts}, {aonmts, quannmts, gangnmts, giaynmts}, {aoxdts, quanxdts, gangxdts, giayxdts}};
 public static final List<AchievementTemplate> ACHIEVEMENTS = new ArrayList<>();
    public static Manager gI() {
        if (i == null) {
            i = new Manager();
        }
        return i;
    }

    private Manager() {
        try {
            loadProperties();
        } catch (IOException ex) {
            Logger.logException(Manager.class, ex, "Lỗi load properites");
            System.exit(0);
        }
        this.loadDatabase();
        NpcFactory.createNpcConMeo();
        NpcFactory.createNpcRongThieng();
        
        this.initMap();
    }
    
    public static List<TOP> realTopSieuHang(Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT id, CAST( split_str(data_point,',',18) AS UNSIGNED) AS rank FROM player ORDER BY CAST( split_str(data_point,',',18) AS UNSIGNED) ASC LIMIT 100");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long rank = rs.getLong("rank");
                if (rank > 0 && rank <= 100) {
                    TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                    top.setInfo1("");
                    top.setInfo2("");
                    tops.add(top);
                }
            }
        } catch (Exception e) {
        }
        return tops;
    }

    private void initMap() {
    int[][] tileTyleTop = readTileIndexTileType(ConstMap.TILE_TOP);
    for (MapTemplate mapTemp : MAP_TEMPLATES) {
        int[][] tileMap = readTileMap(mapTemp.id);
        int[] tileTop = tileTyleTop[mapTemp.tileId - 1];
            barcoll.models.map.Map map = new barcoll.models.map.Map(mapTemp.id,
                mapTemp.name, mapTemp.planetId, mapTemp.tileId, mapTemp.bgId,
                mapTemp.bgType, mapTemp.type, tileMap, tileTop,
                mapTemp.zones,
                mapTemp.maxPlayerPerZone, mapTemp.wayPoints);
        MAPS.add(map);
        map.initMob(mapTemp.mobTemp, mapTemp.mobLevel, mapTemp.mobHp, mapTemp.mobX, mapTemp.mobY);
        map.initNpc(mapTemp.npcId, mapTemp.npcX, mapTemp.npcY);
//        Thread mapThread = new Thread(map, "Update map " + map.mapName);
//        mapThread.start();
    }
    new Thread (()-> { // giảm thread scr
    try {
        while (!Maintenance.isRuning) {
            long st = System.currentTimeMillis();
            for (barcoll.models.map.Map map : MAPS) {
                for (Zone zone : map.zones) {
                    try {
                     zone.update();
                    } catch (Exception e) {
                        
                    }
                }
            }
            long timeDo = System.currentTimeMillis() - st;
            if (1000-timeDo > 0) {
                Thread.sleep(1000 - timeDo);
            }
        }
    } catch (Exception ex) {
        
    }
    },"Update maps").start();
    
    Referee r = new Referee();
    r.initReferee();
    
    TestDame r2 = new TestDame();
        r2.initTestDame();
    
//    Referee1 r1 = new Referee1();
//        r1.initReferee1();
    Logger.success("Init map thành công!\n");
}


    public static void loadPart() {
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            //load part
            ps = con.prepareStatement("select * from part");
            rs = ps.executeQuery();
            List<Part> parts = new ArrayList<>();
            while (rs.next()) {
                Part part = new Part();
                part.id = rs.getShort("id");
                part.type = rs.getByte("type");
                dataArray = (JSONArray) jv.parse(rs.getString("data").replaceAll("\\\"", ""));
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONArray pd = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                    part.partDetails.add(new PartDetail(Short.parseShort(String.valueOf(pd.get(0))),
                            Byte.parseByte(String.valueOf(pd.get(1))),
                            Byte.parseByte(String.valueOf(pd.get(2)))));
                    pd.clear();
                }
                parts.add(part);
                dataArray.clear();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/girlkun/update_data/part"));
            dos.writeShort(parts.size());
            for (Part part : parts) {
                dos.writeByte(part.type);
                for (PartDetail partDetail : part.partDetails) {
                    dos.writeShort(partDetail.iconId);
                    dos.writeByte(partDetail.dx);
                    dos.writeByte(partDetail.dy);
                }
            }
            dos.flush();
            dos.close();
            Logger.success("luu (" + parts.size() + ")\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDatabase() {
        long st = System.currentTimeMillis();
        JSONValue jv = new JSONValue();
        JSONArray dataArray = null;
        JSONObject dataObject = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection con = GirlkunDB.getConnection();) {
            //load part
            ps = con.prepareStatement("select * from part");
            rs = ps.executeQuery();
            List<Part> parts = new ArrayList<>();
            while (rs.next()) {
                Part part = new Part();
                part.id = rs.getShort("id");
                part.type = rs.getByte("type");
                dataArray = (JSONArray) jv.parse(rs.getString("data").replaceAll("\\\"", ""));
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONArray pd = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                    part.partDetails.add(new PartDetail(Short.parseShort(String.valueOf(pd.get(0))),
                            Byte.parseByte(String.valueOf(pd.get(1))),
                            Byte.parseByte(String.valueOf(pd.get(2)))));
                    pd.clear();
                }
                parts.add(part);
                dataArray.clear();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("data/girlkun/update_data/part"));
            dos.writeShort(parts.size());
            for (Part part : parts) {
                dos.writeByte(part.type);
                for (PartDetail partDetail : part.partDetails) {
                    dos.writeShort(partDetail.iconId);
                    dos.writeByte(partDetail.dx);
                    dos.writeByte(partDetail.dy);
                }
            }
            dos.flush();
            dos.close();
            Logger.success("luu (" + parts.size() + ")\n");

            //load small version
            ps = con.prepareStatement("select count(id) from small_version");
            rs = ps.executeQuery();
            List<byte[]> smallVersion = new ArrayList<>();
            if (rs.first()) {
                int size = rs.getInt(1);
                for (int i = 0; i < 4; i++) {
                    smallVersion.add(new byte[size]);
                }
            }
            ps = con.prepareStatement("select * from small_version order by id");
            rs = ps.executeQuery();
            int index = 0;
            while (rs.next()) {
                smallVersion.get(0)[index] = rs.getByte("x1");
                smallVersion.get(1)[index] = rs.getByte("x2");
                smallVersion.get(2)[index] = rs.getByte("x3");
                smallVersion.get(3)[index] = rs.getByte("x4");
                index++;
            }
            for (int i = 0; i < 4; i++) {
                dos = new DataOutputStream(new FileOutputStream("data/girlkun/data_img_version/x" + (i + 1) + "/img_version"));
                dos.writeShort(smallVersion.get(i).length);
                for (int j = 0; j < smallVersion.get(i).length; j++) {
                    dos.writeByte(smallVersion.get(i)[j]);
                }
                dos.flush();
                dos.close();
            }

            //load clan
            ps = con.prepareStatement("select * from clan_sv" + SERVER);
            rs = ps.executeQuery();
            while (rs.next()) {
                Clan clan = new Clan();
                clan.id = rs.getInt("id");
                clan.name = rs.getString("name");
                clan.slogan = rs.getString("slogan");
                clan.imgId = rs.getByte("img_id");
                clan.powerPoint = rs.getLong("power_point");
                clan.maxMember = rs.getByte("max_member");
                clan.capsuleClan = rs.getInt("clan_point");
                clan.level = rs.getByte("level");
                clan.createTime = (int) (rs.getTimestamp("create_time").getTime() / 1000);
                dataArray = (JSONArray) jv.parse(rs.getString("members"));
                for (int i = 0; i < dataArray.size(); i++) {
                    dataObject = (JSONObject) jv.parse(String.valueOf(dataArray.get(i)));
                    ClanMember cm = new ClanMember();
                    cm.clan = clan;
                    cm.id = Integer.parseInt(String.valueOf(dataObject.get("id")));
                    cm.name = String.valueOf(dataObject.get("name"));
                    cm.head = Short.parseShort(String.valueOf(dataObject.get("head")));
                    cm.body = Short.parseShort(String.valueOf(dataObject.get("body")));
                    cm.leg = Short.parseShort(String.valueOf(dataObject.get("leg")));
                    cm.role = Byte.parseByte(String.valueOf(dataObject.get("role")));
                    cm.donate = Integer.parseInt(String.valueOf(dataObject.get("donate")));
                    cm.receiveDonate = Integer.parseInt(String.valueOf(dataObject.get("receive_donate")));
                    cm.memberPoint = Integer.parseInt(String.valueOf(dataObject.get("member_point")));
                    cm.clanPoint = Integer.parseInt(String.valueOf(dataObject.get("clan_point")));
                    cm.joinTime = Integer.parseInt(String.valueOf(dataObject.get("join_time")));
                    cm.timeAskPea = Long.parseLong(String.valueOf(dataObject.get("ask_pea_time")));
                    try {
                        cm.powerPoint = Long.parseLong(String.valueOf(dataObject.get("power")));
                    } catch (Exception e) {
                    }
                    clan.addClanMember(cm);
                }
                CLANS.add(clan);
                dataArray.clear();
                dataObject.clear();
            }

            ps = con.prepareStatement("select id from clan_sv" + SERVER + " order by id desc limit 1");
            rs = ps.executeQuery();
            if (rs.first()) {
                Clan.NEXT_ID = rs.getInt("id") + 1;
            }

            Logger.success("Thông báo tải dữ liệu bang hội thành công (" + CLANS.size() + ") (" + Clan.NEXT_ID + ")\n");

            ps = con.prepareStatement("select * from dhvt_template");
            while (rs.next()) {
                DaiHoiVoThuat dhvt = new DaiHoiVoThuat();
                dhvt.NameCup = rs.getString(2);
                dhvt.Time = rs.getString(3).split("\n");
                dhvt.gem = rs.getInt(4);
                dhvt.gold = rs.getInt(5);
                dhvt.min_start = rs.getInt(6);
                dhvt.min_start_temp = rs.getInt(6);
                dhvt.min_limit = rs.getInt(7);
                LIST_DHVT.add(dhvt);
            }
            Logger.success("Thông báo: tải dữ liệu Đại Hội Võ Thuật thành công (" + LIST_DHVT.size() + ")\n");

            //load skill
            ps = con.prepareStatement("select * from skill_template order by nclass_id, slot");
            rs = ps.executeQuery();
            byte nClassId = -1;
            NClass nClass = null;
            while (rs.next()) {
                byte id = rs.getByte("nclass_id");
                if (id != nClassId) {
                    nClassId = id;
                    nClass = new NClass();
                    nClass.name = id == ConstPlayer.TRAI_DAT ? "Trái Đất" : id == ConstPlayer.NAMEC ? "Namếc" : "Xayda";
                    nClass.classId = nClassId;
                    NCLASS.add(nClass);
                }
                SkillTemplate skillTemplate = new SkillTemplate();
                skillTemplate.classId = nClassId;
                skillTemplate.id = rs.getByte("id");
                skillTemplate.name = rs.getString("name");
                skillTemplate.maxPoint = rs.getByte("max_point");
                skillTemplate.manaUseType = rs.getByte("mana_use_type");
                skillTemplate.type = rs.getByte("type");
                skillTemplate.iconId = rs.getShort("icon_id");
                skillTemplate.damInfo = rs.getString("dam_info");
//                skillTemplate.damInfo1 = rs.getString("dam_info1");
//                skillTemplate.damInfo2 = rs.getString("dam_info2");
                nClass.skillTemplatess.add(skillTemplate);

                dataArray = (JSONArray) jv.parse(
                        rs.getString("skills")
                                .replaceAll("\\[\"", "[")
                                .replaceAll("\"\\[", "[")
                                .replaceAll("\"\\]", "]")
                                .replaceAll("\\]\"", "]")
                                .replaceAll("\\}\",\"\\{", "},{")
                );
                for (int j = 0; j < dataArray.size(); j++) {
                    JSONObject dts = (JSONObject) jv.parse(String.valueOf(dataArray.get(j)));
                    Skill skill = new Skill();
                    skill.template = skillTemplate;
                    skill.skillId = Short.parseShort(String.valueOf(dts.get("id")));
                    skill.point = Byte.parseByte(String.valueOf(dts.get("point")));
                    skill.powRequire = Long.parseLong(String.valueOf(dts.get("power_require")));
                    skill.manaUse = Integer.parseInt(String.valueOf(dts.get("mana_use")));
                    skill.coolDown = Integer.parseInt(String.valueOf(dts.get("cool_down")));
                    skill.dx = Integer.parseInt(String.valueOf(dts.get("dx")));
                    skill.dy = Integer.parseInt(String.valueOf(dts.get("dy")));
                    skill.maxFight = Integer.parseInt(String.valueOf(dts.get("max_fight")));
                    skill.damage = Short.parseShort(String.valueOf(dts.get("damage")));
                    skill.price = Short.parseShort(String.valueOf(dts.get("price")));
                    skill.moreInfo = String.valueOf(dts.get("info"));
                    skillTemplate.skillss.add(skill);
                }
            }
            Logger.success("Thông báo tải dữ liệu skill thành công (" + NCLASS.size() + ")\n");
            
            
            //load head avatar
            ps = con.prepareStatement("select * from head_avatar");
            rs = ps.executeQuery();
            while (rs.next()) {
                HeadAvatar headAvatar = new HeadAvatar(rs.getInt("head_id"), rs.getInt("avatar_id"));
                HEAD_AVATARS.add(headAvatar);
            }
            Logger.success("luu(" + HEAD_AVATARS.size() + ")\n");

            //load flag bag
            ps = con.prepareStatement("select * from flag_bag");
            rs = ps.executeQuery();
            while (rs.next()) {
                FlagBag flagBag = new FlagBag();
                flagBag.id = rs.getByte("id");
                flagBag.name = rs.getString("name");
                flagBag.gold = rs.getInt("gold");
                flagBag.gem = rs.getInt("gem");
                flagBag.iconId = rs.getShort("icon_id");
                String[] iconData = rs.getString("icon_data").split(",");
                flagBag.iconEffect = new short[iconData.length];
                for (int j = 0; j < iconData.length; j++) {
                    flagBag.iconEffect[j] = Short.parseShort(iconData[j].trim());
                }
                FLAGS_BAGS.add(flagBag);
            }
            Logger.success("Load flag bag thành công (" + FLAGS_BAGS.size() + ")\n");

            //load intrinsic
            ps = con.prepareStatement("select * from intrinsic");
            rs = ps.executeQuery();
            while (rs.next()) {
                Intrinsic intrinsic = new Intrinsic();
                intrinsic.id = rs.getByte("id");
                intrinsic.name = rs.getString("name");
                intrinsic.paramFrom1 = rs.getShort("param_from_1");
                intrinsic.paramTo1 = rs.getShort("param_to_1");
                intrinsic.paramFrom2 = rs.getShort("param_from_2");
                intrinsic.paramTo2 = rs.getShort("param_to_2");
                intrinsic.icon = rs.getShort("icon");
                intrinsic.gender = rs.getByte("gender");
                switch (intrinsic.gender) {
                    case ConstPlayer.TRAI_DAT:
                        INTRINSIC_TD.add(intrinsic);
                        break;
                    case ConstPlayer.NAMEC:
                        INTRINSIC_NM.add(intrinsic);
                        break;
                    case ConstPlayer.XAYDA:
                        INTRINSIC_XD.add(intrinsic);
                        break;
                    default:
                        INTRINSIC_TD.add(intrinsic);
                        INTRINSIC_NM.add(intrinsic);
                        INTRINSIC_XD.add(intrinsic);
                }
                INTRINSICS.add(intrinsic);
            }
            Logger.success("Load intrinsic thành công (" + INTRINSICS.size() + ")\n");

            //load task
            ps = con.prepareStatement("SELECT id, task_main_template.name, detail, "
                    + "task_sub_template.name AS 'sub_name', max_count, notify, npc_id, map "
                    + "FROM task_main_template JOIN task_sub_template ON task_main_template.id = "
                    + "task_sub_template.task_main_id");
            rs = ps.executeQuery();
            int taskId = -1;
            TaskMain task = null;
            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != taskId) {
                    taskId = id;
                    task = new TaskMain();
                    task.id = taskId;
                    task.name = rs.getString("name");
                    task.detail = rs.getString("detail");
                    TASKS.add(task);
                }
                SubTaskMain subTask = new SubTaskMain();
                subTask.name = rs.getString("sub_name");
                subTask.maxCount = rs.getShort("max_count");
                subTask.notify = rs.getString("notify");
                subTask.npcId = rs.getByte("npc_id");
                subTask.mapId = rs.getShort("map");
                task.subTasks.add(subTask);
            }
            Logger.success("Load task thành công (" + TASKS.size() + ")\n");

            //load side task
            ps = con.prepareStatement("select * from side_task_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                SideTaskTemplate sideTask = new SideTaskTemplate();
                sideTask.id = rs.getInt("id");
                sideTask.name = rs.getString("name");
                String[] mc1 = rs.getString("max_count_lv1").split("-");
                String[] mc2 = rs.getString("max_count_lv2").split("-");
                String[] mc3 = rs.getString("max_count_lv3").split("-");
                String[] mc4 = rs.getString("max_count_lv4").split("-");
                String[] mc5 = rs.getString("max_count_lv5").split("-");
                sideTask.count[0][0] = Integer.parseInt(mc1[0]);
                sideTask.count[0][1] = Integer.parseInt(mc1[1]);
                sideTask.count[1][0] = Integer.parseInt(mc2[0]);
                sideTask.count[1][1] = Integer.parseInt(mc2[1]);
                sideTask.count[2][0] = Integer.parseInt(mc3[0]);
                sideTask.count[2][1] = Integer.parseInt(mc3[1]);
                sideTask.count[3][0] = Integer.parseInt(mc4[0]);
                sideTask.count[3][1] = Integer.parseInt(mc4[1]);
                sideTask.count[4][0] = Integer.parseInt(mc5[0]);
                sideTask.count[4][1] = Integer.parseInt(mc5[1]);
                SIDE_TASKS_TEMPLATE.add(sideTask);
            }
            Logger.success("Load task thành công (" + TASKS.size() + ")\n");
 // load nhiem vu bo mong
            ps = con.prepareStatement("SELECT * FROM achievement");
            rs = ps.executeQuery();
            while (rs.next()) {
                AchievementTemplate achi = new AchievementTemplate(
                        rs.getInt("id"),
                        rs.getString("info1"),
                        rs.getString("info2"),
                        rs.getInt("count_purpose"),
                        rs.getInt("gem"));
                ACHIEVEMENTS.add(achi);
            }
            Logger.success("Load achievement done (" + ACHIEVEMENTS.size() + ")");
            //load side task
            ps = con.prepareStatement("select * from side_task_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                SideTaskTemplate sideTask = new SideTaskTemplate();
                sideTask.id = rs.getInt("id");
                sideTask.name = rs.getString("name");
                String[] mc1 = rs.getString("max_count_lv1").split("-");
                String[] mc2 = rs.getString("max_count_lv2").split("-");
                String[] mc3 = rs.getString("max_count_lv3").split("-");
                String[] mc4 = rs.getString("max_count_lv4").split("-");
                String[] mc5 = rs.getString("max_count_lv5").split("-");
                sideTask.count[0][0] = Integer.parseInt(mc1[0]);
                sideTask.count[0][1] = Integer.parseInt(mc1[1]);
                sideTask.count[1][0] = Integer.parseInt(mc2[0]);
                sideTask.count[1][1] = Integer.parseInt(mc2[1]);
                sideTask.count[2][0] = Integer.parseInt(mc3[0]);
                sideTask.count[2][1] = Integer.parseInt(mc3[1]);
                sideTask.count[3][0] = Integer.parseInt(mc4[0]);
                sideTask.count[3][1] = Integer.parseInt(mc4[1]);
                sideTask.count[4][0] = Integer.parseInt(mc5[0]);
                sideTask.count[4][1] = Integer.parseInt(mc5[1]);
                SIDE_TASKS_TEMPLATE.add(sideTask);
            }

            //load item template
            ps = con.prepareStatement("select * from item_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemTemplate itemTemp = new ItemTemplate();
                itemTemp.id = rs.getShort("id");
                itemTemp.type = rs.getByte("type");
                itemTemp.gender = rs.getByte("gender");
                itemTemp.name = rs.getString("name");
                itemTemp.description = rs.getString("description");
                itemTemp.iconID = rs.getShort("icon_id");
                itemTemp.part = rs.getShort("part");
                itemTemp.isUpToUp = rs.getBoolean("is_up_to_up");
                itemTemp.strRequire = rs.getInt("power_require");
                itemTemp.gold = rs.getInt("gold");
                itemTemp.gem = rs.getInt("gem");
                itemTemp.head = rs.getInt("head");
                itemTemp.body = rs.getInt("body");
                itemTemp.leg = rs.getInt("leg");
                ITEM_TEMPLATES.add(itemTemp);
            }
            Logger.success("Load map item template thành công (" + ITEM_TEMPLATES.size() + ")\n");

            //load item option template
            ps = con.prepareStatement("select id, name from item_option_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                ItemOptionTemplate optionTemp = new ItemOptionTemplate();
                optionTemp.id = rs.getInt("id");
                optionTemp.name = rs.getString("name");
                ITEM_OPTION_TEMPLATES.add(optionTemp);
            }
            Logger.success("Load map item option template thành công (" + ITEM_OPTION_TEMPLATES.size() + ")\n");

            //load shop
            SHOPS = ShopDAO.getShops(con);
            Logger.success("Load shop thành công (" + SHOPS.size() + ")\n");

            //load reward lucky round
            File folder = new File("data/girlkun/data_lucky_round_reward");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        line = line.replaceAll("[{}\\[\\]]", "");
                        String[] arrSub = line.split("\\|");
                        String[] data1 = arrSub[0].split(":");
                        ItemLuckyRound item = new ItemLuckyRound();
                        item.temp = ItemService.gI().getTemplate(Integer.parseInt(data1[0]));
                        item.ratio = Integer.parseInt(data1[1]);
                        item.typeRatio = Integer.parseInt(data1[2]);
                        if (arrSub.length > 1) {
                            String[] data2 = arrSub[1].split(";");
                            for (String str : data2) {
                                String[] data = str.split(":");
                                ItemOptionLuckyRound io = new ItemOptionLuckyRound();
                                Item.ItemOption itemOption = new Item.ItemOption(Integer.parseInt(data[0]), 0);
                                io.itemOption = itemOption;
                                String[] param = data[1].split("-");
                                io.param1 = Integer.parseInt(param[0]);
                                if (param.length == 2) {
                                    io.param2 = Integer.parseInt(param[1]);
                                }
                                item.itemOptions.add(io);
                            }
                        }
                        LUCKY_ROUND_REWARDS.add(item);
                    }
                }
            }
            Logger.success("Load reward lucky round thành công (" + LUCKY_ROUND_REWARDS.size() + ")\n");
            //load reward mob
            folder = new File("data/girlkun/mob_reward");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    DataInputStream dis = new DataInputStream(new FileInputStream(fileEntry));
                    int size = dis.readInt();
                    for (int i = 0; i < size; i++) {
                        int mobId = dis.readInt();
                        MobReward mobReward = MOB_REWARDS.get(mobId);
                        if (mobReward == null) {
                            mobReward = new MobReward(mobId);
                            MOB_REWARDS.put(mobId, mobReward);
                        }
                        int itemId = dis.readInt();
                        String[] quantity = dis.readUTF().split("-");
                        String[] ratio = dis.readUTF().split("-");
                        int gender = dis.readInt();
                        String map = dis.readUTF();
                        String[] arrMap = map.replaceAll("[\\]\\[]", "").split(",");
                        int[] mapDrop = new int[arrMap.length];
                        for (int g = 0; g < mapDrop.length; g++) {
                            mapDrop[g] = Integer.parseInt(arrMap[g]);
                        }
                        ItemMobReward item = new ItemMobReward(itemId, mapDrop,
                                new int[]{Integer.parseInt(quantity[0]), Integer.parseInt(quantity[1])},
                                new int[]{Integer.parseInt(ratio[0]), Integer.parseInt(ratio[1])}, gender);
                        if (item.getTemp().type == 30) { // sao pha lê
                            item.setRatio(new int[]{20, Integer.parseInt(ratio[1])});
                        }
                        if (item.getTemp().type == 14) { //14 đá nâng cấp
                            item.setRatio(new int[]{20, Integer.parseInt(ratio[1])});
                        }
                        if (item.getTemp().type < 5) {
                            item.setRatio(new int[]{Integer.parseInt(ratio[0]), Integer.parseInt(ratio[1]) / 4 * 3});
                        }

//                        System.out.println(mobReward.getMobId());
//                        System.out.println(item.getTemp().name);
//                        System.out.println(item.getTemp().type);
//                        System.out.println(item.getRatio()[0] + "/" + item.getRatio()[1]);
//                        System.out.println(item.getQuantity()[0] + "/" + item.getQuantity()[1]);
                        if (item.getTemp().type == 9) { //vàng
                            mobReward.getGoldReward().add(item);
                        } else {
                            mobReward.getItemReward().add(item);
                        }
                        int sizeOption = dis.readInt();
                        for (int j = 0; j < sizeOption; j++) {
                            int optionId = dis.readInt();
                            String[] param = dis.readUTF().split("-");
                            String[] ratioOption = dis.readUTF().split("-");
                            ItemOptionMobReward option = new ItemOptionMobReward(optionId,
                                    new int[]{Integer.parseInt(param[0]), Integer.parseInt(param[1])},
                                    new int[]{Integer.parseInt(ratioOption[0]), Integer.parseInt(ratioOption[1])});
                            item.getOption().add(option);
                        }
                    }

                }
            }
            Logger.success("Load reward mob thành công (" + MOB_REWARDS.size() + ")\n");

            //load notify
            folder = new File("data/girlkun/notify");
            for (File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    StringBuffer notify = new StringBuffer(fileEntry.getName().substring(0, fileEntry.getName().lastIndexOf("."))).append("<>");
                    BufferedReader br = new BufferedReader(new FileReader(fileEntry));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        notify.append(line + "\n");
                    }
                    NOTIFY.add(notify.toString());
                }
            }
            Logger.success("Load notify thành công (" + NOTIFY.size() + ")\n");

            //load caption
            ps = con.prepareStatement("select * from caption");
            rs = ps.executeQuery();
            while (rs.next()) {
                CAPTIONS.add(rs.getString("name"));
            }
            Logger.success("Load caption thành công (" + CAPTIONS.size() + ")\n");

            //load image by name
            ps = con.prepareStatement("select name, n_frame from img_by_name");
            rs = ps.executeQuery();
            while (rs.next()) {
                IMAGES_BY_NAME.put(rs.getString("name"), rs.getByte("n_frame"));
            }
            Logger.success("Load images by name thành công (" + IMAGES_BY_NAME.size() + ")\n");

            //load mob template
            ps = con.prepareStatement("select * from mob_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                MobTemplate mobTemp = new MobTemplate();
                mobTemp.id = rs.getByte("id");
                mobTemp.type = rs.getByte("type");
                mobTemp.name = rs.getString("name");
                mobTemp.hp = (int) rs.getLong("hp");
                mobTemp.rangeMove = rs.getByte("range_move");
                mobTemp.speed = rs.getByte("speed");
                mobTemp.dartType = rs.getByte("dart_type");
                mobTemp.percentDame = rs.getByte("percent_dame");
                mobTemp.percentTiemNang = rs.getByte("percent_tiem_nang");
                MOB_TEMPLATES.add(mobTemp);
            }
            Logger.success("Load mob template thành công (" + MOB_TEMPLATES.size() + ")\n");

            //load npc template
            ps = con.prepareStatement("select * from npc_template");
            rs = ps.executeQuery();
            while (rs.next()) {
                NpcTemplate npcTemp = new NpcTemplate();
                npcTemp.id = rs.getByte("id");
                npcTemp.name = rs.getString("name");
                npcTemp.head = rs.getShort("head");
                npcTemp.body = rs.getShort("body");
                npcTemp.leg = rs.getShort("leg");
                npcTemp.avatar = rs.getInt("avatar");
                NPC_TEMPLATES.add(npcTemp);
            }
            Logger.success("Load npc template thành công (" + NPC_TEMPLATES.size() + ")\n");

            //load map template
            ps = con.prepareStatement("select count(id) from map_template");
            rs = ps.executeQuery();
            if (rs.first()) {
                int countRow = rs.getShort(1);
                MAP_TEMPLATES = new MapTemplate[countRow];
                ps = con.prepareStatement("select * from map_template");
                rs = ps.executeQuery();
                short i = 0;
                while (rs.next()) {
                    MapTemplate mapTemplate = new MapTemplate();
                    int mapId = rs.getInt("id");
                    String mapName = rs.getString("name");
                    mapTemplate.id = mapId;
                    mapTemplate.name = mapName;
                    //load data

//                    dataArray = (JSONArray) jv.parse(rs.getString("data"));
//                    mapTemplate.type = Byte.parseByte(String.valueOf(dataArray.get(0)));
//                    mapTemplate.planetId = Byte.parseByte(String.valueOf(dataArray.get(1)));
//                    mapTemplate.bgType = Byte.parseByte(String.valueOf(dataArray.get(2)));
//                    mapTemplate.tileId = Byte.parseByte(String.valueOf(dataArray.get(3)));
//                    mapTemplate.bgId = Byte.parseByte(String.valueOf(dataArray.get(4)));
//                    dataArray.clear();
                    mapTemplate.type = rs.getByte("type");
                    mapTemplate.planetId = rs.getByte("planet_id");
                    mapTemplate.bgType = rs.getByte("bg_type");
                    mapTemplate.tileId = rs.getByte("tile_id");
                    mapTemplate.bgId = rs.getByte("bg_id");
                    mapTemplate.zones = rs.getByte("zones");
                    mapTemplate.maxPlayerPerZone = rs.getByte("max_player");
                    //load waypoints
                    dataArray = (JSONArray) jv.parse(rs.getString("waypoints")
                            .replaceAll("\\[\"\\[", "[[")
                            .replaceAll("\\]\"\\]", "]]")
                            .replaceAll("\",\"", ",")
                    );
                    for (int j = 0; j < dataArray.size(); j++) {
                        WayPoint wp = new WayPoint();
                        JSONArray dtwp = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        wp.name = String.valueOf(dtwp.get(0));
                        wp.minX = Short.parseShort(String.valueOf(dtwp.get(1)));
                        wp.minY = Short.parseShort(String.valueOf(dtwp.get(2)));
                        wp.maxX = Short.parseShort(String.valueOf(dtwp.get(3)));
                        wp.maxY = Short.parseShort(String.valueOf(dtwp.get(4)));
                        wp.isEnter = Byte.parseByte(String.valueOf(dtwp.get(5))) == 1;
                        wp.isOffline = Byte.parseByte(String.valueOf(dtwp.get(6))) == 1;
                        wp.goMap = Short.parseShort(String.valueOf(dtwp.get(7)));
                        wp.goX = Short.parseShort(String.valueOf(dtwp.get(8)));
                        wp.goY = Short.parseShort(String.valueOf(dtwp.get(9)));
                        mapTemplate.wayPoints.add(wp);
                        dtwp.clear();
                    }
                    dataArray.clear();
                    //load mobs
                    dataArray = (JSONArray) jv.parse(rs.getString("mobs").replaceAll("\\\"", ""));
                    mapTemplate.mobTemp = new byte[dataArray.size()];
                    mapTemplate.mobLevel = new byte[dataArray.size()];
                    mapTemplate.mobHp = new int[dataArray.size()];
                    mapTemplate.mobX = new short[dataArray.size()];
                    mapTemplate.mobY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtm = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.mobTemp[j] = Byte.parseByte(String.valueOf(dtm.get(0)));
                        mapTemplate.mobLevel[j] = Byte.parseByte(String.valueOf(dtm.get(1)));
                        mapTemplate.mobHp[j] = (int) Long.parseLong(String.valueOf(dtm.get(2)));
                        mapTemplate.mobX[j] = Short.parseShort(String.valueOf(dtm.get(3)));
                        mapTemplate.mobY[j] = Short.parseShort(String.valueOf(dtm.get(4)));
                        dtm.clear();
                    }
                    dataArray.clear();
                    //load npcs
                    dataArray = (JSONArray) jv.parse(rs.getString("npcs").replaceAll("\\\"", ""));
                    mapTemplate.npcId = new byte[dataArray.size()];
                    mapTemplate.npcX = new short[dataArray.size()];
                    mapTemplate.npcY = new short[dataArray.size()];
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONArray dtn = (JSONArray) jv.parse(String.valueOf(dataArray.get(j)));
                        mapTemplate.npcId[j] = Byte.parseByte(String.valueOf(dtn.get(0)));
                        mapTemplate.npcX[j] = Short.parseShort(String.valueOf(dtn.get(1)));
                        mapTemplate.npcY[j] = Short.parseShort(String.valueOf(dtn.get(2)));
                        dtn.clear();
                    }
                    dataArray.clear();
                    MAP_TEMPLATES[i++] = mapTemplate;
                }
                Logger.success("Load map template thành công (" + MAP_TEMPLATES.length + ")\n");
                RUBY_REWARDS.add(Util.sendDo(861, 0, new ArrayList<>()));
            }          
                ps = con.prepareStatement("SELECT * FROM shop_ky_gui");
                rs = ps.executeQuery();
                while (rs.next()) {
                int i = rs.getInt("id");
                int idPl = rs.getInt("player_id");
                byte tab = rs.getByte("tab");
                short itemId = rs.getShort("item_id");
                int gold = rs.getInt("gold");
                int gem = rs.getInt("gem");
                int quantity = rs.getInt("quantity");
                byte isUp = rs.getByte("isUpTop");
                boolean isBuy = rs.getByte("isBuy") == 1;
                List<Item.ItemOption> op = new ArrayList<>();
                JSONArray jsa2 = (JSONArray) JSONValue.parse(rs.getString("itemOption"));
                for (int j = 0; j < jsa2.size(); ++j) {
                JSONObject jso2 = (JSONObject) jsa2.get(j);
                int idOptions = Integer.parseInt(jso2.get("id").toString());
                int param = Integer.parseInt(jso2.get("param").toString());
                op.add(new Item.ItemOption(idOptions, param));
                }
                ShopKyGuiManager.gI().listItem.add(new ItemKyGui(i,itemId,idPl,tab,gold,gem,quantity,isUp,op,isBuy));
            }
            Logger.success("Thông báo tải dữ liệu item ký gửi [" +ShopKyGuiManager.gI().listItem.size()+"]!\n" );
            
            
            
            
            ps = con.prepareStatement("select * from radar");
            rs = ps.executeQuery();
            while (rs.next()) {
                RadarCard rd = new RadarCard();
                rd.Id = rs.getShort("id");
                rd.IconId = rs.getShort("iconId");
                rd.Rank = rs.getByte("rank");
                rd.Max = rs.getByte("max");
                rd.Type = rs.getByte("type");
                rd.Template = rs.getShort("template");
                rd.Name = rs.getString("name");
                rd.Info = rs.getString("info");
                JSONArray arr = (JSONArray)JSONValue.parse(rs.getString("body"));
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject ob = (JSONObject)arr.get(i);
                    if (ob != null) {
                        rd.Head = Short.parseShort(ob.get("head").toString());
                        rd.Body = Short.parseShort(ob.get("body").toString());
                        rd.Leg = Short.parseShort(ob.get("leg").toString());
                        rd.Bag = Short.parseShort(ob.get("bag").toString());
                    }
                }
                rd.Options.clear();
                arr = (JSONArray)JSONValue.parse(rs.getString("options"));
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject ob = (JSONObject)arr.get(i);
                    if (ob != null) {
                        rd.Options.add(new OptionCard(Integer.parseInt(ob.get("id").toString()), Short.parseShort(ob.get("param").toString()), Byte.parseByte(ob.get("activeCard").toString())));
                    }
                }
                rd.Require = rs.getShort("require");
                rd.RequireLevel = rs.getShort("require_level");
                rd.AuraId = rs.getShort("aura_id");
                RadarService.gI().RADAR_TEMPLATE.add(rd);
            }
            Logger.success("Thông báo tải dữ liệu radar template thành công (" + RadarService.gI().RADAR_TEMPLATE.size() + ")\n");
            topSM = realTop(queryTopSM, con);
            Logger.success("Load top sm thành công (" + topSM.size() + ")\n");
            topvirus = realTop(queryTopvirus, con);
            Logger.success("Load top sm thành công (" + topvirus.size() + ")\n");
            gapthu = realTop(queryTopgapthu, con);
            Logger.success("Load top sm thành công (" + gapthu.size() + ")\n");
            rutthuong = realTop(queryToprutthuong, con);
            Logger.success("Load top sm thành công (" + rutthuong.size() + ")\n");
            sanboss = realTop(queryTopsanboss, con);
            Logger.success("Load top sm thành công (" + sanboss.size() + ")\n");
            topNV = realTop(queryTopNV, con);
            Logger.success("Load top nv thành công (" + topNV.size() + ")\n");
            topSB = realTop(querytopSB, con);
            Logger.success("Load top săn boss thành công (" + topSB.size() + ")\n");
            topPVP = realTop(queryTopPVP, con);
            Logger.success("Load top pvp thành công (" + topSB.size() + ")\n");
            topNHS = realTop(queryTopNHS, con);
            Logger.success("Load top NHS thành công (" + topSB.size() + ")\n");
            topKhiGas = realTop(queryTopKhiGas, con);
            Logger.success("Load top KhiGas thành công (" + topSB.size() + ")\n");
            topSK = realTop(querytopSK, con);
            Logger.success("Load top Sự kiện thành công (" + topSK.size() + ")\n");
            topSieuHang = realTopSieuHang(con);
            Logger.success("Load top Siêu hạng thành công (" + topSieuHang.size() + ")\n");
            topSD = realTop(queryTopSD, con);
            Logger.success("Load top Sức đánh thành công (" + topSD.size() + ")\n");
            topHP = realTop(queryTopHP, con);
            Logger.success("Load top HP thành công (" + topHP.size() + ")\n");
            topVND = realTop(queryTopVND, con);
            Logger.success("Load top Nạp thành công (" + topVND.size() + ")\n");
            Manager.timeRealTop = System.currentTimeMillis();
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        } catch (Exception e) {
            Logger.logException(Manager.class, e, "Lỗi load database");
            System.exit(0);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
            }
        }
        Logger.log(Logger.RED, "Tổng thời gian load database: " + (System.currentTimeMillis() - st) + "(ms)\n");
    }

    public static List<TOP> realTop(String query, Connection con) {
        List<TOP> tops = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TOP top = TOP.builder().id_player(rs.getInt("id")).build();
                switch (query) {
                    case queryTopSM:
                        top.setInfo2("Bảng Xếp Hạng Sư Phụ");
                        top.setInfo1(Util.format(rs.getLong("sm")) + " Sức Mạnh");
                        break;
                    case queryTopvirus:
                        top.setInfo1(Util.powerToString(rs.getLong("sm")) + " Điểm săn virus");
                        top.setInfo2(Util.format(rs.getLong("sm")) + " Điểm săn virus");
                        break;    
                    case queryTopgapthu:
                        top.setInfo2("Bảng Xếp Hạng Gấp Thú");
                        top.setInfo1(Util.format(rs.getLong("diemgapthu")) + " Điểm");
                        break; 
                    case queryToprutthuong:
                        top.setInfo2("Bảng Xếp Hạng Rút Thưởng");
                        top.setInfo1(Util.format(rs.getLong("diemrutthuong")) + " Điểm");
                        break;     
                    case queryTopsanboss:
                        top.setInfo2("Bảng Xếp Hạng Săn Boss");
                        top.setInfo1(Util.format(rs.getLong("diemsanboss")) + " Điểm");
                        break;    
                    case queryTopNV:
                        top.setInfo2("Bảng Xếp Hạng Nhiệm Vụ");
                        top.setInfo1(Util.format(rs.getLong("sm")) + " Nhiệm vụ");
                        break;
                    case queryTopVND:
                        top.setInfo2("Bảng Xếp Hạng TỔNG NẠP");
                        top.setInfo1(Util.format(rs.getInt("vnd")) + " VNĐ");
                        break;
                    case queryTopHP:
                        top.setInfo1(Util.powerToString(rs.getInt("hp")) + " HP");
                        top.setInfo2(Util.format(rs.getInt("hp")) + " HP");
                        break;
                    
                    case querytopSK:
                        top.setInfo1(rs.getInt("event") + " điểm");
                        top.setInfo2(rs.getInt("event") + " điểm");
                        break;
                    case queryTopPVP:
                        top.setInfo1(rs.getInt("pointPvp") + " điểm");
                        top.setInfo2(rs.getInt("pointPvp") + " điểm");
                        break;
                    case queryTopNHS:
                        top.setInfo1(rs.getInt("NguHanhSonPoint") + " điểm");
                        top.setInfo2(rs.getInt("NguHanhSonPoint") + " điểm");
                        break;
                    case queryTopKhiGas:
                        top.setInfo1(rs.getInt("khi_gas") + " điểm Khí Gas");
                        top.setInfo2(rs.getInt("khi_gas") + " điểm Khí Gas");
                        break;
                      case queryTopSD:
                        top.setInfo1(Util.powerToString(rs.getInt("sd")) + " Sức đánh");
                        top.setInfo2(Util.format(rs.getInt("sd")) + " Sức đánh");
                        break;     
                }
                tops.add(top);
            }
        } catch (Exception e) {

        }
        return tops;
    }

    public void loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("data/girlkun/girlkun.properties"));
        Object value = null;
        //###Config sv
        if ((value = properties.get("server.girlkun.port")) != null) {
            ServerManager.PORT = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.name")) != null) {
            ServerManager.NAME = String.valueOf(value);
        }
        if ((value = properties.get("server.girlkun.sv")) != null) {
            SERVER = Byte.parseByte(String.valueOf(value));
        }
        String linkServer = "";
        for (int i = 1; i <= 10; i++) {
            value = properties.get("server.girlkun.sv" + i);
            if (value != null) {
                linkServer += String.valueOf(value) + ":0,";
            }
        }
        DataGame.LINK_IP_PORT = linkServer.substring(0, linkServer.length() - 1);
        if ((value = properties.get("server.girlkun.waitlogin")) != null) {
            SECOND_WAIT_LOGIN = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.maxperip")) != null) {
            MAX_PER_IP = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.maxplayer")) != null) {
            MAX_PLAYER = Integer.parseInt(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.expserver")) != null) {
            RATE_EXP_SERVER = Byte.parseByte(String.valueOf(value));
        }
        if ((value = properties.get("server.girlkun.local")) != null) {
            LOCAL = String.valueOf(value).toLowerCase().equals("true");
        }
    }

    /**
     * @param tileTypeFocus tile type: top, bot, left, right...
     * @return [tileMapId][tileType]
     */
    private int[][] readTileIndexTileType(int tileTypeFocus) {
        int[][] tileIndexTileType = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_set_info"));
            int numTileMap = dis.readByte();
            tileIndexTileType = new int[numTileMap][];
            for (int i = 0; i < numTileMap; i++) {
                int numTileOfMap = dis.readByte();
                for (int j = 0; j < numTileOfMap; j++) {
                    int tileType = dis.readInt();
                    int numIndex = dis.readByte();
                    if (tileType == tileTypeFocus) {
                        tileIndexTileType[i] = new int[numIndex];
                    }
                    for (int k = 0; k < numIndex; k++) {
                        int typeIndex = dis.readByte();
                        if (tileType == tileTypeFocus) {
                            tileIndexTileType[i][k] = typeIndex;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.logException(MapService.class, e);
        }
        return tileIndexTileType;
    }

    /**
     * @param mapId mapId
     * @return tile map for paint
     */
    private int[][] readTileMap(int mapId) {
        int[][] tileMap = null;
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream("data/girlkun/map/tile_map_data/" + mapId));
            int w = dis.readByte();
            int h = dis.readByte();
            tileMap = new int[h][w];
            for (int i = 0; i < tileMap.length; i++) {
                for (int j = 0; j < tileMap[i].length; j++) {
                    tileMap[i][j] = dis.readByte();
                }
            }
            dis.close();
        } catch (Exception e) {
        }
        return tileMap;
    }

    //service*******************************************************************
    public static Clan getClanById(int id) throws Exception {
        for (Clan clan : CLANS) {
            if (clan.id == id) {
                return clan;
            }
        }
        throw new Exception("Không tìm thấy clan id: " + id);
    }

    public static void addClan(Clan clan) {
        CLANS.add(clan);
    }

    public static int getNumClan() {
        return CLANS.size();

    }

    public static MobTemplate getMobTemplateByTemp(int mobTempId) {
        for (MobTemplate mobTemp : MOB_TEMPLATES) {
            if (mobTemp.id == mobTempId) {
                return mobTemp;
            }
        }
        return null;
    }

    public static byte getNFrameImageByName(String name) {
        Object n = IMAGES_BY_NAME.get(name);
        if (n != null) {
            return Byte.parseByte(String.valueOf(n));
        } else {
            return 0;
        }
    }

}
