package com.egrasoft.ds3calc.custom;

import com.egrasoft.ds3calc.Main;
import com.egrasoft.ds3calc.model.ArmorItemStat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StatSettingsPane extends FlowPane{

    @FXML
	private ImageView imageView;
    @FXML
	private Label nameLabel;
    @FXML
	private Label fromLabel;
    @FXML
	private TextField fromTextField;
    @FXML
	private Label toLabel;
    @FXML
	private TextField toTextField;
    @FXML
	private Label weightLabel;
    @FXML
	private TextField weightTextField;

    private ArmorItemStat stat;

    public StatSettingsPane(ArmorItemStat stat) {
        this.stat = stat;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatSettingsPane.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize(){
        imageView.setImage(new Image(Main.class.getResourceAsStream(stat.getImgUrl())));
        nameLabel.setText(stat.getName());
        fromLabel.setText(Main.strings.getString("from"));
        fromTextField.setText(Main.strings.getString("NA"));
        toLabel.setText(Main.strings.getString("to"));
        toTextField.setText(Main.strings.getString("NA"));
        weightLabel.setText(Main.strings.getString("weight"));
        weightTextField.setText("1.0");
    }

    public TextField[] getTextFields(){ return new TextField[]{fromTextField, toTextField, weightTextField}; }

}
