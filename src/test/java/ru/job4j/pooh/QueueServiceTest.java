package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        /* Добавляем данные в очередь weather. Режим queue */
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        /* Забираем данные из очереди weather. Режим queue */
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenPostThenGetQueueTwoTopics() {
        QueueService queueService = new QueueService();
        queueService.process(
                new Req("POST", "queue", "weather", "temperature=18")
        );
        queueService.process(
                new Req("POST", "queue", "travel", "Austria")
        );
        Resp resultWeather = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp resultTravel = queueService.process(
                new Req("GET", "queue", "travel", null)
        );
        assertThat(resultWeather.text(), is("temperature=18"));
        assertThat(resultTravel.text(), is("Austria"));
    }
}