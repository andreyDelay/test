package ru.tele2.andrey.zookeeper.model.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Данный класс использовался для пропагации заголовков до внедрения библиотеки
 * В данный момент в нём нет необходимости.
 * Не используется!
 */
public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String key, String value) {
        assertNotNull(key);
        this.customHeaders.put(key, value);
    }

    @Override
    public String getHeader(String name) {
        String value = customHeaders.get(name);
        if (value != null) {
            return value;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(customHeaders.keySet());
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (customHeaders.containsKey(name)) {
            values.add(customHeaders.get(name));
        }
        return Collections.enumeration(values);
    }

    private void assertNotNull(String key) {
        if (key == null)
            throw new IllegalArgumentException("Header key cannot be null.");
    }

}
