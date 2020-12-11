import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

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
    private ObservableList<Suit> selectedSuit = FXCollections.observableArrayList();

    public void selectCard(Card card){
        card.setSelected(true);
    }

    public void unselectCard(Card card){
        card.setSelected(false);
    }

    public void dealCard(Card card){
        cards.add(card);
    }

    public void selectSuit(Suit suit){
        selectedSuit.clear();
        selectedSuit.add(suit);
    }

    public List<Card> playCards(){
        return cards.filtered(Card::isSelected);
    }

    public ObservableList<Card> getCards() {
        return cards;
    }

    public void setCards(ObservableList<Card> cards) {
        this.cards = cards;
    }

    public ObservableList<Suit> getSelectedSuit() {
        return selectedSuit;
    }

}
