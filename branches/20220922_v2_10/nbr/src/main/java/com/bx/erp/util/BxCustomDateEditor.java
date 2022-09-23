package com.bx.erp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.StringUtils;

import com.bx.erp.action.BaseAction;

/** 令前端支持2种日期格式： "yyyy-MM-dd HH:mm:ss"; "yyyy/MM/dd HH:mm:ss"; */
public class BxCustomDateEditor extends CustomDateEditor {
	protected boolean bAllowEmpty;

	public BxCustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
		this.bAllowEmpty = allowEmpty;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.bAllowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		// else if (text != null && this.exactDateLength >= 0 && text.length() !=
		// this.exactDateLength) {
		// throw new IllegalArgumentException(
		// "Could not parse date: it is not exactly" + this.exactDateLength +
		// "characters long");
		// }
		else {
			try {
				setValue(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).parse(text));
			} catch (ParseException ex) {
				try {
					setValue(new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3).parse(text));
				} catch (ParseException ex2) {
					throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex2);
				}
			}
		}
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		String date = "";
		try {
			date = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(value);
		} catch (Exception ex) {
			try {
				date = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2).format(value);
			} catch (Exception ex2) {
				return "";
			}
		}
		return (value != null ? date : "");
	}
}
