package com.ginko.license.manager.api.controller;

import com.ginko.license.common.custom.LicenseContentType;
import com.ginko.license.manager.api.restentity.ContentWithValue;
import com.ginko.license.manager.api.restentity.LicenseDto;
import com.ginko.license.manager.api.restentity.ResultEntity;
import com.ginko.license.manager.api.restentity.ResultUtil;
import com.ginko.license.manager.exception.LicenseValidateException;
import com.ginko.license.manager.exception.UnifiedException;
import com.ginko.license.manager.utils.CommonUtils;
import com.ginko.license.manager.utils.LicenseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ginko
 * @date 8/3/19
 * TODO: 2020/1/10 添加获得控制参数及其正则的接口
 */
@Controller
@RequestMapping(value = "/licenses")
public class LicenseController {

    private static final Logger log = LoggerFactory.getLogger(LicenseController.class);

    @PostMapping(value = "/create")
    @ResponseBody
    public ResultEntity<String> create(@RequestBody @Validated LicenseDto licenseDto) {
        validate(licenseDto);
        String ticket;
        try {
            ticket = LicenseHandler.createLicense(licenseDto);
        } catch (Exception e) {
            log.error(String.format("Create license for subject %1$s failed", licenseDto.getSubject()), e);
            throw new UnifiedException(-1, String.format("Create license for subject %1$s failed", licenseDto.getSubject()));
        }

        return ResultUtil.success(ticket);
    }

    @GetMapping(value = "/{ticket:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    public void download(@PathVariable String ticket,
                         HttpServletResponse response) {
        File file = null;
        try {
            file = LicenseHandler.zipLicense(ticket);

            response.setCharacterEncoding("utf-8");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());
            response.addHeader("Content-Length", String.valueOf(file.length()));
            response.setContentType("application/octet-stream");

            //writeBytes
            CommonUtils.writeBytes(response.getOutputStream(), file);
        } catch (UnifiedException ex) {
            log.info("Download license file failed, cause by: {}", ex.getMsg());
            throw ex;
        } catch (Exception e) {
            log.info("Download license file failed, cause by: {}", e.getMessage());
            log.debug("Exception:", e);
            throw new UnifiedException(-1, String.format("Download license by ticket %1$s failed", ticket));
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }

    /**
     * 校验license时间区间以及控制参数的是否正确
     * @param dto 前台传入的license对象
     */
    private void validate(LicenseDto dto) {
        if (dto.getNotBefore() == null) {
            dto.setNotBefore(new Date());
        }

        if (dto.getNotBefore().after(dto.getNotAfter())) {
            throw new LicenseValidateException("Make sure the begin date is before end date");
        }

        long days = CommonUtils.daysBetween(dto.getNotBefore(), dto.getNotAfter());
        long maxDays = 3650L;
        if (days > maxDays) {
            throw new LicenseValidateException("License max valid date is 3650, make sure the date are legal");
        }

        if (dto.getContents() != null && !dto.getContents().isEmpty()) {
            List<ContentWithValue> contents = dto.getContents();
            List<String> errorMessages = new ArrayList<>();
            String illegalType = "%1$s is an illegal content type.";
            String regexpNotMatch = "Type %1$s's value %2$s is not match regexp %3$s.";
            for (ContentWithValue cwv : contents) {
                LicenseContentType contentType;
                try {
                    contentType = LicenseContentType.valueOf(cwv.getType());
                } catch (IllegalArgumentException e) {
                    errorMessages.add(String.format(illegalType, cwv.getType()));
                    continue;
                }

                if (!cwv.getValue().matches(contentType.getRegexp())) {
                    errorMessages.add(String.format(regexpNotMatch, cwv.getType(), cwv.getValue(), contentType.getRegexp()));
                }
            }

            if (!errorMessages.isEmpty()) {
                throw new LicenseValidateException(errorMessages.stream().collect(Collectors.joining("\n")));
            }
        }
    }
}
