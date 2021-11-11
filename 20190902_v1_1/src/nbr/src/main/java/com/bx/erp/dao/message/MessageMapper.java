package com.bx.erp.dao.message;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.bx.erp.dao.BaseMapper;
import com.bx.erp.model.BaseModel;

@Component("messageMapper")
public interface MessageMapper extends BaseMapper {

	public List<BaseModel> messageRetrieveNForWx(Map<String, Object> params);

	public BaseModel updateStatus(Map<String, Object> params);
}
