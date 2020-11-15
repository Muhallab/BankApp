package finalproject_javafx;

/**
 *
 * @author 
 */

// This class is a mutable singleton as it is a dispatch class that switches states to alter the displayed UI
// It is responsible for maintaining the overall global state of the program and dispatching 
// one of a various number of GUI generation functions depending on its specific present state
import java.io.File;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankApp {
	
	public String currentDirectory = System.getProperty("user.dir") + "\\";
	public AppState currentAppState;
	public File currentMemberFile;
	public Stage stage;
	public Scene scene;
	
	//Singleton Getter - Is responsible for maintaining exactly one cope of the application to maintain state coherency
	//Requires : Nothing
	//Modifies : In the case that the BankApp object was not instantiated, it modifies AppInstance to point to the singular instantiation of the object
	//Effects  : Ensures no duplicates of the program
	public static BankApp getSingleton(){return AppInstance==null ? AppInstance = new BankApp() : AppInstance;}
	
	private static BankApp AppInstance = null;
	
	//Private Constructor - To ensure no undesired copies of the BankApp are able to be constructed
	//Requires : Nothing
	//Modifies : Nothing
	//Effects  : Ensures no duplicates of the program
	private BankApp(){}
	
	//Handling Function - Is responsible for actually altering the state of the program and reseting the GUI as required
	//Requires : A valid concrete implementation of the abstract class AppState to switch states to
	//Modifies : The program's current state and also the JavaFX GUI by calling a specific setGUI()
	//Effects  : Changes the GUI in a responsive fashion
	public void setState(AppState newState){
		currentAppState = newState;
		System.out.println(toString());
		currentAppState.setGUI(this);
	}
	
	//Rep Invariant
	//  currentAppState is always a member of the set {LoggedOutState, ManagerState, SilverState, GoldState, PlatinumState}
	public boolean repOK(){
		return ((currentAppState.getClass()==LoggedOutState.class)||
				(currentAppState.getClass()==ManagerState.class)||
				(currentAppState.getClass()==SilverState.class)||
				(currentAppState.getClass()==GoldState.class)||
				(currentAppState.getClass()==PlatinumState.class));
	}
	
	//Abstract Function
	//  AF = Representation of GUI elements currently being displayed
	//       More detailed information in concrete classes instead of this dispatch class
	@Override
	public String toString(){
		String str = "";
		str += ("Currently displaying the UI for the program state of " + currentAppState.getClass().getSimpleName());
		if (currentMemberFile == null){
			str += ("; No specific member data loaded");
		}else{
			try{
				str += ("; Displaying user data of \"" + currentMemberFile.getCanonicalPath());
				str = str.replace(currentDirectory, "").replace(".txt", "");
				str += "\"";
			}catch(IOException error){
				System.out.println("IO Error Caught : " + error);			
			}
		}
		return str;
	}
}