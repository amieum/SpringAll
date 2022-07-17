package yuriua.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author Yuriua
 * @since 1.0
 */
public class Doc_Delete {
    public static void main(String[] args) throws Exception {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150", 9200))
        );
        //删除数据
        DeleteRequest request = new DeleteRequest();
        request.index("user").id("10001");

        DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);

        System.out.println(response);
        System.out.println(response.status());
        System.out.println(response.getResult());

        //关闭es客户端
        esClient.close();
    }
}
