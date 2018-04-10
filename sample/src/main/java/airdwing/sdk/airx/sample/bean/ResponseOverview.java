package airdwing.sdk.airx.sample.bean;

/**
 * Created by caojin on 2017/6/16.
 */

public class ResponseOverview {
    private int status;
    private String data;
    private Integer code;

    public static boolean hasData(ResponseOverview response) {
        return response.code == null;
    }

    public ResponseOverview() {}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseBase{" +
                "status=" + status +
                ", data='" + data + '\'' +
                ", code=" + code +
                '}';
    }
}
