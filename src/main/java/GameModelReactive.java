import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
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
        int randInt = getRandomInt(0, BOTS+1);
        if (randInt == BOTS) {
            turnPlayer.set(interactivePlayer); // if random int is out of bots array range
        } else {
            turnPlayer.set(botPlayers.get(randInt)); // else set random bot has turn
        }
    }

    public void nextPlayerTurn(){
        if (botPlayers.contains(getTurnPlayer())){
            // next turn belongs to another bot or interactive player depending on current player
            int index = botPlayers.indexOf(getTurnPlayer());
            if (index == BOTS - 1) {
                turnPlayer.set(interactivePlayer);
            }else {
                turnPlayer.set(botPlayers.get(index + 1));
            }
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
        suitProperty().set(starter.getSuit());
    }

    // Add event listeners to react for changes
    public void init(){
        // These two don't need to be reactive.
        // They are rather static and don't use observables.
        prepareCardDeck();
        prepareBots();
        drawDealer();

    }

    public void setupBots(){
        turnPlayer.addListener((observable, oldTurnPlayer, newTurnPlayer) -> {
            if (newTurnPlayer instanceof BotPlayerReactive){
                // Game Model should make move for bot
                BotPlayerReactive bot = (BotPlayerReactive) newTurnPlayer;
                if (!bot.makeMove(getTopCardFromPile(), getSuit())) {
                    if (stock.isEmpty()){
                        nextPlayerTurn();
                    } else{
                        dealCard();
                        if (!bot.makeMove(getTopCardFromPile(), getSuit())) {
                            nextPlayerTurn();
                        }
                    }
                }
//
//                while (!bot.makeMove(getTopCardFromPile(), getSuit())){
//                    if (stock.isEmpty()){
//                        nextPlayerTurn();
//                        break;
//                    } else{
//                        dealCard();
//                    }
//                }

                if(playCards()){
                    // let bot select suit
//                    setSuit(bot.getSelectedSuit()); // TODO */ maybe unlock
                    nextPlayerTurn();
                }

            }
        });
    }

    /**
     *
     * @return true if suit selector should be shown
     * false otherwise.
     * So if crazy eight is played, it returns true
     * because turnPlayer must select suit.
     * If false, next player have turn
     */
    public boolean playCards(){
        List<CardReactive> selCards = getTurnPlayer().getSelectedCards();
        CardReactive firstCard = selCards.get(0);
        if (!selCards.isEmpty()){

            // Uwaga!!! tutaj jest mala niescislosc,
            // bo false jest zwracany zarowno wtedy gdy zagrana jest osemka
            // lub nic nie zostalo zagrane wiec mozna oszukiwac i wybierac
            // suita po raz drugi, gdy bot przed nami rzucil osemke i wybral suita,
            // a my zaznaczymy bledna karte i wcisniemy od razu confirm.
            // Wyeliminuje to chyba poprzez to samo zabezpieczenie co bylo w Playerze,
            // ze juz na wstepie nie mozna zaznaczyc karty niezgodnie z regulaminem.
            if (firstCard.getDenomination() == Denomination.EIGHT){
                pile.addAll(selCards);
                getTurnPlayer().removeSelectedCards(selCards);
                getTurnPlayer().getSelectedCards().clear();
                return true;
            }else if(firstCard.getSuit() == getSuit() || firstCard.getDenomination() == getTopCardFromPile().getDenomination()){
                pile.addAll(selCards);
                getTurnPlayer().removeSelectedCards(selCards);
                getTurnPlayer().getSelectedCards().clear();
                nextPlayerTurn();
            }
        }

        return false;
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

    public List<BotPlayerReactive> getBotPlayers() {
        return botPlayers;
    }

    public Suit getSuit() {
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
