import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

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
     * Only top card is must be tracked because
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

//
//    /**
//     * We need to track number of cards in stock,
//     * because it depends on whether Controller draw stock or not.
//     */
//    private SimpleBooleanProperty isStockEmpty = new SimpleBooleanProperty(stock.isEmpty());


    public void prepareCardDeck(){
        for (Suit suit: Suit.values()){
            for (Denomination denomination: Denomination.values()){
                stock.add(new CardReactive(suit, denomination));
            }
        }

        Collections.shuffle(stock);
    }

    public void prepareBots(){
        for (int i = 0; i < BOTS; i++) {
            botPlayers.add(new BotPlayerReactive());
        }
    }

    public void drawDealer(){
        //Set player who starts game randomly
        int randInt = getRandomInt(0, botPlayers.size()+1);
        if (randInt == BOTS) {
            turnPlayer.set(interactivePlayer); // if random int is out of bots array range
        } else {
            turnPlayer.set(botPlayers.get(randInt)); // else set random bot has turn
        }
    }

    public void nextPlayerTurn(){
        if (botPlayers.contains(turnPlayer.get())){
            // next turn belongs to another bot or interactive player depending on current player
            int index = botPlayers.indexOf(turnPlayer.get());
            if (index == BOTS - 1) {
                turnPlayer.set(interactivePlayer);
            }
            turnPlayer.set(botPlayers.get(index + 1));
        } else {
            // next turn belongs to bot
            turnPlayer.set(botPlayers.get(0));
        }
    }

    /**
     * Card can be dealt only by current player
     */
    public void dealCard(){
        turnPlayer.get().dealCard(takeTopCardFromStock());
        // Set animation on this action
        // We must disable interactive confirm button in controller
        // by listening to turnPlayer observable to protect by dealing card
        // when interactive player don't have a turn
    }

    public void beginTheDeal(){
        // BOTS+1 to take into account additional interactive player
        // It may be useful to deal according to turn
        // in case wa want to animate dealing cards.
        for (int i = 0; i < BOTS+1; i++) {
            for (int j = 0; j < FIRST_DEAL_CARDS; j++) {
                dealCard();
            }
            nextPlayerTurn();
        }
    }

    public void putStarterOnPile(){
        CardReactive starter;
        while ( (starter = takeTopCardFromStock()).getDenomination() == Denomination.EIGHT){
            // Put eight somewhere in the middle of stock
            stock.add(getRandomInt(0, stock.size())-5, starter);
        }

        pile.add(starter);
    }

    // Add event listeners to react for changes
    public void init(){
        // These two don't need to be reactive.
        // They are rather static and don't use observables.
        prepareCardDeck();
        prepareBots();

        //Setup observables to do dirty job.

        turnPlayer.addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof BotPlayerReactive){
                //Bot has turn
            }
        });




//        botPlayers.forEach(bot -> {
//            bot.getCards().addListener((ListChangeListener<? super CardReactive>) c -> {
//                c.
//            });
//        });





        drawDealer();
        beginTheDeal();
        putStarterOnPile();
    }

    public void playCards(){

    }

    /**
     * Only player that has turn can become winner.
     *
     * @return
     */
    public int winnerPoints(){
        AtomicInteger points = new AtomicInteger();

        botPlayers.forEach(bot -> bot.getCards().forEach(card -> {
            switch (card.getDenomination()) {
                case ACE -> points.addAndGet(1);
                case TEN, JACK, QUEEN, KING -> points.addAndGet(10);
                case EIGHT -> points.addAndGet(50);
            }
        }));

        interactivePlayer.getCards().forEach(card -> {
            switch (card.getDenomination()) {
                case ACE -> points.addAndGet(1);
                case TEN, JACK, QUEEN, KING -> points.addAndGet(10);
                case EIGHT -> points.addAndGet(50);
            }
        });

        return points.get();
    }

    /**
     *
     * @return card removed from stock
     */
    public CardReactive takeTopCardFromStock(){
        return stock.remove(stock.size()-1);
    }

    /**
     *
     * @return card which lays on pile top, but doesn't remove it
     */
    public CardReactive getTopCardFromPile(){
        return pile.get(pile.size()-1);
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

    public InteractivePlayerReactive getInteractivePlayer() {
        return interactivePlayer;
    }
}
