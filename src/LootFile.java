import java.io.File;

public class LootFile {
	String name;
	String filename;
	String[] lines;
	File orig;
	
	LootFile(String name, String filename, String[] lines, File orig) {
		this.name = name;
		this.filename = filename;
		this.lines = lines;
		this.orig = orig;
	}
	LootFile () {
		this.name = "";
		this.filename = "";
		this.lines = new String[0];
		this.orig = null;
	}
}
