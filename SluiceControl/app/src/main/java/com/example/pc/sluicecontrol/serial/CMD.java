package com.example.pc.sluicecontrol.serial;

public class CMD {

    public final static byte LoopCode = (byte)0x01;   //轮询地址   主机---->从机
    public final static byte StartAddress =  (byte)0x21;      //开始符   主机--->从机
    public final static byte EndAddress =  (byte)0xC0;   //主机--->从机

    public final static byte REQUESTCODE71 =  (byte)0x71;   //指令71，读取设备信息
    public final static byte REQUESTCODE70 =  (byte)0x70;   //指令70，读取寄存器信息
    public final static byte REQUESTCODE60 =  (byte)0x60;   //指令60，设置寄存器
    public final static byte REQUESTCODE75 =  (byte)0x75;   //指令75，读取传感器
    public final static byte REQUESTCODE78 =  (byte)0x78;   //指令78，读取参数
    public final static byte REQUESTCODE65 =  (byte)0x65;   //指令65，设置测试
    public final static byte REQUESTCODE68 =  (byte)0x68;   //指令68，设置参数


    public final static byte  DIRAMAddress = (byte) 0x00;   //寄存器DIRAM   方向A通行模式
    public final static byte  DIRBMAddress = (byte) 0x01;  //寄存器DIRBM    方向B通行模式
    public final static byte  DIRGENAddress= (byte) 0x02;  //寄存器GEN
    public final static byte  DIRSET1Address = (byte) 0x03;  //寄存器SET1
    public final static byte  DIRALRMAddress = (byte) 0x04;  //寄存器ALRM
    public final static byte  DIRAUXOAddress = (byte) 0x0A;  //寄存器AUXO     改变指示灯的颜色
    public final static byte  DIRAUXOBddress = (byte) 0x0B;  //寄存器AUXI     读取扇门状态，关闭、打开
    public final static byte  DIRCNTAAddress = (byte) 0x06;  //寄存器 CNTA
    public final static byte  DIRCNTBAddress = (byte) 0x07;  //寄存器 CNTB
    public final static byte  DIRALARMAddress = (byte) 0x04;  //寄存器 ALARM


    //设置双向免费模式
    /**
     *   DIRAM   →   02h
     *   DIRBM   →   02h
     *   GEN     →   00h
     *   SET1    →  00h(NO)  或 01h(NC)
     *
     */

//    //双向不受控
//    public final static byte  DIRAMCode2 = (byte) 0x02;   //寄存器DIRAM    不受控
//    public final static byte  DIRBMCode2 = (byte) 0x02;   //寄存器DIRBM     不受控
//
//    //双向受控
//    public final static byte  DIRAMCode3 = (byte) 0x03;   //寄存器DIRAM    受控
//    public final static byte  DIRBMCode3 = (byte) 0x03;   //寄存器DIRBM     受控
//
//    //双向锁定(暂停模式)
//    public final static byte  DIRAMCode1 = (byte) 0x01;   //寄存器DIRAM     锁定
//    public final static byte  DIRBMCode1 = (byte) 0x01;   //寄存器DIRBM     锁定
//
//
//    public final static byte  DIRGENCode = (byte) 0x00;   //寄存器DIRGEN
//    public final static byte  DIRGENCode2 = (byte) 0x08;   //维修模式


    public final static byte  DIRSET1Code = (byte) 0x01;   //寄存器DIRSET1
    public static byte  DIRAMCode = (byte)0x03;
    public static byte  DIRBMCode = (byte)0x03;
    public static byte  DIRGENCode = (byte) 0x00;

    //设置指示灯
    public static byte  red [] = {(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};   //全红
    public static byte  green [] = {(byte)0x01,(byte)0x20,(byte)0x24,(byte)0x00};   //全绿
    public static byte  agreen [] = {(byte)0x01,(byte)0x00,(byte)0x20,(byte)0x00};    //  A绿B红
    public static byte  ared [] = {(byte)0x00,(byte)0x20,(byte)0x04,(byte)0x00};    //A红B绿

    public static byte b [] = {(byte)0x01,(byte)0x20,(byte)0x24,(byte)0x00};   //全绿(默认)

    public static void mode(int mode ){   //选择模式

        switch (mode){
            case 1:        //受控进站

                DIRAMCode =(byte) 0x03;
                DIRBMCode =(byte) 0x01;
                DIRGENCode =(byte) 0x00;
                b  = agreen;

                break;
            case 2:        //受控出站

                DIRAMCode =(byte) 0x01;
                DIRBMCode =(byte) 0x03;
                DIRGENCode =(byte) 0x00;
                b  = ared;

                break;
            case 3:        //双向受控

                DIRAMCode =(byte) 0x03;
                DIRBMCode =(byte) 0x03;
                DIRGENCode =(byte) 0x00;
                b  = green;

                break;
            case 4:        //受控进站/免费出站

                DIRAMCode =(byte) 0x03;
                DIRBMCode =(byte) 0x02;
                DIRGENCode =(byte) 0x00;
                b  = green;

                break;
            case 5:        //受控出站/免费进站

                DIRAMCode =(byte) 0x02;
                DIRBMCode =(byte) 0x03;
                DIRGENCode =(byte) 0x00;
                b  = green;

                break;
            case 6:        //免费进站/禁止出站

                DIRAMCode =(byte) 0x02;
                DIRBMCode =(byte) 0x01;
                DIRGENCode =(byte) 0x00;
                b  = agreen;

                break;
            case 7:        //免费出站/禁止进站

                DIRAMCode =(byte) 0x01;
                DIRBMCode =(byte) 0x02;
                DIRGENCode =(byte) 0x00;
                b  = ared;

                break;
            case 8:        //双向免费

                DIRAMCode =(byte) 0x02;
                DIRBMCode =(byte) 0x02;
                DIRGENCode =(byte) 0x00;
                b  = green;

                break;
            case 9:        //维修模式

                DIRAMCode =(byte) 0x03;
                DIRBMCode =(byte) 0x03;
                DIRGENCode =(byte) 0x08;
                b  = green;

                break;
        }

    }


}
