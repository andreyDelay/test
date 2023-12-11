package ru.tele2.andrey.zookeeper.model.uniform;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VoidResponse implements Response {
    @JsonIgnore
    private Status status;
}
