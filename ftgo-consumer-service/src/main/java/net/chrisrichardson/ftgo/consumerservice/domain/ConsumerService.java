package net.chrisrichardson.ftgo.consumerservice.domain;

import io.eventuate.tram.events.ResultWithEvents;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import net.chrisrichardson.ftgo.common.Money;
import net.chrisrichardson.ftgo.common.PersonName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ConsumerService {

  @Autowired
  private ConsumerRepository consumerRepository;

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  public void validateOrderForConsumer(long consumerId, Money orderTotal) {
    Optional<Consumer> consumer = consumerRepository.findById(consumerId);
    consumer.orElseThrow(ConsumerNotFoundException::new).validateOrderByConsumer(orderTotal);
  }

  public ResultWithEvents<Consumer> create(PersonName name) {
    ResultWithEvents<Consumer> rwe = Consumer.create(name);
    consumerRepository.save(rwe.result);
    domainEventPublisher.publish(Consumer.class, rwe.result.getId(), rwe.events);
    return rwe;
  }
}
