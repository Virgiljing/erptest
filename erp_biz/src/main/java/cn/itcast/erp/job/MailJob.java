package cn.itcast.erp.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.StoreAlert;
import cn.itcast.erp.util.MailUtil;

@Component("mailJob")
public class MailJob {
	@Autowired
	private MailUtil mailUtil;
	@Autowired
	private IStoredetailBiz storedetailBiz;

    @Value("${mail.storealert_backend_title}")
    private String title;
    @Value("${mail.storealert_backend_to}")
    private String to;
    @Value("${mail.storealert_backend_content}")
    private String content;
    public void doJob() {
    	List<StoreAlert> list = storedetailBiz.getStoreAlertList();
    	if (null != list && list.size() > 0 ) {
    		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             String _title = title.replace("[time]",sdf.format(new Date()));
             String _content = content.replace("[count]", list.size() + "");
             // 存在库存预警
             try {
                 mailUtil.sendMail( to,_title, _content);
                 System.out.println("邮件发送成功");
             } catch (MessagingException e) {
                 e.printStackTrace();
             }
         }
    }
}
