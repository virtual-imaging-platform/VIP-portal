package fr.insalyon.creatis.vip.core.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.inter.DataViews;

@RestControllerAdvice
@SuppressWarnings("removal") 
public class DataViewsRequestHandler extends RequestBodyAdviceAdapter {
    private final Supplier<User> supplier;

    @Autowired
    public DataViewsRequestHandler(Supplier<User> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        // verify that it concerns only "JSON" requests
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }
    
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        final User currentUser = supplier.get();
        Class<?> view = DataViews.User.class;

        // currentUser can be null if unauthorized context
        if (currentUser != null) {
            switch (currentUser.getLevel()) {
                case Administrator:
                    view = DataViews.Admin.class;
                    break;
                case Developer:
                    view = DataViews.Developer.class;
                    break;
                default:
                    break;
            }
        }

        // this ensure the good behavior of JsonView for input filtering based on user level
        // it is very similar to DataViewsResponseHandler but a little bit trickier since 
        // there is no existing abstract classes for Jackson Request Handler
        return new MappingJacksonInputMessage(inputMessage.getBody(), inputMessage.getHeaders(), view);
    }
}