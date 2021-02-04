import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * This abstract class is extended by <code>BotPlayerReactive</code> and <code>InteractivePlayerReactive</code>.
 */
@Getter
@Setter
public abstract class PlayerReactive {
    /**
     * We need to track player's cards to update them on screen.
     * Selected cards will be marked in CardReactive's selected field.
     */
    private ObservableList<CardReactive> cards = FXCollections.observableArrayList();

    /**
     * This field represents selected Cards by the player.
     */
    private ObservableList<CardReactive> selectedCards = FXCollections.observableArrayList();

    /**
     * We need to track actual suit, because when crazy eight
     * is selected, player can select appropriate symbol which
     * can be somewhat updated on screen.
     **/
    private SimpleObjectProperty<Suit> selectedSuit = new SimpleObjectProperty<>();

    /**
     * This method allows the player to select cards with the same denomination.
     * @param card represents card we want to select.
     */
    public void selectCard(CardReactive card) {
        if (selectedCards.isEmpty()
                || selectedCards.get(0).getDenomination().equals(card.getDenomination())) {
            card.setSelected(true);
            selectedCards.add(card);
        }
    }

    public ObservableList<CardReactive> getCards() {
        return cards;
    }

    /**
     *
     * @param card represents card we want to unselect.
     */
    public void unselectCard(CardReactive card) {
        selectedCards.remove(card);
        card.setSelected(false);
    }

    /**
     *
     * @param card represents the card to deal
     */
    public void dealCard(CardReactive card) {
        cards.add(card);
    }

    public void selectSuit(Suit suit) {
        selectedSuit.set(suit);
    }

    public void removeSelectedCards(List<CardReactive> playedCards) {
        cards.removeAll(playedCards);
    }

    public List<CardReactive> getSelectedCards() {
        return selectedCards;
    }

    public Suit getSelectedSuit() {
        return selectedSuit.get();
    }
}
