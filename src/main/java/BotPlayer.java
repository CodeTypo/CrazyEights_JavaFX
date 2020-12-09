import java.util.concurrent.ThreadLocalRandom;

public class BotPlayer extends Player{
    /**
     * BotPlayer contains additional method
     * that takes info about actual game state
     * and automatically selects cards to play.
     * BotPlayer doesn't have any extra info.
     * It knows only about what every other player knows
     * and have its own programmed intuition.
     * When BotPlayer has its turn, use it like this:
     * botPlayer.makeMove(...) and nothing more.
     * All the rest is controlled by gameModel itself.
     *
     * @ topPile card on pile's top, bot selects cards that fit rules
     * @return true if bot played card, false otherwise
     * if makeMove returns false, than gameModel should call gameModel.dealCard()
     * and this bot will automatically got one card from stock top.
     * We probably want to run this makeMove() in controller in loop until it returns true
     * for example: while(!botPLayer.makeMove()){gameModel.dealCard()}; gameModel.playCards();
     * Because gameModel watch for turnPLayer and let to call methods only on
     * current turnPlayer, we don't need to deal with it.
     */
    public boolean makeMove(Card topPile){
        if (!crazyEightStrategy()) // if first strategy doesnt work
            if (!sameDenominationStrategy(topPile)) // then go to second strategy
                if (!sameSuitStrategy(topPile)) // and finally to the last
                    return false; // if no strategy works return false
        return true;
    }

    /**
     *
     * @return true if strategy works (any card is selected)
     */
    private boolean crazyEightStrategy(){
        for (Card card: cards) {
            if (card.getDenomination() == Denomination.EIGHT){
                // botPlayer plays always all crazy eights in its collection at once
                selectedCards.add(card);
            }
        }
        // pick suit at random
        selectedSuit = Suit.values()[ThreadLocalRandom.current().nextInt(0, Suit.values().length)];
        return !selectedCards.isEmpty();
    }

    /**
     *
     * @param topPile
     * @return true if strategy works (any card is selected)
     */
    private boolean sameDenominationStrategy(Card topPile){
        for (Card card: cards) {
            if (card.getDenomination() == topPile.getDenomination()){
                selectedCards.add(card);
                // don't break loop, player can select many cards with the same denomination
                //return true;
            }
        }
        return !selectedCards.isEmpty();
    }

    /**
     *
     * @param topPile
     * @return true if strategy works (any card is selected)
     */
    private boolean sameSuitStrategy(Card topPile){
        for (Card card: cards) {
            if (card.getSuit() == topPile.getSuit()){
                selectedCards.add(card);
                return true;
            }
        }
        return !selectedCards.isEmpty();
    }

}
