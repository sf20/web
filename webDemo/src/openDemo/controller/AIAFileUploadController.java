package openDemo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import openDemo.service.sync.aia.AIASyncService;

@Controller
@RequestMapping("aia")
public class AIAFileUploadController {
	// 文件后缀名
	private static final String FILE_TYPE = ".txt";
	// 字符集编码
	private static final String CHARSET_UTF8 = "UTF-8";
	@Autowired
	private AIASyncService aiaSyncService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String mapKey = "result";
		String retVal = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);

			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
				FileItem fileItem = it.next();
				String fileName = fileItem.getName();
				if (!fileName.toLowerCase().endsWith(FILE_TYPE)) {
					// 上传文件类型错误
					retVal = "上传文件类型错误";
					map.put(mapKey, retVal);
					return map;
				}

				// 数据同步
				aiaSyncService.syncDataFromFile(fileName, readLines(fileItem, CHARSET_UTF8, CHARSET_UTF8));
			}
			// 上传成功
			retVal = "上传成功";
			map.put(mapKey, retVal);
			return map;
		}
		// 无上传文件
		retVal = "无上传文件";
		map.put(mapKey, retVal);
		return map;
	}

	/**
	 * 读取文件并获得行字符串集合
	 * 
	 * @param fileItem
	 *            上传的文件
	 * @param fromCharset
	 *            文件原来的字符集编码
	 * @param toCharset
	 *            同步用的字符集编码
	 * @return
	 * @throws IOException
	 */
	private List<String> readLines(FileItem fileItem, String fromCharset, String toCharset) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		InputStream ins = null;
		try {
			ins = fileItem.getInputStream();
			// 要读取文件的编码
			reader = new BufferedReader(new InputStreamReader(ins, fromCharset));

			String tempLine = null;
			// 一次读一行
			while ((tempLine = reader.readLine()) != null) {
				// 转为需要的编码
				lines.add(new String(tempLine.getBytes(), toCharset));
			}
		} finally {
			if (ins != null) {
				ins.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return lines;
	}
}