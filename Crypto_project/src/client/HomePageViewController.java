package client;

import java.io.IOException;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePageViewController extends Preloader {
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

}
