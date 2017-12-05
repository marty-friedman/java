package com.egrasoft.ds3calc;

import com.egrasoft.ds3calc.model.ArmorCollection;
import com.egrasoft.ds3calc.model.ArmorItem;
import com.egrasoft.ds3calc.model.ArmorItemCharacteristic;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    private TreeMap<String, TextField> toTextFields;
    @FXML
    private TreeMap<String, TextField> fromTextFields;
    @FXML
    private TreeMap<String, TextField> weightTextFields;
    @FXML
    private TreeMap<String, ListView<ArmorItem>> listViews;
    @FXML
    private TreeMap<String, Label> labels;
    @FXML
    private TreeMap<String, Tab> tabs;
    @FXML
    private TabPane tabPane;

    private Property<Optional<ArmorItem>> selected = new SimpleObjectProperty<>(Optional.empty());
    private Map<ArmorSlot, Map<ArmorItemCharacteristic, String[]>> userInput;
    private Map<ArmorSlot, Property<Predicate<ArmorItem>>> filters;
    private Map<ArmorSlot, Property<Comparator<ArmorItem>>> comparators;

    @FXML
    private void initialize(){

        //System.out.println(tabmap.get(HELM));

        // obtain items data
        Map<ArmorSlot, ArmorCollection> collections = new TreeMap<>();
        Map<ArmorSlot, File> files = new TreeMap<>();
        files.put(HELM, new File("./cache/helms.dat"));
        files.put(ARMOR, new File("./cache/armors.dat"));
        files.put(GAUNTLETS, new File("./cache/gauntlets.dat"));
        files.put(LEGS, new File("./cache/legs.dat"));
        try{
            readLocalData(files, collections);
        } catch (IOException | ClassNotFoundException e){
            Map<ArmorSlot, String> links = new TreeMap<>();
            links.put(HELM, "http://ru.darksouls.wikia.com/wiki/Категория:Шлемы_(Dark_Souls_III)");
            links.put(ARMOR, "http://ru.darksouls.wikia.com/wiki/Категория:Доспехи_(Dark_Souls_III)");
            links.put(GAUNTLETS, "http://ru.darksouls.wikia.com/wiki/Категория:Перчатки_(Dark_Souls_III)");
            links.put(LEGS, "http://ru.darksouls.wikia.com/wiki/Категория:Сапоги_(Dark_Souls_III)");
            obtainAndStoreData(files, collections, links);
        }

        // link gui components with enumerations
        for (String key : fromTextFields.keySet())
            ArmorItemCharacteristic.valueOf(key.toUpperCase()).setFromTextField(fromTextFields.get(key));
        for (String key : toTextFields.keySet())
            ArmorItemCharacteristic.valueOf(key.toUpperCase()).setToTextField(toTextFields.get(key));
        for (String key : weightTextFields.keySet())
            ArmorItemCharacteristic.valueOf(key.toUpperCase()).setWeightTextField(weightTextFields.get(key));
        for (String key : labels.keySet())
            ArmorItemCharacteristic.valueOf(key.toUpperCase()).setSelectedLabel(labels.get(key));
        for (String key : listViews.keySet())
            ArmorSlot.valueOf(key.toUpperCase()).setList(listViews.get(key));
        for (String key : tabs.keySet())
            tabs.get(key).setUserData(ArmorSlot.valueOf(key.toUpperCase()));

        // set user input map
        userInput = new TreeMap<>();
        for (ArmorSlot as : ArmorSlot.values()){
            Map<ArmorItemCharacteristic, String[]> slotMap = new TreeMap<>();
            for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values()){
                String[] input = new String[]{
                        aic.getFromTextField().getText(),
                        aic.getToTextField().getText(),
                };
                slotMap.put(aic, input);
            }
            userInput.put(as, slotMap);
        }

        // link tabPane with userInput and text fields
        tabPane.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            ArmorSlot oldSlot = (ArmorSlot) oldValue.getUserData();
            Map<ArmorItemCharacteristic, String[]> oldInputPage = userInput.get(oldSlot);
            for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values()){
                oldInputPage.get(aic)[0] = aic.getFromTextField().getText();
                oldInputPage.get(aic)[1] = aic.getToTextField().getText();
            }
            ArmorSlot newSlot = (ArmorSlot) newValue.getUserData();
            Map<ArmorItemCharacteristic, String[]> newInputPage = userInput.get(newSlot);
            for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values()){
                aic.getFromTextField().setText(newInputPage.get(aic)[0]);
                aic.getToTextField().setText(newInputPage.get(aic)[1]);
            }
        }));

        // set start filters and comparators
        filters = new TreeMap<>();
        comparators = new TreeMap<>();
        for (ArmorSlot as : ArmorSlot.values()) {
            filters.put(as, new SimpleObjectProperty<>(item -> true));
            comparators.put(as, new SimpleObjectProperty<>((item1, item2)->0));
        }

        // set ListViews' cell factories, items, selection policy and bind selected property
        for (ArmorSlot as : ArmorSlot.values()){
            as.getList().setCellFactory(param -> new ListCell<>(){
                @Override
                protected void updateItem(ArmorItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty)
                        setText(item.getName());
                }
            });

            FilteredList<ArmorItem> filteredList = new FilteredList<>(FXCollections.observableArrayList(collections.get(as)));
            filteredList.predicateProperty().bind(filters.get(as));
            SortedList<ArmorItem> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(comparators.get(as));

            as.getList().setItems(sortedList); //todo
            as.getList().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            as.getList().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null)
                    for (ArmorSlot ass : ArmorSlot.values())
                        if (!as.getList().equals(ass.getList()))
                            ass.getList().getSelectionModel().clearSelection();
                selected.setValue(Optional.ofNullable(newValue));
            });
        }

        // bind labels' text properties to selected item characteristics
        for (ArmorItemCharacteristic ac : ArmorItemCharacteristic.values()){
            StringBinding sb = Bindings.createStringBinding(() -> selected.getValue().map(ac.getFunc()).map(String::valueOf).orElse("---"), selected);
            ac.getSelectedLabel().textProperty().bind(sb);
        }

    }

    @FXML
    private void clearSelection(){
        for (ArmorSlot as : ArmorSlot.values())
            as.getList().getSelectionModel().clearSelection();
    }

    @FXML
    private void renewFiltersAndComparators(){
        ArmorSlot oldSlot = (ArmorSlot) tabPane.getSelectionModel().getSelectedItem().getUserData();
        Map<ArmorItemCharacteristic, String[]> oldInputPage = userInput.get(oldSlot);
        for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values()){
            oldInputPage.get(aic)[0] = aic.getFromTextField().getText();
            oldInputPage.get(aic)[1] = aic.getToTextField().getText();
        }

        // update filters (throws NumberFormatException)
        for (ArmorSlot as : ArmorSlot.values()){
            Predicate<ArmorItem> p = item -> true;
            Map<ArmorItemCharacteristic, String[]> slotInput = userInput.get(as);
            for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values()){
                String[] chInput = slotInput.get(aic);
                if (!"NA".equals(chInput[0]))
                    p = p.and(item -> aic.getFunc().apply(item).doubleValue()>=Double.parseDouble(chInput[0]));
                if (!"NA".equals(chInput[1]))
                    p = p.and(item -> aic.getFunc().apply(item).doubleValue()<=Double.parseDouble(chInput[1]));
            }
            filters.get(as).setValue(p);
        }

        // update comparators (throws NumberFormatException)
        for (ArmorSlot as : ArmorSlot.values()){
            Map<ArmorItemCharacteristic, Double> divisors = new TreeMap<>();
            Map<ArmorItemCharacteristic, Double> weights = new TreeMap<>();

            List<ArmorItem> leftover = as.getList().getItems();
            if (leftover.isEmpty())
                continue;
            for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values()){
                weights.put(aic, Double.parseDouble(aic.getWeightTextField().getText()));
                if (aic != ArmorItemCharacteristic.WEIGHT) {
                    Function<ArmorItem, Double> comp = aic.getFunc().andThen(Number::doubleValue).andThen(Math::abs);
                    double best = comp.apply(Collections.max(leftover, Comparator.comparing(comp)));
                    best = (best == 0) ? 1 : best;
                    divisors.put(aic, best);
                }
            }
            Function<ArmorItem, Double> func = item -> {
                double res = 0;
                for (ArmorItemCharacteristic aic : ArmorItemCharacteristic.values())
                    if (aic != ArmorItemCharacteristic.WEIGHT)
                        res += aic.getFunc().apply(item).doubleValue() / divisors.get(aic) * weights.get(aic);
                if (weights.get(ArmorItemCharacteristic.WEIGHT) != 0)
                    res /= ArmorItemCharacteristic.WEIGHT.getFunc().apply(item).doubleValue()*weights.get(ArmorItemCharacteristic.WEIGHT);
                return res;
            };
            comparators.get(as).setValue(Comparator.comparing(func).reversed());
        }
    }

    @FXML
    private void openWebPage(){
        if (!Desktop.isDesktopSupported() || !selected.getValue().isPresent())
            return;
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE))
            return;
        new Thread(()->{
            try {
                desktop.browse(new URI(selected.getValue().get().getUrl()));
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
            showErrorDialog("Ошибка чтения/записи");
            Platform.exit();
        }
    }

    private void showErrorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.setTitle("Ошибка!");
        alert.showAndWait();
    }

}
