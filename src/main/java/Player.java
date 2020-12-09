import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> cards = new ArrayList<>();
    private List<Card> selectedCards = new ArrayList<>();
    private Suit selectedSuit = Suit.SPADES;

    /**
     * Deal card and put into own cards.
     */
    public void dealCard(Card card){
        cards.add(card);
    }

    /**
     * Select card to put on pile
     * during player's turn.
     * Player can select many cards
     * with the same denomination.
     */
    public void selectCard(Card card){
        if ( (selectedCards.isEmpty() || selectedCards.get(0).getDenomination().equals(card.getDenomination()))
                && !selectedCards.contains(card)){
            // Remove card from cards and put into selected cards
            selectedCards.add(cards.remove(cards.indexOf(card)));
        }
    }

    /**
     * Put chosen card on pile.
     * @param card chosen card to play in this turn
     * @return played card
     */
    public Card putCardOnPile(Card card){
        return selectedCards.remove(selectedCards.indexOf(card));
    }

    /**
     * Player can select suit
     * if he selected crazy eight.
     * @param suit selected suit
     */
    public void selectSuit(Suit suit){
        this.selectedSuit = suit;
    }

    /**
     * Get selected cards.
     * Used by game model to check
     * if selected cards are valid.
     * @return selected cards
     */
    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    /**
     * Get selected suit.
     * Used by game model to set
     * suit after crazy eight is played.
     * @return selected suit
     */
    public Suit getSelectedSuit() {
        return selectedSuit;
    }

    /**
     * Get cards number.
     * Used by game model to know winner.
     * @return number of cards in player collection
     */
    public int getCardsNumber() {
        return cards.size() + selectedCards.size();
    }

    /**
     * Get all player cards.
     * Used by game model when the game ends
     * to sum points collected by the winner.
     * @return all player cards
     */
    public List<Card> getAllCards(){
        cards.addAll(selectedCards);
        selectedCards.clear();
        return cards;
    }

    public List<Card> getCards() {
        return cards;
    }
}
