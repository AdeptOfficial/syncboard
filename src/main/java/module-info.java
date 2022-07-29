module com.hacknight.syncboard {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hacknight.syncboard to javafx.fxml;
    exports com.hacknight.syncboard;
}