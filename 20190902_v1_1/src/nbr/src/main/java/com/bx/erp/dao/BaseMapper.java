package com.bx.erp.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.bx.erp.model.BaseModel;

/** 所有的Mapper都是没有状态的，只有操作，所以所有BO共享一个Mapper是没问题的 */
@Component("mapper")
public interface BaseMapper {
	public BaseModel create(Map<String, Object> params);

	public List<List<BaseModel>> createEx(Map<String, Object> params);

	public BaseModel retrieve1(Map<String, Object> params);

	public List<List<BaseModel>> retrieve1Ex(Map<String, Object> params);

	public List<BaseModel> retrieveN(Map<String, Object> params);

	public List<List<BaseModel>> retrieveNEx(Map<String, Object> params);

	public List<List<BaseModel>> updateEx(Map<String, Object> params);

	public BaseModel update(Map<String, Object> params);

	public BaseModel delete(Map<String, Object> params);

	public List<List<BaseModel>> deleteEx(Map<String, Object> params);

	public void checkUniqueField(Map<String, Object> params);
}
