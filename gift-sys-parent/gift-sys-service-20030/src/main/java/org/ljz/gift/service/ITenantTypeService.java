package org.ljz.gift.service;

import org.ljz.gift.domain.TenantType;
import com.baomidou.mybatisplus.service.IService;
import org.ljz.gift.query.TenantTypeQuery;
import org.ljz.gift.util.PageList;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
public interface ITenantTypeService extends IService<TenantType> {

    PageList<TenantType> selectPageList(TenantTypeQuery query);
}
