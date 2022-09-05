package configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private DataSource dataSources;
    private Map<String,MappedStatement> statementMap = new HashMap<>();

    public DataSource getDataSources() {
        return dataSources;
    }

    public void setDataSources(DataSource dataSources) {
        this.dataSources = dataSources;
    }

    public Map<String, MappedStatement> getStatementMap() {
        return statementMap;
    }

    public void setStatementMap(Map<String, MappedStatement> statementMap) {
        this.statementMap = statementMap;
    }
}
