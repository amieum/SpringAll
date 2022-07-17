package yuriua.es.test;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * @author Yuriua
 * @since 1.0
 */
public class Doc_Insert_Batch {
    public static void main(String[] args) throws Exception{
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150",9200))
        );
        //批量插入数据
        BulkRequest request = new BulkRequest();

        IndexRequest request1 = new IndexRequest().index("user").id("10001")
                .source(XContentType.JSON, "name", "克莱因", "sex", "男", "age", "24");
        IndexRequest request2 = new IndexRequest().index("user").id("10002")
                .source(XContentType.JSON, "name", "诗乃", "sex", "女", "age", "16");
        IndexRequest request3 = new IndexRequest().index("user").id("10003")
                .source(XContentType.JSON, "name", "莉法", "sex", "女", "age", "14");
        IndexRequest request4 = new IndexRequest().index("user").id("10004")
                .source(XContentType.JSON, "name", "亚丝娜", "sex", "女", "age", "17");
        IndexRequest request5 = new IndexRequest().index("user").id("10005")
                .source(XContentType.JSON, "name", "西莉卡", "sex", "女", "age", "11");
        IndexRequest request6 = new IndexRequest().index("user").id("10006")
                .source(XContentType.JSON, "name", "艾基尔", "sex", "男", "age", "35");
        IndexRequest request7 = new IndexRequest().index("user").id("10007")
                .source(XContentType.JSON, "name", "香风智乃", "sex", "女", "age", "14");
        IndexRequest request8 = new IndexRequest().index("user").id("10008")
                .source(XContentType.JSON, "name", "春日野穹", "sex", "女", "age", "15");
        IndexRequest request9 = new IndexRequest().index("user").id("10009")
                .source(XContentType.JSON, "name", "五更琉璃", "sex", "男", "age", "15");
        IndexRequest request10 = new IndexRequest().index("user").id("10010")
                .source(XContentType.JSON, "name", "小鸟游六花", "sex", "女", "age", "14");
        IndexRequest request11 = new IndexRequest().index("user").id("10011")
                .source(XContentType.JSON, "name", "毛利小五郎", "sex", "男", "age", "35");
        IndexRequest request12 = new IndexRequest().index("user").id("10012")
                .source(XContentType.JSON, "name", "Flandre Scarlet", "sex", "女", "age", "495");
        IndexRequest request13 = new IndexRequest().index("user").id("10013")
                .source(XContentType.JSON, "name", "Remilia Scarlet", "sex", "女", "age", "500");
        IndexRequest request14 = new IndexRequest().index("user").id("10014")
                .source(XContentType.JSON, "name", "wangwu1", "sex", "男", "age", "22");
        IndexRequest request15 = new IndexRequest().index("user").id("10015")
                .source(XContentType.JSON, "name", "wangwu12", "sex", "女", "age", "17");
        IndexRequest request16 = new IndexRequest().index("user").id("10016")
                .source(XContentType.JSON, "name", "wangwu123", "sex", "男", "age", "28");

        request.add(
                request1,request2,request3,request4,request5, request6,request7,request8, request9,
                request10,request11,request12,request13,request14,request15,request16
        );

        BulkResponse responses = esClient.bulk(request, RequestOptions.DEFAULT);

        System.out.println(responses.status().getStatus());
        System.out.println(responses.getItems());
        System.out.println(responses.iterator().next());
        System.out.println(responses);


        //关闭es客户端
        esClient.close();
    }
}
