import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameModelReactiveTest {

    @Test
    void prepareCardDeck_ShouldSetAllCardsInDeck() {
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        Set<CardReactive> cardSet = new HashSet<>();

        // Act
        gameModelReactive.prepareCardDeck();
        cardSet.addAll(gameModelReactive.getStock());
        int actual = cardSet.size();

        // Assert
        assertEquals(52, actual);
    }

    @Test
    void prepareBots_ShouldCreateThreeBotPlayer(){
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        List<BotPlayerReactive> botPlayerReactiveList = gameModelReactive.getBotPlayers();

        //Act
        gameModelReactive.prepareBots();
        int numberOfBotPlayers = botPlayerReactiveList.size();

        //Assert
        assertEquals(3,numberOfBotPlayers);
    }

    @Test
    void beginTheDeal_ShouldSetPlayersEightCards() {
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        gameModelReactive.init();

        // Act
        gameModelReactive.beginTheDeal();

        // Assert
        for(BotPlayerReactive players : gameModelReactive.getBotPlayers())
            assertEquals(8, players.getCards().size());
    }

    @Test
    void drawDeal_ShouldSetPlayerWhoStartsGameRandomly(){
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        gameModelReactive.prepareBots();
        gameModelReactive.getInteractivePlayer();

        // Act
        gameModelReactive.drawDealer();
        boolean actual = (gameModelReactive.getTurnPlayer() instanceof PlayerReactive);

        // Assert
        assertTrue(actual);
    }

    @Test
    void playCards_WhenTurnPlayerSelectEight_ShouldReturnTrue(){
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        gameModelReactive.init();
        gameModelReactive.putStarterOnPile();
        gameModelReactive.setTurnPlayer(new InteractivePlayerReactive());
        PlayerReactive turnPlayer = gameModelReactive.getTurnPlayer();
        turnPlayer.selectCard(new CardReactive(Suit.SPADES,Denomination.EIGHT));

        // Act
        boolean actual = gameModelReactive.playCards();


        // Assert
        assertTrue(actual);
    }

    @Test
    void playCards_WhenTurnPlayerSelectOtherCardThanEight_ShouldReturnFalse(){
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        gameModelReactive.init();
        gameModelReactive.putStarterOnPile();
        gameModelReactive.setTurnPlayer(new InteractivePlayerReactive());
        PlayerReactive turnPlayer = gameModelReactive.getTurnPlayer();
        turnPlayer.selectCard(new CardReactive(Suit.SPADES,Denomination.TWO));

        // Act
        boolean actual = gameModelReactive.playCards();


        // Assert
        assertFalse(actual);
    }
}