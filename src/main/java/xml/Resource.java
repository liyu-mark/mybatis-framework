package xml;

import java.io.IOException;
import java.io.InputStream;

public class Resource {

    public static InputStream getResourceAsStream(String path){

        InputStream inputstream = Resource.class.getResourceAsStream(path);
        return inputstream;
    }

    public static void main(String[] args) throws IOException {
        //测试
       InputStream stream = Resource.class.getClassLoader().getResourceAsStream("orm/mapper.xml");
        byte[] buff = new byte[1024];
        int length = stream.read(buff);
        System.out.println(new String(buff,0,length));
    }

}
