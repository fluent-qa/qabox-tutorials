package io.fluent.ebeans.dao;

import io.ebean.DB;
import io.ebean.RawSql;
import io.ebean.RawSqlBuilder;
import io.fluent.ebeans.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * ORM queries
 * Pure ORM queries, all SQL is generated
 * Some SQL in the select or where clause
 * Using SQL aka findNative
 * DTO queries
 * Using SQL that we provide with non-entity beans - "dto beans"
 * SqlQuery
 * Using SQL with a row mapper or SqlRow's - not DTO or Entity beans
 * SqlQuery using SqlRow
 * SqlQuery using RowMappers
 * JDBC queries
 * Straight use of JDBC
 * Very rarely do we need to use raw JDBC
 */
public class EBeanQuery {
//    CallableSql cs = server.createCallableSql("{call my_stored_procedure(?,?)}");
//cs.setParameter(1, 42);
//cs.setParameter(2, "baz");
//server.execute(cs);

    @Test
    public void updateQuery(){
//        int rows = new QCustomer()
//                .name.startsWith("Rob")
//                .asUpdate()                      // convert to UpdateQuery
//                .set("registered", now)        // update set ...
//                .update();
//        int rows = new QCustomer()
//                .billingAddress.country.equalTo(nz)
//                .billingAddress.city.equalTo("Auckland")
//                .name.startsWith("Rob")
//                .asUpdate()
//                .set("registered", now)
//                .setRaw("name = concat(?, name, '+', ?)", "before", "after")
//                .update();
    }
    //it is very how to use
    @Test
    public void testQuery() {

        Customer customer = new Customer();
        customer.setName("Hello");
        // insert the customer in the DB
        DB.save(customer);
        String sql = "select id, name from customer where name like ?";

        Customer c = DB.findNative(Customer.class, sql)
                .setParameter("Jo%")
                .findOne();
        System.out.println(c);

        String listSQL = "select * from customer where name like :name order by name desc";

        List<Customer> customers = DB.findNative(Customer.class, listSQL)
                .setParameter("name", "Jo%")
                .findList();
        System.out.println(customers);
    }
    @Test
    public void testMappingColumns(){
        final RawSql rawSql =
                RawSqlBuilder
                        .unparsed("select r.id, r.name from customer r where r.id >= :a and r.name like :b")
                        .columnMapping("r.id", "id")
                        .columnMapping("r.name", "name")
                        .create();
        List<Customer> list = DB.find(Customer.class)
                .setRawSql(rawSql)
                .setParameter("a", 42)
                .setParameter("b", "R%")
                .findList();
        System.out.println(list);
    }
    /**
     * // Use raw SQL with an aggregate function
     *
     * String sql
     * = " select order_id, o.status, c.id, c.name, sum(d.order_qty*d.unit_price) as totalAmount"
     * + " from orders o"
     * + " join customer c on c.id = o.customer_id "
     * + " join order_detail d on d.order_id = o.id "
     * + " group by order_id, o.status ";
     *
     * static final RawSql rawSql = RawSqlBuilder
     *   // let ebean parse the SQL so that it can add
     *   // expressions to the WHERE and HAVING clauses
     *   .parse(sql)
     *     // map resultSet columns to bean properties
     *     .columnMapping("order_id", "order.id")
     *     .columnMapping("o.status", "order.status")
     *     .columnMapping("c.id", "order.customer.id")
     *     .columnMapping("c.name", "order.customer.name")
     *     .create();
     *
     * ...
     *
     * List<OrderAggregate> list = DB.find(OrderAggregate.class);
     *   query.setRawSql(rawSql)
     *   // add expressions to the WHERE and HAVING clauses
     *   .where().gt("order.id", 42)
     *   .having().gt("totalAmount", 20)
     *   .findList();
     */
    /**
     * // fetch additional parts of the object graph
     * // after the Raw SQL query is executed.
     *
     * String sql
     * = " select order_id, sum(d.order_qty*d.unit_price) as totalAmount "
     * + " from order_detail d"
     * + " group by order_id ";
     *
     * static final RawSql rawSql = RawSqlBuilder
     *   .parse(sql)
     *   .columnMapping("order_id", "order.id")
     *   .create();
     *
     * ...
     *
     * Query<OrderAggregate> query = DB.find(OrderAggregate.class);
     *   query.setRawSql(rawSql)
     *     // get ebean to fetch parts of the order and customer
     *     // after the raw SQL query is executed
     *     .fetchQuery("order", "status,orderDate",new FetchConfig().query())
     *     .fetchQuery("order.customer", "name")
     *     .where().gt("order.id", 0)
     *     .having().gt("totalAmount", 20)
     *     .order().desc("totalAmount")
     *     .setMaxRows(10);
     */
}
