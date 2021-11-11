package com.bx.erp.dao.commodity;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("categoryMapper")
public interface CategoryMapper extends BaseMapper {
	public List<BaseModel> retrieveNByParent(Map<String, Object> params);
}
