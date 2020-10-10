package com.example.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * filterType(): Returns a String that stands for the type of the filter — in this case, pre. (It would be route for a routing filter.)
 * filterOrder(): Gives the order in which this filter is to be run, relative to other filters.
 * shouldFilter(): Contains the logic that determines when to run this filter (this particular filter is always run).
 * run(): Contains the functionality of the filter. Which is doing a simple logging of any request that enters the system. But Obv It can do much more.
 */
@Component
public class LoggingFilter extends ZuulFilter{

	private static Logger log = LogManager.getLogger();
			
  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() throws ZuulException {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    log.info(
        "Request recieved. Request Method ->{}, Request URL ->{}",
        () -> request.getMethod(),
        () -> request.getRequestURL());
    return null;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 1;
  }}
