package ru.tele2.andrey.zookeeper.model.uniform;

import lombok.Data;

@Data
public class UniformResponse<T> {
    private T result;
    private Trace trace;
}
