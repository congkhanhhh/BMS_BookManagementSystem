package com.bookstore.project.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class FileInfo implements Serializable {

    private String name;

    private String url;

    private Long size;
}
