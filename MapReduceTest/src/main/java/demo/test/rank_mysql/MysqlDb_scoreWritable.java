package demo.test.rank_mysql;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlDb_scoreWritable implements Writable,DBWritable {

    private Long id;
    private String age;
    private String name;
    private Long score;
    private Long rank;

    /**
     *数据序列化
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(id);
        out.writeUTF(age);
        out.writeUTF(name);
        out.writeLong(score);
        out.writeLong(rank);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readLong();
        this.age = in.readUTF();
        this.name = in.readUTF();
        this.score = in.readLong();
        this.rank = in.readLong();
    }

    /**
     * 数据库读写
          * 向topscore中写入值
     */
    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setLong(1,id);
        statement.setLong(2,rank);
    }

    //从score中读取成绩
    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
//        id = resultSet.getInt(1);
//        sid = resultSet.getInt(2);
//        cid = resultSet.getInt(3);
//        score = resultSet.getInt(4);


    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public Long getScore() {
        return score;
    }

    public Long getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "MysqlDb_scoreWritable{" +
                "id=" + id +
                ", age='" + age + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", rank=" + rank +
                '}';
    }
}