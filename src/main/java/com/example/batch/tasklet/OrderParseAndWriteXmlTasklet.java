package com.example.batch.tasklet;

import com.example.batch.model.OrderDetail;
import com.example.batch.model.OrderFile;
import com.example.batch.model.OrderFooter;
import com.example.batch.model.OrderHeader;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderParseAndWriteXmlTasklet implements Tasklet {

    @Value("${input.file}")
    private String inputFilePath;

    @Value("${output.file}")
    private String outputFilePath;

    @Override
    public org.springframework.batch.repeat.RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path path = Path.of(inputFilePath);
        List<String> rawLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> lines = new ArrayList<>();
        for (String ln : rawLines) {
            String trimmed = ln.replace("\r", "").replace("\n", "");
            if (!trimmed.isEmpty()) {
                lines.add(trimmed);
            }
        }
        if (lines.size() < 2) {
            throw new IllegalArgumentException("File must contain at least a header and a footer.");
        }

        String headerLine = normalize(lines.get(0), 15);
        String footerLine = normalize(lines.get(lines.size() - 1), 4);
        List<String> detailLines = new ArrayList<>(lines.subList(1, lines.size() - 1));

        OrderHeader header = parseHeader(headerLine);
        OrderFooter footer = parseFooter(footerLine);
        List<OrderDetail> details = new ArrayList<>();
        for (String d : detailLines) {
            details.add(parseDetail(normalize(d, 16)));
        }
        if (footer.getCount() != null && footer.getCount() != details.size()) {
            throw new IllegalStateException("Footer count does not match details size");
        }

        OrderFile orderFile = new OrderFile(header, details, footer);
        JAXBContext jaxbContext = JAXBContext.newInstance(OrderFile.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        File out = Path.of(outputFilePath).toFile();
        marshaller.marshal(orderFile, out);
        return org.springframework.batch.repeat.RepeatStatus.FINISHED;
    }

    private String normalize(String line, int expectedLen) {
        String l = line.replace("\r", "").replace("\n", "");
        if (l.length() == expectedLen + 1 && l.startsWith(" ")) {
            return l.substring(1);
        }
        return l;
    }

    private OrderHeader parseHeader(String line) {
        if (line.length() < 15) {
            throw new IllegalArgumentException("Header line too short: " + line.length());
        }
        String seq = line.substring(0, 2);
        String date = line.substring(2, 10);
        String type = line.substring(10, 15).trim();
        return new OrderHeader(seq, date, type);
    }

    private OrderDetail parseDetail(String line) {
        if (line.length() < 16) {
            throw new IllegalArgumentException("Detail line too short: " + line.length());
        }
        String seq = line.substring(0, 2);
        String item = line.substring(2, 7).trim();
        int amount = Integer.parseInt(line.substring(7, 12));
        int precision = Integer.parseInt(line.substring(12, 14));
        int quantity = Integer.parseInt(line.substring(14, 16));
        return new OrderDetail(seq, item, amount, precision, quantity);
    }

    private OrderFooter parseFooter(String line) {
        if (line.length() < 4) {
            throw new IllegalArgumentException("Footer line too short: " + line.length());
        }
        String seq = line.substring(0, 2);
        Integer count = Integer.parseInt(line.substring(2, 4));
        return new OrderFooter(seq, count);
    }
}