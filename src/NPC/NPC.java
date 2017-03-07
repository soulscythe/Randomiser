package NPC;

/**
 * Created by Soulscythe on 07/01/2017.
 */
public class NPC {

	//---------SOCIAL---------//
	String name;		//
	String occupation;	//list
	String gender;		//binary randomisation
	Race raceC;			//race as Class variable
	String race;		//randomise between races, allow for user-input to specify the proportion of different races
	int age;			//randomise between min and max for race
	String alignment;	//two ternary randomisations, user-input to specify 'average' alignment, and the randomisation would vary results on a curve from there
	String personalityTraits; //list
	String appearanceDetails; //list
	String height;		//randomise between given racial heights
	String weight;		//randomise between given racial weights
	String religion;	//randomise between religions within alignment

	//---------COMBAT---------//
	String HP;			//commoner = d3+con
	String playerClass;	//boolean randomisation to specify if a class is present, roughly equal randomisation for each class. Occupation may suggest a class though
	String classLevel;	//curve; low being more common
	String size; 		//Small/Medium/Large etc, judged by race
	//These given in format: [base]+[bonuses]([modifier]) eg, "14+2(+3)"
	//randomised roughly within commoner stat range, randomised as a group such that we can ensure that not all stats are given too high or low
	int strength;
	int dexterity;
	int constitution;
	int intelligence;
	int wisdom;
	int charisma;
	//These given in format: [base]+[bonuses]([modifier]) eg, "14+2(+3)"
	String fullStrength;
	String fullDexterity;
	String fullConstitution;
	String fullIntelligence;
	String fullWisdom;
	String fullCharisma;
	String proficiencyBonus; //applied from class levels, usually 0, as classes are rarely applied to NPC's
	String speed; //taken from race
	//taken from either class or commoner stats
	String saveProficiencies;
	//only 10 + dex can be accounted for
	String AC; //given in format "[10+dex] + armour/shields etc."
	String carriedItems; //randomise via looter, weapons and such must be added manually from here to the combat stats by DM
	String carriedMoney; //randomise via looter WHEN IT WORKS.

	//-------BOTH?-------//
	//randomise skills by... something... one or two points in literally anything perhaps.
	String skills; 			//only to list skills with ranks, any not listed are assumed untrained and the usual -4 is applied if appropriate
	String languages;		//if common is not listed, it should be randomised whether they can speak it at all. Take user-input to decide standard language for group being randomised, and apply that language to /most/ NPCs
	String feats;			//Not sure...
	String racialFeatures;	//given from race

	public String[] toStringArray() {
		//"Name", "Occupation", "Gender", "Race", "Alignment"
		return new String[] {name,occupation,gender,race,alignment};
	}
	public String[] toStringArray(int i) {
		//social
		if (i == 0) return new String[] {name,occupation,gender,race,age+"",alignment,personalityTraits,appearanceDetails,height,weight,religion,skills,languages,feats,racialFeatures};
		//combat
		else if (i == 1) return new String[] {HP,playerClass,classLevel,size,
				fullStrength,fullDexterity,fullConstitution,fullIntelligence,fullWisdom,fullCharisma,
				proficiencyBonus,speed,saveProficiencies,AC,carriedItems,carriedMoney,skills,languages,feats,racialFeatures};
		//full
		else if (i == 2) return new String[] {name,occupation,gender,race,age+"",alignment,personalityTraits,appearanceDetails,height,weight,religion,HP,playerClass,classLevel,size,
				fullStrength,fullDexterity,fullConstitution,fullIntelligence,fullWisdom,fullCharisma,
				proficiencyBonus,speed,saveProficiencies,AC,carriedItems,carriedMoney,skills,languages,feats,racialFeatures};
		else return new String[]{"Error"};
	}

	public static String[] stringArrayFormat_Full() {
		return new String[] {"Name", "Occupation", "Gender", "Race", "Age","Alignment", "Personality Traits", "Physical Traits", "Height", "Weight", "Religion","HP", "Class (if any)", "Class Level", "Size",
				"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma",
				"Base Attack Bonus", "Speed","Initiative","Fortitude Save", "Reflex Save", "Will Save", "AC", "Items on Person", "Money on Person", "Skills","Languages","Feats","Racial Features"};
	}

	public static String[] StringArrayFormat_Social () {
		return new String[] {"Name", "Occupation", "Gender", "Race", "Age","Alignment", "Personality Traits", "Physical Traits", "Height", "Weight", "Religion","Skills","Languages","Feats","Racial Features"};
	}

	public static String[] StringArrayFormat_Combat () {
		return new String[] {"HP", "Class (if any)", "Class Level", "Size",
				"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma",
				"Base Attack Bonus", "Speed","Initiative","Fortitude Save", "Reflex Save", "Will Save", "AC", "Items on Person", "Money on Person", "Skills","Languages","Feats","Racial Features"};
	}

	public static NPC blank() {
		return new NPC();
	}

	public static NPC newRandom(NPCGenerator npcGen) {
		NPC out = NPC.blank();

		out.occupation = npcGen.randomiseOccupation();
		out.gender = npcGen.randomiseGender();
		out.name = npcGen.randomiseName(out.gender);
		out.alignment = npcGen.randomiseAlignment();
		out.personalityTraits = npcGen.randomisePersonalityTraits();
		out.appearanceDetails = npcGen.randomiseAppearanceDetails();
		out.religion = npcGen.randomiseReligion();
		out.HP = "4";
		out.playerClass = npcGen.randomiseClass();
		if (!out.playerClass.equals("")) out.classLevel = npcGen.randomiseLevel();
		int[] stats = npcGen.randomiseStats();
		out.strength = stats[0];
		out.dexterity = stats[1];
		out.constitution = stats[2];
		out.intelligence = stats[3];
		out.wisdom = stats[4];
		out.charisma = stats[5];
		out.applyRace(npcGen.randomiseRace());
		out.height = npcGen.randomiseHeight(out.raceC);
		out.weight = npcGen.randomiseWeight(out.raceC);
		out.age = npcGen.randomiseAge(out.raceC);
		if (out.age < 11) out.occupation = "None (Infant/Child)";
		//Format fullstats
		out.fullStrength = "" + out.strength + " (" + formatModifier((out.strength/2)-5) + ")";
		out.fullDexterity = "" + out.dexterity + " (" + formatModifier((out.dexterity/2)-5) + ")";
		out.fullConstitution = "" + out.constitution + " (" + formatModifier((out.constitution/2)-5) + ")";
		out.fullIntelligence = "" + out.intelligence + " (" + formatModifier((out.intelligence/2)-5) + ")";
		out.fullWisdom = "" + out.wisdom + " (" + formatModifier((out.wisdom/2)-5) + ")";
		out.fullCharisma = "" + out.charisma + " (" + formatModifier((out.charisma/2)-5) + ")";
		out.proficiencyBonus = "+2";
		out.AC = "" + ((out.dexterity/2)+5) + " + armour/shields/etc.";
		out.carriedItems = npcGen.randomiseItems();
		out.carriedMoney = npcGen.randomiseMoney();
		out.skills = npcGen.randomiseSkills();
		out.languages = npcGen.randomiseLanguages();
		//out.feats = wat?

		return out;
	}

	public void applyRace(Race race) {
		this.speed = race.speed + "";
		this.size = race.size;
		this.languages = race.languages;
		this.racialFeatures = race.racialFeatures;
		this.race = race.name;

		this.strength += race.statBonuses[0];
		this.dexterity += race.statBonuses[1];
		this.constitution += race.statBonuses[2];
		this.intelligence += race.statBonuses[3];
		this.wisdom += race.statBonuses[4];
		this.charisma += race.statBonuses[5];
		raceC = race;
	}
	public static String formatModifier(int i) {
		if (i >= 0) return "+"+i;
		else return i + "";
	}
}
