package openDemo.common;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;

public class PrintUtil {
	public static Logger LOGGER = LogManager.getLogger(PrintUtil.class);

	public static void printUsers(List<UserInfoModel> userList) {
		System.out.println(userList.size());
		for (UserInfoModel model : userList) {
			System.out.println(model.getID() + "--" + model.getCnName() + "--" + model.getSex() + "--"
					+ model.getMobile() + "--" + model.getMail() + "--" + model.getOrgOuCode() + "--"
					+ model.getPostionNo() + "--" + model.getEntryTime() + "--" + model.getStatus());
		}
	}

	public static void printOrgs(List<OuInfoModel> orgList) {
		System.out.println(orgList.size());
		for (OuInfoModel model : orgList) {
			System.out.println(
					model.getID() + "--" + model.getOuName() + "--" + model.getParentID() + "--" + model.getStatus());
		}
	}

	public static void printPoss(List<PositionModel> posList) {
		System.out.println(posList.size());
		for (PositionModel model : posList) {
			System.out.println(model.getpNo() + "--" + model.getpNames() + "--" + model.getpNameClass());
		}
	}

	public static void logPrintUsers(List<UserInfoModel> userList) {
		for (UserInfoModel model : userList) {
			LOGGER.info("ID:" + model.getID() + "--CnName:" + model.getCnName() + "--Sex:" + model.getSex()
					+ "--Mobile:" + model.getMobile() + "--Mail:" + model.getMail() + "--OrgOuCode:"
					+ model.getOrgOuCode() + "--PostionNo:" + model.getPostionNo() + "--PostionName:"
					+ model.getPostionName() + "--EntryTime:" + model.getEntryTime() + "--Status:" + model.getStatus());
		}
	}

	public static void logPrintOrgs(List<OuInfoModel> orgList) {
		for (OuInfoModel model : orgList) {
			LOGGER.info(
					model.getID() + "--" + model.getOuName() + "--" + model.getParentID() + "--" + model.getStatus());
		}
	}

	public static void logPrintPoss(List<PositionModel> posList) {
		for (PositionModel model : posList) {
			LOGGER.info(model.getpNo() + "--" + model.getpNames() + "--" + model.getpNameClass());
		}
	}
}
