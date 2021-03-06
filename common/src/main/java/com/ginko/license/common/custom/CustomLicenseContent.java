package com.ginko.license.common.custom;

import de.schlichtherle.license.LicenseContent;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * @author ginko
 * @date 7/20/19
 */
public class CustomLicenseContent extends LicenseContent {

    private static final long serialVersionUID = 3394987441801191249L;
    private static final Date ORIGIN_DATE = new Date(0L);

    /**
     * 对除了基本信息以外的控制参数进行封装
     * 但是key值必须是{@link LicenseContentType}类型
     */
    private Map<LicenseContentType, String> contentValueMap;

    /**
     * 当额外的控制参数为空时，返回的map对象
     */
    private static final Map<LicenseContentType, String> EMPTY_MAP = Collections.emptyMap();

    /**
     * 由于truelicense不支持证书最早可使用的时间晚于证书创建的时间
     * 所以新增额外的控制参数来设置证书最早可使用时间，并废弃原来的证书最早可使用时间参数。
     */
    private Date effectiveDate;

    public Optional<String> getContentValue(LicenseContentType key) {
        return Optional.ofNullable(getContentValueMap().get(key));
    }

    public Set<LicenseContentType> getContentTypes() {
        return contentValueMap.keySet();
    }

    public Date getEffectiveDate() {
        return clone(this.effectiveDate);
    }

    public void setEffectiveDate(Date effectiveDate) {
        final Date oldEffectiveDate = this.effectiveDate;
        this.effectiveDate = clone(effectiveDate);
        firePropertyChange("effectiveDate", clone(oldEffectiveDate), clone(effectiveDate));
    }

    public Map<LicenseContentType, String> getContentValueMap() {
        return contentValueMap == null ? EMPTY_MAP : contentValueMap;
    }

    public void setContentValueMap(Map<LicenseContentType, String> contentValueMap) {
        if (this.contentValueMap != null) {
            throw new UnsupportedOperationException("Can't set control contents multiple times!");
        }
        this.contentValueMap = contentValueMap;
    }

    /**
     * 该参数用effectiveDate代替，所以废弃
     */
    @Deprecated
    @Override
    public Date getNotBefore() {
        return super.getNotBefore() == null ? ORIGIN_DATE : super.getNotBefore();
    }

    /**
     * 该参数用effectiveDate代替，所以废弃
     */
    @Deprecated
    @Override
    public void setNotBefore(Date date) {
        throw new UnsupportedOperationException("This method is deprecated");
    }

    protected Date clone(Date date) {
        return null == date ? null : (Date) date.clone();
    }
}
