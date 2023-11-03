package io.fluent.ebeans.dao;

import io.ebean.DB;
import io.ebean.Database;
import io.fluent.ebeans.entity.Customer;
import io.fluent.ebeans.entity.UserModel;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class CustomerDao {

    @Test
    public void testInsertAndDelete() {

        Customer customer = new Customer();
        customer.setName("Hello world");
        // insert the customer in the DB
        DB.save(customer);
        // delete the customer
        DB.delete(customer);
    }

    @Test
    public void testFindBy() throws ExecutionException, InterruptedException {
        Database database = DB.getDefault();
        Customer customer = new Customer();
        customer.setName("Hello world");
        database.save(customer);
        // Find by Id
        var result = database.find(Customer.class);
        System.out.println(result.findCount());
        System.out.println(result.findIds());
        System.out.println(result.findFutureList().get());
        Customer foundHello = database.find(Customer.class, 1);
        System.out.println(foundHello);
    }

    @Test
    public void testModelDao(){
        UserModel user = new UserModel();
        user.setName("Hello world");
        user.setFullName("FullName");
        user.save();
        user.delete();
    }
}
