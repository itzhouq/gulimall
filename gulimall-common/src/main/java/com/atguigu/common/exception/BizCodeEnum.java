package com.atguigu.common.exception;

/**
 * @author itzhouq
 * @date 2020/8/8 10:34
 *
 * 错误码和错误信息定义类
 *
 * 1. 错误码定义规则为5个数字
 * 2. 前两位标识业务场景，最后三位表示错误码。
 * 3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 *
 * 错误码列表：
 *
 * 10：通用
 *      001：参数格式校验
 * 11：商品
 * 12：订单
 * 13：购物车
 * 14：物流
 */
public enum BizCodeEnum {

    UNKNOW_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败")

    ;

    public int code;
    public String msg;

    BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}
