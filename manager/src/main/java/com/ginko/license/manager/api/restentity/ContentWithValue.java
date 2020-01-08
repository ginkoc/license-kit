package com.ginko.license.manager.api.restentity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author ginko
 * @date 8/3/19
 */
@ApiModel(description = "证书实体对象")
public class ContentWithValue {

    @ApiModelProperty(value = "控制参数的键类型，该参数必须指定", example = "MAC")
    @NotBlank
    private String type;

    @ApiModelProperty(value = "控制参数的键所对应的值，该参数必须指定", example = "00:0c:29:65:37:49")
    @NotBlank
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
