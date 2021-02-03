import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents the game model of <code>Crazy Eights</code>.
 * Rules taken from this source:
 * https://bicyclecards.com/how-to-play/crazy-eights/
 */
@Getter
@Setter
public class GameModelReactive {

    private static final int FIRST_DEAL_CARDS = 8;

    private static final int BOTS = 3;

    /**
     * Final because players don't change during game.
     */
    private final List<BotPlayerReactive> botPlayers = new ArrayList<>();

    /**
     * Final because player doesn't change during game.
     */
    private final InteractivePlayerReactive interactivePlayer = new InteractivePlayerReactive();

    /**
     * Make stock observable to gain insight what cards are left.
     */
    private ObservableList<CardReactive> stock = FXCollections.observableArrayList();

    /**
     * Store cards in observable list to track
     * when elements changes.
     * Only top card must be tracked because
     * Controller needs to refresh its view.
     */
    private ObservableList<CardReactive> pile = FXCollections.observableArrayList();

    /**
     * We need to track actual suit, because when crazy eight
     * is played, appropriate symbol must be updated on screen.
     */
    private SimpleObjectProperty<Suit> suit = new SimpleObjectProperty<>();

    /**
     * We need to track turnPlayer because only
     * he can make action like deal or play card.
     */
    private SimpleObjectProperty<PlayerReactive> turnPlayer = new SimpleObjectProperty<>();

    /**
     * Prepare 52-card deck to put on table.
     * Shuffle cards at random.
     */
    public void prepareCardDeck() {
        for (Suit suit : Suit.values()) {
            for (Denomination denomination : Denomination.values()) {
                stock.add(new CardReactive(suit, denomination));
            }
        }

        Collections.shuffle(stock);
    }

    /**
     * This method prepares the bots for the game.
     */
    public void prepareBots() {
        for (int i = 0; i < BOTS; i++) {
            botPlayers.add(new BotPlayerReactive());
        }
    }

    /**
     * This method sets up the player to deal the cards.
     * Dealer starts the game.
     */
    public void drawDealer() {
        int randInt = getRandomInt(0, BOTS + 1);
        if (randInt == BOTS) {
            turnPlayer.set(interactivePlayer);
        } else {
            turnPlayer.set(botPlayers.get(randInt));
        }
    }

    /**
     * Control which player has turn.
     */
    public void nextPlayerTurn() {
        if (botPlayers.contains(getTurnPlayer())) {
            int index = botPlayers.indexOf(getTurnPlayer());
            if (index == BOTS - 1) {
                turnPlayer.set(interactivePlayer);
            } else {
                turnPlayer.set(botPlayers.get(index + 1));
            }
        } else {
            turnPlayer.set(botPlayers.get(0));
        }
    }

    /**
     * Card can be dealt only by current player.
     */
    public void dealCard() {
        turnPlayer.get().dealCard(takeTopCardFromStock());
    }

    /**
     * Every player deals 8 cards from stock.
     */
    public void beginTheDeal() {
        for (int i = 0; i < BOTS + 1; i++) {
            for (int j = 0; j < FIRST_DEAL_CARDS; j++) {
                dealCard();
            }
            nextPlayerTurn();
        }
    }

    /**
     * Puts card from stock top as starter card.
     * Starter card cannot be eight.
     */
    public void putStarterOnPile() {
        CardReactive starter;
        while ((starter = takeTopCardFromStock()).getDenomination() == Denomination.EIGHT) {
            stock.add(getRandomInt(0, stock.size()) - 5, starter);
        }

        pile.add(starter);
        suitProperty().set(starter.getSuit());
    }

    /**
     * This method implements methods such as:
     * prepareCardDeck(),
     * prepareBots(),
     * drawDealer().
     */
    public void init() {
        prepareCardDeck();
        prepareBots();
        drawDealer();
    }

    /**
     * Prepare AI powered players.
     */
    public void setupBots() {
        turnPlayer.addListener((observable, oldTurnPlayer, newTurnPlayer) -> {
            if (newTurnPlayer instanceof BotPlayerReactive) {
                BotPlayerReactive bot = (BotPlayerReactive) newTurnPlayer;
                if (!bot.makeMove(getTopCardFromPile(), getSuit())) {
                    if (stock.isEmpty()) {
                        nextPlayerTurn();
                    } else {
                        dealCard();
                        if (!bot.makeMove(getTopCardFromPile(), getSuit())) {
                            nextPlayerTurn();
                        }
                    }
                }

                if (playCards()) {
                    setSuit(bot.getSelectedSuit());
                    nextPlayerTurn();
                }

            }
        });
    }

    /**
     * @return true if suit selector should be shown
     * false otherwise.
     * So if crazy eight is played, it returns true
     * because turnPlayer must select suit.
     * If false, next player have turn.
     */
    public boolean playCards() {
        List<CardReactive> selCards = getTurnPlayer().getSelectedCards();
        if (!selCards.isEmpty()) {
            CardReactive firstCard = selCards.get(0);
            if (firstCard.getDenomination() == Denomination.EIGHT) {
                pile.addAll(selCards);
                getTurnPlayer().removeSelectedCards(selCards);
                getTurnPlayer().getSelectedCards().clear();
                return true;
            } else if (firstCard.getSuit() == getSuit() || firstCard.getDenomination() == getTopCardFromPile().getDenomination()) {
                pile.addAll(selCards);
                getTurnPlayer().removeSelectedCards(selCards);
                getTurnPlayer().getSelectedCards().clear();
                nextPlayerTurn();
            }
        }

        return false;
    }

    /**
     * @return card removed from stock.
     */
    public CardReactive takeTopCardFromStock() {
        return stock.remove(stock.size() - 1);
    }

    /**
     * @return card which lays on pile top, but doesn't remove it.
     */
    public CardReactive getTopCardFromPile() {
        return pile.get(pile.size() - 1);
    }

    /**
     * @param a inclusive.
     * @param b exclusive.
     * @return number from range [a,b) without b.
     */
    public int getRandomInt(int a, int b) {
        return ThreadLocalRandom
                .current()
                .nextInt(a, b);
    }

    public List<BotPlayerReactive> getBotPlayers() {
        return botPlayers;
    }

    /**
     * @return the current suit.
     */
    public Suit getSuit() {
        if (suit.get() == null) {
            return Suit.HEARTS;
        }
        return suit.get();
    }

    public SimpleObjectProperty<Suit> suitProperty() {
        return suit;
    }

    public PlayerReactive getTurnPlayer() {
        return turnPlayer.get();
    }

    public SimpleObjectProperty<PlayerReactive> turnPlayerProperty() {
        return turnPlayer;
    }

    public void setSuit(Suit suit) {
        this.suit.set(suit);
    }

    public void setTurnPlayer(PlayerReactive turnPlayer) {
        this.turnPlayer.set(turnPlayer);
    }
}
