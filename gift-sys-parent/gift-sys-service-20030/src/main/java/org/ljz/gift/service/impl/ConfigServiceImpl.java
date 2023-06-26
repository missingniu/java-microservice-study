package org.ljz.gift.service.impl;

import org.ljz.gift.domain.Config;
import org.ljz.gift.mapper.ConfigMapper;
import org.ljz.gift.service.IConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author ljz
 * @since 2023-06-23
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {

}
