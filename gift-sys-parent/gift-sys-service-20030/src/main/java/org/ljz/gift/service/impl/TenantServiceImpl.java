package org.ljz.gift.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.client.AuthClient;
import org.ljz.gift.domain.Employee;
import org.ljz.gift.domain.LoginUser;
import org.ljz.gift.domain.Tenant;
import org.ljz.gift.exception.Assert;
import org.ljz.gift.exception.BusinessException;
import org.ljz.gift.exception.ResponseCode;
import org.ljz.gift.mapper.EmployeeMapper;
import org.ljz.gift.mapper.TenantMapper;
import org.ljz.gift.query.TenantQuery;
import org.ljz.gift.service.ITenantService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.BaiduAuditUtils;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AuthClient authClient;
    @Override
    public PageList<Tenant> selectPageList(TenantQuery query) {
        Page<Tenant> page = new Page<>(query.getPage(), query.getRows());
        //自定义mapper方法，返回关联数据对象
        List<Tenant> data = tenantMapper.loadPageList(page,query);
        return new PageList<>(page.getTotal(),data);
    }

    @Override
    public AjaxResult dealSettlement(@Valid Tenant tenant) {
        //参数校验
        //用户校验是否存在
        Assert.isEq(tenant.getEmployee().getPassword(),tenant.getConfirmPassword(),ResponseCode.RESPONSE_CODE_400001);
        //第一步校叫租户名是否合规
        Map<String, Object> legal = BaiduAuditUtils.textCensor(tenant.getCompanyName());
        LoginUser loginUser = new LoginUser() ;
        Employee employee = new Employee();
        Tenant part = new Tenant();
        if((Boolean)legal.get("success")){
            //判断密码是否是否一致
            //说明租户名称合法

            loginUser.setType(0);//表管理员
            loginUser.setUsername(tenant.getEmployee().getUsername());

            loginUser.setTel(tenant.getEmployee().getTel());
            loginUser.setPassword(tenant.getEmployee().getPassword());
            loginUser.setEmail(tenant.getEmployee().getEmail());

            AjaxResult result = authClient.addOrUpdate(loginUser);
            if(!result.getSuccess()){
                return result;
            }
            //设置loginId
            Long loginId = Long.valueOf(result.getResultObj().toString());
            System.out.println(loginUser);


            //保存管理员信息

            employee.setEmail(tenant.getEmployee().getEmail());
            employee.setTel(tenant.getEmployee().getTel());
            employee.setInputTime(new Date());
            employee.setPassword(tenant.getEmployee().getPassword());
            employee.setLoginId(loginId);
            employee.setUsername(tenant.getEmployee().getUsername());
            employee.setType(1);//设置状态为正常
            employee.setState(1);
            employeeMapper.insert(employee);
            //保存租户信息

            part.setCompanyName(tenant.getCompanyName());
            part.setAddress(tenant.getAddress());
            //Wrapper<Tenant> wrapper = new EntityWrapper<Tenant>().eq("admin_id",employee.getId());
//            List<Tenant> tenants = tenantMapper.selectList(wrapper);
            //System.out.println(tenants);
            part.setAdminId(employee.getId());//有点问题
            part.setCompanyNum(tenant.getCompanyNum());
            part.setState(1);//设置审核状态位通过
            tenantMapper.insert(part);
        }else{
            //租户信息不合法，抛出异常信息
            //保存管理员信息

            employee.setEmail(tenant.getEmployee().getEmail());
            employee.setTel(tenant.getEmployee().getTel());
            employee.setInputTime(new Date());
            employee.setPassword(tenant.getEmployee().getPassword());
            employee.setUsername(tenant.getEmployee().getUsername());
            employee.setType(1);//设置状态为正常
            employee.setState(1);
            employeeMapper.insert(employee);
            //保存租户信息

            part.setCompanyName(tenant.getCompanyName());
            part.setAddress(tenant.getAddress());
            part.setAdminId(employee.getId());//有点问题
            part.setCompanyNum(tenant.getCompanyNum());
            part.setState(1);//设置审核状态位通过
            tenantMapper.insert(part);

        }
        return AjaxResult.me();
    }
}
