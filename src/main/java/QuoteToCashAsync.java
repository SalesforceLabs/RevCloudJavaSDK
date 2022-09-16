import com.google.common.html.HtmlEscapers;
import com.salesforce.revcloud.ApiContext;
import com.salesforce.revcloud.api.SoqlQuery;
import com.salesforce.revcloud.api.generated.*;
import com.salesforce.revcloud.api.generated.response.*;
import com.salesforce.revcloud.api.generated.event.*;
import com.salesforce.revcloud.api.response.ApiResponse;
import com.salesforce.revcloud.api.response.SoqlQueryResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Demo for invoking Rev Cloud APIs from Quote to Cash (payment) asynchronously
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

public class QuoteToCashAsync {

    private Object semaphore = new Object();

    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments("QuoteToCashAsync", args);
        new QuoteToCashAsync().call(arguments);
    }

    private void call(Arguments args) throws Exception {
        ApiContext apiContext = new ApiContext();
        apiContext.init(args.loginEndpoint, args.username, args.password, args.appVersion);
        long currentTime = System.currentTimeMillis();
        try {
            // Step 1: Quote to Order
            quoteToOrder(apiContext, args.quoteId, (quoteToOrderResp) -> {
                String orderId = ((CreateOrderFromQuoteResponse) quoteToOrderResp).getQuoteToOrderCompletedEvent().getOrderId();
                if (orderId != null) {
                    System.out.println("Order created successfully: " + orderId);
                    // Step 2: Order to Asset
                    orderToAssets(apiContext, orderId, (orderToAssetResp) -> {
                        if (orderToAssetResp.isSuccess()) {
                            System.out.println("Assets created successfully: " + ((CreateOrUpdateAssetFromOrderResponse)orderToAssetResp).getCreateAssetOrderEvent().getAssetDetails().stream().map(d -> d.getAssetId()).collect(Collectors.toList()));
                            // Step 3: Order Item to Billing Schedule
                            orderToBillingSchedule(apiContext, orderId, (orderToBillingScheduleResp) -> {
                                if (orderToBillingScheduleResp.isSuccess()) {
                                    System.out.println("Billing Schedules created successfully: " + ((CreateBillingScheduleFromOrderItemResponse)orderToBillingScheduleResp).getBillingScheduleCreatedEvent().getBillingScheduleCreatedEventDetails().stream().map(d -> d.getBillingScheduleId()).collect(Collectors.toList()));
                                    // Step 4: Order to Invoice
                                    orderToInvoice(apiContext, orderId, (orderToInvoiceResp) -> {
                                        String invoiceId = ((CreateInvoiceFromOrderResponse)orderToInvoiceResp).getInvoiceProcessedEvent().getInvoiceProcessedDetailEvents().stream().map(d -> d.getInvoiceId()).collect(Collectors.toList()).get(0);
                                        if (invoiceId != null) {
                                            System.out.println("Invoice created successfully: " + invoiceId);
                                            try {
                                                // get invoice balance
                                                SoqlQueryResponse soqlQueryResponse = new SoqlQuery(apiContext).execute("SELECT Balance FROM Invoice WHERE Id = '" + invoiceId + "'");
                                                Double balance = (Double) soqlQueryResponse.getRecords().get(0).get("Balance");
                                                // Step 5: Pay Invoice
                                                payInvoice(apiContext, balance, args.paymentGatewayId, args.paymentMethodId, (payInvoiceResp) -> {
                                                    if (payInvoiceResp.isSuccess()) {
                                                        String paymentId = ((PaymentSaleResponse) payInvoiceResp).getPaymentCreationEvent().getPaymentId();
                                                        System.out.println("Payment charged successfully: " + paymentId);
                                                        // Step 6: Apply payment to Invoice (synchronous)
                                                        ApplyPaymentResponse applyPaymentResp = applyPayment(apiContext, paymentId, invoiceId, balance);
                                                        if (applyPaymentResp.isSuccess()) {
                                                            System.out.println("Payment applied successfully: " + applyPaymentResp.getPaymentLineInvoiceId());
                                                        } else {
                                                            System.out.println("Failed applying payment: " + applyPaymentResp);
                                                        }
                                                        synchronized (semaphore) {
                                                            semaphore.notify();
                                                        }
                                                    } else {
                                                        fail("Failed processing payment, response: " + payInvoiceResp);
                                                    }
                                                });
                                            } catch (Exception e) {
                                                fail("Error invoking pay invoice " + e.getMessage());
                                            }
                                        } else {
                                            fail("Failed creating invoice, response: " + orderToInvoiceResp);
                                        }
                                    });
                                } else {
                                    fail("Failed creating billing schedule, response: " + orderToBillingScheduleResp);
                                }
                            });
                        } else {
                            fail("Failed creating assets, response: " + orderToAssetResp);
                        }
                    });
                } else {
                    fail("Failed creating order, response: " + quoteToOrderResp);
                }
            });
            synchronized (semaphore) {
                semaphore.wait(30000);
            }
            System.out.println("Process took " + (System.currentTimeMillis() - currentTime) + "ms");
        } finally {
            apiContext.close();
        }
    }

    private void fail(String error) {
        if (error != null) {
            System.out.println(HtmlEscapers.htmlEscaper().escape(error));
        }
        synchronized (semaphore) {
            semaphore.notify();
        }
    }

    private void quoteToOrder(ApiContext apiContext, String quoteId, Consumer<ApiResponse> callback) {
        try {
            CreateOrderFromQuoteResponse resp = new CreateOrderFromQuote(apiContext).setQuoteRecordId(quoteId).setMessageConsumer(callback).invoke();
            if (!resp.isSuccess()) {
                fail("Failed creating order: " + resp);
            }
        } catch (Throwable e) {
            fail("Failed creating order: " + e.getMessage());
        }
    }

    private void orderToAssets(ApiContext apiContext, String orderId, Consumer<ApiResponse> callback) {
        try {
            CreateOrUpdateAssetFromOrderResponse resp = new CreateOrUpdateAssetFromOrder(apiContext).setOrderId(orderId).setMessageConsumer(callback).invoke();
            if (!resp.isSuccess()) {
                fail("Failed creating asset: " + resp);
            }
        } catch (Throwable e) {
            fail("Failed creating asset: " + e.getMessage());
        }
    }

    private void orderToBillingSchedule(ApiContext apiContext, String orderId, Consumer<ApiResponse> callback) {
        try {
            SoqlQueryResponse soqlQueryResponse = new SoqlQuery(apiContext).execute("SELECT Id FROM OrderItem WHERE OrderId='" + orderId + "'");
            List<String> orderItemIds = soqlQueryResponse.getRecords().stream().map(r -> (String) r.get("Id")).collect(Collectors.toList());
            CreateBillingScheduleFromOrderItemResponse resp = new CreateBillingScheduleFromOrderItem(apiContext).setOrderItemIds(orderItemIds).setMessageConsumer(callback).invoke();
            if (!resp.isSuccess()) {
                fail("Failed creating billing schedule: " + resp);
            }
        } catch (Throwable e) {
            fail("Failed creating billing schedule: " + e.getMessage());
        }
    }

    private void orderToInvoice(ApiContext apiContext, String orderId, Consumer<ApiResponse> callback) {
        try {
            String correlationId = UUID.randomUUID().toString();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            CreateInvoiceFromOrderResponse resp = new CreateInvoiceFromOrder(apiContext)
                    .setOrderId(orderId)
                    .setInvoiceDate(new Date())
                    .setTargetDate(calendar.getTime())
                    .setInvoiceStatus("Posted")
                    .setCorrelationId(correlationId)
                    .setMessageConsumer(callback)
                    .invoke();
            if (!resp.isSuccess()) {
                fail("Failed creating invoice: " + resp);
            }
        } catch (Throwable e) {
            fail("Failed creating invoice: " + e.getMessage());
        }
    }

    private void payInvoice(ApiContext apiContext, double balance, String paymentGatewayId, String paymentMethodId, Consumer<ApiResponse> callback) {
        try {
            PaymentSaleResponse resp = new PaymentSale(apiContext)
                    .setPaymentGatewayId(paymentGatewayId)
                    .setPaymentMethodId(paymentMethodId)
                    .setAmount(balance)
                    .setMessageConsumer(callback)
                    .invoke();
            if (!resp.isSuccess()) {
                fail("Failed paying for invoice: " + resp);
            }
        } catch (Throwable e) {
            fail("Failed paying for invoice: " + e.getMessage());
        }
    }

    private ApplyPaymentResponse applyPayment(ApiContext apiContext, String paymentId, String invoiceId, double balance) {
        try {
            return new ApplyPayment(apiContext)
                    .setPaymentId(paymentId)
                    .setAppliedToId(invoiceId)
                    .setAmount(balance)
                    .invoke();
        } catch (Throwable e) {
            fail("Failed applying payment: " + e.getMessage());
            throw new RuntimeException("Failed applying payment", e);
        }
    }
}
