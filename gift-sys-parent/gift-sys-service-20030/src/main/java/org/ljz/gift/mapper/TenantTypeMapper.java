package org.ljz.gift.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.domain.TenantType;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.ljz.gift.query.TenantTypeQuery;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
public interface TenantTypeMapper extends BaseMapper<TenantType> {

    List<TenantType> loadPageList(Page<TenantType> page, TenantTypeQuery query);
}
