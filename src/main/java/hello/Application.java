package hello;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static String baseRUI = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/input";

    private static String baseRUI1 = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/output";

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {

            //			Quote quote = restTemplate.getForObject(
//					"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//			log.info(quote.toString());
            try {

                ResponseEntity<Input[]> response = restTemplate.exchange(baseRUI, HttpMethod.GET, new HttpEntity(createHeader(true)), Input[].class);

                List<Input> inputList = Arrays.asList(response.getBody());

                log.info(String.valueOf(inputList.size()));
                
                
                
                


                output outpt = new output();

                outpt.setOutput(getActiveProductsCount(inputList));
                
                ObjectMapper objectMapper = new ObjectMapper();
    	//Set pretty printing of json
    	objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                String mapToJson = objectMapper.writeValueAsString(outpt);
    	System.out.println("1. Convert Map to JSON :");
    	System.out.println(mapToJson);
                
                HttpEntity<output> entity1 = new HttpEntity(outpt, createHeader(true));
                
                

                // send request and parse result
                ResponseEntity<String> response1 = restTemplate.exchange(baseRUI1, HttpMethod.POST, entity1, String.class);

                System.out.println(response);

            } catch (Exception e) {

                e.printStackTrace();
            }

        };
    }

    private HttpHeaders createHeader(boolean isGet) {
        if (isGet) {
            return new HttpHeaders() {
                {
                    set("UserID", "HJ7B79HwG");
                }
            };
        } else {

            return new HttpHeaders() {
                {
                    set("UserID", "HJ7B79HwG");
                    setContentType(MediaType.APPLICATION_JSON);
                }
            };

        }
    }
    
    
   private static Map<String,Integer> getActiveProductsCount(List<Input> inputList){
       
       int count = 0;
       
       Date today = new Date();
       
       Map<String,Integer> categoryCount = new HashMap();
       
       for(Input in : inputList){
           
           log.info(in.toString());
           
           String catName = in.getCategory();
           
           if(in.getEndDate() == null){              
               
               
               if(categoryCount.get(catName) == null){
                    categoryCount.put(in.getCategory(), 1);
               }else{
                   categoryCount.put(in.getCategory(), categoryCount.get(in.getCategory()) + 1);
                   
               }
              
               count += 1;
           }else if(in.getStartDate().compareTo(today) * today.compareTo(in.getEndDate()) >= 0){
               
                 if(categoryCount.get(catName) == null){
                    categoryCount.put(in.getCategory(), 1);
               }else{
                   categoryCount.put(in.getCategory(), categoryCount.get(in.getCategory()) + 1);
                   
               }
               
               count += 1;
               
           }
       }
       
       log.info("Count "+count);
       
       return categoryCount;
       
   }
}
