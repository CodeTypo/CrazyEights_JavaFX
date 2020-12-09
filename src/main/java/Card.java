enum Suit {
    DIAMONDS("D"),
    CLUBS("C"),
    HEARTS("H"),
    SPADES("S");

    private String s;

    public String getS() {
        return s;
    }

    Suit(String s) {
        this.s = s;
    }
}

enum Denomination {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    private String s;

    public String getS() {
        return this.s;
    }

    Denomination(String s) {
        this.s = s;
    }
}

public class Card {
    private Suit suit;
    private Denomination denomination;

    public Suit getSuit() {
        return this.suit;
    }

    public Denomination getDenomination() {
        return this.denomination;
    }

    public Card(Suit suit, Denomination denomination) {
        this.suit = suit;
        this.denomination = denomination;
    }

    @Override
    public String toString() {
        return denomination.toString() + suit.toString();

    }
}
