/**
 * Created by Soulscythe on 23/07/2016.
 */
public class LevelTreasureChance {
    //percentage chances
    public int nothing;
    public int mundane;
    public int minor;
    public int medium;
    public int major;

    //dx multipliers
    public int mundaneMult;
    public int minorMult;
    public int mediumMult;
    public int majorMult;

    public LevelTreasureChance (int nothing,int mundane,int minor,int medium,int major,
                                int mundaneMult,int minorMult, int mediumMult,int majorMult) {
        this.nothing = nothing;
        this.mundane = mundane;
        this.minor = minor;
        this.medium = medium;
        this.major = major;

        this.mundaneMult = mundaneMult;
        this.minorMult = minorMult;
        this.mediumMult = mediumMult;
        this.majorMult = majorMult;
    }

}
