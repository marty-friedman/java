package com.egrasoft.ds3calc.model;

import javafx.scene.control.ListView;
import javafx.scene.control.Tab;

import java.io.Serializable;

public enum ArmorSlot implements Serializable {
    HELM, ARMOR, GAUNTLETS, LEGS;

    private transient ListView<ArmorItem> list;

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

    public ListView<ArmorItem> getList() {
        return list;
    }

    public void setList(ListView<ArmorItem> list) {
        this.list = list;
    }
}
