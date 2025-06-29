package tutien.models.player;

import tutien.card.Card;
import tutien.card.OptionCard;
import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstRatio;
import tutien.models.intrinsic.Intrinsic;
import tutien.models.item.Item;
import tutien.models.skill.Skill;
import barcoll.server.Manager;
import barcoll.services.EffectSkillService;
import barcoll.services.InventoryServiceNew;
import barcoll.services.ItemService;
import barcoll.services.MapService;
import barcoll.services.PlayerService;
import barcoll.services.Service;
import barcoll.services.TaskService;
import tutien.utils.Logger;
import tutien.utils.SkillUtil;
import tutien.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class NPoint {

    public static final byte MAX_LIMIT = 9;

    private Player player;
    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
    }

    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;
    public short test;

    public long hp, hpMax, hpg;
    public long mp, mpMax, mpg;
    public long dame, dameg;
    public int def, defg;
    public int crit, critg;
    public byte speed = 8;

    public boolean teleport;
    
    public boolean isDraburaFrost; //Cải trang Dracula Frost
    public boolean isBillbingo; //Cải trang Dracula Frost
    public boolean isDrabura; //Cải trang Dracula Frost
    public boolean isThoDaiCa; //Cải trang Dracula Frost

    public boolean khangTDHS;

    /**
     * Chỉ số cộng thêm
     */
    public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public int hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;

    public short multicationChuong;
    public long lastTimeMultiChuong;
    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        if (this.player.pet != null) {
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    private void setPointWhenWearClothes() {
        resetPoint();
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            tlHutMp += RewardBlackBall.R2S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            tlDameAttMob.add(RewardBlackBall.R4S_2);
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R5S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            tlPST += RewardBlackBall.R6S_1;
//            tlNeDon += RewardBlackBall.R6S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            tlHpHoi += RewardBlackBall.R7S_1;
            tlHutHp += RewardBlackBall.R7S_2;
        }

        this.player.setClothes.worldcup = 0;
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                switch (item.template.id) {
                    case 966:
                    case 982:
                    case 983:
                    case 883:
                    case 904:
                        player.setClothes.worldcup++;
                }
                if (item.template.id >= 592 && item.template.id <= 594) {
                    teleport = true;
                }
                Card card = player.Cards.stream().filter(r -> r != null && r.Used == 1).findFirst().orElse(null);
                if (card != null) {
                    for (OptionCard io : card.Options) {
                        if (io.active == card.Level || (card.Level == -1 && io.active == 0)) {
                            switch (io.id) {
                                case 0: //Tấn công +#
                                    this.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.hpAdd += io.param * 1000;
                                    this.mpAdd += io.param * 1000;
                                    break;
                                case 3:// fake
                                    this.voHieuChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.critAdd += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.mpAdd += io.param * 1000;
                                    break;
                                case 27: //+# HP/30s
                                    this.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.hpAdd += io.param;
                                    this.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.tlPST += io.param;
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.tlHutHpMob += io.param;
                                    break;
                                case 147: //+#% sức đánh
                                    this.tlDame.add(io.param);
                                    break;
                                case 159: // x# chưởng mỗi phút
                                    this.multicationChuong += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                    //    case 211: //test
                    //        this.test += io.param;
                    //        break;
                                case 221: //Sức sức mạnh+#%
                                    this.tlDame.add(io.param);
                                    break;
                                case 222: //+#% sức đánh chí mạng sức mạnh
                                    this.tlDameCrit.add(io.param);
                                    break;    
                                case 224: //HP+#% sức mạnh
                                    this.tlHp.add(io.param);
                                    break;
                                case 220: //KI +#% sức mạnh
                                    this.tlMp.add(io.param);
                                    break;      
                            }
                        }
                    }
                }
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 0: //Tấn công +#
                            this.dameAdd += io.param;
                            break;
                        case 2: //HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;
                        case 3:// fake
                            this.voHieuChuong += io.param;
                            break;
                        case 5: //+#% sức đánh chí mạng
                            this.tlDameCrit.add(io.param);
                            break;
                        case 6: //HP+#
                            this.hpAdd += io.param;
                            break;
                        case 7: //KI+#
                            this.mpAdd += io.param;
                            break;
                        case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 14: //Chí mạng+#%
                            this.critAdd += io.param;
                            break;
                        case 19: //Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add(io.param);
                            break;
                        case 22: //HP+#K
                            this.hpAdd += io.param * 1000;
                            break;
                        case 23: //MP+#K
                            this.mpAdd += io.param * 1000;
                            break;
                        case 27: //+# HP/30s
                            this.hpHoiAdd += io.param;
                            break;
                        case 28: //+# KI/30s
                            this.mpHoiAdd += io.param;
                            break;
                        case 33: //dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 47: //Giáp+#
                            this.defAdd += io.param;
                            break;
                        case 48: //HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;
                        case 49: //Tấn công+#%
                        case 50: //Sức đánh+#%
                            this.tlDame.add(io.param);
                            break;
                        case 77: //HP+#%
                            this.tlHp.add(io.param);
                            break;
                        case 80: //HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;
                        case 81: //MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;
                        case 88: //Cộng #% exp khi đánh quái
                            this.tlTNSM.add(io.param);
                            break;
                        case 94: //Giáp #%
                            this.tlDef.add(io.param);
                            break;
                        case 95: //Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;
                        case 96: //Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;
                        case 97: //Phản #% sát thương
                            this.tlPST += io.param;
                            break;
                        case 100: //+#% vàng từ quái
                            this.tlGold += io.param;
                            break;
                        case 101: //+#% TN,SM
                            this.tlTNSM.add(io.param);
                            break;
                        case 103: //KI +#%
                            this.tlMp.add(io.param);
                            break;
                        case 104: //Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;
                        case 105: //Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;
                        case 106: //Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 108: //#% Né đòn
                            this.tlNeDon += io.param;// đối nghịch 
                        case 109: //Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;
                           
                        case 116: //Kháng thái dương hạ san
                            this.khangTDHS = true;
                            break;
                        case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                            this.tlSDDep.add(io.param);
                            break;
                        case 147: //+#% sức đánh
                            this.tlDame.add(io.param);
                            break;
                        case 75: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                            this.tlSubSD += 50;
                            this.tlTNSM.add(io.param);
                            this.tlGold += io.param;
                            break;
                        case 162: //Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;
                        case 173: //Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;
                    //     case 211: //test
                    //        this.test += io.param;
                    //        break; 
                       case 26: //Dracula hóa đá
                            this.isDrabura = true;
                            break;
                        case 212: // Dracula Frost
                            this.isDraburaFrost = true;
                            break;
                        case 213: // Dracula Frost
                            this.isBillbingo = true;
                            break;
                                case 221: //Sức sức mạnh+#%
                                    this.tlDame.add(io.param);
                                    break;
                                case 222: //+#% sức đánh chí mạng sức mạnh
                                    this.tlDameCrit.add(io.param);
                                    break;    
                                case 224: //HP+#% sức mạnh
                                    this.tlHp.add(io.param);
                                    break;
                                case 220: //KI +#% sức mạnh
                                    this.tlMp.add(io.param);
                                    break;     
                    }
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1388) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2074) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2075) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        } else if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 2117) {
                    for (Item.ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }

        setDameTrainArmor();
        setBasePoint();
    }
    
    private void setDameTrainArmor() {
        if (!this.player.isPet && !this.player.isBoss) {
            if (this.player.inventory.itemsBody.size() < 7) {
                return;
            }
            try {
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem()) {
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {
                    if (this.wornTrainArmor) {
                        this.wearingTrainArmor = false;
                        for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {
                            if (io.optionTemplate.id == 9 && io.param > 0) {
                                this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
            }
        }
    }

    public void setBasePoint() {
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
    }

    private void setNeDon() {

    }

    private void setHpHoi() {
        this.hpHoi = Util.TamkjllGH(this.hpMax / 100);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);
        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {
        this.mpHoi = Util.TamkjllGH(this.mpMax / 100);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);
        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

   private void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.hpMax += this.hpMax * this.player.TamkjllHpKiGiaptutien(Util.TamkjllGH(this.player.Tamkjlltutien[1]))
                    / 100d;
        }
        //đồ
        for (Integer tl : this.tlHp) {
            this.hpMax += ((long)this.hpMax * tl / 100);
        }
        //set nappa
        if (this.player.setClothes.nappa == 5) {
            this.hpMax += ((long)this.hpMax * 100 / 100);
        }
        //set nappa
       // if (this.player.setClothes.kidbu == 5) {
       //     this.hpMax += (this.hpMax * 100 / 100);
      // / }
        if (player.capCS > 0) { // cấp cs
            if (player.capCS <= 10) {
                hpMax += (10000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {
                hpMax +=(15000) * (player.capCS);
            }
            if (player.capCS > 20) {
                hpMax +=(17000) * (player.capCS);
            }
        }
        //set tinh ấn
        if (this.player.setClothes.tinhan == 5) {
            this.hpMax += ((long) this.hpMax * 30 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.hpMax += ((long)this.hpMax * 10 / 100);
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.hpMax += ((long)this.hpMax * RewardBlackBall.R1S_1 / 100);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.hpMax += (this.hpMax * percent / 100);
            }
        }
        
        if (this.player.itemTime != null && this.player.itemTime.is1829) {
           this.hpMax *=1.05;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1830) {
            this.hpMax *=1.10;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1831) {
            this.hpMax *=1.17;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1832) {
            this.hpMax *=1.25;
        }
        
        // chỉ số bông tai 
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.hpMax += ((long)this.hpMax * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.hpMax += ((long)this.hpMax * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long)this.hpMax * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += ((long)this.hpMax * 20 / 100);
        }
        //pet berus
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.hpMax +=((long)this.hpMax * 40 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 2// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.hpMax += ((long)this.hpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.hpMax += ((long)this.hpMax * 30 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long)this.hpMax * 30 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += ((long)this.hpMax * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += ((long)this.hpMax * 40 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.hpMax += ((long)this.hpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.hpMax += ((long)this.hpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long)this.hpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += ((long)this.hpMax * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.hpMax += ((long)this.hpMax * 60 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.hpMax += ((long)this.hpMax * 60 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.hpMax += ((long)this.hpMax * 60 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.hpMax += ((long)this.hpMax * 60 / 100);//chi so hp
        }
        //đuôi khỉ
        if (!this.player.isPet && this.player.itemTime.isDuoikhi
                || this.player.isPet && ((Pet) this.player).master.itemTime.isDuoikhi) {
            this.hpMax += ((long) this.hpMax * 10 / 100);
        }
    //    if (!this.player.isPet && this.player.itemTime.isUseMayDo2
    //            || this.player.isPet && ((Pet) this.player).master.itemTime.isUseMayDo2) {
    //        this.hpMax += ((double) this.hpMax * 30 / 100);
    //    }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //+hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //btc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.hpMax *= 1.1;
        }
        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.hpMax *= 1.13;
        }
        //btc4
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.hpMax *= 1.6;
        }
        //btc5
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.hpMax *= 1.2;
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += ((long)this.hpMax * this.player.effectSkill.tiLeHPHuytSao / 100L);

            }
        }
        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax *= 2;
        }
        //bí ngô
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
        }
        // item sieu cấp
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseBoHuyet3) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.hpMax += ((long) this.hpMax * 10 / 100);
            player.diemnaubanhtrung += 2;
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.hpMax += ((long) this.hpMax * 20 / 100);
            player.diemnaubanhtrung += 2;
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.hpMax += ((long) this.hpMax * 30 / 100);
            player.diemnaubanhtrung += 5;
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.hpMax += ((long) this.hpMax * 50 / 100);
        }
        // không lạnh
        
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax /= 2;
        }
        // cold Thánh địa
        if (this.player.zone != null && MapService.gI().isMapCold1(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax /= 2;
        }
        //mèo mun
        if (this.player.effectFlagBag.useMeoMun) {
            this.hpMax += ((long)this.hpMax * 15 / 100);
        }
           if (this.player.isPl()) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.hpMax += this.hpMax * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
          
//            this.hpMax += 1000000d * this.player.playerTask.taskMain.id;
//            this.hpMax += player.TamkjllDauLaDaiLuc[0] * 0d;
//            this.hpMax += player.TamkjllDauLaDaiLuc[1] * 1000000d;
//            this.hpMax += player.TamkjllDauLaDaiLuc[2] * 10000000d;
//            this.hpMax += player.TamkjllDauLaDaiLuc[3] * 100000000d;
//            this.hpMax += player.TamkjllDauLaDaiLuc[4] * 1000000000d;
//            this.hpMax += player.TamkjllDauLaDaiLuc[5] * 10000000000d;
//            this.hpMax += player.TamkjllDauLaDaiLuc[6] * 100000000000d;
//            if (this.player.TamkjllDauLaDaiLuc[17] == 1) {
//                this.hpMax += player.TamkjllDauLaDaiLuc[18] * 1000_000_000d;
//            }
    }
    }    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

     private void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.mpMax += this.mpMax * this.player.TamkjllHpKiGiaptutien(Util.TamkjllGH(this.player.Tamkjlltutien[1]))
                    / 100d;
        } //luudeptrai
        //đồ
        for (Integer tl : this.tlMp) {
            this.mpMax += ((long)this.mpMax * tl / 100);
        }
        if (this.player.setClothes.picolo == 5) {
            this.mpMax *= 3;
        }
        if (player.capCS > 0) {
            if (player.capCS <= 10) {
                mpMax += (13000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {
                mpMax +=(15000) * (player.capCS);
            }
            if (player.capCS > 20) {
                mpMax +=(17500) * (player.capCS);
            }
        }
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += ((long)this.mpMax * RewardBlackBall.R3S_1 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.mpMax += ((long)this.mpMax * 10 / 100);
        }
        //set nhật ấn
        if (this.player.setClothes.nhatan == 5) {
            this.mpMax += ((long) this.mpMax * 30 / 100);
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.mpMax += ((long)this.mpMax * 10 / 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.is1829) {
           this.mpMax *=1.05;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1830) {
            this.mpMax *=1.10;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1831) {
            this.mpMax *=1.17;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1832) {
            this.mpMax *=1.25;
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.mpMax += ((long)this.mpMax * 10 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long)this.mpMax * 10 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += ((long)this.mpMax * 10 / 100);
        }
        //pet br
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.mpMax += ((long)this.mpMax * 20 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.mpMax += ((long)this.mpMax * 20 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long)this.mpMax * 20 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += ((long)this.mpMax * 20 / 100);//MP berus
        } //
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.mpMax += ((long)this.mpMax * 30 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.mpMax += ((long)this.mpMax * 30 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long)this.mpMax * 30 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += ((long)this.mpMax * 30 / 100);//MP berus
        }  //
        if (this.player.isPet && ((Pet) this.player).typePet == 5
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.mpMax += ((long)this.mpMax * 40 / 100);//MP berus
        }
        //đuôi khỉ
        if (!this.player.isPet && this.player.itemTime.isDuoikhi
                || this.player.isPet && ((Pet) this.player).master.itemTime.isDuoikhi) {
            this.mpMax += ((long) this.mpMax * 10 / 100);
        }
    //    if (!this.player.isPet && this.player.itemTime.isUseMayDo2
    //            || this.player.isPet && ((Pet) this.player).master.itemTime.isUseMayDo2) {
    //        this.mpMax += ((double) this.mpMax * 30 / 100);
    //    }
        if (this.player.isPet && ((Pet) this.player).typePet == 5
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.mpMax += ((long)this.mpMax * 40 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long)this.mpMax * 40 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += ((long)this.mpMax * 40 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.mpMax += ((long)this.mpMax * 50 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.mpMax += ((long)this.mpMax * 50 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long)this.mpMax * 50 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += ((long)this.mpMax * 50 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.mpMax += ((long)this.mpMax * 60 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.mpMax += ((long)this.mpMax * 60 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.mpMax += ((long)this.mpMax * 60 / 100);//MP berus
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.mpMax += ((long)this.mpMax * 60 / 100);//MP berus
        }
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        
        
        //BTc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.mpMax *= 1.1;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.mpMax *= 1.13;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.mpMax *= 1.16;
        }
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.mpMax *= 1.2;
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
            this.mpMax *= 2;
        }
        //bí ngô
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseBoKhi3) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.mpMax += ((long) this.mpMax * 10 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.mpMax += ((long) this.mpMax * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.mpMax += ((long) this.mpMax * 30 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.mpMax += ((long) this.mpMax * 50 / 100);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
        //xiên cá
        if (this.player.effectFlagBag.useXienCa) {
            this.mpMax += ((long)this.mpMax * 15 / 100);
        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.dame += this.dame * this.player.TamkjllDametutien(Util.TamkjllGH(this.player.Tamkjlltutien[1])) / 100d;
        }
        //đồ
        for (Integer tl : this.tlDame) {
            this.dame += ((long)this.dame * tl / 100);
        }
        for (Integer tl : this.tlSDDep) {
            this.dame += ((long)this.dame * tl / 100);
        }
        
        //set kidbu
        if (this.player.setClothes.kidbu == 5 ) {
            this.dame += ((long)this.dame * 30 / 100);
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.dame += ((long)this.dame * 10 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.dame += ((long)this.dame * 10 / 100);
        }if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long)this.dame * 10 / 100);
        }if (this.player.isPet && ((Pet) this.player).typePet == 1
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += ((long)this.dame * 10 / 100);
        }
        if (player.capCS > 0) {
            if (player.capCS <= 10) {
                dame += (5000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {
                dame +=(7000) * (player.capCS);
            }
            if (player.capCS > 20) {
                dame +=(9000) * (player.capCS);
            }
        }
        if (this.player.itemTime != null && this.player.itemTime.is1829) {
            this.dame *=1.05;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1830) {
            this.dame *=1.10;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1831) {
            this.dame *=1.17;
        }
        if (this.player.itemTime != null && this.player.itemTime.is1832) {
            this.dame *=1.25;
        }
        //pet mabư
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.dame += ((long)this.dame * 20 / 100);
        }
        //đuôi khỉ
        if (!this.player.isPet && this.player.itemTime.isDuoikhi
                || this.player.isPet && ((Pet) this.player).master.itemTime.isDuoikhi) {
            this.dame += ((long) this.dame * 15 / 100);
        }
    //    if (!this.player.isPet && this.player.itemTime.isUseMayDo2
    //            || this.player.isPet && ((Pet) this.player).master.itemTime.isUseMayDo2) {
    //        this.dame += ((double) this.dame * 20 / 100);
    //    }
        //set nguyệt ấn
        if (this.player.setClothes.nguyetan == 5) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.dame += ((long)this.dame * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long)this.dame * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 2
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += ((long)this.dame * 20 / 100);
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.dame += ((long)this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.dame += ((long)this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += (this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 4// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += ((long)this.dame * 30 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.dame += ((long)this.dame * 40 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.dame += ((long)this.dame * 40 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long)this.dame * 40 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 5// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += ((long)this.dame * 40 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.dame += ((long)this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.dame += ((long)this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long)this.dame * 50 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 6// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += ((long)this.dame * 50 / 100);//chi so hp
        }
        if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2)) {
            this.dame += ((long)this.dame * 60 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3)) {
            this.dame += ((long)this.dame * 60 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4)) {
            this.dame += ((long)this.dame * 60 / 100);//chi so hp
        }if (this.player.isPet && ((Pet) this.player).typePet == 7// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            this.dame += ((long)this.dame * 60 / 100);//chi so dame
        }
        
        
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.dame += this.player.pet.nPoint.dame;
        }
        //btc2
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            this.dame *= 1.10;
        }
        //btc3
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            this.dame *= 1.12;
        }
        //btc4
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            this.dame *= 1.14;
        }
        //btc5
        if (this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            this.dame *= 1.16;
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
            this.dame *= 2;
        }
        //bí ngô
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.dame += ((double) this.dame * 20 / 100);
        }
        
        //thức ăn
        if (!this.player.isPet && this.player.itemTimesieucap.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isEatMeal) {
            this.dame += ((long) this.dame * 10 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseCuongNo3) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.dame += ((long) this.dame * 10 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.dame += ((long) this.dame * 20 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.dame += ((long) this.dame * 30 / 100);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.dame += ((long) this.dame * 50 / 100);
        }
        //giảm dame
        this.dame -= (this.dame * tlSubSD / 100);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
        // map thánh địa
        if (this.player.zone != null && MapService.gI().isMapCold1(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += ((long)this.dame * RewardBlackBall.R1S_2 / 100);
        }
        //set worldcup
        if (this.player.setClothes.worldcup == 2) {
            this.dame += ((long)this.dame * 10 / 100);
            this.tlDameCrit.add(20);
        }
        //phóng heo
        if (this.player.effectFlagBag.usePhongHeo) {
            this.dame += ((long)this.dame * 15 / 100);
        }
         if (this.player.isPl()) {
            if (this.player.TamkjllDauLaDaiLuc[9] == 1) {
                this.dame += this.dame * player.TamkjllDauLaDaiLuc[10] / 100d;
            }
//            this.dame += this.player.LbTamkjll * 700000d;
//            this.dame += this.dame * this.player.CapTamkjll / 100d;
//            this.dame += this.dame * (this.player.TamkjllCS * 10d) / 100d;
//            if (player.CapTamkjll >= 100) {
//                this.dame += this.dame * (this.player.TamkjllCapPb * 10d) / 100d;
//            }
            if (this.player.TamkjllDauLaDaiLuc[15] == 1) {
                this.dame += this.player.TamkjllDauLaDaiLuc[16] * 250000000d;
            }
//            this.dame += 300000d * this.player.playerTask.taskMain.id;
//            this.dame += player.TamkjllDauLaDaiLuc[0] * 10000d;
//            this.dame += player.TamkjllDauLaDaiLuc[1] * 100000d;
//            this.dame += player.TamkjllDauLaDaiLuc[2] * 1000000d;
//            this.dame += player.TamkjllDauLaDaiLuc[3] * 10000000d;
//            this.dame += player.TamkjllDauLaDaiLuc[4] * 100000000d;
//            this.dame += player.TamkjllDauLaDaiLuc[5] * 1000000000d;
//            this.dame += player.TamkjllDauLaDaiLuc[6] * 10000000000d;
//            if (this.player.tamkjllpet != null && this.player.tamkjllpet.getStatus() != Tamkjll_Pet.GOHOME
//                    && (this.player.TamkjllPetGiong == 5 || this.player.TamkjllPetGiong == 9
//                            || this.player.TamkjllPetGiong == 10)) {
//                this.dame += this.dame * ((this.player.TamkjlllevelPet + 1) * 3) / 100;
            }
        
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                this.dame += ((long)this.dame * percent / 100);
            }
        }
    }

     private void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        if (this.player.isPl() && this.player.Tamkjlltutien[2] >= 1) {
            this.def += this.def * this.player.TamkjllHpKiGiaptutien(Util.TamkjllGH(this.player.Tamkjlltutien[1]))
                    / 100d;
        }
        //đồ
        for (Integer tl : this.tlDef) {
            this.def += ((long)this.def * tl / 100);
        }
        //ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.def += ((long)this.def * RewardBlackBall.R2S_2 / 100);
        }
    }

    private void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.crit += RewardBlackBall.R3S_2;
        }
        //biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
    }

    private void resetPoint() {
        this.voHieuChuong = 0;
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.isDrabura = false; //Cải trang Dracula
        this.isDraburaFrost = false; //Cải trang Dracula Frost
        this.isBillbingo = false; //Cải trang Dracula Frost
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.teleport = false;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
    }

    public void addHp(long hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp(float mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void setHp(long hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = hp;
        }
    }

    public void setMp(long mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit();
        long dameAttack = this.dame;
        percentDameIntrinsic = 0;
        long percentDameSkill = 0;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        long percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        
        //ngọc skill
        Item nrb1s = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1822);        
        int star1=0;
        if (nrb1s !=null){                   
            for (Item.ItemOption io : nrb1s.itemOptions) {
                if (io.optionTemplate.id == 230) {
                    star1 = io.param;                            
                }
            }
        }
        Item nrb2s = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1823);        
        int star2=0;
        if (nrb2s !=null){                   
            for (Item.ItemOption io : nrb2s.itemOptions) {
                if (io.optionTemplate.id == 231) {
                    star1 = io.param;                            
                }
            }
        }
        Item nrb3s = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1824);        
        int star3=0;
        if (nrb3s !=null){                   
            for (Item.ItemOption io : nrb3s.itemOptions) {
                if (io.optionTemplate.id == 232) {
                    star3 = io.param;                            
                }
            }
        }
        Item nrb4s = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1825);        
        int star4=0;
        if (nrb4s !=null){                   
            for (Item.ItemOption io : nrb4s.itemOptions) {
                if (io.optionTemplate.id == 233) {
                    star4 = io.param;                            
                }
            }
        }
        Item nrb5s = InventoryServiceNew.gI().findItem(player.inventory.itemsBag, 1826);        
        int star5=0;
        if (nrb5s !=null){                   
            for (Item.ItemOption io : nrb5s.itemOptions) {
                if (io.optionTemplate.id == 234) {
                    star5 = io.param;                            
                }
            }
        }
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
//                if (this.player.itemTime != null && this.player.itemTime.is1822) {
//                    if (Util.isTrue(10,100)){
//                        dameAttack += dameAttack*50/100;
//                    }
//                }               
                if (nrb1s !=null){                                      
                    dameAttack += dameAttack*star1/100;
                }
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame = 100;
                }
                if (player.isPl() || player.isPet) {
                    int dameX42 = player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    if (dameX42 > 0) {
                        if (Util.canDoWithTime(player.lastTimeUseOption, 60000)) {
                            dameAttack *= dameX42;
                            player.lastTimeUseOption = System.currentTimeMillis();
                        }
                    }
                }
                if (nrb2s !=null){                                      
                    dameAttack += dameAttack*star2/100;
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                        percentXDame = 100;
                    }
                
//                if ( player.inventory.haveOption(player.inventory.itemsBody, 5, 222)) {
//                    if (Util.canDoWithTime(player.lastTimeUseOption, 1)) {
//                        dameAttack += (dameAttack * player.inventory.getParam(player.inventory.itemsBody.get(5), 222) / 100);
//                        player.lastTimeUseOption = System.currentTimeMillis();
//                    }
//                }
                if (nrb1s !=null){                                      
                    dameAttack += dameAttack*star1/100;
                }
                break;    
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (player.isPl() || player.isPet) {
                    int dameX41 = player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    if (dameX41 > 0) {
                        if (Util.canDoWithTime(player.lastTimeUseOption, 60000)) {
                            dameAttack *= dameX41;
                            player.lastTimeUseOption = System.currentTimeMillis();
                        }
                    }
                }
                if (nrb2s !=null){                                      
                    dameAttack += dameAttack*star2/100;
                }
                break;
            case Skill.TU_SAT:
                if ( player.inventory.haveOption(player.inventory.itemsBody, 5, 198)) {
                    if (Util.canDoWithTime(player.lastTimeUseOption, 1)) {
                        dameAttack += (dameAttack * player.inventory.getParam(player.inventory.itemsBody.get(5), 198) / 100);
                        player.lastTimeUseOption = System.currentTimeMillis();
                    }
                }
                if (nrb5s !=null){                                      
                    dameAttack += dameAttack*star5/100;
                }
                break;    
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (nrb1s !=null){                                      
                    dameAttack += dameAttack*star1/100;
                }
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (player.isPl() || player.isPet) {
                    int dameX4 = player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    if (dameX4 > 0) {
                        if (Util.canDoWithTime(player.lastTimeUseOption, 60000)) {
                            dameAttack *= dameX4;
                            player.lastTimeUseOption = System.currentTimeMillis();
                        }
                    }
                }
                percentDameSkill = skillSelect.damage;
                if (nrb2s !=null){                                      
                    dameAttack += dameAttack*star2/100;
                }
                break;
            case Skill.KAIOKEN:
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.setkaioken == 5) {
                    isCrit = true;
                }
                if (player.inventory.haveOption(player.inventory.itemsBody, 5, 229)) {
                    if (Util.canDoWithTime(player.lastTimeUseOption, 1)) {
//                            dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 195);
                        dameAttack += (dameAttack * player.inventory.getParam(player.inventory.itemsBody.get(5), 229) / 100);
                        player.lastTimeUseOption = System.currentTimeMillis();
                    }
                }
               if (nrb4s !=null){                                      
                    dameAttack += dameAttack*star4/100;
                }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 100;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.Tamkjllnext(dameAttack - (dameAttack * 5 / 100),
                        dameAttack + (dameAttack * 5 / 100));
                return dameAttack;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                long dameSkill = this.mpMax * percentDameSkill / 100;
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long dame = this.dame * 40;
                if (this.player.setClothes.kirin == 5) {
                    dame *= 2;
                }
                dame = dame + (Util.nextInt(-5, 5) * dame / 100);
                return dame;
        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = dameAttack * percentDameSkill / 100;
        }
        dameAttack += (dameAttack * percentDameIntrinsic / 100);
        dameAttack += (dameAttack * dameAfter / 100);

        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += (dameAttack * tl / 100);
            }
        }
        if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx2) {
            dameAttack *= 2;
        }
        if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx5) {
            dameAttack *= 5;
        }
        if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx7) {
            dameAttack *= 7;
        }
        if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx10) {
            dameAttack *= 10;
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += (dameAttack * tl / 100);
            }
        }
        dameAttack += dameAttack * percentXDame / 100;
        dameAttack = Util.Tamkjllnext(dameAttack - (dameAttack * 5 / 100), dameAttack + (dameAttack * 5 / 100));
        //check activation set
        if (dameAttack > 100_000_000) {
            if (skillSelect.template.id == Skill.KAMEJOKO && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
//            } else if (skillSelect.template.id == Skill.LIEN_HOAN && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
//            } else if (skillSelect.template.id == Skill.GALICK && player.isPl()) {
//                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.KAIOKEN && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.DE_TRUNG && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.MAKANKOSAPPO && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.TU_SAT && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            } else if (skillSelect.template.id == Skill.QUA_CAU_KENH_KHI && player.isPl()) {
                Service.gI().sendThongBaoBenDuoi(player.name + " đã đánh một " + player.playerSkill.skillSelect.template.name + " với sát thương " + Util.numberToMoney(dameAttack));
            }
//            Logger.log(Logger.PURPLE, "Player: " + player.name + " Id: " + player.id + " Skill: " + player.playerSkill.skillSelect.template.name + "Dame: " + Util.numberToMoney(dameAttack) + " \n");
        }
        return dameAttack;
    }

    public long getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return this.hp * 100 / this.hpMax;
    }

    public long getCurrPercentMP() {
        return this.mp * 100 / this.mpMax;
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;

    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(float sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += ((long) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += ((long) tiemNang * 10 / 100);
                } else {
                    tiemNang += ((long) tiemNang * 5 / 100);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseBinhCanx2) {
                tiemNang += tn * 2;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseBinhCanx5) {
                tiemNang += tn * 5;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseBinhCanx7) {
                tiemNang += tn * 7;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseBinhCanx10) {
                tiemNang += tn * 10;
            }
            if (this.player.isPet) {
                if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx2) {
                    tiemNang *= 2;
                }
            }
            if (this.player.isPet) {
                if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx5) {
                    tiemNang *= 5;
                }
            }
            if (this.player.isPet) {
                if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx7) {
                    tiemNang *= 7;
                }
            }
            if (this.player.isPet) {
                if (this.player.isPet && ((Pet) this.player).itemTime.isUseBinhCanx10) {
                    tiemNang *= 10;
                }
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += ((long) tiemNang * this.intrinsic.param1 / 100);
            }
            if (this.power >= 60000000000L) {
                tiemNang -= ((long) tiemNang * 80 / 100);
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
            }
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 10;
        }
        return tiemNang;
    }
    public long calSubTNSM(long tiemNang) {
        if (power >= 110000000000L) {
            tiemNang /= 10000;
        } else if (power >= 100000000000L) {
            tiemNang /= 5000;
        } else if (power >= 90000000000L) {
            tiemNang -= ((long) tiemNang * 99 / 100);
        } else if (power >= 80000000000L) {
            tiemNang -= ((long) tiemNang * 98 / 100);
        }
        return tiemNang;
    }

    public int getTileHutHp(boolean isMob) {
        if (isMob) {
            return this.tlHutHp + this.tlHutHpMob
                    + (this.player.isPl() ? this.player.TamkjllDametutien(Util.TamkjllGH(this.player.Tamkjlltutien[1]))
                            : 0);
        } else {
            return this.tlHutHp
                    + (this.player.isPl() ? this.player.TamkjllDametutien(Util.TamkjllGH(this.player.Tamkjlltutien[1]))
                            : 0);
        }
    }

    public int getTiLeHutMp() {
        return this.tlHutMp
                + (this.player.isPl() ? this.player.TamkjllDametutien(Util.TamkjllGH(this.player.Tamkjlltutien[1]))
                        : 0);
    }

    public double subDameInjureWithDeff(double dame) {
        int def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
//        if (this.player.setClothes.SetThienSu == 5) {
//            dame -= 500000;
//        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
     public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        switch (limitPower) {
            case 0:
                return 17999999999L;
            case 1:
                return 18999999999L;
            case 2:
                return 20999999999L;
            case 3:
                return 24999999999L;
            case 4:
                return 30999999999L;
            case 5:
                return 40999999999L;
            case 6:
                return 60999999999L;
            case 7:
                return 80999999999L;
            case 8:
                return 259999999999L;
            case 9:
                return 999999999999L;
            default:
                return 0;
        }
    }

    public long getPowerNextLimit() {
        switch (limitPower + 1) {
            case 0:
                return 17999999999L;
            case 1:
                return 18999999999L;
            case 2:
                return 20999999999L;
            case 3:
                return 24999999999L;
            case 4:
                return 30999999999L;
            case 5:
                return 40999999999L;
            case 6:
                return 60999999999L;
            case 7:
                return 80999999999L;
            case 8:
                return 259999999999L;
            case 9:
                return 999999999999L;
            default:
                return 0;
        }
    }

    public int getHpMpLimit() {
        if (limitPower == 0) {
            return 220000;
        }
        if (limitPower == 1) {
            return 240000;
        }
        if (limitPower == 2) {
            return 300000;
        }
        if (limitPower == 3) {
            return 350000;
        }
        if (limitPower == 4) {
            return 400000;
        }
        if (limitPower == 5) {
            return 450000;
        }
        if (limitPower == 6) {
            return 500000;
        }
        if (limitPower == 7) {
            return 550000;
        }
        if (limitPower == 8) {
            return 560000;
        }
        if (limitPower == 9) {
            return 1000000;
        }
        return 0;
    }

    public int getDameLimit() {
        if (limitPower == 0) {
            return 11000;
        }
        if (limitPower == 1) {
            return 12000;
        }
        if (limitPower == 2) {
            return 15000;
        }
        if (limitPower == 3) {
            return 18000;
        }
        if (limitPower == 4) {
            return 20000;
        }
        if (limitPower == 5) {
            return 22000;
        }
        if (limitPower == 6) {
            return 25000;
        }
        if (limitPower == 7) {
            return 30000;
        }
        if (limitPower == 8) {
            return 31000;
        }
        if (limitPower == 9) {
            return 35000;
        }
        return 0;
    }

    public short getDefLimit() {
        if (limitPower == 0) {
            return 550;
        }
        if (limitPower == 1) {
            return 600;
        }
        if (limitPower == 2) {
            return 700;
        }
        if (limitPower == 3) {
            return 800;
        }
        if (limitPower == 4) {
            return 1000;
        }
        if (limitPower == 5) {
            return 1200;
        }
        if (limitPower == 6) {
            return 1400;
        }
        if (limitPower == 7) {
            return 1600;
        }
        if (limitPower == 8) {
            return 1700;
        }
        if (limitPower == 9) {
            return 1800;
        }
        return 0;
    }

    public byte getCritLimit() {
        if (limitPower == 0) {
            return 5;
        }
        if (limitPower == 1) {
            return 6;
        }
        if (limitPower == 2) {
            return 7;
        }
        if (limitPower == 3) {
            return 8;
        }
        if (limitPower == 4) {
            return 9;
        }
        if (limitPower == 5) {
            return 10;
        }
        if (limitPower == 6) {
            return 10;
        }
        if (limitPower == 7) {
            return 10;
        }
        if (limitPower == 8) {
            return 10;
        }
        if (limitPower == 9) {
            return 10;
        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point) {
        if (point <= 0 || point > 100) {
            return;
        }
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = (long) (point * (2 * (this.hpg + 1000) + pointHp - 20) / 2);
            if ((this.hpg + pointHp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = (long) (point * (2 * (this.mpg + 1000) + pointMp - 20) / 2);
            if ((this.mpg + pointMp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = (long) (point * (2 * this.dameg + point - 1) / 2 * 100);
            if ((this.dameg + point) <= getDameLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= getDefLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((this.critg + point) <= getCritLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        Service.gI().point(player);
    }

    private boolean doUseTiemNang(long tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.gI().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }


    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                long tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc(player, hpMax / 100 * tiLeHoiPhuc,
                            mpMax / 100 * tiLeHoiPhuc);
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.getInstance().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
    }
}
