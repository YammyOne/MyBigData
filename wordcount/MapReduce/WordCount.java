package MapReduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import MapReduce.WordCount.InvertedIndexMapper.InvertedIndexCombiner;
import MapReduce.WordCount.InvertedIndexMapper.InvertedIndexReducer;

/*
 *����������
 *Ӧ�ã�ȫ����������
 *ԭ���������ݲ����ĵ��ķ�ʽ
 *��Ƚ�Wordcount key�ǵ������ĵ�URI�����
 *URL��URI���Ӽ�  URI��Ψһ��ʾ��
 *
*/
public class WordCount {
	
	
	//Mapper��
	//1,2�ǽ�������
	//3.4��ת��Ŀ��
	public static class InvertedIndexMapper extends Mapper<Object,Text,Text,Text>
	{
		
		private Text keyInfo =new Text();//�洢���ʺ�URI�����
		private Text valueInfo=new Text();//�洢��Ƶ
		private FileSplit split;   //�洢Split����  ��������ļ���URI
		
		
		//map���ܺͷ��͵Ķ���<key,value>�ṹ������      
		//keyΪƫ����
		//valueΪһ�е��ı�����
		//context ���д���
		public void map(Object key,Text value,Context context) throws IOException, InterruptedException
		{
			//���<key,value>��������FileSplit����
			split=(FileSplit)context.getInputSplit();
			
			//StringTokenizer��һ�������ָ�String��Ӧ����
			//javaĬ�ϵķָ����ǡ��ո񡱡����Ʊ��(��\t��)���������з�(��\n��)�������س���(��\r��)��
			StringTokenizer itr=new StringTokenizer(value.toString());
			
			while(itr.hasMoreTokens())
			{
				
				//itr.nextToken()��ָ��ķָ�� ��ȡ�Ľ��  
			    //����key  �ɵ�����URI���
				keyInfo.set(itr.nextToken()+":"+split.getPath().toString());
				//���ô�Ƶ����value
				valueInfo.set("1");
				
				context.write(keyInfo, valueInfo);
	
			}
			
			
		}
		
		
		
		//Combiner��
		//��Mapper�ռ�����<key,value>���д���   �����趨key ʵ�ֵ���Ϊ��,  URI���Ƶ��� value
		//Reduce���Ƕ�<key,value>���д���Ĺ���
		
		//<goog:text1,1> <goog:text1,2>   values ������������ͬkey   value�ļ���     
		//�ȼ��� ��ͬһ����
		
		
		public static class InvertedIndexCombiner extends Reducer<Text,Text,Text,Text>
		{
			private Text info=new Text();//�µ�value
			
			//��Ϊһreduce �ᴦ��ܶ�������
			public void reduce(Text key,Iterable<Text>values,Context context) throws IOException, InterruptedException
			{
			//ͳ�ƴ�Ƶ
			int sum=0;
			for(Text value:values)
			{
				sum+=Integer.parseInt(value.toString());//���ַ���ת����Int
				
			}
			
			//:��λ��
			int splitIndex=key.toString().indexOf(":");
			
			if(splitIndex!=-1)//��ֹ�ڴ����
			{
			
			
			//���key  �γ��µ�<key,value>
			
			//sumʼ�ռ�¼����һ���ĵ���һ�ֵ��ʵ�����
			info.set(key.toString().substring(splitIndex+1)+":"+sum);
			
			key.set(key.toString().substring(0,splitIndex));//�µ�key

			
			context.write(key, info);
			}
			
			}
			
		}
		
		
		
		
		//Reducer
		//��<key,value>�����Ļ��ܴ���
		
		public static class InvertedIndexReducer extends Reducer<Text,Text,Text,Text>
		{
			private Text result =new Text();
			
			
			//��Ϊһreduce �ᴦ��ܶ�������
			public void reduce(Text key,Iterable<Text>values,Context context) throws IOException, InterruptedException
			{

			String list=new String();
			for(Text value:values)
			{
				
				list+=value.toString()+";";
				
				
			}
			result.set(list);
	
			context.write(key, result);
			
			
			}
			
			
		}
		
		
		
	}
	

		public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
			// TODO �Զ����ɵķ������

			//��ȡHadoop����
			Configuration conf=new Configuration();
			//��window ������set�Լ���������
			conf.set("fs.defaultFS","hdfs://192.168.137.100:9000");
			//conf.set("mapred.job.tracker", "hdfs://192.168.10.100:9001");  
			conf.set("mapreduce.framework.name", "yarn");
			conf.set("mapreduce.jobhistory.address", "192.168.137.100:10020");
			//conf.set("hadoop.job.user","root");
			conf.set("yarn.resourcemanager.address","192.168.137.100:8032");
	        conf.set("yarn.resourcemanager.admin.address", "192.168.137.100:8033");
			conf.set("yarn.resourcemanager.scheduler.address","192.168.137.100:8030");
			conf.set("yarn.resourcemanager.resource-tracker.address", "192.168.137.100:8031");
			//���������еĲ����Զ����õ�����conf��
			String []otherArgs=new GenericOptionsParser(conf,args).getRemainingArgs();
			
			//����·�������·��û�б���
			
			if(otherArgs.length!=2)
			{
				System.err.println("ȱ�������������ļ�·��");
				System.exit(2);
			}	
			
			
			Job job=new Job(conf,"word count");//�½�һ��job,����������Ϣ
			
			job.setJarByClass(WordCount.class);//��������
			job.setMapperClass(InvertedIndexMapper.class);//����Mapper��
			job.setReducerClass(InvertedIndexReducer.class);//����Reducer��
			job.setCombinerClass(InvertedIndexCombiner.class);//������ҵ�ϳ���
			
			job.setOutputKeyClass(Text.class);//����������ݵĹؼ���
			job.setOutputValueClass(Text.class);//�������ֵ��
			
			
			FileInputFormat.addInputPath(job, new Path(otherArgs[0]));//�ļ�����
			FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));//�ļ����

			System.exit(job.waitForCompletion(true)? 0:1);//�ȴ�����˳�
			
			
			
			
		
		}
	
	
	
	

}
