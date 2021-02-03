import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents card.
 */
@Getter
@Setter
public class CardReactive {

    /**
     * Image of the back of the Card.
     */
    private static Image cardBack = SVGUtils.getImageFromSVG("/imagesSVG/1B.svg");

    /**
     * We may need to track particular card if it is selected or not
     * to paint it appropriately without rendering whole collection.
     * It may allow us to update style of particular Card when its state changes.
     */
    private SimpleBooleanProperty selected = new SimpleBooleanProperty();

    /**
     * Suit of the Card.
     */
    private Suit suit;

    /**
     * Denomination of the Card.
     */
    private Denomination denomination;

    /**
     * Image of the Card.
     */
    private Image cardFront;

    /**
     * Public constructor with two parameters.
     * @param suit represents suit of the Card.
     * @param denomination represents denomination of Card.
     */
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

    public void setSelected(boolean selected) {
        this.selected.set(selected);
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

    /**
     * @return id of the Card.
     */
    public String getId() {
        return denomination.getS() + suit.getS();
    }
}
