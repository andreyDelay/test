package ru.tele2.andrey.zookeeper.model.uniform;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Trace {
    @JsonProperty("req_id")
    private String reqId;
    @JsonProperty("global_code")
    private String globalCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("local_code")
    private String localCode;
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
}
