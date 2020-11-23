package demo.test.rank_file;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


//会在reduce接收数据时，对key进行排序
public class ResSortReducer extends Reducer<FlowBean, NullWritable, Text, FlowBean>{
    @Override
    protected void reduce(FlowBean key, Iterable<NullWritable> values,
                          Reducer<FlowBean, NullWritable, Text, FlowBean>.Context context) throws IOException, InterruptedException {
        String phoneNB = key.getPhoneNB();
        context.write(new Text(phoneNB), key);
    }
}
