package yuriua.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yuriua
 * @since 1.0
 */
public class Doc_Delete_Batch {
    static class MyDeleteRequest extends DeleteRequest implements Cloneable{
        @Override
        public MyDeleteRequest clone() throws CloneNotSupportedException {
            return (MyDeleteRequest) super.clone();
        }
        //批量生成DeleteRequest
        public MyDeleteRequest[] ids(String index,String... ids){
            super.index = index;
            List<MyDeleteRequest> result = new ArrayList<>();
            if (null == ids || ids.length == 0){
                MyDeleteRequest[] temp = {this};
                return temp;
            }
            for (String id : ids) {
                super.id(id);
                try {
                    result.add(this.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            MyDeleteRequest[] requests = new MyDeleteRequest[result.size()];
            int idx = 0;
            for (Iterator<MyDeleteRequest> iter = result.iterator(); iter.hasNext();){
                requests[idx++] = iter.next();
            }
            return requests;
        }
    }
    public static void main(String[] args) throws Exception {
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150", 9200))
        );
        //批量删除数据
        BulkRequest request = new BulkRequest();

        MyDeleteRequest dr = new MyDeleteRequest();
        MyDeleteRequest[] deleteRequests = dr.ids("user", "10001", "10002", "10003");

        request.add(deleteRequests);

        BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);

        System.out.println(response.status().getStatus());

        //关闭es客户端
        esClient.close();
    }
}
