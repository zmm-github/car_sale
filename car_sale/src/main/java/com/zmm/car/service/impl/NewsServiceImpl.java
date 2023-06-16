package com.zmm.car.service.impl;

import com.zmm.car.entity.News;
import com.zmm.car.mapper.NewsMapper;
import com.zmm.car.service.INewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zmm
 * @since 2023-06-01
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

}
