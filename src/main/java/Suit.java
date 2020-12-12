import javafx.scene.image.Image;

public enum Suit {
    DIAMONDS("D"),
    CLUBS("C"),
    HEARTS("H"),
    SPADES("S");

    private String s;

    Suit(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public Image getSymbol() {
        return SVGUtils.getSymbol(this);
    }
}
