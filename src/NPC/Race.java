package NPC;

import java.util.ArrayList;

/**
 * Created by Soulscythe on 15/01/2017.
 */
public class Race implements java.io.Serializable {
	public String name;
	public ArrayList<Race> subraces;
	public int statBonuses[] = {0,0,0,0,0,0};
	public String racialFeatures;
	public String languages;
	public String size;
	public float minHeight;
	public float maxHeight;
	public int minWeight;
	public int maxWeight;
	public int maxAge;
	public int speed;

	public Race (String n,int stb[],String rf,String l,String s,int sp,float mnh, float mxh, int mnw, int mxw,int mxa) {
		this.name = n;
		if (stb.length == 6) this.statBonuses = stb; else this.statBonuses = new int[] {-100,-100,-100,-100,-100,-100};
		this.racialFeatures = rf;
		this.languages = l;
		this.size = s;
		this.minHeight = mnh;
		this.maxHeight = mxh;
		this.minWeight = mnw;
		this.maxWeight = mxw;
		this.maxAge = mxa;
		this.speed = sp;
		subraces = new ArrayList<Race>();
	}

	public Race newSub (String n, int stb[],String rf) {
		Race out = new Race (this.name,this.statBonuses,this.racialFeatures,this.languages,this.size,this.speed,this.minHeight,this.maxHeight,this.minWeight,this.maxWeight,this.maxAge);
		out.name = n;
		out.statBonuses[0] += stb[0];
		out.statBonuses[1] += stb[1];
		out.statBonuses[2] += stb[2];
		out.statBonuses[3] += stb[3];
		out.statBonuses[4] += stb[4];
		out.statBonuses[5] += stb[5];
		out.racialFeatures += ", " + rf;

		this.subraces.add(out);
		return out;
	}

	public Race duplicate() {
		return new Race(this.name,this.statBonuses,this.racialFeatures,this.languages,this.size,this.speed,this.minHeight,this.maxHeight,this.minWeight,this.maxWeight,this.maxAge);
	}

	public boolean equals(Race r) {
		return this.name.equals(r.name);
	}
}
