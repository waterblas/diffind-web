package diffind;

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
import java.net.InetAddress;


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
        int port = 9300;
        query = query.trim();
        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("182.254.242.25"), port));
        SearchResponse response = client.prepareSearch("index")
                .setTypes("post")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("content", query))
                .setFrom((int)start).setSize(size).setExplain(false)
                .setHighlighterPostTags("</em>")
                .setHighlighterPreTags("<em>")
                .addHighlightedField("content")
                .setFetchSource(new String[]{"url"}, null)
                .execute()
                .actionGet();
        System.out.println(response);
        client.close();
        long totalHits = response.getHits().totalHits();
        if(start > totalHits){
            start = totalHits - size;
        }
        long pages = (long)Math.ceil(totalHits/10.0);
        long currentPage = (long)Math.ceil((start) / 10.0);
        long startPage =  1;
        long endPage = pages > size ? size: pages;
        if((currentPage - size / 2) > 0){
            if((currentPage + size / 2) < pages + 1){
                startPage = currentPage - size / 2 + 1;
                endPage = currentPage + size / 2 + 1;
            }else{
                startPage = (pages - size + 1 > 0) ? (pages - size + 1) :1;
                endPage = pages;
            }
        }
        model.addAttribute("query", query);
        model.addAttribute("size", size);
        model.addAttribute("totalHits", totalHits);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", currentPage+1);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("response", response);
        return "search";
    }

}
