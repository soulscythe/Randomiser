package NPC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Soulscythe on 15/01/2017.
 */
public class NPCGenerator {

	////////////////////////////////////////////////////////
	//////////////Static Lists and Reference Variables//////
	////////////////////////////////////////////////////////
	//minH,maxH,minW,maxW,maxA

	private Random rand = new Random();


	private Race human = new Race("Human",new int[]{1,1,1,1,1,1},"","Common","Medium",30,4.6f,6.7f,90,280,110);
	private Race dwarf = new Race("Dwarf",new int[]{0,0,2,0,0,0},"Darkvision, " +
			"Proficient in Intelligence (History) relating to origin of stonework and adds double proficiency, " +
			"Advantage on Saving throws against poison and resistance to poison damage","Dwarven","Medium",25,3.75f,4.4f,105,230,350);
	private Race hillDwarf;
	private Race mountainDwarf;
	private Race elf = new Race("Elf",new int[]{0,2,0,0,0,0},"Immunity to magical sleep, " +
			"Advantage on saving throws against being charmed, " +
			"Darkvision, " +
			"Proficiency in Perception","Elven","Medium",30,4.6f,6.3f,80,160,750);
	private Race highElf;
	private Race woodElf;
	private Race drow;
	private Race dragonborn = new Race ("Dragonborn",new int[] {2,0,0,0,0,1},"Draconic Ancestry (*C), " +
			"Breath Weapon (*B), " +
			"Damage Resistance (*R)", "Draconic","Medium",30,4.6f,6.8f,115,440,80);
	private Race gnome = new Race("Gnome",new int[]{0,0,0,2,0,0},"Darkvision, " +
			"Advantage on Intelligence, Wisdom and Charisma Saving throws against magic"
			,"Gnomish","Small",25,3f,3.7f,35,50,500);
	private Race forestGnome;
	private Race rockGnome;
	private Race halfElf = new Race("Half-Elf",new int[]{0,0,0,0,0,2},"Immunity to magical sleep effects, " +
			"Darkvision, " +
			"Immunity to magical sleep, " +
			"Advantage on saving throws against being charmed, ","Common, Elven","Medium",30,4.6f,5.9f,85,230,185);
	private Race halfOrc = new Race("Half-Orc",new int[]{2,0,1,0,0,0},"Darkvision, " +
			"Proficiency in Intimidation, " +
			"When reduced to 0HP, can choose to stay at 1HP, requires long rest to do again, " +
			"On a critical hit, rolls one of damage dice once more","Common, Orc","Medium",30,4.6f,6.8f,115,440,80);
	private Race halfling = new Race("Halfling",new int[]{0,2,0,0,0,0},"Re-roll results of 1 on Attacks, Ability checks, and Saving Throws, " +
			"Advantage on Saving throws against Fear, " +
			"Able to move through spaces of any creature of a larger size","Halfling","Small",25,2.7f,3.3f,25,40,200);
	private Race lightfootHalfling;
	private Race stoutHalfling;
	private Race tiefling = new Race("Tiefling",new int[]{0,0,0,1,0,2},"Darkvision, " +
			"Resistance to fire damage, " +
			"Knows Thaumaturgy cantrip","Infernal, Common","Medium",30,4.6f,6.7f,90,280,110);

	private String[] classes = {"Barbarian", "Bard", "Cleric","Druid","Fighter","Monk","Paladin","Ranger","Rogue","Sorcerer","Warlock","Wizard",};
	private String[] skills = {"Acrobatics","Animal Handling","Arcana","Athletics","Deception","History","Insight","Intimidation","Investigation","Medicine","Nature","Perception","Performance","Persuasion","Religion","Sleight of Hand","Stealth","Survival"};

	/////////////////////////////////////////////////
	///TODO: The above should be converted into files read from folder, as to allow for expansion by user
	/////////////////////////////////////////////////



	ArrayList<String> occupations_agrarian;
	ArrayList<String> occupations_art;
	ArrayList<String> occupations_craftsmen;
	ArrayList<String> occupations_criminal;
	ArrayList<String> occupations_government;
	ArrayList<String> occupations_mercantile;
	ArrayList<String> occupations_military;
	ArrayList<String> occupations_nonstandard;
	ArrayList<String> occupations_sailors;
	ArrayList<String> occupations_scholarly;
	ArrayList<String> occupations_services;
	int[] occupationTypeFrequencies = {1000,300,1000,100,50,600,750,100,200,150,1000};

	ArrayList<String> personalityTraits;
	ArrayList<String> appearanceDetails;
	ArrayList<String> idiosyncracies; //appended to personality traits list, but randomised differently.
	ArrayList<String> maleNames;
	ArrayList<String> femaleNames;
	ArrayList<String> surnames;

	public float goodEvil = 1.5f;		//This should represent an average of the group's moral/ethical alignment, and it will use this as a baseline, closer to evil meaning more likely to happen
	public float lawfulChaotic = 1.5f;	//0-1 = evil/chaotic, 2-3 = good/lawful. 1-2 is neutral.
	public float alignmentVariance = 2.05f;
	// Perhaps randomise a value that is +/- 2.05 of the average. this means the lowest value available, if an average of 3 is given, is 0.95, which is just enough to be considered evil(capped)

	public int averageAge; // take user input for average age, as a scale from 0 to maximum?

	String materialPath;

	public NPCGenerator () {

		//subrace initialisation// "NNNNNNNN", new int[] {0,0,0,0,0,0},"CCCCCCCCCC"
		hillDwarf = dwarf.newSub("Hill Dwarf",new int[] {0,0,0,0,1,0},"+1 HP per level (+1 if no levels)");
		mountainDwarf = dwarf.newSub("Mountain Dwarf",new int[] {2,0,0,0,0,0},"Proficiency with light and medium armour");
		highElf = elf.newSub("High Elf", new int[] {0,0,0,1,0,0},"Proficiency with longsword, shortsword, longbow and shortbow, Knows one cantrip");
		woodElf = elf.newSub("Wood Elf", new int[] {0,0,0,0,1,0},"35 foot move speed, Proficiency with longsword, shortsword, longbow and shortbow, Can attempt to hide when lightly obscured by natural things like weather or plantlife");
		drow = elf.newSub("Drow", new int[] {0,0,0,0,0,1},"Superior Darkvision (120ft range), Disadvantage on attack rolls and Wisdom (Perception) checks relying on sight when the subject of the roll is in sunlight, Knows Dancing Lights cantrip, Proficiency with rapiers, shortswords, and hand crossbows");
		lightfootHalfling = halfling.newSub("Lightfoot Halfling", new int[] {0,0,0,0,0,1},"Can attempt to hide even when obscured by a creature only one size larger");
		stoutHalfling = halfling.newSub("Stout Halfling", new int[] {0,1,0,0,0,0},"Advantage on Saving throws against poison and resistance to poison damage");
		forestGnome = gnome.newSub("Forest Gnome", new int[] {0,1,0,0,0,0},"Knows Minor Illusion cantrip, Can communicate with animals of a size Small or lesser");
		rockGnome = gnome.newSub("Rock Gnome", new int[] {0,0,1,0,0,0},"Add twice proficiency to Intelligence (History) checks related to magic items, alchemical objects, or technological devices, Proficiency with artisans tools and can make Tiny clockwork objects");
		//////////////////////////

		try {
			materialPath = new File(new File(".").getAbsolutePath()).getCanonicalPath() + "\\material\\npc";
		} catch (IOException e) {
			System.out.println("Error finding npcPath");
		}

		occupations_agrarian = new ArrayList<String>();
		occupations_art = new ArrayList<String>();
		occupations_craftsmen = new ArrayList<String>();
		occupations_criminal = new ArrayList<String>();
		occupations_government = new ArrayList<String>();
		occupations_mercantile = new ArrayList<String>();
		occupations_military = new ArrayList<String>();
		occupations_nonstandard = new ArrayList<String>();
		occupations_sailors = new ArrayList<String>();
		occupations_scholarly = new ArrayList<String>();
		occupations_services = new ArrayList<String>();
		personalityTraits = new ArrayList<String>();
		appearanceDetails = new ArrayList<String>();
		idiosyncracies = new ArrayList<String>();
		maleNames = new ArrayList<String>();
		femaleNames = new ArrayList<String>();
		surnames = new ArrayList<String>();

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		//occupations parsing////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		for (String s : readFileToArray(new File(materialPath + "\\occupations_agrarian.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_agrarian.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_art.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_art.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_craftsmen.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_craftsmen.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_criminal.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_criminal.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_government.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_government.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_mercantile.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_mercantile.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_military.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_military.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_nonstandard.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_nonstandard.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_sailors.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_sailors.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_scholarly.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_scholarly.add(value.trim());
			}
		}
		for (String s : readFileToArray(new File(materialPath + "\\occupations_services.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue; //handles blank lines and 'comments'
			if (s.split(",").length < 2) continue;
			int weight = Integer.parseInt(s.split(",")[0]);
			String value = s.substring(s.indexOf(',') + 1);
			for (int i = 0; i < weight; i++) {
				occupations_services.add(value.trim());
			}
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////
		for (String s : readFileToArray(new File(materialPath + "\\personality_traits.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue;
			personalityTraits.add(s.trim());
		}
		for (String s : readFileToArray(new File(materialPath + "\\idiosyncracies.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue;
			idiosyncracies.add(s.trim());
		}
		for (String s : readFileToArray(new File(materialPath + "\\appearance_details.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue;
			appearanceDetails.add(s.trim());
		}
		for (String s : readFileToArray(new File(materialPath + "\\names_male.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue;
			maleNames.add(s.trim());
		}
		for (String s : readFileToArray(new File(materialPath + "\\names_female.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue;
			femaleNames.add(s.trim());
		}
		for (String s : readFileToArray(new File(materialPath + "\\names_surname.txt"))) {
			if (s.trim().equals("") || s.startsWith("//")) continue;
			surnames.add(s.trim());
		}

	}


	public String randomiseName(String gender) {
		String out = "";
		if(gender.equals("Male")) out += maleNames.get(rand.nextInt(maleNames.size())) + " ";
		else if(gender.equals("Female")) out += femaleNames.get(rand.nextInt(femaleNames.size())) + " ";
		out += surnames.get(rand.nextInt(surnames.size()));
		return out;
	}

	public int randomiseAge(Race r) {
		boolean abovePrime = rand.nextBoolean();
		int age;
		int prime = r.maxAge/4;

		if (abovePrime) {
			age = squaredChance(r.maxAge-prime,true);
			age += prime;
		} else {
			age = squaredChance(prime,false);
		}

		return age;
	}

	public String randomiseOccupation() {
		int totalFreq = 0;
		for (int i : occupationTypeFrequencies) totalFreq += i; //make note of total weights
		int chosenNum = rand.nextInt(totalFreq);				//pick random number within range of total weights
		int curPos = 0;											//track position as we add to each weight
		ArrayList<String> chosenList = new ArrayList<String>();

		if (chosenNum <= (curPos += occupationTypeFrequencies[0])) {
			chosenList = occupations_agrarian;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[1])) {
			chosenList = occupations_art;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[2])) {
			chosenList = occupations_craftsmen;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[3])) {
			chosenList = occupations_criminal;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[4])) {
			chosenList = occupations_government;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[5])) {
			chosenList = occupations_mercantile;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[6])) {
			chosenList = occupations_military;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[7])) {
			chosenList = occupations_nonstandard;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[8])) {
			chosenList = occupations_sailors;
		} else if (chosenNum <= (curPos += occupationTypeFrequencies[9])) {
			chosenList = occupations_scholarly;
		} else if (chosenNum <= (curPos + occupationTypeFrequencies[10])) {
			chosenList = occupations_services;
		} else {
			chosenList.add("ERROR IN OCCUPATIONTYPE SELECTION");
		}

		return chosenList.get(rand.nextInt(chosenList.size()));
	}

	public String randomiseClass() {
		if (percentageChance(5)) return classes[rand.nextInt(classes.length)];
		else return "";
	}

	public String randomiseLevel() {
		return "" + (squaredChance(19,true)+1);
	}

	public Race randomiseRace() {
		Race[] races = {human,dwarf,elf,gnome,halfElf,halfOrc,halfling,dragonborn,tiefling};
		Race chosenRace = races[rand.nextInt(races.length)].duplicate();
		if (chosenRace.subraces.size() > 0) {
			chosenRace = chosenRace.subraces.get(rand.nextInt(chosenRace.subraces.size()));
		}

		String[] dColours = new String[] {"Black","Blue","Brass","Bronze","Copper","Gold","Green","Red","Silver","White"};
		String[] dDamages = new String[] {"Acid","Lightning","Fire","Lightning","Acid","Fire","Poison","Fire","Cold","Cold"};
		String[] dBreaths = new String[] {"5 by 30 ft. line (Dex. save)","5 by 30 ft. line (Dex. save)","5 by 30 ft. line (Dex. save)","5 by 30 ft. line (Dex. save)","5 by 30 fI. line (Dex. save)","15 ft. cone (Dex. save)",	"15 ft. cone (Con. save)","15 ft. cone (Dex. save)","15 ft. cone (Con. save)","15 ft. cone (Con. save)"};

		/*"Draconic Ancestry (*C), " +
			"Breath Weapon (*B), " +
			"Damage Resistance (*R)"*/
		if (chosenRace.equals(dragonborn)) {
			int dragonType = rand.nextInt(dColours.length);
			chosenRace.racialFeatures = chosenRace.racialFeatures.replace("*C",dColours[dragonType]);
			chosenRace.racialFeatures = chosenRace.racialFeatures.replace("*B",dDamages[dragonType] + ", " + dBreaths[dragonType]);
			chosenRace.racialFeatures = chosenRace.racialFeatures.replace("*R",dDamages[dragonType]);
		}

		if (chosenRace.equals(halfElf)) {
			int A = rand.nextInt(6);
			int B;
			do {B = rand.nextInt(6);} while (B == A);
			chosenRace.statBonuses[A] += 1;
			chosenRace.statBonuses[B] += 1;
		}

		return chosenRace;
	}

	public String randomiseGender() {
		return rand.nextBoolean() ? "Female" : "Male";
	}

	public String randomiseAlignment() {
		String out = "";

		//lawful/chaotic
		float lcmax, lcmin;
		lcmax = lawfulChaotic + alignmentVariance;
		if (lcmax > 3) lcmax = 3;
		lcmin = lawfulChaotic - alignmentVariance;
		if (lcmin < 0) lcmin = 0;
		float lcrange = lcmax - lcmin;
		float lawChaosPick = rand.nextFloat()*lcrange;
		if (lawChaosPick < 0) out += "ErrorLC Alignment < 0";
		else if (lawChaosPick < 1) out += "Chaotic ";
		else if (lawChaosPick < 2) out += "Neutral ";
		else if (lawChaosPick < 3) out += "Lawful ";
		else out += "ErrorLC Alignment > 3";

		//good/evil
		float gemax, gemin;
		gemax = goodEvil + alignmentVariance;
		if (gemax > 3) gemax = 3;
		gemin = goodEvil - alignmentVariance;
		if (gemin < 0) gemin = 0;
		float gerange = gemax - gemin;
		float goodEvilPick = rand.nextFloat()*gerange;
		if (goodEvilPick < 0) out += "ErrorGE Alignment < 0";
		else if (goodEvilPick < 1) out += "Evil";
		else if (goodEvilPick < 2) out += "Neutral";
		else if (goodEvilPick < 3) out += "Good";
		else out += "ErrorGE Alignment > 3";

		return out;
	}

	public String randomiseHeight(Race r) {
		float variance = r.maxHeight-r.minHeight;
		float heightAboveMin = rand.nextFloat()*variance;
		float height = heightAboveMin+r.minHeight;
		int feet = (int)height;
		float remainder = height-(float)feet;
		float inches_f = Math.round(remainder/0.083333f);
		int inches = (int)inches_f;

		String out = "" + feet + "\' " + inches + "\"";

		return out;
	}

	public String randomiseWeight(Race r) {
		int variance = r.maxWeight-r.minWeight;

		int weightAboveMin = rand.nextInt(variance);

		int weight = weightAboveMin + r.minWeight;

		return "" + weight + " lbs";
	}

	public String randomiseReligion() {
		return "";
	}

	public String randomisePersonalityTraits() {
		String out = "";
		int numberOfTraits = squaredChance(4,true)+1; //there is a high chance of 0 traits with this, so we want to ensure at least one, to help RP.
		int numberofIdioTraits = squaredChance(2,true);
		for (int i = 0; i < numberOfTraits; i++) {
			String temp = personalityTraits.get(rand.nextInt(personalityTraits.size()));

			if (temp.charAt(0) == '*') {
				String[] splitted = temp.split(",");
				String mainAttribute = splitted[0].substring(1);
				String detailAttribute = splitted[rand.nextInt(splitted.length-1)+1];

				temp = mainAttribute + " " + detailAttribute;
			}

			out += temp + (i==(numberOfTraits+numberofIdioTraits)-1 ? "" : ", ") ;
		}
		if (numberofIdioTraits > 0) {
			for (int i = 0; i < numberofIdioTraits; i++) {
				String temp = idiosyncracies.get(rand.nextInt(idiosyncracies.size()));

				if (temp.charAt(0) == '*') {
					String[] splitted = temp.split(",");
					String mainAttribute = splitted[0].substring(1);
					String detailAttribute = splitted[rand.nextInt(splitted.length-1)+1];
					temp = mainAttribute + " " + detailAttribute;
				}

				out += temp + (i==numberofIdioTraits-1 ? "" : ", ") ;
			}
		}
		return out;
	}

	public String randomiseAppearanceDetails() {
		String out = "";
		int numberOfTraits = squaredChance(5,true);
		for (int i = 0; i < numberOfTraits; i++) {
			String temp = appearanceDetails.get(rand.nextInt(appearanceDetails.size()));

			if (temp.charAt(0) == '*') {
				String[] splitted = temp.split(",");
				String mainAttribute = splitted[0].substring(1);
				String detailAttribute = splitted[rand.nextInt(splitted.length-1)+1];

				temp = mainAttribute + " " + detailAttribute;
			}

			out += temp + (i==numberOfTraits-1 ? "" : ", ") ;
		}
		return out;
	}

	public int[] randomiseStats() {
		int[] out = new int[6];

		for (int i = 0; i < out.length; i++) {
			int itemp = squaredChance(3,true);
			if (rand.nextBoolean()) itemp = -itemp; //randomly invert result, as we want possibly negative values also.
			out[i] = 10 + itemp;
		}

		return out;
	}

	public String randomiseSkills() {
		String out = "";
		int skillsKnown = squaredChance(8,true);
		ArrayList<String> skillsLearned = new ArrayList<String>();
		for (int i = 0; i < skillsKnown; i++){
			boolean searching = true;
			boolean matchFound;
			while (searching) {
				matchFound = false;
				String possible = skills[rand.nextInt(skills.length)];
				if (skillsLearned.size() == 0) {
					skillsLearned.add(possible);
					break;
				}
				else for (String s : skillsLearned) {
					if (s.equals(possible)) matchFound = true;
				}
				if (!matchFound) {
					skillsLearned.add(possible);
					searching = false;
				}
				else;// else the loop restarts
			}
		}

		for (String s : skillsLearned) {
			out += s + ", " + (squaredChance(5,true)+1) + " Ranks. ";
		}

		return out;
	}

	public String randomiseItems() {
		return ""; //integrate looter randomisation
	}

	public String randomiseMoney() {
		return "";
	}

	public String randomiseLanguages() {
		return ""; //randomise amongst list of languages,
	}

	public String[] readFileToArray(File file) {
		String[] output = null;
		try {

			BufferedReader br = new BufferedReader(new FileReader(file));

			int i = 0;
			String temp = "";
			output = new String[0];
			String[] output_old = new String[0];
			while ((temp = br.readLine()) != null) {
				if (output.length > 0) {
					output_old = output;
					output = new String[output.length+1];
					for (int a = 0; a < output_old.length; a++) {
						output[a] = output_old[a];
					}
				}
				else {
					output = new String[1];
				}
				output[i] = temp;
				i++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	public boolean percentageChance(int percentage) {
		if (percentage <= 0) return false;
		if (percentage >= 100) return true;
		return rand.nextInt(100)+1 <= percentage;
	}

	public int squaredChance(int maxResult, boolean lowerNumbersMoreLikely){ //NOTE WELL: SQUARE RANDOMISATION
		double temp = rand.nextFloat()*Math.pow((double)maxResult,2d);		//Get random between 0 and intended max^2
		temp = Math.round(Math.sqrt(temp));									//round to nearest int for convenient conversion
		int itemp = (int)temp;												//convert to int
		if (lowerNumbersMoreLikely) itemp = maxResult - itemp;				//subtract from max if we want the lower values to be the most likely results
		return itemp;
	}

}
