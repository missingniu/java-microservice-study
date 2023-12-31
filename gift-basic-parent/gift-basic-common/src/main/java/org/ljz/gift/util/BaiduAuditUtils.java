package org.ljz.gift.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百度认证工具类：注意要修改API Key 和 Secret Key
 */
public class BaiduAuditUtils {
    /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.3265383f84ac64db9eff781e70587614.2592000.1654844607.282335-26205415",
     * "expires_in": 2592000
     * }
     */
    public static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "YENQvtXlXWjOEqvAE4RwPZAv";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "DhZPIHdV0aAFmFAQDH2aQ0jjqTqL8SxB";
        return getAuth(clientId, clientSecret);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
           /* for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            //System.err.println("result:" + result);
            JSONObject jsonObject= JSONObject.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            //System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 文本审核
     * @param param
     * @return
     */
    public static Map<String,Object> textCensor(String param) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();
            //处理参数格式
            param = "text=" + param;

            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject= JSONObject.parseObject(result);
            String conclusion = jsonObject.getString("conclusion");
            Map<String,Object> map =  new HashMap<>();
            if ("合规".equals(conclusion)){
                map.put("success",true);
                map.put("message","合规");
                return map;
            }else {
                String dataStr =  jsonObject.getString("data");
                JSONArray jsonArray = JSONObject.parseArray(dataStr);
                String errorMsg= jsonArray.getJSONObject(0).getString("msg");
                map.put("success",false);
                map.put("message",errorMsg);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @param imageUrl 需要审核的图片路径地址
     * @return
     */
    public static Map<String, Object> imgCensor(String imageUrl) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        try {
            //图片地址进行编码
            String imgParam = URLEncoder.encode(imageUrl, "UTF-8");
            //设置参数
            String param = "imgUrl=" + imgParam;
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonObject= JSONObject.parseObject(result);
            String conclusion = jsonObject.getString("conclusion");
            Map<String,Object> map =  new HashMap<>();
            if ("合规".equals(conclusion)){
                map.put("success",true);
                map.put("message","合规");
                return map;
            }else
            {
                String dataStr =  jsonObject.getString("data");
                JSONArray jsonArray = JSONObject.parseArray(dataStr);
                String errorMsg= jsonArray.getJSONObject(0).getString("msg");
                map.put("success",false);
                map.put("message",errorMsg);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//审核工具类
    public static Map<String, Object> censor(String content,String imageUrl){
        String errorMsg = "";
        //验证文本，记录不通过的原因
        if(StringUtils.isNotBlank(content)){
            Map<String, Object> objectMap = textCensor(content);
            Boolean success = (Boolean) objectMap.get("success");
            if(!success){
                errorMsg+= content+"审核不通过原因,原因"+objectMap.get("message").toString();
            }
        }
        //校验图片，图片审核不通过，记录审核不通过的原因
        if(StringUtils.isNotBlank(imageUrl)){
            String[] split = imageUrl.split(",");
            for (String s:split) {
                Map<String, Object> objectMap = imgCensor(s);
                Boolean success =(Boolean) objectMap.get("success");
                if(!success){
                    errorMsg += "图片"+s +"审核不通过原因:"+objectMap.get("message").toString();
                }

            }

        }
        //文本和图片如果都审核通过就返回true,但是如果有一个审核不通过，则说明审核不通过
        Map<String,Object> map = new HashMap<>();
        if(!StringUtils.isNotBlank(errorMsg)){
            map.put("success",true);
            map.put("message","审核成功");
        }else{
            map.put("success",false);
            map.put("message",errorMsg);
        }
        return map;
    }


}