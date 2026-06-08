module mario {
    requires javafx.controls;
    requires javafx.fxml;

    opens mario to javafx.fxml;
    exports mario;
}
