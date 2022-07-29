
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;
	
	
	
	// HackNight application scene functionality members (non-communication)
	Button teamButton;
	Button managerButton, confirmSignin, confirmTeam, addToTeam;
	TextField EnterTeamNumber = new TextField();
	TextField EnterManagerID = new TextField();
	TextField EnterManagerPass = new TextField();
	TextField AddTeamMember = new TextField();
	TextField CurNumber = new TextField();
	
	
	public HashMap<Integer, ArrayList<String>> TeamsDict = new HashMap<Integer, ArrayList<String>>();
	
	ListView<String> listItems, listItems2;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// for sync project
		teamButton = new Button("Team Member");
		
		
		
		
		
		// Manager Functionality
		managerButton = new Button("Manager");
		managerButton.setOnAction(e-> primaryStage.setScene(sceneMap.get("manager")));
		confirmSignin = new Button("Confirm Manager Sign in");
		confirmSignin.setOnAction(e -> {
			
		});
		
		
		confirmTeam = new Button("ConfirmTeamNumber");
		confirmTeam.setOnAction(e -> {
			String curTeam = EnterTeamNumber.getText();
			
			if (curTeam != "") {
				Integer current = Integer.parseInt(curTeam);
				ArrayList<String> tempS = new ArrayList<String>();
				String mID;
				if (TeamsDict.containsKey(current)) {
					tempS = TeamsDict.get(current);
					mID = "U456";
					// temporary user ID for now !!!!!!
				}
				else {
					mID = "U123";
				}
				tempS.add(mID);
				TeamsDict.put(current, tempS);
				for (int a : TeamsDict.keySet()) {
					System.out.println(TeamsDict.get(a));
				}
				// switches to management screen
				primaryStage.setScene(createManagmentScreen(current));
			}
		});
		
		addToTeam = new Button("Add to Team");
		addToTeam.setOnAction( e -> {
			String curID = CurNumber.getText();
			System.out.println(curID);
			ArrayList<String> tempS = new ArrayList<String>();
			tempS = TeamsDict.get(Integer.parseInt(curID));
			tempS.add(AddTeamMember.getText());
			TeamsDict.put(Integer.parseInt(curID), tempS);
			for (int a : TeamsDict.keySet()) {
				System.out.println(TeamsDict.get(a));
			}
		});
		
		
		
		
		// TODO Auto-generated method stub
		primaryStage.setTitle("SyncBoard");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				});
											
		});
		
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
											primaryStage.setTitle("This is a client");
											clientConnection = new Client(data->{
							Platform.runLater(()->{listItems2.getItems().add(data.toString());
											});
							});
							
											clientConnection.start();
		});
		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		
		c1 = new TextField();
		b1 = new Button("Send");
		b1.setOnAction(e->{clientConnection.send(c1.getText()); c1.clear();});
		
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		sceneMap.put("start", createStartScreen());
		
		// manager screens
		sceneMap.put("managerLogin", createManagerLoginScreen());
		sceneMap.put("manager", createManagerScreen());
		//sceneMap.put("management", createManagmentScreen());
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		 
		primaryStage.setScene(sceneMap.get("start"));
		//primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	
	public Scene createStartScreen() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: blue");
		
		HBox h1 = new HBox(teamButton, managerButton);
		Label startScreen = new Label("Welcome");
		VBox v1 = new VBox(startScreen, h1);
		pane.setCenter(v1);
			
		return new Scene(pane, 500, 400);
		
	}
	
	// scene for manager to sign in
	public Scene createManagerLoginScreen() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: green");
		
		Label startScreen = new Label("Manager Login View");
		Label enterLAN = new Label("Enter LAN ID:");
		Label enterPASS = new Label("Enter Password:");

		HBox h1 = new HBox(enterLAN, EnterManagerID);
		HBox h2 = new HBox(enterPASS, EnterManagerPass);
		
		VBox v1 = new VBox(startScreen, h1, h2, confirmSignin);
		pane.setCenter(v1);
			
		return new Scene(pane, 500, 400);
		
	}
	
	// scene for manager to input their team number
	public Scene createManagerScreen() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: green");
		
		Label startScreen = new Label("Manager View");
		Label enterNum = new Label("Enter Team Number:");
		HBox h1 = new HBox(enterNum, EnterTeamNumber);
		
		VBox v1 = new VBox(startScreen, h1, confirmTeam);
		pane.setCenter(v1);
			
		return new Scene(pane, 500, 400);
		
	}
	
	// scene for manager to manage their team
	public Scene createManagmentScreen(int teamNum) {
				
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: green");
		
		Label startScreen = new Label("Management View for team " + String.valueOf(teamNum));
		CurNumber.setText(String.valueOf(teamNum));
		CurNumber.setDisable(true);
		//Label enterNum = new Label("Enter Team Number:");
		HBox h1 = new HBox(AddTeamMember, addToTeam);
		
		VBox v1 = new VBox(startScreen, h1);
		pane.setCenter(v1);
			
		return new Scene(pane, 500, 400);
		
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		
		pane.setCenter(listItems);
	
		return new Scene(pane, 500, 400);
		
		
	}
	
	
	
	public Scene createClientGui() {
		
		clientBox = new VBox(10, c1,b1,listItems2);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 400, 300);
		
	}

}
