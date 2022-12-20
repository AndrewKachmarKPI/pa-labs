package com.labs.domain;

import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreeNodeEntry {
    private Integer key;
    private String value;

    public String getEntryForSave() {
        return this.key + ";" + value;
    }
}
