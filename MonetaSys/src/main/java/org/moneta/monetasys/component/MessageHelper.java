package org.moneta.monetasys.component;

import javafx.scene.control.Label;

public class MessageHelper {

    public static void showSuccess(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #22c55e; -fx-font-weight: bold;");
    }

    public static void showError(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
    }
}