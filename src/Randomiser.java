
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Randomiser {

	public static int level = 0;
	public static int size = 0;
	public static int minItems = 0;
	public static int maxItems = 0;
	public static LootType lootType;
	public static int damageChance = 0; //percentage

	private static String itemsPath;
	private static String magicsPath;
	public static String[] materials;
	public static boolean[] materialsChosen;
	private /*final*/ ArrayList<Item> items; //final may be required, it has been removed for testing purposes but may cause bugs.
	private final ArrayList<MagicComponent> magicBits;
	private static Looter output;

	public static int enchantedChance = 1;
	public static int universalRarityOfUnusualMaterials = 10;

	public Randomiser(Looter screen) {

		//Word Randomiser: pick vowel or consonant to start, go from there, generally picking vowels or consonants in alternation
		/*
		Interface: include language rules, like how to modify a word for different tenses.
		Elven will be: present tense includes 'a' vowels, past tense includes 'i' vowels, and future tense includes 'e' vowels.
		This will be input as:
		present:[             ]
		past:   [[a]i         ]
		future: [[a]e         ]

		Eng:
		present:[+ing         ]
		past:   [+ed          ]
		future: [             ]

		 */

		//Store local workpath as String
		output = screen;
		try {
			itemsPath = new File(new File(".").getAbsolutePath()).getCanonicalPath() + "\\material\\sourcematerials";
		} catch (IOException e) {
			System.out.println("Error finding itemsPath");
		}

		System.out.println(itemsPath);

		//get list of folders in \src\sourcematerials (each folder represents a source material book)
		File[] folderlist = new File(itemsPath).listFiles();
		materials = new String[folderlist.length];
		materialsChosen = new boolean[folderlist.length];

		for (int i = 0; i < materials.length; i++) {
			materials[i] = folderlist[i].getName();
			materialsChosen[i] = true;
			output.print(materials[i]);
		}
		setupMaterials();
		///////////////////////////////////////////
		///////////Magics Read//////////////////////
		try {
			magicsPath = new File(new File(".").getAbsolutePath()).getCanonicalPath() + "\\material\\magic";
		} catch (IOException e) {
			output.print("Error finding magicsPath");
		}

		//get list of files in \src\magic
		File[] files = new File(magicsPath).listFiles();

		ArrayList<File> alFiles = new ArrayList<File>();
		for (File f : files) alFiles.add(f);

		appendWeights(alFiles);

		//iterate over list of files storing each in respective array position of lootfiles
		magicBits = new ArrayList<MagicComponent>();
		for (File f : alFiles) {
			String[] lines = readFileToArray(f);
			output.print("reading from file: " + f.getName());
			for (String l : lines) {
				magicBits.add( new MagicComponent(l,f.getName().substring(0, f.getName().lastIndexOf("."))));
			}
		}
	}

	public void setupMaterials() {

		ArrayList<File> filelist = new ArrayList<File>();
		//get list of files in \src\sourcematerials from each selected source material
		for (int i = 0; i < materials.length; i++) {
			if (materialsChosen[i]) {
				//get array of all files in folder named {materials[i]}
				File[] curfilelist = new File(itemsPath + "\\" + materials[i]).listFiles();

				for (File f : curfilelist) { //add all files
					filelist.add(f);
				}
			}
		}

		//Add randomisation weight defaults of "1" to all files with no randomisation weights
		//appendWeights(filelist);

		//iterate over list of files storing each in respective array position of lootfiles
		items = new ArrayList<Item>();
		for (File f : filelist) {
			String[] lines = readFileToArray(f);
			output.print("reading from file: " + f.getName());
			for (String l : lines) {
				items.add( new Item(Integer.parseInt(l.split(",")[0]),
						l.split(",")[1],
						l.split(",")[2],
						l.split(",")[3],
						l.split(",")[4],
						f.getParentFile().getName(),
						f.getName().substring(0, f.getName().lastIndexOf("."))));
			}
		}
	}

	///////////////////////////////////////////////////////////////////////
	////////////////BASIC RANDOMISATION////////////////////////////////////
	public ArrayList<Item> randomiseGoods() {
		ArrayList<Item> out = new ArrayList<Item>();
		int numOfGoods = (int)(new Random().nextFloat()*Math.sqrt(level))+(level/6); //TODO: test. May be better now//
		for (int g = 0; g < numOfGoods; g++) {
			if (boolPercentage(50)) {
				if (boolPercentage(lootType.art)) {
					out.add(randomiseArt(level));
				}
			} else {
				if (boolPercentage(lootType.jewellery)) {
					out.add(randomiseJewellery());
				}
			}
		}
		return isItMagic(out);
	}

	public ArrayList<Item> randomiseEquipment (){
		ArrayList<Item> out = new ArrayList<Item>();
		for (int i = 0; i < size; i++) {
			out.addAll(randomiseEquip(true,false,true));
		}
		return isItMagic(out);
	}

	public ArrayList<Item> randomiseFluff() {
		int cMinItems = minItems;
		int cMaxItems = maxItems;
		int itemCount = 0;
		if (cMaxItems == cMinItems) {
			itemCount = cMaxItems;
		} else {
			itemCount = (new Random().nextInt(cMaxItems - cMinItems)) + cMinItems; //set as randomised between min and max
		}

		ArrayList<Item> out = new ArrayList<Item>();



		//pick type of fluff to randomise...
		for (int i = 0; i < itemCount; i++) {
			int totalWeights = lootType.combineWeights();
			int curWeight = 0;

			int chosen = new Random().nextInt(totalWeights);

			curWeight += lootType.alchemicals;
			if (chosen < curWeight) out.add(randomiseAlchemical());
			else {
				curWeight += lootType.ammunition;
				if (chosen < curWeight) out.add(randomiseAmmunition());
				else {
					curWeight += lootType.armour;
					if (chosen < curWeight) out.add(randomiseArmour());
					else {
						curWeight += lootType.art;
						if (chosen < curWeight) out.add(randomiseArt(level));
						else {
							curWeight += lootType.clothing;
							if (chosen < curWeight) out.add(randomiseClothing());
							else {
								curWeight += lootType.games;
								if (chosen < curWeight) out.add(randomiseGames());
								else {
									curWeight += lootType.jewellery;
									if (chosen < curWeight) out.add(randomiseJewellery());
									else {
										curWeight += lootType.scrap;
										if (chosen < curWeight) out.add(randomiseScrap());
										else {
											curWeight += lootType.shields;
											if (chosen < curWeight) out.add(randomiseShield());
											else {
												curWeight += lootType.tools;
												if (chosen < curWeight) out.add(randomiseTools());
												else {
													curWeight += lootType.weapons;
													if (chosen < curWeight) out.add(randomiseWeapon());
													else {
														curWeight += lootType.kitchenWare;
														if (chosen < curWeight) out.add(randomiseKitchenware());
														else {
															curWeight += lootType.magicItems;
															if (chosen < curWeight) out.addAll(randomiseEquip(false,true,false));
															else {
																curWeight += lootType.gear;
																if (chosen < curWeight) out.add(randomiseGear());
																else {
																	output.print("!!!! Something went wrong with fluff randomisation !!!!");
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return isItMagic(out);
	}
	///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////

	public ArrayList<Item> isItMagic(ArrayList<Item> itemList) {
		for (Item i : itemList) {
			if (boolPercentage(enchantedChance)) {
				i.name += " *";
			}
		}
		return itemList;
	}

	//Randomise Equipment
	public ArrayList<Item> randomiseEquip(boolean outMundane, boolean strict, boolean multi) {
		ArrayList<Item> items = new ArrayList<Item>();

		LevelTreasureChance[] levels = { //This is so inelegant
				new LevelTreasureChance(71,24, 5, 0, 0,1, 1,1,1), //Level 1
				new LevelTreasureChance(49,36,15, 0, 0,1, 1,1,1), //Level 2
				new LevelTreasureChance(49,30,21, 0, 0,3, 1,1,1), //Level 3
				new LevelTreasureChance(42,20,38, 0, 0,4, 1,1,1), //Level 4
				new LevelTreasureChance(57,10,33, 0, 0,4, 3,1,1), //Level 5
				new LevelTreasureChance(54, 5,40, 1, 0,4, 3,1,1), //Level 6
				new LevelTreasureChance(51, 0,46, 3, 0,1, 3,1,1), //Level 7
				new LevelTreasureChance(48, 0,48, 4, 0,1, 4,1,1), //Level 8
				new LevelTreasureChance(43, 0,48, 9, 0,1, 4,1,1), //Level 9
				new LevelTreasureChance(40, 0,48,11, 1,1, 4,1,1), //Level 10
				new LevelTreasureChance(31, 0,53,14, 2,1, 4,1,1), //Level 11
				new LevelTreasureChance(27, 0,55,15, 3,1, 6,1,1), //Level 12
				new LevelTreasureChance(19, 0,54,21, 5,1, 6,1,1), //Level 13
				new LevelTreasureChance(19, 0,39,34, 8,1, 6,1,1), //Level 14
				new LevelTreasureChance(11, 0,35,44,10,1,10,1,1), //Level 15
				new LevelTreasureChance(40, 0, 6,44,10,1,10,3,1), //Level 16
				new LevelTreasureChance(33, 0, 0,50,17,1, 1,3,1), //Level 17
				new LevelTreasureChance(24, 0, 0,56,20,1, 1,4,1), //Level 18
				new LevelTreasureChance( 4, 0, 0,66,30,1, 1,4,1), //Level 19
				new LevelTreasureChance(25, 0, 0,40,35,1, 1,4,3)  //Level 20
		};

		boolean itemFound = false;

		LevelTreasureChance thisLevel = levels[level-1];

		if (!multi) {
			thisLevel.mundaneMult = 1;
			thisLevel.minorMult = 1;
			thisLevel.mediumMult = 1;
			thisLevel.majorMult = 1;
		}

		do {
			int pick = (int) (new Random().nextFloat() * 100) +1;

			int curpoint = 100;

			if (pick > (curpoint -= thisLevel.major)) {//generate major item
				int multiplier = thisLevel.majorMult;
				if (multiplier > 1) multiplier = (new Random().nextInt(multiplier))+1;
				for (int i=0;i<multiplier;i++) {
					System.out.println("major loop started");
					items.add(randomiseMagicItems(pickRandom(filterItems("major_types")), 2));
				}
				itemFound = true;

			} else if (pick > (curpoint -= thisLevel.medium)) {//generate medium item
				int multiplier = thisLevel.mediumMult;
				if (multiplier > 1) multiplier = (new Random().nextInt(multiplier))+1;
				for (int i=0;i<multiplier;i++) {
					System.out.println("medium loop started");
					items.add(randomiseMagicItems(pickRandom(filterItems("medium_types")), 1));
				}
				itemFound = true;

			} else if (pick > (curpoint -= thisLevel.minor)) {//generate minor item
				int multiplier = thisLevel.minorMult;
				if (multiplier > 1) multiplier = (new Random().nextInt(multiplier))+1;
				for (int i=0;i<multiplier;i++) {
					System.out.println("minor loop started");
					items.add(randomiseMagicItems(pickRandom(filterItems("minor_types")), 0));
				}
				itemFound = true;

			} else if (pick > (curpoint -= thisLevel.mundane) && outMundane) {//generate mundane item
				int multiplier = thisLevel.mundaneMult;
				if (multiplier > 1) multiplier = (new Random().nextInt(multiplier))+1;
				for (int i=0;i<multiplier;i++) {
					System.out.println("mundane loop started");
					items.add(randomiseMundaneItems());
				}
				itemFound = true;

			} else if (!strict) {
				break;
			}

		} while (!itemFound);
		return items;
	}

	//TODO: This needs to be changed
	public String randomiseCoins(int level) {
		Random rand = new Random();

		String out = "";

		int copper = 0;
		int silver = 0;
		int gold = 0;
		int platinum = 0;

		int coinTot = rand.nextInt((level*level)*100);

		int pRation = rand.nextInt(21)-10;
		int gRation = rand.nextInt(51-pRation)-10;
		int sRation = rand.nextInt(61-(pRation+gRation))-10;
		int cRation = 100 - (pRation+gRation+sRation)-10;

		if (pRation < 0) pRation = 0;
		if (gRation < 0) gRation = 0;
		if (sRation < 0) sRation = 0;
		if (cRation < 0) cRation = 0;

		platinum = (coinTot*pRation)/1000;
		gold = (coinTot*gRation)/100;
		silver = (coinTot*sRation)/10;
		copper = (coinTot*cRation)/10;

		out = "Money: " + platinum + " Platinum, " + gold + " Gold, " + silver + " Silver, " + copper + " Copper";

		return out;
	}


	public Magic randomiseEnchantment() {
		Magic out = Magic.empty();

		out.effect = pickRandomMagic(filterMagics("10000","grog")).content;
		out.activationType = pickRandomMagic(filterMagics("activation")).content;
		out.range = pickRandomMagic(filterMagics("range")).content;
		String tempDuration = pickRandomMagic(filterMagics("duration")).content;
		if ( (!tempDuration.equals("instantaneous"))&&(!tempDuration.equals("permanent")) ) {
			tempDuration = (new Random().nextInt(12)+1) + " " + tempDuration;
		}
		out.duration = tempDuration;
		out.target = pickRandomMagic(filterMagics("target")).content;
		out.useType = pickRandomMagic(filterMagics("usetypes")).content;
		if ( (!out.useType.equals("Ulimited"))&&(!out.useType.equals("Oneshot (mundane after)"))&&(!out.useType.equals("Oneshot (disintegrated/destroyed after use)")) ) {
			out.maxUses = Integer.parseInt(out.useType);
			out.usesRemaining = new Random().nextInt(out.maxUses)+1;
		}

		return out;
	}

	//randomise Magical Items
	public Item randomiseMagicItems(Item type, int power) {

		Item item = Item.empty();

		if (type.name.equals("Armour and Shields")) item = randomiseMagicArmourShields(power);
		if (type.name.equals("Weapons")) item = randomiseMagicWeapons(power);
		if (type.name.equals("Potions")) item = randomisePotions(power);
		if (type.name.equals("Rings")) item = randomiseRings(power);
		if (type.name.equals("Rods")) item = randomiseRods(power);
		if (type.name.equals("Scrolls")) item = randomiseScrolls(power);
		if (type.name.equals("Staffs")) item = randomiseStaves(power);
		if (type.name.equals("Wands")) item = randomiseWands(power);
		if (type.name.equals("Wondrous Items")) item = randomiseWondrous(power);

		if (boolPercentage(5)) {
			item.name = "Cursed " + item.name;
		}
		if (boolPercentage(1)) {
			String modification = "";
			modification += "Intelligent ";
			modification += randomiseAlignment().trim();
			item.name = modification + item.name;
		}

		return item;
	}

	//Randomise jewellery
	public Item randomiseJewellery() {

		Item out = Item.empty();

		Item stone = pickRandom(filterItems("mat_jewellery_stones"));
		Item metal = pickRandom(filterItems("mat_jewellery"));
		Item jewellery = pickRandom(filterItems("jewellery"));

		out.name = metal + " " + jewellery;

		if (boolPercentage(50)) out.name += " with inset " + stone;

		return out;
	}
	//Randomise magical armour or shields
	public Item randomiseMagicArmourShields(int power) {
		int percent = 0;

		Item specialAbs = Item.empty();
		String modifier = "";
		Item item = Item.empty();

		int specials = 0; //special abilities

		boolean special = false;
		boolean shield = false;
		boolean armour = false;

		do {
			percent = (int)(new Random().nextFloat()*100);

			special = false;
			if (power == 0) { //minor
				if (percent < 61) {
					//+1 shield
					modifier = "+1";
					shield = true;
				} else if (percent < 81) {
					//+1 armour
					modifier = "+1";
					armour = true;
				} else if (percent < 86) {
					//+2 shield
					modifier = "+2";
					shield = true;
				} else if (percent < 88) {
					//+2 armour
					modifier = "+2";
					armour = true;
				} else if (percent < 90) {
					//specific armour
					item = randomiseArmourSpecific(power);
				} else if (percent < 92) {
					//specific shield
					item = randomiseShieldSpecific(power);
				} else if (specials == 0) {
					//special
					specials++;
					special = true;
				} else {
					special = true;
				}

			} else if (power == 1) { //medium
				if (percent < 6) {
					//+1 shield
					modifier = "+1";
					shield = true;
				} else if (percent < 11) {
					//+1 armour
					modifier = "+1";
					armour = true;
				} else if (percent < 21) {
					//+2 shield
					modifier = "+2";
					shield = true;
				} else if (percent < 31) {
					//+2 armour
					modifier = "+2";
					armour = true;
				} else if (percent < 41) {
					//+3 shield
					modifier = "+3";
					shield = true;
				} else if (percent < 51) {
					//+3 armour
					modifier = "+3";
					armour = true;
				} else if (percent < 56) {
					//+4 shield
					modifier = "+4";
					shield = true;
				} else if (percent < 58) {
					//+4 armour
					modifier = "+4";
					armour = true;
				} else if (percent < 61) {
					//specific armour
					item = randomiseArmourSpecific(power);
				} else if (percent < 64) {
					//specific shield
					item = randomiseShieldSpecific(power);
				} else if (specials == 0) {
					//special
					specials++;
					special = true;
				} else {
					special = true;
				}
			} else if (power == 2) { //major
				if (percent < 9) {
					//+3 shield
					modifier = "+3";
					shield = true;
				} else if (percent < 17) {
					//+3 armour
					modifier = "+3";
					armour = true;
				} else if (percent < 28) {
					//+4 shield
					modifier = "+4";
					shield = true;
				} else if (percent < 39) {
					//+4 armour
					modifier = "+4";
					armour = true;
				} else if (percent < 50) {
					//+5 shield
					modifier = "+5";
					shield = true;
				} else if (percent < 58) {
					//+5 armour
					modifier = "+5";
					armour = true;
				} else if (percent < 61) {
					//specific armour
					item = randomiseArmourSpecific(power);
				} else if (percent < 64) {
					//specific shield
					item = randomiseShieldSpecific(power);
				} else if (specials == 0) {
					//special
					specials++;
					special = true;
				} else {
					special = true;
				}
			}
		} while (special);

		if (shield) {
			item = randomiseShield();
		} else if (armour) {
			item = randomiseArmour();
		}

		for (int i = 0; i < specials; i++) {
			//for each special ability, generate one
			if (boolPercentage(1)) {
				specials += 2;
			} else {
				//randomise special ability
				if (item.source.contains("shield")) {
					specialAbs = randomiseShieldSpecial(power);
				}
				else if (item.source.contains("armour")) {
					specialAbs = randomiseArmourSpecial(power);
				}
			}
		}

		item.name = modifier.trim() + " " + specialAbs.name.trim() + " " + item.name.trim();

		return item;
	}
	//Randomise art
	public Item randomiseArt(int level) {

		int chanceOfJewels = 2*level; //percentage chance of inset jewels in art object

		String outName;

		Item damage = Item.empty();
		Item material = pickRandom(filterItems("mat_art_solid"));
		Item art = pickRandom(filterItems("art_solid"));

		Item compositeOut = Item.empty();

		if (boolPercentage(damageChance)) {
			damage = randomiseDamage(material);
		}

		outName = damage.name + " " + material.name + " " + art.name;

		if (boolPercentage(chanceOfJewels)) outName += " with inset " + pickRandom(filterItems("mat_jewellery_stones")).name;

		compositeOut.name = outName;
		compositeOut.materialPrimary = material.materialPrimary;
		compositeOut.materialSecondary = material.materialSecondary;
		compositeOut.category = art.category;
		compositeOut.source = art.source;

		return compositeOut;
	}
	public Item randomiseMagicWeapons(int power) {
		int percent = 0;

		String specialAbs = "";
		String modifier = "";
		Item item = Item.empty();

		int specials = 0; //special abilities

		boolean special = false;

		do {
			percent = (int)(new Random().nextFloat()*100);

			special = false;
			if (power == 0) { //minor
				if (percent < 71) {
					//+1 weapon
					modifier = "+1";
				} else if (percent < 86) {
					//+2 armour
					modifier = "+2";
				} else if (percent < 91) {
					//specific weapon
					item = randomiseWeaponSpecific(power);
				} else if (specials == 0) {
					//special ability
					specials++;
					special = true;
				} else {
					special = true;
				}

			} else if (power == 1) { //medium
				if (percent < 11) {
					//+1 weapon
					modifier = "+1";
				} else if (percent < 30) {
					//+2 weapon
					modifier = "+2";
				} else if (percent < 59) {
					//+3 weapon
					modifier = "+3";
				} else if (percent < 63) {
					//+4 weapon
					modifier = "+4";
				} else if (percent < 69) {
					//specific weapon
					item = randomiseWeaponSpecific(power);
				} else if (specials == 0) {
					//special
					specials++;
					special = true;
				} else {
					special = true;
				}

			} else if (power == 2) { //major
				if (percent < 21) {
					//+3 weapon
					modifier = "+3";
				} else if (percent < 39) {
					//+4 weapon
					modifier = "+4";
				} else if (percent < 50) {
					//+5 weapon
					modifier = "+5";
				} else if (percent < 64) {
					//specific weapon
					item = randomiseWeaponSpecific(power);
				} else if (specials == 0) {
					//special
					specials++;
					special = true;
				} else {
					special = true;
				}
			}
		} while (special);

		percent = (int)new Random().nextFloat()*100;

		if (item.name.equals("-")) { // if specific weapon has not already been selected
			if (percent < 71) {
				//common melee
				item = randomiseCommonMelee();
			} else if (percent < 73) {
				//uncommon
				item = randomiseUncommonMelee();
			} else if (percent < 81) {
				item = randomiseUncommonRanged();
			} else {
				//common ranged
				item = randomiseCommonRanged();
			}
		} else specialAbs = "";

		for (int i = 0; i < specials; i++) {
			//for each special ability, generate one
			int multiChance = level*5;
			if (multiChance == 0) multiChance = 1;

			if (boolPercentage(multiChance)) {
				specials += 2;
			} else {
				//randomise special ability
				if (item.source.contains("melee")) {
					specialAbs += specialAbs.trim() + " " + randomiseWeaponSpecialMelee(power);
				}
				else if (item.source.contains("ranged")) {
					specialAbs += specialAbs.trim() + " " + randomiseWeaponSpecialRanged(power);
				}
			}
		}

		item.name = modifier.trim() + " " + specialAbs.trim() + " " + item.name.trim();

		return item;
	}
	public Item randomiseAlchemical() {
		Item out = pickRandom(filterItems("alchemical_items"));
		if (boolPercentage(universalRarityOfUnusualMaterials)) {
			out.name = randomiseUnusualMaterial(out).name + " " + out.name;
		}
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public Item randomiseArmour() {
		Item out = pickRandom(filterItems("armour_light","armour_medium","armour_heavy"));
		if (boolPercentage(universalRarityOfUnusualMaterials)) {
			out.name = randomiseUnusualMaterial(out).name + " " + out.name;
		}
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public Item randomiseShield() {
		Item out = pickRandom(filterItems("shields"));
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public Item randomiseWeapon() {
		int random = (int)(new Random().nextFloat()*100);
		if (random < 51) return randomiseCommonMelee();
		else if (random < 53) return randomiseUncommonRanged();
		else if (random < 71) return randomiseUncommonMelee();
		else if (random > 70) return randomiseCommonRanged();
		else return Item.empty();
	}
	public Item randomiseAmmunition() {
		return pickRandom(filterItems("ammunition"));
	}
	public Item randomiseCommonMelee() {
		Item out = pickRandom(filterItems("weapons_common_melee"));
		if (boolPercentage(universalRarityOfUnusualMaterials)) {
			out.name = randomiseUnusualMaterial(out).name + " " + out.name;
		}
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public String randomiseAlignment() {
		String output = "";
		int morality = new Random().nextInt(3);
		int law = new Random().nextInt(3);
		
		if (law == 0) output += " (Chaotic";
		else if (law == 1) output += " (Neutral";
		else if (law == 2) output += " (Lawful";
		
		if (morality == 0) output += " Evil)";
		else if (morality == 1) output += " Neutral)";
		else if (morality == 2) output += " Good)";
		return output;
	}
	public Item randomiseCommonRanged() {
		Item out = pickRandom(filterItems("weapons_common_ranged"));
		if (boolPercentage(universalRarityOfUnusualMaterials)) {
			out.name = randomiseUnusualMaterial(out).name + " " + out.name;
		}
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public Item randomiseUncommonMelee() {
		Item out = pickRandom(filterItems("weapons_uncommon_melee"));
		if (boolPercentage(universalRarityOfUnusualMaterials)) {
			out.name = randomiseUnusualMaterial(out).name + " " + out.name;
		}
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public Item randomiseUncommonRanged() {
		Item out = pickRandom(filterItems("weapons_uncommon_ranged"));
		if (boolPercentage(universalRarityOfUnusualMaterials)) {
			out.name = randomiseUnusualMaterial(out).name + " " + out.name;
		}
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}
	public Item randomiseGear() {
		return pickRandom(filterItems("gear"));
	}
	public Item randomiseGames() {
		return pickRandom(filterItems("games"));
	}
	public Item randomiseMundaneItems() {
		Item out;
		String outputMod = "";
		int percent = (int)(new Random().nextFloat()*100);
		if (percent < 18) { // 			0-17 = alchems
			return randomiseAlchemical();
		} else if (percent < 51) { // 	18-50 = armour
			if (boolPercentage(20)) {
				outputMod += " Masterwork";
			}
			//return random armour OR shield
			//if random number (0-4) = 4 do thing;
			outputMod += boolPercentage(10) ? " Small" : " Medium";
			
			if (new Random().nextInt(4) == 3) {
				//randomise shield
				out = randomiseShield();
			} else {
				//randomise armour
				out = randomiseArmour();
			}
		} else if (percent < 84) { // 	51-83 = weapons
			if (boolPercentage(20)) {
				outputMod += " Masterwork";
			}
			outputMod += boolPercentage(10) ? " Small" : " Medium";
			//return random weap
			out = randomiseWeapon();
		} else { // 					84-100 = tools/gear
			out = randomiseGear();
		}

		out.name = outputMod + out.name;

		return out;
	}
	public Item randomiseArmourSpecific(int power){
		if (power == 0) return pickRandom(filterItems("armour_specific_minor"));
		if (power == 1) return pickRandom(filterItems("armour_specific_medium"));
		if (power == 2) return pickRandom(filterItems("armour_specific_major"));
		return Item.empty();
	}
	public Item randomiseShieldSpecific(int power) {
		if (power == 0) return pickRandom(filterItems("shields_specific_minor"));
		if (power == 1) return pickRandom(filterItems("shields_specific_medium"));
		if (power == 2) return pickRandom(filterItems("shields_specific_major"));
		return Item.empty();
	}
	public Item randomiseShieldSpecial(int power) {
		if (power == 0) return pickRandom(filterItems("shield_specials_minor"));
		if (power == 1) return pickRandom(filterItems("shield_specials_medium"));
		if (power == 2) return pickRandom(filterItems("shield_specials_major"));
		return Item.empty();
	}
	public Item randomiseArmourSpecial(int power){
		if (power == 0) return pickRandom(filterItems("armour_specials_minor"));
		if (power == 1) return pickRandom(filterItems("armour_specials_medium"));
		if (power == 2) return pickRandom(filterItems("armour_specials_major"));
		return Item.empty();
	}
	public Item randomisePotions(int power) {
		if (power == 0) return pickRandom(filterItems("potions_minor"));
		if (power == 1) return pickRandom(filterItems("potions_medium"));
		if (power == 2) return pickRandom(filterItems("potions_major"));
		return Item.empty();
	}
	public Item randomiseRings(int power) {
		Item out = Item.empty();
		if (power == 0) out = pickRandom(filterItems("rings_minor"));
		if (power == 1) out = pickRandom(filterItems("rings_medium"));
		if (power == 2) out = pickRandom(filterItems("rings_major"));
		out.name = "ring of " + out.name;
		return out;
	}
	public Item randomiseRods(int power) {
		Item out = Item.empty();
		if (power == 1) out = pickRandom(filterItems("rods_medium"));
		if (power == 2) out = pickRandom(filterItems("rods_major"));
		out.name = " [Rod] " + out.name;
		return out;
	}
	public Item randomiseStaves(int power) {
		Item out = Item.empty();
		if (power == 1) out = pickRandom(filterItems("staves_medium"));
		if (power == 2) out = pickRandom(filterItems("staves_major"));
		out.name = " staff of " + out.name;
		return out;
	}
	public Item randomiseScrolls(int power) {
		Item out = Item.empty();

		int spellCount = 0;

		if (power == 0) { //minor
			spellCount = (int)(new Random().nextFloat()*3)+1;
		} else if (power == 1) { //medium
			spellCount = (int)(new Random().nextFloat()*4)+1;
		} else if (power == 2) { //major
			spellCount = (int)(new Random().nextFloat()*6)+1;
		} else {
			out.name = "Error creating scroll, Power not in expected bounds";
			return out;
		}

		if (boolPercentage(70)) {
			//arcane
			out.name = "Arcane Scroll, containing:";
			for (int i = 0; i < spellCount; i++) {
				Item spell = randomiseSpell(power,true);
				out.name += " " + spell.name;
			}
		} else {
			//divine
			out.name = "Divine Scroll, containing:";
			for (int i = 0; i < spellCount; i++) {
				Item spell = randomiseSpell(power,false);
				out.name += " " + spell.name;
			}
		}

		return out;
	}
	public Item randomiseSpell(int power, boolean arcane) {
		Item out = Item.empty();

		int pick = (int)(new Random().nextFloat()*100)+1;

		int spellLevel = 0;
		int casterLevel = 0;

		if (power == 0) { //minor
			if (pick <= 5) {
				spellLevel = 0;
				casterLevel = 1;
			}
			else if (pick <= 50)  {
				spellLevel = 1;
				casterLevel = 1;
			}
			else if (pick <= 95) {
				spellLevel = 2;
				casterLevel = 3;
			}
			else if (pick <= 100) {
				spellLevel = 3;
				casterLevel = 5;
			}
		} else if (power == 1) { //medium
			if (pick <= 5) {
				spellLevel = 2;
				casterLevel = 3;
			}
			else if (pick <= 65) {
				spellLevel = 3;
				casterLevel = 5;
			}
			else if (pick <= 95) {
				spellLevel = 4;
				casterLevel = 7;
			}
			else if (pick <= 100) {
				spellLevel = 5;
				casterLevel = 9;
			}
		} else if (power == 2) {
			if (pick <= 5) {
				spellLevel = 4;
				casterLevel = 7;
			}
			else if (pick <= 50) {
				spellLevel = 5;
				casterLevel = 9;
			}
			else if (pick <= 70) {
				spellLevel = 6;
				casterLevel = 11;
			}
			else if (pick <= 85) {
				spellLevel = 7;
				casterLevel = 13;
			}
			else if (pick <= 95) {
				spellLevel = 8;
				casterLevel = 15;
			}
			else if (pick <= 100) {
				spellLevel = 9;
				casterLevel = 17;
			}
		}

		String type = arcane ? "arcane" : "divine";

		out = pickRandom(filterItems("spells_" + type + "_" + spellLevel));

		out.name += " (Caster level " + casterLevel + ")";
		return out;
	}
	public Item randomiseScrap() {
		return pickRandom(filterItems("scrap"));
	}
	public Item randomiseTools() {
		return pickRandom(filterItems("tools"));
	}
	public Item randomiseWands(int power) {
		Item out = Item.empty();
		if (power == 0) out = pickRandom(filterItems("wands_minor"));
		if (power == 1) out = pickRandom(filterItems("wands_medium"));
		if (power == 2) out = pickRandom(filterItems("wands_major"));
		out.name = " wand of " + out.name;
		return out;
	}
	public Item randomiseWondrous(int power) {
		if (power == 0) return pickRandom(filterItems("wondrous_minor"));
		if (power == 1) return pickRandom(filterItems("wondrous_medium"));
		if (power == 2) return pickRandom(filterItems("wondrous_major"));
		return Item.empty();
	}
	public Item randomiseClothing() {
		Item out = Item.empty();

		Item clothingMaterial = pickRandom(filterItems("mat_clothing"));
		Item clothingItem = pickRandom(filterItems("clothing"));
		Item pattern = pickRandom(filterItems("mat_clothing_pattern"));
		Item colour = pickRandom(filterItems("mat_clothing_colour"));

		out.name += clothingMaterial.name + " " + colour.name + " " + pattern.name + " " + clothingItem;

		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;

		return out;
	}
	public String randomiseWeaponSpecialRanged(int power) {
		if (power == 0) return pickRandom(filterItems("weapon_specials_ranged_minor")).name;
		if (power == 1) return pickRandom(filterItems("weapon_specials_ranged_medium")).name;
		if (power == 2) return pickRandom(filterItems("weapon_specials_ranged_major")).name;
		return "";
	}
	public String randomiseWeaponSpecialMelee(int power) {
		if (power == 0) return pickRandom(filterItems("weapon_specials_melee_minor")).name;
		if (power == 1) return pickRandom(filterItems("weapon_specials_melee_medium")).name;
		if (power == 2) return pickRandom(filterItems("weapon_specials_melee_major")).name;
		return "";
	}
	public Item randomiseWeaponSpecific(int power) {
		if (power == 0) return pickRandom(filterItems("weapons_specific_minor"));
		if (power == 1) return pickRandom(filterItems("weapons_specific_medium"));
		if (power == 2) return pickRandom(filterItems("weapons_specific_major"));
		return Item.empty();
	}
	public Item randomiseKitchenware() {
		Item out = pickRandom(filterItems("crockery","cutlery","kitchenware"));
		if (boolPercentage(damageChance)) out.name = randomiseDamage(out).name + " " + out.name;
		return out;
	}

	public ArrayList<Item> filterItems(String... categories) {
		ArrayList<Item> outputList = new ArrayList<Item>();
		for (Item i : items) {
			if (matchAnyString(i.category,categories)) {
				outputList.add(i.duplicate());
			}
		}
		if (outputList.size() == 0) {
			String debugList = "";
			for (String s : categories) {
				debugList += s + " ";
			}
			output.print("No matches found for any of the following categories: " + debugList);
		}
		return outputList;
	}

	public ArrayList<MagicComponent> filterMagics(String... categories) {
		ArrayList<MagicComponent> outputList = new ArrayList<MagicComponent>();
		for (MagicComponent i : magicBits) {
			if (matchAnyString(i.category,categories)) {
				outputList.add(i.duplicate());
			}
		}
		if (outputList.size() == 0) {
			String debugList = "";
			for (String s : categories) {
				debugList += s + " ";
			}
			output.print("No matches found for any of the following categories: " + debugList);
		}
		return outputList;
	}

	public Item randomiseDamage(Item damagedThing) {
		Item out = Item.empty();
		String materialToUse;
		if (damagedThing.materialSecondary.equals("-")) {
			materialToUse = damagedThing.materialPrimary;
		} else materialToUse = boolPercentage(25) ? damagedThing.materialSecondary : damagedThing.materialPrimary;

		//should be one if statement for each damage type file
		if (matchAnyStringVA(materialToUse,"Wood","Bone")) out = pickRandom(filterItems("damage_wood"));
		else if (materialToUse.equals("Metal")) out = pickRandom(filterItems("damage_metal"));
		else if (materialToUse.equals("Leather")) out = pickRandom(filterItems("damage_leather"));
		else if (materialToUse.equals("Cloth")) out = pickRandom(filterItems("damage_cloth"));
		else if (matchAnyStringVA(materialToUse,"Glass","Ceramic","Crystal","Clay")) out = pickRandom(filterItems("damage_brittle"));

		return out;
	}

	public Item randomiseUnusualMaterial(Item baseItem) {
		Item out = Item.empty();
		if (baseItem.materialSecondary.equals("-")) { //no secondary material, so modify primary
			if (baseItem.materialPrimary.equals("Metal"))out = pickRandom(filterItems("uncommon_mat_metal"));
			if (baseItem.materialPrimary.equals("Wood"))out = pickRandom(filterItems("uncommon_mat_wood"));
			if (baseItem.materialPrimary.equals("Leather"))out = pickRandom(filterItems("uncommon_mat_leather"));
		}
		else if (boolPercentage(25)) { //modify secondary material
			if (baseItem.materialSecondary.equals("Metal"))out = pickRandom(filterItems("uncommon_mat_metal"));
			if (baseItem.materialSecondary.equals("Wood"))out = pickRandom(filterItems("uncommon_mat_wood"));
			if (baseItem.materialSecondary.equals("Leather"))out = pickRandom(filterItems("uncommon_mat_leather"));
		} else { // there was a secondary, but the percentages say we modify primary
			if (baseItem.materialPrimary.equals("Metal"))out = pickRandom(filterItems("uncommon_mat_metal"));
			if (baseItem.materialPrimary.equals("Wood"))out = pickRandom(filterItems("uncommon_mat_wood"));
			if (baseItem.materialPrimary.equals("Leather"))out = pickRandom(filterItems("uncommon_mat_leather"));
		}
		return out;
	}

	public boolean matchAnyString (String primary, String[] listToMatch) {
		for (String s : listToMatch) {
			if (s.equals(primary)) {
				return true;
			}
		}
		return false;
	}
	public boolean matchAnyStringVA (String primary, String... listToMatch) {
		for (String s : listToMatch) {
			if (s.equals(primary)) {
				return true;
			}
		}
		return false;
	}

	//pick any random item from list using weighted rarities
	public Item pickRandom(ArrayList<Item> itemsList) {
		try {
			Random rand = new Random();
			int totalWeight = 0;
			int curWeight = 0;

			for (Item item : itemsList) {
				totalWeight += item.rarity;
			}
			int value = rand.nextInt(totalWeight);

			for (Item item : itemsList) {
				curWeight += item.rarity;
				if (value < curWeight) {
					return item;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Item.empty();
	}

	//pick any random item from list using weighted rarities
	public MagicComponent pickRandomMagic(ArrayList<MagicComponent> bitsList) {
		try {
			Random rand = new Random();
			return bitsList.get(rand.nextInt(bitsList.size()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return MagicComponent.empty();
	}
	
	//read text file into array of text lines
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

	//if any files have no randomisation weights, those are added with this
	public void appendWeights (ArrayList<File> files) {
		for (File f : files) {
			String[] lines = readFileToArray(f);
			/*if (!lines[0].contains(",")) {
				try {
					FileOutputStream fos = new FileOutputStream(f);
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
					for (String sl : lines) {
						sl = "1,"+sl;
						bw.write(sl);
						bw.newLine();
					}
					bw.close();
				} catch (IOException e) {
					output.print("Error appending weights");
				}
			}
			ArrayList<String> alLines = new ArrayList<String>();
			for (String l : lines) {
				alLines.add(l.substring(l.indexOf(" ")).trim());
			}
			try {
				FileOutputStream fos = new FileOutputStream(f);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
				for (String sl : alLines) {
					//sl = "1," + sl;
					bw.write(sl);
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				output.print("Error appending weights");
			}*/
		}
	}
	
	//randomise percentage chance and output as boolean
	public boolean boolPercentage (float percentage) {
		Random rand = new Random();
		return (rand.nextFloat()*100 <= percentage);
	}

	public static ArrayList<Item> cleanItems (ArrayList<Item> in) {
		ArrayList<Item> out = new ArrayList<Item>(in);

		for (Item i : out) {
			i.clean();
		}

		return out;
	}

}
