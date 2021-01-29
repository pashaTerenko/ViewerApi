package com.terenko.viewerapi.API;

import java.util.List;

public class Responce {
String type;
Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public String getType() {
        return type;
    }

    public class Payload{
    String text;
    String url;

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}
}
