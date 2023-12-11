package ru.tele2.andrey.zookeeper.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.tele2.andrey.zookeeper.model.uniform.Status;

@Getter
@AllArgsConstructor
public enum CommonResponseStatus implements Status {
    BP_EXTERNAL_SERVICE_INVOCATION_ERROR("bp_external_service_invocation_error", "Ошибка при отправке запроса во внешний севис",HttpStatus.OK),
    BP_EXTERNAL_SERVICE_LOCAL_CODE_ERROR("bp_external_service_local_code_error", "Неподходящий бизнес код от внешнего сервиса", HttpStatus.OK);

    private String code;
    private String message;
    private HttpStatus httpStatus;
}
