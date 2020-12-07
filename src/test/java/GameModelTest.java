import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel = new GameModel();

    @Test
    void prepareCardDeck() {
        gameModel.prepareCardDeck();
        Set<Card> cardSet = new HashSet<>();

        cardSet.addAll(gameModel.getStock());

        assertEquals(52,cardSet.size());
    }

    @Test
    void invitePlayers() {

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();
        Player player7 = new Player();
        Player player8 = new Player();

        gameModel.invitePlayers(player1, player2, player3, player4, player5, player6, player7, player8);


        assertEquals(7,gameModel.getPlayers().size());
    }

    @Test
    void drawDealer() {
    }

    @Test
    void beginTheDeal() {
    }

    @Test
    void putStarterOnPile() {
    }

    @Test
    void dealCard() {
    }

    @Test
    void playCards() {
    }

    @Test
    void turnPlayerIsWinner() {
    }

    @Test
    void nextPlayerTurn() {
    }

    @Test
    void winnerPoints() {
    }

    @Test
    void getPlayers() {
    }

    @Test
    void getDealer() {
    }

    @Test
    void getTurnPlayer() {
    }

    @Test
    void getStock() {
    }

    @Test
    void getPile() {
    }

    @Test
    void getSuit() {
    }
}
