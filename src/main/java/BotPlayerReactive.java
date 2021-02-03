import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents bot player.
 */
public class BotPlayerReactive extends PlayerReactive {
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
     * @return true if bot played card, false otherwise
     * if makeMove returns false, than gameModel should call gameModel.dealCard()
     * and this bot will automatically got one card from stock top.
     * We probably want to run this makeMove() in controller in loop until it returns true
     * for example: while(!botPLayer.makeMove()){gameModel.dealCard()}; gameModel.playCards();
     * Because gameModel watch for turnPLayer and let to call methods only on
     * current turnPlayer, we don't need to deal with it.
     * @ topPile card on pile's top, bot selects cards that fit rules
     */
    public boolean makeMove(CardReactive topPile, Suit suit) {
        if (!sameSuitStrategy(suit))
            if (!sameDenominationStrategy(topPile))
                if (!crazyEightStrategy())
                    return false;
        return true;
    }

    /**
     * @return true if BotPlayer has Eight (any card is selected)
     */
    private boolean crazyEightStrategy() {
        boolean selected = false;
        for (CardReactive card : getCards()) {
            if (card.getDenomination() == Denomination.EIGHT) {
                selectCard(card);
                selectSuit(Suit.values()[ThreadLocalRandom.current().nextInt(0, Suit.values().length)]);
                selected = true;
            }
        }

        return selected;
    }

    /**
     * @param topPile represents the card of the top
     * @return true if strategy works (any card is selected)
     */
    private boolean sameDenominationStrategy(CardReactive topPile) {
        boolean selected = false;
        for (CardReactive card : getCards()) {
            if (card.getDenomination() == topPile.getDenomination()) {
                selectCard(card);
                selected = true;
            }
        }
        return selected;
    }

    /**
     * @param suit represents suit of the card on top
     * @return true if strategy works (any card is selected)
     */
    private boolean sameSuitStrategy(Suit suit) {
        for (CardReactive card : getCards()) {
            if (card.getSuit() == suit) {
                selectCard(card);
                return true;
            }
        }
        return false;
    }
}
