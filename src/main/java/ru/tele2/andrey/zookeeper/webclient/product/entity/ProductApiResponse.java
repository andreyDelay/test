package ru.tele2.andrey.zookeeper.webclient.product.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.tele2.andrey.zookeeper.model.uniform.UniformResponse;

@Data
public class ProductApiResponse extends UniformResponse<Product> {
    @Getter
    @AllArgsConstructor
    public enum ValidLocalCodes {
        BP_OK("bp_ok");
        private final String codeValue;
    }
}
