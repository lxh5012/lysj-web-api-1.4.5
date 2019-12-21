package com.authine.cloudpivot.ext.controller;

import com.authine.cloudpivot.ext.domain.Wl;
import com.authine.cloudpivot.ext.service.BaseService;
import com.authine.cloudpivot.ext.util.LicenseFactory;
import com.authine.cloudpivot.web.api.controller.base.RuntimeController;
import com.authine.cloudpivot.web.api.controller.login.HttpClientHelper;

import com.authine.cloudpivot.web.api.view.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author boliy 2019/11/6 11:56
 */
@Api(value = "深高速定制接口", tags = "深高速定制接口::对接::系统外链接口")
@Controller
@RequestMapping("/api/docking")
public class DockingController extends RuntimeController {
    private static final Logger log = LoggerFactory.getLogger(DockingController.class);
    @Value("${customize.sso.publicKey}")
    public String publicKey;
    @Value("${customize.sso.oauthUrl}")
    public String oauthUrl;
    @Value("${customize.sso.systemUrl}")
    public String systemUrl;
    @Value("${customize.sso.homeUrl}")
    public String homeUrl;

    @Autowired
    private BaseService baseService;

    /**
     * http://120.79.243.139/login?sign=aaaaaaa&u=bbbbbbb&t=timestamp&callid=2222222222&ani=13900000000
     * 说明：
     * 1、sign ： 该值由呼叫系统使用公钥封装
     * 2、u    :  该值为用户名或其他约定字段值
     * 3、t    :  该值为发送请求时的时间戳值
     * 4、callid: 该通话的通话ID
     * 5、ani  ： 该通话的主叫号码（即来电手机号码）,该号码使用3des加密算法加密
     * <p>
     * * 呼叫系统生成一对秘钥对，公钥存呼叫系统做签名加密，私钥存放工单系统做签名解密认证
     * * 创建工单时包含传送以上5个参数
     * * 查询工单时传送1、2、3，前3位参数
     *
     * @param userName
     * @return
     */
    @ApiOperation("第三方系统外链接口")
    @ResponseBody
    @GetMapping("/embed")
    public void embed(@RequestParam("sign") String sign,
                      @RequestParam(value = "u") String userName,
                      @RequestParam(value = "t", required = false) String timestamp,
                      @RequestParam(value = "callid", required = false) String callid,
                      @RequestParam("ani") String ani,
                      @RequestParam("formCode") String formCode,
                      @RequestParam("formType") String formType,
                      HttpServletRequest request,
                      HttpServletResponse response
    ) throws Exception {
        try {
            if (StringUtils.isNoneBlank(userName)
                    && StringUtils.isNoneBlank(timestamp)
//                    && LicenseFactory.verify((userName + timestamp).getBytes(), publicKey, sign)
) {
                StringBuilder redirectUri = new StringBuilder(systemUrl)
                        .append("/newlogin")
                        /**编码*/
                        .append("?formCode=" + formCode)
                        /**类型*/
                        .append("&formType=" + formType)
                        /**第三方接口参数该通话的通话ID*/
                        .append("&callId=" + callid)
                        /**第三方接口参数该通话的主叫号码*/
                        .append("&ani=" + ani);
                redirectUrl(userName, redirectUri, response);
            }
        } catch (Exception e) {
            log.error("get oauth code error.", e);
        }
        response.sendRedirect(URLDecoder.decode(systemUrl + "/error", "utf-8"));
    }

    @ApiOperation("获取Token")
    @GetMapping({"/getToken"})
    @ApiImplicitParams({@ApiImplicitParam(
            name = "url",
            value = "用于刷新token的url",
            defaultValue = "http://ip地址或域名/api",
            required = true,
            dataType = "String",
            paramType = "path"
    ), @ApiImplicitParam(
            name = "code",
            value = "授权码",
            required = true,
            dataType = "String",
            paramType = "path"
    ), @ApiImplicitParam(
            name = "clientSecret",
            value = "客户端密码",
            defaultValue = "c31b32364ce19ca8fcd150a417ecce58",
            required = true,
            dataType = "String",
            paramType = "path"
    ), @ApiImplicitParam(
            name = "clientId",
            value = "客户端id",
            defaultValue = "api",
            required = true,
            dataType = "String",
            paramType = "path"
    ), @ApiImplicitParam(
            name = "redirectUri",
            value = "重定向URI",
            defaultValue = "http://ip地址或域名/oauth",
            required = true,
            dataType = "String",
            paramType = "path"
    )})
    @ResponseBody
    public ResponseResult<Map> getToken(String url, String code, String clientSecret, String clientId, String redirectUri, String formCode, String formType) {
        Map<String, String> map = new HashMap();
        map.put("url", url);
        map.put("client_secret", clientSecret);
        map.put("redirect_uri", redirectUri);
        map.put("client_id", clientId);
        map.put("grant_type", "authorization_code");
        map.put("code", code);
        Map result = HttpClientHelper.getOauthToken(map);
        if (!ObjectUtils.isEmpty(result)) {
            result.put("success", true);
            if (StringUtils.isNoneBlank(formCode) && StringUtils.isNoneBlank(formType)) {
                Wl wl = baseService.selectWl(formCode, formType);
                if (!ObjectUtils.isEmpty(wl)) {
                    result.put("targetUrl", systemUrl + wl.getGxbUrl());
                } else {
                    result.put("targetUrl", "");
                }
            } else {
                result.put("targetUrl", systemUrl + homeUrl);
            }
            return getOkResponseResult(result, "获取Token数据成功！");
        }
        return getOkResponseResult(null, "获取Token数据失败！");
    }

    /**
     * http://120.79.243.139/login?sign=aaaaaaa&u=bbbbbbb&t=timestamp&callid=2222222222&ani=13900000000
     * 说明：
     * 1、sign ： 该值由呼叫系统使用公钥封装
     * 2、u    :  该值为用户名或其他约定字段值
     * 3、t    :  该值为发送请求时的时间戳值
     * 4、callid: 该通话的通话ID
     * 5、ani  ： 该通话的主叫号码（即来电手机号码）,该号码使用3des加密算法加密
     * <p>
     * * 呼叫系统生成一对秘钥对，公钥存呼叫系统做签名加密，私钥存放工单系统做签名解密认证
     * * 创建工单时包含传送以上5个参数
     * * 查询工单时传送1、2、3，前3位数参
     *
     * @param userName
     * @return
     */
    @ApiOperation("第三方系统外链:智慧客服工单系统")
    @GetMapping("/embed/home")
    @ResponseBody
    public void embed(@RequestParam(value = "u") String userName,
                      HttpServletRequest request,
                      HttpServletResponse response
    ) throws Exception {
        try {
            redirectUrl(userName, new StringBuilder(systemUrl).append("/newlogin?t=1"), response);
        } catch (Exception e) {
            log.error("get oauth code error.", e);
        }
        response.sendRedirect(URLDecoder.decode(systemUrl + "/error", "utf-8"));
    }

    private void redirectUrl(String userName, StringBuilder url, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap();
//        map.put("username",baseService.getMapUserName(userName));
        map.put("username","15220089930");
        map.put("portal", "true");
        map.put("User-Agent", request.getHeader("User-Agent"));
        map.put("client_ip", request.getRemoteAddr());

        Map<String, Object> result = HttpClientHelper.getAouthationPost(URLDecoder.decode(oauthUrl, "utf-8"), map);
        if (!ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.get("errcode"))) {
            String code = result.get("errcode").toString();
            if ("200".equalsIgnoreCase(code)) {
                url.append("&code=" + result.get("code"));
                response.sendRedirect(URLDecoder.decode(url.toString(), "utf-8"));
                return;
            } else {
                log.error("get oauth code error.", result.get("errmsg"));
            }
        }
    }
}