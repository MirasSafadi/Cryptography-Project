package client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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
    private ProgressIndicator spinner;
    
	private VBox root;
	private Scene scene;
	private static FXMLLoader loader = null;

	public void start(Stage primaryStage) throws Exception {
		HomePageViewController controller = HomePageViewController.loadController();
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

	}

	@FXML
	void chooseFile(ActionEvent event) {
		// TODO Auto-generated method stub
		spinner.setProgress(0.1);

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.wav"),
				new ExtensionFilter("All Files", "*.*"));
		Node source = (Node) event.getSource();
		Window theStage = source.getScene().getWindow();
		File selectedFile = fileChooser.showOpenDialog(theStage);
		if (selectedFile != null) {
			filePathTextField.setText(selectedFile.getAbsolutePath());
			spinner.setProgress(1);
		}

	}

}
