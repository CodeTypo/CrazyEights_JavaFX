import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BotPlayerReactiveTest {

    @Test
    void makeMove_WhenBotPlayerHasEight_ShouldReturnTrue(){
        //Arrange
        BotPlayerReactive bot = new BotPlayerReactive();
        bot.dealCard(new CardReactive(Suit.HEARTS, Denomination.EIGHT));
        bot.dealCard(new CardReactive(Suit.SPADES, Denomination.TWO));
        bot.dealCard(new CardReactive(Suit.CLUBS, Denomination.THREE));

        //Act
        boolean actual = bot.makeMove(new CardReactive(Suit.DIAMONDS,Denomination.ACE),Suit.DIAMONDS);

        //Assert
        assertTrue(actual);
    }

    @Test
    void makeMove_WhenBotPlayerHasSameDenominationCardAsTopCardFromPile_ShouldReturnTrue(){
        //Arrange
        BotPlayerReactive bot = new BotPlayerReactive();
        bot.dealCard(new CardReactive(Suit.HEARTS, Denomination.QUEEN));
        bot.dealCard(new CardReactive(Suit.SPADES, Denomination.TWO));
        bot.dealCard(new CardReactive(Suit.CLUBS, Denomination.THREE));

        //Act
        boolean actual = bot.makeMove(new CardReactive(Suit.DIAMONDS,Denomination.THREE),Suit.DIAMONDS);

        //Assert
        assertTrue(actual);
    }

    @Test
    void makeMove_WhenBotPlayerHasSameSuitCardAsTopCardFromPile_ShouldReturnTrue(){
        //Arrange
        BotPlayerReactive bot = new BotPlayerReactive();
        bot.dealCard(new CardReactive(Suit.HEARTS, Denomination.QUEEN));
        bot.dealCard(new CardReactive(Suit.SPADES, Denomination.TWO));
        bot.dealCard(new CardReactive(Suit.CLUBS, Denomination.THREE));

        //Act
        boolean actual = bot.makeMove(new CardReactive(Suit.SPADES,Denomination.FIVE),Suit.SPADES);

        //Assert
        assertTrue(actual);
    }

    @Test
    void makeMove_WhenBotPlayerDoesNotHaveRightStrategy_ShouldReturnFalse(){
        //Arrange
        BotPlayerReactive bot = new BotPlayerReactive();
        bot.dealCard(new CardReactive(Suit.HEARTS, Denomination.QUEEN));
        bot.dealCard(new CardReactive(Suit.SPADES, Denomination.TWO));
        bot.dealCard(new CardReactive(Suit.CLUBS, Denomination.THREE));

        //Act
        boolean actual = bot.makeMove(new CardReactive(Suit.DIAMONDS,Denomination.FIVE),Suit.DIAMONDS);


        //Assert
        assertFalse(actual);
    }
}