package demo.test.rank_mysql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;

public class DbApp {

    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(DbApp.class);
        job.setJobName("mysql read write");
        job.setMapperClass(MysqlDbMapper.class);
        job.setReducerClass(MysqlDbReducer.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(MysqlDb_scoreWritable.class);
        job.setOutputKeyClass(MysqlDb_scoreWritable.class);
        job.setOutputValueClass(NullWritable.class);


        //配置数据库信息
        String driverclass = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String username = "root";
        String passwd = "1234qwer";
        DBConfiguration.configureDB(job.getConfiguration(),driverclass,url,username,passwd);
        //设置输入内容
        DBInputFormat.setInput(job,MysqlDb_scoreWritable.class,"select * from score","select count(id) from score");

        //设置输出内容,第一个string是表名，后面可以跟多个string是表字段名
        DBOutputFormat.setOutput(job,"topscore","sid","totalscore");
        job.waitForCompletion(true);

    }
}