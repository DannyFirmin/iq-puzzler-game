package comp1110.ass1;

/**
 * An enumeration representing the twelve pieces in the IQPuzzlerPro game.
 * <p>
 * You may want to look at the 'Planet' example in the Oracle enum tutorial for
 * an example of an enumeration.
 * <p>
 * http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 */
public enum Piece {
    A(new Square[]{new Square(0, 0), new Square(1, 0), new Square(0, 1)}),
    B(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(1, 1)}),
    C(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(0, 2)}),
    D(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(2, 1)}),
    E(new Square[]{new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(1, 1), new Square(0, 2)}),
    F(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1)}),
    G(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(0, 1), new Square(1, 1)}),
    H(new Square[]{new Square(0, 0), new Square(1, 0), new Square(1, 1), new Square(2, 1)}),
    I(new Square[]{new Square(0, 0), new Square(0, 1), new Square(1, 1), new Square(2, 1), new Square(1, 2)}),
    J(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(3, 0), new Square(0, 1)}),
    K(new Square[]{new Square(0, 0), new Square(1, 0), new Square(2, 0), new Square(3, 0), new Square(1, 1)}),
    L(new Square[]{new Square(1, 0), new Square(2, 0), new Square(3, 0), new Square(0, 1), new Square(1, 1)});

    public static final int MAX_PIECE_WIDTH = 4;

    public static class Square {
        public static final int NUM_COLUMNS = 11;
        public static final int NUM_ROWS = 5;
        final int col;
        final int row;

        Square(int col, int row) {
            this.col = col;
            this.row = row;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Square) {
                Square s = (Square) obj;
                return s.col == this.col && s.row == this.row;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return col * 37 + row;
        }

        public String toString() {
            return col + "," + row;
        }
    }

    /**
     * A list of spaces covered by this piece in its default rotation.
     * Each space in the list is given as an offset from the origin (0,0).
     */
    public final Square[] shape;

    Piece(Square[] shape) {
        this.shape = shape;
    }

    public char getId() {
        return this.name().charAt(0);
    }

    /**
     * Return indices corresponding to which board spaces would be covered
     * by this piece, given a correct provided placement.
     * If a part of the piece would lie off the board in a given orientation,
     * return -1 for that part of the piece.
     * <p>
     * Examples:
     * Given the piece placement string 'GACA' would return the indices: {22,23,24,33,34}.
     * Given the piece placement string 'HGBF' would return the indices: {17,28,29,40}.
     * Given the piece placement string 'JBCB' would return the indices: {-1,23,24,35,46}.
     * <p>
     * Hint: You can associate values with each entry in the enum using a constructor,
     * so you could use that to somehow encode the properties of each of the twelve pieces.
     * Then in this method you could use the value to calculate the required indices.
     * <p>
     * See the 'Grade' enum given in the O2 lecture as part of the lecture code (live coding),
     * for an example of an enum with associated state and constructors.
     * <p>
     * The tutorial here: http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
     * has an example of a Planet enum, which includes two doubles in each planet's
     * constructor representing the mass and radius.   Those values are used in the
     * surfaceGravity() method, for example.
     *
     * @param column      the column in which the origin of the piece is placed ('A' to 'K')
     * @param row         the row in which the origin of the tile is placed ('A' to 'E')
     * @param orientation which orientation the tile is in ('A' to 'H')
     * @return A set of indices corresponding to the board positions that would be covered by this piece
     */
    //Task 5 I asked some questions (ideas) and my friends helped me a lot with this.
    int[] getCovered(char column, char row, char orientation) {

        int M = getColumnExtent();//each PIECE column number
        int N = getRowExtent();//each PIECE row number

        int col_int = column - 'A';//PIECE top right's column
        int row_int = row - 'A';//PIECE top right's row

        int[] result = new int[shape.length];
        //shape.length=how many the index it take所占的格数
        for (int i = 0; i < shape.length; i++) {

            int shape_col = shape[i].col;//the col of each grid每格所在的列数
            int shape_row = shape[i].row;

            if (orientation >= 'E' && orientation <= 'H') {// piece is flipped horizontally, left and right exchange
                shape_col = M - 1 - shape_col;//水平翻转，就是左边和右边的位置互换
            }

            if (orientation == 'A' || orientation == 'E') {
            } else if (orientation == 'B' || orientation == 'F') {// 90 degree rotate, change col and row then right and left change
                //90度旋转，先行列位置互换，再，水平左右互换
                int tmp_col = shape_col;
                int tmp_row = shape_row;
                shape_col = tmp_row;
                shape_row = tmp_col;
                shape_col = N - 1 - shape_col;
            } else if (orientation == 'C' || orientation == 'G') {// 180 degree
                shape_col = M - 1 - shape_col;//水平左右互换 left and right exchange
                shape_row = N - 1 - shape_row;//上下位置互换 top and down exchange
            } else if (orientation == 'D' || orientation == 'H') {// 270 degree
                //270度旋转，先行列位置互换，再，上下互换, change col and row then top and down change
                int tmp_col = shape_col;
                int tmp_row = shape_row;
                shape_col = tmp_row;
                shape_row = tmp_col;
                shape_row = M - 1 - shape_row;
            }

            int c = shape_col + col_int;
            int r = shape_row + row_int;

            if (c < Square.NUM_COLUMNS && r < Square.NUM_ROWS) {
                result[i] = c + r * Square.NUM_COLUMNS;
            } else {
                //not in grid, outside of it
                result[i] = -1;
            }
        }
        return result;
    }


    public int getColumnExtent() {
        int xMax = 0;
        for (Square square : shape) {
            xMax = Math.max(xMax, square.col);
        }
        return xMax + 1;
    }

    public int getRowExtent() {
        int yMax = 0;
        for (Square square : shape) {
            yMax = Math.max(yMax, square.row);
        }
        return yMax + 1;
    }
}
