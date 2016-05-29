package edu.macalester.mscs.characters;

import edu.macalester.mscs.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates new versions of the character list and the input text, replacing multiple-word names with hyphenated versions.
 * These new files are created using new names (so the old input files are unchanged).
 *
 * For example, Jon Snow becomes Jon-Snow and Hand of the King becomes Hand-of-the-King.
 * When the text is processed, this will now be treated as a single word unit.
 *
 * We assume that each line in the character list is of the form
 *     unique id, name1, name2, name3, ...
 * where the names are ordered by reverse inclusion. In other words, the line for Jon Snow would look like:
 *     Jon Snow, Jon Snow, Lord Snow, Snow, Jon
 * Here, "Jon" must come after "Jon Snow" since "Jon" is a substring of "Jon Snow"
 * This line will be replaced by a hyphenated version:
 *    Jon-Snow, Jon-Snow, Lord-Snow, Snow, Jon
 * and the text will likewise be updated.
 *
 * Created by abeverid on 4/29/16.
 */
public class CharacterTokenUpdater {


    // Game of Thrones
//    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-list-final.csv";
//    public static final String TEXT_FILE_NAME = "src/main/resources/text/gameofthrones-hyphenated-update.txt";
//    public static final String UPDATED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/got-list-merged.csv";
//    public static final String UPDATED_TEXT_FILE_NAME = "src/main/resources/text/gameofthrones-merged.txt";



    // Clash of Kings
//    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-merged-temp3.csv";
//    public static final String TEXT_FILE_NAME = "src/main/resources/text/clashofkings-merged-1.txt";
//    public static final String UPDATED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/cok-list-merged.csv";
//    public static final String UPDATED_TEXT_FILE_NAME = "src/main/resources/text/clashofkings-merged-2.txt";



    // Storm of Swords
    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/sos-list-merged.csv";
    public static final String TEXT_FILE_NAME = "src/main/resources/text/stormofswords-hyphenated.txt";
    public static final String UPDATED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/sos-list-merged-clean.csv";
    public static final String UPDATED_TEXT_FILE_NAME = "src/main/resources/text/stormofswords-merged.txt";


    // Feast for Crows
//    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/ffc-list-clean-redundant-ordered.csv";
//    public static final String TEXT_FILE_NAME = "src/main/resources/text/feastforcrows-clean.txt";
//    public static final String UPDATED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/ffc-list-curated-hyphenated.csv";
//    public static final String UPDATED_TEXT_FILE_NAME = "src/main/resources/text/feastforcrows-hyphenated.txt";

    // Feast for Crows
//    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/dwd-list-clean-updated.csv";
//    public static final String TEXT_FILE_NAME = "src/main/resources/text/dancewithdragons-updated.txt";
//    public static final String UPDATED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/dwd-list-curated-hyphenated.csv";
//    public static final String UPDATED_TEXT_FILE_NAME = "src/main/resources/text/dancewithdragons-hyphenated.txt";


//    public static final String CHARACTER_FILE_NAME = "src/main/resources/data/characters/pp-characters.csv";
//    public static final String TEXT_FILE_NAME = "src/main/resources/text/prideandprejudice.tex";
//    public static final String UPDATED_CHARACTER_FILE_NAME = "src/main/resources/data/characters/pp-char-hyphen.csv";
//    public static final String UPDATED_TEXT_FILE_NAME = "src/main/resources/text/pandp-hyphen.txt";




    public static void main(String[] args) {

        List<String> charLines = FileUtils.readFile(CHARACTER_FILE_NAME);
        List<String> tokens = new ArrayList<>();
        List<String> tokensNoSpaces = new ArrayList<>();

        // make hyphenated version
        List<String> newCharLines = new ArrayList<>();
        for (String line : charLines) {
            // update for hyphenated character name file
            newCharLines.add(line.replace(" ", "-"));

            /// update list of tokens to be replaced in text
            String[] splitLine = line.split(",");

            // Note that we SKIP the first entry in each line (the unique id).
            // This entry MUST repeat in the list of values that follow!
            // The unique id is often a short version of the name, while the remaining entries must be ordered
            // so that if name1 appears before name2 then name1 is NOT a substring of name2

            for (int i = 1; i < splitLine.length; i++) {
                String word = splitLine[i];
                if (word.contains(" ")) {
                    System.out.println("adding: [" + word + "]");
                    tokens.add(word);
                    tokensNoSpaces.add(word.replace(" ", "-"));
                } else {
                    System.out.println("skipping: [" + word + "]");
                }
            }
        }

        List<String> text = FileUtils.readFile(TEXT_FILE_NAME);
        List<String> newText = new ArrayList<>();

        for (String line : text) {
            for (int k = 0; k < tokens.size(); k++) {
                line = line.replace(tokens.get(k), tokensNoSpaces.get(k));
            }
            newText.add(line);
        }

        FileUtils.writeFile(newText, UPDATED_TEXT_FILE_NAME);
        FileUtils.writeFile(newCharLines, UPDATED_CHARACTER_FILE_NAME);
    }
}
