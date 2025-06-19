module ma.enset.exam2test {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.kordamp.bootstrapfx.core;

    // Ouvrir les packages pour JavaFX FXML
    opens ma.enset.exam2test to javafx.fxml;
    opens ma.enset.exam2test.Controllers to javafx.fxml;
    opens ma.enset.exam2test.entities to javafx.base;

    // Exporter les packages
    exports ma.enset.exam2test;
    exports ma.enset.exam2test.Controllers;
    exports ma.enset.exam2test.entities;
    exports ma.enset.exam2test.Services;
}