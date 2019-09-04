package com.derik.zuul.filters;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class AccessFilter extends ZuulFilter {

	private String[] exclude = new String[] { "/first/**" };
	private String[] resource = new String[] { "/css/**", "/js/**", "/webjars/**" };
	private PathMatcher urlMatcher = new AntPathMatcher();
	
	private final Logger logger = Logger.getLogger(AccessFilter.class.getName());

	/**
	 * 检查是否静态资源，是返回true.
	 * 
	 * @param url
	 * @return
	 */
	private boolean isStaticResource(String url) {
		boolean result = false;
		for (String res : resource) {
			if (urlMatcher.match(res, url)) {
				result = true;
				break;
			}
		}

		return result;
	}
	
	private boolean isExcludeResource(String url) {
		boolean result = false;
		for (String res : exclude) {
			if (urlMatcher.match(res, url)) {
				result = true;
				break;
			}
		}

		return result;
	}

	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		logger.info("AccessFilter url:" + request.getRequestURI() + ",method:" + request.getMethod());

		// 不处理resource资源
		if (isStaticResource(request.getRequestURI())) {
			logger.info("static resource is skip");
			return null;
		}
		
		// 不处理exclude资源
		if(isExcludeResource(request.getRequestURI())) {
			logger.info("exclude resource is skip");
			return null;
		}

//		String token = request.getParameter("token");
//		if (token == null) {
//			logger.info("token is null");
//			// ctx.setSendZuulResponse(false);
//			// ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
//
//			ctx.addZuulResponseHeader("Content-Type", MediaType.TEXT_HTML_VALUE);
//			ctx.setResponseBody("Unauthorized");
//			ctx.setSendZuulResponse(false);
//			return null;
//		}

		logger.info("access token ok");

		return null;
	}

	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

	

}
