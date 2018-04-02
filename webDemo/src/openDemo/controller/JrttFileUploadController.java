package openDemo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
import openDemo.service.sync.jrtt.JrttSyncService;

/**
 * 今日头条数据同步Controller
 * 
 * @author MKT-28
 *
 */
@Controller
@RequestMapping("jrtt")
public class JrttFileUploadController {
	// 文件后缀名
	private static final String FILE_TYPE = ".json";
	// 返回值定义
	private static final String RETURN_CODE = "code";
	private static final String RETURN_CODE_OK = "0";
	private static final String RETURN_CODE_NG = "-1";
	private static final String RETURN_MSG = "message";
	@Autowired
	private JrttSyncService jrttSyncService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(HttpServletRequest request, String apikey, String salt, String signature) throws Exception {
		JSONObject retJson = new JSONObject();
		String msg = "";

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// 处理提交表单
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);

			boolean fileExist = false;
			for (FileItem fileItem : items) {
				// fileItem中封装的是上传文件
				if (!fileItem.isFormField()) {
					String fileName = fileItem.getName();
					if (!fileName.toLowerCase().endsWith(FILE_TYPE)) {
						// 上传文件类型错误
						msg = "上传文件类型错误";
						retJson.put(RETURN_CODE, RETURN_CODE_NG);
						retJson.put(RETURN_MSG, msg);
						return retJson;
					}
					fileExist = true;
				}
			}

			if (!fileExist) {
				// 无上传文件
				msg = "无上传文件";
				retJson.put(RETURN_CODE, RETURN_CODE_NG);
				retJson.put(RETURN_MSG, msg);
				return retJson;
			}

			// 异步处理上传文件
			new Thread() {
				@Override
				public void run() {
					jrttSyncService.asyncProcess(items);
				}
			}.start();

		} else {
			// 无上传文件
			msg = "无上传文件";
			retJson.put(RETURN_CODE, RETURN_CODE_NG);
			retJson.put(RETURN_MSG, msg);
			return retJson;
		}

		// 上传成功
		retJson.put(RETURN_CODE, RETURN_CODE_OK);
		retJson.put(RETURN_MSG, null);
		return retJson;
	}

}