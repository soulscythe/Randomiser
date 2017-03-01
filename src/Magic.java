/**
 * Created by Soulscythe on 15/05/2016.
 */
public class Magic {

    public String effect;
    public String range;
    public String duration;
    public String activationType;
    public String target;
    public int usesRemaining;
    public int maxUses;
    public String useType;
    public String category;

    public Magic (String effect, String range, String duration, String activationType, String target, int usesRemaining, int maxUses, String category) {
        this.effect = effect;
        this.range = range;
        this.duration = duration;
        this.activationType = activationType;
        this.target = target;
        this.usesRemaining = usesRemaining;
        this.maxUses = maxUses;
        this.category = category;
    }

    public String toString() {
        return "Activation: " + activationType + "\n" +
                "Range: " + range + "\n" +
                "Target: " + target + "\n" +
                "Effect: " + effect + "\n" +
                "Duration: " + duration + "\n" +
                "Uses remaining: " + (maxUses == 0 ? useType : (usesRemaining +"/"+ maxUses));
    }

    public static Magic empty() {
        return new Magic("-","-","-","-","-",0,0,"-");
    }
    public Magic duplicate()  {return new Magic(effect,range,duration,activationType,target,usesRemaining,maxUses,category);}
}
