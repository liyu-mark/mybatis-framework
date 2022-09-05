package xml;

import configuration.Configuration;
import configuration.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 解析mapper.xml
 */
public class XmlMapperBuilder {

    private Configuration configuration;
    public XmlMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        Element root = document.getRootElement();
        String namespace = root.attributeValue("namespace");

        Map<String, MappedStatement> statementMap = configuration.getStatementMap();


        List<Element> select = root.selectNodes("select");
        for (Element element : select) {
            MappedStatement statement = new MappedStatement();
            String id = element.attributeValue("id");
            String paramterType = element.attributeValue("paramterType");
            String resultType = element.attributeValue("resultType");
            String sql = element.getTextTrim();
            
            //组装
            statement.setId(id);
            statement.setParamterType(getClassType(paramterType));
            statement.setResultType(getClassType(resultType));
            statement.setSql(sql);

            statementMap.put(namespace+id,statement);
            
            System.out.println(id+" "+paramterType+" "+resultType+" "+sql);
        }

    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        return Class.forName(paramterType);
    }

    public static void main(String[] args) throws Exception{
        XmlMapperBuilder xml = new XmlMapperBuilder(new Configuration());
        InputStream stream = Resource.getResourceAsStream("orm/mapper.xml");
        xml.parse(stream);
    }

}
