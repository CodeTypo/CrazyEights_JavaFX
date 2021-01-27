import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(true,actual);
    }

    @Test
    void init_ShouldPrepareCardDeck(){
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        Set<CardReactive> cardSet = new HashSet<>();

        // Act
        gameModelReactive.init();
        cardSet.addAll(gameModelReactive.getStock());
        int actual = cardSet.size();

        // Assert
        assertEquals(52, actual);
    }

    @Test
    void init_ShouldPrepareBots(){
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
    void init_ShouldDrawDealer(){
        // Arrange
        GameModelReactive gameModelReactive = new GameModelReactive();
        gameModelReactive.prepareBots();
        gameModelReactive.getInteractivePlayer();

        // Act
        gameModelReactive.init();
        boolean actual = (gameModelReactive.getTurnPlayer() instanceof PlayerReactive);

        // Assert
        assertEquals(true,actual);
    }
}