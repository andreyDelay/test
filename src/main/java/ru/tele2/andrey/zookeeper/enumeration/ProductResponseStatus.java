package ru.tele2.andrey.zookeeper.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.tele2.andrey.zookeeper.model.uniform.Status;

@Getter
@AllArgsConstructor
public enum ProductResponseStatus implements Status {
    BP_OK("bp_ok", "OK",HttpStatus.OK),
    BP_ERROR_PARAMS("bp_error_params", "Некорректные входные параметры", HttpStatus.OK),
    BP_DATA_NOT_FOUND("bp_data_not_found", "Данные не найдены", HttpStatus.OK),
    BP_ERROR_DATABASE("bp_error_database", null, HttpStatus.OK),
    BP_BAD_EXTERNAL_API_LOCAL_CODE("bp_bad_external_api_local_code", "Неподходящий ответ от сервиса", HttpStatus.OK),
    BP_BAD_API_RESPONSE("bp_bad_api_response", "Local code not OK", HttpStatus.OK);

    private String code;
    private String message;
    private HttpStatus httpStatus;
}
