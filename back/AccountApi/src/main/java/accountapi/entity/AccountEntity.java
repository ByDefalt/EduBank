package accountapi.entity;

public class AccountEntity {

    private String id;
    private Integer personalInfoId;
    private Integer roleId;
    private String state;
    private String password;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPersonalInfoId() {
        return personalInfoId;
    }

    public void setPersonalInfoId(Integer personalInfoId) {
        this.personalInfoId = personalInfoId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}