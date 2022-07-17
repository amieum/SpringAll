package yuriua.es.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author Yuriua
 * @since 1.0
 */
public class Doc_Get {
    public static void main(String[] args) throws Exception {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150", 9200))
        );
        //获取数据
        GetRequest request = new GetRequest();
        request.index("user").id("10001");

        GetResponse response = esClient.get(request, RequestOptions.DEFAULT);

        System.out.println(response.getSourceAsString());
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(response.getSourceAsString(), new TypeReference<User>() {
        });
        System.out.println(user);

        //关闭es客户端
        esClient.close();
    }
}
