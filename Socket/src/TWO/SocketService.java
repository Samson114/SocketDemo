package TWO;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
/**
 * @˵�� �����˿ڷ���
 * @author cuisuqiang
 * @version 1.0
 * @since
 */
public class SocketService {
	public static void main(String[] args) {
		try {
			// �����ǰʽ���Ӷ���
			ServerSocket ss = new ServerSocket();
			// ���ӵ�ַ����
//			SocketAddress address = new InetSocketAddress("192.168.9.69", 8001);
			SocketAddress address = new InetSocketAddress("192.168.1.18", 8001);//zyp
			// �����Ӷ�������ַss
			ss.bind(address);
			System.out.println("��˿ڳɹ���" + address.toString());
			boolean bRunning = true;
			while (bRunning) {
				// ��������
				Socket s = ss.accept();
				// ������ָ��һ���߳�ȥִ��
				new Thread(new SocketListener(s)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/**
 * @˵�� �����˿����ݲ���ӡ
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
		System.err.println("��ʼ����" + socket.toString());
		try {
			InputStream ips = socket.getInputStream();
			OutputStream ops = socket.getOutputStream();
			while (true) {
				byte[] bts = readStream(ips);
				String s = new String(bts, "GB2312");//zyp
				if (null != bts) {
					System.err.println("���գ�" + Arrays.toString(bts));
					System.err.println("����String��" + s);//zyp
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
		System.err.println("ִ�н�����" + socket.toString());
	}
	/**
	 * ��ȡ�� 5 �볬ʱ 1 ��һ��
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
