package xml;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.jboss.C3P0PooledDataSource;
import configuration.Configuration;
import configuration.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * 解析sqlMapConfig
 */
public class XMLConfigerBuilder {

    public Configuration configuration;

    public XMLConfigerBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration parseConfiguration(String path) throws DocumentException, IOException, PropertyVetoException, ClassNotFoundException {
        InputStream inputStream = Resource.getResourceAsStream(path);
        Document document = new SAXReader().read(inputStream);
        Element root = document.getRootElement();
        List<Element> datasource = root.selectNodes("property");
        Properties properties = new Properties();
        for (Element element : datasource) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("driverClass"));
        dataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));

        //填充数据源
        configuration.setDataSources(dataSource);

        //填充mapper
        List<Element> mapper_urls = root.selectNodes("mapper");

        XmlMapperBuilder xmlMapperBuilder = new XmlMapperBuilder(configuration);
        for (Element url : mapper_urls) {
            xmlMapperBuilder.parse(Resource.getResourceAsStream(url.attributeValue("resource")));
        }

        return configuration;
    }

    public static void main(String[] args) throws DocumentException, IOException, PropertyVetoException {
//        MappedStatement mapper = parseMapper("spring-config/sqlMapConfig.xml");
    }

}
