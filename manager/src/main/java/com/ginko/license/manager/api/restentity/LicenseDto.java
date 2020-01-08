package com.ginko.license.manager.api.restentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author ginko
 * @date 8/3/19
 */
@ApiModel(description = "证书实体对象")
public class LicenseDto {

    @ApiModelProperty(value = "使用该证书的实体。该参数必须指定", example = "CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US")
    @NotBlank
    private String holder;

    @ApiModelProperty(value = "发行证书的实体", example = "CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US")
    private String issuer;

    @ApiModelProperty(value = "License过期时间，该参数必须指定", example = "2020-7-7")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date notAfter;

    @ApiModelProperty(value = "License有效的开始时间", example = "2020-2-2")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date notBefore;

    @ApiModelProperty(value = "需要被许可的实体，可以是一款产品的名字或者软件的代号，该参数必须指定", example = "license-kit")
    @NotBlank
    private String subject;

    @Valid
    private List<ContentWithValue> contents;

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<ContentWithValue> getContents() {
        return contents;
    }

    public void setContents(List<ContentWithValue> contents) {
        this.contents = contents;
    }
}
