package ocr.impl;

import com.baidu.aip.ocr.AipOcr;
import exception.NoRemainingException;
import ocr.OCR;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by 618 on 2018/1/12.
 * @author lingfengsan
 */
public class BaiDuOCR implements OCR{
    //设置APPID/AK/SK
    private static final String APP_ID = "10690267";
    private static final String API_KEY = "Gc02gVtECiYjd4Gh76MUFAzO";
    private static final String SECRET_KEY = "UQS0xGWkIG8LG5OYenDaGErrDyOLQtLd";
    private static final AipOcr CLIENT=new AipOcr(APP_ID, API_KEY, SECRET_KEY);
    BaiDuOCR(){
        // 可选：设置网络连接参数
        CLIENT.setConnectionTimeoutInMillis(2000);
        CLIENT.setSocketTimeoutInMillis(60000);
        System.out.println("欢迎您使用百度OCR进行文字识别");
    }
    @Override
    public String getOCR(File file) {
        Long start=System.currentTimeMillis();
        String path=file.getAbsolutePath();
        // 调用接口
        JSONObject res = CLIENT.basicGeneral(path, new HashMap<String, String>());
        String searchResult=res.toString();
        if(searchResult.contains("error_msg")){
            try {
                throw new NoRemainingException("OCR可使用次数不足");
            } catch (NoRemainingException e) {
                return "OCR可使用次数不足,您可使用TessOCR";
            }
        }
        System.out.println(res.toString());
        JSONArray jsonArray=res.getJSONArray("words_result");
        StringBuilder sb=new StringBuilder();
        for (Object aJsonArray : jsonArray) {
            String str=aJsonArray.toString();
            str=str.substring(10,str.lastIndexOf('"'));
            sb.append(str);
            sb.append("\n");
        }
        Long time=System.currentTimeMillis()-start;
        System.out.println("tessOCR提取信息成功，耗时："+time+"s");
        return sb.toString();
    }

    public static void main(String[] args) {
        OCR ocr=new BaiDuOCR();
        String path = "/Local/Users/xiang.gao/IdeaProjects/study/1.githubs/MillionHero/images/20180117200435.png";
        String result=ocr.getOCR(new File(path));
        System.out.println(result);
    }
}
