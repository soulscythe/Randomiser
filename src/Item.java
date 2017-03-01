/**
 * Created by Soulscythe on 01/05/2016.
 */
public class Item {
    String materialPrimary;
    String materialSecondary;

    String name;
    String source;
    String category;
    String weight;
    int rarity;

    public Item ( int rarity, String name, String weight, String materialPrimary, String materialSecondary, String source, String category) {

        this.rarity = rarity;
        this.name = name;
        this.weight = weight;
        this.materialPrimary = materialPrimary;
        this.materialSecondary = materialSecondary;
        this.source = source;
        this.category = category;

    }

    public String toString() {
        return name;
    }
    public static Item empty() {
        return new Item(0,"-","-","-","-","-","-");
    }
    public Item duplicate()  {return new Item(rarity,name,weight,materialPrimary,materialSecondary,source,category);}
    public String[] toStringArray() {
        String[] out = {name,weight,source,category,rarity+"",materialPrimary,materialSecondary};
        return out;
    }

    public Item clean () {
        this.name = this.name.replace("-"," ");
        this.name = this.name.trim();
        return this;
    }
}
