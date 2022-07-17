package yuriua.es.api;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import yuriua.DemoApplication;
import yuriua.es.dao.ProductDao;
import yuriua.es.entity.Product;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yuriua
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class NativeSearchQueryBuilderTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private ProductDao productDao;

    @Test
    public void test1() {

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termsQuery("title", "米"))//查询条件
                .withPageable(PageRequest.of(0, 2))//分页
                .withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC))//排序
                .withHighlightFields(new HighlightBuilder.Field("小米"))//高亮字段显示
                .build();
        Page<Product> products = productDao.search(nativeSearchQuery);
        products.forEach(System.out::println);
    }

    @Test
    public void test2() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        String[] title = {"米", "包子", "初音未来"};//模拟多
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //boolQueryBuilder.must(QueryBuilders.termsQuery("title", title));
        //boolQueryBuilder.must(QueryBuilders.matchQuery("title", "小米化").operator(Operator.OR));
        //过滤
        //boolQueryBuilder.filter(QueryBuilders.termsQuery("price", 1000));
        FunctionScoreQueryBuilder functionScoreQueryBuilder =  new FunctionScoreQueryBuilder(QueryBuilders.matchQuery("title", "小米化").operator(Operator.OR));
        FunctionScoreQueryBuilder.FilterFunctionBuilder filterFunctionBuilder =
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("title","小"),
                        ScoreFunctionBuilders.gaussDecayFunction("price",10,0.2));
        boolQueryBuilder.must(functionScoreQueryBuilder);
        boolQueryBuilder.filter(filterFunctionBuilder.getFilter());

        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        //分页
        PageRequest pageRequest = PageRequest.of(0, 2);
        nativeSearchQueryBuilder.withPageable(pageRequest);
        NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();

        SearchHits<Product> searchHits = elasticsearchRestTemplate.search(searchQuery, Product.class);
        //看看PageRequest
        System.out.println(pageRequest==searchQuery.getPageable());
        SearchPage<Product> searchPage = SearchHitSupport.searchPageFor(searchHits,searchQuery.getPageable());

        List<SearchHit<Product>> collect = searchPage.getSearchHits().get().collect(Collectors.toList());
        for (SearchHit<Product> productSearchHit : collect) {
            Product content = productSearchHit.getContent();
            System.out.println(content);
        }
    }

    @Test
    public void wildcardQueryTest(){
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("title","*米*"));
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        SearchHits<Product> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, Product.class);
        for (SearchHit<Product> searchHit : searchHits) {
            System.out.println(searchHit.getContent());
        }
    }

    @Test
    public void other(){
        List list = new LinkedList();
        list.add("aaa");
        list.add("bbb");
        String[] strArray = new String[list.size()];
        list.toArray(strArray);
        for (String s : strArray) {
            System.out.print(s + " ");
        }
    }


}