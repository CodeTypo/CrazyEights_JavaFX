import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PlayerReactive {
    /**
     * We need to track player's cards to update them on screen.
     * Selected cards will be marked in CardReactive's selected field.
     */
    private ObservableList<CardReactive> cards = FXCollections.observableArrayList();

    /**
     * We need to track actual suit, because when crazy eight
     * is selected, player can select appropriate symbol which
     * can be somewhat updated on screen.
     */
    private ObservableList<Suit> selectedSuit = FXCollections.observableArrayList();

    /**
     * We need to track if player has turn. Only player who has turn can deal and play cards.
     */
    private SimpleBooleanProperty hasTurn = new SimpleBooleanProperty();

    public void selectCard(CardReactive card){
        card.setSelected(true);
    }

    public void unselectCard(CardReactive card){
        card.setSelected(false);
    }

    public void dealCard(CardReactive card){
        cards.add(card);
    }

    public void selectSuit(Suit suit){
        selectedSuit.clear();
        selectedSuit.add(suit);
    }

    public List<CardReactive> playCards(){
        return cards.filtered(CardReactive::isSelected);
    }

    public ObservableList<CardReactive> getCards() {
        return cards;
    }

    public void setCards(ObservableList<CardReactive> cards) {
        this.cards = cards;
    }

    public ObservableList<Suit> getSelectedSuit() {
        return selectedSuit;
    }

    public void setSelectedSuit(ObservableList<Suit> selectedSuit) {
        this.selectedSuit = selectedSuit;
    }

    public boolean isHasTurn() {
        return hasTurn.get();
    }

    public SimpleBooleanProperty hasTurnProperty() {
        return hasTurn;
    }

    public void setHasTurn(boolean hasTurn) {
        this.hasTurn.set(hasTurn);
    }
}
