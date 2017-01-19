package app.com.bongdadayroi.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tuan on 28/03/2016.
 */
public class MyData implements Serializable{

    public static volatile MyData myData = null;

    private ArrayList<MyVideo> arrayHome, arrayNew, arrayMost, arrayResult;

    private ArrayList<ArrayList<MyVideo>> arrayListData = new ArrayList<ArrayList<MyVideo>>();

    public ArrayList<ArrayList<MyVideo>> getArrayListData() {
        return arrayListData;
    }

    private ArrayList<MyVideo> arrayRelate = new ArrayList<>();
    private ArrayList<Comment> arrayComment = new ArrayList<>();

    public ArrayList<Comment> getArrayComment() {
        return arrayComment;
    }

    public void setArrayComment(ArrayList<Comment> arrayComment) {
        this.arrayComment = arrayComment;
    }

    public ArrayList<MyVideo> getArrayRelate() {
        return arrayRelate;
    }

    public void setArrayRelate(ArrayList<MyVideo> arrayRelate) {
        this.arrayRelate = arrayRelate;
    }

    public void setArrayListData(ArrayList<ArrayList<MyVideo>> arrayListData) {
        this.arrayListData = arrayListData;
    }

    public ArrayList<MyVideo> getArrayResult() {
        return arrayResult;
    }

    public void setArrayResult(ArrayList<MyVideo> arrayResult) {
        this.arrayResult = arrayResult;
    }

    public ArrayList<MyVideo> getArrayHome() {
        return arrayHome;
    }

    public void setArrayHome(ArrayList<MyVideo> arrayHome) {
        this.arrayHome = arrayHome;
    }

    public ArrayList<MyVideo> getArrayNew() {
        return arrayNew;
    }

    public void setArrayNew(ArrayList<MyVideo> arrayNew) {
        this.arrayNew = arrayNew;
    }

    public ArrayList<MyVideo> getArrayMost() {
        return arrayMost;
    }

    public void setArrayMost(ArrayList<MyVideo> arrayMost) {
        this.arrayMost = arrayMost;
    }

    private MyData(){}


    public static MyData getInstance() {
        if (myData == null ) {
            synchronized (MyData.class) {
                if (myData == null) {
                    myData = new MyData();
                }
            }
        }

        return myData;
    }

}
