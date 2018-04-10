package airdwing.sdk.airx.sample.bean;

/**
 * Created by caojin on 2017/6/16.
 */

public class User {
    private String auth;
    private Integer expires;
    private Long uid;
    private String username;
    private String mobile;
    private String email;
    private Long oid;
    private String avatar;
    private Boolean verified;

    public User() {}

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getExpires() {
        return expires;
    }

    public void setExpires(Integer expires) {
        this.expires = expires;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "User{" +
                "auth='" + auth + '\'' +
                ", expires=" + expires +
                ", uid=" + uid +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", oid=" + oid +
                ", avatar='" + avatar + '\'' +
                ", verified=" + verified +
                '}';
    }
}
