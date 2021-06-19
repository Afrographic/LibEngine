module com.mycompany.libengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires com.jfoenix;

    opens com.mycompany.libengine to javafx.fxml;
    exports com.mycompany.libengine;

}
