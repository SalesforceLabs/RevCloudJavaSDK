import com.google.common.collect.ImmutableList;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.SObject;
//import com.salesforce.revcloud.api.generated.Revenue_buynow__BuyNow;
//import com.salesforce.revcloud.api.generated.response.Revenue_buynow__BuyNowResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyNow {

    public static void main(String[] args) throws Exception {
        String username = args[0];
        String password = args[1];
        String appVersion = args[2];
        String host = args[3];
        if (host == null) {
            host = "https://www.salesforce.com";
        }
        if (appVersion == null) {
            appVersion = "55.0";
        }
        new BuyNow().run(host, appVersion, username, password);
    }

    // Reference implementation of invoking a buy now flow
    private void run(String host, String appVersion, String username, String password) throws Exception {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        SObject order = new SObject("Order");
//        order.setField("AccountId", "001xx000003DGQWAA4");
//        order.setField("EffectiveDate", format.format(new Date()));
//        order.setField("Pricebook2Id", "01sxx0000002JgbAAE");
//        order.setField("Status", "Draft");
//        order.setField("CurrencyIsoCode", "USD");
//        SObject orderItem = new SObject("OrderItem");
//        orderItem.setField("UnitPrice", 5199.0);
//        orderItem.setField("PricebookEntryId", "01uxx0000005PjkAAE");
//        orderItem.setField("Quantity", 1);
//        orderItem.setField("ProductSellingModelId", "0jPxx000000000BEAQ");
//        orderItem.setField("TaxTreatmentId", "1ttxx0000000006AAA");
//        orderItem.setField("CurrencyIsoCode", "USD");
//        ApiContext context = new ApiContext();
//        context.init(host, username, password, appVersion, false);
//        Revenue_buynow__BuyNow buyNow = new Revenue_buynow__BuyNow(context);
//        Revenue_buynow__BuyNowResponse response = buyNow
//                .setOrder(order)
//                .setOrderItems(ImmutableList.of(orderItem))
//                .setPaymentMethodId("03Oxx000000000BEAQ")
//                .invoke();
//        if (response.isSuccess()) {
//            SObject orderOutput = response.getOrderOutput();
//            System.out.println("Got order: " + orderOutput.getId());
//        } else {
//            throw new RuntimeException("Error invoking BuyNow. Response " + response);
//        }
    }
}