module com.mycompany.libengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires com.jfoenix;
    requires hyph;
    requires io;
    requires kernel;
    requires pdfa;
    requires layout;
    requires forms;
    requires barcodes;
    requires opencsv;

    opens com.mycompany.libengine to javafx.fxml;
    exports com.mycompany.libengine;

}
