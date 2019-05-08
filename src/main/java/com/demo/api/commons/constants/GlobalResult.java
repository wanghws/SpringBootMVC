package com.demo.api.commons.constants;

/**
 * Created by wanghw on 2019-03-08.
 */
public class GlobalResult {
	public final static String SUCCESS = "0";
	public final static String NOT_LOGIN = "300";
	public final static String FAIL = "500";
	public final static String PAGE_LIMIT = "501";
	public final static String LOGIN_FAIL = "401";
	public final static String ACCESS_DENIED = "403";


	public final static String SYS_OFFICE_NOT_FOUND = "80001";//系统部门记录未找到
	public final static String SYS_NAME_NOT_NULL = "80002";//系统 名字不能为空
	public final static String SYS_OFFICE_ID_NOT_NULL = "80003";//系统部门id不能为空
	public final static String SYS_STATUS_NOT_NULL = "80004";//系统状态不能为空
	public final static String SYS_STATUS_INVALID = "80005";//系统 状态不正确
	public final static String SYS_PERMISSION_NOT_NULL = "80006";//权限关键字不能为空
	public final static String SYS_URL_NOT_NULL = "80007";//权限url不能为空
	public final static String SYS_PERMISSION_ID_NOT_NULL = "80008";//权限id不能为空
	public final static String SYS_PERMISSION_RECORD_NOT_FOUND = "8009";//权限记录未找到
	public final static String SYS_ROLE_NOT_FOUND = "80010";//角色记录未找到
	public final static String SYS_ROLE_ID_NOT_NULL = "80011";//角色id不能为空
	public final static String SYS_ROLE_PERMISSION_NOT_FOUND = "80012";//角色权限关系记录未找到
	public final static String SYS_ROLE_PERMISSION_ID_NOT_NULL = "80013";//角色权限关系id不能为空
	public final static String SYS_NAME_NOT_REPEAT = "80014";//登录名已存在
	public final static String SYS_MOBILE_NOT_REPEAT = "80015";//手机号已存在
	public final static String SYS_EMAIL_NOT_REPEAT = "80016";//邮箱已存在
	public final static String SYS_PASSWORD_NOT_NULL = "80017";//密码不能为空
	public final static String SYS_USER_ID_NOT_NULL = "80018";//系统用户ID不能为空
	public final static String SYS_OFFICE_STATUS_INVALID = "80019";//系统部门状态不正确
	public final static String SYS_USER_NOT_FOUND = "80020";//系统用户信息未找到
	public final static String SYS_USER_STATUS_INVALID = "80021";//系统用户状态不正确
	public final static String SYS_USER_ROLE_NOT_FOUND = "80022";//系统用户角色记录未找到
	public final static String SYS_PERMISSION_REPEAT = "80023";//该权限关键字已经存在
	public final static String SYS_URL_REPEAT = "80024";//该URL已经存在
	public final static String SYS_USER_ROLE_ID_NOT_NULL = "80027";//用户角色关系ID不能为空

}
