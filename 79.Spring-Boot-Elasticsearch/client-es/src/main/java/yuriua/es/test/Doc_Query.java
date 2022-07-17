package yuriua.es.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 1.全量查询 2.条件查询 3.分页查询 4.查询排序 5.过滤字段 6.组合查询
 * 7.范围查询 8.模糊查询 9.高亮查询 10.聚合查询
 *
 * @author Yuriua
 * @since 1.0
 */
public class Doc_Query {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        //创建es客户端
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.0.150", 9200))
        );

        SearchRequest request = new SearchRequest();//查询请求
        request.indices("user");//指定查询的索引集（数据库）

        //1.全量查询：matchAllQuery
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        //SearchSourceBuilder相当于最外面的大括号
        SearchSourceBuilder MATCH_ALL_QUERY = new SearchSourceBuilder().query(matchAllQueryBuilder);

        //2.条件查询：termQuery
        SearchSourceBuilder TERM_QUERY = new SearchSourceBuilder().query(QueryBuilders.termQuery("sex", "女"));

        //3.分页查询
        SearchSourceBuilder TERM_QUERY_PAGING = new SearchSourceBuilder().query(QueryBuilders.termQuery("sex", "女"));
        //(当前页码-1)*每页显示数据条数
        TERM_QUERY_PAGING.from(2);
        TERM_QUERY_PAGING.size(2);

        //4.查询排序
        SearchSourceBuilder TERM_QUERY_SORT = new SearchSourceBuilder().query(QueryBuilders.termQuery("sex", "女"));
        TERM_QUERY_SORT.sort("age", SortOrder.DESC);

        //5.过滤字段
        SearchSourceBuilder MATCH_ALL_QUERY_FETCH_SOURCE = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        String[] include = {};//包含字段
        String[] exclude = {"age"};//排除字段
        MATCH_ALL_QUERY_FETCH_SOURCE.fetchSource(include,exclude);

        //6.组合查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 交集 &&
//        boolQueryBuilder.must(QueryBuilders.matchQuery("age",14));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("sex","女"));
        // 并集 ||
        boolQueryBuilder.should(QueryBuilders.matchQuery("age","14"));
        boolQueryBuilder.should(QueryBuilders.matchQuery("name","五更琉璃"));

        SearchSourceBuilder BOOL_QUERY = new SearchSourceBuilder().query(boolQueryBuilder);

        //7.范围查询
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.gte("30");
        rangeQueryBuilder.lte("40");
        SearchSourceBuilder RANGE_QUERY = new SearchSourceBuilder().query(rangeQueryBuilder);

        //8.模糊查询
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "wangwu").fuzziness(Fuzziness.TWO);
        SearchSourceBuilder FUZZY_QUERY = new SearchSourceBuilder().query(fuzzyQueryBuilder);

        //9.高亮查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "莉");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>").postTags("</font>").field("name");

        SearchSourceBuilder HIGH_LIGHT_QUERY = new SearchSourceBuilder();//SearchSourceBuilder相当于最外面的大括号
        HIGH_LIGHT_QUERY.query(termQueryBuilder);
        HIGH_LIGHT_QUERY.highlighter(highlightBuilder);

        //10.聚合查询
        SearchSourceBuilder AGG_QUERY = new SearchSourceBuilder();//SearchSourceBuilder相当于最外面的大括号
        AGG_QUERY.aggregation(AggregationBuilders.terms("age_groupby").field("age.keyword"));


        //发送查询请求
        request.source(AGG_QUERY);//<-在这里指定查询生成器
        SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String json = searchHit.getSourceAsString();
            User user = mapper.readValue(json, User.class);
            System.out.println("原始JSON："+json+" - "+"对象："+user);
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            //System.out.println(highlightFields.get("name").getFragments());//高亮
        }

        Terms age_groupby = response.getAggregations().get("age_groupby");
        String name = age_groupby.getName();
        List<String> collect = age_groupby.getBuckets().stream().map(o -> o.getKeyAsString()).collect(Collectors.toList());
        System.out.println(name+" "+collect);


        System.out.println(hits.getTotalHits());
        System.out.println(response.getTook());

        //关闭es客户端
        esClient.close();
    }
}
