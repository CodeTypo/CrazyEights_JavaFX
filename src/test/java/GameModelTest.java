import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    GameModel gameModel = new GameModel();

    @Test
    void prepareCardDeck_ShouldSetAllCardsInDeck() {
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
    void invitePlayers_ShouldCreatePlayerListWithAsManyPlayersAsManyCanBeInvited(){
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
    void invitePlayers_ShouldCreatePlayerListWithNoMoreThanSevenPlayers() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();
        Player player7 = new Player();
        Player player8 = new Player();

        // Act
        gameModel.invitePlayers(player1, player2, player3, player4, player5, player6, player7, player8);
        int actual = gameModel.getPlayers().size();

        // Assert
        assertEquals(7,actual);
    }

    @Test
    void invitePlayers_ShouldCreatePlayerListWithAtLeastTwoPlayers() {
        // Arrange
        Player player1 = new Player();

        // Act
        gameModel.invitePlayers(player1);
        int actual = gameModel.getPlayers().size();

        // Assert
        assertEquals(2,actual);
    }

    @Test
    void beginTheDeal_ShouldSetPlayersHaveEightCards_When_InTheGameAreNoMoreThanSixPlayers() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        gameModel.invitePlayers(player1,player2, player3, player4);
        gameModel.prepareCardDeck();

        // Act
        gameModel.beginTheDeal();

        // Assert
        for(Player players : gameModel.getPlayers())
            assertEquals(8, players.getCards().size());
    }

    @Test
    void beginTheDeal_ShouldSetThePlayersSevenCards_When_InTheGameAreSevenPlayers() {
        // Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();
        Player player6 = new Player();
        Player player7 = new Player();
        gameModel.invitePlayers(player1,player2, player3, player4, player5, player6, player7);
        gameModel.prepareCardDeck();

        // Act
        gameModel.beginTheDeal();

        // Assert
        for(Player players : gameModel.getPlayers())
            assertEquals(7, players.getCards().size());
    }

    @Test
    void putStarterOnPile_ShouldSetFirstFiveCardsOnThePileOtherThanEight() {
        // Arrange
        List<Card> stock;
        gameModel.prepareCardDeck();

        //Act
        gameModel.putStarterOnPile();
        stock = gameModel.getStock();
        int stockSize = stock.size();

        // Assert
        for(int i=1; i<=5; i++)
            assertNotEquals(Denomination.EIGHT,stock.get(stockSize-i).getDenomination());
    }

    @Test
    void dealCard_When_OnThePileAreZeroCards_ShouldReturnOne() {
        // Arrange
        Player player = new Player();
        gameModel.getStock();
        gameModel.setTurnPlayer(player);

        //Act
        int actual = gameModel.dealCard();

        //Assert
        assertEquals(1,actual);
    }

    @Test
    void dealCard_When_OnThePileAreMoreThanZeroCards_ShouldReturnZero() {
        // Arrange
        Player player = new Player();
        gameModel.prepareCardDeck();
        gameModel.getStock();
        gameModel.setTurnPlayer(player);

        //Act
        int actual = gameModel.dealCard();

        //Assert
        assertEquals(0,actual);
    }

    @Test
    void playCards_When_PlayerDoesNotChooseCard_ShouldReturnFalse() {
        //Arrange
        Player player = new Player();
        gameModel.setTurnPlayer(player);

        //Act
        boolean actual = gameModel.playCards();

        //Assert
        assertFalse(actual);
    }

    @Test
    void playCards_When_PlayerChoosesEightAsFirst_ShouldReturnTrue() {
        //Arrange
        Player player = new Player();
        Card eightDiamonds = new Card(Suit.DIAMONDS,Denomination.EIGHT);
        Card cardOtherThanEight = new Card(Suit.DIAMONDS, Denomination.JACK);
        gameModel.setTurnPlayer(player);
        player.selectCard(eightDiamonds);
        player.selectCard(cardOtherThanEight);

        //Act
        boolean actual = gameModel.playCards();

        //Assert
        assertTrue(actual);
    }

    @Test
    void playCards_When_PlayerChoosesCardWithTheSameSuitLikeTheLastOneOnThePile_ShouldReturnTrue() {
        //Arrange
        Player player = new Player();
        Card cardChosenByThePlayer = new Card(Suit.HEARTS,Denomination.THREE);
        Card lastOneCardOnThePile = new Card(Suit.HEARTS,Denomination.QUEEN);
        List<Card> pile = gameModel.getPile();
        gameModel.setTurnPlayer(player);
        player.selectCard(cardChosenByThePlayer);
        pile.add(lastOneCardOnThePile);

        //Act
        boolean actual = gameModel.playCards();

        //Assert
        assertTrue(actual);
    }

    @Test
    void playCards_When_PlayerChoosesCardWithTheSameDenominationLikeTheLastOneOnThePile_ShouldReturnTrue() {
        //Arrange
        Player player = new Player();
        Card cardChosenByThePlayer = new Card(Suit.SPADES,Denomination.ACE);
        Card lastOneCardOnThePile = new Card(Suit.HEARTS,Denomination.ACE);
        List<Card> pile = gameModel.getPile();
        gameModel.setTurnPlayer(player);
        player.selectCard(cardChosenByThePlayer);
        pile.add(lastOneCardOnThePile);

        //Act
        boolean actual = gameModel.playCards();

        //Assert
        assertTrue(actual);
    }

    @Test
    void playCards_When_PlayerChoosesCardWithTheDifferentDenominationAndSuitLikeTheLastOneOnThePile_ShouldReturnFalse() {
        //Arrange
        Player player = new Player();
        Card cardChosenByThePlayer = new Card(Suit.SPADES,Denomination.ACE);
        Card lastOneCardOnThePile = new Card(Suit.HEARTS,Denomination.QUEEN);
        List<Card> pile = gameModel.getPile();
        gameModel.setTurnPlayer(player);
        player.selectCard(cardChosenByThePlayer);
        pile.add(lastOneCardOnThePile);

        //Act
        boolean actual = gameModel.playCards();

        //Assert
        assertFalse(actual);
    }

    @Test
    void turnPlayerIsWinner_When_TurnPlayerHasZeroCards_ShouldReturnTrue() {
        //Arrange
        Player player = new Player();
        gameModel.setTurnPlayer(player);

        //Act
        boolean actual = gameModel.turnPlayerIsWinner();

        //Assert
        assertTrue(actual);
    }

    @Test
    void turnPlayerIsWinner_When_TurnPlayerHasMoreThanZeroCards_ShouldReturnFalse() {
        //Arrange
        Player player = new Player();
        gameModel.setTurnPlayer(player);
        player.selectCard(new Card(Suit.SPADES,Denomination.KING));

        //Act
        boolean actual = gameModel.turnPlayerIsWinner();

        //Assert
        assertFalse(actual);
    }

    @Test
    void nextPlayerTurn_When_TurnPlayerIsTheLastOne_WillSetTurnPlayerAsFirstOne() {
        //Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        gameModel.invitePlayers(player1,player2,player3,player4);
        gameModel.setTurnPlayer(player4);

        //Act
        gameModel.nextPlayerTurn();
        Player actual = gameModel.getTurnPlayer();

        //Assert
        assertEquals(player1,actual);
    }

    @Test
    void nextPlayerTurn_When_TurnPlayerIsTheSecondOfFour_WillSetTurnPlayerAsThirdOne() {
        //Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        gameModel.invitePlayers(player1, player2, player3, player4);
        gameModel.setTurnPlayer(player2);

        //Act
        gameModel.nextPlayerTurn();
        Player actual = gameModel.getTurnPlayer();

        //Assert
        assertEquals(player3,actual);
    }

    @Test
    void winnerPoints_When_PlayerPutQueenThreeAceEightOnThePile_ShouldReturnSixtyOne() {
        //Arrange
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        List<Card> cards = new ArrayList();
        cards.add(new Card(Suit.SPADES,Denomination.QUEEN));
        cards.add(new Card(Suit.DIAMONDS,Denomination.THREE));
        cards.add(new Card(Suit.CLUBS,Denomination.ACE));
        cards.add(new Card(Suit.HEARTS,Denomination.EIGHT));
        gameModel.invitePlayers(player1, player2, player3);

        //Act
        int actual = 0;

        for(Card card : cards)
        {
            player1.selectCard(card);
            actual = gameModel.winnerPoints();
        }

        //Assert
        assertEquals(61,actual);
    }
}