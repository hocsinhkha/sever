package tutien.models.player;

import java.util.ArrayList;
import java.util.List;
import tutien.models.item.Item;
import tutien.models.item.Item.ItemOption;
import barcoll.services.GiftService;


public class Inventory {

  public static final long LIMIT_GOLD = 200000000000L;
    public static final int MAX_ITEMS_BAG = 126;
    public static final int MAX_ITEMS_BOX = 126;

    public Item trainArmor;
    public List<String> giftCode;
    public List<Item> itemsBody;
    public List<Item> itemsBag;
    public List<Item> itemsBox;

    public List<Item> itemsBoxCrackBall;
    public List<Item> itemsBoxCrackBallthu;

    public long gold;
    public int gem;
    public int ruby;
    public int coupon;
    public int event;
    public int skien;
    public int skien1;
    public int skien2;
    public int skien3;
    public int skien4;
    public int skien5;

    public Inventory() {
        itemsBody = new ArrayList<>();
        itemsBag = new ArrayList<>();
        itemsBox = new ArrayList<>();
        itemsBoxCrackBall = new ArrayList<>();
        itemsBoxCrackBallthu = new ArrayList<>();
        giftCode = new ArrayList<>();
    }

    public int getGemAndRuby() {
        return this.gem + this.ruby;
    }
    
    public int getParam(Item it , int id){
        for(ItemOption op : it.itemOptions){
            if(op!=null&&op.optionTemplate.id ==id){
                return op.param;
            }
        }
        return 0;
    }
    
    public boolean haveOption(List<Item> l , int index , int id){
        Item it = l.get(index);
        if(it != null && it.isNotNullItem()){
            return it.itemOptions.stream().anyMatch(op -> op != null && op.optionTemplate.id == id);
        }
        return false;
    }

    public void subGemAndRuby(int num) {
        this.ruby -= num;
        if (this.ruby < 0) {
            this.gem += this.ruby;
            this.ruby = 0;
        }
    }

    public void addGold(int gold) {
        this.gold += gold;
        if (this.gold > LIMIT_GOLD) {
            this.gold = LIMIT_GOLD;
        }
    }

    public void dispose() {
        if (this.trainArmor != null) {
            this.trainArmor.dispose();
        }
        this.trainArmor = null;
        if(this.itemsBody!= null){
            for(Item it : this.itemsBody){
                it.dispose();
            }
            this.itemsBody.clear();
        }
        if(this.itemsBag!= null){
            for(Item it : this.itemsBag){
                it.dispose();
            }
            this.itemsBag.clear();
        }
        if(this.itemsBox!= null){
            for(Item it : this.itemsBox){
                it.dispose();
            }
            this.itemsBox.clear();
        }
        if(this.itemsBoxCrackBall!= null){
            for(Item it : this.itemsBoxCrackBall){
                it.dispose();
            }
            this.itemsBoxCrackBall.clear();
        }
        if(this.itemsBoxCrackBallthu!= null){
            for(Item it : this.itemsBoxCrackBallthu){
                it.dispose();
            }
            this.itemsBoxCrackBallthu.clear();
        }
        this.itemsBody = null;
        this.itemsBag = null;
        this.itemsBox = null;
        this.itemsBoxCrackBall = null;
        this.itemsBoxCrackBallthu = null;
    }

}
