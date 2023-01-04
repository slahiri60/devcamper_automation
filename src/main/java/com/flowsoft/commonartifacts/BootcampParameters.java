package com.flowsoft.commonartifacts;

import lombok.Data;

@Data
public class BootcampParameters {

    private String baseURI;
    private String expectedValue;
    private String responseValue;
    private String comparisonParameter;
    private String bootcampId;
    private int bootcampCount;
    private String jsonElement;
}
