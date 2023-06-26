package org.ljz.gift.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.ljz.gift.domain.Tenant;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.ljz.gift.query.TenantQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Repository
public interface TenantMapper extends BaseMapper<Tenant> {

    List<Tenant> loadPageList(Page<Tenant> page, TenantQuery query);
}
