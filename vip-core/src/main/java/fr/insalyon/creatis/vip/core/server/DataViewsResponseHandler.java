package fr.insalyon.creatis.vip.core.server;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import fr.insalyon.creatis.vip.core.models.User;
import fr.insalyon.creatis.vip.core.server.inter.DataViews;

@RestControllerAdvice
@SuppressWarnings("removal") 
public class DataViewsResponseHandler extends AbstractMappingJacksonResponseBodyAdvice {
    private final Supplier<User> supplier;

    @Autowired
    public DataViewsResponseHandler(Supplier<User> supplier) {
        this.supplier = supplier;
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
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
        bodyContainer.setSerializationView(view);
    }
}
