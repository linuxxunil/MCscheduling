package csmp.dmm.dispatcher.api;

import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockResultSetMetaData;

import java.sql.ResultSet;
import java.util.List;
 
public class MyResultSet {
 
    public static ResultSet ListToResultSet(List<String> headers, List<List<Object>> data) throws Exception {
 
        // validation
        if (headers == null || data == null) {
            throw new Exception("null parameters");
        }
 
        if (headers.size() != data.get(0).size()) {
            throw new Exception("parameters size are not equals");
        }
        
 
        // create a mock result set
        MockResultSet mockResultSet = new MockResultSet("myResultSet");
        
        // create a mock result set metadata
        MockResultSetMetaData mockResultSetMetaData = new MockResultSetMetaData();
        mockResultSetMetaData.setColumnCount(headers.size());
//        mockResultSetMetaData.setColumnCount();
 
        // add header
        int headerIndex = 1;
        for (String string : headers) {         
            mockResultSet.addColumn(string);
            mockResultSetMetaData.setColumnName(headerIndex, string);
            headerIndex += 1;
        }
 
        // add data
        for (List<Object> list : data) {
        	mockResultSet.addRow(list);
            
        }
        mockResultSet.setResultSetMetaData(mockResultSetMetaData);
        
 
        return mockResultSet;
    }
}
