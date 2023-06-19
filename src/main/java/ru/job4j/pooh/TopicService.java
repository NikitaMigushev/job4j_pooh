package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {
    private final ConcurrentMap<String,
            ConcurrentMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String topicName = req.getPoohMode();
        String recipientName = req.getSourceName();
        String param = req.getParam();

        if ("POST".equals(req.httpRequestType())) {
            addToTopic(topicName, recipientName, param);
            return new Resp(String.format(
                    "topic: %s, recipient: %s, param: %s",
                    topicName, recipientName, param), "200");
        } else if ("GET".equals(req.httpRequestType())) {
            String message = removeFromTopic(topicName, recipientName);
            return new Resp(message != null ? message : "", "200");
        }

        return new Resp("Invalid request", "400");
    }

    private void addToTopic(String topicName, String recipientName, String message) {
        topics.putIfAbsent(topicName, new ConcurrentHashMap<>());
        ConcurrentMap<String, ConcurrentLinkedQueue<String>> topic = topics.get(topicName);
        topic.putIfAbsent(recipientName, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<String> recipientQueue = topic.get(recipientName);
        recipientQueue.offer(message);
    }

    private String removeFromTopic(String topicName, String recipientName) {
        ConcurrentMap<String, ConcurrentLinkedQueue<String>> topic = topics.getOrDefault(topicName, null);
        if (topic != null) {
            ConcurrentLinkedQueue<String> recipientQueue = topic.getOrDefault(recipientName, null);
            if (recipientQueue != null) {
                return recipientQueue.poll();
            }
        }
        return null;
    }
}