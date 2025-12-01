package com.example.batch.processor;

import com.example.batch.model.Detail;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class DetailItemProcessor implements ItemProcessor<Detail, Detail> {
  @Override
  public Detail process(Detail item) {
    // No-op; you could add transformations/validations here
    return item;
  }
}
