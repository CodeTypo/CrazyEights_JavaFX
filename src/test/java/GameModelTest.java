import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel = new GameModel();

    @Test
    void areAllCardsInDeck() {
        // Arrange
        Set<Card> cardSet = new HashSet<>();

        // Act
        gameModel.prepareCardDeck();
        cardSet.addAll(gameModel.getStock());
        int actual = cardSet.size();

        // Assert
        assertEquals(52,actual);
    }

    @Test
    void areInTheGameAsManyPlayersAsManyCanBeInvited(){
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();

        // Act
        gameModel.invitePlayers(player1, player2, player3, player4);
        int actual = gameModel.getPlayers().size();

        // Assert
        assertEquals(4,actual);
    }

    @Test
    void areNoMorePlayersThanSeven() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();
        Player player7 = new Player();
        Player player8 = new Player();
        gameModel.invitePlayers(player1, player2, player3, player4, player5, player6, player7, player8);

        // Act
        gameModel.invitePlayers(player1, player2, player3, player4, player5, player6, player7, player8);
        int actual = gameModel.getPlayers().size();

        // Assert
        assertEquals(7,actual);
    }

    @Test
    void areAtLeastTwoPlayers() {
        // Arrange
        Player player1 = new Player();

        // Act
        gameModel.invitePlayers(player1);
        int actual = gameModel.getPlayers().size();

        // Assert
        assertEquals(2,actual);
    }

    @Test
    void shouldPlayersHaveEightCards_When_InTheGameAreNoMoreThanSixPlayers() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();

        // Act
        gameModel.invitePlayers(player1,player2, player3, player4);
        gameModel.prepareCardDeck();
        gameModel.beginTheDeal();


        // Assert
        for(Player players : gameModel.getPlayers())
            assertEquals(8, players.getCards().size());
    }

    @Test
    void shouldPlayersHaveSevenCards_When_InTheGameAreSevenPlayers() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();
        Player player7 = new Player();

        // Act
        gameModel.invitePlayers(player1,player2, player3, player4, player5, player6, player7);
        gameModel.prepareCardDeck();
        gameModel.beginTheDeal();

        // Assert
        for(Player players : gameModel.getPlayers())
            assertEquals(7, players.getCards().size());
    }

    @Test
    void isEightOneOfTheFirstFiveCardsOnThePile() {
        // Arrange
        List<Card> stock;

        //Act
        gameModel.prepareCardDeck();
        gameModel.putStarterOnPile();
        stock = gameModel.getStock();
        int stockSize = stock.size();

        // Assert
        for(int i=1; i<=5; i++)
            assertNotEquals(Denomination.EIGHT,stock.get(stockSize-i).getDenomination());
    }

    @Test
    void isTheDealerOneOfThePlayers() {

    }

    @Test
    void dealCard_When_InThePileAreZeroCards_ShouldReturnOne() {
        // Arrange
        Player player = new Player();

        //Act
        gameModel.getStock();
        gameModel.setTurnPlayer(player);
        int actual = gameModel.dealCard();

        //Assert
        assertEquals(1,actual);
    }

    @Test
    void dealCard_When_InThePileAreMoreThanZeroCards_ShouldReturnZero() {
        // Arrange
        Player player = new Player();

        //Act
        gameModel.prepareCardDeck();
        gameModel.setTurnPlayer(player);
        gameModel.getStock();
        int actual = gameModel.dealCard();

        //Assert
        assertEquals(0,actual);
    }

    @Test
    void playCards_WhenPlayerDoesNotChooseCard_ShouldReturnFalse() {
        //Arrange
        Player player = new Player();

        //Act
        gameModel.setTurnPlayer(player);
        boolean actual = gameModel.playCards();

        //Assert
        assertFalse(actual);
    }

    @Test
    void playCards_WhenPlayerChoosesEightAsFirst_ShouldReturnTrue() {
        //Arrange
        Player player = new Player();
        Card eightDiamonds = new Card(Suit.DIAMONDS,Denomination.EIGHT);
        Card cardOtherThanEight = new Card(Suit.DIAMONDS, Denomination.JACK);

        //Act
        gameModel.setTurnPlayer(player);
        player.selectCard(eightDiamonds);
        player.selectCard(cardOtherThanEight);
        boolean actual = gameModel.playCards();

        //Assert
        assertTrue(actual);
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
}
