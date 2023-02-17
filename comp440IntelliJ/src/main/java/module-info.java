module com.example.comp440intellij {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.comp440intellij to javafx.fxml;
    exports com.example.comp440intellij;
}