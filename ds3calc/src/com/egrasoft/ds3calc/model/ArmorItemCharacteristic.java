package com.egrasoft.ds3calc.model;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.util.function.Function;

public enum ArmorItemCharacteristic implements Serializable{
    PHYS_RES(ArmorItem::getPhysRes, Double.class), STRIKE_DEF(ArmorItem::getStrikeDef, Double.class), SLASH_DEF(ArmorItem::getSlashDef, Double.class),
    THRUST_DEF(ArmorItem::getThrustDef, Double.class), MAGIC_DEF(ArmorItem::getMagicDef, Double.class), FIRE_DEF(ArmorItem::getFireDef, Double.class),
    LIGHTNING_DEF(ArmorItem::getLightningDef, Double.class), DARK_DEF(ArmorItem::getDarkDef, Double.class), BLEED_RES(ArmorItem::getBleedRes, Integer.class),
    POISON_RES(ArmorItem::getPoisonRes, Integer.class), FROST_RES(ArmorItem::getFrostRes, Integer.class), CURSE_RES(ArmorItem::getCurseRes, Integer.class),
    POISE(ArmorItem::getPoise, Double.class), DURABILITY(ArmorItem::getDurability, Integer.class), WEIGHT(ArmorItem::getWeight, Double.class);

    private Function<ArmorItem, ? extends Number> func;
    private Class<? extends Number> resClass;
    private transient TextField fromTextField, toTextField, weightTextField;
    private transient Label selectedLabel;

    ArmorItemCharacteristic(Function<ArmorItem, ? extends Number> func, Class<? extends Number> resClass){
        this.func = func;
        this.resClass = resClass;
    }

    public Function<ArmorItem, ? extends Number> getFunc() {
        return func;
    }

    public Class<? extends Number> getResClass() {
        return resClass;
    }

    public TextField getFromTextField() {
        return fromTextField;
    }

    public void setFromTextField(TextField fromTextField) {
        this.fromTextField = fromTextField;
    }

    public TextField getToTextField() {
        return toTextField;
    }

    public void setToTextField(TextField toTextField) {
        this.toTextField = toTextField;
    }

    public TextField getWeightTextField() {
        return weightTextField;
    }

    public void setWeightTextField(TextField weightTextField) {
        this.weightTextField = weightTextField;
    }

    public Label getSelectedLabel() {
        return selectedLabel;
    }

    public void setSelectedLabel(Label selectedLabel) {
        this.selectedLabel = selectedLabel;
    }
}
