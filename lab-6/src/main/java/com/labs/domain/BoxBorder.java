package com.labs.domain;

import javafx.scene.control.Button;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxBorder {
    private Button button;
    private boolean isSelected;
    private String selectedBy;

    public BoxBorder(Button button) {
        this.button = button;
        this.isSelected = false;
        this.selectedBy = "";
    }
}
