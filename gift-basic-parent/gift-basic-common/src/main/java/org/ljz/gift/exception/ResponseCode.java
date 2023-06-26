package org.ljz.gift.exception;



/**
 * 响应信息枚举类
 */
public enum ResponseCode {
    RESPONSE_CODE_200(200,"操作成功"),
    RESPONSE_CODE_400(400,"参数错误！"),
    RESPONSE_CODE_400001(400001,"两次密码不一致,请检查后输入"),
    RESPONSE_CODE_400003(400001,"你输入的密码不正确"),
    RESPONSE_CODE_400002(400002,"店铺已经存在，请检查后重新提交"),
    RESPONSE_CODE_500(500,"网络异常!请稍后再试!"),
    RESPONSE_CODE_4005(4004,"当前没有可以能够上架的商品"),
    RESPONSE_CODE_4006(4006,"请输入验证码"),
    RESPONSE_CODE_4007(4007,"你的验证码不正确") ,
    RESPONSE_CODE_4008(4008,"不要一直点了"),
    ResponseCode_CODE_4009(4009,"请输入正确的验证码"),
    ResponseCode_CODE_40023(4023,"手机号不能为空"),
    ResponseCode_CODE_4010(4010,"手机号码已经被注册"),
    ResponseCode_CODE_4011(4011,"邮箱已经被注册"),
    ResponseCode_CODE_40012(4012,"验证码已经失效，请重新获取"),
    ResponseCode_CODE_40013(40013,"当前用户已经存在"),
    ResponseCode_CODE_40015(40015,"用户名或者手机号或者邮箱不存在"),
    ResponseCode_CODE_40016(40016,"请登录哦兄弟"),
    ResponseCode_CODE_40017(40017,"登录过期"),
    RESPONSE_CODE_4016(4016,"不能够重复绑定"),
    ResponseCode_code_40018(40018,"你的登录机会还有"),
    ResponseCode_code_40019(40019,"你的账户存在被盗风险，系统自动封禁"),
    ResponseCode_code_40021(40021,"当前手机号没有在平台上注册过，不能修改密码"),
    ResponseCode_code_40020(40020,"你的账号状态异常不能登录"),
    ResponseCode_code_40022(40022,"你的入驻名不合法");
    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;
    private String message;


}
