package cn.cibnmp.ott.bean;

import java.io.Serializable;

/**
 * Created by wanqi on 2017/3/9.
 */

public class NavigationBlock implements Serializable{

    private int blockId;

    private String name;

    private int nameType;

    private int position;

    private int positionCount;

    private int state;

    private BlockLayout layout;

    public NavigationBlock() {
    }

    public void setBlockId(int blockId){
        this.blockId = blockId;
    }
    public int getBlockId(){
        return this.blockId;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setNameType(int nameType){
        this.nameType = nameType;
    }
    public int getNameType(){
        return this.nameType;
    }
    public void setPosition(int position){
        this.position = position;
    }
    public int getPosition(){
        return this.position;
    }
    public void setPositionCount(int positionCount){
        this.positionCount = positionCount;
    }
    public int getPositionCount(){
        return this.positionCount;
    }
    public void setState(int state){
        this.state = state;
    }
    public int getState(){
        return this.state;
    }
    public void setLayout(BlockLayout layout){
        this.layout = layout;
    }
    public BlockLayout getLayout(){
        return this.layout;
    }

    @Override
    public String toString() {
        return "NavigationBlock{" +
                "blockId=" + blockId +
                ", name='" + name + '\'' +
                ", nameType=" + nameType +
                ", position=" + position +
                ", positionCount=" + positionCount +
                ", state=" + state +
                ", layout=" + layout +
                '}';
    }
}
