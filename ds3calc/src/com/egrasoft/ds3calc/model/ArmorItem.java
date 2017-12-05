package com.egrasoft.ds3calc.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static com.egrasoft.ds3calc.model.ArmorItemStat.*;

public class ArmorItem implements Serializable {
    private String name;
    private String url;
    private ArmorSlot type;

    private double physRes;
    private double strikeDef;
    private double slashDef;
    private double thrustDef;
    private double magicDef;
    private double fireDef;
    private double lightningDef;
    private double darkDef;

    private int bleedRes;
    private int poisonRes;
    private int frostRes;
    private int curseRes;

    private double poise;
    private int durability;
    private double weight;

    private ArmorItem(String name, String url, ArmorSlot type, Map<ArmorItemStat, Number> vals){
        this.name = name;
        this.type = type;
        this.url = url;
        physRes = vals.get(PHYS_RES).doubleValue();
        strikeDef = vals.get(STRIKE_DEF).doubleValue();
        slashDef = vals.get(SLASH_DEF).doubleValue();
        thrustDef = vals.get(THRUST_DEF).doubleValue();
        magicDef = vals.get(MAGIC_DEF).doubleValue();
        fireDef = vals.get(FIRE_DEF).doubleValue();
        lightningDef = vals.get(LIGHTNING_DEF).doubleValue();
        darkDef = vals.get(DARK_DEF).doubleValue();
        bleedRes = vals.get(BLEED_RES).intValue();
        poisonRes = vals.get(POISON_RES).intValue();
        frostRes = vals.get(FROST_RES).intValue();
        curseRes = vals.get(CURSE_RES).intValue();
        poise = vals.get(POISE).doubleValue();
        durability = vals.get(DURABILITY).intValue();
        weight = vals.get(WEIGHT).doubleValue();
    }

    static ArmorItem valueOf(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ArmorSlot type = ArmorSlot.of(doc.select("aside.portable-infobox div.pi-item.pi-data.pi-item-spacing.pi-border-color h3+div").get(1).selectFirst("a").html().trim());
        String name = doc.select("aside.portable-infobox h2").get(0).html();
        Elements trs = doc.select("section.pi-item.pi-group.pi-border-color table[style=\"width:100%; text-align:center;\"] td:not(:has(a)):matches(.+)");
        ArmorItemStat[] keys = {PHYS_RES, POISE, STRIKE_DEF, BLEED_RES, SLASH_DEF, POISON_RES, THRUST_DEF,
                FROST_RES, MAGIC_DEF, CURSE_RES, FIRE_DEF, LIGHTNING_DEF, DURABILITY, DARK_DEF, WEIGHT};
        Map<ArmorItemStat, Number> vals = new TreeMap<>();
        for(int i=0; i<keys.length; i++){
            Number num = (keys[i].getResClass().equals(Integer.class))? Integer.parseInt(trs.get(i).html()) : Double.parseDouble(trs.get(i).html());
            vals.put(keys[i], num);
        }
        return new ArmorItem(name, url, type, vals);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ArmorSlot getType() {
        return type;
    }

    public double getPhysRes() {
        return physRes;
    }

    public double getStrikeDef() {
        return strikeDef;
    }

    public double getSlashDef() {
        return slashDef;
    }

    public double getThrustDef() {
        return thrustDef;
    }

    public double getMagicDef() {
        return magicDef;
    }

    public double getFireDef() {
        return fireDef;
    }

    public double getLightningDef() {
        return lightningDef;
    }

    public double getDarkDef() {
        return darkDef;
    }

    public int getBleedRes() {
        return bleedRes;
    }

    public int getPoisonRes() {
        return poisonRes;
    }

    public int getFrostRes() {
        return frostRes;
    }

    public int getCurseRes() {
        return curseRes;
    }

    public double getPoise() {
        return poise;
    }

    public int getDurability() {
        return durability;
    }

    public double getWeight() {
        return weight;
    }

}
