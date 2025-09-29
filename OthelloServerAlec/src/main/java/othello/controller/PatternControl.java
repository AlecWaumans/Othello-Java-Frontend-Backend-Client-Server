package othello.controller;

import java.util.regex.Pattern;

/**
 * @author Sami EL YAGHMOURI (2024) 
 * Represents patterns for controlling the game console.
 */
public class PatternControl {
    static final Pattern TAKE_POSITION = Pattern.compile("(\\d+) (\\d+)");
    static final Pattern SURRENDER = Pattern.compile("(s)");
    static final Pattern UNDO = Pattern.compile("(u)");
    static final Pattern REDO = Pattern.compile("(r)");
    static final Pattern HELP = Pattern.compile("(help)");

    /**
     * Retrieves an array of all defined patterns.
     *
     * @return An array of Pattern objects.
     */
    public static Pattern[] values() {
        return new Pattern[]{TAKE_POSITION, SURRENDER, UNDO, REDO, HELP};
    }
}
