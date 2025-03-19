package nl.rabobank.transactionverifier.config;

import nl.rabobank.transactionverifier.model.transaction.Transaction;
import nl.rabobank.transactionverifier.service.validator.TransactionEndAmountValidator;
import nl.rabobank.transactionverifier.service.validator.TransactionUniqueReferenceIdValidator;
import nl.rabobank.transactionverifier.service.validator.Validator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        // other configuration
        return messageSource;
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }
    
    @Bean
    public List<Validator<Transaction>> transactionValidators() {
        return List.of(new TransactionEndAmountValidator(), new TransactionUniqueReferenceIdValidator());
    }
}

