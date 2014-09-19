package TWO;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
/**
 * @说明 Socket客户端，连接成功后发送一组数据然后关闭连接
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class SocketClient {
	public static void main(String[] args) {
		try {
//			Socket socket = new Socket("192.168.9.69", 8001);
			Socket socket = new Socket("192.168.1.18", 8001);//zyp
			socket.setKeepAlive(true);
			InputStream ips = socket.getInputStream();
			OutputStream ops = socket.getOutputStream();
			String outString="hello world!!";//zyp
			while(true){
				byte[] remess = new byte[20];
				for (int i = 0; i < 20; i++) {
					remess[i] = (byte) i;
				}
				 byte[] b=outString.getBytes();//zyp
//				ops.write(remess);
				ops.write(b);//zyp
				byte[] bts = readStream(ips);
				String s = new String(bts, "GB2312");//zyp
				if (null != bts) {
					System.err.println("返回：" + Arrays.toString(bts));
					System.err.println("返回String:：" + s);
				}
				Thread.sleep(2 * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取流 5 秒超时 1 秒一次
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		int count = 0;
		int index = 1;
		while (count == 0) {
			count = inStream.available();
			index++;
			Thread.sleep(1 * 1000);
			if (index > 5 || count > 0) {
				break;
			}
		}
		if (count > 0) {
			byte[] b = new byte[count];
			inStream.read(b);
			return b;
		}
		return null;
	}
}
