import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class GameModelReactive {

    private static final int FIRST_DEAL_CARDS = 8;

    private ObservableList<Player> players = FXCollections.observableArrayList();

    /**
     * Make stock observable to gain insight what cards are left.
     */
    private ObservableList<Card> stock = FXCollections.observableArrayList();

    /**
     * We need to track number of cards in stock,
     * because it depends on whether Controller draw stock or not.
     */
    private SimpleBooleanProperty isStockEmpty = new SimpleBooleanProperty(stock.isEmpty());

    /**
     * We need to track turnPlayer because only
     * he can make action like deal or play card.
     */
    private ObservableList<Player> turnPlayer = FXCollections.observableArrayList();

    /**
     * We don't need to track all cards in pile
     * but store it in observable list to track
     * when element changes (this time only one element).
     * Only top card is tracked because
     * Controller needs to refresh its view.
     */
    private ObservableList<Card> topPile = FXCollections.observableArrayList();


    /**
     * We need to track actual suit, because when crazy eight
     * is played, appropriate symbol must be updated on screen.
     */
    private ObservableList<Suit> suit = FXCollections.observableArrayList();


    public void prepareCardDeck(){
        for (Suit suit: Suit.values()){
            for (Denomination denomination: Denomination.values()){
                stock.add(new Card(suit, denomination));
            }
        }

        Collections.shuffle(stock);
    }

    /**
     *
     * @param player interactive player
     * @param n number of bots
     */
    public void invitePlayers(Player player, int n){
        players.add(player);
        for (int i = 0; i < n; i++) {
            players.add(new BotPlayer());
        }
    }

    public void drawDealer(){
        turnPlayer.add(
                players.get(getRandomInt(0, players.size())));
    }

    public void beginTheDeal(){
        for (Player player: players){
            for (int i = 0; i < FIRST_DEAL_CARDS; i++) {
                player.dealCard(takeTopCardFromStock());
            }
        }
    }

    public void putStarterOnPile(){
        Card starter = takeTopCardFromStock();
        while (starter.getDenomination() == Denomination.EIGHT){
            // Put eight somewhere in the middle of stock
            stock.add(getRandomInt(0, stock.size())-5, starter);
        }

        topPile.add(starter);
    }

    public Card takeTopCardFromStock(){
        return stock.remove(stock.size()-1);
    }

    public Card getTopCardFromPile(){
        return topPile.get(0);
    }



    /**
     *
     * @param a inclusive
     * @param b exclusive
     * @return number from range [a,b) without b
     */
    public int getRandomInt(int a, int b){
        return ThreadLocalRandom
                .current()
                .nextInt(a, b);
    }





}
