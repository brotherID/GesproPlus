package com.management.pro.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    private String type;
    private String value;
    private boolean temporary;
}
