import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.SoqlQuery;
import com.salesforce.revcloud.api.generated.*;
import com.salesforce.revcloud.api.generated.response.*;
import com.salesforce.revcloud.api.response.SoqlQueryResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Demo for invoking Rev Cloud APIs from Quote to Cash (payment)
 * Steps:
 * 1. Quote to Order
 * 2. Order to Asset
 * 3. Order Items to Billing Schedule
 * 4. Order to Invoice
 * 5. Charge payment
 * 6. Apply Payment (to Invoice)
 *
 * @author kzheng
 * @since 55.0
 */
public class QuoteToCashSync {

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments("QuoteToCashSync", args);
        new QuoteToCashSync().call(arguments);
    }

    private void call(Arguments args) throws  Exception {
        ApiContext apiContext = new ApiContext();
        apiContext.init(args.loginEndpoint, args.username, args.password, args.appVersion);
        long currentTime = System.currentTimeMillis();
        try {
            // Step 1: Quote to Order
            String orderId = quoteToOrder(apiContext, args.quoteId);
            // Step 2: Order to Asset
            orderToAssets(apiContext, orderId);
            // Step 3: Order Item to Billing Schedule
            orderToBillingSchedule(apiContext, orderId);
            // Step 4: Order to Invoice
            String invoiceId = orderToInvoice(apiContext, orderId);
            // get invoice balance
            SoqlQueryResponse soqlQueryResponse = new SoqlQuery(apiContext).execute("SELECT Balance FROM Invoice WHERE Id = '" + invoiceId + "'");
            Double balance = (Double) soqlQueryResponse.getRecords().get(0).get("Balance");
            // Step 5: Pay Invoice
            String paymentId = payInvoice(apiContext, balance, args.paymentGatewayId, args.paymentMethodId);
            // Step 6: Apply payment to Invoice
            applyPayment(apiContext, paymentId, invoiceId, balance);
            System.out.println("Process took " + (System.currentTimeMillis() - currentTime) + "ms");
        } finally {
            apiContext.close();
        }
    }

    private String quoteToOrder(ApiContext apiContext, String quoteId) throws Exception {
        CreateOrderFromQuoteResponse result = new CreateOrderFromQuote(apiContext).setQuoteRecordId(quoteId).invokeAndWait();
        if (result.isSuccess()) {
            System.out.println("Quote to Order succeeded, order id: " + result.getQuoteToOrderCompletedEvent().getOrderId());
            return result.getQuoteToOrderCompletedEvent().getOrderId();
        } else {
            throw new RuntimeException("Error creating Order from Quote, response: " + result);
        }
    }

    private void orderToAssets(ApiContext apiContext, String orderId) throws Exception {
        CreateOrUpdateAssetFromOrderResponse result = new CreateOrUpdateAssetFromOrder(apiContext).setOrderId(orderId).invokeAndWait();
        if (result.isSuccess()) {
            System.out.println("Order to Asset succeeded, asset ids: " + result.getCreateAssetOrderEvent().getAssetDetails().stream().map(d -> d.getAssetId()).collect(Collectors.toList()));
        } else {
            throw new RuntimeException("Error creating Assets from Order, response: " + result);
        }
    }

    private void orderToBillingSchedule(ApiContext apiContext, String orderId) throws Exception {
        SoqlQueryResponse soqlQueryResponse = new SoqlQuery(apiContext).execute("SELECT Id FROM OrderItem WHERE OrderId='" + orderId + "'");
        List<String> orderItemIds = soqlQueryResponse.getRecords().stream().map(r -> (String) r.get("Id")).collect(Collectors.toList());
        CreateBillingScheduleFromOrderItemResponse result = new CreateBillingScheduleFromOrderItem(apiContext).setOrderItemIds(orderItemIds).invokeAndWait();
        if (result.isSuccess()) {
            System.out.println("Order Item to Billing Schedule succeeded, billing schedules: " +
                    result.getBillingScheduleCreatedEvent().getBillingScheduleCreatedEventDetails().stream().map(d -> d.getBillingScheduleId()).collect(Collectors.toList()));
        } else {
            throw new RuntimeException("Error creating Billing Schedule from Order Items, response: " + result);
        }
    }

    private String orderToInvoice(ApiContext apiContext, String orderId) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        CreateInvoiceFromOrderResponse result = new CreateInvoiceFromOrder(apiContext)
                .setOrderId(orderId)
                .setInvoiceDate(new Date())
                .setTargetDate(calendar.getTime())
                .setInvoiceStatus("Posted")
                .invokeAndWait();
        if (result.isSuccess()) {
            List<String> invoiceIds = result.getInvoiceProcessedEvent().getInvoiceProcessedDetailEvents().stream().map(d -> d.getInvoiceId()).collect(Collectors.toList());
            System.out.println("Quote to Invoice succeeded, invoice: " + invoiceIds);
            return invoiceIds.get(0);
        } else {
            throw new RuntimeException("Error creating Invoice from Order, response: " + result);
        }
    }

    private String payInvoice(ApiContext apiContext, double balance, String paymentGatewayId, String paymentMethodId) throws Exception {
        PaymentSaleResponse result = new PaymentSale(apiContext)
                .setPaymentGatewayId(paymentGatewayId)
                .setPaymentMethodId(paymentMethodId)
                .setAmount(balance)
                .invokeAndWait();
        if (result.isSuccess()) {
            System.out.println("Payment succeeded, payment: " + result.getPaymentCreationEvent().getPaymentId());
            return result.getPaymentCreationEvent().getPaymentId();
        } else {
            throw new RuntimeException("Error charging payment for invoice, response: " + result);
        }
    }

    private void applyPayment(ApiContext apiContext, String paymentId, String invoiceId, double balance) throws Exception {
        ApplyPaymentResponse result = new ApplyPayment(apiContext)
                .setPaymentId(paymentId)
                .setAppliedToId(invoiceId)
                .setAmount(balance)
                .invokeAndWait();
        if (result.isSuccess()) {
            System.out.println("Payment applied to Invoice, payment line invoice: " + result.getPaymentLineInvoiceId());
        } else {
            throw new RuntimeException("Error applying payment, response: " + result);
        }
    }
}
