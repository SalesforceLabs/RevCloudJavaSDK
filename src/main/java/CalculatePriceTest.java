import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.generated.commerce.pricing.CalculatePrice;
import com.salesforce.revcloud.api.generated.commerce.pricing.response.CalculatePriceResponse;
import com.salesforce.revcloud.api.generated.commerce.pricing.type.CalculatePriceRequest;
import com.salesforce.revcloud.api.generated.commerce.pricing.type.Graph;
import com.salesforce.revcloud.api.generated.commerce.pricing.type.Record;

public class CalculatePriceTest {

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
        new CalculatePriceTest().test(host, appVersion, username, password);
    }

    public void test(String host, String appVersion, String username, String password) throws Exception {
        ApiContext apiContext = new ApiContext();
        apiContext.init(host, username, password, appVersion);
        CalculatePriceRequest request = new CalculatePriceRequest()
                .setListPricebookId("01sxx0000002JgbAAE")
                .setCandidatePricebookIds(ImmutableList.of("01sxx0000002JgbAAE"))
                .setPricingFlow("GET_FINAL_PRICE")
                .setGraph(new Graph()
                            .setGraphId("1")
                            .setRecords(new Record[] {
                                new Record()
                                    .setReferenceId("ref_sales_txn")
                                    .setRecord(ImmutableMap.of("attributes", ImmutableMap.of("type", "Quote"))),
                                new Record()
                                    .setReferenceId("ref_sales_txn_item1")
                                    .setRecord(new ImmutableMap.Builder<String, Object>()
                                        .put("attributes", ImmutableMap.of("type", "QuoteLineItem"))
                                        .put("QuoteId", "@{ref_sales_txn.Id}")
                                        .put("Product2Id", "01txx00000034EcAAI")
                                        .put("ProductSellingModelId", "0jPxx000000000BEAQ")
                                        .put("StartDate", "2022-01-01")
                                        .put("Quantity", 3).build())
                }));
        CalculatePriceResponse response = new CalculatePrice(apiContext)
                .setInput(request)
                .setUriParameter("sobject", "SalesTransaction")
                .invoke();
        System.out.println(new ObjectMapper().writeValueAsString(response.getApiOutput()));
        apiContext.close();
    }
}
