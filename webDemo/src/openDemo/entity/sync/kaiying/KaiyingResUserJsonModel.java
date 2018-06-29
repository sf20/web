package openDemo.entity.sync.kaiying;

/**
 * 恺英接口返回的人员json数据模型
 * 
 * @author yanl
 *
 */
public class KaiyingResUserJsonModel {
	private int code;
	private String msg;
	private KaiyingUserInfoModel data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public KaiyingUserInfoModel getData() {
		return data;
	}

	public void setData(KaiyingUserInfoModel data) {
		this.data = data;
	}

}
