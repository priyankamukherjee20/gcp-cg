package com.test.gcp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader<T> {
    private Class<T> model;

    public JsonReader(Class<T> model) {
        this.model = model;
    }

    public T loadTestJson(String fileName) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        return new ObjectMapper().readValue(file, this.model);
    }
    
    public List<T> loadTestJsonArray(String fileName) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        return new ObjectMapper().readValue(file, new TypeReference<ArrayList<T>>() {});
    }
}
