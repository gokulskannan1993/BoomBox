/**
 * 
 */
package boomBox;

import java.io.File;



import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;

//imports for the components in this application
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Slider; 


//imports for layout
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

import javafx.geometry.Insets;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author gokul
 *
 */
public class BoomBox extends Application {

	//attributes
	Label lblFolder, lblPlaylist, lblStatus, lblVolume;
	Button btnPlay, btnAdd, btnRemove, btnStop, btnPause, btnRemoveall;
	ListView<String> lvFolder, lvPlaylist;
	Slider slTrack, slVolume;
	MediaPlayer mp;
	
	//observablelist for the Playlists
	ObservableList<String> myPlaylist = FXCollections.observableArrayList();
	
	
	public BoomBox() {}
	
	@Override
	public void init() {
		//create labels
		lblFolder = new Label("Available Tracks");
		lblPlaylist = new Label("Selected Tracks");
		lblStatus = new Label("Status:");
		lblVolume = new Label("Volume:");
		//create buttons
		btnAdd = new Button("Add >");
		btnPlay = new Button("Play");
		btnRemove = new Button("< Remove");
		btnStop = new Button("Stop");
		btnPause = new Button("Pause");
		btnRemoveall = new Button("<< Remove All");
		
		
		//create listview
		lvFolder = new ListView<String>();
		lvPlaylist = new ListView<String>();
		
		
		//slider
		slTrack = new Slider();
		slVolume = new Slider();
		
		//btn size
		btnAdd.setMinSize(120, 30);
		btnPlay.setMinSize(120, 30);
		btnRemove.setMinSize(120, 30);
		btnStop.setMinSize(120, 30);
		btnPause.setMinSize(120, 30);
		btnRemoveall.setMinSize(120, 30);
		
		
		//handling Play event
		btnPlay.setOnAction(ae ->{
			
			String selectedItem = lvPlaylist.getSelectionModel().getSelectedItem().toString();
			Media media = new Media(new File("./music/"+selectedItem).toURI().toString());
			mp = new MediaPlayer(media);
			
			mp.currentTimeProperty().addListener((InvalidationListener) ->{
				Duration trackDuration = mp.getTotalDuration();
				Duration duration = mp.getCurrentTime();
				
				//calculate the track slider position.
				slTrack.setValue((duration.toSeconds() * 100/trackDuration.toSeconds()));

				//Show the current track position in a label.
						
				double minutes = Math.floor(duration.toMinutes());
				double seconds = Math.floor(duration.toSeconds() % 60);
				
				int m = (int) minutes;
				int s = (int) seconds;
				
				lblStatus.setText("Status: Playing " + m + ":" + s);
			
			});
			
			mp.play();
		});
		
		//handling Add event
		btnAdd.setOnAction(ae ->{
			//get the selection from folder
			String selectedItem = lvFolder.getSelectionModel().getSelectedItem().toString();
			myPlaylist.add(selectedItem);
			lvPlaylist.setItems(myPlaylist);
		});
		
		//handling Stop event
		btnStop.setOnAction(ae ->{
			mp.stop();
		});
		
		//handling remove Event
		btnRemove.setOnAction(ae ->{
			String selectedItem = lvPlaylist.getSelectionModel().getSelectedItem().toString();
			myPlaylist.remove(selectedItem);
		});
		
		//handling removeall event
		btnRemoveall.setOnAction(ae ->{
			myPlaylist.clear();
		});
		
		//handling pause event
		btnPause.setOnAction(ae -> {
			mp.pause();
		});
		
		
		//handling the volumeslider
		slVolume.setValue(50);//default volume
		
		//change the volume on sliding
		slVolume.valueProperty().addListener(new InvalidationListener() {
			
			@Override
			public void invalidated(Observable observable) {
				mp.setVolume(slVolume.getValue()/100);
				
			}
		});
		

	
	//populate the list view	
	lvFolder.setItems(getMusicFiles());	
		
		
	}//init()
	
	
	public void run() {
        mp.setVolume(slVolume.getValue());

    }
	
	
	
	//populate the available tracks to the folder listView
	public ObservableList<String> getMusicFiles(){
		//observablelist for the music files
		ObservableList<String> musicFiles = FXCollections.observableArrayList();
		
		//String array to store a  list of playable files
		String [] fileList;
		
		//Dot Forwardslash means it is local to the current location.
		File f = new File("./music");
		
		//call list() to get a directory listing
		fileList = f.list();
		
		//add the array of files to the music files observable list
		musicFiles.addAll(fileList);
		
		//return the observable list
		return musicFiles;
		
	}//getMusicFiles()
	
	
	@Override
	public void start(Stage pStage) throws Exception {
		//set the title
		pStage.setTitle("Boom Box");
		
		//set the width and Height
		pStage.setHeight(600);
		pStage.setWidth(800);
		pStage.setResizable(false);
		
		
		//create a layout
		GridPane gp = new GridPane();
		VBox vb = new VBox();

		//add components
		gp.add(lblFolder, 0, 0);
		gp.add(lvFolder, 0, 1, 1, 5);
		vb.getChildren().addAll(btnAdd, btnRemove,btnRemoveall, btnPlay,btnPause, btnStop, lblVolume, slVolume);
		gp.add(vb, 1, 1);
		gp.add(lblPlaylist, 2, 0);
		gp.add(lvPlaylist, 2, 1, 1, 5);
		gp.add(slTrack, 0, 7, 3, 1);
		gp.add(lblStatus, 0, 6);
		
	
		
		//setpadding
		gp.setPadding(new Insets(20));
		vb.setPadding(new Insets(20));
		vb.setSpacing(10);
				
		
		//create a scene
		Scene s = new Scene(gp);
		
		//applying the css
		s.getStylesheets().add("styles.css");
		//set the scene
		pStage.setScene(s);
		
		//show the stage
		pStage.show();
		

		
	}//start()
	
	@Override
	public void stop() {
		
	}//stop()

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//launch the app
		launch();	
	}

}
