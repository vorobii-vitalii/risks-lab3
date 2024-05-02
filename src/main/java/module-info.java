module com.example.riskmgmtlab3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.riskmgmtlab3 to javafx.fxml;
    exports com.example.riskmgmtlab3;
}