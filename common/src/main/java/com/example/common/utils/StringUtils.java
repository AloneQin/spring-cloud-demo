package com.example.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {
	
	/**
	 * 正则匹配用户名
	 * ^[a-zA-z][a-zA-Z0-9_]{6,20}$: 字母、数字、下划线、必须以字母开头、长度 6-20 位
	 * 
	 * @param userName 用户名
	 * @return true or false
	 * 
	 * 链接：
	 * http://www.codeceo.com/article/useful-regular-expression.html
	 * http://www.jb51.net/article/72867.htm
	 */
	public static boolean isUserName(String userName) {
		if (userName == null) {
			return false;
		}
		Pattern p = Pattern.compile("^[a-zA-z][a-zA-Z0-9_]{6,20}$");
        Matcher m = p.matcher(userName);
        return m.matches();
	}
	
	/**
	 * 正则匹配邮箱
	 * @param email 邮箱
	 * @return true or false
	 */
	public static boolean isEmail(String email) {
		if (email == null) {
			return false;
		}
		Pattern p = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher m = p.matcher(email);
        return m.matches();
	}
	
	/**
	 * 正则匹配手机
	 * @param mobile 手机
	 * @return true or false
	 */
	public static boolean isMobile(String mobile) {
		if (mobile == null) {
			return false;
		}
		Pattern p = Pattern.compile("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
	}
	
	/**
	 * 正则匹配密码：必须包含大小写字母和数字的组合，不能使用特殊字符，长度 6-20 位
	 * @param password 密码
	 * @return true or false
	 */
	public static boolean isPassword(String password) {
		if (password == null) {
			return false;
		}
		Pattern p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$");
        Matcher m = p.matcher(password);
        return m.matches();
	}

	/**
	 * 字符串判空
	 * @param str 待判定字符串
	 * @return true or false
	 */
	public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

	/**
	 * 字符串判非空
	 * @param str 待判定字符串
	 * @return true or false
	 */
	public static boolean nonEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 字符串判空，包含全空格
	 * @param str 待判定字符串
	 * @return true or false
	 */
	public static boolean isBlank(String str) {
		return isEmpty(str) || str.trim().isEmpty();
	}

	/**
	 * 字符串判非空，包含全非空格
	 * @param str 待判定字符串
	 * @return true or false
	 */
	public static boolean nonBlank(String str) {
        return !isBlank(str);
    }

	/**
	 * 格式化sql，去除模板引擎中的制表符\t与换行符\n
	 */
	public static String formatSql(String sql) {
		return sql.replace("\n", "").replace("\t", "");
	}

}
