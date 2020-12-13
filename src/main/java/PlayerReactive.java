import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public abstract class PlayerReactive {
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
    private SimpleObjectProperty<Suit> selectedSuit = new SimpleObjectProperty<>();

    public void selectCard(CardReactive card){
        List<CardReactive> selCards = getSelectedCards();
        if (selCards.isEmpty()
                || selCards.get(0).getDenomination() == card.getDenomination()){
            card.setSelected(true);
        }
    }

    public void unselectCard(CardReactive card){
        card.setSelected(false);
    }

    public void dealCard(CardReactive card){
        cards.add(card);
    }

    public void selectSuit(Suit suit){
        selectedSuit.set(suit);
    }

    public void removeSelectedCards(List<CardReactive> playedCards){
        cards.removeAll(playedCards);
    }

    public List<CardReactive> getSelectedCards(){
        return cards.filtered(CardReactive::isSelected);
    }

    public ObservableList<CardReactive> getCards() {
        return cards;
    }

    public void setCards(ObservableList<CardReactive> cards) {
        this.cards = cards;
    }

    public Suit getSelectedSuit() {
        return selectedSuit.get();
    }

    public SimpleObjectProperty<Suit> selectedSuitProperty() {
        return selectedSuit;
    }

}
