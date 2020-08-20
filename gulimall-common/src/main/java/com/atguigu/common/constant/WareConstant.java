package com.atguigu.common.constant;

/**
 *  仓储服务常量
 * @author itzhouq
 * @date 2020/8/20 23:29
 */

public class WareConstant {

    public enum PurchaseEnum {
        CREATE(0, "新建"),
        ASSIGNED(1, "已分配"),
        RECEIVE(2, "已领取"),
        FINISH(3, "已完成"),
        HASHERROR(4, "有异常");

        private int code;
        private String msg;

        PurchaseEnum(int code, String msg) {
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

    public enum PurchaseDetailEnum {
        CREATE(0, "新建"),
        ASSIGNED(1, "已分配"),
        BUYING(2, "正在采购"),
        FINISH(3, "已完成"),
        HASHERROR(4, "采购失败");

        private int code;
        private String msg;

        PurchaseDetailEnum(int code, String msg) {
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
}
