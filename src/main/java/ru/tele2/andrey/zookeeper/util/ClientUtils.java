package ru.tele2.andrey.zookeeper.util;

import ru.tele2.andrey.zookeeper.model.uniform.Trace;
import ru.tele2.andrey.zookeeper.model.uniform.UniformResponse;

import java.util.Optional;

public class ClientUtils {

    public static Trace getTrace(UniformResponse<?> response) {
        return Optional.ofNullable(response.getTrace())
                        .orElseThrow(
                                () -> new RuntimeException("Нет объекта Trace, невозможно провести анализ ответа."));
    }
}
