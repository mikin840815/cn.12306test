package cn.websuper.servlet;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.patchca.background.BackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.filter.ConfigurableFilterFactory;
import org.patchca.filter.library.AbstractImageOp;
import org.patchca.filter.library.WobbleImageOp;
import org.patchca.font.FontFactory;
import org.patchca.service.Captcha;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.text.renderer.TextRenderer;
import org.patchca.word.WordFactory;
import org.springframework.stereotype.Component;

import cn.websuper.common.Base64;
import cn.websuper.common.Md5Util;
import cn.websuper.constant.Constants;
import cn.websuper.constant.validate.StyleEnum;
import cn.websuper.constant.validate.ValidateEnum;
import cn.websuper.domain.validate.GroupParams;
import cn.websuper.domain.validate.GroupSingleton;
import cn.websuper.domain.validate.ImageParam;

/**
 * 
 * Created on 2015-6-25
 * <p>
 * Title: 验证码核心处理
 * </p>
 * <p>
 * Copyright: Copyright (c) 12306test.com 2015
 * </p>
 * <p>
 * Company: websuper
 * </p>
 * 
 * @author [mikin840815] [58294114@qq.com]
 * @version 1.0
 */
@Component("ValidateServlet")
public class ValidateServlet extends HttpServlet {

	private static final long serialVersionUID = -77149297980414859L;

	private static final Logger log = Logger.getLogger(ValidateServlet.class);

	/**
	 * EIGHT、SIX、NINE
	 */
	private static StyleEnum STYLE = StyleEnum.EIGHT;
	
	private static Random random = new Random();
	

    private ConfigurableCaptchaService configurableCaptchaService = null;  
    private WordFactory wordFactory = null;
    private TextRenderer textRenderer = null;  

    ArrayList<Font> fontList = new ArrayList<Font>(){{
    	add(new Font("微软雅黑", random.nextInt(10), 20)); 
    	add(new Font("宋体", random.nextInt(10), 20));
    	add(new Font("黑体", random.nextInt(10), 20));
    	add(new Font("幼圆", random.nextInt(10), 20));
    }};

    public void init() throws ServletException {  
        configurableCaptchaService = new ConfigurableCaptchaService();  
        // 颜色创建工厂,使用一定范围内的随机色  
        //colorFactory = new RandomColorFactory();  
        //configurableCaptchaService.setColorFactory(colorFactory);  
		//自定义背景
        MyCustomBackgroundFactory backgroundFactory = new MyCustomBackgroundFactory();  
        configurableCaptchaService.setBackgroundFactory(backgroundFactory);  
        // 图片滤镜设置  
        ConfigurableFilterFactory filterFactory = new ConfigurableFilterFactory();  
        List<BufferedImageOp> filters = new ArrayList<BufferedImageOp>();  
        WobbleImageOp wobbleImageOp = new WobbleImageOp();  
        wobbleImageOp.setEdgeMode(AbstractImageOp.EDGE_MIRROR);  
        wobbleImageOp.setxAmplitude(2.0);  
        wobbleImageOp.setyAmplitude(1.0);  
        filters.add(wobbleImageOp);  
        filterFactory.setFilters(filters);  
        configurableCaptchaService.setFilterFactory(filterFactory);  
        // 文字渲染器设置  
        textRenderer = new BestFitTextRenderer();  
        textRenderer.setBottomMargin(3);  
        textRenderer.setTopMargin(3);  
        configurableCaptchaService.setTextRenderer(textRenderer);  
        // 验证码图片的大小  
        configurableCaptchaService.setWidth(72);
        configurableCaptchaService.setHeight(20);  
    }  

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			long startTime = System.currentTimeMillis();
			String publicKey = request.getParameter("publicKey");
			int padding = Integer.valueOf(request.getParameter("padding"));
			int width = Integer.valueOf(request.getParameter("width"));
			int height = Integer.valueOf(request.getParameter("height"));
			String sessionId = request.getSession().getId();
//			int padding = 5;
//			int width = 67;
//			int height = 67;
			String jsonpCallback = request.getParameter("jsonpCallback");
			// 随机取分组信息
			GroupParams gp = GroupSingleton.getInstance()
					.get((int) (Math.random() * GroupSingleton.getInstance()
							.size()));
			int randoms = (int) (Math.random() * gp.getImageParam().size());
			List<ImageParam> showParams = new ArrayList<ImageParam>();
			List<ImageParam> params = new ArrayList<ImageParam>();
			for (int i = 0; i <= randoms; i++) {
				int rd = (int) (Math.random() * gp.getImageParam().size());
				params.add(gp.getImageParam().get(rd));
			}
			showParams = addOtherParams(gp.getName(), STYLE.getFeatureValue(), params);
			// 随机插入打乱顺序
			Collections.shuffle(showParams);

			HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
			final String word = gp.getName();
			WordFactory wf = new WordFactory() {
				@Override
				public String getNextWord() {
					return word;
				}
			};
			configurableCaptchaService.setWordFactory(wf);
			configurableCaptchaService.setColorFactory(new ColorFactory() {
	        	int i = random.nextInt(160);
	            @Override
	            public Color getColor(int x) {
//	                int[] c = new int[3];
//	                int i = random.nextInt(c.length);
//	                for (int fi = 0; fi < c.length; fi++) {
//	                    if (fi == i) {
//	                        c[fi] = random.nextInt(71);
//	                    } else {
//	                        c[fi] = random.nextInt(256);
//	                    }
//	                }
	                return new Color(i, i, i);
	            }
	        });
	        FontFactory fontFactory = new FontFactory() {
				@Override
				public Font getFont(int arg0) {
					return fontList.get(random.nextInt(fontList.size()));
				}
			};
	        configurableCaptchaService.setFontFactory(fontFactory);  
			// 得到验证码对象,有验证码图片和验证码字符串  
	        Captcha captcha = configurableCaptchaService.getCaptcha();
	        // 取得验证码图片并输出  
	        BufferedImage bufferedImage = captcha.getImage();
	        if(ImageIO.write(bufferedImage, "png", new File(Constants.PIC_CREATE_PATH+sessionId+"word.png"))){  
				String space = "";
				StringBuffer sb = new StringBuffer();
				// 水印
				sb.append("<style>");
				sb.append("table.gridtable {font-family:微软雅黑;font-size:12px;width:100%;position:relative;}");
				sb.append("table.gridtable th {text-align:left;line-height:25px;font-weight:none;}");
				sb.append("table.gridtable td {background-color: #ffffff;}");
				sb.append("table.gridtable img{position:absolute;top:10px;}");
				sb.append("<table bgcolor='#E5EDF7' class='gridtable' cellpadding='1' cellspacing='1'><tr><th colspan=4>请点击下图中<font color=red>所有的</font> ");
				sb.append("<img src='file:///"+ Constants.PIC_CREATE_PATH+sessionId+"word.png' />");
				sb.append("</th></tr>");
				sb.append("</style>");
				if(STYLE.getFeatureName().equals(StyleEnum.EIGHT.getFeatureName())){
					for (int i = 0; i < showParams.size(); i++) {
						String uuid = showParams.get(i).getName();
						if (uuid.equals(gp.getName()))
							space += i + ",";
						if (i == 0)
							sb.append("<tr>");
						sb.append("<td style='padding:" + padding + "px;'><img width='"
								+ width + "' height='" + height + "' src='file:///"
								+ Constants.PIC_RESOURCES
								+ showParams.get(i).getData() + "' /></td>");
						if (i == 3)
							sb.append("</tr>");
					}
				}else if(STYLE.getFeatureName().equals(StyleEnum.SIX.getFeatureName())){
					for (int i = 0; i < showParams.size(); i++) {
						String uuid = showParams.get(i).getName();
						if (uuid.equals(gp.getName()))
							space += i + ",";
						if (i == 0)
							sb.append("<tr>");
						sb.append("<td style='padding:" + padding + "px;'><img width='"
								+ width + "' height='" + height + "' src='file:///"
								+ Constants.PIC_RESOURCES
								+ showParams.get(i).getData() + "' /></td>");
						if (i == 2)
							sb.append("</tr>");
					}
				}else if(STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
					for (int i = 0; i < showParams.size(); i++) {
						String uuid = showParams.get(i).getName();
						if (uuid.equals(gp.getName()))
							space += i + ",";
						if (i == 0)
							sb.append("<tr>");
						sb.append("<td style='padding:" + padding + "px;'><img width='"
								+ width + "' height='" + height + "' src='file:///"
								+ Constants.PIC_RESOURCES
								+ showParams.get(i).getData() + "' /></td>");
						if (i == 2 || i == 5)
							sb.append("</tr>");
					}
				}
				sb.append("<tr><th colspan=2 style='text-align:left;font-size:8px;color:#666666;'>v2.1</th><th colspan=2 style='font-size:8px;text-align:right;color:#666666;'>12306test.com</th></tr>");
				sb.append("</table>");
				imageGenerator.loadHtml(sb.toString());
				log.info(gp.getName() + " 所在位置：" + space);
				// 创建储存图片二进制流的输出流
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// 创建ImageOutputStream流
				ImageOutputStream imageOutputStream = ImageIO
						.createImageOutputStream(baos);
				BufferedImage buffImg = new BufferedImage(imageGenerator
						.getBufferedImage().getWidth(), imageGenerator
						.getBufferedImage().getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = buffImg.createGraphics();
	
				// 设置对线段的锯齿状边缘处理
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.drawImage(
						imageGenerator.getBufferedImage().getScaledInstance(
								imageGenerator.getBufferedImage().getWidth(),
								imageGenerator.getBufferedImage().getHeight(),
								Image.SCALE_SMOOTH), 0, 0, null);
	
				// 设置水印旋转
				// g.rotate(Math.toRadians(-45),
				// (double) buffImg.getWidth() / 2, (double) buffImg
				// .getHeight() / 2);
	
				// 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
				ImageIcon imgIcon = new ImageIcon(
						Constants.PIC_CREATE_PATH + "12306test.png");
	
				// 得到Image对象。
				Image img = imgIcon.getImage();
	
				float alpha = 0.2f; // 透明度
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
						alpha));
	
				// 表示水印图片的位置
				g.drawImage(img, 0, 0, null);
	
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	
				g.dispose();
	
				// 将二进制数据写进ByteArrayOutputStream
				ImageIO.write(buffImg, "png", imageOutputStream);
				// 输出数组
				// response.getOutputStream().write(baos.toByteArray());
				// log.info(Md5Util.toMD5(baos.toByteArray().toString()));
				// response.flushBuffer();
				String uuid = Md5Util.toMD5(baos.toByteArray().toString());
				// 写入缓存
				// request.getSession().setAttribute(uuid, space);
				// MemcachedClientBuilder builder = new
				// XMemcachedClientBuilder(AddrUtil.getAddresses("127.0.0.1:11211"));
				// MemcachedClient memcachedClient = null;
				// try {
				// memcachedClient = builder.build();
				// memcachedClient.set(uuid, 0, space);
				// //memcachedClient.set("1615eb45df6e98b03a190958e4a20a37", 0,
				// "a0a64cd4cb88669433d3f48c412a9e57");
				// log.info("SUCCESS_TIMES="+memcachedClient.get(publicKey+"_"+ValidateEnum.SUCCESS.getFeatureName())+";FAILED_TIMES="+memcachedClient.get(publicKey+"_"+ValidateEnum.FAILED.getFeatureName())+";AUTH_TIMES="+memcachedClient.get(publicKey+"_"+ValidateEnum.AUTH.getFeatureName())+"");
				// } catch (Exception e) {
				// }finally{
				// if(null != memcachedClient){
				// memcachedClient.shutdown();
				// }
				// }
				session.setAttribute(uuid, space);//如果是集群这里可以考虑使用memcached或则redis
				response.setContentType("text/plain;charset=utf-8");
				PrintWriter pw = null;
				try {
					pw = response.getWriter();
					pw.print(jsonpCallback + "({\"uuid\":\"" + uuid
							+ "\", \"data\":\""
							+ new Base64().encode(baos.toByteArray()) + "\", \"width\":"+imageGenerator.getBufferedImage().getWidth()+", \"style\":\""+STYLE.getFeatureName()+"\"})");
					pw.flush();
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				} finally {
					if (null != pw) {
						pw.close();
					}
					
				}
				long endTime = System.currentTimeMillis();
				log.info("生成验证码用时=>" + (endTime - startTime) + "ms");
	        }
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		long startTime = System.currentTimeMillis();
		String publicKey = request.getParameter("publicKey");
		String offset = request.getParameter("offset");
		int padding = Integer.valueOf(request.getParameter("padding"));
		int width = Integer.valueOf(request.getParameter("width"));
		int height = Integer.valueOf(request.getParameter("height"));
		String uuid = request.getParameter("uuid");
		String sign = request.getParameter("sign");
		log.info("uuid=" + uuid + ">>>>offset=" + offset);
		log.info("sign=" + sign);
		PrintWriter pw = null;
		try {
			
			// log.info(land+","+offset);
			// String spaceStr = (String)
			// request.getSession().getAttribute(uuid);
			// log.info(space);

			pw = response.getWriter();
			String spaceStr = String.valueOf(session.getAttribute(uuid));
			if (null == spaceStr || "".equals(spaceStr)
					|| "null".equals(spaceStr)) {
				log.info("spaceStr is null");
				pw.print(ValidateEnum.FAILED);
				pw.flush();
				return;
			}
			String[] spaceArr = spaceStr.split(",");
			String[] offsetArr = offset.split(",");
			if (spaceArr.length != offsetArr.length / 2) {
				log.info(spaceArr.length+"!="+offsetArr.length / 2);
				pw.print(ValidateEnum.FAILED);
				pw.flush();
				return;
			}
			for (int i = 0; i < spaceArr.length; i++) {
				if (!spaceArr[i].equals("")) {
					log.info("====区域判断 start===");
					boolean flag = true;
					int _height = 25;
					switch (Integer.valueOf(spaceArr[i])) {
					case 0:
						flag = checkSpace((width * 0 + padding * 1),
								(width * 1 + padding * 1), (_height + padding),
								(_height + padding + height), offsetArr);
						break;
					case 1:
						flag = checkSpace((width * 1 + padding * 3),
								(width * 2 + padding * 3), (_height + padding),
								(_height + padding + height), offsetArr);
						break;
					case 2:
						flag = checkSpace((width * 2 + padding * 5),
								(width * 3 + padding * 5), (_height + padding),
								(_height + padding + height), offsetArr);
						break;
					case 3:
						if(STYLE.getFeatureName().equals(StyleEnum.EIGHT.getFeatureName())){
							flag = checkSpace((width * 3 + padding * 7),
									(width * 4 + padding * 7), (_height + padding),
									(_height + padding + height), offsetArr);
						}else if(STYLE.getFeatureName().equals(StyleEnum.SIX.getFeatureName())
								|| STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
							flag = checkSpace((width * 0 + padding * 1),
									(width * 1 + padding * 1),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}
						break;
					case 4:
						if(STYLE.getFeatureName().equals(StyleEnum.EIGHT.getFeatureName())){
							flag = checkSpace((width * 0 + padding * 1),
									(width * 1 + padding * 1),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}else if(STYLE.getFeatureName().equals(StyleEnum.SIX.getFeatureName())
								|| STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
							flag = checkSpace((width * 1 + padding * 3),
									(width * 2 + padding * 3),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}
						break;
					case 5:
						if(STYLE.getFeatureName().equals(StyleEnum.EIGHT.getFeatureName())){
							flag = checkSpace((width * 1 + padding * 3),
									(width * 2 + padding * 3),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}else if(STYLE.getFeatureName().equals(StyleEnum.SIX.getFeatureName())
								|| STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
							flag = checkSpace((width * 2 + padding * 5),
									(width * 3 + padding * 5),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}
						break;
					case 6:
						if(STYLE.getFeatureName().equals(StyleEnum.EIGHT.getFeatureName())){
							flag = checkSpace((width * 2 + padding * 5),
									(width * 3 + padding * 5),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}else if(STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
							flag = checkSpace((width * 0 + padding * 1),
									(width * 1 + padding * 1),
									(_height + height * 2 + padding * 3), (_height
											+ height * 3 + padding * 3), offsetArr);
						}
						break;
					case 7:
						if(STYLE.getFeatureName().equals(StyleEnum.EIGHT.getFeatureName())){
							flag = checkSpace((width * 3 + padding * 7),
									(width * 4 + padding * 7),
									(_height + height + padding * 3), (_height
											+ height * 2 + padding * 3), offsetArr);
						}else if(STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
							flag = checkSpace((width * 1 + padding * 3),
									(width * 2 + padding * 3),
									(_height + height * 2 + padding * 3), (_height
											+ height * 3 + padding * 3), offsetArr);
						}
						break;
					case 8:
						if(STYLE.getFeatureName().equals(StyleEnum.NINE.getFeatureName())){
							flag = checkSpace((width * 2 + padding * 5),
									(width * 3 + padding * 5),
									(_height + height * 2 + padding * 3), (_height
											+ height * 3 + padding * 3), offsetArr);
						}
						break;
					default:
						break;
					}
					log.info("====区域判断 end===");
					log.info("flag="+flag);
					if (!flag) {
						pw.print(ValidateEnum.FAILED);
						pw.flush();
						return;
					}
				}
			}
			// 验证完成删除缓存
			session.removeAttribute(uuid);
			pw.print(ValidateEnum.SUCCESS);
			pw.flush();
			long endTime = System.currentTimeMillis();
			log.info("校验用时=>" + (endTime - startTime) + "ms");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}

	private boolean checkSpace(int xLeft, int xRight, int yUp, int yDown,
			String[] offsetArr) {
		boolean flag = false;
		int result = 0;
		for (int i = 0; i < offsetArr.length; i++) {
			double x = Double.valueOf(offsetArr[i]);
			double y = Double.valueOf(offsetArr[i + 1]);
			i++;
			if (xLeft <= x && x <= xRight && yUp <= y && y <= yDown) {
				result++;
				flag = true;
				log.info(x + "," + y + " YES");
			} else {
				log.info(x + "," + y + " NO");
				continue;
			}
		}
		if (result == 0) {
			flag = false;
		}
		return flag;
	}

	private List<ImageParam> addOtherParams(String name, Integer max,
			List<ImageParam> params) {
		int rd = (int) (Math.random() * GroupSingleton.getInstance().size());
		GroupParams gp = GroupSingleton.getInstance().get(rd);
		if (!gp.getName().equals(name)) {
			rd = (int) (Math.random() * gp.getImageParam().size());
			ImageParam ip = gp.getImageParam().get(rd);
			params.add(ip);
			if (params.size() < max) {
				addOtherParams(name, max, params);
			}
		} else {
			addOtherParams(name, max, params);
		}
		return params;
	}
	
	public void destroy() {  
    	wordFactory = null;
        textRenderer = null;  
        configurableCaptchaService = null;  
        super.destroy(); // Just puts "destroy" string in log  
    } 
	
	/**
	 * 自定义验证码图片背景,主要画一些噪点和干扰线 
	 */
	class MyCustomBackgroundFactory implements BackgroundFactory {  

	    private Random random = new Random();  
	    
	    public void fillBackground(BufferedImage image) {  
	        Graphics graphics = image.getGraphics(); 
	        // 验证码图片的宽高  
	        int imgWidth = image.getWidth();  
	        int imgHeight = image.getHeight();  
	        // 填充为白色背景  
	        graphics.setColor(new Color(229,237,247));  
	        graphics.fillRect(0, 0, imgWidth, imgHeight);  
	        // 画100个噪点(颜色及位置随机)  
	        for(int i = 0; i < 100; i++) {  
	            // 随机颜色  
	            int rInt = random.nextInt(255);  
	            int gInt = random.nextInt(255);  
	            int bInt = random.nextInt(255);  
	            graphics.setColor(new Color(rInt, gInt, bInt));  
	            // 随机位置  
	            int xInt = random.nextInt(imgWidth - 3);  
	            int yInt = random.nextInt(imgHeight - 2);  
	            // 随机旋转角度  
	            int sAngleInt = random.nextInt(360);  
	            int eAngleInt = random.nextInt(360);  
	            // 随机大小  
	            int wInt = random.nextInt(6);  
	            int hInt = random.nextInt(6);  
	            graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);  
	            // 画5条干扰线  
	            if (i % 20 == 0) {  
	                int xInt2 = random.nextInt(imgWidth);  
	                int yInt2 = random.nextInt(imgHeight);  
	                graphics.drawLine(xInt, yInt, xInt2, yInt2);  
	            }  
	        }  
	    }  
	} 
}
