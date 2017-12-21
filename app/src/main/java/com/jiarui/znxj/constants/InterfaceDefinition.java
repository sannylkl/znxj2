package com.jiarui.znxj.constants;

import android.app.Application;

import com.jiarui.znxj.application.AppContext;
import com.jiarui.znxj.utils.HttpUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class InterfaceDefinition {
	/**
	 * 状态信息
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年1月18日
	 */
	public interface IStatus {
		public static final String STATUS = "status";

		public static final String CODE = "code";

		public static final String MESSAGE = "message";
	}

	/**
	 * 分页信息
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年2月13日
	 */
	public interface IPage {
		// 一页显示多少条
		public static final String PAGESIZE = "pagesize";
		String PAGENUM = "pageNum";
		// 当前页码
		public static final String PAGE = "page";

		// 总记录数
		public static final String TOTAL_COUNTS = "totalcounts";

		// 数据集
		public static final String LIST = "list";
	}

	/**
	 * 状态码定义
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年1月18日
	 */
	public interface IStatusCode {
		// 响应成功
		public static final String SUCCESS = "1";

		// token失效
		public static final String TOKEN_FAILURE = "401";

		// 参数错误
		public static final String PARAMS_ERROR = "002";
	}

	/**
	 * 报文数据项信息
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年1月18日
	 */
	public static interface ICommonKey {
		// 校验码
		public static final String TOKEN = "token";

		// key
		public static final String KEY = "n'NI&u#+lFA0y@;$6Wj=5(~9";

		// 报文编号
		public static final String PACK_NO = "pack_no";

		public static final String ROLES = "roles";

		// 发起方时间
		public static final String REQ_DATE = "date";

		// 用户ID
		public static final String USER_ID = "user_id";

		public static final String DEVICE_ID = "deviceId";

		// 报文体
		public static final String DATA = "data";

		// 返回数据体
		public static final String RESULTS = "result";

		// 请求参数key
		public static final String REQUEST_DATA = "requestData";
	}

	public enum Preferences {
		Common, User;
	}

	/**
	 * 请求唯一地址
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年2月13日
	 */
	public interface IPath {
		//访问数据路径,没有用的，后面要删掉，目前有些没有做的板块没有删除,做了的都是HttpUtils里的路径
		public static final String BASE_URL = "http://znxj.0791jr.com/Xunjian/Web/?";
	}
	/**
	 * http请求地址
	 *
	 * @param map URl连接里的参数
	 * @return
	 */
	public static String addUrlValue(Map<String, String> map) {
		String url = HttpUtil.BASE_URL(AppContext.getContext());
		String a = "";
		for (String key : map.keySet()) {
			if (!url.contains("/")) {//编码后的URL不可能存在/
				//编码化url处理 %3F->? %3D->=  %3F问号的转义符
				//存在？ 不存在=
				if (url.contains("%3F") && !url.contains("%3D")) {
					a = "";
					//存在？ 存在=
				} else if (url.contains("%3F") && url.contains("%3D")) {
					a = "%26";
				} else {
					a = "%3F";
				}
			} else {
				//非编码化url处理

				if (url.contains("?") && !url.contains("=")) {//http://zjw.cn?
					a = "";
				} else if (url.contains("?") && url.contains("=")) {//http://zjw.cn?a=b
					a = "&";
				} else {
					//不存在？ 不存在 =
					a = "?";
				}
			}
			url += (a + key + "=" + map.get(key));
		}
		return url;
	}

	/**
	 * http请求地址
	 *
	 * @param map URl连接里的参数
	 * @return
	 */
	public static String addUrlValueLinked(LinkedHashMap<String, String> map) {
		String url = HttpUtil.BASE_URL(AppContext.getContext());
		String a = "";
		for (String key : map.keySet()) {
			if (!url.contains("/")) {//编码后的URL不可能存在/
				//编码化url处理 %3F->? %3D->=  %3F问号的转义符
				//存在？ 不存在=
				if (url.contains("%3F") && !url.contains("%3D")) {
					a = "";
					//存在？ 存在=
				} else if (url.contains("%3F") && url.contains("%3D")) {
					a = "%26";
				} else {
					a = "%3F";
				}
			} else {
				//非编码化url处理

				if (url.contains("?") && !url.contains("=")) {//http://zjw.cn?
					a = "";
				} else if (url.contains("?") && url.contains("=")) {//http://zjw.cn?a=b
					a = "&";
				} else {
					//不存在？ 不存在 =
					a = "?";
				}
			}
			url += (a + key + "=" + map.get(key));
		}
		return url;
	}


	/**
	 * 用户状态存储
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年2月13日
	 */
	public static interface PreferencesUser {
		//用户身份，1巡检员，2设点员
		public static final String GROUP_ID = "group_id";
		public static final String USER_ID = "User_Id";

		// 登录凭据
		public static final String LOGIN_CREDENTIALS = "login_credentials";
		/**
		 * 对接凭据
		 */
		String Docking_CREDENTIALS = "docking_credentials";

		public static final String WELCOME_STATE = "Welcome_State";// 引导页状态

		public static final String LOGIN_STATE = "Login_State";// 登录状态

		public static final String IS_LOADING_NEW_DATA = "isLoadingNewData";// 是否要加载新的数据

		public static final String USER_HEAD = "UserHead";// 头像

		public static final String USER_NICKNAME = "UserNickName";// 昵称

		public static final String USER_NAME = "USER_NAME";// 用户账号

		public static final String REAL_NAME = "realname";// 用户名字

		public static final String PASSWORD = "PASSWORD";// 密码

		public static final String REMEMBER = "remember";// 是否记住密码

		public static final String City = "City";// 所在城市

		public static final String County = "County";// 所属区域

		public static final String Reservoir = "Reservoir";// 所属水库

		public static final String ReservoId = "ReservoId";// 水库id

		public static final String SIGNATURE = "Signature";// 是否要加载新的数据

	}

	/**
	 * 获取公告列表
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年2月13日
	 */
	public interface IBulletin extends ICommonKey, IPage, IStatus, IPath {
		// 报文编号
		String PACKET_NO_DATA = "20000";

		// 时间标记 new:表示获取最新公告 old:表示获取过往公告
		String DATE_FLAG = "date_flag";

		String NEW = "new";

		String OLD = "old";
	}

	/**
	 * 获取任务分类列表
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年2月13日
	 */
	public interface IGetTaskType extends ICommonKey, IStatus,
			IPath, IPage {
		public static final String PACKET_NO_DATA = "30001";
	}

	/**
	 * 获取任务数据列表
	 *
	 * @author Only You
	 * @version 1.0
	 * @date 2016年2月13日
	 */
	public interface IGetTask extends ICommonKey, IStatus, IPage,
			IPath {
		public static final String PACKET_NO_DATA = "30002";

		public static final String TASKTYPE = "TaskType";// （加急）1是 0否
		// 此参数可用于加急任务获取

		public static final String SALOON = "Saloon";// 任务大厅

		public static final String PROVINCE_ID = "province_id";// (查询省份ID，如：江西省ID)

		public static final String CITY_ID = "city_id";// (查询城市ID，如：南昌市ID)

		public static final String AREA_ID = "area_id";// (查询区县ID，如：西湖区ID)

		public static final String TITLE = "title";// (任务标题关键词搜索)

		public static final String ORDER_COMMISSION = "order_commission";// (按佣金排序
		// 参数值
		// ：asc
		// 升序
		// desc
		// 降序)

		public static final String ORDER_ETIME = "order_etime"; // (按下架时间排序 参数值
		// ：asc 升序 desc
		// 降序)

		public static final String URGENT = "urgent";// （加急）1是 0否 此参数可用于加急任务获取

		public static final String ORDINARY = "ordinary";// (普通) 1是 0否
		// 此参数用于普通任务获取

		public static final String LONGTERM = "longterm";// （长期）1是 0否
		// 此参数可用于长期任务获取

		public static final String SAMECITY = "SameCity";

		public static final String AREA = "area";// （位置）此参数可用于同城任务获取

		public static final String TYPE = "type";// （任务类型）

	}

	/**
	 * 请求参数和指令
	 *
	 * @author xjh
	 * @version 1.0
	 * @date 2017年2月17日
	 */
	public interface IKey {
		String R = "r";

		String ACCESS_TOKEN = "access-token";

		String MOBILE = "mobile";

		String TOKEN = "LaeK_VAOefL_g3-YhcjOrtfrQWqfmo-k";

		String TOKEN_USERNAME = "xjandroid";

		String TOKEN_PASSWORE = "waAw42PBG9rsMt";

	}

	/**
	 * 获取token
	 *
	 * @author xjh
	 * @version 1.0
	 * @date 2017年10月28日
	 */
	public interface IToken extends ICommonKey, IPage, IStatus, IPath, IKey {
		String RValue = "auth/get-token/login";
		/**
		 * token账户
		 */
		String USERNAME = "username";
		/**
		 * token密码
		 */
		String PASSWORD = "password";
	}

	/**
	 *任务列表（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/task/index&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年10月28日
	 */
	public interface ITaskList extends ICommonKey, IStatus, IPath, IPage, IKey {
//		param：参数
//		{prepare预发布,under-way进行中,
//				not-started未开始，finished已完成}
//		uid:用户id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/task/index&access-token=";// 路径
		String PARAM = "param";
		String UID = "uid";
		String RESEID = "reseid";
		String TYPES = "types";
	}



	/**
	 *开始任务（请求方式POST）
	 *http://xjapi.0791jr.com/?r=task/task/update&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月2日
	 */
	public interface IStartTask extends ICommonKey, IStatus, IPath, IPage, IKey {
//      task_id：任务id
//		uid:用户id
//		param:参数（start开始，finished:完成)
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/task/update&access-token=";// 路径
		String TASK_ID = "task_id";
		String UID = "uid";
		String PARAM = "param";
	}
	/**
	 *任务详情（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/task/view&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月2日
	 */
	public interface ITaskDetails extends ICommonKey, IStatus, IPath, IPage, IKey {
//		task_id：任务id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/task/view&access-token=";// 路径
		String TASK_ID = "task_id";
	}

	/**
	 *项目检查（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/task/point&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月2日
	 */
	public interface ITaskCheck extends ICommonKey, IStatus, IPath, IPage, IKey {
		//		reseid:水库id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/task/point&access-token=";// 路径
		String RESEID = "reseid";
		String ROUTE_ID = "routeid";
	}

	/**
	 *检查内容（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/task/content&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月2日
	 */
	public interface ITaskItemContent extends ICommonKey, IStatus, IPath, IPage, IKey {
		//		itemid:检查项
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/task/content&access-token=";// 路径
		String ITEMID = "itemid";
	}

	/**
	 *城市区域（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/area/index&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月15日
	 */
	public interface ITaskCity extends ICommonKey, IStatus, IPath, IPage, IKey {
		//		id:区域id（默认0）
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/area/index&access-token=";// 路径
		String ID = "id";
	}
	/**
	 *登录（请求方式POST）
	 *http://xjapi.0791jr.com/?r=user/login/login&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月15日
	 */
	public interface ITaskLogin extends ICommonKey, IStatus, IPath, IPage, IKey {
		//		username:用户名称
//		password:用户密码
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=user/login/login&access-token=";// 路径
		String USERNAME = "username";
		String PASSWORD = "password";
	}
	/**
	 *版本升级（请求方式GET）
	 *http://xjapi.0791jr.com/?r=user/version/index&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月15日
	 */
	public interface IUpdataVersion extends ICommonKey, IStatus, IPath, IPage, IKey {
//		name:app版本名称（不得含中文）
//		number:版本号
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=user/version/index&access-token=";// 路径
		String NAME = "name";
		String NUMBER = "number";
	}
	/**
	 *通讯录（请求方式GET）
	 *http://xjapi.0791jr.com/?r=user/phone/index&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月15日
	 */
	public interface IPhoneList extends ICommonKey, IStatus, IPath, IPage, IKey {
		//		name:app版本名称（不得含中文）
//		number:版本号
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=user/phone/index&access-token=";// 路径
	}

	/**
	 *保存任务报告pdf（请求方式POST）
	 *http://xjapi.0791jr.com/?r=task/check-result/update&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月15日
	 */
	public interface ISavePdf extends ICommonKey, IStatus, IPath, IPage, IKey {
//		uid:用户id
//		task_id:任务id
//		pdf:任务报告地址
//		trails:{json格式数据}经纬度
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/check-result/update&access-token=";// 路径
		String TASK_ID = "task_id";
		String PDF = "pdf";
		String TRAILS = "trails";
	}
	/**
	 *保存检查项（请求方式POST）
	 *http://xjapi.0791jr.com/?r=task/check-result/insert&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年11月15日
	 */
	public interface ISaceCheck extends ICommonKey, IStatus, IPath, IPage, IKey {
//		uid:用户id
//		images:（图片路径，多个逗号隔开）
//		videos:（视频路径，多个逗号隔开）
//		audios:（音频路径，多个逗号隔开）
//		task_id:任务id
//		itemid:检查项id
//		remark:备注说明
//		normal:是否异常
//		work:设备运转
//		repeat:是否重复
//		question:异常问题
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/check-result/insert&access-token=";// 路径
		String UID = "uid";
		String IMAGES = "images";
		String VIDEOS = "videos";
		String AUDIOS = "audios";
		String TASK_ID = "task_id";
		String ITEMID = "itemid";
		String REMARK = "remark";
		String NORMAL = "normal";
		String WORK = "WORK";
		String REPEAT = "repeat";
		String QUESTION = "question";
	}

	/**
	 *观测物（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/item-setting/index&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface IObservingThings extends ICommonKey, IStatus, IPath, IPage, IKey {
		//reseid:水库id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/item-setting/index&access-token=";// 路径
		String RESEID = "reseid";
	}
	/**
	 *巡检点设置/添加（请求方式POST）
	 *http://xjapi.0791jr.com/?r=task/location/insert&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface IAddCheckingPoint extends ICommonKey, IStatus, IPath, IPage, IKey {
//		itemids:观测物id(多个逗号隔开)
//		title:点位名称
//		idcard:标示卡号
//		xpoint:经度
//		ypoint:纬度
//		address:地址
//		reseid:水库id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/location/insert&access-token=";// 路径
		String ITEMIDS = "itemids";
		String TITLE = "title";
		String IDCARD = "idcard";
		String XPOINT = "xpoint";
		String YPOINT = "ypoint";
		String ADDRESS = "address";
		String RESEID = "reseid";
	}
	/**
	 *巡检点内容（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/location/index&access-token=xxxx
	 * @version 1.0
	 * @date 2017年12月2日
	 */
	public interface ICheckingPointContent extends ICommonKey, IStatus, IPath, IPage, IKey {
		//reseid:水库id
//		id:点位id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/location/index&access-token=";// 路径
		String RESEID = "reseid";
		String ID = "id";
	}
	/**
	 *巡检点修改（请求方式POST）
	 *http://xjapi.0791jr.com/?r=task/location/update&access-token=xxxx
	 * @version 1.0
	 * @date 2017年12月2日
	 */
	public interface IUpdataCheckingPoint extends ICommonKey, IStatus, IPath, IPage, IKey {
		//		itemids:观测物id(多个逗号隔开)
//		title:点位名称
//		idcard:标示卡号
//		xpoint:经度
//		ypoint:纬度
//		address:地址
//		reseid:水库id
//		id:点位id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/location/update&access-token=";// 路径
		String ITEMIDS = "itemids";
		String TITLE = "title";
		String IDCARD = "idcard";
		String XPOINT = "xpoint";
		String YPOINT = "ypoint";
		String ADDRESS = "address";
		String RESEID = "reseid";
		String ID = "id";
	}
	/**
	 *巡检点列表（请求方式GET）
	 *http://xjapi.0791jr.com/?r=task/location/list&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface ICheckingPointList extends ICommonKey, IStatus, IPath, IPage, IKey {
		//reseid:水库id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/location/list&access-token=";// 路径
		String RESEID = "reseid";
	}
	/**
	 *获取用户资料（请求方式GET）
	 *http://xjapi.0791jr.com/?r=user/user/index&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface IGetUser extends ICommonKey, IStatus, IPath, IPage, IKey {
		//uid:用户id
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=user/user/index&access-token=";// 路径
		String UID = "uid";
	}
	/**
	 *修改密码（请求方式POST）
	 *http://xjapi.0791jr.com/?r=user/login/update&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface IUpadatePW extends ICommonKey, IStatus, IPath, IPage, IKey {
//		uid:用户id
//		oldPassword:旧密码
//		newPassword:新密码
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=user/login/update&access-token=";// 路径
		String UID = "uid";
		String OLDPASSWORD = "oldPassword";
		String NEWPASSWORD = "newPassword";
	}
	/**
	 *修改用户资料（请求方式POST）
	 *http://xjapi.0791jr.com/?r=user/user/update&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface IUpadateInfo extends ICommonKey, IStatus, IPath, IPage, IKey {
//		id:用户id
//		username:用户名称(账号名称，不可以修改)
//		realname:用户姓名
//		avatar:用户头像
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=user/user/update&access-token=";// 路径
		String ID = "id";
		String REALNAME = "realname";
		String AVATAR = "avatar";
	}
	/**
	 *结束当前点（请求方式POST）
	 *http://xjapi.0791jr.com/?r=task/location/status&access-token=xxxx
	 * @author xjh
	 * @version 1.0
	 * @date 2017年12月1日
	 */
	public interface IEndLoaction extends ICommonKey, IStatus, IPath, IPage, IKey {
//		id:点位id
//		param:signup签到，
//		finished结束当前点，
//		deviation 位置是否偏离
		String IURL = HttpUtil.BASE_URL(AppContext.getContext()) + "r=task/location/status&access-token=";// 路径
		String ID = "id";
		String FINISHED = "finished";
		String DEVIATION = "deviation";
	}

}
