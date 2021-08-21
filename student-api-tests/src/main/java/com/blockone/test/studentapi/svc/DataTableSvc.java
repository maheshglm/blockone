package com.blockone.test.studentapi.svc;

import com.blockone.test.studentapi.CustomException;
import com.blockone.test.studentapi.CustomExceptionType;
import io.cucumber.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Data table svc.
 */
@Service
public class DataTableSvc {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTableSvc.class);

    public static final String DATA_TABLE_IS_EMPTY_OR_NULL = "data table is empty or null";

    /**
     * Gets two columns as map.
     *
     * @param dataTable the data table
     * @return the two columns as map
     */
    public Map<String, String> getTwoColumnsAsMap(final DataTable dataTable) {
        if (dataTable == null) {
            LOGGER.error(DATA_TABLE_IS_EMPTY_OR_NULL);
            throw new CustomException(CustomExceptionType.UNDEFINED, DATA_TABLE_IS_EMPTY_OR_NULL);
        }
        return dataTable.asMap(String.class, String.class);
    }
}
