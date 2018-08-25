package FristMapReduce;

import java.io.IOException;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;



public class WordMain {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		// TODO �Զ����ɵķ������

		//��ȡHadoop����
		Configuration conf=new Configuration();
		//��window ������set�Լ���������
		conf.set("fs.defaultFS","hdfs://192.168.10.100:9000");
		//conf.set("mapred.job.tracker", "hdfs://192.168.10.100:9001");  
		conf.set("mapreduce.framework.name", "yarn");
		conf.set("mapreduce.jobhistory.address", "192.168.10.100:10020");
		//conf.set("hadoop.job.user","root");
		conf.set("yarn.resourcemanager.address","192.168.10.100:8032");
        conf.set("yarn.resourcemanager.admin.address", "192.168.10.100:8033");
		conf.set("yarn.resourcemanager.scheduler.address","192.168.10.100:8030");
		conf.set("yarn.resourcemanager.resource-tracker.address", "192.168.10.100:8031");
		//���������еĲ����Զ����õ�����conf��
		String []otherArgs=new GenericOptionsParser(conf,args).getRemainingArgs();
		
		//����·�������·��û�б���
		
		if(otherArgs.length!=2)
		{
			System.err.println("ȱ�������������ļ�·��");
			System.exit(2);
		}	
		
		
		Job job=new Job(conf,"word count");//�½�һ��job,����������Ϣ
		
		job.setJarByClass(WordMain.class);//��������
		job.setMapperClass(WordMapper.class);//����Mapper��
		job.setReducerClass(WordReducer.class);//����Reducer��
		job.setCombinerClass(WordReducer.class);//������ҵ�ϳ���
		
		job.setOutputKeyClass(Text.class);//����������ݵĹؼ���
		job.setOutputValueClass(IntWritable.class);//�������ֵ��
		
		
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));//�ļ�����
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));//�ļ����

		System.exit(job.waitForCompletion(true)? 0:1);//�ȴ�����˳�
		
		
		
		
	}

}
