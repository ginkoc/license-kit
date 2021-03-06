package com.ginko.license.checker.predicates;

/**
 * @author ginko
 * @date 8/28/19
 * TODO: 2020/1/9 完善几个原生的predicate
 */
public interface LicensePredicate {

    /**
     * 校验逻辑封装接口，使用者可以实现此接口，实现自己的校验逻辑
     *
     * @return 是否校验成功
     */
    boolean test();

    /**
     * TODO: 2020/1/8 有没有更合理的方式来指明错误原因
     * 返回出现错误的原因
     *
     * @return 错误信息
     */
    String getErrorMessage();
}
