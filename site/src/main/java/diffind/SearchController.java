package diffind;

import java.net.InetAddress;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.common.settings.Settings;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;



@Controller
public class SearchController {

    @RequestMapping("/")
    public String home(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @RequestMapping("/s")
    public String search(
            @RequestParam(value="q", required=true) String query,
            @RequestParam(value="start", required=false, defaultValue = "0") long start,
            Model model
    ) throws Exception{
        int size = 10;
        query = query.trim();
        String[] terms = getSegmentTerm(query);
        if(terms.length == 0){ return "home";}

        SearchResponse response = getElasticResponse(terms, start, size);
        long totalHits = response.getHits().totalHits();
        if(start > totalHits){
            start = totalHits - size;
        }
        long pages = (long)Math.ceil(totalHits/10.0);
        long currentPage = (long)Math.ceil((start) / 10.0) + 1;
        long startPage =  1;
        long endPage = pages > size ? size: (pages == 0? 1: pages);
        if((currentPage - size / 2) > 1){
            if((currentPage + size / 2) < pages + 2){
                startPage = currentPage - size / 2;
                endPage = currentPage + size / 2 - 1;
            }else{
                startPage = (pages - size + 1 > 0) ? (pages - size + 1) :1;
                endPage = pages;
            }
        }
        model.addAttribute("query", query);
        model.addAttribute("size", size);
        model.addAttribute("totalHits", totalHits);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("response", response);
        return "search";
    }

    private String[] getSegmentTerm(String query){
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> list = segmenter.process(query, JiebaSegmenter.SegMode.SEARCH);
        int termNum = list.size() > 5? 5:list.size();
        String[] terms = new String[termNum];
        for(int i=0; i < termNum; i++) {
            terms[i] = list.get(i).word;
        }
        return terms;
    }

    private SearchResponse getElasticResponse(String[] terms, long start, int size) throws Exception{
        int port = 9300;
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", "byr-application").build();
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("182.254.242.25"), port));
        SearchResponse response = client.prepareSearch("index")
                .setTypes("post")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termsQuery("content", terms))
                .setFrom((int)start).setSize(size).setExplain(false)
                .setHighlighterPostTags("</em>")
                .setHighlighterPreTags("<em>")
                .addHighlightedField("content")
                .setFetchSource(new String[]{"url", "title"}, null)
                .execute()
                .actionGet();
        System.out.println(response);
        client.close();
        return response;
    }

}
