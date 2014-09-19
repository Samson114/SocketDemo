package TWO;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
/**
 * @说明 监听端口服务
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class SocketService {
	public static void main(String[] args) {
		try {
			// 创建非邦定式连接对象
			ServerSocket ss = new ServerSocket();
			// 连接地址对象
//			SocketAddress address = new InetSocketAddress("192.168.9.69", 8001);
			SocketAddress address = new InetSocketAddress("192.168.1.18", 8001);//zyp
			// 将连接对象邦定到地址ss
			ss.bind(address);
			System.out.println("邦定端口成功：" + address.toString());
			boolean bRunning = true;
			while (bRunning) {
				// 接收请求
				Socket s = ss.accept();
				// 将请求指定一个线程去执行
				new Thread(new SocketListener(s)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/**
 * @说明 监听端口数据并打印
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
class SocketListener implements Runnable {
	Socket socket = null;
	public SocketListener(Socket socket) {
		this.socket = socket;
	}
	@Override
	public void run() {
		System.err.println("开始处理：" + socket.toString());
		try {
			InputStream ips = socket.getInputStream();
			OutputStream ops = socket.getOutputStream();
			while (true) {
				byte[] bts = readStream(ips);
				String s = new String(bts, "GB2312");//zyp
				if (null != bts) {
					System.err.println("接收：" + Arrays.toString(bts));
					System.err.println("接收String：" + s);//zyp
					byte[] btreturn = new byte[bts.length * 2];
					System.arraycopy(bts, 0, btreturn, 0, bts.length);
					System.arraycopy(bts, 0, btreturn, bts.length - 1, bts.length);
					ops.write(btreturn);
					ops.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("执行结束：" + socket.toString());
	}
	/**
	 * 读取流 5 秒超时 1 秒一次
	 */
	public byte[] readStream(InputStream inStream) throws Exception {
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
