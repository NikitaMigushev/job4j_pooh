package ru.job4j.pooh;

public class Req {
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        String requestType = lines[0].split("/")[0].split(" ")[0];
        String poohMode = lines[0].split("/")[1].split(" ")[0];
        String sourceName = lines[0].split("/")[2].split(" ")[0].split(" ")[0];
        String param = "";
        if ("POST".equals(requestType)) {
            param = lines[7];
        }
        return new Req(requestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
