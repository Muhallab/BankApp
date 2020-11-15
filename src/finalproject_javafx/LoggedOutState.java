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
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Line;
/**
 *
 * @author Almuhallab Alsiyabi
 */
public class LoggedOutState extends AppState {
	Scene loginScene;
	
	@Override
	public void setGUI(BankApp app){
		Pane loginPane = new Pane();
		
		Label loginScreenLable = new Label();
		setDimensions(loginScreenLable, 10, 0, 280, loginPane);
		loginScreenLable.setText("Enter Login Information");
		loginScreenLable.setAlignment(Pos.CENTER);
		loginScreenLable.setFont(new Font("Calibri", 20));
		
		Line topLineBreak = new Line();
		topLineBreak.setStartX(0);
		topLineBreak.setEndX(300);
		topLineBreak.setStartY(35);
		topLineBreak.setEndY(35);
		loginPane.getChildren().add(topLineBreak);
		
		Label usernameLabel = new Label();
		setDimensions(usernameLabel, 10, 50, 135, loginPane);
		usernameLabel.setText("User Name");
		usernameLabel.setAlignment(Pos.CENTER_RIGHT);
		usernameLabel.setFont(new Font("Calibri", 20));
		
		Label passwordLabel = new Label();
		setDimensions(passwordLabel, 10, 90, 135, loginPane);
		passwordLabel.setText("Password");
		passwordLabel.setAlignment(Pos.CENTER_RIGHT);
		passwordLabel.setFont(new Font("Calibri", 20));
		
		Label roleLabel = new Label();
		setDimensions(roleLabel, 10, 130, 135, loginPane);
		roleLabel.setText("Role");
		roleLabel.setAlignment(Pos.CENTER_RIGHT);
		roleLabel.setFont(new Font("Calibri", 20));

		TextField usernameField = new TextField();
		setDimensions(usernameField, 155, 50, 135, loginPane);
		TextField passwordField = new TextField();
		setDimensions(passwordField, 155, 90, 135, loginPane);
		
		ToggleGroup roleSelect = new ToggleGroup();
		RadioButton memberButton = new RadioButton();
		setDimensions(memberButton, 155, 120, 290, loginPane);
		memberButton.setText("Customer");
		memberButton.setUserData("customer");
		memberButton.setToggleGroup(roleSelect);
		memberButton.setSelected(true);
		RadioButton managerButton = new RadioButton();
		setDimensions(managerButton, 155, 140, 290, loginPane);
		managerButton.setText("Manager");
		managerButton.setUserData("manager");
		managerButton.setToggleGroup(roleSelect);
		managerButton.setSelected(false);
		
		Line bottomLineBreak = new Line();
		bottomLineBreak.setStartX(0);
		bottomLineBreak.setEndX(300);
		bottomLineBreak.setStartY(200);
		bottomLineBreak.setEndY(200);
		loginPane.getChildren().add(bottomLineBreak);
		
		Button loginButton = new Button();
		setDimensions(loginButton, 10, 210, 280, loginPane);
		loginButton.setText("Login");
		loginButton.setOnAction(new loginEventHandler(usernameField, passwordField, roleSelect));
		
		loginScene = new Scene(loginPane, 300, 250);
		
		app.stage.setTitle("Login Screen");
		app.stage.setScene(loginScene);
		app.stage.show();
	}
	
	private static class loginEventHandler implements EventHandler<ActionEvent> {
		private final TextField usernameField;
		private final TextField passwordField;
		private final ToggleGroup roleSelect;
		public loginEventHandler(TextField usernameField, TextField passwordField, ToggleGroup roleSelect) {
			this.usernameField = usernameField;
			this.passwordField = passwordField;
			this.roleSelect = roleSelect;
		}
		
		@Override
		public void handle(ActionEvent e) {
			if((usernameField.getText().equals("admin")) && (passwordField.getText().equals("admin"))
					&& (roleSelect.getSelectedToggle().getUserData().toString().equals("manager"))){
				Alert managerAttemptSuccess = new Alert(AlertType.NONE, "Manager Login Success", ButtonType.OK);
				managerAttemptSuccess.setTitle("Login Attempt Detected");
				managerAttemptSuccess.showAndWait();
				BankApp.getSingleton().setState(new ManagerState());
			}else if(roleSelect.getSelectedToggle().getUserData().toString().equals("manager")){
				Alert managerAttemptFailure = new Alert(AlertType.NONE, "Login Failed - Incorrect admin credentials", ButtonType.OK);
				managerAttemptFailure.setTitle("Login Attempt Detected");
				managerAttemptFailure.showAndWait();
			}else if(roleSelect.getSelectedToggle().getUserData().toString().equals("customer")){
				File file = new File(BankApp.getSingleton().currentDirectory + usernameField.getText() + ".txt");
				if(file.exists()){
					try{
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String readPass = reader.readLine();
						double readBalance = Double.parseDouble(reader.readLine());
						if(readPass.equals(passwordField.getText())){
							BankApp.getSingleton().currentMemberFile = file;
							Alert customerAttemptSuccess = new Alert(AlertType.NONE, "Customer Login Success", ButtonType.OK);
							customerAttemptSuccess.setTitle("Login Attempt Detected");
							customerAttemptSuccess.showAndWait();
							if(readBalance < 10000){BankApp.getSingleton().setState(new SilverState());}
							else if((readBalance >= 10000) && (readBalance < 20000)) {BankApp.getSingleton().setState(new GoldState());}
							else if(readBalance >= 20000) {BankApp.getSingleton().setState(new PlatinumState());}
						}else{
							Alert alert = new Alert(AlertType.NONE, "Login Failed - Incorrect user credentials", ButtonType.OK);
							alert.setTitle("Login Attempt Detected");
							alert.showAndWait();
						}
					}catch(IOException error){
						Alert customerAttemptFailure = new Alert(AlertType.NONE, "Login Failed - Incorrect user credentials", ButtonType.OK);
						customerAttemptFailure.setTitle("Login Attempt Detected");
						customerAttemptFailure.showAndWait();
					}
				}else{
					Alert customerNotFound = new Alert(AlertType.NONE, "Login Failed - No such user exists", ButtonType.OK);
					customerNotFound.setTitle("Login Attempt Detected");
					customerNotFound.showAndWait();
				}
			}
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
