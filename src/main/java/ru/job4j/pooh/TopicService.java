package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {
    private final ConcurrentMap<String, ConcurrentLinkedQueue<String>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String topicName = req.getSourceName();
        String param = req.getParam();

        if ("POST".equals(req.httpRequestType())) {
            addToTopic(topicName, param);
            return new Resp(String.format("source name: %s, param: %s", req.getSourceName(), req.getParam()), "200");
        } else if ("GET".equals(req.httpRequestType())) {
            String message = removeFromTopic(topicName);
            return message != null ? new Resp(message, "200") : new Resp("", "200");
        }
        return new Resp("Invalid request", "400");
    }

    private void addToTopic(String topicName, String message) {
        topics.putIfAbsent(topicName, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<String> topic = topics.get(topicName);
        topic.offer(message);
    }

    private String removeFromTopic(String topicName) {
        ConcurrentLinkedQueue<String> topic = topics.getOrDefault(topicName, null);
        if (topic != null) {
            return topic.poll();
        }
        return null;
    }
}