package com.atguigu.common.validator.group;

import com.atguigu.common.validator.ListValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义校验器
 * @author itzhouq
 * @date 2020/8/8 12:12
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();
    // 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] value = constraintAnnotation.value();
        for (int v : value) {
            set.add(v);
        }
    }

    /**
     * 判断是否校验成功
     * @param integer  需要检验的值
     * @param constraintValidatorContext  上下文对象
     * @return 是否校验成功
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
