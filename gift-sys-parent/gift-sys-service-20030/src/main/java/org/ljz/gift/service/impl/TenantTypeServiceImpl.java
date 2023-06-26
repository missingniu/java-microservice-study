package org.ljz.gift.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import org.ljz.gift.domain.TenantType;
import org.ljz.gift.mapper.TenantTypeMapper;
import org.ljz.gift.query.TenantTypeQuery;
import org.ljz.gift.service.ITenantTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.ljz.gift.util.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class TenantTypeServiceImpl extends ServiceImpl<TenantTypeMapper, TenantType> implements ITenantTypeService {
    @Autowired
    private TenantTypeMapper tenantTypeMapper;
    @Override
    public PageList<TenantType> selectPageList(TenantTypeQuery query) {
        Page<TenantType> page = new Page<TenantType>(query.getPage(),query.getRows());
        List<TenantType> data = tenantTypeMapper.loadPageList(page,query);
        return new PageList<>(page.getTotal(),data);
    }
}
