import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enum represents suit of the card.
 */
@Getter
@AllArgsConstructor
public enum Suit {
    DIAMONDS("D"),
    CLUBS("C"),
    HEARTS("H"),
    SPADES("S");

    private String s;

    public Image getSymbol() {
        return SVGUtils.getSymbol(this);
    }
}
