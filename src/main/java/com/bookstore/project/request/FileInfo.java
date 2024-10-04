package com.bookstore.project.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class FileInfo implements Serializable {

    private String fileName;
    private String path;

    public FileInfo(String name, String absolutePath) {
    }
}
