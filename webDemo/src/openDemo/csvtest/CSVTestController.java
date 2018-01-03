package openDemo.csvtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

@Controller
@RequestMapping("test")
public class CSVTestController {
	public static final String FILE_TYPE = ".csv";
	public static final String FILE_CHARSET = "utf-8";
	public static final int FILE_MAXSIZE = 1024 * 1024;
	public static final char SEPRATOR = ',';
	public static final char QUOTE_CHAR = '"';
	public static final int LINE_SIZE = 8;

	private CSVTestModelDao dao = new CSVTestModelDao();

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String mapKey = "result";
		String retVal = "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);

			Iterator<FileItem> it = items.iterator();
			if (it.hasNext()) {
				FileItem fileItem = it.next();
				if (!fileItem.getName().toLowerCase().endsWith(FILE_TYPE)) {
					// 上传文件类型错误
					retVal = "上传文件类型错误";
					map.put(mapKey, retVal);
					return map;
				}
				if (fileItem.getSize() > FILE_MAXSIZE) {
					// 超出文件最大size
					retVal = "超出文件最大size";
					map.put(mapKey, retVal);
					return map;
				}

				// List<String> fileStr = readFile(fileItem.getInputStream());
				InputStream ins = null;
				CSVReader csvReader = null;
				List<String[]> list = new ArrayList<String[]>();
				try {
					ins = fileItem.getInputStream();
					csvReader = new CSVReader(new InputStreamReader(ins, Charset.forName(FILE_CHARSET)), SEPRATOR,
							QUOTE_CHAR, 1);
					list = csvReader.readAll();
				} finally {
					if (ins != null) {
						ins.close();
					}
					if (csvReader != null) {
						csvReader.close();
					}
				}
				int count = 0;
				for (String[] line : list) {
					if (line.length != LINE_SIZE) {
						continue;
					}

					try {
						dao.insert(mapRowsToModel(line));
					} catch (SQLException e) {
						// TODO
					}

					count++;
				}
				retVal = "处理成功条数：" + count;
			} else {
				// 无上传文件
				retVal = "无上传文件";
			}
		}
		map.put(mapKey, retVal);
		return map;
	}

	/**
	 * csv文件行数据转java对象
	 * 
	 * @param line
	 * @return
	 */
	private CSVTestModel mapRowsToModel(String[] line) {
		CSVTestModel model = new CSVTestModel();
		model.setName(line[0]);
		model.setSex(line[1]);
		model.setOrgName(line[2]);
		model.setOrgCode(line[3]);
		model.setDeptName(line[4]);
		model.setDeptCode(line[5]);
		model.setUserId(line[6]);
		model.setStatus(line[7]);
		return model;
	}

	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 设置响应头和客户端保存文件名
		response.setCharacterEncoding(FILE_CHARSET);
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		response.setHeader("Content-Disposition", "attachment;fileName=" + "test.csv");

		List<CSVTestModel> list = dao.getAll();

		PrintWriter writer = null;
		CSVWriter csvWriter = null;
		try {
			writer = response.getWriter();
			csvWriter = new CSVWriter(writer, SEPRATOR, QUOTE_CHAR);
			csvWriter.writeNext(getFileHeader());
			csvWriter.writeAll(mapModelToRows(list));
			csvWriter.flush();
		} finally {
			if (writer != null) {
				writer.close();
			}
			if (csvWriter != null) {
				csvWriter.close();
			}
		}
	}

	/**
	 * csv文件头
	 * 
	 * @return
	 */
	private String[] getFileHeader() {
		String[] arr = new String[LINE_SIZE];
		arr[0] = "姓名";
		arr[1] = "性别";
		arr[2] = "机构名称";
		arr[3] = "机构代码";
		arr[4] = "部门名称";
		arr[5] = "部门代码";
		arr[6] = "新一代机构员工编号代码";
		arr[7] = "状态";
		return arr;
	}

	/**
	 * java对象转csv文件行数据
	 * 
	 * @param list
	 * @return
	 */
	private List<String[]> mapModelToRows(List<CSVTestModel> list) {
		List<String[]> rows = new ArrayList<String[]>();
		for (CSVTestModel model : list) {
			String[] arr = new String[LINE_SIZE];
			arr[0] = model.getName();
			arr[1] = model.getSex();
			arr[2] = model.getOrgName();
			arr[3] = model.getOrgCode();
			arr[4] = model.getDeptName();
			arr[5] = model.getDeptCode();
			arr[6] = model.getUserId();
			arr[7] = model.getStatus();
			rows.add(arr);
		}
		return rows;
	}

	private List<String> readFile(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(FILE_CHARSET)));
		List<String> lines = new ArrayList<String>();
		String temp = null;
		try {
			while ((temp = reader.readLine()) != null) {
				lines.add(temp);
			}
		} finally {
			try {
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return lines;
	}
}
