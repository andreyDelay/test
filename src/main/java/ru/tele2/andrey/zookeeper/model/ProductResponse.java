package ru.tele2.andrey.zookeeper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tele2.andrey.zookeeper.model.uniform.Response;
import ru.tele2.andrey.zookeeper.model.uniform.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Response {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("productName")
    private String productName;
    @JsonIgnore
    private Status status;
}
