enum Suit {
    DIAMONDS,
    CLUBS,
    HEARTS,
    SPADES
}

enum Denomination {
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE
}

public class Card {
    Suit suit;
    Denomination denomination;

    public Card(Suit suit, Denomination denomination) {
        this.suit = suit;
        this.denomination = denomination;
    }
}
