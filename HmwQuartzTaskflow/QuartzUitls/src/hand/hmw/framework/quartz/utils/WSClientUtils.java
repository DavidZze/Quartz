package hand.hmw.framework.quartz.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class WSClientUtils implements Serializable{
    public WSClientUtils() {
        super();
    }

    public static String callWebService(String soapRequest,
                                        String serviceEpr) {
        return callWebService(soapRequest, serviceEpr,
                              "application/soap+xml; charset=utf-8");
    }

    public static String callWebService(String soapRequest, String serviceEpr,
                                        String contentType) {

        PostMethod postMethod = new PostMethod(serviceEpr);
        //设置POST方法请求超时
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);

        try {

            //              byte[] b = soapRequest.getBytes("utf-8");
            //              InputStream inputStream = new ByteArrayInputStream(b, 0, b.length);
            //              RequestEntity re = new InputStreamRequestEntity(inputStream, b.length, contentType);
            //              postMethod.setRequestEntity(re);

            StringRequestEntity requestEntity =
                new StringRequestEntity(soapRequest, contentType, "UTF-8");
            postMethod.setRequestEntity(requestEntity);

            HttpClient httpClient = new HttpClient();
            Credentials defaultcreds =
                new UsernamePasswordCredentials("weblogic", "manager05");
            httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
            HttpConnectionManagerParams managerParams =
                httpClient.getHttpConnectionManager().getParams();
            // 设置连接超时时间(单位毫秒)
            managerParams.setConnectionTimeout(300000);
            // 设置读数据超时时间(单位毫秒)
            managerParams.setSoTimeout(6000000);
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK)
                throw new IllegalStateException("调用webservice错误 : " +
                                                postMethod.getStatusLine());

            String soapRequestData = postMethod.getResponseBodyAsString();
            //              inputStream.close();
            return soapRequestData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "errorMessage : " + e.getMessage();
        } catch (HttpException e) {
            e.printStackTrace();
            return "errorMessage : " + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "errorMessage : " + e.getMessage();
        } finally {
            postMethod.releaseConnection();
        }
    }


    public static void main(String[] args) {
        
        
        WSClientUtils u = new WSClientUtils();
        System.out.println("HelloWorld");
        String request =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.glodon.com/soa/utilities/servicemonitor/schema/CustomSoapHeader/V1.0\" xmlns:v11=\"http://www.glodon.com/soa/ebs/material/schema/SaleEbsInvMaterialCreateSvcProxy/V1.0\">\n" +
            "   <soapenv:Header>\n" +
            "      <v1:CustomSOAPHeader>\n" +
            "         <v1:conversationId></v1:conversationId>\n" +
            "         <v1:consumer>soa_oa_2790</v1:consumer>\n" +
            "         <v1:provider>GLODON_SOA_SIEBEL_PRODUCT_001</v1:provider>\n" +
            "         <v1:messageType></v1:messageType>\n" +
            "         <v1:remark1></v1:remark1>\n" +
            "         <v1:remark2></v1:remark2>\n" +
            "         <v1:remark3></v1:remark3>\n" +
            "         <v1:remark4></v1:remark4>\n" +
            "         <v1:remark5></v1:remark5>\n" +
            "      </v1:CustomSOAPHeader>\n" +
            "   </soapenv:Header>\n" +
            "   <soapenv:Body>\n" +
            "      <v11:InputParameters>\n" +
            "         <P_ITM_REC_TABLE>\n" +
            "            <!--1 or more repetitions:-->\n" +
            "            <P_ITM_REC_TABLE_ITEM>\n" +
            "               <ITEM_NUMBER>SOATEST021</ITEM_NUMBER>\n" +
            "               <UOM_CODE>套</UOM_CODE>\n" +
            "               <ITEM_DESCRIPTION>测试物料x3</ITEM_DESCRIPTION>\n" +
            "               <TEMPLATE_NAME>实物</TEMPLATE_NAME>\n" +
            "               <ITEM_CATEGORY_DESCRIPTION>广联达定额排版系统V3.0</ITEM_CATEGORY_DESCRIPTION>\n" +
            "               <REFERENCE_KEY>1-SOATEST21</REFERENCE_KEY>\n" +
            "            </P_ITM_REC_TABLE_ITEM>\n" +
            "         </P_ITM_REC_TABLE>\n" +
            "      </v11:InputParameters>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>\n";
        System.out.println(u.callWebService(request,
                                            "http://soadev.glodon.com:8011/EBS/Product/ProxyService/SaleEbsInvMaterialCreateSvcProxy?wsdl")); //, "application/soap+xml; charset=utf-8"

    }
}


