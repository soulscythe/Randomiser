import java.io.*;
import java.util.*;

/**
 * Created by Soulscythe on 23/06/2016.
 */
public class LanguageMaker {

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
    private static Translator output;
    private String langPath;

    private ArrayList<WordComponent> components_consonants;
    private ArrayList<WordComponent> components_consonants_composite;
    private ArrayList<WordComponent> components_vowels;
    private ArrayList<WordComponent> components_vowels_composite;

    public ArrayList<String> wordList;

    private String[] wordListsUsed = {
            "english-upper.10",
            "english-upper.35",
            "english-upper.40",
            "english-upper.50",
            "english-upper.60",
            "english-upper.70",
            "english-upper.80",
            "english-upper.95",
            "english-words.10",
            "english-words.20",
            "english-words.35",
            "english-words.40",
            "english-words.50",
            "english-words.55",
            "english-words.60",
            "english-words.70",
            "english-words.80",
            "english-words.95"};

    public LanguageMaker (Translator screen) {
        output = screen;

        try {
            langPath = new File(new File(".").getAbsolutePath()).getCanonicalPath() + "\\material\\language";
        } catch (IOException e) {
            System.out.println("Error finding langPath");
        }

        System.out.println(langPath);

        //get list of folders in \src\sourcematerials (each folder represents a source material book)
        File[] componentFileList = new File(langPath + "\\components").listFiles();

        components_consonants = fileToList(new File(langPath + "\\components\\consonants.txt"));
        components_consonants_composite = fileToList(new File(langPath + "\\components\\consonants_composite.txt"));
        components_vowels = fileToList(new File(langPath + "\\components\\vowels.txt"));
        components_vowels_composite = fileToList(new File(langPath + "\\components\\vowels_composite.txt"));

        //TODO: uncomment after testing: //wordList = new ArrayList<String>(java.util.Arrays.asList(readFileToArray(new File(langPath + "\\languages\\English.txt"))));

        /*for (String s : wordListsUsed) {
            for (String s2 : readFileToArray(new File(langPath + "\\eng\\" + s))) {
                wordList.add(s2);
            }
        }

        Collections.sort(wordList, String.CASE_INSENSITIVE_ORDER);
        */

        System.out.println("Language initialisation done");

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

    public ArrayList<WordComponent> fileToList(File file) {
        ArrayList<WordComponent> out = new ArrayList<WordComponent>();
        for (String s : readFileToArray(file)) {
            out.add(new WordComponent(s.split(",")));
        }
        return out;
    }

    public void writeLanguage(String languageName, ArrayList<String> words) {
        File out = new File(langPath + "\\languages\\" + languageName + ".txt");
        try {
            FileOutputStream fos = new FileOutputStream(out);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for (String sl : words) {
                bw.write(sl);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error appending weights");
        }
    }

    public String makeWord (int maxSyllables, int minSyllables) {
        String word = "";

        int componentCount = new Random().nextInt(maxSyllables-minSyllables)+minSyllables;

        //TODO: when giving likelyness of different letter types (eg exotic), we must ensure that the randomisation chance is accounting for the frequency
        //TODO: if there is an equal chance of an 'exotic' component and a 'soft' component, the randomisation needs to know that there are still much more
        //TODO: 'soft' than 'exotic' components.
        //----------------------------------------------------------------------
        //TODO: using descriptors, account for 'start', 'mid', and 'end' eligibility, specifying what part of a word the component can be in,
        //TODO: one result is 'pryashaypr', where in this case it would be best to keep the 'pr' component as a 'start' or 'mid' piece.
        //----------------------------------------------------------------------
        //TODO: perhaps allow specification of the relative frequency of letters in desired language. EG, english has lots of e's, not many x's.

        boolean vowelNext = new Random().nextBoolean();

        ArrayList<WordComponent> fullVowels = new ArrayList<WordComponent>(components_vowels);
        fullVowels.addAll(components_vowels_composite);

        ArrayList<WordComponent> fullConsonants = new ArrayList<WordComponent>(components_consonants);
        fullConsonants.addAll(components_consonants_composite);

        for (int i = 0; i < componentCount; i++) {
            if (vowelNext) {
                word += fullVowels.get(new Random().nextInt(fullVowels.size())).component; //if vowel is next, pick a random vowel
            } else {
                word += fullConsonants.get(new Random().nextInt(fullConsonants.size())).component; //if consonant is next, pick random consonant
            }
            if (boolPercentage(99.9f)) { //95% chance to continue as normal, but a slight possibility to skip this, and randomise a new component of same type
                vowelNext = !vowelNext;
            }
        }

        return word;
    }


    public boolean boolPercentage (float percentage) {
        Random rand = new Random();
        return (rand.nextFloat()*100 <= percentage);
    }
}
