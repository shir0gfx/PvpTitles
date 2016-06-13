/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.jlh.pvptitles.Backend.Exceptions;

import static es.jlh.pvptitles.Backend.Exceptions.DBException.POSSIBLE_ERRORS.DB_CONNECTION;
import static es.jlh.pvptitles.Backend.Exceptions.DBException.POSSIBLE_ERRORS.NOT_FOUND;
import es.jlh.pvptitles.Main.Handlers.DBHandler;
import es.jlh.pvptitles.Main.PvpTitles;
import java.util.HashMap;
import java.util.Map;

/**
 * If you don't know the error cause, please, report it.
 *
 * @author AlternaCraft
 */
public class DBException extends Exception {

    private static final short SIMPLIFIED = 0;
    private static final short ESSENTIAL = 1;
    private static final short FULL = 2;
    
    public static final String UNKNOWN_ERROR = "Unknown error";

    //private final String 
    private final String REPORT = "If you don't know the error cause, please, report it.\n"
            + "http://dev.bukkit.org/bukkit-plugins/pvptitles/create-ticket/";

    private TYPE type = null;
    private HashMap<String, Object> data = new HashMap();
    private String custom_error = null;

    public enum TYPE {
        PLAYER_CONNECTION,
        PLAYER_FAME_SAVING,
        PLAYER_FAME_LOADING,
        PLAYER_TIME_SAVING,
        PLAYER_TIME_LOADING,
        PLAYERS_TOP,
        BOARD_SAVING,
        BOARD_REMOVING,
        BOARD_UPDATING,
        BOARD_SEARCHING
    }

    public enum POSSIBLE_ERRORS {
        NOT_FOUND(0, "Couldn't find a reason for the error..."),
        DB_CONNECTION(1, "The server has lost the MySQL connection");

        private int error_num = -1;
        private String error_str = null;

        private POSSIBLE_ERRORS(int num, String msg) {
            this.error_num = num;
            this.error_str = msg;
        }

        public String getText() {
            return this.error_str + " (0x" + this.error_num + ")";
        }
    }

    public DBException(String message, TYPE type) {
        super(message);
        this.type = type;
    }

    public DBException(String message, TYPE type, String custom_error) {
        super(message);
        this.type = type;
        this.custom_error = custom_error;
    }

    public DBException(String message, TYPE type, HashMap<String, Object> data) {
        super(message);
        this.type = type;
        this.data = data;
    }

    public TYPE getType() {
        return type;
    }

    public String getCustomMessage() {
        int n = PvpTitles.getInstance().manager.params.getErrorFormat();

        switch (n) {
            case SIMPLIFIED:
                return getHeader();
            case ESSENTIAL:
                return new StringBuilder(getHeader())
                        .append(getBody()).toString();
            case FULL:
                return new StringBuilder(getHeader())
                        .append(getExtraData())
                        .append(getBody())
                        .append(getReportMessage()).toString();
            default:
                return "";
        }
    }

    private String getHeader() {
        return new StringBuilder("(" + DBHandler.tipo.toString() + " ERROR) ")
                .append("On ").append(getFilteredString(this.type.toString()))
                .append(" gets \"").append(this.getMessage()).append("\"").toString();
    }

    private String getBody() {
        return new StringBuilder()
                .append("\n\nPossible reason/s for the error:")
                .append("\n--------------------------------")
                .append(getPossibleError()).append("\n").toString();
    }

    private String getReportMessage() {
        return new StringBuilder()
                .append("\n-------------------------------------------------------------\n")
                .append(this.REPORT)
                .append("\n-------------------------------------------------------------").toString();
    }

    private String getFilteredString(String str) {
        return "\"" + str.replaceAll("_", " ") + "\"";
    }

    private String getPossibleError() {
        String possible_errors = "";

        for (Map.Entry<String, Object> entry : this.data.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue().toString();

            if (k.contains("MySQL") && k.contains("connection")) {
                if (v.equals("false")) {
                    possible_errors += "\n- " + DB_CONNECTION.getText();
                }
            }
        }

        return (possible_errors.isEmpty()) ? "\n- " + NOT_FOUND.getText() : possible_errors;
    }

    private String getExtraData() {
        String extradata = "";

        if (!this.data.isEmpty()) {
            extradata = "\n\nMore information:";
            extradata += "\n-----------------";
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                extradata += new StringBuilder().append("\n- ").append(key)
                        .append(": ").append(value).toString();
            }
        } else if (this.custom_error != null) {
            extradata = "\nMore information: " + this.custom_error;
        }

        return extradata;
    }
}