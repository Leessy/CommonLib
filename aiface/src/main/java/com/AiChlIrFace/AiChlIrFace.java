package com.AiChlIrFace;

// 人脸识别认证 SDK 接口类
public class AiChlIrFace {

    // 获取SDK版本（可不调用）
    // 返回：SDK版本号
    // 备注：可不调用，任何时候均可调用
    public static native int Ver();

    // SDK初始化
    // 输入参数：
    //    nMaxChannelNum ---- 需要开启的最大通道数（受加密狗控制）
    //    strCachePath ---- 本APP的cache目录，需要此目录有可读写权限，且能根据上级目录找到lib目录加载模型文件（可参考DEMO例程获取cache目录）
    // 返回：成功返回0，许可无效返回-1，算法初始化失败返回-2
    // 备注：附获取SDK版本外的任何接口都必须在SDK初始化成功后才能调用
    public static native int Init(int nMaxChannelNum, String strCacheDir);
    // 带调试信息的初始化函数
    public static native int InitDebug(int nMaxChannelNum, String strCacheDir);

    // SDK反初始化
    // 备注：必须在初始化成功后调用，反初始化后不能再调用除获取SDK版本及SDK初始化外的任何接口
    public static native void Uninit();

    // 检测人脸
    // 输入参数：
    //           nChannelNo ---- 通道号（0 ~ nMaxChannelNum - 1）
    //           bRgb24 ---- RGB24格式的图象数据
    //           nWidth ---- 图象数据宽度（象素单位）
    //           nHeight ---- 图象数据高度（象素单位）
    // 输出参数：sFaceResult ---- 结构体存放检测到的人脸参数（人脸及眼睛等坐标位置及角度等，调用前必须分配有效的空间）
    // 返回：返回1表示检测到人脸，0表示无人脸，-1表示检测失败
    public static native int DetectFace(int nChannelNo, byte[] bRgb24, int nWidth, int nHeight, FACE_DETECT_RESULT sFaceResult);

    // 检测单个（最大的）人脸, 支持NV21格式，支持旋转与镜象，支持中间区域检测以加快检测速度
    // 输入参数：
    //        nChannelNo ----  通道号(0 ~ nMaxChannelNum - 1)
    //        nFmt ---- 输入源图象数据格式（0：YUV420P, 1: NV12，2: NV21）
    //        bSrcImg ---- 输入源图象数据
    //        nWidth ---- 输入源图象的宽度（象素单位）
    //        nHeight ---- 输入源图象的高度（象素单位）
    //        nLeft ---- 检测区域左上角X坐标(相对于输入源图象，全图检测时填0)
    //        nTop ---- 检测区域左上角Y坐标(相对于输入源图象，全图检测时填0)
    //        nRight ---- 检测区域右下角X坐标(相对于输入源图象，全图检测时也可填0)
    //        nBottom ---- 检测区域右下角Y坐标(相对于输入源图象，全图检测时也可填0)
    //        nRotate ---- 旋转方式（对输入源图象旋转，0：不旋转，1：左旋90度，2：右旋90度）
    //        bMirror ---- 左右镜象（相对于旋转后的图象，0：左右不镜象，1：左右镜象）
    // 输出参数:
    //        bRgb24 ---- 输出的RGB24格式图象数据(裁减、旋转和镜象后的图象数据)
    //        nNewWidth ---- 输出图象的宽度(裁减、旋转和镜象后的图象宽度)
    //        nNewHeight ---- 输出图象的高度(裁减、旋转和镜象后的图象宽度)
    //        sFaceResult ---- 检测到的人脸参数（人脸及眼睛等坐标位置及角度等，相对于裁减、旋转和镜象后的图象，调用前必须分配有效的空间）
    // 返回：返回1表示检测到人脸，0表示无人脸，< 0 表示检测失败
    // 备注：1. 使用此接口的检测结果来提取特征或检测活体时，必须使用这里的输出图象、输出图象的宽高和检测到的人脸参数做为参数
    //       2. 界面上需要画人脸人眼等坐标时，需要对检测出的人脸坐标参数根据裁减、旋转和镜象情况进行校正
    public static native int DetectFaceEx(int nChannelNo, int nFmt, byte[] bSrcImg, int nWidth, int nHeight, int nLeft, int nTop, int nRight, int nBottom, int nRotate, int bMirror, byte[] bRgb24, int[] nNewWidth, int[] nNewHeight, FACE_DETECT_RESULT sFaceResult);

    // 获取特征码大小
    // 返回：特征码大小
    public static native int FeatureSize();

    // 提取特征码
    // 输入参数：
    //           nChannelNo ---- 通道号（0 ~ nMaxChannelNum - 1）
    //           bRgb24 ---- RGB24格式的图象数据
    //           nWidth ---- 图象数据宽度
    //           nHeight ---- 图象数据高度
    //           sFaceResult ---- 检测到的人脸参数（必须将检测人脸返回的结果原样传入）
    // 输出参数：bFeature ---- 特征码（调用前必须分配有效的空间）
    // 成功返回0，失败返回-1
    public static native int FeatureGet(int nChannelNo, byte[] bRgb24, int nWidth, int nHeight, FACE_DETECT_RESULT sFaceResult, byte[] bFeature);

    // 一对一特征比对
    // 输入参数：
    //           nChannelNo ---- 通道号（0 ~ nMaxChannelNum - 1）
    //           bFeature1 ---- 第1个人脸特征
    //           bFeature2 ---- 第2个人脸特征
    // 返回：返回两个人脸特征对应的人脸的相似度（0-100）
    public static native int FeatureCompare(int nChannelNo, byte[] bFeature1, byte[] bFeature2);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为一对多比对接口，对于目标人员较多且相对固定的场景，效率远比循环调用一对一接口时要高得多                 //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 创建一对多特征比对列表
    // 输入参数：nMaxFeatureNum ---- 列表能容纳的最大特征数
    // 返回：返回特征比对列表句柄，为空表示创建列表失败
    // 备注：能容纳的最大特征数越大，则消耗的内存越多，另外实际支持的最大特征数可能受加密狗限制
    public static native int ListCreate(int nMaxFeatureNum);

    // 向一对多特征比对列表加入目标特征
    // 输入参数：
    //        hList ---- 目标特征要加入的特征比对列表句柄
    //        nPos ---- 指针非空时存放特征插入位置，位置值为0表示插到列表最前面，为-1或大于当前列表中的特征数则表示插到列表最后面
    //        bFeatures ---- 要插入的目标特征码，多个特征按特征码大小（IdFaceSdkFeatureSize函数接口返回的值）对齐
    //        nFeatureNum ---- 要插入的特征数量
    // 输出参数：pnPos ---- 指针非空时返回第一个特征的插入位置
    // 返回：返回当前列表的总特征数
    public static native int ListInsert(int hList, int[] nPos, byte[] bFeatures, int nFeatureNum);

    // 从一对多特征比对列表中删除部分特征
    // 输入参数：
    //        hList ---- 要删除特征的特征比对列表句柄
    //        nPos ---- 要删除的特征的起始位置
    //        nFeatureNum ---- 要删除的特征数量
    // 返回：返回当前列表的总特征数
    public static native int ListRemove(int hList, int nPos, int nFeatureNum);

    // 清空一对多特征比对列表中的所有特征
    // 输入参数：
    //        hList ---- 要清空的一对多特征比对列表句柄
    // 返回：无
    // 备注：调用后列表中的实际特征数量为0（列表能容纳的最大特征数保持不变）
    public static native void ListClearAll(int hList);

    // 一对多特征比对
    // 输入参数：
    //        nChannelNo ---- 通道号（0 ~ nMaxChannelNum - 1）
    //        hList ---- 存放要参与比较的目标特征库的特征比对列表句柄
    //        bFeature ---- 要参与特征比对的源特征码
    //        nPosBegin ---- 要参与比对的目标特征的起始位置（如果比对全部目标特征，则起始位置填0）
    //        nCompareNum ---- 要参与比对的目标特征的数量（如果比对全部目标特征，则比对数量填0或-1或实际特征数或最大特征数）
    // 输出参数：nScores ---- 顺序存放与各目标特征进行比对的相似度（每个特征比对的结果均为一字节，值范围为0-100）
    // 返回：返回实际参与比对的特征数量，也是返回的相似度个数
    public static native int ListCompare(int nChannelNo, int hList, byte[] bFeature, int nPosBegin, int nCompareNum, byte[] nScores);

    // 销毁一对多特征比对列表
    // 输入参数：
    //        hList ---- 要销毁的一对多特征比对列表句柄
    // 返回：无
    // 备注：特征比对列表不再使用时需调用此接口释放资源
    public static native void ListDestroy(int hList);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为活体检测接口                                                                                           //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 活体检测
    // 输入参数：
    //           nChannelNo ---- 通道号（0 ~ nMaxChannelNum - 1）
    //           nWidth ---- 图象数据宽度（象素单位）
    //           nHeight ---- 图象数据高度（象素单位）
    //           bColorRgb24 ---- RGB24格式的彩色图象数据
    //           sColorFaceResult ---- 彩色图象检测出的人脸参数（调用 AiFaceDetectFace 对彩色图象检测出的结果）
    //           bBwRgb24 ---- RGB24格式的黑白图象数据
    //           sBwFaceResult ---- 黑白图象检测出的人脸参数（调用 AiFaceDetectFace 对黑白图象检测出的结果）
    // 输出参数：无
    // 返回值：
    //    1 ---- 已确认是活体,本轮结果无需继续检测
    //    0 ---- 本帧不确认是活体,需继续检测
    //   -1 ---- 活体检测功能未授权
    //   -2 ---- 参数错误
    //   -3 ---- 内部错误
    // 备注：
    //    1. 需要同时对黑白图象和彩色图象进行人脸检测，并且都能检测到人脸，然后将检测结果传入
    //    2. 过程中只要一次确认是活体，则本次结果确认为活体；如超时仍无一次确认是活体，则本次结果确认为非活体
    public static native int LiveDetect(int nChannelNo, int nWidth, int nHeight, byte[] bColorRgb24, FACE_DETECT_RESULT sColorFaceResult, byte[] bBwRgb24, FACE_DETECT_RESULT sBwFaceResult);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为特殊应用接口，自动筛选模板完成注册                                                                     //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 启动当前用户的自动注册
    // 输入参数：
    //        nMaxFeature ---- 该用户可注册的最大特征个数（有效值为1-10，建议设置值为3-5，小于1 返回失败，超过10按10处理）
    // 返回值：返回0表示成功，返回-1表示参数错误或未初始化SDK
    public static native int AutoRegStart(int nMaxFeature);

    // 输入预注册的图象
    // 输入参数：
    //        bRgb24 ---- RGB24格式的图象数据
    //        nWidth ---- 图象数据宽度
    //        nHeight ---- 图象数据高度
    // 返回值：返回0表示未检测到人脸，返回1表示检测到人脸但不合格，返回2表示检测到人脸但被忽略，返回3表示检测到人脸并已提取特征，返回负数表示参数无效或未初始化SDK
    // 备注：需不断调用此接口以提取或更新特征码，如果前后帧非常不连续，则被视为人脸在移动而放弃本帧图象
    public static native int AutoRegProcess(byte[] bRgb24, int nWidth, int nHeight);

    // 结束当前用户的自动注册
    // 输入参数：
    //        listener ---- 回调接口，通过本接口的OnAutoRegInfo返回已注册的特征码及对应的人脸图象数据
    // 返回值：非负值表示已注册的特征个数，-1表示参数错误或未初始化SDK
    public static native int AutoRegFinish(AutoRegListener listener);

    // 取消当前用户的自动注册
    // 输入输出参数：无
    // 返回值：无
    public static native void AutoRegCancel();

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为支持应用做二次加密开放接口                                                                                               //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 获取加密狗的唯一ID
    // 输入参数：无
    // 输出参数：bHwID ---- 返回加密狗的唯一ID（8字节二进制数据，调用前必须预分配8字节缓冲区）
    // 返回值：无
    // 备注：对于DM2016加密芯片，需先调用AiDogWriteLicense写入了唯一ID，否则返回的ID无效
    public static native void AiDogGetID(byte[] bHwID);

    // 将应用所需的加密数据写入加密狗中
    // 输入参数：bData ---- 要写入加密狗中的加密数据
    //           nDataLen ---- 要写入的加密数据长度，最大支持64字节
    // 输出参数：无
    // 返回值：返回0表示写数据成功，其它表示写数据失败
    public static native int AiDogWriteData(byte[] bData, int nDataLen);

    // 读出应用写入加密狗中的加密数据
    // 输入参数：nReadLen ---- 要读出的加密数据的长度，最大支持64字节
    // 输出参数：bData ---- 返回读出的加密狗中的加密数据
    // 返回值：返回0表示读数据成功，其它表示读数据失败
    public static native int AiDogReadData(byte[] bData, int nReadLen);

    // 写加密狗的授权数据
    // 输入参数： bHwID ---- 要写入加密狗中的加密狗唯一ID
    //            bLicense ---- 要写入加密狗中的授权数据（授权数据必需由特征工具生成，长度为56字节）
    // 输出参数：无
    // 返回值：返回0表示写入授权数据成功，-1表示写入授权数据失败
    // 备注：本接口仅支持DM2016加密芯片
    public static native int AiDogWriteLicense(byte[] bHwID, byte[] bLicense);

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                               //
    //  以下为辅助接口                                                                                               //
    //                                                                                                               //
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 图象文件解析出原始图象数据（支持 BMP、JPEG 和 PNG 图象）
    // 输入参数：
    //        strFileName ---- 要解析的图象文件名
    //        nBufferSize ---- 图象数据缓冲区长度，长度值小于原始数据所需空间时，解析失败只返回图象分辨率不返回数据
    //        nDepth ---- 需要解析出来的RGB图象数据的位深度（支持8位、16位、24位和32位；需要24位彩色图象传入24，需要8位灰度图象传入8）
    // 输出参数：
    //        bBuffer ---- 解析出的原始图象数据，必须预先分配足够的缓冲区，或者传入空值表示只检测图象分辨率不返回数据
    //        nWidth ---- 返回图象宽度
    //        nHeight ---- 返回图象高度
    // 返回：0-成功 ，< 0 失败
    public static native int ReadImageFile(String strFileName, byte[] bBuffer, int nBufSize, int[] nWidth, int[] nHeight, int nDepth);

    // RGB24或灰度图象数据压缩成JPEG文件
    // 输入参数：
    //        strFileName ---- 要保存的JPEG文件名
    //        bData ---- RGB24或灰度图象数据
    //        nWidth ---- 图象宽度
    //        nHeight ---- 图象高度
    //        nDepth ---- 图象数据的颜色深度，灰度图象数据必须为 8，RGB24图象数据必须为 24
    //        nQuality ---- 图象质量（范围：0-100, 值越大图象质量超好文件也越大，建议一般场景为 75，要求高图象质量时为 95）
    // 返回：0-成功 ，< 0 失败
    public static native int SaveJpegFile(String strFileName, byte[] bData, int nWidth, int nHeight, int nDepth, int nQuality);

    // YUV422图象数据转换为RGB24图象数据
    // 输入参数：
    //        bYuv422 ---- YUV422格式的图象数据（bYExchange参数为0时，图象数据序列：U Y V Y U Y V Y ...，bYExchange参数为1时，图象数据序列为：Y U Y V Y U Y V ...）
    //        nWidth ---- 图象宽度
    //        nHeight ---- 图象高度
    //        bYExchange ---- Y与UV排列位置交换（为0表示UYVY，为1表示YUYV）
    // 输出参数：
    //        bRgb24 ---- RGB24格式的图象数据，必须预先分配足够的缓冲区
    // 返回：0-成功 ，< 0 失败
    public static native int YUV422_TO_RGB24(byte[] bYuv422, int nWidth, int nHeight, int bYExchange, byte[] bRgb24);

    // YUV420P图象数据转换为RGB24图象数据
    // 输入参数：
    //        bYuv420P ---- YUV420P格式的图象数据（nFmt参数为0时，图象数据序列：Y Y Y Y ... U ... V ..., nFmt为1时，图象数据序列为：Y Y Y Y ... U V ...,nFmt为2时，图象数据序列为：Y Y Y Y ... V U ...）
    //        nWidth ---- 图象宽度
    //        nHeight ---- 图象高度
    //        nFmt ---- 数据格式（0：YUV420P, 1: NV12，2: NV21）
    // 输出参数：
    //        bRgb24 ---- RGB24格式的图象数据，必须预先分配足够的缓冲区
    // 返回：0-成功 ，< 0 失败
    public static native int YUV420P_TO_RGB24(byte[] bYuv420P, int nWidth, int nHeight, int nFmt, byte[] bRgb24);

    static {
        try {
            System.loadLibrary("AiChlIrFace");
        } catch (Throwable e) {
        }
    }
}
