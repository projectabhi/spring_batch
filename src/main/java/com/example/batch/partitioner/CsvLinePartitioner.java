package com.example.batch.partitioner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

@Slf4j
public class CsvLinePartitioner implements Partitioner {

  private final String filePath;

  public CsvLinePartitioner(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    Map<String, ExecutionContext> result = new HashMap<>();
    try {
      // Count total rows minus the header
      long totalLines = Files.lines(Paths.get(filePath)).count() - 1;
      long targetSize = totalLines / gridSize;
      long remaining = totalLines % gridSize;

      long startLine = 1; // Line index 0 is skipped as the header

      for (int i = 0; i < gridSize; i++) {
        ExecutionContext context = new ExecutionContext();
        long linesToRead = targetSize + (i < remaining ? 1 : 0);

        context.putLong("startingIndex", startLine);
        context.putLong("itemCount", linesToRead);

        result.put("partition" + i, context);
        log.info("Partition {}: startingIndex={}, itemCount={}", i, startLine, linesToRead);
        startLine += linesToRead;
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read CSV file to partition", e);
    }
    return result;
  }
}
