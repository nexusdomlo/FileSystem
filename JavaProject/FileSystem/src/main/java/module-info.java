module com.example.filesystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.filesystem to javafx.fxml;
    exports com.example.filesystem;
}