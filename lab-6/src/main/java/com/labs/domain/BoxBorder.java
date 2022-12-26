package com.labs.domain;

import javafx.scene.control.Button;
import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxBorder {
    private String id;
    private Button button;
    private boolean isSelected;
    private String selectedBy;

    public BoxBorder(Button button) {
        this.id = button.getId();
        this.button = button;
        this.isSelected = false;
        this.selectedBy = "";
    }

    public void selectBorder(GamePlayer selectedBy) {
        this.isSelected = true;
        this.selectedBy = selectedBy.getType().toString();
        this.button.setStyle("-fx-background-color: #000000");
        this.button.setDisable(true);
    }
}
