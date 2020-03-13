package com.ss.sdk.job;

import com.ss.sdk.Client.CaptureJPEGPicture;
import com.ss.sdk.mapper.DeviceMapper;
import com.ss.sdk.model.Capture;
import com.ss.sdk.model.Device;
import com.ss.sdk.socket.MyWebSocket;
import com.ss.sdk.utils.*;
import com.sun.jna.NativeLong;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ContinueRead extends Thread implements SerialPortEventListener { // SerialPortEventListener

    private JedisUtil jedisUtil = ApplicationContextProvider.getBean(JedisUtil.class);
    private CaptureJPEGPicture captureJPEGPicture = ApplicationContextProvider.getBean(CaptureJPEGPicture.class);
    private DeviceMapper deviceMapper = ApplicationContextProvider.getBean(DeviceMapper.class);
    private PropertiesUtil propertiesUtil = ApplicationContextProvider.getBean(PropertiesUtil.class);
    ;
    // 监听器,我的理解是独立开辟一个线程监听串口数据
// 串口通信管理类
    static CommPortIdentifier portId;

    /* 有效连接上的端口的枚举 */

    static Enumeration<?> portList;
    InputStream inputStream; // 从串口来的输入流
    static OutputStream outputStream;// 向串口输出的流
    static SerialPort serialPort; // 串口的引用
    // 堵塞队列用来存放读到的数据
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

    public static double temp = 0;
    public static int tempType = 0;
    public static long time = 0;

    @Override
    /**
     * SerialPort EventListene 的方法,持续监听端口上是否有数据流
     */
    public void serialEvent(SerialPortEvent event) {//

        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:// 当有可用数据时读取数据
                byte[] readBuffer = null;
                int availableBytes = 0;
                try {
                    availableBytes = inputStream.available();
                    while (availableBytes > 0) {
                        readBuffer = ContinueRead.readFromPort(serialPort);
                        String needData = printHexString(readBuffer);
                        System.out.println(new Date() + "真实收到的数据为：-----" + needData);
                        availableBytes = inputStream.available();
                        msgQueue.add(needData);
                    }
                } catch (IOException e) {
                }
            default:
                break;
        }
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     */
    public static byte[] readFromPort(SerialPort serialPort) {
        InputStream in = null;
        byte[] bytes = {};
        try {
            in = serialPort.getInputStream();
            // 缓冲区大小为一个字节
            byte[] readBuffer = new byte[1];
            int bytesNum = in.read(readBuffer);
            while (bytesNum > 0) {
                bytes = MyUtils.concat(bytes, readBuffer);
                bytesNum = in.read(readBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }


    /**
     * 通过程序打开COM4串口，设置监听器以及相关的参数
     *
     * @return 返回1 表示端口打开成功，返回 0表示端口打开失败
     */
    public int startComPort() {
        // 通过串口通信管理类获得当前连接上的串口列表
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            // 获取相应串口对象
            portId = (CommPortIdentifier) portList.nextElement();

            System.out.println("设备类型：--->" + portId.getPortType());
            System.out.println("设备名称：---->" + portId.getName());
            // 判断端口类型是否为串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 判断如果COM4串口存在，就打开该串口
                if (portId.getName().equals(portId.getName())) {
                    try {
                        // 打开串口名字为COM_4(名字任意),延迟为1000毫秒
                        serialPort = (SerialPort) portId.open(portId.getName(), 3000);

                    } catch (PortInUseException e) {
                        System.out.println("打开端口失败!");
                        e.printStackTrace();
                        return 0;
                    }
                    // 设置当前串口的输入输出流
                    try {
                        inputStream = serialPort.getInputStream();
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    // 给当前串口添加一个监听器
                    try {
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    // 设置监听器生效，即：当有数据时通知
                    serialPort.notifyOnDataAvailable(true);

                    // 设置串口的一些读写参数
                    try {
                        // 比特率、数据位、停止位、奇偶校验位
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                        return 0;
                    }
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            System.out.println("--------------任务处理线程运行了--------------");
            while (true) {
                // 如果堵塞队列中存在数据就将其输出
                if (msgQueue.size() > 0) {
                    String vo = msgQueue.peek();
                    msgQueue.take();
                    //String vo = "F5 01 01 01 81 01";
                    String vos[] = vo.split("  ", -1);
                    String encodeToString = null;
                    long data = getData(vos);
                    double temp = (double) data / 10;
                    if (this.propertiesUtil.getType() == 0) {
                        List<Device> devices = this.deviceMapper.findHivVideoTempDevice();
                        for (Device device : devices) {
                            //启动抓拍并返回抓拍路径
                            String pictureUrl = captureJPEGPicture.SetupAlarmChan((NativeLong) jedisUtil.get(device.getCplatDeviceId()));
                            if (pictureUrl != null) {
                                encodeToString = Base64Util.localBase64(pictureUrl);
                            }
                            Capture capture = new Capture();
                            capture.setDeviceId(device.getDeviceId());
                            capture.setOpendoorMode(5);
                            capture.setCaptureUrl(pictureUrl);
                            capture.setCompareDate(String.valueOf(System.currentTimeMillis()));
                            capture.setTemp(temp);
                            if ("F5".equals(vos[0])) {
                                //发送体温报警信息
                                MyWebSocket.client.send("{'type':'tempAlarm','temp':'" + temp + "','base64':'" + encodeToString + "'," + "'deviceId':'" + device.getCplatDeviceId()
                                        + "','captureTime':'" + System.currentTimeMillis() + "','tenantId':'" + this.propertiesUtil.getTenantId() + "'}");
                                capture.setTempState(1);
                            } else if ("F7".equals(vos[0])) {
                                //发送正常体温信息
                                MyWebSocket.client.send("{'type':'tempNormal','temp':'" + temp + "','base64':'" + encodeToString + "'," + "'deviceId':'" + device.getCplatDeviceId()
                                        + "','captureTime':'" + System.currentTimeMillis() + "','tenantId':'" + this.propertiesUtil.getTenantId() + "'}");
                                capture.setTempState(0);
                            }
                            //存储抓拍信息
                            this.deviceMapper.insertCapture(capture);

                        }
                    } else if (this.propertiesUtil.getType() == 1){
                        if ("F7".equals(vos[0])){
                            ContinueRead.temp =temp;
                            ContinueRead.tempType = 0;
                            long time = System.currentTimeMillis();
                            ContinueRead.time = time;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ContinueRead.reset(time);
                                }
                            }).start();
                        } else if ("F5".equals(vos[0])){
                            ContinueRead.temp =temp;
                            ContinueRead.tempType = 1;
                            long time = System.currentTimeMillis();
                            ContinueRead.time = time;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ContinueRead.reset(time);
                                }
                            }).start();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void reset(long time){
        try {
            Thread.sleep(2000);
            if (ContinueRead.time == time){
                ContinueRead.tempType = 0;
                ContinueRead.temp = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:通过数组解析检测数据
     * @Param: [vo]
     * @return: void
     */
    public long getData(String[] vos) {
        // 数组不为空
        if (vos != null || vos.length != 0) {
            long wind_direction = getNum(vos[3], vos[4]);
            System.out.println(wind_direction);
            return wind_direction;
        } else {
            return 0;
        }
    }

    // 16转10计算
    public long getNum(String num1, String num2) {
        long value = Long.parseLong(num1, 16) * 256 + Long.parseLong(num2, 16);
        return value;
    }

    public static void connect() {
        ContinueRead cRead = new ContinueRead();
        System.out.println("asdasd");
        int i = cRead.startComPort();
        if (i == 1) {
            // 启动线程来处理收到的数据
            cRead.start();
        } else {
            return;
        }
    }

    // 字节数组转字符串
    private String printHexString(byte[] b) {

        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sbf.append(hex.toUpperCase() + "  ");
        }
        return sbf.toString().trim();
    }
}
