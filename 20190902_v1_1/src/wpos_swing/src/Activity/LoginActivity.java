package Activity;

import Interface.IFloginButton;
import Gui.LoginFrame;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class LoginActivity implements IFloginButton {

	private JFrame frame;
	private static LoginActivity loginActivity;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("执行了login main");
					LoginActivity.startLoginActivity();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void startLoginActivity(){
		if (loginActivity == null){
			synchronized (LoginActivity.class){
				if (loginActivity == null){
					loginActivity = new LoginActivity();
					System.out.println("创建了新对象");
				}
			}
		}
		System.out.println("重新可见");
		loginActivity.frame.setVisible(true);
	}

	private LoginActivity() {
		initialize();
	}

	private void initialize() {
		frame = new LoginFrame(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//获取屏幕宽高,将窗口大小设置为屏幕的2/3
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float proportionW = screenSize.width*2/3;
		float proportionH = screenSize.height*2/3;
		frame.setSize((int)proportionW,(int)proportionH);
		
		//设置窗口居中显示
		frame.setLocationRelativeTo(null);
	}

	//登录按钮被点击后
	@Override
	public void loginClick(String phone, String psw) {
		//登录成功
		this.frame.dispose();//关闭当前窗口
		SyncDataActivity.startSyncDataActivity();

		//售前页面
//		this.frame.setVisible(false);
//		PreSaleActivity.startPreSaleActivity(frame);
	}
}
