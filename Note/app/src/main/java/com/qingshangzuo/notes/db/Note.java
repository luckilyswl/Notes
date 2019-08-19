package com.qingshangzuo.notes.db;

import org.litepal.crud.LitePalSupport;

public class Note extends LitePalSupport {
    private int id;
    private String writeContent;

    public void setId(int id){
        this.id = id;
    }

    public void setWriteContent(String writeContent){
        this.writeContent = writeContent;
    }

    public int getId(){
        return id;
    }

    public String getWriteContent(){
        return writeContent;
    }

}
