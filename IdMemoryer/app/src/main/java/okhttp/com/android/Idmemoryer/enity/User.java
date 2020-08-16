package okhttp.com.android.Idmemoryer.enity;

public class User {

    private String loginAccount;

    private String loginPassword;
    private Long phone;
    private Boolean loginResult;

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginPassword(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginAccount(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Boolean getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(Boolean loginResult) {
        this.loginResult = loginResult;
    }

}
