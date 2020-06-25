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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginViewController extends Preloader implements Initializable {

	@FXML
	private PasswordField passwordTextField;
	private final String password = "miras";

	private VBox root;
	private Scene scene;
	private static FXMLLoader loader = null;
	@FXML
	private Label warningMsg;

	public void start(Stage primaryStage) throws Exception {
		LoginViewController controller = LoginViewController.loadController();
		primaryStage.setScene(controller.loadMainScene());
		primaryStage.show();
	}

	public static LoginViewController loadController() throws IOException {
		VBox root = null;
		Scene scene = null;
		if (loader == null) {
			loader = new FXMLLoader();
			loader.setLocation(LoginViewController.class.getResource("LoginView.fxml"));
			root = (VBox) loader.load();
			scene = new Scene(root, root.getMinWidth(), root.getMinHeight());
		}
		LoginViewController controller = loader.getController();
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
		this.warningMsg.setVisible(false);

	}

	@FXML
	void onClickLoginBtn(ActionEvent event) {
		if (passwordTextField.getText().equals(password)) {
			// the password is correct move to next window.
			Node source = (Node) event.getSource();
			Window theStage = source.getScene().getWindow();
			HomePageViewController homePage;
			try {
				homePage = HomePageViewController.loadController();
				homePage.start((Stage) theStage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			this.warningMsg.setVisible(true);
		}
	}

	@FXML
	void passTxtFieldChanged(KeyEvent event) {
		this.warningMsg.setVisible(false);

	}

}
