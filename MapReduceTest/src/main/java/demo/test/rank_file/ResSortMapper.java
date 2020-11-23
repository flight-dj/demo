package demo.test.rank_file;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ResSortMapper extends Mapper<LongWritable, Text, FlowBean, NullWritable>{
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, FlowBean, NullWritable>.Context context)
            throws IOException, InterruptedException {
        //获取一行数据
        String line = value.toString();
        //进行文本分割
        String[] fields = StringUtils.split(line, '\t');
        //数据获取
        String phoneNB = fields[0];
        long up_flow = Long.parseLong(fields[1]);
        long down_flow = Long.parseLong(fields[2]);

        context.write(new FlowBean(phoneNB, up_flow, down_flow), NullWritable.get());
    }
}
