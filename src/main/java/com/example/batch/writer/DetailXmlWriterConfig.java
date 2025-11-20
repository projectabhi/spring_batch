package com.example.batch.writer;

import com.example.batch.model.Detail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.core.configuration.annotation.StepScope;

@Configuration
public class DetailXmlWriterConfig {

    @Value("${details.temp.file}")
    private String detailsTempFile;

    @Bean
    public Jaxb2Marshaller detailMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Detail.class);
        return marshaller;
    }

    @Bean(destroyMethod = "")
    @StepScope
    public StaxEventItemWriter<Detail> detailXmlWriter(Jaxb2Marshaller detailMarshaller) throws Exception {
        StaxEventItemWriter<Detail> writer = new StaxEventItemWriter<>();
        writer.setMarshaller(detailMarshaller);
        writer.setRootTagName("details");
        //writer.setItemTagName("detail");
        writer.setResource(new FileSystemResource(detailsTempFile));
        writer.afterPropertiesSet();
        return writer;
    }
}