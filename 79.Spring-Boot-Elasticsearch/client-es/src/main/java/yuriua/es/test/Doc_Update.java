package yuriua.es.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * @author Yuriua
 * @since 1.0
 */
public class Doc_Update {
    public static void main(String[] args) throws Exception{
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150",9200))
        );
        //修改数据
        UpdateRequest request = new UpdateRequest();
        request.index("user").id("10001");
        User user = new User("王琪雪","女",18);
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);
        request.doc(userJson,XContentType.JSON);

        UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        System.out.println(response);
        System.out.println(result);

        //关闭es客户端
        esClient.close();
    }
}
