package com.example.pc.sluicecontrol.serial;

import android.util.Log;

import java.util.Arrays;

public class MessageTan {

    private byte startAddress;         //设备地址(开始符)
    private byte  btCmd;            //指令代码
    private byte btDataLen;        //数据体长度
    private byte [] btAryData;      //数据体
    private byte btJCQ;          //寄存器
    private byte btJCQG;          //寄存器(高位)
    private byte btJCQD;          //寄存器(低位)
    private byte btCheck;          //校验值(高位)
    private byte btCheck2;          //校验值(低位)
    private byte endAddress;               //结束符
    private byte[] btAryTranData;  //完整数据包



    public MessageTan(byte btCmd) {

        this.startAddress = CMD.StartAddress;
        this.btCmd = btCmd;
        this.endAddress = CMD.EndAddress;

        this.btAryTranData = new byte[5];
        this.btAryTranData[0] = this.startAddress;
        this.btAryTranData[1] = this.btCmd;
        this.btAryTranData[2] = this.endAddress;

        checkSum(this.btAryTranData, 0, 3);   //获取校验值

        this.btAryTranData[2] = this.btCheck;
        this.btAryTranData[3] = this.btCheck2;
        this.btAryTranData[4] = this.endAddress;

    }



    public MessageTan(byte btCmd,byte btJCQ) {   //读取寄存器的信息

        this.startAddress = CMD.StartAddress;
        this.btCmd = btCmd;
        this.endAddress = CMD.EndAddress;

        this.btAryTranData = new byte[7];
        this.btAryTranData[0] = this.startAddress;
        this.btAryTranData[1] = this.btCmd;

        //先得到寄存器的高位与地位

        String ertemp = Integer.toBinaryString((btJCQ & 0xFF) + 0x100).substring(1);   //得到8位二进制数
        //Log.i("123",ertemp+"---===::"+Integer.toBinaryString(btJCQ&0xff));
        String jcqgaowei = "1001"+ertemp.substring(0,4);   //寄存器高位
        String jcqdiwei = "1001"+ertemp.substring(4,8);   //寄存器低位

        this.btJCQG = Util.binStrToByte(jcqgaowei);   //寄存器高位
        this.btJCQD = Util.binStrToByte(jcqdiwei);   //寄存器低位

        this.btAryTranData[2] = this.btJCQG;
        this.btAryTranData[3] = this.btJCQD;
        btAryTranData[4] =this.endAddress;

        checkSum(this.btAryTranData, 0, 5);   //获取校验值

        this.btAryTranData[4] = this.btCheck;
        this.btAryTranData[5] = this.btCheck2;
        this.btAryTranData[6] = this.endAddress;

    }



    public MessageTan(byte btCmd,byte btJCQ,byte[] btAryData) {

        int nlen = btAryData.length;   //数据体长度
        this.startAddress = CMD.StartAddress;
        this.btCmd = btCmd;
        this.endAddress = CMD.EndAddress;

        this.btAryTranData = new byte[nlen+7];
        this.btAryTranData[0] = this.startAddress;
        this.btAryTranData[1] = this.btCmd;

        //先得到寄存器的高位与地位

        String ertemp = Integer.toBinaryString((btJCQ & 0xFF) + 0x100).substring(1);   //得到8位二进制数
       // Log.i("123",ertemp+"---===::"+Integer.toBinaryString(btJCQ&0xff));
        String jcqgaowei = "1001"+ertemp.substring(0,4);   //寄存器高位
        String jcqdiwei = "1001"+ertemp.substring(4,8);   //寄存器低位

        this.btJCQG = Util.binStrToByte(jcqgaowei);   //寄存器高位
        this.btJCQD = Util.binStrToByte(jcqdiwei);   //寄存器低位

        this.btAryTranData[2] = this.btJCQG;
        this.btAryTranData[3] = this.btJCQD;

        System.arraycopy(btAryData, 0, this.btAryTranData, 4, btAryData.length);
        btAryTranData[nlen+4] =this.endAddress;

        checkSum(this.btAryTranData, 0, nlen+5);   //获取校验值

        this.btAryTranData[nlen+4] = this.btCheck;
        this.btAryTranData[nlen+5] = this.btCheck2;
        this.btAryTranData[nlen+6] = this.endAddress;

    }



    public MessageTan(byte btCmd,byte[] btAryData) {   //设置参数

        int nlen = btAryData.length;   //数据体长度
        this.startAddress = CMD.StartAddress;
        this.btCmd = btCmd;
        this.endAddress = CMD.EndAddress;

        this.btAryTranData = new byte[nlen+5];
        this.btAryTranData[0] = this.startAddress;
        this.btAryTranData[1] = this.btCmd;

        System.arraycopy(btAryData, 0, this.btAryTranData, 2, btAryData.length);
        btAryTranData[nlen+2] =this.endAddress;

        checkSum(this.btAryTranData, 0, nlen+3);   //获取校验值

        this.btAryTranData[nlen+2] = this.btCheck;
        this.btAryTranData[nlen+3] = this.btCheck2;
        this.btAryTranData[nlen+4] = this.endAddress;

    }


    public byte getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(byte startAddress) {
        this.startAddress = startAddress;
    }

    public byte getBtCmd() {
        return btCmd;
    }

    public void setBtCmd(byte btCmd) {
        this.btCmd = btCmd;
    }

    public byte getBtDataLen() {
        return btDataLen;
    }

    public void setBtDataLen(byte btDataLen) {
        this.btDataLen = btDataLen;
    }

    public byte[] getBtAryData() {
        return btAryData;
    }

    public void setBtAryData(byte[] btAryData) {
        this.btAryData = btAryData;
    }

    public byte getBtJCQ() {
        return btJCQ;
    }

    public void setBtJCQ(byte btJCQ) {
        this.btJCQ = btJCQ;
    }

    public byte getBtJCQG() {
        return btJCQG;
    }

    public void setBtJCQG(byte btJCQG) {
        this.btJCQG = btJCQG;
    }

    public byte getBtJCQD() {
        return btJCQD;
    }

    public void setBtJCQD(byte btJCQD) {
        this.btJCQD = btJCQD;
    }

    public byte getBtCheck() {
        return btCheck;
    }

    public void setBtCheck(byte btCheck) {
        this.btCheck = btCheck;
    }

    public byte getBtCheck2() {
        return btCheck2;
    }

    public void setBtCheck2(byte btCheck2) {
        this.btCheck2 = btCheck2;
    }

    public byte getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(byte endAddress) {
        this.endAddress = endAddress;
    }

    public byte[] getBtAryTranData() {
        return btAryTranData;
    }

    public void setBtAryTranData(byte[] btAryTranData) {
        this.btAryTranData = btAryTranData;
    }

    /**
     * 计算校验和
     *
     * @param btAryBuffer 数据
     * @param nStartPos   起始位置
     * @param nLen        校验长度
     * @return
     */
    public void checkSum(byte[] btAryBuffer, int nStartPos, int nLen) {

        int btSum = 0;
        for (int nloop = nStartPos; nloop < nStartPos + nLen; nloop++) {

            byte a =btAryBuffer[nloop];

            if (nloop==0){
                btSum = btAryBuffer[0];
            }else {
                btSum^= a;
            }

        }


        String ertemp = Integer.toBinaryString((btSum & 0xFF) + 0x100).substring(1);   //得到8位二进制数
       // Log.i("123",ertemp+"顶::"+Integer.toBinaryString(btSum&0xff));
        String gaowei = "1010"+ertemp.substring(0,4);   //校验值高位
        String diwei = "1011"+ertemp.substring(4,8);   //校验值地位

        this.btCheck = Util.binStrToByte(gaowei);   //校验值高位
        this.btCheck2 = Util.binStrToByte(diwei);   //校验值低位

        //StringUtils.leftPad(Integer.toBinaryString(btSum & 0xff), 8, '0')   //转二进制
       // Integer.toBinaryString(btSum)；

    }


    @Override
    public String toString() {
        return "MessageTan{" +
                "startAddress=" + startAddress +
                ", btCmd=" + btCmd +
                ", btDataLen=" + btDataLen +
                ", btAryData=" + Arrays.toString(btAryData) +
                ", btJCQ=" + btJCQ +
                ", btJCQG=" + btJCQG +
                ", btJCQD=" + btJCQD +
                ", btCheck=" + btCheck +
                ", btCheck2=" + btCheck2 +
                ", endAddress=" + endAddress +
                ", btAryTranData=" + Arrays.toString(btAryTranData) +
                '}';
    }
}
