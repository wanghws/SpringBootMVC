package com.demo.api.commons.utils;

import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tady.hb on 2018/8/10.
 */
public class WebUtils {

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个反向代理IP时，取第一个
        int commaOffset = ip.indexOf(',');
        if (commaOffset > 0) {
            ip = ip.substring(0, commaOffset);
        }
        if (ip.split("\\.").length != 4 ){
            return "127.0.0.1";
        }
        return ip;
    }

    public static String getInternalIp() {
        try {
            InetAddress addr = getLocalHostAddress();
            if (null==addr)return "";
            return addr.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static InetAddress getLocalHostAddress() {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface
                    .getNetworkInterfaces(); nis.hasMoreElements();) {
                NetworkInterface ni = nis.nextElement();
                if (ni.isLoopback() || ni.isVirtual() || !ni.isUp())
                    continue;
                for (Enumeration<InetAddress> ias = ni.getInetAddresses(); ias.hasMoreElements();) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet6Address) continue;
                    return ia;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int ipToInt(HttpServletRequest request) {
        IPv4Address iPv4Address = new IPAddressString(getClientIp(request)).getAddress().toIPv4();
        return iPv4Address.intValue();
    }
    public static int ipToInt(String ip) {
        IPv4Address iPv4Address = new IPAddressString(ip).getAddress().toIPv4();
        return iPv4Address.intValue();
    }
    public static String intToIp(int ip) {
        IPv4Address iPv4Address = new IPv4Address(ip);
        return iPv4Address.toString();
    }


    public static boolean JudgeIsMobile(HttpServletRequest request) {
        boolean isMobile = false;
        String[] mobileAgents = {"iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi", "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod", "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma", "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos", "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem", "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos", "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320", "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-", "Googlebot-Mobile"};
        if (request.getHeader("User-Agent") != null) {
            String agent = request.getHeader("User-Agent").toLowerCase();
            for (String mobileAgent : mobileAgents) {
                if (agent.indexOf(mobileAgent) >= 0 && agent.indexOf("windows nt") <= 0 && agent.indexOf("macintosh") <= 0) {
                    isMobile = true;
                    break;
                }
            }
        }
        return isMobile;
    }

    public static String getDomain(HttpServletRequest httpServletRequest, String pcDomain, String mobileDomain, String protocol) {
        boolean isMobile = JudgeIsMobile(httpServletRequest);
        if (StringUtils.isBlank(protocol)) {
            protocol = "http";
        }
        if (isMobile) {
            return protocol + "://" + mobileDomain;
        } else {
            return protocol + "://" + pcDomain;
        }
    }

    public static boolean isMobile(String str) {
        if (org.springframework.util.StringUtils.isEmpty(str))return false;
        boolean b = false;
        if(str.length()!=11)return b;

        Pattern p = null;
        Matcher m = null;

        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isEmail(String email){
        if (org.springframework.util.StringUtils.isEmpty(email)) return false;

        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public static boolean checkPatten(String str, String patten) {
        Pattern p =  Pattern.compile(patten);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 字符串首字母大写
     * @param str
     * @return
     */
    public static String toUpperCaseFirstOne(String str) {
        if (str == null || "".equals(str))
            return str;
        if (Character.isUpperCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1))
                    .toString();
    }

}
