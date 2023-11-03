package io.fluent.ebeans.dao;

import io.ebean.DB;
import io.ebean.Database;
import io.fluent.ebeans.dto.CustomerDto;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EbeanDtoQuery {
    @Test
    public void testDTOQuery() {
        Database database = DB.getDefault();
        List<CustomerDto> beans =
                database.findDto(CustomerDto.class, "select id, name from customer where name = ?")
                        .setParameter(1, "Rob")
                        .findList();
        System.out.println(beans);
    }
}
