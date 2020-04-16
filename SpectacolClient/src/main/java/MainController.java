import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController implements IObserver{
    ObservableList<Spectacol> artists=FXCollections.observableArrayList();

    private Angajat loggedInClient;
    private IService server;
    private ObservableList<Spectacol> model=FXCollections.observableArrayList();
    private Stage dialogStage;

    @FXML
    TableView showsTable;
    @FXML
    TableView artistTable;

    @FXML
    TableColumn aArtistColumn;
    @FXML
    TableColumn aLocationColumn;
    @FXML
    TableColumn aAvaibleColumn;
    @FXML
    TableColumn aHourColumn;

    @FXML
    TableColumn sArtistColumn;
    @FXML
    TableColumn sLocationColumn;
    @FXML
    TableColumn sDateColumn;
    @FXML
    TableColumn sSoldColumn;
    @FXML
    TableColumn sAvaibleColumn;
    @FXML
    TableColumn sIdColumn;

    @FXML
    TextField wantedSeatsField;
    @FXML
    TextField buyerNameField;

    @FXML
    DatePicker calendar;

    public void setService(IService server,Stage stage){
        this.dialogStage=stage;
        this.server=server;
        initModel();
        calendar.setValue(LocalDate.now());
    }

    private void initModel(){
        Runnable runnable = () -> {
            try{
                Iterable<Spectacol> showuri= Arrays.asList(server.findAllShows());

                List<Spectacol> showList= StreamSupport
                        .stream(showuri.spliterator(),false)
                        .collect(Collectors.toList());
                System.out.println(showList);
                model.setAll(showList);
                initialize();
            }catch (ServiceException e){
                e.printStackTrace();
            }
        };
        Platform.runLater(runnable);
    }

    public void setLoggedInClient(Angajat loggedInClient){
        this.loggedInClient=loggedInClient;
    }
    @FXML
    public void initialize() {
        sIdColumn.setCellValueFactory(new PropertyValueFactory<Spectacol,Integer>("id"));
        sArtistColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, String>("artistName"));
        sLocationColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, String>("locatie"));
        sDateColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, LocalDateTime>("dataTimp"));
        sSoldColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, Integer>("nr_loc_vandute"));
        sAvaibleColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, Integer>("nr_loc_disponibile"));
        showsTable.setItems(model);
        showsTable.setRowFactory(tv -> new TableRow<Spectacol>() {
            @Override
            protected void updateItem(Spectacol item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.getNr_loc_disponibile()== 0)
                    setStyle("-fx-background-color: red;");
                else
                    setStyle("");
            }
        });
    }

    public void searchAction(){
        Spectacol[] shows= null;
        try{
            shows= server.findAllShows();
            List<Spectacol> rez=new ArrayList<>();
            LocalDate data=calendar.getValue();
            for(Spectacol s:shows){
                if(s.getDataTimp().toLocalDate().isEqual(data)){
                    rez.add(s);
                }
            }
            artists.setAll(rez);
            populateArtistTable();
        }catch(ServiceException e){
            e.printStackTrace();
        }
    }

    private void populateArtistTable(){
        aArtistColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, String>("artistName"));
        aLocationColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, String>("locatie"));
        aAvaibleColumn.setCellValueFactory(new PropertyValueFactory<Spectacol, Integer>("nr_loc_disponibile"));
        aHourColumn.setCellValueFactory(new PropertyValueFactory<Spectacol,String>("dataTimp"));
        artistTable.setItems(artists);
    }

    public void logOutAction(ActionEvent actionEvent){
        Runnable runnable = () -> {
          try{
              FXMLLoader loader= new FXMLLoader();
              loader.setLocation(getClass().getResource("/views/loginView.fxml"));

              AnchorPane root=(AnchorPane) loader.load();
              Stage dialogStage= new Stage();
              dialogStage.setTitle("Log In");
              dialogStage.initModality(Modality.WINDOW_MODAL);
              Scene scene= new Scene(root);
              dialogStage.setScene(scene);

              LoginController loginFormController= loader.getController();
              loginFormController.setService(server,dialogStage);

              this.server.logout(loggedInClient,this);
              this.dialogStage.close();
              dialogStage.show();

          }catch (IOException e){
              e.printStackTrace();
          }catch (ServiceException e){
              e.printStackTrace();
          }
        };
        Platform.runLater(runnable);
    }
    public void sellAction(){
        if (showsTable.getSelectionModel().isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "ATENTIE", "Nu ati ales niciun spectacol!");
        } else {
            String item = showsTable.getSelectionModel().getSelectedItem().toString();
            String[] items = item.split(";");
            try {
                if (Integer.parseInt(wantedSeatsField.getText())>Integer.parseInt(items[4]))
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "ATENTIE", "Nu exista suficiente locuri!");
                else {
                    Spectacol sh=new Spectacol(Integer.parseInt(items[0]),LocalDateTime.now(),"location",0,0,"artist");
                    Bilet bilet= new Bilet(0,Integer.parseInt(wantedSeatsField.getText()),buyerNameField.getText(),Integer.parseInt(items[0]));
                    server.ticketsSold(sh,bilet);
                    wantedSeatsField.clear();
                    buyerNameField.clear();
                }
            } catch (NumberFormatException | ServiceException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "ATENTIE", "Parsare esuata!");

            }
        }
        showsTable.getSelectionModel().clearSelection();
    }

    @Override
    public void notifyTicketsSold(Spectacol show) throws ServiceException{
        Runnable runnable= () -> {
            for(Spectacol m: this.model){
                if(m.getId().equals(show.getId())){
                    System.out.println(m.getId().toString()+" "+show.getId());
                    this.model.remove(m);
                    this.model.add(show);
                    break;
                }
            }
            showsTable.setItems(model);
            System.out.println(" FINISHED TO REFRESH TABLE");
        };
        Platform.runLater(runnable);
    }
}
