package com.drxx.drfilemanager.model;

public class FileInfo {
    public FileInfo(String fileName, String filePath, String fileType) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public FileInfo(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public FileInfo(String filePath) {
        this.filePath = filePath;
    }

    private String fileName;
    private String filePath;
    private String fileType;
    private boolean isChoose;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileInfo{" + '\n' +
                "fileName='" + fileName + '\n' +
                ", filePath='" + filePath + '\n' +
                ", isChoose=" + isChoose + '\n' +
                '}';
    }
}
