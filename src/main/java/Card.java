import javafx.scene.image.Image;

// WILL BE REMOVED SOON!!! DO NOT EDIT!!!
// EDIT CardReactive INSTEAD!!!

public class Card {
    private static Image cardBack = SVGUtils.getImageFromSVG("/imagesSVG/1B.svg");

    private Suit suit;
    private Denomination denomination;
    private boolean selected = false;
    private Image cardFront;

    public Card(Suit suit, Denomination denomination) {
        this.suit = suit;
        this.denomination = denomination;
        this.cardFront = SVGUtils.getImageFromSVG(SVGUtils.getSVGCardResourcePath(suit, denomination));
    }
    //private WritableImage wimg;

    public static Image getCardBack() {
        return cardBack;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public Denomination getDenomination() {
        return this.denomination;
    }

    @Override
    public String toString() {
        return denomination.toString() + suit.toString();

    }

    public Image getCardFront() {
        return cardFront;
    }

    public void setCardFront(Image cardFront) {
        this.cardFront = cardFront;
    }
}
