package com.ss.sdk.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自定义基础通用方法
 *
 * @author FrancisYs
 * @date 2019/11/18
 * @email yaoshuai@ss-cas.com
 */
public interface SsMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
