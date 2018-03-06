/*
 * https:api.yunxuetang.com.cn/qidacustomapi/v1/
 */
// 新增或更新申请项目数据
var projectstr1 = '{"id":"","projectName":"测试申请项目","startDate":"2017-12-01","endDate":"2017-12-10",'
		+ '"description":"描述说明","imgUrl":"picimgurl","status":"0",'
		+ '"levels":"","positions":"","publishDate":""}';
var projectarr1 = eval('(' + projectstr1 + ')');
var projecturl1 = "$!{serviceUrl}hzwretail/projects/info";
function insertproject(rstr) {
	if (rstr) {
		console.log(rstr);
	}
}
getdata(projectarr1, projecturl1, insertproject);

// 项目状态更改
var projectstr2 = '{"status":"1"}';

var projectarr2 = eval('(' + projectstr2 + ')');
var projecturl2 = "$!{serviceUrl}hzwretail/projects/948f2ca9-742c-431c-b6fa-4905cad0b890/status";
function changeprojectstate(resultstr) {
	if (resultstr) {
		console.log(resultstr);
		/*
		 * 
		 */
	}
}
// getdata(projectarr2, projecturl2, changeprojectstate);

function getdata(params, url, fname) {
	jQuery
			.ajax({
				type : "POST",
				headers : {
					Accept : "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
					Source : 501,
					Token : "$FunctionLabel.MyProfile.Tokens"
				},
				contentType : "application/json",
				url : url,
				xhrFields : {
					withCredentials : true
				},
				dataType : "json",
				data : JSON.stringify(params),
				cache : false,
				success : function(result) {
					if (result) {
						fname(result);
					}
				},
				error : function(msg) {
					fname(msg);
				}
			});
}
