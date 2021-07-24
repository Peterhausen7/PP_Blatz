package logic;

/**
 * Possible treasures
 */
public enum Treasure {
    T01,
    T02,
    T03,
    T04,
    T05,
    T06,
    T07,
    T08,
    T09,
    T10,
    T11,
    T12,
    T13,
    T14,
    T15,
    T16,
    T17,
    T18,
    T19,
    T20,
    T21,
    T22,
    T23,
    T24;

    /**
     * Gets the index of this treasure (its ordinal). Used instead of ordinal because its clearer.
     * @return the ordinal of this treasure
     */
    public int getIndex() {
        return this.ordinal();
    }

    /**
     * Gets the treasure representing the index. The index is parsed from JSON, and thus needs to be verified.
     * @param index - parsed index
     * @return treasure representing the index, if its valid
     */
    static public Treasure getByParsedNumber(int index) {
        if (index == 0) {
            return null;
        } else if (index > 0 && index <= GameLogic.AMOUNT_OF_TREASURES) {
            return Treasure.values()[index - 1];
        } else {
            throw new IllegalArgumentException(index + " is not a valid Treasure number");
        }
    }
}
