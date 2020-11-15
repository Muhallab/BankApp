/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject_javafx;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 *
 * @author 
 */
public class BankApplicationDemo extends Application{
	@Override
	public void start(Stage primaryStage){
		BankApp.getSingleton().stage = primaryStage;
		BankApp.getSingleton().setState(new LoggedOutState());
	}
	public static void main(String[] args){
		launch(args);
	}
}
