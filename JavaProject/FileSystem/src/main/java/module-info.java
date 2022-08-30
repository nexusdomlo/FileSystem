module com.example.filesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;


    opens com.example.filesystem to javafx.fxml;
    exports com.example.filesystem;
}