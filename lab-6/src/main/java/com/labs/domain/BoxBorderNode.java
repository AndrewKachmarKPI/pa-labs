package com.labs.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BoxBorderNode {
    private int minMaxUtility;
    private BoxBorder boxBorder;
}
