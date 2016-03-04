package diffind;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="elastic")
public class ElasticSettings {

    private byte size;
    private Map<String, String> cluster = new HashMap<String, String>();
    private Map<String, Object> server = new HashMap<String, Object>();

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public Map<String, String> getCluster() {
        return cluster;
    }

    public Map<String, Object> getServer() {
        return server;
    }
}
