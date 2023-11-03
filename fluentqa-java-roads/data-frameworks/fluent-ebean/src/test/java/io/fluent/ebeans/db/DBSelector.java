package io.fluent.ebeans.db;

import io.ebean.DB;
import io.ebean.Database;
import org.junit.jupiter.api.Test;

public class DBSelector {

    @Test
    public void testDatabaseSelector(){
        Database selector = DB.byName("db-1");
        System.out.println(selector);
        System.out.println(selector.name());
    }

    @Test
    public void testDatabaseDefault(){
        Database selector = DB.getDefault();
        System.out.println(selector);
        System.out.println(selector.name());
    }
}
