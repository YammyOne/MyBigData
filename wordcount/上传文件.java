package �ļ�HDFS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class �ϴ��ļ� {

	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO �Զ����ɵķ������
	    //��ȡhadoop����
		Configuration configuration =new Configuration();
		//HDFS����IP�Լ��˿�
		URI url=new URI("hdfs://192.168.10.100:9000");
		
		//��ȡ�ֲ�ʽ�ļ�ϵͳ
		FileSystem fs= FileSystem.get(url,configuration);
		
		
		//�����ļ�
		Path src=new Path("C:/Users/luo/Desktop/test.txt");
		
		//HDFS���λ��
		
		Path dst=new Path("/tmp/input/");
		
		//�����ļ��ϴ�
		fs.copyFromLocalFile(src, dst);
		
		//��ӡ�ϴ���Ϣ
		System.out.println("�ϴ���"+configuration.get("fs.defaultFS"));
		
		
		//�鿴Ŀ��Ŀ¼�µ��ļ���Ϣ
		FileStatus files[]=fs.listStatus(dst);
		
		for(FileStatus file:files)
		{
			System.out.println(file.getPath());
		}
		
		
		
	}

}
