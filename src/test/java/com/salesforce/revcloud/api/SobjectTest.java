package com.salesforce.revcloud.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SobjectTest {

    private ObjectMapper mapper = new ObjectMapper();

    public void setup() {
        mapper.findAndRegisterModules();
    }

    @Test
    public void testSerialization() throws Exception {
        SObject order = new SObject("Order");
        order.setField("Status", "Draft");
        order.setField("CurrencyIsoCode", "USD");
        Calendar d = Calendar.getInstance();
        d.set(Calendar.YEAR, 2022);
        d.set(Calendar.MONTH, 0);
        d.set(Calendar.DATE, 3);
        order.setField("EndDate", d.getTime());
        SObject orderItem = new SObject("OrderItem");
        orderItem.setField("UnitPrice", 123.0);
        orderItem.setField("Quantity", 1);
        List<SObject> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        order.setField("OrderItems", orderItems);
        String result = mapper.writeValueAsString(order);
        System.out.println(result);
        Assert.assertEquals("Unexpected result after serialization",
                "{\"attributes\":{\"type\":\"Order\"},\"Status\":\"Draft\",\"OrderItems\":[{\"attributes\":{\"type\":\"OrderItem\"},\"UnitPrice\":123.0,\"Quantity\":1}],\"CurrencyIsoCode\":\"USD\",\"EndDate\":\"2022-01-03\"}",
                result);
    }

    @Test
    public void testDeserialization() throws Exception {
        String json = "{\"attributes\":{\"type\":\"Order\"},\"Status\":\"Draft\",\"OrderItems\":[{\"attributes\":{\"type\":\"OrderItem\"},\"UnitPrice\":123.0,\"Quantity\":1}],\"CurrencyIsoCode\":\"USD\",\"EndDate\":\"2022-01-03\"}";
        SObject order = mapper.readValue(json, SObject.class);
        Assert.assertEquals("Order", order.getAttributes().get("type"));
        Assert.assertEquals("2022-01-03", order.getField("EndDate"));
        List<SObject> orderItems = order.getField("OrderItems");
        Assert.assertEquals(1, orderItems.size());
        Assert.assertEquals(1, (int)orderItems.get(0).getField("Quantity"));
        Assert.assertEquals(123.0, (double)orderItems.get(0).getField("UnitPrice"), 0);
    }
}
