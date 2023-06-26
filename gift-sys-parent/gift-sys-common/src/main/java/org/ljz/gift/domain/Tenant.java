package org.ljz.gift.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@TableName("t_tenant")
public class Tenant extends Model<Tenant> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "租户名不能为空")
    @TableField("company_name")
    private String companyName;
    @NotBlank(message = "租户手机号不能为空")
    @TableField("company_num")
    @NotBlank
    private String companyNum;
    @TableField("register_time")
    private Date registerTime;
    /**
     * 0待审核，1 审核通过 ， 2审核失败
     */
    private Integer state;
    @NotBlank(message = "租户地址不能为空")
    private String address;
    private String logo;
    /**
     * 店铺管理员
     */
    @TableField("admin_id")
    private Long adminId;
    /**
     * 类型id
     */
    @TableField("type_id")
    private Long typeId;
    @NotBlank(message = "租户管理员不能为空")
    @TableField(exist = false)
    private Employee employee;
    @TableField(exist = false)
    private TenantType type;
    @NotBlank(message="请再次输入密码")
    @TableField(exist = false)
    private String confirmPassword;

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @TableField(exist = false)
    private Meal meal;
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TenantType getType() {
        return type;
    }

    public void setType(TenantType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Tenant{" +
        ", id=" + id +
        ", companyName=" + companyName +
        ", companyNum=" + companyNum +
        ", registerTime=" + registerTime +
        ", state=" + state +
        ", address=" + address +
        ", logo=" + logo +
        ", adminId=" + adminId +
        ", typeId=" + typeId +
        "}";
    }


}
