package com.egrasoft.ds3calc.model;

import com.egrasoft.ds3calc.Main;

import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.function.Function;

public enum ArmorItemStat implements Serializable{
    PHYS_RES(ArmorItem::getPhysRes, Double.class, "img/physRes.png"), STRIKE_DEF(ArmorItem::getStrikeDef, Double.class, "img/strikeDef.png"),
    SLASH_DEF(ArmorItem::getSlashDef, Double.class, "img/slashDef.png"), THRUST_DEF(ArmorItem::getThrustDef, Double.class, "img/thrustDef.png"),
    MAGIC_DEF(ArmorItem::getMagicDef, Double.class, "img/magicDef.png"), FIRE_DEF(ArmorItem::getFireDef, Double.class, "img/fireDef.png"),
    LIGHTNING_DEF(ArmorItem::getLightningDef, Double.class, "img/lightningDef.png"), DARK_DEF(ArmorItem::getDarkDef, Double.class, "img/darkDef.png"),
    BLEED_RES(ArmorItem::getBleedRes, Integer.class, "img/bleedRes.png"), POISON_RES(ArmorItem::getPoisonRes, Integer.class, "img/poisonRes.png"),
    FROST_RES(ArmorItem::getFrostRes, Integer.class, "img/frostRes.png"), CURSE_RES(ArmorItem::getCurseRes, Integer.class, "img/curseRes.png"),
    POISE(ArmorItem::getPoise, Double.class, "img/poise.png"), DURABILITY(ArmorItem::getDurability, Integer.class, "img/durability.png"),
    WEIGHT(ArmorItem::getWeight, Double.class, "img/weight.png");

    private Function<ArmorItem, ? extends Number> func;
    private Class<? extends Number> resClass;
    private String name;
    private String imgUrl;

    ArmorItemStat(Function<ArmorItem, ? extends Number> func, Class<? extends Number> resClass, String imgUrl){
        this.func = func;
        this.resClass = resClass;
        this.imgUrl = imgUrl;
        this.name = Main.strings.getString("characteristicName_"+toString());
    }

    public Function<ArmorItem, ? extends Number> getFunc() {
        return func;
    }

    public Class<? extends Number> getResClass() {
        return resClass;
    }

    public String getName() { return name; }

    public String getImgUrl() { return imgUrl; }


}
