package tutien.models.boss.daihoivothuat;

import barcoll.consts.ConstPlayer;
import barcoll.consts.ConstRatio;
import Amodels.boss.BossData;
import Amodels.boss.BossStatus;
import Amodels.boss.BossesData;
import tutien.models.map.DaiHoiVoThuat23.MartialCongressService;
import tutien.models.player.Player;
import barcoll.services.EffectSkillService;
import barcoll.services.PlayerService;
import barcoll.services.SkillService;
import tutien.utils.SkillUtil;
import tutien.utils.Util;

/**
 * @author BTH sieu cap vippr0 
 */
public class ThienXinHangClone extends BossDHVT {
    private int timeLive;
    private long lastUpdate = System.currentTimeMillis();


    public ThienXinHangClone(byte id, Player player) throws Exception {
        super(id, BossesData.THIEN_XIN_HANG_CLONE);
        this.playerAtt = player;
        timeLive = 10;
//        this.bossStatus = BossStatus.JOIN_MAP;
//        this.bossStatus = BossStatus.ACTIVE;
//        this.typePk = 3;
//        this.nPoint.khangTDHS = true;
//        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_PVP);
//        MartialCongressService.gI().sendTypePK(playerAtt, this);
        this.timeJoinMap = System.currentTimeMillis() + 10000;
    }

    @Override
    public void attack() {
        try {
            if (playerAtt.location != null && playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                if (this.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, playerAtt) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(playerAtt.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)), Util.nextInt(10) % 2 == 0 ? playerAtt.location.y : playerAtt.location.y - Util.nextInt(0, 50), false);
                    }
//                        System.err.println("attack player: " + playerAtt.name + "use skill: " + SkillService.gI().useSkill(this, playerAtt, null));
                    SkillService.gI().useSkill(this, playerAtt, null,null);
                    checkPlayerDie(playerAtt);
                } else {
                    goToPlayer(playerAtt, false);
                }
            } else {
                this.leaveMap();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
//        super.update();
        try {
            EffectSkillService.gI().removeStun(this);
            switch (this.bossStatus) {
                case RESPAWN:
                    this.respawn();
                    this.changeStatus(BossStatus.JOIN_MAP);
                case JOIN_MAP:
                    joinMap();
                    if (this.zone != null) {
                        changeStatus(BossStatus.ACTIVE);
                        timeJoinMap = System.currentTimeMillis();
                        this.immortalMp();
                        this.typePk = 3;
                        MartialCongressService.gI().sendTypePK(playerAtt, this);
                        PlayerService.gI().changeAndSendTypePK(playerAtt, ConstPlayer.PK_PVP);
                        this.changeStatus(BossStatus.ACTIVE);
                    }
                    break;
                case ACTIVE:
                    if (this.playerSkill.prepareTuSat || this.playerSkill.prepareLaze || this.playerSkill.prepareQCKK) {
                        break;
                    } else {
                        this.attack();
                    }
                    break;
            }
            if (Util.canDoWithTime(lastUpdate, 1000)) {
                lastUpdate = System.currentTimeMillis();
                if (timeLive > 0) {
                    timeLive--;
                } else {
                    super.leaveMap();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
