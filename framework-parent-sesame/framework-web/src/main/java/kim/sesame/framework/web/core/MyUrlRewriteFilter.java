package kim.sesame.framework.web.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.Status;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import org.tuckey.web.filters.urlrewrite.UrlRewriteWrappedResponse;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;
import org.tuckey.web.filters.urlrewrite.utils.Log;
import org.tuckey.web.filters.urlrewrite.utils.ModRewriteConfLoader;
import org.tuckey.web.filters.urlrewrite.utils.NumberUtils;
import org.tuckey.web.filters.urlrewrite.utils.ServerNameMatcher;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

public class MyUrlRewriteFilter extends UrlRewriteFilter {

	private static Log log = Log.getLog(MyUrlRewriteFilter.class);

	public static String DEFAULT_WEB_CONF_PATH = "/WEB-INF/urlrewrite.xml";

	/**
	 * The conf for this filter.
	 */
	private UrlRewriter urlRewriter = null;

	/**
	 * A user defined setting that can enable conf reloading.
	 */
	private boolean confReloadCheckEnabled = false;

	/**
	 * A user defined setting that says how often to check the conf has changed.
	 */
	private int confReloadCheckInterval = 0;

	/**
	 * A user defined setting that will allow configuration to be swapped via an
	 * HTTP to rewrite-status.
	 */
	private boolean allowConfSwapViaHttp = false;

	/**
	 * The last time that the conf file was loaded.
	 */
	private long confLastLoad = 0;
	private Conf confLastLoaded = null;
	private long confReloadLastCheck = 30;
	private boolean confLoadedFromFile = true;

	/**
	 * path to conf file.
	 */
	private String confPath;

	/**
	 * Flag to make sure we don't bog the filter down during heavy load.
	 */
	private boolean confReloadInProgress = false;

	private boolean statusEnabled = true;
	private String statusPath = "/rewrite-status";

	private boolean modRewriteStyleConf = false;
	public static final String DEFAULT_MOD_REWRITE_STYLE_CONF_PATH = "/WEB-INF/.htaccess";

	private ServerNameMatcher statusServerNameMatcher;
	private static final String DEFAULT_STATUS_ENABLED_ON_HOSTS = "localhost, local, 127.0.0.1";

	/**
	 *
	 */
	private ServletContext context = null;

	/**
	 * Init is called automatically by the application server when it creates
	 * this filter.
	 *
	 * @param filterConfig
	 *            The config of the filter
	 */
	public void init(final FilterConfig filterConfig) throws ServletException {

		log.debug("filter init called");
		if (filterConfig == null) {
			log.error("unable to init filter as filter config is null");
			return;
		}

		log.debug("init: calling destroy just in case we are being re-inited uncleanly");
		destroyActual();

		context = filterConfig.getServletContext();
		if (context == null) {
			log.error("unable to init as servlet context is null");
			return;
		}

		// set the conf of the logger to make sure we get the messages in
		// context log
		Log.setConfiguration(filterConfig);

		// get init paramerers from context web.xml file
		String confReloadCheckIntervalStr = filterConfig.getInitParameter("confReloadCheckInterval");
		String confPathStr = filterConfig.getInitParameter("confPath");
		String statusPathConf = filterConfig.getInitParameter("statusPath");
		String statusEnabledConf = filterConfig.getInitParameter("statusEnabled");
		String statusEnabledOnHosts = filterConfig.getInitParameter("statusEnabledOnHosts");

		String allowConfSwapViaHttpStr = filterConfig.getInitParameter("allowConfSwapViaHttp");
		if (!StringUtils.isBlank(allowConfSwapViaHttpStr)) {
			allowConfSwapViaHttp = "true".equalsIgnoreCase(allowConfSwapViaHttpStr);
		}

		// confReloadCheckInterval (default to null)
		if (!StringUtils.isBlank(confReloadCheckIntervalStr)) {
			// convert to millis
			confReloadCheckInterval = 1000 * NumberUtils.stringToInt(confReloadCheckIntervalStr);

			if (confReloadCheckInterval < 0) {
				confReloadCheckEnabled = false;
				log.info("conf reload check disabled");

			} else if (confReloadCheckInterval == 0) {
				confReloadCheckEnabled = true;
				log.info("conf reload check performed each request");

			} else {
				confReloadCheckEnabled = true;
				log.info("conf reload check set to " + confReloadCheckInterval / 1000 + "s");
			}

		} else {
			confReloadCheckEnabled = false;
		}

		String modRewriteConf = filterConfig.getInitParameter("modRewriteConf");
		if (!StringUtils.isBlank(modRewriteConf)) {
			modRewriteStyleConf = "true".equals(StringUtils.trim(modRewriteConf).toLowerCase());
		}

		if (!StringUtils.isBlank(confPathStr)) {
			confPath = StringUtils.trim(confPathStr);
		} else {
			confPath = modRewriteStyleConf ? DEFAULT_MOD_REWRITE_STYLE_CONF_PATH : DEFAULT_WEB_CONF_PATH;
		}
		log.debug("confPath set to " + confPath);

		// status enabled (default true)
		if (statusEnabledConf != null && !"".equals(statusEnabledConf)) {
			log.debug("statusEnabledConf set to " + statusEnabledConf);
			statusEnabled = "true".equals(statusEnabledConf.toLowerCase());
		}
		if (statusEnabled) {
			// status path (default /rewrite-status)
			if (statusPathConf != null && !"".equals(statusPathConf)) {
				statusPath = statusPathConf.trim();
				log.info("status display enabled, path set to " + statusPath);
			}
		} else {
			log.info("status display disabled");
		}

		if (StringUtils.isBlank(statusEnabledOnHosts)) {
			statusEnabledOnHosts = DEFAULT_STATUS_ENABLED_ON_HOSTS;
		} else {
			log.debug("statusEnabledOnHosts set to " + statusEnabledOnHosts);
		}
		statusServerNameMatcher = new ServerNameMatcher(statusEnabledOnHosts);

		// now load conf from snippet in web.xml if modRewriteStyleConf is set
		String modRewriteConfText = filterConfig.getInitParameter("modRewriteConfText");
		if (!StringUtils.isBlank(modRewriteConfText)) {
			ModRewriteConfLoader loader = new ModRewriteConfLoader();
			Conf conf = new Conf();
			loader.process(modRewriteConfText, conf);
			conf.initialise();
			checkConf(conf);
			confLoadedFromFile = false;

		} else {

			loadUrlRewriter(filterConfig);
		}
	}

	/**
	 * Separate from init so that it can be overidden.
	 */
	protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
		try {
			loadUrlRewriterLocal();
		} catch (Throwable e) {
			log.error(e);
			throw new ServletException(e);
		}
	}

	private void loadUrlRewriterLocal() {
		InputStream inputStream = context.getResourceAsStream(confPath);
		if (inputStream == null) {
			//inputStream = ClassLoader.getSystemResourceAsStream(confPath);
            inputStream = getClass().getResourceAsStream("urlrewrite.xml");
		}
		URL confUrl = null;
		try {
			confUrl = context.getResource(confPath);
		} catch (MalformedURLException e) {
			log.debug(e);
		}
		String confUrlStr = null;
		if (confUrl != null) {
			confUrlStr = confUrl.toString();
		}
		if (inputStream == null) {
			log.error("unable to find urlrewrite conf file at " + confPath);
			// set the writer back to null
			if (urlRewriter != null) {
				log.error("unloading existing conf");
				urlRewriter = null;
			}

		} else {
			Conf conf = new Conf(context, inputStream, confPath, confUrlStr, modRewriteStyleConf);
			checkConf(conf);
		}
	}

	/**
	 * Separate from checkConfLocal so that it can be overidden.
	 */
	protected void checkConf(Conf conf) {
		checkConfLocal(conf);
	}

	private void checkConfLocal(Conf conf) {
		if (log.isDebugEnabled()) {
			if (conf.getRules() != null) {
				log.debug("inited with " + conf.getRules().size() + " rules");
			}
			log.debug("conf is " + (conf.isOk() ? "ok" : "NOT ok"));
		}
		confLastLoaded = conf;
		if (conf.isOk() && conf.isEngineEnabled()) {
			urlRewriter = new UrlRewriter(conf);
			log.info("loaded (conf ok)");

		} else {
			if (!conf.isOk()) {
				log.error("Conf failed to load");
			}
			if (!conf.isEngineEnabled()) {
				log.error("Engine explicitly disabled in conf"); // not really
																	// an error
																	// but we
																	// want ot
																	// to show
																	// in logs
			}
			if (urlRewriter != null) {
				log.error("unloading existing conf");
				urlRewriter = null;
			}
		}
	}

	/**
	 * Destroy is called by the application server when it unloads this filter.
	 */
	public void destroy() {
		log.info("destroy called");
		destroyActual();
		Log.resetAll();
	}

	public void destroyActual() {
		destroyUrlRewriter();
		context = null;
		confLastLoad = 0;
		confPath = DEFAULT_WEB_CONF_PATH;
		confReloadCheckEnabled = false;
		confReloadCheckInterval = 0;
		confReloadInProgress = false;
	}

	protected void destroyUrlRewriter() {
		if (urlRewriter != null) {
			urlRewriter.destroy();
			urlRewriter = null;
		}
	}

	/**
	 * The main method called for each request that this filter is mapped for.
	 *
	 * @param request
	 *            the request to filter
	 * @param response
	 *            the response to filter
	 * @param chain
	 *            the chain for the filtering
	 * @throws IOException IOException
	 * @throws ServletException ServletException
	 */
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		UrlRewriter urlRewriter = getUrlRewriter(request, response, chain);

		final HttpServletRequest hsRequest = (HttpServletRequest) request;
		final HttpServletResponse hsResponse = (HttpServletResponse) response;
		UrlRewriteWrappedResponse urlRewriteWrappedResponse = new UrlRewriteWrappedResponse(hsResponse, hsRequest, urlRewriter);

		// check for status request
		if (statusEnabled && statusServerNameMatcher.isMatch(request.getServerName())) {
			String uri = hsRequest.getRequestURI();
			if (log.isDebugEnabled()) {
				log.debug("checking for status path on " + uri);
			}
			String contextPath = hsRequest.getContextPath();
			if (uri != null && uri.startsWith(contextPath + statusPath)) {
				showStatus(hsRequest, urlRewriteWrappedResponse);
				return;
			}
		}

		boolean requestRewritten = false;
		if (urlRewriter != null) {

			// process the request
			requestRewritten = urlRewriter.processRequest(hsRequest, urlRewriteWrappedResponse, chain);

		} else {
			if (log.isDebugEnabled()) {
				log.debug("urlRewriter engine not loaded ignoring request (could be a conf file problem)");
			}
		}

		// if no rewrite has taken place continue as normal
		if (!requestRewritten) {
			chain.doFilter(hsRequest, urlRewriteWrappedResponse);
		}
	}

	protected UrlRewriter getUrlRewriter(ServletRequest request, ServletResponse response, FilterChain chain) {
		// check to see if the conf needs reloading
		if (isTimeToReloadConf()) {
			reloadConf();
		}
		return urlRewriter;
	}


	public boolean isTimeToReloadConf() {
		if (!confLoadedFromFile)
			return false;
		long now = System.currentTimeMillis();
		return confReloadCheckEnabled && !confReloadInProgress && (now - confReloadCheckInterval) > confReloadLastCheck;
	}

	public void reloadConf() {
		long now = System.currentTimeMillis();
		confReloadInProgress = true;
		confReloadLastCheck = now;

		log.debug("starting conf reload check");
		long confFileCurrentTime = getConfFileLastModified();
		if (confLastLoad < confFileCurrentTime) {
			// reload conf
			confLastLoad = System.currentTimeMillis();
			log.info("conf file modified since last load, reloading");
			try {
				loadUrlRewriterLocal();
			} catch (Exception ex) {
				log.error("Error in reloading the conf file. No rules to be applied for subsequent requests.", ex);
			}
		} else {
			log.debug("conf is not modified");
		}
		confReloadInProgress = false;
	}

	private long getConfFileLastModified() {
		if (context != null) {
			String realPath = context.getRealPath(confPath);
			if (realPath != null) {
				File confFile = new File(context.getRealPath(confPath));
				return confFile.lastModified();
			}
		}
		return INITIALISED_TIME;
	}

	private static long INITIALISED_TIME = System.currentTimeMillis();


	private void showStatus(final HttpServletRequest request, final ServletResponse response) throws IOException {

		log.debug("showing status");

		if (allowConfSwapViaHttp) {
			String newConfPath = request.getParameter("conf");
			if (!StringUtils.isBlank(newConfPath)) {
				confPath = newConfPath;
				loadUrlRewriterLocal();
			}
		}

		Status status = new Status(confLastLoaded, this);
		status.displayStatusInContainer(request);

		response.setContentType("text/html; charset=UTF-8");
		response.setContentLength(status.getBuffer().length());

		final PrintWriter out = response.getWriter();
		out.write(status.getBuffer().toString());
		out.close();

	}

	public boolean isConfReloadCheckEnabled() {
		return confReloadCheckEnabled;
	}

	public int getConfReloadCheckInterval() {
		return confReloadCheckInterval / 1000;
	}

	public Date getConfReloadLastCheck() {
		return new Date(confReloadLastCheck);
	}

	public boolean isStatusEnabled() {
		return statusEnabled;
	}

	public String getStatusPath() {
		return statusPath;
	}

	public boolean isLoaded() {
		return urlRewriter != null;
	}

	public static String getFullVersionString() {
		Properties props = new Properties();
		String buildNumberStr = "";
		try {
			InputStream is = MyUrlRewriteFilter.class.getResourceAsStream("build.number.properties");
			if (is != null) {
				try {
					props.load(is);
					String buildNumber = (String) props.get("build.number");
					if (!StringUtils.isBlank(buildNumber)) {
						buildNumberStr = props.get("project.version") + " build " + props.get("build.number");
					}
				} finally {
					is.close();
				}
			}
		} catch (IOException e) {
			log.error(e);
		}
		return buildNumberStr;
	}
}
