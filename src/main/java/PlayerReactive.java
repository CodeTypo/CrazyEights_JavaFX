import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public abstract class PlayerReactive {
    /**
     * We need to track player's cards to update them on screen.
     * Selected cards will be marked in CardReactive's selected field.
     */
    private ObservableList<CardReactive> cards = FXCollections.observableArrayList();
    private ObservableList<CardReactive> selectedCards = FXCollections.observableArrayList();

    /**
     * We need to track actual suit, because when crazy eight
     * is selected, player can select appropriate symbol which
     * can be somewhat updated on screen.
     *
     * **/
    private SimpleObjectProperty<Suit> selectedSuit = new SimpleObjectProperty<>();

    public void selectCard(CardReactive card){
//        List<CardReactive> selCards = getSelectedCards();
        if (selectedCards.isEmpty()
                || selectedCards.get(0).getDenomination().equals(card.getDenomination())){
            card.setSelected(true);
//            System.out.println("LISTA");
//            System.out.println(getSelectedCards().toString());
//            cards.remove(card);
            selectedCards.add(card);

        }
    }

    public ObservableList<CardReactive> getCards() {
        return cards;
    }

    public void unselectCard(CardReactive card){

        selectedCards.remove(card);
//        cards.add(card);
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
        return selectedCards;
//        return cards.filtered(CardReactive::isSelected);
    }

    public Suit getSelectedSuit() {
        return selectedSuit.get();
    }

    public SimpleObjectProperty<Suit> selectedSuitProperty() {
        return selectedSuit;
    }
}
