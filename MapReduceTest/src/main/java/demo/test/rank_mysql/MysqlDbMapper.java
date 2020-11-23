package demo.test.rank_mysql;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MysqlDbMapper extends Mapper<LongWritable,MysqlDb_scoreWritable,LongWritable,MysqlDb_scoreWritable> {

    protected void map(LongWritable key, MysqlDb_scoreWritable value, Context context) throws IOException, InterruptedException {
        System.out.println(value.toString());
        Long sid = value.getId();
        context.write(new LongWritable(sid),
                value);
    }
}
