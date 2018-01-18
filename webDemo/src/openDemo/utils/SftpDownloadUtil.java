package openDemo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpDownloadUtil {

	/**
	 * 下载sftp上的文件
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param fileName
	 *            要下载的带目录文件名
	 * @param charsetFrom
	 *            文件原来的字符集编码
	 * @param charsetTo
	 *            同步用的字符集编码
	 * @return 文件行记录集合
	 * @throws JSchException
	 * @throws IOException
	 * @throws SftpException
	 */
	public static List<String> downloadAsString(String host, int port, String username, String password,
			String fileName, String charsetFrom, String charsetTo) throws JSchException, IOException, SftpException {
		Session session = null;
		ChannelSftp channelSftp = null;
		List<String> lines = null;
		try {
			// 连接到sftp服务器
			if (session == null || !session.isConnected()) {
				JSch jsch = new JSch();
				session = jsch.getSession(username, host, port);
				session.setPassword(password);
				Properties sshConfig = new Properties();
				sshConfig.put("StrictHostKeyChecking", "no");
				session.setConfig(sshConfig);
				session.connect();
			}
			if (channelSftp == null || channelSftp.isClosed() || !channelSftp.isConnected()) {
				channelSftp = (ChannelSftp) session.openChannel("sftp");
				channelSftp.connect();
			}

			// 获取文件
			lines = readLines(channelSftp.get(fileName), charsetFrom, charsetTo);
		} finally {
			// 断开连接
			sftpDisconnect(session, channelSftp);
		}

		return lines;
	}

	public static void uploadFile(String host, int port, String username, String password, String fileName)
			throws JSchException, SftpException {
		Session session = null;
		ChannelSftp channelSftp = null;
		try {
			// 连接到sftp服务器
			if (session == null || !session.isConnected()) {
				JSch jsch = new JSch();
				session = jsch.getSession(username, host, port);
				session.setPassword(password);
				Properties sshConfig = new Properties();
				sshConfig.put("StrictHostKeyChecking", "no");
				session.setConfig(sshConfig);
				session.connect();
			}
			if (channelSftp == null || channelSftp.isClosed() || !channelSftp.isConnected()) {
				channelSftp = (ChannelSftp) session.openChannel("sftp");
				channelSftp.connect();
			}

			// TODO 上传文件
			channelSftp.put(fileName);
		} finally {
			// 断开连接
			sftpDisconnect(session, channelSftp);
		}
	}

	/**
	 * 读取文件流并获得行字符串集合
	 * 
	 * @param in
	 *            文件字节流
	 * @param fromCharset
	 *            文件原来的字符集编码
	 * @param toCharset
	 *            同步用的字符集编码
	 * @return
	 * @throws IOException
	 */
	private static List<String> readLines(InputStream in, String fromCharset, String toCharset) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			// 要读取文件的编码
			reader = new BufferedReader(new InputStreamReader(in, fromCharset));

			String tempLine = null;
			// 一次读一行
			while ((tempLine = reader.readLine()) != null) {
				// 转为需要的编码
				lines.add(new String(tempLine.getBytes(), toCharset));
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return lines;
	}

	/**
	 * 与sftp服务器断开连接 释放资源
	 * 
	 * @param session
	 * @param channelSftp
	 */
	private static void sftpDisconnect(Session session, ChannelSftp channelSftp) {
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}

		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	public static void main(String[] args) throws Exception {
		File file = new File("D:\\test.txt");
		System.out.println(file);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(0);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
}
