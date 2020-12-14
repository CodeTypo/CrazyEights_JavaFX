import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
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
    private Button confirmButton;

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

    @FXML
    private AnchorPane table;

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
        suitSymbol.setVisible(false);

        gameModel.getPile().addListener((ListChangeListener<? super CardReactive>) c -> {
            System.out.println("Pile changed");
            while (c.next()){
                if (c.wasAdded()){
                    // Card was added to pile
                    // it takes the first card from the stock and puts it on the pile
                    // When a GameModel putStarterOnPile() method is called, this event will be fired,
                    // as well as after every update

                    //A card from the users box is being put on the pile
                    pileImg.setImage(gameModel.getTopCardFromPile().getCardFront()); //Filling the imageView with a card front image
                    gameModel.setSuit(gameModel.getTopCardFromPile().getSuit());
                }
            }
        });

        gameModel.getStock().addListener((ListChangeListener<? super CardReactive>) c -> {
            System.out.println("Stock changed");
            while (c.next()){
                if (c.wasRemoved()){
                    if (c.getList().size()==0){
                        deckImg.setImage(null); //An image is being removed, we are out of cards
                    }
                }
            }
        });

        gameModel.turnPlayerProperty().addListener((observable, oldTurnPlayer, turnPlayer) -> {
            System.out.println("TurnPlayer changed from: " + oldTurnPlayer + " to: " + turnPlayer );
            // Only interactive player must confirm his actions.
            // Bots make it automatically after they choose cards.
            // Disable confirmButton, when interactive player doesn't have turn.
            confirmButton.setDisable(turnPlayer != this.player);
        });

        gameModel.suitProperty().addListener((observable, oldSuit, newSuit) -> {
            System.out.println("Suit changed from: " + oldSuit + " to: " + newSuit );
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

        // We may want to animate this (eg card position)
        //Deal 8 cards to each player
        gameModel.beginTheDeal();

        bots.forEach(bot -> {
            bot.getCards().addListener((ListChangeListener<? super CardReactive>) c -> {
                System.out.println(bot + " cards changed");
                while (c.next()) {
                    if (c.wasAdded()) {
                        List<CardReactive> addedCards = (List<CardReactive>) c.getAddedSubList();
                        addedCards.forEach(cardReactive -> {
                            addCardToHand(cardReactive, bot);
                            System.out.println(bot + ": card was added:" + cardReactive);
                        });
                    }

                    if (c.wasRemoved()) {
                        System.out.println(bot + " card was removed");
                        List<CardReactive> removedCards = (List<CardReactive>) c.getRemoved();
                        removedCards.forEach(cardReactive -> {
                            removeCardFromHand(cardReactive, bot);
                            System.out.println(bot + ": card was removed:" + cardReactive);
                        });

                        if (c.getList().isEmpty()){
                            // Player is winner
                            // create a alert
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText(bot + " is winner");
                            alert.show();
                        }

                    } // if c.was removed

                }
            });
        });

        player.getCards().addListener((ListChangeListener<? super CardReactive>) c -> {
            System.out.println(player + " cards changed");
            while (c.next()){
                if (c.wasAdded()){
                    List<CardReactive> addedCards = (List<CardReactive>) c.getAddedSubList();
                    addedCards.forEach(cardReactive -> {
                        addCardToHand(cardReactive, player);
                        System.out.println(player + ": card was added:" + cardReactive);
                    });

                }

                if (c.wasRemoved()) {
                    List<CardReactive> removedCards = (List<CardReactive>) c.getRemoved();
                    removedCards.forEach(cardReactive -> {
                        removeCardFromHand(cardReactive, player);
                        System.out.println(player + ": card was removed:" + cardReactive);
                    });

                    if (c.getList().isEmpty()){
                        // Player is winner
                        // create a alert
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText(player+ " is winner");
                        alert.show();
                    }

                }

            }
        });


        System.out.println(gameModel.getTurnPlayer());

        initSuitSymbolSelector();

        gameModel.setupBots();

        gameModel.setTurnPlayer(this.player);
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

    public void addCardToHand(CardReactive card, PlayerReactive playerReactive){
        Pane pane = hands.get(playerReactive);
        ImageView imageView = createCardView(card, pane);
        setupCardView(card, imageView, playerReactive);
        pane.getChildren().add(imageView); // Adds the image view to the box
        imageView.setVisible(false);


        // Animation

        //Creating a new image view only for the animation purposes
        ImageView animationIV = new ImageView();
        animationIV.setImage(CardReactive.getCardBack());
        table.getChildren().add(animationIV);//Adding the image view to the AnchorPane

        //A new TranslateTransformation is being created, so far it works only with the interactivePlayer hand, bot support coming soon
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2),animationIV);
        transition.setFromX(deckImg.getLayoutX());
        transition.setFromY(deckImg.getLayoutY());

        Bounds boundsInScene = imageView.localToScene(imageView.getBoundsInLocal());

        transition.setToX(boundsInScene.getMaxX());
        transition.setToY(boundsInScene.getCenterY()-boundsInScene.getMaxY());

        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {//When the animation finishes
                table.getChildren().remove(animationIV);//The image view is being removed
                imageView.setVisible(true); // make card visible now
            }
        });

    }

    public void removeCardFromHand(CardReactive card, PlayerReactive playerReactive){
        Pane pane = hands.get(playerReactive);
        FilteredList<Node> toRemove = pane.getChildren().filtered(node -> node.getId().equals(card.getId()));
        pane.getChildren().remove(toRemove.get(0));
    }

    public ImageView createCardView(CardReactive card, Pane pane){
        ImageView imageView = new ImageView();  //Creating a new ImageView

        imageView.setId(card.getId()); // Set image id to card id. In this way we bind these two together.

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

        return imageView;
    }

    public void setupCardView(CardReactive card, ImageView imageView, PlayerReactive playerReactive){
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
                ImageView imageView = createCardView(card, pane);
                setupCardView(card,imageView, playerReactive);
                pane.getChildren().add(imageView); // Adds the image view to the box
            }
        });
    }

    public void initDeckImage(){
        deckImg.setFitWidth(100);                                   //Setting its max width
        deckImg.setFitHeight(120);                                  //Setting its max height
        deckImg.setImage(CardReactive.getCardBack());  //Filling the imageView with a card back image. An image of deck laying on the table is being set
        //Adding a onclick listener to the card laying on the top of the deck. While clicked, it executes cardDeal()
        deckImg.setOnMouseClicked(event -> {
            if (gameModel.getTurnPlayer() == this.player){
                // Call method only if interactive Player has turn
                onCardDealt();
            }
//            System.out.println("deck clicked");
        });
    }

    public void onStartClick(){
        //A method that executes when a big start button is being clicked by the user
        addCardsToHands();
        gameModel.putStarterOnPile();
        startButton.setVisible(false);                              //A start button is being hidden
        initDeckImage();
        suitSymbol.setVisible(true);
    }

    @FXML
    void onSelectSuitClick(Event event) {
        ImageView object =  (ImageView) event.getSource();
        switch (object.getId()) {
            case "spadesIV" -> gameModel.setSuit(Suit.SPADES);
            case "diamondsIV" -> gameModel.setSuit(Suit.DIAMONDS);
            case "heartsIV" -> gameModel.setSuit(Suit.HEARTS);
            case "clubsIV" -> gameModel.setSuit(Suit.CLUBS);
        }

        hBoxsOfSuits.setVisible(false);
        gameModel.nextPlayerTurn();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void onCardClicked(CardReactive card, ImageView imageView) {
        //A method that is being added to all of the cardImages belonging to player, making them interactive
        //After the card is clicked, it is being checked if it was already selected or rather not
        if (card.isSelected()){
            //unselect card
            imageView.getStyleClass().remove("clicked"); //removes card css class that styles it as selected
            player.unselectCard(card);                      //sets the card boolean "selected" value to false
            //It is not necessary anymore, because card hold info about if is selected or not
//            player.unselectCard(card);                          //removes the card from player selected cards list
        } else {
            //select card if it agree with rules
            // only cards of interactive player can be selected
            player.selectCard(card); //Checks if the game rules allow player to select this particular card
            if (card.isSelected()){ //sets the card boolean "selected" value to true if player can select this card
                imageView.getStyleClass().add("clicked"); //adds card css class that styles it as selected
            }

        }
        System.out.println(card.toString() + " selected: " + card.isSelected()); //An output statement to check if everything worked well
    }

    private void onCardDealt() {
        //gameModel.setTurnPlayer(player); //Usunąć w późniejszych wersjach !!!

        //Creating a new image view only for the animation purposes
        ImageView animationIV = new ImageView();
        animationIV.setImage(CardReactive.getCardBack());
        table.getChildren().add(animationIV);//Adding the image view to the AnchorPane

        //A new TranslateTransformation is being created, so far it works only with the interactivePlayer hand, bot support coming soon
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2),animationIV);
        transition.setFromX(deckImg.getLayoutX());
        transition.setFromY(deckImg.getLayoutY());
        transition.setToX(box1.getWidth());
        transition.setToY(box1.getLayoutY()-box1.getPrefHeight());

        player.dealCard(gameModel.takeTopCardFromStock());//The card is being dealt

        box1.getChildren().get(box1.getChildren().size()-1).setVisible(false);//It is invisible until the animation ends
        transition.play();
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {//When the animation finishes
                table.getChildren().remove(animationIV);//The image view is being removed
                box1.getChildren().get(box1.getChildren().size()-1).setVisible(true);//The card drawn is now visible

            }
        });
    }


    @FXML
    void onConfirmedClicked(ActionEvent event) {
        // A method executing when the user clicks a GUI "confirm" button
        // This button is enabled only when interactive player has turn

        // gameModel.playCards() calls corresponding method from Player class
        if (gameModel.playCards()){ // Play selected cards and check if show selector
            // If after interactive player's turn, there is crazy eight
            // on pile, he can select suit
            hBoxsOfSuits.setVisible(true);
        }


//        if (gameModel.getTopCardFromPile().getDenomination() == Denomination.EIGHT){
//            hBoxsOfSuits.setVisible(true);
//        }

    }

    @FXML
    void onResetClicked(ActionEvent event) {
        // A method executing when the user clicks a GUI "reset" button
        for(Node node : box1.getChildren()){
            node.getStyleClass().remove("clicked"); //removes "clicked" styling from all of the players cards
            //Dodać tutaj usunięcie karty z listy Selected!!!!
            //TO DO
        }

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}//End of controller class file
