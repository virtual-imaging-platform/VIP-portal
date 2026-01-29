package fr.insalyon.creatis.vip.core.server.business;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.insalyon.creatis.vip.core.server.model.PrecisePage;

@Service
public class PageBuilder {
    
    public <T> PrecisePage<T> doPrecise(int offset, int quantity, List<T> data) {
        PrecisePage<T> page = new PrecisePage<>();
        int dataSize = data.size();
        int endIndex = Math.min(offset + quantity, dataSize);

        // if the offest exceed the data size, then an empty List<T>
        // will be returned 
        if (offset > dataSize) {
            offset = dataSize;
        }

        page.data = data.subList(offset, endIndex);
        page.total = dataSize;
        return page;
    }

    // later we can introduce doUnprecise method that return a different object
    // this will be usefull for workflows/executions since there are a lot of elements
    // and we don't want to load everything in memory and use SQL query LIMIT
}
