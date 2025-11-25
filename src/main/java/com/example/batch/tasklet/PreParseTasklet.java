package com.example.batch.tasklet;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PreParseTasklet implements Tasklet {

  @Value("${input.file}")
  private String inputFilePath;

  @Override
  public org.springframework.batch.repeat.RepeatStatus execute(
      StepContribution contribution, ChunkContext chunkContext) throws Exception {
    Path path = Path.of(inputFilePath);
    List<String> lines =
        Files.readAllLines(path, StandardCharsets.UTF_8).stream()
            .map(s -> s.replace("\r", "").replace("\n", ""))
            .filter(s -> !s.isEmpty())
            .toList();

    if (lines.size() < 2) {
      throw new IllegalArgumentException("File must contain at least header and footer.");
    }

    // Header (len 18): [0:2] fileSeq, [2:10] date, [10:18] fileName
    String headerLine = normalize(lines.get(0), 18);
    String fileSeqHeader = headerLine.substring(0, 2);
    String date = headerLine.substring(2, 10);
    String fileName = headerLine.substring(10, 18).trim();

    // Footer (len 4): [0:2] fileSeq, [2:4] record count
    String footerLine = normalize(lines.get(lines.size() - 1), 4);
    String fileSeqFooter = footerLine.substring(0, 2);
    int recordCount;
    try {
      recordCount = Integer.parseInt(footerLine.substring(2, 4));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid footer record count: " + footerLine.substring(2, 4));
    }

    // Validate file seq and counts against detail lines
    int detailLinesCount = Math.max(lines.size() - 2, 0);
    if (recordCount != detailLinesCount) {
      throw new IllegalStateException(
          "Footer record count ("
              + recordCount
              + ") does not match detail lines ("
              + detailLinesCount
              + ")");
    }
    if (!fileSeqHeader.equals(fileSeqFooter)) {
      throw new IllegalStateException(
          "Header and Footer file sequence mismatch: " + fileSeqHeader + " vs " + fileSeqFooter);
    }

    // Store in JobExecutionContext for later steps
    var jobContext =
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    jobContext.putString("fileSeq", fileSeqHeader);
    jobContext.putString("date", date);
    jobContext.putString("fileName", fileName);
    jobContext.putInt("recordCount", recordCount);

    return org.springframework.batch.repeat.RepeatStatus.FINISHED;
  }

  private String normalize(String line, int expectedLen) {
    String l = line.replace("\r", "").replace("\n", "");
    if (l.length() == expectedLen + 1 && l.startsWith(" ")) {
      return l.substring(1);
    }
    return l;
  }
}
