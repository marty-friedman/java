package com.egrasoft.ds3calc.custom;

import com.egrasoft.ds3calc.Main;
import com.egrasoft.ds3calc.model.ArmorItemStat;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

public class SelectedItemStatsPane extends FlowPane {

    @FXML
    private ImageView imageView;
    @FXML
    private Label label;

    private ArmorItemStat stat;

    public SelectedItemStatsPane(ArmorItemStat stat){
        this.stat = stat;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectedItemStatsPane.fxml"));
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
    }

    public Label getLabel() {
        return label;
    }
}
