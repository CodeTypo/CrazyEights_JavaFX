import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlayerReactive {
    /**
     * We need to track player's cards to update them on screen.
     * Selected cards will be marked in Card's selected field.
     */
    private ObservableList<Card> cards = FXCollections.observableArrayList();

    /**
     * We need to track actual suit, because when crazy eight
     * is selected, player can select appropriate symbol which
     * can be somewhat updated on screen.
     */
    private ObservableObjectValue<Suit> suit = new SimpleObjectProperty<>();

}
