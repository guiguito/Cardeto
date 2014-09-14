package com.ggt.cardetosample.database;

/**
 * Datamodel of data to put in DB.
 *
 * @author guiguito
 */
public class Log {
    private long id;
    private String log;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return id + " - " + log;
    }
}