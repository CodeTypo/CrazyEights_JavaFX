import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player = new Player();

    @Test
    void dealCard_When_PlayerDealsTwoCards_ShouldPlayerGetsTwoCards() {
        //Arrange
        List<Card> cards = player.getCards();
        Card sevenSpades = new Card(Suit.SPADES,Denomination.SEVEN);
        Card queenHeart = new Card(Suit.HEARTS,Denomination.QUEEN);

        //Act
        player.dealCard(sevenSpades);
        player.dealCard(queenHeart);
        int actual = cards.size();

        //Assert
        assertEquals(2, actual);
    }

    @Test
    void selectCard_When_PlayerHasZeroSelectedCardBeforeSelectsCard_ShouldReturnTrue() {
        //Arrange
        Card randomCard = new Card(Suit.CLUBS,Denomination.QUEEN);

        //Act
        boolean actual = player.selectCard(randomCard);

        //Assert
        assertTrue(actual);
    }

    @Test
    void selectCard_When_PlayerSelectsSomeCardsWithTheSameDenomination_ShouldReturnTrue() {
    }

    @Test
    void selectCard_When_PlayerSelectsSomeCardsWithOtherDenomination_ShouldReturnFalse() {
    }

    @Test
    void putCardOnPile() {
    }

    @Test
    void selectSuit() {
    }

    @Test
    void getSelectedCards() {
    }

    @Test
    void getSelectedSuit() {
    }

    @Test
    void getCardsNumber() {
    }

    @Test
    void getAllCards() {
    }
}
