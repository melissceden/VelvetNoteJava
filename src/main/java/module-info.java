module com.example.velvet_note {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;
    requires org.apache.commons.codec;

    opens com.example.velvet_note to javafx.graphics, javafx.fxml;
    opens com.example.velvet_note.controladores to javafx.fxml;
    opens com.example.velvet_note.modelo to javafx.base;

}
