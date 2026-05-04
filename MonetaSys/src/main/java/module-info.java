module org.moneta.monetasys {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.moneta.monetasys to javafx.fxml;
    opens org.moneta.monetasys.ui to javafx.fxml;

    exports org.moneta.monetasys;
    exports org.moneta.monetasys.backend;
    exports org.moneta.monetasys.ui;
    exports org.moneta.monetasys.component;
}