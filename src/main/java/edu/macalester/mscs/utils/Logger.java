package edu.macalester.mscs.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ari Weiland
 */
public class Logger {

    private final List<String> log;

    public Logger() {
        this(new ArrayList<String>());
    }

    public Logger(List<String> log) {
        this.log = log;
    }

    public void log() {
        log((Object) null);
    }

    public void log(List<?> list) {
        for (Object o : list) {
            log(o);
        }
    }

    public void log(Object o) {
        if (o == null) {
            log.add("");
            System.out.println();
        } else {
            log.add(o.toString());
            System.out.println(o.toString());
        }
    }

    public void writeLog(String file) {
        FileUtils.writeFile(log, file);
    }

    public void clear() {
        log.clear();
    }
}
