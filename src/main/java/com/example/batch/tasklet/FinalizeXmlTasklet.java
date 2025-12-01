package com.example.batch.tasklet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class FinalizeXmlTasklet implements Tasklet {

  @Value("${output.file}")
  private String outputFilePath;

  @Value("${details.temp.file}")
  private String detailsTempFile;

  @Override
  public org.springframework.batch.repeat.RepeatStatus execute(
      StepContribution contribution, ChunkContext chunkContext) throws Exception {
    var ctx =
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    String fileSeq = ctx.getString("fileSeq");
    String date = ctx.getString("date");
    String fileName = ctx.getString("fileName");
    int recordCount = ctx.getInt("recordCount");

    String detailsXml = readDetailsXmlRemovingProlog(detailsTempFile);

    try (BufferedWriter bw =
        Files.newBufferedWriter(
            new FileSystemResource(outputFilePath).getFile().toPath(), StandardCharsets.UTF_8)) {
      bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      bw.write("<customerFile>\n");
      bw.write("  <header>\n");
      bw.write("    <fileSeq>" + escapeXml(fileSeq) + "</fileSeq>\n");
      bw.write("    <date>" + escapeXml(date) + "</date>\n");
      bw.write("    <fileName>" + escapeXml(fileName) + "</fileName>\n");
      bw.write("  </header>\n");
      // Include details as-is (already has <details>...</details>)
      bw.write(detailsXml);
      bw.write("\n");
      bw.write("  <footer>\n");
      bw.write("    <fileSeq>" + escapeXml(fileSeq) + "</fileSeq>\n");
      bw.write("    <recordCount>" + recordCount + "</recordCount>\n");
      bw.write("  </footer>\n");
      bw.write("</customerFile>\n");
    }

    return org.springframework.batch.repeat.RepeatStatus.FINISHED;
  }

  private String readDetailsXmlRemovingProlog(String path) throws IOException {
    var lines =
        Files.readAllLines(new FileSystemResource(path).getFile().toPath(), StandardCharsets.UTF_8);
    StringBuilder sb = new StringBuilder();
    for (String l : lines) {
      String line = l.replace("\r", "");
      if (line.startsWith("<?xml")) {
        continue; // remove XML declaration
      }
      sb.append(line).append("\n");
    }
    return sb.toString().trim();
  }

  private String escapeXml(String s) {
    if (s == null) return "";
    return s.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;");
  }
}
