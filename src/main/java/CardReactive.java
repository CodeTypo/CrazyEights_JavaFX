import javafx.beans.property.SimpleBooleanProperty;

public class CardReactive {
    /**
     * We may need to track particular card if it is selected or not
     * to paint it appropriately without rendering whole collection.
     * It may allow us to update style of particular Card when its state changes.
     */
    private SimpleBooleanProperty selected = new SimpleBooleanProperty();

}
