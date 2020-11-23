package demo.test.rank_file;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class FlowBean implements WritableComparable<FlowBean> {
    private String phoneNB;
    private long up_flow;
    private long down_flow;
    private long sum_flow;

    public FlowBean() {}    //无参构造函数，用于反序列化时使用

    public FlowBean(String phoneNB, long up_flow, long down_flow) {
        this.phoneNB = phoneNB;
        this.up_flow = up_flow;
        this.down_flow = down_flow;
        this.sum_flow = up_flow + down_flow;
    }


    public String getPhoneNB() {
        return phoneNB;
    }

    public void setPhoneNB(String phoneNB) {
        this.phoneNB = phoneNB;
    }

    public long getUp_flow() {
        return up_flow;
    }

    public void setUp_flow(long up_flow) {
        this.up_flow = up_flow;
    }

    public long getDown_flow() {
        return down_flow;
    }

    public void setDown_flow(long down_flow) {
        this.down_flow = down_flow;
    }

    public long getSum_flow() {
        return up_flow + down_flow;
    }


    //用于序列化
    @Override
    public void write(DataOutput out) throws IOException {
        // TODO Auto-generated method stub
        out.writeUTF(phoneNB);
        out.writeLong(up_flow);
        out.writeLong(down_flow);
        out.writeLong(up_flow+down_flow);
    }

    //用于反序列化
    @Override
    public void readFields(DataInput in) throws IOException {
        // TODO Auto-generated method stub
        phoneNB = in.readUTF();
        up_flow = in.readLong();
        down_flow = in.readLong();
        sum_flow = in.readLong();
    }

    @Override
    public int compareTo(FlowBean o) {//用于排序操作
        return sum_flow > o.sum_flow ? -1 : 1;    //返回值为-1，则排在前面
    }

    @Override
    public String toString() {
        return "" + up_flow + "\t" + down_flow + "\t"+ sum_flow;
    }


}