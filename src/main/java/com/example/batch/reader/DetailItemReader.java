package com.example.batch.reader;

import com.example.batch.model.Detail;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

@Component
@Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DetailItemReader implements ItemReader<Detail> {

    @Value("${input.file}")
    private String inputFilePath;

    private Iterator<String> iterator;

    @Override
    public Detail read() throws Exception {
        if (iterator == null) {
            List<String> allLines = Files.readAllLines(Path.of(inputFilePath), StandardCharsets.UTF_8)
                    .stream().map(s -> s.replace("\r", "").replace("\n", ""))
                    .filter(s -> !s.isEmpty())
                    .toList();
            if (allLines.size() <= 2) {
                return null;
            }
            // Only details (skip header first line and footer last line)
            List<String> detailLines = allLines.subList(1, allLines.size() - 1);
            iterator = detailLines.iterator();
        }

        if (!iterator.hasNext()) {
            return null;
        }

        String line = normalize(iterator.next(), 17);
        return parseDetail(line);
    }

    private String normalize(String line, int expectedLen) {
        String l = line.replace("\r", "").replace("\n", "");
        if (l.length() == expectedLen + 1 && l.startsWith(" ")) {
            return l.substring(1);
        }
        return l;
    }

    private Detail parseDetail(String line) {
        if (line.length() < 17) {
            throw new IllegalArgumentException("Detail line too short: len=" + line.length());
        }
        String seq = line.substring(0, 2);
        String firstName = line.substring(2, 10).trim();
        String lastName = line.substring(10, 13).trim();
        String department = line.substring(13, 17).trim();
        return new Detail(seq, firstName, lastName, department);
    }
}