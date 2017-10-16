import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;
import javafx.scene.control.Dialog;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import javafx.stage.Modality;

public class NfaInput extends Application{
   @Override 
   public void start(Stage stage){
      addUI(stage);
   }
   private void addUI(Stage stage){
      TableView<NfaEntry> table = new TableView<NfaEntry>();
      Scene scene = new Scene(new Group());
      stage.setTitle("Nfa");
      VBox root = new VBox(5);
      final HBox hb = new HBox();
      
      table.setEditable(true);
      
      TextField source =  new TextField();
      source.setPromptText("Source");
      TextField weight =  new TextField();
      weight.setPromptText("Weight");
      TextField destination =  new TextField();
      destination.setPromptText("Destination");
      
      final ContextMenu sourceValidator = new ContextMenu();
      final ContextMenu destValidator = new ContextMenu();
      final ContextMenu weightValidator = new ContextMenu();
      final ContextMenu calcValidator = new ContextMenu();
      
      TableColumn sourceCol = new TableColumn("Source");
      sourceCol.setCellValueFactory(new PropertyValueFactory("sourceName"));
      sourceCol.setCellFactory(TextFieldTableCell.forTableColumn());
      sourceCol.setOnEditCommit(
         new EventHandler<CellEditEvent<NfaEntry, String>>(){
            @Override
            public void handle(CellEditEvent<NfaEntry,String> t){
               ((NfaEntry)t.getTableView().getItems().get(
                  t.getTablePosition().getRow())
                  ).setSourceName(t.getNewValue());
            }
         }
      );
      
      TableColumn weightCol = new TableColumn("Weight");
      weightCol.setCellValueFactory(new PropertyValueFactory("weightName"));
      weightCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
      weightCol.setOnEditCommit(
         new EventHandler<CellEditEvent<NfaEntry, Integer>> (){
            @Override
            public void handle(CellEditEvent<NfaEntry,Integer> t){
               ((NfaEntry)t.getTableView().getItems().get(
                  t.getTablePosition().getRow())
                  ).setSourceName(t.getNewValue().toString());
            }
         }    
      );
      
      TableColumn destCol = new TableColumn("Destination");
      destCol.setCellValueFactory(new PropertyValueFactory("destName"));
      destCol.setCellFactory(TextFieldTableCell.forTableColumn());
      destCol.setOnEditCommit(
         new EventHandler<CellEditEvent<NfaEntry,String>>(){
            @Override
            public void handle(CellEditEvent<NfaEntry,String> t){
               ((NfaEntry)t.getTableView().getItems().get( 
                  t.getTablePosition().getRow())
                  ).setDestName(t.getNewValue());
            }
         }
      );
      final ObservableList<NfaEntry> data = FXCollections.observableArrayList();
            
           
      final Button addBtn = new Button("add");
      addBtn.defaultButtonProperty().bind(addBtn.focusedProperty());
      addBtn.setOnAction(new EventHandler<ActionEvent>(){
         @Override 
         public void handle(ActionEvent e){
            if(source.getText().equals("")){
               sourceValidator.getItems().clear();
               sourceValidator.getItems().add(
                  new MenuItem("Please enter source"));
               sourceValidator.show(source,Side.BOTTOM, 0, 0);
            }
            if(destination.getText().equals("")){
               destValidator.getItems().clear();
               destValidator.getItems().add(
                  new MenuItem("Please enter a destination"));
               destValidator.show(destination,Side.BOTTOM, 0 , 0);
            }
            if(weight.getText().equals("")||(!weight.getText().matches("1"))&&(!weight.getText().matches("0"))){
               weightValidator.getItems().clear();
               weightValidator.getItems().add(
                  new MenuItem("Please enter a weight of 0 or 1"));
               weightValidator.show(weight, Side.BOTTOM, 0 , 0 );
            }
            else {
               data.add(new NfaEntry(source.getText(),Integer.parseInt(weight.getText()),destination.getText()));
               source.clear();
               weight.clear();
               destination.clear();
           }
         }
      });
      final ObservableList<NfaEntry> popdata = FXCollections.observableArrayList();
      final Button calculate = new Button("calculate");
      calculate.defaultButtonProperty().bind(calculate.focusedProperty());
      calculate.setOnAction(new EventHandler<ActionEvent>(){
         @Override
         public void handle (ActionEvent e) {
            nfa nfacalculate = new nfa();
            for(NfaEntry d :data){
              nfacalculate.addLink(d.getSourceName(), d.getDestName(), d.getWeightName());
            }
            if(nfacalculate.canConvert()){
               nfacalculate.transform();
               TableView<NfaEntry> poptable = new TableView<NfaEntry>();
               poptable.setEditable(false);            
               Stage dialogStage = new Stage();
               dialogStage.setTitle("DFA");
               dialogStage.initModality(Modality.WINDOW_MODAL);
               dialogStage.initOwner(stage);
               
               Map<String,ArrayList<Connection>> popmap = new LinkedHashMap<String,ArrayList<Connection>>(nfacalculate.getDfa());
               TableColumn sourceDfa = new TableColumn("Source");
               sourceDfa.setCellValueFactory(new PropertyValueFactory("sourceName"));
               
               TableColumn weightDfa = new TableColumn("Weight");
               weightDfa.setCellValueFactory(new PropertyValueFactory("weightName"));
               
               TableColumn destDfa = new TableColumn("Destination");
               destDfa.setCellValueFactory(new PropertyValueFactory("destName"));
               
               for(String key : popmap.keySet()){
                  ArrayList<Connection> holdpop = new ArrayList<Connection>(popmap.get(key));
                  for(Connection popcon : holdpop){
                     popdata.add(new NfaEntry(key, popcon.weight, popcon.dest.toString()));
                  }  
               }
               
               poptable.getColumns().setAll(sourceDfa, weightDfa, destDfa);
               poptable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
               poptable.setItems(popdata);
               dialogStage.setScene(new Scene(poptable,700,500));
               dialogStage.showAndWait();
               table.getItems().clear();
            }
            else{
               calcValidator.getItems().add(
                  new MenuItem("Not in Nfa form"));
               calcValidator.show(calculate,Side.BOTTOM, 0, 0);            
            }

         }
        });
      //format the alert button better
      final Button clear = new Button("clear");
      clear.defaultButtonProperty().bind(clear.focusedProperty());
      clear.setOnAction(new EventHandler<ActionEvent>(){
         @Override
         public void handle(ActionEvent e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to clear the table?", ButtonType.YES, ButtonType.NO);
           
            alert.showAndWait();
                        
            if(alert.getResult() == ButtonType.YES){
               table.getItems().clear();
            }
         }
      });
      
      table.setItems(data);
      
      table.getColumns().addAll(sourceCol, weightCol, destCol);
      table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      hb.getChildren().addAll(source,weight,destination,addBtn,calculate,clear);
      hb.setSpacing(25);
      
      root.setSpacing(5);
      root.setPadding( new Insets(10, 0, 0, 10));
      root.getChildren().addAll(table,hb);           
      
      stage.setScene(new Scene(root,800,500)); 
      
           
      stage.show();
   }

   public static void main(String args[]){
      launch(args);
   }
}