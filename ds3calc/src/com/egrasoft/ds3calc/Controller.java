package com.egrasoft.ds3calc;

import com.egrasoft.ds3calc.custom.SelectedItemStatsPane;
import com.egrasoft.ds3calc.custom.StatSettingsPane;
import com.egrasoft.ds3calc.model.ArmorCollection;
import com.egrasoft.ds3calc.model.ArmorItem;
import com.egrasoft.ds3calc.model.ArmorItemStat;
import com.egrasoft.ds3calc.model.ArmorSlot;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.egrasoft.ds3calc.model.ArmorSlot.*;

public class Controller {

    @FXML
    private FlowPane statsSettingsPane;
    @FXML
    private FlowPane selectedItemStatsPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private FlowPane listViewsPane;

    private EnumMap<ArmorItemStat, TextField[]> statsSettingsTextFields = new EnumMap<>(ArmorItemStat.class);
    private EnumMap<ArmorItemStat, Label> selectedItemStatsLabels = new EnumMap<>(ArmorItemStat.class);
    private EnumMap<ArmorSlot, ListView<ArmorItem>> listViews = new EnumMap<>(ArmorSlot.class);
    private EnumMap<ArmorSlot, Property<Predicate<ArmorItem>>> filters = new EnumMap<>(ArmorSlot.class);
    private EnumMap<ArmorSlot, Property<Comparator<ArmorItem>>> comparators = new EnumMap<>(ArmorSlot.class);
    private EnumMap<ArmorSlot, EnumMap<ArmorItemStat, String[]>> userInput = new EnumMap<>(ArmorSlot.class);
    private Property<Optional<ArmorItem>> selectedItem = new SimpleObjectProperty<>(Optional.empty());

    @FXML
    private void initialize(){
        initStatsSettingsPane();
        initSelectedItemStatsPane();

        // init tabs
        for (ArmorSlot as : ArmorSlot.values()) {
            Tab t = new Tab(as.getTabName());
            t.setUserData(as);
            tabPane.getTabs().add(t);
        }

        // init listViews
        for (ArmorSlot as : ArmorSlot.values()){
            ListView<ArmorItem> lv = new ListView<>();
            lv.setPrefHeight(400);
            lv.setPrefWidth(249);
            listViewsPane.getChildren().add(lv);
            listViews.put(as, lv);
        }

        // set initial filters and comparators
        for (ArmorSlot as : ArmorSlot.values()) {
            filters.put(as, new SimpleObjectProperty<>(item -> true));
            comparators.put(as, new SimpleObjectProperty<>((item1, item2)->0));
        }

        // obtain items data
        Map<ArmorSlot, ArmorCollection> collections = new HashMap<>();
        Map<ArmorSlot, File> files = new HashMap<>(){{
            put(HELM, new File("./cache/helms.dat"));
            put(ARMOR, new File("./cache/armors.dat"));
            put(GAUNTLETS, new File("./cache/gauntlets.dat"));
            put(LEGS, new File("./cache/legs.dat"));
        }};
        try{
            readLocalData(files, collections);
        } catch (IOException | ClassNotFoundException e){
            Map<ArmorSlot, String> links = new HashMap<>(){{
                put(HELM, "http://ru.darksouls.wikia.com/wiki/Категория:Шлемы_(Dark_Souls_III)");
                put(ARMOR, "http://ru.darksouls.wikia.com/wiki/Категория:Доспехи_(Dark_Souls_III)");
                put(GAUNTLETS, "http://ru.darksouls.wikia.com/wiki/Категория:Перчатки_(Dark_Souls_III)");
                put(LEGS, "http://ru.darksouls.wikia.com/wiki/Категория:Сапоги_(Dark_Souls_III)");
            }};
            obtainAndStoreData(files, collections, links);
        }

        initUserInputData();
        initListViews(collections);

        // bind labels' text to selectedItem item stats
        for (ArmorItemStat ac : ArmorItemStat.values()){
            StringBinding sb = Bindings.createStringBinding(() -> selectedItem.getValue().map(ac.getFunc()).map(String::valueOf).orElse("---"), selectedItem);
            selectedItemStatsLabels.get(ac).textProperty().bind(sb);
        }

    }

    private void initStatsSettingsPane(){
        for (ArmorItemStat aic : ArmorItemStat.values()){
            StatSettingsPane pane = new StatSettingsPane(aic);
            statsSettingsTextFields.put(aic, pane.getTextFields());
            statsSettingsPane.getChildren().add(pane);
        }
        Button updButton = new Button(Main.strings.getString("update"));
        updButton.setOnAction((event -> updateListsButtonAction()));
        FlowPane pane = new FlowPane();
        pane.setStyle("-fx-padding: 0 5;");
        pane.getChildren().add(updButton);
        statsSettingsPane.getChildren().add(pane);
    }

    private void initSelectedItemStatsPane(){
        for (ArmorItemStat aic : ArmorItemStat.values()){
            SelectedItemStatsPane pane = new SelectedItemStatsPane(aic);
            selectedItemStatsLabels.put(aic, pane.getLabel());
            selectedItemStatsPane.getChildren().add(pane);
        }
    }

    private void initUserInputData(){
        for (ArmorSlot as : ArmorSlot.values()){
            EnumMap<ArmorItemStat, String[]> slotMap = new EnumMap<>(ArmorItemStat.class);
            for (ArmorItemStat aic : ArmorItemStat.values()){
                String[] input = new String[]{
                        statsSettingsTextFields.get(aic)[0].getText(),
                        statsSettingsTextFields.get(aic)[1].getText(),
                };
                slotMap.put(aic, input);
            }
            userInput.put(as, slotMap);
        }
        tabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            ArmorSlot oldSlot = (ArmorSlot) oldValue.getUserData();
            Map<ArmorItemStat, String[]> oldInputPage = userInput.get(oldSlot);
            for (ArmorItemStat aic : ArmorItemStat.values()){
                oldInputPage.get(aic)[0] = statsSettingsTextFields.get(aic)[0].getText();
                oldInputPage.get(aic)[1] = statsSettingsTextFields.get(aic)[1].getText();
            }
            ArmorSlot newSlot = (ArmorSlot) newValue.getUserData();
            Map<ArmorItemStat, String[]> newInputPage = userInput.get(newSlot);
            for (ArmorItemStat aic : ArmorItemStat.values()){
                statsSettingsTextFields.get(aic)[0].setText(newInputPage.get(aic)[0]);
                statsSettingsTextFields.get(aic)[1].setText(newInputPage.get(aic)[1]);
            }
        }));
    }

    private void initListViews(Map<ArmorSlot, ArmorCollection> collections){
        for (ArmorSlot as : ArmorSlot.values()){
            listViews.get(as).setCellFactory(param -> new ListCell<>(){
                @Override
                protected void updateItem(ArmorItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty)
                        setText(item.getName());
                    else
                        setText("");
                }
            });
            FilteredList<ArmorItem> filteredList = new FilteredList<>(FXCollections.observableArrayList(collections.get(as)));
            filteredList.predicateProperty().bind(filters.get(as));
            SortedList<ArmorItem> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(comparators.get(as));

            listViews.get(as).setItems(sortedList);
            listViews.get(as).getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listViews.get(as).getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null)
                    for (ArmorSlot ass : ArmorSlot.values())
                        if (!listViews.get(as).equals(listViews.get(ass)))
                            listViews.get(ass).getSelectionModel().clearSelection();
                selectedItem.setValue(Optional.ofNullable(newValue));
            });
        }
    }

    @FXML
    private void clearSelectionButtonAction(){
        for (ArmorSlot as : ArmorSlot.values())
            listViews.get(as).getSelectionModel().clearSelection();
    }

    @FXML
    private void updateListsButtonAction(){
        // update user input data
        ArmorSlot oldSlot = (ArmorSlot) tabPane.getSelectionModel().getSelectedItem().getUserData();
        Map<ArmorItemStat, String[]> oldInputPage = userInput.get(oldSlot);
        for (ArmorItemStat aic : ArmorItemStat.values()){
            oldInputPage.get(aic)[0] = statsSettingsTextFields.get(aic)[0].getText();
            oldInputPage.get(aic)[1] = statsSettingsTextFields.get(aic)[1].getText();
        }

        // update filters (throws NumberFormatException)
        for (ArmorSlot as : ArmorSlot.values()){
            Predicate<ArmorItem> p = item -> true;
            Map<ArmorItemStat, String[]> slotInput = userInput.get(as);
            for (ArmorItemStat aic : ArmorItemStat.values()){
                String[] chInput = slotInput.get(aic);
                if (!Main.strings.getString("NA").equals(chInput[0]))
                    p = p.and(item -> aic.getFunc().apply(item).doubleValue()>=Double.parseDouble(chInput[0]));
                if (!Main.strings.getString("NA").equals(chInput[1]))
                    p = p.and(item -> aic.getFunc().apply(item).doubleValue()<=Double.parseDouble(chInput[1]));
            }
            filters.get(as).setValue(p);
        }

        // update comparators (throws NumberFormatException)
        for (ArmorSlot as : ArmorSlot.values()){
            Map<ArmorItemStat, Double> divisors = new TreeMap<>();
            Map<ArmorItemStat, Double> weights = new TreeMap<>();

            List<ArmorItem> leftover = listViews.get(as).getItems();
            if (leftover.isEmpty())
                continue;
            for (ArmorItemStat aic : ArmorItemStat.values()){
                weights.put(aic, Double.parseDouble(statsSettingsTextFields.get(aic)[2].getText()));
                if (aic != ArmorItemStat.WEIGHT) {
                    Function<ArmorItem, Double> comp = aic.getFunc().andThen(Number::doubleValue).andThen(Math::abs);
                    double best = comp.apply(Collections.max(leftover, Comparator.comparing(comp)));
                    best = (best == 0) ? 1 : best;
                    divisors.put(aic, best);
                }
            }
            Function<ArmorItem, Double> func = item -> {
                double res = 0;
                for (ArmorItemStat aic : ArmorItemStat.values())
                    if (aic != ArmorItemStat.WEIGHT)
                        res += aic.getFunc().apply(item).doubleValue() / divisors.get(aic) * weights.get(aic);
                if (weights.get(ArmorItemStat.WEIGHT) != 0)
                    res /= ArmorItemStat.WEIGHT.getFunc().apply(item).doubleValue()*weights.get(ArmorItemStat.WEIGHT);
                return res;
            };
            comparators.get(as).setValue(Comparator.comparing(func).reversed());
        }
    }

    @FXML
    private void openWebPageButtonAction(){
        if (!Desktop.isDesktopSupported() || !selectedItem.getValue().isPresent())
            return;
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE))
            return;
        new Thread(()->{
            try {
                desktop.browse(new URI(selectedItem.getValue().get().getUrl()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void readLocalData(Map<ArmorSlot, File> files, Map<ArmorSlot, ArmorCollection> collections) throws IOException, ClassNotFoundException{
        ObjectInputStream ois;

        for(ArmorSlot as : ArmorSlot.values())
            if (!files.get(as).exists())
                throw new FileNotFoundException();

        for(ArmorSlot as : ArmorSlot.values()) {
            ois = new ObjectInputStream(new FileInputStream(files.get(as)));
            collections.put(as, (ArmorCollection) ois.readObject());
            ois.close();
        }
    }

    private void obtainAndStoreData(Map<ArmorSlot, File> files, Map<ArmorSlot, ArmorCollection> collections, Map<ArmorSlot, String> links) {
        try {
            ObjectOutputStream oos;

            for(ArmorSlot as : ArmorSlot.values()) {
                files.get(as).getParentFile().mkdirs();
                files.get(as).createNewFile();
            }

            for(ArmorSlot as : ArmorSlot.values())
                collections.put(as, ArmorCollection.collectionOf(links.get(as)));

            for(ArmorSlot as : ArmorSlot.values()){
                oos = new ObjectOutputStream(new FileOutputStream(files.get(as)));
                oos.writeObject(collections.get(as));
                oos.close();
            }
        } catch (IOException e1) {
            showErrorDialog(Main.strings.getString("ioexception"));
            Platform.exit();
        }
    }

    private void showErrorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.setTitle(Main.strings.getString("exception"));
        alert.showAndWait();
    }

}
