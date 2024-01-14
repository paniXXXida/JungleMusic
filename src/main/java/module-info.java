module com.example.jungle_music {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.jungle_music.model to javafx.base;
    exports com.example.jungle_music;
    opens com.example.jungle_music.controller to javafx.fxml;
}