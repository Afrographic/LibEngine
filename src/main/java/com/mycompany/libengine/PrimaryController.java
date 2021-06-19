package com.mycompany.libengine;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");

    }
}
