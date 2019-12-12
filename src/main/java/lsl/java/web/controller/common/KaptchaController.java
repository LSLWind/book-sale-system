package lsl.java.web.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

@Controller
public class KaptchaController {
    /**
     * 前台请求一个验证码，使用kaptcha生成验证码并将图片流写入到response中
     * @param httpServletRequest Servlet封装的request
     * @param httpServletResponse Servlet封装的response
     */
    @GetMapping("/common/mall/kaptcha")
    public void mallKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        //设置验证码图片具体属性，空格两位，长度四位
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "27,174,171");
        properties.put("kaptcha.noise.color", "20,33,42");
        properties.put("kaptcha.textproducer.font.size", "30");
        properties.put("kaptcha.image.width", "110");
        properties.put("kaptcha.image.height", "40");
        properties.put("kaptcha.session.key","lslVerifyCode");
        properties.put("kaptcha.textproducer.char.space", "2");
        properties.put("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        //产生验证码并写入为BufferedImage
        byte[] captchaOutputStream;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中以供登录检验
            String verifyCode =defaultKaptcha.createText();
            //设置session，值为刚生成的verifyCode
            httpServletRequest.getSession().setAttribute("lslVerifyCode", verifyCode);
            BufferedImage challenge = defaultKaptcha.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //将图片流打入response中
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
