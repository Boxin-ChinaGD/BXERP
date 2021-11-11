package wpos.model;

public class BillCommodityModel {
    private int ID;
    private String time;
    private String name;
    private String totalNumber;
    private String subtotal;
    public void setID(int ID){
        this.ID = ID;
    }
    public int getID(){
        return ID;
    }
    public void setTime(String time){
        this.time=time;
    }
    public String getTime(){
        return time;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setTotalNumber(String totalNumber){
        this.totalNumber=totalNumber;
    }
    public String getTotalNumber(){
        return totalNumber;
    }
    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
    public String getSubtotal(){
        return subtotal;
    }
}
