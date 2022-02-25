/**
 * BillingScheduleCreatedEvent.java
 *
 * Event class for BillingScheduleCreatedEvent
 * Do NOT edit as this is generated by APIGenerator
 */
package com.salesforce.revcloud.api.generated.event;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * @author kzheng
 * @since 1/5/2021
 */
public class BillingScheduleCreatedEvent {

    private String createdDate;
    private String createdById;
    private String requestIdentifier;
    private String correlationIdentifier;
    private List<BillSchdCreatedEventDetail> billingScheduleCreatedEventDetails = new ArrayList();

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
    public String getCreatedById() {
        return this.createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }
    public String getRequestIdentifier() {
        return this.requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }
    public String getCorrelationIdentifier() {
        return this.correlationIdentifier;
    }

    public void setCorrelationIdentifier(String correlationIdentifier) {
        this.correlationIdentifier = correlationIdentifier;
    }
    public List<BillSchdCreatedEventDetail> getBillingScheduleCreatedEventDetails() {
        return this.billingScheduleCreatedEventDetails;
    }

    public void setBillingScheduleCreatedEventDetails(List<BillSchdCreatedEventDetail> billingScheduleCreatedEventDetails) {
        this.billingScheduleCreatedEventDetails = billingScheduleCreatedEventDetails;
    }


    @Override
    public String toString() {
        return "{BillingScheduleCreatedEvent{" +
        ",createdDate='" + createdDate + "'" +
        ",createdById='" + createdById + "'" +
        ",requestIdentifier='" + requestIdentifier + "'" +
        ",correlationIdentifier='" + correlationIdentifier + "'" +
        ",billingScheduleCreatedEventDetails='" + billingScheduleCreatedEventDetails + "'" +
        '}';
    }
}
