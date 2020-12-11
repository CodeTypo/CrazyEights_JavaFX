import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;

public class CardReactive {
    private static Image cardBack = SVGUtils.getImageFromSVG("/imagesSVG/1B.svg");

    /**
     * We may need to track particular card if it is selected or not
     * to paint it appropriately without rendering whole collection.
     * It may allow us to update style of particular Card when its state changes.
     */
    private SimpleBooleanProperty selected = new SimpleBooleanProperty();

    private Suit suit;
    private Denomination denomination;

    private Image cardFront;

    public CardReactive(Suit suit, Denomination denomination) {
        this.suit = suit;
        this.denomination = denomination;

        selected.set(false);
        cardFront = SVGUtils.getImageFromSVG(
                SVGUtils.getSVGCardResourcePath(suit, denomination));
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }

    public static Image getCardBack() {
        return cardBack;
    }

    public static void setCardBack(Image cardBack) {
        CardReactive.cardBack = cardBack;
    }

    public Image getCardFront() {
        return cardFront;
    }

    public void setCardFront(Image cardFront) {
        this.cardFront = cardFront;
    }

    @Override
    public String toString() {
        return "CardReactive{" +
                "suit=" + suit +
                ", denomination=" + denomination +
                '}';
    }
}
