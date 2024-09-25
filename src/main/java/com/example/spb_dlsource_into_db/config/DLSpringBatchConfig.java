package com.example.spb_dlsource_into_db.config;

import com.example.spb_dlsource_into_db.entity.DLCustomer;
import com.example.spb_dlsource_into_db.repository.DLCustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Configuration
public class DLSpringBatchConfig{
     private final JobRepository jobRepository;
     private final DLCustomerRepository dlCustomerRopository;
     private final PlatformTransactionManager transactionManager;

     public DLSpringBatchConfig(JobRepository jobRepository, DLCustomerRepository dlCustomerRopository,
                                PlatformTransactionManager transactionManager) {
         this.jobRepository = jobRepository;
         this.dlCustomerRopository = dlCustomerRopository;
         this.transactionManager = transactionManager;
     }

     @Bean
    public FlatFileItemReader<DLCustomer> reader() {
         FlatFileItemReader<DLCustomer> reader = new FlatFileItemReader<>();
         reader.setResource(new FileSystemResource("src/main/resources/DLCustomers.csv"));
         reader.setName("Reader_DLCustomers");
//         reader.setLinesToSkip(1);
         reader.setLineMapper(lineMapper());
         return reader;
     }

     private LineMapper<DLCustomer> lineMapper() {
         DefaultLineMapper<DLCustomer>lineMapper = new DefaultLineMapper<>();
         //read
         DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
         tokenizer.setDelimiter("|");
         tokenizer.setStrict(false);
         tokenizer.setNames(new String[] {"uid", "Customer_name", "External_Id",
                 "Gender","Registration_Date" });
         //Date_of_Registration
         //map with opject bean mapper
         BeanWrapperFieldSetMapper<DLCustomer> wrapperFileMapper = new BeanWrapperFieldSetMapper<>();
         wrapperFileMapper.setTargetType(DLCustomer.class);

//          Handle custom date format if necessary
         wrapperFileMapper.setCustomEditors(Collections.singletonMap(LocalDateTime.class,
                 new PropertyEditorSupport() {
                     @Override
                     public void setAsText(String text){
                         setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                     }
                 }
         ));
         lineMapper.setFieldSetMapper(wrapperFileMapper);
         lineMapper.setLineTokenizer(tokenizer);
         return lineMapper;
     }

     @Bean
    public DLCustomerProcessor processor(){
         return new DLCustomerProcessor();
     }

     @Bean
    public RepositoryItemWriter<DLCustomer> writer(){
         RepositoryItemWriter<DLCustomer> writer = new RepositoryItemWriter<>();
         writer.setRepository(dlCustomerRopository);
         writer.setMethodName("save");
         return writer;
     }
     @Bean
     public Step step1(){
         return new StepBuilder("step-csv",jobRepository)
                 .<DLCustomer,DLCustomer>chunk(10,transactionManager)
                 .reader(reader())
                 .processor(processor())
                 .writer(writer())
                 .taskExecutor(taskExecutor())
                 .build();
     }
     @Bean
    public Job runJob(){
          return new JobBuilder("ImportDLCustomer",jobRepository)
                  .start(step1()).build();
     }

     @Bean
    public TaskExecutor taskExecutor(){
          SimpleAsyncTaskExecutor asyncExecutor = new SimpleAsyncTaskExecutor();
          asyncExecutor.setConcurrencyLimit(10);
          return asyncExecutor;
     }

}
