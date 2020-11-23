package demo.test.word_count;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class JobMain extends Configured implements Tool {

    /**
     * 指定一个job任务
     *
     * @param strings:
     * @return :
     * @throws Exception:
     */
    public int run(String[] strings) throws Exception {
        //1：创建一个job任务对象
        Job wordCount = Job.getInstance(super.getConf(), "WordCount");
        //如果打包运行出错，则需要加改配置
        wordCount.setJarByClass(JobMain.class);

        //2：配置job任务对象（八个步骤）

        //第一步：指定文件的读取方式和
        wordCount.setInputFormatClass(TextInputFormat.class);
        //      读取路径
//        TextInputFormat.addInputPath(wordCount, new Path("hdfs://nsv:8020/input/wordcount"));
        TextInputFormat.addInputPath(wordCount, new Path("hdfs://localhost:9000/input.txt"));

        //第二步：指定Map阶段的处理方式和数据类型
        wordCount.setMapperClass(WordCountMapper.class);
        //      设置K2的类型
        wordCount.setMapOutputKeyClass(Text.class);
        //      设置V2的类型
        wordCount.setMapOutputValueClass(LongWritable.class);

        //第三、四、五、六shuffle阶段采用默认的方式

        //第七步：指定Reduce阶段的处理方式和数据类型
        wordCount.setReducerClass(WordCountReducer.class);
        //      设置K3的类型
        wordCount.setOutputKeyClass(Text.class);
        //      设置V3的类型
        wordCount.setOutputValueClass(LongWritable.class);

        //第八步：指定输出类型
        wordCount.setOutputFormatClass(TextOutputFormat.class);
        //      输出路径
//        TextOutputFormat.setOutputPath(wordCount, new Path("hdfs://nsv:8020/output/wordcount"));
        TextOutputFormat.setOutputPath(wordCount, new Path("hdfs://localhost:9000/wordcount"));

        //等待任务结束
        boolean bl = wordCount.waitForCompletion(true);

        return bl ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        //启动job任务
        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }
}

