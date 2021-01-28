import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InteractivePlayerReactiveTest {

    @Test
    void selectCard_When_InteractivePlayerHasZeroSelectedCardBeforeSelectsCard_ShouldHaveOneSelectedCard() {
        //Arrange
        InteractivePlayerReactive player = new InteractivePlayerReactive();
        CardReactive randomCard = new CardReactive(Suit.CLUBS, Denomination.QUEEN);

        //Act
        player.selectCard(randomCard);
        int size = player.getSelectedCards().size();

        //Assert
        assertEquals(1, size);
    }

    @Test
    void selectCard_When_InteractivePlayerSelectsSomeCardsWithTheSameDenominations_ShouldHaveAllOfTheseCardsSelected() {
        //Arrange
        InteractivePlayerReactive player = new InteractivePlayerReactive();
        CardReactive firstCardSelected = new CardReactive(Suit.SPADES, Denomination.JACK);
        CardReactive secondCardSelected = new CardReactive(Suit.HEARTS, Denomination.JACK);
        List<CardReactive> selectedCards = player.getSelectedCards();
        selectedCards.add(firstCardSelected);

        //Act
        player.selectCard(secondCardSelected);
        int size = player.getSelectedCards().size();

        //Assert
        assertEquals(2,size);
    }

    @Test
    void selectCard_When_InteractivePlayerSelectsSomeCardsWithOtherDenomination_ShouldHaveOneSelectedCard() {
        //Arrange
        InteractivePlayerReactive player = new InteractivePlayerReactive();
        CardReactive firstCardSelected = new CardReactive(Suit.SPADES, Denomination.JACK);
        CardReactive secondCardSelected = new CardReactive(Suit.HEARTS, Denomination.QUEEN);
        List<CardReactive> selectedCards = player.getSelectedCards();
        selectedCards.add(firstCardSelected);

        //Act
        player.selectCard(secondCardSelected);
        int size = player.getSelectedCards().size();

        //Assert
        assertEquals(1,size);
    }

    @Test
    void dealCard_When_InteractivePlayerDealsTwoCards_ShouldPlayerGetsTwoCards() {
        //Arrange
        InteractivePlayerReactive player = new InteractivePlayerReactive();
        ObservableList<CardReactive> cards = player.getCards();
        CardReactive sevenSpades = new CardReactive(Suit.SPADES, Denomination.SEVEN);
        CardReactive queenHeart = new CardReactive(Suit.HEARTS, Denomination.QUEEN);

        //Act
        player.dealCard(sevenSpades);
        player.dealCard(queenHeart);
        int actual = cards.size();

        //Assert
        assertEquals(2, actual);
    }
}