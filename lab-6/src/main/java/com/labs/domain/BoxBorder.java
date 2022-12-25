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

    public BoxBorder(BoxBorder border) {
        this.id = border.id;
        this.button = border.button;
        this.isSelected = border.isSelected;
        this.selectedBy = border.selectedBy;
    }

    public boolean isNotSelected() {
        return !this.isSelected;
    }

    public void selectBorder(String selectedBy) {
        this.isSelected = true;
        this.selectedBy = selectedBy;
    }
}
