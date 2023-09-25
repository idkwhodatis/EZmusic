package com.idkwhodatis.ezmusic;

public class SongList{
    public String cover;
    public String name;
    public int songCount;

    public SongList(){
        cover="";
        name="";
        songCount=0;
    }
    public SongList(String cover,String name,int songCount){
        this.cover=cover;
        this.name=name;
        this.songCount=songCount;
    }
}
