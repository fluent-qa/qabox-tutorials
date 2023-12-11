package io.fluent.dailycases.collections;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemoClass {
    private String name;
    private String api;
    private String path;
    private String requestBody;
}
