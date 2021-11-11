package com.bx.erp.cache.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.cache.BaseCache;

@Component("configCache")
@Scope("prototype")
public abstract class ConfigCache extends BaseCache {
}
