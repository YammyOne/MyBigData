package FristMapReduce;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



/*
*����Ĺ����Լ��ص�����������MapReduce��Mapper��
*��Ҫ�����ǽ��������Ĺ���


*�����Ƿ񱻱�����ԣ���
*@see(��֮���������)��   ��Դ��
*                     �м䣺
*                     ȥ����
*������˾��λ����007����
*��Ȩ����007

*@author(����)����007
*@since�����ļ�ʹ�õ�jdk����JDK1.8
*@version���汾����1.0
*@���ݿ��ѯ��ʽ��
*@date(��������)��2018/5/24
*�Ľ���
*���������ڣ�
*/

//�̳�Mapper������


//������Դת����Mapper���Դ��������
//����׼�����м��������Reducer
public class WordMapper extends Mapper<Object,Text,Text,IntWritable>{
	
	
	public final static IntWritable one=new IntWritable(1);
	
	private Text word =new Text();
	
	//Mapper������ĺ��ķ�������������
	//key ���ַ�ƫ����
	//value�ļ���һ������           *****����map��ʼ�и��������<key,value>......
	//context Map�˵�������  
	public void map(Object key,Text value,Context context) throws IOException, InterruptedException
	{
		
		//StringTokenizer��һ�������ָ�String��Ӧ����
		//javaĬ�ϵķָ����ǡ��ո񡱡����Ʊ��(��\t��)���������з�(��\n��)�������س���(��\r��)��
		StringTokenizer itr=new StringTokenizer(value.toString());
		
		//hasMoreTokens()�ж��Ƿ��зָ���
		//
		while(itr.hasMoreTokens())
		{
			
			//itr.nextToken()��ָ��ķָ�� ��ȡ�Ľ��  
			//�������һ������
			word.set(itr.nextToken());
			//һ�����ʷ���һ��
			context.write(word,one);
			
		}
		
	}
	
	
	
	
	

}
