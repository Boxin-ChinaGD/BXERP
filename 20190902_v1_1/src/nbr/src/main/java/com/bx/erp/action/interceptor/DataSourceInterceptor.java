package com.bx.erp.action.interceptor;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.bx.erp.util.DataSourceContextHolder;

@Aspect // for aop
@Component // for auto scan
@Order(0) // 在事务前执行
public class DataSourceInterceptor {
	@Pointcut("execution(public * com.bx.erp.action.bo.*.*(..))")
	public void dataSourceSlave() {
	};

	@Before("dataSourceSlave()")
	public void before(JoinPoint jp) {
//		System.out.println("进入切面");
	}

	@After("dataSourceSlave()")
	public void removeDataSoruce(JoinPoint joinPoint) throws Throwable {
		DataSourceContextHolder.clearDbName();
	}

//	@Pointcut("execution(public * com.bx.erp.dao.CompanyMapper.*(..))")
//	public void dataSourceCompanyMapper() {
//	};
//
//	@Before("dataSourceCompanyMapper()")
//	public void beforeCompanyMapper(JoinPoint jp) {
//		DataSourceContextHolder.setDbName(DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx);
//	}
//
//	@After("dataSourceCompanyMapper()")
//	public void removeDataSoruceCompanyMapper(JoinPoint joinPoint) throws Throwable {
//		DataSourceContextHolder.clearDbName();
//	}

//	@Pointcut("execution(public * com.bx.erp.dao.BxStaffMapper.*(..))")
//	public void dataSourceBxStaffMapper() {
//	};
//
//	@Before("dataSourceBxStaffMapper()")
//	public void beforeBxStaffMapper(JoinPoint jp) {
//		DataSourceContextHolder.setDbName(DataSourceContextHolder.DATASOURCE_jdbcDataSource_nbr_bx);
//	}
//
//	@After("dataSourceBxStaffMapper()")
//	public void removeDataSoruceBxStaffMapper(JoinPoint joinPoint) throws Throwable {
//		DataSourceContextHolder.clearDbName();
//	}

//	@Pointcut("execution(* com.bx.erp.cache..*.*(..))")
//	public void dataSourceOfNormalCache() {
//	};
//
//	@Before("dataSourceOfNormalCache()")
//	public void beforeCache(JoinPoint jp) {
//	}
}
