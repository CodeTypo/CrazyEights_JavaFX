import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameModelReactive {

    private ObservableList<Card> cards = FXCollections.observableArrayList();

    private ObservableObjectValue<Player> turnPlayer = new SimpleObjectProperty<>();


}
