module com.example.demofs {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demofs to javafx.fxml;
    exports com.example.demofs;
}