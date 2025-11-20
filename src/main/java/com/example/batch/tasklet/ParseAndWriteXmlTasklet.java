package com.example.batch.tasklet;

import com.example.batch.model.CustomerFile;
import com.example.batch.model.Detail;
import com.example.batch.model.Footer;
import com.example.batch.model.Header;
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
public class ParseAndWriteXmlTasklet implements Tasklet {

    @Value("${input.file}")
    private String inputFilePath;

    @Value("${output.file}")
    private String outputFilePath;

    @Override
    public org.springframework.batch.repeat.RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path path = Path.of(inputFilePath);
        List<String> rawLines = Files.readAllLines(path, StandardCharsets.UTF_8);

        // Remove empty lines
        List<String> lines = new ArrayList<>();
        for (String ln : rawLines) {
            String trimmed = ln.replaceAll("\\r?\\n", "");
            if (!trimmed.isEmpty()) {
                lines.add(trimmed);
            }
        }

        if (lines.size() < 2) {
            throw new IllegalArgumentException("File must contain at least a header and a footer.");
        }

        String headerLine = normalizeLine(lines.get(0), 18);
        String footerLine = normalizeLine(lines.get(lines.size() - 1), 4);
        List<String> detailLines = new ArrayList<>(lines.subList(1, lines.size() - 1));

        Header header = parseHeader(headerLine);
        Footer footer = parseFooter(footerLine);
        List<Detail> details = new ArrayList<>();
        for (String d : detailLines) {
            details.add(parseDetail(normalizeLine(d, 17)));
        }

        // Validate counts
        if (footer.getRecordCount() != null && footer.getRecordCount() != details.size()) {
            throw new IllegalStateException(
                String.format("Footer record count (%d) does not match parsed details (%d)",
                    footer.getRecordCount(), details.size())
            );
        }

        CustomerFile customerFile = new CustomerFile(header, details, footer);

        // Write XML
        JAXBContext jaxbContext = JAXBContext.newInstance(CustomerFile.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        File out = Path.of(outputFilePath).toFile();
        marshaller.marshal(customerFile, out);

        return org.springframework.batch.repeat.RepeatStatus.FINISHED;
    }

    private String normalizeLine(String line, int expectedLen) {
        // Drop Windows line endings, tolerate a single leading space
        String l = line.replace("\r", "").replace("\n", "");
        if (l.length() == expectedLen + 1 && l.startsWith(" ")) {
            return l.substring(1);
        }
        return l;
    }

    private Header parseHeader(String line) {
        if (line.length() < 18) {
            throw new IllegalArgumentException("Header line too short: " + line.length());
        }
        String fileSeq = line.substring(0, 2);
        String date = line.substring(2, 10); // ddMMyyyy
        String fileName = line.substring(10, 18).trim();
        return new Header(fileSeq, date, fileName);
    }

    private Detail parseDetail(String line) {
        if (line.length() < 17) {
            throw new IllegalArgumentException("Detail line too short: " + line.length());
        }
        String seq = line.substring(0, 2);
        String firstName = line.substring(2, 10).trim();
        String lastName = line.substring(10, 13).trim();
        String department = line.substring(13, 17).trim();
        return new Detail(seq, firstName, lastName, department);
    }

    private Footer parseFooter(String line) {
        if (line.length() < 4) {
            throw new IllegalArgumentException("Footer line too short: " + line.length());
        }
        String fileSeq = line.substring(0, 2);
        String recStr = line.substring(2, 4);
        Integer count = null;
        try {
            count = Integer.parseInt(recStr);
        } catch (NumberFormatException ignored) {}
        return new Footer(fileSeq, count);
    }
}