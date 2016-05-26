package edu.macalester.mscs.network;

import edu.macalester.mscs.utils.EntryComparator;
import edu.macalester.mscs.utils.FileUtils;
import edu.macalester.mscs.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixConstructor {

    public static final String LOG_FOLDER = "src/main/resources/data/output";

	private final int bookNumber;
	private final String text;
	private List<String> fullCharacterList;
	private Map<String, Integer> nameIndices;
	private int radius;
	private int noise;
	private Matrix matrix = null;

    /**
     * Constructor
	 * @param bookNum The volume in the series. Will appear in the output file names.
	 * @param textFileName The name of the file containing the book itselt
	 * @param characterFileName The name of the CSV file containing the list of characters and nicknames
	 * @param radius
	 * @param noise
	 */
    public MatrixConstructor(int bookNum, String textFileName, String characterFileName, int radius, int noise) {
		this.bookNumber = bookNum;
		this.text = getText(textFileName);
		makeCharacters(characterFileName);
		this.radius = radius;
		this.noise = noise;

        // eventually add getters and setters for these
	}

	public int getBookNumber() {
		return bookNumber;
	}

	public String getText() {
		return text;
	}

    /**
     * Override this method if you want to specify a different order for the JSON file creation.
     * This list must match the characters that actually appear in the network (and is a subset of the
     * full list of characters).
     * @return null
     */
    public String[] getOrderedCharacters() {
        return null;
    }

	/**
	 * Override this method if you want to create a CSV for the nodes with additional attributes
	 * TODO make this abstract to force the user to implement it
	 * @return
     */
	public String getCharacterDataFileName() { return null; }

	public List<String> getFullCharacterList() {
		return fullCharacterList;
	}

	public Map<String, Integer> getNameIndices() {
		return nameIndices;
	}

	public void makeCharacters(String characterFileName) {
		List<String> lines = FileUtils.readFile(characterFileName);
		fullCharacterList = new ArrayList<>();
		nameIndices = new HashMap<>();
		for (String c : lines) {
			String[] split = c.split(",");
			for (String name : split) {
				nameIndices.put(name, fullCharacterList.size());
			}
			c = split[0];
			c = c.replaceAll(" [a-z]+ ", " "); // remove lowercase filler words
			if (c.contains(" ")) {
				c = c.substring(0, c.indexOf(' ') + 2);
			}
			fullCharacterList.add(c);
		}
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getNoise() {
		return noise;
	}

	public void setNoise(int noise) {
		this.noise = noise;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	/**
	 * This primary workhorse method builds a matrix from a characterString, text, and other parameters.
	 * The log data is written to the console and printed to logFile if specified, or ignored if null.
	 * @return
	 */
    public void constructMatrix(int fileNumber, String fileDescriptor, String logFolder) {
        String logFile = getFileName(logFolder, "log", fileNumber, fileDescriptor, "txt");

        System.out.println("Log file name=" + logFile);

		Logger logger = new Logger();
		logger.log("=============================================================");
		logger.log("=================== PART 1: General Info ====================");
		logger.log("=============================================================");
		logger.log();
		logger.log("Proximity check range: " + radius);
		logger.log("Noise threshold level: " + noise);
		logger.log();
		logger.log("Character list:");
		logger.log(fullCharacterList);
		logger.log();
		logger.log("Name Indices:");
		List<Map.Entry<String, Integer>> entries = new ArrayList(nameIndices.entrySet());
		entries.sort(EntryComparator.ASCENDING);
		logger.log(entries);
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("================== PART 2: Edge Collection ==================");
		logger.log("=============================================================");
		logger.log();

		matrix = new Matrix(fullCharacterList, nameIndices, text, radius);

		logger.log(matrix.getEncounterList());
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("=================== PART 3: Refining Data ===================");
		logger.log("=============================================================");
		logger.log();
		logger.append(matrix.cleanNoise(noise));
		logger.append(matrix.cleanFloaters());
		logger.log();
		logger.log();
		logger.log("=============================================================");
		logger.log("========================= End of log ========================");
		logger.log("=============================================================");
		logger.log();

		if (logFile != null) {
			logger.writeLog(logFile);
		}

	}

	/**
	 * Writes the data in matrix to a set of files. These files are:
	 *  - matrix file
	 *  - edge file
	 *  - total encounter list
	 *  - encounter lists by character
	 * The files will be descriptively named and placed within parentFolder,
	 * with the encounter files placed in their own subdirectory.
	 * @param logFolder
	 */
    public void writeFiles(int fileNumber, String fileDescriptor, String logFolder, boolean encounterListsByCharacter) {
		if (matrix == null) {
			throw new IllegalStateException("The matrix has not been constructed");
		}

		// write encounters file
		Logger logger = new Logger();
		logger.log("char 1, char2, index, text");
		logger.log(matrix.getEncounterList());
		logger.writeLog(getFileName(logFolder, "encounters", fileNumber, fileDescriptor, "csv"));
		if (encounterListsByCharacter) { // optional
			String encountersFolder = getFileName(logFolder, "encounters", fileNumber, fileDescriptor);

			for (String name : matrix.getCharacters()) {
                logger.clear();
                logger.log(name + " Encounters:");
                logger.log(matrix.getEncounterList(name));
                logger.writeLog(encountersFolder + '/' + name.replace(' ', '_') + ".csv");
            }
		}
		// write matrix CSV file
		matrix.toMatrixCsvLog().writeLog(getFileName(logFolder, "mat", fileNumber, fileDescriptor, "csv"));

		// write edge file
		matrix.toEdgeListCsvLog().writeLog(getFileName(logFolder, "edge", fileNumber, fileDescriptor, "csv"));

		// write node file
		if (getCharacterDataFileName() != null) {
			matrix.toNodeListCsvLog(getCharacterDataFileName()).writeLog(getFileName(logFolder, "node", fileNumber, fileDescriptor, "csv"));
		}

		// write matrix JSON file
        matrix.toMatrixJsonLog(getOrderedCharacters()).writeLog(getFileName(logFolder, "mat", fileNumber, fileDescriptor, "json"));


	}

	/**
	 * Returns a file name for a directory in parentFolder, with name
	 *
	 * GoT[bookNumber]-[type][fileNumber]-[descriptor]
	 *
	 * Note that descriptor is optional, and if specified as null or
	 * the empty string, it and the final hyphen will not be included
	 *
	 * @param parentFolder
	 * @param type
	 * @param fileNumber
	 * @param descriptor
	 * @return
	 */
	private String getFileName(String parentFolder, String type, int fileNumber, String descriptor) {
		String fileName = getFileName(parentFolder, type, fileNumber, descriptor, null);
		new File(fileName).mkdirs();
		return fileName;
	}

	/**
	 * Returns a file name in parentFolder, with name
	 *
	 * GoT[bookNumber]-[type][fileNumber]-[descriptor].[extension]
	 *
	 * Note that descriptor is optional, and if specified as null or the empty string,
	 * it and the final hyphen will not be included. Also if the '.' is included in
	 * extension, or if extension is specified as null or the empty string, the '.'
	 * will not be included. However, the getFileName method without the extension
	 * parameter should be used instead because that method also calls mkdirs() for
	 * the returned file name.
	 *
	 * @param parentFolder
	 * @param type
	 * @param fileNumber
	 * @param descriptor
	 * @param extension
	 * @return
	 */
	private String getFileName(String parentFolder, String type, int fileNumber, String descriptor, String extension) {
		if (type == null) {
			type = "";
		} else if (!type.isEmpty()) {
			type = '-' + type;
		}
		if (extension == null) {
			extension = "";
		} else if (!extension.isEmpty() && !extension.startsWith(".")) {
			extension = '.' + extension;
		}
		return parentFolder + "/GoT" + bookNumber + "-" + fileNumber + type + "-" + descriptor + extension;
	}

	/**
	 * This method reads in a massive text file and combines it into a single string,
	 * with line breaks replaced by spaces.
	 * @param file
	 * @return
	 */
	private static String getText(String file) {
		List<String> lines = FileUtils.readFile(file);
		StringBuilder sb = new StringBuilder();
		for (String line : lines) {
			sb.append(line).append(" ");
		}
		return sb.toString().trim();
	}

}
