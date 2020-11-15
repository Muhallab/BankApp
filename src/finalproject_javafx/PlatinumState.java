/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject_javafx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

/**
 *
 * @author  
 */
public class PlatinumState extends AppState{
	Scene platinumScene;
	public String username = "";
	public String password = "";
	public double balance = 0;
	
	@Override
	public void setGUI(BankApp app){
		Pane platinumPane = new Pane();
		platinumScene = new Scene(platinumPane, 300, 250);
		
		getUserDetails();
		
		Label silverScreenLabel = new Label();
		setDimensions(silverScreenLabel, 10, 0, 280, platinumPane);
		silverScreenLabel.setText("User \"" + username + "\" Account Center");
		silverScreenLabel.setAlignment(Pos.CENTER);
		silverScreenLabel.setFont(new Font("Calibri", 20));
		
		Line topLineBreak = new Line();
		topLineBreak.setStartX(0);
		topLineBreak.setEndX(300);
		topLineBreak.setStartY(35);
		topLineBreak.setEndY(35);
		platinumPane.getChildren().add(topLineBreak);
		
		Label amountLabel = new Label();
		setDimensions(amountLabel, 10, 50, 135, platinumPane);
		amountLabel.setText("$ Amount");
		amountLabel.setAlignment(Pos.CENTER_RIGHT);
		amountLabel.setFont(new Font("Calibri", 20));
		
		TextField amountField = new TextField();
		setDimensions(amountField, 155, 50, 135, platinumPane);
		
		Button depositButton = new Button();
		setDimensions(depositButton, 10, 90, 280, platinumPane);
		depositButton.setText("Deposit");
		depositButton.setOnAction(new depositEventHandler(amountField, this));
		Button withdrawButton = new Button();
		setDimensions(withdrawButton, 10, 120, 140, platinumPane);
		withdrawButton.setText("Withdraw");
		withdrawButton.setOnAction(new withdrawEventHandler(amountField, this));
		Button ePurchaseButton = new Button();
		setDimensions(ePurchaseButton, 150, 120, 140, platinumPane);
		ePurchaseButton.setText("ePurchase");
		ePurchaseButton.setOnAction(new ePurchaseEventHandler(amountField, this));		
		Button checkBalanceButton = new Button();
		setDimensions(checkBalanceButton, 10, 160, 280, platinumPane);
		checkBalanceButton.setText("Check Balance");
		checkBalanceButton.setOnAction(new checkBalanceEventHandler(this));
				
		Line bottomLineBreak = new Line();
		bottomLineBreak.setStartX(0);
		bottomLineBreak.setEndX(300);
		bottomLineBreak.setStartY(200);
		bottomLineBreak.setEndY(200);
		platinumPane.getChildren().add(bottomLineBreak);
		
		Button loginButton = new Button();
		setDimensions(loginButton, 10, 210, 280, platinumPane);
		loginButton.setText("Logout");
		loginButton.setOnAction(new logoutEventHandler(this));
		
		app.stage.setTitle("Manager Screen");
		app.stage.setScene(platinumScene);
		app.stage.show();
	}

	private void getUserDetails() {
		File file = BankApp.getSingleton().currentMemberFile;
		this.username = file.getName().replace(".txt", "");
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			password = reader.readLine();
			balance = Double.parseDouble(reader.readLine());
		}catch(IOException error){
			Alert customerAttemptFailure = new Alert(Alert.AlertType.NONE, "User Account File Parsing Failed", ButtonType.OK);
			customerAttemptFailure.setTitle("File IO Error");
			customerAttemptFailure.showAndWait();
			BankApp.getSingleton().setState(new LoggedOutState());
		}
		System.out.println("Read Info - Username "+username+" -Password- "+password+" -Balance- "+balance);
	}
	
	public void rewriteUserDetails(){
		try{
			File file = BankApp.getSingleton().currentMemberFile;
			List<String> lines = Arrays.asList(""+password+"",""+(Math.round(balance*100))/100.00+"");
			Path path = Paths.get(BankApp.getSingleton().currentDirectory + username + ".txt");
			Files.write(path, lines, StandardCharsets.UTF_8);
		}catch(IOException error){
			Alert fileSaveAttemptError = new Alert(Alert.AlertType.NONE, "User Account File Saving Failed : " + error, ButtonType.OK);
			fileSaveAttemptError.setTitle("File IO Error");
			fileSaveAttemptError.showAndWait();
		}
	}
	
	private static class depositEventHandler implements EventHandler<ActionEvent> {
		private PlatinumState currentState;
		private TextField userInputField;
		public depositEventHandler(TextField userInputField, PlatinumState currentState){
			this.currentState = currentState;
			this.userInputField = userInputField;
		}
		@Override
		public void handle(ActionEvent e) {
			if(userInputField.getText().matches("-?\\d+(\\.\\d+)?")){
				System.out.println("Old $"+currentState.balance);
				currentState.balance = currentState.balance + Double.parseDouble(userInputField.getText());
				System.out.println("New $"+currentState.balance);
				
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Deposit complete, new balance $"+currentState.balance, ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				
				currentState.rewriteUserDetails();
			}else{
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Please enter a valid number for transaction value", ButtonType.OK);
				balanceReadOut.setTitle("User Input Error");
				balanceReadOut.showAndWait();
			}
		}
	}
	
	private static class withdrawEventHandler implements EventHandler<ActionEvent> {
		private PlatinumState currentState;
		private TextField userInputField;
		public withdrawEventHandler(TextField userInputField, PlatinumState currentState){
			this.currentState = currentState;
			this.userInputField = userInputField;
		}
		@Override
		public void handle(ActionEvent e) {
			if(userInputField.getText().matches("-?\\d+(\\.\\d+)?")){
				System.out.println("Old $"+currentState.balance);
				currentState.balance = currentState.balance - Double.parseDouble(userInputField.getText());
				System.out.println("New $"+currentState.balance);
				
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Withdrawal complete, new balance $"+currentState.balance, ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				
				currentState.rewriteUserDetails();
			}else{
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Please enter a valid number for transaction value", ButtonType.OK);
				balanceReadOut.setTitle("User Input Error");
				balanceReadOut.showAndWait();
			}
			
			if((currentState.balance < 10000)){
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Account demoted to Silver Status...", ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				BankApp.getSingleton().setState(new SilverState());
			}else if(currentState.balance < 20000){
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Account demoted to Gold Status...", ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				BankApp.getSingleton().setState(new GoldState());
			}
		}
	}
	
	private static class ePurchaseEventHandler implements EventHandler<ActionEvent> {
		private PlatinumState currentState;
		private TextField userInputField;
		public ePurchaseEventHandler(TextField userInputField, PlatinumState currentState){
			this.currentState = currentState;
			this.userInputField = userInputField;
		}
		@Override
		public void handle(ActionEvent e) {
			if(userInputField.getText().matches("-?\\d+(\\.\\d+)?")){
				System.out.println("Old $"+currentState.balance);
				currentState.balance = currentState.balance - Double.parseDouble(userInputField.getText());
				System.out.println("New $"+currentState.balance);
				
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "ePurchase complete, new balance $"+currentState.balance, ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				
				currentState.rewriteUserDetails();
			}else{
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Please enter a valid number for transaction value", ButtonType.OK);
				balanceReadOut.setTitle("User Input Error");
				balanceReadOut.showAndWait();
			}
			
			if((currentState.balance < 10000)){
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Account demoted to Silver Status...", ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				BankApp.getSingleton().setState(new SilverState());
			}else if(currentState.balance < 20000){
				Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Account demoted to Gold Status...", ButtonType.OK);
				balanceReadOut.setTitle("Account Info Updated");
				balanceReadOut.showAndWait();
				BankApp.getSingleton().setState(new GoldState());
			}
		}
	}
	
	private static class checkBalanceEventHandler implements EventHandler<ActionEvent> {
		private PlatinumState currentState;
		public checkBalanceEventHandler(PlatinumState currentState){
			this.currentState = currentState;
		}
		@Override
		public void handle(ActionEvent e) {
			Alert balanceReadOut = new Alert(Alert.AlertType.NONE, "Current Account Balance is $"+currentState.balance+" - Platinum Status", ButtonType.OK);
			balanceReadOut.setTitle("Account Info Requested");
			balanceReadOut.showAndWait();
		}
	}
	
	private static class logoutEventHandler implements EventHandler<ActionEvent> {
		private PlatinumState currentState;
		public logoutEventHandler(PlatinumState currentState) {
			this.currentState = currentState;
		}
		@Override
		public void handle(ActionEvent e) {
			//Resave password & new balance on exit
			currentState.rewriteUserDetails();
			Alert logoutDetected = new Alert(Alert.AlertType.NONE, "Logging out...", ButtonType.OK);
			logoutDetected.setTitle("Logout Attempt Detected");
			logoutDetected.showAndWait();
			BankApp.getSingleton().currentDirectory = null;
			BankApp.getSingleton().setState(new LoggedOutState());
		}
	}
	
	private static void setDimensions(Control c, int positionX, int positionY, int width, Pane pane){
		c.setMaxHeight(30);
		c.setMinHeight(30);
		c.setMaxWidth(width);
		c.setMinWidth(width);
		c.setLayoutX(positionX);
		c.setLayoutY(positionY);
		pane.getChildren().add(c);
	}
}
