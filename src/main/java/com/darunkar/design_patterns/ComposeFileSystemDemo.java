package com.darunkar.design_patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Component Interface ( common for both file and folder)
interface FileComponent{
    void showDetails(String indent);
    int getSize();
    FileComponent search(String name);
    int countFiles();
    boolean delete(String name);
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

    @Override
    public FileComponent search(String name) {
        return this.name.equals(name) ? this : null;
    }

    @Override
    public int countFiles() {
        return 1;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean delete(String name) {
        return false;
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

    public FileComponent search(String name) {

        if(this.name.equals(name)) return this;
        for(FileComponent child: children){
            FileComponent found = child.search(name);
            if(found != null) return found;
        }
        return null;
    }

    @Override
    public int countFiles() {
        int count = 0;
        for(FileComponent child : children){
            count+=1;
        }
        return count;
    }

    @Override
    public boolean delete(String name) {

        Iterator<FileComponent> it = children.iterator();

        while(it.hasNext()){
            FileComponent child = it.next();

            if(child instanceof FileLeaf file && file.getName().equals(name)){
                it.remove();
                System.out.println("File is deleted ! " +  name);
                return true;
            }else if(child instanceof FolderComposite folder){
                if(folder.name.equals(name)){
                    it.remove();
                    System.out.println("Folder is deleted ! " +  name);
                    return true;
                }else if(folder.delete(name)){
                    return true;
                }
            }
        }

        return false;

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

        String seachFile="file1";
        FileComponent foundFile=rootFolder.search(seachFile);
        System.out.println();
        System.out.println(foundFile != null ? seachFile + " Found" : "Not Found");

        System.out.println("\nSize of root folder " + rootFolder.getSize());

        rootFolder.delete("Images");
        rootFolder.delete("file2");

        rootFolder.showDetails("");
        rootFolder.countFiles();
        System.out.println("\nSize of root folder " + rootFolder.getSize());



    }
}
