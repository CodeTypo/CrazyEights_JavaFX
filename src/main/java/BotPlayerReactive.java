public class BotPlayerReactive extends PlayerReactive{
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
    public boolean makeMove(CardReactive topPile, Suit suit){
        if (!sameSuitStrategy(suit)) // and finally to the last
            if (!sameDenominationStrategy(topPile)) // then go to second strategy
                if (!crazyEightStrategy()) // if first strategy doesnt work
                    return false; // if no strategy works return false
        return true;
    }

    /**
     *
     * @return true if strategy works (any card is selected)
     */
    private boolean crazyEightStrategy(){
        boolean selected = false;
        for (CardReactive card: getCards()) {
            if (card.getDenomination() == Denomination.EIGHT){
                // botPlayer plays always all crazy eights in its collection at once
                selectCard(card);
                selected = true;
            }
        }
        // pick suit at random
        // selectSuit(Suit.values()[ThreadLocalRandom.current().nextInt(0, Suit.values().length)]);
        selectSuit(Suit.HEARTS);
        return selected;
    }

    /**
     *
     * @param topPile
     * @return true if strategy works (any card is selected)
     */
    private boolean sameDenominationStrategy(CardReactive topPile){
        boolean selected = false;
        for (CardReactive card: getCards()) {
            if (card.getDenomination() == topPile.getDenomination()){
                selectCard(card);
                selected = true;
                // don't break loop, player can select many cards with the same denomination
                //return true;
            }
        }
        return selected;
    }

    /**
     *
     * @param suit
     * @return true if strategy works (any card is selected)
     */
    private boolean sameSuitStrategy(Suit suit){
        for (CardReactive card: getCards()) {
            if (card.getSuit() == suit){
               selectCard(card);
               return true;
            }
        }
        return false;
    }
}
