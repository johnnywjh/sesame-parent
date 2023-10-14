package kim.sesame.common.entity;

public enum YesOrNO {
    NO(0, "否"),
    YES(1, "是");

    private final Integer code;
    private final String msg;

    private YesOrNO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}