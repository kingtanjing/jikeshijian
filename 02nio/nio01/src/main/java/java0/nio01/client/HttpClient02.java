package java0.nio01.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient02 {
    private static final int SUCCESS_CODE = 200;
    public static String sendGet(HttpGet httpGet) throws Exception {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            /**
             * 创建HttpClient对象
             */
            client = HttpClients.createDefault();
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
