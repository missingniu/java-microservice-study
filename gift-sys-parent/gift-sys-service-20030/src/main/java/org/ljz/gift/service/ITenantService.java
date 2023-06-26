package org.ljz.gift.service;

import org.ljz.gift.domain.Tenant;
import com.baomidou.mybatisplus.service.IService;
import org.ljz.gift.query.TenantQuery;
import org.ljz.gift.util.AjaxResult;
import org.ljz.gift.util.PageList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
public interface ITenantService extends IService<Tenant> {
    PageList<Tenant> selectPageList(TenantQuery userQuery);

    AjaxResult dealSettlement(Tenant tenant);
}
