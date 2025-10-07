package com.darunkar.design_patterns;

import java.util.ArrayList;
import java.util.List;

//Component Interface ( common for both file and folder)
interface FileComponent{
    void showDetails(String indent);
    int getSize();
}

// implementation of file
class FileLeaf implements FileComponent{
    private String name;
    private int size;

    public FileLeaf(String name, int size){
        this.name = name;
        this.size = size;
    }
    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "📄 " + name + " (" + size + "KB)");
    }

    @Override
    public int getSize() {
        return size;
    }
}

class FolderComposite implements FileComponent{

    private String name;
    private List<FileComponent> children = new ArrayList<>();
    public FolderComposite(String name){
        this.name = name;
    }

    @Override
    public void showDetails(String indent) {
        System.out.println(indent + "📁 " + name + "/");
        for(FileComponent child: children) {
            child.showDetails(indent + "  ");
        }
    }

    @Override
    public int getSize() {
        int totalSize = 0;
        for(FileComponent child : children){
            totalSize += child.getSize();
        }
        return totalSize;
    }

    public void add(FileComponent fileComponent){
        children.add(fileComponent);
    }

    public void remove(FileComponent fileComponent){
        children.remove(fileComponent);
    }
}


public class ComposeFileSystemDemo {

    public static void main(String[] args) {

        FileComponent file1 = new FileLeaf("file1", 100);
        FileComponent file2 = new FileLeaf("file2", 200);
        FileComponent file3 = new FileLeaf("file3", 300);

        FolderComposite documentFolder = new FolderComposite("Documents");
        FolderComposite imageFolder = new FolderComposite("Images");

        documentFolder.add(file1);
        documentFolder.add(file2);

        imageFolder.add(file3);

        FolderComposite rootFolder = new FolderComposite("root");
        rootFolder.add(documentFolder);
        rootFolder.add(imageFolder);

        rootFolder.add(new FileLeaf("file10", 10));
        rootFolder.showDetails("");

        System.out.println("\nSize of root folder " + rootFolder.getSize());

    }
}
