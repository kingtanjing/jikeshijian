package java0.nio01.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;


public class HttpClient01 {

    private static final int SUCCESS_CODE = 200;

    public static void main(String[] args) throws Exception{
        String url = "http://127.0.0.1:8808/";
        String result = sendGet(url);
        System.out.println(result);
    }

    public static String sendGet(String url) throws Exception {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            /**
             * 创建HttpClient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建URIBuilder
             */
            URIBuilder uriBuilder = new URIBuilder(url);
            /**
             * 创建HttpGet
             */
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            /**
             * 设置请求头部编码
             */
            httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            /**
             * 设置返回编码
             */
            httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            /**
             * 请求服务
             */
            response = client.execute(httpGet);
            /**
             * 获取响应吗
             */
            int statusCode = response.getStatusLine().getStatusCode();

            if (SUCCESS_CODE == statusCode) {
                /**
                 * 获取返回对象
                 */
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(entity, "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                try {
                    return result;
                } catch (Exception e) {
                    return result;
                }
            } else {
                System.out.println("HttpClientService-line: {}, errorMsg{},GET请求失败！");
            }
        } catch (Exception e) {
            System.out.println("Exception: {}"+ e);
        } finally {
            response.close();
            client.close();
        }
        return null;
    }


}
