package edu.macalester.mscs.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Logger {

    private final List<String> log;
    private final boolean verbose;

    public Logger() {
        this(false);
    }

    public Logger(boolean verbose) {
        this(new ArrayList<String>(), verbose);
    }

    public Logger(List<String> log) { this(log,false); }

    public Logger(List<String> log, boolean verbose) {
        this.log = log;
        this.verbose = verbose;
    }

    /**
     * Logs an empty line
     */
    public void log() {
        log((Object) null);
    }

    /**
     * Logs a line for the toString() representation of the object.
     * A null input is treated as logging an empty line, as log().
     * If the input object is another instance of Logger, this method
     * functions as the append() method.
     * @param o
     */
    public void log(Object o) {
        if (o == null) {
            log.add("");
            if (verbose) { System.out.println(); };
        } else {
            if (o instanceof Logger) {
                append((Logger) o);
            } else {
                log.add(o.toString());
                if (verbose) { System.out.println(o.toString()); };
            }
        }
    }

    /**
     * Logs each element in the list as a separate line
     * @param list
     */
    public void log(List<?> list) {
        for (Object o : list) {
            log(o);
        }
    }

    /**
     * Appends the log from the input logger to the end of this logger.
     * This does not modify the input logger.
     * @param logger
     */
    public void append(Logger logger) {
        log.addAll(logger.log);
    }

    /**
     * Clears the log
     */
    public void clear() {
        log.clear();
    }

    /**
     * Writes the log out to a file, line by line.
     * Does NOT clear the log automatically.
     * @param file
     */
    public void writeLog(String file) {
        FileUtils.writeFile(log, file);
    }
}
