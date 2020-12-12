import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
// imports for loading and converting external svg files


public class CrazyEightsReactiveController {

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Controller class fields ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //A bunch of FXML objects initialization statements
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox box1;

    @FXML
    private HBox box3;

    @FXML
    private VBox box4;

    @FXML
    private VBox box2;

    @FXML
    private Button startButton;

    @FXML
    private ImageView pileImg;

    @FXML
    private ImageView deckImg;

    @FXML
    private ImageView suitSymbol;

    @FXML
    private HBox hBoxsOfSuits;

    @FXML
    private ImageView spadesIV;

    @FXML
    private ImageView clubsIV;

    @FXML
    private ImageView heartsIV;

    @FXML
    private ImageView diamondsIV;

    //The whole game flow is being controlled by this class
    GameModelReactive gameModel = new GameModelReactive();

    //Only p1 Player is interactive and controlled by user
    InteractivePlayerReactive player;

    // Map player hands to panes
    Map<PlayerReactive, Pane> hands = new HashMap<>();

    public CrazyEightsReactiveController() {}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Controller class fields ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @FXML
    void initialize() {
        // Set everything what doesn't change during whole game in this initialize method

        pileImg.setFitWidth(100); //Setting its max width
        pileImg.setFitHeight(140); //Setting its max height

        gameModel.getPile().addListener((ListChangeListener<? super CardReactive>) c -> {
            if (c.wasAdded()){
                // Card was added to pile
                // it takes the first card from the stock and puts it on the pile
                // When a GameModel putStarterOnPile() method is called, this event will be fired,
                // as well as after every update
                pileImg.setImage(gameModel.getTopCardFromPile().getCardFront()); //Filling the imageView with a card front image
            }
        });

        gameModel.getStock().addListener((ListChangeListener<? super CardReactive>) c -> {
            if (c.wasRemoved()){
                if (c.getRemovedSize() == 0){
                    deckImg.setImage(null);
                }
            }
        });

        gameModel.turnPlayerProperty().addListener((observable, oldTurnPlayer, turnPlayer) -> {
            if (turnPlayer == this.player){

            }
        });

        gameModel.suitProperty().addListener((observable, oldSuit, newSuit) -> {
            //Automatically update suit symbol on respective change in gameModel
            suitSymbol.setImage(gameModel.getSuit().getSymbol());
        });


        //Prepares a fresh, brand new deck of cards
        //Adds four players to the game

        gameModel.init();
        this.player = gameModel.getInteractivePlayer();

        List<BotPlayerReactive> bots = gameModel.getBotPlayers();

        // Craete mapping between players and their hands
        //A set of four boxes representing players hands is being created
        hands.put(this.player, box1);
        hands.put(bots.get(0), box2);
        hands.put(bots.get(1), box3);
        hands.put(bots.get(2), box4);

        addCardsToHands();

        gameModel.getTurnPlayer().getCards().addListener((ListChangeListener<? super CardReactive>) c -> {
            if (c.wasUpdated()){
                // rerender appropriate hand view
                // addCardToHand(gameModel.getTurnPlayer());
            }
        });



        // We may want to animate this (eg card position)
        //Deal 8 cards to each player
        gameModel.beginTheDeal();

        gameModel.putStarterOnPile();


        initSuitSymbolSelector();
    }

    public void initSuitSymbolSelector(){
        suitSymbol.setFitWidth(100);
        suitSymbol.setFitHeight(100);
        suitSymbol.setImage(Suit.SPADES.getSymbol());

        clubsIV.setImage(Suit.CLUBS.getSymbol());
        diamondsIV.setImage(Suit.DIAMONDS.getSymbol());
        heartsIV.setImage(Suit.HEARTS.getSymbol());
        spadesIV.setImage(Suit.SPADES.getSymbol());
    }

    // Called only once at the beginning when we need to update whole cards at once.
    // After game is started, cards are updated individually
    public void addCardsToHands(){
        //Each player that was invited to the gamemodel
        //Gets a container for each card added to his box

        hands.forEach((playerReactive, pane) -> {

            //Correct spacings between each card are being set
            // They are negative to make overlapping cards effect.
            if (pane instanceof HBox){
                ((HBox) pane).setSpacing(-60);
            } else if (pane instanceof VBox){
                ((VBox) pane).setSpacing(-100);
            }

            for (CardReactive card: playerReactive.getCards()) {

                ImageView imageView = new ImageView();  //Creating a new ImageView

                imageView.setFitWidth(100);             //Setting its max width
                imageView.setFitHeight(140);            //Setting its max height
                imageView.setPreserveRatio(true);       //Keeps the original value of the image
                imageView.setSmooth(true);              //Smoothens the image a litte bit
                imageView.setCache(true);               //Sets image caching boolean value to true

                // We do not need to differentiate between 0 and 180 degrees
                // and between 270 and 90 because cards look the same from both sides.
                // We need to only differentiate between horizontal and vertical orientation.
                if(pane instanceof VBox){
                    imageView.setRotate(imageView.getRotate()+90); //Sets the appropriate imageview rotation depending on the box
                }

                pane.getChildren().add(imageView); // Adds the image view to the box

                // Set click listeners for interactive Player cards
                if (playerReactive == this.player){ //If the card is going to be added to a player box
                    //Then the card front image is being set and an onClick listener method is being added to it making it interactive
                    imageView.setOnMouseClicked(event -> onCardClicked(card, imageView));
                    imageView.setImage(card.getCardFront());
                } else { //If the box belongs to one of the 3 bot players left
                    //Then the card back image is being shown
                    imageView.setImage(CardReactive.getCardBack()); //Populates the ImageView with selected image
                }

            }
        });
    }

    public void initDeckImage(){
        deckImg.setFitWidth(100);                                   //Setting its max width
        deckImg.setFitHeight(120);                                  //Setting its max height
        deckImg.setImage(CardReactive.getCardBack());  //Filling the imageView with a card back image. An image of deck laying on the table is being set
        //Adding a onclick listener to the card laying on the top of the deck. While clicked, it executes cardDeal()
        deckImg.setOnMouseClicked(event -> {onCardDealt(); System.out.println("deck clicked");});
    }

    public void onClickStart(){
        //A method that executes when a big start button is being clicked by the user
        addCardsToHands();
        startButton.setVisible(false);                              //A start button is being hidden
        initDeckImage();
    }

    @FXML
    void onSelectSuitClick(Event event) {
        ImageView object =  (ImageView) event.getSource();
        switch (object.getId()) {
            case "spadesIV" -> {
                player.selectSuit(Suit.SPADES);
            }
            case "diamondsIV" -> {
                player.selectSuit(Suit.DIAMONDS);
            }
            case "heartsIV" -> {
                player.selectSuit(Suit.HEARTS);
            }
            case "clubsIV" -> {
                player.selectSuit(Suit.CLUBS);
            }
        }

        hBoxsOfSuits.setVisible(false);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void onCardClicked(CardReactive card, ImageView imageView) {
        //A method that is being added to all of the cardImages belonging to player, making them interactive
        //After the card is clicked, it is being checked if it was already selected or rather not
        if (card.isSelected()){
            //unselect card
            imageView.getStyleClass().remove("clicked"); //removes card css class that styles it as selected
            card.setSelected(false);                        //sets the card boolean "selected" value to false
            //It is not necessary anymore, because card hold info about if is selected or not
//            player.unselectCard(card);                          //removes the card from player selected cards list
        } else {
            //select card if it agree with rules
            card.setSelected(true);
            imageView.getStyleClass().add("clicked");
//            if (p1.selectCard(card)){                       //Checks if the game rules allow player to select this particular card
//                card.setSelected(true);                     //sets the card boolean "selected" value to true
//                imageView.getStyleClass().add("clicked");   //adds card css class that styles it as selected
//            }
        }
        System.out.println(card.toString() + " selected: " + card.isSelected()); //An output statement to check if everything worked well
    }

    private void onCardDealt() {
        gameModel.setTurnPlayer(p1);        //Usunąć w późniejszych wersjach !!!
        int check = gameModel.dealCard();                   //calls the gameModel dealcard() method, the method returns 0 if succedeed, 1 when failed
        if(check == 0) {                                    //if the card was drawn successfully
            int pointer = p1.getCards().size() - 1;         //a pointer to the top card of the pile is being set
            System.out.println(pointer);                    //a sout to check if everything is correct
            String suit = p1.getCards().get(pointer).getSuit().toString();                  //A cards suit
            String denomination = p1.getCards().get(pointer).getDenomination().toString();  //and denomination are saved
            addImageViewToBox( denomination + suit, p1.getCards().get(pointer), box1);   //Adds a card to a players box
            // change back cover of card from the deck
            deckImg.setImage(Card.getCardBack());//sets the stock image to the nex card's back   (Czy to jest potrzebne? Każda karta ma taki sam rewers)
        } else {
            deckImg.setImage(null);                         //If the method fails, an image is being removed, we are out of cards
        }
    }


    @FXML
    void onConfirmedClicked(ActionEvent event) {
        // A method executing when the user clicks a GUI "confirm" button
        gameModel.setTurnPlayer(p1);    // Usunąć w późniejszej wersji!!!!!!!!
        if (p1.getSelectedCards().get(0).getDenomination() == Denomination.EIGHT && gameModel.playCards()) {// GameModel playCards() method is being called
            hBoxsOfSuits.setVisible(true);
            render();
        }
        else if (gameModel.playCards())
            render();
        pileImg.setImage(gameModel.getTopCardFromPile().getCardFront()); //A card from the users box is being put on the pile
    }

    @FXML
    void onResetClicked(ActionEvent event) {
        // A method executing when the user clicks a GUI "reset" button
        for(Node node : box1.getChildren()){
            node.getStyleClass().remove("clicked"); //removes "clicked" styling from all of the players cards
            //Dodać tutaj usunięcie karty z listy Selected!!!!
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}//End of controller class file
