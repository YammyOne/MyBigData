package FristMapReduce;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
*����Ĺ����Լ��ص�����������MapReduce��Reducer��
*��Ҫ�����ǻ��ܽ��


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


//һ��reducer���Խ��ܶ��Mapper���͵�����
public class WordReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
	
	
	//��¼��Ƶ
	//������java Integer
	private IntWritable result =new IntWritable();
	
	
	/*key map���͵�text
	 * values Map�����Value�ļ���     context.write(word,one)   //���е� one
	 * 
	 *
	*/
	
	public void reduce(Text key,Iterable<IntWritable>values,Context context) throws IOException, InterruptedException
	{
		int sum=0;
		for (IntWritable val:values)
		{
			sum+=val.get();//��get��one��
			
		}
		result.set(sum);//��¼һ�δ�Ƶ
		context.write(key, result);//���� +��Ƶ
		
	}

	
	
	
	
	
	
	
	
}
