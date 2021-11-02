package com.example.demo.stub.account.util;

import com.example.demo.stub.account.exception.InternalServerError;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.TextFile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SpringClasspathResourceFileSource implements FileSource {


    private final String path;

    public SpringClasspathResourceFileSource(final String path) {
        this.path = path;
    }

    @Override
    public URI getUri() {
        return getURI(path);
    }

    @Override
    public FileSource child(final String subDirectoryName) {
        return new SpringClasspathResourceFileSource(appendToPath(subDirectoryName));
    }

    @Override
    public BinaryFile getBinaryFileNamed(final String name) {
        return new BinaryFile(getURI(appendToPath(name)));
    }

    @Override
    public TextFile getTextFileNamed(final String name) {
        return new TextFile(getURI(appendToPath(name)));
    }

    private URI getURI(final String name) {
        try {
            return new ClassPathResource(name).getURI();
        } catch (IOException root) {
            throw new InternalServerError(root.getMessage());
        }
    }

    private String appendToPath(final String name) {
        return path + "/" + name;
    }

    @Override
    public void createIfNecessary() {
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public List<TextFile> listFilesRecursively() {
        return new ArrayList<>();
    }

    @Override
    public void writeTextFile(final String name, final String contents) {
    }

    @Override
    public void writeBinaryFile(final String name, final byte[] contents) {
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public void deleteFile(final String name) {
    }
}
