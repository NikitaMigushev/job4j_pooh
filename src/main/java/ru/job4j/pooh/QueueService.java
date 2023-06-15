package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class QueueService implements Service {
    private final ConcurrentMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String sourceName = req.getSourceName();
        if ("POST".equals(req.httpRequestType())) {
            String param = req.getParam();
            queue.putIfAbsent(sourceName, new ConcurrentLinkedQueue<>());
            queue.get(sourceName).add(param);
            return new Resp(String.format("source name: %s, param: %s", req.getSourceName(), req.getParam()), "200");
        } else if ("GET".equals(req.httpRequestType())) {
            ConcurrentLinkedQueue<String> messages = queue.getOrDefault(sourceName, new ConcurrentLinkedQueue<>());
            String message = messages.poll();
            if (message != null) {
                return new Resp(message, "200");
            } else {
                return new Resp("Queue is empty", "200");
            }
        }
        return new Resp("Invalid request", "400");
    }
}
