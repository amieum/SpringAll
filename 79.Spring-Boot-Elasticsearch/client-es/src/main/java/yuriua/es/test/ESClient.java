package yuriua.es.test;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author Yuriua
 * @since 1.0
 */
public class ESClient {
    public static void main(String[] args) throws Exception{
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150",9200))
        );

        //关闭es客户端
        esClient.close();
    }
}
