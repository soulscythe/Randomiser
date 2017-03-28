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

    public int softHard, sibilance, lilt, exoticness;

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

    public LanguageMaker (Translator screen,int sh, int sib, int lilt, int ex) {
        output = screen;

        this.softHard = sh;
        this.sibilance = sib;   //test: these values from range 0 - 200. vv
        this.lilt = lilt;       //0 = never, 100 = as likely as others, 200 = double chance of this type
        this.exoticness = ex;

        try {
            langPath = new File(new File(".").getAbsolutePath()).getCanonicalPath() + "\\material\\language";
        } catch (IOException e) {
            System.out.println("Error finding langPath");
        }

        System.out.println(langPath);

        File[] componentFileList = new File(langPath + "\\components").listFiles();

        components_consonants = fileToList(new File(langPath + "\\components\\consonants.txt"));
        components_consonants_composite = fileToList(new File(langPath + "\\components\\consonants_composite.txt"));
        components_vowels = fileToList(new File(langPath + "\\components\\vowels.txt"));
        components_vowels_composite = fileToList(new File(langPath + "\\components\\vowels_composite.txt"));

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

    public void writeLanguage(String languageName) {
        /*for (String s : wordListsUsed) {
            for (String s2 : readFileToArray(new File(langPath + "\\eng\\" + s))) {
                wordList.add(s2);
            }
        }

        Collections.sort(wordList, String.CASE_INSENSITIVE_ORDER);*/

        wordList = new ArrayList<String>(java.util.Arrays.asList(readFileToArray(new File(langPath + "\\languages\\English.txt"))));

        File out = new File(langPath + "\\languages\\" + languageName + ".txt");
        try {
            FileOutputStream fos = new FileOutputStream(out);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for (String eng : wordList) {
                int minSyl,maxSyl;
                minSyl = estimateSyllables(eng)-2;
                if (minSyl<=0) minSyl = 1;
                maxSyl = estimateSyllables(eng)+2;
                String newWord = makeWord(maxSyl,minSyl);
                bw.write(eng + "," + newWord);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error writing language");
        }
    }

    public int estimateSyllables (String word) {
        int out = 1;
        boolean vowelfound = false;
        boolean vowelfound_o = false;
        int a = 'a', e = 'e', i = 'i', o = 'o', u = 'u', y = 'y';
        for (char c : word.toCharArray()) {
            vowelfound_o = vowelfound;
            if (c==a||c==e||c==i||c==o||c==u||c==y) { //current char is a vowel
                vowelfound = true;
            } else {
                vowelfound = false;
            }
            if (vowelfound == vowelfound_o) { // a change has happened
                out++;
            }
        }
        return (out);
    }

    public void updateOptions(int sh, int sib, int lilt, int ex) {
        this.softHard = sh;
        this.sibilance = sib;   //test: these values from range 0 - 200. vv
        this.lilt = lilt;       //0 = never, 100 = as likely as others, 200 = double chance of this type
        this.exoticness = ex;
    }

    public String makeWord (int maxSyllables, int minSyllables) {
        String word = "";

        int componentCount = new Random().nextInt(maxSyllables-minSyllables)+minSyllables;

        //TODO: when giving likelyness of different letter types (eg exotic), we must ensure that the randomisation chance is accounting for the frequency
        //TODO: if there is an equal chance of an 'exotic' component and a 'soft' component, the randomisation needs to know that there are still much more
        //TODO: 'soft' than 'exotic' components.
        //----------------------------------------------------------------------
        //TODO: account for apostrophisation, such as many accounts of elven using apostrophes in words. Ska'lin.
        //----------------------------------------------------------------------
        //TODO: perhaps allow specification of the relative frequency of letters in desired language. EG, english has lots of e's, not many x's.

        boolean vowelNext = new Random().nextBoolean();

        ArrayList<WordComponent> fullVowels = new ArrayList<WordComponent>(components_vowels);
        fullVowels.addAll(components_vowels_composite);

        ArrayList<WordComponent> fullVowels_starts = new ArrayList<WordComponent>();
        ArrayList<WordComponent> fullVowels_mids = new ArrayList<WordComponent>();
        ArrayList<WordComponent> fullVowels_ends = new ArrayList<WordComponent>();
        for (WordComponent wc : fullVowels) {
            if (wc.descContains("start")) fullVowels_starts.add(wc);
            if (wc.descContains("mid")) fullVowels_mids.add(wc);
            if (wc.descContains("end")) fullVowels_ends.add(wc);
        }

        ArrayList<WordComponent> fullConsonants = new ArrayList<WordComponent>(components_consonants);
        fullConsonants.addAll(components_consonants_composite);

        ArrayList<WordComponent> fullConsonants_starts = new ArrayList<WordComponent>();
        ArrayList<WordComponent> fullConsonants_mids = new ArrayList<WordComponent>();
        ArrayList<WordComponent> fullConsonants_ends = new ArrayList<WordComponent>();
        for (WordComponent wc : fullConsonants) {
            if (wc.descContains("start")) fullConsonants_starts.add(wc);
            if (wc.descContains("mid")) fullConsonants_mids.add(wc);
            if (wc.descContains("end")) fullConsonants_ends.add(wc);
        }

        for (int i = 0; i < componentCount; i++) {
            WordComponent chosen;
            if (vowelNext) {
                if (i == 0) { //start
                    chosen = fullVowels_starts.get(new Random().nextInt(fullVowels_starts.size()));
                } else if (i == (componentCount-1)) { //end
                    chosen = fullVowels_ends.get(new Random().nextInt(fullVowels_ends.size()));
                } else { // mid
                    chosen = fullVowels_mids.get(new Random().nextInt(fullVowels_mids.size()));
                }
            } else {
                if (i == 0) { //start
                    chosen = fullConsonants_starts.get(new Random().nextInt(fullConsonants_starts.size()));
                } else if (i == (componentCount-1)) { //end
                    chosen = fullConsonants_ends.get(new Random().nextInt(fullConsonants_ends.size()));
                } else { // mid
                    chosen = fullConsonants_mids.get(new Random().nextInt(fullConsonants_mids.size()));
                }
            }
            ////////////////////////////////////////////////////////////////////////////
            if (exoticness < 100) {//exoticness
                if (chosen.descContains("exotic")) { //if the wc is exotic
                    if (boolPercentage(100-exoticness)) { //re-roll, greater chance with lower exoticness
                        i--;
                        continue;
                    }
                }
            } else if (exoticness > 100) {
                if (!chosen.descContains("exotic")) { //if the wc is NOT exotic
                    if (boolPercentage(exoticness-100)) { //re-roll, greater chance with higher exoticness
                        i--;
                        continue;
                    }
                }
            }
            ////////////////////////////////////////////////////////////////////////////
            if (sibilance < 100) {//sibilance
                if (chosen.descContains("sibilant")) { //if the wc is sibilant
                    if (boolPercentage(100-sibilance)) { //re-roll, greater chance with lower sibilance
                        i--;
                        continue;
                    }
                }
            } else if (sibilance > 100) {
                if (!chosen.descContains("sibilant")) { //if the wc is NOT sibilant
                    if (boolPercentage(sibilance-100)) { //re-roll, greater chance with higher sibilance
                        i--;
                        continue;
                    }
                }
            }
            ////////////////////////////////////////////////////////////////////////////
            if (lilt < 100) {//lilt
                if (chosen.descContains("lilting")) { //if the wc is lilting
                    if (boolPercentage(100-lilt)) { //re-roll, greater chance with lower lilting
                        i--;
                        continue;
                    }
                }
            } else if (lilt > 100) {
                if (!chosen.descContains("lilting")) { //if the wc is NOT lilting
                    if (boolPercentage(lilt-100)) { //re-roll, greater chance with higher lilting
                        i--;
                        continue;
                    }
                }
            }
            ////////////////////////////////////////////////////////////////////////////
            if (!vowelNext) { //if vowel is NOW, then all vowels are soft. So skip this.
                if (softHard < 100) {//soft
                    if (!chosen.descContains("soft")) { //if the wc is NOT soft (which we want)
                        if (boolPercentage(100 - softHard)) { //re-roll, greater chance with lower lilting
                            i--;
                            continue;
                        }
                    }
                } else if (softHard > 100) { //hard
                    if (!chosen.descContains("hard")) { //if the wc is NOT hard (which we want)
                        if (boolPercentage(softHard - 100)) { //re-roll, greater chance with higher lilting
                            i--;
                            System.out.println(chosen.component + ": not hard, looping...");
                            continue;
                        }
                        System.out.println(chosen.component + ": not hard, ignoring");
                    } else System.out.println(chosen.component + ": hard");
                }
            }
            ////////////////////////////////////////////////////////////////////////////
            word += chosen.component;
            //if (boolPercentage(99.9f)) { //99.9% chance to continue as normal, but a slight possibility to skip this, and randomise a new component of same type
                vowelNext = !vowelNext;
            //}
        }

        return word;
    }

    public boolean boolPercentage (float percentage) {
        Random rand = new Random();
        return (rand.nextFloat()*100 < percentage);
    }
    public boolean boolPercentage (int percentage) {
        Random rand = new Random();
        return (rand.nextInt(100) < percentage);
    }
}
