package com.georgeinfo.summary.hc;

import com.georgeinfo.summary.hc.chain.LoginNode;
import com.georgeinfo.summary.hc.chain.MessagePageNode;
import com.georgeinfo.summary.hc.chain.RequestNodeContext;
import com.georgeinfo.summary.hc.utils.HttpClientHolder;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FXMLController implements Initializable {

    private static Logger LOG = LoggerFactory.getLogger(FXMLController.class);
    public static String REQUEST_ID = "testId";

    @FXML
    private Label label;
    @FXML
    private TextArea infoShow;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        label.setText("已发送登录请求");

        //## 使用Http Client登录oschina.net网站 开始
        //组合责任链
        LoginNode loginNode = new LoginNode();
        MessagePageNode msgNode = new MessagePageNode();

        loginNode.setNext(msgNode);

        RequestNodeContext context = new RequestNodeContext();
        //现在主流的网站，都已经是SSL加密的https网址，使用http client访问https网站有诸多限制（毕竟http client没有正规
        //浏览器那样，内置很多主流的证书签名），为了演示简单起见，所以这里选择了一个普通的http论坛登录页，作为演示登录网址
        context.setLoginActionUrl("http://www.discuz.net/member.php?mod=logging&action=login&loginsubmit=yes&infloat=yes&lssubmit=yes");

        context = loginNode.process(context);

        LOG.debug("RESPONSE:" + context.getMessage());
        infoShow.setText(context.getMessage());

        HttpClient client = context.getClient();
        if (context.isSuccess() && client != null) {
            //这里，之所以把httpclient对象，放入全局单例容器，是为了登录成功后，后续操作可以共用这个httpclient对象，
            //这样就可以保持session会话，注意：如果程序是常驻程序（不是运行一次就退出的类型），则需要在处理完所有操作后
            //从全局单例容器中移除这个httpclient对象
            HttpClientHolder.getInstance().addHttpClient(REQUEST_ID, client);
        }
        //## 使用Http Client登录oschina.net网站 结束
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
