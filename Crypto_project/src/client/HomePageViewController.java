package client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import common.Utils;
import javafx.application.Preloader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class HomePageViewController extends Preloader implements Initializable {
	@FXML
	private TextField filePathTextField;
	@FXML
	private Label storingOutput;
	@FXML
	private ProgressIndicator storingSpinner;
	@FXML
	private TextField directoryTextField;
	@FXML
	private TextField fileNameTextField;
	@FXML
	private Label requestingOutput;
	@FXML
	private ProgressIndicator requestSpinner;
	@FXML
	private Button storeFileBtn;
	@FXML
	private Button getFileBtn;

	Task copyWorker;
	private File selectedFile;
	private VBox root;
	private Scene scene;
	private static FXMLLoader loader = null;
	private String userID;
	private Client client;

	public void start(Stage primaryStage) throws Exception {
		HomePageViewController controller = this;
		primaryStage.setScene(controller.loadMainScene());
		primaryStage.show();
	}

	public static HomePageViewController loadController() throws IOException {
		VBox root = null;
		Scene scene = null;
		if (loader == null) {
			loader = new FXMLLoader();
			loader.setLocation(HomePageViewController.class.getResource("HomePage.fxml"));
			root = (VBox) loader.load();
			scene = new Scene(root, root.getMinWidth(), root.getMinHeight());
		}
		HomePageViewController controller = loader.getController();
		if (root != null) {
			controller.root = root;
		}
		if (scene != null) {
			controller.scene = scene;
		}
		return controller;
	}

	public Scene loadMainScene() throws IOException {
		return scene;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.client = new Client();
	}

	public void setUserProps(String userId) {
		this.userID = userId;
	}

	@FXML
	void chooseFile(ActionEvent event) {
		// TODO Auto-generated method stub
//		this.storingSpinner.setProgress(0.1);
		storingSpinner.progressProperty().unbind();
		storingSpinner.setProgress(0.0);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav"),
				new ExtensionFilter("All Files", "*.*"), new ExtensionFilter("Audio", "*.mp3"));
		Node source = (Node) event.getSource();
		Window theStage = source.getScene().getWindow();
		selectedFile = fileChooser.showOpenDialog(theStage);
		if (selectedFile != null) {
			this.filePathTextField.setText(selectedFile.getAbsolutePath());
//			this.storingSpinner.setProgress(1);
			storeFileBtn.setDisable(false);
		}

	}

	@FXML
	void onClickStoreFileBtn(ActionEvent event) {
		try {
			String wav = Utils.convertWAVtoHEX(this.selectedFile);
			// System.out.println(wav);
			System.out.println("length = " + wav.length());
			String[] output = Utils.Inputsplitter(wav, 32);
			// -------------------------------------------------------------------------------------------//
			// Fill the input file in the correct line length for further processing
		
			FileWriter writer = new FileWriter(this.selectedFile.getParent()+"\\"+"WAVtoHEX.txt");
			int len = output.length;
			for (int i = 0; i < len; i++) {
				writer.write(output[i] + '\n');
				writer.flush();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.storeFile(selectedFile, userID);
		
		storingSpinner.progressProperty().unbind();
		storingSpinner.setProgress(0.0);
		copyWorker = createWorker();
		storingSpinner.progressProperty().bind(copyWorker.progressProperty());
		copyWorker.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			}
		});
		new Thread(copyWorker).start();
		
		
		
	}

	@FXML
	void chooseDir(ActionEvent event) {

	}

	@FXML
	void onClickGetFile(ActionEvent event) {

	}

	public Task createWorker() {
		return new Task() {
			@Override
			protected Object call() throws Exception {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(1000);
					updateMessage(String.valueOf(((i*10) + 10)));
					updateProgress(i + 1, 10);
				}
				return true;
			}
		};
	}

}
