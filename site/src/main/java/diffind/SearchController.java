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
    public String search(@RequestParam(value="q", required=true) String query, Model model) throws Exception{
        Client client = TransportClient.builder().build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("182.254.242.25"), 9300));
        SearchResponse response = client.prepareSearch("index")
                .setTypes("post")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("content", "lyc7175"))
                .setFrom(0).setSize(6).setExplain(false)
                .setHighlighterPostTags("</em>")
                .setHighlighterPreTags("<em>")
                .addHighlightedField("content")
                .setFetchSource(new String[]{"url"}, null)
                .execute()
                .actionGet();
        System.out.println(response);
        client.close();

        model.addAttribute("query", query);
        model.addAttribute("response", response);
        return "search";
    }

}
