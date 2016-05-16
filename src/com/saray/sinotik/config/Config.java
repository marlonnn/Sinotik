package com.saray.sinotik.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.saray.sinotk.entity.User;
import com.summer.logger.XLog;


public class Config {

    public static final String ADDRESS = "http://115.159.44.146/ebdpmp/Api/user.php";
    
    public static final String Data = "http://115.159.44.146/EBDPMP/api/data.php";
    
	public static final String KUserName = "username";
    
    public static final String KPassword = "password";
    
	private static String password = "";
	
	private static String username = "";
	
    public static boolean hasloadConfig = false;
    
    private static String fileDir = "";
    
	private static boolean isSavePsw = false;
	
	private static boolean isLogin = false;
    
    private static final String cfgname = "/sales.cfg";
    
    public static User user;
    
    /**
     * 登录
     */
    public static final int LOGIN_TYPE = -1;
    
    /**
     * 获取监测点列表
     */
    public static final int staion_list = 1;
    
    /**
     * 获取所有室外监测点的数据
     */
    public static final int all_out_door = 2;
    
    /**
     * 获取所有的机房监测点数据
     */
    public static final int all_inner = 3;
    
	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Config.password = password;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		Config.username = username;
	}

	public static boolean isHasloadConfig() {
		return hasloadConfig;
	}

	public static void setHasloadConfig(boolean hasloadConfig) {
		Config.hasloadConfig = hasloadConfig;
	}

	public static String getFileDir() {
		return fileDir;
	}

	public static void setFileDir(String fileDir) {
		Config.fileDir = fileDir;
	}

	public static boolean isSavePsw() {
		return isSavePsw;
	}

	public static void setSavePsw(boolean isSavePsw) {
		Config.isSavePsw = isSavePsw;
	}

	public static boolean isLogin() {
		return isLogin;
	}

	public static void setLogin(boolean isLogin) {
		Config.isLogin = isLogin;
	}

	public static String getKusername() {
		return KUserName;
	}

	public static String getKpassword() {
		return KPassword;
	}

	public static String getCfgname() {
		return cfgname;
	}

	public static void saveUser(String name, String pwd) {
		if (name == null || pwd == null) {
			return;
		}
		username = name;
		password = pwd;
		Config.saveConfig();
	}
	
	/**
	 * Load config from local file
	 */
    public static void LoadConfig() 
    {
		hasloadConfig = true;
		FileInputStream stream = null;
		String filename = Config.fileDir + Config.cfgname;
		
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}

			stream = new FileInputStream(filename);
			Properties properties = new Properties();
			properties.load(stream);
			Config.password = properties.getProperty(KPassword, "");
			Config.username = properties.getProperty(KUserName, "");
			String w = properties.getProperty("isSavePsw", "false");
			Config.isSavePsw = Boolean.valueOf(w).booleanValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			XLog.e(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			XLog.e(e.getMessage(), e);
		}finally {
			if (stream != null) {
				try {
					stream.close();
					stream = null;
				} catch (IOException e) {
					XLog.e(e.getMessage(), e);
				}
			}
		}
    }
    
	public static void saveConfig() {
		Properties properties = new Properties();
		properties.setProperty(Config.KUserName,
				String.valueOf(Config.username));
		properties.setProperty(Config.KPassword,
				String.valueOf(Config.password));
		properties.setProperty("isSavePsw", String.valueOf(Config.isSavePsw));
		FileOutputStream stream = null;
		String filename = Config.fileDir + Config.cfgname;
		try {

			File f = new File(filename);
			if (!f.exists()) {
				File dir = new File(Config.fileDir);
				dir.mkdirs();
				f.createNewFile();
			}
			stream = new FileOutputStream(f);
			properties.store(stream, "");
		} catch (FileNotFoundException e) {
			XLog.e("saveConfig" + e.toString(), e);
		} catch (Exception e) {
			XLog.e("saveConfig" + e.toString(), e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
					stream = null;
				} catch (IOException e) {
					XLog.e("saveConfig" + e.toString(), e);
				}
			}
		}
	}
	
	public static void clearPassword() {
		password = "";
		saveConfig();
	}
}
