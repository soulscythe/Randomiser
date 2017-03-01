
public class LootType {
	//type of loot - i.e. a military container / a kitchen cupboard / a wardrobe
	
	//chances of each loot type being found in this location/container/whatever
	//chances are in percentage values
	public int alchemicals;
	public int ammunition;
	public int armour;
	public int art;
	public int clothing;
	public int games;
	public int jewellery;
	public int scrap;
	public int shields;
	public int tools;
	public int weapons;
	public int kitchenWare;
	public int magicItems;
	public int gear;

	public static final int anything = 0; //literally anything, all set at 50
	public static final int military = 1; //general military equipment
	public static final int kitchen = 2; //cupboards, shelves, mostly food related items, cutlery, cups and plates, etc.
	public static final int wardrobe = 3; //mostly clothing
	public static final int treasure = 4; //Random, high value things
	public static final int rubbish = 5; //mostly scrap
	public static final int storage = 6; //crates, barrels. Random low-value items
	public static final int nothing = 7; //nothing, all set at 0. The intention being that one would then modify to create a new set

	public static final String[] types = {"Anything","Military","Kitchen","Wardrobe","Treasure","Rubbish","Storage","Nothing"};
	
	public LootType (int type) {
		if (type == LootType.anything) {
			this.alchemicals = 50;
			this.ammunition = 50;
			this.armour = 50;
			this.art = 50;
			this.clothing = 50;
			this.games = 50;
			this.jewellery = 50;
			this.scrap = 50;
			this.shields = 50;
			this.tools = 50;
			this.weapons = 50;
			this.kitchenWare = 50;
			this.magicItems = 50;
			this.gear = 50;
		}
		else if (type == LootType.military) {
			this.alchemicals = 20;
			this.ammunition = 100;
			this.armour = 100;
			this.art = 10;
			this.clothing = 45;
			this.games = 50;
			this.jewellery = 10;
			this.scrap = 20;
			this.shields = 100;
			this.tools = 20;
			this.weapons = 100;
			this.kitchenWare = 25;
			this.magicItems = 10;
			this.gear = 90;
		}
		else if (type == LootType.wardrobe) {
			this.alchemicals = 0;
			this.ammunition = 0;
			this.armour = 1;
			this.art = 15;
			this.clothing = 95;
			this.games = 0;
			this.jewellery = 60;
			this.scrap = 20;
			this.shields = 0;
			this.tools = 5;
			this.weapons = 1;
			this.kitchenWare = 0;
			this.magicItems = 0;
			this.gear = 10;
		}
		else if (type == LootType.treasure) {
			this.alchemicals = 100;
			this.ammunition = 100;
			this.armour = 100;
			this.art = 100;
			this.clothing = 20;
			this.games = 20;
			this.jewellery = 100;
			this.scrap = 20;
			this.shields = 100;
			this.tools = 20;
			this.weapons = 100;
			this.kitchenWare = 20;
			this.magicItems = 20;
			this.gear = 50;
		}
		else if (type == LootType.rubbish) {
			this.alchemicals = 5;
			this.ammunition = 5;
			this.armour = 5;
			this.art = 2;
			this.clothing = 10;
			this.games = 5;
			this.jewellery = 2;
			this.scrap = 100;
			this.shields = 5;
			this.tools = 10;
			this.weapons = 5;
			this.kitchenWare = 10;
			this.magicItems = 1;
			this.gear = 30;
		}
		else if (type == LootType.storage) {
			this.alchemicals = 100;
			this.ammunition = 100;
			this.armour = 25;
			this.art = 5;
			this.clothing = 80;
			this.games = 50;
			this.jewellery = 5;
			this.scrap = 50;
			this.shields = 25;
			this.tools = 50;
			this.weapons = 25;
			this.kitchenWare = 50;
			this.magicItems = 5;
			this.gear = 100;
		}
		else if (type == LootType.kitchen) {
			this.alchemicals = 5;
			this.ammunition = 0;
			this.armour = 0;
			this.art = 5;
			this.clothing = 5;
			this.games = 25;
			this.jewellery = 10;
			this.scrap = 60;
			this.shields = 0;
			this.tools = 20;
			this.weapons = 0;
			this.kitchenWare = 100;
			this.magicItems = 1;
			this.gear = 30;
		}
		else if (type == LootType.nothing) {
			this.alchemicals = 0;
			this.ammunition = 0;
			this.armour = 0;
			this.art = 0;
			this.clothing = 0;
			this.games = 0;
			this.jewellery = 0;
			this.scrap = 0;
			this.shields = 0;
			this.tools = 0;
			this.weapons = 0;
			this.kitchenWare = 0;
			this.magicItems = 0;
			this.gear = 0;
		}
	}
	
	public LootType (int alchemicals,int ammunition,int armour,int art,int clothing,int games,int jewellery,int scrap,int shields,int tools,int weapons,int kitchenWare,int magicItems, int gear) {
		this.alchemicals = alchemicals;
		this.ammunition = ammunition;
		this.armour = armour;
		this.art = art;
		this.clothing = clothing;
		this.games = games;
		this.jewellery = jewellery;
		this.scrap = scrap;
		this.shields = shields;
		this.tools = tools;
		this.weapons = weapons;
		this.kitchenWare = kitchenWare;
		this.magicItems = magicItems;
		this.gear = gear;
	}

	public int combineWeights () {
		return this.alchemicals +
				this.ammunition +
				this.armour +
				this.art +
				this.clothing +
				this.games +
				this.jewellery +
				this.scrap +
				this.shields +
				this.tools +
				this.weapons +
				this.kitchenWare +
				this.magicItems +
				this.gear;
	}
}
