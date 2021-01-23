import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Suit {
    DIAMONDS("D"),
    CLUBS("C"),
    HEARTS("H"),
    SPADES("S");

    private String s;

    public String getS() {
        return s;
    }

    public Image getSymbol() {
        return SVGUtils.getSymbol(this);
    }
}
