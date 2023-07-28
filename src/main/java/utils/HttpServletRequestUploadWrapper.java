package utils;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

public class HttpServletRequestUploadWrapper {

	private final List<FileItem> items;

	public HttpServletRequestUploadWrapper(HttpServletRequest request) throws FileUploadException {
		// Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        items = upload.parseRequest(request);
	}
	
	public String getParameter(String name) {
		String value = items.stream().filter(item -> item.getFieldName().equals(name)).findFirst()
			.map(item -> item.getString())
			.orElse(null);
		
		return value;
	}
	
	public String getBase64Parameter(String name) throws IOException {
		FileItem fileItem = items.stream().filter(item -> item.getFieldName().equals(name))
				.findFirst()
				.orElse(null);

		if (fileItem == null) {
			return null;
		}

		byte[] bytes = IOUtils.toByteArray(fileItem.getInputStream());
	    byte[] encodedBytes = Base64.getEncoder().encode(bytes);
	    String base64String = new String(encodedBytes);
	    return !"".equals(base64String) ? base64String : null ;
	}
	
	public byte[] getBytesParameter(String name) throws IOException {
		FileItem fileItem = items.stream().filter(item -> item.getFieldName().equals(name))
				.findFirst()
				.orElse(null);

		if (fileItem == null) {
			return null;
		}

		byte[] bytes = IOUtils.toByteArray(fileItem.getInputStream());
	    return bytes;
	}
}
