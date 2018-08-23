package comp1110.ass1;

import java.util.*;

/**
 * This class represents a game of 'IQPuzzlerPro', which is based directly on the puzzle
 * from 'SmartGames' called "IQPuzzlerPro"
 * <p>
 * http://www.smartgames.eu/en/smartgames/iq-puzzler-pro
 * <p>
 * The class and the those that it refer to provide the core logic of
 * the puzzle, which is used by the GUI, which runs the game in a window.
 * <p>
 * The board is composed of spaces arranged in a 11x5 grid.
 * Each space is assigned an index using row-major order, starting at the
 * top left of the board.
 * Thus the top-left space has an index of 0;
 * the top-right space has an index of 10;
 * the bottom-left space has an index of 44; and
 * the bottom-right space has an index of 54.
 * <p>
 * There are twelve puzzle pieces which are composed of various numbers of linked
 * balls, arranged in a planar manner so that they may be laid flat on the grid
 * Each piece's position is described in terms of its origin, which is the
 * top-left-most ball when in its unrotated state (as illustrated above).
 * <p>
 * The puzzle uses a 'placement string' to represent the state of the board.
 * A placement string is composed of zero or more 'piece placement strings',
 * indicating the location of each piece that has already been placed on the board.
 * <p>
 * A piece placement string consists of four characters:
 * - The first character identifies which of the 12 pieces is being placed ('A' to 'L').
 * - The second character identifies the column in which the origin of the tile is placed ('A' to 'K').
 * - The third character identifies the row in which the origin of the tile is placed ('A' to 'E').
 * - The fourth character identifies which orientation the tile is in ('A' to 'H').
 * <p>
 * The default (unrotated) orientation is 'A'.
 * Orientation 'B' means the piece is rotated 90 degrees clockwise;
 * 'C' means the piece is rotated 180 degrees; and
 * 'D' means the piece is rotated 270 degrees clockwise.
 * Rotations 'E' through 'H' mean the piece is flipped horizontally
 * (i.e. reflected about the y-axis) before rotating clockwise.
 * <p>
 * Assume that in its default orientation, a piece is M columns wide and
 * N rows tall.
 * After a 90 degree rotation, it will be N columns wide and M rows tall.
 * To make rotation regular and ensure that rotated pieces correctly align
 * with the quilt board squares, rotation is performed so that the top-left
 * hand corner of the MxN bounding box is always in the same place.
 */
public class IQPuzzlerPro {
    /* constants describing the shape of the board */
    public static final int ROWS = 5;
    public static final int COLS = 11;
    public static final int SPACES = ROWS * COLS; // number of spaces on board

    /* a trivial objective that can be used to drive the game */
    public static final String TRIVIAL_OBJECTIVE = "ADDDBCBDCACDDHBAEICDFGAAGGDGHECFIEAFJJABKBAE";
    public static final String TRIVIAL_SOLUTION = "ADDDBCBDCACDDHBAEICDFGAAGGDGHECFIEAFJJABKBAELAAB";

    /* empty placement string for unplaced pieces */
    public static final String NOT_PLACED = "";

    /* a set of progressively harder objectives that can be used to drive the game */
    public static final String[][] SAMPLE_OBJECTIVES = {
            /* EASY */
            {"ADDDBCBDCACDDGAAGGDGHECFIEAFKBAELAAB", "AGDDDIAAECCDFAAHGADGHBABJEBBKEAELDAD", "AAADBGAADACDEBBCFGCFIDBDJCDCKBAALFAB"},
            /* HARDER */
            {"AEDCBEAADAADGHAEHBAEIDBHJADCLACC", "AADDBCADCFAADDABEBCDGAAHHDDAIFCF", "AFAABEDCCABDDCABIAAHJADCKEADLGBD"},
            /* HARD */
            {"ABAAEDABGIACKFAALAAD", "AHABEDAAGFAFHABAKAAE"},
            /* HARDEST */
            {"CGABDEABECBAKAAE", "AGAAGAAGHEBAKCAA"},
    };


    private String objective;         // the objective of this instance of the game
    private String solution;          // the solution to the current game

    /**
     * Constructor for a game, given a level of difficulty for the new game
     * <p>
     * This should create a new game with a single valid solution and a level of
     * difficulty that corresponds to the argument difficulty.
     *
     * @param difficulty A value between 0.0 (easiest) and 10.0 (hardest) specifying the desired level of difficulty.
     */
    public IQPuzzlerPro(double difficulty) {
        objective = establishInterestingObjective(difficulty);
        if (objective == null)
            objective = establishSimpleObjective(difficulty);
    }


    /**
     * Constructor for a game, given a particular objective for that game
     * <p>
     * This should create a new game with the given objective.
     *
     * @param objective The objective for the new game
     */
    public IQPuzzlerPro(String objective) {
        this.objective = objective;
    }

    /**
     * @param piece the name of the piece, 'A'-'L'
     * @return true if the placement of the given piece is fixed in current puzzle objective;
     * otherwise return false
     */

    //TASK 2
    public boolean isPieceFixed(char piece) {
        if (objective != null) {
            for (int i = 0; i < objective.length() / 4; i++) {
                if (objective.charAt(i * 4) == piece) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Set the game's objective using the given difficulty level and the sample
     * objectives provided in SAMPLE_OBJECTIVES.
     * <p>
     * The code should index into the samples according to the difficulty, using the
     * first arrays for difficulty values less than 2.5/10, the next for values
     * less than 5.0/10, the next for values less than 7.5/10, and the last for
     * the remaining values.
     * <p>
     * Note that difficulty is a double in the range 0.0 and 10.0.  It may take on any
     * value in the range 0.0 to 10.0.   Your task is to map those values to the
     * SAMPLE_OBJECTIVES provided.
     * <p>
     * The code should choose within the arrays randomly, so for a given difficulty
     * level, any one of the sample values might be used.
     * <p>
     * For example, if the difficulty level was 1/10, then the first array ('EASY')
     * of values should be used.   A random number generator should then choose
     * an index between 0 and 2 and set the objective accordingly, so if the randomly
     * generated value was 1, then it would choose the objective
     * "AGDDDIAAECCDFAAHGADGHBABJEBBKEAELDAD" and so on.
     *
     * @param difficulty A value between 0.0 (easiest) and 10.0 (hardest) specifying the desired level of difficulty.
     */
    public static String establishSimpleObjective(double difficulty) {

        if (difficulty < 2.5) {
            int i = 0;
            int j = (int) (SAMPLE_OBJECTIVES[i].length * Math.random());
            return SAMPLE_OBJECTIVES[i][j];

        } else if (difficulty < 5.0) {
            int i = 1;
            int j = (int) (SAMPLE_OBJECTIVES[i].length * Math.random());
            return SAMPLE_OBJECTIVES[i][j];

        } else if (difficulty < 7.5) {
            int i = 2;
            int j = (int) (SAMPLE_OBJECTIVES[i].length * Math.random());
            return SAMPLE_OBJECTIVES[i][j];

        } else {
            int i = 3;
            int j = (int) (SAMPLE_OBJECTIVES[i].length * Math.random());
            return SAMPLE_OBJECTIVES[i][j];
        }
    }

    /**
     * Determine whether a given piece placement string is valid.
     * A pieces placement string is valid if and only if:
     * the first character is a piece ID 'A'-'L';
     * the second character is a column 'A'-K';
     * the third character is a row 'A'-'E'; and
     * the fourth character is an orientation 'A'-'H'
     *
     * @return true if the given piece placement string is valid
     */
    public static boolean isValidPiecePlacement(String piecePlacementString) {
        if (piecePlacementString.length() != 4) return false;
        if (piecePlacementString.charAt(0) < 'A' || piecePlacementString.charAt(0) > 'L') return false;
        if (piecePlacementString.charAt(1) < 'A' || piecePlacementString.charAt(1) > 'K') return false;
        if (piecePlacementString.charAt(2) < 'A' || piecePlacementString.charAt(2) > 'E') return false;
        if (piecePlacementString.charAt(3) < 'A' || piecePlacementString.charAt(3) > 'H') return false;
        return true;
    }

    /**
     * Determine whether a given placement string is valid.
     * A placement string is valid if and only if:
     * it is composed of zero or more four-character piece placement strings,
     * each piece appears only once; and
     * no two pieces overlap.
     *
     * @return true if the given placement string is valid
     */
    public static boolean isValidPlacement(String placementString) {
        HashSet<Piece> pieces = new HashSet<>();
        HashSet<Integer> covered = new HashSet<>();
        for (int offset = 0; offset < placementString.length(); offset += 4) {
            String piecePlacementString = placementString.substring(offset, offset + 4);
            if (!isValidPiecePlacement(piecePlacementString)) {
                return false;
            }
            Piece piece = Piece.valueOf(String.valueOf(piecePlacementString.charAt(0)));
            if (!pieces.add(piece)) {
                return false;
            }
            int[] pieceCover = piece.getCovered(piecePlacementString.charAt(1), piecePlacementString.charAt(2), piecePlacementString.charAt(3));
            for (int space : pieceCover) {
                if (space < 0 || space >= SPACES) {
                    return false;
                }
                if (!covered.add(space)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Set the game's objective using the given difficulty level.
     * <p>
     * This method should generate different objectives according to the following:
     * <p>
     * - It should respect the given difficulty, using some *principled* and *documented*
     * approach determining the difficulty of a particular objective.
     * <p>
     * - It should not use TRIVIAL_OBJECTIVE or objectives from SAMPLE_OBJECTIVES.
     * <p>
     * - It should provide a rich number of objectives (much more than SAMPLE_OBJECTIVES),
     * so that the player is not likely to be given the same objective repeatedly.
     * <p>
     * - It should offer a more graduated notion of difficulty levels, more than just the
     * four levels provided by SAMPLE_OBJECTIVES.   The tests expect to see difficulty
     * resolved to at least eight levels.
     * <p>
     * <p>
     * Note that difficulty is given as a double that is greater or equal to 0.0 and less than 10.0.
     * It may take on any value in the range [0,10).
     * <p>
     * This requires a deeper understanding of the problem, and some way of determining
     * what makes a particular objective difficult or easy.
     *
     * @param difficulty A value between 0.0 (easiest) and 10.0 (hardest) specifying the desired level of difficulty.
     */
    public static String establishInterestingObjective(double difficulty) {
        // FIXME Task 7: Replace this code with a good objective generator that does not draw from a simple set of samples
        return null;
    }

    /**
     * @return the objective of the current game.
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Take a non-empty string composed of four-character piece placement strings,
     * and if the piece name is 'A' through 'E' and the orientation is any char 'E'-'H',
     * replace the orientation as follows:
     * 'E' -> 'A'
     * 'F' -> 'B'
     * 'G' -> 'C'
     * 'H' -> 'D'
     * The orientation char for pieces 'F' through 'L' should not be changed.
     * <p>
     * Examples:
     * <p>
     * in:  "ADDHBGAACECF"    out: "ADDDBGAACECB"
     * in:  "BDAGFGEHGABA"    out: "BDACFGEHGABA"
     * <p>
     * Hint: You may want to convert from String to array of char using toCharArray(), and then
     * do your work using the char array before converting back by creating a new String with
     * the char array as the argument to the constructor.
     *
     * @param in A string composed of four-character piece placement strings
     * @return the input string with corrected orientations for pieces 'A' through 'E'
     */
//Task 3
    public static String fixOrientations(String in) {

            char[] chs = in.toCharArray();
            for (int i = 0; i < chs.length / 4; i++) {
                char ch_name = chs[i * 4 + 0];
                char ch_col = chs[i * 4 + 1];
                char ch_row = chs[i * 4 + 2];
                char ch_ori = chs[i * 4 + 3];
                if ((ch_name == 'A' || ch_name == 'B' || ch_name == 'C' || ch_name == 'D' || ch_name == 'E')
                        && (ch_ori == 'E' || ch_ori == 'F' || ch_ori == 'G' || ch_ori == 'H')) {
                    if (ch_ori == 'E') {
                        chs[i * 4 + 3] = 'A';
                    } else if (ch_ori == 'F') {
                        chs[i * 4 + 3] = 'B';
                    } else if (ch_ori == 'G') {
                        chs[i * 4 + 3] = 'C';
                    } else if (ch_ori == 'H') {
                        chs[i * 4 + 3] = 'D';
                    }
                }
            }
            in = new String(chs);
        return in;
    }


    /**
     * Take a non-empty string composed of four-character piece placement strings,
     * and if the piece name is 'B' or 'D' and the orientation is any char 'E'-'H',
     * replace the orientation as follows:
     * 'E' -> 'A'
     * 'F' -> 'B'
     * 'G' -> 'C'
     * 'H' -> 'D'
     * If the piece name is 'A', 'C' or 'E' and the orientation is any char 'E'-'H',
     * replace the orientation as follows:
     * 'E' -> 'B'
     * 'F' -> 'C'
     * 'G' -> 'D'
     * 'H' -> 'A'
     * If the piece name is 'H' and the orientation is 'C', 'D', 'G' or 'H',
     * replace the orientation as follows:
     * 'C' -> 'A'
     * 'D' -> 'B'
     * 'G' -> 'E'
     * 'H' -> 'F'
     * The orientation char for pieces 'F', 'G', and 'I' through 'L' should not be changed.
     * <p>
     * Examples:
     * <p>
     * in:  "ADDHBGAACECF"    out: "ADDABGAACECC"
     * in:  "BDAGFGEHGABA"    out: "BDACFGEHGABA"
     * in:  "HADCDABAICCD"    out: "HADADABAICCD"
     * <p>
     * Hint: You may want to convert from String to array of char using toCharArray(), and then
     * do your work using the char array before converting back by creating a new String with
     * the char array as the argument to the constructor.
     *
     * @param in A string composed of four-character piece placement strings
     * @return the input string with corrected orientations for pieces 'A' through 'E'
     */
    public static String fixOrientationsProperly(String in) {
        char[] placementChars = in.toCharArray();
        for (int i = 0; i < in.length(); i += 4) {
            int rotation = placementChars[i + 3] - 'A';
            if ((placementChars[i] == 'A' || placementChars[i] == 'C' || placementChars[i] == 'E')
                    && (rotation > 4)) {
                rotation = (rotation + 1) % 4;
            } else if ((placementChars[i] == 'B' || placementChars[i] == 'D')
                    && (rotation > 4)) {
                rotation -= 4;
            } else if ((placementChars[i] == 'H')
                    && (rotation % 4 == 2 || (rotation % 4 == 3))) {
                rotation -= 2;
            }
            placementChars[i + 3] = (char) (rotation + 'A');
        }
        return String.valueOf(placementChars);
    }

    /**
     * Find all solutions to this game, and return them as an array of strings, each string
     * describing a placement of the pieces as a sequence of four-character piece placement
     * strings.
     * <p>
     * Invalid piece orientations should be fixed using the fixOrientationsProperly method.
     * <p>
     * Invalid solutions should not be returned by this method.
     *
     * @return An array of strings representing the set of all solutions to this puzzle.
     * If there are no solutions, the array should be empty (not null).
     */
    public String[] getSolutions() {
        // FIXME Task 6: replace this code with code that determines all solutions for this puzzle's objective
        return new String[]{TRIVIAL_SOLUTION};
    }

    /**
     * Return the solution to the puzzle.  The solution is calculated lazily, so first
     * check whether it's already been calculated.
     *
     * @return A string representing the solution to this habitat.
     */
    public String getSolution() {
        if (solution == null) setSolution();
        return solution;
    }

    /**
     * Establish the solution to this puzzle.
     */
    private void setSolution() {
        String[] solutions = getSolutions();
        if (solutions.length != 1) {
            throw new IllegalArgumentException("IQPuzzlerPro " + objective + " " + (solutions.length == 0 ? " has no " : " has more than one ") + "solution");
        } else
            solution = solutions[0];
    }
}
