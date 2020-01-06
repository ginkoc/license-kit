package com.ginko.license.checker.predicates;

/**
 * @author ginko
 * @date 8/28/19
 */
public interface LicensePredicate {

    /**
     * 校验逻辑封装接口，使用者可以实现此接口，实现自己的校验逻辑
     * @return 是否校验成功
     */
    boolean test();
}
