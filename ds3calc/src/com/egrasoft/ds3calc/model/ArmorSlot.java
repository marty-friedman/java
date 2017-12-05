package com.egrasoft.ds3calc.model;

import com.egrasoft.ds3calc.Main;
import javafx.scene.control.ListView;

import java.io.Serializable;
import java.util.ResourceBundle;

public enum ArmorSlot implements Serializable {
    HELM, ARMOR, GAUNTLETS, LEGS;

    private String tabName;

    ArmorSlot(){
        this.tabName = Main.strings.getString("tabName_"+toString());
    }

    public static ArmorSlot of(String type){
        switch(type){
            case "Шлем":
                return HELM;
            case "Доспех":
                return ARMOR;
            case "Перчатки":
                return GAUNTLETS;
            case "Сапоги":
                return LEGS;
            default:
                return null;
        }
    }

    public String getTabName() {
        return tabName;
    }
}
