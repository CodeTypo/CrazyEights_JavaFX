import java.util.List;

public class Player {
    private List<Card> cards;

    /**
     * Deal card and put into own cards.
     */
    public void dealCard(Card card){
        cards.add(card);
    }

}
