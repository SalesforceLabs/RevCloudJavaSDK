package com.salesforce.revcloud.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer extends StdSerializer<Date> {

    public DateSerializer(Class<Date> t) {
        super(t);
    }
    public DateSerializer() {
        super(Date.class);
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        jsonGenerator.writeString(format.format(date));
    }
}
