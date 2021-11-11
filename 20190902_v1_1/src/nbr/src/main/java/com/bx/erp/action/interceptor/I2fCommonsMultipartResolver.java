package com.bx.erp.action.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component("i2fCommonsMultipartResolver")
public class I2fCommonsMultipartResolver extends CommonsMultipartResolver {
	@Override
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = this.prepareFileUpload(encoding, request);
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);

		}
		catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
		}
		catch (FileUploadBase.FileSizeLimitExceededException ex) {
			throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), ex);
		}
		catch (FileUploadException ex) {
			throw new MultipartException("Failed to parse multipart servlet request", ex);
		}
	}

	protected FileUpload prepareFileUpload(String encoding, HttpServletRequest request) {
		FileUpload fileUpload = getFileUpload();
		FileUpload actualFileUpload = fileUpload;
		if (encoding != null && !encoding.equals(fileUpload.getHeaderEncoding())) {
			actualFileUpload = newFileUpload(getFileItemFactory());
			actualFileUpload.setHeaderEncoding(encoding);
			logger.debug("导入文件请求的URL：" + request.getRequestURI());
			boolean bImportFile = request.getRequestURI().contains("/import/importEx.bx");
			if (bImportFile) {
				actualFileUpload.setSizeMax(5 * 1024 * 1024);// 重新设置文件限制5M
			} else {
				actualFileUpload.setSizeMax(fileUpload.getSizeMax());
			}
		}
		return actualFileUpload;
	}
}
